package cn.gdeng.paycenter.admin.controller.admin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.service.admin.AdminRemitRecordService;
import cn.gdeng.paycenter.admin.service.admin.MemberBankcardInfoService;
import cn.gdeng.paycenter.dto.pay.MemberBankcardInfoDTO;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

/**
 * 交易记录controller
 * 
 */
@Controller
@RequestMapping("paycenter/remitrecord")
public class AdminRemitRecordController extends AdminBaseController {

	private static final GdLogger logger = GdLoggerFactory.getLogger(AdminRemitRecordController.class);

    /** 用户Serivce */
    @Reference
    private AdminRemitRecordService adminRemitRecordService;
    
    @Reference
	private MemberBankcardInfoService memberBankcardInfoService;
    
	/**
	 * 转账
	 * 
	 * @param remitRecordEntity
	 * @return
	 */
	@RequestMapping(value = "update", produces = "application/html;charset=UTF-8")
	@ResponseBody
	public String update(HttpServletRequest request) {
        try {
        	SysRegisterUser user = getUser(request);
        	if(user == null){
        		return "操作异常，请重新登陆";
        	}
        	if(StringUtils.isBlank(request.getParameter("payerName"))){
        		return "开户人不能为空";
        	}
        	if(StringUtils.isBlank(request.getParameter("payerBankName"))){
        		return "支付银行不能为空";
        	}
        	if(StringUtils.isBlank(request.getParameter("bankTradeNo"))){
        		return "转账流水不能为空";
        	}
        	if(StringUtils.isBlank(request.getParameter("payAmt"))){
        		return "转账金额不能为空";
        	}
        	double payAmt = Double.parseDouble(request.getParameter("payAmt"));
        	if(payAmt <= 0 || payAmt > 99999999.99){
        		return "转账金额必须在0.01-99999999.99之间";
        	}
        	if(StringUtils.isBlank(request.getParameter("feeAmt"))){
        		return "转账手续费不能为空";
        	}
        	double feeAmt = Double.parseDouble(request.getParameter("feeAmt"));
        	if(feeAmt < 0 || feeAmt > 99999999.99){
        		return "转账手续费必须在0.00-99999999.99之间";
        	}
        	if(StringUtils.isBlank(request.getParameter("payTime"))){
        		return "转账时间不能为空";
        	}
            // 修改
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderNo", request.getParameter("orderNo"));
            map.put("payCenterNumber", request.getParameter("payCenterNumber"));
            map.put("payerName", request.getParameter("payerName"));
            map.put("payerBankName", request.getParameter("payerBankName"));
            map.put("bankTradeNo", request.getParameter("bankTradeNo"));
            map.put("payAmt", payAmt);
            map.put("feeAmt", feeAmt);
            map.put("payTime", request.getParameter("payTime"));
            map.put("operaUserId", user.getUserID());
            if(StringUtils.isBlank(request.getParameter("updateUserId"))){
            	if(StringUtils.isBlank(request.getParameter("payeeUserId"))){
            		return "收款人id为空";
            	}
                Map<String, Object> bankcardParamMap = new HashMap<String, Object>();
                bankcardParamMap.put("regtype", "1");
                bankcardParamMap.put("memberId", request.getParameter("payeeUserId"));
                List<MemberBankcardInfoDTO> memberBankcardInfoList = memberBankcardInfoService.queryByCondition(bankcardParamMap);
                if(memberBankcardInfoList == null || memberBankcardInfoList.size() == 0){
                	return "收款人没有绑定银行卡";
                }
                if(memberBankcardInfoList.size() > 1){
                	return "收款人存在多个默认卡号";
                }
                MemberBankcardInfoDTO memberBankcardInfoDTO = memberBankcardInfoList.get(0);
                map.put("bankRealName", memberBankcardInfoDTO.getRealName());
                map.put("bankCardNo", memberBankcardInfoDTO.getBankCardNo());
                map.put("depositBankName", memberBankcardInfoDTO.getDepositBankName());
                map.put("provinceName", memberBankcardInfoDTO.getProvinceName());
                map.put("cityName", memberBankcardInfoDTO.getCityName());
                map.put("areaName", memberBankcardInfoDTO.getAreaName());
                map.put("subBankName", memberBankcardInfoDTO.getSubBankName());
                map.put("bankMobile", memberBankcardInfoDTO.getMobile());
        	}
            if(StringUtils.isBlank(request.getParameter("id"))){
            	adminRemitRecordService.insertTransfer(map);
            } else {
            	map.put("id", Integer.parseInt(request.getParameter("id")));
            	map.put("updateUserId", request.getParameter("updateUserId"));
	            adminRemitRecordService.updateTransfer(map);
            }
            return "success";
        } catch (Exception e) {
            // 记录日志;
            e.printStackTrace();
            logger.info(e.getMessage());
            return "操作异常";
        }
	}
}
