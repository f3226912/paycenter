package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**支付类型服务类
 * 
 * @author wjguo
 *
 * datetime:2016年11月14日 上午9:34:34
 */
public interface PayTypeService {
	/**查询全部支付类型
	 * @return
	 */
	List<PayTypeDto> queryAll();
	
	/**
	 * 根据条件查询总记录数
	* @author DavidLiang
	* @date 2016年11月8日 下午1:59:02
	* @param paramMap
	* @return
	* @throws BizException
	 */
	public int selCountByCondition(Map<String, Object> paramMap) throws BizException;
	
	/**
	 * 根据条件分页查询支付渠道
	* @author DavidLiang
	* @date 2016年11月8日 下午1:59:16
	* @param paramMap
	* @return
	* @throws BizException
	 */
	public List<PayTypeEntity> selPageByCondition(Map<String, Object> paramMap) throws BizException;
	
	/**
	 * 根据条件查询
	 * @param paramMap
	 * @return
	 * @throws BizException
	 */
	List<PayTypeEntity> queryByCondition(Map<String, Object> paramMap) throws BizException;
	
	/**
	 * 根据条件分页查询支付渠道
	* @author DavidLiang
	* @date 2016年11月8日 下午2:08:46
	* @param params
	* @return
	* @throws BizException
	 */
	public ApiResult<AdminPageDTO> findPageByCondition(Map<String, Object> paramMap) throws Exception;
	
}
