package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "fee_record")
public class FeeRecordEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2922639203117722139L;

	private Integer id;

	private String orderNo;

	private String thridPayNumber;

	private String feeType;

	private Date financeTime;

	private Double payAmt;

	private String rate;

	private Double feeAmt;

	private String operaUserId;

	private Date operaTime;

	private Date createTime;

	private String createUserId;

	private Date updateTime;

	private String updateUserId;
	
	private String remark; //备注
	
	private String payCenterNumber;
	
	private int  isSys;		//0 系统手续费，1：手动添加
	
	private String sign;

	@Column(name="payCenterNumber")
	public String getPayCenterNumber() {
		return payCenterNumber;
	}

	public void setPayCenterNumber(String payCenterNumber) {
		this.payCenterNumber = payCenterNumber;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Id
	@Column(name = "id")
	public Integer getId() {

		return this.id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	@Column(name = "orderNo")
	public String getOrderNo() {

		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {

		this.orderNo = orderNo;
	}

	@Column(name = "thridPayNumber")
	public String getThridPayNumber() {

		return this.thridPayNumber;
	}

	public void setThridPayNumber(String thridPayNumber) {

		this.thridPayNumber = thridPayNumber;
	}

	@Column(name = "feeType")
	public String getFeeType() {

		return this.feeType;
	}
	
	public String getFeeTypeStr() {
		if("0".equals(feeType)) {
			return "交易手续费";
		} else if("1".equals(feeType)) {
			return "转账手续费";
		} else if("2".equals(feeType)) {
			return "其他";
		} else {
			return "";
		}
	}

	public void setFeeType(String feeType) {

		this.feeType = feeType;
	}

	@Column(name = "financeTime")
	public Date getFinanceTime() {

		return this.financeTime;
	}

	public void setFinanceTime(Date financeTime) {

		this.financeTime = financeTime;
	}

	@Column(name = "payAmt")
	public Double getPayAmt() {

		return this.payAmt;
	}

	public void setPayAmt(Double payAmt) {

		this.payAmt = payAmt;
	}

	@Column(name = "rate")
	public String getRate() {

		return this.rate;
	}

	public void setRate(String rate) {

		this.rate = rate;
	}

	@Column(name = "feeAmt")
	public Double getFeeAmt() {

		return this.feeAmt;
	}

	public void setFeeAmt(Double feeAmt) {

		this.feeAmt = feeAmt;
	}

	@Column(name = "operaUserId")
	public String getOperaUserId() {

		return this.operaUserId;
	}

	public void setOperaUserId(String operaUserId) {

		this.operaUserId = operaUserId;
	}

	@Column(name = "operaTime")
	public Date getOperaTime() {
		return this.operaTime;
	}

	public void setOperaTime(Date operaTime) {

		this.operaTime = operaTime;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {

		return this.createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	@Column(name = "createUserId")
	public String getCreateUserId() {

		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {

		this.createUserId = createUserId;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {

		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {

		this.updateTime = updateTime;
	}

	@Column(name = "updateUserId")
	public String getUpdateUserId() {

		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {

		this.updateUserId = updateUserId;
	}
	
	@Column(name="isSys")
	public int getIsSys() {
		return isSys;
	}

	public void setIsSys(int isSys) {
		this.isSys = isSys;
	}

	@Column(name = "sign")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
