package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.List;

import cn.gdeng.paycenter.enums.PayStatusEnum;

public class FailedBillDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4661776264253120906L;
	
	private String thirdPayNumber;
	
	private String payCenterNumber;
	
	private String orderNo;
	
	private String payAmt;
	
	private String payerMobile;
	
	private String payType;
	
	private String payTime;
	
	private String posClientNo;
	
	private String payStatus;
	
	private String checkStatus;
	
	private String remark;
	
	private String orderAmt;
	
	private String trans_date;
	
	private String payTimeStr;
	
	private String payStatusStr;
	
	private String payTypeStr;
	
	private String failReason;
	
	private String resultType;
	
	/** 6+1 新增属性 begin*/
	private String clientNo;
	private String businessNo;
	private String tradeType;
	private String thirdTransNo;
	private String tradeMoney;
	private String fee;
	private String cardNo;
	private String hasClear;
	private String actualTotalAmt = "0.00";
	/** 6+1 新增属性 end*/

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}

	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

	public String getPayerMobile() {
		return payerMobile;
	}

	public void setPayerMobile(String payerMobile) {
		this.payerMobile = payerMobile;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getPosClientNo() {
		return posClientNo;
	}

	public void setPosClientNo(String posClientNo) {
		this.posClientNo = posClientNo;
	}
	

	public String getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}
	

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}

	public String getPayTimeStr() {
		if(getPayTime() == null){
			return "";
		}
		return this.getPayTime().substring(0, getPayTime().length() - 2);
	}

	public String getPayStatusStr() {
		return PayStatusEnum.getNameByCode(Byte.valueOf(getPayStatus()));
	}

	public String getPayTypeStr() {
		if(getPayType().equals("ALIPAY_H5")){
			return "支付宝支付";
		} else if(getPayType().equals("WEIXIN_APP")){
			return "微信支付";
		} else if(getPayType().equals("ENONG")){
			return "武汉E农";
		} else if(getPayType().equals("NNCCB")){
			return "南宁建行";
		} else if(getPayType().equals("GXRCB")){
			return "广西农信";
		} else if(getPayType().equals("WANGPOS")){
			return "旺POS";
		}
		return "";
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getThirdTransNo() {
		return thirdTransNo;
	}

	public void setThirdTransNo(String thirdTransNo) {
		this.thirdTransNo = thirdTransNo;
	}

	public String getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(String tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getActualTotalAmt() {
		return actualTotalAmt;
	}

	public void setActualTotalAmt(String actualTotalAmt) {
		this.actualTotalAmt = actualTotalAmt;
	}

	public String getHasClear() {
		return hasClear;
	}

	public void setHasClear(String hasClear) {
		this.hasClear = hasClear;
	}
	
	
	

}
