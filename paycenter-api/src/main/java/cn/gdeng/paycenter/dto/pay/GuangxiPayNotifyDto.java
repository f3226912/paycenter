package cn.gdeng.paycenter.dto.pay;

import java.io.Serializable;

public class GuangxiPayNotifyDto implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 谷登订单号 */
	private String orderno;
	
	/** 交易类型（0—订单支付 1—刷卡消费）*/
	private String transype;
	
	/** 订单金额*/
	private String orderfee;
	
	/** 手续费（1—15，单位分）*/
	private String ratefee;
	
	/** 支付金额（1—15，单位分）*/
	private String payfee;
	
	/** 支付响应码 */
	private String payresultcode;
	
	/** 支付响应码说明 */
	private String payresultmsg;
	
	/** 付款卡号 */
	private String paycardno;
	
	/** 交易日期（yyyyMMdd） */
	private String transdate;
	
	/** 交易时间（HHmmss） */
	private String transtime;
	
	/** 交易流水号（1—22） */
	private String transseqno;
	
	/** 接口版本号(填1.0) */
	private String version;
	
	/** 字符集(填1) 1-UTF-8 */
	private String charset ;
	
	/** POS终端号 */
	private String machinenum;
	
	/** POS商户号 */
	private String merchantnum;
	
	/** 保留字节 */
	private String reserved;
	
	private String appKey;

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getTransype() {
		return transype;
	}

	public void setTransype(String transype) {
		this.transype = transype;
	}

	public String getOrderfee() {
		return orderfee;
	}

	public void setOrderfee(String orderfee) {
		this.orderfee = orderfee;
	}

	public String getRatefee() {
		return ratefee;
	}

	public void setRatefee(String ratefee) {
		this.ratefee = ratefee;
	}

	public String getPayfee() {
		return payfee;
	}

	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}

	public String getPayresultcode() {
		return payresultcode;
	}

	public void setPayresultcode(String payresultcode) {
		this.payresultcode = payresultcode;
	}

	public String getPayresultmsg() {
		return payresultmsg;
	}

	public void setPayresultmsg(String payresultmsg) {
		this.payresultmsg = payresultmsg;
	}

	public String getPaycardno() {
		return paycardno;
	}

	public void setPaycardno(String paycardno) {
		this.paycardno = paycardno;
	}

	public String getTransdate() {
		return transdate;
	}

	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}

	public String getTranstime() {
		return transtime;
	}

	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}

	public String getTransseqno() {
		return transseqno;
	}

	public void setTransseqno(String transseqno) {
		this.transseqno = transseqno;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getMachinenum() {
		return machinenum;
	}

	public void setMachinenum(String machinenum) {
		this.machinenum = machinenum;
	}

	public String getMerchantnum() {
		return merchantnum;
	}

	public void setMerchantnum(String merchantnum) {
		this.merchantnum = merchantnum;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

}
