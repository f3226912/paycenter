package com.gd.paytype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.io.StringBufferInputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;
import cn.gdeng.paycenter.dto.account.WeChatAccountParams;
import cn.gdeng.paycenter.dto.account.WeChatAccountRequest;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.wechat.WeChatPreResultDto;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.server.pay.component.WeChatAccountComponent;
import cn.gdeng.paycenter.server.pay.impl.WeChatServiceImpl;
import cn.gdeng.paycenter.server.pay.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.MD5;
import cn.gdeng.paycenter.util.web.api.StringUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;
import cn.gdeng.paycenter.util.web.api.WeChatParseUtil;
import cn.gdeng.paycenter.util.web.api.WeChatSignUtil;

public class WeChatTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 统一下单测试
	 */
	@Test
	public void prePay() {
		PayGateWayDto dto = new PayGateWayDto();
		dto.setPayAmt(0.01);
		dto.setTotalAmt(0.01);
		dto.setTitle("APP支付测试");
		dto.setRequestIp("127.0.0.1");
		dto.setPayCenterNumber("TEFD343435353535");

		ThirdPayConfigEntity config = new ThirdPayConfigEntity();
		//config.setParamJson(
		//		"{\"mch_id\":\"1438179102\",\"appid\":\"wx0b8dbef79e93b7f7\",\"notifyUrl\":\"http://121.15.169.11:18823/alipay/notifyUrl\"}");
		config.setParamJson(
				"{\"mch_id\":\"1440503302\",\"appid\":\"wx9f97b35be11242f5\",\"notifyUrl\":\"http://121.15.169.11:18823/alipay/notifyUrl\"}");
		config.setKeyType("MD5");
		//config.setMd5Key("jtb0adbn4jadfvyiuukvfppkp0bar0hh");
		config.setMd5Key("jbk0zjhn4jadfvyibb3vfpvyr0bar0jj");
		config.setAppKey("nst_car");

		// ThirdPayConfigEntity config =
		// thirdPayConfigService.queryByTypeSub(dto.getAppKey(),
		// PayWayEnum.WEIXIN_APP.getWay(), SubPayType.WECHAT.APP);
		WeChatConfig cf;
		try {
			cf = WeChatConfigUtil.buildConfig(config);

			// 打包参数
			Map<String, String> temp = WeChatServiceImpl.buildRequestParameters(dto, cf);
			// 生成随机字符串
			//WeChatParseUtil.buildNonceStr(temp);
			// 生成MD5摘要
			//WeChatSignUtil.buildRequestSign(temp, cf);
			// 参数转xml
			String xmlParams = WeChatParseUtil.parseMapToStr(temp);
			// logger.info("调用微信支付交易入参:url:" + cf.getGateWay() + " params:" +
			// xmlParams);
System.out.println(xmlParams);
			String xmlResult = HttpUtil.httpClientPost(cf.getGateWay(), xmlParams);
			if (StringUtils.isEmpty(xmlResult)) {
				throw new BizException(MsgCons.C_50000, "调用微信支付接口服务失败");
			}
			// xml转对象
			Map<String, String> resultMap = new HashMap<String, String>();
			WeChatPreResultDto prepayDto = WeChatParseUtil.buildResultObj(xmlResult, resultMap,
					WeChatPreResultDto.class);
			// logger.info("调用微信支付交易返回数据:" + JSON.toJSONString(prepayDto));
			System.out.println(JSON.toJSONString(prepayDto));

			// 验签
			if (!WeChatSignUtil.tryVerifySign(resultMap, cf)) {
				throw new BizException(MsgCons.C_20008, "微信统一下单接口返回数据效验失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 下载对账单测试
	 */
	@Test
	public void downloadbillTest() {
		WeChatAccountComponent c = new WeChatAccountComponent();
		AccountRquestDto dto = new AccountRquestDto();
		dto.setAppKey("nst_car");
		dto.setBillDate("20170213");
		WeChatAccountRequest req = null;
		try {
			c.validateDto(dto);
			/** test start **/
			req = new WeChatAccountRequest();

			WeChatAccountParams params = new WeChatAccountParams();
			params.setAppid("wx0b8dbef79e93b7f7");
			params.setMch_id("1438179102");
			params.setBill_date(dto.getBillDate());
			params.setNonce_str(StringUtil.getRandomString(16));
			// 把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			WeChatConfig ac = new WeChatConfig();
			ac.setKey("jbk0zjhn4jadfvyibb3vfpvyr0bar0jj");
			sParaTemp.put("appid", params.getAppid());
			sParaTemp.put("mch_id", params.getMch_id());
			sParaTemp.put("bill_date", params.getBill_date());
			sParaTemp.put("bill_type", params.getBill_type());
			sParaTemp.put("nonce_str", params.getNonce_str());
			sParaTemp=WeChatSignUtil.buildRequestSign(sParaTemp, ac);
			req.setParams(params);

			/** test end **/

			String requestStr = c.createXmlParams(req.getParams());
			logger.info("调用微信对账接口,地址:" + req.getUrl() + " xml参数：" + requestStr);
			String reponseText = HttpUtil.httpClientPost(req.getUrl(), requestStr);
			if (StringUtils.isEmpty(reponseText)) {
				throw new BizException(MsgCons.C_50000, "调用微信下载对账单接口服务失败");
			}
			/** test start **/
			reponseText = "交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,代金券或立减优惠金额,微信退款单号,商户退款单号,退款金额,代金券或立减优惠退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率\n"
					+ "`2014-11-1016：33：45,`wx2421b1c4370ec43b,`10000100,`0,`1000,`1001690740201411100005734289,`1415640626,`085e9858e3ba5186aafcbaed1,`MICROPAY,`SUCCESS,`CFT,`CNY,`0.01,`0.0,`0,`0,`0,`0,`,`,`被扫支付测试,`订单额外描述,`0,`0.60%\n"
					+ "`2014-11-1016：46：14,`wx2421b1c4370ec43b,`10000100,`0,`1000,`1002780740201411100005729794,`1415635270,`085e9858e90ca40c0b5aee463,`MICROPAY,`SUCCESS,`CFT,`CNY,`0.01,`0.0,`0,`0,`0,`0,`,`,`被扫支付测试,`订单额外描述,`0,`0.60%\n"
					+ "总交易单数,总交易额,总退款金额,总代金券或立减优惠退款金额,手续费总金额\n" + "`2,`0.02,`0.0,`0.0,`0\n";
			/** test end **/
			logger.info("调用阿里对账接口，返回报文:" + reponseText);
			if (reponseText.startsWith(req.getReponseStart())) {
				c.setFailDetail(req, reponseText);
			} else {
				c.parseReponseText(req, reponseText);
			}
		} catch (BizException e) {
			logger.error("调用阿里对账失败" + e.getMsg(), e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("调用阿里对账失败" + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Test
	public void rrrrdom() {
		String str = StringUtil.getRandomString(32);
		System.out.println(str);

		BigDecimal big = new BigDecimal(0);
		big = big.add(new BigDecimal(111));
		big.setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println(big);

		Object j = "";
		if (j instanceof String) {
			System.out.println("1");

		} else {
			System.out.println(2);
		}
		String xml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[No Bill Exist]]></return_msg><error_code><![CDATA[20002]]></error_code></xml>";
		String newXml = xml.substring(0, 1000);
		System.out.println(newXml);
	}

	@Test
	public void testWeixinPay() {
		PayOrderDTO payOrderDTO = new PayOrderDTO();
		payOrderDTO.setAppid("wx0b8dbef79e93b7f7");
		payOrderDTO.setPartnerid("1438179102");
		payOrderDTO.setPackagee("Sign=WXPay");
		payOrderDTO.setPrepayid("wx2017021509235494a58f8c910920808569");

		String randomStr = StringUtil.getRandomString(16);
		long timestamp = new Date().getTime();
		StringBuffer str = new StringBuffer();
		str.append("appid=" + payOrderDTO.getAppid());
		str.append("&noncestr=" + randomStr);
		str.append("&package=" + payOrderDTO.getPackagee());
		str.append("&partnerid=" + payOrderDTO.getPartnerid());
		str.append("&prepayid=" + payOrderDTO.getPrepayid());
		str.append("&timestamp=" + timestamp);

		// ThirdPayConfigEntity config =
		// thirdPayConfigService.queryByTypeSub(dto.getAppKey(),
		// PayWayEnum.WEIXIN_APP.getWay(), SubPayType.WECHAT.APP);
		// WeChatConfig cf = WeChatConfigUtil.buildConfig(config);
		String sign = MD5.sign(str.toString(), "&key=jbk0zjhn4jadfvyibb3vfpvyr0bar0jj", "UTF-8").toUpperCase();

		StringBuffer payUrlPre = new StringBuffer("weixin://app/");
		payUrlPre.append(payOrderDTO.getAppid());
		payUrlPre.append("/pay/?");
		StringBuffer payUrl = new StringBuffer();
		payUrl.append("nonceStr=");
		payUrl.append(randomStr);
		payUrl.append("&package=Sign%3DWXPay");
		payUrl.append("&partnerId=");
		payUrl.append(payOrderDTO.getPartnerid());
		payUrl.append("&prepayId=");
		payUrl.append(payOrderDTO.getPrepayid());
		payUrl.append("&timeStamp=");
		payUrl.append(timestamp);
		// String sign2 = MD5.sign(payUrl.toString(),
		// "&key=jbk0zjhn4jadfvyibb3vfpvyr0bar0jj" , "UTF-8").toUpperCase();
		payUrl.append("&sign=");
		payUrl.append(sign);
		payUrl.append("&signType=SHA1");
		System.out.println("payUrl=" + payUrlPre + payUrl);
	}

	/**
	 * 退款测试
	 * 
	 * @throws Exception
	 * @throws ClientProtocolException
	 */
	@Test
	public void testRefund() throws ClientProtocolException, Exception {
		WeChatServiceImpl ws = new WeChatServiceImpl();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("appid", "wx0b8dbef79e93b7f7");// appid
		parameters.put("mch_id", "1438179102");// 商户号
		WeChatParseUtil.buildNonceStr(parameters);
		// 在notify_url中解析微信返回的信息获取到 transaction_id，此项不是必填，详细请看上图文档
		// parameters.put("transaction_id", "微信支付订单中调用统一接口后微信返回的
		// transaction_id");
		parameters.put("out_trade_no", "TEFD343435353535");// 订单好
		parameters.put("out_refund_no", "TEFD3434353535350");// 我们自己设定的退款申请号，约束为UK
		parameters.put("total_fee", "1");// 单位为分
		parameters.put("refund_fee", "1");// 单位为分
		parameters.put("op_user_id", "sys");// 操作人员,默认为商户账号
		// String sign = createSign("utf-8", parameters);
		WeChatConfig cf = new WeChatConfig();
		cf.setKey("jbk0zjhn4jadfvyibb3vfpvyr0bar0jj");
	    WeChatSignUtil.buildRequestSign(parameters, cf);

		String reuqestXml = WeChatParseUtil.parseMapToStr(parameters);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File("E:\\download\\cert\\apiclient_cert.p12"));// 放退款证书的路径
		try {
			keyStore.load(instream, "1438179102".toCharArray());
		} finally {
			instream.close();
		}

		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1438179102".toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		try {

			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");// 退款接口

			System.out.println("executing request" + httpPost.getRequestLine());
			StringEntity reqEntity = new StringEntity(reuqestXml);
			// 设置类型
			reqEntity.setContentType("text/html");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			StringBuilder sb = new StringBuilder();
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						System.out.println(text);
						sb.append(text);
					}

				}
				EntityUtils.consume(entity);
				System.out.println(sb.toString());
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	
}
