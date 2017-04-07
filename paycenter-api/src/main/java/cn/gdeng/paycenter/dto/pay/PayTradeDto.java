package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeExtendEntity;
import cn.gdeng.paycenter.entity.pay.PayTradePosEntity;

@SuppressWarnings("serial")
public class PayTradeDto extends PayTradeEntity {
	
	private String posClientNo;
	
	private String bankCardNo;

	private PayTradeExtendEntity payTradeExtendEntity;
	
	private PayTradePosEntity payTradePosEntity;

	public PayTradeExtendEntity getPayTradeExtendEntity() {
		return payTradeExtendEntity;
	}

	public void setPayTradeExtendEntity(PayTradeExtendEntity payTradeExtendEntity) {
		this.payTradeExtendEntity = payTradeExtendEntity;
	}

	public PayTradePosEntity getPayTradePosEntity() {
		return payTradePosEntity;
	}

	public void setPayTradePosEntity(PayTradePosEntity payTradePosEntity) {
		this.payTradePosEntity = payTradePosEntity;
	}

	public String getPosClientNo() {
		return posClientNo;
	}

	public void setPosClientNo(String posClientNo) {
		this.posClientNo = posClientNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	
	

}
