package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * 违约金记录DTO
 * 
 * @author DavidLiang
 * @date 2016年12月10日 下午3:25:34
 */
public class PenaltyRecordDTO implements Serializable {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 2024227667268364697L;

	/** clear_detail id */
	private Integer id;

	/** 关联订单号 */
	private String orderNo;

	/** 下单时间 */
	private Date orderTime;

	/** 预付款 */
	private String payAmt;

	/** 违约金额(代付款金额) */
	private String tradeAmt;

	/** 支付状态 */
	private String payStatus;

	/** 第三方流水号 */
	private String thirdPayNumber;

	/** 代付时间 */
	private Date payTime;

	/** 代付方式(银行 + 银行尾号) */
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

	/** 支付方式 */
	private String payType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

	public String getTradeAmt() {
		return tradeAmt;
	}

	public void setTradeAmt(String tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	public String getPayStatus() {
		if (payStatus == null) {
			payStatus = "1";
		}
		return payStatus;
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

}
