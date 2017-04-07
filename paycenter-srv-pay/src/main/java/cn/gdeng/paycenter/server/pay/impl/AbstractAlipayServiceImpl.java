package cn.gdeng.paycenter.server.pay.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;

public abstract class AbstractAlipayServiceImpl implements AlipayService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	
	public PayTradeEntity buildPayTrade4Notify(Map<String,String> params) throws BizException{
		PayTradeEntity newPay = new PayTradeEntity();
		String payCenterNumber = params.get("out_trade_no");
		//String tradeStatus = params.get("trade_status");
		String tradeNo = params.get("trade_no");
		String buyerEmail = params.get("buyer_email");
		//String buyerId = params.get("buyer_id");
		
		String sellerEmail = params.get("seller_email");
		//String sellerId = params.get("seller_id");
		
		//交易付款时间
		String gmt_payment = params.get("gmt_payment");

		newPay.setPayCenterNumber(payCenterNumber);
		newPay.setPayStatus(PayStatus.PAID);
		newPay.setThirdPayNumber(tradeNo);
		newPay.setThirdPayerAccount(buyerEmail);
		newPay.setThirdPayeeAccount(sellerEmail);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			newPay.setPayTime(sdf.parse(gmt_payment));
		} catch (ParseException e) {
			throw new BizException(MsgCons.C_20001, MsgCons.M_20001+","+e.getMessage());
		}
		return newPay;
	}
}
