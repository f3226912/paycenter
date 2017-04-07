package cn.gdeng.paycenter.dto.pos;

import java.io.Serializable;
import java.util.Date;

public class OrderBillHessianDto implements Serializable{

	private static final long serialVersionUID = -5211030203567759960L;
	// 商户号
	private String businessNo;
	// 商户名
	private String businessName;
	// 交易类型
	private String tradeType;
	// 交易时间
	private Date tradeDay;
	// 交易卡号
	private String cardNo;
	// 终端号
	private String clientNo;
	// 交易金额
	private Double tradeMoney;
	// 系统参考号
	private String sysRefeNo;
	// 手续费
	private Double fee;
	// 使用状态 1已使用 2未使用
	private String useStatus;

	private Long marketId;
	
	//银行卡类别
	private String cardType;
	
	private String payChannelCode;
	private String payCenterNumber;
	/**
	 * 开始时间
	 */
	private Date billBeginTime;
	/**
	 * 结束时间
	 */
	private Date billEndTime;
	
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public Date getTradeDay() {
		return tradeDay;
	}
	public void setTradeDay(Date tradeDay) {
		this.tradeDay = tradeDay;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public Double getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(Double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	public String getSysRefeNo() {
		return sysRefeNo;
	}
	public void setSysRefeNo(String sysRefeNo) {
		this.sysRefeNo = sysRefeNo;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	public Long getMarketId() {
		return marketId;
	}
	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Date getBillBeginTime() {
		return billBeginTime;
	}
	public void setBillBeginTime(Date billBeginTime) {
		this.billBeginTime = billBeginTime;
	}
	public Date getBillEndTime() {
		return billEndTime;
	}
	public void setBillEndTime(Date billEndTime) {
		this.billEndTime = billEndTime;
	}
	public String getPayChannelCode() {
		return payChannelCode;
	}
	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}
	public String getPayCenterNumber() {
		return payCenterNumber;
	}
	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

}
