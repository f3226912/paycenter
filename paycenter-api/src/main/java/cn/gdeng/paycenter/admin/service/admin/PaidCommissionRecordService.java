package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.PaidCommissionRecordDTO;
import cn.gdeng.paycenter.api.BizException;

/**
 * 代付佣金记录
 * @author xiaojun
 *
 */
public interface PaidCommissionRecordService {
	/**
	 * 查询代付佣金记录列表
	 * @return
	 * @throws BizException
	 */
	List<PaidCommissionRecordDTO> queryPaidCommissionRecordList(Map<String, Object> map) throws BizException;
	
	/**
	 * 查询代付佣金记录总数
	 * @return
	 * @throws BizException
	 */
	Integer queryPaidCommissionRecordListTotal(Map<String, Object> map) throws BizException;
}
