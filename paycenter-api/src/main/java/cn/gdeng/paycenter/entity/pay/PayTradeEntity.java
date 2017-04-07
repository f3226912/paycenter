package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "pay_trade")
public class PayTradeEntity  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer version;

    private String orderNo;

    private String title;

    private String payType;

    private String payCenterNumber;

    private String thirdPayNumber;

    private String timeOut;

    private Date payTime;

    private Date closeTime;

    private String payStatus;

    private String appKey;

    private String notifyStatus;

    private String reParam;

    private String returnUrl;

    private String notifyUrl;

    private Double totalAmt;

    private Double payAmt;

    private Double receiptAmt;

    private Date orderTime;

    private String closeUserId;

    private String thirdPayerAccount;

    private String payerUserId;

    private String payerAccount;

    private String payerName;

    private String payeeUserId;

    private String payeeAccount;

    private String payeeName;

    private String thirdPayeeAccount;

    private String rate;

    private Double feeAmt;

    private String refundUserId;

    private Date refundTime;

    private String extendJson;

    private String requestIp;

    private Date createTime;

    private String createUserId;

    private Date updateTime;

    private String updateUserId;
    
    private String payerMobile;
    
    private String payeeMobile;

    private String requestNo;
    
    private Integer payCount;
    
    private String logisticsUserId;
    
    private String hasGenFlag;
    
    @Id
    @Column(name = "id")
    public Integer getId(){

        return this.id;
    }
    public void setId(Integer id){

        this.id = id;
    }
    @Column(name = "version")
    public Integer getVersion(){

        return this.version;
    }
    public void setVersion(Integer version){

        this.version = version;
    }
    @Column(name = "orderNo")
    public String getOrderNo(){

        return this.orderNo;
    }
    public void setOrderNo(String orderNo){

        this.orderNo = orderNo;
    }
    @Column(name = "title")
    public String getTitle(){

        return this.title;
    }
    public void setTitle(String title){

        this.title = title;
    }
    @Column(name = "payType")
    public String getPayType(){

        return this.payType;
    }
    public void setPayType(String payType){

        this.payType = payType;
    }
    @Column(name = "payCenterNumber")
    public String getPayCenterNumber(){

        return this.payCenterNumber;
    }
    public void setPayCenterNumber(String payCenterNumber){

        this.payCenterNumber = payCenterNumber;
    }
    @Column(name = "thirdPayNumber")
    public String getThirdPayNumber(){

        return this.thirdPayNumber;
    }
    public void setThirdPayNumber(String thirdPayNumber){

        this.thirdPayNumber = thirdPayNumber;
    }
    @Column(name = "timeOut")
    public String getTimeOut(){

        return this.timeOut;
    }
    public void setTimeOut(String timeOut){

        this.timeOut = timeOut;
    }
    @Column(name = "payTime")
    public Date getPayTime(){

        return this.payTime;
    }
    public void setPayTime(Date payTime){

        this.payTime = payTime;
    }
    @Column(name = "closeTime")
    public Date getCloseTime(){

        return this.closeTime;
    }
    public void setCloseTime(Date closeTime){

        this.closeTime = closeTime;
    }
    @Column(name = "payStatus")
    public String getPayStatus(){

        return this.payStatus;
    }
    public void setPayStatus(String payStatus){

        this.payStatus = payStatus;
    }
    @Column(name = "appKey")
    public String getAppKey(){

        return this.appKey;
    }
    public void setAppKey(String appKey){

        this.appKey = appKey;
    }
    @Column(name = "notifyStatus")
    public String getNotifyStatus(){

        return this.notifyStatus;
    }
    public void setNotifyStatus(String notifyStatus){

        this.notifyStatus = notifyStatus;
    }
    @Column(name = "reParam")
    public String getReParam(){

        return this.reParam;
    }
    public void setReParam(String reParam){

        this.reParam = reParam;
    }
    @Column(name = "returnUrl")
    public String getReturnUrl(){

        return this.returnUrl;
    }
    public void setReturnUrl(String returnUrl){

        this.returnUrl = returnUrl;
    }
    @Column(name = "notifyUrl")
    public String getNotifyUrl(){

        return this.notifyUrl;
    }
    public void setNotifyUrl(String notifyUrl){

        this.notifyUrl = notifyUrl;
    }
    @Column(name = "totalAmt")
    public Double getTotalAmt(){

        return this.totalAmt;
    }
    public void setTotalAmt(Double totalAmt){

        this.totalAmt = totalAmt;
    }
    @Column(name = "payAmt")
    public Double getPayAmt(){

        return this.payAmt;
    }
    public void setPayAmt(Double payAmt){

        this.payAmt = payAmt;
    }
    @Column(name = "receiptAmt")
    public Double getReceiptAmt(){

        return this.receiptAmt;
    }
    public void setReceiptAmt(Double receiptAmt){

        this.receiptAmt = receiptAmt;
    }
    @Column(name = "orderTime")
    public Date getOrderTime(){

        return this.orderTime;
    }
    public void setOrderTime(Date orderTime){

        this.orderTime = orderTime;
    }
    @Column(name = "closeUserId")
    public String getCloseUserId(){

        return this.closeUserId;
    }
    public void setCloseUserId(String closeUserId){

        this.closeUserId = closeUserId;
    }
    @Column(name = "thirdPayerAccount")
    public String getThirdPayerAccount(){

        return this.thirdPayerAccount;
    }
    public void setThirdPayerAccount(String thirdPayerAccount){

        this.thirdPayerAccount = thirdPayerAccount;
    }
    @Column(name = "payerUserId")
    public String getPayerUserId(){

        return this.payerUserId;
    }
    public void setPayerUserId(String payerUserId){

        this.payerUserId = payerUserId;
    }
    @Column(name = "payerAccount")
    public String getPayerAccount(){

        return this.payerAccount;
    }
    public void setPayerAccount(String payerAccount){

        this.payerAccount = payerAccount;
    }
    @Column(name = "payerName")
    public String getPayerName(){

        return this.payerName;
    }
    public void setPayerName(String payerName){

        this.payerName = payerName;
    }
    @Column(name = "payeeUserId")
    public String getPayeeUserId(){

        return this.payeeUserId;
    }
    public void setPayeeUserId(String payeeUserId){

        this.payeeUserId = payeeUserId;
    }
    @Column(name = "payeeAccount")
    public String getPayeeAccount(){

        return this.payeeAccount;
    }
    public void setPayeeAccount(String payeeAccount){

        this.payeeAccount = payeeAccount;
    }
    @Column(name = "payeeName")
    public String getPayeeName(){

        return this.payeeName;
    }
    public void setPayeeName(String payeeName){

        this.payeeName = payeeName;
    }
    @Column(name = "thirdPayeeAccount")
    public String getThirdPayeeAccount(){

        return this.thirdPayeeAccount;
    }
    public void setThirdPayeeAccount(String thirdPayeeAccount){

        this.thirdPayeeAccount = thirdPayeeAccount;
    }
    @Column(name = "rate")
    public String getRate(){

        return this.rate;
    }
    public void setRate(String rate){

        this.rate = rate;
    }
    @Column(name = "feeAmt")
    public Double getFeeAmt(){

        return this.feeAmt;
    }
    public void setFeeAmt(Double feeAmt){

        this.feeAmt = feeAmt;
    }
    @Column(name = "refundUserId")
    public String getRefundUserId(){

        return this.refundUserId;
    }
    public void setRefundUserId(String refundUserId){

        this.refundUserId = refundUserId;
    }
    @Column(name = "refundTime")
    public Date getRefundTime(){

        return this.refundTime;
    }
    public void setRefundTime(Date refundTime){

        this.refundTime = refundTime;
    }
    @Column(name = "extendJson")
    public String getExtendJson(){

        return this.extendJson;
    }
    public void setExtendJson(String extendJson){

        this.extendJson = extendJson;
    }
    @Column(name = "requestIp")
    public String getRequestIp(){

        return this.requestIp;
    }
    public void setRequestIp(String requestIp){

        this.requestIp = requestIp;
    }
    @Column(name = "createTime")
    public Date getCreateTime(){

        return this.createTime;
    }
    public void setCreateTime(Date createTime){

        this.createTime = createTime;
    }
    @Column(name = "createUserId")
    public String getCreateUserId(){

        return this.createUserId;
    }
    public void setCreateUserId(String createUserId){

        this.createUserId = createUserId;
    }
    @Column(name = "updateTime")
    public Date getUpdateTime(){

        return this.updateTime;
    }
    public void setUpdateTime(Date updateTime){

        this.updateTime = updateTime;
    }
    @Column(name = "updateUserId")
    public String getUpdateUserId(){

        return this.updateUserId;
    }
    public void setUpdateUserId(String updateUserId){

        this.updateUserId = updateUserId;
    }
    
    @Column(name = "payerMobile")
	public String getPayerMobile() {
		return payerMobile;
	}
	
	public void setPayerMobile(String payerMobile) {
		this.payerMobile = payerMobile;
	}
	
	@Column(name = "payeeMobile")
	public String getPayeeMobile() {
		return payeeMobile;
	}
	
	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
	}
	
	@Column(name = "requestNo")
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	
	@Column(name = "payCount")
	public Integer getPayCount() {
		return payCount;
	}
	public void setPayCount(Integer payCount) {
		this.payCount = payCount;
	}
	
	@Column(name = "logisticsUserId")
	public String getLogisticsUserId() {
		return logisticsUserId;
	}
	public void setLogisticsUserId(String logisticsUserId) {
		this.logisticsUserId = logisticsUserId;
	}

    @Column(name = "hasGenFlag")
    public String getHasGenFlag(){
        return this.hasGenFlag;
    }
	public void setHasGenFlag(String hasGenFlag) {
		this.hasGenFlag = hasGenFlag;
	}
}

