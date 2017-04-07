package cn.gdeng.paycenter.enums;

/**
 * 对账枚举
* @author DavidLiang
* @date 2016年12月28日 下午4:50:03
 */
public enum CheckStatusEnum {
	
	CHECK_STATUS_SUCCESS("1", "对账成功"),
	
	CHECK_STATUS_FAIL("2", "对账失败");
	
	private String code;

	private String name;

	private CheckStatusEnum(String code, String name) {
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
		CheckStatusEnum[] values = CheckStatusEnum.values();
		for (CheckStatusEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}

}
