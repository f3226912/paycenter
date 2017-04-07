package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "bill_trans_detail")
public class BillTransDetailEntity  implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     *主键
     */
    private Long id;

    /**
     *POS终端号
     */
    private String clientNo;

    /**
     *POS商户号
     */
    private String businessNo;

    /**
     *交易类型
     */
    private String tradeType;

    /**
     *第三方支付流水号
     */
    private String thirdTransNo;

    /**
     *交易金额
     */
    private Double tradeMoney;

    /**
     *手续费
     */
    private Double fee;

    /**
     *交易卡号
     */
    private String cardNo;

    /**
     *交易时间
     */
    private Date payTime;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *创建用户
     */
    private String createUserId;

    @Id
    @Column(name = "id")
    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    @Column(name = "clientNo")
    public String getClientNo(){

        return this.clientNo;
    }
    public void setClientNo(String clientNo){

        this.clientNo = clientNo;
    }
    @Column(name = "businessNo")
    public String getBusinessNo(){

        return this.businessNo;
    }
    public void setBusinessNo(String businessNo){

        this.businessNo = businessNo;
    }
    @Column(name = "tradeType")
    public String getTradeType(){

        return this.tradeType;
    }
    public void setTradeType(String tradeType){

        this.tradeType = tradeType;
    }
    @Column(name = "thirdTransNo")
    public String getThirdTransNo(){

        return this.thirdTransNo;
    }
    public void setThirdTransNo(String thirdTransNo){

        this.thirdTransNo = thirdTransNo;
    }
    @Column(name = "tradeMoney")
    public Double getTradeMoney(){

        return this.tradeMoney;
    }
    public void setTradeMoney(Double tradeMoney){

        this.tradeMoney = tradeMoney;
    }
    @Column(name = "fee")
    public Double getFee(){

        return this.fee;
    }
    public void setFee(Double fee){

        this.fee = fee;
    }
    @Column(name = "cardNo")
    public String getCardNo(){

        return this.cardNo;
    }
    public void setCardNo(String cardNo){

        this.cardNo = cardNo;
    }
    @Column(name = "payTime")
    public Date getPayTime(){

        return this.payTime;
    }
    public void setPayTime(Date payTime){

        this.payTime = payTime;
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
}

