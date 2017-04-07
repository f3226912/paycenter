package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.pay.PayJumpDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public interface AlipayService {
	/**
	 * 支付预处理，确认支付时调用
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	PayJumpDto prePay(PayGateWayDto dto) throws BizException;
	
	/**
	 * 支付验签
	 * @param params
	 * @return
	 * @throws BizException
	 */
	boolean payVerify(Map<String, String> params,List<PayTradeEntity> pay) throws BizException;
	
		
	PayTradeEntity buildPayTrade4Return(Map<String,String> params) throws BizException;	
	
	PayTradeEntity buildPayTrade4Notify(Map<String,String> params) throws BizException;
	
}
