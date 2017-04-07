<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	function saveUser() {
		var url=CONTEXT+"sysmgr/sysRegisterUser/${actionUrl}";
		if ($('#editUserForm').form("validate")) {
			jQuery.post(url, $('#editUserForm').serialize(), function (data) {
				if (data == "success") {
					slideMessage("操作成功！");
					//刷新父页面列表
					$("#userSelect").datagrid('reload');
					$('#editUserDialog').dialog('close');
				} else {
					warningMessage(data);
					return;
				}
			});
		}
	}

</script>
<form id="editUserForm" method="post" >
<input type="hidden" id="userID" name="userID" value="${dto.userID }">
<input type="hidden" id="type" name="type" value="${type }"/>
<input type="hidden" id="orgUnitId" name="orgUnitId" value="${orgUnitId }"/>
<br/>
<div>
	<table class="grid" align="center">
		<tr>
			<td><em style="color:red">*</em><b>用户帐号：</b></td>
			<td>
				<c:if test="${empty dto.userID }">
				<input type="text" id="userCode" name="userCode" class="easyui-validatebox" data-options="required:true,validType:'loginName'" maxlength="10" value="${dto.userCode }"/>
				</c:if>
				<c:if test="${not empty dto.userID }">
				${dto.userCode }
				</c:if>
			</td>
		</tr>
		<tr>
			<td><em style="color:red">*</em><b>用户名称：</b></td>
			<td>
				<input type="text" id="userName" name="userName" class="easyui-validatebox" data-options="required:true,validType:'objectName'" maxlength="10" value="${dto.userName }"/>
			</td>
		</tr>
		<tr>
			<td><b>联系电话：</b></td>
			<td>
				<input type="text" id="mobile" name="mobile" class="easyui-validatebox" maxlength="11" value="${dto.mobile }"/>
			</td>
		</tr>
		<c:if test="${empty dto.userID }">
			<tr>
				<td><em style="color:red">*</em><b>用户密码：</b></td>
				<td>
					<input type="password" id="passWord" name="passWord" class="easyui-validatebox" data-options="required:true,validType:'password'" maxlength="16" value="${dto.userPassWord }"/>
				</td>
			</tr>
		</c:if>
	</table>
</div>
</form>