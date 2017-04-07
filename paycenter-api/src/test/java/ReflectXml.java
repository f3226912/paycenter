

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import cn.gdeng.paycenter.entity.pay.PayTradeUidRecordEntity;
import cn.gdeng.paycenter.entity.pay.RefundFeeItemDetailEntity;
import cn.gdeng.paycenter.entity.pay.RefundRecordEntity;


/**根据实体映射xml文件
 * @author wjguo
 * datetime 2016年9月28日 下午2:25:10  
 *
 */
public class ReflectXml {

	/**实体属性名称集合
	 * 
	 */
	private List<String> entityFields = new ArrayList<String>();
	/**
	 * 数据库列名
	 */
	private List<String> dbColumns = new ArrayList<String>();

	private String tableName;
	
	private String idName;
	
	public Class<?> clazz;
	
	public ReflectXml (Class<?> clazz) throws Exception {
		this.clazz = clazz;
		getTableName();
		getClassId();
		getClassProperties();
	}

	/**获取表名称
	 * @throws Exception
	 */
	public void getTableName() throws Exception {
		Annotation a = this.clazz.getAnnotation(Entity.class);
		Object c = a.annotationType().getMethod("name", null).invoke(a, null);
		tableName = c.toString();
	}
	
	/**获取数据库id
	 * 
	 */
	public void getClassId() {
		java.lang.reflect.Method[] a = this.clazz.getMethods();

		for (int i = 0; i < a.length; i++) {
			if (a[i].getAnnotation(Id.class) != null) {
				String methodName = a[i].getName();
				methodName = methodName.substring(3, 4).toLowerCase()
						+ methodName.substring(4);
				idName = methodName;

			}
		}

	}

	/**获取实体名称和数据库列名称
	 * 
	 */
	public void getClassProperties() {
		java.lang.reflect.Method[] a = this.clazz.getMethods();

		for (int i = 0; i < a.length; i++) {
			if (a[i].getAnnotation(Column.class) != null) {
				String methodName = a[i].getName();
				methodName = methodName.substring(3, 4).toLowerCase()
						+ methodName.substring(4);
				entityFields.add(methodName);
				dbColumns.add(a[i].getAnnotation(Column.class).name());

			}
		}

	}

	/** 创建xml文件
	 * @return
	 */
	public String createXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n");
		sb.append("<sqlMap namespace=\"").append(clazz.getSimpleName().substring(0, clazz.getSimpleName().lastIndexOf("Entity"))).append("\"> \n");
		
		sb.append(createQuery()).append("\n");
		sb.append(createQueryPage()).append("\n");
		sb.append(createCount()).append("\n");
		
		sb.append("</sqlMap>");
		
		return sb.toString();
		
	}
	
	
	/** 创建查询的xml字符串
	 * @return
	 */
	private String createQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("	<!--根据条件查询--> \n");
		sb.append("	<sql id=\"queryByCondition\"> \n");
		sb.append("	   <![CDATA[ \n");
		sb.append("		  SELECT \n");
		
		sb.append(getColumnListStr());
		
		sb.append("\n		  FROM ").append(tableName);
		
		sb.append(getWhereStr());
		
		sb.append("	   ]]>\n");
		sb.append("	</sql>\n");
		return sb.toString();
	}
	
	/** 创建分页查询的xml字符串
	 * @return
	 */
	private String createQueryPage() {
		StringBuilder sb = new StringBuilder();
		sb.append("	<!--根据条件分页查询--> \n");
		sb.append("	<sql id=\"queryByConditionPage\"> \n");
		sb.append("	   <![CDATA[ \n");
		sb.append("		  SELECT \n");
		
		sb.append(getColumnListStr());
		
		sb.append("\n		  FROM ").append(tableName);
		
		sb.append(getWhereStr());
		sb.append("		  LIMIT :startRow,:endRow \n");
		sb.append("	   ]]>\n");
		sb.append("	</sql>\n");
		return sb.toString();
	}
	
	/** 创建统计查询的xml字符串
	 * @return
	 */
	private String createCount() {
		StringBuilder sb = new StringBuilder();
		sb.append("	<!--根据条件统计--> \n");
		sb.append("	<sql id=\"countByCondition\"> \n");
		sb.append("	   <![CDATA[ \n");
		sb.append("		  SELECT count(").append(idName).append(") \n");
		
		sb.append("		  FROM ").append(tableName);

		sb.append(getWhereStr());
		sb.append("	   ]]>\n");
		sb.append("	</sql>\n");
		return sb.toString();
	}
	
	/** 获取数据库表中所有列名的字符串，用逗号分隔。
	 * @return
	 */
	private String getColumnListStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("			");
		for (int i = 0; i < entityFields.size(); i++) {
			String columnName = dbColumns.get(i);
			if(i == entityFields.size() -1) {
				sb.append(columnName);
				break;
			}
			if((i != 0) && (i % 8 == 0)) {
				sb.append("\n			");
			}
			sb.append(columnName+",");
		}
		return sb.toString();
	}
	
	
	/**获取where条件的字符串
	 * @return
	 */
	private String getWhereStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n		  WHERE 1=1 \n");
		for (int i = 0; i < entityFields.size(); i++) {
			String fieldName = entityFields.get(i);
			String columnName = dbColumns.get(i);
			sb.append("			<#if "+fieldName+"?exists && "+fieldName+" != \"\" >\n");
			sb.append("			   AND " + columnName + "=:"+fieldName + "\n");
			sb.append("			</#if>\n");
		}
		return sb.toString();
	}
	
	
	
	/**demo
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ReflectXml reflect = new ReflectXml(RefundFeeItemDetailEntity.class);
		System.err.println(reflect.createXML());
	}
}
