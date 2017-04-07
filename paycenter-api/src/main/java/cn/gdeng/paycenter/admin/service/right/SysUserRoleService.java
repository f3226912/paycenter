package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysUserRole;

/**   
 * @Description 系统用户接口
 * @Project gd-auth-intf
 * @ClassName SysUserRoleService.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:43:10
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
public interface SysUserRoleService {
	
	/**
	 * 根据角色ID查询用户角色List
	 * @param map
	 * @return
	 */
	public List<SysUserRole> getUserRoleList(Map<String, Object> map);

	/**
	 * 依据传入的参数取得对象;
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<SysUserRole> getListSysUserRole(Map<String, Object> map);

	/**
	 * 查询单个角色
	 * 
	 * @param logId
	 *            用户ID
	 * @return
	 */
	public SysUserRole get(String userID);

	/**
	 * 新增一个用户角色信息
	 * 
	 * @param SysUserRole
	 *            用户实体
	 */
	public void insert(SysUserRole sysUserRole);
	
	/**
	 * 批量新增用户角色
	 * 
	 * @param userIDs 用户ID集合
	 * @param roleID 角色ID
	 * @param createUserID 操作用ID
	 */
	public void insertBatch(String userIDs,String roleID,String createUserID) throws Exception;

	/**
	 * 修改一个用户角色信息
	 * 
	 * @param SysUserRole
	 *            用户实体
	 */
	public void update(SysUserRole sysUserRole);

	/**
	 * 删除一个用户角色信息
	 * 
	 * @param logId
	 */
	public void delete(String userID);
	
	/**
	 * 删除一个用户的角色
	 * @param userID 用户ID
	 */ 
	public void deleteByUserID(String userID);
	
	/**
	 * 批量删除用户角色信息
	 * 
	 * @param logId
	 */
	public void batchDelete(List<String> userIdlist);

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	public List<SysUserRole> getByCondition(Map<String, Object> map);

	/**
	 * 记录总数
	 * 
	 * @return
	 */
	public Integer getTotal(Map<String, Object> map);
	
	/**
	 * 查询所有系统用户，会返回每个用户角色个数
	 * @author songhui
	 * @date 创建时间：2015年7月28日 上午9:34:59
	 * @param map
	 * @return
	 *
	 */
	public List<SysUserRole> getSysUserList(Map<String,Object> map)throws Exception;
	
	/**
	 * 查询要分配用户的数量
	 * @author songhui
	 * @date 创建时间：2015年7月31日 下午2:47:30
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 */
	public int getSysUserCount(Map<String,Object> map)throws Exception;
	
	/**
	 * 查询用户所有已分配未分配角色返回已分配标识
	 * @author songhui
	 * @date 创建时间：2015年8月4日 上午10:46:55
	 * @param map
	 * @return
	 *
	 */
	public List<SysUserRole> getUserAllRoleList(Map<String,Object> map);
	
	/**
	 * 角色数量
	 * @author songhui
	 * @date 创建时间：2015年8月4日 上午10:46:55
	 * @param map
	 * @return
	 *
	 */
	public int getUserAllRoleCount(Map<String,Object> map);
	
	/**
	 * 批量分配用户角色
	 * @author songhui
	 * @date 创建时间：2015年8月4日 上午11:13:25
	 * @param userID
	 * @param roleIDs
	 * @param createUserID
	 * @throws Exception
	 *
	 */
	public void insertUserRoleBatch(String userID,String roleIDs,String createUserID) throws Exception;

}
