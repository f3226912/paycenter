package cn.gdeng.paycenter.dto.alipay;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AlipayOpenRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public AlipayOpenRequest(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		setTimestamp(sdf.format(new Date()));
	}

	private String gateWay = "https://openapi.alipay.com/gateway.do";
	
	private String app_id;
	
	private String charset="utf-8";
	
	private String sign_type;
	
	private String sign;
	
	private String timestamp;
	
	private String version="1.0";
	
	private String notify_url;
	
	private String return_url;

	public String getGateWay() {
		return gateWay;
	}

	public void setGateWay(String gateWay) {
		this.gateWay = gateWay;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public abstract String getMethod();

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public abstract String getBiz_content();

	public Map<String,String> buildMap(){
		Map<String, String> para = new HashMap<>();
		para.put("app_id", this.getApp_id());
		para.put("method", this.getMethod());
		para.put("charset", this.getCharset());
		para.put("timestamp", this.getTimestamp());
		para.put("version", this.getVersion());
		para.put("biz_content", this.getBiz_content());
		para.put("version", this.getVersion());
		para.put("return_url", this.getReturn_url());
		para.put("notify_url", this.getNotify_url());
		para.put("sign", this.getSign());
		para.put("sign_type", this.getSign_type());
		return para;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
}
