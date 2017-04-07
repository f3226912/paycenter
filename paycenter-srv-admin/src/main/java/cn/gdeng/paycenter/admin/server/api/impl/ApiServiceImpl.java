package cn.gdeng.paycenter.admin.server.api.impl;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.api.ApiService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.VClearDetailDto;

@Service
public class ApiServiceImpl implements ApiService{
	
	@Resource
	private BaseDao<?> baseDao;

	@Override
	public List<VClearDetailDto> getVClearList(VClearDetailDto dto) throws BizException {
		
		return baseDao.queryForList("ApiService.getVClearList", dto, VClearDetailDto.class);

	}

}
