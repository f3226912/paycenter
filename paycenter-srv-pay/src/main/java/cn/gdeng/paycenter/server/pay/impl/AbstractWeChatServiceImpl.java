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
import cn.gdeng.paycenter.api.server.pay.WeChatService;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;

public abstract class AbstractWeChatServiceImpl implements WeChatService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	
	public PayTradeEntity buildPayTrade4Notify(Map<String,String> params) throws BizException{
		PayTradeEntity newPay = new PayTradeEntity();
		String payCenterNumber = params.get("out_trade_no");//商户订单号
		//String tradeStatus = params.get("result_code");//业务结果
		String tradeNo = params.get("transaction_id");//微信支付订单号
        String bankType=params.get("bank_type");//付款银行
        String openId=params.get("openid");//用户在商户appid下的唯一标识
		//支付完成时间
		String timeEnd = params.get("time_end");

		newPay.setPayCenterNumber(payCenterNumber);
		newPay.setPayStatus(PayStatus.PAID);
		newPay.setThirdPayNumber(tradeNo);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			newPay.setPayTime(sdf.parse(timeEnd));
		} catch (ParseException e) {
			throw new BizException(MsgCons.C_20001, MsgCons.M_20001+","+e.getMessage());
		}
		return newPay;
	}
}
