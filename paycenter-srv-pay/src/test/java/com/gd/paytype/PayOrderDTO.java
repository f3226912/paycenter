package com.gd.paytype;

import java.io.Serializable;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mpan
 * @date 2017年2月14日 下午5:24:54
 */
public class PayOrderDTO implements Serializable {

	private static final long serialVersionUID = 8345829462554232296L;
	private String appid; // 应用ID
	private String partnerid; // 商户号
	private String prepayid; // 预支付交易会话ID
	private String packagee; // 扩展字段
	private String timestamp; // 时间戳
	private String sign; // 签名

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getPackagee() {
		return packagee;
	}

	public void setPackagee(String packagee) {
		this.packagee = packagee;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
