/**
 * 代付佣金记录
 */
var disableExport = false;
var commissionOrder = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'paycenter/paidCommissionRecord/queryPaidCommissionRecordList',
		//查询详情
		queryDetailUrl:CONTEXT+'paycenter/paytrade/detail',
		//导出数据检查
		exportDataCheckUrl:CONTEXT+'paycenter/paidCommissionRecord/checkExportParams',
		//导出数据url
		exportDataUrl:CONTEXT+'paycenter/paidCommissionRecord/exportData'
	},
	//查询导航
	tb:"#paidCommissiontb",
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
		showPayType : payTypeFormatter
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
		"marketId" : $("#marketId").val(),
		"startOrderTime" : $("#startOrderTime").val(),
		"endOrderTime" : $("#endOrderTime").val(),
		"orderNo" : $("#orderNo").val(),
		"mobile" : $("#mobile").val(),
		"startPayTime" : $("#startPayTime").val(),
		"endPayTime" : $("#endPayTime").val()
	};
	return params;
}
function cleardata(){
	$('#userType').combobox('setValue',"");
	$('#payStatus').combobox('setValue',"");
	$("#hasChange").combobox('setValue',"");
	$("#marketId").val("");
	$("#startOrderTime").val("");
	$("#endOrderTime").val("");
	$('#orderNo').val("");
	$('#mobile').val("");
	$("#startPayTime").val("");
	$("#endPayTime").val("");
}
function initPageList(){
	var queryParams=commissionOrder.queryParams();
	//数据加载
	$('#paidCommissiondg').datagrid({
		url:commissionOrder.url.queryByPageListUrl,
		queryParams:queryParams , 
		height: 'auto', 
		nowrap:true,
		toolbar:commissionOrder.tb,
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
		    {field:'id',title:'', hidden:true},
			{field:'orderNo',title:'关联订单号',align:'center', formatter:orderNoFormatter, width:150},
			{field:'orderTime',title:'下单时间',align:'center', width:100},
			{field:'totalAmt',title:'商品总金额',align:'center', width:100},
			{field:'commissionAmt',title:'支付佣金',align:'center', width:100},
			{field:'commissionAmt',title:'代付款金额',align:'center', width:100},
			{field:'hasChange',title:'转结状态',align:'center', formatter:commissionOrder.convert.showHasChange, width:100},
			// 第三方支付流水 : remit_record表的bankTradeNo(财务线下支付流水)
			{field:'thirdPayNumber',title:'第三方支付流水',align:'center', width:100},
			{field:'payTime',title:'代付时间',align:'center', width:100},
//			{field:'payType',title:'代付方式',align:'center',formatter:commissionOrder.convert.showPayType},
			{field:'payType',title:'代付方式',align:'center',formatter:commissionOrder.convert.showPayType, width:100},
			{field:'payStatus',title:'代付状态',align:'center',formatter:commissionOrder.convert.showPayStatus, width:100},
			{field:'mobile',title:'收款方手机',align:'center', width:100},
			{field:'marketName',title:'市场名称',align:'center', width:100},
			/*{field:'userType',title:'收款方来源',align:'center',formatter:commissionOrder.convert.showUserType},*/
			{field:'userType',title:'佣金支出方',align:'center',formatter:commissionOrder.convert.showUserType}
			//{field:'operate',title:'操作',width:100,align:'center',formatter:commissionOrder.operate.view}
		]]
	}); 
	//分页加载
	$("#paidCommissiondg").datagrid("getPager").pagination({
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

function orderNoFormatter(value,row,index){
	//row.orderNo 和 value都是订单号
//	console.debug("row.orderNo : " + row.orderNo);
//	console.debug("value : " + value);
	return "<a class='operate' href='javascript:;' onclick=commissionOrder.viewAction('"+value+"');>"+value+"</a>";
}

function view(val,row,index){
	return "<a class='operate' href='javascript:;' onclick=commissionOrder.viewAction('"+row.orderNo+"');>查看</a>";
}
function viewAction(orderNo){
	$('#editDialog').dialog({'title':'交易详情','href':commissionOrder.url.queryDetailUrl+'?orderNo='+orderNo}).dialog('open');
}
function exportData(){
	if (disableExport){
		slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");
		return;
	}
	disableExport = true;
	var params = commissionOrder.queryParams();
	var paramList = "userType=" + params.userType + "&payStatus=" + params.payStatus + "&hasChange=" + params.hasChange + 
		"&marketId=" + params.marketId + "&startOrderTime=" + params.startOrderTime + "&endOrderTime=" + params.endOrderTime + 
		"&orderNo=" + params.orderNo + "&mobile=" + params.mobile + "&startPayTime=" + params.startPayTime + 
		"&endPayTime=" + params.endPayTime;
	$.ajax({
		url: commissionOrder.url.exportDataCheckUrl,
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				if (disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					//启动下载
					$.download(commissionOrder.url.exportDataUrl,paramList,'post' );
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
	commissionOrder.initPageList();
});