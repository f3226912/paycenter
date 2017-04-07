package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;

/**
 * 订单记录
 */
public class OrderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6845682446311292483L;

	//订单号
	private String orderNo;
	
	//实付金额
	private Double payAmt;

	public Double getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(Double payAmt) {
		this.payAmt = payAmt;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
