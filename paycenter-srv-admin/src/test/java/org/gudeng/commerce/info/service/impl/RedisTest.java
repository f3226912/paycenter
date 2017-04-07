package org.gudeng.commerce.info.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static void main(String[] args) throws InterruptedException{
//		 for(int i = 0 ; i < 15;i++){//15
//			
//		 }
		 new RedisTest().read();
//		try {
//			new RedisTest().pooTest();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
	}
	public  void pooTest() throws Exception{
		RoundRobinJedisPool jodisPool = RoundRobinJedisPool.create().curatorClient("10.17.1.215:2181,10.17.1.216:2181,10.17.1.217:2181", 5000)
                .zkProxyDir("/zk/codis/db_test/proxy").build();
		int i = 0;
		while(true){
			i++;
			try {
				Jedis jedis = jodisPool.getResource();
				//jedis.close();
				System.out.println(i);
			} catch (Exception e) {
				logger.error("获取:"+i,e);
				return;
			}
		}
	}
	
	

	public  void read(){
		 RoundRobinJedisPool jodisPool =  RoundRobinJedisPool.create()
				.curatorClient("10.17.1.215:2181,10.17.1.216:2181,10.17.1.217:2181", 5000)
                .zkProxyDir("/zk/codis/db_test/proxy").build();
		 Jedis jedis = null;
			Pipeline pipline = null;
		
	        	jedis = jodisPool.getResource();
	        	pipline = jedis.pipelined();
	        	long id=40000000000000000L;
	        	
	        		pipline.get((id++)+"");
	        		//jedis.get((id++)+"");
	        		if(id%10==0){
	        			pipline.sync();
	        			//pipline.close();
	        			//jedis.close();
	        			//jedis = jodisPool.getResource();
	        			//pipline = jedis.pipelined();
	        		}
	}
	
}
