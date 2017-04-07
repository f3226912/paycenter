<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/constants.inc"%>
<%@ include file="../../pub/tags.inc"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../pub/head.inc"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
<title>系统角色管理</title>
<script type="text/javascript"
	src="${CONTEXT }js/datagrid-detailview.js"></script>
</head>
<script>
	$(document).ready(function() {
		//数据加载
		$('#dg').datagrid({
			url : CONTEXT + 'sysmgr/sysRole/query',
			width : 1000,
			height : 'auto',
			nowrap : true,
			toolbar : '#tb',
			pageSize : 20,
			rownumbers : true,
			pagination : true,
			fitColumns : true,
			fit : true,
			remoteSort : false
		//页面排序必须加
		});
		//分页加载
		$("#dg").datagrid("getPager").pagination({
			pageList : [ 10, 20, 50, 100 ]
		});
	});

	//单独查询
	function query2(value, name) {
		var queryParams = $('#dg').datagrid('options').queryParams;
		queryParams.roleName = value;
		$("#dg").datagrid('reload');
	}

	//查询
	function query(){
		queryParams = $('#dg').datagrid('options').queryParams;  
        queryParams.roleName = $('#roleName').searchbox('getValue');
		queryParams.attribute = $("#attribute").combobox("getValue");
        $("#dg").datagrid('reload'); 
	}
	
	
	//重置
	function cleardata() {
		$('#roleName').searchbox('setValue', "");
		$("#attribute").combobox("setValue","");
	}

	//删除
	function delObj() {
		//判断是否选中
		var row = $('#dg').datagrid("getSelections");
		if ($(row).length < 1) {
			slideMessage("请选择要操作的数据！");
			return;
		}
		jQuery.messager.confirm('提示', '您确定要删除所选数据吗?', function(r) {
			if (r) {
				var deleteStr = getSelections("roleID");
				jQuery.post(CONTEXT + "sysmgr/sysRole/delete", {
					"delID" : deleteStr
				}, function(data) {
					if (data == "success") {
						slideMessage("操作成功！");
						$("#dg").datagrid('reload');
						$('#dg').datagrid("uncheckAll");
					} else {
						warningMessage(data);
						return;
					}
				});
			} else {
				return;
			}
		});
	}

	//查看权限
	function showRight(id) {
		$('#assignRightDialog').dialog({
			'title' : '查看权限',
			'href' : CONTEXT+ 'sysmgr/sysRole/assignRightList?view=1&roleID='+ id
		}).dialog('open');
		$('#dg').datagrid("uncheckAll");
	}

	//分配权限
	function assignRight(id) {
		$('#assignRightDialog').dialog({
			'title' : '分配权限',
			'href' : CONTEXT + 'sysmgr/sysRole/assignMenuList?roleID=' + id
		}).dialog('open');
	}

	//分配角色
	function assignRole(id) {
		$('#assignRoleDialog').dialog({
			'title' : '分配角色',
			'href' : CONTEXT + 'sysmgr/sysUserRole/assignRoleList?roleID=' + id
		}).dialog('open');
		$('#dg').datagrid("uncheckAll");
	}

	//查看角色用户
	function showRole(id) {
		$('#assignRoleDialog').dialog({
			'title' : '查看用户',
			'href' : CONTEXT+ 'sysmgr/sysUserRole/assignRoleList?view=1&roleID='+ id
		}).dialog('open');
		$('#dg').datagrid("uncheckAll");
	}
	//新增
	function addRoleAction() {
		$('#editDialog').dialog({
			title : '新增',
			width : 500,
			height : 400,
			href : CONTEXT + 'sysmgr/sysRole/addInit'
		}).dialog('open');
	}
	//修改
	function updateAction(id) {
		$('#editDialog').dialog({
			'title' : '修改',
			width : 500,
			height : 400,
			'href' : CONTEXT + 'sysmgr/sysRole/updateInit?roleID=' + id
		}).dialog('open');
		$('#dg').datagrid("uncheckAll");
	}

	//操作
	function rowformater(value, row, index) {
		var html = "";
		html = html+ "<a class='operate' href='javascript:;' onclick=updateAction('"+ row.roleID+ "');>修改</a>"
				+ "<a class='operate' href='javascript:;' onclick=assignRight('"+ row.roleID+ "');>权限管理</a>"
				+ "<a class='operate' href='javascript:;' onclick=showRole('"+ row.roleID + "');>查看用户</a>";
		return html;
	}
	function attrformater(value,row,index){
		if(value==1){
			return "<span style='color:#cc0000;'>后台角色</span>";
		}else if(value==2){
			return "<span style='color:#ff00ff;'>前台角色</span>";
		}else if(value==3){
			return "<span style='color:#33ffff;'>前后台角色</span>";
		}else {
			return "<span style='color:;'>未分配</span>";
		}
	}
</script>
<body>
	<table id="dg" title="">
		<thead>
			<tr>
				<th data-options="field:'roleID',checkbox:true"></th>
				<th
					data-options="field:'roleName',width:250,align:'left',sortable:true">角色名称</th>
				<th
					data-options="field:'attribute',width:250,align:'left',sortable:true,formatter:attrformater">角色类型</th>
				<th
					data-options="field:'remark',width:250,align:'left',formatter:formaterLongText">备注</th>
				<th
					data-options="field:'123',width:250,align:'center',formatter: rowformater">操作</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding: 5px; height: auto">
		<form id="searchform" method="post">
			<div>
				角色名称: <input class="easyui-searchbox" type="text" id="roleName"
					name="roleName" style="width: 150px"
					data-options="prompt:'请输入角色名称',  
            searcher:function(value,name){query2(value,name)}">
				<!-- 角色类型: 
				<select id="attribute" name="attribute" class="easyui-combobox" editable="false" panelHeight="auto" style="width:100px">
					<option value="">全部</option>
					<option value="1">后台角色</option>
					<option value="2">前台角色</option>
					<option value="3">前后台角色</option>
				</select> -->
				<a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>
				<a class="easyui-linkbutton" iconCls="icon-reload"
					onclick="cleardata()">重置</a>
			</div>
		</form>
		<div style="margin-bottom: 5px">
			<xlc:btn btncode="XTJS001">
				<a class="easyui-linkbutton" iconCls="icon-add"
					onclick="addRoleAction()">新增</a>
			</xlc:btn>
			<xlc:btn btncode="XTJS003">
				<a class="easyui-linkbutton" iconCls="icon-remove"
					onclick="delObj()">删除</a>
			</xlc:btn>
			<a class="easyui-linkbutton" iconCls="icon-reload"
				onclick='location.reload(true)'">刷新</a>
		</div>
	</div>
	<!-- 修改 -->
	<div id="editDialog" class="easyui-dialog"
		style="width: 400px; height: 220px;" closed="true" modal="true"
		buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align: center">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'" onclick="save()">保存</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
		</div>
	</div>
	<!-- 分配权限 -->
	<div id="assignRightDialog" class="easyui-dialog"
		style="width: 800px; height: 540px;" closed="true" modal="true"
		buttons="#dlg-buttonsAssignRight">
		<div id="dlg-buttonsAssignRight" style="text-align: center">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#assignRightDialog').dialog('close')">关闭</a>
		</div>
	</div>
	<!-- 分配角色 -->
	<div id="assignRoleDialog" class="easyui-dialog"
		style="width: 800px; height: 450px;" closed="true" modal="true"
		buttons="#dlg-buttonsAssignRole">
		<div id="dlg-buttonsAssignRole" style="text-align: center">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#assignRoleDialog').dialog('close')">关闭</a>
		</div>
	</div>
</body>
</html>