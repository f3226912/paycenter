package cn.gdeng.paycenter.entity.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 用户银行卡信息
 * @author Ailen
 *
 */
@Entity(name = "member_bankcard_info")
public class MemberBankcardInfoEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4516053839737428844L;

	private Integer id;
	
	private Integer infoId;

	private Integer memberId;

	private String realName;

	private String idCard;

	private String bankId;

	private String depositBankName;

	private String subBankName;

	private String bankCardNo;

	private Integer provinceId;

	private Integer cityId;

	private Integer areaId;
	
	private String account; //账号
	
	private String mobile; //银行预留手机号
	
	private String provinceName; //省
	
	private String cityName; //市
	
	private String areaName; //区
	
	private String telephone; //手机号

	private Integer accCardType;

	private String status;

	private String auditStatus;

	private Date createTime;

	private String createUserId;

	private Date updateTime;

	private String updateUserId;
	
	private Date auditTime;
	
	private Date commitTime;
	
	private Integer regtype; //注册来源
	
	@Column(name = "regtype")
	public Integer getRegtype() {
		return regtype;
	}

	public void setRegtype(Integer regtype) {
		this.regtype = regtype;
	}

	@Column(name = "commitTime")
	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	@Column(name = "id")
	public Integer getId() {

		return this.id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	@Column(name = "infoId")
	public Integer getInfoId() {
		return infoId;
	}

	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	@Column(name = "account")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "memberId")
	public Integer getMemberId() {

		return this.memberId;
	}

	public void setMemberId(Integer memberId) {

		this.memberId = memberId;
	}

	@Column(name = "realName")
	public String getRealName() {

		return this.realName;
	}

	public void setRealName(String realName) {

		this.realName = realName;
	}

	@Column(name = "idCard")
	public String getIdCard() {

		return this.idCard;
	}

	public void setIdCard(String idCard) {

		this.idCard = idCard;
	}

	@Column(name = "auditTime")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	@Column(name = "bankId")
	public String getBankId() {

		return this.bankId;
	}

	public void setBankId(String bankId) {

		this.bankId = bankId;
	}

	@Column(name = "depositBankName")
	public String getDepositBankName() {

		return this.depositBankName;
	}

	public void setDepositBankName(String depositBankName) {

		this.depositBankName = depositBankName;
	}

	@Column(name = "subBankName")
	public String getSubBankName() {

		return this.subBankName;
	}

	public void setSubBankName(String subBankName) {

		this.subBankName = subBankName;
	}

	@Column(name = "bankCardNo")
	public String getBankCardNo() {

		return this.bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {

		this.bankCardNo = bankCardNo;
	}

	@Column(name = "provinceId")
	public Integer getProvinceId() {

		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {

		this.provinceId = provinceId;
	}

	@Column(name = "cityId")
	public Integer getCityId() {

		return this.cityId;
	}

	public void setCityId(Integer cityId) {

		this.cityId = cityId;
	}

	@Column(name = "areaId")
	public Integer getAreaId() {

		return this.areaId;
	}
	
	public void setAreaId(Integer areaId) {

		this.areaId = areaId;
	}
	
	@Column(name = "telephone")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "provinceName")
	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	@Column(name = "cityName")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Column(name = "areaName")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "accCardType")
	public Integer getAccCardType() {

		return this.accCardType;
	}

	public void setAccCardType(Integer accCardType) {

		this.accCardType = accCardType;
	}

	@Column(name = "status")
	public String getStatus() {

		return this.status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	@Column(name = "auditStatus")
	public String getAuditStatus() {

		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {

		this.auditStatus = auditStatus;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {

		return this.createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	@Column(name = "createUserId")
	public String getCreateUserId() {

		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {

		this.createUserId = createUserId;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {

		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {

		this.updateTime = updateTime;
	}

	@Column(name = "updateUserId")
	public String getUpdateUserId() {

		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {

		this.updateUserId = updateUserId;
	}
}
