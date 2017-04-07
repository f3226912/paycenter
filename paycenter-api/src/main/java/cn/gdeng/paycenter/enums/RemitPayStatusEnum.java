package cn.gdeng.paycenter.enums;
/**
 * 结算 代付状态
 * @author xiaojun
 *
 */
public enum RemitPayStatusEnum {

	DFK("0", "待付款"), 
	FKCG("1", "付款成功");

	private String code;

	private String name;

	private RemitPayStatusEnum(String code, String name) {
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
		RemitPayStatusEnum[] values = RemitPayStatusEnum.values();
		for (RemitPayStatusEnum val : values) {
			if (val.getCode().equals(code)) {
				return val.getName();
			}
		}
		return null;
	}
}
