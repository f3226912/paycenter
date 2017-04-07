package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;

/**
 * 代付款记录
 * 
 * @author dengjianfeng
 *
 */
public class PaidDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5084669603858512478L;

	private Long memberId;

	private String account;

	private String mobile;

	/** 代付订单笔数(货款笔数) */
	private Integer orderCount;

	/** 代付市场佣金笔数 */
	private Integer commissionCount;

	/** 代付违约金笔数 */
	private Integer penaltyCount;

	/** 代付退款笔数 */
	private Integer refundCount;

	private Double amt;

	private String amtStr;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getCommissionCount() {
		return commissionCount;
	}

	public void setCommissionCount(Integer commissionCount) {
		this.commissionCount = commissionCount;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public String getAmtStr() {
		return amtStr;
	}

	public void setAmtStr(String amtStr) {
		this.amtStr = amtStr;
	}

	public Integer getPenaltyCount() {
		return penaltyCount;
	}

	public void setPenaltyCount(Integer penaltyCount) {
		this.penaltyCount = penaltyCount;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

}
