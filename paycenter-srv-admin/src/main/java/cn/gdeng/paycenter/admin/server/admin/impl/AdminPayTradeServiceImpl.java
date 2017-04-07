package cn.gdeng.paycenter.admin.server.admin.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.admin.dto.admin.PayTradeDTO;
import cn.gdeng.paycenter.admin.service.admin.AdminPayTradeService;
import cn.gdeng.paycenter.admin.service.admin.AdminRemitRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.TradeRecordDTO;
import cn.gdeng.paycenter.dto.pay.TradeRecordDetailDTO;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.OrderTypeEnum;
import cn.gdeng.paycenter.enums.PayerOrderSourceEnum;

/**
 * 交易记录service实现类
 * 
 */
@Service
public class AdminPayTradeServiceImpl implements AdminPayTradeService{
	private Logger logger = LoggerFactory.getLogger(AdminPayTradeServiceImpl.class);
	
	@Autowired
	private BaseDao<?> baseDao;

	@Autowired
	AdminRemitRecordService adminRemitRecordService;

    @Override
    public List<PayTradeDTO> queryTradePageList(Map<String, Object> map) throws Exception {
    	return baseDao.queryForList("PayTrade.queryTradePageList", map, PayTradeDTO.class);
    }

    @Override
    public Integer getTradePageTotal(Map<String, Object> map) throws Exception {
        return (int) baseDao.queryForObject("PayTrade.getTradePageTotal", map, Integer.class);
    }

    @Override
	public PayTradeDTO queryTradeDetail(Map<String, Object> map) throws Exception{
    	return baseDao.queryForObject("PayTrade.queryTradeDetail", map, PayTradeDTO.class);
    }
    
    @Override
    public List<PayTradeDTO> queryPayTradePageList(Map<String, Object> map) throws Exception {
    	return baseDao.queryForList("PayTrade.queryPayTradePageList", map, PayTradeDTO.class);
    }

    @Override
    public Integer getPayTradePageTotal(Map<String, Object> map) throws Exception {
        return (int) baseDao.queryForObject("PayTrade.getPayTradePageTotal", map, Integer.class);
    }
    
  
    
    @Override
    @Transactional
    public void auditPayTrade(List<String> idList, String userId) throws Exception {
    	for(String id : idList) {
    		if(StringUtils.isNotBlank(id)) {
    			Map<String, Object> m = new HashMap<String, Object>();
    			m.put("id", id);
    			PayTradeDTO payTradeDTO = baseDao.queryForObject("PayTrade.queryTradeList", m, PayTradeDTO.class);
	    		if(payTradeDTO != null) {
	    			if(StringUtils.isNotBlank(payTradeDTO.getAuditUserId())){
	    				throw new Exception("不能选择已复核的数据！");
	    			}
	    			Map<String, Object> map = new HashMap<String, Object>();
	    			map.put("orderNo", payTradeDTO.getOrderNo());
	    			map.put("payCenterNumber", payTradeDTO.getPayCenterNumber());
	    			map.put("auditUserId", userId);
	    			if(payTradeDTO.getId() == null) {
	    				adminRemitRecordService.insertAudit(map);
	    			} else {
		    			map.put("id", payTradeDTO.getId());
		    			adminRemitRecordService.updateAudit(map);
	    			}
	    		} else {
	    			throw new Exception("选择的记录不存在");
	    		}
    		}
    	}
    }
    
    @Override
    public List<PayTradeDTO> queryPayTradeList(Map<String, Object> map) throws Exception {
    	return baseDao.queryForList("PayTrade.queryPayTradeList", map, PayTradeDTO.class);
    }
    
    
    /*************************************************** 新的  djb   *****************************************************/
    
    
    /**
     * 导出数据查询  新的
     * @Description:
     * @param map
     * @return
     * @throws Exception
     *
     */
    @Override
    public List<TradeRecordDetailDTO> queryTradeRecordExportData(Map<String, Object> map) throws Exception {
    	return baseDao.queryForList("PayTrade.queryTradeRecordExportData", map, TradeRecordDetailDTO.class);
    }
    
    
    /**
  	 * 查询导出数据总数 新的
  	 */
      public Integer getTradeRecordExportDataTotal(Map<String, Object> map) throws Exception{
      	return (int) baseDao.queryForObject("PayTrade.getTradeRecordExportDataTotal", map, Integer.class);
      }  
      
      
    /**
	 * 查询交易分页记录 新的
	 */

	@Override
	public List<TradeRecordDTO> queryTradeRecordList(Map<String, Object> map) throws Exception {
		return baseDao.queryForList("PayTrade.queryTradeRecordList", map, TradeRecordDTO.class);
	}

	/**
	 * 查询交易记录总数  新的
	 */
	@Override
	public Integer getTradeRecordTotal(Map<String, Object> map) throws Exception {
		return baseDao.queryForObject("PayTrade.countTradeRecord", map, Integer.class);
	}

	/**
	 * 查询交易详情 新的
	 */
	@Override
	public TradeRecordDetailDTO queryTradeRecordDetail(Map<String, Object> map) throws Exception {
		return baseDao.queryForObject("PayTrade.queryTradeRecordDetail", map, TradeRecordDetailDTO.class);
		}


	/**
	 * 获取所有付款方来源
	 * @Description:
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getPayerOrderSourceEnum(){
		return PayerOrderSourceEnum.toList();
	}
	
	/**
	 * 获取所有订单类型
	 * @Description:
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getOrderTypeEnum(){
		
		return OrderTypeEnum.toList();
	}

	/**
	 * 一个订单查询多个流水
	 * */
	@Override
	public List<PayTradeEntity> queryTradeByOrderNo(Map<String, Object> params) {
		return this.baseDao.queryForList("PayTrade.getTradeListByOrderNo", params,PayTradeEntity.class);
	}
	
	/**
	 * 根据订单号查询(退款使用)
	 */
	@Override
	public PayTradeDTO findTradeByOrderNoForRecord(Map<String,Object> paramMap) throws BizException {
		return baseDao.queryForObject("PayTrade.getTradeByOrderNoForRecord", paramMap, PayTradeDTO.class);
	}
}
