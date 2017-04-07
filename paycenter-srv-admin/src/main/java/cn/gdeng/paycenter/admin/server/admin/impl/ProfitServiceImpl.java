package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.ProfitDTO;
import cn.gdeng.paycenter.admin.service.admin.ProfitService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;
@Service	
public class ProfitServiceImpl implements ProfitService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BaseDao<?> baseDao;

	@Override
	public ApiResult<AdminPageDTO> queryPlatCommissionPage(Map<String, Object> params) throws BizException {
		int total = baseDao.queryForObject("Profit.queryPlatCommissionCount", params, Integer.class);
		List<ProfitDTO> list = Collections.emptyList();
//		List<Long> errorIds=Collections.emptyList();
		if(total > 0){
			list = baseDao.queryForList("Profit.queryPlatCommission", params, ProfitDTO.class);
//			List<Long> ids = new ArrayList<Long>();
//			for(PlatCommissionDto dto:list){
//				ids.add(dto.getId().longValue());
//			}
			//验证数据			
//			 errorIds = baseDao.batchValidateSign(ids, RemitRecordEntity.class);
		}
		AdminPageDTO page = new AdminPageDTO(total, list);
//		if(CollectionUtils.isNotEmpty(errorIds)){
//			return new ApiResult<AdminPageDTO>(page, MsgCons.C_60003, MsgCons.M_60003);
//		}
		return new ApiResult<AdminPageDTO>(page, MsgCons.C_10000, MsgCons.M_10000);
	}

	@Override
	public ApiResult<AdminPageDTO> queryRefundPage(Map<String, Object> params) throws BizException {
		int total = baseDao.queryForObject("Profit.queryRefundCount", params, Integer.class);
		List<ProfitDTO> list = Collections.emptyList();
//		List<Long> errorIds=Collections.emptyList();
		if(total > 0){
			list = baseDao.queryForList("Profit.queryRefund", params, ProfitDTO.class);
//			List<Long> ids = new ArrayList<Long>();
//			for(PlatCommissionDto dto:list){
//				ids.add(dto.getId().longValue());
//			}
			//验证数据			
//			 errorIds = baseDao.batchValidateSign(ids, RemitRecordEntity.class);
		}
		AdminPageDTO page = new AdminPageDTO(total, list);
//		if(CollectionUtils.isNotEmpty(errorIds)){
//			return new ApiResult<AdminPageDTO>(page, MsgCons.C_60003, MsgCons.M_60003);
//		}
		return new ApiResult<AdminPageDTO>(page, MsgCons.C_10000, MsgCons.M_10000);
	}

	@Override
	public Integer queryPlatCommissionCount(Map<String, Object> paramMap) throws BizException {
		 return baseDao.queryForObject("Profit.queryPlatCommissionCount", paramMap, Integer.class);
	}

	@Override
	public Integer queryRefundCount(Map<String, Object> paramMap) throws BizException {
		 return baseDao.queryForObject("Profit.queryRefundCount", paramMap, Integer.class);
	}

	@Override
	public List<ProfitDTO> exportPlatCommissionRecord(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForList("Profit.queryPlatCommission", paramMap, ProfitDTO.class);
	}

	@Override
	public List<ProfitDTO> exportRefundRecord(Map<String, Object> paramMap) throws BizException {
		return baseDao.queryForList("Profit.queryRefund", paramMap, ProfitDTO.class);
	}
	
}
