package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 结算管理Entity
 * @author jianhuahuang update
 * @Date:2016年11月8日下午5:03:48
 */
@Entity(name = "remit_record")
public class RemitRecordEntity  implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7571668489991936856L;

	/**
     *
     */
    private Integer id;

    /**
     *订单号
     */
    private String orderNo;

    /**
     *付款行名称
     */
    private String payerBankName;

    /**
     *账务流水号
     */
    private String bankTradeNo;

    /**
     *转账金额
     */
    private Double payAmt;

    /**
     *手续费
     */
    private Double feeAmt;

    /**
     *转账时间(代付时间)
     */
    private Date payTime;

    /**
     *付款人
     */
    private String payerName;

    /**
     *复核状态，1通过 0不通过
     */
    private String auditStatus;

    /**
     *复核人
     */
    private String auditUserId;

    /**
     *区域
     */
    private String bankRealName;

    /**
     *银行卡号
     */
    private String bankCardNo;

    /**
     *开户行名称
     */
    private String depositBankName;

    /**
     *省
     */
    private String provinceName;

    /**
     *城市
     */
    private String cityName;

    /**
     *区域
     */
    private String areaName;

    /**
     *开户行支行名称
     */
    private String subBankName;

    /**
     *银行预留手机号
     */
    private String bankMobile;

    /**
     *平台流水号
     */
    private String payCenterNumber;

    /**
     *结算批次号 yyyymmdd-n
     */
    private String batNo;

    /**
     *会员ID
     */
    private Long memberId;

    /**
     *用户类型(nsy农商友、nps农批商、market市场、plat平台)
     */
    private String userType;

    /**
     *转结算时间
     */
    private Date changeTime;

    /**
     *代付订单笔数
     */
    private Integer orderNum;

    /**
     *代付佣金笔数
     */
    private Integer commissionNum;
    
    /**
     *代付退款笔数
     */
    private Integer refundNum;
    
    /**
     *代付违约金笔数
     */
    private Integer penaltyNum;

    /**
     *代付金额
     */
    private Double dueAmt;

    /**
     *代付状态(1是 0否)
     */
    private String payStatus;

    /**
     *备注
     */
    private String comment;

    /**
     *签名
     */
    private String sign;

    /**
     *
     */
    private Date createTime;

    /**
     *
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

    @Id
    @Column(name = "id")
    public Integer getId(){

        return this.id;
    }
    public void setId(Integer id){

        this.id = id;
    }
    @Column(name = "orderNo")
    public String getOrderNo(){

        return this.orderNo;
    }
    public void setOrderNo(String orderNo){

        this.orderNo = orderNo;
    }
    @Column(name = "payerBankName")
    public String getPayerBankName(){

        return this.payerBankName;
    }
    public void setPayerBankName(String payerBankName){

        this.payerBankName = payerBankName;
    }
    @Column(name = "bankTradeNo")
    public String getBankTradeNo(){

        return this.bankTradeNo;
    }
    public void setBankTradeNo(String bankTradeNo){

        this.bankTradeNo = bankTradeNo;
    }
    @Column(name = "payAmt")
    public Double getPayAmt(){

        return this.payAmt;
    }
    public void setPayAmt(Double payAmt){

        this.payAmt = payAmt;
    }
    @Column(name = "feeAmt")
    public Double getFeeAmt(){

        return this.feeAmt;
    }
    public void setFeeAmt(Double feeAmt){

        this.feeAmt = feeAmt;
    }
    @Column(name = "payTime")
    public Date getPayTime(){

        return this.payTime;
    }
    public void setPayTime(Date payTime){

        this.payTime = payTime;
    }
    @Column(name = "payerName")
    public String getPayerName(){

        return this.payerName;
    }
    public void setPayerName(String payerName){

        this.payerName = payerName;
    }
    @Column(name = "auditStatus")
    public String getAuditStatus(){

        return this.auditStatus;
    }
    public void setAuditStatus(String auditStatus){

        this.auditStatus = auditStatus;
    }
    @Column(name = "auditUserId")
    public String getAuditUserId(){

        return this.auditUserId;
    }
    public void setAuditUserId(String auditUserId){

        this.auditUserId = auditUserId;
    }
    @Column(name = "bankRealName")
    public String getBankRealName(){

        return this.bankRealName;
    }
    public void setBankRealName(String bankRealName){

        this.bankRealName = bankRealName;
    }
    @Column(name = "bankCardNo")
    public String getBankCardNo(){

        return this.bankCardNo;
    }
    public void setBankCardNo(String bankCardNo){

        this.bankCardNo = bankCardNo;
    }
    @Column(name = "depositBankName")
    public String getDepositBankName(){

        return this.depositBankName;
    }
    public void setDepositBankName(String depositBankName){

        this.depositBankName = depositBankName;
    }
    @Column(name = "provinceName")
    public String getProvinceName(){

        return this.provinceName;
    }
    public void setProvinceName(String provinceName){

        this.provinceName = provinceName;
    }
    @Column(name = "cityName")
    public String getCityName(){

        return this.cityName;
    }
    public void setCityName(String cityName){

        this.cityName = cityName;
    }
    @Column(name = "areaName")
    public String getAreaName(){

        return this.areaName;
    }
    public void setAreaName(String areaName){

        this.areaName = areaName;
    }
    @Column(name = "subBankName")
    public String getSubBankName(){

        return this.subBankName;
    }
    public void setSubBankName(String subBankName){

        this.subBankName = subBankName;
    }
    @Column(name = "bankMobile")
    public String getBankMobile(){

        return this.bankMobile;
    }
    public void setBankMobile(String bankMobile){

        this.bankMobile = bankMobile;
    }
    @Column(name = "payCenterNumber")
    public String getPayCenterNumber(){

        return this.payCenterNumber;
    }
    public void setPayCenterNumber(String payCenterNumber){

        this.payCenterNumber = payCenterNumber;
    }
    @Column(name = "batNo")
    public String getBatNo(){

        return this.batNo;
    }
    public void setBatNo(String batNo){

        this.batNo = batNo;
    }
    @Column(name = "memberId")
    public Long getMemberId(){

        return this.memberId;
    }
    public void setMemberId(Long memberId){

        this.memberId = memberId;
    }
    @Column(name = "userType")
    public String getUserType(){

        return this.userType;
    }
    public void setUserType(String userType){

        this.userType = userType;
    }
    @Column(name = "changeTime")
    public Date getChangeTime(){

        return this.changeTime;
    }
    public void setChangeTime(Date changeTime){

        this.changeTime = changeTime;
    }
    @Column(name = "orderNum")
    public Integer getOrderNum(){

        return this.orderNum;
    }
    public void setOrderNum(Integer orderNum){

        this.orderNum = orderNum;
    }
    @Column(name = "commissionNum")
    public Integer getCommissionNum(){

        return this.commissionNum;
    }
    public void setCommissionNum(Integer commissionNum){

        this.commissionNum = commissionNum;
    }
    @Column(name = "dueAmt")
    public Double getDueAmt(){

        return this.dueAmt;
    }
    public void setDueAmt(Double dueAmt){

        this.dueAmt = dueAmt;
    }
    @Column(name = "payStatus")
    public String getPayStatus(){

        return this.payStatus;
    }
    public void setPayStatus(String payStatus){

        this.payStatus = payStatus;
    }
    @Column(name = "comment")
    public String getComment(){

        return this.comment;
    }
    public void setComment(String comment){

        this.comment = comment;
    }
    @Column(name = "sign")
    public String getSign(){

        return this.sign;
    }
    public void setSign(String sign){

        this.sign = sign;
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
    @Column(name = "refundNum")
	public Integer getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(Integer refundNum) {
		this.refundNum = refundNum;
	}
	 @Column(name = "penaltyNum")
	public Integer getPenaltyNum() {
		return penaltyNum;
	}
	public void setPenaltyNum(Integer penaltyNum) {
		this.penaltyNum = penaltyNum;
	}
    
    
}

