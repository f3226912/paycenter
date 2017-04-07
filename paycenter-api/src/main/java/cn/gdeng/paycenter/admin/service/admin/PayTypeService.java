package cn.gdeng.paycenter.admin.service.admin;

import java.util.List;

import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**支付类型服务类
 * 
 * @author Kwang	
 *
 * datetime:2016年11月18日 上午14:00:34
 */
public interface PayTypeService {
	/**查询全部支付类型
	 * @return
	 */
	ApiResult<List<PayTypeDto>> queryAll();
}
