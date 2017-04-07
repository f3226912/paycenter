/**
 *  对账日志明显列表。
 *  ps：js脚本依赖于父脚本billCheckList.js(对账日志列表)。
 *  	使用此脚本必须保证billCheckList.js已经被引入。
 */

/**
 * 当前js的闭包，使用闭包是为了避免与父js出现冲突问题。
 */
(function(){

	//定制对账日志明细对象
	var billCheckItemLog = {
		//所需要访问url
		url : {
			//查询分页
			queryByPageListUrl : CONTEXT+'/billCheckLog/queryBillCheckItemLogList',
		},
		//获取查询所需参数
		queryParams : getQueryParams,
		//渲染结果集
		initPageList : initPageList,
		//属性转换
		convert:{
			payType : payTypeFormmatter,
			payTime : payTimeFormmatter,
			checkAmt: moneyFormmatter,
			checkSuccessAmt: moneyFormmatter,
			checkFailAmt: moneyFormmatter
		}
	};

	/**查询列表数据的参数
	 * @returns 参数对象
	 */
	function getQueryParams() {
		//从父js中获取查询参数
		return queryItemLogListParams;
	}


	/**初始化页面列表
	 * 
	 */
	function initPageList(){
	    //表格定义和数据加载
		var queryParams=billCheckItemLog.queryParams();
		$('#billCheckItemLogDG').datagrid({
			url:billCheckItemLog.url.queryByPageListUrl,
			queryParams:queryParams , 
			height: 'auto', 
			nowrap:true,
			toolbar:'#billCheckItemLogTb',
			pageSize:50,
			rownumbers:true,
			pagination:true,
			fitColumns:false,
			singleSelect:true,
			fit:true,
			//使用自定义的视图
			view: ViewUtils.getEmptyTipView(),
	        emptyMsg: '没有找到相关数据。',
			columns:[[
				{field:'payType',title:'支付渠道',align:'center',width:100, formatter:billCheckItemLog.convert.payType},
				{field:'checkTime',title:'对账时间',align:'center',width:130},
				{field:'payTime',title:'交易支付日期',align:'center',width:100, formatter:billCheckItemLog.convert.payTime},
				{field:'checkNum',title:'对账笔数',align:'center',width:90},
				{field:'checkAmt',title:'对账金额',align:'center',width:110, formatter:billCheckItemLog.convert.checkAmt},
				{field:'checkSuccessNum',title:'对账成功笔数',align:'center',width:90},
				{field:'checkSuccessAmt',title:'对账成功金额',align:'center',width:110,formatter:billCheckItemLog.convert.checkSuccessAmt},
				{field:'checkFailNum',title:'对账失败笔数',align:'center',width:90},
				{field:'checkFailAmt',title:'对账失败金额',align:'center',width:110,formatter:billCheckItemLog.convert.checkFailAmt}
			]],
			onLoadError:function() {
				jQuery.messager.alert('错误','查询错误!','error');
			},
			onLoadSuccess:function(data) {
				if (!data.success) {
					jQuery.messager.alert('错误','查询错误!','error');
				}
			}
		}); 
		//分页加载
		$("#billCheckItemLogDG").datagrid("getPager").pagination({
			pageList: [10,20,50,100]
		});
}
	
	//初始化执行
	$(function(){
		billCheckItemLog.initPageList();
	});
	
}());

