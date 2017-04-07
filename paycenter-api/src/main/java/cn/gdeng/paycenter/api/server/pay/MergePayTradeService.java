package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;

/**
 * 
 * @author sss
 *
 * since:2016年12月7日
 * version 1.0.0
 */
public interface MergePayTradeService {

	String createPayTrade(MergePayTradeRequestDto dto) throws BizException;
}
