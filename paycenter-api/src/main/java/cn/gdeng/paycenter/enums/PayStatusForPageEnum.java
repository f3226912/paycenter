package cn.gdeng.paycenter.enums;

/**
 * 付款状态枚举
 * 
 * @author DavidLiang
 * @date 2016年12月28日 上午10:45:29
 */
public enum PayStatusForPageEnum {

	WAITING_PAYMENT("0", "待付款"),

	SUCCESSFUL_PAYMENT("1", "付款成功");

	private String code;

	private String name;

	private PayStatusForPageEnum(String code, String name) {
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
		PayStatusForPageEnum[] values = PayStatusForPageEnum.values();
		for (PayStatusForPageEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}

}
