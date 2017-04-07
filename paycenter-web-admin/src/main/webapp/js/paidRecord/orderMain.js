/**
 * 代付订单记录
 */
var disableExport = false;
var paidOrder = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'paycenter/paidOrderRecord/queryPaidOrderRecordList',
		//查询详情
		queryDetailUrl:CONTEXT+'paycenter/paytrade/detail',
		//导出数据检查
		exportDataCheckUrl:CONTEXT+'paycenter/paidOrderRecord/checkExportParams',
		//导出数据url
		exportDataUrl:CONTEXT+'paycenter/paidOrderRecord/exportData'
	},
	//查询导航
	tb:"#paidOrdertb",
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
		//收款方来源
		showUserType : showUserType,
		//转结状态转换
		showHasChange : showHasChange,
		//代付方式
		showPayType : payTypeFormatter,
		//转换时间 '-'
		showTime : showTime
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
		"orderType" : $("#orderType").combobox('getValue'),
		"payStatus" : $("#payStatus").combobox('getValue'),
		"hasChange" : $("#hasChange").combobox('getValue'),
		"userType" : $("#userType").combobox('getValue'),
		"startOrderTime" : $("#startOrderTime").val(),
		"endOrderTime" : $("#endOrderTime").val(),
		"startPayTime" : $("#startPayTime").val(),
		"endPayTime" : $("#endPayTime").val(),
		"payeeMobile" : $("#payeeMobile").val(),
		"orderNo" : $("#orderNo").val(),
//		"payCenterNumber" : $("#payCenterNumber").val(),
		"thirdPayNumber" : $("#thirdPayNumber").val()
	};
	return params;
}
function cleardata(){
	$('#orderType').combobox('setValue',"");
	$('#payStatus').combobox('setValue',"");
	$("#hasChange").combobox('setValue',"");
	$("#userType").combobox('setValue',"");
	$("#startOrderTime").val("");
	$("#endOrderTime").val("");
	$("#startPayTime").val("");
	$("#endPayTime").val("");
	$("#payeeMobile").val("");
	$("#orderNo").val("");
//	$("#payCenterNumber").val("");
	$("#thirdPayNumber").val("");
}
function initPageList(){
	var queryParams=paidOrder.queryParams();
	//数据加载
	$('#paidOrderdg').datagrid({
		url:paidOrder.url.queryByPageListUrl,
		queryParams:queryParams , 
		height: 'auto', 
		nowrap:true,
		toolbar:paidOrder.tb,
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
		    {field:'id',title:'', hidden:true},
			{field:'orderNo',title:'订单号',align:'center', formatter:orderNoFormatter, width : 150},
			{field:'orderTime',title:'下单时间',align:'center', width : 100},
			{field:'totalAmt',title:'商品总金额',align:'center', width : 100},
			{field:'feeAmt',title:'谷登代收手续费',align:'center', width : 100},
//			{field:'commissionAmt',title:'佣金支出',align:'center'},
			{field:'marketCommission',title:'市场佣金',align:'center', width : 100},
			{field:'platformCommission',title:'平台佣金',align:'center', width : 100},
			{field:'subsidyAmt',title:'刷卡补贴',align:'center', width : 100},
			{field:'paidAmt',title:'代付款金额',align:'center', width : 100},
			{field:'hasChange',title:'转结状态',align:'center', formatter:paidOrder.convert.showHasChange, width : 100},
//			{field:'payCenterNumber',title:'平台支付流水',align:'center'},
			// 第三方支付流水 : remit_record表的bankTradeNo(财务线下支付流水)
			{field:'thirdPayNumber',title:'第三方支付流水',align:'center', width : 100},
			{field:'payTime',title:'代付时间',align:'center', width : 100, formatter:paidOrder.convert.showTime},
			{field:'payType',title:'代付方式',align:'center', width : 100},
			{field:'payStatus',title:'代付状态',align:'center',formatter:paidOrder.convert.showPayStatus, width : 100},
			{field:'payeeMobile',title:'收款方手机',align:'center', width : 100},
			{field:'orderTypeStr',title:'订单类型',align:'center', width : 100},
			{field:'userTypeStr',title:'收款方',align:'center', width : 100}
//			{field:'operate',title:'操作',align:'center',formatter:paidOrder.operate.view}
		]]
	}); 
	//分页加载
	$("#paidOrderdg").datagrid("getPager").pagination({
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
function showUserType(val,row,index){
	if (val == 'nsy') {
		return "农商友";
	} else if (val == 'nps') {
		return "农批商";
	} 
}
function showHasChange(val,row,index){
	if (val == '1') {
		return "已转结";
	} else {
		return "未转结";
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
	}
	return val;
}
function showTime(val, row, index){
	if (typeof(val)=="undefined") {
		return "-";
	} else {
		return val;
	}
}

function orderNoFormatter(value,row,index){
	//row.orderNo 和 value都是订单号
//	console.debug("row.orderNo : " + row.orderNo);
//	console.debug("value : " + value);
	return "<a class='operate' href='javascript:;' onclick=paidOrder.viewAction('"+value+"');>"+value+"</a>";
}

function view(val,row,index){
	return "<a class='operate' href='javascript:;' onclick=paidOrder.viewAction('"+row.orderNo+"');>查看</a>";
}
function viewAction(orderNo){
	$('#editDialog').dialog({'title':'交易详情','href':paidOrder.url.queryDetailUrl+'?orderNo='+orderNo}).dialog('open');
}
function exportData(){
	if (disableExport){
		slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");
		return;
	}
	disableExport = true;
	var params = paidOrder.queryParams();
	var paramList = "payStatus="+params.payStatus+"&startOrderTime="+params.startOrderTime+ 
		"&endOrderTime="+params.endOrderTime+"&startPayTime="+params.startPayTime+
		"&endPayTime="+params.endPayTime+"&payeeMobile="+params.payeeMobile+
		"&orderNo="+params.orderNo + "&thirdPayNumber="+params.thirdPayNumber;
	$.ajax({
		url: paidOrder.url.exportDataCheckUrl,
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				if (disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					//启动下载
					$.download(paidOrder.url.exportDataUrl,paramList,'post' );
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
	paidOrder.initPageList();
});