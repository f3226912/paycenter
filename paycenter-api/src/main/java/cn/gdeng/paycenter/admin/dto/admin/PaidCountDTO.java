package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;

/**
 * 代付款记录
 * 
 * @author dengjianfeng
 *
 */
public class PaidCountDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5084669603858512478L;

	private String memberId;
	// 可转结算订单笔数
	private int orderCount;
	// 可转结算订单金额
	private Double orderAmt;
	// 可转结算退款笔数
	private int refundCount;
	// 可转结算退款金额
	private Double refundAmt;
	// 可转结算市场佣金笔数
	private int commissionCount;
	// 可转结算市场佣金金额
	private Double commissionAmt;
	// 可转结算违约金笔数
	private int penaltyCount;
	// 可转结算违约金金额
	private Double penaltyAmt;
	
	private Double dueAmt;
	
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public Double getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(Double orderAmt) {
		this.orderAmt = orderAmt;
	}
	public int getRefundCount() {
		return refundCount;
	}
	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}
	public Double getRefundAmt() {
		return refundAmt;
	}
	public void setRefundAmt(Double refundAmt) {
		this.refundAmt = refundAmt;
	}
	public int getCommissionCount() {
		return commissionCount;
	}
	public void setCommissionCount(int commissionCount) {
		this.commissionCount = commissionCount;
	}
	public Double getCommissionAmt() {
		return commissionAmt;
	}
	public void setCommissionAmt(Double commissionAmt) {
		this.commissionAmt = commissionAmt;
	}
	public int getPenaltyCount() {
		return penaltyCount;
	}
	public void setPenaltyCount(int penaltyCount) {
		this.penaltyCount = penaltyCount;
	}
	public Double getPenaltyAmt() {
		return penaltyAmt;
	}
	public void setPenaltyAmt(Double penaltyAmt) {
		this.penaltyAmt = penaltyAmt;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Double getDueAmt() {
		return dueAmt;
	}
	public void setDueAmt(Double dueAmt) {
		this.dueAmt = dueAmt;
	}

}
