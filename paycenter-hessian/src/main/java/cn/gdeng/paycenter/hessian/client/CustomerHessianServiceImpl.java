package cn.gdeng.paycenter.hessian.client;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.gdeng.paycenter.api.server.customer.CustomerHessianService;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
import cn.gdeng.paycenter.util.web.api.GdProperties;

import com.caucho.hessian.client.HessianProxyFactory;
import com.gudeng.commerce.gd.customer.dto.MarketDTO;
import com.gudeng.commerce.gd.customer.service.MarketService;

public class CustomerHessianServiceImpl implements CustomerHessianService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MarketService marketService;
	
	@Resource
	public GdProperties gdProperties;
	
	private MarketService getHessianMarketService() throws MalformedURLException {	
		String hessianUrl = gdProperties.getMarketServiceUrl();
		if (marketService == null) {
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);
			marketService = (MarketService) factory.create(MarketService.class, hessianUrl);
		}
		return marketService;
	}
	
	@Override
	public List<MarketHessianDTO> getAllByType(String type) {	
		List<MarketHessianDTO> marketList = new LinkedList<MarketHessianDTO>();
		try{
			List<MarketDTO> list = this.getHessianMarketService().getAllByType(type);
			for(MarketDTO dto : list){
				MarketHessianDTO hessianDTO = new MarketHessianDTO();
				hessianDTO.setId(dto.getId());
				hessianDTO.setMarketName(dto.getMarketName());
				marketList.add(hessianDTO);
			}
		}catch(Exception e){
			logger.error("获取市场信息hessian接口异常",e.getMessage());
		}		
		return marketList;
	}

}
