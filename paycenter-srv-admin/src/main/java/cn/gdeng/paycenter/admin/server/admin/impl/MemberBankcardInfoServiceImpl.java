package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.gudeng.framework.dba.util.DalUtils;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.service.admin.MemberBankcardInfoService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.MemberBankcardInfoDTO;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import cn.gdeng.paycenter.util.web.api.GdProperties;

@Service
public class MemberBankcardInfoServiceImpl implements MemberBankcardInfoService {
	
	@Autowired
	private BaseDao<?> baseDao;
	
	@Resource
	private SendMessageUtil sendMessageUtil;

	@Resource
	private GdProperties gdProperties;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<MemberBankcardInfoDTO> queryByCondition(Map<String, Object> params) {
		return baseDao.queryForList("MemberBankcardInfo.queryByCondition", params, MemberBankcardInfoDTO.class);
	}

	@Override
	public MemberBankcardInfoDTO queryById(Integer id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return baseDao.queryForObject("MemberBankcardInfo.queryById", params, MemberBankcardInfoDTO.class);
		
	}

	@Override
	public void update(MemberBankcardInfoDTO memberBankcardInfoDTO) {
		/*
		 * 发送MQ同步 同时修改数据
		 */
		//发送MQ
		MemberBankcardInfoDTO umemberBankcardInfoDTO = queryById(memberBankcardInfoDTO.getId());
		
		/*
		 * 发送MQ 同步验证状态 infoId
		 */
		MemberBankcardInfoDTO sendMemberBankInfo = new MemberBankcardInfoDTO();
		sendMemberBankInfo.setInfoId(umemberBankcardInfoDTO.getInfoId());
		sendMemberBankInfo.setAuditStatus(memberBankcardInfoDTO.getAuditStatus());
		
		sendMQ(sendMemberBankInfo);
		
		// 修改数据
		Map<String, Object> params = DalUtils.convertToMap(memberBankcardInfoDTO);

		baseDao.execute("MemberBankcardInfo.update", params);
		
	}

	@Override
	public void updateBatch(List<MemberBankcardInfoDTO> memberBankcardInfoDTOs) {
		if(!memberBankcardInfoDTOs.isEmpty()) {
			for (int i = 0; i < memberBankcardInfoDTOs.size(); i++) {
				this.update(memberBankcardInfoDTOs.get(i));
			}
		}
	}

	@Override
	public Integer queryCount(Map<String, Object> params) {
		return baseDao.queryForObject("MemberBankcardInfo.queryCount", params, Integer.class);
	}

	@Override
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params) {
		ApiResult<AdminPageDTO> apiResult = new ApiResult<>();
		
		AdminPageDTO adminPageDto = new AdminPageDTO();
		
		adminPageDto.setTotal(queryCount(params));
		
		if(adminPageDto.getTotal()!=0) {
			adminPageDto.setRows(queryByCondition(params));
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		} else {
			adminPageDto.setRows(new ArrayList<MemberBankcardInfoDTO>());
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		}
		
		return apiResult;

	}
	
	private void sendMQ(final MemberBankcardInfoDTO memberBankcardInfoDTO) {
		/*
		 * 判断是否存在此平台MQ
		 */
		if (memberBankcardInfoDTO != null) {

			/*
			 * 异步定时任务通知
			 * 
			 */
			sendMessageUtil.sendAsyncMessage(gdProperties.getProperties().getProperty("bankinfoProducerTopic"), "TAGS",
					memberBankcardInfoDTO, new SendCallback() {

						/*
						 * 成功则修改状态未已通知
						 */
						@Override
						public void onSuccess(SendResult sendResult) {
							logger.info("[用户：" + memberBankcardInfoDTO.getAccount() + "]发送MQ验证银行卡不通过成功！");
						}

						/*
						 * 失败，则写入日志 不修改状态
						 */
						@Override
						public void onException(OnExceptionContext context) {
							logger.error("[用户：" + memberBankcardInfoDTO.getAccount() + "]发送验证不通过银行卡信息失败：",
									context.getException().getMessage());
						}
					});

		}
	}

}
 