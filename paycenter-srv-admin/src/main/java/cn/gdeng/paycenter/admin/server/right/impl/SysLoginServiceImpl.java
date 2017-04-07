package cn.gdeng.paycenter.admin.server.right.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.right.SysLoginService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.right.BaseMenu;
import cn.gdeng.paycenter.entity.pay.SysMenu;
import cn.gdeng.paycenter.entity.pay.SysMenuButton;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.entity.pay.SysUserRole;
import cn.gdeng.paycenter.util.admin.web.StringUtil;
import cn.gdeng.paycenter.util.server.MenuComparator;
import cn.gdeng.paycenter.util.web.api.MessageUtil;

/**
 * 取得登录用户的全部信息serivce实现类;
 * 
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {
    @Autowired
    private BaseDao<?> baseDao;
    @Autowired
    private MessageUtil messageUtil;

    /** 登录的输入的错语密码次数 */
    private Integer LOGIN_ERRORPWD_TIMES = 6;

    @Override
    public Object[] getUserAllMenu(String userID) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userID", userID);
        // 取得用户权限,用户所有的合法menu;
        List<SysMenu> sysMenuList = baseDao.queryForList("SysLogin.getSysMenu", map, SysMenu.class);
        // 一级菜单 url;
        List<BaseMenu> baseMenuList = new ArrayList<BaseMenu>();
        // 二级菜单 url
        List<BaseMenu> subMenuList = new ArrayList<BaseMenu>();
        // 三级级菜单 url
        List<BaseMenu> trdMenuList = new ArrayList<BaseMenu>();
        // 循环取出一级菜单
        for (SysMenu menu : sysMenuList) {
            // 如果没有上级菜单，说明是第一级菜单
            if (StringUtils.isBlank(menu.getMenuModuleID())) {
                // 第一级菜单;
                BaseMenu baseMenu = new BaseMenu();
                baseMenu.setMenuID(menu.getMenuID());
                baseMenu.setId(menu.getMenuID());
                baseMenu.setMenuCode(menu.getMenuCode());
                baseMenu.setMenuName(menu.getMenuName());
                baseMenu.setMenuModuleID(menu.getMenuModuleID());
                baseMenu.setText(menu.getMenuName());
                baseMenu.setCurLevel("1");
                baseMenu.setIconCls(menu.getIconCls());
                baseMenu.setSort(menu.getSort());
                baseMenu.setLevel(menu.getLevel());
                baseMenu.setUpdateTime(menu.getUpdateTime());
                baseMenu.setAttribute(menu.getAttribute());
                baseMenu.setChildren(new ArrayList<BaseMenu>());
                // 将已经存在的baseMenu压入list中;
                baseMenuList.add(baseMenu);
            }
        }
        // 循环一级菜单，将父级菜单等于一级菜单的菜单加入到一级菜单子集合和二级菜单集合
        for (BaseMenu firstMenu : baseMenuList) {
            List<BaseMenu> children = new ArrayList<>();
            // 循环取出二级菜单
            for (SysMenu menu : sysMenuList) {
                if (StringUtils.isNotEmpty(menu.getMenuModuleID()) && menu.getMenuModuleID().equals(firstMenu.getMenuID())) {
                    // 第二级菜单
                    BaseMenu subBaseMenu = new BaseMenu();
                    subBaseMenu.setMenuID(menu.getMenuID());
                    subBaseMenu.setId(menu.getMenuID());
                    subBaseMenu.setMenuCode(menu.getMenuCode());
                    subBaseMenu.setMenuName(menu.getMenuName());
                    subBaseMenu.setMenuModuleID(menu.getMenuModuleID());
                    subBaseMenu.setText(menu.getMenuName());
                    subBaseMenu.setCurLevel("2");
                    subBaseMenu.setIconCls(menu.getIconCls());
                    subBaseMenu.setActionUrl(menu.getActionUrl());
                    subBaseMenu.setSort(menu.getSort());
                    subBaseMenu.setLevel(menu.getLevel());
                    subBaseMenu.setUpdateTime(menu.getUpdateTime());
                    subBaseMenu.setAttribute(menu.getAttribute());
                    // 加入二级菜单集合
                    subMenuList.add(subBaseMenu);
                    children.add(subBaseMenu);
                }
            }
            // 对二级菜单进行排序
            Collections.sort(children, new MenuComparator());
            // 加入一级菜单子集合
            firstMenu.setChildren(children);
        }
        for (BaseMenu subMenu : subMenuList) {
            List<BaseMenu> children = new ArrayList<>();
            // 循环取出三级菜单
            for (SysMenu menu : sysMenuList) {
                if (StringUtils.isNotEmpty(menu.getMenuModuleID()) && menu.getMenuModuleID().equals(subMenu.getMenuID())) {
                    // 第三级菜单
                    BaseMenu subBaseMenu = new BaseMenu();
                    subBaseMenu.setMenuID(menu.getMenuID());
                    subBaseMenu.setId(menu.getMenuID());
                    subBaseMenu.setMenuCode(menu.getMenuCode());
                    subBaseMenu.setMenuName(menu.getMenuName());
                    subBaseMenu.setMenuModuleID(menu.getMenuModuleID());
                    subBaseMenu.setText(menu.getMenuName());
                    subBaseMenu.setCurLevel("3");
                    subBaseMenu.setIconCls(menu.getIconCls());
                    subBaseMenu.setActionUrl(menu.getActionUrl());
                    subBaseMenu.setSort(menu.getSort());
                    subBaseMenu.setLevel(menu.getLevel());
                    subBaseMenu.setUpdateTime(menu.getUpdateTime());
                    subBaseMenu.setAttribute(menu.getAttribute());
                    // 加入三级级菜单集合
                    trdMenuList.add(subBaseMenu);
                    children.add(subBaseMenu);
                }
            }
            // 对三级菜单进行排序
            Collections.sort(children, new MenuComparator());
            // 加入二级菜单子集合
            subMenu.setChildren(children);
        }
        // 循环二级菜单，将父级菜单等于二级菜单的菜单加入到二级菜单子集合和三级菜单集合
        // 对一级菜单进行排序
        Collections.sort(baseMenuList, new MenuComparator());
        Object[] obj = new Object[4];
        obj[0] = baseMenuList;// 一级菜单
        obj[1] = null;
        obj[2] = subMenuList;// 二级菜单
        obj[3] = trdMenuList;// 三级菜单
        return obj;
    }

    @Override
    public Map<String, String> getUserAllMenuButton(String userID) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("userID", userID);
        // 取得用户菜单按钮集合;
        List<SysMenuButton> btnList = baseDao.queryForList("SysLogin.getSysMenuButton", map, SysMenuButton.class);
        // 返回的button记录集;
        Map<String, String> btnMap = new HashMap<String, String>();
        for (SysMenuButton sysMenuButton : btnList) {
            btnMap.put(sysMenuButton.getBtnCode(), sysMenuButton.getBtnName());
        }
        return btnMap;
    }

    @Override
    public List<SysMenu> getAllMenu() {
        return baseDao.queryForList("SysMenu.getAll", new HashMap<String, Object>(), SysMenu.class);
    }

    @Override
    public SysRegisterUser getLoginUserInfo(String userCode) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userCode", userCode);
        List<SysRegisterUser> regUserList = baseDao.queryForList("SysLogin.getLoginUser", map, SysRegisterUser.class);
        if (regUserList == null || regUserList.size() == 0) {
            return null;
        } else {
            return regUserList.get(0);
        }
    }

    @Override
    public void updateLoginUser(SysRegisterUser user) throws Exception {
        baseDao.execute("SysLogin.updateUserLogin", user);
    }

    /**
     * @Description 后台用户登录
     * @param regUser
     * @return
     * @throws Exception
     * @CreationDate 2016年3月5日 下午4:13:32
     * @Author lidong(dli@gdeng.cn)
     */
    @Override
    public String processLogin(SysRegisterUser regUser) throws Exception {
        String locked = "0";// 锁定标识;
        Integer pwdErrorTimes = 0;// 错误的密码次数;
        // 登陆时如果没有角色则无法登陆
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userID", regUser.getUserID());
        // 查询用户的角色
        List<SysUserRole> surList = baseDao.queryForList("SysUserRole.getAll", map, SysUserRole.class);
        if (surList == null || surList.size() <= 0) {
            // 用户没有角色
            return messageUtil.getMessage("login.user.norole");
        } else {
            // 是否是后台角色和前后台角色
            boolean hasRole = false;// 默认无前后台、后台角色
            for (SysUserRole sysUserRole : surList) {
                if (("1".equals(sysUserRole.getAttribute()) || "3".equals(sysUserRole.getAttribute())) && regUser.getUserID().equals(sysUserRole.getUserID())) {
                    // 有前后台、后台角色
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                return messageUtil.getMessage("login.user.hasnoRole");
            }
        }
        if ("1".equals(regUser.getLocked())) {
            // 用户已经被锁定;
            return messageUtil.getMessage("login.user.hasLocked");
        }
        if ("0".equals(regUser.getLocked()) && regUser.getLastErrorTime() != null && ((System.currentTimeMillis() - regUser.getLastErrorTime().getTime()) > 24 * 60 * 60 * 1000l)) {
            // 清除24小时后未被锁住的登录错误次数
            regUser.setLocked("0");
            regUser.setPwdErrorTimes(0);
            regUser.setLastErrorTime(null);
            baseDao.execute("SysLogin.updateUserLogin", regUser);
        }
        if (!StringUtil.stringEncryptMD5(regUser.getPassword()).equals(regUser.getUserPassWord())) {
            // 密码不正确;
            pwdErrorTimes = regUser.getPwdErrorTimes() + 1;
            if (pwdErrorTimes >= LOGIN_ERRORPWD_TIMES) {
                locked = "1";
            }
            // 更新用户登录的错误信息;
            regUser.setLocked(locked);
            regUser.setPwdErrorTimes(pwdErrorTimes);
            regUser.setLastErrorTime(new Date());
            baseDao.execute("SysLogin.updateUserLogin", regUser);
            return messageUtil.getMessage("login.errorPassword");
        }
        // 登录成功
        regUser.setLocked("0");
        regUser.setPwdErrorTimes(0);
        regUser.setLastErrorTime(null);
        // 更新用户登录的信息;
        baseDao.execute("SysLogin.updateUserLogin", regUser);
        return null;
    }

    /**
     * @Description 前台用户登录
     * @param regUser
     * @return
     * @throws Exception
     * @CreationDate 2016年3月5日 下午4:13:32
     * @Author lidong(dli@gdeng.cn)
     */
    @Override
    public String processLogin2(SysRegisterUser regUser) throws Exception {
        String locked = "0";// 锁定标识;
        Integer pwdErrorTimes = 0;// 错误的密码次数;
        // 登陆时如果没有角色则无法登陆
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userID", regUser.getUserID());
        // 查询用户的角色
        List<SysUserRole> surList = baseDao.queryForList("SysUserRole.getAll", map, SysUserRole.class);
        if (surList == null || surList.size() <= 0) {
            // 用户没有角色
            return messageUtil.getMessage("login.user.norole");
        } else {
            // 是否是后台角色和前后台角色
            boolean hasRole = false;// 默认无前后台、后台角色
            for (SysUserRole sysUserRole : surList) {
                if (("2".equals(sysUserRole.getAttribute()) || "3".equals(sysUserRole.getAttribute())) && regUser.getUserID().equals(sysUserRole.getUserID())) {
                    // 有前后台、后台角色
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                return messageUtil.getMessage("login.user.hasnoRole");
            }
        }
        if ("1".equals(regUser.getLocked())) {
            // 用户已经被锁定;
            return messageUtil.getMessage("login.user.hasLocked");
        }
        if ("0".equals(regUser.getLocked()) && regUser.getLastErrorTime() != null && ((System.currentTimeMillis() - regUser.getLastErrorTime().getTime()) > 24 * 60 * 60 * 1000l)) {
            // 清除24小时后未被锁住的登录错误次数
            regUser.setLocked("0");
            regUser.setPwdErrorTimes(0);
            regUser.setLastErrorTime(null);
            baseDao.execute("SysLogin.updateUserLogin", regUser);
        }
        if (!StringUtil.stringEncryptMD5(regUser.getPassword()).equals(regUser.getUserPassWord())) {
            // 密码不正确;
            pwdErrorTimes = regUser.getPwdErrorTimes() + 1;
            if (pwdErrorTimes >= LOGIN_ERRORPWD_TIMES) {
                locked = "1";
            }
            // 更新用户登录的错误信息;
            regUser.setLocked(locked);
            regUser.setPwdErrorTimes(pwdErrorTimes);
            regUser.setLastErrorTime(new Date());
            baseDao.execute("SysLogin.updateUserLogin", regUser);
            return messageUtil.getMessage("login.errorPassword");
        }
        // 登录成功
        regUser.setLocked("0");
        regUser.setPwdErrorTimes(0);
        regUser.setLastErrorTime(null);
        // 更新用户登录的信息;
        baseDao.execute("SysLogin.updateUserLogin", regUser);
        return null;
    }
}
