package cn.gdeng.paycenter.dto.pay;

import javax.persistence.Entity;

import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;

@Entity(name = "fee_record")
public class FeeRecordDTO extends FeeRecordEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4054922424771761375L;
	
	private int 	tradeId; 	//交易ID
	private String 	appKey;		//来源
	private String 	payType;	//支付方式
	
	private String 	financeBeginTime;
	
	private String 	financeEndTime;

	
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getFinanceBeginTime() {
		return financeBeginTime;
	}

	public void setFinanceBeginTime(String financeBeginTime) {
		this.financeBeginTime = financeBeginTime;
	}

	public String getFinanceEndTime() {
		return financeEndTime;
	}

	public void setFinanceEndTime(String financeEndTime) {
		this.financeEndTime = financeEndTime;
	}

	public int getTradeId() {
		return tradeId;
	}

	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}

	

	

}
