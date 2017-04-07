package cn.gdeng.paycenter.dto.account;

import java.io.Serializable;


/*
 * 对账请求返回单条对账单对象
 */
public class WeChatAccountResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3667423098273716467L;
	
	private String tradeDate;//交易时间
	
	private String appId;//公众账号ID
	
	private String mchId;//商户号
	
	private String subMchId;//子商户号
	
	private String transactionId;//微信支付订单号
	
	private String outTradeNo;//商户订单号
	
	private String openId;//用户标识
	
	private String tradeType;//交易类型
	
	private String tradeStatus;//交易状态
	
	private String bankType;//付款银行
	
	private String feeType;//货币种类
	
	private String totalFee;//总金额
	
	private String refundApplyDate;//申请退款时间
	
	private String refundSuccessDate;//退款成功时间
	
	private String refundWechatNo;//微信退款单号
	
	private String refundMchNo;//商户退款单号
	
	private String refundType;//退款类型
	
	private String refundFee;//退款金额；
	
	private String fee;//手续费
	
	private String rate;//费率
	
	private String title;//商品名称
	
	private String info;//微信返回原始字符串

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getRefundApplyDate() {
		return refundApplyDate;
	}

	public void setRefundApplyDate(String refundApplyDate) {
		this.refundApplyDate = refundApplyDate;
	}

	public String getRefundSuccessDate() {
		return refundSuccessDate;
	}

	public void setRefundSuccessDate(String refundSuccessDate) {
		this.refundSuccessDate = refundSuccessDate;
	}

	public String getRefundWechatNo() {
		return refundWechatNo;
	}

	public void setRefundWechatNo(String refundWechatNo) {
		this.refundWechatNo = refundWechatNo;
	}

	public String getRefundMchNo() {
		return refundMchNo;
	}

	public void setRefundMchNo(String refundMchNo) {
		this.refundMchNo = refundMchNo;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
	

}
