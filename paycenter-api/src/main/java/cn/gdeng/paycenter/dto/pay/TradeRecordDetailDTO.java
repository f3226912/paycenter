package cn.gdeng.paycenter.dto.pay;



import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.gdeng.paycenter.enums.OrderTypeEnum;
import cn.gdeng.paycenter.enums.PayerOrderSourceEnum;
import cn.gdeng.paycenter.util.admin.web.StringUtil;


/**
* @author DJB
* @version 创建时间：2016年11月15日 下午3:00:57
* 交易记录详情
*/

public class TradeRecordDetailDTO implements java.io.Serializable{
	
	private final static String nullSymbol="-";
	private final static String identity=".";
	
	/**
	 * cn.gdeng.paycenter.admin.dto.admin
	 */
	private static final long serialVersionUID = -8853666142870388301L;
	
	
	private Integer id;
	
	/** 订单号 */
	private String orderNo;
	
	private String vpeOrderNo;
	
	
	private String appKey;
	
	/** 付款方来源 */
	private String payerOrderSourceStr;

	/**  下单时间 */
	private Date orderTime;

	/** 订单类型 */
	private Byte orderType;
	private String orderTypeStr;

	/** 商品信息 */
	private String title;

	/** 订单金额 */
	private String totalAmt; 
	
	
	/** 商品总金额 */
	private String orderAmt;

	/** 付款方账号 */
	private String payerAccount;

	/** 付款方支付佣金 */
	private String payerCommissionAmt;
	private String payerPlatCommissionAmt;

	/**   付款方手机号*/
	private String payerMobile;

	/**  实付金额 */
	private String payAmt;

	/** 支付方式 */
	private String payType;
	private String payTypeStr;

	/** 支付时间 */
	private Date payTime;

	/**  平台流水*/
	private String payCenterNumber;

	/** 第三方支付流水 */
	private String thirdPayNumber;

	/**  收款方账户*/
	private String payeeAccount;

	/** 收款方手机 */
	private String payeeMobile;

	/** 收款方姓名 */
	private String payerName;

	/** 商户ID */
	private String businessId;

	/**  商户名称 */
	private String businessName;

	/**  市场名称*/
	private String marketName;

	/**  谷登代收支付手续费*/
	private String gdFeeAmt;

	/**  收款方佣金*/
	private String payeeCommissionAmt;
	private String payeePlatCommissionAmt;

	/**  收款方补贴*/
	private String payeeSubsidyAmt;

	/**  收款方实收金额*/
	private String payeeAmt;

	/**  收款方收款账号名称*/
	private String payeeRealName;

	/**  收款方收款账号银行*/
	private String payeeDepositBankName;

	/**  收款方收款账号*/
	private String payeeBankCardNo;
	
	private String payeeBankCardNoStr;

	/**  市场账号*/
	private String marketAccount;

	/**  市场手机*/
	private String marketMobile;

	/**  市场收款人姓名*/
	private String marketRealName;

	/**  市场收款账号银行*/
	private String marketDepositBankName;

	/**  市场收款账号*/
	private String marketBankCardNo;
	private String marketBankCardNoStr;

	/**  市场实收佣金*/
	private String marketCommissionAmt;
	
	/***   市场应收金额 **/
	private String marketReceivableCommissionAmt;
	
	
	
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
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Byte getOrderType() {
		return orderType;
	}
	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTotalAmt() {
		return StringUtil.formatDoubleStringTwo(this.totalAmt,nullSymbol,identity,getVpeOrderNo());
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getPayerAccount() {
		return payerAccount;
	}
	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}
	public String getPayerCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.payerCommissionAmt,nullSymbol,identity,getVpeOrderNo());
		//return payerCommissionAmt;
	}
	public void setPayerCommissionAmt(String payerCommissionAmt) {
		this.payerCommissionAmt = payerCommissionAmt;
	}
	public String getPayerMobile() {
		return payerMobile;
	}
	public void setPayerMobile(String payerMobile) {
		this.payerMobile = payerMobile;
	}
	public String getPayAmt() {
		return StringUtil.formatDoubleStringTwo(this.payAmt,nullSymbol,identity,getVpeOrderNo());
		//return payAmt;
	}
	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getPayCenterNumber() {
		return payCenterNumber;
	}
	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}
	public String getThirdPayNumber() {
		return thirdPayNumber;
	}
	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}
	public String getPayeeAccount() {
		return payeeAccount;
	}
	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}
	public String getPayeeMobile() {
		return payeeMobile;
	}
	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getGdFeeAmt() {
		return StringUtil.formatDoubleStringTwo(this.gdFeeAmt,nullSymbol,identity,getVpeOrderNo());

	//	return gdFeeAmt;
	}
	public void setGdFeeAmt(String gdFeeAmt) {
		this.gdFeeAmt = gdFeeAmt;
	}
	public String getPayeeCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.payeeCommissionAmt,nullSymbol,identity,getVpeOrderNo());

		//return payeeCommissionAmt;
	}
	public void setPayeeCommissionAmt(String payeeCommissionAmt) {
		this.payeeCommissionAmt = payeeCommissionAmt;
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
	public String getPayeeRealName() {
		return payeeRealName;
	}
	public void setPayeeRealName(String payeeRealName) {
		this.payeeRealName = payeeRealName;
	}
	public String getPayeeDepositBankName() {
		return payeeDepositBankName;
	}
	public void setPayeeDepositBankName(String payeeDepositBankName) {
		this.payeeDepositBankName = payeeDepositBankName;
	}
	public String getPayeeBankCardNo() {
		return payeeBankCardNo;
	}
	public void setPayeeBankCardNo(String payeeBankCardNo) {
		this.payeeBankCardNo = payeeBankCardNo;
	}
	public String getMarketAccount() {
		return marketAccount;
	}
	public void setMarketAccount(String marketAccount) {
		this.marketAccount = marketAccount;
	}
	public String getMarketMobile() {
		return marketMobile;
	}
	public void setMarketMobile(String marketMobile) {
		this.marketMobile = marketMobile;
	}
	public String getMarketRealName() {
		return marketRealName;
	}
	public void setMarketRealName(String marketRealName) {
		this.marketRealName = marketRealName;
	}
	public String getMarketDepositBankName() {
		return marketDepositBankName;
	}
	public void setMarketDepositBankName(String marketDepositBankName) {
		this.marketDepositBankName = marketDepositBankName;
	}
	public String getMarketBankCardNo() {
		return marketBankCardNo;
	}
	public void setMarketBankCardNo(String marketBankCardNo) {
		this.marketBankCardNo = marketBankCardNo;
	}
	public String getMarketCommissionAmt() {
		return StringUtil.formatDoubleStringTwo(this.marketCommissionAmt,nullSymbol,identity,getVpeOrderNo());

		//return marketCommissionAmt;
	}
	public void setMarketCommissionAmt(String marketCommissionAmt) {
		this.marketCommissionAmt = marketCommissionAmt;
	}	
	
	
	
	
	public String getVpeOrderNo() {
		return vpeOrderNo;
	}
	public void setVpeOrderNo(String vpeOrderNo) {
		this.vpeOrderNo = vpeOrderNo;
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

	
	public String getPayTypeStr() {
		return payTypeStr;
	}
	public void setPayTypeStr(String payTypeStr) {
		this.payTypeStr = payTypeStr;
	}
	
	public String getPayerOrderSourceStr() {
		if (getOrderType()==null) {
			return null;
		}
		return PayerOrderSourceEnum.getNameByCode(getOrderType().toString());
	}
	
	public String getOrderTypeStr() {
		if (getOrderType()==null) {
			return null;
		}
		return OrderTypeEnum.getNameByCode(getOrderType().toString());
	}

	public String getPayeeBankCardNoStr(){
		if(StringUtils.isNotBlank(getPayeeDepositBankName())&&StringUtils.isNotBlank(getPayeeBankCardNo())){
			StringBuffer buffer=new StringBuffer();
			buffer.append(getPayeeDepositBankName());
			buffer.append("(尾号");
			buffer.append(getPayeeBankCardNo().substring(getPayeeBankCardNo().length()-4, getPayeeBankCardNo().length()));
			buffer.append(")");
			return buffer.toString();
		}
		return null;
	}
	
	
	
	public String getMarketBankCardNoStr(){
		if(StringUtils.isNotBlank(getMarketDepositBankName())&&StringUtils.isNotBlank(getMarketBankCardNo())){
			StringBuffer buffer=new StringBuffer();
			buffer.append(getMarketDepositBankName());
			buffer.append("(尾号");
			buffer.append(getMarketBankCardNo().substring(getMarketBankCardNo().length()-4, getMarketBankCardNo().length()));
			buffer.append(")");
			return buffer.toString();
		}
		return null;
	}
	public String getPayeePlatCommissionAmt() {
		return this.payeePlatCommissionAmt;
	}
	public void setPayeePlatCommissionAmt(String payeePlatCommissionAmt) {
		this.payeePlatCommissionAmt = payeePlatCommissionAmt;
	}
	public String getPayerPlatCommissionAmt() {
		return this.payerPlatCommissionAmt;
	}
	public void setPayerPlatCommissionAmt(String payerPlatCommissionAmt) {
		this.payerPlatCommissionAmt = payerPlatCommissionAmt;
	}
	

}
