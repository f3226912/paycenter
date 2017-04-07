package cn.gdeng.paycenter.enums;

public enum WeChatCertEnum {
	 
	/**
	 * 农速通车主
	 */
	NST_CAR("nst_car", "apiclient_cert_nst_car.p12"),
	/**
	 * 农速通货主
	 */
	NST_GOODS("nst_goods", "apiclient_cert_nst_goods.p12"),
	/**
	 * 农速通物流
	 */
	NST_FARE("nst_fare", "apiclient_cert_nst_fare.p12");
	
	/**
	 * appKey
	 */
	private String code;
	
	/**
	 * 证书名称
	 */
	private String name;
	
	
	private WeChatCertEnum(String code, String name) {
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
	public static String getNameByCode(String code){
		WeChatCertEnum[] values = WeChatCertEnum.values();
		for(WeChatCertEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
}
