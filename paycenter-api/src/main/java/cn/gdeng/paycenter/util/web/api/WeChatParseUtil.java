package cn.gdeng.paycenter.util.web.api;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.MsgCons;

public class WeChatParseUtil {
	/**
	 * map转 xml 字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String parseMapToStr(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		sb.append("<xml>");
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append("<" + key + "><![CDATA[" + value + "]]></" + key + ">");
		}

		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 生成随机字符按串
	 * 
	 * @param temp
	 * @return
	 */
	public static String buildNonceStr(Map<String, String> temp) {
		String randomStr = StringUtil.getRandomString(16);
		temp.put("nonce_str", randomStr);
		return randomStr;
	}

	/**
	 * 转化返回结果对象
	 * 
	 * @param map
	 * @return
	 * @throws BizException
	 * @throws Exception
	 */
	public static <T> T buildResultObj(String xmlStr, Map<String, String> resultMap, Class<T> clazz)
			throws BizException {

		try {
			T dto = clazz.newInstance();
			Document dc= DocumentHelper.parseText(xmlStr);
			Element root = dc.getRootElement();

			@SuppressWarnings("unchecked")
			List<Element> list = root.elements();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();

			for (Iterator<Element> its = list.iterator(); its.hasNext();) {
				Element child = its.next();
				if (StringUtils.isNotEmpty(child.getText())) {
					for (PropertyDescriptor propDesc : proDescrtptors) {
						if (StringUtils.equals(propDesc.getName(), child.getName())) {
							resultMap.put(child.getName(), child.getText());
							Method method = propDesc.getWriteMethod();
							method.invoke(dto, child.getText());
						}
					}
				}
			}
			return dto;
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20040, e.getMessage());
		}
	}

}
