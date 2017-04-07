package cn.gdeng.paycenter.dao;

import java.util.List;
import java.util.Map;

import com.gudeng.framework.page.QueryParam;
import com.gudeng.framework.page.QueryResult;

/**
 * 功能描述： baseDao
 * 
 * @param <T>
 *            操作泛型
 */

public interface BaseDao<T> {
	/**
	 * 
	 * 功能描述:以下是实现对T表插入数据的功能
	 * 
	 * @param 将一行表数据存在T中
	 *            ，作为参数
	 * @return 执行结果，类型 是Boolean
	 */
	Number persist(T t);

	/**
	 * 根据传入主键类型requiredType，返回主键值<br>
	 * 非自增长主键，如果传入类型与主键类型不符，会抛出ClassCastException<br>
	 * 自增长主键，必须传入类型为Number类型
	 */
	<E> E persist(Object entity, Class<E> requiredType);

	/**
	 * 
	 * 功能描述：以下是实现数据的更新功能，根据T表中id，对那一行的数据进行更新
	 * 
	 * @param 将那一行数据存在T中
	 *            （其中有更新内容）作为参数
	 * @return 执行结果，类型 是Boolean
	 */
	int merge(T t);

	/** 动态更新 */
	int dynamicMerge(Object entity);

	/**
	 * //成功返回主键
	 * 
	 * @param t
	 * @return
	 */
	int remove(T t);

	<E> E find(Class<E> entityClass, Object entity);

	/**
	 * //成功返回主键
	 * 
	 * @param t
	 * @return
	 */
	T find(T t);

	/** 根据sqlId查询单个对象，返回requiredType类型对象，不需要强转，查不到或查询多个返回第一个 */
	<E> E queryForObject(String sqlId, Map<String, ?> paramMap, Class<E> requiredType);

	/** 根据sqlId查询单个对象，返回requiredType类型对象，不需要强转，查不到或查询多个返回第一个 */
	<E> E queryForObject(String sqlId, Object param, Class<E> requiredType);

	/** 根据sqlId查询单个对象，返回Map集合，key是数据库字段 ，查不到或查询多个返回第一个 */
	Map<String, Object> queryForMap(String sqlId, Map<String, ?> paramMap);

	/** 根据sqlId查询单个对象，返回Map集合，key是数据库字段 ，查不到或查询多个返回第一个 */
	Map<String, Object> queryForMap(String sqlId, Object param);

	/** 根据sqlId查询多个对象，返回requiredType类型对象List集合，不需要强转 */
	<E> List<E> queryForList(String sqlId, Map<String, ?> paramMap, Class<E> elementType);

	/** 根据sqlId查询多个对象，返回requiredType类型对象List集合，不需要强转 */
	<E> List<E> queryForList(String sqlId, Object param, Class<E> requiredType);

	/** 根据sqlId查询，返回Map集合List，key是数据库字段 */
	List<Map<String, Object>> queryForList(String sqlId, Map<String, ?> paramMap);

	/** 根据sqlId查询，返回Map集合List，key是数据库字段 */
	List<Map<String, Object>> queryForList(String sqlId, Object param);

	/** 根据sqlId执行，返回执行成功的记录条数 */
	int execute(String sqlId, Map<String, ?> paramMap);

	/** 根据sqlId执行，返回执行成功的记录条数 */
	int execute(String sqlId, Object param);

	/** 根据sqlId执行，批量执行 */
	int[] batchUpdate(String sqlId, Map<String, Object>[] batchValues);

	/**
	 * 功能描述: 分页查询
	 * 
	 * @param param
	 *            ：查询参数
	 * @return 查询结果集
	 */
	<E> QueryResult<E> pageQuery(String countSqlId, String pageSqlId, QueryParam<Map<String, Object>> param, Class<E> requiredType);

	/**
	 * 插入数据自动添加数字签名
	 * 适用于实体对象插入数据，无需拼写insert into语句
	 * @param entity 实体对象
	 * @return 返回主键ID
	 */
	Long persistBySign(Object entity) throws Exception;
	/**
	 * 获取对象签名
	 * 适用于手动拼写insert into语句插入数据
	 * @param entity 完整实体对象
	 * @return 返回签名
	 * @throws Exception
	 */
	String getEntitySign(Object entity) throws Exception;
	/**
	 * 获取对象签名
	 * 适用于手动拼写insert into语句插入数据
	 * @param entity 部分实体对象（请设置主键值，其他值无需设置）
	 * @return 返回签名
	 * @throws Exception
	 */
	String getEntitySignByKey(Object entity) throws Exception;
	
	/**
	 * 验证签名
	 * @param entity 实体对象(请设置主键值，其他值无需设置)
	 * @return 返回验证通过true 不通过false
	 * @throws Exception
	 */
	Boolean validateSign(Object entity) throws Exception;
	
	/**
	 * 验证签名
	 * @param entity 完整实体对象实例
	 * @param clazz 实体类
	 * @return 返回验证通过true 不通过false
	 * @throws Exception
	 */
	Boolean validateSign(Object entity, Class<?> clazz) throws Exception;
	
	/**
	 * 批量验证签名
	 * @param ids 主键ID列表
	 * @param clazz 实体类
	 * @return List<Long> 验证不通过ID列表
	 * @throws Exception
	 */
	List<Long> batchValidateSign(List<Long> ids, Class<?> clazz) throws Exception;

	/**
	 * 批量签名
	 * @param uuid 
	 * @param clazz 实体对象
	 * 请将UUID存入需要签名的sign字段
	 * 会扫描所有sign为UUID的数据，并进行重新签名
	 * @return 更新行数
	 * @throws Exception
	 */
	int batchSign(String uuid, Class<?> clazz) throws Exception;
}
