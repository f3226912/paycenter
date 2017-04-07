$(document).ready(function(){
	load(null,CONTEXT+'bankinfo/getBankinfoList');
});
$('#icon-search').click(function(){ 
	disableExport = false;
	/*var params={"startBeginTime":$("#startBeginTime").val(),
			"startEndTime":$("#startEndTime").val(),
			"endBeginTime":$("#endBeginTime").val(),
			"endEndTime":$("#endEndTime").val(),};*/
	var params={
//			"account":$("#account").val(),
			"telephone":$("#telephone").val(),
			"realName":$("#realName").val(),
			"auditDoStatus":$("#auditDoStatus").val()
	}
	load(params,CONTEXT+'bankinfo/getBankinfoList');
});

function load(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#bankinfodg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:true,
		toolbar:'#bankinfotb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
			{field:'id',title:'',width:100,checkbox:true},
			{field:'telephone',title:'用户手机号',width:120,align:'center'},
			{field:'realName',title:'持卡人',width:100,align:'center'},
			{field:'depositBankName',title:'开户银行名称',width:100,align:'center'},
			{field:'bankPlace',title:'开户所在地',width:100,align:'center',formatter:formatPlace},
			{field:'subBankName',title:'支行名称',width:150,align:'center'},
			{field:'bankCardNo',title:'银行卡号',width:150,align:'center'},
			{field:'mobile',title:'银行卡预留手机',width:120,align:'center'},
			{field:'commitTime',title:'提交时间',width:150,align:'center'},
			{field:'auditStatus',title:'验证状态',width:100,align:'center',formatter:formatAudit},
			{field:'opt',title:'操作',width:100,align:'center',formatter:optFormat}		
//			{field:'account',title:'用户账号',width:100,align:'center'},
				]]
	}); 
	
}

$("#btn-pass").click(function(){
	batchPass(1);
});

$("#btn-unpass").click(function(){
	batchPass(0);
});

function batchPass(t) {
	var rows = $('#bankinfodg').datagrid('getSelections');
	
	if(rows.length==0) {
		warningMessage("请先选择要操作的记录...");
		return;
	}
	var ids = "";
	for(var i = 0 ; i < rows.length ; i++) {
		ids += rows[i].id;
		if(i!=rows.length) {
			ids += ",";
		}
	}
	
	$.ajax({
        type: "POST",
        url: CONTEXT + "bankinfo/pass/"+ids+"/"+ t,
        dataType: "json",
        success: function(data){
        	data = eval(data);
        	if (data == "success") {
    			slideMessage("操作成功！");
    			$('#bankinfoDialog').dialog('close');
    			$("#bankinfoSearchForm")[0].reset();
    			$("#bankinfodg").datagrid('load',{});
    		}else{
    			slideMessage("操作失败！");
    		}
        },
        error: function(){
        	warningMessage("服务器出错");
        }
	});
}

function formatPlace(v,r) {
	var str = "";
	if(r.provinceName !=null) {
		str += r.provinceName
	}
	if(r.cityName !=null) {
		str += r.cityName
	}
	if(r.areaName!=null) {
		str += r.areaName
	}
	return str;
}

function formatAudit(v,r) {
	if(v==null) {
		return "未验证"
	} else {
		if(v=="1") {
			return "验证通过";
		} else if(v=="0") {
			return "验证不通过";
		} else if(v=="2") {
			return "未验证";
		}
	}
}

function optFormat(value,row,index){
	opt = "<gd:btn btncode='BANKSHOW04'><a class='operate' href='javascript:;' onclick=showBankinfo('"+row.id+"','"+row.auditStatus+"');>查看</a></gd:btn>";
	var opt;
	if(row.auditStatus != 1) {
		opt += "<gd:btn btncode='BANKPASSSTATE05'><a class='operate' href='javascript:;' onclick=showBankinfo('"+row.id+"','"+row.auditStatus+"');>验证</a></gd:btn>";
	}
	return opt;
}

var idEdit;

$("#icon-refresh").click(function(){
	disableExport = false;
	$("#bankinfoSearchForm")[0].reset();
	$("#bankinfodg").datagrid('load',{});
});
$("#btn-reset").click(function(){
	$("#bankinfoSearchForm")[0].reset();
});

var disableExport = false;

$("#btn-import").click(function(){
	
	var params={
			"account":$("#account").val(),
			"telephone":$("#telephone").val(),
			"realName":$("#realName").val(),
			"auditDoStatus":$("#auditDoStatus").val()
	}
	
	var paramList = 'account=' + params.account + '&telephone=' + params.telephone + '&realName=' + params.realName + '&auditDoStatus=' + params.auditDoStatus;

	$.ajax({
		url: CONTEXT+'bankinfo/checkExportParams',
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				/* $("#Loading2").show(); */
				if (!disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					disableExport = true ;
					//启动下载
					$.download(CONTEXT+'bankinfo/exportData',paramList,'post' );
				}else{
					slideMessage("已进行过一次数据导出,导出功能已禁用,勿频繁点击导出,若要重新启用导出,请点击刷新按钮...");
				}
			}else{
				warningMessage(data.message);
			}
		},
		error : function(data){
			warningMessage("error : " + data);
		}
	});
	
	
	$("#bankinfoSearchForm")[0].reset();
});

jQuery.download = function(url, data, method){
	// 获得url和data
    if( url && data ){
        // data 是 string或者 array/object
        data = typeof data == 'string' ? data : jQuery.param(data);
        // 把参数组装成 form的  input
        var inputs = '';
        jQuery.each(data.split('&'), function(){
            var pair = this.split('=');
            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
        });
        // request发送请求
        jQuery('<form action="'+ url +'" method="'+ (method || 'post') +'">'+inputs+'</form>')
        	.appendTo('body').submit().remove();
    };
};

//修改
function showBankinfo(id,s) {
	idEdit = id;
	if(s=='1') {
		$("#pass").hide();
		$("#unpass").hide();
	} else {
		$("#pass").show();
		$("#unpass").show();
	}
	$('#bankinfoDialog').dialog({'title':'查看银行卡信息','href':CONTEXT+'bankinfo/detail/'+id,'width': 500,'height': 300}).dialog('open');
}

function pass(){
	$.ajax({
        type: "POST",
        url: CONTEXT + "bankinfo/pass/"+idEdit+"/"+"1",
        dataType: "json",
        success: function(data){
        	data = eval(data);
        	if (data == "success") {
    			slideMessage("操作成功！");
    			$('#bankinfoDialog').dialog('close');
    			$("#bankinfoSearchForm")[0].reset();
    			$("#bankinfodg").datagrid('load',{});
    		}else{
    			slideMessage("操作失败！");
    		}
        },
        error: function(){
        	warningMessage("服务器出错");
        }
	});
}

function unpass(){
	$.ajax({
        type: "POST",
        url: CONTEXT + "bankinfo/pass/"+idEdit+"/"+"0",
        dataType: "json",
        success: function(data){
        	data = eval(data);
        	if (data == "success") {
    			slideMessage("操作成功！");
    			$('#bankinfoDialog').dialog('close');
    			$("#bankinfoSearchForm")[0].reset();
    			$("#bankinfodg").datagrid('load',{});
    		}else{
    			slideMessage("操作失败！");
    		}
        },
        error: function(){
        	warningMessage("服务器出错");
        }
	});
}



