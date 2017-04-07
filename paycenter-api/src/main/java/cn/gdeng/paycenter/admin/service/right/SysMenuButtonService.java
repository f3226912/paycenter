package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysMenuButton;
/**   
 * @Description 菜单按钮接口
 * @Project gd-auth-intf
 * @ClassName SysMenuButtonService.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:40:11
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 *  Who        When                         What
 *  --------   -------------------------    -----------------------------------
 *  lidong     2015年10月17日 下午2:40:11       初始创建
 */
public interface SysMenuButtonService {
	
	public SysMenuButton getSysButtonByID(String btnId);
	
	/**
	 * 查询记录总数
	 * @param map
	 * @return
	 */
	public int getTotal(Map<String,Object> map);
	
	/**
	 * 查询分页数据列表
	 * @param map
	 * @return
	 */
	public List<SysMenuButton> getByCondition(Map<String,Object> map);
	
	/**
	 * 新增按钮
	 * @param sysMenuButton
	 * @return
	 */
	public String insert(SysMenuButton sysMenuButton);
	
	/**
	 * 修改按钮
	 * @param sysMenuButton
	 * @return
	 */
	public String update(SysMenuButton sysMenuButton);
	
	/**
	 * 删除按钮
	 * @param sysMenuButton
	 */
	public String delete(List<String> buttonList);
}
