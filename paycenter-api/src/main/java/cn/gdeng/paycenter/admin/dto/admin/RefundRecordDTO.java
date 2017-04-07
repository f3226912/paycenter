package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * 退款记录DTO
 * 
 * @author DavidLiang
 * @date 2016年12月12日 下午2:03:57
 */
public class RefundRecordDTO implements Serializable {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -4470139748265757016L;

	/** refund_record id */
	private Integer id;

	/** 退款单号 */
	private String refundNo;

	/** 关联订单号 */
	private String orderNo;

	/** 退款类型(人工，系统) */
	private String refundType;

	/** 申请退款时间 */
	private Date createTime;

	/** 退款金额(代付款金额) */
	private String refundAmt;

	/** 第三方退款单号(第三方支付流水) */
	private String thirdRefundNo;

	/** 代付状态(1支付成功 0待支付) */
	private String payStatus;

	/** 代付时间 */
	private Date payTime;

	/** 代付方式 */
	private String payBankAndNo;

	/** 收款方手机 */
	private String payeeMobile;

	/** 收款方 */
	private String userType;

	/** 会员id */
	private String memberId;

	/** 批次号 */
	private String batNo;

	/** 是否转结算(1是 0否) */
	private String hasChange;

	/** 代付方式 */
	private String payType;

	/** 退款时间(代付时间) */
	private String refundTime;
	
	/** 支付方式 */
	private String payWay;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}

	public String getThirdRefundNo() {
		return thirdRefundNo;
	}

	public void setThirdRefundNo(String thirdRefundNo) {
		this.thirdRefundNo = thirdRefundNo;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayBankAndNo() {
		return payBankAndNo;
	}

	public void setPayBankAndNo(String payBankAndNo) {
		this.payBankAndNo = payBankAndNo;
	}

	public String getPayeeMobile() {
		return payeeMobile;
	}

	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
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

	public String getBatNo() {
		return batNo;
	}

	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}

	public String getHasChange() {
		return hasChange;
	}

	public void setHasChange(String hasChange) {
		this.hasChange = hasChange;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

}
