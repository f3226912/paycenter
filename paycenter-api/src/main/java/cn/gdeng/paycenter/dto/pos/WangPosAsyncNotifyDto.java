package cn.gdeng.paycenter.dto.pos;

import java.io.Serializable;

public class WangPosAsyncNotifyDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String out_trade_no;//商户订单号
	
	private String cashier_trade_no;//收银订单号
	
	private String mcode;//旺POS店铺编号
	
	private String device_en;//旺POS设备En号
	
	private String trade_status;//交易状态
	
	private String pay_info;//交易状态说明
	
	private String pay_type;//	付款方式 1001 现金 1003 微信1004 支付宝1005 百度钱包1006 银行卡1007 易付宝1009 京东钱包1011 QQ钱包
	
	private String pay_fee;//支付金额，单位分
	
	private String time_end;//	支付时间，格式yyyyMMddHHmmss
	
	private String buyer;//付款用户信息，银行卡支付时为银行卡号
	
	private String thirdSerialNo;//第三方流水号，银行卡支付时为参考号，支付宝、微信等支付为支付宝、微信相应的流水号
	
	private String terminalNo;//终端号，银行卡支付时才有该参数
	
	private String cardType;//银行卡类型，银行卡支付时才有该参数
	
	private String thirdDiscount;//支付宝、微信等渠道优惠金额，钱会补贴给商家，单位分
	
	private String thirdMDiscount;//商家在支付宝、微信等渠道优惠金额，钱由商家自己付出，单位分
	
	private String check_fee;//旺POS会员优惠金额，单位分

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getCashier_trade_no() {
		return cashier_trade_no;
	}

	public void setCashier_trade_no(String cashier_trade_no) {
		this.cashier_trade_no = cashier_trade_no;
	}

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getDevice_en() {
		return device_en;
	}

	public void setDevice_en(String device_en) {
		this.device_en = device_en;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getPay_info() {
		return pay_info;
	}

	public void setPay_info(String pay_info) {
		this.pay_info = pay_info;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getPay_fee() {
		return pay_fee;
	}

	public void setPay_fee(String pay_fee) {
		this.pay_fee = pay_fee;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
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

	public String getCheck_fee() {
		return check_fee;
	}

	public void setCheck_fee(String check_fee) {
		this.check_fee = check_fee;
	}

}
