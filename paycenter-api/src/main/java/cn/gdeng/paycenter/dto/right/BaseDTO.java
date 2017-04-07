package cn.gdeng.paycenter.dto.right;

import java.util.Date;

/**   
 * @Description dto基础信息字段
 * @Project gd-auth-intf
 * @ClassName BaseDTO.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:31:09
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:31:09      初始创建
 */
public abstract class BaseDTO {
	
	/***创建用户ID**/
    protected String createUserID;
    /***创建用户名**/
    protected String createUserName;
    /***创建日期**/
    protected Date createTime;
    /***/
    protected String createTimes;
    /***修改人ID**/
    protected String updateUserID;
    /***修改人名称**/
    protected String updateUserName;
    /***修改日期**/
    protected Date updateTime;
    /***/
    protected String updateTimes;
    
    
    private String id;
    
    private String text;
    
	public String getCreateUserID() {
		return createUserID;
	}
	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimes() {
		return createTimes;
	}
	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}
	public String getUpdateUserID() {
		return updateUserID;
	}
	public void setUpdateUserID(String updateUserID) {
		this.updateUserID = updateUserID;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateTimes() {
		return updateTimes;
	}
	public void setUpdateTimes(String updateTimes) {
		this.updateTimes = updateTimes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	   
}
