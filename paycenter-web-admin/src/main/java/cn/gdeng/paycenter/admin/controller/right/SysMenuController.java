package cn.gdeng.paycenter.admin.controller.right;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.dto.admin.TreeNode;
import cn.gdeng.paycenter.admin.service.right.SysMenuButtonService;
import cn.gdeng.paycenter.admin.service.right.SysMenuService;
import cn.gdeng.paycenter.admin.service.right.SysRoleManagerService;
import cn.gdeng.paycenter.entity.pay.SysMenu;
import cn.gdeng.paycenter.entity.pay.SysMenuButton;
import cn.gdeng.paycenter.entity.pay.SysRightBtn;
import cn.gdeng.paycenter.util.admin.web.LoginUserUtil;
import cn.gdeng.paycenter.util.web.api.IdCreater;

/**
 * 系统菜单controller类
 * 
 * @version 1.0
 */
@Controller
@RequestMapping("sysmgr")
public class SysMenuController extends AdminBaseController {

    private static final GdLogger logger = GdLoggerFactory.getLogger(SysMenuController.class);

    /** 菜单service */
    @Reference
    private SysMenuService sysMenuService;

    /** 按钮service */
    @Reference
    private SysMenuButtonService sysMenuButtonService;

    /*** 角色service ***/
    @Reference
    private SysRoleManagerService sysRoleManagerService;

    /**
     * 按钮分页查询初始化页面
     * 
     * @author wwj
     * @date 创建时间：2015年7月24日 下午2:18:14
     * @param request
     * @return
     *
     */
    @RequestMapping("sysMenu/list")
    public String list(HttpServletRequest request) {
        return "sysmgr/sysmenu/menulist";
    }

    /**
     * 菜单分页查询;
     * 
     * @param request
     * @return path
     */
    @RequestMapping(value = "sysMenu/query", produces = "application/html;charset=UTF-8")
    @ResponseBody
    public String query(HttpServletRequest request) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 条件参数
            map.put("menuCode", StringUtils.trimToNull(request.getParameter("menuCode")));
            map.put("menuName", StringUtils.trimToNull(request.getParameter("menuName")));
            map.put("parentMenuName", StringUtils.trimToNull(request.getParameter("parentMenuName")));
            map.put("level", StringUtils.trimToNull(request.getParameter("level")));
            map.put("attribute", StringUtils.trimToNull(request.getParameter("attribute")));
            // 记录数
            map.put("total", sysMenuService.getTotal(map));
            // 设定分页,排序
            setCommParameters(request, map);
            List<SysMenu> list = sysMenuService.getByCondition(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * @Description 查询数据分类列表
     * @param request
     * @return
     * @CreationDate 2016年3月2日 上午11:07:01
     * @Author lidong(dli@gdeng.cn)
     */
    @RequestMapping(value = "sysMenu/query2", produces = "application/html;charset=UTF-8")
    @ResponseBody
    public String query2(HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 条件参数
            map.put("level", 1);
            map.put("attribute", 3);
            map.put("startRow", 0);
            map.put("endRow", 999);
            List<SysMenu> list = sysMenuService.getByCondition(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增菜单-页面初始化
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/addInit")
    public ModelAndView addInit(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        List<SysMenu> baseMenuList = null;
        request.setAttribute("actionUrl", "add");
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("level", 1);
            baseMenuList = sysMenuService.getAll(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("baseMenuList", baseMenuList);
        mv.addObject("actionUrl", "add");
        mv.setViewName("sysmgr/sysmenu/menuedit");
        return mv;
    }

    /**
     * 新增菜单-保存动作
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/add")
    @ResponseBody
    public String addSysMenu(HttpServletRequest request) {
        try {
            SysMenu sysMenu = new SysMenu();
            // 参数
            String menuCode = StringUtils.trimToNull(request.getParameter("menuCode"));
            String menuName = StringUtils.trimToNull(request.getParameter("menuName"));
            String menuModuleID = StringUtils.trimToNull(request.getParameter("menuModuleID"));
            String secondMenuID = StringUtils.trimToNull(request.getParameter("secondMenuID"));
            String menuType = StringUtils.trimToNull(request.getParameter("menuType"));
            String actionUrl = StringUtils.trimToNull(request.getParameter("actionUrl"));
            String iconCls = StringUtils.trimToNull(request.getParameter("iconCls"));
            String sort = StringUtils.trimToNull(request.getParameter("sort"));
            String attribute = StringUtils.trimToNull(request.getParameter("attribute"));
            // 封装参数
            sysMenu.setMenuID(IdCreater.newId());
            sysMenu.setMenuCode(menuCode);
            sysMenu.setMenuName(menuName);
            sysMenu.setMenuModuleID(StringUtils.isNotEmpty(secondMenuID) ? secondMenuID : menuModuleID);
            sysMenu.setMenuType(menuType);
            sysMenu.setActionUrl(actionUrl);
            sysMenu.setIconCls(iconCls);
            sysMenu.setAttribute(attribute);
            if (StringUtils.isEmpty(menuModuleID)) {
                sysMenu.setLevel(1);
                sysMenu.setMenuModuleID(null);
            } else if (StringUtils.isNotEmpty(menuModuleID) && StringUtils.isEmpty(secondMenuID)) {
                sysMenu.setLevel(2);
            } else if (StringUtils.isNotEmpty(menuModuleID) && StringUtils.isNotEmpty(secondMenuID)) {
                sysMenu.setLevel(3);
            }
            sysMenu.setSort(StringUtils.isNumeric(sort) ? Integer.valueOf(sort) : 0);
            sysMenu.setCreateUserID(LoginUserUtil.getLoginUserId(request));
            String message = sysMenuService.insert(sysMenu);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 修改菜单-页面初始化
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/updateInit")
    public String updateInit(HttpServletRequest request) throws Exception {
        String menuID = request.getParameter("menuID");
        SysMenu sysMenu = sysMenuService.getSysMenuByID(menuID);
        List<SysMenu> baseMenuList = null;
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("level", 1);
            baseMenuList = sysMenuService.getAll(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        int level = sysMenu.getLevel();
        if (level == 3) {
            map.put("menuModuleID", sysMenu.getMenuModuleID());
            SysMenu sysMenu2 = sysMenuService.getParentMenu(map);
            request.setAttribute("sndMenu", sysMenu2);// 二级菜单
            map.clear();
            map.put("menuModuleID", sysMenu2.getMenuModuleID());
            SysMenu sysMenu3 = sysMenuService.getParentMenu(map);
            request.setAttribute("firstMenu", sysMenu3);// 一级菜单
        } else if (level == 2) {
            map.put("menuModuleID", sysMenu.getMenuModuleID());
            SysMenu sysMenu3 = sysMenuService.getParentMenu(map);
            request.setAttribute("firstMenu", sysMenu3);// 一级菜单
            request.setAttribute("sndMenu", sysMenu);
        } else if (level == 1) {
            request.setAttribute("firstMenu", sysMenu);
        }
        request.setAttribute("baseMenuList", baseMenuList);
        request.setAttribute("dto", sysMenu);
        request.setAttribute("actionUrl", "update");
        return "sysmgr/sysmenu/menuedit";
    }

    /**
     * 修改菜单-保存动作
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/update")
    @ResponseBody
    public String updateSysMenu(HttpServletRequest request) throws Exception {
        try {
            SysMenu sysMenu = new SysMenu();
            String menuID = request.getParameter("menuID");
            sysMenu = sysMenuService.getSysMenuByID(menuID);
            // 参数
            String menuCode = StringUtils.trimToNull(request.getParameter("menuCode"));
            String menuName = StringUtils.trimToNull(request.getParameter("menuName"));
            String menuModuleID = StringUtils.trimToNull(request.getParameter("menuModuleID"));
            String secondMenuID = StringUtils.trimToNull(request.getParameter("secondMenuID"));
            String menuType = StringUtils.trimToNull(request.getParameter("menuType"));
            String actionUrl = StringUtils.trimToNull(request.getParameter("actionUrl"));
            String iconCls = StringUtils.trimToNull(request.getParameter("iconCls"));
            String sort = StringUtils.trimToNull(request.getParameter("sort"));
            String attribute = StringUtils.trimToNull(request.getParameter("attribute"));
            // 封装参数
            sysMenu.setMenuCode(menuCode);
            sysMenu.setMenuName(menuName);
            sysMenu.setMenuModuleID(StringUtils.isNotEmpty(secondMenuID) ? secondMenuID : menuModuleID);
            sysMenu.setMenuType(menuType);
            sysMenu.setActionUrl(actionUrl);
            sysMenu.setIconCls(iconCls);
            sysMenu.setAttribute(attribute);
            sysMenu.setSort(StringUtils.isNumeric(sort) ? Integer.valueOf(sort) : 0);
            if (StringUtils.isEmpty(menuModuleID)) {
                sysMenu.setLevel(1);
                sysMenu.setMenuModuleID(null);
            } else if (StringUtils.isNotEmpty(menuModuleID) && StringUtils.isEmpty(secondMenuID)) {
                sysMenu.setLevel(2);
            } else if (StringUtils.isNotEmpty(menuModuleID) && StringUtils.isNotEmpty(secondMenuID)) {
                sysMenu.setLevel(3);
            }
            sysMenu.setUpdateUserID(LoginUserUtil.getLoginUserId(request));
            String message = sysMenuService.update(sysMenu);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 删除菜单
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("sysMenu/delete")
    @ResponseBody
    public String deleteSysMenu(HttpServletRequest request, HttpServletResponse response) {
        try {
            String menuID = request.getParameter("menuID");
            String message = sysMenuService.delete(menuID);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * @Description 获取父级菜单
     * @param menuID
     * @param request
     * @return
     * @CreationDate 2016年1月12日 下午1:42:48
     * @Author lidong(dli@gdeng.cn)
     */
    @RequestMapping("sysMenu/getParentMenu/{menuID}")
    @ResponseBody
    public String getParentMenu(@PathVariable("menuID") String menuID, HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("menuModuleID", menuID);
            SysMenu parentMenu = sysMenuService.getParentMenu(map);
            return JSONObject.toJSONString(parentMenu, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description 获取二级菜单
     * @param menuID
     * @param request
     * @return
     * @CreationDate 2016年1月12日 下午1:42:56
     * @Author lidong(dli@gdeng.cn)
     */
    @RequestMapping("sysMenu/getChildMenu/{menuID}")
    @ResponseBody
    public String getChildMenu(@PathVariable("menuID") String menuID, HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("menuModuleID", menuID);
            List<SysMenu> list = sysMenuService.getSecondMenu(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /* ------------------------------------------ 按钮管理 ------------------------------------------ */

    /**
     * 菜单下的按钮列表页初始化
     * 
     * @author wwj
     * @date 创建时间：2015年7月24日 下午5:15:18
     * @param request
     * @return
     *
     */
    @RequestMapping("sysMenu/buttionList")
    public String buttionList(HttpServletRequest request) {
        request.setAttribute("menuID", StringUtils.trimToNull(request.getParameter("menuID")));
        request.setAttribute("view", StringUtils.trimToNull(request.getParameter("view")));
        return "sysmgr/sysmenu/buttonlist";
    }

    /**
     * 按钮分页查询;
     * 
     * @param request
     * @return path
     */
    @RequestMapping("sysMenu/buttionQuery")
    @ResponseBody
    public String buttionQuery(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 条件参数
            map.put("menuID", StringUtils.trimToNull(request.getParameter("menuID")));
            map.put("btnCode", StringUtils.trimToNull(request.getParameter("btnCode")));
            map.put("btnName", StringUtils.trimToNull(request.getParameter("btnName")));
            map.put("attribute", StringUtils.trimToNull(request.getParameter("attribute")));
            // 记录数
            map.put("total", sysMenuButtonService.getTotal(map));
            // 设定分页,排序
            setCommParameters(request, map);
            List<SysMenuButton> list = sysMenuButtonService.getByCondition(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 新增按钮-页面初始化
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/addButtonInit")
    public String addButtonInit(HttpServletRequest request) {
        request.setAttribute("menuID", StringUtils.trimToNull(request.getParameter("menuID")));
        request.setAttribute("actionUrl", "addButton");
        return "sysmgr/sysmenu/buttonedit";
    }

    /**
     * 新增按钮-保存动作
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/addButton")
    @ResponseBody
    public String addSysButton(HttpServletRequest request) throws Exception {
        try {
            SysMenuButton sysMenuButton = new SysMenuButton();
            // 参数
            String btnCode = StringUtils.trimToNull(request.getParameter("btnCode"));
            String btnName = StringUtils.trimToNull(request.getParameter("btnName"));
            String menuID = StringUtils.trimToNull(request.getParameter("menuID"));
            // 封装参数
            sysMenuButton.setBtnID(IdCreater.newId());
            sysMenuButton.setBtnCode(btnCode);
            sysMenuButton.setBtnName(btnName);
            sysMenuButton.setMenuID(menuID);
            sysMenuButton.setCreateUserID(LoginUserUtil.getLoginUserId(request));
            String message = sysMenuButtonService.insert(sysMenuButton);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 修改按钮-页面初始化
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/updateButtonInit")
    public String updateButtonInit(HttpServletRequest request) throws Exception {
        String btnID = StringUtils.trimToNull(request.getParameter("btnID"));
        SysMenuButton sysMenuButton = sysMenuButtonService.getSysButtonByID(btnID);
        request.setAttribute("dto", sysMenuButton);
        request.setAttribute("actionUrl", "updateButton");
        return "sysmgr/sysmenu/buttonedit";
    }

    /**
     * 修改按钮-保存动作
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysMenu/updateButton")
    @ResponseBody
    public String updateSysButton(HttpServletRequest request) {
        try {
            SysMenuButton sysMenuButton = new SysMenuButton();
            String btnID = request.getParameter("btnID");
            sysMenuButton = sysMenuButtonService.getSysButtonByID(btnID);
            // 参数
            String btnCode = StringUtils.trimToNull(request.getParameter("btnCode"));
            String btnName = StringUtils.trimToNull(request.getParameter("btnName"));
            String menuID = StringUtils.trimToNull(request.getParameter("menuID"));
            // 封装参数
            sysMenuButton.setBtnCode(btnCode);
            sysMenuButton.setBtnName(btnName);
            sysMenuButton.setMenuID(menuID);
            String message = sysMenuButtonService.update(sysMenuButton);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 删除按钮
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("sysMenu/deleteButton")
    @ResponseBody
    public String deleteSysButton(HttpServletRequest request, HttpServletResponse response) {
        try {
            String btnIDStr = request.getParameter("btnIDs");
            String[] btnIDs = btnIDStr.split(",");
            List<String> listBtnIDs = Arrays.asList(btnIDs);
            String message = sysMenuButtonService.delete(listBtnIDs);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * @Description getFirstMenu 生成按钮管理中的菜单树
     * @param request
     * @param dto
     * @return
     * @throws Exception
     * @CreationDate 2015年11月24日 下午12:01:08
     * @Author lidong(dli@cnagri-products.com)
     */
    @RequestMapping("sysMenu/getFirstMenu")
    @ResponseBody
    public String getFirstMenu(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        /** =======节点类型 0：根节点，无意义 1：菜单 9：按钮============ **/
        /** =======1 添加根节点============ **/
        TreeNode treeLevel_0_Node = new TreeNode();// 根节点
        List<TreeNode> treeLevel_0_Children = new ArrayList<>();// 根节点子节点(一级菜单节点)
        Map<String, Object> treeLevel_0_Attributes = new HashMap<>();// 根节点自定义属性
        treeLevel_0_Node.setId("0");
        treeLevel_0_Node.setText("系统【按钮】管理");
        treeLevel_0_Node.setState("open");// 根节点展开
        treeLevel_0_Attributes.put("type", 0);// 节点类型,根节点
        treeLevel_0_Node.setAttributes(treeLevel_0_Attributes);

        /** =======2 添加一级菜单============ **/
        List<SysMenu> level_1_list = sysMenuService.getFirstMenu(map);// 获取一级菜单集合
        if (level_1_list != null && level_1_list.size() > 0) {
            for (int i = 0; i < level_1_list.size(); i++) {
                /** =======2 添加一级菜单开始============ **/
                SysMenu sysMenu = level_1_list.get(i);// 一级菜单实体
                if ("3".equals(sysMenu.getAttribute())) {
                    continue;
                }
                TreeNode treeLevel_1_Node = new TreeNode();// 一级菜单节点
                List<TreeNode> treeLevel_1_Children = new ArrayList<>();// 一级子节点(二级菜单节点)
                Map<String, Object> treeLevel_1_Attributes = new HashMap<>();// 一级菜单节点自定义属性
                treeLevel_1_Node.setId(sysMenu.getMenuID());
                treeLevel_1_Node.setText(sysMenu.getMenuName());
                treeLevel_1_Node.setState("closed");// 一级菜单闭合
                treeLevel_1_Attributes.put("type", 1);// 节点类型,菜单
                treeLevel_1_Node.setAttributes(treeLevel_1_Attributes);

                /** =======3 添加二级菜单============ **/
                map.clear();
                map.put("menuModuleID", sysMenu.getMenuID());
                List<SysMenu> level_2_list = sysMenuService.getSecondMenu(map);// 获取二级菜单集合
                if (level_2_list != null && level_2_list.size() > 0) {
                    for (int j = 0; j < level_2_list.size(); j++) {
                        SysMenu sysMenu2 = level_2_list.get(j);// 二级菜单实体
                        TreeNode treeLevel_2_Node = new TreeNode();// 二级菜单节点
                        List<TreeNode> treeLevel_2_Children = new ArrayList<>();// 二级子节点(三级菜单节点集合)
                        Map<String, Object> treeLevel_2_Attributes = new HashMap<>();// 一级菜单节点自定义属性
                        treeLevel_2_Node.setId(sysMenu2.getMenuID());
                        treeLevel_2_Node.setText(sysMenu2.getMenuName());
                        treeLevel_2_Node.setState("open");// 二级菜单展开
                        treeLevel_2_Attributes.put("type", 1);// 节点类型,菜单
                        treeLevel_2_Node.setAttributes(treeLevel_2_Attributes);

                        /** =======4 添加三级菜单============ **/
                        map.clear();
                        map.put("menuModuleID", sysMenu2.getMenuID());
                        List<SysMenu> level_3_list = sysMenuService.getSecondMenu(map);// 获取三级菜单集合
                        if (level_3_list != null && level_3_list.size() > 0) {
                            treeLevel_2_Node.setState("closed");// 若有三级菜单，二级菜单闭合
                            for (int k = 0; k < level_3_list.size(); k++) {
                                SysMenu sysMenu3 = level_3_list.get(k);// 三级菜单实体
                                TreeNode treeLevel_3_Node = new TreeNode();// 二级菜单节点
                                List<TreeNode> treeLevel_3_Children = new ArrayList<>();// 三级子节点(四级菜单节点集合)
                                Map<String, Object> treeLevel_3_Attributes = new HashMap<>();// 一级菜单节点自定义属性
                                treeLevel_3_Node.setId(sysMenu3.getMenuID());
                                treeLevel_3_Node.setText(sysMenu3.getMenuName());
                                treeLevel_3_Node.setState("open");// 三级菜单展开
                                treeLevel_3_Attributes.put("type", 1);// 节点类型,菜单
                                treeLevel_3_Node.setAttributes(treeLevel_3_Attributes);
                                /*** 将一个三级菜单节点加入二级菜单节点子节点集合中 ***/
                                treeLevel_2_Children.add(treeLevel_3_Node);
                                /*** 将所有四级菜单或者按钮节点集合加入三级菜单节点子节点中 ***/
                                treeLevel_3_Node.setChildren(treeLevel_3_Children);
                            }
                        }
                        /*** 将一个二级菜单节点加入一级菜单节点子节点集合中 ***/
                        treeLevel_1_Children.add(treeLevel_2_Node);
                        /*** 将所有三级菜单或者按钮节点集合加入二级菜单节点子节点中 ***/
                        treeLevel_2_Node.setChildren(treeLevel_2_Children);
                    }
                } else {
                    treeLevel_1_Node.setState("open");// 一级菜单展开
                }

                /*** 将所有二级菜单节点集合加入一级菜单节点子节点中 ***/
                treeLevel_1_Node.setChildren(treeLevel_1_Children);
                /*** 将一个一级菜单节点加入根节点子节点集合中 ***/
                treeLevel_0_Children.add(treeLevel_1_Node);
                /** =======1 根节点添加一个一级菜单节点结束============ **/
            }

        }
        /*** 将所有一级菜单节点加入根节点子节点中 ***/
        treeLevel_0_Node.setChildren(treeLevel_0_Children);
        /** =======1 添加根节点结束============ **/

        /*** 构建easyUI树 ***/
        List<TreeNode> jsonList = new ArrayList<>();
        jsonList.add(treeLevel_0_Node);
        return JSONObject.toJSONString(jsonList, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * @Description getMenuButtonTree 生成菜单 按钮 分配 的easyUI tree
     * @param request
     * @param dto
     * @return
     * @throws Exception
     * @CreationDate 2015年11月24日 下午12:00:32
     * @Author lidong(dli@cnagri-products.com)
     */
    @RequestMapping("sysMenu/getMenuButtonTree")
    @ResponseBody
    public String getMenuButtonTree(HttpServletRequest request) throws Exception {
        String roleID = request.getParameter("roleID");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleID", roleID);
        List<SysRightBtn> buttonsByRole = sysRoleManagerService.getButtonsByRole(roleID);
        List<SysMenu> allMenuRole = sysMenuService.getAllMenuRole(map);
        /** =======节点类型 0：根节点，无意义 1：菜单 9：按钮============ **/
        /** =======1 添加根节点============ **/
        TreeNode treeLevel_0_Node = new TreeNode();// 根节点
        List<TreeNode> treeLevel_0_Children = new ArrayList<>();// 根节点子节点(一级菜单节点)
        Map<String, Object> treeLevel_0_Attributes = new HashMap<>();// 根节点自定义属性
        treeLevel_0_Node.setId("0");
        treeLevel_0_Node.setText("系统【菜单-按钮】权限分配管理");
        treeLevel_0_Node.setState("open");// 根节点展开
        treeLevel_0_Attributes.put("type", 0);// 节点类型,根节点
        map.clear();
        map.put("attribute", 1);
        List<SysMenu> firstMenu = sysMenuService.getFirstMenu(map);// 获取一级菜单集合
        if (firstMenu != null && firstMenu.size() > 0) {
            for (int i = 0; i < firstMenu.size(); i++) {
                /** =======2 添加一级菜单节点============ **/
                SysMenu sysMenu = firstMenu.get(i);// 一级菜单实例
                TreeNode treeLevel_1_Node = new TreeNode();// 根节点
                List<TreeNode> treeLevel_1_Children = new ArrayList<>();// 一级菜单节点子节点(二级级菜单节点集合)
                Map<String, Object> treeLevel_1_Attributes = new HashMap<>();// 一级菜单节点自定义属性
                treeLevel_1_Node.setId(sysMenu.getMenuID());
                treeLevel_1_Node.setText(sysMenu.getMenuName());
                treeLevel_1_Node.setState("closed");// 一级菜单节点闭合
                treeLevel_1_Attributes.put("type", 1);// 节点类型,菜单

                map.clear();
                map.put("menuModuleID", sysMenu.getMenuID());
                List<SysMenu> secondMenu = sysMenuService.getSecondMenu(map);// 二级菜单集合
                if (secondMenu != null && secondMenu.size() > 0) {
                    /** =======3 添加二级菜单节点============ **/
                    for (int j = 0; j < secondMenu.size(); j++) {
                        SysMenu sysMenu2 = secondMenu.get(j);
                        TreeNode treeLevel_2_Node = new TreeNode();// 根节点
                        List<TreeNode> treeLevel_2_Children = new ArrayList<>();// 二级菜单节点子节点(三级级菜单节点集合或按钮集合)
                        Map<String, Object> treeLevel_2_Attributes = new HashMap<>();// 二级菜单节点自定义属性
                        treeLevel_2_Node.setId(sysMenu2.getMenuID());
                        treeLevel_2_Node.setText(sysMenu2.getMenuName());
                        treeLevel_2_Node.setState("closed");// 二级菜单节点闭合
                        treeLevel_2_Attributes.put("type", 1);// 节点类型,菜单

                        /** =======3-1 添加三级菜单节点============ **/
                        map.clear();
                        map.put("menuModuleID", sysMenu2.getMenuID());
                        List<SysMenu> secondMenu2 = sysMenuService.getSecondMenu(map);// 二级菜单集合
                        /** =======3-2 添加三级按钮节点============ **/
                        // 根据菜单ID查找按钮,拼接二级菜单下的按钮
                        map.clear();
                        map.put("menuID", sysMenu2.getMenuID());
                        map.put("startRow", 0);
                        map.put("endRow", 99999999);
                        // 根据菜单ID查找按钮,拼接二级菜单下的按钮
                        List<SysMenuButton> menuButtons = sysMenuButtonService.getByCondition(map);

                        /***** 添加二级菜单下按钮 ****/
                        if (menuButtons != null && menuButtons.size() > 0) {
                            for (int k = 0; k < menuButtons.size(); k++) {
                                SysMenuButton sysMenuButton = menuButtons.get(k);
                                TreeNode treeLevel_3_Node = new TreeNode();// 根节点
                                List<TreeNode> treeLevel_3_Children = new ArrayList<>();// 三级菜单节点子节点(四级级菜单节点集合或按钮集合)
                                Map<String, Object> treeLevel_3_Attributes = new HashMap<>();// 三级菜单节点自定义属性
                                treeLevel_3_Node.setId(sysMenuButton.getBtnID());
                                treeLevel_3_Node.setText(sysMenuButton.getBtnName());
                                treeLevel_3_Node.setState("open");// 三级按钮节点展开
                                treeLevel_3_Attributes.put("type", 9);// 节点类型,按钮
                                if (checkButton(buttonsByRole, sysMenuButton.getBtnID())) {
                                    treeLevel_3_Node.setChecked("true");
                                }
                                treeLevel_3_Node.setAttributes(treeLevel_3_Attributes);
                                treeLevel_3_Node.setChildren(treeLevel_3_Children);
                                treeLevel_2_Children.add(treeLevel_3_Node);
                            }
                        } else if (secondMenu2 != null && secondMenu2.size() > 0) {
                            /***** 添加二级菜单下三级菜单 ****/
                            for (int k = 0; k < secondMenu2.size(); k++) {
                                SysMenu sysMenu3 = secondMenu2.get(k);
                                TreeNode treeLevel_3_Node = new TreeNode();// 根节点
                                List<TreeNode> treeLevel_3_Children = new ArrayList<>();// 三级菜单节点子节点(四级级菜单节点集合或按钮集合)
                                Map<String, Object> treeLevel_3_Attributes = new HashMap<>();// 三级菜单节点自定义属性
                                treeLevel_3_Node.setId(sysMenu3.getMenuID());
                                treeLevel_3_Node.setText(sysMenu3.getMenuName());
                                treeLevel_3_Node.setState("closed");// 三级菜单节点闭合
                                treeLevel_3_Attributes.put("type", 1);// 节点类型,菜单

                                // 根据菜单ID查找按钮,拼接三级菜单下的按钮
                                map.clear();
                                map.put("menuID", sysMenu3.getMenuID());
                                map.put("startRow", 0);
                                map.put("endRow", 99999999);
                                // 根据菜单ID查找按钮,拼接二级菜单下的按钮
                                List<SysMenuButton> menuButtons2 = sysMenuButtonService.getByCondition(map);
                                if (menuButtons2 != null && menuButtons2.size() > 0) {
                                    for (int l = 0; l < menuButtons2.size(); l++) {
                                        SysMenuButton sysMenuButton = menuButtons2.get(l);
                                        TreeNode treeLevel_4_Node = new TreeNode();// 根节点
                                        List<TreeNode> treeLevel_4_Children = new ArrayList<>();// 三级菜单节点子节点(四级级菜单节点集合或按钮集合)
                                        Map<String, Object> treeLevel_4_Attributes = new HashMap<>();// 三级菜单节点自定义属性
                                        treeLevel_4_Node.setId(sysMenuButton.getBtnID());
                                        treeLevel_4_Node.setText(sysMenuButton.getBtnName());
                                        treeLevel_4_Node.setState("open");// 四级按钮节点闭合
                                        treeLevel_4_Attributes.put("type", 9);// 节点类型,按钮
                                        if (checkButton(buttonsByRole, sysMenuButton.getBtnID())) {
                                            treeLevel_4_Node.setChecked("true");
                                        }
                                        treeLevel_4_Node.setAttributes(treeLevel_4_Attributes);
                                        treeLevel_4_Node.setChildren(treeLevel_4_Children);
                                        treeLevel_3_Children.add(treeLevel_4_Node);
                                    }
                                } else {
                                    // 若二级菜单下无按钮、无菜单，二级菜单展开，并判断选中状态
                                    treeLevel_3_Node.setState("open");// 三级菜单节点展开
                                    if (checkMenu(allMenuRole, sysMenu3.getId())) {
                                        treeLevel_3_Node.setChecked("true");
                                    }
                                }

                                treeLevel_3_Node.setAttributes(treeLevel_3_Attributes);
                                treeLevel_3_Node.setChildren(treeLevel_3_Children);
                                treeLevel_2_Children.add(treeLevel_3_Node);
                            }
                        } else {
                            // 若二级菜单下无按钮、无菜单，二级菜单展开，并判断选中状态
                            treeLevel_2_Node.setState("open");// 二级菜单节点展开
                            if (checkMenu(allMenuRole, sysMenu2.getMenuID())) {
                                treeLevel_2_Node.setChecked("true");
                            }
                        }
                        treeLevel_2_Node.setAttributes(treeLevel_2_Attributes);
                        treeLevel_2_Node.setChildren(treeLevel_2_Children);
                        /** =======2 添加二级菜单节点结束============ **/
                        treeLevel_1_Children.add(treeLevel_2_Node);
                    }
                } else {
                    // 若一级菜单下无菜单，一级菜单展开，并判断选中状态
                    treeLevel_1_Node.setState("open");// 一级菜单节点展开
                    if (checkMenu(allMenuRole, sysMenu.getMenuID())) {
                        treeLevel_1_Node.setChecked("true");
                    }
                }
                treeLevel_1_Node.setAttributes(treeLevel_1_Attributes);
                treeLevel_1_Node.setChildren(treeLevel_1_Children);
                /** =======2 添加一级菜单节点结束============ **/
                treeLevel_0_Children.add(treeLevel_1_Node);
            }
        }
        treeLevel_0_Node.setAttributes(treeLevel_0_Attributes);
        treeLevel_0_Node.setChildren(treeLevel_0_Children);
        /** =======1 添加根节点结束============ **/

        /**** 构建easyUI树 ****/
        List<TreeNode> jsonList = new ArrayList<>();
        jsonList.add(treeLevel_0_Node);
        return JSONObject.toJSONString(jsonList, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * @Description checkButton 检测一个按钮是否存在按钮集合中
     * @param buttons
     * @param button
     * @return
     * @CreationDate 2015年11月24日 上午10:58:44
     * @Author lidong(dli@cnagri-products.com)
     */
    private boolean checkButton(List<SysRightBtn> buttons, String btnID) {
        boolean hasNext = false;
        for (SysRightBtn sysRightBtn : buttons) {
            if (btnID.equals(sysRightBtn.getBtnId())) {
                hasNext = true;
                return hasNext;
            }
        }
        return hasNext;
    }

    /**
     * @Description checkMenu 检测一个菜单是否在菜单集合中
     * @param menus
     * @param menuID
     * @return
     * @CreationDate 2015年11月24日 上午11:16:43
     * @Author lidong(dli@cnagri-products.com)
     */
    private boolean checkMenu(List<SysMenu> menus, String menuID) {
        boolean hasNext = false;
        for (SysMenu menu : menus) {
            if (menuID.equals(menu.getMenuID())) {
                hasNext = true;
                return hasNext;
            }
        }
        return hasNext;
    }

}