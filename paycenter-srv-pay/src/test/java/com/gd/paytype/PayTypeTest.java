package com.gd.paytype;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring.xml", "classpath*:spring-da.xml", "classpath*:spring-res.xml" }, inheritLocations=false)
public class PayTypeTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private BaseDao<?> baseDao;

	@Test
	@Rollback(false)
	public void test() throws Exception {
		PayTypeEntity e = new PayTypeEntity();
		e.setId(7);
		e = baseDao.find(PayTypeEntity.class, e);
		String sign = baseDao.getEntitySign(e);
		System.out.println("id : " + e.getId() + ", sign : " + sign);
		int successNum = baseDao.dynamicMerge(e);
		System.out.println("success? : " + (successNum >= 1 ? true : false));
		
		
	}

}
