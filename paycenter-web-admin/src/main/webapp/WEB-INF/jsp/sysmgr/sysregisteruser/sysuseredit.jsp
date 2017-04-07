<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	$(function(){
		//用户类型
		$('#editForm #type').combobox({    
		    url:CONTEXT+'sysmgr/sysRegisterUser/getType',    
		    valueField:'id',    
		    textField:'text',
		    editable:false,
		    required:true,
		    onLoadSuccess: function () { 
		    	//数据加载完毕事件
		    	if("${dto.userID}"==""){
		    		 var data = $('#editForm #type').combobox('getData');
		                if (data.length > 0) {
		                    $('#editForm #type').combobox('setValue', data[0].id);
		                }
		    	}
		    	var comVal=$('#editForm #type').combobox('getValue');
		    	//是否验证组织
		    	isCheckOrg(comVal);
            },
            onChange:function(newValue,oldValue){
            	//是否验证组织
		    	isCheckOrg(newValue);
            }
		});  
		
	});
	
	//是否验证组织
	function isCheckOrg(type){
		if(type=="0"){
			//不验证
			$('#editForm #orgButton').linkbutton('disable');
    		$("#editForm #orgUnitName").validatebox('disableValidation');
		}else{
			//验证
			$('#editForm #orgButton').linkbutton('enable');
    		$("#editForm #orgUnitName").validatebox('enableValidation');
		}
	}
	
	function save() {
		if(!validateUser()){
			return false;
		}
		var url=CONTEXT+"sysmgr/sysRegisterUser/${actionUrl}";
		if ($('#editForm').form("validate")) {
			jQuery.post(url, $('#editForm').serialize(), function (data) {
				if (data == "success") {
					slideMessage("操作成功！");
					//刷新父页面列表
					$("#dg").datagrid('reload');
					$('#editDialog').dialog('close');
				} else {
					if(data=='sameUser'){
						warningMessage('该用户帐号已作废或已被占用！');
					}else if(data=='fail'){
						warningMessage('操作失败！');
					}
					return;
				}
			});
		}
	}
	
	function validateUser(){
		var userCode=$.trim($('#editForm #userCode').val());
		var len=userCode.replace(/[\u2E80-\u9FFF]/g,"aa").length;
		if(len>=6&&/^[0-9]/g.test(userCode)){
			warningMessage("用户名不能以数字开头");
			return false;
		}
		if(len>=6&&/[\u4E00-\u9FA5]/i.test(userCode)){
			warningMessage("用户名不能有中文");
			return false;
		}
		console.log(userCode);
		console.log(len);
		if(len<6||len>20||!/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/g.test(userCode)){
			warningMessage("用户名只能6-20个字符，可包含英文字母、中文、数字和下划线。");
			return false;
		} 
		if(!$("#userID").val()){
			var password=$.trim($("#editForm #passWord").val());
			var len2=password.length;
			if(len2==0){
				warningMessage("请重新输入密码，可以是数字、英文、特殊符号或组合");
				return false;
			}
			if(len2<6||len2>20){
				warningMessage("密码长度不正确，只能6-20个字符");
				return false;
			}
			if(len2>=6&&/^\s+$/g.test(password)){
				warningMessage("密码不能包含空格");
				return false;
			}
		}
		
		var mobile=$("#editForm #mobile").val();
		if(mobile){
			if(!(mobile.length==11&&/(^[1][34587][0-9]{9}$)/g.test(mobile))){
				warningMessage("手机号不正确！");
				return false;
			}
		}
		return true;
	}
	
	
</script>
<form id="editForm" method="post">
<input type="text" value="" style="display: none;"/>
<input type="passWord" value="" style="display: none;"/>
<input type="hidden" id="userID" name="userID" value="${dto.userID }">
<br/>
<div>
	<table class="grid" align="center">
		<tr>
			<td align="right"><em style="color:red">*</em><b>用户帐号：</b></td>
			<td>
				<c:if test="${empty dto.userID }">
				<input type="text" id="userCode" name="userCode" class="easyui-validatebox" data-options="required:true,validType:'loginName'" maxlength="20" value="" />
				</c:if>
				<c:if test="${not empty dto.userID }">
				<input type="hidden" id="userCode" name="userCode" value="${dto.userCode }" />
				${dto.userCode }
				</c:if>
			</td>
		</tr>
		<tr>
			<td align="right"><em style="color:red">*</em><b>用户名称：</b></td>
			<td>
				<input type="text" id="userName" name="userName" class="easyui-validatebox" data-options="required:true,validType:'objectName'" maxlength="20" value="${dto.userName }"/>
			</td>
		</tr>
		<tr>
			<td align="right"><em style="color:red">*</em><b>用户职位：</b></td>
			<td>
				<input type="text" id="orgUnitId" name="orgUnitId" class="easyui-validatebox" data-options="required:true,validType:'objectName'" maxlength="20" value="${dto.orgUnitId }"/>
			</td>
		</tr>
		<tr>
			<td align="right"><b>联系电话：</b></td>
			<td>
				<input type="text" id="mobile" name="mobile" class="easyui-validatebox" maxlength="11" value="${dto.mobile }"/>
			</td>
		</tr>
		<c:if test="${empty dto.userID }">
			<tr>
				<td align="right"><em style="color:red">*</em><b>初始密码：</b></td>
				<td>
					<input type="password" editable="false" id="passWord" name="passWord" class="easyui-validatebox" data-options="required:true" maxlength="20" value="888888" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" style="ime-mode:disabled"/>
				</td>
			</tr>
		</c:if>
<%-- 		<c:if test="${!empty dto.userID }"> --%>
<!-- 			<tr> -->
<!-- 				<td align="right"><em style="color:red">*</em><b>用户密码：</b></td> -->
<!-- 				<td> -->
<%-- 					<input type="passWord" id="passWord" name="passWord" class="easyui-validatebox" data-options="required:true" maxlength="20" value="${dto.userPassWord }"/> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
<%-- 		</c:if> --%>
	</table>
</div>
</form>