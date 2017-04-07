package cn.gdeng.paycenter.server.pay.impl;

import java.util.Map;

import javax.annotation.Resource;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayAccountService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenAccountRequest;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;



public class AlipayOpenAccountServiceImpl implements AlipayAccountService<AlipayOpenAccountRequest>{
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	@Override
	public AlipayOpenAccountRequest getAccountRequest(AlipayOpenAccountRequest req) throws BizException {
		//交易账单
		
		//加密
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(req.getAppKey(),PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.OPENER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		req.setApp_id(ac.getAppId());
		req.setSign_type(ac.getKeyType());

		Map<String,String> signMap = AlipaySignUtil.buildRequestPara(req.buildMap(), ac);
		req.setSign(signMap.get("sign"));

		return req;
	}

	@Override
	public boolean signResult(String appKey,Map<String, String> para) throws BizException{
		// TODO Auto-generated method stub
		return false;
	}


}
