package cn.gdeng.paycenter.gateway; 


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gdeng.paycenter.api.server.pay.SynCacheService;
import cn.gdeng.paycenter.constant.CacheKeyContant;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;

/**
 * 应用appkey/secret管理
 *
 */
public class AppSecretManager {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private SynCacheService synCacheService;
	

    private static Map<String, String> appKeySecretMap = new HashMap<String, String>();
    
    @SuppressWarnings("unchecked")
	public void init(){
    	try {
    		synCacheService.synCache();
			Map<String, AccessSysConfigEntity> map = (Map<String, AccessSysConfigEntity>)synCacheService.readCache(CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY);
			if(map == null || map.size() == 0){
				synCacheService.synCache();
				map = (Map<String, AccessSysConfigEntity>)synCacheService.readCache(CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY);
			}
			for (Map.Entry<String, AccessSysConfigEntity> entry : map.entrySet()) {
				appKeySecretMap.put(entry.getKey(), entry.getValue().getMd5Key());
			}
    	} catch (Exception e) {
    		logger.error("初始化接入系统错误:" + e.getMessage(),e);
		}
    } 


    public static String getSecret(String appKey) {
        return appKeySecretMap.get(appKey);
    }


    public static boolean isValidAppKey(String appKey) {
        return getSecret(appKey) != null;
    }
}

