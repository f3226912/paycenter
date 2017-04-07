package cn.gdeng.paycenter.dto.customer;

import java.io.Serializable;

public class MarketHessianDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3446284944243049104L;

	private Long id;
	
	private String marketName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	
	
}
