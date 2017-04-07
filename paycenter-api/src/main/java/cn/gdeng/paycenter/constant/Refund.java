package cn.gdeng.paycenter.constant;

public interface Refund {

	public interface RefundStatus {

		String READ_REFUND = "1";//1待退款 2已退款 3退款失败 
		
		String REFUNDED = "2";
		
		String CLEARED = "4";//4已清算
	}
	
	public interface RefundType {

		String SOURCE_RETURN = "1"; //原路返回
		
		String CLEAR = "2";//清算
	}
	
	interface ClearStatus{
		String READ_CLEAR = "0"; //1已清算 0未清算 2已退款
		
		String CLEARED="1";
		
		String REFUNDED="2";
	}
	
	interface FeeType{
		//1赔付卖家违约金 2赔付平台违约金 3赔付物流违约金
		String SELLER = "1";
		
		String PLATFORM = "2";
		
		String LOGISTICS = "3";
	}

}
