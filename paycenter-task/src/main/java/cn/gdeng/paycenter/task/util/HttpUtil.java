package cn.gdeng.paycenter.task.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;



public final class HttpUtil {

	private HttpUtil() {
	}

	public static final String httpClientPost(String url) {
		String result = "";
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			client.executeMethod(getMethod);
			result = getMethod.getResponseBodyAsString();
		} catch (Exception e) {
		} finally {
			getMethod.releaseConnection();
		}
		return result;
	}

	public static final String httpClientPost(String url, ArrayList<NameValuePair> list) {
		String result = "";
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		try {
			NameValuePair[] params = new NameValuePair[list.size()];
			for (int i = 0; i < list.size(); i++) {
				params[i] = list.get(i);
			}
			postMethod.addParameters(params);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}
	
	public static final String httpClientPost(String url, Map<String,String> map) {
		String result = "";
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		try {			
			List<NameValuePair> list = new ArrayList <NameValuePair>();  	          
	        Set<String> keySet = map.keySet();  
	        for(String key : keySet) {  
	        	list.add(new NameValuePair(key, map.get(key)));  
	        }  
			
			NameValuePair[] params = new NameValuePair[list.size()];
			for (int i = 0; i < list.size(); i++) {
				params[i] = list.get(i);
			}
			postMethod.addParameters(params);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}
}
