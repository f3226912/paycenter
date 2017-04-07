package cn.gdeng.paycenter.admin.server.right.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.right.SysMenuService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.SysMenu;
import cn.gdeng.paycenter.util.web.api.CommonConstant;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private BaseDao<?> baseDao;

    /**
     * @Description getAll
     * @param map
     * @return
     * @throws Exception
     * @CreationDate 2015年10月17日 上午9:11:03
     * @Author lidong(dli@cnagri-products.com)
     */
    @Override
    public List<SysMenu> getAll(Map<String, Object> map) throws Exception {
        return baseDao.queryForList("SysMenu.getAll", map, SysMenu.class);
    }

    /**
     * @Description getByCondition
     * @param map
     * @return
     * @CreationDate 2015年10月17日 上午9:16:10
     * @Author lidong(dli@cnagri-products.com)
     */
    @Override
    public List<SysMenu> getByCondition(Map<String, Object> map) {
        return baseDao.queryForList("SysMenu.getByCondition", map, SysMenu.class);
    }

    /**
     * 检查此菜单是否已经分配给对应的角色用
     */
    @Override
    public List<SysMenu> getAllMenuRole(Map<String, Object> map) {
        return baseDao.queryForList("SysMenu.getAllMenuRole", map, SysMenu.class);
    }

    @Override
    public int getTotal(Map<String, Object> map) {
        return (int) baseDao.queryForObject("SysMenu.getTotal", map, Integer.class);
    }

    @Override
    public SysMenu getSysMenu(String menuCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuCode", menuCode);
        return baseDao.queryForObject("SysMenu.getSysMenu", map, SysMenu.class);
    }

    @Override
    public SysMenu getSysMenuByID(String menuId) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuID", menuId);
        return baseDao.queryForObject("SysMenu.getSysMenuByID", map, SysMenu.class);
    }

    @Override
    public String insert(SysMenu sysMenu) {
        int i = baseDao.execute("SysMenu.insert", sysMenu);
        return i > 0 ? CommonConstant.COMMON_AJAX_SUCCESS : "sysmgr.sysmenu.menuCodeExists";
    }

    @Override
    public String update(SysMenu sysMenu) {
        int i = baseDao.execute("SysMenu.update", sysMenu);
        return i > 0 ? CommonConstant.COMMON_AJAX_SUCCESS : "sysmgr.sysmenu.menuCodeExists";
    }

    @SuppressWarnings("unchecked")
    @Override
    public String delete(String menuIDs) throws Exception {
        String[] menuIDAry = menuIDs.split(",");
        int len = menuIDAry.length;
        Map<String, Object>[] batchValues = new HashMap[len];
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("menuID", StringUtils.trim(menuIDAry[i]));
            batchValues[i] = map;
        }
        return baseDao.batchUpdate("SysMenu.delete", batchValues).length > 0 ? CommonConstant.COMMON_AJAX_SUCCESS : "error";
    }

    @Override
    public List<SysMenu> getFirstMenu(Map<String, Object> map) {
        List<SysMenu> list = baseDao.queryForList("SysMenu.getFirstMenu", map, SysMenu.class);
        return list;
    }

    @Override
    public List<SysMenu> getSecondMenu(Map<String, Object> map) {
        List<SysMenu> list = baseDao.queryForList("SysMenu.getSecondMenu", map, SysMenu.class);
        return list;
    }
    /**
     * @Description 获取父级菜单
     * @param map
     * @return
     * @CreationDate 2016年1月12日 下午12:20:55
     * @Author lidong(dli@gdeng.cn)
    */
    public SysMenu getParentMenu(Map<String, Object> map){
        return baseDao.queryForObject("SysMenu.getParentMenu", map, SysMenu.class);
    }
    
    @Override
    public int checkMenuHasChildren(Map<String, Object> map) {
        return (int) baseDao.queryForObject("SysMenu.checkMenuHasChildren", map, Integer.class);
    }
}