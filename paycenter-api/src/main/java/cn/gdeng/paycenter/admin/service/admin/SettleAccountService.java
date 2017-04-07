package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountExportDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 结算管理service
 * @author jianhuahuang
 * @Date:2016年11月8日下午5:16:51
 */
public interface SettleAccountService {
	
	/**
	 * 分页查询结算记录
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params)throws Exception;
	
	/**
	 * 查询结算详情的  订单分页数据
	 * @param params
	 * @return
	 */
	public  ApiResult<AdminPageDTO> queryOrderDetailPage(Map<String, Object> params)throws BizException;
	
	/**
	 * 查询导出结算的数据
	 * @param params
	 * @return
	 */
	public List<SettleAccountExportDTO> querySettleAccountList(Map<String, Object> params)throws BizException;
	/**
	 * 查询结算记录详情
	 * @param params
	 * @return
	 */
	public ApiResult<SettleAccountDTO> querySettleAccountDetail(Map<String, Object> params)throws BizException;
	
	/**
	 * 更新结算记录详情-代付信息
	 * @param params
	 * @return
	 */
	public void updateSettleAccount(Map<String, Object> params) throws BizException;
	/**
	 * 查询导出数量
	 * @param params
	 * @return
	 */
	public ApiResult<Integer> querySettleAccountCount(Map<String, Object> params)throws Exception;
	
	/**
	 * 导入数据  更新相关字段数据
	 * @return
	 */
	public ApiResult<Integer> importUpdateSettleAccount(List<SettleAccountDTO> list);
	
	/**
	 * 添加结算异常
	 * @param params
	 * @return
	 */
	public ApiResult<Integer> addSettleAccountError(Map<String, Object> params)throws BizException;
	
	
}
