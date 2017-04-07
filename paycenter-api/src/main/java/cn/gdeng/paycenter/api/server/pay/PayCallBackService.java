package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayNotifyResponse;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public interface PayCallBackService {
	
	PayNotifyResponse payReturnCallBack(List<PayTradeEntity> payList,Map<String,String> params,String payType,String payCenterNumber) throws BizException;
	
	PayNotifyResponse payNotify(Map<String,String> params,String payType,String orderNo) throws BizException;
	
	PayNotifyResponse joinPayNotifyResponse(PayNotifyResponse res,String orderNo);

}
