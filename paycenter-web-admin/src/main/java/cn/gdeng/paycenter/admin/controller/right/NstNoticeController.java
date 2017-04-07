package cn.gdeng.paycenter.admin.controller.right;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.admin.dto.admin.NstNoticeEntityDTO;
import cn.gdeng.paycenter.admin.service.admin.NstNoticeService;
import cn.gdeng.paycenter.entity.pay.SysRegisterUser;
import cn.gdeng.paycenter.util.web.api.ApiResult;
/**
 * 农速通controller
 * @author xiaojun
 */
@Controller
@RequestMapping("notice")
public class NstNoticeController extends AdminBaseController {
	  /** 记录日志 */
    @SuppressWarnings("unused")
    private static final GdLogger logger = GdLoggerFactory.getLogger(NstNoticeController.class);
    
    @Reference
    private NstNoticeService nstNoticeService;
    
    @RequestMapping("index")
    public String index(Model model){
    	return "notice/nstNoticeList";
    }
    
    @RequestMapping("addNstNotice")
    public String addNstNotice(){
    	return "notice/addNstNotice";
    }
    /**
     * 获取公告列表
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("getNstNoticeList")
    @ResponseBody
    public String getNstNoticeList(HttpServletRequest request,NstNoticeEntityDTO nstDto){
		Map<String, Object> map = new HashMap<>();
		map.put("startBeginTime", nstDto.getStartBeginTime());
		map.put("startEndTime", nstDto.getStartEndTime());
		map.put("endBeginTime", nstDto.getEndBeginTime());
		map.put("endEndTime", nstDto.getEndEndTime());
		//设定分页,排序
		setCommParameters(request, map);
		ApiResult<AdminPageDTO> apiResult = nstNoticeService.queryPage(map);
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
    @RequestMapping("save")
    @ResponseBody
    public String save(HttpServletRequest request,NstNoticeEntityDTO nstDto) {
    	Map<String, Object> map = new HashMap<>();
    	SysRegisterUser user = getUser(getRequest());
    	getMap(map, nstDto, user);
    	ApiResult<Integer> result =nstNoticeService.insert(map);
    	return JSONObject.toJSONString(result.getResult(),SerializerFeature.WriteDateUseDateFormat);
    }
    /**
     * 拼装map
     * @param map
     */
	private void getMap(Map<String, Object> map,NstNoticeEntityDTO nstDto,SysRegisterUser user) {
		// TODO Auto-generated method stub
		map.put("startTime", nstDto.getStartTime());
		map.put("endTime", nstDto.getEndTime());
		map.put("notice", nstDto.getNotice());
		map.put("createuserId", user==null?"":user.getUserID());
		map.put("updateuserId", user==null?"":user.getUserID());
	}
	
	/**
     * 删除公告
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        ApiResult<String> result = nstNoticeService.delete(id);
        return result.getResult();
    }
    
	/**
     * 修改公告
     * 
     * @param request
     * @param response
     * @return
     */
    
	@RequestMapping("editById/{id}")
    public String editById(@PathVariable("id") String id, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
        NstNoticeEntityDTO dto = nstNoticeService.selectById(map).getResult();
        model.addAttribute("dto", dto);
        return "notice/editNstNotice";
    }
	
    /**
     * 保存更新公告
     * @param request
     * @param nstDto
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public String update(HttpServletRequest request,NstNoticeEntityDTO nstDto) {
    	Map<String, Object> map = new HashMap<>();
    	SysRegisterUser user = getUser(getRequest());
    	getMap(map, nstDto, user);
    	map.put("id", nstDto.getId());
    	ApiResult<Integer> result =nstNoticeService.update(map);
    	return JSONObject.toJSONString(result.getResult());
    }
}
