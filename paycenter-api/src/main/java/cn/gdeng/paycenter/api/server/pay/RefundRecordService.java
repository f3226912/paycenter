package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.refund.RefundRecordDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeDto;
import cn.gdeng.paycenter.entity.pay.RefundRecordEntity;

public interface RefundRecordService {
	
	String getRefundNo()throws BizException;

	List<RefundRecordEntity> getList(Map<String,Object> params) throws BizException;
	
	RefundRecordDto getRefundRecord(RefundTradeDto dto)  throws BizException;

	void update(RefundRecordEntity dto) throws BizException;

	void add(RefundTradeDto dto) throws BizException;
}
