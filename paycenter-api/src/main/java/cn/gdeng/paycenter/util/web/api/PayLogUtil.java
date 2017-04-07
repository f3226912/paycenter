package cn.gdeng.paycenter.util.web.api;

import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;

public class PayLogUtil {
	
	public static String PAY = "1";//支付
	public static String REFUND = "2";//退款

	
	public static PayLogRecordEntity buildAliPayReceiveLog(String orderNo,String msg,String payType){
		PayLogRecordEntity log = new PayLogRecordEntity();
		log.setOprType(PAY);
		log.setOrderNo(orderNo);
		log.setPayType(payType);
		log.setReceive(msg);
		log.setCreateUserId("sys");
		log.setUpdateUserId("sys");
		return log;
	}
	
	public static PayLogRecordEntity buildPayReceiveLog(String payType,String orderNo,String msg){

		return buildAliPayReceiveLog(orderNo,msg,payType);

	} 
}
