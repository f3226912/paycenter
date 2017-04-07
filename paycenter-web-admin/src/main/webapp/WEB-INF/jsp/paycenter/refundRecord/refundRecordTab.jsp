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
	<table id="refundRecordTabdg" title=""></table>
	<div id="refundRecordTabtb" style="padding:5px;height:auto">
	</div>
	<input type="hidden" value="${memberId}" id="memberId">
	<input type="hidden" value="${batNo}" id="batNo">
	<input type="hidden" value="${refundType}" id="refundType">
	<input type="hidden" value="${clearHasChange}" id="clearHasChange">
	<div id="editDialog" class="easyui-dialog" style="width:1000px;height:500px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/refundRecord/refundRecordTab.js"></script>
</html>