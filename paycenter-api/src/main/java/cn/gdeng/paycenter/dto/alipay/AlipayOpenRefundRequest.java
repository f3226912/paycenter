package cn.gdeng.paycenter.dto.alipay;

import org.apache.commons.lang3.StringUtils;

public class AlipayOpenRefundRequest extends AlipayOpenRequest{


	private static final long serialVersionUID = 1L;
	
	private String method="alipay.trade.refund";
	
	private String out_trade_no;
	
	private String refund_amount;
	
	private String refund_reason;
	
	private String out_request_no;
	
	private String biz_content;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getOut_request_no() {
		return out_request_no;
	}

	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}

	@Override
	public String getBiz_content() {
		if(this.biz_content == null){
			String nullString = "";
			if(!StringUtils.isEmpty(out_trade_no)){
				nullString += ",\"out_trade_no\":\""+out_trade_no+"\"";
			}
			if(!StringUtils.isEmpty(refund_amount)){
				nullString += ",\"refund_amount\":\""+refund_amount+"\"";
			}
			if(!StringUtils.isEmpty(refund_reason)){
				nullString += ",\"refund_reason\":\""+refund_reason+"\"";
			}
			if(!StringUtils.isEmpty(out_request_no)){
				nullString += ",\"out_request_no\":\""+out_request_no+"\"";
			}
			if(nullString.length()>0){
				nullString = nullString.substring(1);
			}
			return "{"+nullString+"}";
		}
		return this.biz_content;
	}

	
}
