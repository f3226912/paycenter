package cn.gdeng.paycenter.dto.wechat;

import java.io.Serializable;

/**
 * 微信支付统一下单预支付返回结果
 * @date 2017年2月9日
 */
public class WeChatPreResultDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7305076271037545079L;
	
	/**
	 * 返回状态码
	 */
	private String return_code;
	
	/**
	 * 返回信息
	 */
	private String return_msg;
	
	/**
	 * 应用APPID
	 */
	private String appid;
	/**
	 * 商户号
	 */
	private String mch_id;
	/**
	 * 设备号
	 */
	private String device_info;
	/**
	 * 随机字符串
	 */
	private String nonce_str;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 业务结果
	 */
	private String result_code; 
	/**
	 * 错误代码
	 */
	private String err_code; 
	/**
	 * 错误代码描述
	 */
	private String err_code_des; 
	
	/**
	 * 交易类型
	 */
	private String trade_type;
	
	/**
	 * 预支付交易会话标识
	 */
	private String prepay_id;
	/**
	 * 编码10000成功
	 */
	private Integer respCode;
	
	/**
	 *描述
	 */
	private String respMsg;


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

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
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

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	
	
}
