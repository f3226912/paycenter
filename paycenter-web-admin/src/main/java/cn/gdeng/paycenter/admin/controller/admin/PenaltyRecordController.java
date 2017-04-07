package cn.gdeng.paycenter.admin.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.PenaltyRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.PenaltyRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.HasChangeEnum;
import cn.gdeng.paycenter.enums.PayStatusForPageEnum;
import cn.gdeng.paycenter.enums.UserTypeEnum;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 代付违约金记录控制器
* @author DavidLiang
* @date 2016年12月10日 下午3:55:42
 */
@Controller
@RequestMapping("penaltyRecord")
public class PenaltyRecordController extends AdminBaseController {
	
	private static final GdLogger logger = GdLoggerFactory.getLogger(PenaltyRecordController.class);
	
	@Reference
	private PenaltyRecordService penaltyRecordService;
	
	/**
	 * 违约金记录首页
	* @author DavidLiang
	* @date 2016年12月12日 下午7:00:30
	* @return
	 */
	@RequestMapping("index")
	public String index() {
		return "paycenter/penaltyRecord/penaltyRecordList";
	}
	
	/**
	 * 跳转到违约金记录(没有查询条件的)标签页面
	* @author DavidLiang
	* @date 2016年12月12日 下午7:03:33
	* @param map
	* @param dto
	* @return
	 */
	@RequestMapping("penaltyRecordTab")
	public String penaltyRecordTab(Map<String, Object> map, PenaltyRecordDTO dto) {
		map.put("memberId", dto.getMemberId());
		map.put("batNo", dto.getBatNo());
		map.put("hasChange", dto.getHasChange());
		return "paycenter/penaltyRecord/penaltyRecordTab";
	}
	
	/**
	 *  分页查询违约金记录
	* @author DavidLiang
	* @date 2016年12月10日 下午3:57:37
	* @param request
	* @return
	 */
	@RequestMapping("getPenaltyRecordByPage")
	@ResponseBody
	public String getPenaltyRecordByPage(HttpServletRequest request) {
		Map<String, Object> map = getParametersMap(request);
		try {
			int count = penaltyRecordService.findCountPenaltyRecord(map);
			map.put("total", count);
			List<PenaltyRecordDTO> list = new ArrayList<>();
			if (count > 0) {
				// 设定分页,排序
				setCommParameters(request, map);
				list = penaltyRecordService.findPenaltyRecordByPage(map);
//				for (PenaltyRecordDTO dto : list) {
//					dto.setUserType(UserTypeStrByOrderTypeEnum.getNameByCode(dto.getUserType()));
//				}
			}
			map.put("rows", list);
			return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
		} catch (BizException e) {
			return getExceptionMessage(e, logger);
		}
	}
	
	/**
	 * 检测导出参数,检测通过则后续会在页面启动文件下载
	* @author DavidLiang
	* @date 2016年12月12日 下午8:46:35
	* @param request
	* @return
	 */
	@RequestMapping(value = "checkExportParams", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkExportParams(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (DateUtil.isDateIntervalOverFlow(request.getParameter("startOrderTime"),
					request.getParameter("endOrderTime"), 31)
					&& DateUtil.isDateIntervalOverFlow(request.getParameter("startPayTime"),
							request.getParameter("endPayTime"), 31)) {
				result.put("status", 0);
				result.put("message", "请选择正确的日期范围, 系统最大支持范围为31天..");
				return JSONObject.toJSONString(result);
			}
			Map<String, Object> map = getParametersMap(request);
			int total = penaltyRecordService.findCountPenaltyRecord(map);
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
	
	/**
	 * 导出代付违约金记录
	* @author DavidLiang
	* @date 2016年12月13日 上午10:59:41
	* @param request
	* @return
	 */
	@RequestMapping("exportData")
	@ResponseBody
	public String exportData(HttpServletRequest request) {
		Map<String, Object> map = getParametersMap(request);
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息，
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			SimpleDateFormat time1 = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			 String fileName = new String(("代付违约金记录" + time1.format(date)).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			List<PenaltyRecordDTO> list = penaltyRecordService.findPenaltyRecordByPage(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				WritableSheet sheet = wwb.createSheet("代付违约金记录", 0);
				/**
				 * 第一行合并水平居中
				 */
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);  //把水平对齐方式指定为居中
				sheet.mergeCells(0, 0, 11, 0);
				sheet.addCell(new Label(0, 0, "代付款记录", wcf));
				
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 1, "关联订单号");
				Label label10 = new Label(1, 1, "下单时间");
				Label label20 = new Label(2, 1, "预付款");
				Label label30 = new Label(3, 1, "违约金额");
				Label label40 = new Label(4, 1, "代付款金额");
				Label label50 = new Label(5, 1, "转结状态");
				Label label60 = new Label(6, 1, "第三方流水");
				Label label70 = new Label(7, 1, "代付时间");
				Label label80 = new Label(8, 1, "代付方式");
				Label label90 = new Label(9, 1, "代付状态");
				Label label100 = new Label(10, 1, "收款方手机");
				Label label110 = new Label(11, 1, "收款方");
				sheet.addCell(label00);
				sheet.addCell(label10);
				sheet.addCell(label20);
				sheet.addCell(label30);
				sheet.addCell(label40);
				sheet.addCell(label50);
				sheet.addCell(label60);
				sheet.addCell(label70);
				sheet.addCell(label80);
				sheet.addCell(label90);
				sheet.addCell(label100);
				sheet.addCell(label110);
				/*** 循环添加数据到工作簿 ***/
				if (list != null && ! list.isEmpty() && list.size() > 0) {
					SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0; i<list.size(); i++) {
						PenaltyRecordDTO dto = list.get(i);
						label00 = new Label(0, i + 2, dto.getOrderNo());
						label10 = new Label(1, i + 2, dto.getOrderTime() == null ? "" : sdFormat.format(dto.getOrderTime()));
						label20 = new Label(2, i + 2, dto.getPayAmt());
						label30 = new Label(3, i + 2, dto.getTradeAmt());
						label40 = new Label(4, i + 2, dto.getTradeAmt());
						label50 = new Label(5, i + 2, HasChangeEnum.getNameByCode(dto.getHasChange()));
						label60 = new Label(6, i + 2, dto.getThirdPayNumber());
						label70 = new Label(7, i + 2, dto.getPayTime() == null ? "" : sdFormat.format(dto.getPayTime()));
						label80 = new Label(8, i + 2, dto.getPayBankAndNo());
						label90 = new Label(9, i + 2, PayStatusForPageEnum.getNameByCode(dto.getPayStatus()));
						label100 = new Label(10, i + 2, dto.getPayeeMobile());
						label110 = new Label(11, i + 2, UserTypeEnum.getNameByCode(dto.getUserType()));
						sheet.addCell(label00);
						sheet.addCell(label10);
						sheet.addCell(label20);
						sheet.addCell(label30);
						sheet.addCell(label40);
						sheet.addCell(label50);
						sheet.addCell(label60);
						sheet.addCell(label70);
						sheet.addCell(label80);
						sheet.addCell(label90);
						sheet.addCell(label100);
						sheet.addCell(label110);
					}
				}
				wwb.write();  // 将数据写入工作簿
			}
			wwb.close();  // 关闭
		} catch (Exception e) {
			e.printStackTrace();
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
