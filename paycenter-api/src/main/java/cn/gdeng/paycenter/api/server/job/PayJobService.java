package cn.gdeng.paycenter.api.server.job;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.BillCheckDetailEntity;

/**
 * 定时任务接口
 * @author Ailen
 *
 */
public interface PayJobService {
	
	/**
	 * 自动重发业务系统mq
	 */
	public void processResendNotify();
	
	/**
	 * 支付宝对账
	 */
	public void checkAlipayBill(String checkDate) throws Exception;
	
	/**
	 * 微信对账
	 */
	public void checkWeChatBill(String checkDate) throws Exception;
	
	/**
	 * pos对账
	 */
	public void checkPosBill(String payType, String checkDate) throws Exception;
	
	/**
	 * 自动关闭交易
	 */
	public void closeTrade();
	
	/**
	 * 重新对账
	 * @return
	 * @throws Exception
	 */
	public void checkBill(String payType, String checkDate) throws Exception;

	/**
	 * 重新清算
	 * @return
	 * @throws Exception
	 */
	public void clearBill(String checkDate,String orderNo) throws Exception;
	
	/**
	 * 更新失败的账单
	 * @return
	 * @throws Exception
	 */
	public String updateFailedBill(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询需要对账的支付渠道
	 * @return
	 * @throws Exception
	 */
	public List<PayTypeDto> queryPayTypeCheckBill() throws Exception;
	
	/**
	 * 获取银行或第三方没有的支付账单
	 * @return
	 * @throws Exception
	 */
	public List<BillCheckDetailEntity> queryPayTradeNoCheck(String payType,String payTime)throws Exception;
	
	/**
	 * 清分前 绑定订单与活动
	 * 
	 * */
	public void bindOrderActRelation(String checkDate,String orderNo)throws Exception;
}
