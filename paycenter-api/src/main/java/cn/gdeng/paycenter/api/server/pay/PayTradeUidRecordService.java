package cn.gdeng.paycenter.api.server.pay;

import java.util.List;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.PayTradeUidRecordEntity;

public interface PayTradeUidRecordService {

	PayTradeUidRecordEntity queryByPayUID(String payUid) throws BizException;
	
	void addPayTradeUidRecordEntity(PayTradeUidRecordEntity entity,List<String> orders)throws BizException;

	
	void deletePayTradeUidRecord(String payCenterNumber) throws BizException;
}
