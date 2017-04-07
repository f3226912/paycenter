<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<%@ include file="../pub/constants.inc" %>
		<%@ include file="../pub/head.inc" %>
		<%@ include file="../pub/tags.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=7, IE=9, IE=10, IE=11, IE=12"/>
		<title>代付手续费</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<body>
<table id="feepay" title=""></table>
<div id="feepaysearch" style="padding:5px;height:auto">
	<form id="feepaysearchform" method="post">
		<table>
			<tr>
				<td>第三方支付流水:</td>
				<td>
					<input type="text" id="thridPayNumber" name="thridPayNumber">
				</td>
				<td>代付时间:</td>
				<td>
					<input  type="text"  id="financeBeginTime" name="financeBeginTime"  
					onFocus="WdatePicker({onpicked:function(){financeBeginTime.focus();},maxDate:'#F{$dp.$D(\'financeEndTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){financeBeginTime.focus();},maxDate:'#F{$dp.$D(\'financeEndTime\')}',dateFmt:'yyyy-MM-dd'})"    
					style="width:150px" >
				</td>
				<td>~</td>
				<td>
					<input  type="text"    id="financeEndTime" name="financeEndTime"   
					onFocus="WdatePicker({onpicked:function(){financeEndTime.focus();},minDate:'#F{$dp.$D(\'financeBeginTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){financeEndTime.focus();},minDate:'#F{$dp.$D(\'financeBeginTime\')}',dateFmt:'yyyy-MM-dd'})">
				</td>
					<td>
					<a class="easyui-linkbutton icon-search-btn" iconCls="icon-search" id="icon-search" OnClick ="return $('#feepaysearchform').form('validate');">查询</a>
					</td>
					<td>
					<a class="easyui-linkbutton icon-reload-btn" iconCls="icon-reload" id="btn-reset">重置</a>
					</td>
			</tr>
		</table>
		<td>
			<a class="easyui-linkbutton" iconCls="icon-reload" id="icon-refresh">刷新</a>
		</td>
		<td>
			<gd:btn btncode='BTNEXPORT210302'>	
				<a class="easyui-linkbutton" iconCls="icon-account-excel" id="btn-import">导出EXCEL</a>
			</gd:btn>
		</td>
	</form>
</div>
<script type="text/javascript">
$(document).ready(function(){
	load({
		'feeType':1
	});
});

$('#icon-search').click(function(){
	var params={
		"feeType":1,
		"financeBeginTime":$("#financeBeginTime").val(),
		"financeEndTime":$("#financeEndTime").val(),
		"thridPayNumber":$('#thridPayNumber').val(),
	}
	load(params);
});

$("#btn-reset").click(function(){
	$("#feepaysearchform")[0].reset();
});

$("#icon-refresh").click(function(){
	$("#feepaysearchform")[0].reset();
	$("#feepay").datagrid('load',{
		'feeType':1
	});
});

$("#btn-import").click(function(){
	
	var params={
			"feeType":1,
			"financeBeginTime":$("#financeBeginTime").val(),
			"financeEndTime":$("#financeEndTime").val(),
			"thridPayNumber":$('#thridPayNumber').val(),
		}
	
	if(params.financeBeginTime==""||params.financeEndTime=="") {
		warningMessage("请选择待付时间");
		return;
	}
	
	var paramList = 'feeType=' + params.feeType + '&thridPayNumber='+params.thridPayNumber+ '&financeBeginTime=' + params.financeBeginTime + '&financeEndTime=' + params.financeEndTime;

	$.ajax({
		url: CONTEXT+'fee/checkPayExportParams',
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				slideMessage("数据正在导出中, 请耐心等待...");
				$.download(CONTEXT+'fee/exportPayData',paramList,'post' );
			}else{
				warningMessage(data.message);
			}
		},
		error : function(data){
			warningMessage("error : " + data);
		}
	});
	$("#feepaysearchform")[0].reset();
});



function load(params){
	params = !params ? {}: params;
	$('#feepay').datagrid({
		url:CONTEXT+'fee/getfeePayList',
		queryParams: params,
		height: 'auto', 
		nowrap:false,
		toolbar:'#feepaysearch',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singSelect:true,
		fit:true,
		columns:[[
				{field:'thridPayNumber',title:'第三方支付流水',width:100,align:'center',formatter:thridPayNumberFormat},
				{field:'payType',title:'支付方式',width:100,align:'center',formatter:payTypeFormat},
				{field:'payAmt',title:'代付金额',width:100,align:'center',formatter:payAmtFormat},
				{field:'feeAmt',title:'手续费',width:100,align:'center',formatter:feeAmtFormat},
				{field:'financeTime',title:'待付时间',width:100,align:'center'},
				]]
	}); 
}

function thridPayNumberFormat(value,row,index){
	if(!row.thridPayNumber){
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
</script>
</body>
</html>