package cn.gdeng.paycenter.server.pay.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.order.OrderbaseInfoHessianService;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.PosPayNotifyService;
import cn.gdeng.paycenter.api.server.pay.PosPayService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.RequestNo;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.NanningPayNotifyDto;
import cn.gdeng.paycenter.dto.pay.PayTradeDto;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.dto.pos.OrderBaseinfoHessianDto;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeExtendEntity;
import cn.gdeng.paycenter.entity.pay.PayTradePosEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.entity.pay.PosPayNotifyEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PosTranstype;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;

@Service
public class PosPayServiceImpl implements PosPayService{
	
	@Resource
	private PayTradeService payTradeService;
	
	@Resource
	private OrderbaseInfoHessianService orderbaseInfoHessianService;
	
	@Resource
	private PayLogRecordService payLogRecordService;
	
	@Resource
	private AccessSysConfigService accessSysConfigService;
	
	@Resource
	private SendMessageUtil sendMessage;
	
	@Resource
	private GdProperties gdProperties;
	
	@Resource
	private PosPayNotifyService posPayNotifyService;
	
	private String payNsyCreateTopic;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private BaseDao<?> baseDao;
	
	/**
	 * 获取订单列表
	 */
	public List<Map<String, String>> nanningOrderList(NanningPayNotifyDto dto) throws Exception {
		List<Map<String, String>> eNongOrderList = new ArrayList<>();
		//校验POS号是否存在
		Map<String,Object> params = new HashMap<>();
		params.put("posClientNo", dto.getMachinenum());
		Integer businessId = baseDao.queryForObject("PayTrade.getPosMachineConfig", params, Integer.class);
		if(businessId == null){
			logger.info("POS终端号["+ dto.getMachinenum()+"]不存在");
		} else {
			Map<String,Object> map = new HashMap<>();
			map.put("businessId", businessId);
			eNongOrderList = orderbaseInfoHessianService.getUnpaidOrderList(map);
		}
		return eNongOrderList;
	}

	@Override
	@Transactional
	public void payNotify(PosPayNotifyDto dto) throws BizException{

		//校验POS号是否存在
		Map<String,Object> params = new HashMap<>();
		params.put("posClientNo", dto.getPosClientNo());
		int count = baseDao.queryForObject("PayTrade.getPosMachineConfigCount", params, Integer.class);
		if(count == 0){
			logger.info("POS终端号["+ dto.getPosClientNo()+"]不存在");
			return;
		}
		
		//校验是否重复通知
		
		if(StringUtils.isNotEmpty(dto.getOrderNo())){
			params.clear();
			params.put("orderNo", dto.getOrderNo());
			//订单号已存在直接忽略
			List<PosPayNotifyDto> list = posPayNotifyService.queryByCondition(params);
			if(list != null && list.size() > 0){
				return;
			}
			
		}
		params.clear();
		params.put("transNo", dto.getTransNo());
		params.put("payChannelCode", dto.getPayChannelCode());
		//支付流水已存在，直接忽略
		List<PosPayNotifyDto> list = posPayNotifyService.queryByCondition(params);
		if(list != null && list.size() > 0){
			return;
		}

		if(PosTranstype.Nanning.POSITIVE.equals(dto.getTransType())){
			handlePositive(dto);
		} else {
			handleReverse(dto);
		}

	}
	

	public void handlePositive(PosPayNotifyDto dto) throws BizException{

		String payCenterNumber = payTradeService.getPayCenterNumber();
		dto.setStatus(NotifyStatus.READ_NOTIFY); //POS_PAY_NOTIFY的任务处理
		dto.setPayCenterNumber(payCenterNumber);
		posPayNotifyService.addPosPayNotify(dto);
		
		dto.setPayCenterNumber(payCenterNumber);
		dto.setNotifyStatus(NotifyStatus.READ_NOTIFY); //PAY_TRADE的任务处理
		PayTradeDto writeDto = addPayTrade(dto); // 添加到数据库
		//发送MQ
		final PayTradeEntity newTrade = new PayTradeEntity();
		try {
			String appKey = dto.getAppKey();
			newTrade.setPayCenterNumber(payCenterNumber);
			writeDto.setPosClientNo(dto.getPosClientNo());
			writeDto.setBankCardNo(dto.getBankCardNo());
			AccessSysConfigEntity accessSysConfig = accessSysConfigService.queryByAppKey(appKey);
			logger.info("POS刷卡通知开始发送MQ："+accessSysConfig.getMqTopic());
			sendMessage.sendAsyncMessage(accessSysConfig.getMqTopic(), "TAGS",writeDto.getPayCenterNumber(), writeDto, new SendCallback() {
				
				@Override
				public void onSuccess(SendResult sendResult) {
					// TODO Auto-generated method stub
					newTrade.setNotifyStatus(NotifyStatus.NOTIFIED);
					payTradeService.updatePayTrade(newTrade);
					logger.info("POS刷卡发送MQ成功");
				}
				
				@Override
				public void onException(OnExceptionContext context) {

					logger.info("POS刷卡通知发送MQ失败");
				}
			});
			
		} catch (Exception e) {
			//记录日志
			logger.info("POS刷卡通知发送MQ失败,"+e.getMessage(),e);
		}

	}

	public void handleReverse(PosPayNotifyDto dto) throws BizException{

		//没有订单号
		String payCenterNumber = payTradeService.getPayCenterNumber();
		dto.setStatus(NotifyStatus.READ_NOTIFY); //POS_PAY_NOTIFY 任务处理
		dto.setPayCenterNumber(payCenterNumber);
		dto.setOrderAmt(dto.getPayAmt());
		int id = posPayNotifyService.addPosPayNotify(dto);
		//发送MQ
		final PosPayNotifyEntity newNotify = new PosPayNotifyEntity();
		try {
			
			newNotify.setId(id);
			String topic = getPayNsyCreateTopic();
			//设置gdBankCardNo
			//获取第三方收款方帐号
			Map<String,Object> params = new HashMap<>();
			params.put("payType", dto.getPayChannelCode());
			PayTypeEntity entity = baseDao.queryForObject("PayType.queryByCondition", params, PayTypeEntity.class);
			Assert.notNull(entity, "支付方式["+dto.getPayChannelCode()+"]不存在");
			dto.setGdBankCardNo(entity.getGdBankCardNo());
			logger.info("POS刷卡通知(反向)开始发送MQ："+ topic);
			sendMessage.sendAsyncMessage(topic, "TAGS",dto.getPayCenterNumber(), dto, new SendCallback() {
				
				@Override
				public void onSuccess(SendResult sendResult) {
					// TODO Auto-generated method stub
					newNotify.setStatus(NotifyStatus.NOTIFIED);
					posPayNotifyService.dynamicUpdatePosPayNotify(newNotify);
					logger.info("POS刷卡(反向)发送MQ成功");
				}
				
				@Override
				public void onException(OnExceptionContext context) {

					logger.info("POS刷卡(反向)通知发送MQ失败");
				}
			});
			
		} catch (Exception e) {
			//记录日志
			logger.info("POS刷卡(反向)通知发送MQ失败,"+e.getMessage(),e);
		}

	}

	
	private PayTradeDto addPayTrade(PosPayNotifyDto dto) throws BizException{
		String payCenterNumber = dto.getPayCenterNumber();
		Assert.notNull(payCenterNumber,"支付流水号不能为空");
		//获取第三方收款方帐号
		Map<String,Object> params = new HashMap<>();
		params.put("payType", dto.getPayChannelCode());
		PayTypeEntity entity = baseDao.queryForObject("PayType.queryByCondition", params, PayTypeEntity.class);
		Assert.notNull(entity, "支付方式["+dto.getPayChannelCode()+"]不存在");
		
		//构建PayTrade交易对象
		PayTradeEntity trade = new PayTradeEntity();
		
		OrderBaseinfoHessianDto orderBaseDto = orderbaseInfoHessianService.getByOrderNo(dto.getOrderNo());
		BeanUtils.copyProperties(orderBaseDto, trade);
		trade.setVersion(1);
		trade.setAppKey(dto.getAppKey());//农商友
		if(StringUtils.equals(PosTranstype.Nanning.POSITIVE, dto.getTransType())){
			trade.setTitle(orderbaseInfoHessianService.getProductName(dto.getOrderNo()));
		} else {
			trade.setTitle("农商友");
		}
		
		trade.setFeeAmt(dto.getRateAmt());
		trade.setNotifyStatus(dto.getNotifyStatus());
		trade.setOrderNo(dto.getOrderNo());
		trade.setPayCenterNumber(payCenterNumber);
		trade.setThirdPayNumber(dto.getTransNo());
		trade.setPayAmt(dto.getPayAmt());
		trade.setTotalAmt(dto.getOrderAmt());
		trade.setPayTime(dto.getTransDate());
		trade.setThirdPayerAccount(dto.getBankCardNo());
		trade.setThirdPayeeAccount(entity.getGdBankCardNo());
		trade.setPayType(dto.getPayChannelCode());
		trade.setPayStatus(PayStatus.PAID);
		trade.setCreateTime(new Date());
		trade.setUpdateTime(new Date());
		trade.setCreateUserId("sys");
		trade.setUpdateUserId("sys");
		trade.setPayCount(1);
		trade.setRequestNo(RequestNo.FULL_PAY); //全款
		
		//写交易扩展表
		PayTradeExtendEntity extend = new PayTradeExtendEntity();
		extend.setOrderNo(trade.getOrderNo());
		extend.setMarketId(orderBaseDto.getMarketId());
		extend.setBusinessId(orderBaseDto.getBusinessId());
		extend.setOrderType(orderBaseDto.getOrderType());
		extend.setBusinessName(orderBaseDto.getShopName());
		extend.setMarketName(orderBaseDto.getMarketName());
		extend.setVersion(orderBaseDto.getValidPosNum());
		
		//写交易PoS表
		PayTradePosEntity posEntity = new PayTradePosEntity();
		posEntity.setPayCenterNumber(payCenterNumber);
		posEntity.setPosClientNo(dto.getPosClientNo());
		posEntity.setPayChannelCode(dto.getPayChannelCode());
		posEntity.setBankCardNo(dto.getBankCardNo()); 
		PayTradeDto writeDto = new PayTradeDto();
		BeanUtils.copyProperties(trade, writeDto);
		writeDto.setPayTradeExtendEntity(extend);
		writeDto.setPayTradePosEntity(posEntity);
		payTradeService.addPayTrade(writeDto);
		return writeDto;
	}
	
	private String getPayNsyCreateTopic(){
		if(null == payNsyCreateTopic){
			payNsyCreateTopic = gdProperties.getProperties().getProperty("payNsyCreateTopic");
		}
		return payNsyCreateTopic;
		
	}

	@Override
	@Transactional
	public void orderSync(String payCenterNumber,String orderNo) throws BizException {

		//获取PosPayNotifyDto
		Map<String,Object> params = new HashMap<>();
		params.put("payCenterNumber", payCenterNumber);
		//支付流水已存在，直接忽略
		List<PayTradeEntity> plist  = payTradeService.queryPayTrade(params);
		if(plist != null && plist.size() > 0){
			return;
		}

		List<PosPayNotifyDto> list = posPayNotifyService.queryByCondition(params);
		if(list == null || list.size() == 0){
			throw new BizException(MsgCons.C_20000, "支付流水["+payCenterNumber+"]在POS_PAY_NOTIFY表中不存在");
		}
		PosPayNotifyDto posPay = list.get(0);
		posPay.setNotifyStatus(NotifyStatus.NOTIFIED);//不需要通知
		posPay.setOrderNo(orderNo);
		addPayTrade(posPay);
	}

}
