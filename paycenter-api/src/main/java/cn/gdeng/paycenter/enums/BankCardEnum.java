package cn.gdeng.paycenter.enums;

/**
 * 代付银行卡
 * @author huang
 * @Date:2016年11月21日下午6:47:29
 */
public enum BankCardEnum { 
	/**中国农业银行股份有限公司深圳中心区支行   */
	BUSINESS("4100 5000 0400 33958", "农业银行"),
	/**工商银行深圳卓越时代广场支行			*/
	AGRICULTURE("4000 1097 0910 0101 516", "工商银行");
	
	private String code;
	
	private String name;
	
	
	private BankCardEnum(String code, String name) {
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
		BankCardEnum[] values = BankCardEnum.values();
		for(BankCardEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
	

}
