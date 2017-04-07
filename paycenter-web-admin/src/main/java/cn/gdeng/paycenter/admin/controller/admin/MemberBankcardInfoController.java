package cn.gdeng.paycenter.admin.controller.admin;


import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.service.admin.MemberBankcardInfoService;
import cn.gdeng.paycenter.dto.pay.MemberBankcardInfoDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * 用户银行卡处理
 * @author ailen
 */
@Controller
@RequestMapping("bankinfo")
public class MemberBankcardInfoController extends AdminBaseController {
	  /** 记录日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(MemberBankcardInfoController.class);
    
    @Reference
    private MemberBankcardInfoService memberBankcardInfoService;
    
    @RequestMapping("index")
    public String index(Model model){
    	return "bank/bankinfoList";
    }
    
    /**
     * 获取银行卡信息列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("getBankinfoList")
    @ResponseBody
    public String getBankinfoList(HttpServletRequest request,MemberBankcardInfoDTO memberBankcardInfoDTO){
		Map<String, Object> map = new HashMap<>();
		map.put("account", memberBankcardInfoDTO.getAccount());
		map.put("telephone", memberBankcardInfoDTO.getTelephone());
		map.put("realName", memberBankcardInfoDTO.getRealName());
		
		map.put("auditDoStatus", memberBankcardInfoDTO.getAuditDoStatus());
		//设定分页,排序
		setCommParameters(request, map);
		
		ApiResult<AdminPageDTO> apiResult = memberBankcardInfoService.queryPage(map);
		if(apiResult != null){
			return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
    }
    
    /**
     * 保存公告
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("detail/{id}")
    public String save(@PathVariable("id") Integer id, HttpServletRequest request, Model model) {
    	MemberBankcardInfoDTO result = memberBankcardInfoService.queryById(id);
    	
    	putModel("info", result);
    	
    	return "bank/bankinfoDetail";
    }
    
	/**
     * 修改银行卡验证
     * 
     * @param request
     * @param response
     * @return
     */
    
	@RequestMapping("pass/{ids}/{status}")
	@ResponseBody
    public String editById(@PathVariable("ids") String ids,@PathVariable("status") String status, Model model) {
		String[] idStrs = ids.split(",");
		
		List<MemberBankcardInfoDTO> memberBankcardInfoDTOs = new ArrayList<>();
		
		for(int i = 0 ; i < idStrs.length ; i++) {
			MemberBankcardInfoDTO bankcardInfoDTO = new MemberBankcardInfoDTO();
			bankcardInfoDTO.setId(Integer.parseInt(idStrs[i]));
			bankcardInfoDTO.setAuditStatus(status);
			memberBankcardInfoDTOs.add(bankcardInfoDTO);
		}

		memberBankcardInfoService.updateBatch(memberBankcardInfoDTOs);

		return JSONObject.toJSONString("{code:'success'}",SerializerFeature.WriteDateUseDateFormat);
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
	public String checkExportParams(MemberBankcardInfoDTO memberBankcardInfoDTO, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", memberBankcardInfoDTO.getAccount());
			map.put("telephone", memberBankcardInfoDTO.getTelephone());
			map.put("realName", memberBankcardInfoDTO.getRealName());
			
			map.put("auditDoStatus", memberBankcardInfoDTO.getAuditDoStatus());
			
			//int total = memberBankcardInfoService.queryCount(map);
			/*if (total > 10000) {
				result.put("status", 0);
				result.put("message", "查询结果集太大(>10000条), 请缩减日期范围, 或者修改其他查询条件...");
				return JSONObject.toJSONString(result);
			}*/
			result.put("status", 1);
			result.put("message", "参数检测通过");
		} catch (Exception e) {
			logger.info("product checkExportParams with ex : {} ", new Object[] { e });
		}
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 导出Excel文件
	 * @return
	 * @author lidong
	 */
	@RequestMapping(value = "exportData")
	public String exportData(MemberBankcardInfoDTO memberBankcardInfoDTO, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", memberBankcardInfoDTO.getAccount());
		map.put("telephone", memberBankcardInfoDTO.getTelephone());
		map.put("realName", memberBankcardInfoDTO.getRealName());
		
		map.put("auditDoStatus", memberBankcardInfoDTO.getAuditDoStatus());
		WritableWorkbook wwb = null;
		OutputStream ouputStream = null;
		try {
			// 设置输出响应头信息，
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName  = new String("用户银行卡信息".getBytes(), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			ouputStream = response.getOutputStream();
			// 查询数据
			List<MemberBankcardInfoDTO> list = memberBankcardInfoService.queryByCondition(map);
			wwb = Workbook.createWorkbook(ouputStream);
			if (wwb != null) {
				WritableSheet sheet = wwb.createSheet("银行卡管理", 0);// 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
				// 第一个参数表示列，第二个参数表示行
				Label label00 = new Label(0, 0, "提交时间");// 填充第一行第一个单元格的内容
				Label label10 = new Label(1, 0, "验证时间");// 填充第一行第二个单元格的内容
				Label label20 = new Label(2, 0, "用户账号");// 填充第一行第三个单元格的内容
				Label label30 = new Label(3, 0, "用户手机号");// 填充第一行第四个单元格的内容
				Label label40 = new Label(4, 0, "持卡人");// 填充第一行第五个单元格的内容
				Label label50 = new Label(5, 0, "开户银行名称");// 填充第一行第七个单元格的内容
				Label label60 = new Label(6, 0, "开户行所在地区");// 填充第一行第六个单元格的内容
				Label label70 = new Label(7, 0, "支行名称");// 填充第一行第七个单元格的内容
				Label label80 = new Label(8, 0, "银行卡号");// 填充第一行第七个单元格的内容
				Label label90 = new Label(9, 0, "银行预留手机");// 填充第一行第七个单元格的内容
				Label label100 = new Label(10, 0, "身份证号");// 填充第一行第七个单元格的内容
				Label label110 = new Label(11, 0, "验证状态");// 填充第一行第七个单元格的内容
				sheet.addCell(label00);// 将单元格加入表格
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
				if (list != null && list.size() > 0) {
					SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0, lenght = list.size(); i < lenght; i++) {
						MemberBankcardInfoDTO item = list.get(i);
						Label label0 = new Label(0, i + 1,
								item.getCommitTime() != null ? time.format(item.getCommitTime()) : "");
						Label label1 = new Label(1, i + 1,
								item.getAuditTime() != null ? time.format(item.getAuditTime()) : "");
						Label label2 = new Label(2, i + 1, item.getAccount());
						Label label3 = new Label(3, i + 1, item.getTelephone());
						Label label4 = new Label(4, i + 1, item.getRealName());
						Label label5 = new Label(5, i + 1, item.getDepositBankName());
						
						Label label6 = new Label(6, i + 1, (item.getProvinceName()==null?"":item.getProvinceName())
								+(item.getCityName()==null?"":item.getCityName())
										+(item.getAreaName()==null?"":item.getAreaName()));
						
						Label label7 = new Label(7, i + 1, item.getSubBankName());
						Label label8 = new Label(8, i + 1, item.getBankCardNo());
						Label label9 = new Label(9, i + 1, item.getMobile());
						Label label12 = new Label(10, i + 1, item.getIdCard());
						Label label11 = new Label(11, i + 1, item.getAuditStatus()==null?"未验证":"1".equals(item.getAuditStatus())?"验证通过":"2".equals(item.getAuditStatus())?"未验证":"验证未通过");
						sheet.addCell(label0);
						sheet.addCell(label1);
						sheet.addCell(label2);
						sheet.addCell(label3);
						sheet.addCell(label4);
						sheet.addCell(label5);
						sheet.addCell(label6);
						sheet.addCell(label7);
						sheet.addCell(label8);
						sheet.addCell(label9);
						sheet.addCell(label12);
						sheet.addCell(label11);

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
