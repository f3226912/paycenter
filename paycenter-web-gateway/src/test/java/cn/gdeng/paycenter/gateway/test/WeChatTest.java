package cn.gdeng.paycenter.gateway.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.dto.wechat.WeChatPreResultDto;
import cn.gdeng.paycenter.gateway.util.HttpUtil;

public class WeChatTest {
	private String url = "http://127.0.0.1:8080/paycenter-web-gateway/";
	@Test
	public void xmlTest() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("name", "zlb");
		map.put("age", "5");
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
		}
		sb.append("</xml>");
		System.out.println(sb.toString());
	}

	@Test
	public void xmlTest2() throws Exception {
		String xmlstr = "<xml>" 
			            + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "<appid><![CDATA[wx2421b1c4370ec43b]]></appid>"
						+ "<mch_id><![CDATA[10000100]]></mch_id>" + "<nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>"
						+ "<sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>"
						+ "<result_code><![CDATA[SUCCESS]]></result_code>"
						+ "<prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>"
						+ "<trade_type><![CDATA[APP]]></trade_type>" 
						+ "</xml>";

		Document dc = DocumentHelper.parseText(xmlstr);
		// System.out.println(dc.asXML());

		Element root = dc.getRootElement();
		// System.out.println(root.getName());

		List<Element> list = root.elements();
		WeChatPreResultDto dto = new WeChatPreResultDto();
		BeanInfo beanInfo = Introspector.getBeanInfo(WeChatPreResultDto.class);
		PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
		if (proDescrtptors != null && proDescrtptors.length > 0) {
			for (Iterator<Element> its = list.iterator(); its.hasNext();) {
				Element chileEle = its.next();
				for (PropertyDescriptor propDesc : proDescrtptors) {
					if (StringUtils.equals(propDesc.getName(), chileEle.getName())) {
						Method writeme = propDesc.getWriteMethod();  
						writeme.invoke(dto, chileEle.getText());  
					}
				}
			}
		}
		System.out.println(JSON.toJSONString(dto));
	}
	
	/**
	 * 异步回调测试
	 * @throws Exception
	 */
	@Test
	public void notifyUrlTest() throws Exception {
		url += "/wechat/notifyUrl";
		String requestStr="<xml>"+
						  "<appid><![CDATA[wx2421b1c4370ec43b]]></appid>"+
						  "<attach><![CDATA[支付测试]]></attach>"+
						  "<bank_type><![CDATA[CFT]]></bank_type>"+
						  "<fee_type><![CDATA[CNY]]></fee_type>"+
						  "<is_subscribe><![CDATA[Y]]></is_subscribe>"+
						  "<mch_id><![CDATA[10000100]]></mch_id>"+
						  "<nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>"+
						  "<openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>"+
						  "<out_trade_no><![CDATA[1409811653]]></out_trade_no>"+
						  "<result_code><![CDATA[SUCCESS]]></result_code>"+
						  "<return_code><![CDATA[SUCCESS]]></return_code>"+
						  "<sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>"+
						  "<sub_mch_id><![CDATA[10000100]]></sub_mch_id>"+
						  "<time_end><![CDATA[20140903131540]]></time_end>"+
						  "<total_fee>1</total_fee>"+
						  "<trade_type><![CDATA[JSAPI]]></trade_type>"+
						  "<transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>"+
						  "</xml>";
		String xmlRes=HttpUtil.httpClientPost(url, requestStr);
		System.out.println(xmlRes);

		
	}
	
	@Test
	public void test11(){
		String s="20140903131540";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dt;
		try {
			dt = sdf.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
