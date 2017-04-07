package cn.gdeng.paycenter.server.pay.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.WeChatService;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.wechat.WeChatPreResultDto;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundQuery;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundQueryResponse;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundResponse;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.HttpUtil;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;
import cn.gdeng.paycenter.util.web.api.WeChatParseUtil;
import cn.gdeng.paycenter.util.web.api.WeChatSignUtil;

@Service
public class WeChatServiceImpl extends AbstractWeChatServiceImpl implements WeChatService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	@Override
	public WeChatPreResultDto prePay(PayGateWayDto dto) throws BizException {
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(dto.getAppKey(),
				PayWayEnum.WEIXIN_APP.getWay(), SubPayType.WECHAT.APP);
		WeChatConfig cf = WeChatConfigUtil.buildConfig(config);
		// 打包参数
		Map<String, String> paramters = buildRequestParameters(dto, cf);
		// 参数转xml
		String xmlstr = WeChatParseUtil.parseMapToStr(paramters);
		logger.info("调用微信支付交易入参,url:" + cf.getGateWay() + " params:" + xmlstr);

		String result = HttpUtil.httpClientPost(cf.getGateWay(), xmlstr);
		if (StringUtils.isEmpty(result)) {
			throw new BizException(MsgCons.C_50000, "调用微信支付接口服务失败");
		}
		// xml转对象
		Map<String, String> resultMap = new HashMap<String, String>();
		WeChatPreResultDto responseDto = WeChatParseUtil.buildResultObj(result, resultMap, WeChatPreResultDto.class);
		logger.info("调用微信支付交易返回数据:" + JSON.toJSONString(responseDto));
		// 验签
		if (!WeChatSignUtil.tryVerifySign(resultMap, cf)) {
			throw new BizException(MsgCons.C_20008, "微信统一下单接口返回数据效验失败");
		}
		return responseDto;
	}

	public static Map<String, String> buildRequestParameters(PayGateWayDto dto, WeChatConfig cf)
			throws BizException {

		// 把请求参数打包成数组
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("appid", cf.getAppid());
		parameters.put("mch_id", cf.getMch_id());
		parameters.put("body", dto.getTitle());
		parameters.put("sign_type", cf.getSign_type());
		parameters.put("out_trade_no", dto.getPayCenterNumber());
		parameters.put("fee_type", "CNY");
		DecimalFormat df = new DecimalFormat("#");
		parameters.put("total_fee", df.format(dto.getPayAmt() * 100) + "");
		parameters.put("spbill_create_ip", dto.getRequestIp());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		parameters.put("time_start", sdf.format(new Date()));
		parameters.put("notify_url", cf.getNotify_url());
		parameters.put("trade_type", cf.getTrade_type());
		
		// 生成随机字符串
		WeChatParseUtil.buildNonceStr(parameters);
		// 生成MD5摘要
		parameters=WeChatSignUtil.buildRequestSign(parameters, cf);
		//parameters.put("sign", sign);

		return parameters;
	}

	@Override
	public boolean payVerify(Map<String, String> params, List<PayTradeEntity> payList) throws BizException {
		// 先校验total_fee、seller_id与通知时获取的total_fee、seller_id为一致
		PayTradeEntity pay = payList.get(0);
		ThirdPayConfigEntity config = thirdPayConfigService.queryByOrderSub(pay.getPayCenterNumber(),
				SubPayType.WECHAT.APP);
		WeChatConfig cf = WeChatConfigUtil.buildConfig(config);

		String totalFee = params.get("total_fee");
		String mchId = params.get("mch_id");
		double totalFeeD = Double.valueOf(totalFee) / 100;

		if (totalFeeD != PayTradeUtil.getSumPayAmt(payList) || !StringUtils.equals(mchId, cf.getMch_id())) {
			logger.error("平台流水号" + pay.getPayCenterNumber() + ",总金额【" + totalFee + "】或商户Id【" + mchId + ","
					+ cf.getMch_id() + "】与通知的不一致");
			throw new BizException(MsgCons.C_20001, "总金额[" + totalFee + "]或商户Id[" + mchId + "]与通知的不一致");
		}
		return WeChatSignUtil.tryVerifySign(params, cf);
	}

	@Override
	public String buildPaySign(Map<String, String> params, String appKey) throws BizException {
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(appKey, PayWayEnum.WEIXIN_APP.getWay(),
				SubPayType.WECHAT.APP);
		WeChatConfig cf = WeChatConfigUtil.buildConfig(config);
		Map<String,String> result=WeChatSignUtil.buildRequestSign(params, cf);
		return result.get("sign");
	}

	@Override
	public WeChatRefundQueryResponse refundQuery(String appKey, WeChatRefundQuery params) throws BizException {
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(appKey, PayWayEnum.WEIXIN_APP.getWay(),
				SubPayType.WECHAT.APP);
		WeChatConfig cf = WeChatConfigUtil.buildConfig(config);
		// 打包参数

		params.setAppid(cf.getAppid());
		params.setMch_id(cf.getMch_id());
		Map<String, String> paramsMap = buildRefundParam(params);
		
		// 生成随机字符串
		WeChatParseUtil.buildNonceStr(paramsMap);
		// 生成MD5摘要
		WeChatSignUtil.buildRequestSign(paramsMap, cf);
		// 参数转xml
		String xmlParams = WeChatParseUtil.parseMapToStr(paramsMap);
		logger.info("调用微信退款查询入参,url:" + cf.getRefundQueryWay() + " params:" + xmlParams);

		String xmlResult = HttpUtil.httpClientPost(cf.getRefundQueryWay(), xmlParams);
		if (StringUtils.isEmpty(xmlResult)) {
			throw new BizException(MsgCons.C_50000, "调用微信退款查询接口服务失败");
		}
		// xml转对象
		Map<String, String> resultMap = new HashMap<String, String>();
		WeChatRefundQueryResponse response = WeChatParseUtil.buildResultObj(xmlResult, resultMap,
				WeChatRefundQueryResponse.class);
		logger.info("调用微信退款查询返回数据:" + JSON.toJSONString(response));
		// 验签
		if (!WeChatSignUtil.tryVerifySign(resultMap, cf)) {
			throw new BizException(MsgCons.C_20008, "调用微信退款查询接口返回数据效验失败");
		}
		return response;
	}

	private Map<String, String> buildRefundParam(WeChatRefundQuery dto) {
		// 把请求参数打包成数组
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("appid", dto.getAppid());
		parameters.put("mch_id", dto.getMch_id());
		if (dto.getTransaction_id() != null) {
			parameters.put("transaction_id", dto.getTransaction_id());
		}
		if (dto.getOut_trade_no() != null) {
			parameters.put("out_trade_no", dto.getOut_trade_no());
		}
		if (dto.getOut_refund_no() != null) {
			parameters.put("out_refund_no", dto.getOut_refund_no());
		}
		if (dto.getRefund_id() != null) {
			parameters.put("refund_id", dto.getRefund_id());
		}
		

		return parameters;
	}

}
