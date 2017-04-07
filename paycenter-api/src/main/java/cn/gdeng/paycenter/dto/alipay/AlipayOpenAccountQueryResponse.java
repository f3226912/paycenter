package cn.gdeng.paycenter.dto.alipay;

public class AlipayOpenAccountQueryResponse extends AlipayOpenResponse{

	private static final long serialVersionUID = 1L;
	
	private String trade_no;
	
	private String out_trade_no;
	
	private String buyer_logon_id;//买家支付宝账号

	private String trade_status;//交易状态
	
	private String total_amount;
	
	/**
	 * 本次交易打款给卖家的时间
	 */
	private String send_pay_date;

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}

	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getSend_pay_date() {
		return send_pay_date;
	}

	public void setSend_pay_date(String send_pay_date) {
		this.send_pay_date = send_pay_date;
	}
	
}
