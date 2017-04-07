package cn.gdeng.paycenter.enums;

/**
 * 消息返回code msg定义
 * @author zhangnf
 *错误码定义成5位数
 *10000定义为成功消息
 *20000定义为失败消息
 *30000定义为登录相关消息
 *50000定义成服务器异常消息
 *
 */
public class MsgCons {

	public static final Integer C_10000 = 10000;
	public static final String M_10000 = "成功";
	
	
	public static final Integer C_20000 = 20000;
	public static final String M_20000 = "失败";
	
	public static final Integer C_30000 = 30000;
	public static final String M_30000 = "登录超时";
	
	public static final Integer C_50000 = 50000;
	public static final String M_50000 = "服务异常";
	
	public static final Integer C_50001 = 50001;
	public static final String M_50001 = "服务不存在";
	
	public static final Integer C_60000 = 60000;
	public static final String M_60000 = "数据库层处理失败";
	
	public static final Integer C_60001 = 60001;
	public static final String M_60001 = "\n实体对象不能为空";
	
	public static final Integer C_60002 = 60002;
	public static final String M_60002 = "\n实体对象Entity name表名不能为空";
	
	public static final Integer C_60003 = 60003;
	public static final String M_60003 = "\n数字签名验证异常";
	
	public static final Integer C_60004 = 60004;
	public static final String M_60004 = "\n数据不存在";
	
	public static final Integer C_60005 = 60005;
	public static final String M_60005 = "\n签名不存在";
	
	public static final Integer C_60006 = 60006;
	public static final String M_60006 = "\nUUID不存在";
	
	public static final Integer C_20001=20001;
	public static final String M_20001="参数错误";
	
	public static final Integer C_20002=20002;
	public static final String M_20002="系统接入appKey不存在";
	
	public static final Integer C_20003=20003;
	public static final String M_20003="系统接入验签失败";
	
	public static final Integer C_20004=20004;
	public static final String M_20004="系统接入对应的密钥未配置";
	
	public static final Integer C_20005=20005;
	public static final String M_20005="系统接入密钥存在多个配置";
	
	public static final Integer C_20006=20006;
	public static final String M_20006="系统接入支付方式配置错误，praramJson配置错误";
	
	public static final Integer C_20007=20007;
	public static final String M_20007="URL地址编码错误";
	
	public static final Integer C_20008=20008;
	public static final String M_20008="参数验签失败";

	public static final Integer C_20009=20009;
	public static final String M_20009="请求来源校验失败";
	
	public static final Integer C_20010=20010;
	public static final String M_20010="参数version为空";
	
	public static final Integer C_20011=20011;
	public static final String M_20011="参数appKey为空";
	
	public static final Integer C_20012=20012;
	public static final String M_20012="参数orderNo为空";
	
	public static final Integer C_20013=20013;
	public static final String M_20013="参数timeOut为空";
	
	public static final Integer C_20014=20014;
	public static final String M_20014="参数payerUserId为空";
	
	public static final Integer C_20015=20015;
	public static final String M_20015="参数payeeUserId为空";	
	
	public static final Integer C_20016=20016;
	public static final String M_20016="参数payerName为空";
	
	public static final Integer C_20017=20017;
	public static final String M_20017="参数totalAmt为空";
	
	public static final Integer C_20018=20018;
	public static final String M_20018="参数payAmt为空";
	
	public static final Integer C_20019=20019;
	public static final String M_20019="参数returnUrl为空";
	
	public static final Integer C_20020=20020;
	public static final String M_20020="参数notifyUrl为空";
	
	public static final Integer C_20021=20021;
	public static final String M_20021="参数requestIp为空";
	
	public static final Integer C_20022=20022;
	public static final String M_20022="参数totalAmt错误";
	
	public static final Integer C_20023=20023;
	public static final String M_20023="参数payAmt错误";
	
	public static final Integer C_20024=20024;
	public static final String M_20024="参数receiptAmt错误";
	
	public static final Integer C_20025=20025;
	public static final String M_20025="参数receiptAmt错误";
	
	public static final Integer C_20026=20026;
	public static final String M_20026="时间格式不正确";
	
	public static final Integer C_20027=20027;
	public static final String M_20027="编码错误";
	
	public static final Integer C_20028=20028;
	public static final String M_20028="同步缓存错误";
	
	public static final Integer C_20029=20029;
	public static final String M_20029="读取缓存错误";
	
	/**begin 支付中心返回状态 **/
	public static final Integer C_20030=20030;
	public static final String M_20030="该订单已经支付";	
	public static final Integer C_20031=20031;
	public static final String M_20031="该订单已经关闭";
	/**end 支付中心返回状态 **/
	
	/** begin 第三方支付返回状态 **/
	public static final Integer C_20032=20032;
	public static final String M_20032="该订单已经支付";	
	public static final Integer C_20033=20033;
	public static final String M_20033="该订单已经关闭";	
	/** end 第三方支付返回状态 **/
	
	
	public static final Integer C_20034=20034;
	public static final String M_20034="参数title为空";
	
	public static final Integer C_20035 = 20035;
	public static final String M_20035 = "导出结果集无任何数据，请重新修改查询条件..";
	
	public static final Integer C_20036 = 20036;
	public static final String M_20036 = "导出结果集超过上限（5万条），请重新修改查询条件..";
	/**
	 * 导入excel异常，请联系客服处理!
	 */
	public static final Integer C_20037 = 20037;
	public static final String M_20037 = "导入excel异常，请联系客服处理!";
	
	/**
	 * 导入成功提示!
	 */
	public static final Integer C_20038 = 20038;
	public static final String M_20038 = "导入成功,共导入0行数据";
	/**
	 * 导入失败提示!
	 */
	public static final Integer C_20039 = 20039;
	public static final String M_20039 = "导入失败，数据匹配有误，未执行数据导入";
	
	/**
	 *  xml解析提示
	 */
	public static final Integer C_20040 = 20040;
	public static final String M_20040 = "返回xml解析失败";
	
	/**
	 *  text解析提示
	 */
	public static final Integer C_20041 = 20041;
	public static final String M_20041 = "返回text解析失败";
}
