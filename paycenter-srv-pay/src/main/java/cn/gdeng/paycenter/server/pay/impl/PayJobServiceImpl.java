package cn.gdeng.paycenter.server.pay.impl;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.caucho.hessian.client.HessianProxyFactory;
import com.gudeng.commerce.gd.order.dto.OrderBaseinfoDTO;
import com.gudeng.commerce.gd.order.service.OrderBaseinfoService;
import com.gudeng.commerce.gd.order.service.OrderProductDetailService;
import com.gudeng.framework.dba.transaction.annotation.Transactional;
import com.gudeng.framework.dba.util.DalUtils;

import cn.gdeng.paycenter.admin.dto.admin.GdOrderInfo;
import cn.gdeng.paycenter.admin.dto.admin.PayTradeDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.api.server.order.OrderAdvanceAndPaymentDTO;
import cn.gdeng.paycenter.api.server.order.OrderBillHessianService;
import cn.gdeng.paycenter.api.server.order.OrderbaseInfoHessianService;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.TradeQueryService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;
import cn.gdeng.paycenter.dto.account.TradeQueryRequestDto;
import cn.gdeng.paycenter.dto.account.TradeQueryResponseDto;
import cn.gdeng.paycenter.dto.pay.BillCheckDetailDto;
import cn.gdeng.paycenter.dto.pay.BillCheckSumDTO;
import cn.gdeng.paycenter.dto.pay.BillChecks;
import cn.gdeng.paycenter.dto.pay.BillClearLogDTO;
import cn.gdeng.paycenter.dto.pay.ClearBillDTO;
import cn.gdeng.paycenter.dto.pay.GdOrderActivityInfoDTO;
import cn.gdeng.paycenter.dto.pay.GdOrderActivityResultDTO;
import cn.gdeng.paycenter.dto.pay.PayNotifyResponse;
import cn.gdeng.paycenter.dto.pay.PayTradeDto;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.dto.pay.RefundFeeItemDetailDTO;
import cn.gdeng.paycenter.dto.pay.RefundRecordDTO;
import cn.gdeng.paycenter.dto.pay.StatusCodeEnumWithInfoDTO;
import cn.gdeng.paycenter.dto.pos.OrderBaseinfoHessianDto;
import cn.gdeng.paycenter.dto.pos.OrderBillHessianDto;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.BillCheckDetailEntity;
import cn.gdeng.paycenter.entity.pay.BillClearSumEntity;
import cn.gdeng.paycenter.entity.pay.BillTransDetailEntity;
import cn.gdeng.paycenter.entity.pay.ClearDetailEntity;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeExtendEntity;
import cn.gdeng.paycenter.entity.pay.PayTradePosEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeConfigEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.entity.pay.PosMachineConfigEntity;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.enums.PosTranstype;
import cn.gdeng.paycenter.server.pay.component.AlipayAccountComponent;
import cn.gdeng.paycenter.server.pay.component.WeChatAccountComponent;
import cn.gdeng.paycenter.server.pay.util.HttpUtil;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import cn.gdeng.paycenter.util.server.MathUtil;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;
import cn.gdeng.paycenter.util.server.sign.GdDes3;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.gudeng.framework.dba.transaction.annotation.Transactional;
import com.gudeng.framework.dba.util.DalUtils;

/**
 * 定时任务实现类
 * 
 * @author Ailen
 *
 */
@Service
public class PayJobServiceImpl implements PayJobService {

	private static String RESEND_SYNCH = "resend_synch";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private BaseDao<PayTradeEntity> baseDao;

	@Resource
	private AccessSysConfigService accessSysconfigService;

	@Resource
	private PayTradeService payTradeService;

	@Resource
	private SendMessageUtil sendMessageUtil;
	
	@Resource
	private TradeQueryService tradeQueryService;
	
	@Resource
	private AlipayAccountComponent alipayAccountComponent;
	
	@Resource
	private WeChatAccountComponent weChatAccountComponent;
	
	@Resource
	public GdProperties gdProperties;
	
	@Resource
	private OrderBillHessianService orderBillHessianService;
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;
	
	@Resource
	private OrderbaseInfoHessianService orderbaseInfoHessianService;
	
    private OrderBaseinfoService orderBaseinfoService;
	
	private OrderProductDetailService orderProductDetailService;

	@Override
	public void processResendNotify() {

		/*
		 * 设置查询未通知到的数据 查询条件
		 */
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("notifyStatus", NotifyStatus.READ_NOTIFY);
		paramMap.put("payStatus", PayStatus.PAID);
		/*
		 * 同步块
		 */
		synchronized (RESEND_SYNCH) {

			// 获得未通知的付款信息
			List<PayTradeEntity> payTrades = baseDao.queryForList("PayJob.queryPayTradeForJob", paramMap,
					PayTradeEntity.class);

			/*
			 * 判断是否存在未通知信息
			 */
			if (payTrades.size() > 0) {

				/*
				 * 获得所有未通知的id 更改对应id通知状态
				 */
				List<Integer> ids = new ArrayList<>();
				for (int i = 0; i < payTrades.size(); i++) {
					ids.add(payTrades.get(i).getId());

				}

				/*
				 * 修改通知状态 通知中
				 */
				Map<String, Object> idsParam = new HashMap<>();
				idsParam.put("tradeIds", ids);
				idsParam.put("notifyStatus", NotifyStatus.NOTIFING);

				int result = baseDao.execute("PayJob.updatePayTradeMoreForJob", idsParam);

				/*
				 * 修改状态的数据与查询的数据一致
				 */
				if (result == ids.size()) {
					AccessSysConfigEntity accessSysConfigEntity = null;

					/*
					 * 循环发送信息
					 */
					for (int i = 0; i < payTrades.size(); i++) {
						final PayTradeEntity payTrade = payTrades.get(i);
						// 调用方法发送mq
						
						try {
							accessSysConfigEntity = accessSysconfigService.queryByAppKey(payTrade.getAppKey());
						} catch (BizException e) {
							payTrade.setNotifyStatus(NotifyStatus.READ_NOTIFY);
							baseDao.execute("PayJob.updatePayTradeForJob", payTrade);
							logger.error("查询接入系统错误,错误码：" + e.getCode() + ", 错误消息:" + e.getMsg());
						}

						/*
						 * 判断是否存在此平台MQ
						 */
						if (accessSysConfigEntity != null) {

							/*
							 * 异步定时任务通知
							 * 
							 */
							try {
								sendMessageUtil.sendAsyncMessage(accessSysConfigEntity.getMqTopic(), "TAGS",
										postPay(payTrade), new SendCallback() {

											/*
											 * 成功则修改状态未已通知
											 */
											@Override
											public void onSuccess(SendResult sendResult) {
												payTrade.setNotifyStatus(NotifyStatus.NOTIFIED);
												baseDao.execute("PayJob.updatePayTradeForJob", payTrade);
												logger.info("[订单：" + payTrade.getOrderNo() + "]定时任务异步通知发送MQ成功！");

											}

											/*
											 * 失败，则写入日志 不修改状态
											 */
											@Override
											public void onException(OnExceptionContext context) {
												payTrade.setNotifyStatus(NotifyStatus.READ_NOTIFY);
												baseDao.execute("PayJob.updatePayTradeForJob", payTrade);
												logger.error("[订单：" + payTrade.getOrderNo() + "]定时任务异步通知发送MQ失败：",
														context.getException().getMessage());
											}
										});

							} catch (Exception e) {
								/*
								 * 发送失败记录信息 不再重新发布 状态依然 发送中
								 */
								payTrade.setNotifyStatus(NotifyStatus.READ_NOTIFY );
								baseDao.execute("PayJob.updatePayTradeForJob", payTrade);
								logger.error("[订单：" + payTrade.getOrderNo() + "]定时任务异步通知发送MQ失败：", e.getMessage());
							}

						}

					}
				} else {
					throw new RuntimeException("修改的通知状态与实际发送数据的状态不一致，请检查通知中的订单情况！可能存在其他线程修改了对应订单的通知状态");
				}

			}

		}

	}
	
	@Override
	@Transactional
	public void checkAlipayBill(String checkDate) throws Exception{
		logger.info("ALIPAY_H5 " + checkDate + "，开始对账");
		try{
			BillCheckSumDTO billCheckLogDTO = new BillCheckSumDTO(true);
			BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
			BillCheckSumDTO billCheckDTO = queryBillCheckSum("ALIPAY_H5", checkDate);
			if(billCheckDTO != null) {
				billCheckSumDTO.setId(billCheckDTO.getId());
				billCheckSumDTO.setVersion(billCheckDTO.getVersion());
				logger.info("对账版本号：" + billCheckSumDTO.getVersion());
			}	
			billCheckLogDTO.setPayType("ALIPAY_H5");
			billCheckSumDTO.setPayType("ALIPAY_H5");
			boolean hasNext = true;
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("payType", "ALIPAY_H5");
			List<PayTypeConfigEntity> configs = baseDao.queryForList("PayJob.queryPayTypeConfig", paramMap, PayTypeConfigEntity.class);
			if(configs != null && configs.size() > 0) {
				List<String> partnerList = new ArrayList<String>();
				for(PayTypeConfigEntity payTypeConfigEntity : configs) {
					logger.info("支付宝APPKEY：" + payTypeConfigEntity.getAppKey());
					ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
							(payTypeConfigEntity.getAppKey(), PayWayEnum.ALIPAY_H5.getWay(), SubPayType.ALIPAY.PARTNER);
					AlipayConfig ac = AlipayConfigUtil.buildAlipayConfig(config);
					logger.info("支付宝合作者：" + ac.getPartner());
					if(!partnerList.contains(ac.getPartner())) {
						//获取对账数据
						do {
							//配置查询参数
							AccountRquestDto dto = new AccountRquestDto();
							//设置数据 
							dto.setAppKey(payTypeConfigEntity.getAppKey());
							dto.setBillDate(checkDate);
							dto.setPageSize(20);
							//读取数据
							BillChecks billChecks = alipayAccountComponent.getAccount(dto);
							//设置是否存在数据
							hasNext = billChecks.getHasNext();
							//插入数据库
							BillCheckSumDTO billDTO = processAlipayBill(billChecks);
							
							billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + billDTO.getCheckNum());
							billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),billDTO.getCheckAmt()));
							billCheckLogDTO.setCheckSuccessNum(billCheckLogDTO.getCheckSuccessNum() + billDTO.getCheckSuccessNum());
							billCheckLogDTO.setCheckSuccessAmt(addDouble(billCheckLogDTO.getCheckSuccessAmt(), billDTO.getCheckSuccessAmt()));
							billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + billDTO.getCheckFailNum());
							billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(), billDTO.getCheckFailAmt()));
						
						} while(hasNext);
						partnerList.add(ac.getPartner());
					}
				}
			}
			/**
			 * 增加银行或第三方没有对账单支付平台有的场景
			 */
			//查询银行或第三方没有支付平台有的账单
			List<BillCheckDetailEntity> checkDetailList = queryPayTradeNoCheck("ALIPAY_H5",checkDate);
			for(BillCheckDetailEntity entity : checkDetailList){
				
				billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + 1);
				billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),entity.getPayAmt()));
				billCheckLogDTO.setCheckSuccessNum(billCheckLogDTO.getCheckSuccessNum() + 1);
				billCheckLogDTO.setCheckSuccessAmt(addDouble(billCheckLogDTO.getCheckSuccessAmt(), entity.getPayAmt()));
				billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + 1);
				billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(), entity.getPayAmt()));
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("payCenterNumber", entity.getPayCenterNumber());
				map.put("thirdPayNumber", entity.getThirdPayNumber());
				map.put("checkStatus", entity.getCheckStatus());
				map.put("failReason", entity.getFailReason()+":"+entity.getThirdPayNumber());
				map.put("resultType", entity.getResultType());
				map.put("payChannelCode", entity.getPayChannelCode());
				map.put("payAmt", entity.getPayAmt());
				map.put("createUserId", "SYS");
				map.put("updateUserId", "SYS");
				map.put("payTimeStr", DateUtil.getDate(entity.getPayTime(), DateUtil.DATE_FORMAT_DATETIME));
				baseDao.execute("PayJob.addBillCheck", map);
				
				//增加账单流水明细记录表
				BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
				billTransDetailEntity.setThirdTransNo(entity.getThirdPayNumber());
				billTransDetailEntity.setPayTime(entity.getPayTime());
				billTransDetailEntity.setClientNo(entity.getPosClientNo());
				billTransDetailEntity.setTradeMoney(0d);
				billTransDetailEntity.setFee(0d);
				billTransDetailEntity.setCreateTime(new Date());
				billTransDetailEntity.setCreateUserId("SYS");
				baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
				
			}
			//增加或修改对账汇总记录
			addBillCheckSum(checkDate,billCheckLogDTO,billCheckSumDTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("对账系统异常：", e);
			throw e;
		}
		logger.info("ALIPAY_H5 " + checkDate + "，结束对账");
	}
	
	@Override
	@Transactional
	public void checkWeChatBill(String checkDate) throws Exception{
		logger.info("WEIXIN_APP " + checkDate + "，开始对账");
		try{
			BillCheckSumDTO billCheckLogDTO = new BillCheckSumDTO(true);
			BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
			BillCheckSumDTO billCheckDTO = queryBillCheckSum("WEIXIN_APP", checkDate);
			if(billCheckDTO != null) {
				billCheckSumDTO.setId(billCheckDTO.getId());
				billCheckSumDTO.setVersion(billCheckDTO.getVersion());
				logger.info("对账版本号：" + billCheckSumDTO.getVersion());
			}	
			billCheckLogDTO.setPayType("WEIXIN_APP");
			billCheckSumDTO.setPayType("WEIXIN_APP");
			//boolean hasNext = true;
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("payType", "WEIXIN_APP");
			List<PayTypeConfigEntity> configs = baseDao.queryForList("PayJob.queryPayTypeConfig", paramMap, PayTypeConfigEntity.class);
			if(configs != null && configs.size() > 0) {
				List<String> appIdList = new ArrayList<String>();
				for(PayTypeConfigEntity payTypeConfigEntity : configs) {
					logger.info("微信APPKEY：" + payTypeConfigEntity.getAppKey());
					ThirdPayConfigEntity config = thirdPayConfigService.queryByTypeSub
							(payTypeConfigEntity.getAppKey(), PayWayEnum.WEIXIN_APP.getWay(), SubPayType.WECHAT.APP);
					WeChatConfig ac = WeChatConfigUtil.buildConfig(config);
					logger.info("微信appId：" + ac.getAppid());
					if(!appIdList.contains(ac.getAppid())) {
						//配置查询参数
						AccountRquestDto dto = new AccountRquestDto();
						//设置数据 
						dto.setAppKey(payTypeConfigEntity.getAppKey());
						dto.setBillDate(checkDate.replaceAll("-", ""));
						//dto.setPageSize(20);
						//读取数据
						BillChecks billChecks = weChatAccountComponent.getAccount(dto);
						//设置是否存在数据
						//hasNext = billChecks.getHasNext();
						//插入数据库
						BillCheckSumDTO billDTO = processWeChatBill(billChecks);
						
						billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + billDTO.getCheckNum());
						billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),billDTO.getCheckAmt()));
						billCheckLogDTO.setCheckSuccessNum(billCheckLogDTO.getCheckSuccessNum() + billDTO.getCheckSuccessNum());
						billCheckLogDTO.setCheckSuccessAmt(addDouble(billCheckLogDTO.getCheckSuccessAmt(), billDTO.getCheckSuccessAmt()));
						billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + billDTO.getCheckFailNum());
						billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(), billDTO.getCheckFailAmt()));
						appIdList.add(ac.getAppid());
					}
				}
			}
			/**
			 * 增加银行或第三方没有对账单支付平台有的场景
			 */
			//查询银行或第三方没有支付平台有的账单
			List<BillCheckDetailEntity> checkDetailList = queryPayTradeNoCheck("WEIXIN_APP",checkDate);
			for(BillCheckDetailEntity entity : checkDetailList){
				
				billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + 1);
				billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),entity.getPayAmt()));
				//billCheckLogDTO.setCheckSuccessNum(billCheckLogDTO.getCheckSuccessNum() + 1);
				//billCheckLogDTO.setCheckSuccessAmt(addDouble(billCheckLogDTO.getCheckSuccessAmt(), entity.getPayAmt()));
				billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + 1);
				billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(), entity.getPayAmt()));
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("payCenterNumber", entity.getPayCenterNumber());
				map.put("thirdPayNumber", entity.getThirdPayNumber());
				map.put("checkStatus", entity.getCheckStatus());
				map.put("failReason", entity.getFailReason()+":"+entity.getThirdPayNumber());
				map.put("resultType", entity.getResultType());
				map.put("payChannelCode", entity.getPayChannelCode());
				map.put("payAmt", entity.getPayAmt());
				map.put("createUserId", "SYS");
				map.put("updateUserId", "SYS");
				map.put("payTimeStr", DateUtil.getDate(entity.getPayTime(), DateUtil.DATE_FORMAT_DATETIME));
				baseDao.execute("PayJob.addBillCheck", map);
				
				//增加账单流水明细记录表
				BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
				billTransDetailEntity.setThirdTransNo(entity.getThirdPayNumber());
				billTransDetailEntity.setPayTime(entity.getPayTime());
				billTransDetailEntity.setClientNo(entity.getPosClientNo());
				billTransDetailEntity.setTradeMoney(0d);
				billTransDetailEntity.setFee(0d);
				billTransDetailEntity.setCreateTime(new Date());
				billTransDetailEntity.setCreateUserId("SYS");
				baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
				
			}
			//增加或修改对账汇总记录
			addBillCheckSum(checkDate,billCheckLogDTO,billCheckSumDTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("对账系统异常：", e);
			throw e;
		}
		logger.info("WEIXIN_APP " + checkDate + "，结束对账");
	}
	
	@Override
	@Transactional
	public void checkPosBill(String payType, String checkDate) throws Exception {
		logger.info(payType + " " + checkDate + "，开始对账");
		try {
			BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
			BillCheckSumDTO billCheckLogDTO = new BillCheckSumDTO(true);
			BillCheckSumDTO billCheckDTO = queryBillCheckSum(payType, checkDate);
			if(billCheckDTO != null) {
				billCheckSumDTO.setId(billCheckDTO.getId());
				billCheckSumDTO.setVersion(billCheckDTO.getVersion());
				logger.info("对账版本号：" + billCheckSumDTO.getVersion());
			}
			billCheckSumDTO.setPayType(payType);
			billCheckLogDTO.setPayType(payType);
			List<OrderBillHessianDto> bankStatementList = queryBankStatement(payType, checkDate);
			if(bankStatementList != null && bankStatementList.size() > 0){
				logger.info("银行对账文件记录数： " + bankStatementList.size());
				for(OrderBillHessianDto bankStatementDTO : bankStatementList){
					logger.info("银行对账文件记录明细： sysRefeNo=" + bankStatementDTO.getSysRefeNo() + ",cardType=" + bankStatementDTO.getCardType() + 
							",tradeDay=" + bankStatementDTO.getTradeDay() + ",tradeMoney=" + bankStatementDTO.getTradeMoney() + 
							",fee=" + bankStatementDTO.getFee() + ",PayCenterNumber=" + bankStatementDTO.getPayCenterNumber());
					//判断是否存在此数据
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("thirdPayNumber", bankStatementDTO.getSysRefeNo());
					BillCheckDetailDto billCheckDetailDto = queryBillCheckDetail(paramMap);
					logger.info("来自平台的对账明细记录:" + billCheckDetailDto);
					//未对账或对账失败的重新对账
					if(billCheckDetailDto == null || "2".equals(billCheckDetailDto.getCheckStatus())) {
						//获得对应的pay_trade记录进行对账
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("thirdPayNumber", bankStatementDTO.getSysRefeNo());
						params.put("payStatus", "2");
						List<PayTradeEntity> payTrades = baseDao.queryForList("PayJob.queryPayTrade", params, PayTradeEntity.class);
						/*
						 * 比较金额
						 */
						BillCheckDetailDto bill = new BillCheckDetailDto();
						bill.setThirdPayNumber(bankStatementDTO.getSysRefeNo());
						bill.setTrans_date(bankStatementDTO.getTradeDay());
						bill.setPayAmt(bankStatementDTO.getTradeMoney());
						bill.setFeeAmt(bankStatementDTO.getFee() != null ? bankStatementDTO.getFee() : 0);
						if(payTrades.size() != 0) {
							//获得对应的交易记录
//							PayTradeDTO basePayTrade = payTrades.get(0);
							Double payAmt = 0D;
							for(PayTradeEntity dto : payTrades){
								payAmt = addDouble(payAmt,dto.getPayAmt());
							}
							logger.info("平台流水号:" + payTrades.get(0).getPayCenterNumber());
							bill.setPayCenterNumber(payTrades.get(0).getPayCenterNumber());
//							if(basePayTrade.getPayAmt().equals(bankStatementDTO.getTradeMoney())) {
							if(payAmt.equals(bankStatementDTO.getTradeMoney())) {
								
								billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + 1);
								billCheckLogDTO.setCheckSuccessNum(billCheckLogDTO.getCheckSuccessNum() + 1);
								billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),payAmt));
								billCheckLogDTO.setCheckSuccessAmt(addDouble(billCheckLogDTO.getCheckSuccessAmt(),payAmt));
								
								bill.setPayTimeStr(DateUtil.getDate(bankStatementDTO.getTradeDay(), DateUtil.DATE_FORMAT_DATETIME));
								bill.setCheckStatus("1"); //金额一样 对账成功
								/*
								 * 只有对账成功，才能添加手续费
								 * 判断是否已经添加过手续费 getFeeRecordCount，当没有对应的手续费交易记录时 则添加
								 */
								if (upHasFeeRecord(payTrades.get(0).getPayCenterNumber())) {
									changeTradeAndFee(bill, payTrades);
								}
								bankStatementDTO.setPayCenterNumber(payTrades.get(0).getPayCenterNumber());
								//更新支付交易POS刷卡表
								if(StringUtils.isNotBlank(bankStatementDTO.getCardType())) {
									logger.info("更新支付交易POS刷卡表");
									updatePayTradePos(bankStatementDTO);
								}
								
							} else {
								logger.info("金额不一致，银行：" + String.format("%.2f", bankStatementDTO.getTradeMoney()) + 
										" 交易记录:" + String.format("%.2f", payAmt));
								bill.setPayTimeStr(DateUtil.getDate(bankStatementDTO.getTradeDay(), DateUtil.DATE_FORMAT_DATETIME));
								bill.setCheckStatus("2");
								bill.setResultType("1");
								bill.setFailReason("金额不一致，银行：" + String.format("%.2f", bankStatementDTO.getTradeMoney()) + 
										" 交易记录:" + String.format("%.2f", payAmt));
								
								billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + 1);
								billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + 1);
								billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),payAmt));
								billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(),payAmt));
								
								//增加账单流水明细记录表
								BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
								billTransDetailEntity.setClientNo(bankStatementDTO.getClientNo());
								billTransDetailEntity.setBusinessNo(bankStatementDTO.getBusinessNo());
								billTransDetailEntity.setTradeType(bankStatementDTO.getTradeType());
								billTransDetailEntity.setThirdTransNo(bankStatementDTO.getSysRefeNo());
								billTransDetailEntity.setTradeMoney(bankStatementDTO.getTradeMoney());
								billTransDetailEntity.setFee(bankStatementDTO.getFee());
								billTransDetailEntity.setCardNo(bankStatementDTO.getCardNo());
								billTransDetailEntity.setPayTime(bankStatementDTO.getTradeDay());
								billTransDetailEntity.setCreateTime(new Date());
								billTransDetailEntity.setCreateUserId("SYS");
								baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
							}
						} else {
							logger.info("支付系统没有第三方流水号:" + bankStatementDTO.getSysRefeNo());
							bill.setPayTimeStr(DateUtil.getDate(bankStatementDTO.getTradeDay(), DateUtil.DATE_FORMAT_DATETIME));
							bill.setCheckStatus("2");
							bill.setResultType("3");
							bill.setFailReason("支付系统没有第三方流水号:" + bankStatementDTO.getSysRefeNo());
							
							billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + 1);
							billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + 1);
							billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),bill.getPayAmt()));
							billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(),bill.getPayAmt()));

							//增加账单流水明细记录表
							BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
							billTransDetailEntity.setClientNo(bankStatementDTO.getClientNo());
							billTransDetailEntity.setBusinessNo(bankStatementDTO.getBusinessNo());
							billTransDetailEntity.setTradeType(bankStatementDTO.getTradeType());
							billTransDetailEntity.setThirdTransNo(bankStatementDTO.getSysRefeNo());
							billTransDetailEntity.setTradeMoney(bankStatementDTO.getTradeMoney());
							billTransDetailEntity.setFee(bankStatementDTO.getFee());
							billTransDetailEntity.setCardNo(bankStatementDTO.getCardNo());
							billTransDetailEntity.setPayTime(bankStatementDTO.getTradeDay());
							billTransDetailEntity.setCreateTime(new Date());
							billTransDetailEntity.setCreateUserId("SYS");
							baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
						}
						if(billCheckDetailDto == null) {
							//添加
							logger.info("增加对账明细记录");
							bill.setPayChannelCode(payType);
							addBillCheck(bill);
						} else if("2".equals(billCheckDetailDto.getCheckStatus()) && "1".equals(bill.getCheckStatus())){
							logger.info("更新对账明细记录");
							bill.setPayChannelCode(payType);
							updateBillCheck(bill);
						}
					} 
//					else {
//						billCheckSumDTO.setCheckSuccessNum(billCheckSumDTO.getCheckSuccessNum() + 1);
//						billCheckSumDTO.setCheckSuccessAmt(addDouble(billCheckSumDTO.getCheckSuccessAmt(), billCheckDetailDto.getPayAmt()));
//					}
				}
			}
			/**
			 * 增加银行或第三方没有对账单支付平台有的场景
			 */
			//查询银行或第三方没有支付平台有的账单
			List<BillCheckDetailEntity> checkDetailList = queryPayTradeNoCheck(payType,checkDate);
			for(BillCheckDetailEntity entity : checkDetailList){
				
				billCheckLogDTO.setCheckNum(billCheckLogDTO.getCheckNum() + 1);
				billCheckLogDTO.setCheckFailNum(billCheckLogDTO.getCheckFailNum() + 1);
				billCheckLogDTO.setCheckAmt(addDouble(billCheckLogDTO.getCheckAmt(),entity.getPayAmt()));
				billCheckLogDTO.setCheckFailAmt(addDouble(billCheckLogDTO.getCheckFailAmt(),entity.getPayAmt()));
				
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("payCenterNumber", entity.getPayCenterNumber());
				paramMap.put("thirdPayNumber", entity.getThirdPayNumber());
				paramMap.put("payAmt", entity.getPayAmt());
				paramMap.put("checkStatus", entity.getCheckStatus());
				paramMap.put("failReason", entity.getFailReason()+":"+entity.getThirdPayNumber());
				paramMap.put("resultType", entity.getResultType());
				paramMap.put("payChannelCode", entity.getPayChannelCode());
				paramMap.put("createUserId", "SYS");
				paramMap.put("updateUserId", "SYS");
				paramMap.put("payTimeStr", DateUtil.getDate(entity.getPayTime(), DateUtil.DATE_FORMAT_DATETIME));
				baseDao.execute("PayJob.addBillCheck", paramMap);
				
				//增加账单流水明细记录表
				BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
				billTransDetailEntity.setThirdTransNo(entity.getThirdPayNumber());
				billTransDetailEntity.setPayTime(entity.getPayTime());
				billTransDetailEntity.setClientNo(entity.getPosClientNo());
				billTransDetailEntity.setTradeMoney(0d);
				billTransDetailEntity.setFee(0d);
				billTransDetailEntity.setCreateTime(new Date());
				billTransDetailEntity.setCreateUserId("SYS");
				baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
			}
			//增加或修改对账汇总记录
			addBillCheckSum(checkDate,billCheckLogDTO,billCheckSumDTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("对账系统异常：", e);
			throw e;
		}
		
		logger.info(payType + " " + checkDate + "，结束对账");
	}
	
	private void updatePayTradePos(OrderBillHessianDto bankStatementDTO) throws Exception {
		String bankCardInfo = bankStatementDTO.getCardType();
		
		String bankType = null;
		String cardType = null;
		if("同行借记卡".equals(bankCardInfo)){
			bankType = "1";
			cardType = "2";
		} else if("同行贷记卡".equals(bankCardInfo)){
			bankType = "1";
			cardType = "1";
		} else if("他行借记卡".equals(bankCardInfo)){
			bankType = "2";
			cardType = "2";
		} else if("他行贷记卡".equals(bankCardInfo)){
			bankType = "2";
			cardType = "1";
		} else {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("payCenterNumber", bankStatementDTO.getPayCenterNumber());
		paramMap.put("bankType", bankType); 
		paramMap.put("cardType", cardType);
		baseDao.execute("PayJob.updatePayTradePos", paramMap);
	}
	
	private void calculatePrice(ClearBillDTO clearBillDTO) throws Exception {
		logger.info("正在清分订单" + clearBillDTO.getOrderNo());
		String url = gdProperties.getProperties().getProperty("gd.api.ordrAct.url");
		logger.info("调用计算价格接口,地址:" + url);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("flag", "1");
		paramMap.put("orderNo", clearBillDTO.getOrderNo());
		if(StringUtils.isNotBlank(clearBillDTO.getBankType())){
			paramMap.put("busiType1", clearBillDTO.getBankType());
			paramMap.put("payChannel", clearBillDTO.getPayChannelCode());
			paramMap.put("busiType2", "0");
			paramMap.put("cardType", clearBillDTO.getCardType());
			paramMap.put("tradingFee",String.valueOf(clearBillDTO.getPlatFeeAmt()));
			Map<String, String> map = new HashMap<String, String>();
			if(clearBillDTO.getBuyerId() != null) {
				map.put("memberId", String.valueOf(clearBillDTO.getBuyerId()));
				String buyerSubsidyAmt = baseDao.queryForObject("PayJob.querySubsidyAmt", map, String.class);
				if(buyerSubsidyAmt == null) {
					buyerSubsidyAmt = "0";
				}
				paramMap.put("buyerSubsidyAmt", buyerSubsidyAmt);
			} else {
				paramMap.put("buyerSubsidyAmt", "0");
			}
			map.put("memberId", String.valueOf(clearBillDTO.getSellerId()));
			String sellerSubsidyAmt = baseDao.queryForObject("PayJob.querySubsidyAmt", map, String.class);
			if(sellerSubsidyAmt == null) {
				sellerSubsidyAmt = "0";
			}
			paramMap.put("sellerSubsidyAmt", sellerSubsidyAmt);
		}
		logger.info("调用计算价格接口，请求参数:" + paramMap);
		String reqText = GdDes3.encode(JSON.toJSONString(paramMap));
		paramMap.clear();
		paramMap.put("param", reqText);
		String repText = HttpUtil.httpClientPost(url, paramMap);
		String decodeReptext = GdDes3.decode(repText);
		logger.info("调用计算价格接口，返回结果:" + decodeReptext);
		StatusCodeEnumWithInfoDTO resultDTO = JSON.parseObject(decodeReptext, StatusCodeEnumWithInfoDTO.class);
		if("0".equals(resultDTO.getStatusCode())) {
			GdOrderActivityResultDTO activityResultDTO = resultDTO.getObject();
			if(clearBillDTO.getBuyerId() != null) {
				GdOrderActivityInfoDTO buyerActivityDTO = activityResultDTO.getBuyerActInfo();
				if(buyerActivityDTO != null) {
					clearBillDTO.setBuyerCommissionAmt(buyerActivityDTO.getMarketCommision());
					logger.info("付款方支付市场佣金："+buyerActivityDTO.getMarketCommision());
					clearBillDTO.setBuyerSubsidyAmt(buyerActivityDTO.getSubsidy());
					logger.info("付款方补贴金额："+buyerActivityDTO.getSubsidy());
					//买家支付平台佣金
					clearBillDTO.setBuyerPlatCommissionAmt(buyerActivityDTO.getPlatCommision());
					logger.info("付款方支付平台佣金："+buyerActivityDTO.getPlatCommision());
					//买家退款收入金额
					Double penalTotal = addDouble(buyerActivityDTO.getPlatPenalty(),addDouble(buyerActivityDTO.getSellerPenalty(),buyerActivityDTO.getCompanyPenalty()));
					clearBillDTO.setBuyerRefundAmt(subtractDouble(buyerActivityDTO.getPrepayAmt(),penalTotal));
					logger.info("付款方退款金额："+subtractDouble(buyerActivityDTO.getPrepayAmt(),penalTotal));
					//卖家违约金收入金额
					clearBillDTO.setSellerPenaltyAmt(buyerActivityDTO.getSellerPenalty());
					logger.info("收款方违约金收入金额："+buyerActivityDTO.getSellerPenalty());
					//物流公司违约金收入金额
					clearBillDTO.setLogisPenaltyAmt(buyerActivityDTO.getCompanyPenalty());
					logger.info("物流公司违约金收入金额："+buyerActivityDTO.getCompanyPenalty());
					//平台违约金收入金额
					clearBillDTO.setPlatPenaltyAmt(buyerActivityDTO.getPlatPenalty());
					logger.info("平台违约金收入金额："+buyerActivityDTO.getPlatPenalty());
				}
				
			}
			GdOrderActivityInfoDTO sellerActivityDTO = activityResultDTO.getSellerActInfo();
			if(sellerActivityDTO != null) {
				clearBillDTO.setSellerCommissionAmt(sellerActivityDTO.getMarketCommision());
				logger.info("卖家支付市场佣金："+sellerActivityDTO.getMarketCommision());
				clearBillDTO.setSellerSubsidyAmt(sellerActivityDTO.getSubsidy());
				logger.info("卖家补贴金额："+sellerActivityDTO.getSubsidy());
				//卖家支付平台佣金
				clearBillDTO.setSellerPlatCommissionAmt(sellerActivityDTO.getPlatCommision());
				logger.info("卖家支付平台佣金："+sellerActivityDTO.getPlatCommision());
			}
		}else{
			logger.info("调用计算价格接口返回状态："+resultDTO.getStatusCode());
			throw new Exception("计算价格接口异常！");
			
		}
		logger.info("调用计算价格接口结束");
	}
	
	private void addClearBillInfo(ClearBillDTO clearBillDTO) throws Exception{
		logger.info("清算对象addClearBillInfo.ClearBillDTO：" + clearBillDTO);
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("orderNo", clearBillDTO.getOrderNo());
		paramMap.put("busiCategory", "trade");
		//判断是否已转结算
		Integer hasChangeCount = baseDao.queryForObject("PayJob.queryHasChangeCount", paramMap, Integer.class);
		if(hasChangeCount > 0){
			logger.info("该订单已转结算，不能清算");
			return;
		}
		//将历史数据变为无效
		Integer count = baseDao.queryForObject("PayJob.queryClearDetailValidCount", paramMap, Integer.class);
		if(count > 0){
			logger.info("将历史数据变为无效");
			baseDao.execute("PayJob.updateClearDetailInvalid", paramMap);
		}
		paramMap.put("clearTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		//增加买家货款数据
		Double tradeAmt = subtractDouble(clearBillDTO.getPayAmt(), 
				addDouble(clearBillDTO.getBuyerCommissionAmt(),clearBillDTO.getBuyerPlatCommissionAmt()));
		if(clearBillDTO.getBuyerId() != null) {
			logger.info("增加买家货款数据:" + tradeAmt);
			paramMap.put("memberId", clearBillDTO.getBuyerId());
			if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
					("nst_car".equalsIgnoreCase(clearBillDTO.getAppKey()) || "nst_fare".equalsIgnoreCase(clearBillDTO.getAppKey()))){
				paramMap.put("userType", "nst_car");
			} else if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
					"nst_goods".equalsIgnoreCase(clearBillDTO.getAppKey())){
				paramMap.put("userType", "nst_goods");
			} else {
				paramMap.put("userType", "nsy");
			}
			paramMap.put("feeType", "100001");
			paramMap.put("szType", "2");
			paramMap.put("tradeAmt", tradeAmt);
			addClearDetail(paramMap);
		}
		//增加卖家货款数据
//		Double tradeAmt = subtractDouble(clearBillDTO.getPayAmt(), 
//				addDouble(clearBillDTO.getSellerCommissionAmt(),clearBillDTO.getSellerPlatCommissionAmt()));
		logger.info("增加卖家货款数据:" + tradeAmt);
		paramMap.put("memberId", clearBillDTO.getSellerId());
		if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) 
				&& "nst_car".equalsIgnoreCase(clearBillDTO.getAppKey())){
			paramMap.put("userType", "logis");
		} else if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
				"nst_goods".equalsIgnoreCase(clearBillDTO.getAppKey())){
			paramMap.put("userType", "nst_car");
		} else if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
				"nst_fare".equalsIgnoreCase(clearBillDTO.getAppKey())){
			paramMap.put("userType", "nst_goods");
		} else {
			paramMap.put("userType", "nps");
		}
		paramMap.put("feeType", "100001");
		paramMap.put("szType", "1");
		paramMap.put("tradeAmt", tradeAmt);
		addClearDetail(paramMap);
		if(clearBillDTO.getPlatFeeAmt() != null && clearBillDTO.getPlatFeeAmt() > 0){
			//增加卖家手续费数据
			logger.info("增加卖家手续费数据:" + clearBillDTO.getPlatFeeAmt());
			paramMap.put("memberId", clearBillDTO.getSellerId());
			if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
					"nst_car".equalsIgnoreCase(clearBillDTO.getAppKey())){
				paramMap.put("userType", "logis");
			} else if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
					"nst_goods".equalsIgnoreCase(clearBillDTO.getAppKey())){
				paramMap.put("userType", "nst_car");
			} else if(("ALIPAY_H5".equals(clearBillDTO.getClearType()) || "WEIXIN_APP".equals(clearBillDTO.getClearType())) && 
					"nst_fare".equalsIgnoreCase(clearBillDTO.getAppKey())){
				paramMap.put("userType", "nst_goods");
			} else {
				paramMap.put("userType", "nps");
			}
			paramMap.put("feeType", "100003");
			paramMap.put("szType", "2");
			paramMap.put("tradeAmt", clearBillDTO.getPlatFeeAmt());
			addClearDetail(paramMap);
			//增加平台手续费数据
			logger.info("增加平台手续费数据:" + clearBillDTO.getPlatFeeAmt());
			paramMap.put("memberId", clearBillDTO.getPlatUserId());
			paramMap.put("userType", "plat");
			paramMap.put("feeType", "100003");
			paramMap.put("szType", "1");
			paramMap.put("tradeAmt", clearBillDTO.getPlatFeeAmt());
			addClearDetail(paramMap);
		}
		
		if("POS".equals(clearBillDTO.getClearType())) {
			if(clearBillDTO.getBuyerId() != null && clearBillDTO.getBuyerCommissionAmt() != null && clearBillDTO.getBuyerCommissionAmt() > 0){
				//增加买家佣金数据
				logger.info("增加买家佣金数据:" + clearBillDTO.getBuyerCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getBuyerId());
				paramMap.put("userType", "nsy");
				paramMap.put("feeType", "100002");
				paramMap.put("szType", "2");
				paramMap.put("tradeAmt", clearBillDTO.getBuyerCommissionAmt());
				Integer clearId = addClearDetail(paramMap);
				//增加市场收买家佣金数据
				logger.info("增加市场收买家佣金数据:" + clearBillDTO.getBuyerCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getMarketUserId());
				paramMap.put("userType", "market");
				paramMap.put("feeType", "100002");
				paramMap.put("szType", "1");
				paramMap.put("tradeAmt", clearBillDTO.getBuyerCommissionAmt());
				Integer refId = addClearDetail(paramMap);
				if(clearId.intValue() > 0 && refId.intValue() > 0) {
					logger.info("增加清算关联表数据clearId=" + clearId + ",refId=" + refId);
					addRefClearRelate(clearId, refId);
				}
			}
			if(clearBillDTO.getSellerCommissionAmt() != null && clearBillDTO.getSellerCommissionAmt() > 0){
				//增加卖家佣金数据
				logger.info("增加卖家佣金数据:" + clearBillDTO.getSellerCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getSellerId());
				paramMap.put("userType", "nps");
				paramMap.put("feeType", "100002");
				paramMap.put("szType", "2");
				paramMap.put("tradeAmt", clearBillDTO.getSellerCommissionAmt());
				Integer clearId = addClearDetail(paramMap);
				//增加市场收卖家佣金数据
				logger.info("增加市场收卖家佣金数据:" + clearBillDTO.getSellerCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getMarketUserId());
				paramMap.put("userType", "market");
				paramMap.put("feeType", "100002");
				paramMap.put("szType", "1");
				paramMap.put("tradeAmt", clearBillDTO.getSellerCommissionAmt());
				Integer refId = addClearDetail(paramMap);
				if(clearId.intValue() > 0 && refId.intValue() > 0) {
					logger.info("增加清算关联表数据clearId=" + clearId + ",refId=" + refId);
					addRefClearRelate(clearId, refId);
				}
			}
			//*************************version:1220(6+1) by hxp date:2016/12/8 beging*******************//
			/**
			 *买家平台佣金
			 */
			if(clearBillDTO.getBuyerId() != null && clearBillDTO.getBuyerPlatCommissionAmt() != null && clearBillDTO.getBuyerPlatCommissionAmt() > 0){
				//增加买家佣金数据
				logger.info("增加买家平台佣金数据:" + clearBillDTO.getBuyerPlatCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getBuyerId());
				paramMap.put("userType", "nsy");
				paramMap.put("feeType", "100005");
				paramMap.put("szType", "2");
				paramMap.put("tradeAmt", clearBillDTO.getBuyerPlatCommissionAmt());
				Integer clearId = addClearDetail(paramMap);
				//增加平台佣金数据
				logger.info("增加买家平台佣金数据:" + clearBillDTO.getBuyerPlatCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getPlatUserId());
				paramMap.put("userType", "plat");
				paramMap.put("feeType", "100005");
				paramMap.put("szType", "1");
				paramMap.put("tradeAmt", clearBillDTO.getBuyerPlatCommissionAmt());
				Integer refId = addClearDetail(paramMap);
				if(clearId.intValue() > 0 && refId.intValue() > 0) {
					logger.info("增加清算关联表数据clearId=" + clearId + ",refId=" + refId);
					addRefClearRelate(clearId, refId);
				}
			}
			
			/**
			 *卖家平台佣金
			 */
			if(clearBillDTO.getSellerId() != null && clearBillDTO.getSellerPlatCommissionAmt() != null && clearBillDTO.getSellerPlatCommissionAmt() > 0){
				//增加买家佣金数据
				logger.info("增加卖家平台佣金数据:" + clearBillDTO.getSellerPlatCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getSellerId());
				paramMap.put("userType", "nps");
				paramMap.put("feeType", "100005");
				paramMap.put("szType", "2");
				paramMap.put("tradeAmt", clearBillDTO.getSellerPlatCommissionAmt());
				Integer clearId = addClearDetail(paramMap);
				//增加平台佣金数据
				logger.info("增加卖家平台佣金数据:" + clearBillDTO.getSellerPlatCommissionAmt());
				paramMap.put("memberId", clearBillDTO.getPlatUserId());
				paramMap.put("userType", "plat");
				paramMap.put("feeType", "100005");
				paramMap.put("szType", "1");
				paramMap.put("tradeAmt", clearBillDTO.getSellerPlatCommissionAmt());
				Integer refId = addClearDetail(paramMap);
				if(clearId.intValue() > 0 && refId.intValue() > 0) {
					logger.info("增加清算关联表数据clearId=" + clearId + ",refId=" + refId);
					addRefClearRelate(clearId, refId);
				}
			}
			//*************************version:1220(6+1) by hxp date:2016/12/8 end*******************//
			if(clearBillDTO.getBuyerId() != null && clearBillDTO.getBuyerSubsidyAmt() != null && clearBillDTO.getBuyerSubsidyAmt() > 0){
				//增加买家补贴数据
				logger.info("增加买家补贴数据:" + clearBillDTO.getBuyerSubsidyAmt());
				paramMap.put("memberId", clearBillDTO.getBuyerId());
				paramMap.put("userType", "nsy");
				paramMap.put("feeType", "100004");
				paramMap.put("szType", "1");
				paramMap.put("tradeAmt", clearBillDTO.getBuyerSubsidyAmt());
				addClearDetail(paramMap);
				//增加平台给买家补贴数据
				logger.info("增加平台给买家补贴数据:" + clearBillDTO.getBuyerSubsidyAmt());
				paramMap.put("memberId", clearBillDTO.getPlatUserId());
				paramMap.put("userType", "plat");
				paramMap.put("feeType", "100004");
				paramMap.put("szType", "2");
				paramMap.put("tradeAmt", clearBillDTO.getBuyerSubsidyAmt());
				addClearDetail(paramMap);
			}
			if(clearBillDTO.getSellerSubsidyAmt() != null && clearBillDTO.getSellerSubsidyAmt() > 0){
				//增加卖家补贴数据
				logger.info("增加卖家补贴数据:" + clearBillDTO.getSellerSubsidyAmt());
				paramMap.put("memberId", clearBillDTO.getSellerId());
				paramMap.put("userType", "nps");
				paramMap.put("feeType", "100004");
				paramMap.put("szType", "1");
				paramMap.put("tradeAmt", clearBillDTO.getSellerSubsidyAmt());
				addClearDetail(paramMap);
				//增加平台给卖家补贴数据
				logger.info("增加平台给卖家补贴数据:" + clearBillDTO.getSellerSubsidyAmt());
				paramMap.put("memberId", clearBillDTO.getPlatUserId());
				paramMap.put("userType", "plat");
				paramMap.put("feeType", "100004");
				paramMap.put("szType", "2");
				paramMap.put("tradeAmt", clearBillDTO.getSellerSubsidyAmt());
				addClearDetail(paramMap);
			}
		}
	}
	
	private void addRefundClearBillInfo(ClearBillDTO clearBillDTO) throws Exception{
		logger.info("清算对象addClearBillInfo.ClearBillDTO：" + clearBillDTO);
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("orderNo", clearBillDTO.getOrderNo());
		paramMap.put("busiCategory", "refund");
		//判断是否已转结算
		Integer hasChangeCount = baseDao.queryForObject("PayJob.queryHasChangeCount", paramMap, Integer.class);
		if(hasChangeCount > 0){
			logger.info("该订单已转结算，不能清算");
			return;
		}
		//将历史数据变为无效
		Integer count = baseDao.queryForObject("PayJob.queryClearDetailValidCount", paramMap, Integer.class);
		if(count > 0){
			logger.info("将历史数据变为无效");
			baseDao.execute("PayJob.updateClearDetailInvalid", paramMap);
		}
		paramMap.put("clearTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		/**
		 * 退款
		 */
		//增加卖家违约金数据
		if(clearBillDTO.getSellerPenaltyAmt() != null && clearBillDTO.getSellerPenaltyAmt() > 0){
			logger.info("增加卖家违约金数据:" + clearBillDTO.getSellerPenaltyAmt());
			paramMap.put("memberId", clearBillDTO.getSellerId());
			paramMap.put("userType", "nps");
			paramMap.put("feeType", "100006");
			paramMap.put("szType", "1");
			paramMap.put("tradeAmt", clearBillDTO.getSellerPenaltyAmt());
			addClearDetail(paramMap);
		}
		//增加平台违约金数据
		if(clearBillDTO.getPlatPenaltyAmt() != null && clearBillDTO.getPlatPenaltyAmt() > 0){
			logger.info("增加平台违约金数据:" + clearBillDTO.getPlatPenaltyAmt());
			paramMap.put("memberId", clearBillDTO.getPlatUserId());
			paramMap.put("userType", "plat");
			paramMap.put("feeType", "100006");
			paramMap.put("szType", "1");
			paramMap.put("tradeAmt", clearBillDTO.getPlatPenaltyAmt());
			addClearDetail(paramMap);
		}
		//增加物流公司违约金数据
		if(clearBillDTO.getLogisPenaltyAmt() != null && clearBillDTO.getLogisPenaltyAmt() > 0){
			logger.info("增加物流公司违约金数据:" + clearBillDTO.getLogisPenaltyAmt());
			paramMap.put("memberId", clearBillDTO.getLogisUserId());
			paramMap.put("userType", "logis");
			paramMap.put("feeType", "100006");
			paramMap.put("szType", "1");
			paramMap.put("tradeAmt", clearBillDTO.getLogisPenaltyAmt());
			addClearDetail(paramMap);
		}
		//增加退款数据
		if (clearBillDTO.getBuyerRefundAmt() != null && clearBillDTO.getBuyerRefundAmt() > 0) {
			logger.info("增加物流公司违约金数据:" + clearBillDTO.getBuyerRefundAmt());
			paramMap.put("memberId", clearBillDTO.getBuyerId());
			paramMap.put("userType", "nsy");
			paramMap.put("feeType", "100007");
			paramMap.put("szType", "1");
			paramMap.put("tradeAmt", clearBillDTO.getBuyerRefundAmt());
			addClearDetail(paramMap);
		}
	}
	
	private Integer addClearDetail(Map<String, Object> paramMap){
		ClearDetailEntity entity = new ClearDetailEntity();
		entity.setClearTime(new Date());
		entity.setCreateTime(entity.getClearTime());
		entity.setOrderNo(Long.valueOf(paramMap.get("orderNo").toString()));
		entity.setMemberId(Long.valueOf(paramMap.get("memberId").toString()));
		entity.setUserType(String.valueOf(paramMap.get("userType")));
		entity.setFeeType(String.valueOf(paramMap.get("feeType")));
		entity.setSzType(String.valueOf(paramMap.get("szType")));
		entity.setTradeAmt(Double.valueOf(String.valueOf(paramMap.get("tradeAmt"))));
		entity.setHasChange("0");
		entity.setIsValid("1");
		entity.setCreateUserId("SYS");
		entity.setUpdateUserId("SYS");
		entity.setBusiCategory(String.valueOf(paramMap.get("busiCategory")));
		Long id = baseDao.persist(entity, long.class);
		return Integer.valueOf(id.toString());
	}
	
	private void addRefClearRelate(Integer clearId, Integer refId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clearId", clearId);
		paramMap.put("refId", refId);
		baseDao.execute("PayJob.addRefClearRelate", paramMap);
	}
	
	public void addBillCheckSum(String checkDate, BillCheckSumDTO billCheckLogDTO,
			BillCheckSumDTO billCheckSumDTO) throws Exception{
		billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckSuccessNum() + billCheckSumDTO.getCheckFailNum());
		billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckSuccessAmt(), billCheckSumDTO.getCheckFailAmt()));
		billCheckSumDTO.setCreateUserId("SYS");
		billCheckSumDTO.setPayTimeStr(checkDate);
		billCheckLogDTO.setCreateUserId("SYS");
		billCheckLogDTO.setPayTimeStr(checkDate);
		logger.info("对账汇总记录对象，" + billCheckSumDTO);
		if(billCheckSumDTO.getId() == null){
			logger.info("增加对账汇总记录");
			baseDao.execute("PayJob.addBillCheckSum", billCheckSumDTO);
		} else {
			billCheckSumDTO.setId(billCheckSumDTO.getId());
			billCheckSumDTO.setUpdateUserId("SYS");
			logger.info("更新对账汇总记录");
			int count = baseDao.execute("PayJob.updateBillCheckSum", billCheckSumDTO);
			if(count == 0) {
				logger.error("对账版本号不正确");
				throw new Exception("对账版本号不正确");
			}
		}
		logger.info("更新对账日志");
		baseDao.execute("PayJob.addBillCheckLog", billCheckLogDTO);
	}
	
	public BillCheckSumDTO processAlipayBill(BillChecks billChecks) throws Exception {
		BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
		//获得对账数据
		List<BillCheckDetailDto> billCheckDetails = billChecks.getBillDetail();
		/*
		 * 如果没有对账数据，则返回
		 */
		if(billCheckDetails == null || billCheckDetails.size() == 0) {
			return billCheckSumDTO;
		}
		logger.info("获取对账文件记录数：" + billCheckDetails.size());
		/*
		 * 循环更新服务手续费
		 */
		for (int i = 0; i < billCheckDetails.size(); i++) {
			BillCheckDetailDto bill = billCheckDetails.get(i);
			logger.info("来自支付宝的对账明细记录:" + bill);
			//判断是否存在此数据
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("thirdPayNumber", bill.getThirdPayNumber());
			BillCheckDetailDto billCheckDetailDto = queryBillCheckDetail(paramMap);
			logger.info("来自平台的对账明细记录:" + billCheckDetailDto);
			if(billCheckDetailDto == null || "2".equals(billCheckDetailDto.getCheckStatus())) {
				//获得对应的pay_trade记录进行对账
				Map<String, Object> params = new HashMap<>();
				params.put("payCenterNumber", bill.getPayCenterNumber());
				params.put("payStatus", "2");
				List<PayTradeEntity> payTrades = payTradeService.queryPayTrade(params);
				/*
				 * 比较金额
				 */
				if(payTrades.size() !=0 ) {
					//获得对应的交易记录
//					PayTradeEntity basePayTrade = payTrades.get(0);
					Double payAmt = 0D;
					for(PayTradeEntity dto : payTrades){
						payAmt = addDouble(payAmt,dto.getPayAmt());
					}
					logger.info("平台流水号:" + bill.getPayCenterNumber());
//					if(basePayTrade.getPayAmt().equals(bill.getPayAmt())) {
					if(payAmt.equals(bill.getPayAmt())) {
						bill.setPayTime(bill.getTrans_date());
						bill.setCheckStatus("1"); //金额一样 对账成功
						/*
						 * 只有对账成功，才能添加手续费
						 * 判断是否已经添加过手续费 getFeeRecordCount，当没有对应的手续费交易记录时 则添加
						 */
						if (upHasFeeRecord(bill.getPayCenterNumber())) {
							changeTradeAndFee(bill, payTrades);
						}
						//记录对账日志
						billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckNum() + 1);
						billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckAmt(),payAmt));
						billCheckSumDTO.setCheckSuccessNum(billCheckSumDTO.getCheckSuccessNum() + 1);
						billCheckSumDTO.setCheckSuccessAmt(addDouble(billCheckSumDTO.getCheckSuccessAmt(), payAmt));
					} else {
						logger.info("金额不一致，支付宝：" + String.format("%.2f", bill.getPayAmt()) + 
								" 交易记录:" + String.format("%.2f", payAmt));
						bill.setPayTime(bill.getTrans_date());
						bill.setCheckStatus("2");
						bill.setResultType("1");//1我方有他方有
						bill.setFailReason("金额不一致，支付宝：" + String.format("%.2f", bill.getPayAmt()) + 
								" 交易记录:" + String.format("%.2f", payAmt));
						//记录对账日志
						billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckNum() + 1);
						billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckAmt(),payAmt));
						billCheckSumDTO.setCheckFailNum(billCheckSumDTO.getCheckFailNum() + 1);
						billCheckSumDTO.setCheckFailAmt(addDouble(billCheckSumDTO.getCheckFailAmt(), payAmt));
						//增加账单流水明细记录表
						BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
						billTransDetailEntity.setThirdTransNo(bill.getThirdPayNumber());
						billTransDetailEntity.setTradeMoney(bill.getPayAmt());
						billTransDetailEntity.setFee(bill.getFeeAmt());
						billTransDetailEntity.setCardNo(payTrades.get(0).getThirdPayerAccount());
						billTransDetailEntity.setPayTime(bill.getTrans_date());
						billTransDetailEntity.setCreateTime(new Date());
						billTransDetailEntity.setCreateUserId("SYS");
						baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
					}
				} else {
					logger.info("支付系统没有第三方流水号:" + bill.getThirdPayNumber());
					bill.setPayTime(bill.getTrans_date());
					bill.setCheckStatus("2");
					bill.setResultType("3");//3我方无他方有
					bill.setFailReason("支付系统没有第三方流水号:" + bill.getThirdPayNumber());
					//记录对账日志
					billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckNum() + 1);
					billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckAmt(),bill.getPayAmt()));
					billCheckSumDTO.setCheckFailNum(billCheckSumDTO.getCheckFailNum() + 1);
					billCheckSumDTO.setCheckFailAmt(addDouble(billCheckSumDTO.getCheckFailAmt(), bill.getPayAmt()));
					//增加账单流水明细记录表
					BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
					billTransDetailEntity.setThirdTransNo(bill.getThirdPayNumber());
					billTransDetailEntity.setTradeMoney(bill.getPayAmt());
					billTransDetailEntity.setFee(bill.getFeeAmt());
//					billTransDetailEntity.setCardNo(payTrades.get(0).getThirdPayerAccount());
					billTransDetailEntity.setPayTime(bill.getTrans_date());
					billTransDetailEntity.setCreateTime(new Date());
					billTransDetailEntity.setCreateUserId("SYS");
					baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
				}
				if(billCheckDetailDto == null) {
					//添加
					logger.info("增加对账明细记录");
					bill.setPayChannelCode("ALIPAY_H5");
					addBillCheck(bill);
				} else if("2".equals(billCheckDetailDto.getCheckStatus()) && "1".equals(bill.getCheckStatus())){
					logger.info("更新对账明细记录");
					bill.setPayChannelCode("ALIPAY_H5");
					updateBillCheck(bill);
				}
			}
//			else {
//				billCheckSumDTO.setCheckSuccessNum(billCheckSumDTO.getCheckSuccessNum() + 1);
//				billCheckSumDTO.setCheckSuccessAmt(addDouble(billCheckSumDTO.getCheckSuccessAmt(), billCheckDetailDto.getPayAmt()));
//			}
		}
		return billCheckSumDTO;
	}

	public BillCheckSumDTO processWeChatBill(BillChecks billChecks) throws Exception {
		BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
		//获得对账数据
		List<BillCheckDetailDto> billCheckDetails = billChecks.getBillDetail();
		/*
		 * 如果没有对账数据，则返回
		 */
		if(billCheckDetails == null || billCheckDetails.size() == 0) {
			return billCheckSumDTO;
		}
		logger.info("获取对账文件记录数：" + billCheckDetails.size());
		/*
		 * 循环更新服务手续费
		 */
		for (int i = 0; i < billCheckDetails.size(); i++) {
			BillCheckDetailDto bill = billCheckDetails.get(i);
			logger.info("来自微信的对账明细记录:" + bill);
			//判断是否存在此数据
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("thirdPayNumber", bill.getThirdPayNumber());
			BillCheckDetailDto billCheckDetailDto = queryBillCheckDetail(paramMap);
			logger.info("来自平台的对账明细记录:" + billCheckDetailDto);
			if(billCheckDetailDto == null || "2".equals(billCheckDetailDto.getCheckStatus())) {
				//获得对应的pay_trade记录进行对账
				Map<String, Object> params = new HashMap<>();
				params.put("payCenterNumber", bill.getPayCenterNumber());
				params.put("payStatus", "2");
				List<PayTradeEntity> payTrades = payTradeService.queryPayTrade(params);
				/*
				 * 比较金额
				 */
				if(payTrades.size() !=0 ) {
					//获得对应的交易记录
//					PayTradeEntity basePayTrade = payTrades.get(0);
					Double payAmt = 0D;
					for(PayTradeEntity dto : payTrades){
						payAmt = addDouble(payAmt,dto.getPayAmt());
					}
					logger.info("平台流水号:" + bill.getPayCenterNumber());
//					if(basePayTrade.getPayAmt().equals(bill.getPayAmt())) {
					if(payAmt.equals(bill.getPayAmt())) {
						bill.setPayTime(bill.getTrans_date());
						bill.setCheckStatus("1"); //金额一样 对账成功
						/*
						 * 只有对账成功，才能添加手续费
						 * 判断是否已经添加过手续费 getFeeRecordCount，当没有对应的手续费交易记录时 则添加
						 */
						if (upHasFeeRecord(bill.getPayCenterNumber())) {
							changeTradeAndFee(bill, payTrades);
						}
						//记录对账日志
						billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckNum() + 1);
						billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckAmt(),payAmt));
						billCheckSumDTO.setCheckSuccessNum(billCheckSumDTO.getCheckSuccessNum() + 1);
						billCheckSumDTO.setCheckSuccessAmt(addDouble(billCheckSumDTO.getCheckSuccessAmt(), payAmt));
					} else {
						logger.info("金额不一致，微信：" + String.format("%.2f", bill.getPayAmt()) + 
								" 交易记录:" + String.format("%.2f", payAmt));
						bill.setPayTime(bill.getTrans_date());
						bill.setCheckStatus("2");
						bill.setResultType("1");//1我方有他方有
						bill.setFailReason("金额不一致，微信：" + String.format("%.2f", bill.getPayAmt()) + 
								" 交易记录:" + String.format("%.2f", payAmt));
						//记录对账日志
						billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckNum() + 1);
						billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckAmt(),payAmt));
						billCheckSumDTO.setCheckFailNum(billCheckSumDTO.getCheckFailNum() + 1);
						billCheckSumDTO.setCheckFailAmt(addDouble(billCheckSumDTO.getCheckFailAmt(), payAmt));
						//增加账单流水明细记录表
						BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
						billTransDetailEntity.setThirdTransNo(bill.getThirdPayNumber());
						billTransDetailEntity.setTradeMoney(bill.getPayAmt());
						billTransDetailEntity.setFee(bill.getFeeAmt());
						billTransDetailEntity.setCardNo(payTrades.get(0).getThirdPayerAccount());
						billTransDetailEntity.setPayTime(bill.getTrans_date());
						billTransDetailEntity.setCreateTime(new Date());
						billTransDetailEntity.setCreateUserId("SYS");
						baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
					}
				} else {
					logger.info("支付系统没有第三方流水号:" + bill.getThirdPayNumber());
					bill.setPayTime(bill.getTrans_date());
					bill.setCheckStatus("2");
					bill.setResultType("3");//3我方无他方有
					bill.setFailReason("支付系统没有第三方流水号:" + bill.getThirdPayNumber());
					//记录对账日志
					billCheckSumDTO.setCheckNum(billCheckSumDTO.getCheckNum() + 1);
					billCheckSumDTO.setCheckAmt(addDouble(billCheckSumDTO.getCheckAmt(),bill.getPayAmt()));
					billCheckSumDTO.setCheckFailNum(billCheckSumDTO.getCheckFailNum() + 1);
					billCheckSumDTO.setCheckFailAmt(addDouble(billCheckSumDTO.getCheckFailAmt(), bill.getPayAmt()));
					//增加账单流水明细记录表
					BillTransDetailEntity billTransDetailEntity = new BillTransDetailEntity();
					billTransDetailEntity.setThirdTransNo(bill.getThirdPayNumber());
					billTransDetailEntity.setTradeMoney(bill.getPayAmt());
					billTransDetailEntity.setFee(bill.getFeeAmt());
//					billTransDetailEntity.setCardNo(payTrades.get(0).getThirdPayerAccount());
					billTransDetailEntity.setPayTime(bill.getTrans_date());
					billTransDetailEntity.setCreateTime(new Date());
					billTransDetailEntity.setCreateUserId("SYS");
					baseDao.persist(billTransDetailEntity, BillTransDetailEntity.class);
				}
				if(billCheckDetailDto == null) {
					//添加
					logger.info("增加对账明细记录");
					bill.setPayChannelCode("WEIXIN_APP");
					addBillCheck(bill);
				} else if("2".equals(billCheckDetailDto.getCheckStatus()) && "1".equals(bill.getCheckStatus())){
					logger.info("更新对账明细记录");
					bill.setPayChannelCode("WEIXIN_APP");
					updateBillCheck(bill);
				}
			}
//			else {
//				billCheckSumDTO.setCheckSuccessNum(billCheckSumDTO.getCheckSuccessNum() + 1);
//				billCheckSumDTO.setCheckSuccessAmt(addDouble(billCheckSumDTO.getCheckSuccessAmt(), billCheckDetailDto.getPayAmt()));
//			}
		}
		return billCheckSumDTO;
	}
	
	private void changeTradeAndFee(BillCheckDetailDto bill, List<PayTradeEntity> basePayTradeList) {
		PayTradeEntity payTrade = new PayTradeEntity();
		Double feeAmt = bill.getFeeAmt();
		for (PayTradeEntity payTradeEntity : basePayTradeList) {
			//实际交易手续费没有则按费率计算
			if (bill.getFeeAmt() == null) {
				payTrade.setRate(bill.getRate() != null ? bill.getRate().toString() : null);
				feeAmt = MathUtil.round(MathUtil.mul(payTradeEntity.getPayAmt(), bill.getRate()), 2);
			} else {
				if (bill.getFeeAmt() != 0d) {
					feeAmt = MathUtil.div(MathUtil.mul(payTradeEntity.getPayAmt(), bill.getFeeAmt()),bill.getPayAmt());
				}
			}
			payTrade.setFeeAmt(feeAmt);
			// 实收金额
			payTrade.setReceiptAmt(MathUtil.sub(payTradeEntity.getPayAmt(), feeAmt));
			
			//更新订单
			payTrade.setOrderNo(payTradeEntity.getOrderNo());
			payTrade.setPayCenterNumber(payTradeEntity.getPayCenterNumber());
			// 更新支付对象
			logger.info("更新交易记录,feeAmt=" + payTrade.getFeeAmt() + ",receiptAmt" + bill.getReceiptAmt());
			payTradeService.updateByOrderNo(payTrade);
		}
		/*
		 * 添加手续费数据
		 */
		FeeRecordEntity fee = new FeeRecordEntity();
		fee.setPayCenterNumber(bill.getPayCenterNumber());
//		fee.setOrderNo(basePayTradeList.get(0).getOrderNo());
		fee.setFeeAmt(feeAmt);
		fee.setPayAmt(bill.getPayAmt());
		fee.setOperaUserId("sys");
		fee.setFeeType("0");
		fee.setFinanceTime(basePayTradeList.get(0).getPayTime());
		fee.setRate(bill.getRate() == null ? "" : bill.getRate().toString());
		fee.setThridPayNumber(bill.getThirdPayNumber());
		fee.setCreateUserId("sys");
		fee.setUpdateUserId("sys");

		Map<String, Object> params = DalUtils.convertToMap(fee);
		logger.info("更新手续费," + params);
		baseDao.execute("PayJob.addFeeRecord", params);
		/**
		// 计算修改对应的值
		PayTradeEntity payTrade = new PayTradeEntity();
		// 设置修改条件
		payTrade.setPayCenterNumber(basePayTradeList.get(0).getPayCenterNumber());
		Double payAmt = 0d;
		// 费率
		Double feeAmt = 0d;
		if (bill.getFeeAmt() == null || bill.getFeeAmt() == 0d) {
			payTrade.setRate(bill.getRate() != null ? bill.getRate().toString() : null);

			for (PayTradeEntity payTradeEntity : basePayTradeList) {
				payAmt = addDouble(payAmt, payTradeEntity.getPayAmt());
			}
			feeAmt = MathUtil.round(MathUtil.mul(payAmt, bill.getRate()), 2);
			// 服务费
		} else {
//			feeAmt = bill.getFeeAmt();
			for (PayTradeEntity payTradeEntity : basePayTradeList) {
				MathUtil.div(bill.getPayAmt(), MathUtil.mul(payTradeEntity.getPayAmt(), bill.getFeeAmt()));
				feeAmt = bill.getFeeAmt();
			}
		}
		payTrade.setFeeAmt(feeAmt);

		// 实收金额
		payTrade.setReceiptAmt(MathUtil.sub(payAmt, feeAmt));

		// 更新支付对象
		logger.info("更新交易记录,feeAmt=" + payTrade.getFeeAmt() + ",receiptAmt" + bill.getReceiptAmt());
		payTradeService.updatePayTrade(payTrade);
	**/
		/*
		 * 添加手续费数据
		 */
//		FeeRecordEntity fee = new FeeRecordEntity();
//		fee.setPayCenterNumber(bill.getPayCenterNumber());
//		fee.setOrderNo(basePayTradeList.get(0).getOrderNo());
//		fee.setFeeAmt(payTrade.getFeeAmt());
//		fee.setPayAmt(payAmt);
//		fee.setOperaUserId("sys");
//		fee.setFeeType("0");
//		fee.setFinanceTime(basePayTradeList.get(0).getPayTime());
//		fee.setRate(bill.getRate() == null ? "" : bill.getRate().toString());
//		fee.setThridPayNumber(basePayTradeList.get(0).getThirdPayNumber());
//		fee.setCreateUserId("sys");
//		fee.setUpdateUserId("sys");
//
//		Map<String, Object> params = DalUtils.convertToMap(fee);
//		logger.info("更新手续费," + params);
//		baseDao.execute("PayJob.addFeeRecord", params);
	}

	@Override
	public void closeTrade() {
		
		try {

			/*
			 * 获取所有未付款超时订单
			 */
			List<PayTradeEntity> outtimeTrade = baseDao.queryForList("PayJob.queryListForJobOuttime", new HashMap<>(),
					PayTradeEntity.class);

			/*
			 * 循环未付款订单，查询支付宝接口 真实付款情况
			 */
			Map<String, Object> paramsMap = new HashMap<>();
			for (int i = 0; i < outtimeTrade.size(); i++) {
				PayTradeEntity payTrade = outtimeTrade.get(i);
	
				// 调用接口查询是否真的未付款 此处耦合性很高
				if (payTrade != null && payTrade.getPayType() != null) {
					if (payTrade.getPayType().indexOf("ALIPAY") > -1) {
						/*
						 * 获取对应的支付宝具体支付状态
						 */
						TradeQueryRequestDto requestDto = new TradeQueryRequestDto();
						requestDto.setPayCenterNumber(payTrade.getPayCenterNumber());
						requestDto.setAppKey(payTrade.getAppKey());
						
						//获得支付宝中支付状态
						TradeQueryResponseDto responseDto = tradeQueryService.queryTrade(requestDto);
					
						/*
						 * 判断状态是否为待支付
						 */
						if (responseDto == null || PayStatus.READY_PAY.equals(responseDto.getPayStatus())) {
							
							/*
							 * 关闭对应支付信息 已超时
							 */
							paramsMap.put("id", payTrade.getId());
							paramsMap.put("closeUserId", "sys");
							paramsMap.put("payStatus", "3");
				
							// 修改状态为关闭
							baseDao.execute("PayJob.updatePayTradeForJob", paramsMap);
						} else {
							//不做处理
						}
					
					}
				}
	
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自动关闭订单失败：" + e.getMessage());
		}

	}
	
	/*private void addMoreBillCheck(List<BillCheckDetailDto> billChecks) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("billChecks", billChecks);
		baseDao.execute("PayJob.addMoreBillCheck", paramMap);
	}*/
	
	private void addBillCheck(BillCheckDetailDto billCheck) {
		Map<String, Object> paramMap = DalUtils.convertToMap(billCheck);
		paramMap.put("createUserId", "SYS");
		paramMap.put("updateUserId", "SYS");
		baseDao.execute("PayJob.addBillCheck", paramMap);
	}
	
	private void updateBillCheck(BillCheckDetailDto billCheck) {
		Map<String, Object> paramMap = DalUtils.convertToMap(billCheck);
		paramMap.put("updateUserId", "SYS");
		baseDao.execute("PayJob.updateBillCheck", paramMap);
	}
	
	private BillCheckDetailDto queryBillCheckDetail(Map<String, Object> paramMap) {
		return baseDao.queryForObject("PayJob.queryBillCheckDetail", paramMap, BillCheckDetailDto.class);
	}
	
	private List<PayTradeDTO> queryClearBillList(Map<String, Object> paramMap) {
		return baseDao.queryForList("PayJob.queryClearBillList", paramMap, PayTradeDTO.class);
	}
	
	private List<PayTradeDTO> queryClearBillList1220(Map<String, Object> paramMap) {
		return baseDao.queryForList("PayJob.queryClearBillList1220", paramMap, PayTradeDTO.class);
	}
	
	private Integer queryClearBillCount(Map<String, Object> paramMap) {
		return baseDao.queryForObject("PayJob.queryClearBillCount", paramMap, Integer.class);
	}
	
	private boolean upHasFeeRecord(String payCenterNumber) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("payCenterNumber", payCenterNumber);
		Integer count = baseDao.queryForObject("PayJob.getFeeRecordCount", paramMap, Integer.class);
		logger.info("查询手续记录:count=" + count);
		if (count == 0) {
			return true;
		}
		return false;
	}
	
	private BillCheckSumDTO queryBillCheckSum(String payType, String payTime) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("payType", payType);
		paramMap.put("payTime", payTime);
		return baseDao.queryForObject("PayJob.queryBillCheckSum", paramMap, BillCheckSumDTO.class);
	}
	
	private List<OrderBillHessianDto> queryBankStatement(String payType, String checkDate) throws Exception{
		OrderBillHessianDto dto = new OrderBillHessianDto();
		dto.setPayChannelCode(payType);
		dto.setBillBeginTime(DateUtils.parseDate(checkDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		dto.setBillEndTime(DateUtils.parseDate(checkDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return orderBillHessianService.getOrderBillByCondition(dto);
	}


	/**
	 * 生成发送支付结果消息
	 * 
	 * @param pay
	 * @return
	 * @throws BizException
	 */
	@Transactional
	private PayNotifyResponse postPay(PayTradeEntity payTrade) throws BizException {
		PayNotifyResponse res = new PayNotifyResponse();
		BeanUtils.copyProperties(payTrade, res);
		//设置posClientNo
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("payCenterNumber", payTrade.getPayCenterNumber());
		PayTradePosEntity pos = baseDao.queryForObject("PayTrade.getPayTradePos", paramMap, PayTradePosEntity.class);
		if(pos != null){
			res.setPosClientNo(pos.getPosClientNo());
		}
		res.setRespCode(MsgCons.C_10000);
		res.setRespMsg(MsgCons.M_10000);

		return res;
	}
	
	public static Double addDouble(Double v1, Double v2) {
		if(v1 == null){
			v1 = 0d;
		}
		if(v2 == null){
			v2 = 0d;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
    }
	
	public static Double subtractDouble(Double v1, Double v2) {
		if(v1 == null){
			v1 = 0d;
		}
		if(v2 == null){
			v2 = 0d;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
    }
	
	public static Double mulDouble(Double v1, Double v2) {
		if(v1 == null){
			v1 = 0d;
		}
		if(v2 == null){
			v2 = 0d;
		}
		BigDecimal b1 = new BigDecimal(v1.toString());  
		BigDecimal b2 = new BigDecimal(v2.toString());  
		return new Double(b1.multiply(b2).doubleValue());  
	}
	
	public static Double divDouble(Double v1, Double v2, int scale) {
		if(v1 == null){
			v1 = 0d;
		}
		BigDecimal b1 = new BigDecimal(v1.toString());  
		BigDecimal b2 = new BigDecimal(v2.toString());  
		return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());  
	}  
	
	@Override
	public void checkBill(String payType, String checkDate) throws Exception{
		logger.info(payType + " " + checkDate + "，开始重新对账");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("payType", payType);
		paramMap.put("hasCheck", "1");
		Integer count = baseDao.queryForObject("PayJob.queryPayTypeCheckBillCount", paramMap, Integer.class);
		if(count != null && count > 0) {
			if("ALIPAY_H5".equalsIgnoreCase(payType)){
				checkAlipayBill(checkDate);
			} else if("WEIXIN_APP".equalsIgnoreCase(payType)){
				checkWeChatBill(checkDate);
			} else {
				checkPosBill(payType, checkDate);
			}
		}
//		//清算
		clearBill(checkDate,null);
		logger.info(payType + " " + checkDate + "，结束重新对账");
	}
	
	@Override
	@Transactional
	public void clearBill(String checkDate,String orderNo) throws Exception {
		BillClearLogDTO billClearLogDTO = new BillClearLogDTO(true);
		billClearLogDTO.setPayTime(checkDate);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("checkStatus", "1");
		if(orderNo == null || "".equals(orderNo)){
			paramMap.put("payTime", checkDate);
		}else{
			paramMap.put("orderNo", orderNo);
		}
		//正常交易账款（除退款外）
		List<PayTradeDTO> haveClearOrderList = baseDao.queryForList("PayJob.haveClearOrderList",paramMap,PayTradeDTO.class);
		for(PayTradeDTO dto : haveClearOrderList){
			//订单是否已清分
			if(isClearByOrderNo(dto.getOrderNo())){
				continue;
			}
			//订单是否以对账成功
			if(isCheckSuc(dto.getOrderNo())){
				if(dto.getPayCount() == 1) {//单笔支付
					//获取交易基本信息
					paramMap.clear();
					paramMap.put("orderNo", dto.getOrderNo());
					PayTradeDTO basePayTradeDto = baseDao.queryForObject("PayJob.queryBasePayTradeObject", paramMap, PayTradeDTO.class);
					logger.info("正在清分订单："+basePayTradeDto.getOrderNo());
					ClearBillDTO clearBillDTO = null;
					String clearType = null;
					paramMap.put("orderNo", basePayTradeDto.getOrderNo());
					if("ALIPAY_H5".equals(basePayTradeDto.getPayType())) {
						paramMap.put("payType", "ALIPAY_H5");
						paramMap.put("appKey", basePayTradeDto.getAppKey());
						PayTypeConfigEntity payTypeConfigEntity = baseDao.queryForObject("PayJob.queryPayTypeConfig", paramMap, PayTypeConfigEntity.class);
						if(payTypeConfigEntity != null && "1".equals(payTypeConfigEntity.getHasClear())) {
							clearBillDTO = baseDao.queryForObject("PayJob.queryClearBillInfo", paramMap, ClearBillDTO.class);
							logger.info("清算对象ClearBillDTO：" + clearBillDTO);
							if(clearBillDTO != null){
								clearType = "ALIPAY_H5";
								billClearLogDTO.setClearNum(billClearLogDTO.getClearNum() + 1);
								billClearLogDTO.setClearAmt(addDouble(billClearLogDTO.getClearAmt(),basePayTradeDto.getPayAmt()));
							}
						}
					} else if("WEIXIN_APP".equals(basePayTradeDto.getPayType())) {
						paramMap.put("payType", "WEIXIN_APP");
						paramMap.put("appKey", basePayTradeDto.getAppKey());
						PayTypeConfigEntity payTypeConfigEntity = baseDao.queryForObject("PayJob.queryPayTypeConfig", paramMap, PayTypeConfigEntity.class);
						if(payTypeConfigEntity != null && "1".equals(payTypeConfigEntity.getHasClear())) {
							clearBillDTO = baseDao.queryForObject("PayJob.queryClearBillInfo", paramMap, ClearBillDTO.class);
							logger.info("清算对象ClearBillDTO：" + clearBillDTO);
							if(clearBillDTO != null){
								clearType = "WEIXIN_APP";
								billClearLogDTO.setClearNum(billClearLogDTO.getClearNum() + 1);
								billClearLogDTO.setClearAmt(addDouble(billClearLogDTO.getClearAmt(),basePayTradeDto.getPayAmt()));
							}
						}
					} else {
						if(StringUtils.isNotBlank(basePayTradeDto.getPosVersion())) {
							Map<String, Object> posMachineConfigParam = new HashMap<String, Object>();
							posMachineConfigParam.put("posVersions",  Arrays.asList(basePayTradeDto.getPosVersion().split(",")));
							posMachineConfigParam.put("machineNum", basePayTradeDto.getPosClientNo());
							List<PosMachineConfigEntity> posMachineConfigList = baseDao.queryForList("PayJob.queryPosMachineConfig", posMachineConfigParam, PosMachineConfigEntity.class);
							if(posMachineConfigList != null && posMachineConfigList.size() > 0){
								PosMachineConfigEntity posMachineConfigEntity = posMachineConfigList.get(0);
								logger.info("终端号：" + dto.getPosClientNo() + 
										",状态:" + posMachineConfigEntity.getState() + ",清算:" + posMachineConfigEntity.getHasClear());
								if("1".equals(posMachineConfigEntity.getState()) && "1".equals(posMachineConfigEntity.getHasClear())) {
									clearBillDTO = baseDao.queryForObject("PayJob.queryPosClearBillInfo", paramMap, ClearBillDTO.class);
									logger.info("清算对象ClearBillDTO：" + clearBillDTO);
									if(clearBillDTO != null){
										//调用计算价格接口,获取佣金等信息
										calculatePrice(clearBillDTO);
										clearType = "POS";
										billClearLogDTO.setClearNum(billClearLogDTO.getClearNum() + 1);
										billClearLogDTO.setClearAmt(addDouble(billClearLogDTO.getClearAmt(),basePayTradeDto.getPayAmt()));
									}
								}
							}
							
						}
					}
					if(clearType != null) {
						//插入数据库
						clearBillDTO.setClearType(clearType);
						clearBillDTO.setPlatUserId(Integer.parseInt(gdProperties.getProperties().getProperty("maven.gd.memberId")));
						addClearBillInfo(clearBillDTO);
					}
				}else if(dto.getPayCount() == 2){//多次支付
					//获取交易基本信息
					paramMap.clear();
					paramMap.put("orderNo", dto.getOrderNo());
					paramMap.put("requestNo", dto.getPayCount());
					PayTradeDTO basePayTradeDto = baseDao.queryForObject("PayJob.queryBasePayTradeObject", paramMap, PayTradeDTO.class);
					if(basePayTradeDto == null){
						continue;
					}
					logger.info("正在清分订单："+basePayTradeDto.getOrderNo());
					ClearBillDTO clearBillDTO = null;
					String clearType = null;
					if(StringUtils.isNotBlank(basePayTradeDto.getPosVersion())) {
						Map<String, Object> posMachineConfigParam = new HashMap<String, Object>();
						posMachineConfigParam.put("posVersions",  Arrays.asList(basePayTradeDto.getPosVersion().split(",")));
						posMachineConfigParam.put("machineNum", basePayTradeDto.getPosClientNo());
						List<PosMachineConfigEntity> posMachineConfigList = baseDao.queryForList("PayJob.queryPosMachineConfig", posMachineConfigParam, PosMachineConfigEntity.class);
						if(posMachineConfigList != null && posMachineConfigList.size() > 0){
							PosMachineConfigEntity posMachineConfigEntity = posMachineConfigList.get(0);
							logger.info("终端号：" + basePayTradeDto.getPosClientNo() + 
									",状态:" + posMachineConfigEntity.getState() + ",清算:" + posMachineConfigEntity.getHasClear());
							if("1".equals(posMachineConfigEntity.getState()) && "1".equals(posMachineConfigEntity.getHasClear())) {
								clearBillDTO = baseDao.queryForObject("PayJob.queryPosClearBillInfo", paramMap, ClearBillDTO.class);
								ClearBillDTO amtSum = baseDao.queryForObject("PayJob.amtSum", paramMap, ClearBillDTO.class);
								clearBillDTO.setPayAmt(amtSum.getPayAmt());
								clearBillDTO.setPlatFeeAmt(amtSum.getPlatFeeAmt());
								logger.info("清算对象ClearBillDTO：" + clearBillDTO);
								if(clearBillDTO != null){
									//调用计算价格接口,获取佣金等信息
									calculatePrice(clearBillDTO);
									clearType = "POS";
									billClearLogDTO.setClearNum(billClearLogDTO.getClearNum() + 1);
									billClearLogDTO.setClearAmt(addDouble(billClearLogDTO.getClearAmt(),basePayTradeDto.getPayAmt()));
								}
							}
						}
					}
					if(clearType != null) {
						//插入数据库
						clearBillDTO.setClearType(clearType);
						clearBillDTO.setPlatUserId(Integer.parseInt(gdProperties.getProperties().getProperty("maven.gd.memberId")));
						addClearBillInfo(clearBillDTO);
					}
				}
			}
		}
		//清分退款账单
		billClearLogDTO = clearRefundBill(checkDate,billClearLogDTO);
		//清分日志
		updClearLog(billClearLogDTO);
		
	}
	
	@Override
	@Transactional
	public String updateFailedBill(Map<String, Object> paramMap) throws Exception {
		logger.info("修改对账明细:" + paramMap);
		if(paramMap.get("thirdPayNumber") == null) {
			throw new Exception("第三方流水号为空");
		}
		if(paramMap.get("orderList") == null) {
			throw new Exception("订单列表为空");
		}
	    List<GdOrderInfo> orderList =JSONArray.parseArray(String.valueOf(paramMap.get("orderList")),GdOrderInfo.class); 
		if(orderList == null || orderList.size() == 0) {
			throw new Exception("订单列表为空");
		}
		paramMap.put("orderList", orderList);
		try{
			String resultType = String.valueOf(paramMap.get("resultType"));
			//1我方有他方有 2我方有他方无 3我方无他方有
			if("1".equals(resultType)) {
				updateFailedTWBill(paramMap);
			} else if("2".equals(resultType)) {
				updateFailedWBill(paramMap);
			} else if("3".equals(resultType)) {
				updateFailedTBill(paramMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改对账明细异常：", e);
			throw e;
		}
		logger.info("修改对账明细结束");
		return "success";
	}
	
	public void updateFailedTWBill(Map<String, Object> paramMap) throws Exception {
		String thirdPayNumber = String.valueOf(paramMap.get("thirdPayNumber"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("thirdPayNumber", thirdPayNumber);
		List<PayTradeDTO> payTradeList = baseDao.queryForList("PayJob.queryPayTrade", map, PayTradeDTO.class);
		if(payTradeList == null || payTradeList.size() == 0) {
			throw new Exception("支付系统没有第三方流水号:" + thirdPayNumber);
		}
		List<GdOrderInfo> gdOrderInfoList = (List<GdOrderInfo>)paramMap.get("orderList");
		logger.info("订单列表记录数：" + gdOrderInfoList.size());
		if(payTradeList.size() != gdOrderInfoList.size()) {
			throw new Exception("订单列表不正确");
		}
		PayTradeDTO payTradeDTO = payTradeList.get(0);
		BillCheckDetailDto billCheckDetailDto = queryBillCheckDetail(map);
		logger.info("来自平台的对账明细记录:" + billCheckDetailDto);
		String transDateStr = DateFormatUtils.format(billCheckDetailDto.getTrans_date(), "yyyy-MM-dd HH:mm:ss");
		String payTimeStr = transDateStr.substring(0, 10);
		BillCheckSumDTO billDTO = queryBillCheckSum(payTradeDTO.getPayType(), payTimeStr);
		logger.info("查询汇总记录" + billDTO);
		Double payAmt = Double.valueOf(String.valueOf(paramMap.get("tradeMoney")));
		map.put("checkStatus", "1");
		map.put("payAmt", payAmt);
		map.put("remark", paramMap.get("remark"));
		map.put("updateUserId", paramMap.get("updateUserId"));
		//更新对账明细
		logger.info("更新对账明细：" + map);
		baseDao.execute("PayJob.updateFailedBillCheckDetail", map);
		//增加或修改手续费
		HashMap<String, Object> updateFeeRecordparamMap = new HashMap<String, Object>();
		updateFeeRecordparamMap.put("payCenterNumber", payTradeDTO.getPayCenterNumber());
		updateFeeRecordparamMap.put("thridPayNumber", thirdPayNumber);
		updateFeeRecordparamMap.put("feeAmt", paramMap.get("fee"));
		updateFeeRecordparamMap.put("payAmt", payAmt);
		updateFeeRecordparamMap.put("payTime", DateFormatUtils.format(payTradeDTO.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
		logger.info("增加或修改手续费：" + updateFeeRecordparamMap);
		updateFeeRecord(updateFeeRecordparamMap);
		List<OrderAdvanceAndPaymentDTO> orderAdvanceAndPaymentList = new ArrayList<OrderAdvanceAndPaymentDTO>();
		map.put("payStatus", paramMap.get("payStatus"));
		Double orderSumPayAmt = new Double(0d);
		for(PayTradeDTO payTrade : payTradeList) {
			map.remove("payAmt");
			map.put("orderNo", payTrade.getOrderNo());
			for(GdOrderInfo gdOrderInfo : gdOrderInfoList) {
				logger.info("订单列表对象：" + gdOrderInfo);
				if(payTrade.getOrderNo().equals(gdOrderInfo.getOrderNo())) {
					if(StringUtils.isBlank(gdOrderInfo.getActualAmt()) || 
							Double.valueOf(gdOrderInfo.getActualAmt()).equals(0d)) {
						throw new Exception("订单应付金额不能为0");
					}
					map.put("payAmt", gdOrderInfo.getActualAmt());
					map.put("feeAmt", divDouble(mulDouble(Double.valueOf(String.valueOf(paramMap.get("fee"))), 
							Double.valueOf(gdOrderInfo.getActualAmt())), payAmt, 2));
					break;
				}
			}
			if(!map.containsKey("payAmt")) {
				throw new Exception("找不到订单编号：" + payTrade.getOrderNo());
			}
			orderSumPayAmt = addDouble(orderSumPayAmt, Double.valueOf(String.valueOf(map.get("payAmt"))));
			//更新交易记录
			logger.info("更新交易记录：" + map);
			baseDao.execute("PayJob.updatePayTrade", map);
			if("2".equals(String.valueOf(paramMap.get("payStatus")))) {
				//更新卡信息
				OrderBillHessianDto dto = new OrderBillHessianDto();
				dto.setSysRefeNo(thirdPayNumber);
				List<OrderBillHessianDto> orderBillHessianDtoList = orderBillHessianService.getOrderBillByCondition(dto);
				if(orderBillHessianDtoList != null && orderBillHessianDtoList.size() > 0) {
					OrderBillHessianDto orderBillHessianDto = orderBillHessianDtoList.get(0);
					orderBillHessianDto.setPayCenterNumber(payTradeDTO.getPayCenterNumber());
					//更新支付交易POS刷卡表
					if(StringUtils.isNotBlank(orderBillHessianDto.getCardType())) {
						logger.info("更新支付交易POS刷卡表");
						updatePayTradePos(orderBillHessianDto);
					}
				}
				//清除参数
				paramMap.clear();
				paramMap.put("orderNo", payTrade.getOrderNo());
				//清算
				clearBill(payTimeStr, payTrade.getOrderNo());
				logger.info("回调业务系统appkey:" + payTrade.getAppKey());
				if("gys".equals(payTrade.getAppKey()) || "nsy_pay".equals(payTrade.getAppKey())
						 || "nsy".equals(payTrade.getAppKey())) {
					OrderAdvanceAndPaymentDTO orderAdvanceAndPayment = new OrderAdvanceAndPaymentDTO();
					String type = "0";
					if("2".equals(payTrade.getPayCount())) {
						type = payTrade.getRequestNo();
					}
					orderAdvanceAndPayment.setType(type);
					orderAdvanceAndPayment.setOrderNo(payTrade.getOrderNo());
					orderAdvanceAndPayment.setPayAmt(Double.valueOf(String.valueOf(map.get("payAmt"))));
					orderAdvanceAndPayment.setUpdateUserId(String.valueOf(map.get("updateUserId")));
					orderAdvanceAndPayment.setPayCenterNumber(payTrade.getPayCenterNumber());
					orderAdvanceAndPayment.setThirdPayNumber(payTrade.getThirdPayNumber());
					orderAdvanceAndPayment.setThirdPayerAccount(payTrade.getThirdPayerAccount());
					orderAdvanceAndPayment.setThirdPayeeAccount(payTrade.getThirdPayeeAccount());
					orderAdvanceAndPayment.setPayTime(DateUtils.parseDate(transDateStr, "yyyy-MM-dd HH:mm:ss"));
					orderAdvanceAndPayment.setPayerUserId(payTrade.getPayerUserId());
					orderAdvanceAndPayment.setPayType(payTrade.getPayType());
					logger.info("增加回调订单中心修改价格接口参数对象:" + orderAdvanceAndPayment);
					orderAdvanceAndPaymentList.add(orderAdvanceAndPayment);
				}
			}
		}
		if(!orderSumPayAmt.equals(payAmt)) {
			throw new Exception("订单金额不一致");
		}
		if(orderAdvanceAndPaymentList != null && orderAdvanceAndPaymentList.size() > 0) {
			logger.info("修改订单中心价格");
			String result = orderbaseInfoHessianService.dealAdvanceAndPayment(orderAdvanceAndPaymentList);
			if(!"success".equals(result)) {
				throw new Exception("修改订单中心价格失败");
			}
		}
		//增加对账日志
		BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
		billCheckSumDTO.setCreateUserId("SYS");
		billCheckSumDTO.setPayType(payTradeDTO.getPayType());
		billCheckSumDTO.setPayTimeStr(payTimeStr);
		billCheckSumDTO.setCheckSuccessNum(1);
		billCheckSumDTO.setCheckSuccessAmt(payAmt);
		billCheckSumDTO.setCheckNum(1);
		billCheckSumDTO.setCheckAmt(payAmt);
		logger.info("增加对账日志" + billCheckSumDTO);
		baseDao.execute("PayJob.addBillCheckLog", billCheckSumDTO);
		//更新对账汇总
		billDTO.setCheckSuccessNum(billDTO.getCheckSuccessNum() + 1);
		billDTO.setCheckSuccessAmt(addDouble(billDTO.getCheckSuccessAmt(), payAmt));
		billDTO.setCheckFailNum(billDTO.getCheckFailNum() - 1);
		billDTO.setCheckFailAmt(subtractDouble(billDTO.getCheckFailAmt(), billCheckDetailDto.getPayAmt()));
		billDTO.setCheckAmt(addDouble(subtractDouble(billDTO.getCheckAmt(), 
				billCheckDetailDto.getPayAmt()), payAmt));
		billDTO.setUpdateUserId("SYS");
		logger.info("更新汇总记录" + billDTO);
		int count = baseDao.execute("PayJob.updateBillCheckSum", billDTO);
		if(count == 0) {
			logger.error("汇总记录版本号不正确");
			throw new Exception("汇总记录版本号不正确");
		}
	}
	
	public void updateFailedWBill(Map<String, Object> paramMap) throws Exception {
		String thirdPayNumber = String.valueOf(paramMap.get("thirdPayNumber"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("thirdPayNumber", thirdPayNumber);
		List<PayTradeDTO> payTradeList = baseDao.queryForList("PayJob.queryPayTrade", map, PayTradeDTO.class);
		if(payTradeList == null || payTradeList.size() == 0) {
			throw new Exception("支付系统没有第三方流水号:" + thirdPayNumber);
		}
		List<GdOrderInfo> gdOrderInfoList = (List<GdOrderInfo>)paramMap.get("orderList");
		logger.info("订单列表记录数：" + gdOrderInfoList.size());
		if(payTradeList.size() != gdOrderInfoList.size()) {
			throw new Exception("订单列表不正确");
		}
		PayTradeDTO payTradeDTO = payTradeList.get(0);
		BillCheckDetailDto billCheckDetailDto = queryBillCheckDetail(map);
		logger.info("来自平台的对账明细记录:" + billCheckDetailDto);
		String transDateStr = String.valueOf(paramMap.get("payTime"));
		String payTimeStr = transDateStr.substring(0, 10);
		BillCheckSumDTO billDTO = queryBillCheckSum(payTradeDTO.getPayType(), payTimeStr);
		logger.info("查询汇总记录" + billDTO);
		Double payAmt = Double.valueOf(String.valueOf(paramMap.get("tradeMoney")));
		//更新对账明细
		Map<String, Object> billCheckDetailMap = new HashMap<String, Object>();
		billCheckDetailMap.put("payCenterNumber", billCheckDetailDto.getPayCenterNumber());
		billCheckDetailMap.put("trans_date_str", transDateStr);
		billCheckDetailMap.put("payAmt", payAmt);
		billCheckDetailMap.put("remark", paramMap.get("remark"));
		billCheckDetailMap.put("checkStatus", "1");
		billCheckDetailMap.put("updateUserId", paramMap.get("updateUserId"));
		logger.info("更新对账明细：" + billCheckDetailMap);
		baseDao.execute("PayJob.updateBillCheck", billCheckDetailMap);
		//增加或修改手续费
		HashMap<String, Object> updateFeeRecordparamMap = new HashMap<String, Object>();
		updateFeeRecordparamMap.put("payCenterNumber", payTradeDTO.getPayCenterNumber());
		updateFeeRecordparamMap.put("thridPayNumber", thirdPayNumber);
		updateFeeRecordparamMap.put("feeAmt", paramMap.get("fee"));
		updateFeeRecordparamMap.put("payAmt", payAmt);
		updateFeeRecordparamMap.put("payTime", DateFormatUtils.format(payTradeDTO.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
		logger.info("增加或修改手续费：" + updateFeeRecordparamMap);
		updateFeeRecord(updateFeeRecordparamMap);
		Map<String, Object> payTradePosParamMap = new HashMap<String, Object>();
		payTradePosParamMap.put("payCenterNumber", billCheckDetailDto.getPayCenterNumber());
		payTradePosParamMap.put("bankType", paramMap.get("bankType")); 
		payTradePosParamMap.put("cardType", paramMap.get("cardType"));
		logger.info("更新银行卡号信息：" + payTradePosParamMap);
		baseDao.execute("PayJob.updatePayTradePos", payTradePosParamMap);
		map.put("updateUserId", paramMap.get("updateUserId"));
		map.put("payStatus", paramMap.get("payStatus"));
		List<OrderAdvanceAndPaymentDTO> orderAdvanceAndPaymentList = new ArrayList<OrderAdvanceAndPaymentDTO>();
		Double orderSumPayAmt = new Double(0);
		for(PayTradeDTO payTrade : payTradeList) {
			map.remove("payAmt");
			map.put("orderNo", payTrade.getOrderNo());
			for(GdOrderInfo gdOrderInfo : gdOrderInfoList) {
				logger.info("订单列表对象：" + gdOrderInfo);
				if(payTrade.getOrderNo().equals(gdOrderInfo.getOrderNo())) {
					if(StringUtils.isBlank(gdOrderInfo.getActualAmt()) || 
							Double.valueOf(gdOrderInfo.getActualAmt()).equals(0d)) {
						throw new Exception("订单应付金额不能为0");
					}
					map.put("payAmt", gdOrderInfo.getActualAmt());
					map.put("feeAmt", divDouble(mulDouble(Double.valueOf(String.valueOf(paramMap.get("fee"))), 
							Double.valueOf(gdOrderInfo.getActualAmt())), payAmt, 2));
					break;
				}
			}
			if(!map.containsKey("payAmt")) {
				throw new Exception("找不到订单编号：" + payTrade.getOrderNo());
			}
			orderSumPayAmt = addDouble(orderSumPayAmt, Double.valueOf(String.valueOf(map.get("payAmt"))));
			//更新交易记录
			logger.info("更新交易记录：" + map);
			baseDao.execute("PayJob.updatePayTrade", map);
			if("2".equals(String.valueOf(paramMap.get("payStatus")))) {
				//清除参数
				paramMap.clear();
				paramMap.put("orderNo", payTrade.getOrderNo());
				//清算
				clearBill(payTimeStr, payTrade.getOrderNo());
				logger.info("回调业务系统appkey:" + payTrade.getAppKey());
				if("gys".equals(payTrade.getAppKey()) || "nsy_pay".equals(payTrade.getAppKey())
						 || "nsy".equals(payTrade.getAppKey())) {
					OrderAdvanceAndPaymentDTO orderAdvanceAndPayment = new OrderAdvanceAndPaymentDTO();
					String type = "0";
					if("2".equals(payTrade.getPayCount())) {
						type = payTrade.getRequestNo();
					}
					orderAdvanceAndPayment.setType(type);
					orderAdvanceAndPayment.setOrderNo(payTrade.getOrderNo());
					orderAdvanceAndPayment.setPayAmt(Double.valueOf(String.valueOf(map.get("payAmt"))));
					orderAdvanceAndPayment.setUpdateUserId(String.valueOf(map.get("updateUserId")));
					orderAdvanceAndPayment.setPayCenterNumber(payTrade.getPayCenterNumber());
					orderAdvanceAndPayment.setThirdPayNumber(payTrade.getThirdPayNumber());
					orderAdvanceAndPayment.setThirdPayerAccount(payTrade.getThirdPayerAccount());
					orderAdvanceAndPayment.setThirdPayeeAccount(payTrade.getThirdPayeeAccount());
					orderAdvanceAndPayment.setPayTime(DateUtils.parseDate(transDateStr, "yyyy-MM-dd HH:mm:ss"));
					orderAdvanceAndPayment.setPayerUserId(payTrade.getPayerUserId());
					orderAdvanceAndPayment.setPayType(payTrade.getPayType());
					logger.info("增加回调订单中心修改价格接口参数对象:" + orderAdvanceAndPayment);
					orderAdvanceAndPaymentList.add(orderAdvanceAndPayment);
				}
			}
		}
		if(!orderSumPayAmt.equals(payAmt)) {
			throw new Exception("订单金额不一致");
		}
		if(orderAdvanceAndPaymentList != null && orderAdvanceAndPaymentList.size() > 0) {
			logger.info("修改订单中心价格");
			String result = orderbaseInfoHessianService.dealAdvanceAndPayment(orderAdvanceAndPaymentList);
			if(!"success".equals(result)) {
				throw new Exception("修改订单中心价格失败");
			}
		}
		//增加对账日志
		BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
		billCheckSumDTO.setCreateUserId("SYS");
		billCheckSumDTO.setPayType(payTradeDTO.getPayType());
		billCheckSumDTO.setPayTimeStr(payTimeStr);
		billCheckSumDTO.setCheckSuccessNum(1);
		billCheckSumDTO.setCheckSuccessAmt(payAmt);
		billCheckSumDTO.setCheckNum(1);
		billCheckSumDTO.setCheckAmt(payAmt);
		logger.info("增加对账日志" + billCheckSumDTO);
		baseDao.execute("PayJob.addBillCheckLog", billCheckSumDTO);
		//更新对账汇总
		billDTO.setCheckSuccessNum(billDTO.getCheckSuccessNum() + 1);
		billDTO.setCheckSuccessAmt(addDouble(billDTO.getCheckSuccessAmt(), payAmt));
		billDTO.setCheckFailNum(billDTO.getCheckFailNum() - 1);
		billDTO.setCheckFailAmt(subtractDouble(billDTO.getCheckFailAmt(), billCheckDetailDto.getPayAmt()));
		billDTO.setCheckAmt(addDouble(subtractDouble(billDTO.getCheckAmt(), 
				billCheckDetailDto.getPayAmt()), payAmt));
		billDTO.setUpdateUserId("SYS");
		logger.info("更新汇总记录" + billDTO);
		int count = baseDao.execute("PayJob.updateBillCheckSum", billDTO);
		if(count == 0) {
			logger.error("汇总记录版本号不正确");
			throw new Exception("汇总记录版本号不正确");
		}
	}
	
	public void updateFailedTBill(Map<String, Object> paramMap) throws Exception {
		String thirdPayNumber = String.valueOf(paramMap.get("thirdPayNumber"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("thirdPayNumber", thirdPayNumber);
		BillCheckDetailDto billCheckDetailDto = queryBillCheckDetail(map);
		if(billCheckDetailDto == null) {
			throw new Exception("支付系统没有第三方流水号:" + thirdPayNumber);
		}
		logger.info("来自平台的对账明细记录:" + billCheckDetailDto);
		String transDateStr = DateFormatUtils.format(billCheckDetailDto.getTrans_date(), "yyyy-MM-dd HH:mm:ss");
		String payTimeStr = transDateStr.substring(0, 10);
		List<PayTradeDTO> payTradeList = baseDao.queryForList("PayJob.queryPayTrade", map, PayTradeDTO.class);
		Double payAmt = Double.valueOf(String.valueOf(paramMap.get("tradeMoney")));
		BillCheckSumDTO billDTO = queryBillCheckSum(billCheckDetailDto.getPayChannelCode(), payTimeStr);
		logger.info("查询汇总记录" + billDTO);
		List<GdOrderInfo> gdOrderInfoList = (List<GdOrderInfo>)paramMap.get("orderList");
		List<OrderAdvanceAndPaymentDTO> orderAdvanceAndPaymentList = new ArrayList<OrderAdvanceAndPaymentDTO>();
		Double orderSumPayAmt = new Double(0);
		logger.info("交易记录数：" + (payTradeList == null ? "null" : payTradeList.size()));
		if(payTradeList != null && payTradeList.size() > 0) {
			logger.info("订单列表记录数：" + gdOrderInfoList.size());
			if(payTradeList.size() != gdOrderInfoList.size()) {
				throw new Exception("订单列表不正确");
			}
			PayTradeDTO payTradeDTO = payTradeList.get(0);
			map.put("payCenterNumber", payTradeDTO.getPayCenterNumber());
			map.put("checkStatus", "1");
			map.put("remark", paramMap.get("remark"));
			map.put("payAmt", payAmt);
			map.put("updateUserId", paramMap.get("updateUserId"));
			//更新对账明细
			logger.info("更新对账明细：" + map);
			baseDao.execute("PayJob.updateFailedBillCheckDetail", map);
			//增加或修改手续费
			HashMap<String, Object> updateFeeRecordparamMap = new HashMap<String, Object>();
			updateFeeRecordparamMap.put("payCenterNumber", payTradeDTO.getPayCenterNumber());
			updateFeeRecordparamMap.put("thridPayNumber", thirdPayNumber);
			updateFeeRecordparamMap.put("feeAmt", paramMap.get("fee"));
			updateFeeRecordparamMap.put("payAmt", payAmt);
			updateFeeRecordparamMap.put("payTime", DateFormatUtils.format(payTradeDTO.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
			logger.info("增加或修改手续费：" + updateFeeRecordparamMap);
			updateFeeRecord(updateFeeRecordparamMap);
			map.put("payStatus", paramMap.get("payStatus"));
			for(PayTradeDTO payTrade : payTradeList) {
				map.remove("payAmt");
				map.put("orderNo", payTrade.getOrderNo());
				for(GdOrderInfo gdOrderInfo : gdOrderInfoList) {
					logger.info("订单列表对象：" + gdOrderInfo);
					if(payTrade.getOrderNo().equals(gdOrderInfo.getOrderNo())) {
						if(StringUtils.isBlank(gdOrderInfo.getActualAmt()) || 
								Double.valueOf(gdOrderInfo.getActualAmt()).equals(0d)) {
							throw new Exception("订单应付金额不能为0");
						}
						map.put("payAmt", gdOrderInfo.getActualAmt());
						map.put("feeAmt", divDouble(mulDouble(Double.valueOf(String.valueOf(paramMap.get("fee"))), 
								Double.valueOf(gdOrderInfo.getActualAmt())), payAmt, 2));
						break;
					}
				}
				if(!map.containsKey("payAmt")) {
					throw new Exception("找不到订单编号：" + payTrade.getOrderNo());
				}
				orderSumPayAmt = addDouble(orderSumPayAmt, Double.valueOf(String.valueOf(map.get("payAmt"))));
				//更新交易记录
				logger.info("更新交易记录：" + map);
				baseDao.execute("PayJob.updatePayTrade", map);
				if("2".equals(String.valueOf(paramMap.get("payStatus")))) {
					//更新卡信息
					OrderBillHessianDto dto = new OrderBillHessianDto();
					dto.setSysRefeNo(thirdPayNumber);
					List<OrderBillHessianDto> orderBillHessianDtoList = orderBillHessianService.getOrderBillByCondition(dto);
					if(orderBillHessianDtoList != null && orderBillHessianDtoList.size() > 0) {
						OrderBillHessianDto orderBillHessianDto = orderBillHessianDtoList.get(0);
						orderBillHessianDto.setPayCenterNumber(String.valueOf(map.get("payCenterNumber")));
						//更新支付交易POS刷卡表
						if(StringUtils.isNotBlank(orderBillHessianDto.getCardType())) {
							logger.info("更新支付交易POS刷卡表");
							updatePayTradePos(orderBillHessianDto);
						}
					}
					//清除参数
					paramMap.clear();
					paramMap.put("orderNo", payTrade.getOrderNo());
					//清算
					clearBill(payTimeStr, payTrade.getOrderNo());
					logger.info("回调业务系统appkey:" + payTrade.getAppKey());
					if("gys".equals(payTrade.getAppKey()) || "nsy_pay".equals(payTrade.getAppKey())
							 || "nsy".equals(payTrade.getAppKey())) {
						OrderAdvanceAndPaymentDTO orderAdvanceAndPayment = new OrderAdvanceAndPaymentDTO();
						String type = "0";
						if("2".equals(payTrade.getPayCount())) {
							type = payTrade.getRequestNo();
						}
						orderAdvanceAndPayment.setType(type);
						orderAdvanceAndPayment.setOrderNo(payTrade.getOrderNo());
						orderAdvanceAndPayment.setPayAmt(Double.valueOf(String.valueOf(map.get("payAmt"))));
						orderAdvanceAndPayment.setUpdateUserId(String.valueOf(map.get("updateUserId")));
						orderAdvanceAndPayment.setPayCenterNumber(payTrade.getPayCenterNumber());
						orderAdvanceAndPayment.setThirdPayNumber(payTrade.getThirdPayNumber());
						orderAdvanceAndPayment.setThirdPayerAccount(payTrade.getThirdPayerAccount());
						orderAdvanceAndPayment.setThirdPayeeAccount(payTrade.getThirdPayeeAccount());
						orderAdvanceAndPayment.setPayTime(DateUtils.parseDate(transDateStr, "yyyy-MM-dd HH:mm:ss"));
						orderAdvanceAndPayment.setPayerUserId(payTrade.getPayerUserId());
						orderAdvanceAndPayment.setPayType(payTrade.getPayType());
						logger.info("增加回调订单中心修改价格接口参数对象:" + orderAdvanceAndPayment);
						orderAdvanceAndPaymentList.add(orderAdvanceAndPayment);
					}
				}
			}
			if(!orderSumPayAmt.equals(payAmt)) {
				throw new Exception("订单金额不一致");
			}
			if(orderAdvanceAndPaymentList != null && orderAdvanceAndPaymentList.size() > 0) {
				logger.info("修改订单中心价格");
				String result = orderbaseInfoHessianService.dealAdvanceAndPayment(orderAdvanceAndPaymentList);
				if(!"success".equals(result)) {
					throw new Exception("修改订单中心价格失败");
				}
			}
		} else {
			String tradeType = String.valueOf(paramMap.get("tradeType"));
			if("0".equals(tradeType) || "1".equals(tradeType)) {
				if(gdOrderInfoList.size() > 1) {
					throw new Exception("pos机刷卡，订单记录数不正确");
				}
				GdOrderInfo gdOrderInfo = gdOrderInfoList.get(0);
				logger.info("订单列表对象：" + gdOrderInfo);
				if(gdOrderInfo.getActualAmt() == null) {
					throw new Exception("订单金额为空");
				}
				orderSumPayAmt = Double.valueOf(gdOrderInfo.getActualAmt());
				if(!orderSumPayAmt.equals(payAmt)) {
					throw new Exception("订单金额不一致");
				}
				//校验POS号是否存在
				Map<String,Object> params = new HashMap<>();
				params.put("posClientNo", String.valueOf(paramMap.get("clientNo")));
				int count = baseDao.queryForObject("PayTrade.getPosMachineConfigCount", params, Integer.class);
				if(count == 0){
					throw new Exception("POS终端号["+ String.valueOf(paramMap.get("clientNo"))+"]不存在");
				}
				if("2".equals(String.valueOf(paramMap.get("payStatus"))) && "1".equals(tradeType)) {
					if("2".equals(gdOrderInfo.getOrderFlag())) {
						Map<String, Object> orderMap = new HashMap<String, Object>();
						orderMap.put("bankCardNo", String.valueOf(paramMap.get("cardNo")));
						orderMap.put("posNumber", String.valueOf(paramMap.get("clientNo")));
						orderMap.put("payAmt", String.valueOf(gdOrderInfo.getActualAmt()));
						orderMap.put("payTime", DateFormatUtils.format(billCheckDetailDto.getTrans_date(), "yyyy-MM-dd HH:mm:ss"));
						orderMap.put("updateUserId", String.valueOf(paramMap.get("updateUserId")));
						logger.info("生成订单参数:" + orderMap);
						String orderNo = orderbaseInfoHessianService.addOrderForCheckBill(orderMap);
						logger.info("制单订单编号：" + orderNo);
						gdOrderInfo.setOrderNo(orderNo);
					}
				}
				PosPayNotifyDto posPayNotifyDto = new PosPayNotifyDto();
				posPayNotifyDto.setPosClientNo(String.valueOf(paramMap.get("clientNo")));
				posPayNotifyDto.setOrderNo(gdOrderInfo.getOrderNo());
				posPayNotifyDto.setTransNo(thirdPayNumber);
				posPayNotifyDto.setPayChannelCode(billCheckDetailDto.getPayChannelCode());
				posPayNotifyDto.setTransType(tradeType);
				posPayNotifyDto.setPayCenterNumber(payTradeService.getPayCenterNumber());
				posPayNotifyDto.setNotifyStatus(NotifyStatus.NOTIFIED);
				posPayNotifyDto.setAppKey("nsy_pay");
				posPayNotifyDto.setRateAmt(Double.valueOf(String.valueOf((paramMap.get("fee")))));
				posPayNotifyDto.setPayAmt(payAmt);
				posPayNotifyDto.setTransDate(billCheckDetailDto.getTrans_date());
				posPayNotifyDto.setBankCardNo(String.valueOf(paramMap.get("cardNo")));
				posPayNotifyDto.setStatus(String.valueOf(paramMap.get("payStatus")));
				logger.info("增加交易记录");
				//增加交易记录
				addPayTrade(posPayNotifyDto);
				map.put("payCenterNumber", posPayNotifyDto.getPayCenterNumber());
				map.put("checkStatus", "1");
				map.put("remark", paramMap.get("remark"));
				map.put("payAmt", payAmt);
				map.put("updateUserId", paramMap.get("updateUserId"));
				//更新对账明细
				logger.info("更新对账明细：" + map);
				baseDao.execute("PayJob.updateFailedBillCheckDetail", map);
				//增加或修改手续费
				HashMap<String, Object> updateFeeRecordparamMap = new HashMap<String, Object>();
				updateFeeRecordparamMap.put("payCenterNumber", posPayNotifyDto.getPayCenterNumber());
				updateFeeRecordparamMap.put("thridPayNumber", thirdPayNumber);
				updateFeeRecordparamMap.put("feeAmt", paramMap.get("fee"));
				updateFeeRecordparamMap.put("payAmt", payAmt);
				updateFeeRecordparamMap.put("payTime", DateFormatUtils.format(billCheckDetailDto.getTrans_date(), "yyyy-MM-dd HH:mm:ss"));
				logger.info("增加或修改手续费：" + updateFeeRecordparamMap);
				updateFeeRecord(updateFeeRecordparamMap);
				if("2".equals(String.valueOf(paramMap.get("payStatus")))) {
					//更新卡信息
					OrderBillHessianDto dto = new OrderBillHessianDto();
					dto.setSysRefeNo(thirdPayNumber);
					List<OrderBillHessianDto> orderBillHessianDtoList = orderBillHessianService.getOrderBillByCondition(dto);
					if(orderBillHessianDtoList != null && orderBillHessianDtoList.size() > 0) {
						OrderBillHessianDto orderBillHessianDto = orderBillHessianDtoList.get(0);
						orderBillHessianDto.setPayCenterNumber(posPayNotifyDto.getPayCenterNumber());
						//更新支付交易POS刷卡表
						if(StringUtils.isNotBlank(orderBillHessianDto.getCardType())) {
							logger.info("更新支付交易POS刷卡表");
							updatePayTradePos(orderBillHessianDto);
						}
					}
					Map<String, Object> payTradeMap = new HashMap<String, Object>();
					payTradeMap.put("payCenterNumber", posPayNotifyDto.getPayCenterNumber());
					List<PayTradeDTO> ptList = baseDao.queryForList("PayJob.queryPayTrade", payTradeMap, PayTradeDTO.class);
					PayTradeDTO pt = ptList.get(0);
					//清算
					clearBill(payTimeStr, pt.getOrderNo());
					//回调订单中心
					if("0".equals(tradeType) || ("1".equals(tradeType) && "1".equals(gdOrderInfo.getOrderFlag()))) {
						Map<String, Object> orderMap = new HashMap<String, Object>();
						orderMap.put("orderNo", pt.getOrderNo());
						orderMap.put("payCenterNumber", pt.getPayCenterNumber());
						orderMap.put("payAmt", pt.getPayAmt());
						orderMap.put("payType", pt.getPayType());
						orderMap.put("thirdPayerAccount", pt.getThirdPayerAccount());
						orderMap.put("thirdPayeeAccount", pt.getThirdPayeeAccount());
						orderMap.put("payTime", DateFormatUtils.format(billCheckDetailDto.getTrans_date(), "yyyy-MM-dd HH:mm:ss"));
						orderMap.put("thirdPayNumber", thirdPayNumber);
						orderMap.put("posNumber", String.valueOf(paramMap.get("clientNo")));
						orderMap.put("updateUserId", String.valueOf(paramMap.get("updateUserId")));
						logger.info("更新订单参数:" + orderMap);
						if(!"success".equals(orderbaseInfoHessianService.updateOrderForCheckBill(orderMap))) {
							throw new Exception("回调订单中心失败");
						}
					} else if("1".equals(tradeType)){
						Map<String, Object> paySerialnumberMap = new HashMap<String, Object>();
						paySerialnumberMap.put("orderNo", pt.getOrderNo());
						paySerialnumberMap.put("payCenterNumber", pt.getPayCenterNumber());
						paySerialnumberMap.put("payAmt", pt.getPayAmt());
						paySerialnumberMap.put("payType", pt.getPayType());
						paySerialnumberMap.put("thirdPayerAccount", pt.getThirdPayerAccount());
						paySerialnumberMap.put("thirdPayeeAccount", pt.getThirdPayeeAccount());
						paySerialnumberMap.put("payTime", DateFormatUtils.format(billCheckDetailDto.getTrans_date(), "yyyy-MM-dd HH:mm:ss"));
						paySerialnumberMap.put("thirdPayNumber", thirdPayNumber);
						paySerialnumberMap.put("posNumber", String.valueOf(paramMap.get("clientNo")));
						paySerialnumberMap.put("updateUserId", String.valueOf(paramMap.get("updateUserId")));
						logger.info("增加订单中心交易流水:" + paySerialnumberMap);
						if(!"success".equals(orderbaseInfoHessianService.addPaySerialnumberForCheckBill(params))) {
							throw new Exception("回调订单中心失败");
						}
					}
				}
			}
		}
		//增加对账日志
		BillCheckSumDTO billCheckSumDTO = new BillCheckSumDTO(true);
		billCheckSumDTO.setCreateUserId("SYS");
		billCheckSumDTO.setPayType(billCheckDetailDto.getPayChannelCode());
		billCheckSumDTO.setPayTimeStr(payTimeStr);
		billCheckSumDTO.setCheckSuccessNum(1);
		billCheckSumDTO.setCheckSuccessAmt(payAmt);
		billCheckSumDTO.setCheckNum(1);
		billCheckSumDTO.setCheckAmt(payAmt);
		logger.info("增加对账日志" + billCheckSumDTO);
		baseDao.execute("PayJob.addBillCheckLog", billCheckSumDTO);
		//更新对账汇总
		billDTO.setCheckSuccessNum(billDTO.getCheckSuccessNum() + 1);
		billDTO.setCheckSuccessAmt(addDouble(billDTO.getCheckSuccessAmt(), payAmt));
		billDTO.setCheckFailNum(billDTO.getCheckFailNum() - 1);
		billDTO.setCheckFailAmt(subtractDouble(billDTO.getCheckFailAmt(), billCheckDetailDto.getPayAmt()));
		billDTO.setCheckAmt(addDouble(subtractDouble(billDTO.getCheckAmt(), 
				billCheckDetailDto.getPayAmt()), payAmt));
		billDTO.setUpdateUserId("SYS");
		logger.info("更新汇总记录" + billDTO);
		int count = baseDao.execute("PayJob.updateBillCheckSum", billDTO);
		if(count == 0) {
			logger.error("汇总记录版本号不正确");
			throw new Exception("汇总记录版本号不正确");
		}
	}
	
	private PayTradeDto addPayTrade(PosPayNotifyDto dto) throws BizException{
		String payCenterNumber = dto.getPayCenterNumber();
		//获取第三方收款方帐号
		Map<String,Object> params = new HashMap<>();
		params.put("payType", dto.getPayChannelCode());
		PayTypeEntity entity = baseDao.queryForObject("PayType.queryByCondition", params, PayTypeEntity.class);
		
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
		trade.setPayStatus(dto.getStatus());
		trade.setCreateTime(new Date());
		trade.setUpdateTime(new Date());
		trade.setCreateUserId("sys");
		trade.setUpdateUserId("sys");
		trade.setHasGenFlag("1");
		trade.setPayCount(1);
		trade.setReceiptAmt(subtractDouble(trade.getPayAmt(), trade.getFeeAmt()));
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
	
	@Override
	public List<PayTypeDto> queryPayTypeCheckBill() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("hasCheck", "1");
		return baseDao.queryForList("PayJob.queryPayTypeCheckBill", paramMap, PayTypeDto.class);
	}
	
	public static void main(String[] args) {
	}

	@Override
	public List<BillCheckDetailEntity> queryPayTradeNoCheck(String payChannelCode,String payTime) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("payChannelCode", payChannelCode);
		paramMap.put("payTime", payTime);
		return baseDao.queryForList("PayJob.queryPayTradeNoCheck", paramMap, BillCheckDetailEntity.class);
	}
	
	public boolean isClearByOrderNo(String orderNo){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orderNo", orderNo);
		Integer count = baseDao.queryForObject("PayJob.isClearByOrderNo", paramMap, Integer.class);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 更新清分日志
	 * @param entity
	 */
	public void updClearLog(BillClearLogDTO billClearLogDTO)  throws Exception {
		// 清分成功笔数
		billClearLogDTO.setClearSuccessNum(billClearLogDTO.getClearNum());
		// 清分成功金额
		billClearLogDTO.setClearSuccessAmt(billClearLogDTO.getClearAmt());
		// 创建用户
		billClearLogDTO.setCreateUserId("SYS");
		//插入清分日志表
		baseDao.execute("PayJob.insertBillClearLog", billClearLogDTO);
		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("payType", billClearLogDTO.getPayType());
		paramMap.put("payTime", billClearLogDTO.getPayTime());
		BillClearSumEntity billClearSumEntity = baseDao.queryForObject("PayJob.clearSumObject", paramMap,
				BillClearSumEntity.class);
		if (billClearSumEntity != null) {
			paramMap.put("clearNum",billClearSumEntity.getClearNum() + billClearLogDTO.getClearNum());
			paramMap.put("clearAmt",addDouble(billClearSumEntity.getClearAmt(), billClearLogDTO.getClearAmt()));
			paramMap.put("clearSuccessNum", billClearSumEntity.getClearSuccessNum() + billClearLogDTO.getClearNum());
			paramMap.put("clearSuccessAmt",addDouble(billClearSumEntity.getClearSuccessAmt(),billClearLogDTO.getClearAmt()));
			paramMap.put("updateUserId", "SYS");
			baseDao.execute("PayJob.updateClearSum", paramMap);
		} else {
			BillClearSumEntity entity = new BillClearSumEntity();
			entity.setPayType(billClearLogDTO.getPayType());
			entity.setPayTime(billClearLogDTO.getPayTime());
			entity.setClearNum(billClearLogDTO.getClearNum());
			entity.setClearAmt(billClearLogDTO.getClearAmt());
			entity.setClearSuccessAmt(billClearLogDTO.getClearAmt());
			entity.setClearSuccessNum(billClearLogDTO.getClearNum());
			entity.setCreateUserId("SYS");
			baseDao.execute("PayJob.insertClearSum", entity);
		}
	}
	
	/**
	 * 清分退款账单
	 * 目前只有支付渠道为支付宝支付的才有退款清分
	 */
	public BillClearLogDTO clearRefundBill(String checkDate,BillClearLogDTO billClearLogDTO)  throws Exception {
		List<RefundRecordDTO> refundRecordList = null;
		billClearLogDTO.setPayTime(checkDate);
		// 退款账款
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("payTime", checkDate);
		// 获取带清算的退款订单
		refundRecordList = baseDao.queryForList("Refund.queryRefundOrderNo", paramMap, RefundRecordDTO.class);
		for (RefundRecordDTO refundRecordDTO : refundRecordList) {
			// 获取退款订单明细
			paramMap.clear();
			paramMap.put("refundNo", refundRecordDTO.getRefundNo());
			List<RefundFeeItemDetailDTO> refundFeeItemDetailList = baseDao
					.queryForList("Refund.queryRefundItemList", paramMap, RefundFeeItemDetailDTO.class);
			if(refundFeeItemDetailList.size() > 0){
				ClearBillDTO clearBillDTO = new ClearBillDTO();
				clearBillDTO.setOrderNo(refundRecordDTO.getOrderNo());
				// 获取退款订单买卖物流公司用户ID
				paramMap.clear();
				paramMap.put("orderNo", refundRecordDTO.getOrderNo());
				List<PayTradeDTO> tradeList = baseDao.queryForList("Refund.queryPayTradeList", paramMap, PayTradeDTO.class);
				PayTradeDTO payTradeDTO = null;
				if (tradeList != null && tradeList.size() > 0) {
					payTradeDTO = tradeList.get(0);
				}
				
				Double sellerPenaltyAmt = 0d;
				Double platPenaltyAmt = 0d;
				Double logisPenaltyAmt = 0d;
				Double penaltyAmt = 0d;
				for (RefundFeeItemDetailDTO refundFeeItemDetailDTO : refundFeeItemDetailList) {
					// 赔付卖家违约金
					if ("1".equals(refundFeeItemDetailDTO.getFeeType())) {
						sellerPenaltyAmt = refundFeeItemDetailDTO.getFeeAmt();
					}
					// 赔付平台违约金
					else if ("2".equals(refundFeeItemDetailDTO.getFeeType())) {
						platPenaltyAmt = refundFeeItemDetailDTO.getFeeAmt();
					}
					// 赔付物流公司违约金
					else if ("3".equals(refundFeeItemDetailDTO.getFeeType())) {
						logisPenaltyAmt = refundFeeItemDetailDTO.getFeeAmt();
					}
				}
				penaltyAmt = addDouble(addDouble(sellerPenaltyAmt,platPenaltyAmt),logisPenaltyAmt);
				// 增加赔付卖家违约金
				clearBillDTO.setSellerId(Integer.valueOf(payTradeDTO.getPayeeUserId()));
				clearBillDTO.setSellerPenaltyAmt(sellerPenaltyAmt);
				// 增加赔付平台违约金
				clearBillDTO.setPlatPenaltyAmt(platPenaltyAmt);
				// 增加赔付物流公司违约金
				clearBillDTO.setLogisUserId(Integer.valueOf(payTradeDTO.getLogisUserId()));
				clearBillDTO.setLogisPenaltyAmt(logisPenaltyAmt);
				if("2".equals(refundRecordDTO.getRefundType())){
					// 买家赔付三方违约金后的退款金额-买家退款收入金额
					clearBillDTO.setBuyerId(Integer.valueOf(payTradeDTO.getPayerUserId()));
					clearBillDTO.setBuyerRefundAmt(refundRecordDTO.getRefundAmt());
					// 更改状态
					paramMap.clear();
					paramMap.put("refundNo", refundRecordDTO.getRefundNo());
					paramMap.put("status", "4");// 已清算
					baseDao.execute("Refund.updStatusByRefundNo", paramMap);
				}
				billClearLogDTO.setClearNum(billClearLogDTO.getClearNum() + 1);
				billClearLogDTO.setClearAmt(addDouble(penaltyAmt, refundRecordDTO.getRefundAmt()));
				// 插入数据库
				clearBillDTO.setClearType("ALIPAY_H5");
				clearBillDTO
						.setPlatUserId(Integer.parseInt(gdProperties.getProperties().getProperty("maven.gd.memberId")));
				addRefundClearBillInfo(clearBillDTO);
				paramMap.clear();
				paramMap.put("refundNo", refundRecordDTO.getRefundNo());
				paramMap.put("clearState", "1");// 已清算
				baseDao.execute("Refund.updItemStatusByRefundNo", paramMap);
			}
		}
		return billClearLogDTO;
	}
	
	/**
	 * 根据订单号判断是否对账成功
	 */
	private boolean isCheckSuc(String orderNo){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderNo", orderNo);
		List<PayTradeDTO> payTradeList = baseDao.queryForList("PayJob.queryByOrderNo", map, PayTradeDTO.class);
		if(payTradeList != null && payTradeList.size() > 0){
			List<String> thirdPayNumbers = new ArrayList<String>();
			for(PayTradeDTO dto : payTradeList){
				if(dto.getThirdPayNumber() != null){
					thirdPayNumbers.add(dto.getThirdPayNumber());
				}
			}
			map.clear();
			map.put("thirdPayNumbers", thirdPayNumbers);
			Integer checkCount = baseDao.queryForObject("PayJob.queryCheckByThirdNum", map, Integer.class);
			if(payTradeList.size() == checkCount){
				return true;
			}
		}
		return false;
	}
	
	private void updateFeeRecord(Map<String, Object> params) {
		if(upHasFeeRecord(String.valueOf(params.get("payCenterNumber")))) {
			params.put("rate", "");
			params.put("operaUserId", "sys");
			params.put("feeType", "0");
			params.put("financeTime", String.valueOf(params.get("payTime")));
			params.put("createUserId", "sys");
			params.put("updateUserId", "sys");
			logger.info("增加手续费," + params);
			baseDao.execute("PayJob.addFeeRecord", params);
		} else {
			params.put("feeType", "0");
			logger.info("更新手续费," + params);
			baseDao.execute("PayJob.updateFeeRecord", params);
		}
	}
	
	@Override
	public void bindOrderActRelation(String checkDate,String orderNo) throws Exception {
		String url = gdProperties.getProperties().getProperty("gd.api.pre.ordrAct.url");
		logger.info("调用清算前绑定订单活动接口,地址:" + url);
		BillClearLogDTO billClearLogDTO = new BillClearLogDTO(true);
		billClearLogDTO.setPayTime(checkDate);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("checkStatus", "1");
		if(orderNo == null || "".equals(orderNo)){
			paramMap.put("payTime", checkDate);
		}else{
			paramMap.put("orderNo", orderNo);
		}
		//正常交易账款（除退款外）
		List<PayTradeDTO> haveClearOrderList = baseDao.queryForList("PayJob.haveClearOrderList",paramMap,PayTradeDTO.class);
		for(PayTradeDTO dto : haveClearOrderList){
			//订单是否已清分
			if(isClearByOrderNo(dto.getOrderNo())){
				continue;
			}
			OrderBaseinfoDTO orderInfo = getHessianOrderbaseService().getByOrderNo(new Long(dto.getOrderNo()));
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("businessId", orderInfo.getBusinessId().toString());
			params.put("orderAmount", orderInfo.getOrderAmount().toString());
			params.put("payAmount", orderInfo.getPayAmount().toString());
			params.put("sellerId", orderInfo.getSellMemberId().toString());
			params.put("buyerId", orderInfo.getMemberId().toString());
			params.put("marketId", orderInfo.getMarketId().toString());
//			params.put("productList", productList);
			params.put("hasProduct", "true");
			params.put("ordered", "false");
			
			logger.info("调用清算前绑定订单活动接口，请求参数:" + params);
			String reqText = GdDes3.encode(JSON.toJSONString(params));
			params.clear();
			params.put("param", reqText);
			String repText = HttpUtil.httpClientPost(url, params);
			String decodeReptext = GdDes3.decode(repText);
			logger.info("调用计算价格接口，返回结果:" + decodeReptext);
			
		}
	}
	
	private OrderBaseinfoService getHessianOrderbaseService() throws MalformedURLException {
		String hessianUrl = gdProperties.getProperties().getProperty("gd.orderBaseinfo.url");
		if (orderBaseinfoService == null) {
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);
			orderBaseinfoService = (OrderBaseinfoService) factory.create(OrderBaseinfoService.class, hessianUrl);
		}
		return orderBaseinfoService;
	}
	
}
