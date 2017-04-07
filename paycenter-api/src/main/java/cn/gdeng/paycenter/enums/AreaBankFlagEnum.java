package cn.gdeng.paycenter.enums;

/**
* @author Kwang
* @version 创建时间：2016年11月14日 下午2:51:33
* 业务类型 
*/
public enum AreaBankFlagEnum { 
	BH("1", "本行"),
	KH("2", "跨行");
	
	private String code;
	
	private String name;
	
	
	private AreaBankFlagEnum(String code, String name) {
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
	
	public static String getNameByCode(String code){
		AreaBankFlagEnum[] values = AreaBankFlagEnum.values();
		for(AreaBankFlagEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
	

}
