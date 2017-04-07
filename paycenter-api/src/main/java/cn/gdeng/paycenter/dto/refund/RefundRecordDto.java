package cn.gdeng.paycenter.dto.refund;

import java.util.List;

import cn.gdeng.paycenter.entity.pay.RefundFeeItemDetailEntity;
import cn.gdeng.paycenter.entity.pay.RefundRecordEntity;

public class RefundRecordDto extends RefundRecordEntity{

	private static final long serialVersionUID = 1L;

	private List<RefundFeeItemDetailEntity> feeList;

	public List<RefundFeeItemDetailEntity> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<RefundFeeItemDetailEntity> feeList) {
		this.feeList = feeList;
	}
}
