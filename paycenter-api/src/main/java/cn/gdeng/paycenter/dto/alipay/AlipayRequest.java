package cn.gdeng.paycenter.dto.alipay;

import java.io.Serializable;
import java.util.Map;

public abstract class AlipayRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	private String partner;
	
	private String _input_charset="utf-8";
	
	private String sign_type;
	
	private String sign;
	
	private String gateWay = "https://mapi.alipay.com/gateway.do";
	
	public abstract Map<String,String> buildMap();
	

	/**
	 * 服务名
	 * @param service
	 */
	public abstract void setService(String service);


	public String getPartner() {
		return partner;
	}


	public void setPartner(String partner) {
		this.partner = partner;
	}


	public String get_input_charset() {
		return _input_charset;
	}


	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}


	public String getSign_type() {
		return sign_type;
	}


	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getGateWay() {
		return gateWay;
	}


	public void setGateWay(String gateWay) {
		this.gateWay = gateWay;
	}
	
}
