package cn.gdeng.paycenter.admin.controller.right;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;
import com.gudeng.paltform.sms.GdSMS;

import cn.gdeng.paycenter.admin.service.right.SysLoginService;
import cn.gdeng.paycenter.admin.util.CommonUtil;
import cn.gdeng.paycenter.dto.right.BaseMenu;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.admin.web.LoginUserUtil;
import cn.gdeng.paycenter.util.web.api.CommonConstant;
import cn.gdeng.paycenter.util.web.api.Constant;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.MessageUtil;
import cn.gdeng.paycenter.util.web.api.PathUtil;

/**
 * 用户登录的action;
 * 
 * @author huyong;
 * @date 2012-06-11;
 * 
 */
@Controller
@RequestMapping("sys")
public class LoginController extends AdminBaseController {

    /** 用户登录的Service */
	@Reference
    private SysLoginService sysLoginService;
    @Autowired
    private MessageUtil messageUtil;
    @Resource
    private GdProperties gdProperties;
    /** 日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(LoginController.class);

    /**
     * 联采中心登录
     * 
     * @author songhui
     * @date 创建时间：2015年8月10日 下午3:27:45
     * @param request
     * @return
     * 
     */
    @RequestMapping("login")
    public String loginDispatcher(HttpServletRequest request, HttpServletResponse response) {
        if (LoginUserUtil.getLoginUserId(request) != null) {
            return "redirect:" + PathUtil.getBasePath(request) + "index";
        }
        String message = login(request, response);
        if (message == null) {
            // 登录成功
            return "redirect:" + PathUtil.getBasePath(request) + "index";
        }
        request.setAttribute(CommonConstant.COMMON_AJAX_ERROR, message);
        return "login";
    }

    /**
     * 登录操作
     * 
     * @author songhui
     * @date 创建时间：2015年8月10日 下午3:28:19
     * @param request
     * @param response
     * @param sysType
     *            系统类型
     * @return
     * 
     */
    public String login(HttpServletRequest request, HttpServletResponse response) {

        try {
            // 校验用户与密码;
            String userCode = StringUtils.trimToNull(request.getParameter("userCode"));
            String userPassword = StringUtils.trimToNull(request.getParameter("userPassword"));
            Properties properties = gdProperties.getProperties();
            String gatewaySign = properties.getProperty("gd.gateway.sign");
            String message = null;
            // 校验界面输入的数据;
            if (StringUtils.isBlank(userCode)) {
                // 用户没有输入;
                message = messageUtil.getMessage("login.noUserCode");
            } else if (StringUtils.isBlank(userPassword)) {
                // 密码没有输入;
                message = messageUtil.getMessage("login.noUserPassword");
            }
            // 依据code取得用户的信息;
            SysRegisterUser regUser = sysLoginService.getLoginUserInfo(userCode);
            // 根据登录用户判断
            if (regUser == null) {
                // 用户不存在;
                message = messageUtil.getMessage("login.user.noExist");
            } else {
                // 登录处理
                regUser.setPassword(userPassword);
                message = sysLoginService.processLogin(regUser);
                regUser.setUserIP(LoginUserUtil.getIpAddr(request));// 记录用户登录IP
            }
            
            // 如果有错误信息，返回错误信息
            if (StringUtils.isNotBlank(message)) {
                return message;
            }
            
			if (StringUtils.isNotEmpty(gatewaySign) && gatewaySign.equals("true")) {
				// 校验验证码
				HttpSession session = request.getSession();
				String code = StringUtils.trimToNull(request.getParameter("verifyCode"));
				String mobile = regUser.getMobile();
				String scode = (String) session.getAttribute(Constant.MOBILE_CHECK_LOGIN + mobile);

				if (StringUtils.isBlank(code)) {
					message = "请输入验证码";
					return message;
				}

				if (StringUtils.isBlank(scode)) {
					message = "验证码有误请重新获取";
					return message;
				}
				
				if (!code.equals(scode)) {
					message = "验证码输入有误";
					return message;
				}

				Map<String, Long> checkmap = (Map<String, Long>) session.getAttribute(Constant.MOBILE_CHECK_TIME_LOGIN);
				if (checkmap != null && checkmap.get(mobile) != null) {
					Long value = (System.currentTimeMillis() - checkmap.get(mobile)) / 1000;
					if (value > 300) {
						message = "验证码已过期；验证码有效期时间为5分钟";
						return message;
					}
				}
			}
		
            // 设定用户session按钮
            setUserSessionButton(regUser.getUserID(), request);
            // 设定用户session菜单;
            setUserSessionMenu(regUser.getUserID(), request);
            // 设定用户的session 按钮;
            // setUserSessionButton(regUser.getUserID(), request);
            // 设定全局的用户ID;
//            jodisTemplate.setex(Constant.SYSTEM_USERID, Constant.SESSION_EXPIRE_TIME, regUser.getUserID());
             request.getSession().setAttribute(Constant.SYSTEM_USERID, regUser.getUserID());
            // 设定全局的用户名称;
//            jodisTemplate.setex(Constant.SYSTEM_USERNAME, Constant.SESSION_EXPIRE_TIME, regUser.getUserName());
             request.getSession().setAttribute(Constant.SYSTEM_USERNAME, regUser.getUserName());
            // 设定全局的用户账号;
//            jodisTemplate.setex(Constant.SYSTEM_USERCODE, Constant.SESSION_EXPIRE_TIME, regUser.getUserCode());
             request.getSession().setAttribute(Constant.SYSTEM_USERCODE, regUser.getUserCode());
            // 设定全局用户
//            jodisTemplate.setex(Constant.SYSTEM_SMGRLOGINUSER, Constant.SESSION_EXPIRE_TIME, JSONObject.toJSONString(regUser));
             request.getSession().setAttribute(Constant.SYSTEM_SMGRLOGINUSER, regUser);

//            System.out.println(jodisTemplate.get(Constant.SYSTEM_USERID));
//            System.out.println(jodisTemplate.get(Constant.SYSTEM_USERNAME));
//            System.out.println(jodisTemplate.get(Constant.SYSTEM_USERCODE));
//            System.out.println(jodisTemplate.get(Constant.SYSTEM_SMGRLOGINUSER));
//            System.out.println("fffffffffff");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.trace("登录异常", e);
            return "登录异常";
        }
    }

    /**
     * 设定session菜单;
     * 
     * @param userID
     *            用户ID;
     * @param request
     *            请求;
     */
    @SuppressWarnings("unchecked")
    private void setUserSessionMenu(String userID, HttpServletRequest request) throws Exception {

        Object[] obj = sysLoginService.getUserAllMenu(userID);
        // 菜单code
        Map<String, String> userMenuMap = (Map<String, String>) obj[1];
        // 一级菜单
        List<BaseMenu> menuList = (List<BaseMenu>) obj[0];
        // 二级菜单
        List<BaseMenu> subMenuList = (List<BaseMenu>) obj[2];
        // 三级菜单
        List<BaseMenu> trdMenuList = (List<BaseMenu>) obj[3];
        // 一级菜单
        request.getSession().setAttribute(Constant.SYSTEM_ALLMENU, menuList);
        // 菜单code
        request.getSession().setAttribute(Constant.SYSTEM_MENUCODE, userMenuMap);
        // 二级菜单
        request.getSession().setAttribute(Constant.SYSTEM_SENCONDMENU, subMenuList);
        // 三级菜单
        request.getSession().setAttribute(Constant.SYSTEM_THIRDMENU, trdMenuList);
    }

    /**
     * 设定session Button;
     * 
     * @param userID
     *            用户id;
     * @param request
     *            请求;
     */
    private void setUserSessionButton(String userID, HttpServletRequest request) {
        Map<String, String> btnMap = null;
        try {
            btnMap = sysLoginService.getUserAllMenuButton(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute(Constant.SYSTEM_BUTTONCODE, btnMap);
    }

    /**
     * 退出登陆
     * 
     * @param request
     * @param response
     * @author tanjun
     * @date 2012-12-03
     * @return
     */
    @RequestMapping("loginOut")
    public ModelAndView loginOut(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        // 设置session失效
        request.getSession().invalidate();
        mv.addObject("logout", "logout");
        mv.setViewName("redirect:/");
        return mv;
    }
    
    /**
     * 发送短信验证信息
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping(value="generateVerifyCode")
    @ResponseBody
	public Map<String, Object> generateVerifyCode(HttpServletRequest request) {
    	Map<String, Object> retmap = new HashMap<String, Object>();
    	String message = null;
		try {
			String userCode = StringUtils.trimToNull(request.getParameter("userCode"));
			SysRegisterUser regUser = sysLoginService.getLoginUserInfo(userCode);
            // 根据登录用户判断
            if (regUser == null) {
                // 用户不存在;
            	message =  messageUtil.getMessage("login.user.noExist");
            	retmap.put("msg", message);
            	return retmap;
            }
			Long sessionTime = System.currentTimeMillis();
			Map<String, Object> map = getParametersMap(request);
			GdSMS sms = new GdSMS();
			String mobile = regUser.getMobile();
			Map<String, Long> checkmap = (Map<String, Long>)request.getSession().getAttribute(Constant.MOBILE_CHECK_TIME_LOGIN);
			if (checkmap != null && checkmap.get(mobile) != null) {
				// 判断两次时间是否大于1分钟
				Long value = (System.currentTimeMillis() - checkmap.get(mobile)) / 1000;
				if (value < 60) {
					message = "请60s后发送验证码！";
					retmap.put("msg", message);
	            	return retmap;
				}
			}
			String channel = Constant.Alidayu.DEFAULTNO;
			String randi = getSixCode();
			String smsmsg = Constant.SMSMSG.replace("{code}", randi);
			System.out.println("----------------------------->" + randi);
			// 阿里大鱼短信通道
			if (Constant.Alidayu.REDISTYPE.equals(channel)) {
				smsmsg = CommonUtil.alidayuUtil(Constant.Alidayu.MESSAGETYPE6, randi);
			}
			boolean b = sms.SendSms(channel, smsmsg, mobile);
			if (b) {
				// 记录发送成功的手机号
				Map<String, Long> remaps = new HashMap<String, Long>();
				remaps.put(mobile, sessionTime);
				// 记录验证码
				request.getSession().setAttribute(Constant.MOBILE_CHECK_LOGIN + mobile, randi.toString());
				request.getSession().setAttribute(Constant.MOBILE_CHECK_LOGIN + "vaild", sessionTime);
				request.getSession().setAttribute(Constant.MOBILE_CHECK_TIME_LOGIN, remaps);
				message = "验证码已发送";
				retmap.put("msg", message);
            	return retmap;
			} else {
				message = "发送短信验证码失败,请重试!";
				retmap.put("msg", message);
            	return retmap;
			}
		} catch (Exception e) {
			logger.trace(e.getMessage());
			e.printStackTrace();
		}
		return retmap;
	}
    
    /**
	 * 确认验证码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="verifyCode")
	@ResponseBody
	public Map<String,Object> verifyCode(HttpServletRequest request){
		Map<String,Object> retmap = new HashMap<String,Object>();
		try {
			HttpSession session = request.getSession();
			String code = request.getParameter("code");
			String mobile = request.getParameter("mobile");
			String scode = (String) session.getAttribute(Constant.MOBILE_CHECK_LOGIN + mobile);
			
			if(StringUtils.isBlank(code)){
				retmap.put("status", "4");
				retmap.put("msg", "请输入验证码");
				return retmap;
			}
			
			if(StringUtils.isBlank(scode)){
				retmap.put("status", "5");
				retmap.put("msg", "验证码有误请重新获取");
				return retmap;
			}
			
			Map<String, Long> checkmap = (Map<String, Long>) session.getAttribute(Constant.MOBILE_CHECK_TIME_LOGIN);
			if (checkmap != null && checkmap.get(mobile) != null) {
				Long value = (System.currentTimeMillis() - checkmap.get(mobile)) / 1000;
				if (value > 300) {
					retmap.put("status", "2");
					retmap.put("msg", "验证码已过期；验证码有效期时间为5分钟");
					return retmap;
				}
			}
			
			if(code.equals(scode)){
				retmap.put("status", "1");
				retmap.put("msg", "验证通过");
			}else{
				retmap.put("status", "3");
				retmap.put("msg", "验证码输入有误");
			}
		} catch (Exception e) {
			logger.trace(e.getMessage());
			e.printStackTrace();
			
		}
		return retmap;
	}
	
	
	/**
	 * 产生随机的六位数
	 * @return
	 */
	public String getSixCode(){
		Random rad=new Random();
		
		String result  = rad.nextInt(1000000) +"";
		
		if(result.length()!=6){
			return getSixCode();
		}
		return result;
	}	

}
