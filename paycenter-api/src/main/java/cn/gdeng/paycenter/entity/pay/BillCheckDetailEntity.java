package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "bill_check_detail")
public class BillCheckDetailEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3146938390815389033L;

	private Integer id;

	private String payCenterNumber;

	private String thirdPayNumber;

	private Date trans_date;

	private Date payTime;
	
	private String goodsName;

	private Double payAmt;

	private String checkStatus; //1:对账成功 2:对账失败

	private String billInfo;
	
	private Double rate; //费率
	
	private Double receiptAmt; //实收款
	
	private String failReason; //对账失败原因
	
	private String remark; //备注
	
	private String payChannelCode; //支付渠道编码
	
	private String resultType;//对账结果类型 1我方有他方有 2我方有他方无 3我方无他方有
	
	private String posClientNo;
	
    /**
     *创建时间
     */
    private Date createTime;
    /**
     *创建用户
     */
    private String createUserId;
    /**
     *
     */
    private Date updateTime;
    /**
     *
     */
    private String updateUserId;
	
	public Double getReceiptAmt() {
		return receiptAmt;
	}

	public void setReceiptAmt(Double receiptAmt) {
		this.receiptAmt = receiptAmt;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Column(name = "id")
	public Integer getId() {

		return this.id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	@Column(name = "payCenterNumber")
	public String getPayCenterNumber() {

		return this.payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {

		this.payCenterNumber = payCenterNumber;
	}

	@Column(name = "thirdPayNumber")
	public String getThirdPayNumber() {

		return this.thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {

		this.thirdPayNumber = thirdPayNumber;
	}

	@Column(name = "trans_date")
	public Date getTrans_date() {

		return this.trans_date;
	}

	public void setTrans_date(Date trans_date) {

		this.trans_date = trans_date;
	}

	@Column(name = "payTime")
	public Date getPayTime() {

		return this.payTime;
	}

	public void setPayTime(Date payTime) {

		this.payTime = payTime;
	}
	
	@Column(name = "goodsName")
	public String getGoodsName() {

		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {

		this.goodsName = goodsName;
	}

	@Column(name = "payAmt")
	public Double getPayAmt() {

		return this.payAmt;
	}

	public void setPayAmt(Double payAmt) {

		this.payAmt = payAmt;
	}

	@Column(name = "checkStatus")
	public String getCheckStatus() {

		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {

		this.checkStatus = checkStatus;
	}

	@Column(name = "billInfo")
	public String getBillInfo() {

		return this.billInfo;
	}

	public void setBillInfo(String billInfo) {

		this.billInfo = billInfo;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "createUserId")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "updateUserId")
	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getPayChannelCode() {
		return payChannelCode;
	}

	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}

	@Column(name = "resultType")
	public String getResultType() {
		return resultType;
	}
	
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getPosClientNo() {
		return posClientNo;
	}

	public void setPosClientNo(String posClientNo) {
		this.posClientNo = posClientNo;
	}
	
	
}
