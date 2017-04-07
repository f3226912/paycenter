package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "pos_pay_notify")
public class PosPayNotifyEntity  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;

    private String payCenterNumber;

    private String businessNo;

    private String transType;

    private String orderNo;

    private Double orderAmt;

    private Double payAmt;

    private Double rateAmt;

    private String bankCardNo;

    private String posClientNo;

    private Date transDate;

    private String transNo;

    private String payChannelCode;

    private String status;
    
	private String appKey;

    private Date createTime;

    private String createUserId;

    private Date updateTime;

    private String updateUserId;

    @Column(name = "id")
    @Id
    public Integer getId(){

        return this.id;
    }
    public void setId(Integer id){

        this.id = id;
    }
    @Column(name = "payCenterNumber")
    public String getPayCenterNumber(){

        return this.payCenterNumber;
    }
    public void setPayCenterNumber(String payCenterNumber){

        this.payCenterNumber = payCenterNumber;
    }
    @Column(name = "businessNo")
    public String getBusinessNo(){

        return this.businessNo;
    }
    public void setBusinessNo(String businessNo){

        this.businessNo = businessNo;
    }
    @Column(name = "transType")
    public String getTransType(){

        return this.transType;
    }
    public void setTransType(String transType){

        this.transType = transType;
    }
    @Column(name = "orderNo")
    public String getOrderNo(){

        return this.orderNo;
    }
    public void setOrderNo(String orderNo){

        this.orderNo = orderNo;
    }
    @Column(name = "orderAmt")
    public Double getOrderAmt(){

        return this.orderAmt;
    }
    public void setOrderAmt(Double orderAmt){

        this.orderAmt = orderAmt;
    }
    @Column(name = "payAmt")
    public Double getPayAmt(){

        return this.payAmt;
    }
    public void setPayAmt(Double payAmt){

        this.payAmt = payAmt;
    }
    @Column(name = "rateAmt")
    public Double getRateAmt(){

        return this.rateAmt;
    }
    public void setRateAmt(Double rateAmt){

        this.rateAmt = rateAmt;
    }
    @Column(name = "bankCardNo")
    public String getBankCardNo(){

        return this.bankCardNo;
    }
    public void setBankCardNo(String bankCardNo){

        this.bankCardNo = bankCardNo;
    }
    @Column(name = "posClientNo")
    public String getPosClientNo(){

        return this.posClientNo;
    }
    public void setPosClientNo(String posClientNo){

        this.posClientNo = posClientNo;
    }
    @Column(name = "transDate")
    public Date getTransDate(){

        return this.transDate;
    }
    public void setTransDate(Date transDate){

        this.transDate = transDate;
    }
    @Column(name = "transNo")
    public String getTransNo(){

        return this.transNo;
    }
    public void setTransNo(String transNo){

        this.transNo = transNo;
    }
    @Column(name = "payChannelCode")
    public String getPayChannelCode(){

        return this.payChannelCode;
    }
    public void setPayChannelCode(String payChannelCode){

        this.payChannelCode = payChannelCode;
    }
    @Column(name = "status")
    public String getStatus(){

        return this.status;
    }
    public void setStatus(String status){

        this.status = status;
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
    
    @Column(name = "appKey")
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}

