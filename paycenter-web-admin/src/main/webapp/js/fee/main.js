$(document).ready(function(){
	load({
		'feeType':0
	},CONTEXT+'fee/getfeeList');
	getAppkeyList();
});
$('#icon-search').click(function(){
	disableExport = false;
	/*var params={"startBeginTime":$("#startBeginTime").val(),
			"startEndTime":$("#startEndTime").val(),
			"endBeginTime":$("#endBeginTime").val(),
			"endEndTime":$("#endEndTime").val(),};*/
	var params={
//		"appKey":$('#appKey').val(),
		"feeType":0,
		"financeBeginTime":$("#financeBeginTime").val(),
		"financeEndTime":$("#financeEndTime").val(),
		"thridPayNumber":$('#thridPayNumber').val(),
//		"orderNo":$('#orderNo').val()
	}
	load(params,CONTEXT+'fee/getfeeList');
});

function getAppkeyList(){
	$.ajax({
		type:'get',
		dataType: "json",
		url:CONTEXT+'fee/accsysconfig/all',
		success:function(result){
			if(result.success){
				$.each(result.data,function(id,item){
					$('#appKey').append("<option value='"+item.appKey+"'>"+item.name+"</option>");
				});
			}else{
				errorMessage('查询异常！');
			}
		},error:function(){
			errorMessage('请求错误');
		}
	});
	
}

function load(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#feedg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:false,
		toolbar:'#feetb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singSelect:true,
		fit:true,
		columns:[[
//					{field:'financeTime',title:'付款/转账时间',width:100,align:'center'},
//					{field:'orderNo',title:'订单号',width:100,align:'center',formatter:formatNumber},
//					{field:'thridPayNumber',title:'第三方支付/转账流水',width:100,align:'center',formatter:formatNumber},
//					{field:'feeType',title:'费用类型',width:100,align:'center',formatter:formatFeeType},
//					{field:'payAmt',title:'业务发生金额',width:100,align:'center',formatter:formatDouble},
//					{field:'rate',title:'费率',width:100,align:'center',formatter:formatHu},
//					{field:'feeAmt',title:'手续费',width:100,align:'center',formatter:formatDouble},
//					{field:'opt',title:'操作',width:100,align:'center',formatter:optFormat},
//					{field:'remark',title:'备注',width:200,align:'center',formatter:formatRemark}
//				{field:'appKey',title:'来源',width:100,align:'center',formatter:formatAppKey},
//				{field:'feeType',title:'手续费类型',width:100,align:'center',formatter:formatFeeType},
				{field:'thridPayNumber',title:'第三方支付流水',width:100,align:'center',formatter:thridPayNumberFormat},
//				{field:'orderNo',title:'订单号',width:100,align:'center',formatter:orderNoFormat},
				{field:'payType',title:'支付方式',width:100,align:'center',formatter:payTypeFormat},
				{field:'payAmt',title:'代收金额',width:100,align:'center',formatter:payAmtFormat},
//				{field:'rate',title:'费率/固定值',width:100,align:'center',formatter:rateFormat},
				{field:'feeAmt',title:'手续费',width:100,align:'center',formatter:feeAmtFormat},
				{field:'financeTime',title:'支付时间',width:100,align:'center'},
//				{field:'opt',title:'操作',width:100,align:'center',formatter:optFormat}
				]]
	}); 
	
}

function thridPayNumberFormat(value,row,index){
	if(!row.thridPayNumber){
		return "-";
	}
	return value;
}

function orderNoFormat(value,row,index){
	if(!row.orderNo){
		return "-";
	}
	return value;
}

function payTypeFormat(value,row,index){
	if(!row.payType){
		return "-";
	}
	return value;
}

function payAmtFormat(value,row,index){
	if(!row.payAmt){
		return "-";
	}
	return value.toFixed(2);
}
function feeAmtFormat(value,row,index){
	if(!row.feeAmt){
		return "-";
	}
	return value.toFixed(2);
}
function rateFormat(value,row,index){
	if(!row.rate){
		return "-";
	}
	return value;
}

function optFormat(value,row,index){
	var opt = "";
	opt += "<gd:btn btncode='FEESHOW03'><a class='operate' href='javascript:;' onclick=showFeeDetail('"+row.tradeId+"');>查看</a></gd:btn>";
	if(row.isSys == 1){
		opt += "<gd:btn btncode='FEEEDIT04'><a class='operate' href='javascript:;' onclick=updateFeeDetail('"+row.id+"');>修改</a></gd:btn>";
	}
	return opt;
}

function showFeeDetail(tradeId){
	$('#showFeeDetailDialog').dialog({'title':'交易详情','href':CONTEXT+'paycenter/paytrade/detail?id='+tradeId}).dialog('open');
}

function updateFeeDetail(id){
	$('#updateFeeDialog').dialog({'title':'修改手续费','href':CONTEXT+'fee/detail/get?id='+id,'width': 500,'height': 300}).dialog('open');
}

function update_submit(){
	var appKey = $('#edit_appKey').val();
	var feeType = $('#eidt_feeType').val();
	var feeAmt = $('#edit_feeAmt').val();
	var operaTime = $('#edit_operaTime').val();
	if(!appKey){
		warningMessage("请选择来源");
		return;
	}else if(!feeType){
		warningMessage("请选择手续费类型");
		return;
	}else if(!feeAmt){
		warningMessage("请填写手续费金额");
		return;
	}else if(!operaTime){
		warningMessage("请选择支付时间");
		return;
	}
	$.ajax({
		type:'post',
		dataType: "json",
		url:'detail/update',
		data:$('#editFeeDetailForm').form().serialize(),
		success:function(result){
			if(result.success){
				slideMessage("修改成功！");
				$('#updateFeeDialog').dialog('close');
				$("#feedg").datagrid('load',{});
				$('#editFeeDetailForm')[0].reset();
			}else{
				errorMessage('修改异常！');
			}
		},error:function(){
			errorMessage('请求错误');
		}
	});
}

function formatRemark(v,r) {
//	debugger
	if(v==null) {
		return "";
	}
	return v;
}

$("#btn-add").click(function(){
	showfee();
});

function formatNumber(v,r) {
	if(v==null) {
		return "-";
	}
	return v;
}

function formatHu(v,r) {
	if(v==null||v=='') {
		return "-";
	}
	return (Math.round(v * 10000)/100).toFixed(2) + '%';;
}

function formatDouble(v,r) {
	if(v!=null) {
		return v.toFixed(2)
	}
	return "-";
}

function formatFeeType(v,r) {
	if(v=="0") {
		return "交易手续费";
	} else if(v=="1") {
		return "转账手续费";
	} else if(v=="2") {
		return "其他";
	} else {
		return "";
	}
}

function formatAppKey(value,row,index){
	if(!row.appKey){
		return "-";
	}
	return row.appKey;
}

$("#icon-refresh").click(function(){
	disableExport = false;
	$("#feeSearchForm")[0].reset();
	$("#feedg").datagrid('load',{
		'feeType':0
	});
});
$("#btn-reset").click(function(){
	$("#feeSearchForm")[0].reset();
});

var disableExport = false;

$("#btn-import").click(function(){
	
	var params={
			"feeType":0,
			"financeBeginTime":$("#financeBeginTime").val(),
			"financeEndTime":$("#financeEndTime").val(),
			"thridPayNumber":$('#thridPayNumber').val(),
		}
	
	if(params.financeBeginTime==""||params.financeEndTime=="") {
		warningMessage("请选择支付时间");
		return;
	}
	
	var paramList = 'feeType=' + params.feeType +'&thridPayNumber='+params.thridPayNumber+ '&financeBeginTime=' + params.financeBeginTime + '&financeEndTime=' + params.financeEndTime;

	$.ajax({
		url: CONTEXT+'fee/checkExportParams',
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				slideMessage("数据正在导出中, 请耐心等待...");
				$.download(CONTEXT+'fee/exportData',paramList,'post' );
			}else{
				warningMessage(data.message);
			}
		},
		error : function(data){
			warningMessage("error : " + data);
		}
	});
	$("#feeSearchForm")[0].reset();
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
function showfee() {
	$('#feeDialog').dialog({'title':'添加手续费','href':CONTEXT+'fee/detail','width': 500,'height': 300}).dialog('open');
}


function save(){
	var params={
		"appKey":$('#addForm').find("#appKey").val(),
		"feeType":$('#addForm').find("#feeType").val(),
		"feeAmt":$('#addForm').find("#feeAmt").val(),
		"thridPayNumber":$('#addForm').find("#thridPayNumber").val(),
		"operaTime":$('#addForm').find("#operaTime").val(),
		"remark":$('#addForm').find("#remark").val()
	}
	if(!params.appKey){
		warningMessage("请选择手续费来源");
		return;
	}
	if(!params.feeType){
		warningMessage("请选择手续费类型");
		return;
	}
	if(!params.feeAmt){
		warningMessage("手续费必须为不超过两位的数字,范围在0.01-99999999.99");
		return;
	}
	if(!params.operaTime){
		warningMessage("请设置代收/代付时间");
		return;
	}
	$.ajax({
        type: "POST",
        url:"fee/save",
        dataType: "json",
        data: params,
        success: function(data){
        	data = eval(data);
        	if (data == "success") {
    			slideMessage("添加成功！");
    			$('#feeDialog').dialog('close');
    			$("#feeSearchForm")[0].reset();
    			$("#feedg").datagrid('load',{});
    		}else{
    			slideMessage("添加失败！");
    		}
        },
        error: function(){
        	warningMessage("服务器出错");
        }
	});
}

function setRed(btn) {
	btn.css("borderColor","#ff0000");
}

