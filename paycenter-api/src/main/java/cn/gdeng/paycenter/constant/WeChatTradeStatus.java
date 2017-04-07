package cn.gdeng.paycenter.constant;

/**
 * 微信交易
 *
 */
public interface WeChatTradeStatus {

	String TRADE_SUCCESS = "SUCCESS";// 业务处理成功

	String TRADE_FAIL = "FAIL";// 业务处理失败

	String RETURN_SUCCESS = "SUCCESS";// 通信处理成功

	String RETURN_FAIL = "FAIL";// 通信处理失败
	
	String RETURN_MSG="OK";//返回信息

}
