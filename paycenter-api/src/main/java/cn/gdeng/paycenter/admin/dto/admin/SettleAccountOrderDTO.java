package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.Date;

import cn.gdeng.paycenter.enums.SettleAccountOrderTypeEnum;
import cn.gdeng.paycenter.enums.SettleAccountStatusEnum;

public class SettleAccountOrderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1613058452469202395L;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 代付款类型
	 */
	private Integer orderType;

	/**
	 * 下单时间
	 */
	private Date createOrderDate;
	/**
	 * 代付款金额
	 */
	private Double paidAmt;
	/**
	 * 代付款状态
	 */
	private Integer payStatus;
	
	/**
	 * 总数
	 */
	private Integer count;
	/**
	 * 批次号
	 */
	private String batNo;
	/**
	 * 会员ID
	 */
	private Integer memberId;
	
	private String feeType;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Date getCreateOrderDate() {
		return createOrderDate;
	}

	public void setCreateOrderDate(Date createOrderDate) {
		this.createOrderDate = createOrderDate;
	}

	public Double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	
	public String getPayStatusStr(){
		return SettleAccountStatusEnum.getNameByCode(getPayStatus().byteValue());
	}

	public String getOrderTypeStr() {
		if (getOrderType() == null) {
			return null;
		}
		return SettleAccountOrderTypeEnum.getNameByCode(getOrderType().byteValue());
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getBatNo() {
		return batNo;
	}

	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

}
