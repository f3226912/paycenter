package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.RefundRecordDTO;
import cn.gdeng.paycenter.api.BizException;

/**
 * 退款记录接口
* @author DavidLiang
* @date 2016年12月12日 下午2:20:09
 */
public interface RefundRecordService {
	
	/**
	 * 分页查询退款记录
	* @author DavidLiang
	* @date 2016年12月12日 下午2:22:57
	* @param map
	* @return
	* @throws BizException
	 */
	List<RefundRecordDTO> findRefundRecordByPage(Map<String, Object> map) throws BizException;
	
	/**
	 * 根据条件查询退款记录总数
	* @author DavidLiang
	* @date 2016年12月12日 下午2:24:40
	* @param map
	* @return
	* @throws BizException
	 */
	int findCountRefundRecord(Map<String, Object> map) throws BizException;
	
	

}
