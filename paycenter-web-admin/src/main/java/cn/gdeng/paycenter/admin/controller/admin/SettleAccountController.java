package cn.gdeng.paycenter.admin.controller.admin;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountDTO;
import cn.gdeng.paycenter.admin.dto.admin.SettleAccountExportDTO;
import cn.gdeng.paycenter.admin.service.admin.SettleAccountService;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/**
 * 结算管理Controller
 * @author jianhuahuang
 * @Date:2016年11月9日上午9:46:46
 */
@Controller
@RequestMapping("settleAccount")
public class SettleAccountController extends AdminBaseController {
	  /** 记录日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(SettleAccountController.class);
    
    @Reference
    private SettleAccountService settleAccountService;
    
    @RequestMapping("index")
    public String index(Model model){
    	return "settleAccount/settleAccountList";
    }
    
    /**
     * 获取结算记录信息列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("getSettleAccountList")
    @ResponseBody
    public String getSettleAccountList(HttpServletRequest request,SettleAccountDTO feeRecordDTO){
    	try {
	    	Map<String, Object> map = getParametersMap(request);
			if(!StringUtils.isEmpty(feeRecordDTO.getChangeTimeBeginTime())) {
				map.put("changeTimeBeginTime", feeRecordDTO.getChangeTimeBeginTime() + " 00:00:00");
			}
			if(!StringUtils.isEmpty(feeRecordDTO.getChangeTimeEndTime())) {
				map.put("changeTimeEndTime", feeRecordDTO.getChangeTimeEndTime() + " 23:59:59");
			}
			if(!StringUtils.isEmpty(feeRecordDTO.getPayTimeBeginTime())) {
				map.put("payTimeBeginTime", feeRecordDTO.getPayTimeBeginTime() + " 00:00:00");
			}
			if(!StringUtils.isEmpty(feeRecordDTO.getPayTimeEndTime())) {
				map.put("payTimeEndTime", feeRecordDTO.getPayTimeEndTime() + " 23:59:59");
			}
			//设定分页,排序
			setCommParameters(request, map);
			
			ApiResult<AdminPageDTO> apiResult = settleAccountService.queryPage(map);
			if(apiResult != null){
				return JSONObject.toJSONString(apiResult,SerializerFeature.WriteDateUseDateFormat);
			}
		} catch (Exception e) {
			return getExceptionMessage(e, logger);
		}
		return null;
    }
    
    /**
     * 查询结算详情页面
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("settleAccountDetail/{id}")
    public String settleAccountDetail(@PathVariable String id, Model model) {
    	try {
	    	 Map<String, Object> map = getParametersMap(request);
	    	 ApiResult<SettleAccountDTO>  apiResult=settleAccountService.querySettleAccountDetail(map);
	    	 if(!StringUtils.isEmpty(apiResult.getResult().getPayerBankCardNo())){
	    		 String bankCardNo = apiResult.getResult().getPayerBankCardNo().replace(" ", "");
	    		 apiResult.getResult().setPayerBankCardNo(bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length()));
	    	 }
	    	 model.addAttribute("entity", apiResult.getResult());
	    	 model.addAttribute("option", map.get("option"));
    	} catch (Exception e) {
    		return getExceptionMessage(e, logger);
		}
    	 return "settleAccount/settleAccountDetail";
    }
    
	
    /**
	 * 查询结算详情页面 -待付款列表
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("queryOrderDetailPage")
	@ResponseBody
	public String queryOrderDetailPage(HttpServletRequest request, Model model){
		try{
			Map<String, Object> map = getParametersMap(request);
				//设定分页,排序
			setCommParameters(request, map);
				
			ApiResult<AdminPageDTO> apiResult = settleAccountService.queryOrderDetailPage(map);
			if(apiResult != null){
				return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
			}
		}catch (Exception e) {
    		return getExceptionMessage(e, logger);
		}
		return null;
	}
	
	  /**
		 * 标记异常
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping(value="addErrorMark",produces="application/json;charset=utf-8")
		@ResponseBody
		public String addErrorMark(HttpServletRequest request){
			 ApiResult<Integer> apiResult=new ApiResult<>();
			try{ 
				 Map<String, Object> map = getParametersMap(request);
				 map.put("createUserId", getUser(request).getUserID());
				 apiResult= settleAccountService.addSettleAccountError(map);
			}catch (Exception e) {
	    		return getExceptionMessage(e, logger);
			}
			 return JSONObject.toJSONString(apiResult,SerializerFeature.WriteDateUseDateFormat);
		}
		
		 /**
		 * 处理结算记录-更新相关信息
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping(value="updateSettleAccount",produces="application/json;charset=utf-8")
		@ResponseBody
		public String updateSettleAccount(HttpServletRequest request){
			Map<String, Object> resultMap = new HashMap<String, Object>();
			try{  
				Map<String, Object> map = getParametersMap(request);
				map.put("updateUserId", getUser(request).getUserID());
				settleAccountService.updateSettleAccount(map);
				resultMap.put("success", true);
			}catch (Exception e) {
				logger.info("保存结算信息失败：", e);
				resultMap.put("success", false);
			}
			return JSONObject.toJSONString(resultMap);
		}
		
		/**
		 * 导出前检查
		 * @param request
		 * @return
		 */
		@RequestMapping(value="exportCheck",produces="application/json;charset=utf-8")
		@ResponseBody
		public String exportCheck(HttpServletRequest request){
			ApiResult<String> apiResult = new ApiResult<String>();
			//查询参数
			Map<String, Object> paramMap = getParametersMap(request);
			try{
				if(paramMap != null && paramMap.size() > 0){
					 if(paramMap.get("changeTimeEndTime") != null && !paramMap.get("changeTimeEndTime").equals("")){
						 paramMap.put("changeTimeEndTime", paramMap.get("changeTimeEndTime").toString()+" 23:59:59");
					 }
					 if(paramMap.get("payTimeEndTime") != null && !paramMap.get("payTimeEndTime").equals("")){
						 paramMap.put("payTimeEndTime", paramMap.get("payTimeEndTime").toString()+" 23:59:59");
					 }
				}
				ApiResult<Integer> remoteApiResult = settleAccountService.querySettleAccountCount(paramMap);
				if(remoteApiResult == null || remoteApiResult.getResult() == null){
					apiResult.setCodeMsg(MsgCons.C_50000, MsgCons.M_50000);
					apiResult.setResult("服务器出错！");
					return JSONObject.toJSONString(apiResult);
				}
				
				//总记录数验证
				int total = remoteApiResult.getResult();
				
				if(total <= 0){
					return JSONObject.toJSONString(new ApiResult<String>().withError(MsgCons.C_20035, MsgCons.M_20035));
				}
				else if(total > EXPORT_MAX_SIZE){
					return JSONObject.toJSONString(new ApiResult<String>().withError(MsgCons.C_20036, MsgCons.M_20036));
				}
				apiResult.setCode(remoteApiResult.getCode());
				apiResult.setMsg(remoteApiResult.getMsg());
			}catch (Exception e) {
	    		return getExceptionMessage(e, logger);
			}
			return JSONObject.toJSONString(apiResult);
		}
		
		@RequestMapping("export")
		public void export(HttpServletRequest request, HttpServletResponse response){
			//查询参数
			Map<String, Object> paramMap = getParametersMap(request);
			
			OutputStream ouputStream = null;
			try{
				String fileName = "结算记录"+DateUtil.toString(new Date(), "yyyyMMdd");
				 // 设置输出响应头信息，
	            response.setContentType("application/vnd.ms-excel");
	            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1") + ".xls");
	            ouputStream = response.getOutputStream();
	            
	            WritableWorkbook wwb = Workbook.createWorkbook(ouputStream);
	            if(wwb == null){
	            	return;
	            }
	            
	            // 创建一个工作页，第一个参数的页名，第二个参数表示该工作页在excel中处于哪一页
	            WritableSheet sheet = wwb.createSheet("结算记录", 0);
	            
	            /*String[] headers = {"用户账号","收款方手机","转结算时间","代付订单笔数", "代付佣金笔数", "代付退款笔数", "代付违约金笔数","代付金额", "代付时间", "第三方支付流水", "代付状态",
	            		"持卡人姓名","开户银行名称","开户行所在地","支行名称","银行卡号","银行卡预留手机号","异常标记",
	            		"代付单位","代付银行名称","代付银行卡号","代付手续费","平台支付流水","备注"};*/
	            
	            String[] headers = {"用户账号","收款方手机","转结算时间","代付订单笔数", "代付市场佣金笔数", "代付退款笔数", "代付违约金笔数","代付款金额", "代付手续费", "代付时间", "第三方支付流水", "代付状态",
	            		"持卡人姓名","开户银行名称","开户行所在地","支行名称","银行卡号","银行卡预留手机号","异常标记","代付单位","代付银行名称","代付银行卡号"};
	            
	            sheet.mergeCells(0, 0, 22, 0);
//				WritableFont font = new WritableFont(WritableFont.TIMES,16,WritableFont.BOLD);
				WritableCellFormat format = new WritableCellFormat();
				format.setAlignment(jxl.format.Alignment.CENTRE);
				Label title = new Label(0, 0, "结算记录", format);
				sheet.addCell(title);
				
	            for(int i = 0, len = headers.length; i < len; i++){
	            	 sheet.addCell(new Label(i, 1, headers[i]));
	            }
	            
	            if(paramMap != null && paramMap.size() > 0){
	            	if(paramMap.get("changeTimeEndTime") != null && !paramMap.get("changeTimeEndTime").equals("")){
						 paramMap.put("changeTimeEndTime", paramMap.get("changeTimeEndTime").toString()+" 23:59:59");
					 }
					 if(paramMap.get("payTimeEndTime") != null && !paramMap.get("payTimeEndTime").equals("")){
						 paramMap.put("payTimeEndTime", paramMap.get("payTimeEndTime").toString()+" 23:59:59");
					 }
				} 
	            
				// 查询导出数据总数
				ApiResult<Integer> countApiResult = settleAccountService.querySettleAccountCount(paramMap);
	            int totalCount = 0;
	            if(countApiResult != null){
	            	totalCount = countApiResult.getResult();
	            }
	            // 计算分几次查询导出数据
	            int exportCount = totalCount / EXPORT_PAGE_SIZE;
	            if((totalCount % EXPORT_PAGE_SIZE) != 0){
	            	exportCount++;
	            }
	            int startRow = 0;
	    		for(int i = 0; i < exportCount; i++){
	            	// 查询分页数据
	            	paramMap.put("startRow", startRow);
	            	paramMap.put("endRow", EXPORT_PAGE_SIZE);
	            	List<SettleAccountExportDTO> list= settleAccountService.querySettleAccountList(paramMap);
		   
		            if(CollectionUtils.isEmpty(list)){
		            	break;
		            }
	            	for(int j = 0, len = list.size(); j < len; j++){
	            		SettleAccountExportDTO dto = list.get(j);
	                	sheet.addCell(new Label(0, j+2+startRow, dto.getAccount()));//结算信息
	                	sheet.addCell(new Label(1, j+2+startRow, dto.getMobile()));
	                	sheet.addCell(new Label(2, j+2+startRow, dto.getChangeTimeStr()));
	                	sheet.addCell(new Label(3, j+2+startRow, dto.getOrderNum()==null?"":dto.getOrderNum().toString()));
	                    sheet.addCell(new Label(4, j+2+startRow, dto.getCommissionNum()==null?"":dto.getCommissionNum().toString()));
	                    sheet.addCell(new Label(5, j+2+startRow, dto.getRefundNum()==null?"":dto.getRefundNum().toString()));
	                    sheet.addCell(new Label(6, j+2+startRow, dto.getPenaltyNum()==null?"":dto.getPenaltyNum().toString()));
	                    sheet.addCell(new Label(7, j+2+startRow, dto.getDueAmtFormat()==null?"":dto.getDueAmtFormat()));
	                    sheet.addCell(new Label(8, j+2+startRow, dto.getFeeAmtFormat()==null?"":dto.getFeeAmtFormat()));
	                    sheet.addCell(new Label(9, j+2+startRow, dto.getPayTimeStr()));
	                    sheet.addCell(new Label(10, j+2+startRow, dto.getThirdPayNumber()));
	                    sheet.addCell(new Label(11, j+2+startRow, dto.getPayStatusStr()));
	                    sheet.addCell(new Label(12, j+2+startRow, dto.getRealName()));
	                    sheet.addCell(new Label(13, j+2+startRow, dto.getUserDepositBankName()));//用户信息
	                    sheet.addCell(new Label(14, j+2+startRow, dto.getAddress()));
	                    sheet.addCell(new Label(15, j+2+startRow, dto.getUserSubBankName()));
	                    sheet.addCell(new Label(16, j+2+startRow, dto.getUserBankCardNo()));
	                    sheet.addCell(new Label(17, j+2+startRow, dto.getUserMobile()));
	                    sheet.addCell(new Label(18, j+2+startRow, dto.getExceptionMackCount()==null?"":dto.getExceptionMackCount().toString()));
	                    sheet.addCell(new Label(19, j+2+startRow, dto.getPayerName()));//代付信息
	                    sheet.addCell(new Label(20, j+2+startRow, dto.getPayerBankName()));
	                    sheet.addCell(new Label(21, j+2+startRow, dto.getBankCardNo()));
	            	}
	            	startRow += EXPORT_PAGE_SIZE;
	    		}
	            wwb.write();
	            wwb.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally {
	            try {
	                ouputStream.flush();
	                ouputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
		
		@RequestMapping(value = "importUpdate", method = RequestMethod.POST, produces="application/json;charset=utf-8")
		@ResponseBody
		public String importUpdate(HttpServletRequest request, HttpServletResponse response,MultipartHttpServletRequest multipartHttpServletRequest){
				ApiResult<Integer> apiResult = new ApiResult<Integer>();
				List<SettleAccountDTO> list=new ArrayList<>();
				InputStream in=null;
				try {
				 	MultipartFile  fileFile = multipartHttpServletRequest.getFile("fileImport"); 
				 	//转换成输入流  
			         in = fileFile.getInputStream();  
			        //得到Excel  
			        Workbook readWb = Workbook.getWorkbook(in);  
			        //得到sheet  
			        Sheet readSheet = readWb.getSheet(0);  
			        //得到多少列  
			        int rsColumns = readSheet.getColumns(); 
			        //得到多少行  
			        int rsRows = readSheet.getRows();
			        //单元格  
			        Cell cell ;  
			        String[] excelInfo=null; 
			        for(int i=1;i<rsRows;i++) {  
			        	excelInfo = new String[rsColumns];  
			            for (int j = 0; j < rsColumns; j++) {  
			                 cell = readSheet.getCell(j,i);  
			                 if(cell.getType()==CellType.DATE){//时间的处理  
			                     DateCell dc = (DateCell)cell;  
			                     Date date = dc.getDate();  
			                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			                     String sDate = sdf.format(date);  
			                     excelInfo[j] = sDate;  
			                 }else{  
			                	 excelInfo[j] = cell.getContents();  
			                 }  
			            }
			            SettleAccountDTO settleAccountDTO=arrayToObject(excelInfo);
			            settleAccountDTO.setUpdateUserId(getUser(request).getUserID());
			            list.add(settleAccountDTO);
			        }
			} catch (Exception e) {
				logger.info("解析excel文件异常:{}",e);
				return JSONObject.toJSONString(new ApiResult<String>().withError(MsgCons.C_20037, MsgCons.M_20037));
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					logger.info("解析excel关闭文件流异常:{}",e);
				}
			}
			  apiResult= settleAccountService.importUpdateSettleAccount(list);
			  return JSONObject.toJSONString(apiResult);
		}
		
		 /** 
	     * 数组转对象 
	     * @param info 
	     * @return 
	     */  
	    private static SettleAccountDTO arrayToObject(String[] excelInfo){
	    	SettleAccountDTO dto=new SettleAccountDTO();
	    	//用于验证
	    	/**账号		*/
	    	dto.setAccount(excelInfo[0]);
	    	/**手机		*/
	    	dto.setMobile(excelInfo[1]);
	    	/**转结算时间		*/
	    	dto.setChangeTimeBeginTime(excelInfo[2]);
	    	/**持卡人姓名		*/
	    	dto.setRealName(excelInfo[9]);
	    	
	    	//需要更新信息
	    	/**代付单位*/
	    	dto.setPayerName(excelInfo[16]);
	    	/**代付银行名称*/
	    	dto.setPayerBankName(excelInfo[17]);
	    	/**代付银行卡号*/
	    	dto.setBankCardNo(excelInfo[18]);
	    	/**代付时间*/
	    	dto.setPayTimeBeginTime(excelInfo[6]);
	    	/**代付手续费*/
	    	if(org.apache.commons.lang3.StringUtils.isNotBlank(excelInfo[19])){
	    		dto.setFeeAmt(Double.valueOf(excelInfo[19]));
	    	}
	    	/**第三方支付流水*/
	    	dto.setBankTradeNo(excelInfo[7]);
	    	/**平台流水号*/
	    	dto.setPayCenterNumber(excelInfo[20]);
	    	/**备注*/
	    	dto.setComment(excelInfo[21]);
	    	return dto;
	    }
}	
