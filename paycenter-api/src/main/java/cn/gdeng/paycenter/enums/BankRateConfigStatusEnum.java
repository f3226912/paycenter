package cn.gdeng.paycenter.enums;

/**
* @author Kwang
* @version 创建时间：2016年11月14日 下午2:51:33
* 开关
*/
public enum BankRateConfigStatusEnum { 
	KQ(1, "开启"),
	GB(2, "关闭");
	
	private Integer code;
	
	private String name;
	
	
	private BankRateConfigStatusEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String getNameByCode(Integer code){
		BankRateConfigStatusEnum[] values = BankRateConfigStatusEnum.values();
		for(BankRateConfigStatusEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
	

}
