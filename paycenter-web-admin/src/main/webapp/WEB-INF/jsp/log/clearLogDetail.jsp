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
		<title>清分日志</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script>
<body>
<table id="clearlogDetailTable" title=""></table>
<script type="text/javascript">

$(document).ready(function(){
	var payType = '${clearSum.payType}';
	var payTime = '${clearSum.payTime}';
	loadDetail({
		'payType':payType,
		'payTime':payTime
	});
});

function loadDetail(params){
	params = !params ? {}: params;
	$('#clearlogDetailTable').datagrid({
		url:CONTEXT+'clear/log/detail/list',
		queryParams: params,
		height: 'auto', 
		nowrap:false,
		//toolbar:'#clearlogsearch',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singSelect:true,
		fit:true,
		columns:[[
				//{field:'payType',title:'支付渠道',width:100,align:'center',formatter:payTypeFormat},
				{field:'clearTime',title:'清分时间',width:200,align:'center',formatter:clearTimeFormat},
				{field:'payTime',title:'交易支付日期',width:200,align:'center',formatter:payTimeFormat},
				{field:'clearNum',title:'清分笔数',width:100,align:'center'},
				{field:'clearAmt',title:'清分金额',width:100,align:'center'},
				{field:'clearSuccessNum',title:'清分成功笔数',width:100,align:'center'},
				{field:'clearSuccessAmt',title:'清分成功金额',width:100,align:'center'},
				{field:'clearFailNum',title:'清分失败笔数',width:100,align:'center'},
				{field:'clearFailAmt',title:'清分失败金额',width:100,align:'center'},
				]]
	}); 
}

function clearTimeFormat(value,row,index){
	if(!value){
	}else{
		return value.split(".")[0];
	}
}

function payTimeFormat(value,row,index){
	if (!value){
	}else{
		return value.split(" ")[0];
	}
}


function payTypeFormat(value,row,index){
	if(value == 'ALIPAY_H5'){
		return '支付宝';
	}else if(value = 'WEIXIN_APP'){
		return '微信';
	}else if(value = 'POS'){
		return 'POS刷卡';
	}else{
		return value;
	}
}
</script>
</body>
</html>