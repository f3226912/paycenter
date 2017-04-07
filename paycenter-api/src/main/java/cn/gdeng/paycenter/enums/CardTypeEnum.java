package cn.gdeng.paycenter.enums;

/**
* @author Kwang
* @version 创建时间：2016年11月14日 下午2:51:33
* 卡类型 
*/
public enum CardTypeEnum { 
	JJK("1", "借记卡"),
	HJK("2", "货记卡");
	
	private String code;
	
	private String name;
	
	
	private CardTypeEnum(String code, String name) {
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
		CardTypeEnum[] values = CardTypeEnum.values();
		for(CardTypeEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
	

}
