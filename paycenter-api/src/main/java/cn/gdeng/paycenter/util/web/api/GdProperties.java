package cn.gdeng.paycenter.util.web.api;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 参数属性;
 * 
 */
public class GdProperties {

	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/** demo的地址 */
	public String getDemoUrl() {
		return properties.getProperty("gd.demo.url");
	}

	/** sysLoginService的地址 */
	public String getSysLoginServiceUrl() {
		return properties.getProperty("gd.sysLoginService.url");
	}

	/** sysMenuButtonService的地址 */
	public String getSysMenuButtonServiceUrl() {
		return properties.getProperty("gd.sysMenuButtonService.url");
	}

	/** sysMenuService的地址 */
	public String getSysMenuServiceUrl() {
		return properties.getProperty("gd.sysMenuService.url");
	}

	/** sysRegisterUserService的地址 */
	public String getSysRegisterUserServiceUrl() {
		return properties.getProperty("gd.sysRegisterUserService.url");
	}

	/** sysRoleManagerService的地址 */
	public String getSysRoleManagerServiceUrl() {
		return properties.getProperty("gd.sysRoleManagerService.url");
	}

	/** sysRoleService的地址 */
	public String getSysRoleServiceUrl() {
		return properties.getProperty("gd.sysRoleService.url");
	}

	/** sysUserRoleService的地址 */
	public String getSysUserRoleServiceUrl() {
		return properties.getProperty("gd.sysUserRoleService.url");
	}

	/**
	 * 获取超级管理员ID
	 * 
	 * @return
	 * 
	 */
	public String getSysSupperAdminId() {
		return properties.getProperty("system.admin.id");
	}

	public String getSysSupperAdminTip() {
		return properties.getProperty("admin.lock.error");
	}

	/**
	 * 获取交易预报表服务
	 * 
	 * @return
	 */
	public String getProBszbankUrl() {
		return properties.getProperty("gd.proBszbankService.url");
	}

	public String getReportsServiceUrl() {
		return properties.getProperty("gd.reportsService.url");
	}

	public String getSysrolereportsServiceUrl() {
		return properties.getProperty("gd.sysrolereportsService.url");
	}

	public String getBoardServiceUrl() {
		return properties.getProperty("gd.boardService.url");
	}

	public String getDatasourceServiceUrl() {
		return properties.getProperty("gd.datasourceService.url");
	}

	public String getDatasourceBaiduServiceUrl() {
		return properties.getProperty("gd.datasourceBaiduService.url");
	}

	public String getOrderBillServiceUrl() {
		return properties.getProperty("gd.orderBillService.url");
	}
    public String getSysroleboardServiceUrl(){
        return properties.getProperty("gd.sysroleboardService.url");
    }
    public String getSysmessageServiceUrl(){
        return properties.getProperty("gd.sysmessageService.url");
    }
    public String getSysmessageuserServiceUrl(){
        return properties.getProperty("gd.sysmessageuserService.url");
    }

	public String getProBaiduServiceUrl() {
		 return properties.getProperty("gd.proBaiduService.url");
	}
    
    public String getProOperateServiceUrl(){
        return properties.getProperty("gd.proOperateService.url");
    }
    
    public String getMarketServiceUrl(){
    	return properties.getProperty("gd.marketService.url");
    }
    
    public String rsaSignKeyPath(){
        return properties.getProperty("maven.rsa.sign.key.path");
    }
    
    public boolean signOnOff(){
    	String onOff = properties.getProperty("maven.rsa.sign.on.off");
    	if("true".equals(onOff)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean isSign(){
    	String sign = properties.getProperty("gateway.sign");
		if(StringUtils.equals(sign, "false")){
			return false;
		} else {
			return true;
		}
    }
    
    public String getEnv(){
    	 return properties.getProperty("maven.env");
    }
}
