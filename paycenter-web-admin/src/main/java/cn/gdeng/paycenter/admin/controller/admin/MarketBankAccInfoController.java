package cn.gdeng.paycenter.admin.controller.admin;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.service.admin.MarketBankAccInfoService;
import cn.gdeng.paycenter.api.server.customer.CustomerHessianService;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
import cn.gdeng.paycenter.dto.pay.MarketBankAccInfoDTO;
import cn.gdeng.paycenter.entity.pay.MarketBankAccInfoEntity;
import cn.gdeng.paycenter.entity.pay.MemberBaseinfoEntity;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;
/**
 * 市场银行账号信息
 * @author wj
 */
@Controller
@RequestMapping("marketBankAccInfo")
public class MarketBankAccInfoController extends AdminBaseController {
	  /** 记录日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(MarketBankAccInfoController.class);
    
    @Reference
    private MarketBankAccInfoService marketBankAccInfoService;
	@Resource
	CustomerHessianService customerHessianService;
    
    @RequestMapping("index")
    public String index(Model model){
    	return "marketBankAccInfo/marketBankAccInfoList";
    }
    
    /**
     * 获取市场信息列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("getMarketBankAccInfoList")
    @ResponseBody
    public String getMarketBankAccInfoList(HttpServletRequest request,MarketBankAccInfoDTO marketBankAccInfoDTO){
    	logger.info("");
		Map<String, Object> map = new HashMap<>();
		map.put("marketId", marketBankAccInfoDTO.getMarketId());
		map.put("mobile", marketBankAccInfoDTO.getMobile());
		
		//设定分页,排序
		setCommParameters(request, map);
		
		ApiResult<AdminPageDTO> apiResult = marketBankAccInfoService.queryPage(map);
		if(apiResult != null){
			return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
    }
    
	/**
	 * 通过hession调用获取市场列表的服务
	 * @param type
	 * @return
	 */
    @RequestMapping("queryMarketList")
    @ResponseBody
    public String queryMarketList(){
    	ApiResult<List<MarketHessianDTO>> result = new ApiResult<List<MarketHessianDTO>>();
    	result.setResult(customerHessianService.getAllByType("2"));
		if(result.getResult().size() > 0){
			return JSONObject.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
		}
    	return null;
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public String delete() throws Exception{
		String idsStr = request.getParameter("ids");
		String[] ids = idsStr.split(",");
		ApiResult<int[]> apiResult = marketBankAccInfoService.batchDelete(ids);
		return JSONObject.toJSONString(apiResult);
    }
    
    @RequestMapping("addDialog")
    public String addDialog(){
    	return "marketBankAccInfo/add";
    }
    
	@RequestMapping(value="save", produces="application/json;charset=utf-8")
	@ResponseBody
	public String save(HttpServletRequest request, MarketBankAccInfoEntity entity) throws Exception{
		String validateResult = validateLogin(request);
		if(validateResult != null){
			return validateResult;
		}
		String value[] = entity.getMarketName().split(",");
		entity.setMarketId(Long.parseLong(value[0]));
		entity.setMarketName(value[1]);
		entity.setCreateUserId(getUser(request).getUserID());
		entity.setIsDeleted(0);
		ApiResult<Long> apiResult = marketBankAccInfoService.save(entity);
		return JSONObject.toJSONString(apiResult);
	}
	
	/**
	 * 验证登录信息是否存在
	 * @param request
	 * @return
	 */
	private String validateLogin(HttpServletRequest request){
		SysRegisterUser registerUser = getUser(request);
		if(registerUser == null){
			ApiResult<String> apiResult = new ApiResult<String>();
			return JSONObject.toJSONString(apiResult.withError(MsgCons.C_30000,  MsgCons.M_30000)); 
		}
		return null;
	}
	
	@RequestMapping("showDialog/{id}")
	public String detail(@PathVariable("id")Long id){
		ApiResult<MarketBankAccInfoDTO> apiResult = marketBankAccInfoService.queryById(id);
		if(apiResult != null){
			putModel("dto", apiResult.getResult());
		}
		return "marketBankAccInfo/show";
	}
	
	@RequestMapping("editDialog/{id}")
	public String editDialog(@PathVariable("id")Long id){
		ApiResult<MarketBankAccInfoDTO> apiResult = marketBankAccInfoService.queryById(id);
		if(apiResult != null){
			putModel("dto", apiResult.getResult());
		}
		return "marketBankAccInfo/edit";
	}
	
	@RequestMapping(value="edit", produces="application/json;charset=utf-8")
	@ResponseBody
	public String edit(HttpServletRequest request, MarketBankAccInfoEntity entity) throws Exception{
		String validateResult = validateLogin(request);
		if(validateResult != null){
			return validateResult;
		}
		entity.setUpdateUserId(getUser(request).getUserID());
		entity.setUpdateTime(new Date());
		ApiResult<Integer> apiResult = marketBankAccInfoService.edit(entity);
		return JSONObject.toJSONString(apiResult);
	}
	
	@RequestMapping(value="getByMobile")
	@ResponseBody
	public ApiResult<MemberBaseinfoEntity> getByMobile(String mobile){
		return marketBankAccInfoService.getByMobile(mobile);
	}
}
