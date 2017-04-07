package cn.gdeng.paycenter.admin.controller.admin;


import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.PayTradeDTO;
import cn.gdeng.paycenter.admin.service.admin.AdminPayTradeService;
import cn.gdeng.paycenter.api.server.customer.CustomerHessianService;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
import cn.gdeng.paycenter.dto.pay.TradeRecordDTO;
import cn.gdeng.paycenter.dto.pay.TradeRecordDetailDTO;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;



/**
 * 交易记录controller
 * 
 */
@Controller
@RequestMapping("paycenter/paytrade")
public class AdminPayTradeController extends AdminBaseController {

	private static final GdLogger logger = GdLoggerFactory.getLogger(AdminPayTradeController.class);

    /** 用户Serivce */
    @Reference
    private AdminPayTradeService adminPayTradeService;
    
    @Resource
	CustomerHessianService customerHessianService;
    
	/**
	 * 显示交易记录
	 */
    @RequestMapping("list")
	public Object showTrade(PayTradeEntity payTradeEntity,ModelAndView mv) {
    
    	//查询所有付款来源
    	List<Map<String, String>> payerOrderSourceList=adminPayTradeService.getPayerOrderSourceEnum();
    	mv.addObject("payerOrderSourceList", payerOrderSourceList);
    	
    	//查询所有所属市场
    	List<MarketHessianDTO> marketList = customerHessianService.getAllByType("2");
    	mv.addObject("marketList", marketList);
    	
    	//查询所有订单类型
    	List<Map<String, String>> orderTypeList=adminPayTradeService.getOrderTypeEnum();
    	mv.addObject("orderTypeList", orderTypeList);
    	
    	mv.setViewName("paycenter/paytrade/tradelist");
    	return mv;
    
    }

    /**
     * 查询交易记录
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping("queryTradeList")
    @ResponseBody
    public String queryTradeList(HttpServletRequest request) {
        try {
            Map<String, Object> map = getParametersMap(request);
            Map<String, Object> resultMap = new HashMap<>();
            // 记录数
            int total=adminPayTradeService.getTradeRecordTotal(map);
         
            // 设定分页,排序
            setCommParameters(request, map);
           
            List<TradeRecordDTO> list=Collections.emptyList();
            if (total>0) {
                // list
                 list = adminPayTradeService.queryTradeRecordList(map);
                //map.put("rows", list);// rows键 存放每页记录 list
			}
            resultMap.put("total",total);
            resultMap.put("rows", list);
            return JSONObject.toJSONString(resultMap, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }
    
    /**
     * 查询交易记录详情
     * 
     * @param request
     * @return
     * 
     */
    @RequestMapping("detail")
    public String queryTradeDetail(HttpServletRequest request) throws Exception {
        String orderNo = request.getParameter("orderNo");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
       // PayTradeDTO payTradeDTO = adminPayTradeService.queryTradeDetail(map);
        TradeRecordDetailDTO payTradeDTO = adminPayTradeService.queryTradeRecordDetail(map);
        request.setAttribute("dto", payTradeDTO);
        return "paycenter/paytrade/tradedetail";
    }
    /**
     * 一条订单，查询多条交易流水
     * */
    @RequestMapping("detailByOrderNo")
    @ResponseBody
    public String getTradeDetail(HttpServletRequest request){
    	Map<String, Object> params = new HashMap<>();
    	params.put("orderNo", request.getParameter("orderNo"));
    	Map<String, Object> map = new HashMap<>();
    	try {
			List<PayTradeEntity> list = this.adminPayTradeService.queryTradeByOrderNo(params);
			map.put("success", true);
			map.put("total", list.size());
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
    	return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
    }
    
    @RequestMapping("paylist")
	public String showPayTrade(PayTradeEntity payTradeEntity) {
    	return "paycenter/paytrade/paytradelist";
    }
    
    /**
     * 查询所有市场
     * @Description:
     * @param modelMap
     * @return
     *
     */
    @RequestMapping("/marketList")
	public String list(ModelMap modelMap){
		List<MarketHessianDTO> marketList = customerHessianService.getAllByType("2");
		modelMap.addAttribute("marketList", marketList);
		return "agencyCollection/list";
	}
    
    
    
    /**
     * 查询付款记录
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping("queryPayTradeList")
    @ResponseBody
    public String queryPayTradeList(HttpServletRequest request) {
        try {
            Map<String, Object> map = getParametersMap(request);
            // 记录数
            map.put("total", adminPayTradeService.getPayTradePageTotal(map));
            // 设定分页,排序
            setCommParameters(request, map);
            // list
            List<PayTradeDTO> list = adminPayTradeService.queryPayTradePageList(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            return getExceptionMessage(e, logger);
        }
    }
    
	/**
	 * 审核交易记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "auditPayTrade", produces = "application/html;charset=UTF-8")
	@ResponseBody
	public String auditPayTrade(String ids) {
		Map<String, Object> map = new HashMap<>();
		try {
			SysRegisterUser user = getUser(request);
			if(user == null){
				map.put("errMsg", "操作异常，请重新登陆");
			} else {
			    if (StringUtils.isEmpty(ids)) {
	                map.put("errMsg", "请选择要操作的数据");
	            } else {
		            List<String> list = Arrays.asList(ids.split(","));
	            	adminPayTradeService.auditPayTrade(list, user.getUserID());
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errMsg", "服务器出错");
		}
		return JSONObject.toJSONString(map);
	}
	
    /**
     * 查询付款记录详情
     * 
     * @param request
     * @return
     * 
     */
    @RequestMapping("viewpaydetail")
    public String queryViewPayTradeDetail(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        PayTradeDTO payTradeDTO = adminPayTradeService.queryTradeDetail(map);
        request.setAttribute("dto", payTradeDTO);
        return "paycenter/paytrade/viewpaytradedetail";
    }
	
    /**
     * 修改付款记录
     * 
     * @param request
     * @return
     * 
     */
    @RequestMapping("paydetail")
    public String queryPayTradeDetail(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        PayTradeDTO payTradeDTO = adminPayTradeService.queryTradeDetail(map);
        request.setAttribute("dto", payTradeDTO);
        return "paycenter/paytrade/paytradedetail";
    }
    
	/**
	 * 检测导出参数,检测通过则后续会在页面启动文件下载
	 * 
	 * @param request
	 * @return
	 * @author lidong
	 */
	@ResponseBody
	@RequestMapping(value = "checkExportParams", produces = "application/json; charset=utf-8")
	public String checkExportParams(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (DateUtil.isDateIntervalOverFlow(request.getParameter("startDate"), request.getParameter("endDate"), 31)){
				result.put("status", 0);
				result.put("message", "请选择正确的日期范围, 系统最大支持范围为31天..");
				return JSONObject.toJSONString(result);
			}
            Map<String, Object> map = getParametersMap(request);
			int total = adminPayTradeService.getTradeRecordExportDataTotal(map);
			if (total > 10000) {
				result.put("status", 0);
				result.put("message", "查询结果集太大(>10000条), 请缩减日期范围, 或者修改其他查询条件...");
				return JSONObject.toJSONString(result);
			}
			result.put("status", 1);
			result.put("message", "参数检测通过");
		} catch (Exception e) {
			logger.info("product checkExportParams with ex : {} ", new Object[] { e });
		}
		return JSONObject.toJSONString(result);
	}
    
/*    *//**
     * 导出交易记录
     * 
     * @param request
     * @throws Exception
     *//*
    @RequestMapping("exportData")
    @ResponseBody
    public String exportData(HttpServletRequest request) {
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
            Map<String, Object> map = getParametersMap(request);
			// 设置输出响应头信息，
            String exportType = request.getParameter("exportType");
            String name = "";
            if("1".equals(exportType)){
            	name = "交易记录";
            } else if("2".equals(exportType)){
            	name = "付款记录";
            }
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName  = new String(name.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<PayTradeDTO> list = adminPayTradeService.queryPayTradeList(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet(name + "列表", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				WritableCellFormat wcf = new WritableCellFormat();  
		        wcf.setAlignment(Alignment.CENTRE);
				sheet.mergeCells(0,0,5,0);
				sheet.mergeCells(6,0,13,0);
				sheet.mergeCells(14,0,25,0);
				sheet.mergeCells(26,0,27,0);
				sheet.mergeCells(28,0,33,0);
				sheet.addCell(new Label(0, 0, "交易信息", wcf));
				sheet.addCell(new Label(6, 0, "付款方信息", wcf));
				sheet.addCell(new Label(14, 0, "收款方信息", wcf));
				sheet.addCell(new Label(26, 0, "谷登第三方账户信息", wcf));
				sheet.addCell(new Label(28, 0, "谷登转账信息", wcf));
				String[] titles={"订单号","平台支付流水","下单时间","商品信息","付款方来源","订单金额","用户账号",
						"用户手机号","付款方式","付款第三方账号","第三方流水","支付时间","实付金额","支付状态","用户账号","用户手机号",
						"收款方姓名","收款方式","收款时间","实收金额","持卡人姓名","银行卡号","开户银行名称","开户行所在地","支行名称",
						"银行预留手机","实收净额","手续费","开户人","支付银行","转账流水","转账金额","转账手续费","转账时间"};
				for (int i = 0; i < titles.length; i++) {
					sheet.setColumnView(i, 18);
					sheet.addCell(new Label(i, 1, titles[i]));
				}
				*//*** 循环添加数据到工作簿 ***//*
				if (list != null && list.size() > 0) {
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						PayTradeDTO item = list.get(i);
						sheet.addCell(new Label(0, i + 2, item.getOrderNo()));
						sheet.addCell(new Label(1, i + 2, item.getPayCenterNumber()));
						sheet.addCell(new Label(2, i + 2, item.getOrderTime()==null?"":DateFormatUtils.format(item.getOrderTime(), "yyyy-MM-dd HH:mm:ss")));
						sheet.addCell(new Label(3, i + 2, item.getTitle()));
						String appkey = "";
						if("nst_car".equals(item.getAppKey())){
							appkey = "农速通-车主";
						} else if("nst_goods".equals(item.getAppKey())){
							appkey = "农速通-货主";
						} else if("nps".equals(item.getAppKey())){
							appkey = "农批商";
						} else if("nsy".equals(item.getAppKey())){
							appkey = "农商友";
						} else if("gys".equals(item.getAppKey())){
							appkey = "供应商";
						}
						sheet.addCell(new Label(4, i + 2, appkey));
						sheet.addCell(new Label(5, i + 2, String.format("%.2f", item.getTotalAmt())));
						sheet.addCell(new Label(6, i + 2, item.getPayerAccount()));
						sheet.addCell(new Label(7, i + 2, item.getPayerMobile()));
						String payType = "";
						if("ALIPAY_H5".equals(item.getPayType())){
							payType = "支付宝";
						} else if("WEIXIN_APP".equals(item.getPayType())){
							payType = "微信";
						} else if("PINAN".equals(item.getPayType())){
							payType = "平安银行";
						}
						sheet.addCell(new Label(8, i + 2, payType));
						sheet.addCell(new Label(9, i + 2, item.getThirdPayerAccount()));
						sheet.addCell(new Label(10, i + 2, item.getThirdPayNumber()));
						sheet.addCell(new Label(11, i + 2, item.getPayTime()==null? "" : DateFormatUtils.format(item.getPayTime(), "yyyy-MM-dd HH:mm:ss")));
						sheet.addCell(new Label(12, i + 2, item.getPayAmt() == null ? "" : String.format("%.2f", item.getPayAmt())));
						String payStatus = "";
						if("1".equals(item.getPayStatus())){
							payStatus = "待付款";
						} else if("2".equals(item.getPayStatus())){
							payStatus = "付款成功";
						} else if("3".equals(item.getPayStatus())){
							payStatus = "已关闭";
						} else if("4".equals(item.getPayStatus())){
							payStatus = "已退款";
						}
						sheet.addCell(new Label(13, i + 2, payStatus));
						sheet.addCell(new Label(14, i + 2, item.getPayeeAccount()));
						sheet.addCell(new Label(15, i + 2, item.getPayeeMobile()));
						sheet.addCell(new Label(16, i + 2, item.getRealName()));
						String depositBankName = item.getDepositBankName();
						if(StringUtils.isNotBlank(item.getBankCardNo())){
							if(item.getBankCardNo().length() > 4) {
								depositBankName += "(尾号" + 
										item.getBankCardNo().substring(item.getBankCardNo().length() - 4, item.getBankCardNo().length()) + ")";
							} else {
								depositBankName += "(尾号" + item.getBankCardNo() + ")";
							}
						}
						sheet.addCell(new Label(17, i + 2, depositBankName));
						sheet.addCell(new Label(18, i + 2, item.getTransferTime()==null? "" : DateFormatUtils.format(item.getTransferTime(), "yyyy-MM-dd HH:mm:ss")));
						sheet.addCell(new Label(19, i + 2, item.getTransferAmt() == null ? "" : String.format("%.2f", item.getTransferAmt())));
						sheet.addCell(new Label(20, i + 2, item.getRealName()));
						sheet.addCell(new Label(21, i + 2, item.getBankCardNo()));
						sheet.addCell(new Label(22, i + 2, item.getDepositBankName()));
						String provinceName = "";
						if(StringUtils.isNotBlank(item.getProvinceName())){
							provinceName = item.getProvinceName();
						} else if(StringUtils.isNotBlank(item.getCityName())){
							provinceName += "-" + item.getCityName();
						} else if(StringUtils.isNotBlank(item.getAreaName())){
							provinceName += "-" + item.getAreaName();
						}
						sheet.addCell(new Label(23, i + 2, provinceName));
						sheet.addCell(new Label(24, i + 2, item.getSubBankName()));
						sheet.addCell(new Label(25, i + 2, item.getUserBankMobile()));
						sheet.addCell(new Label(26, i + 2, item.getReceiptAmt() == null ? "" : String.format("%.2f", item.getReceiptAmt())));
						sheet.addCell(new Label(27, i + 2, item.getFeeAmt() == null ? "" : String.format("%.2f", item.getFeeAmt())));
						sheet.addCell(new Label(28, i + 2, item.getTransferPayerName()));
						sheet.addCell(new Label(29, i + 2, item.getPayerBankName()));
						sheet.addCell(new Label(30, i + 2, item.getBankTradeNo()));
						sheet.addCell(new Label(31, i + 2, item.getTransferAmt() == null ? "" : String.format("%.2f", item.getTransferAmt())));
						sheet.addCell(new Label(32, i + 2, item.getTransferFeeAmt() == null ? "" : String.format("%.2f", item.getTransferFeeAmt())));
						sheet.addCell(new Label(33, i + 2, item.getTransferTime()==null? "" : DateFormatUtils.format(item.getTransferTime(), "yyyy-MM-dd HH:mm:ss")));
					}
				}
				wwb.write();// 将数据写入工作簿
			}
			wwb.close();// 关闭
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
    }
    */
    
	
    /**
     * 导出交易记录 新的
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping("tradeRecordExportData")
    @ResponseBody
    public String tradeRecordExportData(HttpServletRequest request) {
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
            Map<String, Object> map = getParametersMap(request);
			// 设置输出响应头信息，
            //tring exportType = request.getParameter("exportType");
            String d=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
            String name = "交易记录"+d;
            
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName  = new String(name.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<TradeRecordDetailDTO> list = adminPayTradeService.queryTradeRecordExportData(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet(name + "列表", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				WritableCellFormat wcf = new WritableCellFormat();  
		        wcf.setAlignment(Alignment.CENTRE);
			/*	sheet.mergeCells(0,0,5,0);*/
			/*	sheet.mergeCells(6,0,13,0);
				sheet.mergeCells(14,0,25,0);
				sheet.mergeCells(26,0,27,0);
				sheet.mergeCells(28,0,33,0);*/
				/*sheet.addCell(new Label(0, 0, "交易信息", wcf));*/
			/*	sheet.addCell(new Label(6, 0, "付款方信息", wcf));
				sheet.addCell(new Label(14, 0, "收款方信息", wcf));
				sheet.addCell(new Label(26, 0, "谷登第三方账户信息", wcf));
				sheet.addCell(new Label(28, 0, "谷登转账信息", wcf));*/
				String[] titles={"订单号","订单类型","下单时间","商品总金额","付款方手机","付款方来源",
						"付款方支付平台佣金","付款方支付市场佣金","付款方实付","谷登代收","谷登代收支付手续费",
						"收款方手机","商铺ID","商铺名称","收款方支付平台佣金","收款方支付市场佣金","刷卡补贴",
						"收款方应收金额","市场应收金额","所属市场","市场绑定手机"};
				for (int i = 0; i < titles.length; i++) {
					sheet.setColumnView(i, 20);
					sheet.addCell(new Label(i, 0, titles[i]));
				}
				/*** 循环添加数据到工作簿 ***/
				if (list != null && list.size() > 0) {
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						TradeRecordDetailDTO item = list.get(i);
						sheet.addCell(new Label(0, i + 1, item.getOrderNo()));
						sheet.addCell(new Label(1, i + 1, item.getOrderTypeStr()));
						sheet.addCell(new Label(2, i + 1, item.getOrderTime()==null?"":DateFormatUtils.format(item.getOrderTime(), "yyyy-MM-dd HH:mm:ss")));
						sheet.addCell(new Label(3, i + 1, item.getOrderAmt()));
						sheet.addCell(new Label(4, i + 1, item.getPayerMobile()));
						sheet.addCell(new Label(5, i + 1, item.getPayerOrderSourceStr()));
						sheet.addCell(new Label(6, i + 1, item.getPayerPlatCommissionAmt())); 	//平台佣金
						sheet.addCell(new Label(7, i + 1, item.getPayerCommissionAmt()));		//市场佣金
						sheet.addCell(new Label(8, i + 1, item.getPayAmt()));
						sheet.addCell(new Label(9, i + 1, item.getPayAmt()));
						sheet.addCell(new Label(10, i + 1, item.getGdFeeAmt()));
						sheet.addCell(new Label(11, i +1, item.getPayeeMobile()));
						sheet.addCell(new Label(12, i +1, item.getBusinessId()));
						sheet.addCell(new Label(13, i +1, item.getBusinessName()));
						sheet.addCell(new Label(14, i + 1,item.getPayeePlatCommissionAmt()));	//平台佣金
						sheet.addCell(new Label(15, i + 1,item.getPayeeCommissionAmt()));		//市场佣金
						sheet.addCell(new Label(16, i + 1,item.getPayeeSubsidyAmt()));
						sheet.addCell(new Label(17, i + 1,item.getPayeeAmt()));
						sheet.addCell(new Label(18, i + 1,item.getMarketReceivableCommissionAmt()));
						sheet.addCell(new Label(19, i + 1, item.getMarketName()));
						sheet.addCell(new Label(20, i + 1, item.getMarketMobile()));
					}
				}
				wwb.write();// 将数据写入工作簿
			}
			wwb.close();// 关闭
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
    }
}
