<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="margin: 10px auto;width:80%">
	<form action="" id="editMarketForm">
		<input type="hidden" name="id" value="${dto.id}"/>
		<table>
			<tr>
				<td>市场名称：</td>
				<td>
					<input value="${dto.marketName}" style="width:153px" disabled="disabled" title="${dto.marketName}"/>
				</td>
			</tr>
			<tr>
				<td>用户手机：</td>
				<td>
					<input type="text" name="mobile" value="${dto.mobile}" validType="mobileEditValidate" style="width:153px" disabled="disabled"/>
				</td>
			</tr>
			<tr>
				<td>用户账号：</td>
				<td>
					<input type="text" value="${dto.account}" style="width:153px" disabled="disabled"/>
					<input type="hidden" name="memberId" id="memberIdEdit" value="${dto.memberId}"/>
				</td>
			</tr>
			<tr>
				<td>持卡人:</td>
				<td>
					<input type="text" name="realName" value="${dto.realName}" style="width:153px" class="easyui-validatebox" validType="length[0,30]"/>
				</td>
			</tr>
			<tr>
				<td>银行卡号:</td>
				<td>
					<input type="text" name="bankCardNo" value="${dto.bankCardNo}" style="width:153px" class="easyui-numberbox" validType="length[0,20]" invalidMessage="输入内容长度必须介于0和20之间"/>
				</td>
			</tr>
			<tr>
				<td>开户银行名称：</td>
				<td>
					<input type="text" name="depositBankName" value="${dto.depositBankName}" style="width:153px" class="easyui-validatebox" validType="length[0,100]"/>
				</td>
			</tr>
			<tr>
				<td>支行名称:</td>
				<td>
					<input type="text" name="subBankName" value="${dto.subBankName}" style="width:153px" class="easyui-validatebox" validType="length[0,100]"/>
				</td>
			</tr>
			<tr>
				<td>银行所在地:</td>
				<td>
					<input type="text" name="addr" value="${dto.addr}" style="width:153px" class="easyui-validatebox" validType="length[0,100]"/>
				</td>
			</tr>
			<tr>
				<td>银行卡预留手机:</td>
				<td>
					<input type="text" name="reservePhone" value="${dto.reservePhone}" class="easyui-validatebox" validType="mobileEditValidate" style="width:153px"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		initMobileEditValidate();
	});
	
	function initMobileEditValidate(){
		$.extend($.fn.validatebox.defaults.rules, {    
			mobileEditValidate: { //验证手机号   
		        validator: function(value, param){ 
		         return /^1[3-8]+\d{9}$/.test(value);
		        },    
		        message: '请输入正确的手机号码。'   
		    }
		});
	}
</script>