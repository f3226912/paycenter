package cn.gdeng.paycenter.task.reconciliation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;

public class ReconciliationTask {
	
	@Autowired(required=false)
	private PayJobService payJobService;
	
	@Autowired(required=false)
	private AccessSysConfigService accessSysConfigService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void demo(){
		System.out.println("任务开始了.....");
		
		System.out.println("任务结束了.....");
	}
	
	/**
	 * 自动关闭订单定时器
	 */
	public void payCloseTradeTask() {
		System.out.println("关闭订单定时器启动。。。");
		payJobService.closeTrade();

	
	}
	
	/**
	 * 自动对账
	 */
	public void checkBill() {
		logger.info("自动对账任务开始");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DATE,-1); 
        String checkDate = sdf.format(cal.getTime());
		try {
			List<PayTypeDto> payTypelList = payJobService.queryPayTypeCheckBill();
			if(payTypelList != null && payTypelList.size() > 0) {
				for(PayTypeDto payTypeDto : payTypelList) {
					try{
						if(StringUtils.isNotBlank(payTypeDto.getPayType())) {
							if("ALIPAY_H5".equals(payTypeDto.getPayType())) {
								//对支付宝的账单
								payJobService.checkAlipayBill(checkDate);
							} else if("WEIXIN_APP".equals(payTypeDto.getPayType())) {
								//对微信的账单
								payJobService.checkWeChatBill(checkDate);
							} else {
								//对POS机账单
								payJobService.checkPosBill(payTypeDto.getPayType(), checkDate);
							}
						} else {
							logger.info("自动对账任务：支付渠道编号为空");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("自动对账任务异常(" + payTypeDto.getPayType() + "," + checkDate + ")：", e);
					}
				}
				//清算
				payJobService.clearBill(checkDate,null);
			} else {
				logger.info("自动对账任务：找不到支付渠道");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自动对账任务异常：", e.getMessage());
		}
		logger.info("自动对账任务结束");
	}
	
	/**
	 * 自动对账for 支付宝
	 */
	public void checkBillForAliPay(){
		logger.info("支付宝自动对账任务开始");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DATE,-1); 
        String checkDate = sdf.format(cal.getTime());
      //对支付宝的账单
		try {
			payJobService.checkAlipayBill(checkDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝自动对账任务异常(" + checkDate + ")：", e);
		}
		logger.info("支付宝自动对账任务结束");
	}
	
	/**
	 * 自动对账for 武汉E农
	 */
	public void checkBillForEnong(){
		logger.info("武汉E农自动对账任务开始");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DATE,-1); 
        String checkDate = sdf.format(cal.getTime());
        //对武汉E农的账单
		try {
			payJobService.checkPosBill("ENONG",checkDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("武汉E农自动对账任务异常(" + checkDate + ")：", e);
		}
		logger.info("武汉E农自动对账任务结束");
	}
	
	/**
	 * 自动对账for WEIXIN
	 */
	public void checkBillForWeiXin(){
		logger.info("微信自动对账任务开始");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DATE,-1); 
        String checkDate = sdf.format(cal.getTime());
        //对微信的账单
		try {
			payJobService.checkPosBill("WEIXIN_APP",checkDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("微信自动对账任务异常(" + checkDate + ")：", e);
		}
		logger.info("微信自动对账任务结束");
	}
	
	/**
	 * 自动对账for NNCCB 南宁建行
	 */
	public void checkBillForNNCCB(){
		logger.info("南宁建行自动对账任务开始");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DATE,-1); 
        String checkDate = sdf.format(cal.getTime());
        //对南宁建行的账单
		try {
			payJobService.checkPosBill("NNCCB",checkDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("南宁建行自动对账任务异常(" + checkDate + ")：", e);
		}
		logger.info("南宁建行自动对账任务结束");
	}
	
	/**
	 * 自动对账for GXRCB 广西农信社
	 */
	public void checkBillForGXRCB(){
		logger.info("广西农信社自动对账任务开始");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.DATE,-1); 
        String checkDate = sdf.format(cal.getTime());
        //对广西农信社的账单
		try {
			payJobService.checkPosBill("GXRCB",checkDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("广西农信社自动对账任务异常(" + checkDate + ")：", e);
		}
		logger.info("广西农信社自动对账任务结束");
	}
	
	
	/**
	 * 重新清算
	 */
	public void checkBill(String type, String payType, String checkDate,String orderNo) {
		try{
			if("1".equals(type)) {
				payJobService.checkBill(payType, checkDate);
			} else if("2".equals(type)) {
				payJobService.clearBill(checkDate,orderNo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 自动重发
	 */
	public void payResendTask() {
			payJobService.processResendNotify();
	}
}
