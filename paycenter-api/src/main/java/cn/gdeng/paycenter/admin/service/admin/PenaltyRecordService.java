package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.PenaltyRecordDTO;
import cn.gdeng.paycenter.api.BizException;

/**
 * 违约金记录接口
* @author DavidLiang
* @date 2016年12月10日 下午3:38:49
 */
public interface PenaltyRecordService {
	
	/**
	 * 分页查询违约金记录
	* @author DavidLiang
	* @date 2016年12月10日 下午3:41:30
	* @param map
	* @return
	* @throws BizException
	 */
	List<PenaltyRecordDTO> findPenaltyRecordByPage(Map<String, Object> map) throws BizException;
	
	/**
	 * 根据条件查询违约金记录总数
	* @author DavidLiang
	* @date 2016年12月10日 下午3:43:26
	* @param map
	* @return
	* @throws BizException
	 */
	int findCountPenaltyRecord(Map<String, Object> map) throws BizException;

}
