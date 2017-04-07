package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public interface PayCallBackVerifyService {
	
	boolean payVerify(List<PayTradeEntity> pay,Map<String, String> params)throws BizException;

}
