package cn.gdeng.paycenter.util.admin.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import sun.misc.BASE64Encoder;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.ReusableIterator;

/**
 *功能描述：
 */
public class StringUtil extends com.gudeng.framework.ws.utils.StringUtil {

	/**
	 * 前导标识
	 */
	public static final int BEFORE = 1;

	/**
	 * 后继标识
	 */
	public static final int AFTER = 2;

	/**
	 * 默认的分隔符
	 */
	public static final String DEFAULT_PATH_SEPARATOR = ",";

	
	/**
	 * 取得前台中文参数 (ISO8859-1转UTF-8)
	 * 
	 * @param commParameter
	 *            前台参数值
	 * @return commParametered 转码后的值
	 * @throws UnsupportedEncodingException
	 * @author wenxuewu
	 * @date 2012-05-31
	 */
	public static String getCommParameter(String commParameter)
			throws UnsupportedEncodingException {

		return (StringUtils.isEmpty(commParameter) ? null : (new String(
				commParameter.getBytes("ISO8859-1"), "UTF-8")));
	}
	
	/**
	 * 生成随机字符串
	 * 
	 */
	public static String GenerateMobileRandomStr(int randStrLength) {
		String randStr = "0123456789"; // 写入你所希望的所有的字母A-Z,a-z,0-9
		StringBuffer generateRandStr = new StringBuffer();
		Random rand = new Random();
		// int randStrLength = 4; // 你想生成的随机数的长度
		for (int i = 0; i < randStrLength; i++) {
			int randNum = rand.nextInt(10);
			generateRandStr.append(randStr.substring(randNum, randNum + 1));
		}
		return generateRandStr.toString();
	}

	/**
	 * 生成随机字符串---不得有重复值
	 * 
	 */
	public static int[] getRandomStrByLength(int length, int randSize) {
		int[] randStr = new int[randSize];
		Random rand = new Random();
		for (int i = 0; i < randStr.length; i++) {
			int randNum = rand.nextInt(length);
			if (i == 0) {
				randStr[i] = randNum;
			} else {
				boolean b = true;
				for (int j = 0; j < randStr.length; j++) {
					if (randStr[j] == randNum) {
						i--;
						b = false;
						break;
					}
				}
				if (b) {
					randStr[i] = randNum;
				}
			}
		}
		return randStr;
	}

	/**
	 * 生成随机字符串
	 * 
	 */
	public static String GenerateRandomStr() {
		String randStr = "ABCDEFGHIabcdef0123456789"; // 写入你所希望的所有的字母A-Z,a-z,0-9
		StringBuffer generateRandStr = new StringBuffer();
		Random rand = new Random();
		int randStrLength = 5; // 你想生成的随机数的长度
		for (int i = 0; i < randStrLength; i++) {
			int randNum = rand.nextInt(25);
			generateRandStr.append(randStr.substring(randNum, randNum + 1));
		}
		return "_" + generateRandStr.toString();
	}

	/**
	 * 判断传入的字符串是否是一个合法的移动手机号码
	 * 
	 * @param srcNo
	 * @return true or false
	 */
	public static boolean isMobileNum(String srcNo) {
		if (srcNo != null)
			return srcNo.matches("(13|15)[0-9]{9}");
		else
			return false;
	}

	/**
	 * 检查邮箱有效性
	 * 
	 * @param emailName
	 * @return
	 */
	public static boolean isEmail(String emailName) {
		if (emailName.matches("[\\w\\W]{3,30}@(?:\\w+\\.)+\\w+")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回字符串的长度
	 * 
	 * @param str
	 * @return
	 */
	public static int getStrLen(String str) {
		/*
		 * String reg="[^u4e00-^u9fa5]"; java.util.regex.Pattern
		 * p=java.util.regex.Pattern.compile(reg,java.util.regex.Pattern.CASE_INSENSITIVE);
		 * java.util.regex.Matcher m=p.matcher(str); return
		 * m.replaceAll("aa").length();
		 */
		if ((str != null) && (str.length() > 0)) {
			return str.getBytes().length;
		}
		return 0;
	}

	/**
	 * 替换字符串
	 * 
	 * @param orig
	 * @param dis
	 * @return
	 */
	public static String replaceStr(String orig, String dis) {
		int m = 0;
		String msg = "";
		while (true) {
			int n = orig.indexOf(0x0D, m);
			if (n == -1) {
				break;
			}

			if (n > m) {
				msg += orig.substring(m, n);
			}
			msg += dis;
			m = n + 2;
		}
		if (m >= 0) {
			msg += orig.substring(m);
		}
		return strFilter(msg);
	}

	/**
	 * 过滤字符串里的特殊字符
	 * 
	 * @param msg1
	 * @return
	 */
	public static String strFilter(String msg1) {
		// System.out.println("msg="+msg1+" length="+msg1.length());
		boolean b = true;
		StringBuffer msg = new StringBuffer(msg1);
		if (msg != null && !msg.equals("")) {
			// System.out.println("----------------");
			for (int i = 0; i < msg.length(); i++) {
				if (msg.charAt(i) == '\\' && i != msg.length() - 1) {
					if (msg.charAt(i + 1) != 'n') {
						msg.insert(i, '\\');
						i++;
					}
					b = false;
				}
				if (msg.charAt(i) == '\"' && b == true) {
					msg.insert(i, '\\');
					i++;
				}
				b = true;
			}
		}
		return msg.toString();
	}

	/**
	 * 输出字符串长度
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String outStr(String str, Integer len) {
		if (getStrLen(str) > len) {
			for (int i = 0; i < (len); i++) {

				if (getStrLen(str.substring(0, i + 1)) > len) {
					return str.substring(0, i);
				}
			}
			return str.substring(0, len);
		}
		return strFilter(str);
	}

	/**
	 * 过滤非法字符
	 * 
	 * @param input
	 * @return
	 */
	public static String filter(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&#034;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			case '\'':
				filtered.append("&#039;");
				break;
			default:
				filtered.append(c);
			}

		}
		return (filtered.toString());
	}

	/**
	 * 判断是否存在特殊字符
	 * 
	 * @param input
	 * @return
	 */
	public static boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i <= input.length() - 1; i++) {
				c = input.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;

				}
			}
		}
		return flag;
	}

	/**
	 * 输出字符串的长度
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String outStrByLen(String str, Integer len) {
		String s = splitStrByLen(str, len);
		s = filter(s);
		return s;
	}

	/**
	 * 按字符串的长度输出字符串
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String splitStrByLen(String str, Integer len) {
		if (getStrLen(str) > len) {
			for (int i = 0; i < (len - 3); i++) {
				if (getStrLen(str.substring(0, i + 1)) > (len - 3)) {
					return str.substring(0, i) + "...";
				}
			}
			return str.substring(0, len - 3) + "...";
		}
		return strFilter(str);

	}

	/**
	 * 判断字符串是否为中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean getChinese(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) > 128) {
				return true;
			}

		}
		return false;

	}

	/**
	 * 判断字符串转型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean stringToLong(String str) {
		boolean b = false;
		try {
			Long.parseLong(str);
			b = true;
		} catch (Exception e) {
			b = false;
		}
		return b;
	}

	public static byte[] gbk2utf8(String chenese) {
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);
			// System.out.println(word);

			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			// 补零
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");

			// System.out.println(sb.toString());

			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);

			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];

		}
		return fullByte;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// String s = "" ;
		//String a = "asdfsadf<>azzAS审时',.度势法";
		//String b = new String(a.getBytes(), "GBK");
		//byte[] fullByte = gbk2utf8(b);
		//String fullStr = new String(fullByte, "UTF-8");
		// System.out.println("string from GBK to UTF-8 byte: " + fullStr);

	}

	/**
	 * 将一个中间带逗号分隔符的字符串转换为ArrayList对象
	 * 
	 * @param str
	 *            待转换的符串对象
	 * @return ArrayList对象
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList strToArrayList(String str) {
		return strToArrayList(str, DEFAULT_PATH_SEPARATOR);
	}

	/**
	 * 将字符串对象按给定的分隔符separator转象为ArrayList对象
	 * 
	 * @param str
	 *            待转换的字符串对象
	 * @param separator
	 *            字符型分隔符
	 * @return ArrayList对象
	 */
	public static ArrayList<String> strToArrayList(String str, String separator) {
		StringTokenizer strTokens = new StringTokenizer(str, separator);
		ArrayList<String> list = new ArrayList<String>();

		while (strTokens.hasMoreTokens()) {
			list.add(strTokens.nextToken().trim());
		}
		return list;
	}

	/**
	 * 将一个中间带逗号分隔符的字符串转换为字符型数组对象
	 * 
	 * @param str
	 *            待转换的符串对象
	 * @return 字符型数组
	 */
	public static String[] strToStrArray(String str) {
		return strToStrArray(str, DEFAULT_PATH_SEPARATOR);
	}

	/**
	 * 将字符串对象按给定的分隔符separator转象为字符型数组对象
	 * 
	 * @param str
	 *            待转换的符串对象
	 * @param separator
	 *            字符型分隔符
	 * @return 字符型数组
	 */
	public static String[] strToStrArray(String str, String separator) {
		StringTokenizer strTokens = new StringTokenizer(str, separator);
		String[] strArray = new String[strTokens.countTokens()];
		int i = 0;

		while (strTokens.hasMoreTokens()) {
			strArray[i] = strTokens.nextToken().trim();
			i++;
		}

		return strArray;
	}
	
	/**
	 * 
	 * 使用默认的分隔符将字符串进行分割，并放入集合
	 * 
	 * @param str
	 * @return
	 */
	public static Set<String> strToSet(String str) {
		return strToSet(str, DEFAULT_PATH_SEPARATOR);
	}

	/**
	 * 
	 * 用分隔符将字符串进行分割，并放入集合
	 * 
	 * @param str
	 * @param separator
	 * @return
	 */
	public static Set<String> strToSet(String str, String separator) {
		String[] values = strToStrArray(str, separator);
		Set<String> result = new LinkedHashSet<String>();
		for (int i = 0; i < values.length; i++) {
			result.add(values[i]);
		}
		return result;
	}

	/**
	 * 将一个数组，用逗号分隔变为一个字符串
	 * 
	 * @param array
	 * @return
	 */
	public static String ArrayToStr(Object[] array) {
		if (array == null || array.length < 0)
			return null;
		String str = "";
		int _step = 0;
		for (int i = 0; i < array.length; i++) {
			if (_step > 0)
				str += ",";
			str += (String) array[i];
			_step++;
		}
		return str;
	}

	/**
	 * 将一个数组,用逗号和空格分隔变成一个字符串
	 * 
	 * @param array
	 * @return
	 */
	public static String ArrayToUtilStr(Object[] array) {
		if (array == null || array.length < 0)
			return null;
		String str = "";
		int _step = 0;
		for (int i = 0; i < array.length; i++) {
			if (_step > 0)
				str += ", ";
			str += (String) array[i];
			_step++;
		}
		return str;
	}

	/**
	 * 转换给定字符串的编码格式
	 * 
	 * @param inputString
	 *            待转换字符串对象
	 * @param inencoding
	 *            待转换字符串的编码格式
	 * @param outencoding
	 *            准备转换为的编码格式
	 * @return 指定编码与字符串的字符串对象
	 */
	public static String encodingTransfer(String inputString,
			String inencoding, String outencoding) {
		if (inputString == null || inputString.length() == 0)
			return inputString;
		try {
			return new String(inputString.getBytes(outencoding), inencoding);
		} catch (Exception e) {
			return inputString;
		}
	}

	/**
	 * 删除字符串中指定的字符 只要在delStrs参数中出现的字符，并且在inputString中也出现都会将其它删除
	 * 
	 * @param inputString
	 *            待做删除处理的字符串
	 * @param delStrs
	 *            作为删除字符的参照数据，用逗号分隔。如果要删除逗号可以在这个字符串再加一个逗号
	 * @return 返回一个以inputString为基础不在delStrs出现新字符串
	 */
	public static String delString(String inputString, String delStrs) {
		if (inputString == null || inputString.length() == 0)
			return inputString;

		String[] strs = strToStrArray(delStrs);
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equals(""))
				inputString = inputString.replaceAll(" ", "");
			else {
				if (strs[i].compareTo("a") >= 0)
					inputString = replace(inputString, strs[i], "");
				else
					inputString = inputString.replaceAll("\\" + strs[i], "");
			}
		}
		if (subCount(delStrs, ",") > strs.length)
			inputString = inputString.replaceAll("\\,", "");

		return inputString;
	}

	/**
	 * 去除左边多余的空格。
	 * 
	 * @param value
	 *            待去左边空格的字符串
	 * @return 去掉左边空格后的字符串
	 */
	public static String trimLeft(String value) {
		String result = value;
		if (result == null)
			return result;
		char ch[] = result.toCharArray();
		int index = -1;
		for (int i = 0; i < ch.length; i++) {
			if (Character.isWhitespace(ch[i])) {
				index = i;
			} else {
				break;
			}
		}
		if (index != -1) {
			result = result.substring(index + 1);
		}
		return result;
	}

	/**
	 * 去除右边多余的空格。
	 * 
	 * @param value
	 *            待去右边空格的字符串
	 * @return 去掉右边空格后的字符串
	 */
	public static String trimRight(String value) {
		String result = value;
		if (result == null)
			return result;
		char ch[] = result.toCharArray();
		int endIndex = -1;
		for (int i = ch.length - 1; i > -1; i--) {
			if (Character.isWhitespace(ch[i])) {
				endIndex = i;
			} else {
				break;
			}
		}
		if (endIndex != -1) {
			result = result.substring(0, endIndex);
		}
		return result;
	}

	/**
	 * 判断一个字符串中是否包含另一个字符串
	 * 
	 * @param parentStr
	 *            父串
	 * @param subStr
	 *            子串
	 * @return 如果父串包含子串的内容返回真，否则返回假
	 */
	public static boolean isInclude(String parentStr, String subStr) {
		boolean hasSub = false;
		for (int i = 0; i < (parentStr.length() - subStr.length() + 1); i++) {
			String tempString = parentStr.substring(i, i + subStr.length());
			if (subStr.equals(tempString)) {
				hasSub = true;
				break;
			}
		}

		return hasSub;
	}

	/**
	 * 判断一个字符串中是否包含另一个字符串数组的任何一个
	 * 
	 * @param parentStr
	 *            父串
	 * @param subStrs
	 *            子串集合
	 * @return 如果父串包含子串的内容返回真，否则返回假
	 */
	public static boolean isIncludes(String parentStr, String subStrs) {
		String[] subStrArray = strToStrArray(subStrs);
		for (int j = 0; j < subStrArray.length; j++) {
			String subStr = subStrArray[j];
			if (isInclude(parentStr, subStr))
				return true;
			else
				continue;
		}
		return false;
	}

	/**
	 * 判断一个子字符串在父串中重复出现的次数
	 * 
	 * @param parentStr
	 *            父串
	 * @param subStr
	 *            子串
	 * @return 子字符串在父字符串中重得出现的次数
	 */
	public static int subCount(String parentStr, String subStr) {
		int count = 0;

		for (int i = 0; i < (parentStr.length() - subStr.length()); i++) {
			String tempString = parentStr.substring(i, i + subStr.length());
			if (subStr.equals(tempString)) {
				count++;
			}
		}

		return count;
	}

	/**
	 * 得到在字符串中从开始字符串到结止字符串中间的子串
	 * 
	 * @param parentStr
	 *            父串
	 * @param startStr
	 *            开始串
	 * @param endStr
	 *            结止串
	 * @return 返回开始串与结止串之间的子串
	 */
	public static String subString(String parentStr, String startStr,
			String endStr) {
		return parentStr.substring(parentStr.indexOf(startStr)
				+ startStr.length(), parentStr.indexOf(endStr));
	}

	/**
	 * 得到在字符串中从开始字符串到结止字符串中间的子串的集合
	 * 
	 * @param parentStr
	 *            父串
	 * @param startStr
	 *            开始串
	 * @param endStr
	 *            结止串
	 * @return 返回开始串与结止串之间的子串的集合
	 */
	public static List<String> subStringList(String parentStr, String startStr,
			String endStr) {
		List<String> result = new ArrayList<String>();
		List<String> elements = dividToList(parentStr, startStr, endStr);
		for (String element : elements) {
			if (!isIncludes(element, startStr) || !isIncludes(element, endStr))
				continue;
			result.add(subString(element, startStr, endStr));
		}
		return result;

	}

	/**
	 * 将指定的String转换为Unicode的等价值
	 * 
	 * 基础知识： 所有的转义字符都是由 '\' 打头的第二个字符 0-9 :八进制 u :是Unicode转意，长度固定为6位
	 * Other:则为以下字母中的一个 b,t,n,f,r,",\ 都不满足，则产生一个编译错误 提供八进制也是为了和C语言兼容. b,t,n,f,r
	 * 则是为控制字符.书上的意思为:描述数据流的发送者希望那些信息如何被格式化或者被表示. 它可以写在代码的任意位置，只要转义后是合法的. 例如:
	 * int c=0\u003b 上面的代码可以编译通过，等同于int c=0; \u003b也就是';'的Unicode代码
	 * 
	 * @param str
	 *            待转换为Unicode的等价字符串
	 * @return Unicode的字符串
	 */
	public static String getUnicodeStr(String inStr) {
		char[] myBuffer = inStr.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < inStr.length(); i++) {
			//byte b = (byte) myBuffer[i];
			short s = (short) myBuffer[i];
			//String hexB = Integer.toHexString(b);
			String hexS = Integer.toHexString(s);
			/*
			 * //输出为大写 String hexB = Integer.toHexString(b).toUpperCase();
			 * String hexS = Integer.toHexString(s).toUpperCase(); //print char
			 * sb.append("char["); sb.append(i); sb.append("]='");
			 * sb.append(myBuffer[i]); sb.append("'\t");
			 * 
			 * //byte value sb.append("byte="); sb.append(b); sb.append(" \\u");
			 * sb.append(hexB); sb.append('\t');
			 */

			// short value
			sb.append(" \\u");
			sb.append(fillStr(hexS, "0", 4, AFTER));
			// sb.append('\t');
			// Unicode Block
			// sb.append(Character.UnicodeBlock.of(myBuffer[i]));
		}
		return sb.toString();
	}

	/**
	 * 判断一个字符串是否是合法的Java标识符
	 * 
	 * @param s
	 *            待判断的字符串
	 * @return 如果参数s是一个合法的Java标识符返回真，否则返回假
	 */
	public static boolean isJavaIdentifier(String s) {
		if (s.length() == 0 || Character.isJavaIdentifierStart(s.charAt(0)))
			return false;
		for (int i = 0; i < s.length(); i++)
			if (!Character.isJavaIdentifierPart(s.charAt(i)))
				return false;
		return true;
	}

	/**
	 * 替换字符串中满足条件的字符 例如: replaceAll("\com\hi\platform\base\\util",'\\','/');
	 * 返回的结果为: /com/hi/platform/base/util
	 * 
	 * @param str
	 *            待替换的字符串
	 * @param oldchar
	 *            待替换的字符
	 * @param newchar
	 *            替换为的字符
	 * @return 将str中的所有oldchar字符全部替换为newchar,并返回这个替换后的字符串
	 */
	public static String replaceAll(String str, char oldchar, char newchar) {
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == oldchar)
				chars[i] = newchar;
		}
		return new String(chars);
	}

	/**
	 * 如果inStr字符串与没有给定length的长度，则用fill填充，在指定direction的方向
	 * 如果inStr字符串长度大于length就直截返回inStr，不做处理
	 * 
	 * @param inStr
	 *            待处理的字符串
	 * @param fill
	 *            以该字符串作为填充字符串
	 * @param length
	 *            填充后要求的长度
	 * @param direction
	 *            填充方向，如果是AFTER填充在后面，否则填充在前面
	 * @return 返回一个指定长度填充后的字符串
	 */
	public static String fillStr(String inStr, String fill, int length,
			int direction) {
		if (inStr == null || inStr.length() > length || inStr.length() == 0)
			return inStr;

		StringBuffer tempStr = new StringBuffer("");
		for (int i = 0; i < length - inStr.length(); i++) {
			tempStr.append(fill);
		}

		if (direction == AFTER)
			return new String(tempStr.toString() + inStr);
		else
			return new String(inStr + tempStr.toString());
	}

	/**
	 * 字符串替换
	 * 
	 * @param str
	 *            源字符串
	 * @param pattern
	 *            待替换的字符串
	 * @param replace
	 *            替换为的字符串
	 * @return
	 */
	public static String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));

		return result.toString();
	}

	/**
	 * 分隔字符串到一个集合
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> dividToList(String str, String start, String end) {
		if (str == null || str.length() == 0)
			return null;

		int s = 0;
		int e = 0;
		List<String> result = new ArrayList<String>();
		if (isIncludes(str, start) && isIncludes(str, end)) {
			while ((e = str.indexOf(start, s)) >= 0) {
				result.add(str.substring(s, e));
				s = str.indexOf(end, e) + end.length();
				result.add(str.substring(e, s));
			}
			if (s < str.length())
				result.add(str.substring(s));

			if (s == 0)
				result.add(str);
		} else
			result.add(str);

		return result;
	}

	/**
	 * 首字母大写
	 * 
	 * @param string
	 * @return
	 */
	public static String upperFrist(String string) {
		if (string == null)
			return null;
		String upper = string.toUpperCase();
		return upper.substring(0, 1) + string.substring(1);
	}

	/**
	 * 首字母小写
	 * 
	 * @param string
	 * @return
	 */
	public static String lowerFrist(String string) {
		if (string == null)
			return null;
		String lower = string.toLowerCase();
		return lower.substring(0, 1) + string.substring(1);
	}

	
	/**
	 * 
	 * 对URL编码
	 * 
	 * @param string
	 * @param encode
	 * @return
	 */
	public static String URLEncode(String string, String encode) {
		try {
			return URLEncoder.encode(string, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将一个日期类型的对象，转换为指定格式的字符串
	 * 
	 * @param date
	 *            待转换的日期
	 * @param format
	 *            转换为字符串的相应格式 例如：DateToStr(new Date() ,"yyyy.MM.dd G 'at'
	 *            hh:mm:ss a zzz");
	 * @return 一个字符串
	 *         <p>
	 */
	public static String DateToStr(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	/**
	 * 功能： 字符串加密
	 * 
	 * @param strInput
	 *            加密字符串
	 * 
	 * @return String strOutput 加密后字符串
	 * @throws Exception
	 */
	public static String stringEncryptMD5(String strInput) throws Exception {

		String strOutput = "";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(strInput.getBytes());
		byte b[] = md.digest();
		for (int i = 0; i < b.length; i++) {
			char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			char[] ob = new char[2];
			ob[0] = digit[(b[i] >>> 4) & 0X0F];
			ob[1] = digit[b[i] & 0X0F];
			strOutput += new String(ob);
		}
		strOutput = new BASE64Encoder().encode(strOutput.getBytes());
		strOutput = strOutput.substring(0, strOutput.length() - 1);
		return strOutput;
	}
	
	public static boolean isEmpty(String str){
		return null==str || "".equals(str)||"null".equals(str.toLowerCase());
	}
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	/**
	 * 更改字符串编码
	 * @param str
	 * @param newCharset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
			  
		  if (str != null) {
		   //用默认字符编码解码字符串。
		   byte[] bs = str.getBytes();
		   //用新的字符编码生成字符串
		   return new String(bs, newCharset);
		  }
		  return null;
		 }
	
	
	/**
	 * 
	 * @Description:  将字符串格式化为指定格式  例如   321--》 321.00
	 * @param str  原始字符串
	 * @param nullSymbol   为null时返回的字符串  例如 -
	 * @param identity  分隔符  例如 .
	 * @return
	 * @throws Exception
	 *
	 */
	public static String formatDoubleString(String str,String nullSymbol,String identity) {
		if (str==null) {
			return nullSymbol;
		}else {
			String [] strArray=str.trim().split(identity);
			if (strArray.length==1) {
				return str+".00";
			}else if (strArray.length==2) {
				if (strArray[1].length()==1) {
					return str+"0";
				}
			}
		}
		return str;
	}
	
	
	
	/**
	 * 
	 * @Description:  将字符串格式化为指定格式  例如   321--》 321.00
	 * @param str  原始字符串
	 * @param nullSymbol   为null时返回的字符串  例如 -
	 * @param identity  分隔符  例如 .
	 * @return
	 * @throws Exception
	 *
	 */
	public static String formatDoubleStringTwo(String str,String nullSymbol,String identity,String flag) {
		if (str==null) {
			if (flag==null) {
				return nullSymbol;
			}else{
				return "0.00";
			}
			
		}else {
			String [] strArray=str.trim().split(identity);
			if (strArray.length==1) {
				return str+".00";
			}else if (strArray.length==2) {
				if (strArray[1].length()==1) {
					return str+"0";
				}
			}
		}
		return str;
	}
	
  
	
  }


