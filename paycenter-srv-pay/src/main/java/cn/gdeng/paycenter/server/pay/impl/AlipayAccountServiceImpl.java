package cn.gdeng.paycenter.server.pay.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayAccountService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.alipay.AlipayAccountRequest;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.AlipayVerifyUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;

@Service
public class AlipayAccountServiceImpl implements AlipayAccountService<AlipayAccountRequest>{
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	@Override
	public AlipayAccountRequest getAccountRequest(AlipayAccountRequest req) throws BizException {
		//交易账单
		
		//加密
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(req.getAppKey(),PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		req.setPartner(ac.getPartner());
		req.setSign_type(ac.getKeyType());
		
		Map<String,String> signMap = AlipaySignUtil.buildRequestPara(req.buildMap(), ac);
		req.setSign(signMap.get("sign"));

		return req;
	}

	@Override
	public boolean signResult(String appKey,Map<String, String> para) throws BizException {
		//加密
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(appKey,PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		ac.setInput_charset("GBK");//这里一定要用GBK验签，否则会失败
		return AlipayVerifyUtil.getSignVeryfy(para, para.get("sign"), ac);
	}

}
