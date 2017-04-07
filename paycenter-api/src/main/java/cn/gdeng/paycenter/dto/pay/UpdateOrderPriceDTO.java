package cn.gdeng.paycenter.dto.pay;

/**
 * 错误码对象
 * 附带返回信息
 */
public class UpdateOrderPriceDTO {

	private String statusCode;
	
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
