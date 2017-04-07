package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.BillCheckLogService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.BillCheckLogDTO;

/**
 * 对账日志服务实现类
 * @author wjguo
 *
 * datetime:2016年11月11日 下午5:07:17
 */
@Service
public class BillCheckLogServiceImpl implements BillCheckLogService {

	@Autowired
	BaseDao<?> baseDao;
    @Override
    public Integer countByCondition(Map<String, Object> paramMap) throws BizException {
        return baseDao.queryForObject("BillCheckLog.countByCondition", paramMap, Integer.class);
    }

	@Override
	public List<BillCheckLogDTO> queryByConditionPage(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForList("BillCheckLog.queryByConditionPage", paramMap, BillCheckLogDTO.class);
	}

}
