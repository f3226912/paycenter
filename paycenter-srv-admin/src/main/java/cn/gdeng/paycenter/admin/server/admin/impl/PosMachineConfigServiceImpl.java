package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.service.admin.PosMachineConfigService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
import cn.gdeng.paycenter.dto.pay.PosMachineConfigDTO;
import cn.gdeng.paycenter.entity.pay.PosMachineConfigEntity;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class PosMachineConfigServiceImpl implements PosMachineConfigService {
	
	@Autowired
	private BaseDao<?> baseDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public Integer queryCount(Map<String, Object> params) {
		return baseDao.queryForObject("PosMachineConfig.queryCount", params, Integer.class);
	}
	
	@Override
	public List<PosMachineConfigDTO> queryByCondition(Map<String, Object> params) {
		return baseDao.queryForList("PosMachineConfig.queryByCondition", params, PosMachineConfigDTO.class);
	}
	
	@Override
	public int deleteByBusinessId(Map<String, Object> params) {
		return (int)baseDao.execute("PosMachineConfig.deleteByBusinessId", params);
	}
	
	@Override
	public Long  addPosMachineConfig(PosMachineConfigEntity posMachineConfigEntity) {
			 return (Long) baseDao.persist(posMachineConfigEntity, Long.class);
		
	}
    
	
	@Override
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params) {
		logger.info("");
		ApiResult<AdminPageDTO> apiResult = new ApiResult<>();
		AdminPageDTO adminPageDto = new AdminPageDTO();
		adminPageDto.setTotal(queryCount(params));
		
		if(adminPageDto.getTotal()!=0) {
			adminPageDto.setRows(queryByCondition(params));
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		} else {
			adminPageDto.setRows(new ArrayList<PosMachineConfigDTO>());
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		}
		return apiResult;
	}
}
 