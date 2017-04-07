package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;

public interface SynCacheService {
	
	public void synCache() throws BizException;
	
	public void synAccessSysConfig() throws BizException;
	
	public void synThirdPayConfig() throws BizException;
	
	public void synPayTypeConfig() throws BizException;
	
	public Object readCache(String key) throws BizException;
	
	public String getEnv();

}
