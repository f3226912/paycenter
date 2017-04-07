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
		<title>系统用户管理</title>
	</head>
	<script>
	
		$(document).ready(function(){
			loadData(null);
		});
		
		function loadData(dataParams){
			//数据加载
			$('#dg').datagrid({
	    		url:CONTEXT+'sysmgr/sysRegisterUser/query',
	    		queryParams : dataParams,
				width: 1000,  
	    		height: 'auto', 
				nowrap:true,
				toolbar:'#tb',
				pageSize:20,
				rownumbers:true,
				pagination:true,
				fitColumns:true,
				fit:true,
				remoteSort:false//页面排序必须加
			}); 
			//分页加载
			$("#dg").datagrid("getPager").pagination({
				pageList: [20,50,100,200]
			}); 
		}
	
		//新增
		function addAction(){
			$('#editDialog').dialog({'title':'新增','href':CONTEXT+'sysmgr/sysRegisterUser/addInit'}).dialog('open');
		}
		
   		//修改状态[启用/禁用]
		function updateStatus(status){
			//判断是否选中
			var updateStr = getSelections("userID");
            if(updateStr=="") {
				slideMessage("请选择要操作的数据！");
                return ;
            }
			var msg = status=="0"?"启用":"禁用";
			var url=status=="0"?CONTEXT+"sysmgr/sysRegisterUser/unlock":CONTEXT+"sysmgr/sysRegisterUser/lock";
    		jQuery.messager.confirm('提示', '您确定要'+msg+'所选数据吗?', function(r){
    			if (r){
            	    //执行动作
        			jQuery.post(url,{"ids":updateStr,"status":status},function(data){
        				if (data == "success"){
							slideMessage("操作成功！");
        					$("#dg").datagrid('reload');
        					$('#dg').datagrid("uncheckAll");
        				}else{
        					warningMessage(data);
        				}
            		});
    			}else{
					return;
				}
    		});
		}
		
		//查询
		function query(){
			var dataParams={
				userCode : $('#userCode').searchbox('getValue'),
				userName : $("#userName").searchbox("getValue"),
				mobile : $("#mobile").searchbox("getValue"),
				locked : $("#locked").combobox("getValue"),
				roleTotal : $("#roleTotal").combobox("getValue")
			};
			loadData(dataParams);
		}
		
		//单独查询
		function query2(value,name){
			var dataParams;
			if(name=='userCode'){
				dataParams={
					userCode : value
				};
			 }
			 if(name=='userName'){
				dataParams={
					userName : value
				};
			 }
			 if(name=='mobile'){
				dataParams={
					mobile : value
				};
			 }
			loadData(dataParams);
		}
		
		//重置
		function cleardata(){
			$('#userCode').searchbox('setValue',"");
			$("#userName").searchbox("setValue","");
			$("#mobile").searchbox("setValue","");
			$("#").combobox("setValue","");
			$("#roleTotal").combobox("setValue","");
		}
		
		//重置密码
		function resetPwd(){
			//判断是否选中
			var row = $('#dg').datagrid("getSelections");
            if($(row).length < 1 ) {
				slideMessage("请选择要操作的数据！");
                return ;
            }
    		jQuery.messager.confirm('提示', '您确定要重置所选数据吗?', function(r){
    			if (r){
            		var ids = getSelections("userID");
        			jQuery.post(CONTEXT+"sysmgr/sysRegisterUser/resetPassword",{"ids":ids},function(data){
        				if (data == "success"){
							slideMessage("密码重置成功！密码：888888");
        					$("#dg").datagrid('reload');
        					$('#dg').datagrid("uncheckAll");
        				}else{
							warningMessage(data);
        					return;
        				}
            		});
    			}else{
					return;
				}
    		});
		}
		
		
		//删除
		function delObj(){
			//判断是否选中
			var row = $('#dg').datagrid("getSelections");
            if($(row).length < 1 ) {
				slideMessage("请选择要操作的数据！");
                return ;
            }
    		jQuery.messager.confirm('提示', '您确定要删除所选数据吗?', function(r){
    			if (r){
            		var deleteStr = getSelections("userID");
            		jQuery.post(CONTEXT+"sysmgr/sysRegisterUser/delete",{"deleteStr":deleteStr},function(data){
        				if (data == "success"){
							slideMessage("操作成功！");
        					$("#dg").datagrid('reload');
        					$('#dg').datagrid("uncheckAll");
        				}else{
							warningMessage(data);
        					return;
        				}
            		});
    			}else{
					return;
				}
    		});
		}
		
		//修改
		function updateAction(id){
			$('#editDialog').dialog({'title':'修改','href':CONTEXT+'sysmgr/sysRegisterUser/updateInit?userID='+id}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		//分配角色
		function assignRole(id){
			$('#assignRoleDialog').dialog({
				'title':'分配角色',
				'width':700,
				'height':400,
				'href':CONTEXT+'sysmgr/sysUserRole/assignUserList?userID='+id
			}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		//查看角色
		function showRole(id){
			$('#assignRoleDialog').dialog({
				'title':'查看角色',
				'width':600,
				'height':300,
				'href':CONTEXT+'sysmgr/sysUserRole/assignUserList?view=1&userID='+id
			}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		//操作修改
		function updateOperate(value,row,index){
			
			var html="";
			html=html+"<a class='operate' href='javascript:;' onclick=updateAction('"+row.userID+"');>修改</a>"
			         +"<a class='operate' href='javascript:;' onclick=assignRole('"+row.userID+"');>分配角色</a>"
// 			         +"<xlc:btn btncode='XTYH008||CGYSZ008'><td><a class='operate' href='javascript:;' onclick=showRole('"+row.userID+"');>查看角色</a></td></xlc:btn>";
			return html;
		}
		
		//用户是否已分配角色
		function hasRoleFormatter(value,row,index){
			
			if(value>0){
				return "<span style='color:green;'>是</span>";
			}else{
				return "<span style='color:red;'>否</span>";
			}
		}
    </script>
<body>
	<table id="dg" title="">
		<thead>
			<tr>
				<th data-options="field:'userID',checkbox:true"></th>
				<th data-options="field:'userCode',width:150,align:'left',sortable:true">用户帐号</th>
				<th data-options="field:'userName',width:150,align:'left',sortable:true">用户名称</th>
				<th data-options="field:'orgUnitId',width:150,align:'left',sortable:true">职位</th>
				<th data-options="field:'mobile',width:150,align:'left',sortable:true">联系电话</th>
				<th data-options="field:'locked',width:100,align:'center',formatter: function(value){if(value=='0')return '<font color=green>启用</font>';if(value=='1')return '<font color=red>禁 用</font>';},sortable:true">用户状态</th>
				<th data-options="field:'total',width:100,align:'center',sortable:true,formatter:hasRoleFormatter">是否已分配角色</th>
				<th data-options="field:'action',width:150,align:'center',formatter:updateOperate">操作</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:5px;height:auto">
		<form id="searchform" method="post">
		<div>
			用户帐号: <input class="easyui-searchbox" type="text" id="userCode" name="userCode" style="width:150px" data-options="prompt:'请输入用户帐号',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
			用户名称: <input class="easyui-searchbox" type="text" id="userName" name="userName" style="width:150px" data-options="prompt:'请输入用户名称',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
			联系电话: <input class="easyui-searchbox" type="text" id="mobile" name="mobile" style="width:150px" data-options="prompt:'请输入联系电话',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
<!-- 			所属组织: <input class="easyui-searchbox" type="text" id="orgUnitName" name="orgUnitName" style="width:150px" data-options="prompt:'请输入组织名称',   -->
<!--             searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp; -->
        </div>
        <div style="margin-top:10px;">
        	是否已分配角色: 
        	<select class="easyui-combobox" id="roleTotal" name="roleTotal" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="1">是</option>
				<option value="0">否</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			用户状态: 
			<select class="easyui-combobox" id="locked" name="locked" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="0">启用</option>
				<option value="1">禁用</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="cleardata()">重置</a>
			
		</div>
		</form>
		<div style="margin-bottom:5px">
			<xlc:btn btncode="XTYH001||CGYSZ001"><a class="easyui-linkbutton" iconCls="icon-add" onclick="addAction()">新增</a></xlc:btn>
			<xlc:btn btncode="XTYH003||CGYSZ003"><a class="easyui-linkbutton" iconCls="icon-remove" onclick="delObj()">删除</a></xlc:btn>
			<xlc:btn btncode="XTYH004||CGYSZ004"><a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="updateStatus('0')">启用</a></xlc:btn>
			<xlc:btn btncode="XTYH005||CGYSZ005"><a class="easyui-linkbutton" data-options="iconCls:'icon-no'" onclick="updateStatus('1')">禁用</a></xlc:btn>
			<xlc:btn btncode="XTYH006||CGYSZ006"><a class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="resetPwd()">重置密码</a></xlc:btn>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)'">刷新</a>
		</div>
	</div>
	<div id="editDialog" class="easyui-dialog" style="width:400px;height:300px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
	<!-- 用户所属组织 -->
	<div id="choseOrgDialog" class="easyui-dialog" style="width:700px;height:600px;" closed="true" modal="true" buttons="#dlg-buttonsOrg">
		<div id="dlg-buttonsOrg" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#choseOrgDialog').dialog('close')">关闭</a>
        </div>
	</div>
	<!-- 分配角色 -->
	<div id="assignRoleDialog" class="easyui-dialog" style="width:800px;height:540px;" closed="true" modal="true" buttons="#dlg-buttonsRole">
		<div id="dlg-buttonsRole" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#assignRoleDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
</html>