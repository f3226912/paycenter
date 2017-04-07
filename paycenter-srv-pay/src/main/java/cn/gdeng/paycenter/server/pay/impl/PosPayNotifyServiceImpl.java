package cn.gdeng.paycenter.server.pay.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PosPayNotifyService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.entity.pay.PosPayNotifyEntity;

@Service
public class PosPayNotifyServiceImpl implements PosPayNotifyService{
	
	@Resource
	private BaseDao<PosPayNotifyEntity> baseDao;

	@Override
	public int addPosPayNotify(PosPayNotifyDto dto) throws BizException {
		// TODO Auto-generated method stub
		PosPayNotifyEntity entity = new PosPayNotifyEntity();
		BeanUtils.copyProperties(dto, entity);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		long id = baseDao.persist(entity,Long.class);
		dto.setId((int)id);
		return (int)id;
	}

	@Override
	public void dynamicUpdatePosPayNotify(PosPayNotifyEntity entity) {
		entity.setUpdateTime(new Date());
		baseDao.dynamicMerge(entity);
	}

	@Override
	public List<PosPayNotifyDto> queryByCondition(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return baseDao.queryForList("PosPayNotify.queryByCondition", params,PosPayNotifyDto.class);
	}

}
