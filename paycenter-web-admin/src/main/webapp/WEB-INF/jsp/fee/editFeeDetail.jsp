<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<div style="margin: 0 auto;font-size: 17px" >
<form action="" name="editFeeDetailForm" id="editFeeDetailForm">
	<table>
		<tr>
			<td><input type="hidden" name="id" value="${feeRecord.id}"></td>
		</tr>
		<tr>
			<td><font color="red">*</font>来源：</td>
			<td>
				<select id="edit_appKey" name="appKey">
					<option value="">请选择${ payTradeEntity.appKey}</option>
					<option value="nst_car" <c:if test="${payTradeEntity.appKey == 'nst_car'}">selected</c:if>>农速通-车主</option>
					<option value="nst_goods" <c:if test="${payTradeEntity.appKey == 'nst_goods'}">selected</c:if>>农速通-货主</option>
					<option value="nst_fare" <c:if test="${payTradeEntity.appKey == 'nst_fare'}">selected</c:if>>农速通-运费</option>
					<option value="gys" <c:if test="${payTradeEntity.appKey == 'gys'}">selected</c:if>>供应商</option>
					<option value="nsy_pay" <c:if test="${payTradeEntity.appKey == 'nsy_pay'}">selected</c:if>>农批商</option>
					<option value="nsy" <c:if test="${payTradeEntity.appKey == 'nsy'}">selected</c:if>>农商友</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><font color="red">*</font>手续费类型：</td>
			<td>
				<select  id="eidt_feeType" name="feeType">
					<option value="">请选择</option>
					<option value="0" <c:if test="${feeRecord.feeType == 0}">selected</c:if>>交易手续费</option>
					<option value="1" <c:if test="${feeRecord.feeType == 1}">selected</c:if>>转账手续费</option>
					<option value="2" <c:if test="${feeRecord.feeType == 2}">selected</c:if>>其他</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><font color="red">*</font>手续费金额</td>
			<td><input type="number"  value="${feeRecord.feeAmt}" id="edit_feeAmt"  name="payAmt" maxlength="11" style="width:150px" /></td>
		</tr>
		<tr>
			<td>第三方支付流水</td>
			<td><input type="text" value="${feeRecord.thridPayNumber}" id="thridPayNumber" name="thridPayNumber" maxlength="11" style="width:150px" /></td>
		</tr>
		<tr>
			<td><font color="red">*</font>代收/代付时间</td>
			<td>
				<%-- <input type="text" value="${operaTime }" id="edit_operaTime" name="operaTime" maxlength="11" style="width:150px" /> --%>
				<input type="text"    id="edit_operaTime" name="operaTime" value="${operaTime }"
				onFocus="WdatePicker({onpicked:function(){edit_operaTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"   
				onClick="WdatePicker({onpicked:function(){edit_operaTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"    
				style="width:150px" />
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea id="remark" name="remark" rows="5" cols="50" maxlength="500">${feeRecord.remark}</textarea></td>
		</tr>
	</table>
</form>
</div>
