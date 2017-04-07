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
<!-- 		<link rel="stylesheet" href="../../css/zTree/zTreeStyle.css" type="text/css"> -->
<!-- 		<link rel="stylesheet" href="../../css/zTree/demo.css" type="text/css"> -->
		<title>系统菜单-按钮管理</title>
	</head>
<script type="text/javascript">
	var menuID='${menuID}';
	$(document).ready(function(){
		   $('#treeMenu').tree({
		        url:CONTEXT+'sysmgr/sysMenu/getFirstMenu',
		        animate:true,
		        onClick:function(node){
		        	if(node.attributes.type=='1'){
			    		menuID=node.id;
			    		$("#menuID").val(menuID);
			            buttonQuery();
		        	}else{
		        		$("#menuID").val("");
		        	}
		        }
		   });
		
		
		//数据加载
		$('#buttondg').datagrid({
			url:CONTEXT+'sysmgr/sysMenu/buttionQuery',
			width: 400,  
			height: 'auto', 
			nowrap:true,
			toolbar:'#buttontb',
			pageSize:20,
			rownumbers:true,
			pagination:true,
			fitColumns:true,
			fit:true
		}); 
		//分页加载
		$("#buttondg").datagrid("getPager").pagination({   
			pageList: [10,20,50,100]
	    }); 
	});

	//查询
	function buttonQuery(){
//         queryParams.btnName = $('#btnName').searchbox('getValue');
// 		queryParams.btnCode = $("#btnCode").searchbox("getValue");
		$('#buttondg').datagrid({
			queryParams: {
				menuID: $("#menuID").val()
			}
		});
        $("#buttondg").datagrid('reload'); 
	}
	
	//单独查询
	function buttonQuery2(value,name){
		 var queryParams = $('#buttondg').datagrid('options').queryParams;  
		 if(name=='btnName'){
		 	queryParams.btnName = value;
		 }
		 if(name=='btnCode'){
		 	queryParams.btnCode = value;
		 }
         $("#buttondg").datagrid('reload'); 
	}
	
	//重置
	function clearButtonData(){
		$('#btnName').searchbox('setValue',"");
		$("#btnCode").searchbox("setValue","");
	}
	
	//新增
	function addButtonObj(){
		if($("#menuID").val()){
			$('#editButtonDialog').dialog({'title':'新增','href':CONTEXT+'sysmgr/sysMenu/addButtonInit?menuID='+menuID}).dialog('open');
		}else{
			warningMessage("请选择要添加按钮的菜单！");
		}
	}
	
	//删除按钮
	function delButtonObj(){
		//判断是否选中
		var row = $('#buttondg').datagrid("getSelections");
        if($(row).length < 1 ) {
			slideMessage("请选择要操作的数据！");
            return ;
        }
		jQuery.messager.confirm('提示', '您确定要删除所选数据吗?', function(r){
			if (r){
        		var deleteStr = getSelections("btnID");
        		jQuery.post(CONTEXT+"sysmgr/sysMenu/deleteButton",{"btnIDs":deleteStr},function(data){
    				if (data == "success"){
						slideMessage("操作成功！");
    					$("#buttondg").datagrid('reload');
    					$('#buttondg').datagrid("uncheckAll");
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
	
	function deleteButAction(id){
		jQuery.messager.confirm('提示', '您确定要删除所选数据吗?', function(r){
			if (r){
        		var deleteStr = id;
        		jQuery.post(CONTEXT+"sysmgr/sysMenu/deleteButton",{"btnIDs":deleteStr},function(data){
    				if (data == "success"){
						slideMessage("操作成功！");
    					$("#buttondg").datagrid('reload');
    					$('#buttondg').datagrid("uncheckAll");
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
	function updateButAction(id){
		$('#editButtonDialog').dialog({'title':'修改','href':CONTEXT+'sysmgr/sysMenu/updateButtonInit?btnID='+id}).dialog('open');
		$('#buttondg').datagrid("uncheckAll");
	}
	
	function rowButtonFormater(value,row,index){
		
		var html="";
		html=html+"<a class='operate' href='javascript:;' onclick=updateButAction('"+row.btnID+"');>修改</a>";
		html=html+"<a class='operate' href='javascript:;' onclick=deleteButAction('"+row.btnID+"');>删除</a>";
		return html;
	}
	
</script>
<body class="easyui-layout">
	<div data-options="region:'north'" style="padding-top: 10px;background: #fafafa;border-color: #ddd;">
		<div>
		<form id="searchButtonForm" method="post">
<!-- 			按钮编号:  -->
<%-- 			<input class="easyui-searchbox" type="text" id="btnCode" name="btnCode" style="width:150px" data-options="prompt:'请输入按钮编号',   --%>
<%-- <!-- 	           searcher:function(value,name){buttonQuery2(value,name)}">  --%>
<!-- 			<input type="hidden" id="btnCode" name="btnCode" > -->
<!-- 			<input class="easyui-validatebox" type="hidden" id="btnCode" name="btnCode" data-options="" />    -->
<!-- 			按钮名称:  -->
<%-- 			<input class="easyui-searchbox" type="text" id="btnName" name="btnName" style="width:150px" data-options="prompt:'请输入按钮名称',   --%>
<!-- <!-- 	           searcher:function(value,name){buttonQuery2(value,name)}">   -->
<!-- 			<input type="hidden" id="btnName" name="btnName" >  -->
	           <input id="menuID" name="menuID" value="${menuID }" type="hidden" />
<!-- 			<a class="easyui-linkbutton" iconCls="icon-search" onclick="buttonQuery()">查询</a> -->
<!-- 			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="clearButtonData()">重置</a> -->
		</form>
		<div style="margin-bottom:5px;padding-left: 10px;">
			<a class="easyui-linkbutton" iconCls="icon-add" onclick="addButtonObj()">新增</a>
			<a class="easyui-linkbutton" iconCls="icon-remove" onclick="delButtonObj()">删除</a>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)'>刷新</a>
		</div>
		</div>
	</div>   
    <div data-options="region:'center'" style="padding-top: 10px;background: #fafafa;">   
        <div class="easyui-layout" data-options="fit:true">   
            <div data-options="region:'west',expand:true" style="width:250px;padding-left: 10px;">
                <div data-options="region:'west',expand:true" style="width:250px">
	            	<ul id="treeMenu" class="ztree"></ul>
            	</div>   
            </div>   
            <div data-options="region:'center'">
            	<table id="buttondg" title="" >
					<thead>
						<tr>
							<th data-options="field:'btnID',checkbox:true"></th>
							<th data-options="field:'btnCode',width:80,align:'center',sortable:true">按钮编号</th>
							<th data-options="field:'btnName',width:80,align:'center',sortable:true">按钮名称</th>
							<th data-options="field:'action',width:80,align:'center',formatter: rowButtonFormater">操作</th>
						</tr>
					</thead>
				</table>
            </div>   
        </div>   
    </div>
    <!-- 分配管理-->
	<div id="editButtonDialog" class="easyui-dialog" style="width:300px;height:150px;" closed="true" modal="true" buttons="#dlg-buttons2Edit">
		<div id="dlg-buttons2Edit" style="text-align:center">
	     	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
	         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editButtonDialog').dialog('close')">关闭</a>
	     </div>
	</div>
</body>
</html>