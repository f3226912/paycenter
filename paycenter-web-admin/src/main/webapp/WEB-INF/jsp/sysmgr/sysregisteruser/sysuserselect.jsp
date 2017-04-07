<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script>
	
$(document).ready(function(){
	//数据加载
	$('#userSelect').datagrid({
   		url:CONTEXT+'sysmgr/sysRegisterUser/chose?orgUnitId=${orgUnitId}',
		width: 930,  
   		height: 490, 
		nowrap:true,
		toolbar:'#selectUserTb',
		pageSize:10,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		remoteSort:false//页面排序必须加
	}); 
	//分页加载
	$("#userSelect").datagrid("getPager").pagination({   
		pageList: [10,20,50,100]
       }); 
});

//新增
function addSelectUserAction(){
	$('#editUserDialog').dialog({'title':'新增','href':CONTEXT+'sysmgr/sysRegisterUser/addSelectInit?orgUnitId=${orgUnitId}&type=${type}'}).dialog('open');
}

//修改状态[启用/禁用]
function updateSelectUserStatus(status){
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
      					$("#userSelect").datagrid('reload');
      					$('#userSelect').datagrid("uncheckAll");
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
function selectUserQuery(){
  	queryParams = $('#userSelect').datagrid('options').queryParams;  
    queryParams.userCode = $('#userCode').searchbox('getValue');
	queryParams.userName = $("#userName").searchbox("getValue");
	queryParams.mobile = $("#mobile").searchbox("getValue");
	queryParams.locked = $("#locked").combobox("getValue");
	queryParams.roleTotal = $("#roleTotal").combobox("getValue");
    $("#userSelect").datagrid('reload'); 
}

//单独查询
function querySelectUser2(value,name){
	 var queryParams = $('#userSelect').datagrid('options').queryParams;  
	 if(name=='userCode'){
	 	queryParams.userCode = value;
	 }
	 if(name=='userName'){
	 	queryParams.userName = value;
	 }
	 if(name=='mobile'){
	 	queryParams.mobile = value;
	 }
           $("#userSelect").datagrid('reload'); 
}

//重置
function clearSelectUserdata(){
	$('#userCode').searchbox('setValue',"");
	$("#userName").searchbox("setValue","");
	$("#mobile").searchbox("setValue","");
	$("#mobile").searchbox("setValue","");
	$("#locked").combobox("setValue","");
	$("#roleTotal").combobox("setValue","");
}

//重置密码
function resetSelectPwd(){
	//判断是否选中
	var row = $('#userSelect').datagrid("getSelections");
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
      					$("#userSelect").datagrid('reload');
      					$('#userSelect').datagrid("uncheckAll");
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
function delSelectUserObj(){
	//判断是否选中
	var row = $('#userSelect').datagrid("getSelections");
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
      					$("#userSelect").datagrid('reload');
      					$('#userSelect').datagrid("uncheckAll");
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
function updateSelectUserAction(id){
	$('#editUserDialog').dialog({'title':'修改','href':CONTEXT+'sysmgr/sysRegisterUser/updateSelectInit?userID='+id}).dialog('open');
	$('#userSelect').datagrid("uncheckAll");
}

//分配角色
function assignSelectUserRole(id){
	$('#assignRoleDialog').dialog({'title':'分配角色','href':CONTEXT+'sysmgr/sysUserRole/assignUserList?userID='+id}).dialog('open');
	$('#userSelect').datagrid("uncheckAll");
}

//查看角色
function showSelectUserRole(id){
	$('#assignRoleDialog').dialog({'title':'查看角色','href':CONTEXT+'sysmgr/sysUserRole/assignUserList?view=1&userID='+id}).dialog('open');
	$('#userSelect').datagrid("uncheckAll");
}

//操作修改
function updateOperate(value,row,index){
	
	var html="<table class='operateTable'><tr>";
	html=html+"<td><a class='operate' href='javascript:;' onclick=updateSelectUserAction('"+row.userID+"');>修改</a></td>"
	         +"<td><a class='operate' href='javascript:;' onclick=assignSelectUserRole('"+row.userID+"');>分配角色</a></td>"
	         +"<td><a class='operate' href='javascript:;' onclick=showSelectUserRole('"+row.userID+"');>查看角色</a></td>";
	return html+"</tr></table>";
}

//用户是否已分配角色
function hasRoleFormatter(value,row,index){
	
	if(value>0){
		return "<span>是</span>";
	}else{
		return "";
	}
}

//刷新
function reloadCurPage(){
	$("#userSelect").datagrid('reload'); 
}
</script>
<table id="userSelect" title="">
	<thead>
		<tr>
			<th data-options="field:'userID',checkbox:true"></th>
			<th data-options="field:'userCode',width:50,align:'left',sortable:true">用户帐号</th>
			<th data-options="field:'userName',width:50,align:'left',sortable:true">用户名称</th>
			<th data-options="field:'mobile',width:40,align:'left',sortable:true">联系电话</th>
			<th data-options="field:'locked',width:30,align:'center',formatter: function(value){if(value=='0')return '<font color=green>启用</font>';if(value=='1')return '<font color=red>禁 用</font>';},sortable:true">用户状态</th>
			<th data-options="field:'total',width:30,align:'center',sortable:true,formatter:hasRoleFormatter">是否已分配角色</th>
			<th data-options="field:'createTime',width:80,align:'center',sortable:true">创建时间</th>
			<th data-options="field:'action',width:100,align:'center',formatter:updateOperate">操作</th>
		</tr>
	</thead>
</table>
<div id="selectUserTb" style="padding:5px;height:auto">
	<form id="searchform" method="post">
	<div>
		用户帐号: <input class="easyui-searchbox" type="text" id="userCode" name="userCode" style="width:150px" data-options="prompt:'请输入用户帐号',  
           searcher:function(value,name){querySelectUser2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
		用户名称: <input class="easyui-searchbox" type="text" id="userName" name="userName" style="width:150px" data-options="prompt:'请输入用户名称',  
           searcher:function(value,name){querySelectUser2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
		联系电话: <input class="easyui-searchbox" type="text" id="mobile" name="mobile" style="width:150px" data-options="prompt:'请输入联系电话',  
           searcher:function(value,name){querySelectUser2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
       </div>
       <div style="margin-top:5px;">
       	是否已分配角色: 
       	<select class="easyui-combobox" id="roleTotal" name="roleTotal" editable="false" panelHeight="auto" style="width:100px">
			<option value="">请选择</option>
			<option value="0">否</option>
			<option value="1">是</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		状态: 
		<select class="easyui-combobox" id="locked" name="locked" editable="false" panelHeight="auto" style="width:100px">
			<option value="">请选择</option>
			<option value="0">未锁定</option>
			<option value="1">已锁定</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-search" onclick="selectUserQuery()">查询</a>&nbsp;&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-reload" onclick="clearSelectUserdata()">重置</a>
	</div>
	</form>
	<div style="margin-bottom:5px">
		<a class="easyui-linkbutton" iconCls="icon-add" onclick="addSelectUserAction()">新增</a>
		<a class="easyui-linkbutton" iconCls="icon-remove" onclick="delSelectUserObj()">删除</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="updateSelectUserStatus('0')">启用</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-no'" onclick="updateSelectUserStatus('1')">禁用</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-redo'" onclick="resetSelectPwd()">重置密码</a>
		<a class="easyui-linkbutton" iconCls="icon-reload" onclick="reloadCurPage()">刷新</a>
	</div>
</div>

<!-- 用户编缉 -->
<div id="editUserDialog" class="easyui-dialog" style="width:400px;height:230px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
	<div id="dlg-buttonsEdit" style="text-align:center">
      	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveUser()">保存</a>
       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editUserDialog').dialog('close')">关闭</a>
    </div>
</div>
<!-- 用户所属组织 -->
<div id="choseOrgDialog" class="easyui-dialog" style="width:700px;height:600px;" closed="true" modal="true" buttons="#dlg-buttonsOrg">
	<div id="dlg-buttonsOrg" style="text-align:center">
         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#choseOrgDialog').dialog('close')">关闭</a>
    </div>
</div>
<!-- 分配角色 -->
<div id="assignRoleDialog" class="easyui-dialog" style="width:800px;height:600px;" closed="true" modal="true" buttons="#dlg-buttonsRole">
	<div id="dlg-buttonsRole" style="text-align:center">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#assignRoleDialog').dialog('close')">关闭</a>
    </div>
</div>
