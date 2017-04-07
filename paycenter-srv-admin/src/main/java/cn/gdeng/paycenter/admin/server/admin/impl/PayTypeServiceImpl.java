package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import cn.gdeng.paycenter.admin.service.admin.PayTypeService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Service;
@Service
public class PayTypeServiceImpl implements PayTypeService {

	@Resource
	private BaseDao<?> baseDao;
	
	@Override
	public ApiResult<List<PayTypeDto>> queryAll() {
		List<PayTypeDto> list =	baseDao.queryForList("PayType.queryAll", new HashMap<String, Object>(),PayTypeDto.class);
		return new ApiResult<List<PayTypeDto>>().setResult(list);
	}
  
}
