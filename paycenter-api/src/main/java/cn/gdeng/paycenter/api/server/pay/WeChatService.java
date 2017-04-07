package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.wechat.WeChatPreResultDto;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundQuery;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundQueryResponse;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundResponse;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public interface WeChatService {
	/**
	 * 支付预处理，统一下单接口、确认支付时调用
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	WeChatPreResultDto prePay(PayGateWayDto dto) throws BizException;
	
	/**
	 * 验签
	 * @param params
	 * @return
	 * @throws BizException
	 */
	boolean payVerify(Map<String, String> params,List<PayTradeEntity> pay) throws BizException;
	
	PayTradeEntity buildPayTrade4Notify(Map<String,String> params) throws BizException;
	
	/**
	 * 为SDK支付生成SIGN
	 * @param params
	 * @param appKey
	 * @return
	 * @throws BizException
	 */
	public String buildPaySign(Map<String,String> params,String appKey) throws BizException;
	
	/**
	 * 退款查询
	 * @param params
	 * @return
	 */
	public WeChatRefundQueryResponse refundQuery(String appKey,WeChatRefundQuery params)throws BizException;
}
