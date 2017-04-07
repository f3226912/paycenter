<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<div style="margin: 0 auto;font-size: 17px" >
	<table>
		<tr>
			<td>用户账号：${info.account }</td>
			<td>持卡人：${info.realName }</td>
		</tr>
		<tr>
			<td>用户手机号：${info.telephone }</td>
			<td>银行卡号：${info.bankCardNo }</td>
		</tr>
		<tr>
			<td>提交时间：<fmt:formatDate value="${info.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td>开户银行名称：${info.depositBankName }</td>
		</tr>
		<tr>
			<td>验证时间：<fmt:formatDate value="${info.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td>开户行所在地：${info.provinceName}${info.cityName}${info.areaName }</td>
		</tr>
		<tr>
			<td>身份证：${info.idCard }</td>
			<td>支行名称：${info.subBankName }</td>
		</tr>
		<tr>
			<td>状态：${info.auditStatus==null? "未验证" : info.auditStatus == "1" ? "已通过" : info.auditStatus == "2" ? "未验证" : "未通过" }</td>
			<td>银行卡预留手机：${info.mobile }</td>
		</tr>
		
	</table>
	
	
</div>
