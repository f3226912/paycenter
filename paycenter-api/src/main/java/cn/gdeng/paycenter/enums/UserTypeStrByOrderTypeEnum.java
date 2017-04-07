package cn.gdeng.paycenter.enums;
/**
 * 根据订单类型得到(代付订单) 用户类型 收款方
 * @author xiaojun
 *
 */
public enum UserTypeStrByOrderTypeEnum {

	NPS("1", "农批商"), 
	NSY("2", "供应商"), 
	GDPT("4", "谷登平台"), 
	WLGS("21", "物流公司"),
	HYDD("22","车主");

	private String code;

	private String name;

	private UserTypeStrByOrderTypeEnum(String code, String name) {
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
		UserTypeStrByOrderTypeEnum[] values = UserTypeStrByOrderTypeEnum.values();
		for (UserTypeStrByOrderTypeEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
}
