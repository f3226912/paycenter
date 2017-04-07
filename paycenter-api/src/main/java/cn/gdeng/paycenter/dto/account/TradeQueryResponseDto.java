package cn.gdeng.paycenter.dto.account;

import java.io.Serializable;

/**
 * 交易查询返回DTO
 * @author sss
 *
 */
public class TradeQueryResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderNo;
	
	private String thirdPayNumber;
	
	private String payType;
	
	/**
	 * 支付状态 支付宝查询时，返回已关闭，可能是退款引起的关闭，无法返回已退款状态
	 * 如果是支付宝支付的，要确定是否是退款引起的关闭，需要调用退款查询接口
	 */
	private String payStatus;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
}
