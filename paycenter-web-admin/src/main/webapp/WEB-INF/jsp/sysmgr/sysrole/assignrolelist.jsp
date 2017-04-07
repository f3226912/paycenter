<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	
	$(document).ready(function(){
		//数据加载
		$('#roledg').datagrid({
			url:CONTEXT+'sysmgr/sysUserRole/assignRoleQuery?view=${view}&roleID=${dto.roleID}',
			width: 400,  
			height: 'auto', 
			nowrap:true,
			toolbar:'#roletb',
			pageSize:10,
			rownumbers:true,
			pagination:true,
			fitColumns:true,
			fit:true,
			onLoadSuccess:function(row){//当表格成功加载时执行               
                var rowData = row.rows;
                $.each(rowData,function(idx,val){//遍历JSON
                      if(val.isAuth>0&&"${view}"==""){
                        $("#roledg").datagrid("selectRow", idx);//如果数据行为已选中则选中改行
                      }
                });              
            }
		}); 
		
		//分页加载
		$("#roledg").datagrid("getPager").pagination({   
			pageList: [10,20,50,100]
        }); 
	});

	//确定分配
	function saveAssignRole(){
		//判断是否选中
		var row = $('#roledg').datagrid("getSelections");
		var msg='您确定要分配所选数据吗?';
        if($(row).length < 1 ) {
			msg='您确定要取消分配数据吗?';
        }
		jQuery.messager.confirm('提示', msg, function(r){
			if (r){
        		var userIDs = getSelections("userID");
        		jQuery.post(CONTEXT+"sysmgr/sysUserRole/assignRole",{"userIDs":userIDs,"roleID":"${dto.roleID}"},function(data){
    				if (data == "success"){
						slideMessage("操作成功！");
    					$("#roledg").datagrid('reload');
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
	
	//查询
	function queryUser(){
		queryParams = $('#roledg').datagrid('options').queryParams;  
        queryParams.userCode = $('#userCode').searchbox('getValue');
		queryParams.userName = $("#userName").searchbox("getValue");
// 		queryParams.orgUnitName = $("#orgUnitName").searchbox("getValue");
		queryParams.locked = $("#locked").combobox("getValue");
        $("#roledg").datagrid('reload'); 
	}
	
	//单独查询
	function queryUser2(value,name){
		 var queryParams = $('#roledg').datagrid('options').queryParams;  
		 if(name=='userCode'){
		 	queryParams.userCode = value;
		 	queryParams.userName = null;
		 }
		 if(name=='userName'){
		 	queryParams.userName = value;
		 	queryParams.userCode = null;
		 }
		 if(name=='orgUnitName'){
		 	queryParams.orgUnitName = value;
		 }
         $("#roledg").datagrid('reload'); 
	}
	
	//重置
	function clearUserdata(){
		$('#userCode').searchbox('setValue',"");
		$("#userName").searchbox("setValue","");
// 		$("#orgUnitName").searchbox("setValue","");
		$("#locked").combobox("setValue","");
	}
	
	//刷新
	function reloadCurPage(){
		clearUserdata();
		queryUser();
	}
</script>
<table id="roledg" title="" >
	<thead>
		<tr>
			<c:if test="${empty view }">
				<th data-options="field:'userID',checkbox:true"></th>
			</c:if>
			<th data-options="field:'userCode',width:80,align:'left'">用户编号</th>
			<th data-options="field:'userName',width:80,align:'left'">用户名称</th>
			<th data-options="field:'locked',width:80,align:'center',formatter: function(value){if(value=='0')return '<font color=green>启用</font>';if(value=='1')return '<font color=red>禁用</font>';}">用户状态</th>
<!-- 			<th data-options="field:'orgUnitName',width:100,align:'left'">所属组织</th> -->
		</tr>
	</thead>
</table>
<div id="roletb" style="padding:5px;height:auto">
	<form id="searchUserForm" method="post">
		<div>
			用户帐号: <input class="easyui-searchbox" type="text" id="userCode" name="userCode" style="width:150px" data-options="prompt:'请输入用户帐号',  
            searcher:function(value,name){queryUser2(value,name)}">
			用户名称: <input class="easyui-searchbox" type="text" id="userName" name="userName" style="width:150px" data-options="prompt:'请输入用户名称',  
            searcher:function(value,name){queryUser2(value,name)}">
<!-- 			所属组织: <input class="easyui-searchbox" type="text" id="orgUnitName" name="orgUnitName" style="width:150px" data-options="prompt:'请输入所属组织',   -->
<!--             searcher:function(value,name){queryUser2(value,name)}"> -->
        </div>
		<div style="margin-top:5px">
			状态: 
			<select class="easyui-combobox" id="locked" name="locked" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="0">启用</option>
				<option value="1">禁用</option>
			</select>
			<a class="easyui-linkbutton" iconCls="icon-search" onclick="queryUser()">查询</a>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="clearUserdata()">重置</a>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='reloadCurPage()'">刷新</a>
		</div>
	</form>
	<div style="margin-bottom:5px">
		当前角色是：${dto.roleName }
		<c:if test="${empty view }">
			<a style="margin-left:15px" class="easyui-linkbutton" iconCls="icon-group-link" onclick="saveAssignRole()">确定分配</a>
		</c:if>
	</div>
</div>
