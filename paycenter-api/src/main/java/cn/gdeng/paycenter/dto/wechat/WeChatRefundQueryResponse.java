package cn.gdeng.paycenter.dto.wechat;

import java.io.Serializable;

/**
 * 退款查询响应参数
 * @date 2017年2月20日
 */
public class WeChatRefundQueryResponse implements Serializable{

	private static final long serialVersionUID = -2234164773010456018L;

	public String return_code;
	public String return_msg;
	public String result_code;
	public String err_code;
	public String err_code_des;
	public String appid;
	public String mch_id;
	public String device_info;
	public String nonce_str;
	public String sign;
	public String transaction_id;
	public String out_trade_no;
	public Integer total_fee;
	public String fee_type;
	public Integer cash_fee;
	public Integer refund_count;
	public String out_refund_no_$n;
	public String refund_id_$n;
	public String refund_channel_$n;
	public String refund_account;
	public Integer refund_fee_$n;
	public Integer coupon_refund_fee_$n;
	public Integer coupon_refund_count_$n;
	public String coupon_refund_batch_id_$n_$m;
	public String coupon_refund_id_$n_$m;
	public Integer coupon_refund_fee_$n_$m;
	public String refund_status_$n;
	public String refund_recv_accout_$n;
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
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public Integer getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public Integer getCash_fee() {
		return cash_fee;
	}
	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}
	public Integer getRefund_count() {
		return refund_count;
	}
	public void setRefund_count(Integer refund_count) {
		this.refund_count = refund_count;
	}
	public String getOut_refund_no_$n() {
		return out_refund_no_$n;
	}
	public void setOut_refund_no_$n(String out_refund_no_$n) {
		this.out_refund_no_$n = out_refund_no_$n;
	}
	public String getRefund_id_$n() {
		return refund_id_$n;
	}
	public void setRefund_id_$n(String refund_id_$n) {
		this.refund_id_$n = refund_id_$n;
	}
	public String getRefund_channel_$n() {
		return refund_channel_$n;
	}
	public void setRefund_channel_$n(String refund_channel_$n) {
		this.refund_channel_$n = refund_channel_$n;
	}
	public String getRefund_account() {
		return refund_account;
	}
	public void setRefund_account(String refund_account) {
		this.refund_account = refund_account;
	}
	public Integer getRefund_fee_$n() {
		return refund_fee_$n;
	}
	public void setRefund_fee_$n(Integer refund_fee_$n) {
		this.refund_fee_$n = refund_fee_$n;
	}
	public Integer getCoupon_refund_fee_$n() {
		return coupon_refund_fee_$n;
	}
	public void setCoupon_refund_fee_$n(Integer coupon_refund_fee_$n) {
		this.coupon_refund_fee_$n = coupon_refund_fee_$n;
	}
	public Integer getCoupon_refund_count_$n() {
		return coupon_refund_count_$n;
	}
	public void setCoupon_refund_count_$n(Integer coupon_refund_count_$n) {
		this.coupon_refund_count_$n = coupon_refund_count_$n;
	}
	public String getCoupon_refund_batch_id_$n_$m() {
		return coupon_refund_batch_id_$n_$m;
	}
	public void setCoupon_refund_batch_id_$n_$m(String coupon_refund_batch_id_$n_$m) {
		this.coupon_refund_batch_id_$n_$m = coupon_refund_batch_id_$n_$m;
	}
	public String getCoupon_refund_id_$n_$m() {
		return coupon_refund_id_$n_$m;
	}
	public void setCoupon_refund_id_$n_$m(String coupon_refund_id_$n_$m) {
		this.coupon_refund_id_$n_$m = coupon_refund_id_$n_$m;
	}
	public Integer getCoupon_refund_fee_$n_$m() {
		return coupon_refund_fee_$n_$m;
	}
	public void setCoupon_refund_fee_$n_$m(Integer coupon_refund_fee_$n_$m) {
		this.coupon_refund_fee_$n_$m = coupon_refund_fee_$n_$m;
	}
	public String getRefund_status_$n() {
		return refund_status_$n;
	}
	public void setRefund_status_$n(String refund_status_$n) {
		this.refund_status_$n = refund_status_$n;
	}
	public String getRefund_recv_accout_$n() {
		return refund_recv_accout_$n;
	}
	public void setRefund_recv_accout_$n(String refund_recv_accout_$n) {
		this.refund_recv_accout_$n = refund_recv_accout_$n;
	}
	
	
}
