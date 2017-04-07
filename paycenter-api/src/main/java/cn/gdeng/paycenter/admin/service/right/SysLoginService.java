package cn.gdeng.paycenter.admin.service.right;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.entity.pay.SysMenu;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;

/**
 * @Description 系统登录接口
 * @Project gd-auth-intf
 * @ClassName SysLoginService.java
 * @Author lidong(dli@cnagri-products.com)
 * @CreationDate 2015年10月17日 下午2:39:59
 * @Version V1.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory Who When What -------- ------------------------- ----------------------------------- lidong 2015年10月17日 下午2:39:59 初始创建
 */
public interface SysLoginService {

    /**
     * 取得用户所有菜单;
     * 
     * @param userID
     * @return List<菜单>;菜单map，二级菜单List
     */
    public Object[] getUserAllMenu(String userID);

    /**
     * 取得所有的菜单;
     * 
     * @return List<SysMenu>;
     */
    public List<SysMenu> getAllMenu();

    /**
     * 取得用户所有菜单按钮;
     * 
     * @param userID
     * @return map<btnCode,'1'>;
     */
    public Map<String, String> getUserAllMenuButton(String userID);

    /**
     * 用户登录验证,并取得用户信息。
     * 
     * @param userCode
     *            用户编号;
     * @return SysRegisterUser 对象;
     * @throws Exception
     */
    public SysRegisterUser getLoginUserInfo(String userCode) throws Exception;

    /**
     * 更新用户登录后信息;
     * 
     * @param user
     * @throws Exception
     */
    public void updateLoginUser(SysRegisterUser user) throws Exception;

    /**
     * 后台用户登录
     * 
     * @author songhui
     * @date 创建时间：2015年8月4日 下午4:45:20
     * @param regUser
     * @return
     * @throws Exception
     *
     */
    public String processLogin(SysRegisterUser regUser) throws Exception;

    /**
     * @Description 前台用户登录
     * @param regUser
     * @return
     * @throws Exception
     * @CreationDate 2016年3月5日 下午4:15:23
     * @Author lidong(dli@gdeng.cn)
     */
    public String processLogin2(SysRegisterUser regUser) throws Exception;

}
