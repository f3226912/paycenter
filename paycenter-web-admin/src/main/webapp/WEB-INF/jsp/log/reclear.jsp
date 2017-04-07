<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<div style="margin: 0 auto;font-size: 17px" ></div>
<form action="" name="reclearform" id="reclearform">
	<table>
		<!-- <tr>
			<td><font color="red">*</font>支付渠道:</td>
			<td>
				<select id="reClearPayType" name="reClearPayType" style="width:150px">
					<option value="">请选择</option>
					<option value="ALIPAY_H5">支付宝</option>
					<option value="WEIXIN_APP">微信</option>
					<option value="POS">POS刷卡</option>
				</select>
			</td>
		</tr> -->
		<tr>
			<td><font color="red">*</font>交易支付日期：</td>
			<td>
				<input  type="text"  id="reClearPayTime" name="reClearPayTime"  
					onFocus="WdatePicker({onpicked:function(){reClearPayTime.focus();},dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){reClearPayTime.focus();},dateFmt:'yyyy-MM-dd'})"    
					style="width:150px">
			</td>
			<script type="text/javascript">
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth()+1;
			var day = date.getDate();
			$("#reClearPayTime").val(year+'-'+month+'-'+day);
			</script>
		</tr>
	</table>
</form>