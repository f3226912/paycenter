package cn.gdeng.paycenter.admin.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import cn.gdeng.paycenter.admin.dto.admin.PaidCommissionRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.PaidCommissionRecordService;
import cn.gdeng.paycenter.api.server.customer.CustomerHessianService;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
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
 * 代付市场佣金记录Controller
 * 
 * @author xiaojun
 *
 */
@Controller
@RequestMapping("paycenter/paidCommissionRecord")
public class PaidCommissionRecordController extends AdminBaseController {
	private static final GdLogger logger = GdLoggerFactory.getLogger(PaidCommissionRecordController.class);

	@Reference
	private PaidCommissionRecordService paidCommissionRecordService;

	@Resource
	CustomerHessianService customerHessianService;

	/**
	 * 跳转代付市场佣金记录列表
	 */
	@RequestMapping("showCommissionList")
	public String showCommissionList(Map<String, Object> map, PaidCommissionRecordDTO dto) {
		List<MarketHessianDTO> marketList = customerHessianService.getAllByType("2");
		if ("1".equals(dto.getRedirect())) {
			map.put("redirectPayStatus", dto.getRedirectPayStatus());
			map.put("memberId", dto.getMemberId());
			map.put("hasChange", dto.getHasChange());
			map.put("batNo", dto.getBatNo());
			return "paycenter/paidRecord/commissionByMobileList";
		}
		map.put("marketList", marketList);
		return "paycenter/paidRecord/commissionList";
	}

	/**
	 * 代付市场佣金记录列表
	 */
	@RequestMapping("queryPaidCommissionRecordList")
	@ResponseBody
	public String queryPaidCommissionRecordList(HttpServletRequest request) {
		try {
			Map<String, Object> map = getParametersMap(request);
			Integer total = paidCommissionRecordService.queryPaidCommissionRecordListTotal(map);
			// 记录数
			map.put("total", total);
			// 设定分页,排序
			setCommParameters(request, map);
			// list
			List<PaidCommissionRecordDTO> list = paidCommissionRecordService.queryPaidCommissionRecordList(map);
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
			int total = paidCommissionRecordService.queryPaidCommissionRecordListTotal(map);
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
			String fileName = new String(("代付佣金记录" + time1.format(date)).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<PaidCommissionRecordDTO> list = paidCommissionRecordService.queryPaidCommissionRecordList(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet("代付佣金记录", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				/**
				 * 第一行合并水平居中
				 */
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);  //把水平对齐方式指定为居中
				sheet.mergeCells(0, 0, 12, 0);
				sheet.addCell(new Label(0, 0, "代付款记录", wcf));
				
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 1, "关联订单号");
				Label label10 = new Label(1, 1, "下单时间");
				Label label20 = new Label(2, 1, "商品总金额");
				Label label30 = new Label(3, 1, "支付佣金");
				Label label40 = new Label(4, 1, "代付款金额");
				Label label50 = new Label(5, 1, "转结状态");
				Label label60 = new Label(6, 1, "第三方支付流水");
				Label label70 = new Label(7, 1, "代付时间");
				Label label80 = new Label(8, 1, "代付方式");
				Label label90 = new Label(9, 1, "代付状态");
				Label label100 = new Label(10, 1, "收款方手机");
				Label label110 = new Label(11, 1, "市场名称");
				Label label120 = new Label(12, 1, "佣金支出方");
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
				/*** 循环添加数据到工作簿 ***/
				if (list != null && list.size() > 0) {
					SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						PaidCommissionRecordDTO item = list.get(i);
						label00 = new Label(0, i + 2, item.getOrderNo());
						label10 = new Label(1, i + 2, item.getOrderTime() == null ? "" : time.format(item.getOrderTime()));
						label20 = new Label(2, i + 2, item.getTotalAmt());
						label30 = new Label(3, i + 2, item.getCommissionAmt());
						label40 = new Label(4, i + 2, item.getCommissionAmt());
						label50 = new Label(5, i + 2, HasChangeEnum.getNameByCode(item.getHasChange()));
						label60 = new Label(6, i + 2, item.getThirdPayNumber());
						label70 = new Label(7, i + 2, item.getPayTime() == null ? "" : time.format(item.getPayTime()));
						label80 = new Label(8, i + 2, item.getPayType());
						label90 = new Label(9, i + 2, PayStatusForPageEnum.getNameByCode(item.getPayStatus()));
						label100 = new Label(10, i + 2, item.getMobile());
						label110 = new Label(11, i + 2, item.getMarketName());
						label120 = new Label(12, i + 2, UserTypeEnum.getNameByCode(item.getUserType()));
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
