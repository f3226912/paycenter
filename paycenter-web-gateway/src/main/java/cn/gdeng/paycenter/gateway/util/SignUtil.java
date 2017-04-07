package cn.gdeng.paycenter.gateway.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.util.web.api.MD5;

public class SignUtil {

    public static String sign(Map<String, String> paramValues,List<String> ignoreParamNames, String secret) {
        return sign(createLinkString(paraFilter(paramValues,ignoreParamNames)),secret);
    }
    
    public static String sign(String content,String secret){
		return MD5.sign(content, secret, "utf-8");
	}

	public static Map<String, String> paraFilter(Map<String, String> sArray,List<String> ignoreParamNames) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		if(ignoreParamNames == null || ignoreParamNames.size() <= 0){
			return result;
		}		
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);			
			if(isContainIgnore(key,ignoreParamNames)){
				continue;
			}			
			result.put(key, value);
		}
		return result;
	}
	
	private static boolean isContainIgnore(String key,List<String> ignoreParamNames){
		boolean flag = false;
		for(String ignore : ignoreParamNames){
			if (key.equalsIgnoreCase(ignore)) {
				flag = true;
				break;
			}			
		}		
		return flag;
	}
	
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	} 
    
}


