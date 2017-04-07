package cn.gdeng.paycenter.gateway.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.WangPosService;
import cn.gdeng.paycenter.constant.RequestNo;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.dto.pos.WangPosAsyncNotifyDto;
import cn.gdeng.paycenter.dto.pos.WangPosOrderListRequestDto;
import cn.gdeng.paycenter.dto.pos.WangPosPayNotifyDto;
import cn.gdeng.paycenter.dto.pos.WangPosResultDto;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import cn.gdeng.paycenter.util.web.api.Assert;
import cn.gdeng.paycenter.util.web.api.Des3Request;
import cn.gdeng.paycenter.util.web.api.Des3Response;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.MD5;
import cn.gdeng.paycenter.util.web.api.PayLogUtil;

/**
 * 
 * @author sss
 *
 * since:2016年12月7日
 * version 1.0.0
 */
@RequestMapping("wangPos")
@Controller
public class WangPosController {
	
	@Reference
	private WangPosService wangPosService;
	
	@Resource
	private GdProperties gdProperties;
	
	@Reference
	private PayLogRecordService payLogRecordService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private ThirdPayConfigService thirdPayConfigService;


	@RequestMapping("orderList")
	public void orderList(HttpServletRequest req,HttpServletResponse response) {
		ApiResult<WangPosResultDto> res = new ApiResult<>();
		String param = req.getParameter("param");
		logger.info("WANGPOS查询尾款列表入参,param="+param);
		try {
			WangPosOrderListRequestDto dto = decrypt3des(param,WangPosOrderListRequestDto.class);
			Assert.notEmpty(dto.getBusinessId(), "businessId不能为空");
			
		} catch (Exception e) {
			handelException(res, e);
		}
		String json = JSON.toJSONString(res);
		logger.info("WANGPOS查询尾款列表出参:"+json);
		setResut(json,response);
	}
	
	@RequestMapping("create")
	public void create(HttpServletRequest req,HttpServletResponse response){
		ApiResult<Map<String,String>> res = new ApiResult<>();
		String param = req.getParameter("param");
		logger.info("WANGPOS交易预创建入参,param="+param);
		try {
			MergePayTradeRequestDto dto = decrypt3des(param,MergePayTradeRequestDto.class);
			List<MergePayTradeRequestDto.OrderInfo> orderList = JSON.parseArray(dto.getOrderInfos(), MergePayTradeRequestDto.OrderInfo.class);
			dto.setOrderInfoList(orderList);
			//校验入参
			validateCreateDto(dto);
			dto.setPayType(PayWayEnum.WANGPOS.getWay());
			String payCenterNumber = wangPosService.createPayTrade(dto);
			Map<String,String> result= new HashMap<>();
			result.put("payCenterNumber", payCenterNumber);
			result.put("notifyUrl", getNotifyUrl());
			res.setResult(result);
		} catch (Exception e) {
			handelException(res, e);
		}
		String json = JSON.toJSONString(res);
		logger.info("WANGPOS交易预创建出参:"+json);
		setResut(json,response);
	}
	
	@RequestMapping("payNotify")
	public void payNotify(HttpServletRequest req,HttpServletResponse response){
		ApiResult<String> res = new ApiResult<>();
		String param = req.getParameter("param");
		logger.info("WANGPOS交易成功通知入参,param="+param);
		try {
			WangPosPayNotifyDto dto = decrypt3des(param,WangPosPayNotifyDto.class);
			//去掉支付时间
			dto.setTradeTime(null);
			if("PAY".equals(dto.getTradeStatus())){
				dto.setSendMq(false);
				wangPosService.payNotify(dto);
			}
		} catch (Exception e) {
			handelException(res, e);
		}
		String json = JSON.toJSONString(res);
		logger.info("WANGPOS交易成功出参:"+json);
		setResut(json,response);
	}
	
	@RequestMapping("asyncPayNotify")
	public void asyncPayNotify(WangPosAsyncNotifyDto dto,HttpServletRequest request,HttpServletResponse response) throws IOException  {
		Map<String,String> param = getAllParam(request);
		logger.info("WANGPOS异步通知入参:"+JSON.toJSONString(param));
		//记录日志 以银行流水号，为基准记录日志
		PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(PayWayEnum.WANGPOS.getWay(),dto.getOut_trade_no(), "收到旺POS异步支付通知: "+JSON.toJSONString(param));
		long id = payLogRecordService.addLog(log);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PayLogRecordEntity send = new PayLogRecordEntity();
		send.setId(id);
		
		try {
			String key = thirdPayConfigService.queryByTypeSub("nsy", PayWayEnum.WANGPOS.getWay(), "").getMd5Key();
			if(!verify(param,key)){
				logger.error("MD5验签失败");
				send.setSend("WANGPOS异步通知处理结果:MD5验签失败,key="+key);
				payLogRecordService.updateLog(send);
				response.getWriter().write("false");
				return;
			}
			if("PAY".equals(dto.getTrade_status())){
				//转成WangPosPayNotifyDto
				WangPosPayNotifyDto newDto = new WangPosPayNotifyDto();
				newDto.setBuyer(dto.getBuyer());
				newDto.setPayCenterNumber(dto.getOut_trade_no());
				newDto.setPayType(dto.getPay_type());
				newDto.setPosClientNo(dto.getTerminalNo());
				newDto.setTradeStatus(dto.getTrade_status());
				newDto.setTradeNo(dto.getCashier_trade_no());
				String tradeAmt = dto.getPay_fee();
				
				newDto.setTradeAmt((new BigDecimal(tradeAmt).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP)).doubleValue());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				newDto.setTradeTime(sdf.parse(dto.getTime_end()));
				newDto.setSendMq(true);
				wangPosService.payNotify(newDto);
				
			}
			
			logger.info("处理旺POS异步通知成功");
			send.setSend("WANGPOS异步通知处理结果:success");
			payLogRecordService.updateLog(send);
			response.getWriter().write("success");
		} catch (Exception e) {
			logger.info("处理旺POS异步通知失败",e);
			send.setSend("WANGPOS异步通知处理结果:处理失败");
			payLogRecordService.updateLog(send);
			response.getWriter().write("false");
			
		}
		
	}
	
	@RequestMapping("checkValid")
	public void checkValid(HttpServletRequest req,HttpServletResponse response){
		String signature = req.getParameter("signature");
		logger.info("signature=" + signature);
		String event = req.getParameter("event");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echoStr = req.getParameter("echo_str");
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("echo_str=");
		strbuff.append(echoStr);
		strbuff.append("&event=");
		strbuff.append(event);
		strbuff.append("&nonce=");
		strbuff.append(nonce);
		strbuff.append("&timestamp=");
		strbuff.append(timestamp);
		strbuff.append("&token=");
		strbuff.append("7c4a8d09ca3762af61e59520943dc26494f8941b");
		
		logger.info("请求报文：" + strbuff.toString());
		String sign = DigestUtils.shaHex(strbuff.toString()).toUpperCase();
		logger.info("签名sign=" + sign);
		if (sign.equals(signature.toUpperCase())) {
			logger.info("wangpos valid successful");
			String json = "{status:0,info:\"success\",data:{echo_str:\"" + echoStr + "\"}}";
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			try {
				response.getWriter().write(json);
			} catch (Exception e) {
				logger.error("返回Json结果错误 ",e);
			}
		} else {
			logger.info("wangpos valid failed");
		}
		
	}
	
	private String getNotifyUrl() throws BizException{
		String url = gdProperties.getProperties().getProperty("maven.paycenter.gateway.wangpos.asyncNotify");
		Assert.notEmpty(url, "旺POS异步通知URL未配置");
		return url;
	}
	
	private Map<String,String> getAllParam(HttpServletRequest request){
		Map<String,String> param = new HashMap<>();
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String value = request.getParameter(key);
			param.put(key, value);
		}
		return param;
	}
	
	private boolean verify(Map<String,String> param,String key){
		
		String text = AccessSysSignUtil.createLinkString(AccessSysSignUtil.paraFilter(param));
		logger.info("****************MD5验签串**********************");
		logger.info("MD5验签串:"+text+"&key="+key);
		return MD5.verify(text, param.get("sign"), "&key="+key, "utf-8");
	}
	
	private <T> T decrypt3des(String param,Class<T> clazz) throws BizException{
		String text = null;
//		if(gdProperties.isSign()){
			try {
				text = Des3Request.decode(param);
				logger.info("***********************DES3解密后,text="+text);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				throw new BizException(MsgCons.C_20001, "DES3解密失败");
			}
			
//		} else {
//			text = param;
//		}
		try {
			T t = JSON.parseObject(text, clazz);
			return t;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new BizException(MsgCons.C_20000, "转成"+clazz.getName()+"对象失败");
		}
	}
	
	private void handelException(ApiResult res,Exception e){
		if(e instanceof BizException){
			logger.error(e.getMessage(),e);
			BizException biz = (BizException)e;
			res.setCode(biz.getCode());
			res.setMsg(biz.getMsg());
			res.setIsSuccess(false);
		} else {
			logger.error(e.getMessage(),e);
			res.setCode(MsgCons.C_20000);
			res.setMsg("系统错误");
			res.setIsSuccess(false);
		}

	}
	
	private void setResut(String res,HttpServletResponse response){
		String encrpt = res;
//		if(gdProperties.isSign()){
			try {
				encrpt = Des3Response.encode(res);
				logger.info("***********************DES3加密后,encrpt="+encrpt);
			} catch (Exception e) {
				logger.error("DES3加密失败",e);
			}
//		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			response.getWriter().write(encrpt);
		} catch (Exception e) {
			logger.error("返回Json结果错误 ",e);
		}
		
	}
	
	private void validateCreateDto(MergePayTradeRequestDto dto) throws BizException{
		Assert.notEmpty(dto.getAppKey(), "appKey不能为空");
		if(dto.getOrderInfoList() == null || dto.getOrderInfoList().size() == 0){
			throw new BizException(MsgCons.C_20000, "orderInfos不能为空");
		}
		for(MergePayTradeRequestDto.OrderInfo oi : dto.getOrderInfoList()){
			Assert.notEmpty(oi.getOrderNo(), "orderNo不能为空");
			Assert.notEmpty(oi.getPayeeUserId(), "payeeUserId不能为空");
			Assert.notEmpty(oi.getPayerUserId(), "payerUserId不能为空");
			if(oi.getOrderTime() == null){
				throw new BizException(MsgCons.C_20001, "orderTime不能为空");
			}
			if(oi.getPayAmt() <= 0){
				throw new BizException(MsgCons.C_20001, "支付金额需要大于0");
			}
			if(!RequestNo.FINAL_PAY.equals(oi.getRequestNo())){
				throw new BizException(MsgCons.C_20001, "requestNo必须等于"+RequestNo.FINAL_PAY);
			}
			if(oi.getPayCount() != 2){
				throw new BizException(MsgCons.C_20001, "payCount必须等于2");
			}
		}
	}
	
	public static void main(String[] args) throws ParseException {
		String str="{\"businessId\":\"11\",\"pageNum\":1,\"pageSize\":\"\"}";
		WangPosOrderListRequestDto dto =
				JSON.parseObject(str, WangPosOrderListRequestDto.class);
		System.out.println(dto.toString());
		ApiResult<Map<String,String>> api = new ApiResult();
		Map<String,String> result= new HashMap<>();
		result.put("payCenterNumber", "1111");
		result.put("notifyUrl", "http://");
		api.setResult(result);
		System.out.println(JSON.toJSONString(api));
		System.out.println(1.233d);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(new Date(1482391973000l));
		System.out.println((new BigDecimal(101.0d).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP)).toString());
	}
}
