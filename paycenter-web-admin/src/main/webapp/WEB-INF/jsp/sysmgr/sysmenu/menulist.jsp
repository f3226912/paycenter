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
		<title>系统菜单管理</title>
	</head>
	<script>
	
		$(document).ready(function(){
			//数据加载
			$('#dg').datagrid({
	    		url:CONTEXT+'sysmgr/sysMenu/query',
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
				pageList: [10,20,50,100]
	        }); 
		});
	
		//新增
		function addAction(){
			$('#editDialog').dialog({'title':'新增','href':CONTEXT+'sysmgr/sysMenu/addInit'}).dialog('open');
		}
		
		//查询
		function query(){
    		queryParams = $('#dg').datagrid('options').queryParams;  
            queryParams.menuName = $('#menuName').searchbox('getValue');
			queryParams.menuCode = $("#menuCode").searchbox("getValue");
			queryParams.parentMenuName = $("#parentMenuName").searchbox("getValue");
			queryParams.level = $("#level").combobox("getValue");
			//queryParams.attribute = $("#attribute").combobox("getValue");
			console.log(queryParams); 
            $("#dg").datagrid('reload'); 
		}
		
		//单独查询
		function query2(value,name){
			 var queryParams = $('#dg').datagrid('options').queryParams;  
			 if(name=='menuName'){
			 	queryParams.menuName = value;
			 	queryParams.menuCode=null;
			 	queryParams.parentMenuName=null;
			 }
			 if(name=='menuCode'){
			 	queryParams.menuCode = value;
			 	queryParams.menuName = null;
			 	queryParams.parentMenuName = null;
			 }
			 if(name=='parentMenuName'){
			 	queryParams.parentMenuName = value;
			 	queryParams.menuName = null;
			 	queryParams.menuCode = null;
			 }
             $("#dg").datagrid('reload'); 
		}
		
		//重置
		function cleardata(){
			$('#menuName').searchbox('setValue',"");
			$("#menuCode").searchbox("setValue","");
			$("#parentMenuName").searchbox("setValue","");
			$("#level").combobox("setValue","");
			$("#attribute").combobox("setValue","");
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
            		var deleteStr = getSelections("menuID");
            		jQuery.post(CONTEXT+"sysmgr/sysMenu/delete",{"menuID":deleteStr},function(data){
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
   
		//分配按钮
		function assignButton(id){
			$('#assignButDialog').dialog({'title':'分配按钮','href':CONTEXT+'sysmgr/sysMenu/buttionList?menuID='+id}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		//查看按钮
		function showButton(id){
			$('#assignButDialog').dialog({'title':'查看按钮','href':CONTEXT+'sysmgr/sysMenu/buttionList?view=1&menuID='+id}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		//修改
		function updateAction(id){
			$('#editDialog').dialog({'title':'修改','href':CONTEXT+'sysmgr/sysMenu/updateInit?menuID='+id}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		function rowformater(value,row,index){
			var html="";
			html=html+"<a class='operate' href='javascript:;' onclick=updateAction('"+row.menuID+"');>修改</a>";
			if(row.menuModuleID!=null&&row.menuModuleID!=""){
			}else{
				html=html+"";
			}
			return html;
		}
		function attrformater(value,row,index){
			if(value==1){
				return "<span style='color:#cc0000;'>后台菜单</span>";
			}else if(value==2){
				return "<span style='color:#ff00ff;'>前台菜单</span>";
			}else if(value==3){
				return "<span style='color:#33ffff;'>数据分类</span>";
			}else {
				return "<span style='color:;'>未分配</span>";
			}
		}
		function levelformater(value,row,index){
			if(value==1){
				return "<span style='color:#FF1A00;'>一级</span>";
			}
			if(value==2){
				return "<span style='color:#FFA700;'>二级</span>";
			}
			if(value==3){
				return "<span style='color:#FF00DE;'>三级</span>";
			}
		}
		
    </script>
<body >
<!-- 	<div data-options="region:'north'" style="height:100px"> -->
		<div id="tb" style="padding:5px;height:auto">
			<form id="searchform" method="post">
			<div>
				菜单编号: <input class="easyui-searchbox" type="text" id="menuCode" name="menuCode" style="width:150px" data-options="prompt:'请输入菜单编号',  
	            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
				菜单名称: <input class="easyui-searchbox" type="text" id="menuName" name="menuName" style="width:150px" data-options="prompt:'请输入菜单名称',  
	            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
	 			上级菜单名称: <input class="easyui-searchbox" type="text" id="parentMenuName" name="parentMenuName" style="width:150px" data-options="prompt:'请输入上级菜单名称',  
	            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;<br>
	 			<!-- 菜单类型: 
	 			<select class="easyui-combobox" id="attribute" name="attribute" editable="false" panelHeight="auto" style="width:100px">
					<option value="">请选择</option>
	 				<option value="1">后台菜单</option>
	 				<option value="2">前台菜单</option>
	 				<option value="3">数据类型</option>
				</select> -->
	 			菜单级别:
	 			<select class="easyui-combobox" id="level" name="level" editable="false" panelHeight="auto" style="width:100px">
	 				<option value="">请选择</option>
	 				<option value="1">一级</option>
	 				<option value="2">二级</option>
	 				<option value="3">三级</option>
	 			</select>
				<a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>&nbsp;&nbsp;
				<a class="easyui-linkbutton" iconCls="icon-reload" onclick="cleardata()">重置</a>
			</div>
			</form>
			<div style="margin-bottom:5px">
				<xlc:btn btncode="XTCD001"><a class="easyui-linkbutton" iconCls="icon-add" onclick="addAction()">新增</a></xlc:btn>
				<xlc:btn btncode="XTCD003"><a class="easyui-linkbutton" iconCls="icon-remove" onclick="delObj()">删除</a></xlc:btn>
				<a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)'">刷新</a>
			</div>
		</div>
	
        <div class="easyui-layout" data-options="fit:true">   
            <div data-options="region:'center'">
            <table id="dg" title="">
				<thead>
					<tr>
						<th data-options="field:'menuID',checkbox:true"></th>
						<th data-options="field:'menuCode',width:150,align:'left',sortable:true">菜单编号</th>
						<th data-options="field:'menuName',width:180,align:'left',sortable:true">菜单名称</th>
						<th data-options="field:'attribute',width:100,align:'left',sortable:true,formatter: attrformater">菜单类别</th>
						<th data-options="field:'level',width:100,align:'center',formatter: levelformater">菜单级别</th>
						<th data-options="field:'parentMenuName',width:180,align:'left',sortable:true">上级菜单</th>
						<th data-options="field:'actionUrl',width:180,align:'left'">菜单Url</th>
						<th data-options="field:'sort',width:50,align:'center'">排序</th>
						<th data-options="field:'123',width:100,align:'center',formatter: rowformater">操作</th>
					</tr>
				</thead>
			</table>
            </div>   
        </div>   
	
	<div id="editDialog" class="easyui-dialog" style="width:500px;height:400px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
	<!-- 分配按钮 -->
	<div id="assignButDialog" class="easyui-dialog" style="width:700px;height:500px;" closed="true" modal="true" buttons="#dlg-buttonsAssignBut">
		<div id="dlg-buttonsAssignBut" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#assignButDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
</html>