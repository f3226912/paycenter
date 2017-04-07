package cn.gdeng.paycenter.dto.pos;

import java.io.Serializable;
import java.util.Date;

public class WangPosPayNotifyDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String payCenterNumber;//支付中心流水号
	
	private String tradeNo;
	
	private String tradeStatus;
	
	private Date tradeTime;

	private double tradeAmt;//交易金额
	
	private String posClientNo;//POS终端号
	
	private String payType;//WANGPOS支付方式  1001	现金 1003	微信1004	支付宝1005	百度钱包1006	银行卡
							//1007	易付宝1009	京东钱包1011	QQ钱包
	
	private String buyer;//银行卡号
	
	private boolean sendMq; //是否要发送MQ,

	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public double getTradeAmt() {
		return tradeAmt;
	}

	public void setTradeAmt(double tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	public String getPosClientNo() {
		return posClientNo;
	}

	public void setPosClientNo(String posClientNo) {
		this.posClientNo = posClientNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public boolean isSendMq() {
		return sendMq;
	}

	public void setSendMq(boolean sendMq) {
		this.sendMq = sendMq;
	}


}
