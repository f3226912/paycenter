package cn.gdeng.paycenter.gateway.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.gdeng.paycenter.dto.pay.PayNotifyResponse;

public class BeanToMap {

	public static Map<String, Object> transBean2Map(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
		}
		return map;
	}

	public static void main(String[] args) {
		PayNotifyResponse person = new PayNotifyResponse();
		person.setAppKey("nst");
		person.setOrderNo("1111111111");
		System.out.println(BeanToMap.transBean2Map(person));
	}

}
