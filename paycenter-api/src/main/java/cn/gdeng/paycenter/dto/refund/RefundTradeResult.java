package cn.gdeng.paycenter.dto.refund;

import java.io.Serializable;
import java.util.Date;

public class RefundTradeResult implements Serializable{

	private static final long serialVersionUID = 1L;

	private String code;//10000 成功 
	
	private String msg;
	
	private String refundNo;//退款单号
	
	private Date refundTime;//退款时间

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	
	
}
