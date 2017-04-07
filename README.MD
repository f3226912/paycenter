支付中心项目说明(201608026)

注意:

    1 由于支付宝支付回调依赖外网环境，需要将paycenter-web-gateway映射到外网端口，
               支付宝才能回调到支付中心网关回调接口。


一、项目模块
		项目名称				项目描述		用途
		gd-paycenter-pom 			主目录		公共jar、配置文件参数、打包编译
		paycenter-api				接口(jar)	各个模块dto、vo、接口、实体类、工具类
		paycenter-srv-admin		服务层(war)	后台管理模块服务实现类		
		paycenter-srv-pay		服务层(war)	支付服务实现类
		paycenter-srv-notify	服务层(war)   支付中心mq消费端
		paycenter-task		服务层(war)	定时任务
		paycenter-web-admin		控制层(war)	后台管理 Controller
		paycenter-web-gateway	支付网关
	备注：Controller层可调用任意server服务，admin后台管理除外
		  server层服务之间不能相互调用
		  业务均写在server层,Controller层只做数据返回(均返回对象:ApiResult.java)
		 请写接口的人员务必自测接口，通过junit测试，或者http请求测试(推荐使用jmeter)
	
二、导入
		首先下载源码gd-paycenter-pom,右键导入import-->Maven-->Existing maven prijects
		勾选上述项目名称，确定即可
	
三、配置
		编码：UTF-8
		JDK：1.7
		依赖：各个项目均依赖paycenter-api
	
四、包名规范
		根包名：cn.gdeng.paycenter
		子模块包命名规则 cn.gdeng.paycenter.子模块名称，例如：
												cn.gdeng.paycenter.api
												cn.gdeng.paycenter.server
												cn.gdeng.paycenter.web
五、paycenter-api规范
		查询条件对象DTO：cn.gdeng.paycenter.dto.项目名称.模块名称，例如：
										cn.gdeng.paycenter.api.dto.pay
		返回结果对象VO：cn.gdeng.paycenter.dto.项目名称.模块名称，例如：
										cn.gdeng.paycenter.api.vo.pay
		实体对象：cn.gdeng.paycenter.entity.数据库名
		接口对象：cn.gdeng.nst.server.模块名称
							cn.gdeng.paycenter.admin.server.pay
		公共工具类：cn.gdeng.paycenter.util.分类名
		
六、paycenter-srv-*规范
		DAO对象：cn.gdeng.paycenter.server.dao
				 cn.gdeng.paycenter.server.dao.impl
						cn.gdeng.paycenter.admin.server.pay.impl
		工具类：均写在paycenter-api中
		
七、paycenter-web规范
		拦截器：cn.gdeng.paycenter.web.interceptor
		控制层:cn.gdeng.paycenter.web.controller.模块名称
					cn.gdeng.paycenter.web.controller.pay
		
八、项目调试
	paycenter-srv-* paycenter-web-* 可放在同一tomcat启动,建议 server和web分开启动
		