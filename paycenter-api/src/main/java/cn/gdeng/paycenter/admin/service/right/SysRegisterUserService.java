package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysRegisterUser;

/**   
 * @Description 用户接口
 * @Project gd-auth-intf
 * @ClassName SysRegisterUserService.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:42:03
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
/**   
 * @Description TODO
 * @Project info-customer-intf
 * @ClassName SysRegisterUserService.java
 * @Author lidong(dli@gdeng.cn)    
 * @CreationDate 2016年3月1日 上午11:31:32
 * @Version V2.0
 * @Copyright 谷登科技 2015-2016
 * @ModificationHistory
 */
public interface SysRegisterUserService {

	/**
	 * 依据传入的参数取得对象;
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<SysRegisterUser> getListSysRegisterUser(Map<String,Object> map)
			throws Exception;

	/**
	 * 批量更新
	 * 
	 * @param ids
	 *            更新集合
	 * @param userId
	 * @param sqlMap
	 *            mybatis对应的ID
	 * @throws Exception 
	 */
	public void updateBatch(String ids,String userId) throws Exception;
	
	/**
	 * 查询单个用户 
	 * @param logId 用户ID
	 * @return
	 */
	public SysRegisterUser get(String userID) ;
	
	/**
	 * 新增一个用户信息
	 * @param SysRegisterUser 用户实体
	 * @throws Exception 
	 */
	public String insert(SysRegisterUser sysRegisterUser) throws Exception ;
	
	/**
	 * 修改一个用户信息
	 * @param SysRegisterUser 用户实体
	 * @throws Exception 
	 */
	public String update(SysRegisterUser sysRegisterUser) throws Exception ;
	
	/**
	 * 删除一个用户信息
	 * @param logId
	 */
	public void delete(String userID);
	
	/**
	 * 分页查询
	 * @return
	 */
	public List<SysRegisterUser> getByCondition(Map<String,Object> map);
	
	/**
	 * 记录总数
	 * @return
	 */
	public Integer getTotal(Map<String,Object> map);
	
	/**
	 * 锁定用户
	 * @param user
	 * @return
	 */
	public int updateLockUser(SysRegisterUser user);
	
	/**
	 * 解锁用户
	 * @param user
	 * @return
	 */
	public int updateUnlockUser(SysRegisterUser user);
	
	/**
	 * 密码重置
	 * @param user
	 * @return
	 */
	public int updateResetPassword(SysRegisterUser user);
	
	/**
	 * 检查用户编码是否存在
	 * @param userCode
	 * @return
	 */
	public int checkUserCode(String userCode);
	
	/**
	 * 检查用户名是否存在
	 * @param userName
	 * @return
	 */
	public int checkUserName(String userName);
	
	/**
	 * 查询买手list
	 * @param map
	 * @return
	 * @author tanjun
	 */
	public List<SysRegisterUser> getBuyerByCondition(Map<String,Object>map);
	
	/**
	 * 修改用户名密码
	 * @param map
	 * @author tanjun
	 * @return
	 * @throws Exception 
	 */
	public String updateUserPwd(Map<String,Object> map) throws Exception;

	/**
	 * @Description 获取用户数据库中加密的密码
	 * @param map
	 * @return
	 * @throws Exception
	 * @CreationDate 2016年3月1日 上午11:31:34
	 * @Author lidong(dli@gdeng.cn)
	*/
	public String getUserPwd(Map<String,Object> map) throws Exception; 
	
	/**
	 * 根据学校查找下面的采购员
	 * @param map
	 * @return
	 * @author tanjun
	 */
	public List<SysRegisterUser> getCanteenPurchase(Map<String,Object>map);
	
	/**
	 * 根据用户组织ID改变用户启用，禁用状态
	 * @author songhui
	 * @date 创建时间：2015年8月11日 下午4:58:05
	 * @param user
	 * @throws Exception 
	 *
	 */
	public void updateUserStatusByOrgID(SysRegisterUser user) throws Exception;
}






