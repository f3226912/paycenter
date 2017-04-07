package cn.gdeng.paycenter.constant;

public interface SubPayType {

	interface ALIPAY{
		
		String PARTNER = "1";//合作伙伴
		String OPENER = "2";//开放平台
	}
	
	interface WECHAT{
		public static final String APP = "1";//APP支付
	}
}
