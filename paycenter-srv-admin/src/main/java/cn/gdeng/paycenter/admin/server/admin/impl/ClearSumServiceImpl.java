package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.ClearSumService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.BillClearSumEntity;

@Service
public class ClearSumServiceImpl implements ClearSumService{
	
	@Autowired
	public BaseDao<BillClearSumEntity> clearSumDao;

	@Override
	public List<BillClearSumEntity> queryByParams(Map<String, Object> params) throws BizException {
		List<BillClearSumEntity> list = this.clearSumDao.queryForList("ClearSum.queryByParams", params,BillClearSumEntity.class);
		return list;
	}

	@Override
	public int queryCountByParams(Map<String, Object> params) throws BizException {
		return this.clearSumDao.queryForObject("ClearSum.queryCountByParams", params, Integer.class);
	}

	@Override
	public BillClearSumEntity getClearSumById(long id) {
		BillClearSumEntity	billClearSumEntity = new BillClearSumEntity();
		billClearSumEntity.setId(id);
		return this.clearSumDao.find(billClearSumEntity);
	}

}
