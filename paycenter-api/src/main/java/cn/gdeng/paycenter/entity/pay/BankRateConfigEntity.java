package cn.gdeng.paycenter.entity.pay;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "bank_rate_config")
public class BankRateConfigEntity  implements java.io.Serializable{

	private static final long serialVersionUID = -2064572969788273057L;

	/**
     *
     */
    private Long id;

    /**
     *支付渠道
     */
    private String payChannel;

    /**
     *卡类型
     */
    private String cardType;

    /**
     *本行/他行
     */
    private String areaBankFlag;

    /**
     *业务类型
     */
    private String type;

    /**
     *费用规则json
     */
    private String feeRuleJson;

    /**
     *备注
     */
    private String remark;
    
    /**
     *状态
     */
    private Integer status;

    @Id
    @Column(name = "id")
    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    @Column(name = "payChannel")
    public String getPayChannel(){

        return this.payChannel;
    }
    public void setPayChannel(String payChannel){

        this.payChannel = payChannel;
    }
    @Column(name = "cardType")
    public String getCardType(){

        return this.cardType;
    }
    public void setCardType(String cardType){

        this.cardType = cardType;
    }
    @Column(name = "areaBankFlag")
    public String getAreaBankFlag(){

        return this.areaBankFlag;
    }
    public void setAreaBankFlag(String areaBankFlag){

        this.areaBankFlag = areaBankFlag;
    }
    @Column(name = "type")
    public String getType(){

        return this.type;
    }
    public void setType(String type){

        this.type = type;
    }
    @Column(name = "feeRuleJson")
    public String getFeeRuleJson(){
 
        return this.feeRuleJson;
    }
    public void setFeeRuleJson(String feeRuleJson){

        this.feeRuleJson = feeRuleJson;
    }
    @Column(name = "remark")
    public String getRemark(){

        return this.remark;
    }
    public void setRemark(String remark){

        this.remark = remark;
    }
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    
    
}

