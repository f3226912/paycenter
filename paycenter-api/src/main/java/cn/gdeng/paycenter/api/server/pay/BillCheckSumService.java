package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.BillCheckSumDTO;

/**对账汇总服务接口类
 *   
 * @author wjguo
 *
 * datetime:2016年11月11日 下午5:05:19
 */
public interface BillCheckSumService {
	/**根据条件统计总记录数量
	 * @param paramMap
	 * @return
	 * @throws BizException
	 */
	public Integer countByCondition(Map<String, Object> paramMap) throws BizException;
	 /**根据条件分页查询
	 * @param paramMap
	 * @return
	 * @throws BizException
	 */
	public List<BillCheckSumDTO> queryByConditionPage(Map<String, Object> paramMap) throws BizException;
	/**针对导出excel提供的查询方法
	 * @param paramMap
	 * @return
	 * @throws BizException
	 */
	public List<BillCheckSumDTO> queryForExcel(Map<String, Object> paramMap)throws BizException;

}
