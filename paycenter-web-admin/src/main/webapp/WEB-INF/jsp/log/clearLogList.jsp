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
<table id="clearlogTable" title=""></table>
<div id="clearlogsearch" style="padding:5px;height:auto">
	<form id="clearlogsearchform" method="post">
		<table>
			<tr>
				<!-- <td>支付渠道:</td>
				<td>
					<select id="payType" name="payType">
						<option value="">请选择</option>
						<option value="ALIPAY_H5">支付宝</option>
						<option value="WEIXIN_APP">微信</option>
						<option value="POS">POS刷卡</option>
					</select>
				</td> -->
				<td>交易支付日期:</td>
				<td>
					<input  type="text"  id="payTimeBegin" name="payTimeBegin"  
					onFocus="WdatePicker({onpicked:function(){payTimeBegin.focus();},maxDate:'#F{$dp.$D(\'payTimeEnd\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){payTimeBegin.focus();},maxDate:'#F{$dp.$D(\'payTimeEnd\')}',dateFmt:'yyyy-MM-dd'})"    
					style="width:150px" placeholder="开始时间">
				</td>
				<td>~</td>
				<td>
					<input  type="text"    id="payTimeEnd" name="payTimeEnd"   
					onFocus="WdatePicker({onpicked:function(){payTimeEnd.focus();},minDate:'#F{$dp.$D(\'payTimeBegin\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){payTimeEnd.focus();},minDate:'#F{$dp.$D(\'payTimeBegin\')}',dateFmt:'yyyy-MM-dd'})"
					style="width:150px" placeholder="结束时间">
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
			<gd:btn btncode='BTNRECLEAR'>	
				<a class="easyui-linkbutton" iconCls="icon-edit" id="btn-reclear">重新清分</a>
			</gd:btn>
		</td>
	</form>
</div>
<div id="clearLogDetailDialog" class="easyui-dialog" style="width:900px;height:400px;" closed="true" modal="true" buttons="#clearLogDetailBtn">
	<div id="clearLogDetailBtn" style="text-align:center">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#clearLogDetailDialog').dialog('close')">关闭</a>
	</div>
</div>
<div id="reclearDialog" class="easyui-dialog" style="width:400px;height:200px;" closed="true" modal="true" buttons="#reclearBtn">
	<div id="reclearBtn" style="text-align:center">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:save()">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#reclearDialog').dialog('close')">关闭</a>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	load(null);
});
function load(params){
	params = !params ? {}: params;
	$('#clearlogTable').datagrid({
		url:CONTEXT+'clear/log/list',
		queryParams: params,
		height: 'auto', 
		nowrap:false,
		toolbar:'#clearlogsearch',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singSelect:true,
		fit:true,
		columns:[[
				//{field:'payType',title:'支付渠道',width:100,align:'center',formatter:payTypeFormat},
				{field:'payTime',title:'交易支付日期',width:200,align:'center',formatter:payTimeFormat},
				{field:'clearNum',title:'清分笔数',width:100,align:'center'},
				{field:'clearAmt',title:'清分金额',width:100,align:'center',formatter:clearAmtFormat},
				{field:'clearSuccessNum',title:'清分成功笔数',width:100,align:'center'},
				{field:'clearSuccessAmt',title:'清分成功金额',width:100,align:'center',formatter:clearSuccessAmtFormat},
				{field:'clearFailNum',title:'清分失败笔数',width:100,align:'center'},
				{field:'clearFailAmt',title:'清分失败金额',width:100,align:'center'},
				{field:'opt',title:'操作',width:200,align:'center',formatter:optFormat}
				]]
	}); 
}

$('#icon-search').click(function(){
	var params={
		"payType":$("#payType").val(),
		"payTimeBegin":$("#payTimeBegin").val(),
		"payTimeEnd":$('#payTimeEnd').val(),
	}
	load(params);
});

$('#btn-reset').click(function(){
	$("#clearlogsearchform")[0].reset();
});

$('#icon-refresh').click(function(){
	$("#clearlogsearchform")[0].reset();
	$("#clearlogTable").datagrid('load',null);
});

$('#btn-reclear').click(function(){
	$('#reclearDialog').dialog({'title':'重新清分','href':CONTEXT+'clear/reclear/index'}).dialog('open');
});

function payTimeFormat(value,row,index){
	if (!value){
	}else{
		return value.split(" ")[0];
	}
}

function clearAmtFormat(value,row,index){
	if(!value){
	}else{
		return value.toFixed(2);
	}
}

function clearSuccessAmtFormat(value,row,index){
	if(!value){
	}else{
		return value.toFixed(2);
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

function optFormat(value,row,index){
	var opt = "";
	opt += "<gd:btn btncode='OPERRECLEAR'><a class='operate' href='javascript:;' onclick=reclear('"+row.payType+"','"+encodeURI(row.payTime)+"');>重新清分</a></gd:btn>";
	opt += "<gd:btn btncode='SHOWCLEARLOGDETAIL'><a class='operate' href='javascript:;' onclick=showClearLogDetail('"+row.id+"');>清分日志</a></gd:btn>";
	return opt;
}

function reclear(payType,payTime){
	ajaxReClear({
		'payTime':decodeURI(payTime)
	});
}

function save(){
	var payTime = $('#reClearPayTime').val();
	if(!payTime){
		warningMessage("请选择交易支付日期!");
		return;
	}
	ajaxReClear({
		'payTime':payTime
	});
}

function ajaxReClear(params){
	$.post(CONTEXT+'clear/reclear',params,function(result){
		if(result.success){
			$('#reclearDialog').dialog('close');
			slideMessage("清分成功,并生成了对应清分文件！");
			load(null);
		}else{
			slideMessage("清分失败！");
		}
	});
}

function showClearLogDetail(id){
	$('#clearLogDetailDialog').dialog({'title':'清分详情','href':CONTEXT+'clear/log/detail/index?id='+id}).dialog('open');
}
</script>
</body>
</html>