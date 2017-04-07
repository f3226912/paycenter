package cn.gdeng.paycenter.enums;
/**
 * 结算用户类型
 * @author xiaojun
 */
public enum UserTypeEnum {

	NSY("nsy", "农商友"), 
	NSY_PAY("nsy_pay", "农商友"), 
	GYS("gys", "供应商"),
	NPS("nps", "农批商"), 
	MARKET("market", "市场"), 
	PLAT("plat", "谷登平台"), 
	NST_CAR("nst_car", "车主"), 
	NST_GOOD("nst_goods", "货主"), 
	LOGIS("logis", "物流公司");

	private String code;

	private String name;

	private UserTypeEnum(String code, String name) {
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
		UserTypeEnum[] values = UserTypeEnum.values();
		for (UserTypeEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
}
