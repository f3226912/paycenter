package cn.gdeng.paycenter.admin.controller.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 支付渠道控制器
* @author DavidLiang
* @date 2016年11月8日 下午12:13:41
 */
@Controller
@RequestMapping("payType")
public class PayTypeController extends AdminBaseController {
	
	@Reference
	private PayTypeService payTypeService;
	
	/**
	 * 支付渠道首页
	* @author DavidLiang
	* @date 2016年11月8日 下午3:22:14
	* @return
	 */
	@RequestMapping("index")
    public String index(){
    	return "sysmgr/payChannel/payChannelList";
    }
	
	/**
	 * 根据条件分页查询支付渠道
	* @author DavidLiang
	* @date 2016年11月8日 下午2:36:09
	* @param request
	* @param dto
	* @return
	* @throws Exception
	 */
	@RequestMapping("getPageByCondition")
	@ResponseBody
	public String getPageByCondition(HttpServletRequest request, PayTypeDto dto) throws Exception{
		@SuppressWarnings("unchecked")
		Map<String,Object> paramMap = BeanUtils.describe(dto);
		setCommParameters(request, paramMap);
		ApiResult<AdminPageDTO> apiResult = payTypeService.findPageByCondition(paramMap);
		if (apiResult != null) {
			/**
			 * 由于easyui datagrid接收的json数据最外层必须要有rows, total
			 * 所以在这先把apiResult.getResult()转化的json, 再切割, 添加值, 最后拼接返回
			 */
			String json = JSONObject.toJSONString(apiResult.getResult(), SerializerFeature.WriteDateUseDateFormat);
			return json.substring(0, json.length() - 1) + ",\"code\":" + apiResult.getCode() + "}";
//			return JSONObject.toJSONString(apiResult.getResult(), SerializerFeature.WriteDateUseDateFormat); 
		}
		return null;
	}
	
	

}
