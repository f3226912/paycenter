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
		<title>清分权限管理</title>
	</head> 
<body>
	<table id="posdg" title="">
	</table>
	<div id="postb" style="padding:5px;height:auto">
		<form id="posSearchForm" method="post">
			所属市场：<select id="marketId" name="marketId">
						<option value="">全部</option>
					</select>
			终端号：<input type="text" id="machineNum" name="machineNum" style="width:150px" maxlength="100"/>
			商铺名称:<input type="text" id="shopsName" name="shopsName" style="width:150px" maxlength="100"/>
			<gd:btn btncode="QFQXGL001"><a class="easyui-linkbutton icon-search-btn" iconCls="icon-search" id="btn-search">查询</a></gd:btn>
			<a class="easyui-linkbutton icon-reload-btn" iconCls="icon-reload" id="btn-reset">重置</a>
		</form>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/posMachineConfig/main.js"></script>
</html>
