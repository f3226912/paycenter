package cn.gdeng.paycenter.entity.pay;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 角色按钮关联
 * @Project gd-auth-intf
 * @ClassName Sysrolebutton.java
 * @Author lidong(dli@cnagri-products.com)
 * @CreationDate 2015年11月23日 上午11:28:57
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
@Entity
@Table(name = "sysrightbtn", catalog = "gudeng_auth")
public class SysRightBtn implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 411115002516228515L;
	private String id;// 主键
	private String roleId;// 角色ID
	private String userId;// 用户ID
	private String btnId;// 按钮ID
	private String createUserId;// 创建者
	private Date createTime;// 创建时间
	private String updateUserId;// 修改者
	private Date updateTime;// 修改时间

	// Constructors

	/** default constructor */
	public SysRightBtn() {
	}

	/** minimal constructor */
	public SysRightBtn(String id) {
		this.id = id;
	}

	/** full constructor */
	public SysRightBtn(String id, String roleId, String userId, String btnId, String createUserId, Date createTime, String updateUserId, Timestamp updateTime) {
		this.id = id;
		this.roleId = roleId;
		this.userId = userId;
		this.btnId = btnId;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "roleID")
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "userID")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "btnID")
	public String getBtnId() {
		return this.btnId;
	}

	public void setBtnId(String btnId) {
		this.btnId = btnId;
	}

	@Column(name = "createUserID")
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "updateUserID")
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}