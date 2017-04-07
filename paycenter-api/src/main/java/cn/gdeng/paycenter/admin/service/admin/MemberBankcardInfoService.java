package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.dto.pay.MemberBankcardInfoDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 用户银行卡管理
 * @author Ailen
 *
 */
public interface MemberBankcardInfoService {

	/**
	 * 根据条件查询
	 * @param params
	 * @return
	 */
	public List<MemberBankcardInfoDTO> queryByCondition(Map<String, Object> params);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public MemberBankcardInfoDTO queryById(Integer id);
	
	/**
	 * 更新
	 * @param memberBankcardInfoDTO
	 */
	public void update(MemberBankcardInfoDTO memberBankcardInfoDTO);
	
	/**
	 * 批量更新
	 * @param memberBankcardInfoDTOs
	 */
	public void updateBatch(List<MemberBankcardInfoDTO> memberBankcardInfoDTOs);
	
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
	
}