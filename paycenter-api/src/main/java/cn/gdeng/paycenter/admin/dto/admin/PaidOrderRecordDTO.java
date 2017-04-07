package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * 代付订单记录DTO
 * 
 * @author xiaojun
 *
 */
public class PaidOrderRecordDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6091404235024293548L;
	/**
	 * 支付交易表主键
	 */
	private Integer id;

	/**
	 * 会员memberId
	 */
	private String memberId;
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
	 * 谷登代收手续费
	 */
	private String feeAmt;
	/**
	 * 佣金支出
	 */
	private String commissionAmt;

	/**
	 * 平台佣金
	 */
	private Double platCommissionAmt;
	/**
	 * 补贴
	 */
	private String subsidyAmt;
	/**
	 * 代付款金额
	 */
	private String paidAmt;
	/**
	 * 平台支付流水
	 */
	private String payCenterNumber;
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
	 * 代付方式
	 */
	private String payWay;
	/**
	 * 代付状态
	 */
	private String payStatus;
	/**
	 * 收款方手机
	 */
	private String payeeMobile;
	/**
	 * 收款方来源
	 */
	private String appKey;
	/**
	 * 订单类型
	 */
	private String orderType;
	/**
	 * 订单类型Str
	 */
	private String orderTypeStr;
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
	 * 是否转结算(1是 0否)
	 */
	private String hasChange;
	/**
	 * 批次号
	 */
	private String batNo;
	/**
	 * 用户结算记录表 主键id
	 */
	private Integer remitRecordId;

	private String userType;
	/**
	 * 用户类型
	 */
	private String userTypeStr;

	/** 市场佣金 */
	private String marketCommission;

	/** 平台佣金 */
	private String platformCommission;

	/** 开户行名称 */
	private String depositBankName;

	/** 银行卡号 */
	private String bankCardNo;

	private String payType;

	public String getOrderTypeStr() {
		return orderTypeStr;
	}

	public void setOrderTypeStr(String orderTypeStr) {
		this.orderTypeStr = orderTypeStr;
	}

	public String getUserTypeStr() {
		return userTypeStr;
	}

	public void setUserTypeStr(String userTypeStr) {
		this.userTypeStr = userTypeStr;
	}

	public Integer getRemitRecordId() {
		return remitRecordId;
	}

	public void setRemitRecordId(Integer remitRecordId) {
		this.remitRecordId = remitRecordId;
	}

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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getCommissionAmt() {
		return commissionAmt;
	}

	public void setCommissionAmt(String commissionAmt) {
		this.commissionAmt = commissionAmt;
	}

	public Double getPlatCommissionAmt() {
		return platCommissionAmt;
	}

	public void setPlatCommissionAmt(Double platCommissionAmt) {
		this.platCommissionAmt = platCommissionAmt;
	}

	public String getSubsidyAmt() {
		return subsidyAmt;
	}

	public void setSubsidyAmt(String subsidyAmt) {
		this.subsidyAmt = subsidyAmt;
	}

	public String getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(String paidAmt) {
		this.paidAmt = paidAmt;
	}

	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayStatus() {
		if (payStatus == null) {
			payStatus = "0";
		}
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayeeMobile() {
		return payeeMobile;
	}

	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMarketCommission() {
		return marketCommission;
	}

	public void setMarketCommission(String marketCommission) {
		this.marketCommission = marketCommission;
	}

	public String getPlatformCommission() {
		return platformCommission;
	}

	public void setPlatformCommission(String platformCommission) {
		this.platformCommission = platformCommission;
	}

	public String getDepositBankName() {
		return depositBankName;
	}

	public void setDepositBankName(String depositBankName) {
		this.depositBankName = depositBankName;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
