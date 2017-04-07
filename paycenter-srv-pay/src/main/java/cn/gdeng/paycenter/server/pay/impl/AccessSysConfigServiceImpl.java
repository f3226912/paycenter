package cn.gdeng.paycenter.server.pay.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.SynCacheService;
import cn.gdeng.paycenter.constant.CacheKeyContant;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;

/**
 * 接入系统配置服务
 *
 */
@Service
public class AccessSysConfigServiceImpl implements AccessSysConfigService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private BaseDao<?> baseDao;
	
	@Resource
	private SynCacheService synCacheService;


	/**
	 * 根据appKey查找接入系统实体
	 * 
	 * @param appKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AccessSysConfigEntity queryByAppKey(String appKey) throws BizException {
	
		Map<String, AccessSysConfigEntity> map = (Map<String, AccessSysConfigEntity>)synCacheService.readCache(CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY);
		if(null == map){
			synCacheService.synAccessSysConfig();
			map =  (Map<String, AccessSysConfigEntity>)synCacheService.readCache(CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY);
		}
		if(null == map){
			throw new BizException(MsgCons.C_20029, MsgCons.M_20029);
		}
		AccessSysConfigEntity accessSysConfigEntity = map.get(appKey);
		Assert.notNull(accessSysConfigEntity,appKey+"-->在缓存[接入密钥]中不存在,请刷新缓存:http://ip:post/test/syncache");

		return accessSysConfigEntity;
	}

	/**
	 * 查询所有的接入系统
	 * 
	 * @return
	 */
	public List<AccessSysConfigEntity> queryAll() throws BizException {
		try{
			List<AccessSysConfigEntity> list = baseDao.queryForList("AccessSysConfig.queryAll",
					new HashMap<String, String>(), AccessSysConfigEntity.class);
			return list;
		}catch(Exception e){
			logger.info("查询接入系统错误：" + e.getMessage(),e);
			throw new BizException(MsgCons.C_20000, MsgCons.M_20000);
		}		
	}

}
