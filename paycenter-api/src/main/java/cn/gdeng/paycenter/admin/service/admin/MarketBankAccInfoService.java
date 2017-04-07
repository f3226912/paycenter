package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.dto.pay.MarketBankAccInfoDTO;
import cn.gdeng.paycenter.entity.pay.MarketBankAccInfoEntity;
import cn.gdeng.paycenter.entity.pay.MemberBaseinfoEntity;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 市场银行账号信息
 * @author wj
 *
 */
public interface MarketBankAccInfoService {

	/**
	 * 市场银行账号信息
	 * @param params
	 * @return
	 */
	public List<MarketBankAccInfoDTO> queryByCondition(Map<String, Object> params);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public ApiResult<MarketBankAccInfoDTO> queryById(Long id);
	
	
	/**
	 * 查询数量
	 * @param params
	 * @return
	 */
	public Integer queryCount(Map<String, Object> params);
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params);
	
	/**
	 * 批量删除
	 * @param ids
	 * @param loginUserId
	 * @return
	 */
	public ApiResult<int[]> batchDelete(String[] ids) throws Exception;
	
	public ApiResult<Long> save(MarketBankAccInfoEntity entity) throws Exception;
	
	public ApiResult<Integer> edit(MarketBankAccInfoEntity entity) throws Exception;
	
	public ApiResult<MemberBaseinfoEntity> getByMobile(String mobile);
	
	public ApiResult<MemberBaseinfoEntity> getByAccount(String account);
	
}