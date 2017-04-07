package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "pay_trade_pos")
public class PayTradePosEntity  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;

    private String payCenterNumber;

    private String posClientNo;

    private String payChannelCode;
    
    private String bankCardNo;

    private String bankType;

    private String cardType;

    private String areaType;

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
    @Column(name = "posClientNo")
    public String getPosClientNo(){

        return this.posClientNo;
    }
    public void setPosClientNo(String posClientNo){

        this.posClientNo = posClientNo;
    }
    @Column(name = "payChannelCode")
    public String getPayChannelCode(){

        return this.payChannelCode;
    }
    public void setPayChannelCode(String payChannelCode){

        this.payChannelCode = payChannelCode;
    }
    @Column(name = "bankType")
    public String getBankType(){

        return this.bankType;
    }
    public void setBankType(String bankType){

        this.bankType = bankType;
    }
    @Column(name = "cardType")
    public String getCardType(){

        return this.cardType;
    }
    public void setCardType(String cardType){

        this.cardType = cardType;
    }
    @Column(name = "areaType")
    public String getAreaType(){

        return this.areaType;
    }
    public void setAreaType(String areaType){

        this.areaType = areaType;
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
    @Column(name = "bankCardNo")
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
    
    
}

