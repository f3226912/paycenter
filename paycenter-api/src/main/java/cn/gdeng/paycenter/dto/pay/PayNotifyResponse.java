package cn.gdeng.paycenter.dto.pay;

public class PayNotifyResponse extends PayTradeDto {

	private static final long serialVersionUID = 1L;

	private String sign;

	/**
	 * 10000 成功
	 */
	private Integer respCode;

	private String respMsg;
	
	/**
	 * 第三方交易状态   TRADE_SUCCESS 交易成功
	 */
	private String tradeStatus;

	public Integer getRespCode() {
		return respCode;
	}

	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

}
