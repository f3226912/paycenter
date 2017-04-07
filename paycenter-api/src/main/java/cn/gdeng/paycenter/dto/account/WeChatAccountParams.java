package cn.gdeng.paycenter.dto.account;

import java.io.Serializable;
import java.util.List;

/**
 * 微信 对账请求参数DTO
 *
 */
public class WeChatAccountParams    implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3999429047089913856L;

	private String appid;
	
	private String bill_date;
	
	private String bill_type="ALL";
	
	private String mch_id;
	
	private String nonce_str;
	
	private String sign;
	
	private String sign_type;
	
	private String tar_type;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getTar_type() {
		return tar_type;
	}

	public void setTar_type(String tar_type) {
		this.tar_type = tar_type;
	}
	
	
	
}
