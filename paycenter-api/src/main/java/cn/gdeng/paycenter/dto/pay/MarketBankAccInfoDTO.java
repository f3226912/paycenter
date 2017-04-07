package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.MarketBankAccInfoEntity;

public class MarketBankAccInfoDTO extends MarketBankAccInfoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2439219467783485062L;
	
	private String account;
	
	private String mobile;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
