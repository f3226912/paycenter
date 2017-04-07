/**
 *  对账日志列表
 */

//是否禁止导出。 
var disableExport = false;
//查询对账明细的参数，供弹出对账明细列表框时做查询使用。
var queryItemLogListParams;

//定制对账对象
var billCheck = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'/billCheckLog/queryBillCheckSumList',
		//导出数据检查
		exportDataCheckUrl:CONTEXT+'/billCheckLog/checkExportParams',
		//导出数据url
		exportDataUrl:CONTEXT+'/billCheckLog/exportExcel',
		//重新对账url
		afreshCheckBillUrl: CONTEXT+'/billCheckLog/afreshBillCheck'
	},
	//获取查询所需参数
	queryParams : getQueryParams,
	//渲染结果集
	initPageList : initPageList,
	//查询结果集
	query:reloadPageList,
	//每个item项的操作
	operateItem: {
		//显示
		view:operateItemView,
		//行为
		viewAction:{
			afreshCheck:afreshCheckViewAction,
			checkLog:checkLogViewAction,
			checkSuccessNum:checkSuccessNumViewAction,
			checkFailNum:checkFailNumViewAction
		}
	},
	//属性转换
	convert:{
		payType : payTypeFormmatter,
		payTime : payTimeFormmatter,
		checkAmt: moneyFormmatter,
		checkSuccessAmt: moneyFormmatter,
		checkFailAmt: moneyFormmatter,
		checkSuccessNum:checkSuccessNumFormatter,
		checkFailNum:checkFailNumFormatter
	},
	//工具栏事件
	toolbarAction: {
		//重新对账
		afreshCheck:afreshCheckToolbarAction,
		//导出结果集
		exportData:exportDataToolbarAction,
		//重新对账弹出框的确定事件
		confirmAfreshCheck:confirmAfreshCheckToolbarAction
	}
};

/**查询列表数据的参数
 * @returns 参数对象
 */
function getQueryParams() {
	var params = {
		payType:$("#payType").val(),
		startPayTime:$("#startDate").val(),
		endPayTime:$("#endDate").val(),
	};
	return params;
}

/**支付类型格式化
 * @param val
 * @param row
 * @param index
 * @returns {String}
 */
function payTypeFormmatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	//直接在页面元素中获取支付类型对应的名称。
	return $("#payType option[value='" + val + "']").text();
}

/**交易支付日期格式化
 * @param val
 * @param row
 * @param index
 * @returns {String}
 */
function payTimeFormmatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	//去除时分秒
	return val.substring(0, val.indexOf(" "));
}

/**所有金额类型的格式化
 * @param val
 * @param row
 * @param index
 * @returns
 */
function moneyFormmatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	//去除时分秒
	return formatNumber(val, 2);
}

/**对账成功笔数的格式化
 * @param val
 * @param row
 * @param index
 * @returns
 */
function checkSuccessNumFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	return "<a class='operate' href='javascript:;' onclick=\"billCheck.operateItem.viewAction.checkSuccessNum('"+index+"');\" >" + val + "</a>";
}

/**对账失败笔数的格式化
 * @param val
 * @param row
 * @param index
 * @returns
 */
function checkFailNumFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	return "<a class='operate' href='javascript:;' onclick=\"billCheck.operateItem.viewAction.checkFailNum('"+index+"');\" >" + val + "</a>";
}



/**初始化页面列表
 * 
 */
function initPageList(){
    //表格定义和数据加载
	var queryParams=billCheck.queryParams();
	$('#billCheckDG').datagrid({
		queryParams:queryParams , 
		height: 'auto', 
		nowrap:true,
		toolbar:'#billCheckTb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singleSelect:true,
		fit:true,
		//使用自定义的视图
		view: ViewUtils.getEmptyTipView(),
        emptyMsg: '没有找到相关数据。',
		columns:[[
			{field:'payType',title:'支付渠道',align:'center',width:130, formatter:billCheck.convert.payType},
			{field:'payTime',title:'交易支付日期',align:'center',width:100, formatter:billCheck.convert.payTime},
			{field:'checkNum',title:'对账笔数',align:'center',width:100},
			{field:'checkAmt',title:'对账金额',align:'center',width:120, formatter:billCheck.convert.checkAmt},
			{field:'checkSuccessNum',title:'对账成功笔数',align:'center',width:100, formatter:billCheck.convert.checkSuccessNum},
			{field:'checkSuccessAmt',title:'对账成功金额',align:'center',width:120,formatter:billCheck.convert.checkSuccessAmt},
			{field:'checkFailNum',title:'对账失败笔数',align:'center',width:100, formatter:billCheck.convert.checkFailNum},
			{field:'checkFailAmt',title:'对账失败金额',align:'center',width:120,formatter:billCheck.convert.checkFailAmt},
			{field:'id',title:'操作',align:'center',width:140,formatter:billCheck.operateItem.view}
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
	$("#billCheckDG").datagrid("getPager").pagination({
		pageList: [10,20,50,100]
	});
	
	//有查询权限，则初始化后进行查询。
	if ($("#queryBtn").length > 0) {
		$('#billCheckDG').datagrid({url:billCheck.url.queryByPageListUrl});
	}
}

/**重新加载页面列表数据
 * 
 */
function reloadPageList() {
	$('#billCheckDG').datagrid("load", billCheck.queryParams());
}

/**导出数据
 * 
 */
function exportDataToolbarAction(){
	if (disableExport){
		slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");
		return;
	}
	disableExport = true;
	var params = billCheck.queryParams();
	var paramList = "account="+params.account+"&mobile="+params.mobile+
		"&startAmt="+params.startAmt+"&endAmt="+params.endAmt;
	$.ajax({
		url: billCheck.url.exportDataCheckUrl,
		data : params,
		type:'post',
		success : function(data){
			//1.判断服务器是否处理成功
			if (!data.success) {
				warningMessage("服务器处理失败！");
				return false;
			}
			//2.判断检测是否通过
			if (data.isPassed){
				if (disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					//启动下载
					$.download(billCheck.url.exportDataUrl,params,'post');
				}
			}else{
				warningMessage(data.message);
			}
		},
		error : function(){
			warningMessage("服务器出错");
		}
	});
	// 10秒后导出按钮重新可用
	setInterval(function(){
		disableExport = false;
	}, 10000);
}

/**格式化数值。
 * @param num 需要格式化的数值
 * @param precision 保存的小数位个数
 * @param separator 分隔符，默认为逗号分隔。
 * @returns
 */
function formatNumber(num, precision, separator) {
    var parts;
    // 判断是否为数字
    if (!isNaN(parseFloat(num)) && isFinite(num)) {
     // 把类似 .5, 5. 之类的数据转化成0.5, 5, 为数据精度处理做准, 至于为什么
     // 不在判断中直接写 if (!isNaN(num = parseFloat(num)) && isFinite(num))
     // 是因为parseFloat有一个奇怪的精度问题, 比如 parseFloat(12312312.1234567119)
     // 的值变成了 12312312.123456713
     num = new Number(num);
     // 处理小数点位数
     num = (typeof precision !== 'undefined' ? num.toFixed(precision) : num).toString();
     // 分离数字的小数部分和整数部分
     parts = num.split('.');
     // 整数部分加[separator]分隔, 借用一个著名的正则表达式
     parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (separator || ','));

     return parts.join('.');
    }
    return NaN;
}


/**表格记录项操作视图
 * @param id 当前数据id
 * @param row 当前行数据
 * @param index 当前行索引
 * @returns
 */
var hasAfreshCheckBillPrivi =  $("#afreshCheckBillBtn").length > 0;
var hasSeeCheckLogPrivi =  $("#chekLogPrivilegeFlag").length > 0;
function operateItemView(id, row, index) {
	var afreshCheck = "";
	//判断是否有重新对账权限
	if (hasAfreshCheckBillPrivi) {
		afreshCheck = "<a class='operate' href='javascript:;' onclick=\"billCheck.operateItem.viewAction.afreshCheck('"+index+"');\">重新对账</a>";
	}
	
	//判断是否有查看对账日志的权限。
	var checkLog = "";
	if (hasSeeCheckLogPrivi) {
		checkLog = "<a class='operate' href='javascript:;' onclick=\"billCheck.operateItem.viewAction.checkLog('"+index+"');\">对账日志</a>";
	}
	return afreshCheck + "&nbsp;&nbsp;" + checkLog;
}

/** 重新对账视图的行为
 * @param rowIndex 行索引
 */
function afreshCheckViewAction(rowIndex) {
	var rows = $('#billCheckDG').datagrid("getRows");
	var curRow = rows[rowIndex];
	var payType = curRow.payType;
	var payTime = curRow.payTime;
	//去除时分秒
	payTime = payTime.substring(0, payTime.indexOf(" "))
	//执行对账操作
	doAfreshCheckBill(payType, payTime);
}

/** 对账日志视图的行为
 * @param rowIndex 行索引
 */
function checkLogViewAction(rowIndex) {
	var rows = $('#billCheckDG').datagrid("getRows");
	var curRow = rows[rowIndex];
	queryItemLogListParams = {
		payType:curRow.payType,
		payTime:curRow.payTime
	};
	//弹出日志明显列表对话框。
	$('#itemLogListDialog').dialog({
		'title':'对账日志明细', 
		'width':1000, 
		'height':'350',
		'href':CONTEXT+'/billCheckLog/toItemLogList',
		'closed':true,
		'resizable':true,
		'maximizable':true
	}).dialog('open');

}

/** 对账成功笔数的视图行为
 * @param rowIndex 行索引
 */
function checkSuccessNumViewAction(rowIndex) {
	
	var rows = $('#billCheckDG').datagrid("getRows");
	var curRow = rows[rowIndex];
	var payType = curRow.payType;
	var payTime = curRow.payTime;
	//去除时分秒
	payTime = payTime.substring(0, payTime.indexOf(" "));
	//目标地址
	var targetUrl = CONTEXT + "/paycenter/agencyCollection/list";
	targetUrl += "?payType="+payType;
	targetUrl += "&startDate="+payTime;
	targetUrl += "&endDate="+payTime;
	targetUrl += "&checkStatus=1";
	//切换tab
	TabUtils.addTab("代收款记录", targetUrl);
}

/** 对账失败笔数的视图行为
 * @param rowIndex 行索引
 */
function checkFailNumViewAction(rowIndex) {
	var rows = $('#billCheckDG').datagrid("getRows");
	var curRow = rows[rowIndex];
	var payType = curRow.payType;
	var payTime = curRow.payTime;
	//去除时分秒
	payTime = payTime.substring(0, payTime.indexOf(" "));
	var targetUrl = CONTEXT + "/paycenter/failedBill/list";
	targetUrl += "?payType="+payType;
	targetUrl += "&startDate="+payTime;
	targetUrl += "&endDate="+payTime;
	//切换tab
	TabUtils.addTab("对账失败明细", targetUrl);
}

/**
 * 重新对账的工具条行为
 */
function afreshCheckToolbarAction() {
	//设置弹框的日期默认值。
	if (!$("#payTimeForDialog").val()) {
		var curDate = new Date();
		var year = curDate.getFullYear();
		var month = curDate.getMonth() + 1;
		if(month < 10) {
			month = "0" + month;
		}
		var day = curDate.getDate();
		if(day < 10) {
			day = "0" + day;
		}
		var formatCurDate = year + "-" + month + "-" + day;
		$("#payTimeForDialog").val(formatCurDate);
	}
	$('#afreshCheckDialog').dialog({'title':'提示', 'width':300, 'height':'180'}).dialog('open');
}

/**
 * 重新对账弹出框的确定事件
 */
function confirmAfreshCheckToolbarAction() {
	var payType = $("#payTypeForDialog").val();
	var payTime = $("#payTimeForDialog").val();
	//执行对账
	doAfreshCheckBill(payType, payTime);
	//关闭弹出框
	$('#afreshCheckDialog').dialog('close');
}

/**
 * 执行重新对账
 * @param payType 支付类型
 * @param payTime 支付时间
 */
function doAfreshCheckBill(payType, payTime) {
	$.ajax({
		url : billCheck.url.afreshCheckBillUrl,
		data : {"payType": payType, "payTime": payTime},
		type : 'post',
		dataType : "json",
		async : false, 
		success : function(data){
			if (!data.success) {
				if (data.msg && data.msg != "") {
					$.messager.alert('错误',data.msg, 'error');
				} else {
					$.messager.alert('错误','对账失败', 'error');
				}
				return ;
			}
			
			$.messager.alert('提示','对账成功并成生成了对应清分文件！', "info", function() {
				//重新加载查询表格数据。
				billCheck.query();
			});
		},
		error : function(data) {
			$.messager.alert('错误','对账失败', 'error');
		},
		complete: function() {
			MaskUtils.unmask();
		},
		beforeSend: function() {
			MaskUtils.mask("对账数据请求中...");
		}
	});
}


//初始化执行
$(function(){
	billCheck.initPageList();
});
