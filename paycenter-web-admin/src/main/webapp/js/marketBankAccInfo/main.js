$(document).ready(function(){
	load(null,CONTEXT+'marketBankAccInfo/getMarketBankAccInfoList');
	initMarketList();
});
$('#btn-search').click(function(){ 
	var params={
			"marketId":$("#marketId").val(),
			"mobile":$("#mobile").val()
	}
	load(params,CONTEXT+'marketBankAccInfo/getMarketBankAccInfoList');
});

function load(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#marketdg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:true,
		toolbar:'#markettb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
		          	{field:'id',title:'',width:100,checkbox:true},
					{field:'marketName',title:'所属市场',width:150,align:'center'},
					{field:'account',title:'用户账号',width:150,align:'center'},
					{field:'mobile',title:'用户手机号',width:80,align:'center'},
					{field:'realName',title:'持卡人',width:80,align:'center'},
					{field:'depositBankName',title:'开户银行名称',width:80,align:'center'},
					{field:'addr',title:'开户所在地',width:100,align:'center'},
					{field:'subBankName',title:'支行名称',width:100,align:'center'},
					{field:'bankCardNo',title:'银行卡号',width:150,align:'center'},
					{field:'opt',title:'操作',width:100,align:'center',formatter:optFormat}
				]]
	}); 
	
}

//重置
$("#btn-reset").click(function(){
	$("#marketSearchForm")[0].reset();
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
		url: CONTEXT+'marketBankAccInfo/queryMarketList',
		type:'post',
		dataType:"json",
		success : function(data){
			result = data.result;
		}
	});
	return result;
}


$("#btn-add").click(function(){
	$('#addDialog').dialog({'title':'新增市场信息', 'width':500, 'height':360, 'href':CONTEXT+'marketBankAccInfo/addDialog'}).dialog('open');
});

function openEditDialog(id){
	$('#editDialog').dialog({'title':'修改市场信息', 'width':500, 'height':360, 'href':CONTEXT+'marketBankAccInfo/editDialog/'+id}).dialog('open');
};

function openShowDialog(id){
	$('#showDialog').dialog({'title':'查看市场信息', 'width':500, 'height':360, 'href':CONTEXT+'marketBankAccInfo/showDialog/'+id}).dialog('open');
}

//批量删除
$("#btn-delete").click(function(){
	var rows = $('#marketdg').datagrid('getSelections');
	if(rows.length==0) {
		warningMessage("请先选择要操作的记录...");
		return;
	}
	jQuery.messager.confirm('提示', '您确定要删除所选数据吗?', function(r){
		if (r){
			var ids = getSelections("id");
			$.ajax({
		        type: "POST",
		        url: CONTEXT + "marketBankAccInfo/delete",
		        data:{"ids": ids},
		        dataType: "json",
		        success: function(data){
		        	if (data.code == 10000) {
		    			slideMessage("操作成功！");
		    			$("#marketdg").datagrid('reload');
		    		}else{
		    			warningMessage(data.result);
		    			return;
		    		}
		        },
		        error: function(){
		        	warningMessage("服务器出错");
		        }
			});
		}
	});
});

//新增弹出框保存
$("#btn-add-save").click(function(){
	if(!$("#addMarketForm").form('validate')){
		return false;
	}
	$.ajax({
        type: "POST",
        url: CONTEXT + "marketBankAccInfo/save",
        data:$('#addMarketForm').serialize(),
        dataType: "json",
        success: function(data){
        	if (data.code == 10000) {
    			slideMessage("操作成功！");
    			$('#addDialog').dialog('close');
    			$("#marketdg").datagrid('reload');
    		}else{
    			warningMessage(data.result);
    			return;
    		}
        },
        error: function(){
        	warningMessage("服务器出错");
        }
	});
});

//修改弹出框保存
$("#btn-edit-save").click(function(){
	if(!$("#editMarketForm").form('validate')){
		return false;
	}
	jQuery.messager.confirm('提示', '您确定要修改所选数据吗?', function(r){
		if (r){
			$.ajax({
		        type: "POST",
		        url: CONTEXT + "marketBankAccInfo/edit",
		        data:$('#editMarketForm').serialize(),
		        dataType: "json",
		        success: function(data){
		        	if (data.code == 10000) {
		    			slideMessage("操作成功！");
		    			$('#editDialog').dialog('close');
		    			$("#marketdg").datagrid('reload');
		    		}else{
		    			warningMessage(data.result);
		    			return;
		    		}
		        },
		        error: function(){
		        	warningMessage("服务器出错");
		        }
			});
		}
	});
});



