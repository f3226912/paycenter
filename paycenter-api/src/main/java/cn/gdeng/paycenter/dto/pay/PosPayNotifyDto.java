package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.PosPayNotifyEntity;

public class PosPayNotifyDto  extends PosPayNotifyEntity{

	private static final long serialVersionUID = 1L;
	
	private String notifyStatus;
	
	private String gdBankCardNo;

	public String getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public String getGdBankCardNo() {
		return gdBankCardNo;
	}

	public void setGdBankCardNo(String gdBankCardNo) {
		this.gdBankCardNo = gdBankCardNo;
	}

}
