package cn.gdeng.paycenter.util.web.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import cn.gdeng.paycenter.enums.KeyTypeEnum;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;

public class WeChatSignUtil {

	/**
	 * 生成请求摘要值
	 * 
	 * @param sParaTemp
	 * @param config
	 * @return
	 */
	public static Map<String, String> buildRequestSign(Map<String, String> params, WeChatConfig config) {

		// 获取signType
		String signType = config.getKeyType();
		// 除去数组中的空值和签名参数
		Map<String,String> filterParams = paraFilter(params, config.isUseSignType());
		String prestr = createLinkString(filterParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String sign = "";
		if (StringUtils.equals(signType, KeyTypeEnum.MD5.getWay())) {
			sign = MD5.sign(prestr, "&key=" + config.getKey(), config.getInput_charset()).toUpperCase();
		}

		// 签名结果与签名方式加入请求提交参数组中
		filterParams.put("sign", sign);
		return filterParams;
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

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray, boolean useSignType) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
				continue;
			} else if (!useSignType && key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 统一下单返回结果是否篡改验证
	 * 
	 * @param xmlResult
	 */
	public static boolean tryVerifySign(Map<String, String> sParaTemp, WeChatConfig config) {
		boolean isVerify = true;
		String signType = config.getKeyType();
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = paraFilter(sParaTemp, config.isUseSignType());

		String prestr = createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		if (StringUtils.equals(signType, KeyTypeEnum.MD5.getWay())) {
			isVerify = MD5.verify(prestr, sParaTemp.get("sign"), "&key="+config.getKey(), config.getInput_charset());
		}
		return isVerify;
	}

}
