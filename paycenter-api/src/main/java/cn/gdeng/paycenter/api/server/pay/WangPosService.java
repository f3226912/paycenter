package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.dto.pos.WangPosPayNotifyDto;

public interface WangPosService {

	String createPayTrade(MergePayTradeRequestDto dto) throws BizException;
	
	/**
	 * 旺POS前端同步通知
	 * @param dto
	 * @throws BizException
	 */
	void payNotify(WangPosPayNotifyDto dto) throws BizException;
}
