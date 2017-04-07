package cn.gdeng.paycenter.enums;
/**
 * POS清分用户类型
 * @author wj
 */
public enum PosUserTypeEnum {

	GDNP("1", "谷登农批"), 
	NST("2", "农速通"), 
	NSY("3", "农商友"), 
	CDGYS("4", "产地供应商"),
	NPY("5", "门岗");

	private String code;

	private String name;

	private PosUserTypeEnum(String code, String name) {
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
		PosUserTypeEnum[] values = PosUserTypeEnum.values();
		for (PosUserTypeEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
}
