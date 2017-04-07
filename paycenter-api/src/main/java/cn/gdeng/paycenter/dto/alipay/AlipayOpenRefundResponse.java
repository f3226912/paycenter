package cn.gdeng.paycenter.dto.alipay;

public class AlipayOpenRefundResponse extends AlipayOpenResponse{

	private static final long serialVersionUID = 1L;
	
	private String trade_no;
	
	private String gmt_refund_pay;

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getGmt_refund_pay() {
		return gmt_refund_pay;
	}

	public void setGmt_refund_pay(String gmt_refund_pay) {
		this.gmt_refund_pay = gmt_refund_pay;
	}
	
	

}
