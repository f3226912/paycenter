<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">

	function save() {
		var url=CONTEXT+"sysmgr/sysMenu/${actionUrl}";
		if ($('#editButtonForm').form("validate")) {
			jQuery.post(url, $('#editButtonForm').serialize(), function (data) {
				if (data == "success") {
					slideMessage("操作成功！");
					//刷新父页面列表
					$("#buttondg").datagrid('reload');
					$('#editButtonDialog').dialog('close');
				} else {
					warningMessage(data);
					return;
				}
			});
		}
	}
</script>
<form id="editButtonForm" method="post" >
<c:if test="${empty menuID }">
<input type="hidden" id="menuID" name="menuID" value="${dto.menuID }">
</c:if>
<c:if test="${not empty menuID }">
<input type="hidden" id="menuID" name="menuID" value="${menuID }">
</c:if>
<input type="hidden" id="btnID" name="btnID" value="${dto.btnID }">
<br/>
<div>
	<table class="grid" align="center">
		<tr>
			<td><em style="color:red">*</em><b>按钮编号：</b></td>
			<td>
				<input type="text" id="btnCode" name="btnCode" class="easyui-validatebox" data-options="required:true,validType:'enmath'" maxlength="32" value="${dto.btnCode }"/>
			</td>
		</tr>
		<tr>
			<td><em style="color:red">*</em><b>按钮名称：</b></td>
			<td>
				<input type="text" id="btnName" name="btnName" class="easyui-validatebox" data-options="required:true,validType:'btnName'" maxlength="30" value="${dto.btnName }"/>
			</td>
		</tr>
	</table>
</div>
</form>