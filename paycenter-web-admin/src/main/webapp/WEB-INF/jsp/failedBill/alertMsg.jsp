<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input type="hidden" id="resultType" value="${resultType}"></input>

   <c:if test="${resultType == 1}">
     <div id="alertInfoDiv1">	
		<table class="beneficiaryDetail" align="center" width="100%" >
			<tr>
				<td width="25%" height="15px">&nbsp;<b>执行前，请检查修改数据是否正确 ，执行会进行下列操作</b></td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;1.修改对账状态</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;2.若需要对账，执行清分操作</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;3.更新对账/订单信息</td>
			</tr>
		</table>
		</div>
		</c:if>
		
		<c:if test="${resultType == 2}">
		<div id="alertInfoDiv2">	
		<table class="beneficiaryDetail" align="center" width="100%" >
			<tr>
				<td width="25%" height="15px">&nbsp;<b>执行前，请检查修改数据是否正确 ，执行会进行下列操作</b></td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;1.执行对账操作</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;2.对账成功后，执行清分操作</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;3.更新对账文件修改数据</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;4.更新订单中心数据更新</td>
			</tr>
		</table>
		</div>
		</c:if>
		
		<c:if test="${resultType == 3}">
		<div id="alertInfoDiv3">	
		<table class="beneficiaryDetail" align="center" width="100%" >
			<tr>
				<td width="25%" height="15px">&nbsp;<b>执行前，请检查修改数据是否正确 ，执行会进行下列操作</b></td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;1.修改对账状态</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;2.若需要对账，执行清分操作</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;3.更新对账/订单信息</td>
			</tr>
			<tr>
				<td width="25%" height="15px">&nbsp;&nbsp;&nbsp;4.若需要制单，根据提交信息通知订单中心制单;</td>
			</tr>
		</table>
		</div>
        </c:if>