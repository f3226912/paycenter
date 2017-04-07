package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.BillClearSumEntity;

public class BillClearSumDTO extends BillClearSumEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BillClearSumDTO(){
		
	}
	public BillClearSumDTO(boolean isInit){
		this.setClearNum(0);
		this.setClearAmt(0d);
		this.setClearSuccessNum(0);
		this.setClearSuccessAmt(0d);
		this.setClearFailNum(0);
		this.setClearFailAmt(0d);
	}

}
