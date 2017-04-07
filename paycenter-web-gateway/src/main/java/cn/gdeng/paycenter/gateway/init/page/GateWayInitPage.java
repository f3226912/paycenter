package cn.gdeng.paycenter.gateway.init.page;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class GateWayInitPage {
	
	private static final String UTF_8 = "utf-8";
	
	
	/**
	 * 初始化支付页面参数转map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> initPageParam(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<String, String>();
		String version = getString_UrlDecode_UTF8("version");
		paramMap.put("version", version);
		String appKey = getString_UrlDecode_UTF8("appKey"); // 企业支付KEY
		paramMap.put("appKey", appKey);
		String orderNo = getString_UrlDecode_UTF8("orderNo"); // 订单编号
		paramMap.put("orderNo", orderNo);
		String timeOut = getString_UrlDecode_UTF8("timeOut"); // 订单超时时间
		paramMap.put("timeOut", timeOut);
		String title = getString_UrlDecode_UTF8("title"); // 主题（订单名称）
		paramMap.put("title", title);
		String sign = getString_UrlDecode_UTF8("sign"); // 签名
		paramMap.put("sign", sign);
		String keyType = getString_UrlDecode_UTF8("keyType"); // 签名方式
		paramMap.put("keyType", keyType);
		String orderTime = getString_UrlDecode_UTF8("orderTime"); // 下单时间
		paramMap.put("orderTime", orderTime);
		String payerUserId = getString_UrlDecode_UTF8("payerUserId"); // 付款方用户id
		paramMap.put("payerUserId", payerUserId);
		String payerAccount = getString_UrlDecode_UTF8("payerAccount"); // 付款方账号
		paramMap.put("payerAccount", payerAccount);
		String payerName = getString_UrlDecode_UTF8("payerName"); // 付款方姓名
		paramMap.put("payerName", payerName);
		String payeeUserId = getString_UrlDecode_UTF8("payeeUserId"); // 收款方用户ID
		paramMap.put("payeeUserId", payeeUserId);
		String payeeAccount = getString_UrlDecode_UTF8("payeeAccount"); // 收款方账号
		paramMap.put("payeeAccount", payeeAccount);
		String payeeName = getString_UrlDecode_UTF8("payeeName"); // 收款方姓名
		paramMap.put("payeeName", payeeName);
		String totalAmt = getString_UrlDecode_UTF8("totalAmt"); // 订单金额
		paramMap.put("totalAmt", totalAmt);
		String payAmt = getString_UrlDecode_UTF8("payAmt"); // 交易金额
		paramMap.put("payAmt", payAmt);
		String returnUrl = getString_UrlDecode_UTF8("returnUrl"); // 同步通知URL
		paramMap.put("returnUrl", returnUrl);
		String notifyUrl = getString_UrlDecode_UTF8("notifyUrl"); // 后台异步通知URL
		paramMap.put("notifyUrl", notifyUrl);
		String reParam = getString_UrlDecode_UTF8("reParam"); // 公用回传参数
		paramMap.put("reParam", reParam);
		String requestIp = getString_UrlDecode_UTF8("requestIp"); // 请求ip
		paramMap.put("requestIp", requestIp);
		
		String payerMobile = getString_UrlDecode_UTF8("payerMobile"); // 买家手机号
		paramMap.put("payerMobile", payerMobile);
		
		String payeeMobile = getString_UrlDecode_UTF8("payeeMobile"); // 卖家手机号
		paramMap.put("payeeMobile", payeeMobile);
		
		String requestNo = getString_UrlDecode_UTF8("requestNo"); // 请求号
		paramMap.put("requestNo", requestNo);
		
		String payCount = getString_UrlDecode_UTF8("payCount"); // 支付笔数
		paramMap.put("payCount", payCount);
		
		String orderInfos = getString_UrlDecode_UTF8("orderInfos"); // 合并付款用到
		paramMap.put("orderInfos", orderInfos);
		
		String logisticsUserId = getString_UrlDecode_UTF8("logisticsUserId"); // 合并付款用到
		paramMap.put("logisticsUserId", logisticsUserId);
		
		String sumPayAmt = getString_UrlDecode_UTF8("sumPayAmt"); // 合并付款用到
		paramMap.put("sumPayAmt", sumPayAmt);
		
		//错误信息
		String respCode = getString_UrlDecode_UTF8("respCode"); // 错误码
		paramMap.put("respCode", respCode);
		String respMsg = getString_UrlDecode_UTF8("respMsg"); //
		paramMap.put("respMsg", respMsg);
		
		String hasWeixinPay = getString_UrlDecode_UTF8("hasWeixinPay"); //
		paramMap.put("hasWeixinPay", hasWeixinPay);
		return paramMap;
	}
	
	
	public static Map<String, String> initRefundMap(HttpServletRequest request) {
		Map<String, String> param = new HashMap<>();
		param.put("payCenterNumber", request.getParameter("payCenterNumber"));
		param.put("refundRequestNo", request.getParameter("refundRequestNo"));
		param.put("refundAmt", request.getParameter("refundAmt"));
		param.put("refundReason", request.getParameter("refundReason"));
		param.put("orderNo", request.getParameter("orderNo"));
		param.put("appKey", request.getParameter("appKey"));
		param.put("refundUserId", request.getParameter("refundUserId"));
		param.put("sellerRefundAmt", request.getParameter("sellerRefundAmt"));
		param.put("platRefundAmt", request.getParameter("platRefundAmt"));
		param.put("logisRefundAmt", request.getParameter("logisRefundAmt"));
		param.put("sign", request.getParameter("sign"));
		return param;
	}
	
	public static String getString_UrlDecode_UTF8(String key) {
		try {
			return URLDecoder.decode(getString(key), UTF_8);
		} catch (Exception e) {
			return "";
		}
	}
	
	protected static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static String getString(String name) {
		return getString(name, null);
	}

	public static String getString(String name, String defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr == null || "".equals(resultStr) || "null".equals(resultStr) || "undefined".equals(resultStr)) {
			return defaultValue;
		} else {
			return resultStr;
		}
	}

}
