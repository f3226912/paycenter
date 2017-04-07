package cn.gdeng.paycenter.server.pay.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.PayTypeConfigService;
import cn.gdeng.paycenter.api.server.pay.SynCacheService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.CacheKeyContant;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;

/**
 * 第三方支付配置服务
 *
 */
@Service
public class ThirdPayConfigServiceImpl implements ThirdPayConfigService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BaseDao<?> baseDao;
	
	@Resource
	private PayTypeConfigService payTypeConfigService;
	
	@Resource
	private PayTradeService payTradeService;
	
	@Resource
	private SynCacheService synCacheService;


	@Override
	public List<ThirdPayConfigEntity> queryPayConfigList(Map<String,Object> map) {

		List<ThirdPayConfigEntity> list = baseDao.queryForList("ThirdPayConfig.queryByPayType", map,ThirdPayConfigEntity.class);
		return list;
	}
	
	@Override
	public ThirdPayConfigEntity queryByOrderSub(String payCenterNumber, String subType) throws BizException{
		PayTradeEntity pay = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber).get(0);

		return queryByTypeSub(pay.getAppKey(),pay.getPayType(),subType);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public ThirdPayConfigEntity queryByTypeSub(String appKey,String payType, String subType)throws BizException {
		Map<String,Object> param = new HashMap<>();
		param.put("appKey",appKey);
		param.put("payType",payType);
		param.put("status", 1);//有效
		param.put("subPayType",subType);
		Map<String,ThirdPayConfigEntity> map = null;
		ThirdPayConfigEntity tpcf = null;
		try{
			map = (Map<String,ThirdPayConfigEntity>)synCacheService.readCache(CacheKeyContant.THIRD_PAY_CONFIG_MAP_KEY);
			if(map == null){
				synCacheService.synThirdPayConfig();
				map = (Map<String,ThirdPayConfigEntity>)synCacheService.readCache(CacheKeyContant.THIRD_PAY_CONFIG_MAP_KEY);
			}
			if(map != null && map.size() > 0){
				tpcf = map.get(appKey + "_" + payType + "_" + SynCacheServiceImpl.handleSubType(subType));
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		if(tpcf != null){
			return tpcf;
		}
		return _queryKeySub(param);
		
	}
	
	private ThirdPayConfigEntity _queryKeySub(Map<String,Object> param) throws BizException {
		List<ThirdPayConfigEntity> clist = null;
		try {
			clist = queryPayConfigList(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new BizException(MsgCons.C_60000, "查询失败,"+e.getMessage());
		}
		if(null == clist || clist.size() == 0){
			throw new BizException(MsgCons.C_20004, MsgCons.M_20004);
		}else if(clist.size() >1){
			throw new BizException(MsgCons.C_20005, MsgCons.M_20005);
		}
		ThirdPayConfigEntity config = clist.get(0);
		return config;
	}

	public List<ThirdPayConfigEntity> queryAll() throws BizException {
		try{
			List<ThirdPayConfigEntity> list = baseDao.queryForList("ThirdPayConfig.queryAll", new HashMap<String,String>(),ThirdPayConfigEntity.class);
			return list;
		}catch(Exception e){
			logger.info("查询第三方支付配置错误：" + e.getMessage());
			throw new BizException(MsgCons.C_20000, MsgCons.M_20000);
		}
	}

}
