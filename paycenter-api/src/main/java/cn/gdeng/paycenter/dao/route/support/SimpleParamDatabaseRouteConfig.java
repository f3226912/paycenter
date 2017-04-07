package cn.gdeng.paycenter.dao.route.support;

import java.util.Map;

import javax.sql.DataSource;

import com.gudeng.framework.dba.route.DatabaseRouteConfig;

/** 简单参数配置数据源的路由。
 * @author wjguo
 * data 2016年7月25日 上午11:57:49 
 *
 */
public class SimpleParamDatabaseRouteConfig implements DatabaseRouteConfig{
	/**默认的参数key
	 * 
	 */
	public static final String DEFAULT_PARAMKEY = "dbResource";
	/**引用的数据源集合
	 * 
	 */
	private Map<String, DataSource> referDataSources;
	/**默认数据源
	 *  
	 */
	private DataSource defaultDataSource;
	
	@Override
	public DataSource route(Object paramObject) {
		if (this.referDataSources.size() > 0  && paramObject != null ) {
			if (paramObject instanceof Map) {
				Object dbSrouceFlag =  ((Map<?, ?>)paramObject).get(DEFAULT_PARAMKEY);
				if (dbSrouceFlag != null) {
					DataSource dataSource = referDataSources.get(dbSrouceFlag.toString());
					if (dataSource != null) {
						return dataSource;
					}
				}
				
			}
		}
		return defaultDataSource;
	}

	public void setReferDataSources(Map<String, DataSource> referDataSources) {
		this.referDataSources = referDataSources;
	}

	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}
}
