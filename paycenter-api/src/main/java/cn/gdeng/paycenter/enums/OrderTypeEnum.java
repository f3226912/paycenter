package cn.gdeng.paycenter.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单类型
 * @author xiaojun
 */
public enum OrderTypeEnum {

	NSYCGDD("1", "农商友采购订单"), 
	NPSCGDD("2","农批商采购订单"), 
/*	NSYCX("3", "农商友6+1订单"), */
	FWDD("4", "服务订单"),
	INFORMATION_FEES("21", "信息费订单"),
	FREIGHT("22", "货运订单");

	private String code;

	private String name;

	private OrderTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getNameByCode(String code) {
		OrderTypeEnum[] values = OrderTypeEnum.values();
		for (OrderTypeEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public static List toList() {
		 OrderTypeEnum[] ary = OrderTypeEnum.values();
	        List list = new ArrayList();
	        for (int i = 0; i < ary.length; i++) {
	            Map<String, String> map = new HashMap<String, String>();
	            map.put("code", ary[i].getCode());
	            map.put("name", ary[i].getName());
	            list.add(map);
	        }
	        return list;
	    }
}
