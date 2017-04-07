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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.RefundRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.AdminPayTradeService;
import cn.gdeng.paycenter.admin.service.admin.RefundRecordService;
import cn.gdeng.paycenter.admin.service.admin.RemitRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.HasChangeEnum;
import cn.gdeng.paycenter.enums.PayStatusForPageEnum;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.enums.RefundTypeEnum;
import cn.gdeng.paycenter.enums.UserTypeEnum;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 代付退款记录控制器
* @author DavidLiang
* @date 2016年12月12日 下午2:34:46
 */
@Controller
@RequestMapping("refundRecord")
public class RefundRecordController extends AdminBaseController {
	
	private static final GdLogger logger = GdLoggerFactory.getLogger(RefundRecordController.class);
	
	@Reference
	private RefundRecordService refundRecordService;
	
	@Reference
	private AdminPayTradeService adminPayTradeService;
	
	@Reference
	private RemitRecordService remitRecordService;
	
	/**
	 * 退款列表首页
	* @author DavidLiang
	* @date 2016年12月12日 下午7:04:36
	* @return
	 */
	@RequestMapping("index")
	public String index() {
		return "paycenter/refundRecord/refundRecordList";
	}
	
	/**
	 * 跳转到退款记录(没有查询条件的)标签页面
	* @author DavidLiang
	* @date 2016年12月12日 下午7:06:00
	* @param map
	* @param dto
	* @return
	 */
	@RequestMapping("refundRecordTab")
	public String refundRecordTab(Map<String, Object> map, RefundRecordDTO dto) {
		map.put("memberId", dto.getMemberId());
		map.put("batNo", dto.getBatNo());
		//从笔数点进去是只查询人工退款，不查原路返回的退款记录
		map.put("refundType", 2);
		//从笔数点进去clearHasChange(clear_detail的hasChange)为0
		map.put("clearHasChange", 0);
		return "paycenter/refundRecord/refundRecordTab";
	}
	
	/**
	 * 分页查询退款记录
	* @author DavidLiang
	* @date 2016年12月12日 下午7:06:53
	* @param request
	* @return
	 */
	@RequestMapping("getRefundRecordByPage")
	@ResponseBody
	public String getPenaltyRecordByPage(HttpServletRequest request) {
		Map<String, Object> map = getParametersMap(request);
		try {
			int count = refundRecordService.findCountRefundRecord(map);
			List<RefundRecordDTO> list = new ArrayList<>();
			// 设定分页,排序
			setCommParameters(request, map);
			map.put("total", count);
			if (count > 0) {
				list = refundRecordService.findRefundRecordByPage(map);
				if (CollectionUtils.isNotEmpty(list)) {
					for (RefundRecordDTO dto : list) {
						if ("1".equals(dto.getRefundType())) {
							dto.setPayType(PayWayEnum.getNameByCode(dto.getPayWay()));
						}
					}
				}
				/**
				 * 代付方式：1系统：pay_trade表中的payType
				 *	 					    2人工：remit_record表的depositBankName + bankCardNo
				 */
				/*for (RefundRecordDTO dto : list) {
					//顺便收款方赋值"农商友"
//					dto.setUserType("农商友");
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("orderNo", dto.getOrderNo());
					if ("1".equals(dto.getRefundType())) {
						PayTradeDTO ptDto = adminPayTradeService.findTradeByOrderNoForRecord(paramMap);
						if (ptDto != null) {
							dto.setPayType(ptDto.getPayType());
						} 
					} else if ("2".equals(dto.getRefundType())) {
						RemitRecordEntity rre = remitRecordService.findRemitRecByOrderNo(paramMap);
						if (rre != null) {
							String payType = rre.getDepositBankName();
							String bankCardNo = rre.getBankCardNo();
							if (bankCardNo != null && bankCardNo.length() >= 4) {
								payType += "(" + bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length()) + ")";
							} else if (bankCardNo != null && bankCardNo.length() < 4) {
								payType += "(" + bankCardNo + ")";
							}
							dto.setPayType(payType);
						}
					}

					//dto.setUserType(UserTypeStrByOrderTypeEnum.getNameByCode(dto.getUserType()));
				}*/
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
	* @date 2016年12月12日 下午8:48:46
	* @param request
	* @return
	 */
	@RequestMapping(value = "checkExportParams", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkExportParams(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (DateUtil.isDateIntervalOverFlow(request.getParameter("startCreateTime"),
					request.getParameter("endCreateTime"), 31)
					&& DateUtil.isDateIntervalOverFlow(request.getParameter("startPayTime"),
							request.getParameter("endPayTime"), 31)) {
				result.put("status", 0);
				result.put("message", "请选择正确的日期范围, 系统最大支持范围为31天..");
				return JSONObject.toJSONString(result);
			}
			Map<String, Object> map = getParametersMap(request);
			int total = refundRecordService.findCountRefundRecord(map);
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
	 * 导出代付退款记录
	* @author DavidLiang
	* @date 2016年12月13日 下午12:11:10
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
			 String fileName = new String(("代付退款记录" + time1.format(date)).getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			List<RefundRecordDTO> list = refundRecordService.findRefundRecordByPage(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				WritableSheet sheet = wwb.createSheet("代付退款记录", 0);
				/**
				 * 第一行合并水平居中
				 */
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);  //把水平对齐方式指定为居中
				sheet.mergeCells(0, 0, 12, 0);
				sheet.addCell(new Label(0, 0, "代付款记录", wcf));
				
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 1, "退款编号");
				Label label10 = new Label(1, 1, "关联订单号");
				Label label20 = new Label(2, 1, "退款类型");
				Label label30 = new Label(3, 1, "申请退款时间");
				Label label40 = new Label(4, 1, "退款金额");
				Label label50 = new Label(5, 1, "代付款金额");
				Label label60 = new Label(6, 1, "转结状态");
				Label label70 = new Label(7, 1, "第三方支付流水");
				Label label80 = new Label(8, 1, "代付时间");
				Label label90 = new Label(9, 1, "代付方式");
				Label label100 = new Label(10, 1, "代付状态");
				Label label110 = new Label(11, 1, "收款方手机");
				Label label120 = new Label(12, 1, "收款方");
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
				if (list != null && ! list.isEmpty() && list.size() > 0) {
					SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0; i<list.size(); i++) {
						RefundRecordDTO dto = list.get(i);
						label00 = new Label(0, i + 2, dto.getRefundNo());
						label10 = new Label(1, i + 2, dto.getOrderNo());
						label20 = new Label(2, i + 2, RefundTypeEnum.getNameByCode(dto.getRefundType()));
						label30 = new Label(3, i + 2, dto.getCreateTime() == null ? "" : sdFormat.format(dto.getCreateTime()));
						label40 = new Label(4, i + 2, dto.getRefundAmt());
						label50 = new Label(5, i + 2, dto.getRefundAmt());
						label60 = new Label(6, i + 2, HasChangeEnum.getNameByCode(dto.getHasChange()));
						label70 = new Label(7, i + 2, dto.getThirdRefundNo());
						label80 = new Label(8, i + 2, dto.getPayTime() == null ? "" : sdFormat.format(dto.getPayTime()));
						if ("1".equals(dto.getRefundType())) {
							label90 = new Label(9, i + 2, PayWayEnum.getNameByCode(dto.getPayWay()));
						} else {
							label90 = new Label(9, i + 2, dto.getPayType());
						}
						label100 = new Label(10, i + 2, PayStatusForPageEnum.getNameByCode(dto.getPayStatus()));
						label110 = new Label(11, i + 2, dto.getPayeeMobile());
						label120 = new Label(12, i + 2, UserTypeEnum.getNameByCode(dto.getUserType()));
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
