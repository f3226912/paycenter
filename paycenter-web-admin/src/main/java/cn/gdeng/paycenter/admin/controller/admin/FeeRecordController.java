package cn.gdeng.paycenter.admin.controller.admin;


import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;
import com.mongodb.util.Hash;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.PayTradeDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.FeeRecordService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.dto.pay.FeeRecordDTO;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * 手续费处理
 * @author ailen
 */
@Controller
@RequestMapping("fee")
public class FeeRecordController extends AdminBaseController {
	  /** 记录日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(FeeRecordController.class);
    
    @Reference
    private FeeRecordService feeRecordService;
    
    @RequestMapping("receive/index")
    public String index(Model model){
    	return "fee/feeList";
    }
    
    @RequestMapping("pay/index")
    public String payIndex(Model model){
		return "fee/payList";
    }
    
    /**
     * 获取手续费信息列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("getfeeList")
    @ResponseBody
    public String getfeeList(HttpServletRequest request,FeeRecordDTO feeRecordDTO){
		Map<String, Object> map = new HashMap<>();
		map.put("appKey", feeRecordDTO.getAppKey());
		map.put("feeType", feeRecordDTO.getFeeType());
		if(!StringUtils.isEmpty(feeRecordDTO.getFinanceBeginTime())) {
		map.put("financeBeginTime", feeRecordDTO.getFinanceBeginTime() + " 00:00:00");
		}
		if(!StringUtils.isEmpty(feeRecordDTO.getFinanceEndTime())) {
		map.put("financeEndTime", feeRecordDTO.getFinanceEndTime() + " 23:59:59");
		}
		map.put("thridPayNumber", feeRecordDTO.getThridPayNumber());
		map.put("orderNo", feeRecordDTO.getOrderNo());
		//设定分页,排序
		setCommParameters(request, map);
		ApiResult<AdminPageDTO> apiResult = feeRecordService.queryPage(map);
		if(apiResult != null){
			return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
    }
    //结算手续费
    @RequestMapping("getfeePayList")
    @ResponseBody
    public String getfeePayList(HttpServletRequest request,FeeRecordDTO feeRecordDTO){
		Map<String, Object> map = new HashMap<>();
		map.put("appKey", feeRecordDTO.getAppKey());
		map.put("feeType", feeRecordDTO.getFeeType());
		if(!StringUtils.isEmpty(feeRecordDTO.getFinanceBeginTime())) {
		map.put("financeBeginTime", feeRecordDTO.getFinanceBeginTime() + " 00:00:00");
		}
		if(!StringUtils.isEmpty(feeRecordDTO.getFinanceEndTime())) {
		map.put("financeEndTime", feeRecordDTO.getFinanceEndTime() + " 23:59:59");
		}
		map.put("thridPayNumber", feeRecordDTO.getThridPayNumber());
		map.put("orderNo", feeRecordDTO.getOrderNo());
		//设定分页,排序
		setCommParameters(request, map);
		ApiResult<AdminPageDTO> apiResult = feeRecordService.queryPayListPage(map);
		if(apiResult != null){
			return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
    }
    
    /**
     * 添加页面
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("detail")
    public String save(HttpServletRequest request, Model model) {
    	
    	return "fee/feeDetail";
    }
    
	/**
     * 添加手续费
     * 
     * @param request
     * @param response
     * @return
     */
    
	@RequestMapping("save")
	@ResponseBody
    public String save(FeeRecordDTO feeRecordDTO, HttpServletRequest request, Model model) {

		feeRecordDTO.setCreateUserId(getUser(request).getUserID());
		feeRecordDTO.setOperaUserId(getUser(request).getUserID());
		feeRecordDTO.setIsSys(1);
		feeRecordService.addFeeRecord(feeRecordDTO);

		return JSONObject.toJSONString("{code:'success'}",SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
     * 修改页面
     * @param request
     * @param nstDto
     * @return
	 * @throws BizException 
     */
    @RequestMapping("detail/get")
    public String updateFee(FeeRecordDTO feeRecordDTO,HttpServletRequest request, Model model) throws BizException {
    	System.out.println(feeRecordDTO.getId());
    	FeeRecordDTO feeRecord = this.feeRecordService.queryById(feeRecordDTO.getId());
    	request.setAttribute("feeRecord",feeRecord);
    	request.setAttribute("operaTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(feeRecord.getOperaTime()));
    	PayTradeEntity payTradeEntity = new PayTradeEntity();
    	if(null != feeRecord.getPayCenterNumber() && !feeRecord.getPayCenterNumber().equals("")){
    		payTradeEntity = this.feeRecordService.queryPayTradeByPayCenterNumber(feeRecord.getPayCenterNumber());
    	}
    	request.setAttribute("payTradeEntity", payTradeEntity);
    	return "fee/editFeeDetail";
    }
    
    /**
     * 修改保存页面
     * @param request
     * @param nstDto
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws ParseException 
     */
    @RequestMapping("detail/update")
    @ResponseBody
    public Map<String,Object> updateFeeSave(FeeRecordDTO feeRecordDTO, HttpServletRequest request, Model model) throws IllegalAccessException, InvocationTargetException, ParseException {
    	Map<String, Object> map = new HashMap<>();
    	FeeRecordEntity	feeRecordEntity = new FeeRecordEntity();
    	feeRecordEntity.setId(feeRecordDTO.getId());
    	feeRecordEntity.setFeeType(feeRecordDTO.getFeeType());
    	feeRecordEntity.setPayAmt(feeRecordDTO.getPayAmt());
    	feeRecordEntity.setThridPayNumber(feeRecordDTO.getThridPayNumber());
    	feeRecordEntity.setOperaTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("operaTime")));
    	feeRecordEntity.setRemark(feeRecordDTO.getRemark());
    	feeRecordEntity.setIsSys(1);
    	try {
    		this.feeRecordService.updateFeeDetailAndAppKey(request.getParameter("appKey"), feeRecordEntity);
    		map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
    	return map;
    }
    
    @RequestMapping("accsysconfig/all")
    @ResponseBody
    public Map<String,Object> getAccSysConfigAll(){
    	Map<String, Object> map = new HashMap<>();
    	List<AccessSysConfigEntity> data;
		try {
			data = this.feeRecordService.getAccSysConfigs();
			map.put("success", true);
	    	map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
    	return map;
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
	public String checkExportParams(FeeRecordDTO feeRecordDTO, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (getDistanceDays(feeRecordDTO.getFinanceBeginTime(), feeRecordDTO.getFinanceEndTime()) > 31) {
				result.put("status", 0);
				result.put("message", "时间范围必须小于31天");
				return JSONObject.toJSONString(result);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			
			if (!StringUtils.isEmpty(feeRecordDTO.getFinanceBeginTime())) {
				map.put("financeBeginTime", feeRecordDTO.getFinanceBeginTime() + " 00:00:00");
			}
			if (!StringUtils.isEmpty(feeRecordDTO.getFinanceEndTime())) {
				map.put("financeEndTime", feeRecordDTO.getFinanceEndTime() + " 23:59:59");
			}
			map.put("feeType", feeRecordDTO.getFeeType());
			int total = feeRecordService.queryCount(map);
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
	
	@ResponseBody
	@RequestMapping(value = "checkPayExportParams", produces = "application/json; charset=utf-8")
	public String checkPayExportParams(FeeRecordDTO feeRecordDTO, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (getDistanceDays(feeRecordDTO.getFinanceBeginTime(), feeRecordDTO.getFinanceEndTime()) > 31) {
				result.put("status", 0);
				result.put("message", "时间范围必须小于31天");
				return JSONObject.toJSONString(result);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			
			if (!StringUtils.isEmpty(feeRecordDTO.getFinanceBeginTime())) {
				map.put("financeBeginTime", feeRecordDTO.getFinanceBeginTime() + " 00:00:00");
			}
			if (!StringUtils.isEmpty(feeRecordDTO.getFinanceEndTime())) {
				map.put("financeEndTime", feeRecordDTO.getFinanceEndTime() + " 23:59:59");
			}
			map.put("feeType", feeRecordDTO.getFeeType());
			int total = feeRecordService.queryPayCount(map);
			if (total > 10000) {
				result.put("status", 0);
				result.put("message", "查询结果集太大(>10000条), 请缩减日期范围, 或者修改其他查询条件...");
				return JSONObject.toJSONString(result);
			}
			result.put("status", 1);
			result.put("message", "参数检测通过");
		} catch (Exception e) {
			logger.info("product checkPayExportParams with ex : {} ", new Object[] { e });
		}
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 导出Excel文件
	 * @return
	 * @author lidong
	 */
	@RequestMapping(value = "exportData")
	public String exportData(FeeRecordDTO feeRecordDTO, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(feeRecordDTO.getFinanceBeginTime())) {
			map.put("financeBeginTime", feeRecordDTO.getFinanceBeginTime() + " 00:00:00");
		}
		if (!StringUtils.isEmpty(feeRecordDTO.getFinanceEndTime())) {
			map.put("financeEndTime", feeRecordDTO.getFinanceEndTime() + " 23:59:59");
		}
		map.put("feeType", feeRecordDTO.getFeeType());
		map.put("thridPayNumber", feeRecordDTO.getThridPayNumber());
		
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息，
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			
			String fileName  = new String(("代收手续费"+new SimpleDateFormat("yyyyMMdd").format(new Date())).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<FeeRecordDTO> list = feeRecordService.queryByCondition(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet("代收手续费", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 0, "第三方支付");// 填充第一行第一个单元格的内容
				Label label10 = new Label(1, 0, "支付方式");// 填充第一行第二个单元格的内容
				Label label20 = new Label(2, 0, "代收金额");// 填充第一行第三个单元格的内容
				Label label30 = new Label(3, 0, "手续费");// 填充第一行第五个单元格的内容
				Label label40 = new Label(4, 0, "支付时间");// 填充第一行第七个单元格的内容
				sheet.addCell(label00);// 将单元格加入表格
				sheet.addCell(label10);
				sheet.addCell(label20);
				sheet.addCell(label30);
				sheet.addCell(label40);
				/*** 循环添加数据到工作簿 ***/
				if (list != null && list.size() > 0) {
					DecimalFormat    df   = new DecimalFormat("######0.00");   
					SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						FeeRecordDTO item = list.get(i);
						Label label0 = null;
						Label label2 = null;
						Label label3 = null;
						Label label4 = null;
						Label label5 = null;
						if(null != item.getThridPayNumber() && !item.getThridPayNumber().equals("")){
							label0 = new Label(0, i + 1,item.getThridPayNumber());
						}else{
							label0 = new Label(0, i + 1,"-");
						}
						if(null != item.getPayType() && !item.getPayType().equals("")){
							label2 = new Label(1, i + 1, item.getPayType());
						}else{
							label2 = new Label(1, i + 1,"-");
						}
						if(null != item.getPayAmt() && item.getPayAmt() != 0){
							label3 = new Label(2, i + 1,df.format(item.getPayAmt())+"");
						}else{
							label3 = new Label(2, i + 1,"-");
						}
						if(null != item.getFeeAmt() && item.getFeeAmt() != 0){
							label4 = new Label(3, i + 1,df.format(item.getFeeAmt())+"");
						}else{
							label4 = new Label(3, i + 1,"-");
						}
						if(null != item.getFinanceTime()){
							label5 = new Label(4, i + 1, time.format(item.getFinanceTime()));
						}else{
							label5 = new Label(4, i + 1,"-");
						}
						sheet.addCell(label0);
						sheet.addCell(label2);
						sheet.addCell(label3);
						sheet.addCell(label4);
						sheet.addCell(label5);
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
	@RequestMapping(value = "exportPayData")
	public String exportPayData(FeeRecordDTO feeRecordDTO, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(feeRecordDTO.getFinanceBeginTime())) {
			map.put("financeBeginTime", feeRecordDTO.getFinanceBeginTime() + " 00:00:00");
		}
		if (!StringUtils.isEmpty(feeRecordDTO.getFinanceEndTime())) {
			map.put("financeEndTime", feeRecordDTO.getFinanceEndTime() + " 23:59:59");
		}
		map.put("feeType", feeRecordDTO.getFeeType());
		map.put("thridPayNumber", feeRecordDTO.getThridPayNumber());
		
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息，
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			
			String fileName  = new String(("代付手续费"+new SimpleDateFormat("yyyyMMdd").format(new Date())).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<FeeRecordDTO> list = feeRecordService.queryFeePayList(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet("代付手续费", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 0, "第三方支付");// 填充第一行第一个单元格的内容
				Label label10 = new Label(1, 0, "支付方式");// 填充第一行第二个单元格的内容
				Label label20 = new Label(2, 0, "代付金额");// 填充第一行第三个单元格的内容
				Label label30 = new Label(3, 0, "手续费");// 填充第一行第五个单元格的内容
				Label label40 = new Label(4, 0, "代付时间");// 填充第一行第七个单元格的内容
				sheet.addCell(label00);// 将单元格加入表格
				sheet.addCell(label10);
				sheet.addCell(label20);
				sheet.addCell(label30);
				sheet.addCell(label40);
				/*** 循环添加数据到工作簿 ***/
				if (list != null && list.size() > 0) {
					DecimalFormat    df   = new DecimalFormat("######0.00");   
					SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						FeeRecordDTO item = list.get(i);
						Label label0 = null;
						Label label2 = null;
						Label label3 = null;
						Label label4 = null;
						Label label5 = null;
						if(null != item.getThridPayNumber() && !item.getThridPayNumber().equals("")){
							label0 = new Label(0, i + 1,item.getThridPayNumber());
						}else{
							label0 = new Label(0, i + 1,"-");
						}
						if(null != item.getPayType() && !item.getPayType().equals("")){
							label2 = new Label(1, i + 1, item.getPayType());
						}else{
							label2 = new Label(1, i + 1,"-");
						}
						if(null != item.getPayAmt() && item.getPayAmt() != 0){
							label3 = new Label(2, i + 1,df.format(item.getPayAmt())+"");
						}else{
							label3 = new Label(2, i + 1,"-");
						}
						if(null !=item.getFeeAmt() && item.getFeeAmt() != 0){
							label4 = new Label(3, i + 1,df.format(item.getFeeAmt())+"");
						}else{
							label4 = new Label(3, i + 1,"-");
						}
						if(null != item.getFinanceTime()){
							label5 = new Label(4, i + 1, time.format(item.getFinanceTime()));
						}else{
							label5 = new Label(4, i + 1,"-");
						}
						sheet.addCell(label0);
						sheet.addCell(label2);
						sheet.addCell(label3);
						sheet.addCell(label4);
						sheet.addCell(label5);
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
	
	public static long getDistanceDays(String str1, String str2) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date one;
		Date two;
		long days = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
	
}
