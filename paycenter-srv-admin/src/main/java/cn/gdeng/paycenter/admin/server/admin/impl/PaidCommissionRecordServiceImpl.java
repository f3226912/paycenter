package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.dto.admin.PaidCommissionRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.PaidCommissionRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;

/**
 * 代支付佣金记录
 * 
 * @author xiaojun
 *
 */
@Service
public class PaidCommissionRecordServiceImpl implements PaidCommissionRecordService {
	@Autowired
	private BaseDao<?> baseDao;

	@Override
	public List<PaidCommissionRecordDTO> queryPaidCommissionRecordList(Map<String, Object> map) throws BizException {
		// 获取结果集
		List<PaidCommissionRecordDTO> list = baseDao.queryForList("PaidCommissionRecord.queryPaidCommissionRecordList", map,
				PaidCommissionRecordDTO.class);
		return list;
	}

	@Override
	public Integer queryPaidCommissionRecordListTotal(Map<String, Object> map) throws BizException {
		Integer toatl = baseDao.queryForObject("PaidCommissionRecord.queryPaidCommissionRecordListTotal", map, Integer.class);
		return toatl;
	}

}
