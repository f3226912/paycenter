package cn.gdeng.paycenter.dto.refund;

public class RefundQueryResult {

	private String code;//10000 成功 20000失败  30000交易不存在
	
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
