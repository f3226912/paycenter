var disableExport = false;
var penaltyRecord = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'penaltyRecord/getPenaltyRecordByPage',
		//查询详情
		queryDetailUrl:CONTEXT+'paycenter/paytrade/detail',
		//导出数据检查
		exportDataCheckUrl:CONTEXT+'penaltyRecord/checkExportParams',
		//导出数据url
		exportDataUrl:CONTEXT+'penaltyRecord/exportData'
	},
	//查询导航
	tb:"#penaltyRecordtb",
	//获取查询所需参数
	queryParams : getQueryParams,
	//重置查询参数
	cleardata:cleardata,
	//渲染结果集
	initPageList : initPageList,
	//查询结果集
	query:function(){
		initPageList();
	},
	//导出结果集
	exportData:exportData,
	//属性转换
	convert:{
		//代付转态
		showPayStatus : showPayStatus,
		//转结状态转换
		showHasChange : showHasChange,
		//转结用户类型
		showUserType : showUserType
	},
	//操作
	operate:{
		//查看操作
		view:view
	},
	//查看详情
	viewAction:viewAction
};
function getQueryParams() {
	var params = {
		"userType" : $("#userType").combobox('getValue'),
		"payStatus" : $("#payStatus").combobox('getValue'),
		"hasChange" : $("#hasChange").combobox('getValue'),
		"startOrderTime" : $("#startOrderTime").val(),
		"endOrderTime" : $("#endOrderTime").val(),
		"startPayTime" : $("#startPayTime").val(),
		"endPayTime" : $("#endPayTime").val(),
		"payeeMobile" : $("#payeeMobile").val(),
		"orderNo" : $("#orderNo").val()
	};
	return params;
}
function cleardata(){
	$('#userType').combobox('setValue',"");
	$('#payStatus').combobox('setValue',"");
	$("#hasChange").combobox('setValue',"");
	$("#startOrderTime").val("");
	$("#endOrderTime").val("");
	$("#startPayTime").val("");
	$("#endPayTime").val("");
	$("#payeeMobile").val("");
	$("#orderNo").val("");
}
function initPageList(){
	var queryParams=penaltyRecord.queryParams();
	//数据加载
	$('#penaltyRecorddg').datagrid({
		url:penaltyRecord.url.queryByPageListUrl,
		queryParams:queryParams , 
		height: 'auto', 
		nowrap:true,
		toolbar:penaltyRecord.tb,
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
		    {field:'id',title:'', hidden:true},
			{field:'orderNo',title:'关联订单号',align:'center', width:150, formatter:orderNoFormatter},
			{field:'orderTime',title:'下单时间',align:'center', width:100},
			{field:'payAmt',title:'预付款',align:'center', width:100},
			{field:'tradeAmt',title:'违约金额',align:'center', width:100},
			{field:'tradeAmt',title:'代付款金额',align:'center', width:100},
			{field:'hasChange',title:'转结状态',align:'center', formatter:penaltyRecord.convert.showHasChange, width:100},
			{field:'thirdPayNumber',title:'第三方支付流水',align:'center', width:100},
			{field:'payTime',title:'代付时间',align:'center', width:100},
			{field:'payType',title:'代付方式',align:'center', width:100},
			{field:'payStatus',title:'代付状态',align:'center',formatter:penaltyRecord.convert.showPayStatus, width:100},
			{field:'payeeMobile',title:'收款方手机',align:'center', width:100},
			{field:'userType',title:'收款方',align:'center', width:100, formatter:penaltyRecord.convert.showUserType}
		]]
	}); 
	//分页加载
	$("#penaltyRecorddg").datagrid("getPager").pagination({
		pageList: [10,20,50,100]
	});
}
function showPayStatus(val,row,index){
	if (val == '1') {
		return "付款成功";
	} else {
		return "待付款";
	}
}
function showHasChange(val,row,index){
	if (val == '1') {
		return "已转结";
	} else {
		return "未转结";
	}
}
function showRefundType(val,row,index){
	if (val == '1') {
		return "系统退款";
	} else if (val == '2') {
		return "人工退款";
	}
}
function payTypeFormatter(val, row, index){
	if(val == 'ALIPAY_H5'){
		return "支付宝";
	} else if(val == 'WEIXIN_APP'){
		return "微信";
	} else if(val == 'PINAN'){
		return "平安银行";
	}else if(val == 'ENONG'){
		return "E农";
	}else if(val == 'NNCCB'){
		return "南宁建行";
	}else if(val == 'GXRCB'){
		return "广西农信";
	} else if (val == 'WANGPOS') {
		return '旺POS';
	} else if (val == 'nsy') {
		return "农商友";
	}
}

function showUserType(val,row,index){
	if (val == 'nsy') {
		return "农商友";
	} else if (val == 'nps') {
		return "农批商";
	} else if (val == 'market') {
		return "市场";
	} else if (val == 'plat') {
		return "谷登平台";
	} else if (val == 'nst_car') {
		return "车主";
	} else if (val == 'nst_goods') {
		return "货主";
	} else if (val == 'logis') {
		return "物流公司";
	}
}

function orderNoFormatter(value,row,index){
	//row.orderNo 和 value都是订单号
//	console.debug("row.orderNo : " + row.orderNo);
//	console.debug("value : " + value);
	return "<a class='operate' href='javascript:;' onclick=penaltyRecord.viewAction('"+value+"');>"+value+"</a>";
}

function view(val,row,index){
	return "<a class='operate' href='javascript:;' onclick=penaltyRecord.viewAction('"+row.id+"');>查看</a>";
}
function viewAction(orderNo){
	$('#editDialog').dialog({'title':'交易详情','href':penaltyRecord.url.queryDetailUrl+'?orderNo='+orderNo}).dialog('open');
}
function exportData(){
	if (disableExport){
		slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");
		return;
	}
	disableExport = true;
	var params = penaltyRecord.queryParams();
	var paramList = "payStatus="+params.payStatus+"&startOrderTime="+params.startOrderTime+ 
		"&endOrderTime="+params.endOrderTime+"&startPayTime="+params.startPayTime+
		"&endPayTime="+params.endPayTime+"&payeeMobile="+params.payeeMobile+
		"&orderNo="+params.orderNo+"&payCenterNumber="+params.payCenterNumber+
		"&thirdPayNumber="+params.thirdPayNumber;
	$.ajax({
		url: penaltyRecord.url.exportDataCheckUrl,
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				if (disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					//启动下载
					$.download(penaltyRecord.url.exportDataUrl,paramList,'post' );
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
$(function(){
	penaltyRecord.initPageList();
});