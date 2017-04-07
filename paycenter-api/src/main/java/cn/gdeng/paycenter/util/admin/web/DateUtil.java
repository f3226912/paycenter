package cn.gdeng.paycenter.util.admin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;


/**
 * @author jingyul
 * @version 1.0  2009-1-19
 * @preserve protected
 */
public class DateUtil  extends DateUtils {

	/**
	 * 一天的MilliSecond 数
	 */
	public static final long DAY_MILLI = 24 * 60 * 60 * 1000;

	/**
	 * 没有分割符号的日期格式  yyyyMMdd
	 */
	public static final String DATE_FORMAT_DATEONLYNOSP = "yyyyMMdd";

	/**
	 * yyyy/MM/dd
	 */
	public static final String DATE_FORMAT_DATEONLYED = "yyyy/MM/dd";

	/**
	 * 要用到的DATE Format的定义[yyyy-MM-dd]
	 */
	public static final String DATE_FORMAT_DATEONLY = "yyyy-MM-dd";

	/**
	 * 要用到的DATE Format的定义[yyyy-MM-dd HH:mm:ss]
	 */
	public static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 *要用到的DATE Format的定义[yyyy/MM/dd HH:mm]
	 */
	public final static String DATE_FORMAT_DATEHOURMIN = "yyyy/MM/dd HH:mm";

	/**
	 * 要用到的DATE Format的定义[yyyyMMddHHmmss]
	 */
	public final static String DATE_FORMAT_SESSION = "yyyyMMddHHmmss"; // 年/月/日 时:分:秒
	/**
	 * 要用到的DATE Format的定义[yyyyMM]
	 */
	public final static String DATE_FORMAT_SESSION_YEARMONTH = "yyyyMM"; // 年/月/日 时:分:秒

	public static SimpleDateFormat sdfDateOnlyNoSp = new SimpleDateFormat(
			DATE_FORMAT_DATEONLYNOSP);

	/**
	 * Global SimpleDateFormat object
	 */
	public static SimpleDateFormat sdfDateOnly = new SimpleDateFormat(
			DATE_FORMAT_DATEONLY);

	/**
	 * 日期格式为yyyy/MM/dd
	 */
	public static SimpleDateFormat sdfDateOnlyed = new SimpleDateFormat(
			DATE_FORMAT_DATEONLYED);

	public static SimpleDateFormat sdfDateTime = new SimpleDateFormat(
			DATE_FORMAT_DATETIME);

	/**
	 * 日期格式为yyyyMMddHHMMss
	 */
	private static SimpleDateFormat sdfDateTimes = new SimpleDateFormat(
			DATE_FORMAT_SESSION);

	/**
	 * 以YYYY-MM-DD格式返回系统日期
	 * @return  系统日期
	 * @since 1.0
	 * @history
	 */
	public static String getSysDateString() {
		return toString(new java.util.Date(System.currentTimeMillis()),
				DateUtil.sdfDateOnly);
	}

	/**
	 * 以YYYY-MM-DD HH:mm:ss格式返回系统日期
	 * @return
	 * @author 李冬
	 * @time 2015年10月15日 上午9:21:21
	 */
	public static String getSysDateTimeString() {
		return toString(new java.util.Date(System.currentTimeMillis()),
				DateUtil.sdfDateTime);
	}

	/**
	 * 返回当前系统日期，返回日期格式为:yyyyMMDD
	 * @author jingyul
	 * @return
	 */
	public static String getNoSpSysDateString() {
		return toString(new java.util.Date(System.currentTimeMillis()),
				DateUtil.sdfDateOnlyNoSp);
	}



	public static String get14BitSysDateString() {
		return toString(new java.util.Date(System.currentTimeMillis()),
				DateUtil.sdfDateTimes);
	}

	/*****************************************************
	 * java.util.Date ==> String 的转换函数
	 *****************************************************/
	/**
	 * 利用缺省的Date格式(YYYY/MM/DD)转化String到Date
	 * @param  sDate Date string
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static java.util.Date formateDate(String sDate) {
		return formateDate(sDate, DateUtil.sdfDateOnly);
	}

	/**
	 * 根据指定的Format转化String到Date
	 * @author jingyul
	 * @param strDate
	 * @param strFmt DATE_FORMAT_DATEONLY or  DATE_FORMAT_DATETIME
	 * @return
	 */
	public static Date formateDate(String strDate, String strFmt) {
		if (strFmt.equals(DATE_FORMAT_DATETIME)) { //  "YYYY/MM/DD HH24:MI:SS"
			return formateDate(strDate, DateUtil.sdfDateTime);
			//}else if( sFmt.equals (DateUtil.DATE_FORMAT_SESSION  ) ){  //YYYYMMDDHHMI
			//    return toDate(sDate,DateUtil.sdfDateSession );
		} else if (strFmt.equals(DATE_FORMAT_DATEONLY)) { //YYYY/MM/DD
			return formateDate(strDate, DateUtil.sdfDateOnly);
		} else if (!StringUtils.isEmpty(strFmt)) {
			return formateDate(strDate, new SimpleDateFormat(strFmt));
		} else {
			return null;
		}
	}

	/**
	 * 获得当前日期为一个星期几,用1至7分别表示星期一到星期天
	 * @author jingyul
	 * @param strDate  20090427日期格式其余不支持
	 * @return
	 */
	public static int getDayOfWeek(String strDate) {
		Date d = formateDate(strDate, DateUtil.DATE_FORMAT_DATEONLYNOSP);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int i = cal.get(Calendar.DAY_OF_WEEK);
		switch (i) {
			case 1:
				return 7;
			case 2:
				return 1;
			case 3:
				return 2;
			case 4:
				return 3;
			case 5:
				return 4;
			case 6:
				return 5;
			default:
				return 6;
		}
	}
	/**
	 * 去星期六天除计算获得两日期内的时间天数清单，星期六天不往后延迟，法定日除外.
	 * @author jingyul
	 * @param startStr  20090708日期格式
	 * @param endStr    20090709日期格式
	 * @return
	 */
	public static List getDay(String startStr, String endStr) {
		List r = new ArrayList();
		Date start = DateUtil.formateDate(startStr,DATE_FORMAT_DATEONLYNOSP);
		double num = DateUtil.getBetweenDays(startStr, endStr);
		if (getDayOfWeek(startStr)<6) {
			r.add(startStr);
		}
		if(startStr.equals(endStr)){
			return r;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		int n =0;
		if(num>0){
			n = (int)num;
		}
		for (int i = 0; i<n;) {
			cal.add(Calendar.DATE, 1);
			i++;
			switch (cal.get(Calendar.DAY_OF_WEEK)) {
				case Calendar.SUNDAY:
				case Calendar.SATURDAY:
					break;
				default:
					StringBuffer b = new StringBuffer(cal.get(Calendar.YEAR));
					b.append(cal.get(Calendar.MONTH)+1);
					String year = String.valueOf(cal.get(Calendar.YEAR));
					int mon = cal.get(Calendar.MONTH)+1;
					String month = null;
					if(mon<10){
						month = "0"+mon;
					}else{
						month = String.valueOf(mon);
					}
					int day = cal.get(Calendar.DATE);
					String strday;
					if(day<10){
						strday ="0"+day;
					}else{
						strday = String.valueOf(day);
					}
					String sumday = year+month+strday;
					if(sumday.equals(endStr)){
						r.add(sumday);
						return r;
					}
					r.add(sumday);
					break;
			}
		}
		return r;
	}

	/**
	 * 获得两个日期之间的相隔的天数
	 * @author jingyul
	 * @param start
	 * @param end
	 * @return 如果end 在 start之后，返回正数，否则返回负数
	 */
	public static long getBetweenDays(java.sql.Timestamp start,
			java.sql.Timestamp end) {
		return (start.getTime() - end.getTime()) / DAY_MILLI;
	}

	/**
	 * 获得两个日期之间的相隔的天数
	 * @author fuzhuan
	 * @param start
	 * @param end
	 * @return 如果end 在 start之后，返回正数，否则返回负数,有一个日期为空则返回0
	 */
	public static double getBetweenDays(java.util.Date start, java.util.Date end) {
		double en=0,cn=0;
		if(end!=null&&start!=null){

			 en = end.getTime();
			 cn = start.getTime();
		}
		double rs = (en - cn) / DAY_MILLI;
		return rs;
	}
	/**
	 * 计算两个日期之间相差的天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int daysBetween(java.util.Date start, java.util.Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / DAY_MILLI;

		return Integer.parseInt(String.valueOf(between_days));
	}
	/**
	 * 按照指定日期格式，格式化日期，并返回这两个日期之间相隔的天数.<br>
	 * 如果日期为(20081010121200)14位的日期如果两日期之间没有满足24小时，不计为一天.
	 * <b>注意:日期格式化样式与日期要相匹配，否则计算的结果会出现问题
	 * @author jingyul
	 * @param startDate  开始日期 20081010 or 20081010121200 or 2008-12-12 12:10:10
	 * @param endDate    结束日期 20081010 or 20081010121200 or 2008-12-12 12:10:10
	 * @param formatStyle 日期格式化样式
	 * @return 如果end 在 start之后，返回正数，否则返回负数
	 */
	public static double getBetweenDays(String startDate, String endDate,
			String formatStyle) {
		Date start = DateUtil.formateDate(startDate, formatStyle);
		Date end = DateUtil.formateDate(endDate, formatStyle);
		return getBetweenDays(start, end);
	}

	/**
	 * 比对两日期之间相隔的天数，默认格式化样式为yyyyMMdd.日期只能为:20090207类似的格式
	 * @author jingyul
	 * @param startDate 开始日期
	 * @param endDate  结束日期
	 * @return 如果end 在 start之后，返回正数，否则返回负数
	 */
	public static double getBetweenDays(String startDate, String endDate) {
		return DateUtil.getBetweenDays(startDate, endDate,
				DATE_FORMAT_DATEONLYNOSP);
	}

	public static Date formateDate(String strDate, SimpleDateFormat formatter) {
		java.util.Date dt = null;
		try {
			dt = formatter.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
			dt = null;
		}
		return dt;
	}

	/*****************************************************
	 * String ==> java.util.Date  的转换函数
	 *****************************************************/
	/**
	 * 根据缺省的Format(YYYY/MM/DD)转化java.util.Date到String
	 * @param  dt java.util.Date instance
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static String toString(java.util.Date dt) {
		if (dt == null) {
			return "";
		}
		return toString(dt, DateUtil.sdfDateOnly);
	}

	/**
	 * 根据指定的Format转化java.util.Date到String
	 * @param  dt java.util.Date instance
	 * @param  sFmt  Date format ,  DATE_FORMAT_DATEONLY or  DATE_FORMAT_DATETIME
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static String toString(java.util.Date dt, String sFmt) {
		// add by SJNS/zq 03/16
		if (dt == null) {
			return "";
		}
		if (!StringUtils.isEmpty(sFmt)) {
			return toString(dt, new SimpleDateFormat(sFmt));
		} else {
			return toString(dt, DateUtil.sdfDateOnly);
		}
	}

	/**
	 * 利用指定SimpleDateFormat instance转换java.util.Date到String
	 * @param  dt java.util.Date instance
	 * @param  formatter  SimpleDateFormat Instance
	 * @return
	 * @since 1.0
	 * @history
	 */
	private static String toString(java.util.Date dt, SimpleDateFormat formatter) {
		String sRet = null;

		try {
			sRet = formatter.format(dt).toString();
		} catch (Exception e) {
			e.printStackTrace();
			sRet = null;
		}

		return sRet;
	}
	/**
	 * 取系统Date型时间yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static java.sql.Timestamp getNow() {
		Date dt  = new Date();
		java.sql.Timestamp dateTime = new java.sql.Timestamp(dt.getTime());//Timestamp类
		return dateTime;
	}
	/**
	 * 根据java.util.DATE获得java.sql.Timestamp系统时间
	 * @param date
	 * @return
	 */
	public static java.sql.Timestamp getNow(java.util.Date date) {
		if(null == date){
			return null;
		}
		java.sql.Timestamp dateTime = new java.sql.Timestamp(date.getTime());//Timestamp类
		 return dateTime;
	}

	 /**
     * 获取指定格式的日期字符串
     *
     * @param date
     * @param fmt
     * @return
     */
    public static Date getDate(String date, String fmt) {
    	Date da = null;
        if (date == null)
            return null;
        try{
        	SimpleDateFormat format1 = new SimpleDateFormat(fmt);
        	da = format1.parse(date);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return da;
    }

    /**
     * 获取指定格式的日期字符串
     *
     * @param date
     * @param fmt
     * @return
     */
    public static String getDate(Date date, String fmt) {
        if (date == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        return format.format(date);
    }

    /**
	 * 计算两个日期之间相差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetweenNew(java.util.Date start, java.util.Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		double between_days = (double)(time2 - time1) / DAY_MILLI;
		if(between_days>0){
			long days = (time2 - time1) / DAY_MILLI +1;
			return Integer.parseInt(String.valueOf(days));
		}else{
			return 0;
		}
	}
	/**
	 * 判断两个时间间隔是否大于interval天, startDate为空，设定结果为大于interval天,
	 * endDate为空, 计算startDate 至当前时间的天数。
	 * startDate 和endDate不为空，直接转为时间计算间隔天数
	 * */
	public static boolean  isDateIntervalOverFlow(String startDate, String endDate, int interval) {
		Double between = 0d;
		if(StringUtil.isEmpty(startDate)){
			between = Double.MAX_VALUE;
		}else if(StringUtil.isEmpty(endDate)){
			 between = getBetweenDays(DateUtil.formateDate(startDate, DATE_FORMAT_DATEONLY),new Date());
		}else {
			 between = getBetweenDays(startDate, endDate, DATE_FORMAT_DATEONLY);
		}
		return between > interval;
	}

	/**
	 * 格式化字符串时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatStrDate(String date, String format){
		return getDate(formateDate(date, format), format);
	}

	public static void main(String[] args) {
		String startDate = "2016-04-12";
		String endDate = "";
		System.out.println(isDateIntervalOverFlow(startDate, endDate, 31));
//		System.out.println(DateUtil.getNow());
//		System.out.println(DateUtil.getSysDateString());
//		System.out.println(DateUtil.getSysDateTimeString());
	}
}
