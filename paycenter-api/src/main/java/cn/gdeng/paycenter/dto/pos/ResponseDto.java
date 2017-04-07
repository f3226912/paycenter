package cn.gdeng.paycenter.dto.pos;

public class ResponseDto {
	
	private String resultcode;// 0000	操作成功
	
	private String resultmsg;//0001	具体失败原因
	
	private Object datajson;
	
	private String signmsg;

	public String getResultcode() {
		return resultcode;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	public String getResultmsg() {
		return resultmsg;
	}

	public void setResultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}

	public String getSignmsg() {
		return signmsg;
	}

	public void setSignmsg(String signmsg) {
		this.signmsg = signmsg;
	}

	public Object getDatajson() {
		return datajson;
	}

	public void setDatajson(Object datajson) {
		this.datajson = datajson;
	}
	

}
