package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;

import cn.gdeng.paycenter.dto.right.BaseDTO;


/**
 * @Description 菜单Model
 * @Project gd-auth-intf
 * @ClassName SysMenu.java
 * @Author lidong(dli@cnagri-products.com)
 * @CreationDate 2015年10月17日 下午2:35:53
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 */
public class SysMenu extends BaseDTO implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 8863815421785557892L;

    /**
     * 菜单id
     */
    private String menuID;

    /**
     * 菜单编号
     */
    private String menuCode;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 上级菜单名称
     */
    private String parentMenuName;

    /**
     * 菜单组件id
     */
    private String menuModuleID;

    /**
     * 上级菜单的menuCode;
     */
    private String parentMenuCode;

    /**
     * 菜单的按钮
     */
    private String menuButtons;

    /**
     * 菜单actionUrl
     */
    private String actionUrl;

    /**
     * 菜单总数
     */
    private String total;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 菜单名称;
     */
    private String menuTypeName;

    /**
     * 菜单图标样式名称
     */
    private String iconCls;

    /** 修改时间 **/
    private Date updateTime;

    /** 修改 人ID **/
    private String updateUserID;

    private int sort;// 排序，数字越大，菜单排列越靠前
    private int level = 1;// 菜单级别
    private String attribute;// 菜单属性(1后台菜单2前台菜单3数据分类)

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

    public String getParentMenuCode() {
        return parentMenuCode;
    }

    public void setParentMenuCode(String parentMenuCode) {
        this.parentMenuCode = parentMenuCode;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMenuButtons() {
        return menuButtons;
    }

    public void setMenuButtons(String menuButtons) {
        this.menuButtons = menuButtons;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserID() {
        return updateUserID;
    }

    public void setUpdateUserID(String updateUserID) {
        this.updateUserID = updateUserID;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
   
}
