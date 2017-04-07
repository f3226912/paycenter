package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.List;

import cn.gdeng.paycenter.dto.right.BaseDTO;


/**   
 * @Description 角色权限分配
 * @Project gd-auth-intf
 * @ClassName SysRoleManager.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:38:30
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:38:30       初始创建
 */
public class SysRoleManager extends BaseDTO implements Serializable{

	private static final long serialVersionUID = 4368628781658521985L;
	
	/**角色菜单关系ID*/
	private String rmID;
	
	/**角色ID*/
	private String roleID;
	
	/**菜单ID*/
	private String menuID;
	
	/**菜单编号*/
	private String menuCode;
	
	/**菜单名称*/
	private String menuName;
	
	/**菜单模块ID*/
	private String menuModuleID;
	
	/**菜单action路径*/
	private String actionUrl;
	
	/**角色是否拥有这个菜单的标志    大于0的话 就表示有这个菜单功能*/
	private String menuTotal;
	
	/**按钮属性（菜单拥有哪些按钮）*/
	private List<SysMenuButton> buttonList;
	
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

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuModuleID() {
		return menuModuleID;
	}

	public void setMenuModuleID(String menuModuleID) {
		this.menuModuleID = menuModuleID;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public List<SysMenuButton> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<SysMenuButton> buttonList) {
		this.buttonList = buttonList;
	}

	public String getMenuTotal() {
		return menuTotal;
	}

	public void setMenuTotal(String menuTotal) {
		this.menuTotal = menuTotal;
	}

	

}
