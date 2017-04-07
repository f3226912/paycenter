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
		<title>平台佣金收益</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<body>
	<table id="platformCommissiondg" title="">
	</table>
	<div id="platformCommissiontb" style="padding:5px;height:auto">
		<form id="platformCommissionSearchForm" method="post">
		<div>
			佣金支出方：
					<select type="text" id="userType" name="userType">
						<option value="">全部</option>
						<option value="nsy">农商友</option>
						<option value="nps">农批商</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
			关联订单号: <input class="easyui-searchbox" type="text" id="orderNo" name="orderNo" style="width:150px" data-options="prompt:'请输入关联订单号'">&nbsp;&nbsp;&nbsp;&nbsp;
			关联用户手机: <input class="easyui-searchbox" type="text" id="payerMobile" name="payerMobile" style="width:150px" data-options="prompt:'请输入关联用户手机'">&nbsp;&nbsp;&nbsp;&nbsp;
			下单时间: 
			<input  type="text" id="orderTimeBegin" name="orderTimeBegin"  
					onFocus="WdatePicker({onpicked:function(){orderTimeBegin.focus();},maxDate:'#F{$dp.$D(\'orderTimeEnd\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){orderTimeBegin.focus();},maxDate:'#F{$dp.$D(\'orderTimeEnd\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> -
			<input  type="text" id="orderTimeEnd" name="orderTimeEnd"   
					onFocus="WdatePicker({onpicked:function(){orderTimeEnd.focus();},minDate:'#F{$dp.$D(\'orderTimeBegin\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){orderTimeEnd.focus();},minDate:'#F{$dp.$D(\'orderTimeBegin\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
        <gd:btn btncode='BTNPTYJSY01'><a class="easyui-linkbutton" iconCls="icon-search" onclick="query()" >查询</a></gd:btn>&nbsp;&nbsp;
        <!-- OnClick ="return $('#platformCommissionSearchForm').form('validate');" id="icon-searchList"-->
        </div>
        
			<div style="background:#efefef;padding:5px 0 5px 0;height:25px;">
			<div style="float: left;font-size:14px;margin-left:5px;">平台佣金收益</div>
			<div style="float:right;margin-right:10px;">
					<gd:btn btncode='BTNPTYJSY02'>	
						<a class="easyui-linkbutton" iconCls="icon-account-excel" id="btn-export" onclick="exportData()">导出EXCEL</a>
					</gd:btn>
			</div>
		</div>
		</form>
	</div>
	<div id="editDialog" class="easyui-dialog" style="width:900px;height:500px;overflow: auto;"  class="easyui-dialog" closed="true" modal="true"  buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
	
</body>
<script type="text/javascript" src="${CONTEXT}js/profit/main.js"></script>

</html>
