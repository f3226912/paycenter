package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.ProfitDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 收益管理service
 * @author youj
 * @Date:2016年12月6日下午20:16:51
 */
public interface ProfitService {
	
	/**
	 * 平台佣金记录数
	 * @param params
	 * @return
	 */
	public Integer queryPlatCommissionCount(Map<String, Object> paramMap) throws BizException;
	
	/**
	 * 分页查询平台佣金记录
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryPlatCommissionPage(Map<String, Object> params)throws BizException;
	
	/**
	 * 导出平台佣金记录
	 * */
	public List<ProfitDTO> exportPlatCommissionRecord (Map<String, Object> paramMap) throws BizException;
	
	/**
	 * 违约金记录数
	 * @param params
	 * @return
	 */
	public Integer queryRefundCount(Map<String, Object> paramMap) throws BizException;
	
	/**
	 * 分页查询违约金金记录
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryRefundPage(Map<String, Object> params)throws BizException;
	
	/**
	 * 导出违约金记录
	 * */
	public List<ProfitDTO> exportRefundRecord (Map<String, Object> paramMap) throws BizException;
}
