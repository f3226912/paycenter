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
		<title>银行卡管理</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<body>
	<table id="bankinfodg" title="">
	</table>
	<div id="bankinfotb" style="padding:5px;height:auto">
		<form id="bankinfoSearchForm" method="post">
			<table>
				<tr>
					<td>用户手机号：</td>
					<td>
						<input type="text" id="telephone" name="telephone" style="width:150px" maxlength="100"/>
					</td>
					<td>持卡人:</td>
					<td>
						<input type="text" id="realName" name="realName" style="width:150px" maxlength="100"/>
					</td>
					<td>验证状态:</td>
					<td>
						<select type="text" id="auditDoStatus" name="auditDoStatus">
							<option value="">全部</option>
							<option value="2">未验证</option>
							<option value="1">已验证</option>
						</select>
					</td>
					<td>
						<a class="easyui-linkbutton icon-search-btn" iconCls="icon-search" id="icon-search" OnClick ="return $('#bankinfoSearchForm').form('validate');">查询</a>
					</td>
					<td>
						<a class="easyui-linkbutton icon-reload-btn" iconCls="icon-reload" id="btn-reset">重置</a>
					</td>
				</tr>
			</table>
			<tr>
					<td>
						<a class="easyui-linkbutton" iconCls="icon-reload" id="icon-refresh">刷新</a>
					</td>
					<td>
					<gd:btn btncode='BANKEXPORTS01'>	
						<a class="easyui-linkbutton" iconCls="icon-account-excel" id="btn-import">导出EXCEL</a>
						</gd:btn>
					</td>
					<td>
					<gd:btn btncode='BANKPASSBTN02'>
						<a class="easyui-linkbutton" iconCls="icon-save" id="btn-pass">验证通过</a>
						</gd:btn>
					</td>
					<td>
						<gd:btn btncode='BANKUNPASSBTN03'>
							<a class="easyui-linkbutton" iconCls="icon-save" id="btn-unpass">验证不通过</a>
						</gd:btn>
					</td>
				</tr>
			<!-- 用户账号：<input type="text" id="account" name="account" style="width:150px" maxlength="100"/> -->
		</form>
	</div>
	<div id="bankinfoDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#bankinfoDetail">
		<div id="bankinfoDetail" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:pass()" id="pass">验证通过</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:unpass()"id="unpass">验证不通过</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#bankinfoDialog').dialog('close')">关闭</a>
		</div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/bank/main.js"></script>
</html>
