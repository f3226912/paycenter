package cn.gdeng.paycenter.entity.pay;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "pos_machine_config")
public class PosMachineConfigEntity  implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -478210312572919687L;

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
    *终端号
    */
    private String machineNum;

    /**
    *商铺ID
    */
    private Long businessId;

    /**
    *商铺名称
    */
    private String shopsName;

    /**
    *用户类型
    */
    private String userType;

    /**
    *用户帐号
    */
    private Long memberId;

    /**
    *是否清算:1是 0否
    */
    private String hasClear;

    /**
    *创建时间
    */
    private String createTime;

    /**
    *创建用户
    */
    private String createUserId;

    /**
    *
    */
    private String updateTime;

    /**
    *
    */
    private String updateUserId;
    
    /**
    *状态 1有效 0无效
    */
    private String state;

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
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	
    @Column(name = "machineNum")
    public String getMachineNum(){

        return this.machineNum;
    }
    public void setMachineNum(String machineNum){

        this.machineNum = machineNum;
    }
    
    @Column(name = "businessId")
    public Long getBusinessId(){

        return this.businessId;
    }
    public void setBusinessId(Long businessId){

        this.businessId = businessId;
    }
    
    @Column(name = "shopsName")
    public String getShopsName(){

        return this.shopsName;
    }
    public void setShopsName(String shopsName){

        this.shopsName = shopsName;
    }
    
    @Column(name = "userType")
    public String getUserType(){

        return this.userType;
    }
    public void setUserType(String userType){

        this.userType = userType;
    }
    
    @Column(name = "memberId")
    public Long getMemberId(){

        return this.memberId;
    }
    public void setMemberId(Long memberId){

        this.memberId = memberId;
    }
        
    @Column(name = "hasClear")
    public String getHasClear(){

        return this.hasClear;
    }
    public void setHasClear(String hasClear){

        this.hasClear = hasClear;
    }
    
    @Column(name = "createTime")
    public String getCreateTime(){

        return this.createTime;
    }
    public void setCreateTime(String createTime){

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
    public String getUpdateTime(){

        return this.updateTime;
    }
    public void setUpdateTime(String updateTime){

        this.updateTime = updateTime;
    }
    
    @Column(name = "updateUserId")
    public String getUpdateUserId(){

        return this.updateUserId;
    }
    public void setUpdateUserId(String updateUserId){

        this.updateUserId = updateUserId;
    }
    @Column(name = "state")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}

