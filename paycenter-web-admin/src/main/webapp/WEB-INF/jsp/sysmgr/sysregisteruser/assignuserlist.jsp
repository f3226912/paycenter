<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	
	$(document).ready(function(){
		//数据加载
		$('#roledg').datagrid({
			url:CONTEXT+'sysmgr/sysUserRole/assignUserQuery?view=${view}&userID=${dto.userID}',
			width: 400,  
			height: 'auto', 
			nowrap:true,
			toolbar:'#roletb',
			pageSize:20,
			rownumbers:true,
			pagination:true,
			fitColumns:true,
			fit:true,
			onLoadSuccess:function(row){//当表格成功加载时执行               
                var rowData = row.rows;
                if("${dto.userID}"==""){
                	//新增用户分配
                	 var roleIDs=$("#roleIDs").val();
                	 var roleAry=roleIDs.split(",");
                	 $.each(rowData,function(idx,val){//遍历JSON
	                 	for(var i=0;i<roleAry.length;i++){
	                 		if(roleAry[i]==val.roleID){
	                 			$("#roledg").datagrid("selectRow", idx);//如果数据行为已选中则选中改行
	                 		}
	                 	}
	                 });
                }else{
                	//现有用户分配
                	$.each(rowData,function(idx,val){//遍历JSON
	                    if(val.isAuth>0&&"${view}"==""){
	                        $("#roledg").datagrid("selectRow", idx);//如果数据行为已选中则选中改行
	                    }
	                });
                }
            },
            onUncheck:function(rowIndex,rowData){
            	//当前用户本身拥有的角色，但是登录用户没有此角色则不可以进行分配
//             	if(rowData.canAssign<=0&&rowData.isAuth>0){
//             		$("#roledg").datagrid("selectRow", rowIndex);
//             	}
            },
            onUncheckAll:function(rows){
            	//当前用户本身拥有的角色，但是登录用户没有此角色则不可以进行分配
//                 $.each(rows,function(idx,val){
//                 	if(val.canAssign<=0&&val.isAuth>0){
//                 		$("#roledg").datagrid("selectRow", idx);
//                 	}
//                 }); 
            },
            rowStyler:function(index,row){ 
            	//当不可分配则背景色置为灰色
//                 if (row.canAssign<=0&&"${view}"==""){   
//                     return 'background-color:#ddd;color:#575765;';   
//                 }   
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
			msg='您确定要取消所有分配数据吗?';
        }
		jQuery.messager.confirm('提示', msg, function(r){
			if (r){
        		var roleIDs = getSelections("roleID");
        		if("${dto.userID}"==""){
        			//给新增用户分配
        			$("#roleIDs").val(roleIDs);
        			$('#assignRoleDialog').dialog('close');
        		}else{
        			//直接给已有的用户分配
        			jQuery.post(CONTEXT+"sysmgr/sysUserRole/assignUser",{"roleIDs":roleIDs,"userID":"${dto.userID}"},function(data){
        				if (data == "success"){
    						slideMessage("操作成功！");
        					$("#roledg").datagrid('reload');
        					$("#userSelect").datagrid('reload');
        					$("#dg").datagrid('reload');
        				}else{
    						warningMessage(data);
        					return;
        				}
            		}); 
        		}
			}else{
				return;
			}
		});
	}
	
	//单独查询
	function queryRole(value,name){
		 var queryParams = $('#roledg').datagrid('options').queryParams;  
		 if(name=='roleName'){
		 	queryParams.roleName = value;
		 }else{
			queryParams.roleName = $('#roleName').searchbox('getValue');
			queryParams.attribute = $('#attribute').combobox('getValue');
		 }
         $("#roledg").datagrid('reload'); 
	}
	
	//重置
	function clearRoledata(){
		$('#roleName').searchbox('setValue',"");
		$('#attribute').combobox('setValue',"");
	}
	
	//刷新
	function reloadCurPage(){
		clearRoledata();
		queryRole();
	}
	function formaterAttribute(value,row,index){
		if(value==1){
			return "后台角色";
		}else if(value==2){
			return "前台角色";
		}else if(value==3){
			return "前后台角色";
		}else{
			return "未分配";
		}
	}
</script>
<table id="roledg" title="" >
	<thead>
		<tr>
			<c:if test="${empty view }">
				<th data-options="field:'roleID',checkbox:true"></th>
			</c:if>
			<th data-options="field:'roleName',width:200,align:'left'">角色名称</th>
			<th data-options="field:'attribute',width:100,align:'left',formatter:formaterAttribute">角色类型</th>
			<th data-options="field:'remark',width:300,align:'left',formatter:formaterLongText">备注</th>
		</tr>
	</thead>
</table>
<div id="roletb" style="padding:5px;height:auto">
	<form id="searchRoleform" method="post">
		<div>
			角色名称: <input class="easyui-searchbox" type="text" id="roleName" name="roleName" style="width:150px" data-options="prompt:'请输入角色名称',  
            searcher:function(value,name){queryRole(value,name)}">
			<!-- 角色类型: 
        	<select class="easyui-combobox" id="attribute" name="attribute" editable="false" panelHeight="auto" style="width:100px">
				<option value="">全部</option>
				<option value="1">后台角色</option>
				<option value="2">前台角色</option>
				<option value="3">前后台角色</option>
			</select> -->
			<a class="easyui-linkbutton" iconCls="icon-search" onclick='queryRole()'>查询</a>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="clearRoledata()">重置</a>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='reloadCurPage()'>刷新</a>
		</div>
	</form>
	<div style="margin-bottom:5px">
		<c:if test="${not empty dto.userID }">当前用户：${dto.userCode }&nbsp;&nbsp;姓名：${dto.userName }&nbsp;&nbsp;职位：${dto.orgUnitId }</c:if>
		<c:if test="${empty view }">
			<a style="margin-left:15px" class="easyui-linkbutton" iconCls="icon-group-link" onclick="saveAssignRole()">确定分配</a>
			<font color="red">注意：请操作者谨慎操作</font>
		</c:if>
	</div>
</div>
