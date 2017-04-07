<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	
	$(document).ready(function(){
		//数据加载
		$('#rightdg').datagrid({
			url:CONTEXT+'sysmgr/sysRole/assignRightQuery?view=${view}&roleID=${dto.roleID}',
			width: 400,  
			height: "auto", 
			nowrap:true,
			toolbar:'#righttb',
			rownumbers:true,
			pagination:false,
			fitColumns:true,
			fit:true,
			view: detailview,
			detailFormatter: function(rowIndex, rowData){
				var innerTable='<table>';
				if(rowData.buttonList.length>0){
					
					for(var i=0;i<rowData.buttonList.length;i++){
						if(i%4==0){
							innerTable=innerTable+'<tr>';
						}
						if("${view}"==""){
							if(rowData.buttonList[i].total>0){
								innerTable=innerTable+'<td width=\'10px\' style=\'border:0;\'><input type=\'checkbox\' btnID_menuID=\'btnID_menuID\' name=\'btnID_'+rowData.menuID+'\' id=\''+rowData.buttonList[i].btnID+'\' value=\''+rowData.buttonList[i].btnID+'\' onclick=buttonCheck(\''+rowIndex+'\',\''+rowData.menuID+'\',\''+rowData.buttonList[i].btnID+'\') checked=\'checked\'></input></td>';
							}else{
								innerTable=innerTable+'<td width=\'10px\' style=\'border:0;\'><input type=\'checkbox\' btnID_menuID=\'btnID_menuID\' name=\'btnID_'+rowData.menuID+'\' id=\''+rowData.buttonList[i].btnID+'\' value=\''+rowData.buttonList[i].btnID+'\' onclick=buttonCheck(\''+rowIndex+'\',\''+rowData.menuID+'\',\''+rowData.buttonList[i].btnID+'\') ></input></td>';
							}
						}
						innerTable=innerTable+'<td width=\'150px\' style=\'border:0;\'>【'+rowData.buttonList[i].btnCode+'】'+rowData.buttonList[i].btnName+'</td>';	
						if((i+1)%4==0){
							innerTable=innerTable+'</tr>';
						}
					}
				}else{
					innerTable=innerTable+'<tr><td align=\'center\' width=\'720px\' style=\'border:0;\'>--没有按钮--</td></tr>';
				}
				innerTable=innerTable+'</table>';
				return innerTable;
			},
			onCheck:function(rowIndex,rowData){
				//当勾选菜单checkbox则下面的铵钮如果全部没有勾选则全部勾选
				if($("input[name='btnID_"+rowData.menuID+"']:checked").length==0){
					$("input[name='btnID_"+rowData.menuID+"']").attr("checked",true);
				}
			},
			onUncheck:function(rowIndex,rowData){
				//当取消勾选菜单checkbox则下面的铵钮全部乐勾选
				$("input[name='btnID_"+rowData.menuID+"']").attr("checked",false);
			},
			onCheckAll:function(rows){
				//当勾选菜单checkbox则下面的铵钮如果全部没有勾选则全部勾选
				/* if($("input[btnID_menuID='btnID_menuID']:checked").length==0){} */
				$("input[btnID_menuID='btnID_menuID']").attr("checked",true);
			},
			onUncheckAll:function(rows){
				//当取消勾选菜单checkbox则下面的铵钮全部乐勾选
				$("input[btnID_menuID='btnID_menuID']").attr("checked",false);
			},
			onLoadSuccess:function(row){//当表格成功加载时执行               
                var rowData = row.rows;
                $.each(rowData,function(idx,val){//遍历JSON
                      if(val.menuTotal>0&&"${view}"==""){
                        $("#rightdg").datagrid("selectRow", idx);//如果数据行为已选中则选中改行
                        var noCount=0;
                        //处理菜单勾选，但没有勾选按钮的情况
                         $.each(val.buttonList,function(index,button){
                        	 if(button.total==0){
                        		 noCount++;
                        	 }
                         });
                         if(noCount==val.buttonList.length){
                        	 $("input[name='btnID_"+val.menuID+"']").attr("checked",false);
                         }
                      }
                });              
            }
		}); 
	});
	
	//按钮勾选则对应的菜单也被勾选
	function buttonCheck(rowIndex,menuID,buttonID){
		if($("#"+buttonID).attr("checked")){
			$('#rightdg').datagrid('selectRow', rowIndex);
		}/* else{
			if($("input[name='btnID_"+menuID+"']:checked").length==0){
				$('#rightdg').datagrid('unselectRow', rowIndex);
			}
		}	 */ 
	}
	
	//确定分配
	function saveAssignRight(){
		//判断是否选中
		var row = $('#rightdg').datagrid("getSelections");
		var msg='您确定要分配所选数据吗?';
        if($(row).length < 1 ) {
        	msg='您确定要取消所有分配数据吗?';
        }
		jQuery.messager.confirm('提示', msg, function(r){
			if (r){
        		var menuIDs = getSelections("menuID");
        		if(menuIDs==""){
        			trCheckBox=null;
        		}else{
        			var menuIDAry=menuIDs.split(",");
            		//根据菜单ID获取按钮，构造成：menuID1:buttonID1,buttonID2|menuID2:buttonID1,buttonID2格式
            		var trCheckBox="";
            		for(var i=0;i<menuIDAry.length;i++){
            			if(i!=(menuIDAry.length-1)){
            				trCheckBox=trCheckBox+menuIDAry[i]+":"+getSelections("btnID_"+menuIDAry[i])+"┩┩";
            			}else{
            				trCheckBox=trCheckBox+menuIDAry[i]+":"+getSelections("btnID_"+menuIDAry[i]);
            			}
            		}
        		}
        		jQuery.post(CONTEXT+"sysmgr/sysRole/assignRight",{"trCheckBox":trCheckBox,"roleID":"${dto.roleID}"},function(data){
    				if (data == "success"){
						slideMessage("操作成功！");
    					$("#rightdg").datagrid('reload');
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
</script>
<table id="rightdg" title="" >
	<thead>
		<tr>
			<c:if test="${empty view }">
			<th data-options="field:'menuID',checkbox:true"></th>
			</c:if>
			<th data-options="field:'menuCode',width:80,align:'left'">菜单编号</th>
			<th data-options="field:'menuName',width:180,align:'left'">菜单钮名称</th>
		</tr>
	</thead>
</table>
<div id="righttb" style="padding:5px;height:auto">
	<form id="searchMenuForm" method="post">
	<div>
		菜单编号: <input class="easyui-searchbox" type="text" id="menuCode" name="menuCode" style="width:150px" data-options="prompt:'请输入菜单编号',  
           searcher:function(value,name){queryMenu2(value,name)}">
		菜单名称: <input class="easyui-searchbox" type="text" id="menuName" name="menuName" style="width:150px" data-options="prompt:'请输入菜单名称',  
           searcher:function(value,name){queryMenu2(value,name)}">
		<a class="easyui-linkbutton" iconCls="icon-search" onclick="queryMenu()">查询</a>
		<a class="easyui-linkbutton" iconCls="icon-reload" onclick="clearMenudata()">重置</a>
		<a class="easyui-linkbutton" iconCls="icon-reload" onclick='reloadCurPage()'">刷新</a>
	</div>
	</form>
	<div style="margin-bottom:5px">
		当前角色是：${dto.roleName }
		<c:if test="${empty view }">
			<a style="margin-left:15px" class="easyui-linkbutton" iconCls="icon-group-link" onclick="saveAssignRight()">确定分配</a>
		</c:if>
	</div>
</div>
