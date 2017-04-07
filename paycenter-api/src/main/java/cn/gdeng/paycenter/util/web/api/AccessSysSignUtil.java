package cn.gdeng.paycenter.util.web.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.enums.KeyTypeEnum;

/**
 * 接入系统签名工具类
 *
 */
public class AccessSysSignUtil {
	
	private final static String encoding = "utf-8";
	
	/**
	 * 验签
	 * @param paramMap
	 * @param sign
	 * @param asce
	 * @return
	 */
	public static boolean verifySign(Map<String , String> paramMap,String sign,AccessSysConfigEntity asce){
		String link = createLinkString(paraFilter(paramMap));
		if(asce.getKeyType().equals(KeyTypeEnum.RSA.getWay())){
			return RsaUtil.verify(link, sign, asce.getSysPublicKey(), encoding);
		}else if(asce.getKeyType().equals(KeyTypeEnum.MD5.getWay())){
			return MD5.verify(link, sign, asce.getMd5Key(), encoding);
		}
		return false;
	}
	
	public static String sign(Map<String , String> paramMap,String keyType,String privateKey){
		String link = createLinkString(paraFilter(paramMap));
		return sign(link,keyType,privateKey);
	}
	
	public static String sign(String content,String keyType,String privateKey){
		if(keyType.equals(KeyTypeEnum.RSA.getWay())){
			return RsaUtil.sign(content, privateKey, encoding);
		}else if(keyType.equals(KeyTypeEnum.MD5.getWay())){
			return MD5.sign(content, privateKey, encoding);
		}
		return "";
	}
	
	
	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("singType") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
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
