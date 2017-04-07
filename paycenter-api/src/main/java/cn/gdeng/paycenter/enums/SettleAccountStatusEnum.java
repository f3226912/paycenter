package cn.gdeng.paycenter.enums;

/**
 * 结算 代付状态
 * @author huang
 * @Date:2016年11月12日上午11:20:31
 */
public enum SettleAccountStatusEnum {
	
	/**  wait for payment  待支付**/
	WP((byte)0, "待支付"),
	/**  payment successful 支付成功**/
	PS((byte)1, "支付成功");

	private Byte code;
	
	private String name;

	private SettleAccountStatusEnum(Byte code, String name) {
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
		SettleAccountStatusEnum[] values = SettleAccountStatusEnum.values();
		for(SettleAccountStatusEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
}
