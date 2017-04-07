package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "market_bank_acc_info")
public class MarketBankAccInfoEntity  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4017393764449889038L;

	/**
    *
    */
    private Long id;

    /**
    *市场ID
    */
    private Long marketId;

    /**
    *市场名称
    */
    private String marketName;

    /**
    *会员id
    */
    private Long memberId;
    /**
    *持卡人姓名
    */
    private String realName;

    /**
    *开户行名称
    */
    private String depositBankName;

    /**
    *开户所在地（省）
    */
    private Long provinceId;

    /**
    *开户所在地（市）
    */
    private Long cityId;

    /**
    *开户所在地
    */
    private Long areaId;

    /**
    *支行名称
    */
    private String subBankName;

    /**
    *银行卡号
    */
    private String bankCardNo;

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
    
    /**
    *  数字签名
    */
    private String sign;
    
    /**
    *  开户行所在地
    */
    private String addr;
    
    /**
    *  银行预留手机号
    */
    private String reservePhone;
    
    /**
    *  是否被删除
    */
    private Integer isDeleted; 

    @Id
    @Column(name = "id")
    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    
    @Column(name = "marketId")
    public Long getMarketId(){

        return this.marketId;
    }
    public void setMarketId(Long marketId){

        this.marketId = marketId;
    }
    
    @Column(name = "marketName")
    public String getMarketName(){

        return this.marketName;
    }
    public void setMarketName(String marketName){

        this.marketName = marketName;
    }
    
    @Column(name = "memberId")
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
    
    @Column(name = "realName")
    public String getRealName(){

        return this.realName;
    }
    public void setRealName(String realName){

        this.realName = realName;
    }
    
    @Column(name = "depositBankName")
    public String getDepositBankName(){

        return this.depositBankName;
    }
    public void setDepositBankName(String depositBankName){

        this.depositBankName = depositBankName;
    }
    
    @Column(name = "provinceId")
    public Long getProvinceId(){

        return this.provinceId;
    }
    public void setProvinceId(Long provinceId){

        this.provinceId = provinceId;
    }
    
    @Column(name = "cityId")
    public Long getCityId(){

        return this.cityId;
    }
    public void setCityId(Long cityId){

        this.cityId = cityId;
    }
    
    @Column(name = "areaId")
    public Long getAreaId(){

        return this.areaId;
    }
    public void setAreaId(Long areaId){

        this.areaId = areaId;
    }
    
    @Column(name = "subBankName")
    public String getSubBankName(){

        return this.subBankName;
    }
    public void setSubBankName(String subBankName){

        this.subBankName = subBankName;
    }
    
    @Column(name = "bankCardNo")
    public String getBankCardNo(){

        return this.bankCardNo;
    }
    public void setBankCardNo(String bankCardNo){

        this.bankCardNo = bankCardNo;
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	@Column(name = "addr")
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Column(name = "reservePhone")
	public String getReservePhone() {
		return reservePhone;
	}
	public void setReservePhone(String reservePhone) {
		this.reservePhone = reservePhone;
	}
	
	@Column(name = "isDeleted")
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
    
}
