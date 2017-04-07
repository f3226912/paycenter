package cn.gdeng.paycenter.server.pay.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;

@Service
public class PayTypeServiceImpl implements PayTypeService {
	@Resource
	private BaseDao<PayLogRecordEntity> baseDao;
	
	@Override
	public List<PayTypeDto> queryAll() {
		return baseDao.queryForList("PayType.queryByCondition", null, PayTypeDto.class);
	}
	
	/**
	 * 根据条件查询总记录数
	* @author DavidLiang
	* @date 2016年11月8日 下午1:59:02
	* @param paramMap
	* @return
	* @throws BizException
	 */
	@Override
	public int selCountByCondition(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForObject("PayType.countByCondition", paramMap, Integer.class);
	}
	
	/**
	 * 根据条件分页查询支付渠道
	* @author DavidLiang
	* @date 2016年11月8日 下午1:59:16
	* @param paramMap
	* @return
	* @throws BizException
	 */
	@Override
	public List<PayTypeEntity> selPageByCondition(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForList("PayType.queryByConditionPage", paramMap, PayTypeEntity.class);
	}
	
	/**
	 * 根据条件分页查询支付渠道
	* @author DavidLiang
	* @date 2016年11月8日 下午2:08:46
	* @param params
	* @return
	* @throws BizException
	 */
	@Override
	public ApiResult<AdminPageDTO> findPageByCondition(Map<String, Object> paramMap) throws BizException {
		ApiResult<AdminPageDTO> apiResult = new ApiResult<>();
		AdminPageDTO adminPageDto = new AdminPageDTO();
		int count = selCountByCondition(paramMap);
		adminPageDto.setTotal(count);
		if (count > 0) {
			List<PayTypeEntity> payTypeList = selPageByCondition(paramMap);
			// 先验证签名
			if (validatorSign(payTypeList)) {
				apiResult.setCode(MsgCons.C_10000);
				adminPageDto.setRows(payTypeList);
				apiResult.setIsSuccess(true);
			} else {
				apiResult.setCode(MsgCons.C_60003);
				apiResult.setMsg(MsgCons.M_60003);
				adminPageDto.setRows(new ArrayList<PayTypeEntity>());
				apiResult.setIsSuccess(false);
			}
		} else {
			apiResult.setCode(MsgCons.C_60004);
			adminPageDto.setRows(new ArrayList<PayTypeEntity>());
			apiResult.setIsSuccess(true);
		}
		apiResult.setResult(adminPageDto);
		return apiResult;
	}
	
	/**
	 * 验证签名
	* @author DavidLiang
	* @date 2016年11月22日 下午2:26:46
	* @param payTypeList
	* @return false : 验证失败，true : 验证成功
	* @throws Exception
	 */
	private boolean validatorSign(List<PayTypeEntity> payTypeList) {
		List<Long> idList = new ArrayList<>();
		for (PayTypeEntity pc : payTypeList) {
			idList.add(Long.valueOf(pc.getId()));	 
		}
		if (idList != null && ! idList.isEmpty()) {
			List<Long> errorIdList = new ArrayList<>();
			try {
				errorIdList = baseDao.batchValidateSign(idList, PayTypeEntity.class);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			if (! errorIdList.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<PayTypeEntity> queryByCondition(Map<String, Object> paramMap) throws BizException {
		List<PayTypeEntity> list = baseDao.queryForList("PayType.queryByCondition", paramMap, PayTypeEntity.class);
		return list;
	}

}
