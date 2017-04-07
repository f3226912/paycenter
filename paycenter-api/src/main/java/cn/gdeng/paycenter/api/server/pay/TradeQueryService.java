package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.account.TradeQueryRequestDto;
import cn.gdeng.paycenter.dto.account.TradeQueryResponseDto;

public interface TradeQueryService {

	/**
	 * 交易查询接口 payCenterNumber,thirdPayNumber两个不能同时为空 appKey必传
	 * @param dto
	 * @return 如果交易不存在，返因NULL,其它的拖异常
	 * @throws BizException
	 */
	TradeQueryResponseDto queryTrade(TradeQueryRequestDto dto) throws BizException;
}
