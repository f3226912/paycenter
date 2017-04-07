package cn.gdeng.paycenter.admin.server.admin.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.admin.dto.admin.PaidCountDTO;
import cn.gdeng.paycenter.admin.dto.admin.PaidDTO;
import cn.gdeng.paycenter.admin.service.admin.PaidService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.ClearDetailEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;

@Service
public class PaidServiceImpl implements PaidService{
	
	private Logger logger = LoggerFactory.getLogger(PaidServiceImpl.class);
	
	@Resource
	private BaseDao<?> baseDao;

	@Override
	public ApiResult<List<PaidDTO>> queryPage(Map<String, Object> map) throws Exception {
		List<PaidDTO> list =  baseDao.queryForList("Paid.queryPage", map, PaidDTO.class);
		
		if (CollectionUtils.isNotEmpty(list)) {
			DecimalFormat df=new DecimalFormat("0.00");
			for(PaidDTO dto : list) {
				Double amt = dto.getAmt();
				if (amt != null) {
					// 设置代付款金额 保留2位小数
					dto.setAmtStr(df.format(amt));
				}
			}
		}
		
		
		ApiResult<List<PaidDTO>> apiResult = new ApiResult<List<PaidDTO>>();
		apiResult.setResult(list);
		
		// 数据验签
		List<Long> memberIds = new ArrayList<Long>();
		for(PaidDTO dto : list) {
			memberIds.add(dto.getMemberId());
		}
		List<Long> clearDetailIdList = queryClearDetailIdsByMemberId(memberIds);
		List<Long> errorIds = baseDao.batchValidateSign(clearDetailIdList, ClearDetailEntity.class);
		if(CollectionUtils.isNotEmpty(errorIds)) {
			apiResult.setCodeMsg(MsgCons.C_60003,  MsgCons.M_60003);
			logger.error("数据验证异常========>表名：clear_detail；数据id：" + errorIds);
		}
		return apiResult;
	}

	@Override
	public int countByCondition(Map<String, Object> map) {
		Integer total = baseDao.queryForObject("Paid.countByCondition", map, Integer.class);
		return total == null ? 0 : total;
	}

	@Override
	public ApiResult<List<PaidDTO>> queryList(Map<String, Object> map) throws Exception {
		List<PaidDTO> list = baseDao.queryForList("Paid.queryList", map, PaidDTO.class);
		
		if (CollectionUtils.isNotEmpty(list)) {
			DecimalFormat df=new DecimalFormat("0.00");
			for(PaidDTO dto : list) {
				Double amt = dto.getAmt();
				if (amt != null) {
					// 设置代付款金额 保留2位小数
					dto.setAmtStr(df.format(amt));
				}
			}
		}
		
		ApiResult<List<PaidDTO>> apiResult = new ApiResult<List<PaidDTO>>();
		apiResult.setResult(list);
		
//		// 数据验签
//		List<Long> memberIds = new ArrayList<Long>();
//		for(PaidDTO dto : list) {
//			memberIds.add(dto.getMemberId());
//		}
//		List<Long> clearDetailIdList = queryClearDetailIdsByMemberId(memberIds);
//		List<Long> errorIds = baseDao.batchValidateSign(clearDetailIdList, null);
//		if(CollectionUtils.isNotEmpty(errorIds)) {
//			apiResult.setCodeMsg(MsgCons.C_60003,  MsgCons.M_60003);
//		}
		return apiResult;
	}
	
	@Override
	@Transactional
	public void insertSettlement(String settleType, String settleDate, String operId,String account,String mobile,String startAmt,String endAmt,String greaterZero) {
		logger.info("转结算参数：" + settleType + " " + settleDate + " " + operId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		paramMap.put("account", account);
		paramMap.put("mobile", mobile);
		paramMap.put("startAmt", startAmt);
		paramMap.put("endAmt", endAmt);
		paramMap.put("greaterZero", greaterZero);
		//查询符合转结算条件的用户
		List<PaidDTO> list =  baseDao.queryForList("Paid.queryPage", paramMap, PaidDTO.class);
		List<Object> memberIds = new ArrayList<Object>();
		for(PaidDTO paidDTO : list){
			memberIds.add(paidDTO.getMemberId());
		}
		String batNoPrefix = DateFormatUtils.format(new Date(), "yyyyMMdd");
		//当天最大批次号+1
		paramMap.clear();
		paramMap.put("batNoPrefix", batNoPrefix);
		Integer batNoSuffix = baseDao.queryForObject("Paid.getBatNo", paramMap, Integer.class);
		String batNo = batNoPrefix;
		if(batNoSuffix == null){
			batNo = batNo + "-1";
		} else {
			batNo = batNo + "-" + batNoSuffix;
		}
		logger.info("批次号：" + batNo);
		//清除传参
		paramMap.clear();
		paramMap.put("batNo", batNo);
		paramMap.put("payTime", settleDate);
		paramMap.put("createUserId", operId);
		paramMap.put("updateUserId", operId);
		paramMap.put("memberIds", memberIds);
		map.put("batNo", batNo);
		for(String type : settleType.split(",")){
			//写入结算表
			//全选或者是选择了订单
			if("0".equals(type) || "1".equals(type)) {
				//更新清算表批次号
				int count = baseDao.execute("Paid.updateOrderClearDetail", paramMap);
				if(count > 0) {
					//查询满足转结算的订单笔数和金额
					List<PaidCountDTO> orderCountDto = baseDao.queryForList("Paid.countOrd", paramMap,PaidCountDTO.class);
					for(PaidCountDTO orderDto : orderCountDto){
						paramMap.put("memberId", orderDto.getMemberId());
						paramMap.put("orderNum", orderDto.getOrderCount());
						paramMap.put("commissionNum", 0d);
						paramMap.put("refundNum", 0d);
						paramMap.put("penaltyNum", 0d);
						paramMap.put("dueAmt", orderDto.getOrderAmt());
						baseDao.execute("Paid.insertRemit", paramMap);
					}
				}
			}
			//全选或者是选择了市场佣金
			if("0".equals(type) || "2".equals(type)) {
				//更新清算表批次号
				int count = baseDao.execute("Paid.updateCommissionClearDetail", paramMap);
				if(count > 0) {
					//查询满足转结算的市场佣金/退款/违约金笔数和金额
					List<PaidCountDTO> otherCountDto = baseDao.queryForList("Paid.countother", paramMap,PaidCountDTO.class);
					for(PaidCountDTO otherDto : otherCountDto){
						map.put("memberId", otherDto.getMemberId());
						PaidCountDTO dto = baseDao.queryForObject("Paid.queryByBatNoMemId", map, PaidCountDTO.class);
						if(dto != null){
							paramMap.put("dueAmt", addDouble(dto.getDueAmt(),otherDto.getCommissionAmt()));
							paramMap.put("commissionNum", otherDto.getCommissionCount() + dto.getCommissionCount());
							baseDao.execute("Paid.updateRemit", paramMap);
						}else{
							paramMap.put("memberId", otherDto.getMemberId());
							paramMap.put("orderNum", 0d);
							paramMap.put("commissionNum", otherDto.getCommissionCount());
							paramMap.put("refundNum", 0d);
							paramMap.put("penaltyNum", 0d);
							paramMap.put("dueAmt", otherDto.getCommissionAmt());
							baseDao.execute("Paid.insertRemit", paramMap);
						}
					}
				}
			}
			//全选或者是选择了退款
			if("0".equals(type) || "3".equals(type)) {
				//更新清算表批次号
				int count = baseDao.execute("Paid.updateBackClearDetail", paramMap);
				if(count > 0) {
					//查询满足转结算的市场佣金/退款/违约金笔数和金额
					List<PaidCountDTO> otherCountDto = baseDao.queryForList("Paid.countother", paramMap,PaidCountDTO.class);
					for(PaidCountDTO otherDto : otherCountDto){
						map.put("memberId", otherDto.getMemberId());
						PaidCountDTO dto = baseDao.queryForObject("Paid.queryByBatNoMemId", map, PaidCountDTO.class);
						if(dto != null){
							paramMap.put("dueAmt", addDouble(dto.getDueAmt(),otherDto.getRefundAmt()));
							paramMap.put("refundNum", otherDto.getRefundCount() + dto.getRefundCount());
							baseDao.execute("Paid.updateRemit", paramMap);
						}else{
							paramMap.put("memberId", otherDto.getMemberId());
							paramMap.put("orderNum", 0d);
							paramMap.put("commissionNum", 0d);
							paramMap.put("refundNum", otherDto.getRefundCount());
							paramMap.put("penaltyNum", 0d);
							paramMap.put("dueAmt", otherDto.getRefundAmt());
							baseDao.execute("Paid.insertRemit", paramMap);
						}
					}
				}
			}
			//全选或者是选择了违约金(卖家违约金，物流违约金)
			if("0".equals(type) || "4".equals(type)) {
				//更新清算表批次号-违约金
				int count = baseDao.execute("Paid.updateWyjClearDetail", paramMap);
				if(count > 0) {
					//查询满足转结算的市场佣金/退款/违约金笔数和金额
					List<PaidCountDTO> otherCountDto = baseDao.queryForList("Paid.countother", paramMap,PaidCountDTO.class);
					for(PaidCountDTO otherDto : otherCountDto){
						map.put("memberId", otherDto.getMemberId());
						PaidCountDTO dto = baseDao.queryForObject("Paid.queryByBatNoMemId", map, PaidCountDTO.class);
						if(dto != null){
							paramMap.put("dueAmt", addDouble(dto.getDueAmt(),otherDto.getPenaltyAmt()));
							paramMap.put("penaltyNum", otherDto.getPenaltyCount() + dto.getPenaltyCount());
							baseDao.execute("Paid.updateRemit", paramMap);
						}else{
							paramMap.put("memberId", otherDto.getMemberId());
							paramMap.put("orderNum", 0d);
							paramMap.put("commissionNum", 0d);
							paramMap.put("refundNum", 0d);
							paramMap.put("penaltyNum", otherDto.getPenaltyCount());
							paramMap.put("dueAmt", otherDto.getPenaltyAmt());
							baseDao.execute("Paid.insertRemit", paramMap);
						}
					}
				}
			}
		}
		logger.info("转结算成功");
	}

	@Override
	public List<Long> queryClearDetailIdsByMemberId(List<Long> memberIds) {
		if(CollectionUtils.isEmpty(memberIds)){
			return null;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("memberIds", memberIds);
		return baseDao.queryForList("Paid.queryClearDetailIdsByMemberIds", param, Long.class);
	}

	public static Double addDouble(Double v1, Double v2) {
		if(v1 == null){
			v1 = 0d;
		}
		if(v2 == null){
			v2 = 0d;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
    }
}
