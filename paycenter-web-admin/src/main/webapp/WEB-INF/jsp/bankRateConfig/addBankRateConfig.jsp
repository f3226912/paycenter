<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div  id="add"   style="margin: 10px auto;width:80%">
	<form action="" name="" id="addForm">
		<table>
		   <tr>
				<td width="80px"><font color="red">*</font>支付渠道：</td>
				<td>
				<select id="payChannel" name="payChannel" style="width: 305px;" >
				<option  value="">请选择</option>
				</select>
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>卡类型：</td>
				<td>
				<select name="cardType" id="cardType" style="width: 305px;" >
				<option value="0" selected="selected">请选择</option>
				<option value="1">借记卡</option>
				<option value="2">代记卡</option>
				</select>
			
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>业务类型：</td>
				<td>
		        <select name="areaBankFlag" id="areaBankFlag" style="width: 155px;" >
				<option value="0" selected="selected">请选择</option>
				<option value="1">本行</option>
				<option value="2">跨行</option>
				</select>
				 <select name="type" id="type"  style="width: 148px;" >
				<option value="0" selected="selected">请选择</option>
				<option value="1">同城</option>
				<option value="2">异地</option>
				</select>
			   </td>
			</tr>
			<tr>
				<td><font color="red">*</font>手续费：</td>
				<td>
				  <input name="procedures" id="u21_input" type="radio" value="1"/> 按比例(%)<input value="" validtype="proportion" class="easyui-validatebox"    id="proportion" name="proportion"  style="width:50px"/>封顶：<input value=""  validtype="max" id="max"  name="max"class="easyui-validatebox" style="width:132px"/><br>
				  <input name="procedures" id="u22_input" type="radio" value="2"/> 固定值&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input value=""validtype="fixed" class="easyui-validatebox"  id="fixed"name="fixed"  style="width:87px"/>
				</td>
			</tr>
			<tr>
				<td><font color="red">*</font>状态：</td>
				<td>
				  <input name="status" id="status1" type="radio" value="1"/>开启
				    <input name="status" id="status2" type="radio" value="2"/>关闭
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
					<textarea rows="10" cols="28" style="width: 298px;" name="remark" maxlength="200" id="remark"></textarea>
				</td>
			</tr>
		</table>
		<center>
		 <gd:btn btncode='BTNQDZFFL03'>	
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn-save" >保存</a>
	    </gd:btn>
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addBankRateConfigDialog').dialog('close')">关闭</a>
	    </center>
	</form>
</div>
<script type="text/javascript" src="${CONTEXT}../js/bankRateConfig/addBankRateConfig.js"></script>
 
