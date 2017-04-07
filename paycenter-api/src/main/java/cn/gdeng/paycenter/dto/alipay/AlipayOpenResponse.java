package cn.gdeng.paycenter.dto.alipay;

import java.io.Serializable;

public class AlipayOpenResponse implements Serializable {

	/**
	 * 10000 成功 否则失败
	 */
	private String code;

	private String msg;

	private String sub_code;

	private String sub_msg;

	private String body;
	
	private static final long serialVersionUID = 1L;
	
	public boolean isSuccess(){
		return "10000".equals(code);
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public String getSub_code() {
		return sub_code;
	}

	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}

	public String getSub_msg() {
		return sub_msg;
	}

	public void setSub_msg(String sub_msg) {
		this.sub_msg = sub_msg;
	}
	
	

}
