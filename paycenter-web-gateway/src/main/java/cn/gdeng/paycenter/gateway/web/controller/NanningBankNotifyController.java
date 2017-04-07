package cn.gdeng.paycenter.gateway.web.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.PayLogRecordService;
import cn.gdeng.paycenter.api.server.pay.PosPayService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.dto.pay.NanningPayNotifyDto;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.dto.pos.ResponseDto;
import cn.gdeng.paycenter.entity.pay.PayLogRecordEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.enums.PosTranstype;
import cn.gdeng.paycenter.gateway.util.PosPayUtil;
import cn.gdeng.paycenter.gateway.util.ResponseUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.MD5;
import cn.gdeng.paycenter.util.web.api.PayLogUtil;
import cn.gdeng.paycenter.util.web.api.RsaUtil;

/**
 * 南宁建行支付通知
 * @author sss
 *
 */
@Controller
@RequestMapping("nnccb")
public class NanningBankNotifyController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private PosPayService posPayService;
	
	@Reference
	private ThirdPayConfigService thirdPayConfigService;
	
	@Reference
	private PayLogRecordService payLogRecordService;
	
	@Resource
	private GdProperties gdProperties;
	
	private boolean isSign;
	
	@PostConstruct
	private void init(){
		String sign = gdProperties.getProperties().getProperty("gateway.sign");
		if(StringUtils.equals(sign, "false")){
			isSign = false;
		} else {
			isSign = true;
		}
	}
	
	@RequestMapping("/orderList")
	public void orderList(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String reqdata = request.getParameter("reqdata");
		String signmsg = request.getParameter("signmsg");
		logger.info("南宁建行获取订单列表入参:reqdata="+reqdata+",signmsg="+signmsg);
		//验签
		String appKey = "nsy_pay";
		String privateKey = null;
		ResponseDto res = new ResponseDto();
		try {
			
			ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(appKey, PayWayEnum.NNCCB.getWay(), "");
			privateKey = config.getPrivateKey();
			if(isSign && !RsaUtil.verify(MD5.crypt(reqdata).toLowerCase(), signmsg, config.getSysPublicKey(), "utf-8")){
				//验签失败
				logger.error("南宁建行获取订单列表,验签失败");
				response.getWriter().write(ResponseUtil.getFailSignResData(res,"验签失败", privateKey));
				return;
			}
		
			NanningPayNotifyDto dto = JSON.parseObject(reqdata, NanningPayNotifyDto.class);
			
			dto.setAppKey(appKey);
			res.setDatajson(posPayService.nanningOrderList(dto));
			String resMsg = ResponseUtil.getSuccessSignResData(res, privateKey);
			logger.info("南宁建行获取订单列表,返回结果:"+resMsg);
			response.getWriter().write(resMsg);
		} catch (Exception e) {
			logger.error("南宁建行获取订单列表失败,"+e.getMessage(),e);
			//返回失败 
			response.getWriter().write(ResponseUtil.getFailSignResData(res, "获取订单列表失败", privateKey));
			return;
		}
	}

	@RequestMapping("payNotify")
	public void payNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String reqdata = request.getParameter("reqdata");
		String signmsg = request.getParameter("signmsg");
		logger.info("南宁建行支付通知回调入参:reqdata="+reqdata+",signmsg="+signmsg);
		//验签
		String appKey = "nsy_pay";
		String privateKey = null;
		try {
			ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub(appKey, PayWayEnum.NNCCB.getWay(),"");
			privateKey = config.getPrivateKey();
			if(isSign && !RsaUtil.verify(MD5.crypt(reqdata).toLowerCase(), signmsg, config.getSysPublicKey(), "utf-8")){
				//验签失败
				logger.error("南宁建行支付通知,验签失败");
				response.getWriter().write(ResponseUtil.getFailSignResData(new ResponseDto(),"验签失败", privateKey));
				return;
			}
			NanningPayNotifyDto dto = JSON.parseObject(reqdata, NanningPayNotifyDto.class);
			//记录日志 以银行流水号，为基准记录日志
			PayLogRecordEntity log = PayLogUtil.buildPayReceiveLog(PayWayEnum.NNCCB.getWay(),dto.getTransseqno(), "收到南宁建行POS支付通知: "+JSON.toJSONString(dto));
			payLogRecordService.addLog(log);
			if(StringUtils.equals(dto.getPayresultcode(), "0000")){
				validate(dto);
				PosPayNotifyDto newDto = PosPayUtil.build4Nanning(dto);
				newDto.setAppKey(appKey);
				newDto.setPayChannelCode(PayWayEnum.NNCCB.getWay());
				posPayService.payNotify(newDto);
			} else {
				logger.info("南宁建行支付失败,"+dto.getPayresultmsg());
			}

		} catch (BizException e) {
			logger.error(e.getMessage(),e);
			//返回失败 
			response.getWriter().write(ResponseUtil.getFailSignResData(new ResponseDto(), e.getMsg(), privateKey));
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			//返回失败 
			response.getWriter().write(ResponseUtil.getFailSignResData(new ResponseDto(), "通知失败", privateKey));
			return;
		}
		//返回成功
		logger.info("南宁建行支付通知回调成功");
		response.getWriter().write(ResponseUtil.getSuccessSignResData(new ResponseDto(), privateKey));
	}

	private void validate(NanningPayNotifyDto dto){
		Assert.notNull(dto.getTransype(), "交易类型不能为空");
		Assert.notNull(dto.getMachinenum(), "POS终端号不能为空");
		Assert.notNull(dto.getMerchantnum(), "POS商户号不能为空");
		Assert.notNull(dto.getPayfee(), "支付金额不能为空");
		Assert.notNull(dto.getTransseqno(), "交易流水不能为空");
		Assert.notNull(dto.getPaycardno(),"交易卡号不能为空");
		if(PosTranstype.Nanning.POSITIVE.equals(dto.getTransype())){
			Assert.notNull(dto.getOrderno(),"订单号不能为空");
		}
	}

}
