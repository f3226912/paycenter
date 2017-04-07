package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;

public class GdOrderInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4661776264253102993L;
	
    private String orderFlag; //1 绑单  2 制单
	
	private String orderNo; //谷登订单号
	
	private String payCenterNumber; //平台支付流水
	
	private String actualAmt; //实付金额
	
    private String sellerMobile; //卖家手机号
    
    private String appKey;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	public String getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(String actualAmt) {
		this.actualAmt = actualAmt;
	}

	public String getSellerMobile() {
		return sellerMobile;
	}

	public void setSellerMobile(String sellerMobile) {
		this.sellerMobile = sellerMobile;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}

	public String toString() {
		return "orderFlag=" + orderFlag + ",orderNo=" + orderNo + ",payCenterNumber=" + payCenterNumber
				+ ",actualAmt=" + actualAmt + ",sellerMobile=" + sellerMobile 
				+ ",appKey=" + appKey;
	}
    
}
