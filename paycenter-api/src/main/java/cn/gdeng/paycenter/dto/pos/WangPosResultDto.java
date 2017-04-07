package cn.gdeng.paycenter.dto.pos;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author sss
 *
 * since:2016年12月7日
 * version 1.0.0
 */
public class WangPosResultDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private int currentPage;
	
	private boolean hasNextPage;
	
	private int pageCount;//总页数
	
	private int recordCount;//总记录数
	
	private List<OrderbaseInfo> recordList;
	
	
	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public boolean isHasNextPage() {
		return hasNextPage;
	}


	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}


	public int getPageCount() {
		return pageCount;
	}


	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}


	public int getRecordCount() {
		return recordCount;
	}


	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}


	public List<OrderbaseInfo> getRecordList() {
		return recordList;
	}


	public void setRecordList(List<OrderbaseInfo> recordList) {
		this.recordList = recordList;
	}


	static class OrderbaseInfo{
		
		private String buyerName;
		
		private String createDate;//下单日期
		
		private Double orderAmount; //订单价格
		
		private Double payAmount; //用户实际支付
		
		private Double sumPayAmount; ///订单需要支付总金额
		
		private String orderNo;
		
		private String orderStatus;
		
		private int productNum; //产品数
		
		private String productsName;//产品名称

		public String getBuyerName() {
			return buyerName;
		}

		public void setBuyerName(String buyerName) {
			this.buyerName = buyerName;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public Double getOrderAmount() {
			return orderAmount;
		}

		public void setOrderAmount(Double orderAmount) {
			this.orderAmount = orderAmount;
		}

		public Double getPayAmount() {
			return payAmount;
		}

		public void setPayAmount(Double payAmount) {
			this.payAmount = payAmount;
		}

		public Double getSumPayAmount() {
			return sumPayAmount;
		}

		public void setSumPayAmount(Double sumPayAmount) {
			this.sumPayAmount = sumPayAmount;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		public int getProductNum() {
			return productNum;
		}

		public void setProductNum(int productNum) {
			this.productNum = productNum;
		}

		public String getProductsName() {
			return productsName;
		}

		public void setProductsName(String productsName) {
			this.productsName = productsName;
		}

	}
	
}
