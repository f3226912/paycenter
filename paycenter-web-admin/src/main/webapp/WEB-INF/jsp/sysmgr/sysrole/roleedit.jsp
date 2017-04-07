<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script type="text/javascript">
	function save() {
		var url=CONTEXT+"sysmgr/sysRole/${actionUrl}";
		var msg='如果角色类型改变，会清除角色现有的权限，请谨慎操作！';
		jQuery.messager.confirm('提示', msg, function(r){
			if (r){
				if ($('#editForm').form("validate")) {
					jQuery.post(url, $('#editForm').serialize(), function (data) {
						if (data == "success") {
							slideMessage("操作成功！");
							//刷新父页面列表
							$("#dg").datagrid('reload');
							$('#editDialog').dialog('close');
						} else {
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
</script>
<form id="editForm" method="post" >
<input type="hidden" id="roleID" name="roleID" value="${dto.roleID }">
<br/>
<div>
	<table class="grid" align="center">
		<tr>
			<td align="right"><em style="color:red">*</em><b>角色名称：</b></td>
			<td>
				<input type="text" id="roleName" name="roleName" class="easyui-validatebox" size="38" data-options="required:true,validType:'inputName'" maxlength="30" value="${dto.roleName }"/>
			</td>
		</tr>
		<tr>
			<td align="right"><em style="color:red">*</em><b>角色类型：</b></td>
			<td>
				<select id="attribute" name="attribute" <c:if test="${!empty dto}">disabled="disabled"</c:if> class="easyui-combobox" editable="false" panelHeight="auto" style="width:100px">
					<option value="1" <c:if test="${dto.attribute==1}">selected="selected"</c:if>>后台角色</option>
		<%-- 			<option value="2" <c:if test="${dto.attribute==2}">selected="selected"</c:if>>前台角色</option>
					<option value="3" <c:if test="${dto.attribute==3}">selected="selected"</c:if>>前后台角色</option> --%>
				</select>
			</td>
			<input type="hidden" name="oldAttribute" value="${dto.attribute}"/>
		</tr>
		<tr>
			<td align="right"><b>备注：</b></td>
			<td>
				<textarea id="remark" name="remark" rows="5" cols="40" maxlength="400" placeholder="最多输入400个字符">${dto.remark }</textarea>
			</td>
		</tr>
	</table>
</div>
</form>