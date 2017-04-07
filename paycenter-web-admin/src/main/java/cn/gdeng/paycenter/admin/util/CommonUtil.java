package cn.gdeng.paycenter.admin.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gdeng.paycenter.admin.enums.AlidayuEnum;
import cn.gdeng.paycenter.util.web.api.Constant;



public class CommonUtil {

	public static String generatePictureName(String fileName, int width){
		int index = fileName.lastIndexOf(".");
		String fileExt = fileName.substring(index);
		String newName = fileName.substring(0, index) + width + "_" + width + fileExt;
		return newName;
	}
	public static String generateSimpleFileName(String orgName){
		int index = orgName.lastIndexOf(File.separator);
		String fileName = "";
		if (index != -1){
			fileName = orgName.substring(index+1);
		}else {
			fileName = orgName;
		}
		return fileName;
	}
	public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }
	public static String getStartOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return DateUtil.getDate(calendar.getTime(), DateUtil.DATE_FORMAT_DATETIME);
	}
	public static String getEndOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return DateUtil.getDate(calendar.getTime(), DateUtil.DATE_FORMAT_DATETIME);
	}
	/**
	 * 
	 * @param messageType 消息类型 1:找回密码 2：WEB端注册验证码3：手机端注册验证码4商户添加用户短信通知
	 * @param param 参数
	 * @return
	 * @throws JSONException 
	 */
	public static String alidayuUtil(Integer messageType,String param) throws JSONException{
		JSONObject  alidayujson = new JSONObject();
		JSONObject alidayuParam = new JSONObject();
		if(messageType==Constant.Alidayu.MESSAGETYPE1){
			//模版id
			alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE1.value());
			//短信签名
			alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
			alidayuParam.put(Constant.Alidayu.CODE, param);
			alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
		}
		if(messageType==Constant.Alidayu.MESSAGETYPE2){
		//模版id
		alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE2.value());
		//短信签名
		alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
		alidayuParam.put(Constant.Alidayu.CODE, param);
		alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
		}
		if(messageType==Constant.Alidayu.MESSAGETYPE3){
			//模版id
			alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE3.value());
			//短信签名
			alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
			alidayuParam.put(Constant.Alidayu.CODE, param);
			alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
			}
		if(messageType==Constant.Alidayu.MESSAGETYPE4){
			//模版id
			alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE4.value());
			//短信签名
			alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
			alidayuParam.put(Constant.Alidayu.PASSWORD, param);
			alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
			}
		if(messageType==Constant.Alidayu.MESSAGETYPE5){
			//模版id
			alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE5.value());
			//短信签名
			alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
			alidayuParam.put(Constant.Alidayu.CODE, param);
			alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
			}
		if(messageType==Constant.Alidayu.MESSAGETYPE6){
			//模版id
			alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE8.value());
			//短信签名
			alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
			alidayuParam.put(Constant.Alidayu.CODE, param);
			alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
			}
		if(messageType==Constant.Alidayu.MESSAGETYPE7){
			//模版id
			alidayujson.put(Constant.Alidayu.TEMPLATECODE, AlidayuEnum.templateCode.TEMPLATECODE9.value());
			//短信签名
			alidayujson.put(Constant.Alidayu.FREESIGNNAME,AlidayuEnum.FreeSignName.FREESIGNNAME1.value());
			alidayuParam.put(Constant.Alidayu.CODE, param);
			alidayujson.put(Constant.Alidayu.PARAM, alidayuParam);
			}
		return  alidayujson.toString();
	}
}
