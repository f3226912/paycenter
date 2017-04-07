package cn.gdeng.paycenter.admin.controller.admin;


import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.util.DateUtil;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.api.server.pay.BillCheckLogService;
import cn.gdeng.paycenter.api.server.pay.BillCheckSumService;
import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.dto.pay.BillCheckLogDTO;
import cn.gdeng.paycenter.dto.pay.BillCheckSumDTO;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.util.admin.web.StringUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 *  对账日志控制类
 * @author wjguo
 *
 * datetime:2016年11月11日 下午5:01:51
 */
@Controller
@RequestMapping("billCheckLog")
public class BillCheckLogController extends AdminBaseController {

	private static final GdLogger logger = GdLoggerFactory.getLogger(BillCheckLogController.class);

	/**
	 * 对账日志服务
	 */
	@Reference
	private BillCheckLogService billCheckLogService;
    /**对账汇总服务
     * 
     */
    @Reference
    private BillCheckSumService billCheckSumService;
    /**
     * 支付类型服务
     */
    @Reference
    private PayTypeService payTypeService;
	/**
	 * 支付任务服务
	 */
	@Reference
	private PayJobService payJobService;
	
	/**
	 * 跳转到对账日志列表页面
	 */
	@RequestMapping("toIndex")
	public Object toIndex(ModelAndView mv) {
		List<PayTypeDto> payTypes = payTypeService.queryAll();
		mv.addObject("payTypes", payTypes);
		mv.setViewName("paycenter/paytrade/billCheckList");
		return mv;
	}
    
    /**跳转到对账日志项列表页面
     * @param payType 支付渠道
     * @param payTime 对账日期
     * @param mv
     * @return
     */
    @RequestMapping("toItemLogList")
	public Object toItemLogList(String payType, String payTime, ModelAndView mv) {
    	//List<PayTypeDto> payTypes = payTypeService.queryAll();
    	//mv.addObject("payTypes", payTypes);
    	mv.addObject("payType", payType);
    	mv.addObject("payTime", payTime);
    	mv.setViewName("paycenter/paytrade/billCheckItemLogList");
    	return mv;
    }

    /**
     * 查询对账汇总列表数据
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="queryBillCheckSumList", produces = "application/html;charset=UTF-8")
    @ResponseBody
    public String queryBillCheckSumList(HttpServletRequest request) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		Map<String, Object> paramMap = getParametersMap(request);
    		// 记录数
    		Integer total = billCheckSumService.countByCondition(paramMap);
    		
    		// 设定分页,排序
    		setCommParameters(request, paramMap);
    		
    		// list
    		List<BillCheckSumDTO> list = null;
    		if (total < 1) {
    			list = Collections.emptyList();
    		} else {
    			list = billCheckSumService.queryByConditionPage(paramMap);
    		}
    		
    		if (logger.isDebugEnabled()) {
    			logger.debug("查询对账汇总列表数据成功，共查询出{}条数据", new Object[]{list.size()});
    		}
    		resultMap.put("total", total);
    		resultMap.put("rows", list);
    		resultMap.put("success", true);
    	} catch (Exception e) {
    		resultMap.put("success", false);
    		e.printStackTrace();
    		logger.warn("查询对账汇总列表数据错误，错误信息为：" + e.getMessage());
    	}
    	return JSONObject.toJSONString(resultMap, SerializerFeature.WriteDateUseDateFormat);
    }
    
    /**
     * 查询对账日志项列表数据
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="queryBillCheckItemLogList", produces = "application/html;charset=UTF-8")
    @ResponseBody
    public String queryBillCheckItemLogList(HttpServletRequest request) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		Map<String, Object> paramMap = getParametersMap(request);
    		// 记录数
    		Integer total = billCheckLogService.countByCondition(paramMap);
    		
    		// 设定分页,排序
    		setCommParameters(request, paramMap);
    		
    		// list
    		List<BillCheckLogDTO> list = null;
    		if (total < 1) {
    			list = Collections.emptyList();
    		} else {
    			list = billCheckLogService.queryByConditionPage(paramMap);
    		}
    		
    		if (logger.isDebugEnabled()) {
    			logger.debug("查询对账日志item列表数据成功，共查询出{}条数据", new Object[]{list.size()});
    		}
    		resultMap.put("total", total);
    		resultMap.put("rows", list);
    		resultMap.put("success", true);
    	} catch (Exception e) {
    		resultMap.put("success", false);
    		e.printStackTrace();
    		logger.warn("查询对账日志item列表数据错误，错误信息为：" + e.getMessage());
    	}
    	return JSONObject.toJSONString(resultMap, SerializerFeature.WriteDateUseDateFormat);
    }
    
    /**
     * 重新对账
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="afreshBillCheck", produces = "application/html;charset=UTF-8")
    @ResponseBody
    public String afreshBillCheck(HttpServletRequest request) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		//支付类型
    		String payType = request.getParameter("payType");
    		//支付时间
    		String payTime = request.getParameter("payTime");
    		
    		//重新对账
    		payJobService.checkBill(payType, payTime);
    		
    		if (logger.isDebugEnabled()) {
    			logger.debug("重新对账成功，对账支付渠道为{},交易支付日期为{}", new Object[]{payType, payTime});
    		}
    		resultMap.put("success", true);
    	} catch (Exception e) {
    		resultMap.put("success", false);
    		if(e.getMessage().contains("对账版本号不正确")) {
    			resultMap.put("msg", "正在对账中，请稍侯重试");
    		}
    		e.printStackTrace();
    		logger.warn("查询对账失败，错误信息为：" + e.getMessage());
    	}
    	return JSONObject.toJSONString(resultMap);
    }
    
    
	/**
	 * 导出对账汇总数据检查
	 * @param feeRecordDTO
	 * @param request
	 * @return
	 * @throws BizException 
	 */
	@RequestMapping(value = "checkExportParams", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkExportParams(HttpServletRequest request) throws BizException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		Map<String, Object> paramMap = getParametersMap(request);
    		int total = billCheckSumService.countByCondition(paramMap);
    		if (total > 10000) {
    			resultMap.put("isPassed", false);
    			resultMap.put("message", "查询结果集太大(>10000条), 请缩减日期范围, 或者修改其他查询条件...");
    		} else if (total == 0) {
    			resultMap.put("isPassed", false);
    			resultMap.put("message", "查询结果集没有任何的数据，请修改其他查询条件...");
    		} else {
    			resultMap.put("isPassed", true);
    			resultMap.put("message", "参数检测通过");
    		}
    		resultMap.put("success", true);
    	} catch (Exception e) {
    		resultMap.put("success", false);
    		e.printStackTrace();
    		logger.warn("导出对账汇总数据检查错误，错误信息为：" + e.getMessage());
    	}
    	
		return JSONObject.toJSONString(resultMap);
	}
	
	/**
	 * 导出对账汇总数据Excel
	 * @param request
	 */
	@RequestMapping("exportExcel")
	public void exportExcel(HttpServletRequest request) {
		Map<String, Object> paramMap = getParametersMap(request);
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息，
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName  = new String(("对账日志" + DateUtil.getSysDateString()).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			wwb = Workbook.createWorkbook(ouputStream);
			// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
			WritableSheet sheet = wwb.createSheet("对账日志", 0);
			sheet.setColumnView(0, 15);
			sheet.setColumnView(1, 15);
			sheet.setColumnView(2, 15);
			sheet.setColumnView(3, 15);
			sheet.setColumnView(4, 15);
			sheet.setColumnView(5, 15);
			sheet.setColumnView(6, 15);
			sheet.setColumnView(7, 15);
			
			String[] titles = {"支付渠道", "交易支付日期", "对账笔数", "对账金额", "对账成功笔数", 
					"对账成功金额", "对账失败笔数", "对账失败金额"};
			
			for(int i = 0, len = titles.length; i < len; i++) {
				// 第一个参数表示列，第二个参数表示行
				Label titleLable = new Label(i, 0, titles[i]);
				sheet.addCell(titleLable);
			}
			
			List<BillCheckSumDTO> billCheckSums = billCheckSumService.queryForExcel(paramMap);
			for(int i = 0, len = billCheckSums.size(); i < len; i++) {
				BillCheckSumDTO dto = billCheckSums.get(i);
				Label lablel1 = new Label(0, i+1, dto.getPayTypeName() == null ? "" : dto.getPayTypeName());
				Label lablel2 = new Label(1, i+1, dto.getPayTime() == null ? "" : StringUtil.DateToStr(dto.getPayTime(), "yyyy-MM-dd"));
				Label lablel3 = new Label(2, i+1, dto.getCheckNum() == null ? "" : dto.getCheckNum().toString());
				Label lablel4 = new Label(3, i+1, dto.getCheckAmt() == null ? "" : formatMoney(dto.getCheckAmt()));
				Label lablel5 = new Label(4, i+1, dto.getCheckSuccessNum() == null ? "" : dto.getCheckSuccessNum().toString());
				Label lablel6 = new Label(5, i+1, dto.getCheckSuccessAmt() == null ? "" : formatMoney(dto.getCheckSuccessAmt()));
				Label lablel7 = new Label(6, i+1, dto.getCheckFailNum() == null ? "" : dto.getCheckFailNum().toString());
				Label lablel8 = new Label(7, i+1, dto.getCheckFailAmt() == null ? "" : formatMoney(dto.getCheckFailAmt()));
				
				sheet.addCell(lablel1);
				sheet.addCell(lablel2);
				sheet.addCell(lablel3);
				sheet.addCell(lablel4);
				sheet.addCell(lablel5);
				sheet.addCell(lablel6);
				sheet.addCell(lablel7);
				sheet.addCell(lablel8);
			}
		
			
			wwb.write();
			wwb.close();
			logger.debug("导出对账汇总数据excel成功");
		} catch(Exception e){
			e.printStackTrace();
			logger.warn("导出对账汇总数据excel错误，错误信息为：" + e.getMessage());
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
