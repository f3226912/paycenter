package cn.gdeng.paycenter.server.pay.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;

public class AlipayParseResUtil {
	
	private static String SIGN ="sign";

	public static <T> T parse(Class<T> clazz ,AlipayConfig ac,String json,String responseName) throws BizException{
		//先验签
		JSONObject js = JSON.parseObject(json);
		responseName = StringUtils.replace(responseName, ".", "_");
		
		String signSource = parseSignSourceData(json,responseName);
		String sign = js.getString("sign");
		boolean res = AlipayVerifyUtil.getSignVeryfy(signSource, sign, ac);
		if(res){
			return JSON.parseObject(signSource, clazz);
		} else {
			throw new BizException(MsgCons.C_20008, MsgCons.M_20008);
		}
	}
	
	  /**
     *   获取签名源串内容
     *    
     * 
     * @param body
     * @param rootNode
     * @param indexOfRootNode
     * @return
     */
    private static String parseSignSourceData(String body, String rootNode) {

        //  第一个字母+长度+引号和分号
    	int indexOfRootNode = body.indexOf(rootNode);
        int signDataStartIndex = indexOfRootNode + rootNode.length() + 2;
        int indexOfSign = body.indexOf("\"" + SIGN + "\"");

        if (indexOfSign < 0) {

            return null;
        }

        // 签名前-逗号
        int signDataEndIndex = indexOfSign - 1;

        return body.substring(signDataStartIndex, signDataEndIndex);
    }
}
