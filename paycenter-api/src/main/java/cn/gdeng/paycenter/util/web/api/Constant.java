package cn.gdeng.paycenter.util.web.api;

/**
 * Created by Administrator on 2015/6/3.
 */
public class Constant {
    /** 登录用户ID **/
    public static final String SYSTEM_USERID = "systemUserId";
    /** 登录用户账号 **/
    public static final String SYSTEM_USERCODE = "systemUserCode";
    /** 登录用户名称 **/
    public static final String SYSTEM_USERNAME = "systemUserName";
    /** 登录用户菜单 **/
    public static final String SYSTEM_ALLMENU = "systemAllMenu";
    /** 登录用户菜单编号 **/
    public static final String SYSTEM_MENUCODE = "systemMenuCode";
    /** 登录用户按钮编号 **/
    public static final String SYSTEM_BUTTONCODE = "systemButtonCode";
    /** 登录用户 **/
    public static final String SYSTEM_SMGRLOGINUSER = "systemLoginUser";
    /** 登录用户二级菜单 **/
    public static final String SYSTEM_SENCONDMENU = "systemSecondMenu";
    /** 登录用户三级菜单 **/
    public static final String SYSTEM_THIRDMENU = "systemThirdMenu";
    /** 前台用户的合法数据类型 **/
    public static final String USER_DATAMENU = "userDatamenu";
    // redis信息保存时间
    public static final int SESSION_EXPIRE_TIME = 1800;

    /** 系统简称 **/
    public static final String SYS_NAME = "sysName";
    /** 系统类型：后台管理 **/
    public static final String SYS_FULL_NAME_PURCHASE = "谷登运营管理后台";
    /** 登录用户Id session key **/
    public static String LOGIN_SESSSION_USER = "GD_UserInfo";

    /** 登录cookie login uuid **/
    public static String LOGIN_SSO_NAME = "GD_LOGIN_SSO_NAME";

    /** 自动登录cookie login uuid **/
    public static String LOGIN_AUTO_NAME = "GD_LOGIN_AUTO_NAME";

    /** 登录cookie login account **/
    public static String LOGIN_ACCOUNT = "GD_LOGIN_ACCOUNT";

    /** 登录redis前缀 **/
    public static String LOGIN_REDIS_PRE = "GD_LOGIN_PRE_";

    /** 验证redis前缀 **/
    public static String VERIFY_REDIS_PRE = "GD_VERIFY_PRE_";

    /** 修改密码redis前缀 **/
    public static String EDIT_PASSWORD_REDIS_PRE = "GD_EDIT_PWD_PRE_";

    /** 修改手机redis前缀 **/
    public static String EDIT_MOBLIE_REDIS_PRE = "GD_EDIT_MOBLIE_PRE_";

    /** ------------- 登录代码 1000 - 1100 start --------------------- */

    public static final int JSON_RESULT_NOTLOGIN_CODE = 11;
    public static final String JSON_RESULT_NOTLOGIN_DESC = "Not login";
    public static final int JSON_RESULT_REQUEST_PARAM_EMPTY_CODE = 101;
    public static final String JSON_RESULT_REQUEST_PARAM_EMPTY_DESC = "Request parameter {0} can't be empty";

    public static final int JSON_RESULT_REQUEST_PARAM_INVALID_CODE = 102;
    public static final String JSON_RESULT_REQUEST_PARAM_INVALID_DESC = "Request parameter {0} invalid";
    public static final int JSON_RESULT_LOGIN_USER_NOT_EXIST_CODE = 1000;
    public static final String JSON_RESULT_LOGIN_USER_NOT_EXIST_DESC = "用户不存在";
    public static final int JSON_RESULT_LOGIN_PASSWORD_ERROR_CODE = 1001;
    public static final String JSON_RESULT_LOGIN_PASSWORD_ERROR_DESC = "密码错误";
    public static final int JSON_RESULT_LOGIN_INVALID_CODE = 1002;
    public static final String JSON_RESULT_LOGIN_INVALID_DESC = "自动登录失效";
    public static final int JSON_RESULT_AUTO_LOGIN_DISABLED_CODE = 1003;
    public static final String JSON_RESULT_AUTO_LOGIN_DISABLED_DESC = "没有开启自动登录";
    public static final int JSON_RESULT_WEIXIN_AUTH_FAIL_CODE = 1010;
    public static final String JSON_RESULT_WEIXIN_AUTH_FAIL_DESC = "微信授权失败";
    /** ------------- 登录代码 1000 - 1100 end --------------------- */

    /** ------------- 用户注册，用户资料，用户设置 1100 - 1199 start --------------------- */
    public static final int JSON_RESULT_REGISTER_NUMBER_EXIST_CODE = 1110;
    public static final String JSON_RESULT_REGISTER_NUMBER_EXIST_DESC = "用户名已注册";
    public static final int JSON_RESULT_REGISTER_VERIFI_ERROR_CODE = 1111;
    public static final String JSON_RESULT_REGISTER_VERIFI_ERROR_DESC = "验证码不正确";
    /** ------------- 用户注册，用户资料，用户设置 1100 - 1199 end --------------------- */

    public static final String MESSAGE_STATUS_UNREAD = "1";
    public static final String USERBOARD_STATUS_CLOSE = "0";
    public static final String USERBOARD_STATUS_OPEN = "1";
    public static final String USERBOARD_TYPE_SYNTHESIZE = "1";
    public static final String USERBOARD_TYPE_CLASSIFY = "2";
    
    /**-----------------------  数据库表的删除标识 start   -------------------------------------*/
    public static final Byte TABLE_NOT_DELETE=0x00; //未删除
    public static final Byte TABLE_DELETE=0x01;  //已删除
    /**-----------------------  数据库表的删除标识  end   -------------------------------------*/
    
    /**-----------------------  选中标识 start   -------------------------------------*/
    public static final Byte TABLE_NOT_SELECT=0x00; //未选中
    public static final Byte TABLE_SELECT=0x01;  //选中
    /**-----------------------  选中标识  end   -------------------------------------*/
    
    /** 手机验证码模版**/
//	public static final String SMSMSG = "【谷登科技】您好,手机验证码:{code}";
    public static final String SMSMSG = "验证码:{code},您正在进行支付中心后台登录操作,请勿向他人泄漏";
	/** 阿里大鱼短信通道**/
	public static final String REDISTYPE="4";
    
	/** 手机验证码 **/
	public static final String MOBILE_CHECK = "mobileCheck";
	
	/** 手机验证码 **/
	public static final String MOBILE_CHECK_LOGIN = "mobileCheck_login";
	
	public static final String MOBILE_CHECK_MODIFY_BILL = "mobileCheck_modify_bill";
	
	
	/** 手机验证码 **/
	public static final String PASS_CHECK = "passCheck";
	
	/** 记住的手机号**/
	public static final String REMEMBER_MOBILE="rememberMobile";
	
	/** 手机验证码发送时间 **/
	public static final String MOBILE_CHECK_TIME = "mobileCheckTime";
    
	/** 手机验证码发送时间 **/
	public static final String MOBILE_CHECK_TIME_LOGIN = "mobileCheckTimeLogin";
	
	public static final String MOBILE_CHECK_TIME_MODIFY_BILL = "mobileCheckTimeModifyBill";
	
	

    /**
     * 帐号类型
     */
    public static enum AccountTypeEnum {
        USERNAME("用户名", 0),

        MOBLIE("手机", 1),

        EMAIL("邮箱", 2),

        NICKNAME("昵称", 3);

        public String value;

        public int num;

        AccountTypeEnum(String value, int num) {
            this.value = value;
            this.num = num;
        }
    }

    /**
     * 密码类型
     */
    public static enum PasswordTypeEnum {
        PLAIN("明文", 0),

        CIPHER("密文", 1);

        public String value;

        public int num;

        PasswordTypeEnum(String value, int num) {
            this.value = value;
            this.num = num;
        }
    }

    public static enum LoginClientEnum {

        WEB("WEB", 0),

        WEIXIN("微信HTML5", 1),

        IOS("iOS客户端", 2),

        ANDORID("android客户端", 3);

        public String value;

        public int num;

        LoginClientEnum(String value, int num) {
            this.value = value;
            this.num = num;
        }

    }
    
    /**
	 * 阿里大鱼常量
	 * @author gcwu
	 *
	 */
	public final static class Alidayu{
		/** alidayu模版id **/
		public static final String TEMPLATECODE = "templateCode";
		
		/** alidayu短信签名 **/
		public static final String FREESIGNNAME = "freeSignName";
		
		/** alidayu手机验证码  **/
		public static final String CODE = "code";
		
		/** alidayu手机m密码  **/
		public static final String PASSWORD = "password";
		
		/** code模版内容 **/
		public static final String PARAM = "param";
		
		/** 阿里大鱼短信通道**/
		public static final String REDISTYPE="4";
		/** 默认短信通道**/
		public static final String  DEFAULTNO ="4";
		
		/** 找回密码**/
		public static final Integer MESSAGETYPE1=1;
		
		/** WEB端注册验证码 **/
		public static final Integer MESSAGETYPE2=2;
		
		/** 手机端注册验证码 **/
		public static final Integer MESSAGETYPE3=3;
		
		/** 商户添加用户短信通知 **/
		public static final Integer MESSAGETYPE4=4;
		
		/** 修改支付密码**/
		public static final Integer MESSAGETYPE5=5;
		
		/** 支付中心后台登录短信验证 **/
		public static final Integer MESSAGETYPE6=6;
		
		/** 支付中心后台修改对账短信验证 **/
		public static final Integer MESSAGETYPE7=7;
	}

}
