<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<div  id="update"   style="margin: 10px auto;width:80%">

	<form action="" name="" id="updateForm">
	 <input id="id" name="id" value="${dto.id}" type="hidden" />
     <input id="payChannelId" value="${dto.payChannel}" type="hidden" />
		<table>
		   <tr>
				<td width="80px"><font color="red">*</font>支付渠道：</td>
				<td>
				<select id="payChannel" name="payChannel" style="width: 305px;" >
			
				</select>
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>卡类型：</td>
				<td>
				<select name="cardType" id="cardType" style="width: 305px;" >
				<option value="1" <c:if test="${dto.cardType==1}">selected="selected"</c:if> >借记卡</option>
				<option value="2" <c:if test="${dto.cardType==2}">selected="selected"</c:if> >代记卡</option>
				</select>
			
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>业务类型：</td>
				<td>
		        <select name="areaBankFlag" id="areaBankFlag" style="width: 155px;" >
				<option value="1" <c:if test="${dto.areaBankFlag==1}">selected="selected"</c:if>>本行</option>
				<option value="2" <c:if test="${dto.areaBankFlag==2}">selected="selected"</c:if>>跨行</option>
				</select>
				 <select name="type" id="type"  style="width: 148px;" >
				<option value="0" selected="selected">请选择</option>
				<option value="1" <c:if test="${dto.type==1}">selected="selected"</c:if>>同城</option>
				<option value="2" <c:if test="${dto.type==2}">selected="selected"</c:if>>异地</option>
				</select>
			   </td>
			</tr>
			<tr>
				<td><font color="red">*</font>手续费：</td>
				<td>
				  <input name="procedures"<c:if test="${dto.procedures==1}"> checked="checked"</c:if> id="u21_input" type="radio" value="1"/> 按比例(%)<input value="${dto.proportion}" validtype="proportion" class="easyui-validatebox"    id="proportion" name="proportion"  style="width:50px"/>封顶：<input value="${dto.max}"  validtype="max" id="max"  name="max"class="easyui-validatebox" style="width:132px"/><br>
				  <input name="procedures"<c:if test="${dto.procedures==2}"> checked="checked"</c:if> id="u22_input" type="radio" value="2"/> 固定值&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input value="${dto.fixed}"validtype="fixed" class="easyui-validatebox"  id="fixed"name="fixed"  style="width:87px"/>
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>状态：</td>
				<td>
				  <input name="status"<c:if test="${dto.status==1}"> checked="checked"</c:if>id="status1" type="radio" value="1"/>开启
				  <input name="status"<c:if test="${dto.status==2}"> checked="checked"</c:if> id="status2" type="radio" value="2"/>关闭
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
					<textarea rows="10" cols="28" style="width: 298px;" name="remark" maxlength="200" id="remark">${dto.remark}</textarea>
				</td>
			</tr>
		</table>
		<center>
		<c:if test="${vStatus==1}">
		<gd:btn btncode='BTNQDZFFL02'>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn-updateSave" >保存</a>
		</gd:btn>
		</c:if>
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editBankRateConfigDialog').dialog('close')">关闭</a>
	    </center>
	</form>
</div>
<script type="text/javascript" src="${CONTEXT}../js/bankRateConfig/editBankRateConfig.js"></script>
 
