package cn.gdeng.paycenter.admin.enums;

public enum ApiResultCodeEnum {
	
	SUCCESS(0001, "正常"), 
	SIGN_FAIL(-1, "签名失败");
	
	private int key;  
	private String value;
	
	private ApiResultCodeEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
