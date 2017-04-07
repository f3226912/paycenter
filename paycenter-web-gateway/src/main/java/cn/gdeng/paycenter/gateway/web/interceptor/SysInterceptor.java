package cn.gdeng.paycenter.gateway.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.util.web.api.ApiResult;

public class SysInterceptor extends HandlerInterceptorAdapter {
	
	 /** 不用检查的checkUrl */
    private List<String> doNotCheckUrl;

    public List<String> getDoNotCheckUrl() {
        return doNotCheckUrl;
    }

    public void setDoNotCheckUrl(List<String> doNotCheckUrl) {
        this.doNotCheckUrl = doNotCheckUrl;
    }  

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 过滤不检查url
    	
//    	String url = request.getRequestURI();
//        if (doNotCheckUrl != null) {
//            String str = "";
//            for (int i = 0; i < doNotCheckUrl.size(); i++) {
//                str = doNotCheckUrl.get(i);
//                if (url.indexOf(str) >= 0) {
//                    return super.preHandle(request, response, handler);
//                }
//            }
//        }
//    	
//    	//Map<String,String> headerMap = WebUtil.getHeaderMap(request);
//    	//处理header todo
//    	
//    	//验证参数签名正确性 to-do    	
//    	Map<String,String> paramMap = WebUtil.getParameterMap(request);
//    	String appKey = paramMap.get("appKey");    	
//    	String appSecret = AppSecretManager.getSecret(appKey);
//    	String signParam = (String)paramMap.get("sign");
//    	if(StringUtils.isEmpty(appKey) 
//    			|| StringUtils.isEmpty(appSecret)
//    			||StringUtils.isEmpty(signParam)){
//    		ApiResult<?> result = new ApiResult<>();
//    		result.withError(MsgCons.C_20001, MsgCons.M_20001);    		
//    		renderJson(response,result);
//    		return false;
//    	}    	
//    	
//    	List<String> ignore = new ArrayList<String>();
//    	ignore.add("sign");
//    	String sign = SignUtil.sign(paramMap, ignore, appSecret);
//    	
//    	if(!signParam.equals(sign)){
//    		ApiResult<?> result = new ApiResult<>();
//    		result.withError(MsgCons.C_20001, MsgCons.M_20001);    		
//    		renderJson(response,result);
//    		return false;
//    	}    	
    	
        return super.preHandle(request, response, handler);
    }
    
    private void renderJson(HttpServletResponse response,ApiResult<?> result){
    	try {
			String jsonStrCipher = JSON.toJSONString(result);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(jsonStrCipher);
		} catch (Exception e) {
		}
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }


}
