package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.NanningPayNotifyDto;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;

/**
 * POS刷卡服务
 * @author sss
 *
 */
public interface PosPayService {

	void payNotify(PosPayNotifyDto dto) throws BizException;
	
	/**
	 * 订单中心同步 
	 */
	void orderSync(String payCenterNumber,String orderno) throws BizException;
	
	/**
	 * 获取订单列表
	 */
	List<Map<String, String>> nanningOrderList(NanningPayNotifyDto dto) throws Exception;

}
