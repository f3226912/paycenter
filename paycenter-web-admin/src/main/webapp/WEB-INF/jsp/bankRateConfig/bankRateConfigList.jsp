<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="../pub/constants.inc" %>
		<%@ include file="../pub/head.inc" %>
		<%@ include file="../pub/tags.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=7, IE=9, IE=10, IE=11, IE=12"/>
		<title>渠道支付费率设置</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<body>
	<table id="bankRateConfigdg" title="">
	</table>
	<div id="bankRateConfigtb" style="padding:5px;height:auto">
		<form id="bankRateConfigSearchForm" method="post">
		<table>
				<tr>
					<td>支付渠道：</td>
					<td><select id="payChannel" name="payChannel"> 
					    <option value="">全部</option>
					    </select></td>
					<td>卡类型:</td>
					<td><select id="cardType" name="cardType"> 
					    <option value="">全部</option>
					    <option value="1">借记卡</option>
					    <option value="2">货记卡</option>
					    </select></td>
					<td>业务类型</td>
					<td><select id="areaBankFlag" name="areaBankFlag"> 
					    <option value="">全部</option>
					    <option value="1">本行</option>
					    <option value="2">跨行</option>
					    </select>
					    <select id="type" name="type"> 
					    <option value="">全部</option>
					    <option value="1">同城</option>
					    <option value="2">异地</option>
					    </select>
					    
					    </td>
					<td colspan="2">
				        <a class="easyui-linkbutton icon-search-btn" iconCls="icon-search" id="icon-search" OnClick ="return $('#bankRateConfigSearchForm').form('validate');">查询</a>
			      <!--      <a class="easyui-linkbutton" iconCls="icon-reload" id="icon-refresh" onclick="location.reload(true)">刷新</a> -->
					</td>
				</tr>	
			</table>
		<div style="background:#efefef;padding:5px 0 5px 0;height:25px;">
			<div style="float: left;font-size:14px;margin-left:5px;">渠道支付费率设置</div>
			<div style="float:right;margin-right:10px;">
		     <gd:btn btncode='BTNQDZFFL03'>		
		      <a class="easyui-linkbutton" iconCls="icon-add" id="btn-add">增加</a>
			 </gd:btn>
			</div>
		</div>	
     </form>
	</div>
	
	<div id="addBankRateConfigDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="addBankRateConfig">
	
	</div>
	<div id="editBankRateConfigDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="editBankRateConfig">
	
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/bankRateConfig/bankRateConfigList.js"></script>
<script type="text/javascript">
function optFormat(value,row,index){
	var opt = "";
	opt += "<gd:btn btncode='BTNQDZFFL01'><a class='operate' href='javascript:;' onclick=showbankRateConfigDetail('"+row.id+"');>查看</a></gd:btn>";
	opt += "<gd:btn btncode='BTNQDZFFL02'><a class='operate' href='javascript:;' onclick=updatebankRateConfigDetail('"+row.id+"');>修改</a></gd:btn>";
	return opt;
}
</script>
</html>
