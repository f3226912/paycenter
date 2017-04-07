package cn.gdeng.paycenter.admin.server.admin.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.admin.service.admin.AdminRemitRecordService;
import cn.gdeng.paycenter.api.server.pay.FeeRecordService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.FeeRecordDTO;
import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

/**
 * 交易记录service实现类
 * 
 */
@Service
public class AdminRemitRecordServiceImpl implements AdminRemitRecordService{
	private Logger logger = LoggerFactory.getLogger(AdminRemitRecordServiceImpl.class);
	
	@Autowired
	private BaseDao<?> baseDao;
	
	@Autowired
	private FeeRecordService feeRecordService;
	
    @Override
    public void insertAudit(Map<String, Object> map) throws Exception {
		baseDao.execute("RemitRecord.insertAudit", map);
    }
    
    @Override
    public void updateAudit(Map<String, Object> map) throws Exception {
		baseDao.execute("RemitRecord.updateAudit", map);
    }
    
    @Override
    @Transactional
    public void insertTransfer(Map<String, Object> map) throws Exception {
		baseDao.execute("RemitRecord.insertTransfer", map);
        Map<String, Object> feeRecordParamMap = new HashMap<String, Object>();
        feeRecordParamMap.put("feeType", "1");
        feeRecordParamMap.put("payCenterNumber", map.get("payCenterNumber"));
        List<FeeRecordDTO> feeRecordList = feeRecordService.queryByCondition(feeRecordParamMap);
        if(feeRecordList != null && feeRecordList.size() > 0){
        	throw new Exception("重复订单号" + (String)map.get("payCenterNumber"));
        }
        Map<String, Object> bankcardParamMap = new HashMap<String, Object>();
        bankcardParamMap.put("accCardType", "2");
        bankcardParamMap.put("payeeUserId", map.get("payeeUserId"));
        Map<String, Object> feeRecordMap = new HashMap<String, Object>();
        feeRecordMap.put("orderNo", map.get("orderNo"));
        feeRecordMap.put("thridPayNumber", map.get("bankTradeNo"));
        feeRecordMap.put("feeType", "1");
        feeRecordMap.put("payAmt", (Double)map.get("payAmt"));
        feeRecordMap.put("feeAmt", (Double)map.get("feeAmt"));
        feeRecordMap.put("financeTime", map.get("payTime"));
        feeRecordMap.put("operaUserId", map.get("operaUserId"));
        feeRecordMap.put("updateUserId", map.get("operaUserId"));
        feeRecordMap.put("rate", "");
        feeRecordMap.put("remark", "");
        feeRecordMap.put("createUserId", map.get("operaUserId"));
        feeRecordMap.put("payCenterNumber", map.get("payCenterNumber"));
        feeRecordService.addFeeRecord(feeRecordMap);
    }
    
    @Override
    @Transactional
    public void updateTransfer(Map<String, Object> map) throws Exception {
		baseDao.execute("RemitRecord.updateTransfer", map);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("feeType", "1");
        paramMap.put("orderNo", map.get("orderNo"));
        List<FeeRecordDTO> feeRecordList = feeRecordService.queryByCondition(paramMap);
        Map<String, Object> feeRecordMap = new HashMap<String, Object>();
        feeRecordMap.put("orderNo", map.get("orderNo"));
        feeRecordMap.put("thridPayNumber", map.get("bankTradeNo"));
        feeRecordMap.put("feeType", "1");
        feeRecordMap.put("payAmt", (Double)map.get("payAmt"));
        feeRecordMap.put("feeAmt", (Double)map.get("feeAmt"));
        feeRecordMap.put("financeTime", map.get("payTime"));
        feeRecordMap.put("operaUserId", map.get("operaUserId"));
        feeRecordMap.put("updateUserId", map.get("operaUserId"));
        feeRecordMap.put("rate", "");
        feeRecordMap.put("remark", "");
        feeRecordMap.put("payCenterNumber", map.get("payCenterNumber"));
        if(feeRecordList != null && feeRecordList.size() > 0){
        	if(feeRecordList.size() == 1){
        		feeRecordService.updateFeeRecord(feeRecordMap);
        	} else {
        		throw new Exception("重复订单号" + (String)map.get("orderNo"));
        	}
        } else {
            feeRecordMap.put("createUserId", map.get("operaUserId"));
	        feeRecordService.addFeeRecord(feeRecordMap);
        }
    }
}
