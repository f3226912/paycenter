package cn.gdeng.paycenter.admin.dto.admin;

import java.util.Date;

import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public class PayTradeDTO extends PayTradeEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6683052894017079630L;

	private Double chargeAmt;

	private Double transferAmt;

	private String transferStatus;

	private Date transferTime;

	private String realName;

	private String bankCardNo;

	private String depositBankName;

	private String provinceName;

	private String cityName;

	private String areaName;

	private String subBankName;

	private String userBankMobile;

	private String payerBankName;

	private Double transferFeeAmt;

	private String bankTradeNo;

	private String transferPayerName;

	private String operUserName;

	private String auditUserName;

	private String auditUserId;

	private String posClientNo;

	private String posVersion;

	private Integer payCount;

	// 物流公司ID
	private String logisUserId;

	/** 支付类型 */
	private String payType;

	public Double getChargeAmt() {
		return chargeAmt;
	}

	public void setChargeAmt(Double chargeAmt) {
		this.chargeAmt = chargeAmt;
	}

	public Double getTransferAmt() {
		return transferAmt;
	}

	public void setTransferAmt(Double transferAmt) {
		this.transferAmt = transferAmt;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public Date getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getDepositBankName() {
		return depositBankName;
	}

	public void setDepositBankName(String depositBankName) {
		this.depositBankName = depositBankName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getSubBankName() {
		return subBankName;
	}

	public void setSubBankName(String subBankName) {
		this.subBankName = subBankName;
	}

	public String getUserBankMobile() {
		return userBankMobile;
	}

	public void setUserBankMobile(String userBankMobile) {
		this.userBankMobile = userBankMobile;
	}

	public String getPayerBankName() {
		return payerBankName;
	}

	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}

	public String getBankTradeNo() {
		return bankTradeNo;
	}

	public void setBankTradeNo(String bankTradeNo) {
		this.bankTradeNo = bankTradeNo;
	}

	public Double getTransferFeeAmt() {
		return transferFeeAmt;
	}

	public void setTransferFeeAmt(Double transferFeeAmt) {
		this.transferFeeAmt = transferFeeAmt;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getTransferPayerName() {
		return transferPayerName;
	}

	public void setTransferPayerName(String transferPayerName) {
		this.transferPayerName = transferPayerName;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getOperUserName() {
		return operUserName;
	}

	public void setOperUserName(String operUserName) {
		this.operUserName = operUserName;
	}

	public String getPosClientNo() {
		return posClientNo;
	}

	public void setPosClientNo(String posClientNo) {
		this.posClientNo = posClientNo;
	}

	public String getPosVersion() {
		return posVersion;
	}

	public void setPosVersion(String posVersion) {
		this.posVersion = posVersion;
	}

	public Integer getPayCount() {
		return payCount;
	}

	public void setPayCount(Integer payCount) {
		this.payCount = payCount;
	}

	public String getLogisUserId() {
		return logisUserId;
	}

	public void setLogisUserId(String logisUserId) {
		this.logisUserId = logisUserId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
