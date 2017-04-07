package cn.gdeng.paycenter.util.web.api;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;

public class WeChatConfigUtil {
	public static WeChatConfig buildConfig(ThirdPayConfigEntity cf) throws BizException {
		try {
			WeChatConfig c = new WeChatConfig();
			String paramJson = cf.getParamJson();
			JSONObject json = (JSONObject) JSON.parse(paramJson);

			String mch_id = json.getString("mch_id");
			String notifyUrl = json.getString("notifyUrl");
			String appId = json.getString("appid");

			if (StringUtils.isEmpty(mch_id) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(appId)) {
				throw new BizException(MsgCons.C_20006, MsgCons.M_20006);

			}
			c.setMch_id(mch_id);
			c.setNotify_url(notifyUrl);
			c.setAppid(appId);
			setKey(c, cf);
			return c;
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20006, MsgCons.M_20006);
		}

	}

	private static void setKey(WeChatConfig cf, ThirdPayConfigEntity config) {
		cf.setKey(config.getMd5Key());
		cf.setSign_type(config.getKeyType());
	}

	/**
	 * 微信统一下单接口参数
	 * 
	 * @date 2017年2月9日
	 */
	public static class WeChatConfig {

		private String appid; // 应用ID

		private String mch_id;// 商户号

		private String device_info;// 设备号

		private String nonce_str;// 随机字符串

		private String sign; // 签名

		private String sign_type;// 签名类型

		private String body;// 商品描述

		private String detail;// 商品详情

		private String attach;// 附加数据

		private String out_trade_no;// 商户订单号

		private String fee_type = "CNY"; // 货币类型

		private Integer total_fee;// 总金额 单位为分

		private String spbill_create_ip;// 用户端实际ip

		private String time_start;// 交易起始时间

		private String time_expire;// 交易结束时间

		private String goods_tag;// 商品标记;

		private String notify_url;// 通知地址

		private String trade_type="APP";// 交易类型

		private String limit_pay;// 指定支付方式

		private String gateWay = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 统一下单接口地址
		
		private String billWay="https://api.mch.weixin.qq.com/pay/downloadbill"; //下载对账单接口地址
		
		private String refundWay="https://api.mch.weixin.qq.com/secapi/pay/refund"; //申请退款地址
		
		private String refundQueryWay="https://api.mch.weixin.qq.com/pay/refundquery";//退款查询地址
		
		private String key;//加密key
		
		private String keyType="MD5";//加密类型
		
		private String input_charset="utf-8";
		/**
		 * 是否需要对sign_type进行签名 
		 *  true sign_type一起加密 
		 *  false 过滤掉sign_type 不参与加密
		 */
		private boolean useSignType=false;

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

		public String getSign_type() {
			return sign_type;
		}

		public void setSign_type(String sign_type) {
			this.sign_type = sign_type;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getAttach() {
			return attach;
		}

		public void setAttach(String attach) {
			this.attach = attach;
		}

		public String getOut_trade_no() {
			return out_trade_no;
		}

		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}

		public String getFee_type() {
			return fee_type;
		}

		public void setFee_type(String fee_type) {
			this.fee_type = fee_type;
		}

		public Integer getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(Integer total_fee) {
			this.total_fee = total_fee;
		}

		public String getSpbill_create_ip() {
			return spbill_create_ip;
		}

		public void setSpbill_create_ip(String spbill_create_ip) {
			this.spbill_create_ip = spbill_create_ip;
		}

		public String getTime_start() {
			return time_start;
		}

		public void setTime_start(String time_start) {
			this.time_start = time_start;
		}

		public String getTime_expire() {
			return time_expire;
		}

		public void setTime_expire(String time_expire) {
			this.time_expire = time_expire;
		}

		public String getGoods_tag() {
			return goods_tag;
		}

		public void setGoods_tag(String goods_tag) {
			this.goods_tag = goods_tag;
		}

		public String getTrade_type() {
			return trade_type;
		}

		public void setTrade_type(String trade_type) {
			this.trade_type = trade_type;
		}

		public String getLimit_pay() {
			return limit_pay;
		}

		public void setLimit_pay(String limit_pay) {
			this.limit_pay = limit_pay;
		}

		public String getGateWay() {
			return gateWay;
		}

		public void setGateWay(String gateWay) {
			this.gateWay = gateWay;
		}

		public String getNotify_url() {
			return notify_url;
		}

		public void setNotify_url(String notify_url) {
			this.notify_url = notify_url;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key =key;
		}

		public String getKeyType() {
			return keyType;
		}

		public void setKeyType(String keyType) {
			this.keyType = keyType;
		}

		public boolean isUseSignType() {
			return useSignType;
		}

		public void setUseSignType(boolean useSignType) {
			this.useSignType = useSignType;
		}

		public String getInput_charset() {
			return input_charset;
		}

		public void setInput_charset(String input_charset) {
			this.input_charset = input_charset;
		}

		public String getBillWay() {
			return billWay;
		}

		public void setBillWay(String billWay) {
			this.billWay = billWay;
		}

		public String getRefundWay() {
			return refundWay;
		}

		public void setRefundWay(String refundWay) {
			this.refundWay = refundWay;
		}

		public String getRefundQueryWay() {
			return refundQueryWay;
		}

		public void setRefundQueryWay(String refundQueryWay) {
			this.refundQueryWay = refundQueryWay;
		}

		
	}
}
