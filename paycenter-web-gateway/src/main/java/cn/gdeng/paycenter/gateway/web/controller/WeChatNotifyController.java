package cn.gdeng.paycenter.gateway.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.PayCallBackService;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.constant.WeChatTradeStatus;
import cn.gdeng.paycenter.dto.pay.PayNotifyResponse;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.util.web.api.PayLogUtil;

/**
 * 处理微信APP支付的通知
 * 
 * @date 2017年2月13日
 */
@Controller
@RequestMapping("wechat")
public class WeChatNotifyController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Reference
	private PayCallBackService payCallBackService;

	@Reference
	private AccessSysConfigService accessSysConfigService;

	@Reference
	private PayTradeService payTradeService;

	@Reference
	private PayLogRecordService payLogRecordService;

	private static final String CHART_SET = "UTF-8";

	/**
	 * 异步回调入口
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("notifyUrl")
	public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取微信POST过来反馈信息
		PayNotifyResponse res = new PayNotifyResponse();
		Map<String, String> params = buildSignParam(request);
		String payCenterNumber = params.get("out_trade_no");
		String tradeStatus = params.get("result_code");
		logger.info("进入微信异步回调接口：平台支付流水【" + payCenterNumber + "】-微信支付状态【" + tradeStatus + "】;微信异步入参:"
				+ JSON.toJSONString(params));
		try {

			// 如果是退款
			String out_refund_no = request.getParameter("out_refund_no");
			PayLogRecordEntity log = null;
			Long id = null;
			if (StringUtils.isNotEmpty(out_refund_no)) {
				// 申请退款接口，也会发异步通知
				log = PayLogUtil.buildPayReceiveLog(PayWayEnum.WEIXIN_APP.getWay(), payCenterNumber,
						"收到微信退款异步通知:" + JSON.toJSONString(params));
				id = payLogRecordService.addLog(log);
			} else {
				// 获取交易状态，交易结束不做处理
				log = PayLogUtil.buildPayReceiveLog(PayWayEnum.WEIXIN_APP.getWay(), payCenterNumber,
						"收到微信交易异步通知:" + JSON.toJSONString(params));
				id = payLogRecordService.addLog(log);
				if (StringUtils.equals(tradeStatus, WeChatTradeStatus.TRADE_SUCCESS)) {
					res = payCallBackService.payNotify(params, PayWayEnum.WEIXIN_APP.getWay(), payCenterNumber);
				}
			}

			if (id != 0) {
				log.setSend("返回给微信:success");
				log.setId(id);
				payLogRecordService.updateLog(log);
			}
		} catch (Exception e) {
			logger.info("微信异步通知返回fail,错误信息:" + e.getMessage(), e);
			response.getWriter().write(createResponseMsg(WeChatTradeStatus.RETURN_FAIL,""));
			return;
		}
		logger.info("结束微信异步回调接口：订单号【" + res != null ? res.getOrderNo()
				: "" + "】,平台支付流水【" + payCenterNumber + "】 微信异步通知返回success");
		response.getWriter().write(createResponseMsg(WeChatTradeStatus.RETURN_SUCCESS,WeChatTradeStatus.RETURN_MSG));
		return;
	}

	public Map<String, String> buildSignParam(HttpServletRequest request) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		ServletInputStream is = request.getInputStream();
		String resultStr = getStrRequest(is);
		Document doc = DocumentHelper.parseText(resultStr);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		for (Iterator<Element> its = list.iterator(); its.hasNext();) {
			Element child = its.next();
			if (StringUtils.isNotEmpty(child.getText())) {
				params.put(child.getName(), child.getText());
			}
		}
		return params;
	}

	/**
	 * 读取流
	 * 
	 * @param ls
	 * @return 字符串
	 * @throws Exception
	 */
	public static String getStrRequest(InputStream is) throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			stream.write(buffer, 0, len);
		}
		stream.close();
		is.close();
		return stream.toString(CHART_SET);
	}

	public static String createResponseMsg(String code, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<return_code><![CDATA[" + code + "]]></return_code>");
		sb.append(" <return_msg><![CDATA[" + msg + "]]></return_msg>");
		sb.append("</xml>");
		return sb.toString();
	}
	

}
