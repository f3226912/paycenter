/**
 * 通过电话查询代付佣金记录
 */
var disableExport = false;
var commissionByMobileOrder = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'paycenter/paidCommissionRecord/queryPaidCommissionRecordList',
		//查询详情
		queryDetailUrl:CONTEXT+'paycenter/paytrade/detail',
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
		queryParams.payStatus=$("#redirectPayStatus").val();
		queryParams.memberId=$("#memberId").val();
		queryParams.hasChange=$("#hasChange").val();
		queryParams.batNo=$("#batNo").val();
	//数据加载
	$('#paidCommissionByMobiledg').datagrid({
		url:commissionByMobileOrder.url.queryByPageListUrl,
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
			{field:'orderNo',title:'关联订单号',align:'center', formatter:orderNoFormatter, width:150},
			{field:'orderTime',title:'下单时间',align:'center', width:100},
			{field:'totalAmt',title:'商品总金额',align:'center', width:100},
			{field:'commissionAmt',title:'支付佣金',align:'center', width:100},
			{field:'commissionAmt',title:'代付款金额',align:'center', width:100},
			{field:'hasChange',title:'转结状态',align:'center', formatter:commissionByMobileOrder.convert.showHasChange, width:100},
			{field:'thirdPayNumber',title:'第三方支付流水',align:'center', width:100},
			{field:'payTime',title:'代付时间',align:'center', width:100},
			{field:'payType',title:'代付方式',align:'center',formatter:commissionByMobileOrder.convert.showPayType, width:100},
			{field:'payStatus',title:'代付状态',align:'center',formatter:commissionByMobileOrder.convert.showPayStatus, width:100},
			{field:'mobile',title:'收款方手机',align:'center', width:100},
			{field:'marketName',title:'市场名称',align:'center', width:100},
//			{field:'userType',title:'收款方来源',align:'center',formatter:commissionByMobileOrder.convert.showUserType},
			{field:'userType',title:'佣金支出方',align:'center',formatter:commissionByMobileOrder.convert.showUserType, width:100},
//			{field:'operate',title:'操作',width:100,align:'center',formatter:commissionByMobileOrder.operate.view}
		]]
	}); 
	//分页加载
	$("#paidCommissionByMobiledg").datagrid("getPager").pagination({
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
	return "<a class='operate' href='javascript:;' onclick=commissionByMobileOrder.viewAction('"+value+"');>"+value+"</a>";
}

function view(val,row,index){
	return "<a class='operate' href='javascript:;' onclick=commissionByMobileOrder.viewAction('"+row.id+"');>查看</a>";
}
function viewAction(orderNo){
	$('#editDialog').dialog({'title':'交易详情','href':commissionByMobileOrder.url.queryDetailUrl+'?orderNo='+orderNo}).dialog('open');
}
$(function(){
	commissionByMobileOrder.initPageList();
});