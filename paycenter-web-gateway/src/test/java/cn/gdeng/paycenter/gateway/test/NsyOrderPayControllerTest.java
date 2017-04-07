package cn.gdeng.paycenter.gateway.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.gateway.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.Des3Request;
import cn.gdeng.paycenter.util.web.api.Des3Response;

public class NsyOrderPayControllerTest {

	//private static String URL = "http://10.17.1.203:8082/nsyAlipay/signParam";
	private static String URL = "http://10.17.1.201:8082/nsyAlipay/signParam";
	//private static String URL = "http://localhost:8080/gd-api/nsyAlipay/signParam";
	
	@Test
	public void test1单个订单() throws Exception{
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("orderNo", "792016000285000");
		//p1.put("title", "大白菜");
		p1.put("orderTime", "2016-12-13");
		p1.put("payerUserId", "177336");
		p1.put("payeeUserId", "33961");
		p1.put("logisticsUserId", "237777");
		p1.put("totalAmt", "2646");
		p1.put("payAmt", "2646");
		List<Map> list = new ArrayList<>();
		list.add(p1);
		String orderInfos = JSON.toJSONString(list);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderInfos", orderInfos);
		String param = Des3Request.encode(JSON.toJSONString(paramMap));
		Map<String,String> params = new HashMap<>();
		params.put("param", param);

		String str = HttpUtil.httpClientPost(URL, params);
		System.out.println(Des3Response.decode(str));
		
	}
	
	@Test
	public void test单个订单() throws Exception{
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("orderNo", "412016000216002");
		p1.put("title", "大白菜");
		p1.put("orderTime", "2016-11-09 11:11:11");
		p1.put("payerUserId", "225");
		p1.put("payeeUserId", "226");
		p1.put("logisticsUserId", "227");
		p1.put("totalAmt", "0.01");
		p1.put("payAmt", "0.01");
		p1.put("reParam", "{\"h\":\"5\"}");
		List<Map> list = new ArrayList<>();
		list.add(p1);
		String orderInfos = JSON.toJSONString(list);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderInfos", orderInfos);
		String param = Des3Request.encode(JSON.toJSONString(paramMap));
		Map<String,String> params = new HashMap<>();
		params.put("param", param);

		String str = HttpUtil.httpClientPost(URL, params);
		System.out.println(Des3Response.decode(str));
		
	}
	
	@Test
	public void test多个订单() throws Exception{
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("orderNo", "412016000216001");
		p1.put("title", "大白菜");
		p1.put("orderTime", "2016-11-09 11:11:11");
		p1.put("payerUserId", "225");
		p1.put("payeeUserId", "226");
		p1.put("logisticsUserId", "227");
		p1.put("totalAmt", "0.01");
		p1.put("payAmt", "0.01");
		p1.put("reParam", "{\"h\":\"5\"}");
		
		Map<String, String> p2 = new HashMap<String, String>();
		p2.put("orderNo", "412016000216002");
		p2.put("title", "大白菜");
		p2.put("orderTime", "2016-11-09 11:11:11");
		p2.put("payerUserId", "225");
		p2.put("payeeUserId", "226");
		p2.put("logisticsUserId", "227");
		p2.put("totalAmt", "0.01");
		p2.put("payAmt", "0.01");
		p2.put("reParam", "{\"h\":\"5\"}");
		List<Map> list = new ArrayList<>();
		list.add(p1);
		list.add(p2);
		String orderInfos = JSON.toJSONString(list);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderInfos", orderInfos);
		String param = Des3Request.encode(JSON.toJSONString(paramMap));
		Map<String,String> params = new HashMap<>();
		params.put("param", param);

		String str = HttpUtil.httpClientPost(URL, params);
		System.out.println(Des3Response.decode(str));
		
	}
}
