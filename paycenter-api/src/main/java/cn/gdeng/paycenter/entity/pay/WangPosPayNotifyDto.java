package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: TODO(旺pos异步响应json对象)
 * @author mpan
 * @date 2016年12月13日 下午12:09:18
 */
public class WangPosPayNotifyDto implements Serializable {

	private static final long serialVersionUID = -4745053289344414041L;

	@JSONField(name = "out_trade_no")
	private String outTradeNo; // 商户订单号
	
	@JSONField(name = "cashier_trade_no")
	private String cashierTradeNo; // 收银订单号
	
	@JSONField(name = "mcode")
	private String mcode; // 旺POS店铺编号
	
	@JSONField(name = "device_en")
	private String deviceEn; // 旺POS设备En号
	
	@JSONField(name = "total_fee")
	private String totalFee; // 订单总金额，单位分
	
	@JSONField(name = "trade_status")
	private String tradeStatus; // 交易状态，参考附录交易状态说明
	
	@JSONField(name = "pay_info")
	private String payInfo; // 交易状态说明
	
	@JSONField(name = "operator_name")
	private String operatorName; // 收银操作员
	
	@JSONField(name = "time_create")
	private String timeCreate; // 订单创建时间，格式yyyyMMddHHmmss
	
	@JSONField(name = "attach")
	private String attach; // 下单时传入的参数原样返回
	
	/** 以下参数仅当trade_status为PAY或REFUND返回 **/
	
	@JSONField(name = "pay_type")
	private String payType; // 付款方式，参考付款方式附录说明
	
	@JSONField(name = "pay_fee")
	private String payFee; // 支付金额，单位分
	
	@JSONField(name = "check_fee")
	private String checkFee; // 旺POS会员优惠金额，单位分
	
	@JSONField(name = "refund_amount")
	private String refundAmount; // 退款金额，单位分
	
	@JSONField(name = "thirdDiscount")
	private String thirdDiscount; // 支付宝、微信等渠道优惠金额，钱会补贴给商家，单位分
	
	@JSONField(name = "thirdMDiscount")
	private String thirdMDiscount; // 商家在支付宝、微信等渠道优惠金额，钱由商家自己付出，单位分
	
	@JSONField(name = "time_end")
	private String timeEnd; // 支付时间，格式yyyyMMddHHmmss
	
	@JSONField(name = "buyer")
	private String buyer; // 付款用户信息，银行卡支付时为银行卡号
	
	@JSONField(name = "thirdSerialNo")
	private String thirdSerialNo; // 第三方流水号，银行卡支付时为参考号，支付宝、微信等支付为支付宝、微信相应的流水号
	
	@JSONField(name = "terminalNo")
	private String terminalNo; // 终端号，银行卡支付时才有该参数
	
	@JSONField(name = "cardType")
	private String cardType; // 银行卡类型，银行卡支付时才有该参数
	
	@JSONField(name = "discount_goods_detail")
	private String discountGoodsDetail; // 支付宝支付特殊返回参数，不限长度

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getCashierTradeNo() {
		return cashierTradeNo;
	}

	public void setCashierTradeNo(String cashierTradeNo) {
		this.cashierTradeNo = cashierTradeNo;
	}

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getDeviceEn() {
		return deviceEn;
	}

	public void setDeviceEn(String deviceEn) {
		this.deviceEn = deviceEn;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTimeCreate() {
		return timeCreate;
	}

	public void setTimeCreate(String timeCreate) {
		this.timeCreate = timeCreate;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getCheckFee() {
		return checkFee;
	}

	public void setCheckFee(String checkFee) {
		this.checkFee = checkFee;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getThirdDiscount() {
		return thirdDiscount;
	}

	public void setThirdDiscount(String thirdDiscount) {
		this.thirdDiscount = thirdDiscount;
	}

	public String getThirdMDiscount() {
		return thirdMDiscount;
	}

	public void setThirdMDiscount(String thirdMDiscount) {
		this.thirdMDiscount = thirdMDiscount;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getThirdSerialNo() {
		return thirdSerialNo;
	}

	public void setThirdSerialNo(String thirdSerialNo) {
		this.thirdSerialNo = thirdSerialNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getDiscountGoodsDetail() {
		return discountGoodsDetail;
	}

	public void setDiscountGoodsDetail(String discountGoodsDetail) {
		this.discountGoodsDetail = discountGoodsDetail;
	}

}
