package cn.gdeng.paycenter.util.server.jodis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** redis模板接口类
 * @author wjguo
 * datetime 2016年9月14日 上午10:59:03  
 *
 */
public interface RedisTemplateInterface {
	public String set(String key, String value);
	
	public String set(String key, String value, String nxxx, String expx, long time);
	
	public String get(String key);
	
	public Long exists(String... keys);
	
	public Boolean exists(String key);
	
	public Long del(String... keys);
	
	public Long del(String key);
	
	public String type(String key);
	
	public Set<String> keys(String pattern);
	
	public Long expire(String key, int seconds);
	
	public String setex(String key, int seconds, String value);
	
	public String set(byte[] key, byte[] value);
	
	public byte[] get(byte[] key);
	
	public Long hset(String key, String field, String value);
	
	public String hget(String key, String field);
	
	public String hmset(String key, Map<String, String> hash);
	
	public List<String> hmget(String key, String... fields);
	
	public Long hincrBy(String key, String field, long value);
	
	/** 执行回调接口实现类。
	 * @param callback
	 */
	public <T> T exec(RedisCallBack callback);
}
