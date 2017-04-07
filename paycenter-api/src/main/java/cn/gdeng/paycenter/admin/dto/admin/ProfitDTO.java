package cn.gdeng.paycenter.admin.dto.admin;

public class ProfitDTO implements java.io.Serializable{
	private static final long serialVersionUID = -4661776264253113092L;

	/**
	 * 关联交易记录ID
	 * */
	private String id;
	
	/**
	 * 关联订单号
	 */
	private String orderNo;
	
	/**
	 * 用户手机
	 */
	private String payerMobile;
	
	/**
	 * 商品总金额
	 */
	private Double totalAmt;
	
	/**
	 * 佣金收益
	 */
	private Double commission;
	
	/**
	 * 下单时间
	 */
	private String orderTime;
	
	/**
	* 佣金支出方
	*/
	private String userType;
	
	private String userTypeStr;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayerMobile() {
		return payerMobile;
	}

	public void setPayerMobile(String payerMobile) {
		this.payerMobile = payerMobile;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public String getOrderTime() {
		if(orderTime == null){
			return "";
		}
		return this.orderTime.substring(0, orderTime.length() - 2);
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserTypeStr() {
		if(this.getUserType() == null){
			return "农商友";
		}else{
			if("nsy".equals(this.getUserType())){
				return "农商友";
			}
			if("nps".equals(this.getUserType())){
				return "农批商";
			}
		}
		return "农商友";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}
