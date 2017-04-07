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
		<title>代付款记录</title>
	</head>
<body>
	<table id="paiddg" title=""></table>
	<div id="paidtb" style="padding:0px;height:auto">
		<form id="searchform" method="post" style="padding:10px">
			用户账号:  
			<input class="" type="text" id="account" name="account" maxlength="100"
				style="width:150px" data-options="prompt:'请输入用户账号'">&nbsp;&nbsp;&nbsp;&nbsp;
			
			用户手机号：
			<input class="" type="text" id="mobile" name="mobile" maxlength="100"
				style="width:150px" data-options="prompt:'请输入用户手机号'">&nbsp;&nbsp;&nbsp;&nbsp;
			
			代付款金额：
			<input class="" type="text" id="startAmt" name="startAmt" style="width:100px" > - 
			<input class="" type="text" id="endAmt" name="endAmt" style="width:100px">&nbsp;&nbsp;&nbsp;&nbsp;
			
			<input type="checkBox" value="1" id="greaterZero" name="greaterZero">代付款金额>0&nbsp;&nbsp;&nbsp;&nbsp;
			<gd:btn btncode="BTNDFK01"><a class="easyui-linkbutton" iconCls="icon-search" onclick="paid.query()">查询</a>&nbsp;&nbsp;</gd:btn>
		</form>
		<div style="background:#efefef;padding:5px 0 5px 0;height:25px;line-height:25px;">
			<div style="float: left;font-size:14px;margin-left:10px;">代付款记录</div>
			<div style="float:right;margin-right:10px;">
				<gd:btn btncode="BTNDFK02"><a id="tranfer-btn" class="easyui-linkbutton" iconCls="icon-edit" onclick="paid.tranferSettlement()">转结算</a></gd:btn>
				<gd:btn btncode="BTNDFK03"><a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel" onclick="paid.exportData()">导出EXCEL</a></gd:btn>
			</div>
		</div>
	</div>
	
	<div id="transferDialog" class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttonsTransfer">
		<div id="dlg-buttonsTransfer" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" id="icon-save-btn" onclick="paid.saveTransferSettlement()">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#transferDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/paidRecord/paidMain.js"></script>
</html>