package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.admin.RemitRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;

/**
 * 用户结算记录实现
* @author DavidLiang
* @date 2016年12月21日 下午2:21:52
 */
@Service
public class RemitRecordServiceImpl implements RemitRecordService {
	
	@Autowired
	private BaseDao<?> baseDao;

	/**
	 * 根据订单号查询结算记录
	 * (这里适用于退款功能，其它可能是返回多条)
	 */
	@Override
	public RemitRecordEntity findRemitRecByOrderNo(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForObject("RemitRecord.selRemitRecByOrderNo", paramMap, RemitRecordEntity.class);
	}

}
