package cn.gdeng.paycenter.admin.controller.base;


import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * desc: 控制器基类，可以在里面增加一些公共输出方
 * User: zhangsi
 * Date: 2015/9/7 10:37
 *
 * @Copyright: Aerospace Silk Road Supply Chain Management Co.,Ltd 2015-2019
 */
public class BaseController {
	
	/**
	 * 定义记录日志信息
	 */
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 注入HttpServletRequest
	 */
	@Resource
	private HttpServletRequest httpRequest;
	/**
	 * 设置Session
	 */
	private HttpSession session;

	protected static String EXPORTING = "exporting";
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return httpRequest;
	}

	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		this.session = httpRequest.getSession();
		return this.session;
	}

	
	public void createZipHttpResponseHead(HttpServletResponse response,String fileName){
		response.setContentType("application/octet-stream;charset=UTF-8");
		try {
			fileName = new String(fileName.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
	}

	
	public void createTextHttpResponseHead(HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Content-Disposition", "");
	}	
}
