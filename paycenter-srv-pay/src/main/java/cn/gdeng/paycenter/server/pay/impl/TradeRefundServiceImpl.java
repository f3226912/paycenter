package cn.gdeng.paycenter.server.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.RefundRecordService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.TradeRefundService;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.Refund;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.constant.WeChatTradeStatus;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenRefundQueryRequest;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenRefundQueryRespons;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenRefundRequest;
import cn.gdeng.paycenter.dto.alipay.AlipayOpenRefundResponse;
import cn.gdeng.paycenter.dto.refund.RefundQueryResult;
import cn.gdeng.paycenter.dto.refund.RefundTradeDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeResult;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundRequest;
import cn.gdeng.paycenter.dto.wechat.WeChatRefundResponse;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.RefundRecordEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.enums.WeChatCertEnum;
import cn.gdeng.paycenter.server.pay.util.AlipayParseResUtil;
import cn.gdeng.paycenter.server.pay.util.HttpUtil;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;
import cn.gdeng.paycenter.util.web.api.Assert;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;
import cn.gdeng.paycenter.util.web.api.WeChatParseUtil;
import cn.gdeng.paycenter.util.web.api.WeChatSignUtil;

@Service
public class TradeRefundServiceImpl implements TradeRefundService, ApplicationContextAware {

	@Autowired
	public GdProperties gdProperties;

	@Resource
	private ThirdPayConfigService thirdPayConfigService;

	@Resource
	private RefundRecordService fefundRecordService;

	@Resource
	private PayTradeService payTradeService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private WebApplicationContext context;

	@Override
	@Transactional
	public RefundTradeResult refund(RefundTradeDto dto) throws BizException {
		RefundTradeResult result = new RefundTradeResult();
		try {
			result = _refund(dto);
		} catch (BizException e) {
			result.setCode(e.getCode() + "");
			result.setMsg(e.getMsg());
		}
		return result;
	}

	private RefundTradeResult _refund(RefundTradeDto dto) throws BizException {
		RefundTradeResult result = new RefundTradeResult();
		result.setCode("10000");
		result.setMsg("成功");
		// 获取支付记录
		List<PayTradeEntity> tradeList = getPayTrade(dto);
		// 先校验
		validateRefund(dto, tradeList);
		// 查询是否重复退款
		List<RefundRecordEntity> rlist = getRefundLog(dto);
		if (rlist != null && rlist.size() > 0) {
			RefundRecordEntity record = rlist.get(0);
			dto.setRefundNo(record.getRefundNo());
			if (Refund.RefundType.CLEAR.equals(record.getRefundType())) {
				result.setCode("30000");
				result.setMsg("原路返回失败，转结算");
				return result;
			} else {
				if (Refund.RefundStatus.REFUNDED.equals(record.getStatus())) {
					result.setCode("10000");
					result.setRefundNo(record.getRefundNo());
					result.setRefundTime(record.getRefundTime());
					result.setMsg("该笔退款已交易完成，请忽重复退款");
					return result;
				} else {
					// 可以重复退款
					// RefundQueryResult queryRes = alipayRefundQuery(dto);
					// if(queryRes.getCode().equals("10000")){
					// //已退款完成
					// //更新
					// RefundRecordEntity entity = new RefundRecordEntity();
					// entity.setRefundNo(record.getRefundNo());
					// entity.setStatus(Refund.RefundStatus.REFUNDED);
					// entity.setRefundTime(new Date());
					// fefundRecordService.update(entity);
					// result.setCode("20002");
					// result.setMsg("该笔退款已交易完成，请忽重复退款");
					// return result;
					// } else if(queryRes.getCode().equals("30000")){
					// //未退款
					// } else {
					// throw new BizException(MsgCons.C_20000, "查询退款交易失败");
					// }
				}
			}
		}
		// Add
		fefundRecordService.add(dto);
		// 如果退款金额不为0
		if (Double.valueOf(dto.getRefundAmt()) != 0) {
			// 判断通过何种方式退款
			PayTradeEntity tradeLog = tradeList.get(0);
			PayWayEnum way = PayWayEnum.getEnum(tradeLog.getPayType());
			switch (way) {
			case ALIPAY_H5:
				refundByAlipay(dto, result);
				break;
			case WEIXIN_APP:
				refundByWeChat(dto, result);
				break;
			default:
				break;
			}

		} else {
			// 退款金额为0的情况，注：订单金额扣除违约金后，有可能为0
			Date refundTime = new Date();
			result.setRefundTime(refundTime);
			result.setRefundNo(dto.getRefundNo());
			// 更新
			RefundRecordEntity entity = new RefundRecordEntity();
			entity.setRefundNo(dto.getRefundNo());
			entity.setStatus(Refund.RefundStatus.REFUNDED);
			entity.setRefundTime(refundTime);
			// entity.setThirdRefundNo("");//交易流水号设置空
			fefundRecordService.update(entity);
		}

		return result;
	}

	private void validateRefund(RefundTradeDto dto, List<PayTradeEntity> list) throws BizException {
		Assert.notEmpty(dto.getAppKey(), 20001, "appKey不能为空");
		Assert.notEmpty(dto.getOrderNo(), 20001, "orderNo不能为空");
		Assert.notEmpty(dto.getPayCenterNumber(), 20001, "payCenterNumber不能为空");
		Assert.notEmpty(dto.getRefundAmt(), 20001, "refundAmt不能为空");
		Assert.notEmpty(dto.getRefundRequestNo(), 20001, "refundRequestNo不能为空");
		PayTradeUtil.validateDecialmal(dto.getRefundAmt(), 2, "退款金额最多两位小数");
		PayTradeUtil.validateDecialmal(dto.getSellerRefundAmt(), 2, "卖家违约金最多两位小数");
		PayTradeUtil.validateDecialmal(dto.getPlatRefundAmt(), 2, "平台违约金最多两位小数");
		PayTradeUtil.validateDecialmal(dto.getLogisRefundAmt(), 2, "物流公司违约金最多两位小数");
		String refundAmt = dto.getRefundAmt();
		try {
			Double.valueOf(refundAmt);
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20001, "退款金额不是一个数字");
		}
		if (Double.valueOf(dto.getRefundAmt()) < 0) { // 退款金额=0 违约金不等于0
			throw new BizException(MsgCons.C_20001, "退款金额需要大于等于0");
		}

		if (list == null || list.size() == 0) {
			throw new BizException(MsgCons.C_20001, "交易不存在");
		}
		PayTradeEntity pay = list.get(0);
		if (!PayStatus.PAID.equals(pay.getPayStatus())) {
			throw new BizException(MsgCons.C_20001, "[" + dto.getPayCenterNumber() + "]不是已支付状态");
		} else {
			if (Double.valueOf(dto.getRefundAmt()) > pay.getPayAmt()) {
				throw new BizException(MsgCons.C_20001, "退款金额不能大于交易金额");
			}
		}
	}

	private RefundQueryResult alipayRefundQuery(RefundTradeDto dto) throws BizException {
		// 签名
		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(dto.getAppKey(),
				PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.OPENER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		AlipayOpenRefundQueryRequest req = new AlipayOpenRefundQueryRequest();
		req.setApp_id(ac.getAppId());
		req.setSign_type(ac.getKeyType());
		req.setOut_trade_no(dto.getPayCenterNumber());
		req.setOut_request_no(dto.getOrderNo() + dto.getRefundRequestNo());
		ac.setUseSignType(true);
		Map<String, String> signMap = AlipaySignUtil.buildRequestPara(req.buildMap(), ac);
		req.setSign(signMap.get("sign"));

		logger.info("调用支付宝退款查询入参:" + AlipaySignUtil.buildUrl(req.getGateWay(), signMap));

		String json = HttpUtil.httpClientGET(AlipaySignUtil.buildUrl(req.getGateWay(), signMap));

		logger.info("支付宝退款查询返回数据:" + json);

		AlipayOpenRefundQueryRespons resp = AlipayParseResUtil.parse(AlipayOpenRefundQueryRespons.class, ac, json,
				req.getMethod() + "_response");
		RefundQueryResult res = new RefundQueryResult();
		if (MsgCons.C_10000.toString().equals(resp.getCode())) {
			// 成功
			res.setCode("10000");
		} else if ("TRADE_NOT_EXIST".equals(resp.getSub_code())) {
			res.setCode("30000");
		} else {
			throw new BizException(Integer.parseInt(resp.getCode()), resp.getMsg());
		}
		return res;
	}

	/**
	 * 通过支付宝退款
	 * 
	 * @param dto
	 * @param result
	 * @throws BizException
	 */
	public void refundByAlipay(RefundTradeDto dto, RefundTradeResult result) throws BizException {

		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(dto.getAppKey(),
				PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.OPENER);
		AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
		AlipayOpenRefundRequest req = new AlipayOpenRefundRequest();
		req.setApp_id(ac.getAppId());
		req.setSign_type(ac.getKeyType());
		req.setOut_trade_no(dto.getPayCenterNumber());
		req.setOut_request_no(dto.getThirdRefundRequestNo());
		req.setRefund_amount(dto.getRefundAmt());
		req.setRefund_reason(dto.getRefundReason());
		ac.setUseSignType(true);
		Map<String, String> signMap = AlipaySignUtil.buildRequestPara(req.buildMap(), ac);
		req.setSign(signMap.get("sign"));

		logger.info("调用支付宝退款入参:" + AlipaySignUtil.buildUrl(req.getGateWay(), signMap));

		String json = HttpUtil.httpClientGET(AlipaySignUtil.buildUrl(req.getGateWay(), signMap));

		logger.info("支付宝退款接口返回数据:" + json);
		if (json == null) {
			throw new BizException(MsgCons.C_50000, "调用阿里退款服务失败");
		}

		AlipayOpenRefundResponse resp = AlipayParseResUtil.parse(AlipayOpenRefundResponse.class, ac, json,
				req.getMethod() + "_response");
		// 状态判断 ACQ.TRADE_NOT_EXIST 交易不存在，返回NULL
		if (MsgCons.C_10000.toString().equals(resp.getCode())) {
			// 成功
			Date refundTime = DateUtil.getDate(resp.getGmt_refund_pay(), "yyyy-MM-dd HH:mm:ss");
			result.setRefundTime(refundTime);
			result.setRefundNo(dto.getRefundNo());
			// 更新
			RefundRecordEntity entity = new RefundRecordEntity();
			entity.setRefundNo(dto.getRefundNo());
			entity.setStatus(Refund.RefundStatus.REFUNDED);
			entity.setRefundTime(refundTime);
			entity.setThirdRefundNo(resp.getTrade_no());
			fefundRecordService.update(entity);

		} else if ("40004".equals(resp.getCode())) { // 业务失败,走结算
			// 失败
			logger.info("调用退款接口失败，不允许退款，走结算");
			// 更新
			RefundRecordEntity entity = new RefundRecordEntity();
			entity.setRefundNo(dto.getRefundNo());
			entity.setRefundType(Refund.RefundType.CLEAR);
			entity.setStatus(Refund.RefundStatus.READ_REFUND);
			result.setCode("40004");
			result.setMsg(resp.getSub_msg());
			fefundRecordService.update(entity);
		} else {
			throw new BizException(Integer.parseInt(resp.getCode()), resp.getMsg());
		}
	}

	/**
	 * 通过微信退款
	 * 
	 * @param dto
	 * @param result
	 * @throws BizException
	 */
	public void refundByWeChat(RefundTradeDto dto, RefundTradeResult result) throws BizException {

		ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(dto.getAppKey(),
				PayWayEnum.WEIXIN_APP.getWay(), SubPayType.WECHAT.APP);
		WeChatConfig ac = WeChatConfigUtil.buildConfig(config);

		WeChatRefundRequest req = createRequestObj(ac, dto);

		Map<String, String> parameters = buildParamTemp(req);
		// 生成随机字符串
		WeChatParseUtil.buildNonceStr(parameters);

		// 生成MD5摘要
		WeChatSignUtil.buildRequestSign(parameters, ac);

		String xmlStr = WeChatParseUtil.parseMapToStr(parameters);
		logger.info("调用微信退款接口交易入参:url:" + ac.getRefundWay() + " params:" + xmlStr);

		String xmlResult = null;
		String certFilePath = getCertPath(WeChatCertEnum.getNameByCode(dto.getAppKey()));
		xmlResult = HttpUtil.httpCertClientPost(ac.getRefundWay(), xmlStr, certFilePath, ac.getMch_id());

		if (StringUtils.isEmpty(xmlResult)) {
			throw new BizException(MsgCons.C_50000, "调用微信退款接口服务失败");
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		WeChatRefundResponse responseDto = WeChatParseUtil.buildResultObj(xmlResult, resultMap,
				WeChatRefundResponse.class);
		logger.info("调用微信退款接口交易返回数据:" + JSON.toJSONString(responseDto));

		// 验签
		if (!WeChatSignUtil.tryVerifySign(resultMap, ac)) {
			throw new BizException(MsgCons.C_20008, "调用微信支付退款接口数据效验失败");
		}
		// 返回状态码 SUCCESS/FAIL
		if (WeChatTradeStatus.RETURN_SUCCESS.equals(responseDto.getReturn_code())) {
			// 业务结果
			if (WeChatTradeStatus.TRADE_SUCCESS.equals(responseDto.getResult_code())) {

				result.setRefundTime(new Date());
				result.setRefundNo(dto.getRefundNo());
				// 更新
				RefundRecordEntity entity = new RefundRecordEntity();
				entity.setRefundNo(dto.getRefundNo());
				entity.setStatus(Refund.RefundStatus.REFUNDED);
				entity.setRefundTime(new Date());
				entity.setThirdRefundNo(responseDto.getRefund_id());
				fefundRecordService.update(entity);
			} else {
				// 业务失败
				logger.info("调用微信退款接口失败，不允许退款，走结算  info:" + responseDto.getErr_code_des());
				// 更新
				RefundRecordEntity entity = new RefundRecordEntity();
				entity.setRefundNo(dto.getRefundNo());
				entity.setRefundType(Refund.RefundType.CLEAR);
				entity.setStatus(Refund.RefundStatus.READ_REFUND);
				result.setCode("40005");
				result.setMsg(responseDto.getErr_code_des());
				fefundRecordService.update(entity);
			}
		} else {
			throw new BizException(Integer.parseInt(responseDto.getReturn_code()), responseDto.getReturn_msg());
		}

	}

	public static Map<String, String> buildParamTemp(WeChatRefundRequest dto) throws BizException {

		// 把请求参数打包成数组
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("appid", dto.getAppid());
		parameters.put("mch_id", dto.getMch_id());
		parameters.put("out_trade_no", dto.getOut_trade_no());
		parameters.put("out_refund_no", dto.getOut_refund_no());
		parameters.put("total_fee", String.valueOf(dto.getTotal_fee()));
		parameters.put("refund_fee", String.valueOf(dto.getRefund_fee()));
		parameters.put("op_user_id", dto.getOp_user_id());

		return parameters;
	}

	public String getCertPath(String certName) {
		String env = gdProperties.getEnv();
		String certPath = context.getServletContext().getRealPath("/WEB-INF/cert/" + env + "/" + certName);
		return certPath;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = (WebApplicationContext) applicationContext;
	}

	/**
	 * 获取支付记录
	 * 
	 * @param dto
	 * @return
	 */
	public List<PayTradeEntity> getPayTrade(RefundTradeDto dto) {
		Map<String, Object> params = new HashMap<>();
		params.put("payCenterNumber", dto.getPayCenterNumber());
		params.put("orderNo", dto.getOrderNo());
		return payTradeService.queryPayTrade(params);

	}

	/**
	 * 获取退款记录
	 * 
	 * @param dto
	 * @return
	 * @throws BizException
	 */
	public List<RefundRecordEntity> getRefundLog(RefundTradeDto dto) throws BizException {
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", dto.getOrderNo());
		params.put("appKey", dto.getAppKey());
		params.put("refundRequestNo", dto.getRefundRequestNo());
		return fefundRecordService.getList(params);
	}

	/**
	 * 请求参数
	 * 
	 * @param ac
	 * @param dto
	 * @return
	 */
	public WeChatRefundRequest createRequestObj(WeChatConfig ac, RefundTradeDto dto) {
		WeChatRefundRequest req = new WeChatRefundRequest();
		req.setAppid(ac.getAppid());
		req.setMch_id(ac.getMch_id());
		req.setOut_trade_no(dto.getPayCenterNumber());
		req.setOut_refund_no(dto.getOrderNo() + dto.getRefundRequestNo());
		Double refundAmt = Double.valueOf(dto.getRefundAmt()) * 100;
		req.setTotal_fee(refundAmt.intValue());
		req.setRefund_fee(refundAmt.intValue());
		req.setOp_user_id(dto.getRefundUserId());
		return req;
	}
}
