<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="margin: 10px auto;width:80%">
	<form action="" id="addMarketForm">
		<table>
			<tr>
				<td><font color="red">*</font>市场名称：</td>
				<td>
					<select name="marketName" id="addMarket" style="width:158px" class="easyui-validatebox" required="true">
					</select>
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>用户手机：</td>
				<td>
					<input type="text" name="mobile" id="mobileAdd" validType="mobileAddValidate" style="width:153px" class="easyui-validatebox" required="true"/>
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>用户账号：</td>
				<td>
					<input type="text" name="account" id="accountAdd" style="width:153px" class="easyui-validatebox" required="true"/>
					<input type="hidden" name="memberId" id="memberIdAdd"/>
				</td>
			</tr>
			<tr>
				<td>持卡人:</td>
				<td>
					<input type="text" name="realName" style="width:153px" class="easyui-validatebox" validType="length[0,30]"/>
				</td>
			</tr>
			<tr>
				<td>银行卡号:</td>
				<td>
					<input type="text" name="bankCardNo" style="width:153px" class="easyui-numberbox" validType="length[0,20]" invalidMessage="输入内容长度必须介于0和20之间"/>
				</td>
			</tr>
			<tr>
				<td>开户银行名称：</td>
				<td>
					<input type="text" name="depositBankName" style="width:153px" class="easyui-validatebox" validType="length[0,100]"/>
				</td>
			</tr>
			<tr>
				<td>支行名称:</td>
				<td>
					<input type="text" name="subBankName" style="width:153px" class="easyui-validatebox" validType="length[0,100]"/>
				</td>
			</tr>
			<tr>
				<td>开户所在地:</td>
				<td>
					<input type="text" name="addr" style="width:153px" class="easyui-validatebox" validType="length[0,100]"/>
				</td>
			</tr>
			<tr>
				<td>银行卡预留手机:</td>
				<td>
					<input type="text" name="reservePhone" class="easyui-validatebox" validType="mobileAddValidate" style="width:153px"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		initAddMarketList();
		initMobileAddValidate();
	});
	
	function initAddMarketList(){
		var marketList = queryMarketList();
		$.each(marketList, function(i, item){
			$("#addMarket").append("<option value='"+item.id+","+item.marketName+"'>"+item.marketName+"</option>");   
		}); 
	}
	
	$("#accountAdd").focus(function(){
		$.ajax({
	        type: "POST",
	        url: CONTEXT + "marketBankAccInfo/getByMobile",
	        data:{"mobile" : $("#mobileAdd").val()},
	        dataType: "json",
	        success: function(data){
	        	if (data.code == 10000) {
	        		if(data.result != null){
	        			$("#accountAdd").val(data.result.account);
	        			$("#memberIdAdd").val(data.result.memberId);
	        		}
	    		}else{
	    			warningMessage("用户不存在！");
	    			return;
	    		}
	        },
	        error: function(){
	        	warningMessage("服务器出错");
	        }
		});
	});
	
	function initMobileAddValidate(){
		$.extend($.fn.validatebox.defaults.rules, {    
			mobileAddValidate: { //验证手机号   
		        validator: function(value, param){ 
		         return /^1[3-8]+\d{9}$/.test(value);
		        },    
		        message: '请输入正确的手机号码。'   
		    }
		});
	}
</script>