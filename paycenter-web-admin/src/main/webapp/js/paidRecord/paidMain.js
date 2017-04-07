/**
 * 代付款记录
 */
var disableExport = false;
var paid = {
	//所需要访问url
	url : {
		//查询分页
		queryByPageListUrl : CONTEXT+'paid/queryPage',
		//导出数据检查
		exportDataCheckUrl:CONTEXT+'paid/checkExportParams',
		//导出数据url
		exportDataUrl:CONTEXT+'paid/exportExcel',
		//代付订单记录
		orderDetailUrl:CONTEXT+'paycenter/paidOrderRecord/showOrderList',
		//代付市场佣金记录
		commissionDetailUrl:CONTEXT+'paycenter/paidCommissionRecord/showCommissionList',
		//点击违约金总数跳转到违约金标签页
		penaltyRecordTab : CONTEXT + 'penaltyRecord/penaltyRecordTab',
		//点击退款总数跳转到退款标签页
		refundRecordTab : CONTEXT + 'refundRecord/refundRecordTab',
		//转结算
		saveTransferSettlementUrl:CONTEXT+'paid/insertSettlement'
	},
	//获取查询所需参数
	queryParams : getQueryParams,
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
		orderCount : orderCountFommatter,
		commissionCount: commissionCountFormatter,
		penaltyCount : penaltyCountFormatter,
		refundCount : refundCountFormatter,
		amt : amtFormatter
	},
	// 打开转结算操作页面
	tranferSettlement : transferSettlement,
	// 转结算
	saveTransferSettlement : saveTransferSettlement
};
function getQueryParams() {
	var greaterZero_checked = $("#searchform #greaterZero").attr("checked");
	var greaterZero = "";
	if(greaterZero_checked) {
		greaterZero = "1";
	}
	
	var params = {
		account:$("#searchform #account").val(),
		mobile:$("#searchform #mobile").val(),
		startAmt:$("#searchform #startAmt").val(),
		endAmt:$("#searchform #endAmt").val(),
		greaterZero:greaterZero
	};
	return params;
}

function orderCountFommatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	return "<a href='javascript:void(0)' onclick=\"openOrderDetail('"+row.memberId+"')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";
}

function commissionCountFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	return "<a href='javascript:void(0)' onclick=\"openCommissionDetail('"+row.memberId+"')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";
}

function penaltyCountFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	return "<a href='javascript:void(0)' onclick=\"openPenaltyRecordTab('"+row.memberId+"')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";
}

function refundCountFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	return "<a href='javascript:void(0)' onclick=\"openRefundRecordTab('"+row.memberId+"')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";
}

//待支付金额格式化，处理科学计数法
function amtFormatter(val, row, index) {
	if(val != undefined) {
		return val;
	}
	return "";
}

//打开转结算页面
function transferSettlement() {
	$('#transferDialog').dialog({'title':'转结算', 'width':450, 'height':'300', 'href':CONTEXT+'paid/tranferSettlement'}).dialog('open');
}

//转结算
function saveTransferSettlement() {
	var chk_value =[]; 
	$('input[name="type"]:checked').each(function(){ 
		chk_value.push($(this).val()); 
	}); 
	
	var type =""; //类型：0，订单和佣金；1，订单；2，佣金
	var len = chk_value.length;
	if(len == 0) {
		alert("选择结算类型");
		return;
	}
	else if(len < 4) {
		for(var i=0;i<len;i++){
			type += chk_value[i] + ","
		}
	}
	else if(len == 4) {
		type = 0;
	}
	var settleDate = $("#transferForm #settleDate").val();
	if(settleDate == undefined || settleDate == "") {
		alert("选择结算时间");
		return;
	}
	var greaterZero_checked = $("#searchform #greaterZero").attr("checked");
	var greaterZero = "";
	if(greaterZero_checked) {
		greaterZero = "1";
	}
	var params = {
		type : type,
		settleDate:settleDate,
		account:$("#searchform #account").val(),
		mobile:$("#searchform #mobile").val(),
		startAmt:$("#searchform #startAmt").val(),
		endAmt:$("#searchform #endAmt").val(),
		greaterZero:greaterZero
	};
	$.ajax({
		async: false, 
		url: paid.url.saveTransferSettlementUrl,
		data:params,
		type:'post',
		dataType:"json",
		success : function(data){
			if(data.success){
				alert("转结算成功");
				$('#transferDialog').dialog("close");
				$('#paiddg').datagrid('load',{});
			}else{
				alert("转结算失败");
			}
		}
	});
}
//代付款-订单笔数详情
function openOrderDetail(memberId) {
	if(mobile==undefined){
		mobile = '';
	}
	var url = paid.url.orderDetailUrl+"?redirect=1&redirectPayStatus=0&hasChange=0&memberId="+memberId;
	addTab("代付款-订单记录", url);
}
//代付款-佣金笔数详情
function openCommissionDetail(memberId) {
	if(mobile==undefined){
		mobile = '';
	}
	var url = paid.url.commissionDetailUrl+"?redirect=1&redirectPayStatus=0&hasChange=0&memberId="+memberId;
	addTab("代付款-市场佣金记录", url);
}
//代付款-违约金笔数详情
function openPenaltyRecordTab(memberId) {
	if(mobile==undefined){
		mobile = '';
	}
	var url = paid.url.penaltyRecordTab+"?hasChange=0&memberId="+memberId;
	addTab("代付款-违约金记录", url);
}
//代付款-退款笔数详情
function openRefundRecordTab(memberId) {
	if(mobile==undefined){
		mobile = '';
	}
	var url = paid.url.refundRecordTab+"?hasChange=0&memberId="+memberId;
	addTab("代付款-退款记录", url);
}

function addTab(title,url){
    var jq = top.jQuery; 
    if (jq("#my_tabs").tabs('exists', title)){
    	jq("#my_tabs").tabs('close', title); 
    }
    var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
    jq("#my_tabs").tabs('add',{title:title,content:content,closable:true});        
} 

function initPageList(){
	var queryParams=paid.queryParams();
	//数据加载
	$('#paiddg').datagrid({
		url:paid.url.queryByPageListUrl,
		queryParams:queryParams , 
		height: 'auto', 
		nowrap:true,
		toolbar:'#paidtb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
			{field:'account',title:'用户账号',align:'center',width:200},
			{field:'mobile',title:'用户手机号',align:'center',width:200},
			{field:'orderCount',title:'代付订单笔数',align:'center',width:200, formatter:paid.convert.orderCount},
			{field:'refundCount',title:'代付退款笔数',align:'center',width:200, formatter:paid.convert.refundCount},
			{field:'penaltyCount',title:'代付违约金笔数',align:'center',width:200, formatter:paid.convert.penaltyCount},
			{field:'commissionCount',title:'代付市场佣金笔数',align:'center',width:200, formatter:paid.convert.commissionCount},
			{field:'amtStr',title:'代付款金额',align:'center',width:200,formatter:paid.convert.amt}
		]],
		onLoadSuccess:function(data) {
			if(data.code && data.code == 60003) {
				warningMessage(data.msg);
			}
		}
	}); 
	//分页加载
	$("#paiddg").datagrid("getPager").pagination({
		pageList: [10,20,50,100]
	});
}

function exportData(){
	if (disableExport){
		slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");
		return;
	}
	disableExport = true;
	var params = paid.queryParams();
	var paramList = "account="+params.account+"&mobile="+params.mobile+
		"&startAmt="+params.startAmt+"&endAmt="+params.endAmt+"&greaterZero="+params.greaterZero;
	$.ajax({
		url: paid.url.exportDataCheckUrl,
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				if (disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					//启动下载
					$.download(paid.url.exportDataUrl,paramList,'post');
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
	paid.initPageList();
});