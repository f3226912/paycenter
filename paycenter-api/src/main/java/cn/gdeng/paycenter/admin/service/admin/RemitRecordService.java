package cn.gdeng.paycenter.admin.service.admin;

import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;

/**
 * 用户结算记录接口
 * 
 * @author DavidLiang
 * @date 2016年12月21日 下午2:06:54
 */
public interface RemitRecordService {

	/**
	 * 根据订单号查询结算记录
	 * (这里适用于退款功能，其它可能是返回多条)
	 * @author DavidLiang
	 * @date 2016年12月21日 下午2:19:43
	 * @param paramMap
	 * @return
	 * @throws BizException
	 */
	RemitRecordEntity findRemitRecByOrderNo(Map<String, Object> paramMap) throws BizException;

}
