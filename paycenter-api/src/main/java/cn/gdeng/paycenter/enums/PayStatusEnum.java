package cn.gdeng.paycenter.enums;

/**
 * 失败账单 
 * @author youj
 * @Date:2016年11月14日上午11:20:31
 */
public enum PayStatusEnum {
	
	DFK((byte)1, "待付款"),
	YFK((byte)2, "已付款"),
	YGB((byte)3, "已关闭"),
	YTK((byte)4, "已退款");
	private Byte code;
	
	private String name;

	private PayStatusEnum(byte code, String name) {
		this.code = code;
		this.name = name;
	}

	public Byte getCode() {
		return code;
	}

	public void setCode(Byte code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String getNameByCode(Byte code){
		PayStatusEnum[] values = PayStatusEnum.values();
		for(PayStatusEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
}
