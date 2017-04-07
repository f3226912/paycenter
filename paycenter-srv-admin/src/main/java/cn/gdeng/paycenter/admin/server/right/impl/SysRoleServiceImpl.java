package cn.gdeng.paycenter.admin.server.right.impl;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.right.SysRoleService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.SysRole;
import cn.gdeng.paycenter.entity.pay.SysUserRole;
import cn.gdeng.paycenter.util.web.api.CommonConstant;
import cn.gdeng.paycenter.util.web.api.IdCreater;

/**
 * 角色操作实现类;
 * 
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private BaseDao<?> baseDao;

    @Override
    public List<SysRole> getAll(Map<String, Object> map) throws Exception {
        List<SysRole> list = baseDao.queryForList("SysRole.getAll", map, SysRole.class);
        return list;
    }

    @Override
    public List<SysRole> getAll() {
        List<SysRole> list = baseDao.queryForList("SysRole.getAll", new HashMap<>(), SysRole.class);
        return list;
    }

    @Override
    public List<SysRole> getByCondition(Map<String, Object> map) {
        List<SysRole> list = baseDao.queryForList("SysRole.getByCondition", map, SysRole.class);
        return list;
    }

    @Override
    public int getTotal(Map<String, Object> map) {
        // return sysRoleMapper.getTotal(map);
        return (int) baseDao.queryForObject("SysRole.getTotal", map, Integer.class);
    }

    @Override
    public String insert(SysRole sysRole) {
        // 判断是否有重复角色名
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleName", sysRole.getRoleName());
        map.put("attribute", sysRole.getAttribute());
        List<SysRole> list = baseDao.queryForList("SysRole.getAll", map, SysRole.class);
        if (list != null && list.size() > 0) {
            for (SysRole sysRole2 : list) {
                if (sysRole.getRoleName().equals(sysRole2.getRoleName())) {
                    return "该类型下，角色名已存在";
                }
            }
        }
        sysRole.setRoleID(IdCreater.newId());
        // 进行添加操作
        baseDao.execute("SysRole.insert", sysRole);
        return CommonConstant.COMMON_AJAX_SUCCESS;
    }

    @Override
    public String update(SysRole sysRole) throws Exception {
        // 判断是否有重复角色名
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleName", sysRole.getRoleName());
        map.put("attribute", sysRole.getAttribute());
        List<SysRole> list = baseDao.queryForList("SysRole.getAll", map, SysRole.class);
        if (list != null && list.size() > 0) {
            for (SysRole sysRole2 : list) {
                if (sysRole.getRoleName().equals(sysRole2.getRoleName()) && (!sysRole.getRoleID().equals(sysRole2.getRoleID()))) {
                    return "该类型下，角色名已存在";
                }
            }
        }

        // 执行修改动作
        baseDao.execute("SysRole.update", sysRole);
        return CommonConstant.COMMON_AJAX_SUCCESS;
    }

    @Override
    @Transactional
    public void delete(String roleIDs) throws Exception {
        List<String> roleIdslist = Arrays.asList(roleIDs.split(","));
        Map<String, Object> map = new HashMap<String, Object>();
        List<SysUserRole> sulist = null;
        SysRole role = null;
        for (String roleID : roleIdslist) {
            map.put("roleID", roleID);
            // 查询该角色下是否有分配用户
            sulist = baseDao.queryForList("SysUserRole.getUserRoleList", map, SysUserRole.class);
            // 如果删除的ID中有关联结果，则返回
            if (sulist.size() > 0) {
                // 根据角色ID查询角色
                role = this.getSysRoleById(roleID);
                throw new Exception("[" + role.getRoleName() + "]角色已分配用户不能删除！");
            } else {
                // 删除系统角色菜单按钮关系表原有的信息
                baseDao.execute("SysRoleManager.deleteBtn", map);
                // 删除系统角色菜单关系表原有的信息
                baseDao.execute("SysRoleManager.deleteMenu", map);
                // 再删除角色
                baseDao.execute("SysRole.delete", map);
            }
        }
    }

    @Override
    public SysRole getSysRoleById(String roleID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("roleID", roleID);
        return baseDao.queryForObject("SysRole.getSysRoleById", map, SysRole.class);
    }
}
