package cn.gdeng.paycenter.admin.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.job.PayJobService;
import cn.gdeng.paycenter.api.server.pay.ClearDetailService;
import cn.gdeng.paycenter.api.server.pay.ClearLogService;
import cn.gdeng.paycenter.api.server.pay.ClearSumService;
import cn.gdeng.paycenter.dto.pay.VClearDetailDto;

/**
 * 清分日志
 * @author gaofeng
 * */
@Controller
@RequestMapping("clear")
public class ClearLogController extends AdminBaseController{
	
	@Reference
	public ClearDetailService	clearDetailService;
	
	@Reference
	public ClearLogService	clearLogService;
	
	@Reference
	public ClearSumService	clearSumService;
	
	@Reference
	private PayJobService payJobService;
	
	/**
	 * 返回清分日志页面
	 * */
	@RequestMapping("log/index")
	public String logIndex(Model model){
		return "log/clearLogList";
	}
	
	/**
	 * 返回清分日志详情页面
	 * */
	@RequestMapping("log/detail/index")
	public String logDetailIndex(Model model,HttpServletRequest	request){
		long id = Long.parseLong(request.getParameter("id"));
		request.setAttribute("clearSum", this.clearSumService.getClearSumById(id));
		return "log/clearLogDetail";
	}
	/**
	 * 返回重新清分页面
	 * */
	@RequestMapping("reclear/index")
	public String reClearIndex(Model model){
		return "log/reclear";
	}
	
	/**
	 * 清分日志分页数据
	 * */
	@RequestMapping("log/list")
    @ResponseBody
	public Map<String,Object> getClearLogList(HttpServletRequest request){
		Map<String, Object> params = new HashMap<>();
		params.put("payType", request.getParameter("payType"));
		params.put("payTimeBegin", request.getParameter("payTimeBegin"));
		params.put("payTimeEnd", request.getParameter("payTimeEnd"));
		params.put("startRow", (Integer.parseInt(request.getParameter("page"))-1) * Integer.parseInt(request.getParameter("rows")));
		params.put("endRow", Integer.parseInt(request.getParameter("page")) * Integer.parseInt(request.getParameter("rows")));
		Map<String,Object> map = new HashMap<>();
		try {
			map.put("success", true);
			map.put("total", this.clearSumService.queryCountByParams(params));
			map.put("rows", this.clearSumService.queryByParams(params));
		} catch (BizException e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 清分日志详情分页数据
	 * */
	@RequestMapping("log/detail/list")
    @ResponseBody
	public Map<String, Object> getClearLogDetailList(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("payType", request.getParameter("payType"));
		params.put("payTime", request.getParameter("payTime"));
		params.put("startRow", (Integer.parseInt(request.getParameter("page"))-1) * Integer.parseInt(request.getParameter("rows")));
		params.put("endRow", Integer.parseInt(request.getParameter("page")) * Integer.parseInt(request.getParameter("rows")));
		try {
			map.put("success", true);
			map.put("total", this.clearLogService.queryCountByParams(params));
			map.put("rows", this.clearLogService.queryByParams(params));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 重新清分
	 * */
	@RequestMapping("reclear")
	@ResponseBody
	public Map<String,Object> reClear(HttpServletRequest request){
		String payTime = request.getParameter("payTime");
		Map<String, Object> map = new HashMap<>();
		try {
			String[] payTimes = payTime.split("\\s+");
//			payJobService.bindOrderActRelation(payTimes[0], null); //清分前先绑定订单与活动的关系表
			payJobService.clearBill(payTimes[0], null);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}
	
	
	
	/**
	 * 清分明细接口查询
	 * */
	@RequestMapping("detail/list")
	@ResponseBody
	public Map<String, Object> getClearDetailByParams(HttpServletRequest request,VClearDetailDto vClearDetailDto){
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", vClearDetailDto.getOrderNo());
		params.put("memberId", vClearDetailDto.getMemberId());
		params.put("userType", vClearDetailDto.getUserType());
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("success",true);
			result.put("data", this.clearDetailService.queryByParams(params));
		} catch (BizException e) {
			e.printStackTrace();
			result.put("success",false);
		}
		return result;
	}

}
