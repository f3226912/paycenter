package cn.gdeng.paycenter.dto.alipay;

import org.apache.commons.lang3.StringUtils;

public class AlipayOpenPaymentRequest extends AlipayOpenRequest{

	private static final long serialVersionUID = 1L;
	
	private String method="alipay.trade.wap.pay";
	
	/**
	 * 长度 128
	 */
	private String body;
	
	/**
	 * 长度 256
	 */
	private String subject;
	
	/**
	 * 长度 64
	 */
	private String out_trade_no;
	
	/**
	 * 超时  6h
	 */
	private String timeout_express;
	
	/**
	 * 长度 9
	 */
	private String total_amount;
	
	private String biz_content;


	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTimeout_express() {
		return timeout_express;
	}

	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	
	public void setMethod(String method) {
		this.method = method;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

	@Override
	public String getBiz_content() {
		if(this.biz_content == null){
			String nullString = "";
			if(!StringUtils.isEmpty(body)){
				nullString += ",\"body\":\""+getBody()+"\"";
			}
			if(!StringUtils.isEmpty(subject)){
				nullString += ",\"subject\":\""+getSubject()+"\"";
			}
			return "{\"out_trade_no\":\""+getOut_trade_no()+"\""
					+",\"timeout_express\":\""+getTimeout_express()+"\""
					+nullString
					+",\"total_amount\":\""+getTotal_amount()+"\"}";
		}
		return this.biz_content;
	}
	
	

}
