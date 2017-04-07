package cn.gdeng.paycenter.enums;

/**
* @author Kwang
* @version 创建时间：2016年11月14日 下午2:51:33
* 收费类型
*/
public enum ProceduresEnum { 
	BL("1", "比例"),
	GDZ("2", "固定值");
	
	private String code;
	
	private String name;
	
	
	private ProceduresEnum(String code, String name) {
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
		ProceduresEnum[] values = ProceduresEnum.values();
		for(ProceduresEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
	

}
