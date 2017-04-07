package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.PaidOrderRecordDTO;
import cn.gdeng.paycenter.api.BizException;

/**
 * 代付订单记录
 * @author xiaojun
 *
 */
public interface PaidOrderRecordService {
	/**
	 * 查询代付订单记录列表
	 * @return
	 * @throws BizException
	 */
	List<PaidOrderRecordDTO> queryPaidOrderRecordList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询代付订单记录总数
	 * @return
	 * @throws BizException
	 */
	Integer queryPaidOrderRecordListTotal(Map<String, Object> map) throws BizException;
	/**
	 * 验签
	 * @param list
	 * @return
	 * @throws Exception
	 */
	Boolean isVerifySignature(List<PaidOrderRecordDTO> list) throws Exception;
}
