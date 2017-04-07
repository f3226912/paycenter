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
		<title>市场信息管理</title>
	</head> 
<body>
	<table id="marketdg" title="">
	</table>
	<div id="markettb" style="padding:5px;height:auto">
		<form id="marketSearchForm" method="post">
			所属市场：<select id="marketId" name="marketId" style="width:158px">
						<option value="">全部</option>
					</select>
			用户手机号：<input type="text" id="mobile" name="mobile" style="width:150px" maxlength="100"/>
			<gd:btn btncode="SCXXGL001"><a class="easyui-linkbutton icon-search-btn" iconCls="icon-search" id="btn-search">查询</a></gd:btn>
			<a class="easyui-linkbutton icon-reload-btn" iconCls="icon-reload" id="btn-reset">重置</a><br>
			<gd:btn btncode="SCXXGL002"><a class="easyui-linkbutton icon-reload-btn" iconCls="icon-add" id="btn-add">新增</a></gd:btn>&nbsp;&nbsp;&nbsp;&nbsp;
			<gd:btn btncode="SCXXGL003"><a class="easyui-linkbutton icon-reload-btn" iconCls="icon-remove" id="btn-delete">删除</a></gd:btn>
		</form>
	</div>
	<div id="addDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#dlg-buttonsAdd">
		<div id="dlg-buttonsAdd" style="text-align:center">
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" id="btn-add-save">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addDialog').dialog('close')">关闭</a>
        </div>
	</div>
	<div id="showDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#dlg-buttonsShow">
		<div id="dlg-buttonsShow" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showDialog').dialog('close')">关闭</a>
        </div>
	</div>
	<div id="editDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" id="btn-edit-save">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/marketBankAccInfo/main.js"></script>

<script type="text/javascript">
	function optFormat(value,row,index){
		var opt = "";
		opt += "<gd:btn btncode='SCXXGL004'><a class='operate' href='javascript:;' onclick=openShowDialog('"+row.id+"');>查看</a></gd:btn>";
		opt += "<gd:btn btncode='SCXXGL005'><a class='operate' href='javascript:;' onclick=openEditDialog('"+row.id+"');>修改</a></gd:btn>";
		return opt;
	}
</script>
</html>
