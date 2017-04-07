package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;

public interface PayLogRecordService {

	/**
	 * 添加日志
	 * @param entity
	 */
	public Long addLog(PayLogRecordEntity entity);
	
	public Integer updateLog(PayLogRecordEntity entity);
}
