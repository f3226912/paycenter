package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**   
 * @Description 菜单按钮
 * @Project gd-auth-intf
 * @ClassName SysMenuButton.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:37:20
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:37:20       初始创建
 */
public class SysMenuButton implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 701712340016297512L;

	/**
	 * 菜单组件id
	 */
	private String btnID;

	/**
	 * 菜单组件编号
	 */
	private String btnCode;

	/**
	 * 菜单组件名称
	 */
	private String btnName;

	/**
	 * 菜单id
	 */
	private String menuID;

	/**
	 * 新建用户id
	 */
	private String createUserID;

	/**
	 * 新建时间
	 */
	private Date createTime;

	private String total;

	private List<SysMenuButton> children;
	/**
	 * 用于礼品卡发放 业务button是否选中标识
	 */
	private boolean flag;
	public List<SysMenuButton> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuButton> children) {
        this.children = children;
    }

    public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getBtnID() {
		return btnID;
	}

	public void setBtnID(String btnID) {
		this.btnID = btnID;
	}

	public String getBtnCode() {
		return btnCode;
	}

	public void setBtnCode(String btnCode) {
		this.btnCode = btnCode;
	}

	public String getBtnName() {
		return btnName;
	}

	public void setBtnName(String btnName) {
		this.btnName = btnName;
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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "btnID=" + btnID + ";menuID=" + menuID;
	}
}
