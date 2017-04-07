$(document).ready(function(){
	load(null,CONTEXT+'posMachineConfig/getPosMachineConfigList');
	initMarketList();
});
$('#btn-search').click(function(){ 
	var params={
			"marketId":$("#marketId").val(),
			"machineNum":$("#machineNum").val(),
			"shopsName":$("#shopsName").val()
	}
	load(params,CONTEXT+'posMachineConfig/getPosMachineConfigList');
});

function load(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#posdg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:true,
		toolbar:'#postb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
					{field:'machineNum',title:'终端号',width:100,align:'center'},
					{field:'businessId',title:'商铺ID',width:50,align:'center'},
					{field:'shopsName',title:'商铺名称',width:150,align:'center'},
					{field:'marketName',title:'所属市场',width:150,align:'center'},
					{field:'userTypeStr',title:'用户类型',width:80,align:'center'},
					{field:'mobile',title:'用户手机号',width:80,align:'center'},
					{field:'account',title:'用户账号',width:150,align:'center'},
					{field:'createTimeStr',title:'绑定时间',width:150,align:'center'}
				]]
	}); 
	
}

//重置
$("#btn-reset").click(function(){
	$("#posSearchForm")[0].reset();
});


function initMarketList(){
	var marketList = queryMarketList();
	$.each(marketList, function(i, item){
		$("#marketId").append("<option value='"+item.id+"'>"+item.marketName+"</option>");   
	}); 
}

function queryMarketList(){
	var result;
	$.ajax({
		async: false, 
		url: CONTEXT+'posMachineConfig/queryMarketList',
		type:'post',
		dataType:"json",
		success : function(data){
			result = data.result;
		}
	});
	return result;
}





