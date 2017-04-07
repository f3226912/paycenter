package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.dto.pay.PosMachineConfigDTO;
import cn.gdeng.paycenter.entity.pay.PosMachineConfigEntity;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * POS终端配置
 * @author wj
 *
 */
public interface PosMachineConfigService {

	/**
	 * 市场银行账号信息
	 * @param params
	 * @return
	 */
	public List<PosMachineConfigDTO> queryByCondition(Map<String, Object> params);
	
	/**
	 * 查询数量
	 * @param params
	 * @return
	 */
	public Integer queryCount(Map<String, Object> params);
	/**
	 * 插入之前先删除原有数据
	 * @param params
	 * @return
	 */
	
	public int deleteByBusinessId(Map<String, Object> params);
	
	/**
	 * 新增
	 * @param posMachineConfigEntity
	 * @return
	 */
	public Long  addPosMachineConfig(PosMachineConfigEntity posMachineConfigEntity);
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params);
	
}