package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description 角色菜单
 * @Project gd-auth-intf
 * @ClassName SysRoleMenu.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:39:03
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:39:03       初始创建
 */
public class SysRoleMenu implements Serializable{

	/**
	 * 序列号;
	 */
	private static final long serialVersionUID = -4848623906244405132L;

	/**标识*/
	private String rmID;
	
	/**角色ID*/
	private String roleID;
	
	/**菜单ID*/
	private String menuID;

	/**创建人ID*/
	private String createUserID;
	
	/**创建时间*/
	private Date createTime;
	
	private String total;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getRmID() {
		return rmID;
	}

	public void setRmID(String rmID) {
		this.rmID = rmID;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getMenuID() {
		return menuID;
	}

	public void setMenuID(String menuID) {
		this.menuID = menuID;
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
	
}
