package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysRole;

/**   
 * @Description 系统角色接口
 * @Project gd-auth-intf
 * @ClassName SysRoleService.java
 * @Author lidong(dli@cnagri-products.com)    
 * @CreationDate 2015年10月17日 下午2:42:55
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
public interface SysRoleService{
	
	public List<SysRole> getAll(Map<String, Object> map) throws Exception;
	
	public List<SysRole> getAll();
	
	public List<SysRole> getByCondition(Map<String, Object> map);
	
	public int getTotal(Map<String,Object> map);
	
	public String insert(SysRole sysRole);
	
	public String update(SysRole sysRole)throws Exception;
	
	public void delete(String roleIDs) throws Exception;
	
	/**
	 * 根据角色ID获取角色信息
	 * @param roleID
	 * @return
	 */
	public SysRole getSysRoleById(String roleID) throws Exception;
}
