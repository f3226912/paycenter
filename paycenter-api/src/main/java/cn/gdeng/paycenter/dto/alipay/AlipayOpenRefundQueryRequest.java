package cn.gdeng.paycenter.dto.alipay;

import org.apache.commons.lang3.StringUtils;

public class AlipayOpenRefundQueryRequest extends AlipayOpenRequest{

	private static final long serialVersionUID = 1L;

	private String method="alipay.trade.fastpay.refund.query";
	
	private String out_trade_no;

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
