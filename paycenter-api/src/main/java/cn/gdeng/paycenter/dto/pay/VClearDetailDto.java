package cn.gdeng.paycenter.dto.pay;

import java.io.Serializable;

public class VClearDetailDto implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String orderNo;
	
	private String userType;
	
	private String memberId;
	
	private String orderAmt;	 //交易金额
	
	private String commissionAmt;//市场佣金 
	
	private String feeAmt;		 //手续费
	
	private String subsidyAmt;	 //补贴
	
	private String platCommissionAmt;	//平台佣金
	
	private String penaltyAmt;		//违约金
	
	private String refundAmt;		//退款

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}

	public String getCommissionAmt() {
		return commissionAmt;
	}

	public void setCommissionAmt(String commissionAmt) {
		this.commissionAmt = commissionAmt;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getSubsidyAmt() {
		return subsidyAmt;
	}

	public void setSubsidyAmt(String subsidyAmt) {
		this.subsidyAmt = subsidyAmt;
	}

	public String getPlatCommissionAmt() {
		return platCommissionAmt;
	}

	public void setPlatCommissionAmt(String platCommissionAmt) {
		this.platCommissionAmt = platCommissionAmt;
	}

	public String getPenaltyAmt() {
		return penaltyAmt;
	}

	public void setPenaltyAmt(String penaltyAmt) {
		this.penaltyAmt = penaltyAmt;
	}

	public String getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}

	

}
