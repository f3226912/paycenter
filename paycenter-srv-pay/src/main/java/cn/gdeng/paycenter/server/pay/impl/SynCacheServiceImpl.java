package cn.gdeng.paycenter.server.pay.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.PayTypeConfigService;
import cn.gdeng.paycenter.api.server.pay.SynCacheService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.CacheKeyContant;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.server.jodis.JodisTemplate;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.SerializeUtil;

@Service
public class SynCacheServiceImpl implements SynCacheService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private AccessSysConfigService accessSysConfigService;
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;
	
	@Resource
	private PayTypeConfigService payTypeConfigService;
	
	@Resource
	private JodisTemplate jodisTemplate;
	
	@Resource
	private GdProperties gdProperties;
	
	private static String env="dev";
	
	public static String EMPTY = "";
	
	@PostConstruct
	private void init(){
		env = gdProperties.getEnv();
	}
	
	private String getAccessKey(){
		return env+CacheKeyContant.ACCESS_SYS_CONFIG_MAP_KEY;
	}
	
	private String getThirdKey(){
		return env+CacheKeyContant.THIRD_PAY_CONFIG_MAP_KEY;
	}
	
	private String getPayTypeKey(){
		return env+CacheKeyContant.PAY_TYPE_CONFIG_MAP_KEY;
	}

	public void synCache() throws BizException {
		try{
			logger.info("*********************开始初始化缓存************************");
			synAccessSysConfig();
			synThirdPayConfig();
			synPayTypeConfig();
			logger.info("*********************初始化缓存成功************************");
		}catch(Exception e){
			logger.error("同步缓存错误:" + e.getMessage(),e);
			throw new BizException(MsgCons.C_20008, MsgCons.M_20008);
		}
	}

	public Object readCache(String key) {
		String envKey = env+key;
		Object obj = SerializeUtil.unserialize(jodisTemplate.get(envKey.getBytes()));
		return obj;
	}

	


	@Override
	public void synAccessSysConfig() throws BizException {
		//同步接入系统入缓存
		List<AccessSysConfigEntity> listAsce = accessSysConfigService.queryAll();
		Map<String,AccessSysConfigEntity> mapAsce = new HashMap<String,AccessSysConfigEntity>();
		for(AccessSysConfigEntity asce : listAsce){
			mapAsce.put(asce.getAppKey(), asce);
		}
		jodisTemplate.set(getAccessKey().getBytes(), SerializeUtil.serialize(mapAsce));
		
	}



	@Override
	public void synThirdPayConfig() throws BizException {
		//同步第三方系统配置入缓存
		List<ThirdPayConfigEntity> listTpce = thirdPayConfigService.queryAll();
		Map<String,ThirdPayConfigEntity> mapTpce = new HashMap<String,ThirdPayConfigEntity>();
		for(ThirdPayConfigEntity tpce : listTpce){
			String subType = handleSubType(tpce.getSubPayType());
			mapTpce.put(tpce.getAppKey() + "_" + tpce.getPayType()
								+ "_" + subType, tpce);
		}
		jodisTemplate.set(getThirdKey().getBytes(), SerializeUtil.serialize(mapTpce));
		
	}


	public static String handleSubType(String subType){
		if(StringUtils.isEmpty(subType)){
			subType = EMPTY;
		}
		return subType;
	}

	@Override
	public void synPayTypeConfig() throws BizException {
		//同步支付方式入缓存
		Map<String,List<PayTypeDto>> mapPtd = payTypeConfigService.queryAll();
		jodisTemplate.set(getPayTypeKey().getBytes(), SerializeUtil.serialize(mapPtd));
	}

	@Override
	public String getEnv() {
		return env;
	}
}
