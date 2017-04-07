package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.dto.admin.RefundRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.RefundRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;

/**
 * 退款记录实现类
* @author DavidLiang
* @date 2016年12月12日 下午2:26:27
 */
@Service
public class RefundRecordServiceImpl implements RefundRecordService {

	@Autowired
	private BaseDao<?> baseDao;
	
	/**
	 * 分页查询退款记录
	 */
	@Override
	public List<RefundRecordDTO> findRefundRecordByPage(Map<String, Object> map) throws BizException {
		return baseDao.queryForList("RefundRecord.selRefundRecordByPage", map, RefundRecordDTO.class);
	}

	/**
	 * 根据条件查询退款记录总数
	 */
	@Override
	public int findCountRefundRecord(Map<String, Object> map) throws BizException {
		return baseDao.queryForObject("RefundRecord.selRefundRecordCount", map, Integer.class);
	}

}
