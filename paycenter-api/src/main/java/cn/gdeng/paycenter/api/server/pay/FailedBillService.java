package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.FailedBillDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 失败账单
 * 
 * @author youj
 *
 */
public interface FailedBillService {
	
	public Integer countTotal(Map<String,Object> map);
	
	/**
	 * 失败账单列表
	 */
	public List<Map<String,Object>> queryList(Map<String,Object> map);
	
	/**
	 * 查询对账单详情(流水-银行有谷登有)
	 * @param params
	 * @return
	 */
	public ApiResult<FailedBillDTO> queryFailedBilltDetail(Map<String, Object> params);
	
	/**
	 * 查询对账单详情(流水-银行无谷登有)
	 * @param params
	 * @return
	 */
	public ApiResult<FailedBillDTO> queryFailedBilltDetailBankNone(Map<String, Object> params);
	
	/**
	 * 查询对账单详情(流水-银行有谷登无)
	 * @param paramss
	 * @return
	 */
	public ApiResult<FailedBillDTO> queryFailedBilltDetailGuDengNone(Map<String, Object> params);
	
	/**
	 * 更新对账明细
	 * @param params
	 * @return
	 */
	public ApiResult<Integer> updateFailedBill(Map<String, Object> params);
	
	/**
	 * 谷登订单信息列表
	 */
	public List<Map<String,Object>> queryGdOrderList(Map<String, Object> params);
	
	/**
	 * 查询订单场景类型
	 */
	public Map<String,Object> queryResultType(Map<String, Object> params);
	
	/**
	 * 获取最新清分标识
	 */
	public Map<String,Object> queryLastestClearFlag(Map<String, Object> params);
}
