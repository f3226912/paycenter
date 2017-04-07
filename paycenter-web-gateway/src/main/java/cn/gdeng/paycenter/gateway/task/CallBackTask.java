package cn.gdeng.paycenter.gateway.task;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gdeng.paycenter.gateway.util.HttpUtil;

public class CallBackTask implements Runnable {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, String> paramMap;

	public CallBackTask(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public void run() {
		try{
			String ret = HttpUtil.httpClientPost(paramMap.get("returnUrl"), paramMap);
			logger.info("回调返回:" + ret);
		}catch(Exception e){
			logger.error("回调失败:" + e);
		}
	}
}
