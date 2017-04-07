import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.gudeng.commerce.gd.order.dto.OrderBillDTO;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.order.OrderBillHessianService;
import cn.gdeng.paycenter.api.server.order.OrderbaseInfoHessianService;
import cn.gdeng.paycenter.dto.pos.OrderBaseinfoHessianDto;
import cn.gdeng.paycenter.dto.pos.OrderBillHessianDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class BaseinfoServiceTest {

	@Resource
	private OrderBillHessianService orderBillHessianService;
	
	@Resource
	private OrderbaseInfoHessianService orderbaseInfoHessianService;
	
	@Test
	public void test() throws BizException, ParseException{
		OrderBillHessianDto dto = new OrderBillHessianDto();
		dto.setClientNo("1201H829");
		String d1 = "2015-12-26 15:40:52";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dto.setBillBeginTime(sdf.parse(d1));
		List<OrderBillHessianDto> list = orderBillHessianService.getOrderBillByCondition(dto);
		System.out.println("****************");
		System.out.println(JSON.toJSON(list));
		System.out.println("****************");
		
	}
	
	@Test
	public void test获取商品名称() throws BizException, ParseException{
		String orderNo="112016000000662";
		
		System.out.println("****************");
		String name = orderbaseInfoHessianService.getProductName(orderNo);
		System.out.println(name);
		System.out.println("****************");
		
	}
	
	@Test
	public void test获取订单() throws BizException, ParseException{
		String orderNo="112016000000662";
		
		System.out.println("****************");
		OrderBaseinfoHessianDto dto = orderbaseInfoHessianService.getByOrderNo(orderNo);
		System.out.println(JSON.toJSON(dto));
		System.out.println("****************");
		
	}
}
