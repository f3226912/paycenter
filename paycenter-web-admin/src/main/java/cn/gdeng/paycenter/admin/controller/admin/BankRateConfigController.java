package cn.gdeng.paycenter.admin.controller.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gdeng.paycenter.admin.controller.right.AdminBaseController;
import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.BankRateConfigDTO;
import cn.gdeng.paycenter.admin.service.admin.BankRateConfigService;
import cn.gdeng.paycenter.admin.service.admin.PayTypeService;
import cn.gdeng.paycenter.dto.pay.PayTypeDto;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.web.api.ApiResult;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gudeng.framework.core2.GdLogger;
import com.gudeng.framework.core2.GdLoggerFactory;
/**
 * 渠道支付费率Controller
 * 
 * @author kwang
 *
 */
@Controller
@RequestMapping("bankRateConfigController")
public class BankRateConfigController extends AdminBaseController {
	  /** 记录日志 */
    @SuppressWarnings("unused")
    private static final GdLogger logger = GdLoggerFactory.getLogger(BankRateConfigController.class);
    
	@Reference
	private BankRateConfigService bankRateConfigService;
	@Reference
	private PayTypeService payTypeService;
	

	
    @RequestMapping("index")
	public String index(Model model){
		return "bankRateConfig/bankRateConfigList";
	}

    @RequestMapping("addBankRateConfig")
    public String addNstNotice(){
    	return "bankRateConfig/addBankRateConfig";
    }
	
    /**
     * 获取 渠道支付费率列表
     * @param request
     * @param BankRateConfigDTO
     * @return
     */
    @RequestMapping("getBankRateConfigList")
    @ResponseBody
    public String getBankRateConfigList(HttpServletRequest request){
		Map<String, Object> map = getParametersMap(request);
		//设定分页,排序
		setCommParameters(request, map);
		ApiResult<AdminPageDTO> apiResult = bankRateConfigService.queryPage(map);
		if(apiResult != null){
			return JSONObject.toJSONString(apiResult.getResult(),SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
    }
    
    @RequestMapping("getPayChannel")
    @ResponseBody
    public String getPayChannel() {
	try {
		 ApiResult<List<PayTypeDto>> apiResult = payTypeService.queryAll();
	    return JSONObject.toJSONString(apiResult);
	} catch (Exception e) {
		e.printStackTrace();
		return JSONObject.toJSONString(null);
	}
	}

    
    
    

    @RequestMapping("save")
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request) {
    	Map<String, Object> map = getParametersMap(request);
    	SysRegisterUser user = getUser(getRequest());
    	map.put("remark",map.get("remark").toString().replaceAll("\\r\\n", ""));
    	getMap(map,  user);
    	ApiResult<Integer> Count =bankRateConfigService.queryBankRateConfigSettingCount(map);
    	if(Count.getResult()>0){
    		map.put("msg", "同一支付渠道&卡类型&业务类型，只允许添加一次！");
    	  	return map;
    	}
    	try {
    		getMapJson(map);
    		bankRateConfigService.insert(map);
			map.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "添加异常");
		}
    	return map;
    }
	
    
  	@RequestMapping("editById")
      public String editById( Model model) {
  		Map<String, Object> map = getParametersMap(request);
        BankRateConfigDTO dto = bankRateConfigService.selectById(map).getResult();
        model.addAttribute("dto", dto);
        model.addAttribute("vStatus", map.get("vStatus"));
        return "bankRateConfig/editBankRateConfig";
      }
  	

    @RequestMapping("update")
    @ResponseBody
    public Map<String, Object> update(HttpServletRequest request) {
    	Map<String, Object> map = getParametersMap(request);
    	SysRegisterUser user = getUser(getRequest());
    	map.put("remark",map.get("remark").toString().replaceAll("\\r\\n", ""));
    	getMap(map,  user);
    	ApiResult<Integer> Count =bankRateConfigService.queryBankRateConfigSettingCount(map);
    	if(Count.getResult()>0){
    		map.put("msg", "同一支付渠道&卡类型&业务类型，只允许添加一次！");
    	  	return map;
    	}
    	try {
    		getMapJson(map);
    		bankRateConfigService.update(map);
			map.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改异常");
		}
    	return map;
    }
	
	
	
	
    /**
     * 拼装map
     * @param map
     */
	private void getMap(Map<String, Object> map,SysRegisterUser user) {
		map.put("createuserId", user==null?"":user.getUserID());
		map.put("updateuserId", user==null?"":user.getUserID());
	}

    /**
     * 拼装map
     * @param map
     */
	private void getMapJson(Map<String, Object> map) {
		String  procedures =(String)map.get("procedures");
		JSONObject json=new JSONObject();
		if(procedures.equals("1")){
			json.put("procedures", procedures);
			json.put("proportion", String.valueOf(map.get("proportion")));
			json.put("max", String.valueOf(map.get("max")));
		}
		if(procedures.equals("2")){
			json.put("procedures", procedures);
			json.put("fixed", String.valueOf(map.get("fixed")));
		}
		map.put("feeRuleJson", json.toJSONString());
	}
	
	
	
	
	
	
}
