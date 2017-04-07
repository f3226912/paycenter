package cn.gdeng.paycenter.util.web.api;

import org.springframework.util.StringUtils;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.MsgCons;

/**
 * 
 * @author sss
 *
 * since:2016年12月7日
 * version 1.0.0
 */
public class Assert {

	public static void notEmpty(String str,String msg) throws BizException{
		if(!StringUtils.hasText(str)){
			throw new BizException(MsgCons.C_20001, msg);
		}
	}
	
	public static void notEmpty(String str,Integer code,String msg) throws BizException{
		if(!StringUtils.hasText(str)){
			throw new BizException(code, msg);
		}
	}
}
