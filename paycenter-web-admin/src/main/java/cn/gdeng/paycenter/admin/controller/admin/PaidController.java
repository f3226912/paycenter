package cn.gdeng.paycenter.admin.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.PaidDTO;
import cn.gdeng.paycenter.admin.service.admin.MarketBankAccInfoService;
import cn.gdeng.paycenter.admin.service.admin.PaidService;
import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 代付款记录
 * @author dengjianfeng
 *
 */
@Controller
@RequestMapping("paid")
public class PaidController extends AdminBaseController{
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Reference
	private PaidService paidService;
	
	@Reference
	private PayJobService payJobService;
	
	@Reference
	private MarketBankAccInfoService marketBankAccInfoService;
	
	@RequestMapping("index")
	public String index() {
		return "paycenter/paidRecord/paidList";
	}
	
	/**
	 * 查询分页数据
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("queryPage")
	@ResponseBody
	public String queryPage(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = getParametersMap(request);
		int total = paidService.countByCondition(map);
		
		List<PaidDTO> list = null;
		if(total > 0) {
			// 设定分页,排序
			setCommParameters(request, map);
			
			try {
				ApiResult<List<PaidDTO>> apiResult = paidService.queryPage(map);
				list = apiResult.getResult();
				
				resultMap.put("code", apiResult.getCode());
				resultMap.put("msg", apiResult.getMsg());
			} catch(Exception e){
				e.printStackTrace();
			}
		} else {
			list = Collections.emptyList();
		}
		
		resultMap.put("total", total);
		resultMap.put("rows", list);
		return JSONObject.toJSONString(resultMap, SerializerFeature.WriteDateUseDateFormat);
	}
	
	
	
	/**
	 * 导出数据检查
	 * @param feeRecordDTO
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "checkExportParams", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkExportParams(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> paramMap = getParametersMap(request);
		int total = paidService.countByCondition(paramMap);
		if (total > 10000) {
			result.put("status", 0);
			result.put("message", "查询结果集太大(>10000条), 请缩减日期范围, 或者修改其他查询条件...");
			return JSONObject.toJSONString(result);
		}
		result.put("status", 1);
		result.put("message", "参数检测通过");
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 进入转结算操作页面
	 * @return
	 */
	@RequestMapping("tranferSettlement")
	public String transferSettlement() {
		return "paycenter/paidRecord/paid-transferSettlement";
	}
	
	/**
	 * 转结算
	 * @param type 类型, 0 订单和佣金  1订单    2佣金
	 * @param settleDate
	 * @return
	 */
	@RequestMapping("insertSettlement")
	@ResponseBody
	public String insertSettlement(HttpServletRequest request, String type, String settleDate,
			String account,String mobile,String startAmt,String endAmt,String greaterZero) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String userId = null;
			SysRegisterUser user = getUser(request);
			if(user != null) {
				userId = user.getUserID();
			}
			paidService.insertSettlement(type, settleDate, userId,account,mobile,startAmt,endAmt,greaterZero);
			resultMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", false);
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	/**
	 * 导出Excel
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
			String fileName = "代付款记录" + new SimpleDateFormat("yyyyMMdd").format(new Date());
			fileName  = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			wwb = Workbook.createWorkbook(ouputStream);
			WritableSheet sheet = wwb.createSheet("代付款记录", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
			/**
			 * 第一行合并水平居中
			 */
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);  //把水平对齐方式指定为居中
			sheet.mergeCells(0, 0, 6, 0);
			sheet.addCell(new Label(0, 0, "代付款记录", wcf));
			
			String[] titles = {"用户账号", "用户手机号", "代付订单笔数", "代付退款笔数", "代付违约金笔数", "代付市场佣金笔数", "代付款金额"};
			for(int i = 0, len = titles.length; i < len; i++) {
				// 第一个参数表示列，第二个参数表示行
				Label titleLable = new Label(i, 1, titles[i]);
				sheet.addCell(titleLable);
			}
			ApiResult<List<PaidDTO>> apiResult = paidService.queryPage(paramMap);
			List<PaidDTO> list = apiResult.getResult();
			if(list != null) {
				for(int i = 0, len = list.size(); i < len; i++) {
					PaidDTO dto = list.get(i);
					Label label0 = new Label(0, i+2, dto.getAccount() == null ? "" : dto.getAccount());
					Label label1 = new Label(1, i+2, dto.getMobile() == null ? "" : dto.getMobile());
					Label label2 = new Label(2, i+2, dto.getOrderCount() == null ? "-" : dto.getOrderCount().toString());
					Label label3 = new Label(3, i+2, dto.getRefundCount() == null ? "-" : dto.getRefundCount().toString());
					Label label4 = new Label(4, i+2, dto.getPenaltyCount() == null ? "-" : dto.getPenaltyCount().toString());
					Label label5 = new Label(5, i+2, dto.getCommissionCount() == null ? "-" : dto.getCommissionCount().toString());
					Label label6 = new Label(6, i+2, dto.getAmtStr() == null ? "" : dto.getAmtStr());
					sheet.addCell(label0);
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
				}
			}
			wwb.write();
			wwb.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
