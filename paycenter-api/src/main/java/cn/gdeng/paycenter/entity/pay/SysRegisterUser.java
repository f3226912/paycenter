package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description 用户表
 * @Project gd-auth-intf
 * @ClassName SysRegisterUser.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:36:54
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:36:54       初始创建
 */
public class SysRegisterUser implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3945083214949676036L;

	/** 
	* @Fields userIP 登录用户IP
	* @since Ver 1.0
	*/   
	private String userIP;
	
	/** 用户ID */
	private String userID;

	/** 用户登录的帐号Code */
	private String userCode;

	/** 用户名称 */
	private String userName;

	/** 用户密码 */
	private String userPassWord;

	/** 是否锁定(0:未锁定;1:已经锁定) */
	private String locked;

	/** 是否删除(0:未删除;1:已经删除) */
	private String deleted;

	/** 输入密码错误次数 */
	private Integer pwdErrorTimes;

	/** 创建用户ID */
	private String createUserID;

	/** 创建时间 */
	private Date createTime;

	/** 最后更新用户ID */
	private String updateUserID;

	/** 最后更新时间 */
	private Date updateTime;

	/** 删除记录用户ID */
	private String deletedUserID;

	/** 删除记录时间 */
	private Date deletedTime;
	
	/**类型(0系统，1联采中心，2配送站，3学校，4食堂，5基地)**/
	private int type;
	
	/**组织id(不同类型组织的id)**/
	private String orgUnitId;
	
	/**组织名称**/
	private String orgUnitName;
	
	/**用户角色标识字段**/
	private String total;
	
	/**用户类型名称**/
	private String typeName;
	
	/**最后密码输入错误时间**/
	private Date lastErrorTime;
	
	/**输入的密码**/
	private String password;
	
	/**联系电话**/
	private String mobile;
	
	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassWord() {
		return userPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Integer getPwdErrorTimes() {
		return pwdErrorTimes;
	}

	public void setPwdErrorTimes(Integer pwdErrorTimes) {
		this.pwdErrorTimes = pwdErrorTimes;
	}

	public String getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}

	public Date getCreateTime() {
		return this.createTime;

	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserID() {
		return updateUserID;
	}

	public void setUpdateUserID(String updateUserID) {
		this.updateUserID = updateUserID;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDeletedUserID() {
		return deletedUserID;
	}

	public void setDeletedUserID(String deletedUserID) {
		this.deletedUserID = deletedUserID;
	}

	public Date getDeletedTime() {
		return deletedTime;
	}

	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}


	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(String orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public String getOrgUnitName() {
		return orgUnitName;
	}

	public void setOrgUnitName(String orgUnitName) {
		this.orgUnitName = orgUnitName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getLastErrorTime() {
		return lastErrorTime;
	}

	public void setLastErrorTime(Date lastErrorTime) {
		this.lastErrorTime = lastErrorTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	private String orgUnitAddress;

	public String getOrgUnitAddress() {
		return orgUnitAddress;
	}

	public void setOrgUnitAddress(String orgUnitAddress) {
		this.orgUnitAddress = orgUnitAddress;
	}

	@Override
	public String toString() {
		return "SysRegisterUser [userID=" + userID + ", userCode=" + userCode
				+ ", userName=" + userName + ", userPassWord=" + userPassWord
				+ ", locked=" + locked + ", deleted=" + deleted
				+ ", pwdErrorTimes=" + pwdErrorTimes + ", createUserID="
				+ createUserID + ", createTime=" + createTime
				+ ", updateUserID=" + updateUserID + ", updateTime="
				+ updateTime + ", deletedUserID=" + deletedUserID
				+ ", deletedTime=" + deletedTime + ", type=" + type
				+ ", orgUnitId=" + orgUnitId + ", orgUnitName=" + orgUnitName
				+ ", total=" + total + ", typeName=" + typeName
				+ ", lastErrorTime=" + lastErrorTime + ", password=" + password
				+ ", mobile=" + mobile + ", orgUnitAddress=" + orgUnitAddress
				+ "]";
	}
	
	
}
