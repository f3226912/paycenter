package cn.gdeng.paycenter.server.pay.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.MergePayTradeService;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.PayTradeUidRecordService;
import cn.gdeng.paycenter.api.server.pay.ValidprePayParamService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.PayTradeForce;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.entity.pay.MemberBaseinfoEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeUidRecordEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;

@Service
public class MergePayTradeServiceImpl implements MergePayTradeService{

	@Resource
	private BaseDao<PayTradeEntity> baseDao;
	
	@Resource
	private ValidprePayParamService validprePayParamService;
	
	@Resource
	private PayTradeUidRecordService payTradeUidRecordService;
	
	@Resource
	private PayTradeService payTradeService;
	
	@Resource
	private PayLogRecordService payLogRecordService;
	
	@Override
	@Transactional
	public String createPayTrade(MergePayTradeRequestDto dto) throws BizException {

		//校验是否已支付

		List<PayTradeEntity> tradeList = new ArrayList<>();
		BigDecimal big = new BigDecimal(0);
		for(MergePayTradeRequestDto.OrderInfo orderInfo : dto.getOrderInfoList()){
			PayTradeEntity payTrade = buildPayTradeEntity(dto,orderInfo);
			validprePayParamService.validReadPay(payTrade);
			big = big.add(new BigDecimal(orderInfo.getPayAmt()));
			tradeList.add(payTrade);
			
		}
		if(dto.getSumPayAmt() != null && dto.getSumPayAmt() != 0){
			if(dto.getSumPayAmt().doubleValue() != big.doubleValue()){
				throw new BizException(MsgCons.C_20001, "总金额["+dto.getSumPayAmt()+"]与各订单交易金额之和不一致");
			}
		}
		
		String payUid = dto.getPayUid();
		if(StringUtils.isEmpty(payUid)){
			payUid = PayTradeUtil.getPayUuid(dto);
		}
		//支付流水是否存在
		PayTradeUidRecordEntity record = payTradeUidRecordService.queryByPayUID(payUid);
		String payCenterNumber = null;
		if(null != record){
			payCenterNumber = record.getPayCenterNumber();
			//判断是否需要强制重新生成流水号
			if(StringUtils.equals(dto.getPayType(), PayWayEnum.WEIXIN_APP.getWay())
					|| PayTradeForce.RECREATE.equals(dto.getForce())){
				payTradeUidRecordService.deletePayTradeUidRecord(payCenterNumber);
				payCenterNumber = payTradeService.getPayCenterNumber();//重新生成流水号
			}
		} else {
			payCenterNumber = payTradeService.getPayCenterNumber();

		}

		//设置payCenterNumber
		for(PayTradeEntity entity : tradeList){
			entity.setPayCenterNumber(payCenterNumber);
		}
		//添加payTradeUidRecord
		PayTradeUidRecordEntity uidObj = new PayTradeUidRecordEntity();
		uidObj.setAppKey(dto.getAppKey());
		uidObj.setPayCenterNumber(payCenterNumber);
		uidObj.setPayUid(dto.getPayUid());
		List<String> orders = new ArrayList<>();
		for(MergePayTradeRequestDto.OrderInfo orderInfo : dto.getOrderInfoList()){
			orders.add(orderInfo.getOrderNo());
		}
		payTradeUidRecordService.addPayTradeUidRecordEntity(uidObj,orders);
		payTradeService.addPayTrade(tradeList);
		return payCenterNumber;
	}
	
	private PayTradeEntity buildPayTradeEntity(MergePayTradeRequestDto dto,
			MergePayTradeRequestDto.OrderInfo orderInfo){
		PayTradeEntity payTrade = new PayTradeEntity();
		
		BeanUtils.copyProperties(orderInfo, payTrade);
		payTrade.setAppKey(dto.getAppKey());
		payTrade.setVersion(dto.getVersion());
		payTrade.setPayType(dto.getPayType());
		payTrade.setTimeOut(dto.getTimeOut()+"");
		payTrade.setPayStatus(PayStatus.READY_PAY);
		payTrade.setNotifyStatus(NotifyStatus.NO_NOTIFY);
		payTrade.setReturnUrl(dto.getReturnUrl());
		//payTrade.setNotifyUrl("");
		payTrade.setRequestIp(dto.getRequestIp());
		//查询买家，卖家信息
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("memberId", payTrade.getPayerUserId());
		MemberBaseinfoEntity payer = baseDao.queryForObject("PayTrade.getMemberBaseinfo",
				paramMap, MemberBaseinfoEntity.class);
		payTrade.setPayerAccount(payer.getAccount());
		payTrade.setPayerMobile(payer.getMobile());
		payTrade.setPayerName(payer.getRealName());
		paramMap.clear();
		paramMap.put("memberId", payTrade.getPayeeUserId());
		MemberBaseinfoEntity payee = baseDao.queryForObject("PayTrade.getMemberBaseinfo",
				paramMap, MemberBaseinfoEntity.class);
		
		payTrade.setPayeeAccount(payee.getAccount());
		payTrade.setPayeeMobile(payee.getMobile());
		payTrade.setPayeeName(payee.getRealName());
		return payTrade;
	}

}
