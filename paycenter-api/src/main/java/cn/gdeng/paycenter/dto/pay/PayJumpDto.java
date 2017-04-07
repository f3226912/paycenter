package cn.gdeng.paycenter.dto.pay;

import java.io.Serializable;

public class PayJumpDto implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 第三方地址
	 */
	private String url;
	
	/**
	 * 支付方式 
	 */
	private String payType;
	
	/**
	 * 微信支付参数字符串
	 */
	private String jsonStr;
	
	/**
	 * 编码10000成功
	 */
	private Integer respCode;
	
	private String respMsg;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getRespCode() {
		return respCode;
	}

	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	
}
