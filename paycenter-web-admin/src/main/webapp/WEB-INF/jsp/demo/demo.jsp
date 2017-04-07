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
		<title>demo</title>
	</head>
<body>
	<table id="demodg" title="">
	</table>
	<div id="demotb" style="padding:5px;height:auto">
		<form id="demoSearchForm" method="post">
		<div>
			名称: <input  type="text" id="name" name="name" style="width:150px" data-options="prompt:'请输入名称">&nbsp;&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton icon-search-btn" iconCls="icon-search">查询</a>&nbsp;&nbsp;
			<a class="easyui-linkbutton icon-reload-btn" iconCls="icon-reload">重置</a>
		</div>
		</form>
		<div style="margin-bottom:5px">
			<xlc:btn btncode='XZ001'><a class="easyui-linkbutton icon-add-btn" iconCls="icon-add">新增</a></xlc:btn>
			<xlc:btn btncode='XZ003'><a class="easyui-linkbutton icon-remove-btn" iconCls="icon-remove">删除</a></xlc:btn>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)'">刷新</a>
		</div>
		
		<div id="addDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#dlg-buttonsAdd">
		<div id="dlg-buttonsAdd" style="text-align:center">
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" id="icon-save-btn" onclick="save()">上传</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addDialog').dialog('close')">关闭</a>
        </div>
	</div>
		
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/demo/demo.js"></script>
</html>

