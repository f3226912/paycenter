package cn.gdeng.paycenter.gateway.exception.hander;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.ApiResult;


public class CommonExceptionHandler implements HandlerExceptionResolver{
	
	/**
	 * 定义记录日志信息
	 */
	protected Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handlerMethod,
			Exception ex) {
		ApiResult<?> result = new ApiResult<>();
		//返回公共api错误信息
		if(ex instanceof BizException){
			BizException biz = (BizException)ex;
			result.withError(biz.getCode(), biz.getMsg());
		}else{
			result.withError(MsgCons.C_50000, MsgCons.M_50000);
		}
		logger.info(JSON.toJSONString(result), ex);
		
		try {
			String jsonStrCipher = JSON.toJSONString(result);
			//设置json头
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(jsonStrCipher);
		} catch (Exception e) {
			logger.error("加密失败", e);
		}
		//不能直接返回null
		return new ModelAndView();
	}

}

