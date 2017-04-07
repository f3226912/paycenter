package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.refund.RefundTradeDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeResult;

/**
 * 交易退款服务
 * @author sss
 *
 * since:2016年12月9日
 * version 1.0.0
 */
public interface TradeRefundService {

	RefundTradeResult refund(RefundTradeDto dto) throws BizException;
}
