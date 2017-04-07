package cn.gdeng.paycenter.util.server.sign;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.gdeng.paycenter.dto.account.TradeQueryResponseDto;

/**
 * 签名工具类
 * 对象转map
 * @author zhangnf20161109
 *
 */
public class ConvertUtil {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh");
	/**忽略字段**/
	private final static String KEY_VERSION = "version";
	/**簽名字段**/
	private final static String KEY_SIGN = "sign";
	/** 
	 * 使用Introspector进行转换 objectToMap
	 */ 
	@SuppressWarnings("unchecked")
	public static Map<String, Object> objectToMap(Object obj) throws Exception { 
		if(obj == null) return null;  
		
		if(obj instanceof Map){
			return (Map<String, Object>)obj;
		}
        Map<String, Object> map = new HashMap<String, Object>();   
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {    
            String key = property.getName();    
            if (key.compareToIgnoreCase("class") == 0) {   
                continue;  
            }  
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;  
            map.put(key, value);  
        }
        return map;  
    }
	/**
	 * 對象添加簽名信息
	 * @param obj 被簽名對象
	 * @param sign 簽名值
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void addSign(Object obj,String sign) throws Exception { 
		if(obj == null) return;  
		
		if(obj instanceof Map){
			Map<String, Object> mapTmp = (Map<String, Object>)obj;
			mapTmp.put(KEY_SIGN, sign);
		}
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {    
            String key = property.getName();    
            if (key.compareToIgnoreCase("class") == 0) {   
                continue;  
            }
            if(key.equals(KEY_SIGN)){
            	property.getWriteMethod().invoke(obj, sign);
            }
        }
    }
	
	/**
	 * 签名数据map转字符串，按照字符升序排列，空值补充null
	 * @param map
	 * @return
	 */
	public static <K, V> String getMapValues(Map<K,V> map){
		if(null == map){
			return null;
		}
		map.remove(KEY_VERSION);
		map.remove(KEY_SIGN);
		Iterator<Entry<K,V>> i = map.entrySet().iterator();    
		List<String> list = new ArrayList<String>();
		while(i.hasNext()){
           Map.Entry<K,V> m = i.next();
           V value= m.getValue();
           if(null != value){
        	   if(value instanceof Date){
        		   list.add(format.format(value));
        	   }else{
        		   list.add(value.toString());
        	   }
        	   
           }else{
        	   list.add("null");
           }
           if(!i.hasNext()){
        	   break;
           }
        }
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for(String str : list){
			sb.append(str);
		}
		return sb.toString();
	}
	public static void main(String[] args) throws Exception{
		TradeQueryResponseDto dto = new TradeQueryResponseDto();
		String str = ConvertUtil.getMapValues(ConvertUtil.objectToMap(dto));
		System.out.println(str);
	}
}
