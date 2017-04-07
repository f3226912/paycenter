package cn.gdeng.paycenter.dto.refund;

import java.io.Serializable;

public class RefundTradeDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String payCenterNumber;
	
	private String refundRequestNo;
	
	private String refundAmt;
	
	private String refundReason;
	
	private String orderNo;
	
	private String appKey;
	
	private String thirdRefundRequestNo;
	
	private String refundUserId;
	
	//费用类型 1赔付卖家违约金 2赔付平台违约金 3赔付物流违约金
	
	private String sellerRefundAmt;
	
	private String platRefundAmt;
	
	private String logisRefundAmt;
	
	private String refundNo;//退款单号

	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	public String getRefundRequestNo() {
		return refundRequestNo;
	}

	public void setRefundRequestNo(String refundRequestNo) {
		this.refundRequestNo = refundRequestNo;
	}

	public String getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getThirdRefundRequestNo() {
		if(this.thirdRefundRequestNo == null){
			return getOrderNo()+"_"+getRefundRequestNo();
		}
		return thirdRefundRequestNo;
	}

	public void setThirdRefundRequestNo(String thirdRefundRequestNo) {
		this.thirdRefundRequestNo = thirdRefundRequestNo;
	}

	public String getSellerRefundAmt() {
		return sellerRefundAmt;
	}

	public void setSellerRefundAmt(String sellerRefundAmt) {
		this.sellerRefundAmt = sellerRefundAmt;
	}

	public String getPlatRefundAmt() {
		return platRefundAmt;
	}

	public void setPlatRefundAmt(String platRefundAmt) {
		this.platRefundAmt = platRefundAmt;
	}

	public String getLogisRefundAmt() {
		return logisRefundAmt;
	}

	public void setLogisRefundAmt(String logisRefundAmt) {
		this.logisRefundAmt = logisRefundAmt;
	}

	public String getRefundUserId() {
		return refundUserId;
	}

	public void setRefundUserId(String refundUserId) {
		this.refundUserId = refundUserId;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	
	
}
