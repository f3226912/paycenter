package cn.gdeng.paycenter.admin.controller.right;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.service.right.SysRegisterUserService;
import cn.gdeng.paycenter.admin.service.right.SysRoleService;
import cn.gdeng.paycenter.admin.service.right.SysUserRoleService;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.entity.pay.SysRole;
import cn.gdeng.paycenter.entity.pay.SysUserRole;
import cn.gdeng.paycenter.util.admin.web.LoginUserUtil;
import cn.gdeng.paycenter.util.web.api.CommonConstant;

/**
 * 用户角色controller类
 * 
 * @author songhui
 *
 */
@Controller
@RequestMapping("sysmgr")
public class SysUserRoleController extends AdminBaseController {

    // 日志
    private GdLogger logger = GdLoggerFactory.getLogger(SysUserRoleController.class);

    /** 用户角色Serivce */
    @Reference
    private SysUserRoleService sysUserRoleService;
    /** 角色Serivce */
    @Reference
    private SysRoleService sysRoleService;

    @Reference
    private SysRegisterUserService sysRegisterUserService;

    /**
     * 分配用户角色 - 页面初始化;
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("sysUserRole/assignRoleList")
    public String assignRoleList(HttpServletRequest request) {

        String roleID = StringUtils.trimToNull(request.getParameter("roleID"));
        // 根据角色ID查询
        try {
            SysRole dto = sysRoleService.getSysRoleById(roleID);
            request.setAttribute("dto", dto);
            request.setAttribute("view", StringUtils.trimToNull(request.getParameter("view")));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return "sysmgr/sysrole/assignrolelist";
    }

    /**
     * 查询被分配用户列表
     * 
     * @author songhui
     * @date 创建时间：2015年7月27日 下午6:19:17
     * @param request
     * @return
     *
     */
    @RequestMapping("sysUserRole/assignRoleQuery")
    @ResponseBody
    public String assignRoleQuery(HttpServletRequest request) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 获取前台查询条件
            map.put("userCode", StringUtils.trimToNull(request.getParameter("userCode")));
            map.put("userName", StringUtils.trimToNull(request.getParameter("userName")));
            map.put("locked", StringUtils.trimToNull(request.getParameter("locked")));
            map.put("roleID", StringUtils.trimToNull(request.getParameter("roleID")));
            map.put("orgUnitName", StringUtils.trimToNull(request.getParameter("orgUnitName")));
            // String view = StringUtils.trimToNull(request.getParameter("view"));
            // if (StringUtils.isNotBlank(view)) {
            // // 只查看被要配角色的用户标识
            // map.put("isAuth", 1);
            // }
            // 当前登录用户可以查看的用户类型
            // SysRegisterUser loginUser=LoginUserUtil.getSysRegisterUser(request);
            // 系统和湘联采：查看所有系统里面的用户；学校类型的用户可以查看：学校和学校下食堂的用户；其他只能查看本组织下的用户
            // if(!(UserType.SYSTEM==loginUser.getType()||UserType.PURCHASE_CENTER==loginUser.getType())){
            // if(UserType.SCHOOL==loginUser.getType()){
            // map.put("schoolId", loginUser.getOrgUnitId());
            // }else{
            // map.put("orgUnitId", loginUser.getOrgUnitId());
            // }
            // }
            // 记录数
            map.put("total", sysUserRoleService.getSysUserCount(map));
            // 设定分页,排序
            setCommParameters(request, map);
            // 查询所有的菜单和按钮信息到前台展示
            List<SysUserRole> list = sysUserRoleService.getSysUserList(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 新增用户角色 - 保存动作;
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysUserRole/assignRole")
    @ResponseBody
    public String assignRole(HttpServletRequest request, HttpServletResponse response) {

        try {
            // 获取页面角色ID、用户ID集合
            String roleID = StringUtils.trimToNull(request.getParameter("roleID"));
            String userIDs = StringUtils.trimToNull(request.getParameter("userIDs"));
            sysUserRoleService.insertBatch(userIDs, roleID, LoginUserUtil.getLoginUserId(request));
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
        return CommonConstant.COMMON_AJAX_SUCCESS;
    }

    /**
     * 给用户分配角色页面初始化
     * 
     * @author songhui
     * @date 创建时间：2015年8月4日 上午10:26:40
     * @param request
     * @return
     *
     */
    @RequestMapping("sysUserRole/assignUserList")
    public String assignUserList(HttpServletRequest request) throws Exception {

        String userID = StringUtils.trimToNull(request.getParameter("userID"));
        if (StringUtils.isNotBlank(userID)) {
            // 现有用户分配
            // 根据用户ID查询用户
            SysRegisterUser dto = sysRegisterUserService.get(userID);
            request.setAttribute("dto", dto);
        }
        // 详情标识
        request.setAttribute("view", StringUtils.trimToNull(request.getParameter("view")));
        return "sysmgr/sysregisteruser/assignuserlist";
    }

    /**
     * 查询用户分配的所有角色和未分配的角色
     * 
     * @date 创建时间：2015年8月4日 上午10:30:22
     * @param request
     * @return
     *
     */
    @RequestMapping("sysUserRole/assignUserQuery")
    @ResponseBody
    public String assignUserQuery(HttpServletRequest request) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 获取前台查询条件
            map.put("userID", StringUtils.trimToNull(request.getParameter("userID")));
            String roleName = StringUtils.trimToNull(request.getParameter("roleName"));
            map.put("roleName", roleName);
            map.put("attribute", StringUtils.trimToNull(request.getParameter("attribute")));
            // 记录数
            map.put("total", sysUserRoleService.getUserAllRoleCount(map));
            // 设定分页,排序
            setCommParameters(request, map);
            // 查询所有的菜单和按钮信息到前台展示
            List<SysUserRole> list = sysUserRoleService.getUserAllRoleList(map);
            List<SysUserRole> tmp = new ArrayList<>();
            if (StringUtils.isNotEmpty(roleName)) {
                for (SysUserRole userRole : list) {
                    if (userRole.getRoleName().contains(roleName) || userRole.getIsAuth().equals("1")) {
                        tmp.add(userRole);
                    }
                }
                map.put("rows", tmp);
            } else {
                map.put("rows", list);
            }
            // rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 给用户分配角色
     * 
     * @author songhui
     * @date 创建时间：2015年8月4日 上午11:09:58
     * @param request
     * @return
     *
     */
    @RequestMapping("sysUserRole/assignUser")
    @ResponseBody
    public String assignUser(HttpServletRequest request) {

        try {
            // 获取页面用户ID、角色ID集合
            String roleIDs = StringUtils.trimToNull(request.getParameter("roleIDs"));
            String userID = StringUtils.trimToNull(request.getParameter("userID"));
            sysUserRoleService.insertUserRoleBatch(userID, roleIDs, LoginUserUtil.getLoginUserId(request));
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
        return CommonConstant.COMMON_AJAX_SUCCESS;
    }

}
