package cn.gdeng.paycenter.enums;

public enum AppKeyOrderTypeEnum {

    // 订单类型 1:农商友采购订单 2:农批商采购订单 3:农商友6+1订单 4:服务订单 21信息费订单 22货运订单
	NST_CAR("nst_car", "21","信息费订单"),
	
	NST_GOOD("nst_goods","22", "货运订单"),
	
	GYS("gys","4", "服务订单"),
	
	NSY("nsy", "1","农商友采购订单"),
	
	NSY_FARE("nst_fare","22", "货运订单");
	
	private String appKey;
	
	private String code;
	
	private String name;
	
	private AppKeyOrderTypeEnum(String appKey,String code, String name) {
		this.appKey = appKey;
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

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	public static String getCodeByAppKey(String appKey){
		AppKeyOrderTypeEnum[] values = AppKeyOrderTypeEnum.values();
		for(AppKeyOrderTypeEnum val : values){
			if(val.getAppKey().equals(appKey)){
				return val.getCode();
			}
		}
		return null;
	}

	
}
