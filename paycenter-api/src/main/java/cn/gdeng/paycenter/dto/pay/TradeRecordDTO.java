package cn.gdeng.paycenter.dto.pay;


import java.util.Date;

import cn.gdeng.paycenter.enums.OrderTypeEnum;
import cn.gdeng.paycenter.enums.PayerOrderSourceEnum;
import cn.gdeng.paycenter.util.admin.web.StringUtil;

/**
* @author DJB
* @version 创建时间：2016年11月10日 上午10:08:20
* 交易记录列表
*/

public class TradeRecordDTO implements java.io.Serializable {
	
	private final static String nullSymbol="-";
	private final static String identity=".";

	/**
	 * cn.gdeng.paycenter.admin.dto.admin
	 */
	private static final long serialVersionUID = 3580685709612948090L;
	
	private Integer id;
	
	private String orderNo;
	
	private Byte orderType;
	private String orderTypeStr;
	
	private Date orderTime;
	
	private String totalAmt;
	
	/** 商品总金额 */
	private String orderAmt;
	
	private String payerMobile;
	
	private String payerCommissionAmt; //市场佣金
	
	private String payerPlatCommissonAmt; //平台佣金
	
	private String payerAmt;
	
	private String gdAmt;
	
	private String gdFeeAmt;
	
	private String payeeMobile;
	
	private String payeeCommissionAmt; //收款方市场佣金
	
	private String payeePlatCommissonAmt; //收款方平台佣金
	
	private String payeeSubsidyAmt;
	
	private String payeeAmt;
	
	private String marketCommissionAmt;
	
	private Integer marketId;
	
	private String marketName;
	
	private String appKey;
	
	/** 付款方来源 */
	private String payerOrderSourceStr;
	
	
	/***   市场应收金额 **/
	private String marketReceivableCommissionAmt;
	
	private String vpeOrderNo;
	
	

	public String getVpeOrderNo() {
		return vpeOrderNo;
	}

	public void setVpeOrderNo(String vpeOrderNo) {
		this.vpeOrderNo = vpeOrderNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Byte getOrderType() {
		return orderType;
	}

	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getTotalAmt() {
		return StringUtil.formatDoubleStringTwo(this.totalAmt,nullSymbol,identity,getVpeOrderNo());
		//return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getPayerMobile() {
		return payerMobile;
	}

	public void setPayerMobile(String payerMobile) {
		this.payerMobile = payerMobile;
	}

	public String getPayerCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.payerCommissionAmt,nullSymbol,identity,getVpeOrderNo());
		//return payerCommissionAmt;
	}

	public void setPayerCommissionAmt(String payerCommissionAmt) {
		this.payerCommissionAmt = payerCommissionAmt;
	}
	

	public String getPayerPlatCommissonAmt() {
		return StringUtil.formatDoubleStringTwo(this.payerPlatCommissonAmt,nullSymbol,identity,getVpeOrderNo());
	}

	public void setPayerPlatCommissonAmt(String payerPlatCommissonAmt) {
		this.payerPlatCommissonAmt = payerPlatCommissonAmt;
	}

	public String getPayerAmt() {
		return payerAmt;
		//return payerAmt;
	}

	public void setPayerAmt(String payerAmt) {
		this.payerAmt = payerAmt;
	}

	public String getGdAmt() {
		return gdAmt;
		//return gdAmt;
	}

	public void setGdAmt(String gdAmt) {
		this.gdAmt = gdAmt;
	}

	public String getGdFeeAmt() {
		return StringUtil.formatDoubleStringTwo(this.gdFeeAmt,nullSymbol,identity,getVpeOrderNo());
		//return gdFeeAmt;
	}

	public void setGdFeeAmt(String gdFeeAmt) {
		this.gdFeeAmt = gdFeeAmt;
	}

	public String getPayeeMobile() {
		return payeeMobile;
	}

	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
	}

	public String getPayeeCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.payeeCommissionAmt,nullSymbol,identity,getVpeOrderNo());
		//return payeeCommissionAmt;
	}

	public void setPayeeCommissionAmt(String payeeCommissionAmt) {
		this.payeeCommissionAmt = payeeCommissionAmt;
	}

	public String getPayeePlatCommissonAmt() {
		return StringUtil.formatDoubleStringTwo(this.payeePlatCommissonAmt,nullSymbol,identity,getVpeOrderNo());
	}

	public void setPayeePlatCommissonAmt(String payeePlatCommissonAmt) {
		this.payeePlatCommissonAmt = payeePlatCommissonAmt;
	}

	public String getPayeeSubsidyAmt() {
		return StringUtil.formatDoubleStringTwo(this.payeeSubsidyAmt,nullSymbol,identity,getVpeOrderNo());
		//return payeeSubsidyAmt;
	}

	public void setPayeeSubsidyAmt(String payeeSubsidyAmt) {
		this.payeeSubsidyAmt = payeeSubsidyAmt;
	}

	public String getPayeeAmt() {
		return StringUtil.formatDoubleStringTwo(this.payeeAmt,nullSymbol,identity,getVpeOrderNo());
		//return payeeAmt;
	}

	public void setPayeeAmt(String payeeAmt) {
		this.payeeAmt = payeeAmt;
	}

	public String getMarketCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.marketCommissionAmt,nullSymbol,identity,getVpeOrderNo());
		//return marketCommissionAmt;
	}

	public void setMarketCommissionAmt(String marketCommissionAmt) {
		this.marketCommissionAmt = marketCommissionAmt;
	}

	public Integer getMarketId() {
		return marketId;
	}

	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getOrderAmt() {
		return StringUtil.formatDoubleStringTwo(this.orderAmt,nullSymbol,identity,getVpeOrderNo());
		//return orderAmt;
	}
	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}
	
	
	public String getMarketReceivableCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.marketReceivableCommissionAmt,nullSymbol,identity,getVpeOrderNo());
		//return marketReceivableCommissionAmt;
	}
	public void setMarketReceivableCommissionAmt(String marketReceivableCommissionAmt) {
		this.marketReceivableCommissionAmt = marketReceivableCommissionAmt;
	}
	public String getOrderTypeStr() {
		if (getOrderType()==null) {
			return null;
		}
		return OrderTypeEnum.getNameByCode(getOrderType().toString());
	}
	
	public String getPayerOrderSourceStr() {
		if (getOrderType()==null) {
			return null;
		}
		return PayerOrderSourceEnum.getNameByCode(getOrderType().toString());
	}
	
	
	
	
	
	
	

}
