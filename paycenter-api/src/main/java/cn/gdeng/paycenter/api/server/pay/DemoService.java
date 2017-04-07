package cn.gdeng.paycenter.api.server.pay;

import java.util.Map;

import cn.gdeng.paycenter.util.web.api.ApiResult;

public interface DemoService {

	/**
	 * 测试
	 */
	public ApiResult<String> getDemo(String demo) throws Exception;
	
	
	public String getDemo1(String demo) throws Exception;
	
//	public ApiResult<Object> sign() throws Exception;
//	
//	public ApiResult<Object> batchSign(Map<String,Object> OparamMap) throws Exception;
//	
//	public ApiResult<Object> batchValSign(Map<String,Object> OparamMap) throws Exception;
//	
//	public ApiResult<Object> batchValSignTime() throws Exception;
	
}