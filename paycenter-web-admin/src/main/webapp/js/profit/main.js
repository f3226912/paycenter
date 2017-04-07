$(document).ready(function(){
	loadData(null,CONTEXT+'profit/platformCommissionList');
});
var disableExport = false;
function getSearchParams(){
	var params={
			"userType":$("#userType").val(),
			"orderTimeBegin":$("#orderTimeBegin").val(),
			"orderTimeEnd":$("#orderTimeEnd").val(),
			"orderNo":$("#orderNo").searchbox("getValue"),
			"payerMobile":$("#payerMobile").searchbox("getValue")
		};
	return params;
}

$('#icon-searchList').click(function(){
	disableExport = false;
	loadData(getSearchParams(),CONTEXT+'profit/platformCommissionList');
});

//查询
function query(){
	var dataParams=getSearchParams();
	loadData(dataParams,CONTEXT+'profit/platformCommissionList');
}

function loadData(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#platformCommissiondg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:true,
		toolbar:'#platformCommissiontb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		remoteSort:false,
		striped:true, 
		
		columns:[[
                    {field:'id',checkbox:true, hidden:true},
					{field:'orderNo',title:'关联订单号',width:110,align:'center',sortable:true,formatter:checkTradeDetailFormatter},
					{field:'payerMobile',title:'关联用户手机',width:100},
					{field:'totalAmt',title:'商品总金额',width:100,formatter:formatDouble},
					{field:'commission',title:'佣金收益',width:100,formatter:formatDouble},
					{field:'orderTime',title:'下单时间',width:100},
					{field:'userType',title:'佣金支出方',width:110,align:'center',formatter:formatUserType}
				]],
				//过滤提示签名验证
				loadFilter: function(data){
				//数字签名验证异常
					if (data.result&&data.code=='60003'){
						warningMessage(data.msg);
						return data.result;
					} else {
						return data.result;
					}
				}
	}); 
	
}

function formatDouble(v,r) {
	if(v!=null) {
		return v.toFixed(2)
	}
	return "-";
}

function formatUserType(val, row, index){
	if(val == 'nsy'){
		return "农商友";
	} else if(val == 'nps'){
		return "农批商";
	} 
}

function checkTradeDetailFormatter(val, row, index) {
	return "<a class='operate' href='javascript:;' onclick=checkTradeDetail('"+row.orderNo+"'); >"+val+"</a>";
}

/**
 * 交易详情
 * */
function checkTradeDetail(orderNo) {
	/*var rows = $('#platformCommissiondg').datagrid("getRows");
	var curRow = rows[rowIndex];
	var orderNo = curRow.orderNo;*/
	$('#editDialog').dialog({'title':'交易详情','href':CONTEXT+'paycenter/paytrade/detail?orderNo='+orderNo}).dialog('open');
	$('#platformCommissiondg').datagrid("uncheckAll");
	
}

/**导出数据
 * 
 */
function exportData(){
	if (disableExport){
		slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");
		return;
	}
	disableExport = true;
	var params = getSearchParams();
	var paramList = "userType="+params.userType+"&orderNo="+params.orderNo+
		"&payerMobile="+params.payerMobile+"&orderTimeBegin="+params.orderTimeBegin+"&orderTimeEnd="+params.orderTimeEnd;
	$.ajax({
		url: CONTEXT+'/profit/checkPlatCommissionParam',
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
					$.download(CONTEXT+'/profit/exportPlatCommission',params,'post');
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

