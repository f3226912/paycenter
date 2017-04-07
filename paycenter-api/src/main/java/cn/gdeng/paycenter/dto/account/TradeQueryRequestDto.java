package cn.gdeng.paycenter.dto.account;

import java.io.Serializable;

public class TradeQueryRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String payCenterNumber;
	
	private String thirdPayNumber;
	
	private String appKey;

	

	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
