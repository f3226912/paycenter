package cn.gdeng.paycenter.constant;

public interface CacheKeyContant {
	
	//接入系统缓存 map key
	public static String ACCESS_SYS_CONFIG_MAP_KEY = "PAYCENTER_ACCESS_SYS_CONFIG_MAP_KEY";
	
	//第三方系统支付配置缓存 map key
	public static String THIRD_PAY_CONFIG_MAP_KEY = "PAYCENTER_THIRD_PAY_CONFIG_MAP_KEY";
	
	//appKey对应的支付方式缓存 map key
	public static String PAY_TYPE_CONFIG_MAP_KEY = "PAYCENTER_PAY_TYPE_CONFIG_MAP_KEY";

}
