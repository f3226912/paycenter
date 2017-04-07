package cn.gdeng.paycenter.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 支付方式
 */
public enum PayWayEnum {

	WEIXIN_APP("WEIXIN_APP", "微信APP支付"),

	ALIPAY_APP("ALIPAY_APP", "支付宝APP支付"),
	
	ALIPAY_H5("ALIPAY_H5", "支付宝H5支付"),

	PINAN("PINAN", "平安银行"),
	
	NNCCB("NNCCB","南宁建行"),
	
	GXRCB("GXRCB","广西农信"),
	
	WANGPOS("WANGPOS","旺POS");

	/** 描述 */
	private String way;

	private PayWayEnum(String way) {
		this.way = way;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}
	
	/** 描述 */
    private String desc;
    
    private PayWayEnum(String way,String desc) {
        this.desc = desc;
        this.way = way;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Map<String, Map<String, Object>> toMap() {
    	PayWayEnum[] ary = PayWayEnum.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
        for (int num = 0; num < ary.length; num++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = ary[num].name();
            map.put("desc", ary[num].getDesc());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List toList() {
    	PayWayEnum[] ary = PayWayEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static PayWayEnum getEnum(String name) {
    	PayWayEnum[] arry = PayWayEnum.values();
        for (int i = 0; i < arry.length; i++) {
            if (arry[i].name().equalsIgnoreCase(name)) {
                return arry[i];
            }
        }
        return null;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List getWayList(String way) {
    	PayWayEnum[] ary = PayWayEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
        	if(ary[i].way.equals(way)){
        		Map<String, String> map = new HashMap<String, String>();
                map.put("desc", ary[i].getDesc());
                map.put("name", ary[i].name());
                list.add(map);
        	}
        }
        return list;
    }

    /**
     * 取枚举的json字符串
     *
     * @return
     */
    public static String getJsonStr() {
    	PayWayEnum[] enums = PayWayEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (PayWayEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }
    
    
	public static String getNameByCode(String code){
		PayWayEnum[] values = PayWayEnum.values();
		for(PayWayEnum val : values){
			if(val.getWay().equals(code)){
				return val.getDesc();
			}
		}
		return null;
	}


}
