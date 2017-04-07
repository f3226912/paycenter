package cn.gdeng.paycenter.entity.pay;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "refund_record")
public class RefundRecordEntity  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;

    private String appKey;

    private String orderNo;

    private String payCenterNumber;

    private String refundNo;

    private String refundRequestNo;

    private String thirdRefundNo;

    private String thirdRefundRequestNo;

    private String refundUserId;

    private String refundType;

    private Double refundAmt;

    private String refundReason;

    private Date refundTime;

    private String extendJson;

    private String status;

    private Date createTime;

    private String createUserId;

    private Date updateTime;

    private String updateUserId;

    private String sign;

    @Id
    @Column(name = "id")
    public Integer getId(){

        return this.id;
    }
    public void setId(Integer id){

        this.id = id;
    }
    @Column(name = "appKey")
    public String getAppKey(){

        return this.appKey;
    }
    public void setAppKey(String appKey){

        this.appKey = appKey;
    }
    @Column(name = "orderNo")
    public String getOrderNo(){

        return this.orderNo;
    }
    public void setOrderNo(String orderNo){

        this.orderNo = orderNo;
    }
    @Column(name = "payCenterNumber")
    public String getPayCenterNumber(){

        return this.payCenterNumber;
    }
    public void setPayCenterNumber(String payCenterNumber){

        this.payCenterNumber = payCenterNumber;
    }
    @Column(name = "refundNo")
    public String getRefundNo(){

        return this.refundNo;
    }
    public void setRefundNo(String refundNo){

        this.refundNo = refundNo;
    }
    @Column(name = "refundRequestNo")
    public String getRefundRequestNo(){

        return this.refundRequestNo;
    }
    public void setRefundRequestNo(String refundRequestNo){

        this.refundRequestNo = refundRequestNo;
    }
    @Column(name = "thirdRefundNo")
    public String getThirdRefundNo(){

        return this.thirdRefundNo;
    }
    public void setThirdRefundNo(String thirdRefundNo){

        this.thirdRefundNo = thirdRefundNo;
    }
    @Column(name = "thirdRefundRequestNo")
    public String getThirdRefundRequestNo(){

        return this.thirdRefundRequestNo;
    }
    public void setThirdRefundRequestNo(String thirdRefundRequestNo){

        this.thirdRefundRequestNo = thirdRefundRequestNo;
    }
    @Column(name = "refundUserId")
    public String getRefundUserId(){

        return this.refundUserId;
    }
    public void setRefundUserId(String refundUserId){

        this.refundUserId = refundUserId;
    }
    @Column(name = "refundType")
    public String getRefundType(){

        return this.refundType;
    }
    public void setRefundType(String refundType){

        this.refundType = refundType;
    }
    @Column(name = "refundAmt")
    public Double getRefundAmt(){

        return this.refundAmt;
    }
    public void setRefundAmt(Double refundAmt){

        this.refundAmt = refundAmt;
    }
    @Column(name = "refundReason")
    public String getRefundReason(){

        return this.refundReason;
    }
    public void setRefundReason(String refundReason){

        this.refundReason = refundReason;
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
    @Column(name = "sign")
    public String getSign(){

        return this.sign;
    }
    public void setSign(String sign){

        this.sign = sign;
    }
}

