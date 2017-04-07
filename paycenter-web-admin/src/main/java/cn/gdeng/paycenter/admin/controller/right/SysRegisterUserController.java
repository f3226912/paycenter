package cn.gdeng.paycenter.admin.controller.right;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import cn.gdeng.paycenter.admin.service.right.SysUserRoleService;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.admin.web.LoginUserUtil;
import cn.gdeng.paycenter.util.admin.web.StringUtil;
import cn.gdeng.paycenter.util.web.api.CommonConstant;
import cn.gdeng.paycenter.util.web.api.CommonConstant.UserLowType;
import cn.gdeng.paycenter.util.web.api.CommonConstant.UserType;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.IdCreater;

/**
 * 用户controller
 * 
 * @author wwj
 * 
 */
@Controller
@RequestMapping("sysmgr")
public class SysRegisterUserController extends AdminBaseController {

    private static final GdLogger logger = GdLoggerFactory.getLogger(SysRegisterUserController.class);

    /** 用户Serivce */
    @Reference
    private SysRegisterUserService sysRegisterUserService;

    /** 用户角色 */
    @Reference
    private SysUserRoleService sysUserRoleService;

    @Resource
    private GdProperties gdProperties;

    /**
     * 用户列表初始化
     * 
     * @author songhui
     * @date 创建时间：2015年8月3日 下午3:30:46
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/list")
    public String list(HttpServletRequest request) {

        // 查询当前用户可以查看的用户类型
        SysRegisterUser dto = LoginUserUtil.getSysRegisterUser(request);
        request.setAttribute("typeList", UserLowType.getLowTypeDTOList(dto.getType()));
        return "sysmgr/sysregisteruser/sysuserlist";
    }

    /**
     * 用户选择
     * 
     * @author songhui
     * @date 创建时间：2015年8月13日 下午3:42:06
     * @param request
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/select")
    public String select(HttpServletRequest request) {

        request.setAttribute("orgUnitId", StringUtils.trimToNull(request.getParameter("orgUnitId")));
        request.setAttribute("type", StringUtils.trimToNull(request.getParameter("type")));
        // 查询当前用户可以查看的用户类型
        SysRegisterUser dto = LoginUserUtil.getSysRegisterUser(request);
        request.setAttribute("typeList", UserLowType.getLowTypeDTOList(dto.getType()));
        return "sysmgr/sysregisteruser/sysuserselect";
    }

    /**
     * 用户选择
     * 
     * @param request
     * @return path
     * @throws Exception
     */
    @RequestMapping("sysRegisterUser/chose")
    @ResponseBody
    public String chose(HttpServletRequest request, HttpServletResponse response) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 设置查询参数
            map.put("userCode", StringUtils.trimToNull(request.getParameter("userCode")));
            map.put("userName", StringUtils.trimToNull(request.getParameter("userName")));
            map.put("locked", StringUtils.trimToNull(request.getParameter("locked")));
            map.put("roleTotal", StringUtils.trimToNull(request.getParameter("roleTotal")));
            map.put("mobile", StringUtils.trimToNull(request.getParameter("mobile")));
            map.put("orgUnitId", StringUtils.trimToNull(request.getParameter("orgUnitId")));
            // 记录数
            map.put("total", sysRegisterUserService.getTotal(map));
            // 设定分页,排序
            setCommParameters(request, map);
            // list
            List<SysRegisterUser> list = sysRegisterUserService.getByCondition(map);
            ;
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 用户列表;
     * 
     * @param request
     * @return path
     * @throws Exception
     */
    @RequestMapping("sysRegisterUser/query")
    @ResponseBody
    public String query(HttpServletRequest request, HttpServletResponse response) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 设置查询参数
            map.put("userCode", StringUtils.trimToNull(request.getParameter("userCode")));
            map.put("userName", StringUtils.trimToNull(request.getParameter("userName")));
            map.put("locked", StringUtils.trimToNull(request.getParameter("locked")));
            // map.put("orgUnitName",
            // StringUtils.trimToNull(request.getParameter("orgUnitName")));
            // map.put("type",
            // StringUtils.trimToNull(request.getParameter("type")));
            map.put("roleTotal", StringUtils.trimToNull(request.getParameter("roleTotal")));
            map.put("mobile", StringUtils.trimToNull(request.getParameter("mobile")));
            // 当前登录用户可以查看的用户类型
            // SysRegisterUser loginUser = LoginUserUtil
            // .getSysRegisterUser(request);
            // 系统和湘联采：查看所有系统里面的用户；学校类型的用户可以查看：学校和学校下食堂的用户；其他只能查看本组织下的用户
            // if (!(UserType.SYSTEM == loginUser.getType() || UserType.PURCHASE_CENTER == loginUser
            // .getType())) {
            // if (UserType.SCHOOL == loginUser.getType()) {
            // map.put("schoolId", loginUser.getOrgUnitId());
            // } else {
            // map.put("orgUnitId", loginUser.getOrgUnitId());
            // }
            // }
            // 记录数
            map.put("total", sysRegisterUserService.getTotal(map));
            // 设定分页,排序
            setCommParameters(request, map);
            // list
            List<SysRegisterUser> list = sysRegisterUserService.getByCondition(map);
            ;
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 新增用户- 页面初始化;
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysRegisterUser/addInit")
    public String addInit(HttpServletRequest request) {

        request.setAttribute("actionUrl", "add");
        return "sysmgr/sysregisteruser/sysuseredit";
    }

    /**
     * 根据组织和类型初始新增页面
     * 
     * @author songhui
     * @date 创建时间：2015年8月13日 下午5:10:04
     * @param request
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/addSelectInit")
    public String addSelectInit(HttpServletRequest request) {

        request.setAttribute("actionUrl", "add");
        request.setAttribute("orgUnitId", StringUtils.trimToNull(request.getParameter("orgUnitId")));
        request.setAttribute("type", StringUtils.trimToNull(request.getParameter("type")));
        return "sysmgr/sysregisteruser/sysuserselectedit";
    }

    /**
     * 新增用户- 保存动作;
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysRegisterUser/add")
    @ResponseBody
    public String add(HttpServletRequest request) {

        try {
            SysRegisterUser user = new SysRegisterUser();
            // 参数
            String userCode = StringUtils.trimToNull(request.getParameter("userCode"));
            String userName = StringUtils.trimToNull(request.getParameter("userName"));
            String passWord = StringUtils.trimToNull(request.getParameter("passWord"));
            String type = StringUtils.trimToNull(request.getParameter("type"));
            String orgUnitId = StringUtils.trimToNull(request.getParameter("orgUnitId"));
            // 封装参数
            String userID = IdCreater.newId();
            user.setUserID(userID);
            user.setLocked("0");
            user.setUserCode(userCode);
            user.setUserName(userName);
            user.setUserPassWord(StringUtil.stringEncryptMD5(passWord));
            user.setType(type == null ? 0 : Integer.parseInt(type));
            user.setOrgUnitId(orgUnitId);
            user.setMobile(StringUtils.trimToNull(request.getParameter("mobile")));
            user.setCreateUserID(LoginUserUtil.getLoginUserId(request));
            // 新增
            String message = sysRegisterUserService.insert(user);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 修改用户初始化页面
     * 
     * @author songhui
     * @date 创建时间：2015年7月23日 下午5:36:07
     * @param request
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/updateInit")
    public String updateInit(HttpServletRequest request) throws Exception {

        request.setAttribute("actionUrl", "update");
        String userID = request.getParameter("userID");
        SysRegisterUser sysRegisterUser = sysRegisterUserService.get(userID);
        request.setAttribute("dto", sysRegisterUser);
        return "sysmgr/sysregisteruser/sysuseredit";
    }

    /**
     * 根据组织和类型修改页初始化
     * 
     * @author songhui
     * @date 创建时间：2015年8月13日 下午5:13:57
     * @param request
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/updateSelectInit")
    public String updateSelectInit(HttpServletRequest request) throws Exception {

        request.setAttribute("actionUrl", "update");
        String userID = request.getParameter("userID");
        SysRegisterUser sysRegisterUser = sysRegisterUserService.get(userID);
        request.setAttribute("dto", sysRegisterUser);
        return "sysmgr/sysregisteruser/sysuserselectedit";
    }

    /**
     * 修改用户- 保存动作;
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysRegisterUser/update")
    @ResponseBody
    public String update(HttpServletRequest request) throws Exception {

        try {
            // 参数
            String userID = request.getParameter("userID");
            String userName = StringUtils.trimToNull(request.getParameter("userName"));
            // 判断用户是否存在
            SysRegisterUser user = sysRegisterUserService.get(userID);
            if (null == user) {
                return "该用户已经不存在！";
            }
            // 封装参数
            user.setUserID(userID);
            user.setUserName(userName);
            user.setMobile(StringUtils.trimToNull(request.getParameter("mobile")));
            user.setOrgUnitId(StringUtils.trimToNull(request.getParameter("orgUnitId")));
            user.setPassword(StringUtils.trimToNull(request.getParameter("passWord")));
            // 修改
            String message = sysRegisterUserService.update(user);
            return message;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 修改用户密码初始化页面;
     * 
     * @author tanjun
     * @param request
     * @return
     */
    @RequestMapping(value = "sysRegisterUser/updateUserPWDInit")
    public String updateUserPWDInit(HttpServletRequest request) throws Exception {
        // if (LoginUserUtil.getLoginUserId(request) == null) {
        // return "redirect:" + PathUtil.getBasePath(request);
        // }
        return "sysmgr/sysregisteruser/sysusereditpwd";
    }

    /**
     * 修改用户密码
     * 
     * @author tanjun
     * @param request
     * @return
     */
    @RequestMapping(value = "sysRegisterUser/updateUserPwd")
    @ResponseBody
    public String updatePwd(HttpServletRequest request) throws Exception {

        String message = "";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 原密码
            String userPwd = request.getParameter("oldPWD");
            userPwd = StringUtil.stringEncryptMD5(userPwd);
            map.put("userPassWord", userPwd);
            // 新密码
            String userNewPassWord = request.getParameter("newPWD");
            userNewPassWord = StringUtil.stringEncryptMD5(userNewPassWord);
            map.put("userNewPassWord", userNewPassWord);
            // 用户ID;
            map.put("userID", LoginUserUtil.getLoginUserId(request));
            message = sysRegisterUserService.updateUserPwd(map);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
        return message;
    }

    /**
     * 整批删除用户 ;
     * 
     * @param request
     * @return
     */
    @RequestMapping("sysRegisterUser/delete")
    @ResponseBody
    public String deleteSysRegisterUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            // 获取页面id集合
            String tIds = request.getParameter("deleteStr");
            sysRegisterUserService.updateBatch(tIds, LoginUserUtil.getLoginUserId(request));
            List<String> userIdlist = Arrays.asList(tIds.split(","));
            for (String userID : userIdlist) {
                sysUserRoleService.deleteByUserID(userID);
            }
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
        return CommonConstant.COMMON_AJAX_SUCCESS;
    }

    /**
     * 锁定用户
     * 
     * @param request
     * @return url
     */
    @RequestMapping("sysRegisterUser/lock")
    @ResponseBody
    public String lockUser(HttpServletRequest request) throws Exception {

        try {
            String ids = request.getParameter("ids");
            if (null != ids) {
                // 参数
                SysRegisterUser user = new SysRegisterUser();
                user.setUpdateUserID(LoginUserUtil.getLoginUserId(request));
                String[] lockIdlist = ids.split(",");
                for (int i = 0; i < lockIdlist.length; i++) {
                    if (gdProperties.getSysSupperAdminId().equals(lockIdlist[i])) {
                        return gdProperties.getSysSupperAdminTip();
                    }
                    // 锁定用户
                    user.setUserID(lockIdlist[i]);
                    sysRegisterUserService.updateLockUser(user);
                }
            }
            return CommonConstant.COMMON_AJAX_SUCCESS;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 解锁用户
     * 
     * @param request
     * @return url
     */
    @RequestMapping("sysRegisterUser/unlock")
    @ResponseBody
    public String unlockUser(HttpServletRequest request) throws Exception {

        try {
            String ids = request.getParameter("ids");
            if (null != ids) {
                // 参数
                SysRegisterUser user = new SysRegisterUser();
                user.setUpdateUserID(LoginUserUtil.getLoginUserId(request));

                String[] lockIdlist = ids.split(",");
                for (int i = 0; i < lockIdlist.length; i++) {
                    // 解锁用户
                    user.setUserID(lockIdlist[i]);
                    sysRegisterUserService.updateUnlockUser(user);
                }
            }
            return CommonConstant.COMMON_AJAX_SUCCESS;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 密码重置
     * 
     * @param request
     * @return url
     */
    @RequestMapping("sysRegisterUser/resetPassword")
    @ResponseBody
    public String resetPassword(HttpServletRequest request) throws Exception {

        try {
            String ids = request.getParameter("ids");
            if (null != ids) {
                // 参数
                SysRegisterUser user = new SysRegisterUser();
                user.setUpdateUserID(LoginUserUtil.getLoginUserId(request));
                user.setUserPassWord(StringUtil.stringEncryptMD5("888888"));
                String[] resetIdlist = ids.split(",");
                for (int i = 0; i < resetIdlist.length; i++) {
                    user.setUserID(resetIdlist[i]);
                    sysRegisterUserService.updateResetPassword(user);
                }
            }
            return CommonConstant.COMMON_AJAX_SUCCESS;
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }

    /**
     * 选择用户组织初始化页面
     * 
     * @author songhui
     * @date 创建时间：2015年7月29日 下午5:31:14
     * @param request
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/choseOrgInit")
    public String choseOrgInit(HttpServletRequest request) {

        request.setAttribute("type", StringUtils.trimToNull(request.getParameter("type")));
        return "sysmgr/sysregisteruser/orglist";
    }

    /**
     * 根据当前用户获取当前用户可以创建的用户类型
     * 
     * @author songhui
     * @date 创建时间：2015年7月29日 下午7:45:33
     * @param request
     * @return
     * 
     */
    @RequestMapping("sysRegisterUser/getType")
    @ResponseBody
    public String getType(HttpServletRequest request) {

        Integer userType = LoginUserUtil.getUserType(request);
        if (userType != null) {
            // 除了系统用户和联采中心都不能创建自己，联采中心也只不能创建食堂
            List<UserLowType> typeList = UserLowType.getLowTypeDTOList(userType);
            if (typeList != null && typeList.size() > 0) {
                if (!(UserType.SYSTEM == userType || UserType.PURCHASE_CENTER == userType)) {
                    typeList.remove(0);
                }
                if (UserType.PURCHASE_CENTER == userType) {
                    for (UserLowType userLowType : typeList) {
                        if (UserType.CANTEEN == userLowType.getId()) {
                            typeList.remove(userLowType);
                            break;
                        }
                    }
                }
            }
            return JSONObject.toJSONString(typeList);
        }
        return null;
    }

}
