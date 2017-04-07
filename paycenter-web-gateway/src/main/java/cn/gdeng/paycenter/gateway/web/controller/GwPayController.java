package cn.gdeng.paycenter.gateway.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.AccessSysSignService;
import cn.gdeng.paycenter.api.server.pay.PayService;
import cn.gdeng.paycenter.api.server.pay.PayTypeConfigService;
import cn.gdeng.paycenter.api.server.pay.TradeRefundService;
import cn.gdeng.paycenter.constant.RequestNo;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.pay.PayJumpDto;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeResult;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.gateway.base.BaseController;
import cn.gdeng.paycenter.gateway.init.page.GateWayInitPage;
import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;
import cn.gdeng.paycenter.util.web.api.Base64;
import cn.gdeng.paycenter.util.web.api.RsaUtil;

@Controller
@RequestMapping("gw")
public class GwPayController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String HAS_WEHCAT_PAY = "1";// 是否支持微信支付 1为支持

	@Reference
	private PayTypeConfigService payTypeConfigService;
	@Reference
	private PayService payService;

	@Reference
	private AccessSysSignService accessSysSignService;

	@Reference
	private AccessSysConfigService accessSysConfigService;

	@Reference
	private TradeRefundService tradeRefundService;

	@RequestMapping("initPage")
	public Object initPage(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap = GateWayInitPage.initPageParam(request);
		logger.info("网关请求参数:" + paramMap.toString());
		map.addAllAttributes(paramMap);
		String sign = getString("sign");
		List<PayTypeDto> payTypelist = null;
		AccessSysConfigEntity accessSysConfig = new AccessSysConfigEntity();
		if (StringUtils.isEmpty(paramMap.get("sumPayAmt"))) {
			map.put("sumPayAmt", paramMap.get("payAmt"));
		}
		if (StringUtils.isNotEmpty(paramMap.get("orderInfos"))) {
			// 设置 orderNo
			StringBuilder sb = new StringBuilder();
			List<MergePayTradeRequestDto.OrderInfo> orderInfos = JSON.parseArray(paramMap.get("orderInfos"),
					MergePayTradeRequestDto.OrderInfo.class);
			for (MergePayTradeRequestDto.OrderInfo order : orderInfos) {
				sb.append(order.getOrderNo()).append(",");
			}
			map.put("orderNo", sb.substring(0, sb.length() - 1));
		}

		try {
			accessSysConfig = accessSysConfigService.queryByAppKey(paramMap.get("appKey"));
			sign = new String(Base64.decode(sign), "utf-8");
			paramMap.put("sign", new String(Base64.decode(paramMap.get("sign")), "utf-8"));
			accessSysSignService.accessSysSign(paramMap, sign);
			// 接入系统支付方式
			payTypelist = payTypeConfigService.qureyByAppKey(paramMap.get("appKey"));
			// 兼容老版本无法发起微信SDK支付，将其过滤
			boolean hasWeixinPay = HAS_WEHCAT_PAY.equals(getString("hasWeixinPay"));
			if (!hasWeixinPay) {
				filterPayType(PayWayEnum.WEIXIN_APP.getWay(), payTypelist);
			}
		} catch (BizException e) {
			logger.error("进入支付页面错误,错误编码:" + e.getCode() + " ,错误消息:" + e.getMsg(), e);
			map.put("respCode", e.getCode());
			map.put("respMsg", e.getMsg());
			installMap(map, paramMap, accessSysConfig, e.getCode().toString(), e.getMsg());
			return "payFailure";
		} catch (Exception e) {
			logger.error("进入支付页面错误,系统错误", e);
			map.put("respCode", MsgCons.C_20000);
			map.put("respMsg", MsgCons.M_20000);
			installMap(map, paramMap, accessSysConfig, MsgCons.C_20000.toString(), MsgCons.M_20000);
			return "payFailure";
		}
		// 最终跳转到支付网关h5页面
		map.put("payTypeList", payTypelist);

		return "gateway";
	}

	private void installMap(ModelMap map, Map<String, String> paramMap, AccessSysConfigEntity accessSysConfig,
			String respCode, String respMsg) {
		paramMap.put("respCode", respCode);
		paramMap.put("respMsg", respMsg);
		String oldSign = AccessSysSignUtil.sign(paramMap, accessSysConfig.getKeyType(),
				accessSysConfig.getPrivateKey());
		String sign = Base64.encode(oldSign.getBytes());
		logger.info("签名:" + sign);
		map.put("sign", sign);
	}

	@RequestMapping("pay/{payType}")
	@ResponseBody
	public Object toPay(PayGateWayDto dto, @PathVariable String payType) throws Exception {
		long beginTime = System.currentTimeMillis();
		logger.info("接入系统:" + dto.getAppKey() + ",支付请求支付类型:" + payType);
		PayJumpDto res = new PayJumpDto();
		try {

			// 请求号为空，设置为全款
			if (StringUtils.isEmpty(dto.getRequestNo())) {
				dto.setRequestNo(RequestNo.FULL_PAY);
				dto.setPayCount(1);// 一笔
			}
			res = payService.prePay(dto);

		} catch (BizException e) {
			logger.error(e.getCode() + ":" + e.getMsg(), e);
			res = new PayJumpDto();
			res.setRespCode(e.getCode());
			res.setRespMsg(e.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			res = new PayJumpDto();
			res.setRespCode(MsgCons.C_20000);
			res.setRespMsg("系统异常");
		}
		long endTime = System.currentTimeMillis();
		logger.info("返回支付页面参数:" + JSON.toJSONString(res));
		logger.info("订单号[" + dto.getOrderNo() + "]请求支付花费时间" + (endTime - beginTime) + "ms");
		return res;
	}

	@RequestMapping("payFail")
	public Object payFail(ModelMap map, HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap = GateWayInitPage.initPageParam(request);
		map.addAllAttributes(paramMap);
		return "payFailure";
	}

	@RequestMapping("paySuccess")
	public Object paySuccess(ModelMap map, HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap = GateWayInitPage.initPageParam(request);
		map.addAllAttributes(paramMap);
		return "paySuccess";
	}

	/**
	 * 申请退款 (返回结果不签名)
	 * 
	 * @param dto
	 *            10000 退款成功, 20000 系统错误 20002 该笔退款已存在 20001 参数错误 30000
	 *            原路返回失败，转结算 40004 支付宝业务异常 40005 微信业务异常
	 * 
	 * @return
	 */
	@RequestMapping("refund")
	@ResponseBody
	public RefundTradeResult refund(HttpServletRequest request, HttpServletResponse response) throws Exception {
		RefundTradeResult res = new RefundTradeResult();
		AccessSysConfigEntity accessSysConfig = new AccessSysConfigEntity();
		try {
			Map<String, String> paramMap = GateWayInitPage.initRefundMap(request);
			logger.info("退款入参:" + paramMap);
			// 验证签名
			accessSysConfig = accessSysConfigService.queryByAppKey(paramMap.get("appKey"));

			String sign = new String(Base64.decode(paramMap.get("sign")), "utf-8");
			String link = AccessSysSignUtil.createLinkString(AccessSysSignUtil.paraFilter(paramMap));
			if (RsaUtil.verify(link, sign, accessSysConfig.getSysPublicKey(), "utf-8")) {
				RefundTradeDto dto = JSON.parseObject(JSON.toJSONString(paramMap), RefundTradeDto.class);
				res = tradeRefundService.refund(dto);
			} else {
				res.setCode("20000");
				res.setMsg("签名验证失败");
			}

		} catch (BizException e) {
			logger.error(e.getMessage(), e);
			res.setCode(e.getCode() + "");
			res.setMsg(e.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			res.setCode("20000");
			res.setMsg("系统错误");
		}
		logger.info("退款出参 :" + JSON.toJSONString(res, SerializerFeature.WriteDateUseDateFormat));
		return res;
	}

	/**
	 * 过滤指定支付类型
	 * 
	 * @param payType
	 * @param payTypelist
	 */
	public void filterPayType(String payType, List<PayTypeDto> payTypelist) {
		if (CollectionUtils.isNotEmpty(payTypelist)) {
			for (PayTypeDto pay : payTypelist) {
				if (payType.equalsIgnoreCase(pay.getPayType())) {
					payTypelist.remove(pay);
					break;
				}
			}
		}
	}

}
