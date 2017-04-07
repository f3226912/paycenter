package cn.gdeng.paycenter.api.server.pay;

import java.util.Map;

import cn.gdeng.paycenter.api.BizException;

/**
 * 阿里对账服务
 * @author sss
 *
 */
public interface AlipayAccountService<T> {

	/**
	 * 只支持月账单，日账单两种，格式分别为 yyyy-MM-dd,yyyy-MM
	 * @param billDate
	 * @return
	 * @throws BizException
	 */
	T getAccountRequest(T t) throws BizException;
	
	/**
	 * 对账务返回结果进行验签
	 * @param para
	 * @return
	 */
	boolean signResult(String appKey,Map<String,String> para) throws BizException;
}
