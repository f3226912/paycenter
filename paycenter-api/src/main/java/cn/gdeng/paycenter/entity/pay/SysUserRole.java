package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description 用户角色
 * @Project gd-auth-intf
 * @ClassName SysUserRole.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:39:32
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:39:32       初始创建
 */
public class SysUserRole implements Serializable {

	/**
	 * 序列号;
	 */
	private static final long serialVersionUID = -4848623906244405132L;
	/** 主键ID */
	private String urID;
	/** 角色ID(SysRole.roleID) */
	private String roleID;
	/** 用户ID(SysRegisterUser.userID) */
	private String userID;
	private String attribute;
	/** 创建用户ID */
	private String createUserID;
	/** 创建时间 */
	private Date createTime;
	
	/** 用户名 */
	private String userName;
	/** 角色名*/
	private String roleName;
	
	/**被分配角色用户则选中标记*/
	private String isAuth;
	
	/**用户编号**/
	private String userCode;
	
	/**用户状态**/
	private String locked;
	
	/**组织名称**/
	private String orgUnitName;
	
	/**备注**/
	private String remark;
	
	/**可以进行分配的标识**/
	private String canAssign;
	
	public String getUrID() {
		return urID;
	}

	public void setUrID(String urID) {
		this.urID = urID;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private String total;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getOrgUnitName() {
		return orgUnitName;
	}

	public void setOrgUnitName(String orgUnitName) {
		this.orgUnitName = orgUnitName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCanAssign() {
		return canAssign;
	}

	public void setCanAssign(String canAssign) {
		this.canAssign = canAssign;
	}

}
