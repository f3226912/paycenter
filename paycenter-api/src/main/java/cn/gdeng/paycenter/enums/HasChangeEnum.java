package cn.gdeng.paycenter.enums;

/**
 * 转结状态枚举
* @author DavidLiang
* @date 2016年12月27日 下午3:19:44
 */
public enum HasChangeEnum {
	IS_HAS_CHANGE("1", "已转结"),
	NOT_HAS_CHANGE("0", "未转结");
	
	private String code;

	private String name;
	
	private HasChangeEnum(String code, String name) {
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
		HasChangeEnum[] values = HasChangeEnum.values();
		for (HasChangeEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}

}
