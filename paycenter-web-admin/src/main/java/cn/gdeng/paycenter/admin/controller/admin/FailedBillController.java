package cn.gdeng.paycenter.admin.controller.admin;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;
import com.gudeng.paltform.sms.GdSMS;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.FailedBillDTO;
import cn.gdeng.paycenter.admin.service.right.SysRegisterUserService;
import cn.gdeng.paycenter.admin.util.CommonUtil;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.api.server.pay.FailedBillService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import cn.gdeng.paycenter.util.web.api.Constant;

/**
 * 对账失败明细
 * @author youj
 *
 */
@Controller
@RequestMapping("paycenter/failedBill")
public class FailedBillController extends AdminBaseController{
	private static final GdLogger logger = GdLoggerFactory.getLogger(FailedBillController.class);
	@Reference FailedBillService failedBillService;
	@Reference SysRegisterUserService sysRegisterUserService;
	@Reference PayTradeService payTradeService;
	@Reference PayJobService payJobService;
	@Reference PayTypeService payTypeService;

	
	@RequestMapping("/list")
	public String list(ModelMap modelMap, HttpServletRequest request) throws BizException{
		String payType = request.getParameter("payType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		modelMap.addAttribute("payType", payType);
		modelMap.addAttribute("startDate", startDate);
		modelMap.addAttribute("endDate", endDate);
		List<PayTypeDto> payTypes = payTypeService.queryAll();
		modelMap.addAttribute("payTypes", payTypes);
		return "failedBill/list";
	}
	
	/**
	 * 失败账单列表
	 */
	@ResponseBody
	@RequestMapping("/queryPage")
	public String queryPage(HttpServletRequest request){
		 try {
	            Map<String, Object> map = getParametersMap(request);
	            
	            Integer total = failedBillService.countTotal(map);
	           
	            setCommParameters(request, map);
	            // list
	            List<Map<String,Object>> list = null;
	            
	            if (total < 1) {
	    			list = Collections.emptyList();
	    		} else {
	    			list = failedBillService.queryList(map);
	    		}
	            if (logger.isDebugEnabled()) {
	    			logger.debug("查询对账失败明细列表数据成功，共查询出{}条数据", new Object[]{list.size()});
	    		}
	            map.put("rows", list);// rows键 存放每页记录 list
	            map.put("total", total);
	            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
	        } catch (Exception e) {
	            return getExceptionMessage(e, logger);
	        }
	}
	
	/**
     * 提示页面
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("toAlertMsg/{resultType}")
    public String toAlertMsg(@PathVariable String resultType, Model model) {
    	 model.addAttribute("resultType", resultType);
    	 return "failedBill/alertMsg";
    }
	 /**
     * 短信验证页面
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("toSmsValidate/{parameter}")
    public String toSmsValidate(@PathVariable String parameter, Model model) {
    	 StringBuffer mobile = new StringBuffer("");
    	 String userId = getUser(request).getUserID();
    	 SysRegisterUser user = sysRegisterUserService.get(userId);
    	 if(user != null ){
    		 mobile.append(user.getMobile());
    	 }
    	 if(parameter.indexOf("_") >= 0){
    	    model.addAttribute("thirdPayNumber", parameter.substring(0, parameter.indexOf("_")));
		    model.addAttribute("mobile", mobile.toString());
		    model.addAttribute("resultType", parameter.substring(parameter.indexOf("_")+1, parameter.length()));
    	 }
		 if(!StringUtils.isEmpty(mobile.toString())
				 && mobile.toString().length() >= 11){
		    model.addAttribute("vmobile", mobile.replace(3, 7, "****"));
		 }
    	 return "failedBill/smsValidate";
    }
    
    /**
     * 查询对账单详情页面
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping(value="generateVerifyCode")
	@ResponseBody
	public Map<String,Object> generateVerifyCode(HttpServletRequest request) {
    	Map<String,Object> retmap = new HashMap<String,Object>();
		try {
			Long sessionTime = System.currentTimeMillis();
			Map<String, Object> map = getParametersMap(request);
			GdSMS sms = new GdSMS();
			String mobile = map.get("mobile").toString();
			Map<String, Long> checkmap = (Map<String, Long>) session.getAttribute(Constant.MOBILE_CHECK_TIME_MODIFY_BILL);
			if (checkmap != null && checkmap.get(mobile) != null) {
				// 判断两次时间是否大于1分钟
				Long value = (System.currentTimeMillis() - checkmap.get(mobile)) / 1000;
				if (value < 60) {
					retmap.put("status", "3");
					retmap.put("msg", "请60s后发送验证码！");
					logger.info("##########剩余时间" + (60 - value));
					return retmap;
				}
			}
			String channel = Constant.Alidayu.DEFAULTNO;
			String randi = getSixCode();
			String smsmsg = Constant.SMSMSG.replace("{code}", randi);
			System.out.println("----------------------------->" + randi);
			// 阿里大鱼短信通道
			if (Constant.Alidayu.REDISTYPE.equals(channel)) {
				smsmsg = CommonUtil.alidayuUtil(Constant.Alidayu.MESSAGETYPE7, randi);
			}
			boolean b = sms.SendSms(channel, smsmsg, mobile);
			if (b) {
				// 记录发送成功的手机号
				Map<String, Long> remaps = new HashMap<String, Long>();
				remaps.put(mobile, sessionTime);
				// 记录验证码
				session.setAttribute(Constant.MOBILE_CHECK_MODIFY_BILL + mobile, randi.toString());
				session.setAttribute(Constant.MOBILE_CHECK_MODIFY_BILL + "vaild", sessionTime);
				session.setAttribute(Constant.MOBILE_CHECK_TIME_MODIFY_BILL, remaps);
				retmap.put("status", "1");
				retmap.put("msg", "验证码已发送");
			} else {
				retmap.put("status", "2");
				retmap.put("msg", "发送短信验证码失败,请重试!");
			}
		} catch (Exception e) {
			logger.trace(e.getMessage());
			e.printStackTrace();
			retmap.put("status", "2");
			retmap.put("msg", "发送短信验证码失败,请重试!");
		}
		return retmap;
	}
    
    /**
	 * 确认验证码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="verifyCode")
	@ResponseBody
	public Map<String,Object> verifyCode(HttpServletRequest request){
		Map<String,Object> retmap = new HashMap<String,Object>();
		try {
			HttpSession session = request.getSession();
			String code = request.getParameter("code");
			String mobile = request.getParameter("mobile");
			String scode = (String) session.getAttribute(Constant.MOBILE_CHECK_MODIFY_BILL + mobile);
			
			if(StringUtils.isBlank(code)){
				retmap.put("status", "4");
				retmap.put("msg", "请输入验证码");
				return retmap;
			}
			
			if(StringUtils.isBlank(scode)){
				retmap.put("status", "5");
				retmap.put("msg", "验证码有误请重新获取");
				return retmap;
			}
			
			if(code.equals(scode)){
				retmap.put("status", "1");
				retmap.put("msg", "验证通过");
			}else{
				retmap.put("status", "3");
				retmap.put("msg", "验证码输入有误");
				return retmap;
			}
			
			Map<String, Long> checkmap = (Map<String, Long>) session.getAttribute(Constant.MOBILE_CHECK_TIME_MODIFY_BILL);
			if (checkmap != null && checkmap.get(mobile) != null) {
				Long value = (System.currentTimeMillis() - checkmap.get(mobile)) / 1000;
				if (value > 300) {
					retmap.put("status", "2");
					retmap.put("msg", "验证码已过期；验证码有效期时间为5分钟");
					return retmap;
				}
			}
		} catch (Exception e) {
			logger.trace(e.getMessage());
			e.printStackTrace();
			
		}
		return retmap;
	}
    
	 /**
     * 查询对账单详情页面
     * @param request
     * @param nstDto
     * @return
     */
	@SuppressWarnings("unused")
    @RequestMapping("failedBillDetail/{thirdPayNumber}")
    public String failedBillDetail(@PathVariable String thirdPayNumber, Model model) {
    	 Map<String, Object> map = getParametersMap(request);
    	 String responseUrl = "";
    	 double actualTotalAmt = 0;
    	 map.put("thirdPayNumber", thirdPayNumber);
    	 ApiResult<FailedBillDTO> apiResult = null;
    	 FailedBillDTO entity = null;
    	 Map<String, Object> retmap = failedBillService.queryResultType(map);
		 if ("1".equals(retmap.get("resultType"))) { // 流水-银行有谷登有
			apiResult = failedBillService.queryFailedBilltDetail(map);
			entity = apiResult.getResult();
			responseUrl = "failedBill/detailComplete";
		 } else if ("2".equals(retmap.get("resultType"))) { // 流水-银行无谷登有
			apiResult = failedBillService.queryFailedBilltDetailBankNone(map);
			entity = apiResult.getResult();
			responseUrl = "failedBill/detailImcomplete";
		 } else {
			apiResult = failedBillService.queryFailedBilltDetailGuDengNone(map);
			entity = apiResult.getResult();
			responseUrl = "failedBill/detail"; // 流水-银行有谷登无
		 }
		 
    	 if(entity == null){entity = new FailedBillDTO();entity.setResultType(retmap.get("resultType").toString());}
    	 /*谷登订单信息 **/
    	 List<Map<String,Object>> gdOrderInfos = failedBillService.queryGdOrderList(map);
    	 if(gdOrderInfos != null && gdOrderInfos.size() > 0){
    	    for(Map<String,Object> order : gdOrderInfos){
    	    	if(order.get("payAmt") != null){
    	    	   actualTotalAmt += new Double(order.get("payAmt").toString()).doubleValue();
    	    	   entity.setActualTotalAmt(String.valueOf(actualTotalAmt));
    	    	}
    	    	String clientNo = order.get("clientNo")==null?"":order.get("clientNo").toString();
    	    	map.put("machineNum", clientNo);
    	    	Map<String, Object> returnMap = failedBillService.queryLastestClearFlag(map);
    	    	if(returnMap != null && returnMap.containsKey("hasClear")){
    	    	   order.put("hasClear", returnMap.get("hasClear")==null?"":returnMap.get("hasClear").toString());
    	    	}
    	    }
    	 }
    	 model.addAttribute("entity", entity);
    	 model.addAttribute("orderInfoList", gdOrderInfos);
    	 return responseUrl;
    }
    
    
    /**
	 * 修改对账明细
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="updateFailedBill",produces="application/json;charset=utf-8")
	@ResponseBody
	public String updateFailedBill(HttpServletRequest request) {
		ApiResult<Integer> apiResult=new ApiResult<>();
		try {
			String retMsg = "保存成功!";
			Map<String, Object> map = getParametersMap(request);
			map.put("updateUserId", getUser(request).getUserID());
			String msg = payJobService.updateFailedBill(map);
			if(!msg.equals("success")){
				retMsg = msg;
				apiResult.setResult(0);
				apiResult.setMsg(retMsg);
				return JSONObject.toJSONString(apiResult, SerializerFeature.WriteDateUseDateFormat);
			}
			apiResult.setResult(1);
			apiResult.setMsg(retMsg);
		} catch (Exception e) {
			logger.info("updateBill failed!");
			apiResult.setResult(0);
    		if(e.getMessage().contains("汇总记录版本号不正确")) {
    			apiResult.setMsg("正在对账中，请稍侯重试");
    		} else {
    			apiResult.setMsg("保存失败!");
    		}
		}
		return JSONObject.toJSONString(apiResult, SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 产生随机的六位数
	 * @return
	 */
	public String getSixCode(){
		Random rad=new Random();
		
		String result  = rad.nextInt(1000000) +"";
		
		if(result.length()!=6){
			return getSixCode();
		}
		return result;
	}	
	
	/**格式化金额
	 * @param money
	 * @return
	 */
	private String formatMoney(double money) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.format(money);
	}
	
}
