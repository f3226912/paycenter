package cn.gdeng.paycenter.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DJB
 * @version 创建时间：2016年11月25日 上午10:28:17 
 * 付款方来源
 */

public enum PayerOrderSourceEnum {

	/**
	 * 
	 * 订单类型 付 收
	 * 
	 * 农商友采购订单 农商友 农批商
	 * 
	 * 农批商采购订单 农批商 供应商
	 * 
	 * 服务订单 供应商 谷登平台
	 * 
	 * 信息费订单 车主 物流公司
	 * 
	 * 货运订单 货主 车主
	 **/

	PAYER_SOURCE_NSYCGDD("1", "农商友"), 
	PAYER_SOURCE_NPSCGDD("2", "农批商"),
	PAYER_SOURCE_FWDD("4", "供应商"), 
	PAYER_SOURCE_INFORMATION_FEES("21", "车主"), 
	PAYER_SOURCE_FREIGHT("22", "货主");

	private String code;
	private String name;


	private PayerOrderSourceEnum(String code, String name) {
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
		PayerOrderSourceEnum[] values = PayerOrderSourceEnum.values();
		for (PayerOrderSourceEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public static List toList() {
		 PayerOrderSourceEnum[] ary = PayerOrderSourceEnum.values();
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
