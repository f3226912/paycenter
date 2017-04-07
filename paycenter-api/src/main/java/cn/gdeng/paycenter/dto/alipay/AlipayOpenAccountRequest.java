package cn.gdeng.paycenter.dto.alipay;

public class AlipayOpenAccountRequest extends AlipayOpenRequest{
	
	private static final long serialVersionUID = 1L;
	
	private String bill_type="trade";
	
	private String bill_date;
	
	private String method="alipay.data.dataservice.bill.downloadurl.query";
	
	private String biz_content;
	
	private String appKey;
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public AlipayOpenAccountRequest(){
		super();
	}

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	@Override
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	public String getBiz_content() {
		if(this.biz_content == null){
			return "{\"bill_type\":\""+getBill_type()+"\""
					+",\"bill_date\":\""+getBill_date()+"\"}";
		}
		return this.biz_content;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

}
