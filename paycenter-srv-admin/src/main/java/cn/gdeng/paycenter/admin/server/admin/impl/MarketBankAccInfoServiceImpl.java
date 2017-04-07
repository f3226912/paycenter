package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.service.admin.MarketBankAccInfoService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.MarketBankAccInfoDTO;
import cn.gdeng.paycenter.entity.pay.MarketBankAccInfoEntity;
import cn.gdeng.paycenter.entity.pay.MemberBaseinfoEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class MarketBankAccInfoServiceImpl implements MarketBankAccInfoService {
	
	@Autowired
	private BaseDao<?> baseDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public Integer queryCount(Map<String, Object> params) {
		return baseDao.queryForObject("MarketBankAccInfo.queryCount", params, Integer.class);
	}
	
	@Override
	public List<MarketBankAccInfoDTO> queryByCondition(Map<String, Object> params) {
		return baseDao.queryForList("MarketBankAccInfo.queryByCondition", params, MarketBankAccInfoDTO.class);
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
			adminPageDto.setRows(new ArrayList<MarketBankAccInfoDTO>());
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		}
		return apiResult;
	}

	@Override
	public ApiResult<MarketBankAccInfoDTO> queryById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		MarketBankAccInfoDTO dto = baseDao.queryForObject("MarketBankAccInfo.queryById", map, MarketBankAccInfoDTO.class);
		ApiResult<MarketBankAccInfoDTO> result = new ApiResult<MarketBankAccInfoDTO>();
		return result.setResult(dto);
	}

	@Override
	@Transactional
	public ApiResult<int[]> batchDelete(String[] ids) throws Exception{
		@SuppressWarnings("unchecked")
		Map<String, Object>[] batchValues = new HashMap[ids.length];
		String uuid = UUID.randomUUID().toString();
		for(int i = 0, len = batchValues.length; i < len; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", Integer.parseInt(ids[i]));
			batchValues[i] = map;
			map.put("sign", uuid);
		}
		int[] result = baseDao.batchUpdate("MarketBankAccInfo.batchDelete", batchValues);
		baseDao.batchSign(uuid, MarketBankAccInfoEntity.class);
		return new ApiResult<int[]>().setResult(result);
	}
	
	public ApiResult<Long> save(MarketBankAccInfoEntity entity) throws Exception{
		//Long id = baseDao.persist(entity, Long.class);	
		Long id = baseDao.persistBySign(entity);
		return new ApiResult<Long>().setResult(id);
	}
	
	public ApiResult<Integer> edit(MarketBankAccInfoEntity entity) throws Exception{
		String sign = baseDao.getEntitySign(entity);
		entity.setSign(sign);
		Integer id = baseDao.dynamicMerge(entity);	
		return new ApiResult<Integer>().setResult(id);
	}

	@Override
	public ApiResult<MemberBaseinfoEntity> getByMobile(String mobile) {
		ApiResult<MemberBaseinfoEntity> result = new ApiResult<MemberBaseinfoEntity>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		MemberBaseinfoEntity memberBaseinfoEntity = (MemberBaseinfoEntity)baseDao.queryForObject("MemberBaseinfo.getByMobile", map, MemberBaseinfoEntity.class);
		if(memberBaseinfoEntity == null){
			result.setCode(MsgCons.C_20000);
		}
		return result.setResult(memberBaseinfoEntity);
	}

	@Override
	public ApiResult<MemberBaseinfoEntity> getByAccount(String account) {
		ApiResult<MemberBaseinfoEntity> result = new ApiResult<MemberBaseinfoEntity>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		MemberBaseinfoEntity memberBaseinfoEntity = (MemberBaseinfoEntity)baseDao.queryForObject("MemberBaseinfo.getByAccount", map, MemberBaseinfoEntity.class);
		return result.setResult(memberBaseinfoEntity);
	}
}
 