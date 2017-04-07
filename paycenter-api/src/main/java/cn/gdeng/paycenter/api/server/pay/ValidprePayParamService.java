package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public interface ValidprePayParamService {
	
	public void validPrePayParam(PayTradeEntity dto) throws BizException;
	
	/**
	 * 校验订单是否待付款
	 * @throws BizException
	 */
	public void validReadPay(PayTradeEntity payTrade) throws BizException;

}
