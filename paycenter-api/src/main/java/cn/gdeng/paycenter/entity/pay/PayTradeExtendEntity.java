package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "pay_trade_extend")
public class PayTradeExtendEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String orderNo;
	
	private String appKey;
	
	private Long marketId;
	
	private Long businessId;
	
	private String orderType;
	
	private String businessName;
	
	private String marketName;
	
	private String version;
	
    private Date createTime;

    private String createUserId;

    private Date updateTime;

    private String updateUserId;

    @Id
    @Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "orderNo")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "marketId")
	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}

	@Column(name = "businessId")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {

		return this.createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	@Column(name = "createUserId")
	public String getCreateUserId() {

		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {

		this.createUserId = createUserId;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {

		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {

		this.updateTime = updateTime;
	}

	@Column(name = "updateUserId")
	public String getUpdateUserId() {

		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {

		this.updateUserId = updateUserId;
	}

	@Column(name = "orderType")
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Column(name = "businessName")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Column(name = "marketName")
	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getVersion() {
		return version;
	}

	@Column(name = "version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getAppKey() {
		return appKey;
	}

	@Column(name = "appKey")
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	
}
