package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.PaidDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 代付款记录
 * @author dengjianfeng
 *
 */
public interface PaidService {

	/**
	 * 列表分页查询
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	ApiResult<List<PaidDTO>> queryPage(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询总记录数
	 * @param map
	 * @return
	 */
	int countByCondition(Map<String, Object> map);
	
	/**
	 * 列表查询
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	ApiResult<List<PaidDTO>> queryList(Map<String, Object> map) throws Exception;
	
	/**
	 * 转结算
	 * @param settleType 类型, 0 订单和佣金  1订单    2佣金
	 * @param settleDate 日期 
	 * @param operId 操作者
	 * @return
	 */
	void insertSettlement(String settleType, String settleDate, String operId,String account,String mobile,String startAmt,String endAmt,String greaterZero);
	
	/**
	 * 根据memberId获取clear_detail表的主键id
	 * @param memberIds
	 * @return
	 */
	List<Long> queryClearDetailIdsByMemberId(List<Long> memberIds);
}
