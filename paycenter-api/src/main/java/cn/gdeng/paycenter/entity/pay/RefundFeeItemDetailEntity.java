package cn.gdeng.paycenter.entity.pay;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "refund_fee_item_detail")
public class RefundFeeItemDetailEntity  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;

    private String refundNo;

    private String feeType;

    private Double feeAmt;

    private String clearState;

    private Date remitTime;

    private Date createTime;

    private String createUser;

    @Id
    @Column(name = "id")
    public Integer getId(){

        return this.id;
    }
    public void setId(Integer id){

        this.id = id;
    }
    @Column(name = "refundNo")
    public String getRefundNo(){

        return this.refundNo;
    }
    public void setRefundNo(String refundNo){

        this.refundNo = refundNo;
    }
    @Column(name = "feeType")
    public String getFeeType(){

        return this.feeType;
    }
    public void setFeeType(String feeType){

        this.feeType = feeType;
    }
    @Column(name = "feeAmt")
    public Double getFeeAmt(){

        return this.feeAmt;
    }
    public void setFeeAmt(Double feeAmt){

        this.feeAmt = feeAmt;
    }
    @Column(name = "clearState")
    public String getClearState(){

        return this.clearState;
    }
    public void setClearState(String clearState){

        this.clearState = clearState;
    }
    @Column(name = "remitTime")
    public Date getRemitTime(){

        return this.remitTime;
    }
    public void setRemitTime(Date remitTime){

        this.remitTime = remitTime;
    }
    @Column(name = "createTime")
    public Date getCreateTime(){

        return this.createTime;
    }
    public void setCreateTime(Date createTime){

        this.createTime = createTime;
    }
    @Column(name = "createUser")
    public String getCreateUser(){

        return this.createUser;
    }
    public void setCreateUser(String createUser){

        this.createUser = createUser;
    }
}

