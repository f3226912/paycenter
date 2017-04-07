package cn.gdeng.paycenter.dto.account;

public class AccountQueryAccountLogVO {

	/**
	 * 余额
	 */
	private String balance;
	
	/**
	 * 收入金额
	 */
	private String income;
	
	/**
	 * 支出金额
	 */
	private String outcome;
	
	/**
	 * 订单付款时间
	 */
	private String trans_date;
	
	/**
	 * 业务类型
	 */
	private String trans_code_msg;
	
	/**
	 * 商户订单号
	 */
	private String merchant_out_order_no;
	
	/**
	 * 银行名称
	 */
	private String bank_name;
	
	/**
	 * 银行账号 
	 */
	private String bank_account_no;
	
	/**
	 * 银行账户名字
	 */
	private String bank_account_name;
	
	/**
	 * 买家支付宝人民币资金账号
	 */
	private String buyer_account;
	
	/**
	 * 卖家支付宝人民币资金账号
	 */
	private String seller_account;
	
	/**
	 * 卖家姓名
	 */
	private String seller_fullname;
	
	/**
	 * 商品名称 对应pay_trade中的title
	 */
	private String goods_title;
	
	
	private String partner_id;
	
	private String total_fee;
	
	/**
	 * 支付宝交易号
	 */
	private String trade_no;
	
	/**
	 * 累积退款金额
	 */
	private String trade_refund_amount;
	
	/**
	 * 签约产品
	 */
	private String sign_product_name;
	
	/**
	 * 费率
	 */
	private String rate;
	
	private String xml;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}

	public String getTrans_code_msg() {
		return trans_code_msg;
	}

	public void setTrans_code_msg(String trans_code_msg) {
		this.trans_code_msg = trans_code_msg;
	}

	public String getMerchant_out_order_no() {
		return merchant_out_order_no;
	}

	public void setMerchant_out_order_no(String merchant_out_order_no) {
		this.merchant_out_order_no = merchant_out_order_no;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_account_no() {
		return bank_account_no;
	}

	public void setBank_account_no(String bank_account_no) {
		this.bank_account_no = bank_account_no;
	}

	public String getBank_account_name() {
		return bank_account_name;
	}

	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}

	public String getBuyer_account() {
		return buyer_account;
	}

	public void setBuyer_account(String buyer_account) {
		this.buyer_account = buyer_account;
	}

	public String getSeller_account() {
		return seller_account;
	}

	public void setSeller_account(String seller_account) {
		this.seller_account = seller_account;
	}

	public String getSeller_fullname() {
		return seller_fullname;
	}

	public void setSeller_fullname(String seller_fullname) {
		this.seller_fullname = seller_fullname;
	}

	public String getGoods_title() {
		return goods_title;
	}

	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_refund_amount() {
		return trade_refund_amount;
	}

	public void setTrade_refund_amount(String trade_refund_amount) {
		this.trade_refund_amount = trade_refund_amount;
	}

	public String getSign_product_name() {
		return sign_product_name;
	}

	public void setSign_product_name(String sign_product_name) {
		this.sign_product_name = sign_product_name;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
	
}
