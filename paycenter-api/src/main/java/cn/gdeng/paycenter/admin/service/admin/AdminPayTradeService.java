package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.PayTradeDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.AccessSysConfigDTO;
import cn.gdeng.paycenter.dto.pay.PayTradeDto;
import cn.gdeng.paycenter.dto.pay.TradeRecordDTO;
import cn.gdeng.paycenter.dto.pay.TradeRecordDetailDTO;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

/**   
 * 交易记录service接口
 */
public interface AdminPayTradeService {

	/**
	 * 查询交易分页记录
	 */
	public List<PayTradeDTO> queryTradePageList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询交易记录总数
	 */
    public Integer getTradePageTotal(Map<String, Object> map) throws Exception;
    
	/**
	 * 查询交易详情
	 */
	public PayTradeDTO queryTradeDetail(Map<String, Object> map) throws Exception;
	
	
	/***********************************  新的  djb ******************************************/
	
	
	
	/**
	 * 查询交易分页记录
	 */
	public List<TradeRecordDTO> queryTradeRecordList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询交易记录总数
	 */
    public Integer getTradeRecordTotal(Map<String, Object> map) throws Exception;
    
	/**
	 * 查询交易详情
	 */
	public TradeRecordDetailDTO queryTradeRecordDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 导出数据查询
	 * @Description:
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 */
	 List<TradeRecordDetailDTO> queryTradeRecordExportData(Map<String, Object> map) throws Exception;
	
	

	/**
	 * 查询导出数据总数
	 */
    public Integer getTradeRecordExportDataTotal(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询付款记录分页列表
	 */
	public List<PayTradeDTO> queryPayTradePageList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询付款记录总数
	 */
    public Integer getPayTradePageTotal(Map<String, Object> map) throws Exception;
    
	/**
	 * 复核交易记录
	 */
    public void auditPayTrade(List<String> idList, String userId) throws Exception;
    
	/**
	 * 查询付款记录列表
	 */
	public List<PayTradeDTO> queryPayTradeList(Map<String, Object> map) throws Exception;

	/**
	 * 获取所有的付款方来源
	 * @Description:
	 * @return
	 *
	 */
	public List<Map<String, String>> getPayerOrderSourceEnum();
	
	/***
	 * 获取所有的订单类型
	 * @Description:
	 * @return
	 *
	 */
	public List<Map<String, String>> getOrderTypeEnum();
	
	/**
	 * 一个订单查询多个流水
	 * */
	public List<PayTradeEntity>	queryTradeByOrderNo(Map<String,Object> params);
	
	/** 根据订单号查询(退款使用) */
	PayTradeDTO findTradeByOrderNoForRecord(Map<String,Object> paramMap) throws BizException;
	
}






