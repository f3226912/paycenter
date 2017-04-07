package cn.gdeng.paycenter.api.server.customer;

import java.util.List;

import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;


public interface CustomerHessianService {

	List<MarketHessianDTO> getAllByType(String type);
}
