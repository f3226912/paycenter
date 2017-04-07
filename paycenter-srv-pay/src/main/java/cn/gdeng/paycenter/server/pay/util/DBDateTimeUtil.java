package cn.gdeng.paycenter.server.pay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DB时间处理，暂时的
 * @author sss
 *
 */
public class DBDateTimeUtil {

	public static String getDBDateTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null == date){
			return null;
		} else {
			return sdf.format(date);
		}
	}
}
