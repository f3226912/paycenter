package cn.gdeng.paycenter.dto.pay;

public class ClearBillDTO {

	//单号
	private String orderNo;
	//
	private String clearType;
	//交易来源
	private String appKey;
	//交易金额
	private Double payAmt;
	//买家(付款方)id
	private Integer buyerId;
	//卖家(收款方)id
	private Integer sellerId;
	//市场用户id
	private Integer marketUserId;
	//平台id
	private Integer platUserId;
	//平台手续费
	private Double platFeeAmt;
	//买家支付市场佣金
	private Double buyerCommissionAmt;
	//卖家支付市场佣金
	private Double sellerCommissionAmt;
	//买家补贴
	private Double buyerSubsidyAmt;
	//卖家补贴
	private Double sellerSubsidyAmt;
	//银行类型
	private String bankType;
	//支付类型
	private String payChannelCode;
	//区域类型
	private String areaType;
	//卡类型
	private String cardType;
	
	
	//买家支付平台佣金
	private Double buyerPlatCommissionAmt;
	//卖家支付平台佣金
	private Double sellerPlatCommissionAmt;
	//买家退款收入金额
	private Double buyerRefundAmt;
	//卖家违约金收入金额
	private Double sellerPenaltyAmt;
	//物流公司违约金收入金额
	private Double logisPenaltyAmt;
	private Integer logisUserId;
	//平台违约金收入金额
	private Double platPenaltyAmt;
	
	//业务分类(trade交易 refund退款)
	private String busiCategory;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Double getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(Double payAmt) {
		this.payAmt = payAmt;
	}

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public String getClearType() {
		return clearType;
	}

	public void setClearType(String clearType) {
		this.clearType = clearType;
	}

	public Double getBuyerCommissionAmt() {
		return buyerCommissionAmt;
	}

	public void setBuyerCommissionAmt(Double buyerCommissionAmt) {
		this.buyerCommissionAmt = buyerCommissionAmt;
	}

	public Double getSellerCommissionAmt() {
		return sellerCommissionAmt;
	}

	public void setSellerCommissionAmt(Double sellerCommissionAmt) {
		this.sellerCommissionAmt = sellerCommissionAmt;
	}

	public Double getBuyerSubsidyAmt() {
		return buyerSubsidyAmt;
	}

	public void setBuyerSubsidyAmt(Double buyerSubsidyAmt) {
		this.buyerSubsidyAmt = buyerSubsidyAmt;
	}

	public Double getSellerSubsidyAmt() {
		return sellerSubsidyAmt;
	}

	public void setSellerSubsidyAmt(Double sellerSubsidyAmt) {
		this.sellerSubsidyAmt = sellerSubsidyAmt;
	}

	public Integer getMarketUserId() {
		return marketUserId;
	}

	public void setMarketUserId(Integer marketUserId) {
		this.marketUserId = marketUserId;
	}

	public Integer getPlatUserId() {
		return platUserId;
	}

	public void setPlatUserId(Integer platUserId) {
		this.platUserId = platUserId;
	}

	public Double getPlatFeeAmt() {
		return platFeeAmt;
	}

	public void setPlatFeeAmt(Double platFeeAmt) {
		this.platFeeAmt = platFeeAmt;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getPayChannelCode() {
		return payChannelCode;
	}

	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	
	public Double getBuyerPlatCommissionAmt() {
		return buyerPlatCommissionAmt;
	}

	public void setBuyerPlatCommissionAmt(Double buyerPlatCommissionAmt) {
		this.buyerPlatCommissionAmt = buyerPlatCommissionAmt;
	}

	public Double getSellerPlatCommissionAmt() {
		return sellerPlatCommissionAmt;
	}

	public void setSellerPlatCommissionAmt(Double sellerPlatCommissionAmt) {
		this.sellerPlatCommissionAmt = sellerPlatCommissionAmt;
	}

	public Integer getLogisUserId() {
		return logisUserId;
	}

	public void setLogisUserId(Integer logisUserId) {
		this.logisUserId = logisUserId;
	}

	public Double getBuyerRefundAmt() {
		return buyerRefundAmt;
	}

	public void setBuyerRefundAmt(Double buyerRefundAmt) {
		this.buyerRefundAmt = buyerRefundAmt;
	}

	public Double getSellerPenaltyAmt() {
		return sellerPenaltyAmt;
	}

	public void setSellerPenaltyAmt(Double sellerPenaltyAmt) {
		this.sellerPenaltyAmt = sellerPenaltyAmt;
	}

	public Double getLogisPenaltyAmt() {
		return logisPenaltyAmt;
	}

	public void setLogisPenaltyAmt(Double logisPenaltyAmt) {
		this.logisPenaltyAmt = logisPenaltyAmt;
	}

	public Double getPlatPenaltyAmt() {
		return platPenaltyAmt;
	}

	public void setPlatPenaltyAmt(Double platPenaltyAmt) {
		this.platPenaltyAmt = platPenaltyAmt;
	}

	public String getBusiCategory() {
		return busiCategory;
	}

	public void setBusiCategory(String busiCategory) {
		this.busiCategory = busiCategory;
	}

	@Override
	public String toString() {
		return "ClearBillDTO [orderNo=" + orderNo + ", clearType=" + clearType + ", appKey=" + appKey + ", payAmt="
				+ payAmt + ", buyerId=" + buyerId + ", sellerId=" + sellerId + ", marketUserId=" + marketUserId
				+ ", platUserId=" + platUserId + ", platFeeAmt=" + platFeeAmt + ", buyerCommissionAmt="
				+ buyerCommissionAmt + ", sellerCommissionAmt=" + sellerCommissionAmt + ", buyerSubsidyAmt="
				+ buyerSubsidyAmt + ", sellerSubsidyAmt=" + sellerSubsidyAmt + ", bankType=" + bankType
				+ ", payChannelCode=" + payChannelCode + ", areaType=" + areaType + ", cardType=" + cardType
				+ ", buyerPlatCommissionAmt=" + buyerPlatCommissionAmt + ", sellerPlatCommissionAmt="
				+ sellerPlatCommissionAmt + ", buyerRefundAmt=" + buyerRefundAmt + ", sellerPenaltyAmt="
				+ sellerPenaltyAmt + ", logisPenaltyAmt=" + logisPenaltyAmt + ", logisUserId=" + logisUserId
				+ ", platPenaltyAmt=" + platPenaltyAmt + "]";
	}

	

	
}
