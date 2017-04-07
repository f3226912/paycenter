$(document).ready(function(){
	load(null,CONTEXT+'bankRateConfigController/getBankRateConfigList');
	payChannel();
});
$('#icon-search').click(function(){
	var params={ "payChannel":$("#bankRateConfigSearchForm #payChannel").val(),
			     "cardType":$("#bankRateConfigSearchForm #cardType").val(),
			     "areaBankFlag":$("#bankRateConfigSearchForm #areaBankFlag").val(),
			     "type":$("#bankRateConfigSearchForm #type").val()
	        }
	load(params,CONTEXT+'bankRateConfigController/getBankRateConfigList');
});

function load(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#bankRateConfigdg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:false,
		toolbar:'#bankRateConfigtb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singSelect:true,
		fit:true,
		columns:[[
					{field:'payChannelName',title:'支付渠道',width:100,align:'center'},
					{field:'cardTypeStr',title:'卡类型',width:100,align:'center'},
					{field:'business',title:'业务类型',width:100,align:'center'},
					{field:'proceduresStr',title:'手续费',width:100,align:'center'},
					{field:'statusStr',title:'状态',width:100,align:'center'},
					{field:'opt',title:'操作',width:100,align:'center',formatter:optFormat},
				]]
	}); 
}

function showbankRateConfigDetail(id){
	$('#editBankRateConfigDialog').dialog({'title':'查看','href':CONTEXT+'bankRateConfigController/editById?id='+id+"&vStatus=0",'width': 600,'height': 450}).dialog('open');
}
function updatebankRateConfigDetail(id){
	$('#editBankRateConfigDialog').dialog({'title':'修改','href':CONTEXT+'bankRateConfigController/editById?id='+id+"&vStatus=1",'width': 600,'height': 450}).dialog('open');
}


$("#btn-add").click(function(){
	$('#addBankRateConfigDialog').dialog({'title':'新增渠道费率', 'width':600, 'height':450, 'href':CONTEXT+'bankRateConfigController/addBankRateConfig'}).dialog('open');
});

function payChannel(){
	$("#bankRateConfigSearchForm #payChannel").html("<option value=''>全部</option>");
	var areaList = payChannelAll();
	$.each(areaList, function(i, item){
		$("#bankRateConfigSearchForm #payChannel").append("<option value='"+item.id+"'>"+item.payTypeName+"</option>"); 
	});
}
function payChannelAll(){ 
	var result;
	$.ajax({
		async: false, 
		url: CONTEXT+'bankRateConfigController/getPayChannel',
		type:'post',
		dataType:"json",
		success : function(data){
			result = data.result;
		}
	});
	return result;
}

