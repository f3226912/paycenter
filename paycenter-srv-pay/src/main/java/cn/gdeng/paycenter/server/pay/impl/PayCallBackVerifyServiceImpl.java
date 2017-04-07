package cn.gdeng.paycenter.server.pay.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.PayCallBackVerifyService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.WeChatService;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.PayWayEnum;

@Service
public class PayCallBackVerifyServiceImpl implements PayCallBackVerifyService {

	@Resource
	private AlipayService alipayService;

	@Resource
	WeChatService wechatService;

	@Resource
	private PayTradeService payTradeService;

	public boolean payVerify(List<PayTradeEntity> pay, Map<String, String> params) throws BizException {
		if (StringUtils.equals(pay.get(0).getPayType(), PayWayEnum.ALIPAY_H5.getWay())) {
			return alipayService.payVerify(params, pay);
		} else if (StringUtils.equals(pay.get(0).getPayType(), PayWayEnum.WEIXIN_APP.getWay())) {
			return wechatService.payVerify(params, pay);
		}
		return false;
	}

}
