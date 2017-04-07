<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/constants.inc" %>
<%@ include file="../../pub/tags.inc" %>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../../pub/head.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<title>代付退款记录</title>
	</head>
<body>
	<table id="refundRecorddg" title=""></table>
	<div id="refundRecordtb" style="padding:5px;height:auto">
		<form id="searchform" method="post">
		<div>
			收款方: 
			<select class="easyui-combobox" id="userType" name="userType" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="nsy">农商友</option>
				<option value="nps">农批商</option>
				<option value="market">供应商</option>
				<option value="nst_car">车主</option>
				<option value="nst_goods">货主</option>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;
			代付状态: 
			<select class="easyui-combobox" id="payStatus" name="payStatus" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="0">待付款</option>
				<option value="1">付款成功</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			转结状态:
			 <select class="easyui-combobox" id="hasChange"  name="hasChange" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="0">未转结</option>
				<option value="1">已转结</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			收款方手机:  
			<input type="text" id="payeeMobile" name="payeeMobile" maxlength="100"
								style="width:150px" placeholder="请输入收款方手机">&nbsp;&nbsp;&nbsp;&nbsp;
			退款编号:		
			<input type="text" id="refundNo" name="refundNo" maxlength="100"
								style="width:150px" placeholder="请输入订单号">&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        
        <div style="margin-top:10px;">
        	关联订单号:		
			<input type="text" id="orderNo" name="orderNo" maxlength="100"
								style="width:150px" placeholder="请输入订单号">&nbsp;&nbsp;&nbsp;&nbsp;
			第三方流水:  
			<input type="text" id="thirdRefundNo" name="thirdRefundNo" maxlength="100"
								style="width:150px" placeholder="请输入收款方手机">&nbsp;&nbsp;&nbsp;&nbsp;
			申请退款时间: 
			<input  type="text" id="startCreateTime" name="startCreateTime"  
					onFocus="WdatePicker({onpicked:function(){startCreateTime.focus();},maxDate:'#F{$dp.$D(\'endCreateTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startCreateTime.focus();},maxDate:'#F{$dp.$D(\'endCreateTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endCreateTime" name="endCreateTime"   
					onFocus="WdatePicker({onpicked:function(){endCreateTime.focus();},minDate:'#F{$dp.$D(\'startCreateTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endCreateTime.focus();},minDate:'#F{$dp.$D(\'startCreateTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
			代付时间: 
			<input  type="text" id="startPayTime" name="startPayTime"  
					onFocus="WdatePicker({onpicked:function(){startPayTime.focus();},maxDate:'#F{$dp.$D(\'endPayTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startPayTime.focus();},maxDate:'#F{$dp.$D(\'endPayTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endPayTime" name="endPayTime"   
					onFocus="WdatePicker({onpicked:function(){endPayTime.focus();},minDate:'#F{$dp.$D(\'startPayTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endPayTime.focus();},minDate:'#F{$dp.$D(\'startPayTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="margin-top: 5px;">
			<gd:btn btncode="BTNDFTKJL01"><a class="easyui-linkbutton" iconCls="icon-search" onclick="refundRecord.query()">查询</a></gd:btn>&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="refundRecord.cleardata()">重置</a>
			<gd:btn btncode="BTNDFTKJL02"><a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel" onclick="refundRecord.exportData()">导出EXCEL</a></gd:btn>
		</div>
		</form>
	</div>
	<div id="editDialog" class="easyui-dialog" style="width:1000px;height:500px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/refundRecord/refundRecordList.js"></script>
</html>