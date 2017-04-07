package cn.gdeng.paycenter.dto.pay;

import java.text.SimpleDateFormat;

import cn.gdeng.paycenter.entity.pay.BillCheckDetailEntity;

public class BillCheckDetailDto extends BillCheckDetailEntity {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 */
	private static final long serialVersionUID = 9001355106417121969L;
	
	private String trans_date_str;
	
	private String payTimeStr;
	
	private Double feeAmt;
	
	private String orderNo;

	public String getTrans_date_str() {

		if(trans_date_str==null) {
			if(getTrans_date()!=null)
				trans_date_str = dateFormat.format(getTrans_date());
		}
		return trans_date_str;
	}

	public void setTrans_date_str(String trans_date_str) {
		this.trans_date_str = trans_date_str;
	}

	public Double getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(Double feeAmt) {
		this.feeAmt = feeAmt;
	}
	
	public String toString() {
         return "id=" + getId() + ",payCenterNumber=" + getPayCenterNumber() + ",thirdPayNumber=" + getThirdPayNumber()
        		 + ",trans_date=" + getTrans_date() + ",paygoodsName=" + getGoodsName() + ",payAmt=" + getPayAmt() + ",orderNo=" + getOrderNo()
        		 + ",checkStatus=" + getCheckStatus() + ",billInfo=" + getBillInfo() + ",rate=" + getRate() + ",failReason=" + getFailReason()
        		 + ",receiptAmt=" + getReceiptAmt() + ",remark=" + getRemark() + ",feeAmt=" + getFeeAmt() + ",payChannelCode=" + getPayChannelCode();
    }

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayTimeStr() {
		if(payTimeStr==null) {
			if(getPayTime()!=null)
				payTimeStr = dateFormat.format(getPayTime());
		}
		return payTimeStr;
	}

	public void setPayTimeStr(String payTimeStr) {
		this.payTimeStr = payTimeStr;
	}


	
}
