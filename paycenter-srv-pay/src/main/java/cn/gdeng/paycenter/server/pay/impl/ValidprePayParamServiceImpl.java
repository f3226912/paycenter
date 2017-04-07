package cn.gdeng.paycenter.server.pay.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.TradeQueryService;
import cn.gdeng.paycenter.api.server.pay.ValidprePayParamService;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.dto.account.TradeQueryRequestDto;
import cn.gdeng.paycenter.dto.account.TradeQueryResponseDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.PayTradeUtil;

@Service
public class ValidprePayParamServiceImpl implements ValidprePayParamService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private TradeQueryService tradeQueryService;
	
	@Resource
	private PayTradeService payTradeService;

	public void validPrePayParam(PayTradeEntity dto) throws BizException {
		if(PayWayEnum.WEIXIN_APP.getWay().equals(dto.getPayType()) || 
				PayWayEnum.ALIPAY_H5.getWay().equals(dto.getPayType())){
			validAliPrePayParam(dto);
		}

	}

	/**
	 * 每个字段逐一校验,这段校验只适合支付宝手机网站支付2.0版本
	 * 长度较验验证自己数据库的，与支付宝的，取最小值
	 * @param dto
	 * @throws BizException
	 */
	private void validAliPrePayParam(PayTradeEntity dto) throws BizException {
		//校验订单号长度 支付中心20位，支付宝64位 取20位
		validateNotNull(dto.getOrderNo(),"订单号不能为空");
		validateLength(dto.getOrderNo(),20,"订单号不能超过20位");
		//校验商品名称 支付中心 255，支付宝256位 取255
		validateNotNull(dto.getTitle(),"订单名称不能为空");
		validateLength(dto.getTitle(),255,"订单名称不能超过255位");
		//超时时间
		validateNumber(dto.getTimeOut(),"超时时间不是一个数字");
		// 系统来源不能为空 appKey
		validateNotNull(dto.getAppKey(),"系统来源不能为空");
		validateLength(dto.getAppKey(),20,"系统来源不能超过20位");
		//校验公用回传
		validateLength(dto.getReParam(),255,"公用回传不能超过255位");
		
		// 校验 return_url长度(256) 支付中心255
		validateNotNull(dto.getReturnUrl(),"returnUrl不能为空");
		validateLength(dto.getReturnUrl(),255,"returnUrl不能超过255位");
		
		// 校验 notify_url长度(256 ) 支付中心255
		validateLength(dto.getNotifyUrl(),255,"notifyUrl不能超过255位");
		
		//校验 订单金额 2位小数
		validateNotNull(dto.getPayAmt(),"交易金额不能为空");
		PayTradeUtil.validateDecialmal(dto.getPayAmt());
		//支付方式不能为空
		validateNotNull(dto.getPayType(),"支付方式不能为空");
		
		//请求IP，32位
		validateLength(dto.getRequestIp(),32,"请求IP不能超过32位");
		validReadPay(dto);

		// 校验卖家会员ID，买家会员ID，卖家会员名称，买家会员名称

	}
	
	@Override
	public void validReadPay(PayTradeEntity payTrade) throws BizException {
		Map<String,Object> param = new HashMap<>();
		param.put("orderNo", payTrade.getOrderNo());
		param.put("appKey", payTrade.getAppKey());

		List<PayTradeEntity> list = payTradeService.queryPayTrade(param);//全额付款与预付款和尾款不能同时存在
		PayTradeEntity pa = null;
		if(list == null || list.size() == 0){
			return;
		} else if(list.size() > 0){
			for(PayTradeEntity ent : list){
				if(StringUtils.isEmpty(ent.getRequestNo()) && StringUtils.isNotEmpty(payTrade.getRequestNo())){
					throw new BizException(MsgCons.C_20001,"该订单["+payTrade.getOrderNo()+"]已存在全额付款记录");
				} else if(StringUtils.isEmpty(payTrade.getRequestNo()) && StringUtils.isNotEmpty(ent.getRequestNo())){
					throw new BizException(MsgCons.C_20001,"该订单["+payTrade.getOrderNo()+"]已存在预付款或者尾款记录");
				} else if(StringUtils.equals(payTrade.getRequestNo(), ent.getRequestNo())){
					pa = ent;
				} else {
					return;
				}
			}
		}

		if(pa == null){
			throw new BizException(MsgCons.C_20001,"该订单["+payTrade.getOrderNo()+"]存在异常记录， 请联系管理员");
		}
		if(!PayStatus.READY_PAY.equals(pa.getPayStatus())){
			logger.info("该订单["+payTrade.getOrderNo()+"]不是待付款状态");
			throw new BizException(MsgCons.C_20000, "该订单["+payTrade.getOrderNo()+"]不是待付款状态");
		}else {
			// 如果存在订单，且是未支付状态，需要调用第三方接口查询订单状态
			//查询是否已支付
			if(PayWayEnum.ALIPAY_H5.getWay().equals(payTrade.getPayType())){
				TradeQueryRequestDto req = new TradeQueryRequestDto();
				req.setAppKey(payTrade.getAppKey());
				req.setPayCenterNumber(pa.getPayCenterNumber());
				TradeQueryResponseDto res = tradeQueryService.queryTrade(req);
				if(res!=null){
					if(PayStatus.PAID.equals(res.getPayStatus()) || PayStatus.REFUND.equals(res.getPayStatus())){
						logger.info("该订单["+payTrade.getOrderNo()+"]已经付款");
						throw new BizException(MsgCons.C_20032, "该订单["+payTrade.getOrderNo()+"]已经付款");
					}else if(PayStatus.CLOSED.equals(res.getPayStatus())){
						logger.info("该订单["+payTrade.getOrderNo()+"]已经关闭");
						throw new BizException(MsgCons.C_20033, "该订单["+payTrade.getOrderNo()+"]已经关闭");
					}
				}
			}
		}


		
	}
	
	/**
	 * 校验时间，统一格式yyyy-MM-dd HH:mm:ss
	 */
	private void validateTime(String time,String msg)throws BizException{
		if(StringUtils.isEmpty(time)){
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			sdf.parse(time);
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20001, msg);
		}
	}
	
	private void validateNotNull(Object str,String msg) throws BizException{
		if(str == null){
			throw new BizException(MsgCons.C_20001, msg);
		}
	}
	
	private void validateLength(String str,int length,String msg) throws BizException{
		if(str == null)return;
		byte[] bs = null;
		try {
			bs = str.getBytes("GBK");
			
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20027, "GBK编码不存在");
		}
		if(bs.length>length){
			throw new BizException(MsgCons.C_20001, msg);
		}

	}
	
	private void validateNumber(String str,String msg)throws BizException{
		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20001, msg);
		}
	}

	

}
