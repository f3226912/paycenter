package cn.gdeng.paycenter.dto.pay;

import java.io.Serializable;
import java.util.List;

public class BillChecks implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -552154659121440448L;

	private List<BillCheckDetailDto> billDetail;
	
	private Boolean hasNext; //是否还有数据

	public List<BillCheckDetailDto> getBillDetail() {
		return billDetail;
	}

	public void setBillDetail(List<BillCheckDetailDto> billDetail) {
		this.billDetail = billDetail;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

}
