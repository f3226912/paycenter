package cn.gdeng.paycenter.dto.alipay;

import java.util.HashMap;
import java.util.Map;

public class AlipayAccountRequest extends AlipayRequest {

	private static final long serialVersionUID = 1L;
	
	private String service="account.page.query";
	
	private String page_no="1";//页号
	
	/**
	 * 账务查询开始时间 格式为yyyy-MM-dd HH:mm:ss。
	 * 开始时间不能大于当前时间和查询结束时间，并且与账务查询结束时间的间隔不能大于1天。
	 * 开始时间最早为当前时间前3年。
	 */
	private String gmt_start_time;
	
	/**
	 * 账务查询结束时间 格式为yyyy-MM-dd HH:mm:ss。
	 */
	private String gmt_end_time;
	
	/**
	 * 交易流水号
	 */
	private String trade_no;

	/**
	 * 订单号
	 */
	private String merchant_out_order_no;
	
	/**
	 * 分页大小小于等于5000的正整数。为空或者大于5000时，默认为5000。
	 */
	private String page_size;
	
	/**
	 * 交易类型代码 6001
	 */
	private String trans_code="6001";
	
	private String appKey;
	
	@Override
	public void setService(String service) {
		this.service = service;
		
	}

	public String getService() {
		return service;
	}

	public String getPage_no() {
		return page_no;
	}

	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}

	public String getGmt_start_time() {
		return gmt_start_time;
	}

	public void setGmt_start_time(String gmt_start_time) {
		this.gmt_start_time = gmt_start_time;
	}

	public String getGmt_end_time() {
		return gmt_end_time;
	}

	public void setGmt_end_time(String gmt_end_time) {
		this.gmt_end_time = gmt_end_time;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getMerchant_out_order_no() {
		return merchant_out_order_no;
	}

	public void setMerchant_out_order_no(String merchant_out_order_no) {
		this.merchant_out_order_no = merchant_out_order_no;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public String getTrans_code() {
		return trans_code;
	}

	public void setTrans_code(String trans_code) {
		this.trans_code = trans_code;
	}


	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Override
	public Map<String, String> buildMap() {
		Map<String, String> para = new HashMap<>();
		para.put("partner", getPartner());
		para.put("_input_charset", get_input_charset());
		para.put("sign_type", getSign_type());
		para.put("sign", getSign());

		para.put("service", getService());
		para.put("page_no", getPage_no());
		para.put("gmt_start_time", getGmt_start_time());
		para.put("gmt_end_time", getGmt_end_time());
		para.put("trade_no", getTrade_no());
		para.put("merchant_out_order_no", getMerchant_out_order_no());

		para.put("page_size", getPage_size());
		para.put("trans_code", getTrans_code());
		// 去除空值
		Map<String, String> result = new HashMap<String, String>();

		for (String key : para.keySet()) {
			String value = para.get(key);
			if (value == null || value.equals("")) {
				continue;
			}
			result.put(key, value);
		}
		return result;

	}
	
}
