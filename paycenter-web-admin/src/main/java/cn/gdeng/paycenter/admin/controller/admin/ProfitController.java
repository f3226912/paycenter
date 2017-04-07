package cn.gdeng.paycenter.admin.controller.admin;


import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.ProfitDTO;
import cn.gdeng.paycenter.admin.service.admin.ProfitService;
import cn.gdeng.paycenter.admin.util.DateUtil;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * 收益管理Controller
 * @author youj
 * @Date:2016年12月6日上午19:32:46
 */
@Controller
@RequestMapping("profit")
public class ProfitController extends AdminBaseController {
	  /** 记录日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(ProfitController.class);
    
    @Reference
    private ProfitService profitService;
    
    /**
     * 平台佣金收益
     * */
    @RequestMapping("/platform/index")
    public String platform(Model model){
    	return "profit/platformCommissionList";
    }
    
    /**
     * 平台佣金收益列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("platformCommissionList")
    @ResponseBody
    public String platformCommissionList(HttpServletRequest request, ProfitDTO platCommissionDTO){
    	try {
	    	Map<String, Object> map = getParametersMap(request);
			if(!StringUtils.isEmpty(map.get("orderTimeBegin"))) {
				map.put("orderTimeBegin", map.get("orderTimeBegin") + " 00:00:00");
			}
			if(!StringUtils.isEmpty(map.get("orderTimeEnd"))) {
				map.put("orderTimeEnd", map.get("orderTimeEnd") + " 23:59:59");
			}
			//设定分页,排序
			setCommParameters(request, map);
			
			ApiResult<AdminPageDTO> apiResult = profitService.queryPlatCommissionPage(map);
			if(apiResult != null){
				return JSONObject.toJSONString(apiResult,SerializerFeature.WriteDateUseDateFormat);
			}
		} catch (Exception e) {
			return getExceptionMessage(e, logger);
		}
		return null;
    }
    
    /**
	 * 导出平台佣金数据检查
	 * @param profitDTO
	 * @param request
	 * @return
	 * @throws BizException 
	 */
	@RequestMapping(value = "checkPlatCommissionParam", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkPlatCommissionParam(HttpServletRequest request) throws BizException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		Map<String, Object> paramMap = getParametersMap(request);
    		if(!StringUtils.isEmpty(paramMap.get("orderTimeBegin"))) {
    			paramMap.put("orderTimeBegin", paramMap.get("orderTimeBegin") + " 00:00:00");
			}
			if(!StringUtils.isEmpty(paramMap.get("orderTimeEnd"))) {
				paramMap.put("orderTimeEnd", paramMap.get("orderTimeEnd") + " 23:59:59");
			}
    		int total = profitService.queryPlatCommissionCount(paramMap);
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
    		logger.warn("导出平台佣金收益检查错误，错误信息为：" + e.getMessage());
    	}
    	
		return JSONObject.toJSONString(resultMap);
	}
	
	/**
	 * 导出对账汇总数据Excel
	 * @param request
	 */
	@RequestMapping("exportPlatCommission")
	public void exportPlatCommission(HttpServletRequest request) {
		Map<String, Object> paramMap = getParametersMap(request);
		if(!StringUtils.isEmpty(paramMap.get("orderTimeBegin"))) {
			paramMap.put("orderTimeBegin", paramMap.get("orderTimeBegin") + " 00:00:00");
		}
		if(!StringUtils.isEmpty(paramMap.get("orderTimeEnd"))) {
			paramMap.put("orderTimeEnd", paramMap.get("orderTimeEnd") + " 23:59:59");
		}
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String content = "平台佣金收益" + DateUtil.getCurrentDate();
			String fileName  = new String(content.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			wwb = Workbook.createWorkbook(ouputStream);
			// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
			WritableSheet sheet = wwb.createSheet("平台佣金收益", 0);
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 15);
			sheet.setColumnView(2, 15);
			sheet.setColumnView(3, 15);
			sheet.setColumnView(4, 20);
			sheet.setColumnView(5, 16);
			
			String[] titles = {"关联订单号", "关联用户手机", "商品总金额", "佣金收益", "下单时间", "佣金支出方"};
					
			sheet.mergeCells(0, 0, 5, 0);
//			WritableFont font = new WritableFont(WritableFont.TIMES,16,WritableFont.BOLD);
			WritableCellFormat format = new WritableCellFormat();
			format.setAlignment(jxl.format.Alignment.CENTRE);
			Label title = new Label(0, 0, "平台佣金收益", format);
			sheet.addCell(title);
					 
			
			for(int i = 0, len = titles.length; i < len; i++) {
				// 第一个参数表示列，第二个参数表示行
				Label titleLable = new Label(i, 1, titles[i]);
				sheet.addCell(titleLable);
			}
			List<ProfitDTO> list = profitService.exportPlatCommissionRecord(paramMap);
			for(int i = 0, len = list.size(); i < len; i++) {
				ProfitDTO dto = list.get(i);
				Label lablel1 = new Label(0, i+2, dto.getOrderNo() == null ? "" : dto.getOrderNo());
				Label lablel2 = new Label(1, i+2, dto.getPayerMobile() == null ? "" : dto.getPayerMobile());
				Label lablel3 = new Label(2, i+2, dto.getTotalAmt() == null ? "" : formatMoney(dto.getTotalAmt()));
				Label lablel4 = new Label(3, i+2, dto.getCommission() == null ? "" : formatMoney(dto.getCommission()));
				Label lablel5 = new Label(4, i+2, dto.getOrderTime() == null ? "" : dto.getOrderTime());
				Label lablel6 = new Label(5, i+2, dto.getUserType() == null ? "" : dto.getUserTypeStr());
				
				sheet.addCell(lablel1);
				sheet.addCell(lablel2);
				sheet.addCell(lablel3);
				sheet.addCell(lablel4);
				sheet.addCell(lablel5);
				sheet.addCell(lablel6);
			}
			wwb.write();
			wwb.close();
			logger.debug("导出平台佣金汇总数据excel成功");
		} catch(Exception e){
			e.printStackTrace();
			logger.warn("导出平台佣金汇总数据excel错误，错误信息为：" + e.getMessage());
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 导出平台佣金数据检查
	 * @param profitDTO
	 * @param request
	 * @return
	 * @throws BizException 
	 */
	@RequestMapping(value = "checkRefundParam", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkRefundParam(HttpServletRequest request) throws BizException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		Map<String, Object> paramMap = getParametersMap(request);
    		if(!StringUtils.isEmpty(paramMap.get("orderTimeBegin"))) {
    			paramMap.put("orderTimeBegin", paramMap.get("orderTimeBegin") + " 00:00:00");
    		}
    		if(!StringUtils.isEmpty(paramMap.get("orderTimeEnd"))) {
    			paramMap.put("orderTimeEnd", paramMap.get("orderTimeEnd") + " 23:59:59");
    		}
    		int total = profitService.queryPlatCommissionCount(paramMap);
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
    		logger.warn("导出违约金收益检查错误，错误信息为：" + e.getMessage());
    	}
    	
		return JSONObject.toJSONString(resultMap);
	}
	
	/**
	 * 导出对账汇总数据Excel
	 * @param request
	 */
	@RequestMapping("exportRefund")
	public void exportRefund(HttpServletRequest request) {
		Map<String, Object> paramMap = getParametersMap(request);
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String content = "违约金收益" + DateUtil.getCurrentDate();
			String fileName  = new String(content.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			wwb = Workbook.createWorkbook(ouputStream);
			// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
			WritableSheet sheet = wwb.createSheet("违约金收益", 0);
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 15);
			sheet.setColumnView(2, 15);
			sheet.setColumnView(3, 15);
			sheet.setColumnView(4, 20);
			sheet.setColumnView(5, 16);
			
			String[] titles = {"关联订单号", "关联用户手机", "预付款", "违约金收益", "下单时间", "违约金支出"};
					
			sheet.mergeCells(0, 0, 5, 0);
//			WritableFont font = new WritableFont(WritableFont.TIMES,16,WritableFont.BOLD);
			WritableCellFormat format = new WritableCellFormat();
			format.setAlignment(jxl.format.Alignment.CENTRE);
			Label title = new Label(0, 0, "违约金收益", format);
			sheet.addCell(title);
					 
			
			for(int i = 0, len = titles.length; i < len; i++) {
				// 第一个参数表示列，第二个参数表示行
				Label titleLable = new Label(i, 1, titles[i]);
				sheet.addCell(titleLable);
			}
			
			if(!StringUtils.isEmpty(paramMap.get("orderTimeBegin"))) {
				paramMap.put("orderTimeBegin", paramMap.get("orderTimeBegin") + " 00:00:00");
			}
			if(!StringUtils.isEmpty(paramMap.get("orderTimeEnd"))) {
				paramMap.put("orderTimeEnd", paramMap.get("orderTimeEnd") + " 23:59:59");
			}
			List<ProfitDTO> list = profitService.exportRefundRecord(paramMap);
			for(int i = 0, len = list.size(); i < len; i++) {
				ProfitDTO dto = list.get(i);
				Label lablel1 = new Label(0, i+2, dto.getOrderNo() == null ? "" : dto.getOrderNo());
				Label lablel2 = new Label(1, i+2, dto.getPayerMobile() == null ? "" : dto.getPayerMobile());
				Label lablel3 = new Label(2, i+2, dto.getTotalAmt() == null ? "" : this.formatMoney(dto.getTotalAmt()));
				Label lablel4 = new Label(3, i+2, dto.getCommission() == null ? "" : this.formatMoney(dto.getCommission()));
				Label lablel5 = new Label(4, i+2, dto.getOrderTime() == null ? "" : dto.getOrderTime());
				Label lablel6 = new Label(5, i+2, dto.getUserType() == null ? "" : dto.getUserTypeStr());
				
				sheet.addCell(lablel1);
				sheet.addCell(lablel2);
				sheet.addCell(lablel3);
				sheet.addCell(lablel4);
				sheet.addCell(lablel5);
				sheet.addCell(lablel6);
			}
			wwb.write();
			wwb.close();
			logger.debug("导出违约金汇总数据excel成功");
		} catch(Exception e){
			e.printStackTrace();
			logger.warn("导出违约金汇总数据excel错误，错误信息为：" + e.getMessage());
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    /**
     * 违约金收益
     * */
    @RequestMapping("/penalty/index")
    public String penalty(Model model){
    	return "profit/refundList";
    }
    
    /**
     * 违约金收益列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("refundList")
    @ResponseBody
    public String refundList(HttpServletRequest request, ProfitDTO platCommissionDTO){
    	try {
	    	Map<String, Object> map = getParametersMap(request);
	    	if(!StringUtils.isEmpty(map.get("orderTimeBegin"))) {
	    		map.put("orderTimeBegin", map.get("orderTimeBegin") + " 00:00:00");
			}
			if(!StringUtils.isEmpty(map.get("orderTimeEnd"))) {
				map.put("orderTimeEnd", map.get("orderTimeEnd") + " 23:59:59");
			}
			//设定分页,排序
			setCommParameters(request, map);
			
			ApiResult<AdminPageDTO> apiResult = profitService.queryRefundPage(map);
			if(apiResult != null){
				return JSONObject.toJSONString(apiResult,SerializerFeature.WriteDateUseDateFormat);
			}
		} catch (Exception e) {
			return getExceptionMessage(e, logger);
		}
		return null;
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
