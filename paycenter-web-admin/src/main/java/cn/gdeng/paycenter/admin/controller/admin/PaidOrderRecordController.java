package cn.gdeng.paycenter.admin.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import cn.gdeng.paycenter.admin.dto.admin.PaidOrderRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.PaidOrderRecordService;
import cn.gdeng.paycenter.enums.HasChangeEnum;
import cn.gdeng.paycenter.enums.PayStatusForPageEnum;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 代支付订单记录Controller
 * 
 * @author xiaojun
 *
 */
@Controller
@RequestMapping("paycenter/paidOrderRecord")
public class PaidOrderRecordController extends AdminBaseController {

	private static final GdLogger logger = GdLoggerFactory.getLogger(PaidOrderRecordController.class);

	@Reference
	private PaidOrderRecordService paidOrderRecordService;

	/**
	 * 跳转代付订单记录列表
	 */
	@RequestMapping("showOrderList")
	public String showOrderList(Map<String, Object> map, PaidOrderRecordDTO dto) {
		if ("1".equals(dto.getRedirect())) {
			//map.put("redirectPayStatus", dto.getRedirectPayStatus());
			map.put("memberId", dto.getMemberId());
			map.put("hasChange", dto.getHasChange());
			map.put("batNo", dto.getBatNo());
			return "paycenter/paidRecord/orderByMobileList";
		}
		return "paycenter/paidRecord/orderList";
	}

	/**
	 * 代付订单记录列表
	 */
	@RequestMapping("queryPaidOrderRecordList")
	@ResponseBody
	public String queryPaidOrderRecordList(HttpServletRequest request) {
		try {
			Map<String, Object> map = getParametersMap(request);
			Integer total = paidOrderRecordService.queryPaidOrderRecordListTotal(map);
			// 记录数
			map.put("total", total);
			// 设定分页,排序
			setCommParameters(request, map);
			// list
			List<PaidOrderRecordDTO> list = paidOrderRecordService.queryPaidOrderRecordList(map);
			map.put("rows", list);// rows键 存放每页记录 list
			return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			return getExceptionMessage(e, logger);
		}
	}

	/**
	 * 检测导出参数,检测通过则后续会在页面启动文件下载
	 * 
	 * @param request
	 * @return
	 * @author lidong
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
			int total = paidOrderRecordService.queryPaidOrderRecordListTotal(map);
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
	 * 导出交易记录
	 * 
	 * @param request
	 * @throws Exception
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
			String fileName = new String(("代付订单记录" + time1.format(date)).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<PaidOrderRecordDTO> list = paidOrderRecordService.queryPaidOrderRecordList(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet("代付订单记录", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				/**
				 * 第一行合并水平居中
				 */
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);  //把水平对齐方式指定为居中
				sheet.mergeCells(0, 0, 15, 0);
				sheet.addCell(new Label(0, 0, "代付款记录", wcf));
				
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 1, "订单号");
				Label label10 = new Label(1, 1, "下单时间");
				Label label20 = new Label(2, 1, "商品总金额");
				Label label30 = new Label(3, 1, "谷登代收手续费");
				Label label40 = new Label(4, 1, "市场佣金");
				Label label50 = new Label(5, 1, "平台佣金");
				Label label60 = new Label(6, 1, "刷卡补贴");
				Label label70 = new Label(7, 1, "代付款金额");
				Label label80 = new Label(8, 1, "转结状态");
				Label label90 = new Label(9, 1, "第三方支付流水");
				Label label100 = new Label(10, 1, "代付时间");
				Label label110 = new Label(11, 1, "代付方式");
				Label label120 = new Label(12, 1, "代付状态");
				Label label130 = new Label(13, 1, "收款方手机");
				Label label140 = new Label(14, 1, "订单类型");
				Label label150 = new Label(15, 1, "收款方");
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
				sheet.addCell(label120);
				sheet.addCell(label130);
				sheet.addCell(label140);
				sheet.addCell(label150);
				/*** 循环添加数据到工作簿 ***/
				if (list != null && list.size() > 0) {
					SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						PaidOrderRecordDTO item = list.get(i);
						label00 = new Label(0, i + 2, item.getOrderNo());
						label10 = new Label(1, i + 2, item.getOrderTime()==null?"":time.format(item.getOrderTime()));
						label20 = new Label(2, i + 2, item.getTotalAmt());
						label30 = new Label(3, i + 2, item.getFeeAmt());
						label40 = new Label(4, i + 2, item.getMarketCommission());
						label50 = new Label(5, i + 2, item.getPlatformCommission());
						label60 = new Label(6, i + 2, item.getSubsidyAmt());
						label70 = new Label(7, i + 2, item.getPaidAmt());
						label80 = new Label(8, i + 2, HasChangeEnum.getNameByCode(item.getHasChange()));
						label90 = new Label(9, i + 2, item.getThirdPayNumber());
						label100 = new Label(10, i + 2, item.getPayTime()==null?"":time.format(item.getPayTime()));
						label110 = new Label(11, i + 2, item.getPayType());
						label120 = new Label(12, i + 2, PayStatusForPageEnum.getNameByCode(item.getPayStatus()));
						label130 = new Label(13, i + 2, item.getPayeeMobile());
						label140 = new Label(14, i + 2, item.getOrderTypeStr());
						label150 = new Label(15, i + 2, item.getUserTypeStr());
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
						sheet.addCell(label120);
						sheet.addCell(label130);
						sheet.addCell(label140);
						sheet.addCell(label150);
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
