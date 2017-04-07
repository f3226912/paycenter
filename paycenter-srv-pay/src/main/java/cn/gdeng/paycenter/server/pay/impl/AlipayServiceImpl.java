package cn.gdeng.paycenter.server.pay.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.AlipayTradeStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.SubPayType;
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

@Service
public class AlipayServiceImpl extends AbstractAlipayServiceImpl implements AlipayService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;
	
	@Override
	public PayJumpDto prePay(PayGateWayDto dto) throws BizException {	
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(dto.getAppKey(),PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
		AlipayConfig cf = AlipayConfigUtil.buildAlipayConfig(config);
		//打包参数
		Map<String, String> temp = buildParamTemp(dto,cf,config);
		//组装URL
		String url = AlipaySignUtil.buildUrl(cf, temp);
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
		//老版md5查密钥
		PayTradeEntity pay = payList.get(0);
		ThirdPayConfigEntity config = thirdPayConfigService.queryByOrderSub(pay.getPayCenterNumber(), SubPayType.ALIPAY.PARTNER);		
		AlipayConfig cf = AlipayConfigUtil.buildAlipayConfig(config);
		//老版参数
		String totalFee = params.get("total_fee");		
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
		String tradeStatus = params.get("trade_status");
		String tradeNo = params.get("trade_no");	
		newPay.setPayCenterNumber(payCenterNumber);
		//老版验证支付状态
		if(StringUtils.equals(tradeStatus, AlipayTradeStatus.TRADE_SUCCESS)){
			newPay.setPayStatus(PayStatus.PAID);
		}
		newPay.setThirdPayNumber(tradeNo);
		return newPay;
	}
	
	private Map<String,String> buildParamTemp(PayGateWayDto dto,AlipayConfig cf,ThirdPayConfigEntity config) throws BizException{

		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
	
		sParaTemp.put("service", cf.getService());
        sParaTemp.put("partner", cf.getPartner());
        sParaTemp.put("seller_id", cf.getSeller_id());
        sParaTemp.put("_input_charset", cf.getInput_charset());
		sParaTemp.put("payment_type", cf.getPayment_type());
		sParaTemp.put("notify_url", cf.getNotify_url());
		sParaTemp.put("return_url", cf.getReturn_url());
		sParaTemp.put("out_trade_no", dto.getPayCenterNumber());
		sParaTemp.put("subject", dto.getTitle());
		sParaTemp.put("total_fee", dto.getPayAmt()+"");//交易金额
		sParaTemp.put("it_b_pay", dto.getTimeOut()+"h");
		//sParaTemp.put("show_url", "");
		return sParaTemp;
	}

}
