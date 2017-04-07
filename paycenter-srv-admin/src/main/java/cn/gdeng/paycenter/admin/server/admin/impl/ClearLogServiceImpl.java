package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.ClearLogService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.BillClearLogEntity;

@Service
public class ClearLogServiceImpl implements ClearLogService{
	
	@Autowired
	public BaseDao<BillClearLogEntity> clearLogDao;

	@Override
	public List<BillClearLogEntity> queryByParams(Map<String, Object> params) throws BizException {
		return this.clearLogDao.queryForList("ClearLog.queryByParams", params,BillClearLogEntity.class);
	}

	@Override
	public int queryCountByParams(Map<String, Object> params) throws BizException {
		return this.clearLogDao.queryForObject("ClearLog.queryCountByParams", params, Integer.class);
	}

}
