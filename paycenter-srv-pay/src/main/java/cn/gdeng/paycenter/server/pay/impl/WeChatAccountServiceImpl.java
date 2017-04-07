package cn.gdeng.paycenter.server.pay.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.WeChatAccountService;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;
import cn.gdeng.paycenter.dto.account.WeChatAccountParams;
import cn.gdeng.paycenter.dto.account.WeChatAccountRequest;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.AlipayVerifyUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.StringUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil;
import cn.gdeng.paycenter.util.web.api.WeChatSignUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;

@Service
public class WeChatAccountServiceImpl implements WeChatAccountService<WeChatAccountRequest>{
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	@Override
	public WeChatAccountRequest getAccountRequest(AccountRquestDto dto) throws BizException {
		WeChatAccountRequest request=new WeChatAccountRequest();

		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
				(dto.getAppKey(),PayWayEnum.WEIXIN_APP.getWay(), SubPayType.WECHAT.APP);
		WeChatConfig ac = WeChatConfigUtil.buildConfig(config);
		request.setUrl(ac.getBillWay());
		WeChatAccountParams params=new WeChatAccountParams();
		params.setAppid(ac.getAppid());
		params.setMch_id(ac.getMch_id());
		params.setBill_date(dto.getBillDate());
		params.setNonce_str(StringUtil.getRandomString(16));
		Map<String, String> temp = buildParamTemp(params);
		temp=WeChatSignUtil.buildRequestSign(temp, ac);
		params.setSign(temp.get("sign"));
		request.setParams(params);
		return request;
	}

	public static Map<String, String> buildParamTemp(WeChatAccountParams dto)
			throws BizException {

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();

		sParaTemp.put("appid", dto.getAppid());
		sParaTemp.put("mch_id", dto.getMch_id());
		sParaTemp.put("bill_date",dto.getBill_date());
		sParaTemp.put("bill_type",dto.getBill_type() );
		sParaTemp.put("nonce_str", dto.getNonce_str());
	
		return sParaTemp;
	}
}
