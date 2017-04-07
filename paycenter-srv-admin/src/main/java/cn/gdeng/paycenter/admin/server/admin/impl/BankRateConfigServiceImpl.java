package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.BankRateConfigDTO;
import cn.gdeng.paycenter.admin.service.admin.BankRateConfigService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Service;
@Service
public class BankRateConfigServiceImpl implements BankRateConfigService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BaseDao<?> baseDao;
	@Override
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params) {
		ApiResult<AdminPageDTO> apiResult = new ApiResult<AdminPageDTO>();
		try{
			int total = baseDao.queryForObject("BankRateConfig.getTotal", params, Integer.class);
	     	List<BankRateConfigDTO> list = baseDao.queryForList("BankRateConfig.getByCondition",params, BankRateConfigDTO.class);
			AdminPageDTO page = new AdminPageDTO(total,list);
			apiResult.setResult(page);
			return apiResult;
		}catch(Exception e){
			logger.error("BankRateConfigServiceImpl分页查询：", e);
			return apiResult.withError(MsgCons.C_50000, MsgCons.M_50000);
		}
	}
	
	@Override
	public ApiResult<Integer> insert(Map<String, Object> map)throws Exception{
		ApiResult<Integer> apiResult = new ApiResult<Integer>();
			int status = baseDao.execute("BankRateConfig.insert", map);
			return apiResult.setResult(status);
	}

	@Override
	public ApiResult<Integer> update(Map<String, Object> map)throws Exception{
		ApiResult<Integer> result = new ApiResult<Integer>();
			result.setResult(baseDao.execute("BankRateConfig.update", map));
			return result;
	}
	@Override
	public ApiResult<BankRateConfigDTO> selectById(Map<String, Object> map){
		ApiResult<BankRateConfigDTO> result = new ApiResult<BankRateConfigDTO>();
		try{
			result.setResult(baseDao.queryForObject("BankRateConfig.selectById", map, BankRateConfigDTO.class));
			return result;
		}catch(Exception e){	
			logger.error("", e);
			return result.withError(MsgCons.C_50000, MsgCons.M_50000);
		}
	}

	@Override
	public ApiResult<Integer> queryBankRateConfigSettingCount(Map<String, Object> params) {
		ApiResult<Integer> result = new ApiResult<Integer>();
		int total = baseDao.queryForObject("BankRateConfig.queryBankRateConfigSettingCount",params,Integer.class);
		result.setResult(total);
		return result;
	}

}
