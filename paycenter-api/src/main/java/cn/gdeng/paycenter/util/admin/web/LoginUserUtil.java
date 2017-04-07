package cn.gdeng.paycenter.util.admin.web;

import javax.servlet.http.HttpServletRequest;

import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.web.api.Constant;

/**
 * 登录用户的共通工具类
 * 
 * @author 胡勇
 * @date 2012-03-20
 * 
 */
public class LoginUserUtil {

    /**
     * 取得登录用户的名称;userName
     * 
     * @param request
     * @return 用户的名称
     * @throws Exception
     */
    public static String getLoginUserName(HttpServletRequest request) {
        if (request.getSession().getAttribute(Constant.SYSTEM_USERNAME) != null) {
            return request.getSession().getAttribute(Constant.SYSTEM_USERNAME).toString();
        } else {
            return null;
        }
    }

    /**
     * 取得登录用户的ID;
     * 
     * @param request
     * @return 登录用户的ID
     * @throws Exception
     */
    public static String getLoginUserId(HttpServletRequest request) {

        if (request.getSession().getAttribute(Constant.SYSTEM_USERID) != null) {
            return request.getSession().getAttribute(Constant.SYSTEM_USERID).toString();
        } else {
            return null;
        }
    }

    /**
     * 获取用户登录账号 userCode
     * 
     * @author wwj
     * @date 创建时间：2015年7月29日 下午2:49:57
     * @param request
     * @return
     * @throws Exception
     * 
     */
    public static String getLoginUserCode(HttpServletRequest request) {

        if (request.getSession().getAttribute(Constant.SYSTEM_USERCODE) != null) {
            return request.getSession().getAttribute(Constant.SYSTEM_USERCODE).toString();
        } else {
            return null;
        }
    }

    /**
     * 获取用户登录对象 SysRegisterUser
     * 
     * @author wwj
     * @date 创建时间：2015年7月29日 下午2:50:54
     * @param request
     * @return
     * @throws Exception
     * 
     */
    public static SysRegisterUser getSysRegisterUser(HttpServletRequest request) {

        if (request.getSession().getAttribute(Constant.SYSTEM_SMGRLOGINUSER) != null) {
            return (SysRegisterUser) request.getSession().getAttribute(Constant.SYSTEM_SMGRLOGINUSER);
        } else {
            return null;
        }
    }

    /**
     * 获取登录用户类型
     * 
     * @author wwj
     * @date 创建时间：2015年8月7日 上午11:20:26
     * @param request
     * @return
     *
     */
    public static Integer getUserType(HttpServletRequest request) {

        if (request.getSession().getAttribute(Constant.SYSTEM_SMGRLOGINUSER) != null) {
            SysRegisterUser user = (SysRegisterUser) request.getSession().getAttribute(Constant.SYSTEM_SMGRLOGINUSER);
            return user.getType();
        } else {
            return null;
        }
    }

    /**
     * 通过HttpServletRequest返回IP地址
     * 
     * @param request
     *            HttpServletRequest
     * @return ip String
     * @throws Exception
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
