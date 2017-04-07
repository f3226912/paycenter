package cn.gdeng.paycenter.entity.pay;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "access_sys_config")
public class AccessSysConfigEntity  implements java.io.Serializable{

	private static final long serialVersionUID = 3774152199429361830L;

	private Integer id;

    private String appKey;

    private String name;

    private String keyType;

    private String publicKey;

    private String privateKey;

    private String sysPublicKey;

    private String md5Key;

    private String status;
    
    private String mqTopic;

    private Date createTime;

    private String createUserId;

    private Date updateTime;

    private String updateUserId;

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
    @Column(name = "name")
    public String getName(){

        return this.name;
    }
    public void setName(String name){

        this.name = name;
    }
    @Column(name = "keyType")
    public String getKeyType(){

        return this.keyType;
    }
    public void setKeyType(String keyType){

        this.keyType = keyType;
    }
    @Column(name = "publicKey")
    public String getPublicKey(){

        return this.publicKey;
    }
    public void setPublicKey(String publicKey){

        this.publicKey = publicKey;
    }
    @Column(name = "privateKey")
    public String getPrivateKey(){

        return this.privateKey;
    }
    public void setPrivateKey(String privateKey){

        this.privateKey = privateKey;
    }
    @Column(name = "sysPublicKey")
    public String getSysPublicKey(){

        return this.sysPublicKey;
    }
    public void setSysPublicKey(String sysPublicKey){

        this.sysPublicKey = sysPublicKey;
    }
    @Column(name = "md5Key")
    public String getMd5Key(){

        return this.md5Key;
    }
    public void setMd5Key(String md5Key){

        this.md5Key = md5Key;
    }
    @Column(name = "status")
    public String getStatus(){

        return this.status;
    }
    public void setStatus(String status){

        this.status = status;
    }    
    
    @Column(name = "mqTopic")
    public String getMqTopic() {
		return mqTopic;
	}
    
	public void setMqTopic(String mqTopic) {
		this.mqTopic = mqTopic;
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

