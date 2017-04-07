package cn.gdeng.paycenter.dto.pay;

/**
 * 错误码对象
 * 附带返回信息
 */
public class StatusCodeEnumWithInfoDTO {

	private String statusCode;
	
	private GdOrderActivityResultDTO object;


	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public GdOrderActivityResultDTO getObject() {
		return object;
	}

	public void setObject(GdOrderActivityResultDTO object) {
		this.object = object;
	}
}
