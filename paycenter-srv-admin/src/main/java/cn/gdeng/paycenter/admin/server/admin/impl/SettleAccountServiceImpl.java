package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountExportDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountOrderDTO;
import cn.gdeng.paycenter.admin.service.admin.SettleAccountService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;
import cn.gdeng.paycenter.entity.pay.RemitRecordErrorEntity;
import cn.gdeng.paycenter.enums.BankCardEnum;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.SettleAccountStatusEnum;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import cn.gdeng.paycenter.util.web.api.ApiResult;
@Service	
public class SettleAccountServiceImpl implements SettleAccountService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BaseDao<?> baseDao;
	@Override
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params) throws Exception{
		int total = baseDao.queryForObject("RemitRecord.queryCount", params, Integer.class);
		List<SettleAccountDTO> list = Collections.emptyList();
		List<Long> errorIds=Collections.emptyList();
		if(total > 0){
			list = baseDao.queryForList("RemitRecord.queryPage", params, SettleAccountDTO.class);
			List<Long> ids = new ArrayList<Long>();
			for(SettleAccountDTO dto:list){
				ids.add(dto.getId().longValue());
			}
			//验证数据			
			 errorIds = baseDao.batchValidateSign(ids, RemitRecordEntity.class);
		}
		AdminPageDTO page = new AdminPageDTO(total, list);
		if(CollectionUtils.isNotEmpty(errorIds)){
			return new ApiResult<AdminPageDTO>(page, MsgCons.C_60003, MsgCons.M_60003);
		}
		return new ApiResult<AdminPageDTO>(page, MsgCons.C_10000, MsgCons.M_10000);
	}
	
	@Override
	public ApiResult<AdminPageDTO> queryOrderDetailPage(Map<String, Object> params) throws BizException{
		SettleAccountOrderDTO dto = baseDao.queryForObject("RemitRecord.queryOrderDetailCount", params, SettleAccountOrderDTO.class);
		List<SettleAccountOrderDTO> list = null;
		if(dto!=null){
			list = baseDao.queryForList("RemitRecord.queryOrderDetailPage", params, SettleAccountOrderDTO.class);
		}else{
			list = Collections.emptyList();
		}
		AdminPageDTO page = new AdminPageDTO(dto.getCount(), list);
		return new ApiResult<AdminPageDTO>(page, MsgCons.C_10000, MsgCons.M_10000);
	}
	
	@Override
	public List<SettleAccountExportDTO> querySettleAccountList(Map<String, Object> params)throws BizException {
		List<SettleAccountExportDTO> list = baseDao.queryForList("RemitRecord.queryList", params, SettleAccountExportDTO.class);
		if(list==null||list.size()<=0){
			return null;
		}
		return list;
	}
	
	@Override
	public ApiResult<SettleAccountDTO> querySettleAccountDetail(Map<String, Object> params)throws BizException{
		ApiResult<SettleAccountDTO>  apiResult=new ApiResult<>();
		RemitRecordEntity entity=new RemitRecordEntity();
		entity.setId(Integer.parseInt(params.get("id").toString()));
		entity =baseDao.find(RemitRecordEntity.class, entity);
		//查询结算详情
		params.put("batNo", entity.getBatNo());
		params.put("memberId", entity.getMemberId());
		SettleAccountDTO settleAccountDTO= baseDao.queryForObject("RemitRecord.queryDetail",params,SettleAccountDTO.class);
		if(settleAccountDTO==null){
			logger.info("查询结算详情异常,id:{}",params.get("id"));
			apiResult.setCode(MsgCons.C_20000);	
			apiResult.setMsg(MsgCons.M_20000);	
		}
		//异常集合
		List<RemitRecordErrorEntity> list = baseDao.queryForList("RemitRecord.queryDetailErrorMarkById", params, RemitRecordErrorEntity.class);
		if(CollectionUtils.isNotEmpty(list)){
			settleAccountDTO.setListError(list);
		}
		//订单总金额
//		SettleAccountOrderDTO dto = baseDao.queryForObject("RemitRecord.queryOrderDetailCount", params, SettleAccountOrderDTO.class);
//		if(dto!=null){
//			settleAccountDTO.setPaidAmt(dto.getPaidAmt());
//		}
		return apiResult.setResult(settleAccountDTO);
	}
	
	@Override
	@Transactional
	public void updateSettleAccount(Map<String, Object> params)throws BizException{
		if(params.get("payerBankName").toString().equals(BankCardEnum.BUSINESS.getName())){
			params.put("bankCardNo", BankCardEnum.BUSINESS.getCode());
		}else{
			params.put("bankCardNo", BankCardEnum.AGRICULTURE.getCode());
		}
		params.put("payStatus", SettleAccountStatusEnum.PS.getCode());
		baseDao.execute("RemitRecord.updateGenerationPay", params);
		//step1 查询是否更新过手续费记录
		Integer count=baseDao.queryForObject("RemitRecord.queryFeeRecord", params,Integer.class);
		Map<String, Object> feeMap = new HashMap<String, Object>();
		if(count>0){
			//step 2 更新手续费记录
			//0:交易手续费 1:转账手续费 2:其他
			feeMap.put("feeType", 1);
			feeMap.put("financeTime", params.get("payTime"));//账务发生时间-代付时间
			feeMap.put("payAmt", params.get("payAmt"));//实付金额-代付金额
			feeMap.put("feeAmt", params.get("feeAmt"));//服务费-代付手续费
			feeMap.put("operaUserId", params.get("updateUserId"));
			feeMap.put("updateUserId", params.get("updateUserId"));
			feeMap.put("bankTradeNo", params.get("bankTradeNo"));//第三方流水
			feeMap.put("batNo", params.get("batNo"));
			feeMap.put("memberId", params.get("memberId"));
			baseDao.execute("RemitRecord.updateFeeRecord", feeMap);
		}else{
			//step3 添加手续费记录 
			feeMap.put("feeType", "1");
			feeMap.put("financeTime", params.get("payTime"));
			feeMap.put("payAmt", params.get("payAmt"));
			feeMap.put("feeAmt", params.get("feeAmt"));
			feeMap.put("operaUserId", params.get("updateUserId"));
			feeMap.put("updateUserId", params.get("updateUserId"));
			feeMap.put("createUserId", params.get("updateUserId"));
			feeMap.put("thridPayNumber", params.get("bankTradeNo"));
			feeMap.put("rate", "");
			feeMap.put("batNo", params.get("batNo"));
			feeMap.put("memberId", params.get("memberId"));
			baseDao.execute("RemitRecord.addFeeRecord", feeMap);
		}
		feeMap.clear();
		feeMap.put("id", params.get("id"));
		int updateCount = baseDao.execute("RemitRecord.updateRefundRecord", feeMap);
		if(updateCount > 0) {
			baseDao.execute("RemitRecord.updateRefundRecordDetail", feeMap);
		}
	}
	
	@Override
	public ApiResult<Integer> querySettleAccountCount(Map<String, Object> params)throws Exception{
		ApiResult<Integer> apiResult=new ApiResult<>();
		Integer result = baseDao.queryForObject("RemitRecord.queryCount", params, Integer.class);
		apiResult.setResult(result);
		//校验数据
        params.put("startRow", 0);
        params.put("endRow", Integer.MAX_VALUE);
		List<SettleAccountExportDTO> list=querySettleAccountList(params);
		List<Long> ids=new ArrayList<>();
		if(list!=null&&list.size()>0){
			for(SettleAccountExportDTO dto:list){
				ids.add(dto.getId().longValue());
			}
			//验证数据			
			List<Long> errorIds = baseDao.batchValidateSign(ids, RemitRecordEntity.class);
			if(CollectionUtils.isNotEmpty(errorIds)){
				return new ApiResult<Integer>(result, MsgCons.C_60003, MsgCons.M_60003);
			}
		}
		return apiResult;
	}

	@Override
	public ApiResult<Integer> importUpdateSettleAccount(List<SettleAccountDTO> list){
		ApiResult<Integer> apiResult=new ApiResult<>();
		if(list==null||list.size()<=0){
			return new ApiResult<Integer>(null, MsgCons.C_20038, MsgCons.M_20038);
		}
		int sumSuccessCount=0;
		try {
			Map<String, Object> map=new HashMap<>();
			//step 1匹配数据  验证
			Map<String, Object> mapResult= querySettleAccountDetail(list);
			List<SettleAccountDTO> listSettleAccountDTO= (List<SettleAccountDTO>) mapResult.get("list");
			if(Integer.valueOf(mapResult.get("sumFailCount").toString())>0){
				apiResult.setMsg("导入失败，匹配数据"+Integer.valueOf(mapResult.get("sumFailCount").toString())+"条有误，未执行数据导入");
				return apiResult;
			}
			//step 2更新匹配验证过的数据
			for (SettleAccountDTO dto:listSettleAccountDTO) {
				//step 2 更新数据
				map.put("payCenterNumber", dto.getPayCenterNumber().trim());
				map.put("payerBankName", dto.getPayerBankName().trim());
				map.put("payerName", dto.getPayerName().trim());
				map.put("bankTradeNo", dto.getBankTradeNo().trim());
				map.put("payAmt", dto.getPayAmt());
				map.put("feeAmt", dto.getFeeAmt());
				map.put("comment", dto.getComment().trim());
				map.put("payTime", dto.getPayTimeBeginTime());
				map.put("bankCardNo", dto.getBankCardNo().trim());
				map.put("updateUserId", dto.getUpdateUserId());
				map.put("id", dto.getId());
				map.put("payStatus", SettleAccountStatusEnum.PS.getCode());
				Integer result=baseDao.execute("RemitRecord.updateGenerationPay", map);
				sumSuccessCount+=result;
			}
		} catch (Exception e) {
			logger.error("导入的结算记录异常:{}",e);
			apiResult.setCode(MsgCons.C_20037);
			apiResult.setMsg(MsgCons.M_20037);
			return apiResult;
		}
		apiResult.setMsg("导入成功,共导入"+sumSuccessCount+"行数据");
		return apiResult;
	}

	/**
	 * 查询匹配的数据
	 * @param list
	 * @param sumFailCount
	 * @return
	 */
	private Map<String, Object> querySettleAccountDetail(List<SettleAccountDTO> list)throws BizException{
		Map<String, Object> map=new HashMap<>();
		Integer sumFailCount=0;
		List<SettleAccountDTO> listSettleAccountDTO=new ArrayList<>();
		for (SettleAccountDTO dto:list) {
			//step 1 匹配数据
			map.put("changeTime", dto.getChangeTimeBeginTime());
			map.put("account", dto.getAccount().trim());
			map.put("mobile", dto.getMobile().trim());
			map.put("realName", dto.getRealName().trim());
			SettleAccountDTO settleAccountDTO = baseDao.queryForObject("RemitRecord.matchData", map, SettleAccountDTO.class);
			if(settleAccountDTO==null){
				logger.info("匹配导入的结算记录失败:account={},mobile={},realName={},changeTime={}",
						new Object[]{dto.getAccount(),dto.getMobile(),dto.getRealName(),dto.getChangeTimeBeginTime()});
				++sumFailCount;
				continue;
			}
			dto.setId(settleAccountDTO.getId());
			listSettleAccountDTO.add(dto);
		}
		map.put("list", listSettleAccountDTO);
		map.put("sumFailCount", sumFailCount);
		return map;
	}
	
	@Override
	public ApiResult<Integer> addSettleAccountError(Map<String, Object> params) throws BizException{
		ApiResult<Integer> apiResult=new ApiResult<>();
		RemitRecordErrorEntity entity=new RemitRecordErrorEntity();
		entity.setRemitRecordId(Integer.valueOf(params.get("id").toString()));
		entity.setCreateTime(new Date());
		entity.setCreateUserId(params.get("createUserId").toString());
		entity.setComment(params.get("comment").toString());
		Long result=baseDao.persist(entity, Long.class);
		apiResult.setResult(result.intValue());
		//更新用户银行卡验证状态
		if(params.containsKey("bankCardNo")&&StringUtils.isNotBlank(params.get("bankCardNo").toString())){
			 baseDao.execute("RemitRecord.updateMemeberBank",params);
		}
		return apiResult;
	}
}
