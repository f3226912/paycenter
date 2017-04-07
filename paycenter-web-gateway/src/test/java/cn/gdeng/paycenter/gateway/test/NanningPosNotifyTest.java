package cn.gdeng.paycenter.gateway.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.dto.pay.NanningPayNotifyDto;
import cn.gdeng.paycenter.gateway.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.MD5;
import cn.gdeng.paycenter.util.web.api.RsaUtil;

public class NanningPosNotifyTest {
	
	private static String URL = "http://localhost:8080/paycenter-web-gateway/nnccb/";
	
	//private static String URL = "http://10.17.1.193:8311/nnccb/";
	
	//private static String URL = "http://10.17.1.203:8311/nnccb/";
	

	
	@Test
	public void test正向刷卡(){
		NanningPayNotifyDto dto = new NanningPayNotifyDto();
		
		dto.setMachinenum("1201i785");
		dto.setMerchantnum("113420151310205");
		dto.setOrderfee("12300"); //单位 分 
		dto.setOrderno("522016000014802");
		dto.setPaycardno("1234567890");
		dto.setPayfee("12300");
		dto.setRatefee("10");
		dto.setTransdate("2016111");
		dto.setTranstime("111111");

		dto.setTransseqno("20161122001");
		dto.setTransype("0");
		dto.setPayresultcode("0000");
		dto.setPayresultmsg("刷卡失败");
		String reqdata = JSON.toJSONString(dto);
		String sign = signMsg(reqdata);
		Map<String,String> params = new HashMap<>();
		params.put("reqdata", reqdata);
		params.put("signmsg", sign);
		String msg = HttpUtil.httpClientPost(URL+"payNotify", params);
		System.out.println(msg);
	}
	
	@Test
	public void test逆向刷卡(){
		NanningPayNotifyDto dto = new NanningPayNotifyDto();
		
		dto.setMachinenum("1201i785");
		dto.setMerchantnum("113420151310205");
		dto.setOrderfee("120300"); //单位 分 
		//dto.setOrderno("922016000072260");
		dto.setPaycardno("1234567890");
		dto.setPayfee("121300");
		dto.setRatefee("100");
		dto.setTransdate("2016112");
		dto.setTranstime("111112");
		dto.setTransseqno("20161122003");
		dto.setPayresultcode("0000");
		dto.setTransype("1");
		String reqdata = JSON.toJSONString(dto);
		String sign = signMsg(reqdata);
		Map<String,String> params = new HashMap<>();
		params.put("reqdata", reqdata);
		params.put("signmsg", sign);
		String msg = HttpUtil.httpClientPost(URL+"payNotify", params);
		System.out.println(msg);
	}
	
	@Test
	public void test查询订单(){
		String machinenum = "1201i785";
		
		String reqdata = "{\"machinenum\":\""+machinenum+"\"}";
		String sign = signMsg(reqdata);
		Map<String,String> params = new HashMap<>();
		params.put("reqdata", reqdata);
		params.put("signmsg", sign);
		String msg = HttpUtil.httpClientPost(URL+"orderList", params);
		System.out.println(msg);
	}
	
	private static String signMsg(String data){
		
		
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOo/1pdonzkdLFy+EXrVFxDaONlt7P8Lgy6vITAjxSurXx0fK2MqcjhlP2qzRDpQATe397mZrbPQOuJE1+KfJBzE7xQz4hpaX+t/2IEmXjH6+LseTzsttLI2FmzmF5IB84TCEw62g1/B6jWxM3XT4gVSZYv6tZHZ2jf4UJeyjMR9AgMBAAECgYAR4Ac3DJPB3vkIprRcxC/7aDuYU+wW1kEPaevFaaVdHLmoVj2QVqfap9c8BCv7qyk8oDw2HIUFyKkFpS04LN6rcfGoYOi2RAeVG1IYM2i3t36WjPYTW75I4Qu2+BdLD+h7+ojo4QWh7E0NJEGEAHTbeVyUQv3yuIAhjibvqDkP3QJBAP36vrqhiLstb/ftGTff7Na+zrPfznpaJFZiWTssf6hcrRg4V6Rr+4mJAdKT3OBOJLMsTI4EvUySdWmseC4Xg8MCQQDsHOkto+tRzlkwBDTRhiWZBpPuhiLyBzyRTS9E1Rp6lCKatTkYnhMWfk6TL/B8xf7qQ90CNI5mILMIPimyL1K/AkAdzTBYIeAzZm9+/fk5jzLxN851WMuXm8AryrqBsQUBsm41K1dNWcZYKxVlqif+weyYgZgyCehUddMFJVGsxuGVAkEAn3FmhUuMPVH7KFGu2U6dVRj8DLbIImiAnh5hLTo7B0vBkneOdvFIMohYx0w4Ogn6engZZsPW5WBvhcF0pr8OQQJAB7+MWEcgC+ALXWHKL2ZsMd/WSsjkdWdUbBFpnHYfwvxacVjG+yvKQcPphGvBw1K2EdQuvmz1yoTmK8OHrXJ5qQ==";
		String sign = RsaUtil.sign(MD5.crypt(data).toLowerCase(), privateKey, "utf-8");
		return sign;
	}
	
	public static void main(String[] args) {
		String msg = "{\"charset\":\"UTF-8\",\"machinenum\":\"1201i988\",\"merchantnum\":\"943611041190001\",\"reserved\":\"\",\"version\":\"1.0\"}";
		System.out.println(signMsg(msg));
	}

}
