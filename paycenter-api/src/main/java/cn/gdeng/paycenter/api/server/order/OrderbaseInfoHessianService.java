package cn.gdeng.paycenter.api.server.order;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pos.OrderBaseinfoHessianDto;
import cn.gdeng.paycenter.dto.pos.WangPosOrderListRequestDto;
import cn.gdeng.paycenter.dto.pos.WangPosResultDto;


public interface OrderbaseInfoHessianService {

	OrderBaseinfoHessianDto getByOrderNo(String orderNo) throws BizException;
	
	List<Map<String, String>> getUnpaidOrderList(Map<String, Object> map) throws Exception;
	
	String getProductName(String orderNo) throws BizException;
	
	/**
	 * 分页获取尾款信息
	 * @return
	 * @throws BizException
	 */
	WangPosResultDto getFinalOrder4Page(WangPosOrderListRequestDto dto) throws BizException;
	
	public String updateOrderForCheckBill(Map<String, Object> params) throws Exception;
	
	public String addOrderForCheckBill(Map<String, Object> params) throws Exception;
	
	public String addPaySerialnumberForCheckBill(Map<String, Object> params) throws Exception;
	
	public String dealAdvanceAndPayment(List<OrderAdvanceAndPaymentDTO> list) throws Exception;
}
