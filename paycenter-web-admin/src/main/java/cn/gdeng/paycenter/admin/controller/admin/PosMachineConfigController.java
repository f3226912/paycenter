package cn.gdeng.paycenter.admin.controller.admin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.service.admin.PosMachineConfigService;
import cn.gdeng.paycenter.api.server.customer.CustomerHessianService;
import cn.gdeng.paycenter.dto.customer.MarketHessianDTO;
import cn.gdeng.paycenter.dto.pay.PosMachineConfigDTO;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;
/**
 * POS终端管理
 * @author wj
 */
@Controller
@RequestMapping("posMachineConfig")
public class PosMachineConfigController extends AdminBaseController {
	  /** 记录日志 */
    private static final GdLogger logger = GdLoggerFactory.getLogger(PosMachineConfigController.class);
    
    @Reference
    private PosMachineConfigService posMachineConfigService;
	@Resource
	CustomerHessianService customerHessianService;
    
    @RequestMapping("index")
    public String index(Model model){
    	return "posMachineConfig/posMachineConfigList";
    }
    
    /**
     * 获取POS终端管理列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("getPosMachineConfigList")
    @ResponseBody
    public String getBankinfoList(HttpServletRequest request, PosMachineConfigDTO posMachineConfigDTO){
    	logger.info("");
		Map<String, Object> map = new HashMap<>();
		map.put("marketId", posMachineConfigDTO.getMarketId());
		map.put("machineNum", posMachineConfigDTO.getMachineNum());
		map.put("shopsName", posMachineConfigDTO.getShopsName());
		//设定分页,排序
		setCommParameters(request, map);
		
		ApiResult<AdminPageDTO> apiResult = posMachineConfigService.queryPage(map);
		if(apiResult != null){
			return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
    }
    
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
}
