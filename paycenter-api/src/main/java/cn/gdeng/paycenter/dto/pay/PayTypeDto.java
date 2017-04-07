package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.PayTypeEntity;

@SuppressWarnings("serial")
public class PayTypeDto extends PayTypeEntity{

	private String payChannelCode; //=payType 
	
	private String payChannelName;//=payTypeName

	public String getPayChannelCode() {
		if(null == payChannelCode){
			return getPayType();
		}
		return payChannelCode;
	}

	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}

	public String getPayChannelName() {
		if(null == payChannelName){
			return getPayTypeName();
		}
		return payChannelName;
	}

	public void setPayChannelName(String payChannelName) {
		this.payChannelName = payChannelName;
	}
	
}
