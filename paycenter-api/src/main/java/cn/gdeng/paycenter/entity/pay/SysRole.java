package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 角色表
 * @Project gd-auth-intf
 * @ClassName SysRole.java
 * @Author lidong(dli@cnagri-products.com)
 * @CreationDate 2015年10月17日 下午2:38:13
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory Who When What -------- ------------------------- ----------------------------------- lidong 2015年10月17日 下午2:38:13 初始创建
 */
public class SysRole implements Serializable {

    /**
     * 序列号;
     */
    private static final long serialVersionUID = 9137184630373309702L;

    /** 角色ID */
    private String roleID;

    /** 角色Name */
    private String roleName;

    /** 备注 */
    private String remark;

    /** 创建人ID */
    private String createUserID;

    /** 创建时间 */
    private Date createTime;

    /** 修改人ID */
    private String updateUserID;

    /** 修改时间 */
    private Date updateTime;

    /** 记录总数 */
    private String total;
    private String attribute;

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

}
