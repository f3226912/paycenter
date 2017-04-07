package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * 代付佣金记录DTO
 * 
 * @author xiaojun
 *
 */
public class PaidCommissionRecordDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2519013425231308538L;

	/**
	 * 支付交易表主键
	 */
	private Integer id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 下单时间
	 */
	private Date orderTime;

	/**
	 * 查询下单开始时间
	 */
	private String startOrderTime;
	/**
	 * 查询下单结束时间
	 */
	private String endOrderTime;

	/**
	 * 订单金额
	 */
	private String totalAmt;
	/**
	 * 支付佣金
	 */
	private String commissionAmt;

	/**
	 * 代付款金额
	 */
	private String paidAmt;

	/**
	 * 支付状态
	 */
	private String payStatus;

	/**
	 * 第三方支付流水
	 */
	private String thirdPayNumber;

	/**
	 * 代付时间
	 */
	private Date payTime;

	/**
	 * 查询开始代付时间
	 */
	private String startPayTime;
	/**
	 * 查询结束代付时间
	 */
	private String endPayTime;

	/**
	 * 收款方手机(市场)
	 */
	private String mobile;
	/**
	 * 市场名称
	 */
	private String marketName;

	/**
	 * 收款来源
	 */
	private String userType;
	/**
	 * 跳转判断
	 */
	private String redirect;
	/**
	 * 跳转查询状态
	 */
	private String redirectPayStatus;
	/**
	 * 跳转查询电话
	 */
	private String redirectMobile;
	/**
	 * 市场memberId
	 */
	private String memberId;
	/**
	 * 是否转结算(1是 0否)
	 */
	private String hasChange;
	/**
	 * 批次号
	 */
	private String batNo;

	/**
	 * 代付方式
	 */
	private String payType;

	public String getBatNo() {
		return batNo;
	}

	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}

	public String getHasChange() {
		if (hasChange == null) {
			hasChange = "0";
		}
		return hasChange;
	}

	public void setHasChange(String hasChange) {
		this.hasChange = hasChange;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getRedirectPayStatus() {
		return redirectPayStatus;
	}

	public void setRedirectPayStatus(String redirectPayStatus) {
		this.redirectPayStatus = redirectPayStatus;
	}

	public String getRedirectMobile() {
		return redirectMobile;
	}

	public void setRedirectMobile(String redirectMobile) {
		this.redirectMobile = redirectMobile;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayStatus() {
		if (payStatus == null) {
			payStatus = "0";
		}
		return payStatus;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getCommissionAmt() {
		return commissionAmt;
	}

	public void setCommissionAmt(String commissionAmt) {
		this.commissionAmt = commissionAmt;
	}

	public String getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(String paidAmt) {
		this.paidAmt = paidAmt;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartPayTime() {
		return startPayTime;
	}

	public void setStartPayTime(String startPayTime) {
		this.startPayTime = startPayTime;
	}

	public String getEndPayTime() {
		return endPayTime;
	}

	public void setEndPayTime(String endPayTime) {
		this.endPayTime = endPayTime;
	}

	public String getStartOrderTime() {
		return startOrderTime;
	}

	public void setStartOrderTime(String startOrderTime) {
		this.startOrderTime = startOrderTime;
	}

	public String getEndOrderTime() {
		return endOrderTime;
	}

	public void setEndOrderTime(String endOrderTime) {
		this.endOrderTime = endOrderTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
