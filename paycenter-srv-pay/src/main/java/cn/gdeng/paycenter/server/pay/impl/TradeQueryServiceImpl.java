package cn.gdeng.paycenter.server.pay.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.TradeQueryService;
import cn.gdeng.paycenter.constant.AlipayTradeStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.account.TradeQueryRequestDto;
import cn.gdeng.paycenter.dto.account.TradeQueryResponseDto;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenAccountQueryResponse;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenTradeQueryRequest;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.AlipayParseResUtil;
import cn.gdeng.paycenter.server.pay.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;

@Service
public class TradeQueryServiceImpl implements TradeQueryService{
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public TradeQueryResponseDto queryTrade(TradeQueryRequestDto dto) throws BizException {
		//签名
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(dto.getAppKey(),PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.OPENER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		AlipayOpenTradeQueryRequest req = new AlipayOpenTradeQueryRequest();
		req.setApp_id(ac.getAppId());
		req.setSign_type(ac.getKeyType());
		req.setOut_trade_no(dto.getPayCenterNumber());
		req.setTrade_no(dto.getThirdPayNumber());
		ac.setUseSignType(true);
		Map<String,String> signMap = AlipaySignUtil.buildRequestPara(req.buildMap(), ac);
		req.setSign(signMap.get("sign"));
		logger.info("调用支付宝交易查询入参:"+AlipaySignUtil.buildUrl(req.getGateWay(), signMap));
		
		String json = HttpUtil.httpClientGET(AlipaySignUtil.buildUrl(req.getGateWay(), signMap));
		
		logger.info("支付宝交易查询接口返回数据:"+ json);
		if(json == null){
			throw new BizException(MsgCons.C_50000, "调用阿里查询订单服务失败");
		}
		
		AlipayOpenAccountQueryResponse resp = AlipayParseResUtil.parse(AlipayOpenAccountQueryResponse.class, ac, json, req.getMethod()+"_response");
		//状态判断 ACQ.TRADE_NOT_EXIST 交易不存在，返回NULL
		if(("ACQ.TRADE_NOT_EXIST").equals(resp.getSub_code())){
			return null;
		}
		else if(!resp.isSuccess()){
			throw new BizException(MsgCons.C_20000, resp.getSub_code()+"###"+resp.getSub_msg());
		}
		TradeQueryResponseDto res = new TradeQueryResponseDto();
		res.setOrderNo(resp.getOut_trade_no());
		res.setPayType(PayWayEnum.ALIPAY_H5.getWay());
		res.setThirdPayNumber(resp.getTrade_no());
		//支付状态转换   
		if(AlipayTradeStatus.WAIT_BUYER_PAY.equals(resp.getTrade_status())){
			res.setPayStatus(PayStatus.READY_PAY);
		} else if(AlipayTradeStatus.TRADE_FINISHED.equals(resp.getTrade_status())){
			res.setPayStatus(PayStatus.PAID);
		} else if(AlipayTradeStatus.TRADE_SUCCESS.equals(resp.getTrade_status())){
			res.setPayStatus(PayStatus.PAID);
		} else if(AlipayTradeStatus.TRADE_PENDING.equals(resp.getTrade_status())){
			res.setPayStatus(PayStatus.PAID);
		} else {
			//其它状态当做已关闭处理 TRADE_CLOSED
			res.setPayStatus(PayStatus.CLOSED);
		}
		
		return res;
	}

}
