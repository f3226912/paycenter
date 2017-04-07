package cn.gdeng.paycenter.dto.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单活动返回结果DTO
 * @author TerryZhang
 */
public class GdOrderActivityResultDTO implements java.io.Serializable{

	private static final long serialVersionUID = 3974030193542551755L;
	
	/** 订单号 */
	private String orderNo;

	/** 商铺id */
	private Integer businessId;
	
	/** 市场id */
	private Integer marketId;

	/** 买家id */
	private Integer buyerId;
	
	/** 卖家id */
	private Integer sellerId;
	
	/** 是否有活动 */
	private boolean hasAct = false;
	
	/** 买家是否有活动 */
	private boolean hasBuyerAct = false;
	
	/** 卖家是否有活动 */
	private boolean hasSellerAct = false;
	
	/** 买家活动信息列表 */
	private GdOrderActivityInfoDTO buyerActInfo;
	
	/** 买家商品活动信息列表 */
	private Map<Integer, Integer> buyerProductActInfo;

	/** 卖家佣金补贴信息列表 */
	private GdOrderActivityInfoDTO sellerActInfo;
	
	/** 卖家商品活动信息列表 */
	private Map<Integer, Integer> sellerProductActInfo;
	
	public boolean isHasAct() {
		return hasAct;
	}

	public void setHasAct(boolean hasAct) {
		this.hasAct = hasAct;
	}

	public Integer getMarketId() {
		return marketId;
	}

	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
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

	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public boolean isHasBuyerAct() {
		return hasBuyerAct;
	}

	public void setHasBuyerAct(boolean hasBuyerAct) {
		this.hasBuyerAct = hasBuyerAct;
	}

	public boolean isHasSellerAct() {
		return hasSellerAct;
	}

	public void setHasSellerAct(boolean hasSellerAct) {
		this.hasSellerAct = hasSellerAct;
	}
	
	public GdOrderActivityInfoDTO getBuyerActInfo() {
		return buyerActInfo;
	}

	public void setBuyerActInfo(GdOrderActivityInfoDTO buyerActInfo) {
		this.buyerActInfo = buyerActInfo;
	}

	public GdOrderActivityInfoDTO getSellerActInfo() {
		return sellerActInfo;
	}

	public void setSellerActInfo(GdOrderActivityInfoDTO sellerActInfo) {
		this.sellerActInfo = sellerActInfo;
	}
	
	public Map<Integer, Integer> getBuyerProductActInfo() {
		if(buyerProductActInfo == null){
			buyerProductActInfo = new HashMap<>();
		}
		return buyerProductActInfo;
	}

	public void setBuyerProductActInfo(Map<Integer, Integer> buyerProductActInfo) {
		this.buyerProductActInfo = buyerProductActInfo;
	}

	public Map<Integer, Integer> getSellerProductActInfo() {
		if(sellerProductActInfo == null){
			sellerProductActInfo = new HashMap<>();
		}
		return sellerProductActInfo;
	}

	public void setSellerProductActInfo(Map<Integer, Integer> sellerProductActInfo) {
		this.sellerProductActInfo = sellerProductActInfo;
	}
}
