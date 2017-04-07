package cn.gdeng.paycenter.server.pay.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;

@Service
public class PayLogRecordServiceImpl implements PayLogRecordService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Resource
	private BaseDao<PayLogRecordEntity> baseDao;

	@Override
	public Long addLog(PayLogRecordEntity entity) {
		//日志异常，不处理
		try {
			//截取send,receive
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			Long id = (Long) baseDao.persist(entity);
			return id;
		} catch (Exception e) {
			logger.error("记录支付日志失败",e);
			return 0L;
		}
		
	}

	@Override
	public Integer updateLog(PayLogRecordEntity entity) {
		//日志异常，不处理
		try {
			if(entity.getId() != null && entity.getId()>0){
				entity.setUpdateTime(new Date());
				baseDao.execute("PayLogRecord.updateLog", entity);
				return 1;
			}
		} catch (Exception e) {
			logger.error("记录支付日志失败",e);
			
		}
		return 0;
				
	}

}
