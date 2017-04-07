package cn.gdeng.paycenter.server.pay.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.enums.KeyTypeEnum;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;
import cn.gdeng.paycenter.util.web.api.MD5;
import cn.gdeng.paycenter.util.web.api.RsaUtil;

/* *
 * 
 */
public class AlipayVerifyUtil {
	
	private static Logger logger = LoggerFactory.getLogger(AlipayVerifyUtil.class);

    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params,AlipayConfig config) throws BizException {

        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "true";
//		if(params.get("notify_id") != null) {
//			String notify_id = params.get("notify_id");
//			responseTxt = verifyResponse(notify_id,config);
//		}
		if(!StringUtils.equals(responseTxt, "true")){
			throw new BizException(MsgCons.C_20009, MsgCons.M_20009);
		}
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    boolean isSign = getSignVeryfy(params, sign,config);
	    if(!isSign){
	    	throw new BizException(MsgCons.C_20008, MsgCons.M_20008);
	    }
        //写日志记录（若要调试，请取消下面两行注释）
        String sWord = "支付宝###URL地址验证:responseTxt=" + responseTxt + "\n 参数验签:isSign=" + isSign + "\n 返回来的参数：" + AlipaySignUtil.createLinkString(params);
	    //AlipayCore.logResult(sWord);
        logger.info(sWord);
        return true;

    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	public static boolean getSignVeryfy(Map<String, String> Params, String sign,AlipayConfig config) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = AlipaySignUtil.paraFilter(Params,config.isUseSignType());
        //获取待签名字符串
        String preSignStr = AlipaySignUtil.createLinkString(sParaNew);
        //获得签名验证结果

        return getSignVeryfy(preSignStr,sign,config);
    }
	
	 /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	public static boolean getSignVeryfy(String preSignStr, String sign,AlipayConfig config) {
        //获得签名验证结果
        boolean isSign = false;
        if(config.getKeyType().equals(KeyTypeEnum.MD5.getWay())) {
        	isSign = MD5.verify(preSignStr, sign, config.getKey(), config.getInput_charset());
        } else if(config.getKeyType().equals(KeyTypeEnum.RSA.getWay())){
        	isSign = RsaUtil.verify(preSignStr, sign, config.getAlipayPublicKey(), config.getInput_charset());
        }
        return isSign;
    }

    /**
    * 获取远程服务器ATN结果,验证返回URL
    * @param notify_id 通知校验ID
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    @SuppressWarnings("unused")
	private static String verifyResponse(String notify_id,AlipayConfig config) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

        String partner = config.getPartner();
        String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;

        return checkUrl(veryfy_url);
    }

    /**
    * 获取远程服务器ATN结果
    * @param urlvalue 指定URL路径地址
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String checkUrl(String urlvalue) {
        String inputLine = "";

        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                .getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }

        return inputLine;
    }
}
