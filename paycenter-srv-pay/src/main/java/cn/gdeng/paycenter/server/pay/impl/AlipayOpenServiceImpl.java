package cn.gdeng.paycenter.server.pay.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenPaymentRequest;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.pay.PayJumpDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.AlipayVerifyUtil;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;


public class AlipayOpenServiceImpl extends AbstractAlipayServiceImpl implements AlipayService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;


	@Override
	public PayJumpDto prePay(PayGateWayDto dto) throws BizException {	
		//付款
		AlipayOpenPaymentRequest req = new AlipayOpenPaymentRequest();
		
		//加密
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(dto.getAppKey(),PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.OPENER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		//设置参数
		req.setSign_type(ac.getKeyType());
		req.setApp_id(ac.getAppId());
		req.setTimeout_express(dto.getTimeOut()+"h");
		req.setSubject(dto.getTitle());
		req.setTotal_amount(dto.getPayAmt()+"");//交易金额
		req.setOut_trade_no(dto.getPayCenterNumber());
		req.setReturn_url(ac.getReturn_url());
		req.setNotify_url(ac.getNotify_url());
		ac.setUseSignType(true);
		//加密参数
		Map<String,String> signMap = AlipaySignUtil.buildRequestPara(req.buildMap(), ac);
		//组装URL
		String url = AlipaySignUtil.buildUrl(req.getGateWay(),signMap);
		//返回,记录到支付宝的入参日志
		PayJumpDto res = new PayJumpDto();
		res.setUrl(url);
		res.setPayType(dto.getPayType());
		res.setRespCode(MsgCons.C_10000);
		logger.info(JSON.toJSONString(res));	
		return res;
	}	
	
	
	@Override
	public boolean payVerify(Map<String, String> params,List<PayTradeEntity> payList) throws BizException {		
		//先校验total_fee、seller_id与通知时获取的total_fee、seller_id为一致
		//新版rsa
		PayTradeEntity pay = payList.get(0);
		ThirdPayConfigEntity config = thirdPayConfigService.queryByOrderSub(pay.getPayCenterNumber(), SubPayType.ALIPAY.OPENER);	
		AlipayConfig cf = AlipayConfigUtil.buildAlipayConfig(config);	
		//新版参数
		String totalFee = params.get("total_amount");		
		String sellerId = params.get("seller_id");
		double totalFeeD = Double.valueOf(totalFee);
		if(totalFeeD != PayTradeUtil.getSumPayAmt(payList)
				|| !StringUtils.equals(sellerId, cf.getSeller_id())){
			logger.error("平台流水号" + pay.getPayCenterNumber() + ",总金额【"+totalFee+"】或卖家Id【"+sellerId + "," + cf.getSeller_id() + "】与通知的不一致");
			throw new BizException(MsgCons.C_20001, "总金额["+totalFee+"]或卖家Id["+sellerId+"]与通知的不一致");
		}
		return AlipayVerifyUtil.verify(params, cf);
	}	
	
	public PayTradeEntity buildPayTrade4Return(Map<String,String> params) throws BizException{
		PayTradeEntity newPay = new PayTradeEntity();
		String payCenterNumber = params.get("out_trade_no");
		String tradeNo = params.get("trade_no");	
		newPay.setPayCenterNumber(payCenterNumber);
		//新版支付状态为成功
		newPay.setPayStatus(PayStatus.PAID);
		newPay.setThirdPayNumber(tradeNo);
		return newPay;
	}

}
