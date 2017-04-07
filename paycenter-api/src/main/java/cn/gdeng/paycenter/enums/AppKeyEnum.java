package cn.gdeng.paycenter.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易表中,接入系统来源
 * 
 * @author xiaojun
 */
public enum AppKeyEnum {

	NST_GOODS("nst_goods", "农速通-货主"), 
	NST_CAR("nst_car", "农速通-车主"), 
	NST_COMPANY("nst_company", "物流公司APP"), 
	GYS("gys","供应商"), 
	NPS("nsy_pa", "农商友"), 
	NSY("nst_fare", "农速通-运费");
	

	private String code;

	private String name;

	private AppKeyEnum(String code, String name) {
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
		AppKeyEnum[] values = AppKeyEnum.values();
		for (AppKeyEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
	
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	    public static List toList() {
		 AppKeyEnum[] ary = AppKeyEnum.values();
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
