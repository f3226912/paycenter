package cn.gdeng.paycenter.util.web.api;

import java.util.UUID;

/**
 * 
 * 主键生成器
 * 
 *
 */
public class IdCreater {
	
	/**
	 * 使用UUID字符串作为主键
	 * @return
	 */
	public static String newId(){
		String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		return id;
	}
	
//	/**
//	 * 创建申请码。格式：apply_code_申请者用户名_同伴用户名_时间
//	 * @param username 申请者用户名
//	 * @param friendUsername 同伴用户名
//	 * @return
//	 */
//	public static String newApplyCode(String username, String friendUsername){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
//		String dateStr = sdf.format(new Date());
//		String applyCode = "apply_code_"+username+"_"+friendUsername+"_"+dateStr;
//		return applyCode;
//	}
}
