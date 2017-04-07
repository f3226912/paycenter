<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div style="width:350px; margin:20px auto;">
	<form id="transferForm" action="" method="post">
	<table style="border-spacing:10px;">
		<tr>
			<td>转结算类型：</td>
			<td>
				<input type="checkbox" name="type" value="1"/>订单 
				<input type="checkbox" name="type" value="3"/>退款 
				<input type="checkbox" name="type" value="4"/>违约金 
				<input type="checkbox" name="type" value="2"/>市场佣金
			</td>
		</tr>
		<tr>
			<td>转结算日期：</td>
			<td>
				<input type="text" id="settleDate" name="settleDate"  
					onFocus="WdatePicker({onpicked:function(){settleDate.focus();},dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){settleDate.focus();},dateFmt:'yyyy-MM-dd'})" 
					style="width:150px" placeholder="转结算日期">
			</td>
		</tr>
	</table>
	</form>
</div>