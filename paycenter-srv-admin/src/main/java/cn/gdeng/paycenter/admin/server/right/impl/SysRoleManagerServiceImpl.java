package cn.gdeng.paycenter.admin.server.right.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.right.SysRoleManagerService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.SysMenuButton;
import cn.gdeng.paycenter.entity.pay.SysRightBtn;
import cn.gdeng.paycenter.entity.pay.SysRoleManager;
import cn.gdeng.paycenter.util.web.api.CommonConstant;
import cn.gdeng.paycenter.util.web.api.IdCreater;

/**
 * 用户菜单按钮关系管理service实现类
 * 
 * @version 1.0
 */
@Service
public class SysRoleManagerServiceImpl implements SysRoleManagerService {

    /** 用户菜单和按钮关系管理 Mapper */
    @Autowired
    private BaseDao<?> baseDao;

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
    public String update(List<String> menuList, String roleID, String createrID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("roleID", roleID);
        // 新增之前 先删除旧的菜单
        baseDao.execute("SysRoleManager.deleteMenu", map);
        if (menuList != null && menuList.size() > 0) {
            int len = menuList.size();
            Map<String, Object>[] batchValues = new HashMap[len];
            for (int i = 0; i < len; i++) {
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("roleID", roleID);
                mapTemp.put("rmID", IdCreater.newId());
                mapTemp.put("menuID", menuList.get(i));
                mapTemp.put("createUserID", createrID);
                batchValues[i] = mapTemp;
            }
            baseDao.batchUpdate("SysRoleManager.insertMenu", batchValues);
        }
        return "success";
    }

    public List<SysRoleManager> get(Map<String, Object> map) throws Exception {

        // 只查询被分配的菜单
        if (map.get("view") != null) {
            map.put("menuTotal", 1);
        }
        // 得到所有的菜单
        // List <SysRoleManager> list = sysRoleManagerMapper.getAll(map);
        List<SysRoleManager> list = baseDao.queryForList("SysRoleManager.getAll", map, SysRoleManager.class);

        // 创建一个map 用来存储list中的对象
        Map<String, Object> listMap = new HashMap<String, Object>();
        // 创建一个List用来保存根据menuID得到的所有的按钮
        List<SysMenuButton> buttonList = new ArrayList<SysMenuButton>();
        // 创建一个List用来保存添加按钮属性以后的sysRoleManager对象
        List<SysRoleManager> lastList = new ArrayList<SysRoleManager>();
        SysRoleManager srm = new SysRoleManager();
        // 循环所有的菜单，根据菜单ID 查询所有的按钮
        return list;
    }

    @Override
    public String updateBtn(List<String> menuButtonList, String roleID, String createrID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("roleID", roleID);
        map.put("createUserID", createrID);
        map.put("userID", "");
        // 判断如果trCheckBox是null 就表示撤销用户所有的按钮和菜单关系
        baseDao.execute("SysRoleManager.deleteBtn", map);
        if (menuButtonList != null && menuButtonList.size() > 0) {
            // 批量插入信息到系统角色菜单按钮关系表
            int len = menuButtonList.size();
            Map<String, Object>[] batchValues = new HashMap[len];
            for (int i = 0; i < len; i++) {
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("id", IdCreater.newId());
                mapTemp.put("roleID", roleID);
                mapTemp.put("createUserID", createrID);
                mapTemp.put("userID", "");
                mapTemp.put("btnID", menuButtonList.get(i));
                batchValues[i] = mapTemp;
            }
            baseDao.batchUpdate("SysRoleManager.insertBtn", batchValues);
        }
        return CommonConstant.COMMON_AJAX_SUCCESS;
    }

    /**
     * @Description getButtonsByRole 根据角色查询按钮
     * @param roleID
     * @return
     * @throws Exception
     * @CreationDate 2015年11月24日 上午10:41:05
     * @Author lidong(dli@cnagri-products.com)
     */
    public List<SysRightBtn> getButtonsByRole(String roleID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("roleID", roleID);
        List<SysRightBtn> list = baseDao.queryForList("SysRoleManager.getButtonByRole", map, SysRightBtn.class);
        return list;
    }

}
