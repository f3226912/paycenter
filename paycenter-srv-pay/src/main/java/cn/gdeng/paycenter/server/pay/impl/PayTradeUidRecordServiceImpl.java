package cn.gdeng.paycenter.server.pay.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayTradeUidRecordService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PayTradeUidRecordEntity;
import cn.gdeng.paycenter.util.admin.web.DateUtil;

@Service
public class PayTradeUidRecordServiceImpl implements PayTradeUidRecordService{
	
	@Resource
	private BaseDao<PayTradeUidRecordEntity> baseDao;

	@Override
	public PayTradeUidRecordEntity queryByPayUID(String payUid) throws BizException {
		
		Map<String,String> param = new HashMap<>();
		param.put("payUid", payUid);
		List<PayTradeUidRecordEntity> list = baseDao.queryForList("PayTradeUidRecord.queryByCondition", param,PayTradeUidRecordEntity.class);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void addPayTradeUidRecordEntity(PayTradeUidRecordEntity entity,List<String> orders) throws BizException {
		PayTradeUidRecordEntity old = queryByPayUID(entity.getPayUid());
		if(old == null){
			for(String orderNo : orders){
				entity.setCreateTime(DateUtil.getNow());
				entity.setUpdateTime(DateUtil.getNow());
				entity.setOrderNo(orderNo);
				entity.setCreateUserId("sys");
				entity.setUpdateUserId("sys");
				baseDao.persist(entity);
			}
		}
	}

	@Override
	public void deletePayTradeUidRecord(String payCenterNumber) throws BizException {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("payCenterNumber", payCenterNumber);
		baseDao.execute("PayTradeUidRecord.deletePayTradeUidRecord", paramMap);
	}
}
