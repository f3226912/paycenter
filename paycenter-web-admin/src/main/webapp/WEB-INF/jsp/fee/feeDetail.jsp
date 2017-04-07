<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<div style="margin: 0 auto;font-size: 17px" >
<form name="addForm" id="addForm">
	<table>
		<tr>
			<td><font color="red">*</font>来源：</td>
			<td>
				<select id="appKey" name="appKey">
					<option value="">请选择</option>
					<option value="nst_car">农速通-车主</option>
					<option value="nst_goods">农速通-货主</option>
					<option value="nst_fare">农速通-运费</option>
					<option value="gys">供应商</option>
					<option value="nsy_pay">农商友</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><font color="red">*</font>手续费类型：</td>
			<td>
				<select  id="feeType" name="feeType">
					<option value="">请选择</option>
					<option value="0">交易手续费</option>
					<option value="1">转账手续费</option>
					<option value="2">其他</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><font color="red">*</font>手续费金额</td>
			<td><input type="number" id="feeAmt"  name="feeAmt" maxlength="11" style="width:150px" /></td>
		</tr>
		<tr>
			<td>第三方支付流水</td>
			<td><input type="text" id="thridPayNumber" name="thridPayNumber" maxlength="11" style="width:150px" /></td>
		</tr>
		<tr>
			<td><font color="red">*</font>代收/代付时间</td>
			<td>
				<input type="text"    id="operaTime" name="operaTime"
				onFocus="WdatePicker({onpicked:function(){edit_operaTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"   
				onClick="WdatePicker({onpicked:function(){edit_operaTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"    
				style="width:150px" />
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea id="remark" name="remark" rows="5" cols="50" maxlength="500"></textarea></td>
		</tr>
	</table>
</form>
</div>
