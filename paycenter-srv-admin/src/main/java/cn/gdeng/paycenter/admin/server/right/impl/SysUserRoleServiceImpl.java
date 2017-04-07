package cn.gdeng.paycenter.admin.server.right.impl;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.right.SysUserRoleService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.SysUserRole;
import cn.gdeng.paycenter.util.web.api.IdCreater;

/**
 * 用户操作实现类;
 * 
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {
    @Autowired
    private BaseDao<?> baseDao;

    @Override
    public void delete(String userID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userID", userID);
        baseDao.execute("SysUserRole.delete", map);
    }

    @Override
    public SysUserRole get(String userID) {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        return baseDao.queryForObject("SysUserRole.get", map, SysUserRole.class);
    }

    @Override
    public List<SysUserRole> getByCondition(Map<String, Object> map) {
        List<SysUserRole> list = baseDao.queryForList("SysUserRole.getByCondition", map, SysUserRole.class);
        return list;
    }

    @Override
    public List<SysUserRole> getListSysUserRole(Map<String, Object> map) {
        List<SysUserRole> list = baseDao.queryForList("SysUserRole.getAll", map, SysUserRole.class);
        return list;
    }

    @Override
    public Integer getTotal(Map<String, Object> map) {
        return (int) baseDao.queryForObject("SysUserRole.getTotal", map, Integer.class);
    }

    @Override
    public void insert(SysUserRole sysUserRole) {
        baseDao.execute("SysUserRole.insert", sysUserRole);
    }

    @Override
    public void update(SysUserRole sysUserRole) {
        baseDao.execute("SysUserRole.update", sysUserRole);
    }

    @Override
    public void batchDelete(List<String> userIdlist) {
        int len = userIdlist.size();
        Map<String, Object>[] batchValues = new HashMap[len];
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("urId", StringUtils.trim(userIdlist.get(i)));
            batchValues[i] = map;
        }
        baseDao.batchUpdate("SysUserRole.delete", batchValues);
    }

    @Override
    public void insertBatch(String userIDs, String roleID, String createUserID) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleID", roleID);
        if (userIDs == null) {
            // 删除该角色下的所有用户
            baseDao.execute("SysUserRole.deleteByRoleID", map);
        } else {
            List<String> userIdList = Arrays.asList(userIDs.split(","));
            // 删除该角色下的所有用户
            baseDao.execute("SysUserRole.deleteByRoleID", map);
            // 再新增用户角色
            SysUserRole sysUserRoleTemp = null;
            for (String userID : userIdList) {
                sysUserRoleTemp = new SysUserRole();
                sysUserRoleTemp.setUrID(IdCreater.newId());
                sysUserRoleTemp.setUserID(userID);
                sysUserRoleTemp.setRoleID(roleID);
                sysUserRoleTemp.setCreateUserID(createUserID);
                baseDao.execute("SysUserRole.insert", sysUserRoleTemp);
            }
        }
    }

    @Override
    public void deleteByUserID(String userID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userID", userID);
        baseDao.execute("SysUserRole.deleteByUserID", map);
    }

    @Override
    public List<SysUserRole> getUserRoleList(Map<String, Object> map) {
        List<SysUserRole> list = baseDao.queryForList("SysUserRole.getUserRoleList", map, SysUserRole.class);
        return list;
    }

    @Override
    public List<SysUserRole> getSysUserList(Map<String, Object> map) throws Exception {
        List<SysUserRole> list = baseDao.queryForList("SysUserRole.getSysUserList", map, SysUserRole.class);
        return list;
    }

    @Override
    public int getSysUserCount(Map<String, Object> map) throws Exception {
        return (int) baseDao.queryForObject("SysUserRole.getSysUserCount", map, Integer.class);
    }

    @Override
    public List<SysUserRole> getUserAllRoleList(Map<String, Object> map) {
        List<SysUserRole> list = baseDao.queryForList("SysUserRole.getUserAllRoleList", map, SysUserRole.class);
        return list;
    }

    @Override
    public int getUserAllRoleCount(Map<String, Object> map) {
        return (int) baseDao.queryForObject("SysUserRole.getUserAllRoleCount", map, Integer.class);
    }

    @Override
    public void insertUserRoleBatch(String userID, String roleIDs, String createUserID) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userID", userID);
        if (roleIDs == null) {
            // 删除该用户下的所有角色
            baseDao.execute("SysUserRole.deleteByUserID", map);
        } else {
            List<String> roleIdList = Arrays.asList(roleIDs.split(","));
            // 删除该用户下的所有角色
            baseDao.execute("SysUserRole.deleteByUserID", map);
            // 再新增用户角色
            SysUserRole sysUserRoleTemp = null;
            for (String roleID : roleIdList) {
                sysUserRoleTemp = new SysUserRole();
                sysUserRoleTemp.setUrID(IdCreater.newId());
                sysUserRoleTemp.setUserID(userID);
                sysUserRoleTemp.setRoleID(roleID);
                sysUserRoleTemp.setCreateUserID(createUserID);
                baseDao.execute("SysUserRole.insert", sysUserRoleTemp);
            }
        }
    }
}
