package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "pay_type")
public class PayTypeEntity  implements java.io.Serializable{
    /** @Fields serialVersionUID : */
	private static final long serialVersionUID = -4988577762800735380L;
	/**
     *
     */
    private Integer id;
    /**
     *支付方式 ALIPAY_H5：支付宝 WEIXIN_APP：微信 POS：POS刷卡
     */
    private String payType;
    /**
     *
     */
    private String payTypeName;
    /**
     *
     */
    private String image;
    /**
     *
     */
    private String bak;
    /**
     *
     */
    private Date createTime;
    /**
     *
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
     *数字签名
     */
    private String sign;
    /**
     *是否要对账(0 否 1是)
     */
    private String hasCheck;
    /**
     *谷登银行卡号
     */
    private String gdBankCardNo;
    /**
     *发卡银行名称
     */
    private String bankName;
    /**
     *联系人
     */
    private String contact;
    /**
     *联系电话
     */
    private String telephone;
    /**
     *联系人邮箱
     */
    private String mail;
    @Id
    @Column(name = "id")
    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    @Column(name = "payType")
    public String getPayType(){
        return this.payType;
    }
    public void setPayType(String payType){
        this.payType = payType;
    }
    @Column(name = "payTypeName")
    public String getPayTypeName(){
        return this.payTypeName;
    }
    public void setPayTypeName(String payTypeName){
        this.payTypeName = payTypeName;
    }
    @Column(name = "image")
    public String getImage(){
        return this.image;
    }
    public void setImage(String image){
        this.image = image;
    }
    @Column(name = "bak")
    public String getBak(){
        return this.bak;
    }
    public void setBak(String bak){
        this.bak = bak;
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
    @Column(name = "hasCheck")
    public String getHasCheck(){
        return this.hasCheck;
    }
    public void setHasCheck(String hasCheck){
        this.hasCheck = hasCheck;
    }
    @Column(name = "gdBankCardNo")
    public String getGdBankCardNo(){
        return this.gdBankCardNo;
    }
    public void setGdBankCardNo(String gdBankCardNo){
        this.gdBankCardNo = gdBankCardNo;
    }
    @Column(name = "bankName")
    public String getBankName(){
        return this.bankName;
    }
    public void setBankName(String bankName){
        this.bankName = bankName;
    }
    @Column(name = "contact")
    public String getContact(){
        return this.contact;
    }
    public void setContact(String contact){
        this.contact = contact;
    }
    @Column(name = "telephone")
    public String getTelephone(){
        return this.telephone;
    }
    public void setTelephone(String telephone){
        this.telephone = telephone;
    }
    @Column(name = "mail")
    public String getMail(){
        return this.mail;
    }
    public void setMail(String mail){
        this.mail = mail;
    }
}

