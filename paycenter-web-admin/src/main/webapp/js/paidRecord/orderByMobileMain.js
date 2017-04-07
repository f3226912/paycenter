/**
 * 通过电话查询代付订单记录
 */
var paidByMobileOrder = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'paycenter/paidOrderRecord/queryPaidOrderRecordList',
		//查询详情
		queryDetailUrl:CONTEXT+'paycenter/paytrade/detail'
	},
	//渲染结果集
	initPageList : initPageList,
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
function initPageList(){
	var queryParams={};
	//queryParams.payStatus=$("#redirectPayStatus").val();
	queryParams.memberId=$("#memberId").val();
	queryParams.hasChange=$("#hasChange").val();
	queryParams.batNo=$("#batNo").val();
	//数据加载
	$('#paidOrderByMobiledg').datagrid({
		url:paidByMobileOrder.url.queryByPageListUrl,
		queryParams:queryParams , 
		height: 'auto', 
		nowrap:true,
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
		    {field:'id',title:'', hidden:true},
			{field:'orderNo',title:'订单号',align:'center', formatter:orderNoFormatter, width:150},
			{field:'orderTime',title:'下单时间',align:'center', width:100},
			{field:'totalAmt',title:'商品总金额',align:'center', width:100},
			{field:'feeAmt',title:'谷登代收手续费',align:'center', width:100},
//			{field:'commissionAmt',title:'佣金支出',align:'center'},
			{field:'marketCommission',title:'市场佣金',align:'center', width:100},
			{field:'platformCommission',title:'平台佣金',align:'center', width:100},
			{field:'subsidyAmt',title:'刷卡补贴',align:'center', width:100},
			{field:'paidAmt',title:'代付款金额',align:'center', width:100},
//			{field:'payCenterNumber',title:'平台支付流水',align:'center'},
			{field:'hasChange',title:'转结状态',align:'center', formatter:paidByMobileOrder.convert.showHasChange, width:100},
			{field:'thirdPayNumber',title:'第三方支付流水',align:'center', width:100},
			{field:'payTime',title:'代付时间',align:'center', width:100},
			{field:'payType',title:'代付方式',align:'center',formatter:paidByMobileOrder.convert.showPayType, width:100},
			{field:'payStatus',title:'代付状态',align:'center',formatter:paidByMobileOrder.convert.showPayStatus, width:100},
			{field:'payeeMobile',title:'收款方手机',align:'center', width:100},
			{field:'orderTypeStr',title:'订单类型',align:'center', width:100},
			{field:'userTypeStr',title:'收款方',align:'center', width:100}
//			{field:'operate',title:'操作',align:'center',formatter:paidByMobileOrder.operate.view}
		]]
	}); 
	//分页加载
	$("#paidOrderByMobiledg").datagrid("getPager").pagination({
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
	return "<a class='operate' href='javascript:;' onclick=paidByMobileOrder.viewAction('"+value+"');>"+value+"</a>";
}

function view(val,row,index){
	return "<a class='operate' href='javascript:;' onclick=paidByMobileOrder.viewAction('"+row.id+"');>查看</a>";
}
function viewAction(orderNo){
	$('#editDialog').dialog({'title':'交易详情','href':paidByMobileOrder.url.queryDetailUrl+'?orderNo='+orderNo}).dialog('open');
}
$(function(){
	paidByMobileOrder.initPageList();
});