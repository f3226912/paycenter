package cn.gdeng.paycenter.api.server.pay;

import java.util.List;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;

/**
 * 接入系统配置服务
 *
 */
public interface AccessSysConfigService {
	
	/**
	 * 根据appKey查找接入系统实体
	 * @param appKey
	 * @return
	 */
	public AccessSysConfigEntity queryByAppKey(String appKey) throws BizException;
	
	/**
	 * 查询所有的接入系统
	 * @return
	 */
	public List<AccessSysConfigEntity> queryAll() throws BizException;

}
