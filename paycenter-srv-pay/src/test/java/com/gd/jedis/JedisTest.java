package com.gd.jedis;



import org.junit.Test;

import cn.gdeng.paycenter.util.web.api.SerializeUtil;
import redis.clients.jedis.Jedis;

public class JedisTest {
	
	@Test
	public void test(){
		Jedis jedis = new Jedis("10.17.1.166");
		System.out.println(jedis.ping());
		jedis.set("key1", "111");
		Object obj = jedis.get("PAYCENTER_THIRD_PAY_CONFIG_MAP_KEY".getBytes());
		System.out.println(obj);
		System.out.println(jedis.exists("key1"));
		System.out.println(jedis.get("key1"));
		jedis.hset("key2", "a", "1");
		jedis.hset("user:1000:password", "b", "2");
	}
}
