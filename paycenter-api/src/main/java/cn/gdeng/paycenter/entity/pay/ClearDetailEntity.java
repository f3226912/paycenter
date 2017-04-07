package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "clear_detail")
public class ClearDetailEntity  implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3361942805799989808L;

	/**
    *
    */
    private Long id;

    /**
    *订单号
    */
    private Long orderNo;

    /**
    *会员ID
    */
    private Long memberId;

    /**
     *业务分类(trade交易 refund退款)
     */
     private String busiCategory;
     
    /**
    *用户类型(nsy农商友、nps农批商、market市场、plat平台、nst_car农速通-车主、nst_goods农速通货主、logis物流公司)
    */
    private String userType;

    /**
    *费用类型(100001货款、100002佣金、100003手续费、100004补贴)
    */
    private String feeType;

    /**
    *收支类型(1收 2支)
    */
    private String szType;

    /**
    *收支金额
    */
    private Double tradeAmt;

    /**
    *清算时间
    */
    private Date clearTime;

    /**
    *结算批次号 yyyymmdd-n
    */
    private String batNo;

    /**
    *是否转结算(1是 0否)
    */
    private String hasChange;

    /**
    *说明
    */
    private String comment;

    /**
    *是否有效(1有效 2无效)
    */
    private String isValid;

    /**
    *签名
    */
    private String sign;

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

    @Id
    @Column(name = "id")
    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    @Column(name = "orderNo")
    public Long getOrderNo(){

        return this.orderNo;
    }
    public void setOrderNo(Long orderNo){

        this.orderNo = orderNo;
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
    @Column(name = "feeType")
    public String getFeeType(){

        return this.feeType;
    }
    public void setFeeType(String feeType){

        this.feeType = feeType;
    }
    @Column(name = "busiCategory")
    public String getBusiCategory(){

        return this.busiCategory;
    }
    public void setBusiCategory(String busiCategory){

        this.busiCategory = busiCategory;
    }
    @Column(name = "szType")
    public String getSzType(){

        return this.szType;
    }
    public void setSzType(String szType){

        this.szType = szType;
    }
    @Column(name = "tradeAmt")
    public Double getTradeAmt(){

        return this.tradeAmt;
    }
    public void setTradeAmt(Double tradeAmt){

        this.tradeAmt = tradeAmt;
    }
    @Column(name = "clearTime")
    public Date getClearTime(){

        return this.clearTime;
    }
    public void setClearTime(Date clearTime){

        this.clearTime = clearTime;
    }
    @Column(name = "batNo")
    public String getBatNo(){

        return this.batNo;
    }
    public void setBatNo(String batNo){

        this.batNo = batNo;
    }
    @Column(name = "hasChange")
    public String getHasChange(){

        return this.hasChange;
    }
    public void setHasChange(String hasChange){

        this.hasChange = hasChange;
    }
    @Column(name = "comment")
    public String getComment(){

        return this.comment;
    }
    public void setComment(String comment){

        this.comment = comment;
    }
    @Column(name = "isValid")
    public String getIsValid(){

        return this.isValid;
    }
    public void setIsValid(String isValid){

        this.isValid = isValid;
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
}

