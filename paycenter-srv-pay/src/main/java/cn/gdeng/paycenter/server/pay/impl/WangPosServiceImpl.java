package cn.gdeng.paycenter.server.pay.impl;

import java.math.BigDecimal;
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
import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.MergePayTradeService;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.WangPosService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PayTradeDto;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.dto.pos.WangPosPayNotifyDto;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeExtendEntity;
import cn.gdeng.paycenter.entity.pay.PayTradePosEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;
import cn.gdeng.paycenter.util.web.api.PayLogUtil;

@Service
public class WangPosServiceImpl implements WangPosService {
	
	@Resource
	private PayLogRecordService payLogRecordService;
	
	@Resource
	private MergePayTradeService mergePayTradeService;

	@Resource
	private PayTradeService payTradeService;
	
	@Resource
	private AccessSysConfigService accessSysConfigService;
	
	@Resource
	private SendMessageUtil sendMessage;
	
	@Resource
	private BaseDao<?> baseDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String createPayTrade(MergePayTradeRequestDto dto) throws BizException {
		
		String payUID = PayTradeUtil.getPayUuid(dto);
		PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(dto.getPayType(),payUID, "收到业务系统(旺POS)参数:"+JSON.toJSONString(dto));
		Long id = payLogRecordService.addLog(log);
		dto.setPayUid(payUID);
		String payCenterNumber = mergePayTradeService.createPayTrade(dto);
		log.setId(id);
		log.setSend("发送给WANGPOS的参数:"+payCenterNumber);
		payLogRecordService.updateLog(log);
		return payCenterNumber;
	}


	@Override
	public void payNotify(WangPosPayNotifyDto dto) throws BizException {
		//记录日志

		String payCenterNumber = dto.getPayCenterNumber();
		if(StringUtils.isEmpty(payCenterNumber)){
			throw new BizException(MsgCons.C_20001, "平台支付流水号不能为空");
		}
		PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(PayWayEnum.WANGPOS.getWay(),payCenterNumber, "收到旺POS支付通知:"+JSON.toJSONString(dto));
		Long id = payLogRecordService.addLog(log);
		
		//查询出支付流	
		List<PayTradeEntity> oldPay = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);
		if(oldPay == null || oldPay.size() == 0){
			throw new BizException(MsgCons.C_20001, "平台支付流水号["+payCenterNumber+"]不存在");
		}

		BigDecimal big = new BigDecimal(0);
		String appKey = oldPay.get(0).getAppKey();
		for(PayTradeEntity old : oldPay){
			big = big.add(new BigDecimal(old.getPayAmt()));
		}
		big = big.setScale(2, BigDecimal.ROUND_HALF_UP);//解决传double可能出现的问题  115.74 + 135.09
		if(dto.getTradeAmt() != big.doubleValue()){
			throw new BizException(MsgCons.C_20001,"交易金额["+dto.getTradeAmt()+"]与订单交易金额之和不一致");
		}
		PayTradeEntity newPay = new PayTradeEntity();
		newPay.setPayCenterNumber(payCenterNumber);
		newPay.setPayStatus(PayStatus.PAID);
		newPay.setThirdPayNumber(dto.getTradeNo());
		newPay.setPayTime(dto.getTradeTime());
		
		
		//获取第三方收款方帐号
		Map<String,Object> params = new HashMap<>();
		params.put("payType", PayWayEnum.WANGPOS.getWay());
		PayTypeEntity entity = baseDao.queryForObject("PayType.queryByCondition", params, PayTypeEntity.class);
		Assert.notNull(entity, "支付方式["+PayWayEnum.WANGPOS.getWay()+"]不存在");
		newPay.setThirdPayeeAccount(entity.getGdBankCardNo());
		
		if("1006".equals(dto.getPayType())){
			//银行卡支付写交易POS表
			//写交易PoS表
			insertPayTradePos(dto,appKey);
		} else if("1001".equals(dto.getPayType())){
			//写死
			//写交易PoS表
			dto.setPosClientNo("12345678");
			dto.setBuyer("61111234567890000");
			insertPayTradePos(dto,appKey);
		}
		newPay.setThirdPayerAccount(dto.getBuyer());

		if(!dto.isSendMq()){
			payTradeService.updatePayTrade(newPay);
			logger.info("WANGPOS通知不需要发送MQ,直接返回");
		} else {
			//待通知
			newPay.setNotifyStatus(NotifyStatus.READ_NOTIFY);
			payTradeService.updatePayTrade(newPay);
			oldPay = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);
			for(PayTradeEntity old : oldPay){
				//发送MQ
				final PayTradeDto newTrade = new PayTradeDto();
				final PayTradeEntity finalEntity = new PayTradeEntity();
				finalEntity.setId(old.getId());
				try {
					BeanUtils.copyProperties(old, newTrade);
					newTrade.setBankCardNo(dto.getBuyer());
					newTrade.setPosClientNo(dto.getPosClientNo());
					
					AccessSysConfigEntity accessSysConfig = accessSysConfigService.queryByAppKey(newTrade.getAppKey());
					logger.info("旺POS发送支付成功MQ："+accessSysConfig.getMqTopic());
					sendMessage.sendAsyncMessage(accessSysConfig.getMqTopic(), "TAGS",payCenterNumber, newTrade, new SendCallback() {
						
						@Override
						public void onSuccess(SendResult sendResult) {
							// TODO Auto-generated method stub
							finalEntity.setNotifyStatus(NotifyStatus.NOTIFIED);
							baseDao.dynamicMerge(finalEntity);
							logger.info("旺POS异步通知发送MQ成功");
						}
						
						@Override
						public void onException(OnExceptionContext context) {
							logger.info("旺POS异步通知发送MQ失败");
						}
					});
					
				} catch (Exception e) {

					logger.info("旺POS异步通知发送MQ失败,"+e.getMessage(),e);
				}
			}
		}

		if(id != 0){
			log.setSend("返回给旺POS:success");
			log.setId(id);
			payLogRecordService.updateLog(log);
		}
	}
	
	private void insertPayTradePos(WangPosPayNotifyDto dto,String appKey){
		//getPayTradePos payTradePos是否已存在
		Map<String,Object> param1 = new HashMap<>();
		param1.put("payCenterNumber", dto.getPayCenterNumber());
		PayTradePosEntity p1 = baseDao.queryForObject("PayTrade.getPayTradePos", param1, PayTradePosEntity.class);
		if(p1 == null){
			PayTradePosEntity posEntity = new PayTradePosEntity();
			posEntity.setPayCenterNumber(dto.getPayCenterNumber());
			posEntity.setPosClientNo(dto.getPosClientNo());
			posEntity.setPayChannelCode(PayWayEnum.WANGPOS.getWay());
			posEntity.setBankCardNo(dto.getBuyer()); 
			baseDao.execute("PayTrade.insertPayTradePos", posEntity);
		}

		//获取version
		Map<String,Object> param2 = new HashMap<>();
		param2.put("machineNum", dto.getPosClientNo());
		String ids = baseDao.queryForObject("PayTrade.getMaxIdByPosCientNo", param2, String.class);
		//更新version
		Map<String,Object> param3 = new HashMap<>();
		param3.put("payCenterNumber", dto.getPayCenterNumber());
		param3.put("version", ids);
		param3.put("appKey", appKey);
		baseDao.execute("PayTrade.updatePayTradeExtend", param3);
	}

	
}
