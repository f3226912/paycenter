package cn.gdeng.paycenter.util.server.jodis;

import redis.clients.jedis.Jedis;

/** redis回调接口
 * @author wjguo
 * datetime 2016年9月13日 下午5:27:09  
 *
 */
public interface RedisCallBack {
	/**调用方法。
	 * @param jedis
	 */
	public Object invoke(Jedis jedis);
}
