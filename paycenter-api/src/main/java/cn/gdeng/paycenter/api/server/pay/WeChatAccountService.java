package cn.gdeng.paycenter.api.server.pay;

import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;

/**
 * 微信对账服务
 *
 */
public interface WeChatAccountService<T> {

	/**
	 * 只支持日账单，格式分别为 yyyyMMdd
	 * @param billDate
	 * @return
	 * @throws BizException
	 */
	T getAccountRequest(AccountRquestDto dto) throws BizException;
	
}
