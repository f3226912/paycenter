package cn.gdeng.paycenter.dto.pos;

public class WangPosOrderListRequestDto {

	private String businessId;
	
	private int pageNum=1;//默认1
	 
	private int pageSize=10;//默认10

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "WangPosOrderListRequestDto [businessId=" + businessId + ", pageNum=" + pageNum + ", pageSize="
				+ pageSize + "]";
	}
	
	
}
