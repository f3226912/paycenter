package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public class PayGateWayDto extends PayTradeEntity{



	private static final long serialVersionUID = 1L;
	
	private String orderInfos; //合并支付使用
	
	private double sumPayAmt;

	public String getOrderInfos() {
		return orderInfos;
	}

	public void setOrderInfos(String orderInfos) {
		this.orderInfos = orderInfos;
	}

	public double getSumPayAmt() {
		return sumPayAmt;
	}

	public void setSumPayAmt(double sumPayAmt) {
		this.sumPayAmt = sumPayAmt;
	}

}
