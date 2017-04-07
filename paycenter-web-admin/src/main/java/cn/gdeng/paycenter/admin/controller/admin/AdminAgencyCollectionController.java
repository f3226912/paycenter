package cn.gdeng.paycenter.admin.controller.admin;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.api.server.customer.CustomerHessianService;
import cn.gdeng.paycenter.api.server.pay.AgencyCollectionService;
import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
import cn.gdeng.paycenter.enums.CheckStatusEnum;
import cn.gdeng.paycenter.enums.PayStatusEnum;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.enums.UserTypeEnum;
import cn.gdeng.paycenter.util.admin.web.DateUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 代收款
 * @author yangjj
 *
 */
@Controller
@RequestMapping("paycenter/agencyCollection")
public class AdminAgencyCollectionController extends AdminBaseController{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	public GdProperties gdProperties;
	@Reference
	AgencyCollectionService agencyCollectionService;
	@Resource
	CustomerHessianService customerHessianService;
	@Reference
	private PayTypeService payTypeService;
	
	 private static Map<String,Object> payTypeMap = null;
	    
	 private static Map<String,Object> payStatusMap = null;
	 
	 private static Map<String,Object> checkStatusMap = null;
	 
	 private static Map<String,Object> appKeyMap = null;
	    
	 static{
	    	payTypeMap = new LinkedHashMap<String,Object>();
	    	payTypeMap.put("ALIPAY_H5", "支付宝");
	    	payTypeMap.put("WEIXIN_APP", "微信");
	    	payTypeMap.put("PINAN", "平安银行");
	    	payTypeMap.put("ENONG", "E农");
	    	payTypeMap.put("NNCCB", "南宁建行");
	    	payTypeMap.put("GXRCB", "广西农信");
	    	
	    	payStatusMap = new LinkedHashMap<String,Object>();
	    	payStatusMap.put("2","付款成功");
	    	payStatusMap.put("3","付款失败");
	    	
	    	checkStatusMap = new LinkedHashMap<String,Object>();
	    	checkStatusMap.put("1", "对账成功");
	    	checkStatusMap.put("2", "对账失败");
	    	
	    	appKeyMap = new LinkedHashMap<String,Object>();
	    	appKeyMap.put("nst","农速通");
	    	appKeyMap.put("nst_car","农速通-车主");
	    	appKeyMap.put("nst_goods","农速通-货主");
	    	appKeyMap.put("nps","农批商");
	    	appKeyMap.put("nsy_pay","农商友");
	    	appKeyMap.put("gys","供应商");
	    	appKeyMap.put("nst_fare","农速通-运费");
	 }
	
	@RequestMapping("/list")
	public String list(ModelMap modelMap,HttpServletRequest request) throws Exception{
		String payType = request.getParameter("payType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String checkStatus = request.getParameter("checkStatus");
		List<MarketHessianDTO> marketList = customerHessianService.getAllByType("2");
		modelMap.addAttribute("payType", payType);
		modelMap.addAttribute("startDate", startDate);
		modelMap.addAttribute("endDate", endDate);
		modelMap.addAttribute("checkStatus", checkStatus);
		modelMap.addAttribute("marketList", marketList);
		modelMap.addAttribute("payTypeList", payTypeService.queryByCondition(null));
		return "agencyCollection/list";
	}
	
	/**
	 * 校验签名
	 */
	@ResponseBody
	@RequestMapping("/validateSign/{type}")
	public Map<String,Object> validateSign(@PathVariable Integer type,HttpServletRequest request){
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
		Map<String, Object> paramMap = getParametersMap(request);
		List<Map<String,Object>> list = null;
		List<Long> ids = new LinkedList<Long>();
		//type 1 列表 2导出 3详情
		if(type==1){		
			paramMap.put("total", agencyCollectionService.countTotal(paramMap));
            setCommParameters(request, paramMap);
            // list
            list = agencyCollectionService.queryList(paramMap);            
		}else if(type==2){
			String memberId = gdProperties.getProperties().getProperty("gd.memberId");
	        paramMap.put("startRow", 0);
	        paramMap.put("endRow", 50000);
	        paramMap.put("memberId", memberId);
	        list = agencyCollectionService.queryList(paramMap);
		}else if(type==3){
			String id =  paramMap.get("id").toString();
			ids.add(Long.valueOf(id));
		}		 
		try {
			if(list != null && list.size() > 0){
	           	 for(Map<String,Object> map : list){
	           		 if(map.containsKey("id") && map.get("id") != null){
	           			 Long id  = ((Integer)map.get("id")).longValue();
	           			 ids.add(id);
	           		 }              	 
	              }
            }
			if(ids != null && ids.size() > 0){
				boolean validRes = agencyCollectionService.validateSign(ids);
				if(validRes){
					resultMap.put("code", "1000");
				}else{
					resultMap.put("code", "2000");
					resultMap.put("msg", "验证签名失败");
				}
			}else{
				resultMap.put("code", "1000");
			}
		} catch (Exception e) {
			resultMap.put("code", "2000");
			resultMap.put("msg", e.getMessage());
			logger.error("校验签名异常>>>>>>>>>>>>>",e.getMessage());
		}
		return resultMap;
	}
	
	/**
	 * 代收款记录列表
	 */
	@ResponseBody
	@RequestMapping("/queryPage")
	public String queryPage(HttpServletRequest request){	
            Map<String, Object> map = getParametersMap(request);
            map.put("total", agencyCollectionService.countTotal(map));
            setCommParameters(request, map);
            List<Map<String, Object>> list = findByCondition(map);
            map.put("rows", list);// rows键 存放每页记录 list
            return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);	        
	}
	
	/**
	 * 根据条件查询(供分页查询和导出方法使用)
	* @author DavidLiang
	* @date 2016年12月28日 下午3:10:40
	* @param paramMap
	* @return
	 */
	private List<Map<String, Object>> findByCondition(Map<String, Object> paramMap) {
		List<Map<String, Object>> list = new ArrayList<>();
		/**
         * 161227  bug3603
         * 由于一条流水对应多个订单，根据流水号或其它条件查询可以查询到多条订单连接情况；
         * 但只要有根据订单号为查询条件的情况下查询不到多条订单连接情况，这个流水对应多个订单也只会出现一个订单。
         * 所以：在有订单号查询条件的情况下，先返回列表，从中取出流水号逐个查询。
         */
        if (paramMap.get("orderNo") != null && paramMap.get("orderNo") != "") {
        	List<Map<String, Object>> tempList = agencyCollectionService.queryList(paramMap);
        	paramMap.remove("orderNo");
        	for (Map<String, Object> temp : tempList) {
        		paramMap.put("thirdPayNumber", temp.get("thirdPayNumber"));
        		list.addAll(agencyCollectionService.queryList(paramMap));
        	}
        } else {
        	list = agencyCollectionService.queryList(paramMap);
        }
        return list;
	}
	
	/**
	 * 代收款详情
	 * 161224参数由pay_trade的id修改为payCenterNumber
	 */
	@RequestMapping("/detail/{payCenterNumber}")
	public String detail(ModelMap modelMap,@PathVariable String payCenterNumber){
		String memberId = gdProperties.getProperties().getProperty("gd.memberId");
		Map<String,Object> paramMap = new LinkedHashMap<String,Object>();
//		paramMap.put("id", tradeId);
		paramMap.put("payCenterNumber", payCenterNumber);
		Map<String,Object> payInfo = agencyCollectionService.queryPayInfo(paramMap);
		//付款方信息列表
		List<Map<String,Object>> payerInfoList = null;
		//谷登代收信息列表
		List<Map<String,Object>> valleyOfCollectingInfoList = new ArrayList<>();
		//收款方信息列表
		List<Map<String,Object>> beneficiaryInfoList = null;
		//市场方信息列表
		List<Map<String,Object>> marketInfoList = null;
		if(payInfo != null){
			paramMap.put("orderNo", payInfo.get("orderNo"));
			if(payInfo.get("payerUserId") != null){
				paramMap.put("payerUserId", payInfo.get("payerUserId"));
				payerInfoList = agencyCollectionService.payerInfoList(paramMap);				
			}
			if(payInfo.get("payeeUserId") != null){
				paramMap.put("payeeUserId", payInfo.get("payeeUserId"));
				beneficiaryInfoList = agencyCollectionService.beneficiaryInfoList(paramMap);
			}
			
			/**
			 * 谷登代收信息(一定是一条, 所以从payerInfoList随便取一条判断requestNo即可)：如果"付款方信息"是预付款，对应的一条"谷登代收信息"也应该是预付款，尾款不应该出现
			 * 反之亦然
			 */
			/*if(StringUtils.isNotEmpty(memberId)){
				paramMap.put("memberId", memberId);
				for (Map<String,Object> map : payerInfoList) {
					paramMap.put("requestNo", map.get("requestNo"));
					valleyOfCollectingInfoList.add(agencyCollectionService.valleyOfCollectingInfo(paramMap));
					paramMap.remove("requestNo");
				}
			}*/
			if(StringUtils.isNotEmpty(memberId)){
	        	paramMap.put("memberId", memberId);
	        	//这里可能导致查不出valleyOfCollectingInfoList
	        	if (payerInfoList != null && payerInfoList.size() != 0) {
	        		paramMap.put("requestNo", payerInfoList.get(0).get("requestNo"));
	        		valleyOfCollectingInfoList = agencyCollectionService.valleyOfCollectingInfoList(paramMap);
	        	}
	        }
			
			/**
			 * 161224
			 * 彭宁：预付款或者全额付款的情况下，市场方信息列表(marketInfoList)不用显示
			 */
			if (payInfo.get("requestNo") == null || "2".equals(payInfo.get("requestNo")) || "0".equals(payInfo.get("requestNo"))) {
				marketInfoList = agencyCollectionService.marketInfoList(paramMap);
			}
		    
		    /**
		     * bug 3421
		     * 在(2)预付款的情况下：
		     * 付款方列表(payerInfoList)信息的市场佣金和平台佣金全为0；
		     * 收款方列表(beneficiaryInfoList)信息的市场佣金，平台佣金和刷卡补贴全为0.
		     */
//		    if ("1".equals(payInfo.get("requestNo"))) {
//		    	for (Map<String, Object> p : payerInfoList) {
//		    		p.put("marketCommissionAmt", 0.00);
//		    		p.put("platCommissionAmt", 0.00);
//		    	}
//		    	for (Map<String, Object> b : beneficiaryInfoList) {
//		    		b.put("marketCommissionAmt", 0.00);
//		    		b.put("platCommissionAmt", 0.00);
//		    		b.put("subsidyAmt", 0.00);
//		    	}
//		    }
			/**
		     * bug 3421
		     * 在(2)预付款的情况下：
		     * 付款方列表(payerInfoList)信息的市场佣金和平台佣金全为0；
		     * 收款方列表(beneficiaryInfoList)信息的市场佣金，平台佣金和刷卡补贴全为0.
		     */
		    if (CollectionUtils.isNotEmpty(payerInfoList)) {
		    	Map<String,Object> clearMap = new LinkedHashMap<String,Object>();
		    	for (Map<String, Object> p : payerInfoList) {
		    		if ("2".equals(p.get("payCount").toString())) {
			    		if ("1".equals(p.get("requestNo").toString())) { // 预付款
			    			p.put("marketCommissionAmt", 0.00);
				    		p.put("platCommissionAmt", 0.00);
				    	}
		    		}
		    		if ("1".equals(p.get("hasClearStatus"))) {
		    			p.put("clearStatus", "2"); // 清分成功
		    		} else {
		    			clearMap.clear();
		    			clearMap.put("orderNo", p.get("orderNo"));
		    			List<Map<String,Object>> orderClearList = agencyCollectionService.queryOrderClearInfo(clearMap);
		    			if (CollectionUtils.isNotEmpty(orderClearList)) {
			    			if (orderClearList.size() == 1) { // 单次支付场景
			    				if ("1".equals(orderClearList.get(0).get("hasClearPos")) || "1".equals(orderClearList.get(0).get("hasClearOnline"))) {
			    					p.put("clearStatus", "1"); // 待清分
			    				} else {
			    					p.put("clearStatus", "0"); // 不需清分
			    				}
			    			} else { // 多次支付场景
			    				boolean flag = true;
				    			String clearStatus = null;
			    				for (Map<String, Object> m : orderClearList) {
				    				if ("1".equals(m.get("hasClearPos"))) { // 存在线下pos时，以pos清分状态为准
				    					clearStatus = "1"; // 待清分
				    					break;
				    				} else if ("0".equals(m.get("hasClearPos"))) { // 存在线下pos时，以pos清分状态为准
				    					clearStatus = "0"; // 不需清分
				    					break;
				    				}
				    				if (!"1".equals(m.get("hasClearOnline"))) {  // 如果两次支付都为线上支付时，两个清分状态都需要清分时才清算
				    					flag = false;
				    				}
				    			}
			    				if (clearStatus == null) {
					    			if (flag) {
					    				clearStatus = "1"; // 待清分
					    			} else {
					    				clearStatus = "0"; // 不需清分
					    			}
			    				}
				    			p.put("clearStatus", clearStatus);
			    			}
		    			}
		    			
		    		}
		    		
		    	}
		    }
		    
		    for (Map<String, Object> m : payerInfoList) {
				if ("1".equals(payInfo.get("requestNo"))) {
					m.put("orderNo", m.get("orderNo") + " - 预付款");
				} else if ("2".equals(payInfo.get("requestNo"))) {
					m.put("orderNo", m.get("orderNo") + " - 尾款");
				} else if (payInfo.get("requestNo") == null || "0".equals(payInfo.get("requestNo"))) {
					m.put("orderNo", m.get("orderNo") + " - 全额付款");
				}
			}
		    
		    if (CollectionUtils.isNotEmpty(beneficiaryInfoList)) {
			    for (Map<String, Object> p : beneficiaryInfoList) {
			    	if ("2".equals(p.get("payCount").toString())) {
			    		if ("1".equals(p.get("requestNo").toString())) { // 预付款
			    			p.put("marketCommissionAmt", 0.00);
				    		p.put("platCommissionAmt", 0.00);
				    		p.put("subsidyAmt", 0.00);
				    		p.put("receivableAmt", p.get("prePayAmt"));
				    	} else {
				    		if (p.get("receivableAmt") != null && p.get("prePayAmt") != null) {
				    			p.put("receivableAmt", Double.valueOf(p.get("receivableAmt").toString()) - Double.valueOf(p.get("prePayAmt").toString()));
				    		}
				    	}
		    		}
			    }
		    }
		}	
		modelMap.addAttribute("payInfo", payInfo);
		modelMap.addAttribute("payerInfoList", payerInfoList);
		modelMap.addAttribute("valleyInfoList", valleyOfCollectingInfoList);
		modelMap.addAttribute("beneficiaryInfoList", beneficiaryInfoList);
		modelMap.addAttribute("marketInfoList", marketInfoList);
		return "agencyCollection/detail";
	}
	
	@ResponseBody
    @RequestMapping(value="/exportCheck",produces="application/json;charset=utf-8")
    public String personalExportCheck(HttpServletRequest request){
       //查询参数
        Map<String, Object> paramMap = getParametersMap(request);
        paramMap.put("code", 10000);
        Integer total = agencyCollectionService.countTotal(paramMap);
        if(total == 0){
        	paramMap.put("code", 20000);
            paramMap.put("result","导出的结果集无任何数据，请重新修改查询条件");
            return JSONObject.toJSONString(paramMap);
        }
        if(total > 50000){
        	paramMap.put("code", 20000);
            paramMap.put("result","查询结果集太大(>50000条), 请缩减日期范围, 或者修改其他查询条件！");
            return JSONObject.toJSONString(paramMap);
        }
        return JSONObject.toJSONString(paramMap);
    }
	
	/**
	 * 导出数据
	 */
    @RequestMapping("/exportData")
    public void exportData(HttpServletRequest request){
        //查询参数
    	String memberId = gdProperties.getProperties().getProperty("gd.memberId");
        Map<String, Object> paramMap = getParametersMap(request);
        paramMap.put("startRow", 0);
        paramMap.put("endRow", 50000);
        paramMap.put("memberId", memberId);
//        List<Map<String,Object>> mapList  =  agencyCollectionService.exportAllData(paramMap);
        List<Map<String,Object>> mapList  =  findByCondition(paramMap);
        if(mapList != null && mapList.size() > 0){    		
            String[] cell = {"第三方支付流水", "平台支付流水", "关联订单", "实付金额", "谷登代收金额", "谷登代收支付手续费", 
            		"付款方手机","支付方式","支付时间","终端号","支付状态","对账状态","所属市场","付款方"};
            writeExcel("代收款记录",cell,mapList);
        }  
    }
    
    private void writeExcel(String title,String[] cell,List<Map<String,Object>> mapList){
        OutputStream ouputStream = null;
        WritableWorkbook wwb = null;
        try{
            String fileName = title + DateUtil.toString(new Date(),"yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1") + ".xls");
            ouputStream = response.getOutputStream();           
            wwb = Workbook.createWorkbook(ouputStream);
            if(wwb != null){
                WritableSheet sheet = wwb.createSheet(title, 0);
                /**
    			 * 第一行合并水平居中
    			 */
    			WritableCellFormat wcf = new WritableCellFormat();
    			wcf.setAlignment(Alignment.CENTRE);  //把水平对齐方式指定为居中
    			sheet.mergeCells(0, 0, 13, 0);
    			sheet.addCell(new Label(0, 0, "代收款记录", wcf));
                
                //excel第2行
                for(int i = 0;i < cell.length;i++){
                    sheet.addCell(new Label(i, 1, cell[i]));
                }
                if(mapList != null && mapList.size() > 0){
                	/*int j = 0;
                    int k = 1;
                    for(Map<String, Object> map : mapList){
                        for(Map.Entry<String, Object> entry : map.entrySet()){
                        	if(entry.getKey().contains("payType")){
                        		Object value = payTypeMap.get(entry.getValue());
                        		sheet.addCell(new Label(j, k, entry.getValue()==null ? "" : String.valueOf(value).replace("null", "")));
                        	}else if(entry.getKey().contains("payStatus")){
                        		Object value = payStatusMap.get(entry.getValue());
                        		sheet.addCell(new Label(j, k, entry.getValue()==null ? "" : String.valueOf(value).replace("null", "")));                        		
                        	}else if(entry.getKey().contains("checkStatus")){
                        		Object value = checkStatusMap.get(entry.getValue());
                        		sheet.addCell(new Label(j, k, entry.getValue()==null ? "" : String.valueOf(value).replace("null", "")));                        		
                        	}else if(entry.getKey().contains("appKey")){
                        		Object value = appKeyMap.get(entry.getValue());
                        		sheet.addCell(new Label(j, k, entry.getValue()==null ? "" : String.valueOf(value).replace("null", "")));                        		
                        	}else{
                                sheet.addCell(new Label(j, k, entry.getValue()==null ? "" : String.valueOf(entry.getValue()).replace("null", "")));                        		
                        	}
                            j++;                         
                        }
                        j=0;
                        k++;
                    }*/
                	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i=0; i<mapList.size(); i++) {
                    	Map<String, Object> map = mapList.get(i);
                    	Label label0 = new Label(0, i + 2, map.get("thirdPayNumber") == null ? "-" : map.get("thirdPayNumber").toString());
                    	Label label1 = new Label(1, i + 2, map.get("payCenterNumber") == null ? "-" : map.get("payCenterNumber").toString());
                    	Label label2 = new Label(2, i + 2, map.get("orderNo") == null ? "-" : map.get("orderNo").toString());
                    	Label label3 = new Label(3, i + 2, map.get("payAmt") == null ? "-" : map.get("payAmt").toString());
                    	Label label4 = new Label(4, i + 2, map.get("payAmt") == null ? "-" : map.get("payAmt").toString());
                    	Label label5 = new Label(5, i + 2, map.get("feeAmt") == null ? "-" : map.get("feeAmt").toString());
                    	Label label6 = new Label(6, i + 2, map.get("payerMobile") == null ? "-" : map.get("payerMobile").toString());
                    	Label label7 = new Label(7, i + 2, PayWayEnum.getNameByCode(map.get("payType") == null ? "-" : map.get("payType").toString()));
                    	Label label8 = new Label(8, i + 2, map.get("payTime") == null ? "-" : sdFormat.format(sdFormat.parse(map.get("payTime").toString())));
                    	Label label9 = new Label(9, i + 2, map.get("posClientNo") == null ? "-" : map.get("posClientNo").toString());
                    	Label label10 = new Label(10, i + 2, PayStatusEnum.getNameByCode(Byte.valueOf(map.get("payStatus") == null ? "-" : map.get("payStatus").toString())));
                    	Label label11 = new Label(11, i + 2, CheckStatusEnum.getNameByCode(map.get("checkStatus") == null ? "-" : map.get("checkStatus").toString()));
                    	Label label12 = new Label(12, i + 2, map.get("marketName") == null ? "-" : map.get("marketName").toString());
                    	Label label13 = new Label(13, i + 2, UserTypeEnum.getNameByCode(map.get("appKey") == null ? "-" : map.get("appKey").toString()));
                    	System.out.println("map.get(appKey)" + map.get("appKey") + ",  " + "Enum : " + UserTypeEnum.getNameByCode(map.get("appKey").toString()));
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
                    	sheet.addCell(label10);
                    	sheet.addCell(label11);
                    	sheet.addCell(label12);
                    	sheet.addCell(label13);
                    }
                }
                wwb.write();
                wwb.close();
                ouputStream.flush();
                ouputStream.close();
            }            
        }catch(Exception e){
            logger.info(title+"导出EXCEL异常>>>>>>>>",e);
        }finally {
            wwb = null;
            ouputStream = null;
        }
    }

}
