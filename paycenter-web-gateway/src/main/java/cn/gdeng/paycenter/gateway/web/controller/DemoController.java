package cn.gdeng.paycenter.gateway.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.api.server.pay.DemoService;
import cn.gdeng.paycenter.api.server.pay.SynCacheService;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.gateway.base.BaseController;
import cn.gdeng.paycenter.util.web.api.ApiResult;


@Controller
@RequestMapping("test")
public class DemoController extends BaseController{	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Reference
	private DemoService demoService;
	
	@Reference
	private PayJobService payJobService;
	
	@Reference
	private SynCacheService synCacheService;	
	
	@RequestMapping("demo")
	@ResponseBody
	public Object demo(HttpServletRequest request) throws Exception {
		ApiResult<String> result = new ApiResult<String>();
		String age = request.getParameter("age");
		result = demoService.getDemo(age);
		return result;
	}
	
	
	@RequestMapping("syncache")
	@ResponseBody
	public Object syncache(HttpServletRequest request) throws Exception {
		ApiResult<String> result = new ApiResult<String>();
		try{		
			synCacheService.synCache();
			
//			Map<String,AccessSysConfigEntity> map = (Map<String,AccessSysConfigEntity>)synCacheService.readCache(CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY);
//			System.out.println(map);
			
			
		}catch(Exception e){
			result.withError(MsgCons.C_20000,MsgCons.M_20000);
		}
		return result;
	}
	
	@RequestMapping("readcache")
	@ResponseBody
	public Object getSyncache(String key){
		ApiResult<Object> result = new ApiResult<Object>();
		try{
			if(StringUtils.isEmpty(key)){
				return result;
			}
			Object obj = synCacheService.readCache(key);
			
//			Map<String,AccessSysConfigEntity> map = (Map<String,AccessSysConfigEntity>)synCacheService.readCache(CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY);
//			System.out.println(map);
			
			result.setResult(obj);
		}catch(Exception e){
			result.withError(MsgCons.C_20000,MsgCons.M_20000);
		}
		return result;
	}
	
	@RequestMapping("env")
	@ResponseBody
	public Object getEnv(HttpServletRequest request) throws Exception {
		ApiResult<String> result = new ApiResult<String>();	
		//payJobService.closeTrade();
		String env = synCacheService.getEnv();
		result.setResult(env);
		return result;
	}
	
	@RequestMapping("notify")
	@ResponseBody
	public Object notify(HttpServletRequest request) throws Exception {
		ApiResult<String> result = new ApiResult<String>();	
		//payJobService.closeTrade();
		result = demoService.getDemo("444");
		return result;
	}
	
//	/**
//	 * 签名实例
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("sign")
//	@ResponseBody
//	public Object sign(HttpServletRequest request) throws Exception {
//		return demoService.sign();
//	}
//	
//	/**
//	 * 批量签名
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("batchSign")
//	@ResponseBody
//	public Object batchSign(HttpServletRequest request) throws Exception {
//		long start = System.currentTimeMillis();
//		List<String> list = new ArrayList<String>();
////		list.add("bank_rate_config");
////		list.add("bill_check_detail");
////		list.add("bill_check_log");
////		list.add("bill_check_sum");
////		list.add("clear_detail");
////		list.add("fee_record");
////		list.add("market_bank_acc_info");
////		list.add("member_bankcard_info");
////		list.add("pay_log_record");
//		list.add("pay_trade");
////		list.add("pay_trade_extend");
////		list.add("pay_trade_pos");
////		list.add("pay_type");
////		list.add("pay_type_config");
////		list.add("pos_machine_config");
////		list.add("pos_pay_notify");
////		list.add("ref_clear_relate");
////		list.add("refund_record");
////		list.add("remit_record");
////		list.add("remit_record_error");
//		
//		for(String tableName: list){
//			Map<String,Object> OparamMap = new HashMap<String,Object>();
//			OparamMap.put("tableName", tableName);
//			 demoService.batchSign(OparamMap);
//		}
//		logger.debug(" 总更新耗时："+(System.currentTimeMillis()-start));
//		return "ok";
//	}
//
//	/**
//	 * 批量签名验证
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("batchValSign")
//	@ResponseBody
//	public Object batchValSign(HttpServletRequest request) throws Exception {
//		long start = System.currentTimeMillis();
//		List<String> list = new ArrayList<String>();
//		list.add("bank_rate_config");
//		list.add("bill_check_detail");
//		list.add("bill_check_log");
//		list.add("bill_check_sum");
//		list.add("clear_detail");
//		list.add("fee_record");
//		list.add("market_bank_acc_info");
//		list.add("member_bankcard_info");
//		list.add("pay_log_record");
//		list.add("pay_trade");
//		list.add("pay_trade_extend");
//		list.add("pay_trade_pos");
//		list.add("pay_type");
//		list.add("pay_type_config");
//		list.add("pos_machine_config");
//		list.add("pos_pay_notify");
//		list.add("ref_clear_relate");
//		list.add("refund_record");
//		list.add("remit_record");
//		list.add("remit_record_error");
//		
//		for(String tableName: list){
//			Map<String,Object> OparamMap = new HashMap<String,Object>();
//			OparamMap.put("tableName", tableName);
//			 demoService.batchValSign(OparamMap);
//		}
//		logger.debug(" 总验证耗时："+(System.currentTimeMillis()-start));
//		return "ok";
//	}
//	
//	/**
//	 * 批量签名验证性能测试
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("batchValSignTime")
//	@ResponseBody
//	public Object batchValSignTime(HttpServletRequest request) throws Exception {
//		 demoService.batchValSignTime();
//		return "ok";
//	}
}
