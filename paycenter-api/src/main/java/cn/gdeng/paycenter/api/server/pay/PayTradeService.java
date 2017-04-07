package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PayTradeDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;

public interface PayTradeService {

	/**
	 * 获取支付中心的支付流水
	 * @return
	 */
	String getPayCenterNumber();
	
	/**
	 * 添加支付流水
	 * @param payTradeEntity
	 */
	void addPayTrade(PayTradeDto dto) throws BizException;
	
	/**
	 * 添加支付流水
	 * @param payTradeEntity
	 */
	void addPayTrade(List<PayTradeEntity> list) throws BizException;
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	PayTradeEntity getById(Integer id) throws BizException;

	
	/**
	 * 新增订单,如果存在就更新,返回平台流水号
	 */
	void addPayTradeEntity(PayTradeEntity dto) throws BizException;
	
	/**
	 * 修改订单
	 * @param payTrade
	 */
	void updatePayTrade(PayTradeEntity payTrade) ;
	
	/**
	 * 查询订单
	 * @param map
	 */
	List<PayTradeEntity> queryPayTrade(Map<String,Object> map);
	
	/**
	 * 根据平台流水号查交易，找不到抛异常
	 * @return
	 * @throws BizException
	 */
	List<PayTradeEntity> queryPayTradeByPayCenterNumber(String payCenterNumber)throws BizException;
	
	/**
	 * 删除交易流水，删除交易pay_trade_pos
	 * @param payCenterNumber
	 * @throws BizException
	 */
	void deletePayTrade(String appKey,String orderNo,String requestNo) throws BizException;
	
	/**
	 * 删除交易流水，删除交易pay_trade_pos
	 * @param payCenterNumber
	 * @throws BizException
	 */
	void deletePayTrade(String payCenterNumber) throws BizException;
	
	void updateByOrderNo(PayTradeEntity payTrade) ;

}
