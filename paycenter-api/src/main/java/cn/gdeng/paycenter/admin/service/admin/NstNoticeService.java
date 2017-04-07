package cn.gdeng.paycenter.admin.service.admin;

import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.NstNoticeEntityDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 农速通公告service
 * 
 */
public interface NstNoticeService {
	/**
	 * 插入公告
	 * @param map
	 * @return
	 */
	public ApiResult<Integer> insert(Map<String, Object> map);
	/**
	 * 根据id更新公告
	 * @param id
	 * @return
	 */
	public ApiResult<Integer> update(Map<String, Object> map);
	
	/**
	 * 根据id删除公告
	 * @param id
	 * @return
	 */
	public ApiResult<String> delete(String id);
	
	/**
	 * 根据id查找公告
	 * @param id
	 * @return
	 */
	public ApiResult<NstNoticeEntityDTO> selectById(Map<String, Object> map);
	
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> map);
}
