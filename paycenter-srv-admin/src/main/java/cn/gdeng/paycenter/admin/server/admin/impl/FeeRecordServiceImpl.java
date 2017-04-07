package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.util.DalUtils;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.FeeRecordService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.FeeRecordDTO;
import cn.gdeng.paycenter.dto.pay.MemberBankcardInfoDTO;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;

@Service
public class FeeRecordServiceImpl implements FeeRecordService {
	
	@Autowired
	private BaseDao<?> baseDao;
	
	@Resource
	private BaseDao<PayTradeEntity> payTradeDao;
	
	@Resource
	private BaseDao<FeeRecordEntity> feeRecordDao;
	
	@Resource
	private BaseDao<PayTypeEntity> payTypeDao;
	
	@Resource
	private BaseDao<AccessSysConfigEntity> accessSysConfigDao;


	@Override
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params) {
		ApiResult<AdminPageDTO> apiResult = new ApiResult<>();
		
		AdminPageDTO adminPageDto = new AdminPageDTO();
		
		adminPageDto.setTotal(queryCount(params));
		
		if(adminPageDto.getTotal()!=0) {
			adminPageDto.setRows(queryByCondition(params));
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		} else {
			adminPageDto.setRows(new ArrayList<MemberBankcardInfoDTO>());
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		}
		
		return apiResult;

	}
	@Override
	public ApiResult<AdminPageDTO> queryPayListPage(Map<String, Object> params) {
		ApiResult<AdminPageDTO> apiResult = new ApiResult<>();
		
		AdminPageDTO adminPageDto = new AdminPageDTO();
		
		adminPageDto.setTotal(queryPayCount(params));
		
		if(adminPageDto.getTotal()!=0) {
			adminPageDto.setRows(queryFeePayList(params));
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		} else {
			adminPageDto.setRows(new ArrayList<MemberBankcardInfoDTO>());
			apiResult.setCode(0001);
			apiResult.setIsSuccess(true);
			apiResult.setResult(adminPageDto);
		}
		
		return apiResult;
	}




	@Override
	public List<FeeRecordDTO> queryByCondition(Map<String, Object> params) {
		List<FeeRecordDTO> list = baseDao.queryForList("FeeRecord.queryFeeList", params, FeeRecordDTO.class);
//		for(FeeRecordDTO feeRecordDTO:list){
//			try {
//				if(null != feeRecordDTO.getPayCenterNumber() && !feeRecordDTO.getPayCenterNumber().equals("")){
//					PayTradeEntity payTradeEntity = queryPayTradeByPayCenterNumber(feeRecordDTO.getPayCenterNumber());
//					if(null != payTradeEntity){
//						feeRecordDTO.setTradeId(payTradeEntity.getId());
//						feeRecordDTO.setAppKey(payTradeEntity.getAppKey());
//						if(null != payTradeEntity.getPayType() && !payTradeEntity.getPayType().equals("")){
//							PayTypeEntity payTypeEntity = queryPayTypeByType(payTradeEntity.getPayType());
//							if(null != payTypeEntity ){
//								feeRecordDTO.setPayType(payTypeEntity.getPayTypeName());
//							}
//						}
//					}
//				}
//			} catch (BizException e) {
//				e.printStackTrace();
//			}
//		}
		return list;
	}
	
	

	@Override
	public List<FeeRecordDTO> queryFeePayList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		List<FeeRecordDTO> list = baseDao.queryForList("FeeRecord.queryPayList", params, FeeRecordDTO.class);
		return list;
	}


	@Override
	public PayTypeEntity queryPayTypeByType(String payType) throws BizException {
		Map<String,Object> param = new HashMap<>();
		param.put("payType", payType);
		List<PayTypeEntity> list =  this.payTypeDao.queryForList("PayType.queryPayTypeByType", param, PayTypeEntity.class);
		if(list == null || list.size() == 0){
			throw new BizException(MsgCons.C_20001, "["+payType+"]不存在");
		}
		return list.get(0);
	}


	@Override
	public FeeRecordDTO queryById(Integer id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return baseDao.queryForObject("FeeRecord.queryById", params, FeeRecordDTO.class);
	}


	@Override
	public Integer queryCount(Map<String, Object> params) {
		return baseDao.queryForObject("FeeRecord.queryCount", params, Integer.class);
	}
	
	@Override
	public Integer queryPayCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return baseDao.queryForObject("FeeRecord.queryPayCount", params, Integer.class);
	}




	@Override
	public void addFeeRecord(FeeRecordDTO feeRecordDTO) {
		Map<String, Object> params = DalUtils.convertToMap(feeRecordDTO);
		baseDao.execute("FeeRecord.addFeeRecord", params);
	}

	@Override
	public void updateFeeRecord(FeeRecordDTO feeRecordDTO) throws Exception {
		String sign = baseDao.getEntitySign(feeRecordDTO);
		feeRecordDTO.setSign(sign);
		Map<String, Object> params = DalUtils.convertToMap(feeRecordDTO);
		baseDao.execute("FeeRecord.updateFeeRecord", params);
	}
	
	@Override
	public void addFeeRecord(Map<String, Object> params) {
		baseDao.execute("FeeRecord.addFeeRecord", params);
	}
	
	@Override
	public void updateFeeRecord(Map<String, Object> params) {
		baseDao.execute("FeeRecord.updateFeeRecord", params);
	}

	@Override
	public PayTradeEntity queryPayTradeByPayCenterNumber(String payCenterNumber) throws BizException {
		Map<String,Object> param = new HashMap<>();
		param.put("payCenterNumber", payCenterNumber);
		List<PayTradeEntity> list =  this.payTradeDao.queryForList("PayTrade.queryPayTradeByNumber", param, PayTradeEntity.class);
		if(list == null || list.size() == 0){
			throw new BizException(MsgCons.C_20001, "平台流水号["+payCenterNumber+"]不存在");
		}
		return list.get(0);
	}

	@Override
	public void updateFeeDetailAndAppKey(String appKey, FeeRecordEntity feeRecordEntity) throws Exception {
		String sign = this.feeRecordDao.getEntitySign(feeRecordEntity);
		feeRecordEntity.setSign(sign);
		this.feeRecordDao.dynamicMerge(feeRecordEntity);
		feeRecordEntity = queryById(feeRecordEntity.getId());
		if(null != feeRecordEntity.getPayCenterNumber() && !feeRecordEntity.getPayCenterNumber().equals("")){
			PayTradeEntity payTradeEntity = queryPayTradeByPayCenterNumber(feeRecordEntity.getPayCenterNumber());
			payTradeEntity.setAppKey(appKey);
			this.payTradeDao.dynamicMerge(payTradeEntity);
		}
	}


	@Override
	public List<AccessSysConfigEntity> getAccSysConfigs() {
		Map<String,Object> param = new HashMap<>();
		return this.accessSysConfigDao.queryForList("AccessSysConfig.getAll",param,AccessSysConfigEntity.class);
	}
	
	
	
}
 