package cn.gdeng.paycenter.task.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.server.pay.AlipayAccountService;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;
import cn.gdeng.paycenter.dto.account.AlipayAccountPageDto;
import cn.gdeng.paycenter.dto.alipay.AlipayAccountRequest;
import cn.gdeng.paycenter.task.component.AlipayAccountComponent;
import cn.gdeng.paycenter.task.reconciliation.ReconciliationTask;

@Controller
@RequestMapping("alipay")
public class AlipayAccountController {
	
	@Resource
	private AlipayAccountComponent alipayAccountComponent;
	
	@Reference	
	private AlipayAccountService<AlipayAccountRequest> alipayAccountService;
	
	@Resource
	private ReconciliationTask reconciliationTask;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("")
	public String checkBill(String type, String payType, String checkDate){
		/*if("0".equals(type)){
			reconciliationTask.checkBill();
		} else if("1".equals(type) || "2".equals(type)){
			reconciliationTask.checkBill(type, payType, checkDate);
		}*/
		return "";
		/*String url = "";
		try {
			AlipayOpenAccountRequest req = new AlipayOpenAccountRequest();
			Map<String,String> param = req.buildMap();
			logger.info("调用阿里对账接口,地址:"+AlipaySignUtil.buildUrl(req.getGateWay(), param));
			url = HttpUtil.httpClientPost(req.getGateWay(), param);
			logger.info("调用阿里对账接口，返回对账地址:"+url);
		} catch (BizException e) {
			logger.error("调用阿里对账失败"+e.getMessage(),e);
		}
		return url;*/
	}
	
	@RequestMapping("close")
	public String closeTrade(){
		reconciliationTask.payCloseTradeTask();
		return "";
		/*String url = "";
		try {
			AlipayOpenAccountRequest req = new AlipayOpenAccountRequest();
			Map<String,String> param = req.buildMap();
			logger.info("调用阿里对账接口,地址:"+AlipaySignUtil.buildUrl(req.getGateWay(), param));
			url = HttpUtil.httpClientPost(req.getGateWay(), param);
			logger.info("调用阿里对账接口，返回对账地址:"+url);
		} catch (BizException e) {
			logger.error("调用阿里对账失败"+e.getMessage(),e);
		}
		return url;*/
	}
	
	@RequestMapping("resend")
	public String resendTrade(){
		reconciliationTask.payResendTask();
		return "";
		/*String url = "";
		try {
			AlipayOpenAccountRequest req = new AlipayOpenAccountRequest();
			Map<String,String> param = req.buildMap();
			logger.info("调用阿里对账接口,地址:"+AlipaySignUtil.buildUrl(req.getGateWay(), param));
			url = HttpUtil.httpClientPost(req.getGateWay(), param);
			logger.info("调用阿里对账接口，返回对账地址:"+url);
		} catch (BizException e) {
			logger.error("调用阿里对账失败"+e.getMessage(),e);
		}
		return url;*/
	}
	
	/**
	 * 按天来查
	 * @param appKey
	 * @param billDate  yyyy-MM-dd
	 * @return
	 */
	@RequestMapping("account")
	@ResponseBody
	public String getAccount(String appKey,String billDate,String payCenterNumber){

		try {
			AccountRquestDto req = new AccountRquestDto();
			req.setAppKey(appKey);
			req.setBillDate(billDate);
			req.setPayCenterNumber(payCenterNumber);
			AlipayAccountPageDto dto = alipayAccountComponent._getAccount(req);
			return JSON.toJSONString(dto);
		} catch (Exception e) {
			logger.error("调用阿里对账失败"+e.getMessage(),e);
			return e.getMessage();
		}

	}
}
