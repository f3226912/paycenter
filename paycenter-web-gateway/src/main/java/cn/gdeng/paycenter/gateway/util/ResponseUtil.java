package cn.gdeng.paycenter.gateway.util;

import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.dto.pos.ResponseDto;
import cn.gdeng.paycenter.util.web.api.MD5;
import cn.gdeng.paycenter.util.web.api.RsaUtil;

public class ResponseUtil {
	
	public static String getSuccessSignResData(ResponseDto res,String privateKey){
		if(null == res){
			res = new ResponseDto();
		}
		res.setResultcode("0000");
		res.setResultmsg("成功");
		return getSignResData(res,privateKey);
	}
	
	public static String getFailSignResData(ResponseDto res,String errorMsg,String privateKey){
		if(null == res){
			res = new ResponseDto();
		}
		res.setResultcode("0001");
		res.setResultmsg(errorMsg);
		return getSignResData(res,privateKey);
	}

	private static String getSignResData(ResponseDto res,String privateKey){
		if(null == privateKey){
			throw new RuntimeException("密钥不存在");
		}
		res.setSignmsg(null);
		String msg = JSON.toJSONString(res);
		String signmsg = RsaUtil.sign(MD5.crypt(msg).toLowerCase(), privateKey, "UTF-8");
		res.setSignmsg(signmsg);
		//msg不做处理
		return JSON.toJSONString(res);
	}
	
	public static void main(String[] args) {
		ResponseDto res = new ResponseDto();
		res.setResultcode("0000");
		res.setResultmsg("成功");
		res.setDatajson("aaaaa");
		res.setSignmsg("dddddddddddd222");
		System.err.println(JSON.toJSONString(res));
	}

}
