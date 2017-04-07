package cn.gdeng.paycenter.server.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.MergePayTradeService;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PayService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.ValidprePayParamService;
import cn.gdeng.paycenter.api.server.pay.WeChatService;
import cn.gdeng.paycenter.dto.pay.PayGateWayDto;
import cn.gdeng.paycenter.dto.pay.PayJumpDto;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.dto.wechat.WeChatPreResultDto;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;
import cn.gdeng.paycenter.util.web.api.PayLogUtil;
import cn.gdeng.paycenter.util.web.api.StringUtil;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

@Service
public class PayServiceImpl implements PayService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private AlipayService alipayService;
	
	@Resource
	private WeChatService weChatService;
	
	@Resource
	private PayLogRecordService payLogRecordService;
	
	@Resource
	private ValidprePayParamService validprePayParamService;
	
	@Resource
	private PayTradeService payTradeService;
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;
	
	@Resource
	private MergePayTradeService mergePayTradeService;

	@Override
	public PayJumpDto prePay(PayGateWayDto dto) throws BizException{
		//请求支付入参日志
		logger.info("H5入参:"+JSON.toJSONString(dto));
		if(StringUtils.isNotEmpty(dto.getOrderInfos())){
			return mutablePrePay(dto);
		} else {
			return fullPrePay(dto);
		}
	}
	
	private PayJumpDto fullPrePay(PayGateWayDto dto)throws BizException{
		PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(dto.getPayType(),dto.getOrderNo(), "收到业务系统参数:"+JSON.toJSONString(dto));
		Long id = payLogRecordService.addLog(log);

		long beginTime = System.currentTimeMillis();
		validprePayParamService.validPrePayParam(dto);
		long endTime = System.currentTimeMillis();
		logger.info("订单号["+dto.getOrderNo()+"]请求支付，校验参数合法性及支付状态共花费"+(endTime-beginTime)+"ms");
		PayJumpDto pjd = null;
		//先查询订单
		Map<String,Object> map = new HashMap<>();
		map.put("orderNo", dto.getOrderNo());
		map.put("appKey", dto.getAppKey());
		map.put("requestNo", dto.getRequestNo());
		List<PayTradeEntity> list = payTradeService.queryPayTrade(map);
		String payCenterNumber = null;
		if(null != list && list.size() > 0){
			if(StringUtils.equals(dto.getPayType(), PayWayEnum.WEIXIN_APP.getWay())) {
				payCenterNumber = payTradeService.getPayCenterNumber();
			} else {
				payCenterNumber = list.get(0).getPayCenterNumber();
			}
			//删除老流水号，因为该流水号下可能存在其它订单
			payTradeService.deletePayTrade(payCenterNumber);
		} else {
			payCenterNumber = payTradeService.getPayCenterNumber();
		}

		PayTradeEntity paytrade = new PayTradeEntity();
		BeanUtils.copyProperties(dto, paytrade);
		paytrade.setPayCenterNumber(payCenterNumber);
		if(StringUtils.isEmpty(dto.getRequestNo())){
			paytrade.setPayCount(1);
		}
		
		payTradeService.addPayTradeEntity(paytrade);
		
		if(StringUtils.equals(dto.getPayType(), PayWayEnum.ALIPAY_H5.getWay())){
			//查密钥 
			//ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
			dto.setPayCenterNumber(payCenterNumber);
			pjd = alipayService.prePay(dto);
		} else if(StringUtils.equals(dto.getPayType(), PayWayEnum.WEIXIN_APP.getWay())){
			//查密钥 
			//ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
			dto.setPayCenterNumber(payCenterNumber);
			WeChatPreResultDto weChatPreResultDto= weChatService.prePay(dto);
			if(StringUtils.isNotBlank(weChatPreResultDto.getPrepay_id())) {
				pjd = new PayJumpDto();
				pjd.setPayType(dto.getPayType());
				pjd.setRespCode(MsgCons.C_10000);
				Map<String,String> params = new HashMap<String,String>();
				params.put("appid", weChatPreResultDto.getAppid());
				params.put("partnerid", weChatPreResultDto.getPrepay_id());
				params.put("prepayid", weChatPreResultDto.getPrepay_id());
				params.put("package", "Sign=WXPay");
				params.put("noncestr", StringUtil.getRandomString(16));
				params.put("timestamp", String.valueOf(new Date().getTime()));
				logger.info("返回sign前:" + params + "," + dto.getAppKey());
				String sign = weChatService.buildPaySign(params, dto.getAppKey());
				logger.info("返回sign=" + sign);
				params.put("sign", sign);
				pjd.setJsonStr(JSON.toJSONString(params));
			}
		} else {
			throw new BizException(MsgCons.C_50001, MsgCons.M_50001);
		}
		
		log.setId(id);
		log.setSend("发送给支付页面参数:"+JSON.toJSONString(pjd));
		payLogRecordService.updateLog(log);
		return pjd;
	}
	
	private PayJumpDto mutablePrePay(PayGateWayDto dto)throws BizException{
		//转成MergePayTradeRequestDto
		MergePayTradeRequestDto merge = new MergePayTradeRequestDto();
		BeanUtils.copyProperties(dto, merge);
		List<MergePayTradeRequestDto.OrderInfo> orderList = JSON.parseArray(dto.getOrderInfos(), MergePayTradeRequestDto.OrderInfo.class);
		merge.setOrderInfoList(orderList);

		String payUID = PayTradeUtil.getPayUuid(merge);
		PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(dto.getPayType(),payUID, "合并付款收到业务系统参数:"+JSON.toJSONString(dto));
		Long id = payLogRecordService.addLog(log);
		merge.setPayUid(payUID);
		String payCenterNumber = mergePayTradeService.createPayTrade(merge);
		//设置title payAmt
		dto.setTitle(orderList.get(0).getTitle());
		dto.setPayAmt(dto.getSumPayAmt());
		dto.setPayCenterNumber(payCenterNumber);
		
		
		PayJumpDto pjd = null;
		if(StringUtils.equals(dto.getPayType(), PayWayEnum.ALIPAY_H5.getWay())){
			//查密钥 
			//ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
			dto.setPayCenterNumber(payCenterNumber);
			pjd = alipayService.prePay(dto);
		} else if(StringUtils.equals(dto.getPayType(), PayWayEnum.WEIXIN_APP.getWay())){
			//查密钥 
			//ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
			dto.setPayCenterNumber(payCenterNumber);
			WeChatPreResultDto weChatPreResultDto= weChatService.prePay(dto);
			if(StringUtils.isNotBlank(weChatPreResultDto.getPrepay_id())) {
				pjd = new PayJumpDto();
				pjd.setPayType(dto.getPayType());
				pjd.setRespCode(MsgCons.C_10000);
				Map<String,String> params = new HashMap<String,String>();
				params.put("appid", weChatPreResultDto.getAppid());
				params.put("partnerid", weChatPreResultDto.getPrepay_id());
				params.put("prepayid", weChatPreResultDto.getPrepay_id());
				params.put("package", "Sign=WXPay");
				params.put("noncestr", StringUtil.getRandomString(16));
				params.put("timestamp", String.valueOf(new Date().getTime()));
				String sign = weChatService.buildPaySign(params, dto.getAppKey());
				params.put("sign", sign);
				pjd.setJsonStr(JSON.toJSONString(params));
			}
		} else {
			throw new BizException(MsgCons.C_50001, MsgCons.M_50001);
		}
		log.setId(id);
		log.setSend("发送给支付宝的参数:"+JSON.toJSONString(pjd));
		payLogRecordService.updateLog(log);
		return pjd;
	}

}
