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
import cn.gdeng.paycenter.api.server.pay.PayTypeConfigService;
import cn.gdeng.paycenter.api.server.pay.SynCacheService;
import cn.gdeng.paycenter.constant.CacheKeyContant;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;

/**
 * 支付类型配置服务
 *
 */
@Service
public class PayTypeConfigServiceImpl implements PayTypeConfigService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private BaseDao<?> baseDao;
	
	@Resource
	private AccessSysConfigService accessSysConfigService;
	
	@Resource
	private SynCacheService synCacheService;


	/**
	 * 接入系统id查出支付类型列表
	 * 
	 * @param appKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PayTypeDto> qureyByAppKey(String appKey) throws BizException {		

		Map<String, List<PayTypeDto>> map = (Map<String, List<PayTypeDto>>)synCacheService.readCache(CacheKeyContant.PAY_TYPE_CONFIG_MAP_KEY);
		if(null == map){
			synCacheService.synPayTypeConfig();
			map = (Map<String, List<PayTypeDto>>)synCacheService.readCache(CacheKeyContant.PAY_TYPE_CONFIG_MAP_KEY);
		}
		if(null == map){
			throw new BizException(MsgCons.C_20029, MsgCons.M_20029);
		}
		List<PayTypeDto> list = map.get(appKey);		
		Assert.notNull(list,appKey+"-->在缓存[支付方式]中不存在,请刷新缓存:http://ip:post/test/syncache");

		return list;
	}
	
	private List<PayTypeDto> _qureyByAppKey(String appKey) throws BizException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("appKey", appKey);
		try{			
			List<PayTypeDto> list = baseDao.queryForList("PayTypeConfig.qureyByAppKey", param,
					PayTypeDto.class);		
			return list;
		}catch(Exception e){
			logger.error("查询接入系统id查出支付类型列表出错:" + e.getMessage(),e);
			throw new BizException(MsgCons.C_20000, MsgCons.M_20000);
		}
	}

	/**
	 * 查询appKey对应的支付方式
	 * @return
	 * @throws BizException
	 */
	public Map<String, List<PayTypeDto>> queryAll() throws BizException {
		Map<String,List<PayTypeDto>> map = new HashMap<String,List<PayTypeDto>>();
		try{
			List<AccessSysConfigEntity> list = accessSysConfigService.queryAll();		
			for(AccessSysConfigEntity asce : list){
				List<PayTypeDto> listPtd = _qureyByAppKey(asce.getAppKey());
				map.put(asce.getAppKey(), listPtd);
			}		
			return map;
		}catch(Exception e){
			logger.error("查询appKey对应的支付方式出错:" + e.getMessage(),e);
			throw new BizException(MsgCons.C_20000, MsgCons.M_20000);
		}
	}
	
	
	

}
