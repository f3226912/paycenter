package cn.gdeng.paycenter.gateway.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.AlipayService;
import cn.gdeng.paycenter.api.server.pay.PayCallBackService;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.constant.AlipayTradeStatus;
import cn.gdeng.paycenter.dto.pay.PayNotifyResponse;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.gateway.base.BaseController;
import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;
import cn.gdeng.paycenter.util.web.api.Base64;
import cn.gdeng.paycenter.util.web.api.PayLogUtil;

/**
 * 处理支付宝的通知
 * @author sss
 *
 */
@Controller
@RequestMapping("alipay")
public class AlipayNotifyController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private AlipayService alipayService;
	
	@Reference
	private PayCallBackService payCallBackService;
	
	@Reference
	private AccessSysConfigService accessSysConfigService;
	
	@Reference
	private PayTradeService payTradeService;
	
	@Reference
	private PayLogRecordService payLogRecordService;
	
	private String REFUND_STATUS = "REFUND_SUCCESS";
	
	/**
	 * 同步回调入口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("returnUrl")
	public ModelAndView returnUrl(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();
		//获取支付宝GET过来反馈信息
		Map<String,String> params = buildSignParam(request);
		String payCenterNumber = params.get("out_trade_no");		
		logger.info("进入支付宝同步回调接口：平台流水号【" + payCenterNumber +"】;支付宝同步通知入参:"+JSON.toJSONString(params));	
		PayNotifyResponse res = new PayNotifyResponse();
		//根据平台流水号查订单号
	
		//res.setOrderNo(orderNo);
		AccessSysConfigEntity accessSysConfig = new AccessSysConfigEntity();
		try {
			//交易成功，才做处理
			//获取交易状态，交易结束不做处理
			//记录日志				
			PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(PayWayEnum.ALIPAY_H5.getWay(),payCenterNumber, "收到支付宝同步通知:"+JSON.toJSONString(params));
			Long id = payLogRecordService.addLog(log);		
			List<PayTradeEntity> payList = payTradeService.queryPayTradeByPayCenterNumber(payCenterNumber);
			accessSysConfig = accessSysConfigService.queryByAppKey(payList.get(0).getAppKey());
			res = payCallBackService.payReturnCallBack(payList,params,PayWayEnum.ALIPAY_H5.getWay(),payCenterNumber);
			//记录日志
			if(id != 0){
				log.setSend("返回给H5参数:"+JSON.toJSONString(res));
				log.setId(id);
				payLogRecordService.updateLog(log);
			}
		} catch (BizException e) {
			res.setRespCode(e.getCode());
			res.setRespMsg(e.getMsg());
			logger.error("支付宝同步回调出错：错误编码【" + e.getCode() + "】错误消息【" + e.getMsg() + "】");
		} catch(Exception e){
			res.setRespCode(MsgCons.C_20000);
			res.setRespMsg("未知异常"+e.getMessage());
			logger.error("支付宝同步回调出错：发生未知异常:【" + e.getMessage() + "】");
		}
		
		logger.info("支付宝同步通知返回参数:"+JSON.toJSONString(res));
		if(MsgCons.C_10000.equals(res.getRespCode())){
			mv.setViewName("paySuccess");//成功页面
			logger.info("支付宝同步通知返回成功页面");
		}else{
			//是否要再次查出payTrade,返回到支付失败页面
			//todo			
			res = payCallBackService.joinPayNotifyResponse(res,payCenterNumber);
			mv.setViewName("payFailure");//失败页面
			logger.info("支付宝同步通知返回失败页面");
		}			
		//返回		
		Map<String,String> map = transRes2Map(res,accessSysConfig);		
		mv.addAllObjects(map);		
		logger.info("结束支付宝同步回调接口：订单号【" + res.getOrderNo() +"】,平台支付流水["+res.getPayCenterNumber()+"]");
		return mv;
	}		

	private Map<String,String> transRes2Map(PayNotifyResponse res,AccessSysConfigEntity accessSysConfig){		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("version", res.getVersion()!=null?String.valueOf(res.getVersion()):"");
		paramMap.put("appKey", res.getAppKey());
		paramMap.put("orderNo", res.getOrderNo());
		paramMap.put("title", res.getTitle());
		paramMap.put("payCenterNumber", res.getPayCenterNumber());
		paramMap.put("thirdPayNumber", res.getThirdPayNumber());
		paramMap.put("payAmt", res.getPayAmt()+"");
		paramMap.put("reParam", res.getReParam());
		paramMap.put("payType", res.getPayType());
		paramMap.put("timeOut", res.getTimeOut());
		paramMap.put("keyType", accessSysConfig.getKeyType());
		paramMap.put("payStatus", res.getPayStatus());
		paramMap.put("payerUserId", res.getPayerUserId());
		paramMap.put("payerAccount", res.getPayerAccount());
		paramMap.put("payerName", res.getPayerName());
		paramMap.put("payeeUserId", res.getPayerUserId());
		paramMap.put("payeeAccount", res.getPayeeAccount());
		paramMap.put("payeeName", res.getPayeeName());
		paramMap.put("totalAmt", res.getTotalAmt()+"");
		paramMap.put("respCode", res.getRespCode()!=null?String.valueOf(res.getRespCode()):"");
		paramMap.put("respMsg", res.getRespMsg());
		paramMap.put("returnUrl", res.getReturnUrl());
		paramMap.put("notifyUrl", res.getNotifyUrl());
		paramMap.put("payerMobile", res.getPayerMobile());
		paramMap.put("payeeMobile", res.getPayeeMobile());
		String oldSign = AccessSysSignUtil.sign(paramMap, accessSysConfig.getKeyType(),
				accessSysConfig.getPrivateKey());
		String sign = Base64.encode(oldSign.getBytes());
		logger.info("签名:" + sign);
		paramMap.put("sign", sign);		
		return paramMap;
	}	
	
	/**
	 * 异步回调入口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("notifyUrl")
	public void notifyUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取支付宝POST过来反馈信息
		PayNotifyResponse res = new PayNotifyResponse();
		Map<String,String> params = buildSignParam(request);
		String payCenterNumber = params.get("out_trade_no");
		String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		logger.info("进入支付宝异步回调接口：平台支付流水【" + payCenterNumber +"】-支付宝支付状态【" + tradeStatus + "】;支付宝异步入参:"+JSON.toJSONString(params));	
		try {
			
			//如果是退款
			String refundStatus = request.getParameter("refund_status");
			PayLogRecordEntity log = null;
			Long id = null;
			if(StringUtils.isNotEmpty(refundStatus)){
				//申请退款接口，也会发异步通知
				log = PayLogUtil.buildPayReceiveLog(PayWayEnum.ALIPAY_H5.getWay(),payCenterNumber, "收到支付宝退款异步通知:"+JSON.toJSONString(params));
				id = payLogRecordService.addLog(log);
			} else {
				//获取交易状态，交易结束不做处理 
				log = PayLogUtil.buildPayReceiveLog(PayWayEnum.ALIPAY_H5.getWay(),payCenterNumber, "收到支付宝交易异步通知:"+JSON.toJSONString(params));
				id = payLogRecordService.addLog(log);
				if(StringUtils.equals(tradeStatus, AlipayTradeStatus.TRADE_SUCCESS)){	
					res = payCallBackService.payNotify(params,PayWayEnum.ALIPAY_H5.getWay(),payCenterNumber);
				}
			}
			
			if(id != 0){
				log.setSend("返回给支付宝:success");
				log.setId(id);
				payLogRecordService.updateLog(log);
			}
		} catch(Exception e){
			logger.info("支付宝异步通知返回fail,错误信息:"+e.getMessage(),e);
			response.getWriter().write("fail");
			return;
		}
		logger.info("结束支付宝异步回调接口：订单号【" + res!=null?res.getOrderNo():"" +"】,平台支付流水【"+payCenterNumber+"】支付宝异步通知返回success");
		response.getWriter().write("success");
		return;
	}
	
	
	@SuppressWarnings("rawtypes")
	private Map<String,String> buildSignParam(HttpServletRequest request) throws UnsupportedEncodingException{
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}

}
