package cn.gdeng.paycenter.admin.service.admin;

import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.BankRateConfigDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 渠道支付配置
 * @author kwang
 *
 */
public interface BankRateConfigService {
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params);
	
	public ApiResult<Integer> insert(Map<String, Object> map)throws Exception;;
	
	public ApiResult<Integer> update(Map<String, Object> map)throws Exception;;
	
	public ApiResult<BankRateConfigDTO> selectById(Map<String, Object> map);
	
	public ApiResult<Integer>  queryBankRateConfigSettingCount(Map<String, Object> params);
}
