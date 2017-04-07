package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.pay.PayJumpDto;

public interface PayService {


	/**
	 * 支付预处理，确认支付时调用
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	PayJumpDto prePay(PayGateWayDto dto) throws BizException;
}
