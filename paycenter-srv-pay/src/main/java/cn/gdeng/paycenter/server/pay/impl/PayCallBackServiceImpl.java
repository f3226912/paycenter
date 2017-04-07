package cn.gdeng.paycenter.server.pay.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.PayCallBackService;
import cn.gdeng.paycenter.api.server.pay.PayCallBackVerifyService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.WeChatService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PayNotifyResponse;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;

@Service
public class PayCallBackServiceImpl implements PayCallBackService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AlipayService alipayService;

	@Resource
	private WeChatService wechatService;

	@Resource
	private PayTradeService payTradeService;

	@Resource
	private PayCallBackVerifyService payCallBackVerifyService;

	@Resource
	private SendMessageUtil sendMessage;

	@Resource
	private AccessSysConfigService accessSysConfigService;

	@Resource
	private BaseDao<?> baseDao;

	/**
	 * 同步回调
	 */
	public PayNotifyResponse payReturnCallBack(List<PayTradeEntity> payList, Map<String, String> params, String payType,
			String payCenterNumber) throws BizException {
		PayNotifyResponse res = new PayNotifyResponse();
		logger.info("支付方式：" + payType + ",回调参数：" + params);
		if (StringUtils.isEmpty(payCenterNumber)) {
			res.setRespCode(MsgCons.C_20001);
			res.setRespMsg("平台流水号[" + payCenterNumber + "]不存在");
			logger.error("平台流水号[" + payCenterNumber + "]不存在");
			return res;
		}
		// 不同支付方式不同处理
		res.setPayCenterNumber(payCenterNumber);

		if (StringUtils.equals(payType, PayWayEnum.ALIPAY_H5.getWay())) {
			payCallBackVerifyService.payVerify(payList, params);
			PayTradeEntity newPay = alipayService.buildPayTrade4Return(params);
			res = postPay(newPay);
		}
		payList = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);
		// 返回H5随机取一个订单
		BeanUtils.copyProperties(payList.get(0), res);
		res.setOrderNo(PayTradeUtil.getOrderNos(payList));
		res.setPayAmt(Double.valueOf(params.get("total_fee")));// 显示用
		return res;
	}

	/**
	 * 异步回调
	 */
	public PayNotifyResponse payNotify(Map<String, String> params, String payType, String payCenterNumber)
			throws BizException {
		// 记录日志
		PayNotifyResponse res = null;
		if (StringUtils.isEmpty(payCenterNumber)) {
			throw new BizException(MsgCons.C_20001, "平台支付流水号不存在");
		}

		// 查询出支付流水
		List<PayTradeEntity> payList = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);

		// 支付宝微信验签
		PayTradeEntity newPay = null;
		payCallBackVerifyService.payVerify(payList, params);
		if (StringUtils.equals(payType, PayWayEnum.ALIPAY_H5.getWay())) {
			newPay = alipayService.buildPayTrade4Notify(params);
		} else if (StringUtils.equals(payType, PayWayEnum.WEIXIN_APP.getWay())) {
			newPay = wechatService.buildPayTrade4Notify(params);
		}
		newPay.setNotifyStatus(NotifyStatus.READ_NOTIFY); // 待通知
		res = postPay(newPay);

		payList = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);
		// 发送MQ
		for (PayTradeEntity old : payList) {
			final PayTradeEntity finalEntity = new PayTradeEntity();
			final String payDesc=PayWayEnum.getNameByCode(payType);
			try {

				finalEntity.setId(old.getId());
				BeanUtils.copyProperties(old, res);
				AccessSysConfigEntity accessSysConfig = accessSysConfigService.queryByAppKey(res.getAppKey());
				logger.info(payDesc+"发送支付成功MQ：" + accessSysConfig.getMqTopic());
				sendMessage.sendAsyncMessage(accessSysConfig.getMqTopic(), "TAGS", payCenterNumber, res,
						new SendCallback() {

							@Override
							public void onSuccess(SendResult sendResult) {
								finalEntity.setNotifyStatus(NotifyStatus.NOTIFIED);
								baseDao.dynamicMerge(finalEntity);
								logger.info(payDesc+"异步通知发送MQ成功");
							}

							@Override
							public void onException(OnExceptionContext context) {

								logger.info(payDesc+"异步通知发送MQ失败");
							}
						});

			} catch (Exception e) {
				// 记录日志
				logger.info(payDesc+"异步通知发送MQ失败," + e.getMessage(), e);
			}
		}
		return res;

	}

	@Transactional
	private PayNotifyResponse postPay(PayTradeEntity pay) throws BizException {

		payTradeService.updatePayTrade(pay);

		// 构建返回参数
		PayNotifyResponse res = new PayNotifyResponse();

		res.setRespCode(MsgCons.C_10000);
		res.setRespMsg(MsgCons.M_10000);
		return res;
	}

	public PayNotifyResponse joinPayNotifyResponse(PayNotifyResponse res, String payCenterNumber) {

		try {
			List<PayTradeEntity> plist = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);
			PayTradeEntity ptrade = plist.get(0);
			BeanUtils.copyProperties(ptrade, res);
			res.setOrderNo(PayTradeUtil.getOrderNos(plist));
		} catch (Exception e) {
			logger.error("查询支付流水失败,原因：" + e.getMessage());
		}

		return res;
	}

}
