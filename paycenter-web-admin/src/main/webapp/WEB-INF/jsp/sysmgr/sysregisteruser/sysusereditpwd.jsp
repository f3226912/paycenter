<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	function saveEditPwd() {
		if(!validateUser()){
			return false;
		}
		var url="${CONTEXT}sysmgr/sysRegisterUser/updateUserPwd";
		if ($('#pwdForm').form("validate")) {
			jQuery.post(url, $('#pwdForm').serialize(), function (data) {
				if (data == "success") {
					slideMessage("操作成功！");
					$('#headerEditDialog').dialog('close');
				} else {
					warningMessage('操作失败!原始密码输入错误！');
					return;
				}
			});
		}
	}
	
	function validateUser(){
		var password=$.trim($("#pwdForm #rnewPWD").val());
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
		return true;
	}
	
</script>
<form id="pwdForm" method="post" >
<input type="hidden" id="userID" name="userID" value="${systemUserId }">
<br/>
<div>
	<table style="width:100%;height:100%; border-collapse: separate;border-spacing: 4px;" align="center">
		<tr>
			<td align="right"><em style="color:red">*</em><b>原密码：</b></td>
			<td>
				<input type="password" id="oldPWD" name="oldPWD" class="easyui-validatebox" data-options="required:true,validType:'password'" maxlength="20" />
			</td>
		</tr>
		<tr>
			<td align="right"><em style="color:red">*</em><b>新密码：</b></td>
			<td>
				<input type="password" id="newPWD" name="newPWD" class="easyui-validatebox" data-options="required:true,validType:'password'" maxlength="20" />
			</td>
		</tr>
		<tr>
			<td align="right"><em style="color:red">*</em><b>确认密码：</b></td>
			<td>
				<input type="password" id="rnewPWD" name="rnewPWD" class="easyui-validatebox" required="required" validType="equalTo['#newPWD']" maxlength="20" />
			</td>
		</tr>
	</table>
</div>
</form>