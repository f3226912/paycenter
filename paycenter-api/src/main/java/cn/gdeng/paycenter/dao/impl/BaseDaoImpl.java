package cn.gdeng.paycenter.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.gudeng.framework.dba.client.DbaClient;
import com.gudeng.framework.page.QueryParam;
import com.gudeng.framework.page.QueryResult;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.server.sign.ConvertUtil;
import cn.gdeng.paycenter.util.server.sign.PrivateKeyUtil;
import cn.gdeng.paycenter.util.server.sign.RSASignature;
import cn.gdeng.paycenter.util.server.sign.SignMailUtil;
import cn.gdeng.paycenter.util.server.sign.SignUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;

/**
 * 功能描述： baseDao实现
 * @author 
 * @param <T> 操作泛型
 */

public class BaseDaoImpl<T> implements BaseDao<T> {

    public static final String    SQL_COUNT         = "count";

    public static final String    SQL_LIST         = "list";

    public static final String    SQL_LIST_BY_PAGE    = "listByPage";

    public static final String    SQL_INSERT         = "insert";

    public static final String    SQL_FIND_BY         = "findBy";

    public static final String    SQL_UPDATE         = "update";

    public static final String    SQL_DELETE         = "delete";

    @Autowired
    private DbaClient     dalClient;
    @Autowired
    private GdProperties     gdProperties;
        
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public Number persist(T t) {
        return dalClient.persist(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> E persist(Object entity, Class<E> requiredType) {
        return dalClient.persist(entity, requiredType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int merge(T t) {
        return dalClient.merge(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int remove(T t) {
        return dalClient.remove(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> E find(Class<E> entityClass, Object entity) {
        return dalClient.find(entityClass, entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T find(T t) {
        return (T) dalClient.find(t.getClass(), t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> E queryForObject(String sqlId, Map<String, ?> paramMap, Class<E> requiredType) {
        return dalClient.queryForObject(sqlId, paramMap, requiredType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> E queryForObject(String sqlId, Object param, Class<E> requiredType) {
        return dalClient.queryForObject(sqlId, param, requiredType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> queryForMap(String sqlId, Map<String, ?> paramMap) {

        return dalClient.queryForMap(sqlId, paramMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> queryForMap(String sqlId, Object param) {

        return dalClient.queryForMap(sqlId, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> queryForList(String sqlId, Map<String, ?> paramMap, Class<E> elementType) {
        return dalClient.queryForList(sqlId, paramMap, elementType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> queryForList(String sqlId, Object param, Class<E> requiredType) {
        return dalClient.queryForList(sqlId, param, requiredType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> queryForList(String sqlId, Map<String, ?> paramMap) {
        return dalClient.queryForList(sqlId, paramMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> queryForList(String sqlId, Object param) {
        return dalClient.queryForList(sqlId, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int execute(String sqlId, Map<String, ?> paramMap) {
        return dalClient.execute(sqlId, paramMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int execute(String sqlId, Object param) {
        return dalClient.execute(sqlId, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] batchUpdate(String sqlId, Map<String, Object>[] batchValues) {
        return dalClient.batchUpdate(sqlId, batchValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int dynamicMerge(Object entity) {
        return dalClient.dynamicMerge(entity);
    }

    /**
     * <p>
     * 分页查询
     * </p>
     * <p>
     * 部分限制：查询条数的sql：查询结果需要以count为别名
     * </p>
     * @param countSqlId 查询总条数的SQL_ID
     * @param sqlId 查询内容的SQL_ID
     * @param param 参数集合
     * @param requiredType 返回类型
     * @return requiredType类型的查询结果
     */
    public final <E> QueryResult<E> pageQuery(String countSqlId, String pageSqlId, 
            QueryParam<Map<String, Object>> param, Class<E> requiredType) {

            QueryResult<E> result = null;

            if (param != null) {
                int totalCount = 0;
                Map<String, Object> map = queryForMap(countSqlId, param.getQueryParam());
                if (map != null) {
                    totalCount = Integer.valueOf(String.valueOf(map.get("totalRows"))).intValue();
                }

                // 把检索参数置回返回参数中
                result = new QueryResult<E>(totalCount, param.getPageSize(), param.getPageNumber());

                param.getQueryParam().put("totalRows", totalCount);//总行数
                // 如果总数大于0，继续查询
                if (totalCount != 0) {
                    // 设置检索开始行
                    param.getQueryParam().put("startIndex", result.getIndexNumber());
                    // 设置检索数据件数
                    param.getQueryParam().put("maxCount", result.getPageSize());

                    // 检索数据列表，放到返回结果对象中
                    List<E> retList = queryForList(pageSqlId, param.getQueryParam(), requiredType);

                    result.setDatas(retList);
                }
            }
            return result;
        }

    /**
     * {@inheritDoc}
     */
	@Override
	public Long persistBySign(Object entity) throws Exception{
		Long id = dalClient.persist(entity, Long.class);
		Map<String, Object> entityMap = ConvertUtil.objectToMap(entity);
		entityMap.put("id", id);
		String sign = RSASignature.sign(ConvertUtil.getMapValues(entityMap),PrivateKeyUtil.getPrivateKey(gdProperties.rsaSignKeyPath()));
		dalClient.dynamicMerge(SignUtil.getSignDto(entity,id,sign));
		return id;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public String getEntitySign(Object entity) throws Exception {
		return RSASignature.sign(ConvertUtil.getMapValues(ConvertUtil.objectToMap(entity)),PrivateKeyUtil.getPrivateKey(gdProperties.rsaSignKeyPath()));
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public String getEntitySignByKey(Object entity) throws Exception {
		Object obj = dalClient.find(entity.getClass(), entity);
		if(null == obj){
			logger.error(MsgCons.M_60004+":\n"+JSON.toJSONString(entity));
			throw new BizException(MsgCons.C_60004, MsgCons.M_60004);
		}
		return RSASignature.sign(ConvertUtil.getMapValues(ConvertUtil.objectToMap(obj)),PrivateKeyUtil.getPrivateKey(gdProperties.rsaSignKeyPath()));
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public Boolean validateSign(Object entity) throws Exception {
		if(!gdProperties.signOnOff()){
			return true;
		}
		Object obj = dalClient.find(entity.getClass(), entity);
		if(null == obj){
			//insertSignError("",JSON.toJSONString(entity), MsgCons.M_60004);
			logger.error(MsgCons.M_60004+":\n"+JSON.toJSONString(entity));
			throw new BizException(MsgCons.C_60004, MsgCons.M_60004);
		}
		Map<String, Object> entityMap =  ConvertUtil.objectToMap(obj);
		if(!entityMap.containsKey("sign")
				|| null == entityMap.get("sign")){
			insertSignError(entity.getClass().getName(),JSON.toJSONString(obj), MsgCons.M_60005);
			logger.error(MsgCons.M_60005+":\n"+JSON.toJSONString(entity));
			throw new BizException(MsgCons.C_60005, MsgCons.M_60005);
		}
		String sign = (String)entityMap.get("sign");
		String publicKey = PrivateKeyUtil.getPublicKey(gdProperties.rsaSignKeyPath());
		boolean flag = RSASignature.doCheck(ConvertUtil.getMapValues(entityMap), sign,publicKey);
		if(false == flag){
			insertSignError(entity.getClass().getName(),JSON.toJSONString(obj)+" sign="+sign, MsgCons.M_60003);
			logger.error(MsgCons.M_60003+":\n"+JSON.toJSONString(entity));
			//throw new BizException(MsgCons.C_60003, MsgCons.M_60003);
		}
		return flag;
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public Boolean validateSign(Object entity, Class<?> clazz) throws Exception {
		if(!gdProperties.signOnOff()){
			return true;
		}
		if(null == entity){
			//insertSignError("",JSON.toJSONString(entity), MsgCons.M_60004);
			logger.error(MsgCons.M_60004+":\n"+JSON.toJSONString(entity));
			throw new BizException(MsgCons.C_60004, MsgCons.M_60004);
		}
		Map<String, Object> entityMap =  ConvertUtil.objectToMap(entity);
		if(!entityMap.containsKey("sign")
				|| null == entityMap.get("sign")){
			insertSignError(entity.getClass().getName(),JSON.toJSONString(entity), MsgCons.M_60005);
			logger.error(MsgCons.M_60005+":\n"+JSON.toJSONString(entity));
			throw new BizException(MsgCons.C_60005, MsgCons.M_60005);
		}
		String sign = (String)entityMap.get("sign");
		String publicKey = PrivateKeyUtil.getPublicKey(gdProperties.rsaSignKeyPath());
		boolean flag = RSASignature.doCheck(ConvertUtil.getMapValues(entityMap), sign,publicKey);
		if(false == flag){
			insertSignError(entity.getClass().getName(),JSON.toJSONString(entity)+" sign="+sign, MsgCons.M_60003);
			logger.error(MsgCons.M_60003+":\n"+JSON.toJSONString(entity));
			//throw new BizException(MsgCons.C_60003, MsgCons.M_60003);
		}
		return flag;
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public List<Long> batchValidateSign(List<Long> ids, Class<?> clazz) throws Exception {
		if(!gdProperties.signOnOff()){
			return new ArrayList<Long>();
		}
		if(null == clazz){
			logger.error(MsgCons.M_60001+":\n"+JSON.toJSONString(ids));
			throw new BizException(MsgCons.C_60001, MsgCons.M_60001);
		}
		if(null == ids
				|| ids.size() < 1){
			logger.error(MsgCons.M_60004+":\n"+JSON.toJSONString(clazz));
			throw new BizException(MsgCons.C_60004, MsgCons.M_60004);
		}
		Map<String,Object> tableMap = new HashMap<String,Object>();
		String tableName = SignUtil.getEntityTableName(clazz.newInstance());
		tableMap.put("tableName", tableName);
		tableMap.put("idList", ids);
		
		List<Map<String,Object>> list = dalClient.queryForList("sign_map.queryListByIds", tableMap);
		if(null == list
				|| list.size() < 1){
			insertSignError(clazz.getName(),JSON.toJSONString(ids),MsgCons.M_60004);
			logger.error(MsgCons.M_60004+":\n"+JSON.toJSONString(ids)+clazz.getName());
			throw new BizException(MsgCons.C_60004, MsgCons.M_60004);
		}
		List<Long> errorIds = new ArrayList<Long>();
		String publicKey = PrivateKeyUtil.getPublicKey(gdProperties.rsaSignKeyPath());
		for(Map<String,Object> map : list){
			String sign = (String)map.get("sign");
			boolean flag = RSASignature.doCheck(ConvertUtil.getMapValues(map), sign,publicKey);
			if(!flag){
				if(map.get("id") instanceof Integer){
					errorIds.add(((Integer) map.get("id")).longValue());
				}else{
					errorIds.add((Long)map.get("id"));
				}
				insertSignError(clazz.getName(),JSON.toJSONString(map)+"sign="+sign,MsgCons.M_60003);
			}
		}
		return errorIds;
	}

	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	@Override
	public int batchSign(String uuid, Class<?> clazz) throws Exception {
		if(!gdProperties.signOnOff()){
			return 0;
		}
		if(null == uuid
				|| "".equals(uuid)){
			logger.error(MsgCons.M_60006+":\n"+JSON.toJSONString(clazz));
			throw new BizException(MsgCons.C_60006, MsgCons.M_60006);
		}
		Map<String,Object> tableMap = new HashMap<String,Object>();
		String tableName = SignUtil.getEntityTableName(clazz.newInstance());
		tableMap.put("tableName", tableName);
		tableMap.put("sign", uuid);
		
		List<Map<String,Object>> list = dalClient.queryForList("sign_map.queryListByUUID", tableMap);
		if(null == list
				|| list.size() < 1){
			return 0;
		}
		
		int index = 0;
		String privateKey = PrivateKeyUtil.getPrivateKey(gdProperties.rsaSignKeyPath());
		Map<String,Object>[] mapAray = new HashMap[list.size()];
		for(Map<String,Object> map : list){
			String sign = RSASignature.sign(ConvertUtil.getMapValues(map),privateKey);
			Map<String,Object> rowMap = new HashMap<String,Object>(); 
			rowMap.put("sign", sign);
			rowMap.put("id", map.get("id"));
			rowMap.put("tableName", tableName);
			mapAray[index++] = rowMap;
		}
		return dalClient.batchUpdate("sign_map.updateSignByUUID", mapAray).length;
	}
	
	/**
	 * 签名验证异常记录
	 * @param tableName
	 * @param content
	 * @param error
	 */
	private void insertSignError(String tableName, String content, String error){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName", tableName);
		map.put("content", content);
		map.put("error", error);
		map.put("bizType", 1);
		dalClient.execute("sign_map.insertSignError", map);
		SignMailUtil.sendMail("业务数据签名验证失败", map.toString());
	}
}

