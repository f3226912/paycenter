package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.BillCheckSumService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.BillCheckSumDTO;

/**
 * 对账汇总服务实现类
 * @author wjguo
 *
 * datetime:2016年11月11日 下午5:07:31
 */
@Service
public class BillCheckSumServiceImpl implements BillCheckSumService {
	@Autowired
	BaseDao<?> baseDao;
    @Override
    public Integer countByCondition(Map<String, Object> paramMap) throws BizException {
        return baseDao.queryForObject("BillCheckSum.countByCondition", paramMap, Integer.class);
    }

	@Override
	public List<BillCheckSumDTO> queryByConditionPage(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForList("BillCheckSum.queryByConditionPage", paramMap, BillCheckSumDTO.class);
	}

	@Override
	public List<BillCheckSumDTO> queryForExcel(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForList("BillCheckSum.queryForExcel", paramMap, BillCheckSumDTO.class);
	}
}
