package cn.gdeng.paycenter.api.server.pay;

import java.util.Map;

import cn.gdeng.paycenter.api.BizException;

public interface AccessSysSignService {
	
	public void accessSysSign(Map<String , String> paramMap,String sign) throws BizException; 

}
