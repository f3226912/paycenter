package cn.gdeng.paycenter.gateway.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.gdeng.paycenter.gateway.util.HttpUtil;

public class OrderPayControlerTest {

	//private static String URL = "http://localhost:8580/gd-m/nstFare/signParam";
	
	private static String URL = "http://m.gdeng.cn/nstFare/signParam";
	
	@Test
	public void test加密参数(){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderNo", "201611210023");
		paramMap.put("orderTime", "2016-11-11 10:11:22");

		paramMap.put("payerUserId", "228");
		paramMap.put("sourceId", "1234567890");
		paramMap.put("payeeUserId", "226");
		paramMap.put("totalAmt", "111");
		paramMap.put("payAmt", "0.01");
		
		String str = HttpUtil.httpClientPost(URL, paramMap);
		System.out.println(str);
	}

	//private static String URL = "http://10.17.1.203:8311/";
	
	@Test
	public void test刷卡创建订单(){
		Map<String,String> params = new HashMap<>();
		params.put("payCenterNumber", "2016121254960002");
		params.put("transType", "1");
		params.put("orderNo", "200611120001");
		params.put("orderAmt", "111");
		params.put("payAmt", "111");
		params.put("bankCardNo", "611233333333333");
		params.put("posClientNo", "11111111");
		params.put("transDate", "2016-12-12 12:12:12");
		params.put("transNo", "123456789001");
		params.put("payChannelCode", "NNCCB");
		params.put("gdBankCardNo", "6112339999999");
		String str = HttpUtil.httpClientPost(URL+"demo/nsyCreate", params);
		System.out.println(str);
	}
}
