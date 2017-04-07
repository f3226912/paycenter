package cn.gdeng.paycenter.util.server.sign;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignTableUtil {
	private static Logger logger = LoggerFactory.getLogger(SignTableUtil.class);
	private static final String SIGN_FILE="sign.properties" ;
	private static final String RE_SIGN_TABLES="re.sign.tables" ;
	private static final String SIGN_ON_OFF="sign.on.off" ;
	private static final String SIGN_BATCH_ON_OFF="sign.batch.validate.on.off" ;

	public static List<String> getReSignTables(String filePath) {
		List<String> list = new ArrayList<String>();
		try {
			String tableStr = getPropertie(filePath,RE_SIGN_TABLES);
			if(null == tableStr
					|| "".equals(tableStr)){
				return list;
			}
			String[] tableArray = tableStr.split(",");
			for(String table : tableArray){
				if(null == table
						|| "".equals(table.trim())){
					continue;
				}
				list.add(table.trim());
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return list;
	}
	
	public static boolean getSignOnOff(String filePath) {
		try {
			String onOff = getPropertie(filePath,SIGN_ON_OFF);
			if(null == onOff
					|| "".equals(onOff)){
				return false;
			}
			if(onOff.trim().equals("true")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return false;
	}
	
	public static boolean getSignValOnOff(String filePath) {
		try {
			String onOff = getPropertie(filePath,SIGN_BATCH_ON_OFF);
			if(null == onOff
					|| "".equals(onOff)){
				return false;
			}
			if(onOff.trim().equals("true")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	private static String getPropertie(String filePath, String key) throws Exception {
		if(!new File(filePath+SIGN_FILE).exists()){
			return null;
		}
		Properties pps = new Properties();
		pps.load(new FileInputStream(filePath+SIGN_FILE));
		Enumeration enum1 = pps.propertyNames();
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			if(strKey.equals(key)){
				return pps.getProperty(strKey);
			}
		}
		return null;
	}
}
