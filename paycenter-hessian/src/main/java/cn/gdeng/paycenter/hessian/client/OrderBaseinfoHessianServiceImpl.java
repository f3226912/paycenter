package cn.gdeng.paycenter.hessian.client;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.order.OrderAdvanceAndPaymentDTO;
import cn.gdeng.paycenter.api.server.order.OrderbaseInfoHessianService;
import cn.gdeng.paycenter.dto.pos.OrderBaseinfoHessianDto;
import cn.gdeng.paycenter.dto.pos.WangPosOrderListRequestDto;
import cn.gdeng.paycenter.dto.pos.WangPosResultDto;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.GdProperties;

import com.caucho.hessian.client.HessianProxyFactory;
import com.gudeng.commerce.gd.order.dto.OrderBaseinfoDTO;
import com.gudeng.commerce.gd.order.dto.OrderProductDetailDTO;
import com.gudeng.commerce.gd.order.service.OrderBaseinfoService;
import com.gudeng.commerce.gd.order.service.OrderProductDetailService;

@Service
public class OrderBaseinfoHessianServiceImpl implements OrderbaseInfoHessianService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private OrderBaseinfoService orderBaseinfoService;
	
	private OrderProductDetailService orderProductDetailService;
	
	@Resource
	public GdProperties gdProperties;
	
	private OrderBaseinfoService getHessianOrderbaseService() throws MalformedURLException {
		String hessianUrl = gdProperties.getProperties().getProperty("gd.orderBaseinfo.url");
		if (orderBaseinfoService == null) {
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);
			orderBaseinfoService = (OrderBaseinfoService) factory.create(OrderBaseinfoService.class, hessianUrl);
		}
		return orderBaseinfoService;
	}
	
	private OrderProductDetailService getHessianOrderProductDetailService() throws MalformedURLException {
		String hessianUrl = gdProperties.getProperties().getProperty("gd.orderProductDetailService.url");
		if (orderProductDetailService == null) {
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);
			orderProductDetailService = (OrderProductDetailService) factory.create(OrderProductDetailService.class, hessianUrl);
		}
		return orderProductDetailService;
	}

	@Override
	public OrderBaseinfoHessianDto getByOrderNo(String orderNo) throws BizException {
		try {
			OrderBaseinfoDTO dto = getHessianOrderbaseService().getByOrderNo(Long.valueOf(orderNo));
			//转换所需要的信息 
			if(dto == null){
				throw new BizException(MsgCons.C_20000, "订单["+orderNo+"]不存在");
			}
			OrderBaseinfoHessianDto newDto = new OrderBaseinfoHessianDto();
			newDto.setOrderNo(dto.getOrderNo()+"");
			newDto.setOrderTime(dto.getOrderTime());
			newDto.setPayAmt(dto.getPayAmount());
			newDto.setTotalAmt(dto.getOrderAmount());
			if(dto.getMemberId() != null){
				newDto.setPayerUserId(dto.getMemberId()+"");
			}
			
			newDto.setPayerAccount(dto.getAccount());
			newDto.setPayerName(dto.getRealName());
			newDto.setPayerMobile(dto.getBuyerMobile());
			newDto.setPayeeUserId(dto.getSellMemberId()+"");
			newDto.setPayeeAccount(dto.getSellAccount());
			//newDto.setPayeeName(dto.getSellMemberName());
			newDto.setPayeeMobile(dto.getMobile());
			newDto.setMarketId((long)dto.getMarketId());
			newDto.setBusinessId((long)dto.getBusinessId());
			newDto.setOrderType(dto.getOrderType());
			newDto.setMarketName(dto.getMarketName());
			newDto.setShopName(dto.getShopName());
			newDto.setValidPosNum(dto.getValidPosNum());
			return newDto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BizException(MsgCons.C_20000, "从Hessian获取BaseinfoDto失败");
		}

	}
	
	@Override
	public List<Map<String, String>> getUnpaidOrderList(Map<String, Object> map) throws Exception {
		List<Map<String, String>> resultList = new ArrayList<>();
		List<OrderBaseinfoDTO> orderBaseList = getHessianOrderbaseService().getUnpaidOrderList(map);
		if(orderBaseList != null && orderBaseList.size() > 0){
			for(int i=0, len=orderBaseList.size(); i<len;i++){
				OrderBaseinfoDTO orderDTO = orderBaseList.get(i);
				String mobile = orderDTO.getBuyerMobile();
				Map<String, String> tmpMap = new HashMap<>();
				//谷登订单号
				tmpMap.put("orderno", orderDTO.getOrderNo() + "");
				//订单金额
				if(null == orderDTO.getTotalPayAmt()){
					throw new BizException(MsgCons.C_20000, "订单号["+orderDTO.getOrderNo()+"]totalPayAmt为空");
				}
				double orderFee = new BigDecimal(orderDTO.getTotalPayAmt()).multiply(new BigDecimal(100)).doubleValue();
				DecimalFormat df = new DecimalFormat("#0");
				
				tmpMap.put("orderfee", df.format(orderFee));
			    //收款方名称（预留）
				tmpMap.put("accountname", StringUtils.isBlank(orderDTO.getShopName()) ? "":orderDTO.getShopName());
				//付款方名称（买家手机号后四位）
				tmpMap.put("customname", StringUtils.isBlank(mobile) ? "8888":mobile.substring(mobile.length() - 4)); 
				resultList.add(tmpMap);
			}
		}
		return resultList;
	}

	@Override
	public String getProductName(String orderNo) throws BizException {
		Map<String,Object> map = new HashMap<>();
		map.put("orderNo", orderNo);
		try {
			
			OrderProductDetailDTO dto = getHessianOrderProductDetailService().getFirstProductByOrderNo(map);
			return dto.getProductName();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BizException(MsgCons.C_20000, "从Hessian获取OrderProductDetailDTO失败");
		}

	}

	public static void main(String[] args) {
		BigDecimal bg = new BigDecimal(0.01).multiply(new BigDecimal(100));
		double d = bg.doubleValue();
		DecimalFormat df = new DecimalFormat("#0");
		System.out.println(df.format(d));
	}

	@Override
	public WangPosResultDto getFinalOrder4Page(WangPosOrderListRequestDto dto) throws BizException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String updateOrderForCheckBill(Map<String, Object> params) throws Exception {
		return getHessianOrderbaseService().updateOrderForCheckBill(params);
	}
	
	@Override
	public String addOrderForCheckBill(Map<String, Object> params) throws Exception {
		return getHessianOrderbaseService().addOrderForCheckBill(params);
	}
	
	@Override
	public String addPaySerialnumberForCheckBill(Map<String, Object> params) throws Exception {
		return getHessianOrderbaseService().addPaySerialnumberForCheckBill(params);
	}
	
	@Override
	public String dealAdvanceAndPayment(List<OrderAdvanceAndPaymentDTO> list) throws Exception {
		List<com.gudeng.commerce.gd.order.dto.OrderAdvanceAndPaymentDTO> orderAdvanceAndPaymentList = 
				new ArrayList<com.gudeng.commerce.gd.order.dto.OrderAdvanceAndPaymentDTO>();
		for(OrderAdvanceAndPaymentDTO orderAdvanceAndPaymentDTO : list) {
			com.gudeng.commerce.gd.order.dto.OrderAdvanceAndPaymentDTO orderAdvanceAndPayment =
					new com.gudeng.commerce.gd.order.dto.OrderAdvanceAndPaymentDTO();
			orderAdvanceAndPayment.setOrderNo(orderAdvanceAndPaymentDTO.getOrderNo());
			orderAdvanceAndPayment.setPayAmt(orderAdvanceAndPaymentDTO.getPayAmt());
			orderAdvanceAndPayment.setPayBank(orderAdvanceAndPaymentDTO.getPayBank());
			orderAdvanceAndPayment.setPayCenterNumber(orderAdvanceAndPaymentDTO.getPayCenterNumber());
			orderAdvanceAndPayment.setPayerUserId(orderAdvanceAndPaymentDTO.getPayerUserId());
			orderAdvanceAndPayment.setPayTime(orderAdvanceAndPaymentDTO.getPayTime());
			orderAdvanceAndPayment.setPayType(orderAdvanceAndPaymentDTO.getPayType());
			orderAdvanceAndPayment.setThirdPayeeAccount(orderAdvanceAndPaymentDTO.getThirdPayeeAccount());
			orderAdvanceAndPayment.setThirdPayerAccount(orderAdvanceAndPaymentDTO.getThirdPayerAccount());
			orderAdvanceAndPayment.setThirdPayNumber(orderAdvanceAndPaymentDTO.getThirdPayNumber());
			orderAdvanceAndPayment.setType(orderAdvanceAndPaymentDTO.getType());
			orderAdvanceAndPayment.setUpdateUserId(orderAdvanceAndPaymentDTO.getUpdateUserId());
			orderAdvanceAndPaymentList.add(orderAdvanceAndPayment);
		}
		return getHessianOrderbaseService().dealAdvanceAndPayment(orderAdvanceAndPaymentList);
	}
}
