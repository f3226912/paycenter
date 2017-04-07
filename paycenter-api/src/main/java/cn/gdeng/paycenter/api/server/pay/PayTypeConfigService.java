package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;

/**
 * 支付类型配置服务
 *
 */
public interface PayTypeConfigService {

	/**
	 * 接入系统查出支付类型列表
	 * @param appKey
	 * @return
	 */
	public List<PayTypeDto> qureyByAppKey(String appKey)  throws BizException;
	
	/**
	 * 查询appKey对应的支付方式
	 * @return
	 * @throws BizException
	 */
	public Map<String,List<PayTypeDto>> queryAll() throws BizException;
	
}
