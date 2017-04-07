package cn.gdeng.paycenter.api.server.order;

import java.io.Serializable;
import java.util.Date;

/**
 * @purpose 订单预付款和尾款对象
 * @date 2016年12月16日
 */
public class OrderAdvanceAndPaymentDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -247672643923650229L;

	private String orderNo;
	private Double payAmt;
	private String type;
	private String updateUserId;
	private String payCenterNumber;
	private String thirdPayerAccount;
	private String thirdPayeeAccount;
	private Date payTime;
	private String thirdPayNumber;
	private String payerUserId;
	private String payType;
	private String payBank;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Double getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(Double payAmt) {
		this.payAmt = payAmt;
	}
	public String getPayCenterNumber() {
		return payCenterNumber;
	}
	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}
	public String getThirdPayerAccount() {
		return thirdPayerAccount;
	}
	public void setThirdPayerAccount(String thirdPayerAccount) {
		this.thirdPayerAccount = thirdPayerAccount;
	}
	public String getThirdPayeeAccount() {
		return thirdPayeeAccount;
	}
	public void setThirdPayeeAccount(String thirdPayeeAccount) {
		this.thirdPayeeAccount = thirdPayeeAccount;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayBank() {
		return payBank;
	}
	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}
	public String getThirdPayNumber() {
		return thirdPayNumber;
	}
	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}
	public String getPayerUserId() {
		return payerUserId;
	}
	public void setPayerUserId(String payerUserId) {
		this.payerUserId = payerUserId;
	}
	public String toString() {
		return "orderNo=" + orderNo + ",payAmt=" + payAmt + ",type=" + type
				+ ",updateUserId=" + updateUserId + ",payCenterNumber=" + payCenterNumber 
				+ ",thirdPayerAccount=" + thirdPayerAccount + ",thirdPayeeAccount=" + thirdPayeeAccount
				+ ",payTime=" + payTime + ",thirdPayNumber=" + thirdPayNumber + ",payerUserId=" + payerUserId 
				+ ",payType=" + payType + ",payBank=" + payBank;
	}
}
