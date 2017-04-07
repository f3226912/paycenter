package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.api.server.pay.AgencyCollectionService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class AgencyCollectionServiceImpl implements AgencyCollectionService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BaseDao<?> baseDao;
	
	@Override
	public Integer countTotal(Map<String, Object> paramMap) {		
		return (Integer) baseDao.queryForObject("AgencyCollection.countTotal", paramMap, Integer.class);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> paramMap) {
		return baseDao.queryForList("AgencyCollection.queryList", paramMap);
	}
	
	@Override
	public List<Map<String, Object>> exportAllData(Map<String, Object> paramMap) {
		return baseDao.queryForList("AgencyCollection.exportData", paramMap);
	}


	@Override
	public Map<String, Object> queryPayInfo(Map<String, Object> paramMap) {
		return baseDao.queryForMap("AgencyCollection.payInfo", paramMap);
	}

	@Override
	public List<Map<String, Object>> payerInfoList(Map<String, Object> paramMap) {
		return baseDao.queryForList("AgencyCollection.payerInfoList", paramMap);
	}

	@Override
	public List<Map<String, Object>> valleyOfCollectingInfoList(Map<String, Object> paramMap) {
		return baseDao.queryForList("AgencyCollection.valleyOfCollectingInfoList", paramMap);
	}
	
	/**
	 * 单个谷登代收信息
	 */
	@Override
	public Map<String,Object> valleyOfCollectingInfo(Map<String,Object> map) {
		return baseDao.queryForMap("AgencyCollection.valleyOfCollectingInfo", map);
	}

	@Override
	public List<Map<String, Object>> beneficiaryInfoList(Map<String, Object> paramMap) {
		return baseDao.queryForList("AgencyCollection.beneficiaryInfoList", paramMap);
	}

	@Override
	public List<Map<String, Object>> marketInfoList(Map<String, Object> paramMap) {
		return baseDao.queryForList("AgencyCollection.marketInfoList", paramMap);
	}

	@Override
	public boolean validateSign(List<Long> ids) throws Exception {
		List<Long> errorIds = baseDao.batchValidateSign(ids, PayTradeEntity.class);
		if(errorIds.size() > 0){
			for(Long id : errorIds){
				logger.error("无效的签名ID：" + id + ",");
			}
			return false;
		}else{
			return true;
		}		
	}

	@Override
	public List<Map<String, Object>> queryOrderClearInfo(Map<String, Object> map) {
		return baseDao.queryForList("AgencyCollection.queryOrderClearInfo", map);
	}
}
