package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysMenu;

/**
 * @Description 系统菜单接口
 * @Project gd-auth-intf
 * @ClassName SysMenuService.java
 * @Author lidong(dli@cnagri-products.com)
 * @CreationDate 2015年10月17日 下午2:40:40
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory Who When What -------- ------------------------- ----------------------------------- lidong 2015年10月17日 下午2:40:40 初始创建
 */
public interface SysMenuService {

    public List<SysMenu> getAll(Map<String, Object> map) throws Exception;

    /**
     * 检查此菜单是否已经分配给对应的角色用
     */
    public List<SysMenu> getAllMenuRole(Map<String, Object> map);

    public List<SysMenu> getByCondition(Map<String, Object> map);

    public int getTotal(Map<String, Object> map);

    public SysMenu getSysMenu(String menuCode);

    public SysMenu getSysMenuByID(String menuId);

    /**
     * 新增菜单
     * 
     * @param sysMenu
     * @return
     */
    public String insert(SysMenu sysMenu);

    /**
     * 修改菜单
     * 
     * @param sysMenu
     * @return
     */
    public String update(SysMenu sysMenu);

    /**
     * 删除
     * 
     * @param menuID
     * @throws Exception
     */
    public String delete(String menuID) throws Exception;

    /**
     * 查询一级菜单
     * 
     * @author songhui
     * @date 创建时间：2015年7月24日 下午2:58:09
     * @param map
     * @return
     * 
     */
    public List<SysMenu> getFirstMenu(Map<String, Object> map);

    /**
     * @Description getSecondMenu 查询二级菜单
     * @param map
     * @return
     * @CreationDate 2015年11月17日 上午10:40:44
     * @Author lidong(dli@cnagri-products.com)
     */
    public List<SysMenu> getSecondMenu(Map<String, Object> map);

    /**
     * @Description 获取父级菜单
     * @param map
     * @return
     * @CreationDate 2016年1月12日 下午12:20:55
     * @Author lidong(dli@gdeng.cn)
    */
    public SysMenu getParentMenu(Map<String, Object> map) throws Exception;

    /**
     * @Description checkMenuHasChildren 检查菜单下是否有下级菜单
     * @param map
     * @return
     * @CreationDate 2015年11月17日 上午11:47:15
     * @Author lidong(dli@cnagri-products.com)
     */
    public int checkMenuHasChildren(Map<String, Object> map);

}