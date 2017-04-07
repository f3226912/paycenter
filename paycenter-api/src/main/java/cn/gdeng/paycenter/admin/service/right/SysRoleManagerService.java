package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysRightBtn;
import cn.gdeng.paycenter.entity.pay.SysRoleManager;

/**
 * @Description 角色权限分配接口
 * @Project gd-auth-intf
 * @ClassName SysRoleManagerService.java
 * @Author lidong(dli@cnagri-products.com)
 * @CreationDate 2015年10月17日 下午2:42:41
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
public interface SysRoleManagerService {

	/**
	 * @Description update 批量添加角色菜单
	 * @param menuList
	 * @param roleID
	 * @param createrID
	 * @return
	 * @throws Exception
	 * @CreationDate 2015年10月19日 下午3:57:25
	 * @Author lidong(dli@cnagri-products.com)
	 */
	public String update(List<String> menuList, String roleID, String createrID) throws Exception;

	/**
	 * 得到所有的按钮和菜单（返回值有一个用户是否拥有此菜单和按钮功能的标志）
	 * 
	 * @return
	 */
	public List<SysRoleManager> get(Map<String, Object> map) throws Exception;

	/**
	 * 修改用户所拥有的按钮功能（根据rmID先删除相关数据，然后再添加数据到数据库中）
	 * 
	 * @param trCheckBox
	 * @param roleID
	 * @param createrID
	 * @return
	 * @throws Exception
	 */
	public String updateBtn(List<String> menuButtonList, String roleID, String createrID) throws Exception;

	/**
	 * @Description getButtonsByRole 根据角色查询按钮
	 * @param roleID
	 * @return
	 * @throws Exception
	 * @CreationDate 2015年11月24日 上午10:41:05
	 * @Author lidong(dli@cnagri-products.com)
	 */
	public List<SysRightBtn> getButtonsByRole(String roleID) throws Exception;

}
