package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.dto.admin.FailedBillDTO;
import cn.gdeng.paycenter.api.server.pay.FailedBillService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.util.web.api.ApiResult;
@Service
public class FailedBillServiceImpl implements FailedBillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BaseDao<?> baseDao; 
	
	@Override
	public Integer countTotal(Map<String, Object> map) {
		return (Integer) baseDao.queryForObject("BillCheckDetail.countTotal", map, Integer.class);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return baseDao.queryForList("BillCheckDetail.queryList", map);
	}

	@Override
	public ApiResult<FailedBillDTO> queryFailedBilltDetail(Map<String, Object> params) {
		ApiResult<FailedBillDTO>  apiResult=new ApiResult<>();
		FailedBillDTO failedBillDTO= baseDao.queryForObject("BillCheckDetail.queryDetail",params,FailedBillDTO.class);
		return apiResult.setResult(failedBillDTO);
	}

	@Override
	public ApiResult<Integer> updateFailedBill(Map<String, Object> params) {
		ApiResult<Integer> apiResult=new ApiResult<>();
		Integer result = baseDao.execute("BillCheckDetail.updateFailedBill", params);
		apiResult.setResult(result);
		return apiResult;
	}

	@Override
	public List<Map<String,Object>> queryGdOrderList(Map<String, Object> params) {
		return baseDao.queryForList("BillCheckDetail.queryGdOrderList", params);
	}

	@Override
	public ApiResult<FailedBillDTO> queryFailedBilltDetailBankNone(Map<String, Object> params) {
		ApiResult<FailedBillDTO>  apiResult=new ApiResult<>();
		FailedBillDTO failedBillDTO = baseDao.queryForObject("BillCheckDetail.queryDetailBankNone",params,FailedBillDTO.class);
		return apiResult.setResult(failedBillDTO);
	}

	@Override
	public ApiResult<FailedBillDTO> queryFailedBilltDetailGuDengNone(Map<String, Object> params) {
		ApiResult<FailedBillDTO>  apiResult=new ApiResult<>();
		FailedBillDTO failedBillDTO= baseDao.queryForObject("BillCheckDetail.queryDetailGuDengNone",params,FailedBillDTO.class);
		return apiResult.setResult(failedBillDTO);
	}

	@Override
	public Map<String,Object> queryResultType(Map<String, Object> params) {
		Map<String,Object> retMap = baseDao.queryForMap("BillCheckDetail.queryResultType",params);
		return retMap;
	}

	@Override
	public Map<String,Object> queryLastestClearFlag(Map<String, Object> params) {
		Map<String,Object> retMap = baseDao.queryForMap("BillCheckDetail.queryLastestClearFlag",params);
		return retMap;
	}
	
}
 