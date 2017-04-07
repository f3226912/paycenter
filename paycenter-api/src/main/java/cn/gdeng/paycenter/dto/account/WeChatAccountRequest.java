package cn.gdeng.paycenter.dto.account;

import java.io.Serializable;
import java.util.List;

public class WeChatAccountRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -148467447022436023L;

	private WeChatAccountParams params;//请求参数
	
	private List<WeChatAccountResponse> responseList;//响应对账单列表

	private String url = "https://api.mch.weixin.qq.com/pay/downloadbill";//接口地址

	private String returnCode;

	private String returnMsg;
	
	private Integer colsLength=24;//表头列长
	
	private String reponseStart="<xml>";

	public WeChatAccountParams getParams() {
		return params;
	}

	public void setParams(WeChatAccountParams params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public List<WeChatAccountResponse> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<WeChatAccountResponse> responseList) {
		this.responseList = responseList;
	}

	public Integer getColsLength() {
		return colsLength;
	}

	public void setColsLength(Integer colsLength) {
		this.colsLength = colsLength;
	}

	public String getReponseStart() {
		return reponseStart;
	}

	public void setReponseStart(String reponseStart) {
		this.reponseStart = reponseStart;
	}

	
}
