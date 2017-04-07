package cn.gdeng.paycenter.admin.controller.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.api.ApiService;
import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.api.server.pay.RefundRecordService;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.dto.pay.VClearDetailDto;
import cn.gdeng.paycenter.dto.refund.RefundRecordDto;
import cn.gdeng.paycenter.dto.refund.RefundTradeDto;
import cn.gdeng.paycenter.util.web.api.ApiResult;

@Controller
@RequestMapping("api")
public class ApiController {

	@Reference
	private ApiService apiService;
	
	@Reference
	private PayTypeService payTypeService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private RefundRecordService refundRecordService;
	
	@RequestMapping("getVClearList")
	@ResponseBody
	public ApiResult<List<VClearDetailDto>> getVClearList(VClearDetailDto dto){
		
		logger.info("获取结算数据入参:"+JSON.toJSONString(dto));
		
		ApiResult<List<VClearDetailDto>> res = new ApiResult<>();
		try {
			List<VClearDetailDto> list = apiService.getVClearList(dto);
			res.setResult(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			res.setIsSuccess(false);
			res.setMsg(e.getMessage());
		}
		logger.info("获取结算数据返回:"+JSON.toJSONString(res));
		return res;

	}
	
	/**
	 * 需要给外部调用，如订单系统
	 * @param request
	 * @return
	 */
	@RequestMapping("getAll")
	@ResponseBody
	public ApiResult<List<PayTypeDto>> getAllChannel(HttpServletRequest request){
		ApiResult<List<PayTypeDto>> res = new ApiResult<>();
		try {
			List<PayTypeDto> list = payTypeService.queryAll();
			res.setResult(list);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			res.setIsSuccess(false);
			res.setMsg("获取支付渠道失败");
		}
		return res;

	}
	
	@RequestMapping("refundQuery")
	public void refundQuery(RefundTradeDto dto,HttpServletResponse response) throws BizException, IOException{
		ApiResult<RefundRecordDto> res = new ApiResult<>();
		logger.info("退款查询入参:"+JSON.toJSONString(dto));
		RefundRecordDto refund = refundRecordService.getRefundRecord(dto);
		res.setResult(refund);
		String resStr = JSON.toJSONString(res,SerializerFeature.WriteDateUseDateFormat);
		logger.info("退款查询出参:"+resStr);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(resStr);
	}
	public static void main(String[] args) {
		RefundRecordDto dto = new RefundRecordDto();
		dto.setRefundTime(new Date());
		System.out.println(JSON.toJSONString(dto,SerializerFeature.WriteDateUseDateFormat));
	}
}
