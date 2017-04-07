package cn.gdeng.paycenter.api.server.order;

import java.util.List;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pos.OrderBillHessianDto;

public interface OrderBillHessianService {

	List<OrderBillHessianDto> getOrderBillByCondition(OrderBillHessianDto dto) throws BizException;
}
