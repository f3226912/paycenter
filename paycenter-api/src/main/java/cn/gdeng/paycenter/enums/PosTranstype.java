package cn.gdeng.paycenter.enums;

public interface PosTranstype {

	interface Nanning{
		String POSITIVE = "0"; //正向刷卡（订单支付）
		
		String REVERSE = "1"; //刷卡消费
	}
}
