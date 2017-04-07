package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.BillCheckSumEntity;

public class BillCheckSumDTO extends BillCheckSumEntity {
	/**
	 * 支付类型名称。
	 */
	private String payTypeName;

    private String payTimeStr;
    
	public BillCheckSumDTO(){
		
	}
	public BillCheckSumDTO(boolean isInit){
		this.setCheckNum(new Integer(0));
		this.setCheckAmt(new Double(0d));
		this.setCheckSuccessNum(new Integer(0));
		this.setCheckSuccessAmt(new Double(0d));
		this.setCheckFailNum(new Integer(0));
		this.setCheckFailAmt(new Double(0d));
	}
	public String getPayTimeStr() {
		return payTimeStr;
	}
	public void setPayTimeStr(String payTimeStr) {
		this.payTimeStr = payTimeStr;
	}
	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	
	public String toString() {
        return "id=" + getId() + ",payType=" + getPayType() + ",payTime=" + getPayTime() + ",version=" + getVersion()
       		 + ",checkNum=" + getCheckNum() + ",checkAmt=" + getCheckAmt() + ",checkSuccessNum=" + getCheckSuccessNum()
       		 + ",checkSuccessAmt=" + getCheckSuccessAmt() + ",checkFailNum=" + getCheckFailNum() + ",checkFailAmt=" + getCheckFailAmt();
   }
}
