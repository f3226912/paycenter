package cn.gdeng.paycenter.util.web.api;

import java.util.Properties;

/**
 * 类说明 公共属性文件
 **/
public class CommonPropertiesUtil {

private Properties properties;
	
	public String getValue(String key){
	    return properties.getProperty(key);
	}
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * 获取信息
	 * @param key
	 * @param params
	 * @return
	 *
	 */
	public String getMessageByKey(String key,String ...params){
		try{
			String message = properties.getProperty(key);
			if( null != params){
				int paramsLen = params.length;
				for(int i = 0 ; i<paramsLen ; i++){
					message = message.replace("{"+i+"}", params[i]);
				}
			}
			return message;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取超级管理员ID
	 * @return
	 *
	 */
	public String getSysSupperAdminId(){
		
		return properties.getProperty("system.admin.id");
	}
	
}
