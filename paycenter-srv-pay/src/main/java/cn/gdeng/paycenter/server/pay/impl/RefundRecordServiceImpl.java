package cn.gdeng.paycenter.server.pay.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.RefundRecordService;
import cn.gdeng.paycenter.constant.Refund;
import cn.gdeng.paycenter.constant.Refund.RefundStatus;
import cn.gdeng.paycenter.constant.Refund.RefundType;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.refund.RefundRecordDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeDto;
import cn.gdeng.paycenter.entity.pay.RefundFeeItemDetailEntity;
import cn.gdeng.paycenter.entity.pay.RefundRecordEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.Assert;
import cn.gdeng.paycenter.util.web.api.RandomIdGenerator;

@Service
public class RefundRecordServiceImpl implements RefundRecordService{
	
	@Resource 
	private BaseDao<RefundRecordEntity> baseDao;
	
	@Override
	public String getRefundNo() throws BizException {
		Long number = baseDao.queryForObject("RefundRecord.queryRefundNo", null, Long.class);
		//

		String id = RandomIdGenerator.randomId(number);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return "T"+sdf.format(new Date())+id;
	}

	@Override
	public List<RefundRecordEntity> getList(Map<String, Object> params) throws BizException {
		return baseDao.queryForList("RefundRecord.queryByCondition", params, RefundRecordEntity.class);

	}

	@Override
	public void update(RefundRecordEntity dto) throws BizException {
		Assert.notEmpty(dto.getRefundNo(), "refundNo不能为空");
		Map<String,Object> params = new HashMap<>();
		params.put("refundNo", dto.getRefundNo());
		List<RefundRecordEntity> rlist = getList(params);
		if(rlist == null || rlist.size() == 0){
			throw new BizException(MsgCons.C_20001, "退款单号["+dto.getRefundNo()+"]不存在");
		}
		dto.setId(rlist.get(0).getId());
		baseDao.dynamicMerge(dto);
	}

	@Override
	@Transactional
	public void add(RefundTradeDto dto) throws BizException {
		String refundNo = "";
		RefundRecordDto refund = buildRefund(dto);
		RefundRecordEntity entity = new RefundRecordEntity();
		BeanUtils.copyProperties(refund, entity);
		if(StringUtils.isNotEmpty(dto.getRefundNo())){
			refundNo = dto.getRefundNo();
			Map<String,Object> params = new HashMap<>();
			params.put("refundNo", refundNo);
			List<RefundRecordEntity> rlist = getList(params);
			if(rlist == null || rlist.size() == 0){
				throw new BizException(MsgCons.C_20001, "退款单号["+refundNo+"]不存在");
			}
			entity.setId(rlist.get(0).getId());
			baseDao.merge(entity);
			//删除feeItem
			baseDao.execute("RefundRecord.deleteRefundFeeItem", params);
		} else {
			refundNo = getRefundNo();
			dto.setRefundNo(refundNo);
			entity.setRefundNo(refundNo);
			baseDao.persist(entity, Long.class);
		}

		List<RefundFeeItemDetailEntity> list = refund.getFeeList();
		if(list != null && list.size() > 0){
			for(RefundFeeItemDetailEntity fee : list){
				fee.setRefundNo(refundNo);
				baseDao.persist(fee, Long.class);
			}
		}
	}
	
	private RefundRecordDto buildRefund(RefundTradeDto dto) throws BizException{
		
		RefundRecordDto refund = new RefundRecordDto();
		//添加refundRecordEntity
		RefundRecordEntity entity = new RefundRecordEntity();
		entity.setAppKey(dto.getAppKey());
		entity.setOrderNo(dto.getOrderNo());
		entity.setPayCenterNumber(dto.getPayCenterNumber());
		entity.setRefundAmt(Double.valueOf(dto.getRefundAmt()));
		entity.setRefundNo(dto.getRefundNo());
		entity.setRefundReason(dto.getRefundReason());
		entity.setRefundRequestNo(dto.getRefundRequestNo());
		entity.setRefundType(RefundType.SOURCE_RETURN);
		entity.setRefundUserId(dto.getRefundUserId());
		entity.setStatus(RefundStatus.READ_REFUND);
		//entity.setThirdRefundNo(dto.getThirdRefundNo());
		entity.setThirdRefundRequestNo(dto.getThirdRefundRequestNo());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setCreateUserId("sys");
		entity.setUpdateUserId("sys");
		
		//添加refundFeeItemDetail
		List<RefundFeeItemDetailEntity> list = new ArrayList<>();
		if(StringUtils.isNotEmpty(dto.getSellerRefundAmt())){
			RefundFeeItemDetailEntity d1 = new RefundFeeItemDetailEntity();
			d1.setClearState(Refund.ClearStatus.READ_CLEAR);
			d1.setFeeType(Refund.FeeType.SELLER);
			d1.setFeeAmt(Double.valueOf(dto.getSellerRefundAmt()));
			//d1.setRefundNo(refundNo);
			d1.setCreateTime(new Date());
			list.add(d1);
		}
		if(StringUtils.isNotEmpty(dto.getPlatRefundAmt())){
			RefundFeeItemDetailEntity d2 = new RefundFeeItemDetailEntity();
			d2.setClearState(Refund.ClearStatus.READ_CLEAR);
			d2.setFeeType(Refund.FeeType.PLATFORM);
			d2.setFeeAmt(Double.valueOf(dto.getPlatRefundAmt()));
			//d2.setRefundNo(refundNo);
			d2.setCreateTime(new Date());
			list.add(d2);
		}
		
		if(StringUtils.isNotEmpty(dto.getLogisRefundAmt())){
			RefundFeeItemDetailEntity d3 = new RefundFeeItemDetailEntity();
			d3.setClearState(Refund.ClearStatus.READ_CLEAR);
			d3.setFeeType(Refund.FeeType.LOGISTICS);
			d3.setFeeAmt(Double.valueOf(dto.getLogisRefundAmt()));
			//d3.setRefundNo(refundNo);
			d3.setCreateTime(new Date());
			list.add(d3);
		}
		BeanUtils.copyProperties(entity, refund);
		refund.setFeeList(list);
		return refund;
		
	}

	@Override
	public RefundRecordDto getRefundRecord(RefundTradeDto dto) throws BizException {
		Map<String,Object> params = new HashMap<>();
		params.put("appKey", dto.getAppKey());
		params.put("orderNo", dto.getOrderNo());
		params.put("refundRequestNo", dto.getRefundRequestNo());
		List<RefundRecordEntity> rlist = getList(params);
		if(rlist == null || rlist.size() == 0){
			throw new BizException(MsgCons.C_20001, "退款记录不存在");
		}
		RefundRecordEntity entity = rlist.get(0);
		params.clear();
		params.put("refundNo", entity.getRefundNo());
		List<RefundFeeItemDetailEntity> dlist = baseDao.queryForList("RefundFeeItemDetail.queryByCondition", params, RefundFeeItemDetailEntity.class);
		RefundRecordDto res = new RefundRecordDto();
		BeanUtils.copyProperties(entity, res);
		res.setFeeList(dlist);
		return res;
	}

}
