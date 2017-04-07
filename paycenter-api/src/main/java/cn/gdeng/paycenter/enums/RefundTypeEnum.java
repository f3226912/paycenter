package cn.gdeng.paycenter.enums;

/**
 * 退款类型枚举
* @author DavidLiang
* @date 2016年12月27日 下午4:30:51
 */
public enum RefundTypeEnum {

	SYSTEM("1", "系统退款"), //原路返回
	
	ARTIFICIAL("2", "人工退款");  //清算
	
	private String code;
	
	private String name;

	private RefundTypeEnum(String code, String name) {
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
		RefundTypeEnum[] values = RefundTypeEnum.values();
		for (RefundTypeEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
	
}
