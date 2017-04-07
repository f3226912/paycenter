package cn.gdeng.paycenter.util.web.api;

import java.util.Properties;

/**
 * 消息公用类
 */
public class MessageUtil {

    private Properties properties;

    /**
     * 获取消息
     * 
     * @param key
     * @return Properties
     */
    public String getMessage(String key) {
        try {
            String msg = (String) getProperties().get(key);
            return msg;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取配置文件数据
     * 
     * @param key,数组
     * @return Properties
     */
    public String getMessage(String key, String... param) {
        try {
            String msg = (String) getProperties().get(key);
            for (int i = 0; i < param.length; i++) {
                msg = msg.replace("{" + i + "}", param[i]);
            }
            return msg;
        } catch (Exception e) {
            return null;
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
