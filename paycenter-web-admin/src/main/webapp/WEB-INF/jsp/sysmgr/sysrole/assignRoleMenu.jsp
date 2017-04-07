<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(document).ready(function(){
		   $('#treeMenu').tree({
			   url:CONTEXT+'sysmgr/sysMenu/getMenuButtonTree?roleID=${dto.roleID}',
		        animate:true,
		        checkbox:true,
		   });
	});
	
	//确定分配
	function saveAssignRight(roleID,attribute){
		/* if(1==attribute||3==attribute){
			
		}else{
			warningMessage("该角色类型无法分配后台权限,如有需求，请先修改角色类型");
			return;
		} */
		//判断是否选中
		var row = $('#treeMenu').tree('getChecked');	// 获取选中的节点
		var nodes2 = $('#treeMenu').tree('getChecked','indeterminate');//获取未确定节点，即：已选中节点的父节点，处于非勾选状态的选中节点
		var msg='您确定要分配所选数据吗?';
        if($(row).length < 1 ) {
        	msg='您确定要取消所有分配数据吗?';
        }
		jQuery.messager.confirm('提示', msg, function(r){
			if (r){
				var menuIDs="";
				var btnIDs="";
				$(row).each(function(){
					var node=$(this);
					if(node[0].attributes.type=='9'){
						btnIDs+=node[0].id.toString()+",";
					}else if(node[0].attributes.type=='1'){
						menuIDs+=node[0].id.toString()+",";
					}
				});
				$(nodes2).each(function(){
					var node=$(this);
					if(node[0].attributes.type=='9'){
						btnIDs+=node[0].id.toString()+",";
					}else if(node[0].attributes.type=='1'){
						menuIDs+=node[0].id.toString()+",";
					}
				});
				
				jQuery.post(CONTEXT+"sysmgr/sysRole/assignMenu",
						{
							"roleID":roleID,
		    				"menuIDs":menuIDs,
		    				"btnIDs":btnIDs
						},
						function(data){
		    				if (data == "success"){
								slideMessage("操作成功！");
								$("#treeMenu").tree('reload');
		    				}else{
								warningMessage("操作失败！");
		    					return;
		    				}
						}
        		); 
			}else{
				return;
			}
		});
	}
	
	//查询
	function queryMenu(){
		queryParams = $('#rightdg').datagrid('options').queryParams;  
        queryParams.menuName = $('#menuName').searchbox('getValue');
		queryParams.menuCode = $("#menuCode").searchbox("getValue");
        $("#rightdg").datagrid('reload'); 
	}
	
	//单独查询
	function queryMenu2(value,name){
		 var queryParams = $('#rightdg').datagrid('options').queryParams;  
		 if(name=='menuName'){
		 	queryParams.menuName = value;
		 }
		 if(name=='menuCode'){
		 	queryParams.menuCode = value;
		 }
         $("#rightdg").datagrid('reload'); 
	}
	
	//重置
	function clearMenudata(){
		$('#menuName').searchbox('setValue',"");
		$("#menuCode").searchbox("setValue","");
	}
	
	//刷新
	function reloadCurPage(){
		$("#rightdg").datagrid('reload'); 
	}
	//分配前台权限
	function addAction(id,attribute){
		var url= CONTEXT + 'sysmgr/sysRole/toAssignRight?roleID=' + id;
		window.location.href=url;
	}
	//分配后台权限
	function assignRight(id,attribute) {
		var url=CONTEXT + 'sysmgr/sysRole/assignMenuList?roleID=' + id;
		window.location.href=url;
	}
	function back(){
		window.location.href=CONTEXT + 'sysmgr/sysRole/list';
	}
</script>
<div id="righttb" style="padding:5px;height:auto">
	<div style="margin-bottom:5px">
		角色名称：${dto.roleName }&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-group-link" onclick="saveAssignRight('${dto.roleID }','${dto.attribute}')">确定分配</a>
	</div>
	<div>
		<ul id="treeMenu" class="ztree"></ul>
	</div>
</div>
