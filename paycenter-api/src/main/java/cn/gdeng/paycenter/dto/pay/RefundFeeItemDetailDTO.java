package cn.gdeng.paycenter.dto.pay;

public class RefundFeeItemDetailDTO {

	
	private String feeType;
	
	private Double feeAmt;

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Double getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(Double feeAmt) {
		this.feeAmt = feeAmt;
	}
	
	
}
