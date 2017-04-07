package cn.gdeng.paycenter.util.web.api;

import java.util.Random;

public class StringUtil {
	/**
	 * 生成指定长度字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
}
