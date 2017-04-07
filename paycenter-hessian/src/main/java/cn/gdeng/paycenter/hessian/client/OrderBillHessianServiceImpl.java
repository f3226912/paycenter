package cn.gdeng.paycenter.hessian.client;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.caucho.hessian.client.HessianProxyFactory;
import com.gudeng.commerce.gd.order.dto.OrderBillDTO;
import com.gudeng.commerce.gd.order.service.OrderBillService;
import com.gudeng.framework.dba.util.DalUtils;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.order.OrderBillHessianService;
import cn.gdeng.paycenter.dto.pos.OrderBillHessianDto;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;

@Service
public class OrderBillHessianServiceImpl implements OrderBillHessianService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private OrderBillService orderBillService;
	
	@Resource
	public GdProperties gdProperties;
	
	private OrderBillService getHessianOrderBillService() throws MalformedURLException {
		String hessianUrl = gdProperties.getProperties().getProperty("gd.orderBillService.url");
		if (orderBillService == null) {
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);
			orderBillService = (OrderBillService) factory.create(OrderBillService.class, hessianUrl);
		}
		return orderBillService;
	}

	@Override
	public List<OrderBillHessianDto> getOrderBillByCondition(OrderBillHessianDto dto) throws BizException {

		Map<String,Object> map = new HashMap<>();
		map.put("marketId", dto.getMarketId());
		map.put("businessNo", dto.getBusinessNo());
		map.put("clientNo", dto.getClientNo());
		map.put("businessName", dto.getBusinessName());
		map.put("sysRefeNo", dto.getSysRefeNo());
		map.put("billBeginTime", DateUtil.getNow(dto.getBillBeginTime()));
		map.put("billEndTime", DateUtil.getNow(dto.getBillEndTime()));
		map.put("payChannelCode", dto.getPayChannelCode());
		try {
			List<OrderBillDTO> list = getHessianOrderBillService().getOrderBillByCondition(map);
			List<OrderBillHessianDto> res = new ArrayList<>();
			if(list != null && list.size() > 0){
				for(OrderBillDTO bill : list){
					OrderBillHessianDto hessian = new OrderBillHessianDto();
					BeanUtils.copyProperties(bill, hessian);
					res.add(hessian);
				}
			}
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new BizException(MsgCons.C_20000, "从Hessian获取OrderBillDto失败");
		}

	}

}
