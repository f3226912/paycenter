package cn.gdeng.paycenter.admin.enums;
public class AlidayuEnum {
	public static enum templateCode{
		
		TEMPLATECODE1("SMS_7811374"),//找回密码
		TEMPLATECODE2("SMS_7801370"),//WEB端注册验证码
		TEMPLATECODE3("SMS_13595066"),//手机端注册验证码
		TEMPLATECODE4("SMS_7895243"),//商户添加用户短信通知
		TEMPLATECODE5("SMS_7955032"),//修改支付密码验证码 
		TEMPLATECODE6("SMS_13275206"),//验证旧手机
		TEMPLATECODE7("SMS_13350131"),//确认新手机
		TEMPLATECODE8("SMS_28195021"),//支付中心登录短信验证
		TEMPLATECODE9("SMS_28080035"),//支付中心修改对账短信验证
		;
		private String _value;
		public String value()
	    {
	      return _value;
	    }
		private templateCode(String value){
			this._value=value;
		}
	}
public static enum FreeSignName{
		FREESIGNNAME1("谷登科技")//短信签名
		;
		private String _value;
		public String value()
	    {
	      return _value;
	    }
		private FreeSignName(String value){
			this._value=value;
		}
	}
}
