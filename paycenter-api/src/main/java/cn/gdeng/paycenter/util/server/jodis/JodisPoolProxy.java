package cn.gdeng.paycenter.util.server.jodis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jodis连接池配置
 * 
 * @author zhangnf
 *
 */
public class JodisPoolProxy {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static RoundRobinJedisPool roundRobinJedisPool;

	public JodisPoolProxy(JedisPoolConfig jedisPoolConfig, String zkAddr, int zkSessionTimeoutMs, int timeoutMs,
			String zkProxyDir) {
		roundRobinJedisPool = RoundRobinJedisPool.create().poolConfig(jedisPoolConfig).timeoutMs(timeoutMs)
				.zkProxyDir(zkProxyDir).curatorClient(zkAddr, zkSessionTimeoutMs).build();
	}

	public Jedis getJedis() {
		try {
			Jedis jedis = roundRobinJedisPool.getResource();
			if (jedis.isConnected()) {
				return jedis;
			}
		} catch (Exception e) {
			logger.error("get jedis first time failer*****************************************");
		}

		try {
			Jedis jedis = roundRobinJedisPool.getResource();
			if (jedis.isConnected()) {
				return jedis;
			}
		} catch (Exception e) {
			logger.error("get jedis second time failer*****************************************");
		}

		try {
			Jedis jedis = roundRobinJedisPool.getResource();
			if (jedis.isConnected()) {
				return jedis;
			}
		} catch (Exception e) {
			logger.error("get jedis third time failer*****************************************");
			throw new RuntimeException(e);
		}
		return null;
	}
}
