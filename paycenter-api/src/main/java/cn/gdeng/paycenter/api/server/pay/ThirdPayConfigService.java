package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;

/**
 * 第三方支付配置服务
 *
 */
public interface ThirdPayConfigService {

	/**
	 * 查询密钥
	 * @param map
	 * @return
	 */
	public List<ThirdPayConfigEntity> queryPayConfigList(Map<String,Object> map);
	
	/**
	 * 查询密钥，找不到抛出异常 C_20004 ,C_20004
	 * @param orderNo
	 * @param subType
	 * @return
	 */
	public ThirdPayConfigEntity queryByOrderSub(String payCenterNumber,String subType) throws BizException;
	
	/**
	 * 查询密钥，找不到抛出异常 C_20004 ,C_20004
	 * @param orderNo
	 * @param subType
	 * @return
	 */
	public ThirdPayConfigEntity queryByTypeSub(String appkey,String payType,String subType) throws BizException; 
	
	/**
	 * 查询所有
	 * @return
	 * @throws BizException
	 */
	public List<ThirdPayConfigEntity> queryAll() throws BizException; 

}
