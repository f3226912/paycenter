package cn.gdeng.paycenter.util.server.sign;



import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.enums.MsgCons;
/**
 * 获取签名DTO对象，自动设置表名
 * 获取私钥
 * @author zhangnf20161110
 *
 */
public class SignUtil {
	private static Logger logger = LoggerFactory.getLogger(SignUtil.class);
	public static void main(String args[]) throws Exception{
		getEntityTableName(new FeeRecordEntity());
	}

	/**
	 * 获取实体对象表名
	 * @param obj 实体对象
	 * @return DB表名
	 */
	public static String getEntityTableName(Object obj) {
		Annotation annos[] = obj.getClass().getAnnotations();
		for (Annotation a : annos) {
			if (a.annotationType().equals(Entity.class)) {
				String annoStr = a.toString();
				annoStr = annoStr.substring(annoStr.indexOf("=") + 1, annoStr.length() - 1);
				logger.debug("\n entity and table name is-->"+obj.getClass().getName()+" :"+annoStr);
				return annoStr;
			}
		}
		return null;
	}
		
	public static String setEntityTableName(Object obj, String tableName) {
		Annotation annos[] = obj.getClass().getAnnotations();
		for (Annotation a : annos) {
			if (a.annotationType().equals(Entity.class)) {
				String annoStr = a.toString();
				annoStr = annoStr.substring(annoStr.indexOf("=") + 1, annoStr.length() - 1);
				logger.debug("\n entity and table name is-->"+obj.getClass().getName()+" :"+annoStr);
				return annoStr;
			}
		}
		return null;
	}
	
	/**
	 * 获取签名DTO对象，已自动设置表名
	 * @param obj 实体对象
	 * @return SignDto
	 */
	public static Object getSignDto(Object obj,Long id, String sign) throws Exception{
		if(null == obj){
			throw new BizException(MsgCons.C_60001, MsgCons.M_60001);
		}
		String tableName = getEntityTableName(obj);
		if(null == tableName){
			throw new BizException(MsgCons.C_60002, MsgCons.M_60002);
		}
		Object dto = ClassPoolUtils.tableMapping(SignDto.class.getName(),tableName).newInstance();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("id", id);
		map.put("sign", sign);
		ReflectValue.setFieldValue(map, dto);
		return dto;
	}
}