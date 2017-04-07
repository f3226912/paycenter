package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.dto.admin.PenaltyRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.PenaltyRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;

/**
 * 违约金记录实现类
* @author DavidLiang
* @date 2016年12月10日 下午3:46:21
 */
@Service
public class PenaltyRecordServiceImpl implements PenaltyRecordService {

	@Autowired
	private BaseDao<?> baseDao;
	
	/**
	 * 分页查询违约金记录
	* @author DavidLiang
	* @date 2016年12月10日 下午3:41:30
	* @param map
	* @return
	* @throws BizException
	 */
	@Override
	public List<PenaltyRecordDTO> findPenaltyRecordByPage(Map<String, Object> map) throws BizException {
		return baseDao.queryForList("PenaltyRecord.selPenaltyRecordByPage", map, PenaltyRecordDTO.class);
	}

	/**
	 * 根据条件查询违约金记录
	* @author DavidLiang
	* @date 2016年12月10日 下午3:43:26
	* @param map
	* @return
	* @throws BizException
	 */
	@Override
	public int findCountPenaltyRecord(Map<String, Object> map) throws BizException {
		return baseDao.queryForObject("PenaltyRecord.selPenaltyRecordCount", map, Integer.class);
	}

}
