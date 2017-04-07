package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

public interface AgencyCollectionService {
	
	public Integer countTotal(Map<String,Object> map);
	/**
	 * 代收款记录列表
	 */
	public List<Map<String,Object>> queryList(Map<String,Object> map);
	/**
	 * 导出数据countTotal
	 */
	public List<Map<String,Object>> exportAllData(Map<String,Object> map);
    /**
     * 代收款记录详情
     */
	public Map<String,Object> queryPayInfo(Map<String,Object> map);
	/**
	 * 付款方信息
	 */
	public List<Map<String,Object>> payerInfoList(Map<String,Object> map);
	
	/**
	 * 谷登代收信息
	 */
	public List<Map<String,Object>> valleyOfCollectingInfoList(Map<String,Object> map);
	/**
	 * 单个谷登代收信息
	 */
	public Map<String,Object> valleyOfCollectingInfo(Map<String,Object> map);
	
	/**
	 * 收款方信息
	 */
	public List<Map<String,Object>> beneficiaryInfoList(Map<String,Object> map);
	/**
	 * 市场方信息
	 */
	public List<Map<String,Object>> marketInfoList(Map<String,Object> map);
	
	/**
	 * 查询订单清算信息
	 */
	public List<Map<String,Object>> queryOrderClearInfo(Map<String,Object> map);
	/**
	 * 校验签名
	 */
	public boolean validateSign(List<Long> ids) throws Exception;
}
