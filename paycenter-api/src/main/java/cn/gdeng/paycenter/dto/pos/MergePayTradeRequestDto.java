package cn.gdeng.paycenter.dto.pos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MergePayTradeRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer version;
	
	private String appKey;
	private String timeOut;
	private String requestIp;
	private String payType;
	private String returnUrl;
	private Double sumPayAmt;
	private String payUid;
	private String force;//1 重新生成 2不需要重新生成
	private String orderInfos;
	private List<OrderInfo> orderInfoList;

	public static class OrderInfo implements Serializable{

		private static final long serialVersionUID = 1L;

		private String orderNo;
		
		private String payerUserId;

		private String payeeUserId;
		
		private String logisticsUserId;//物流公司会员ID
		private double totalAmt;
		private double payAmt;
		private String title;
		
		private String requestNo;
		
		private int payCount;
		
		private String reParam;
		
		private Date orderTime;
		
		public Date getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(Date orderTime) {
			this.orderTime = orderTime;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getPayerUserId() {
			return payerUserId;
		}

		public void setPayerUserId(String payerUserId) {
			this.payerUserId = payerUserId;
		}

		public String getPayeeUserId() {
			return payeeUserId;
		}

		public void setPayeeUserId(String payeeUserId) {
			this.payeeUserId = payeeUserId;
		}

		public double getTotalAmt() {
			return totalAmt;
		}

		public void setTotalAmt(double totalAmt) {
			this.totalAmt = totalAmt;
		}

		public double getPayAmt() {
			return payAmt;
		}

		public void setPayAmt(double payAmt) {
			this.payAmt = payAmt;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRequestNo() {
			return requestNo;
		}

		public void setRequestNo(String requestNo) {
			this.requestNo = requestNo;
		}

		public int getPayCount() {
			return payCount;
		}

		public void setPayCount(int payCount) {
			this.payCount = payCount;
		}

		public String getReParam() {
			return reParam;
		}

		public void setReParam(String reParam) {
			this.reParam = reParam;
		}

		public String getLogisticsUserId() {
			return logisticsUserId;
		}

		public void setLogisticsUserId(String logisticsUserId) {
			this.logisticsUserId = logisticsUserId;
		}
		
		
	}
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public List<OrderInfo> getOrderInfoList() {
		return orderInfoList;
	}

	public void setOrderInfoList(List<OrderInfo> orderInfoList) {
		this.orderInfoList = orderInfoList;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Double getSumPayAmt() {
		return sumPayAmt;
	}

	public void setSumPayAmt(Double sumPayAmt) {
		this.sumPayAmt = sumPayAmt;
	}

	public String getPayUid() {
		return payUid;
	}

	public void setPayUid(String payUid) {
		this.payUid = payUid;
	}

	public String getOrderInfos() {
		return orderInfos;
	}

	public void setOrderInfos(String orderInfos) {
		this.orderInfos = orderInfos;
	}

	public String getForce() {
		return force;
	}

	public void setForce(String force) {
		this.force = force;
	}

}
