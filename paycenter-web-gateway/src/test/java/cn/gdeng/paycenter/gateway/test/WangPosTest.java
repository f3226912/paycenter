package cn.gdeng.paycenter.gateway.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.org.apache.commons.codec.digest.DigestUtils;
import com.mongodb.util.Hash;

import cn.gdeng.paycenter.dto.pos.WangPosPayNotifyDto;
import cn.gdeng.paycenter.gateway.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;
import cn.gdeng.paycenter.util.web.api.Des3Request;
import cn.gdeng.paycenter.util.web.api.Des3Response;
import cn.gdeng.paycenter.util.web.api.MD5;

public class WangPosTest {

	private static String URL = "http://localhost:8080/paycenter-web-gateway/wangPos/";
	
	//private static String URL = "http://10.17.1.203:8311/wangPos/";
	
	@Test
	public void test创建一个订单() throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("version", "222");
		paramMap.put("appKey", "nsy");
		paramMap.put("timeOut", "5");
		paramMap.put("requestIp", "8.8.8.8");
		
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("orderNo", "412016000216100");
		p1.put("title", "大白菜");
		p1.put("orderTime", "2016-11-09 11:11:11");
		p1.put("payerUserId", "225");
		p1.put("payeeUserId", "226");
		p1.put("logisticsUserId", "227");
		p1.put("totalAmt", "0.01");
		p1.put("payAmt", "0.01");
		p1.put("requestNo", "2");
		p1.put("payCount", "2");
		List<Map> list = new ArrayList<>();
		list.add(p1);
		//String orderInfos = JSON.toJSONString(list);
		paramMap.put("orderInfos", list);
		Map<String,String> param = new HashMap<>();
		String paramStr = Des3Request.encode(JSON.toJSONString(paramMap));
		param.put("param",paramStr);
		System.out.println(paramStr);
		System.out.println(Des3Request.decode(paramStr));
		String str = HttpUtil.httpClientPost(URL+"create", param);
		System.out.println(Des3Response.decode(str));
	}
	
	@Test
	public void test创建2个订单() throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("version", "222");
		paramMap.put("appKey", "nsy");
		paramMap.put("timeOut", "5");
		paramMap.put("requestIp", "8.8.8.8");
		paramMap.put("force", "2");
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("orderNo", "412016000216108");
		p1.put("title", "大白菜a");
		p1.put("orderTime", "2016-11-09 11:11:11");
		p1.put("payerUserId", "225");
		p1.put("payeeUserId", "226");
		p1.put("logisticsUserId", "227");
		p1.put("totalAmt", "0.01");
		p1.put("payAmt", "0.01");
		p1.put("requestNo", "2");
		p1.put("payCount", "2");
		Map<String, String> p2 = new HashMap<String, String>();
		p2.put("orderNo", "412016000216109");
		p2.put("title", "大白菜");
		p2.put("orderTime", "2016-11-09 11:11:11");
		p2.put("payerUserId", "225");
		p2.put("payeeUserId", "226");
		p2.put("logisticsUserId", "227");
		p2.put("totalAmt", "0.01");
		p2.put("payAmt", "0.01");
		p2.put("requestNo", "2");
		p2.put("payCount", "2");
		
		List<Map> list = new ArrayList<>();
		list.add(p1);
		list.add(p2);
		
		paramMap.put("orderInfos", list);
		Map<String,String> param = new HashMap<>();
		String paramStr = Des3Request.encode(JSON.toJSONString(paramMap));
		param.put("param",paramStr);
		System.out.println(paramStr);
		System.out.println(Des3Request.decode(paramStr));
		String str = HttpUtil.httpClientPost(URL+"create", param);
		System.out.println(Des3Response.decode(str));
	}
	
	@Test
	public void test异步支付通知() throws Exception{
		Map<String,String> params =new HashMap<>();
		params.put("out_trade_no", "2016122120596985");
		params.put("pay_fee", "2");
		params.put("trade_status", "PAY");
		params.put("time_end", "20161220111111");
		params.put("cashier_trade_no", "20111111111123335");
		params.put("pay_type", "1001");

	
		String str = HttpUtil.httpClientPost(URL+"asyncPayNotify", params);
		System.out.println(str);
	}
	
	@Test
	public void test同步支付通知() throws Exception{
		Map<String,String> params =new HashMap<>();
		params.put("payCenterNumber", "2016122010713916");
		params.put("tradeAmt", "0.02");
		params.put("tradeStatus", "PAY");
		params.put("payTime", "20161220111111");
		params.put("tradeNo", "20111111111123334");
		params.put("payType", "1001");

		String paramStr = Des3Request.encode(JSON.toJSONString(params));
		Map<String,String> param = new HashMap<>();
		param.put("param", paramStr);
		String str = HttpUtil.httpClientPost(URL+"asyncPayNotify", param);
		System.out.println(Des3Response.decode(str));
	}
	
	public static void main(String[] args) {
		Map<String,String> pa = new HashMap<>();
		pa.put("attach", "attach");
		pa.put("bp_id", "56c68ecdfa0bab61c6daadf0");
		pa.put("cashier_trade_no", "10001835102016122800000007");
		pa.put("check_fee", "0");
		pa.put("device_en", "cde07b3b");
		pa.put("input_charset", "UTF-8");
		pa.put("mcode", "183510");
		pa.put("operator_name", "匿名用户");
		pa.put("out_trade_no", "2016122804420099");
		pa.put("pay_fee", "99999899");
		
		pa.put("pay_info", "支付成功");
		pa.put("pay_type", "1001");
		pa.put("sign", "e1492c831600e5e3fc6b38e2578dd3d7");
		pa.put("sign_type", "MD5");
		pa.put("thirdDiscount", "0");
		pa.put("thirdMDiscount", "0");
		pa.put("time_create", "20161228092136");
		
		pa.put("time_end", "20161228092139");
		pa.put("total_fee", "99999899");
		pa.put("trade_status", "PAY");
		String text = AccessSysSignUtil.createLinkString(AccessSysSignUtil.paraFilter(pa));
		String key = "ZfOklc7cWmCimUYzmTaWXTvUR4FRaCip";
		String sign = MD5.sign(text,"&key="+key, "UTF-8");
		System.out.println(text+"&key="+key);
		System.out.println(DigestUtils.md5Hex(text+"&key="+key));
		System.out.println(sign);
		System.out.println(MD5.verify(text, pa.get("sign"), "&key="+key, "utf-8"));
	}
}
