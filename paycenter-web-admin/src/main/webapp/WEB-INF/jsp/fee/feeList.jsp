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
		<title>代收手续费</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<body>
	<table id="feedg" title="">
	</table>
	<div id="feetb" style="padding:5px;height:auto">
		<form id="feeSearchForm" method="post">
			<table>
				<!-- <tr>
					<td>来源：</td>
					<td>
						<select type="text" id="appKey" name="appKey">
							<option value="">请选择</option>
						</select>
					</td>
					<td>手续费类型：</td>
					<td>
						<select type="text" id="feeType" name="feeType">
							<option value="">全部</option>
							<option value="0">交易手续费</option>
							<option value="1">转账手续费</option>
							<option value="2">其他</option>
						</select>
					</td> 
				</tr> -->
				<tr>
				<td>第三方支付流水:</td>
					<td>
						<input type="text" id="thridPayNumber" name="thridPayNumber">
					</td>
					<!-- <td>订单号:</td>
					<td><input type="text" id="orderNo" name="orderNo"></td> -->
					<td>
				<td>支付时间：</td>
					<td>
						<input  type="text"  id="financeBeginTime" name="financeBeginTime"  
						onFocus="WdatePicker({onpicked:function(){financeBeginTime.focus();},maxDate:'#F{$dp.$D(\'financeEndTime\')}',dateFmt:'yyyy-MM-dd'})"   
						onClick="WdatePicker({onpicked:function(){financeBeginTime.focus();},maxDate:'#F{$dp.$D(\'financeEndTime\')}',dateFmt:'yyyy-MM-dd'})"    
						style="width:150px" >
					</td>
					<td>~</td>
					<td>
						<input  type="text"    id="financeEndTime" name="financeEndTime"   
						onFocus="WdatePicker({onpicked:function(){financeEndTime.focus();},minDate:'#F{$dp.$D(\'financeBeginTime\')}',dateFmt:'yyyy-MM-dd'})"   
						onClick="WdatePicker({onpicked:function(){financeEndTime.focus();},minDate:'#F{$dp.$D(\'financeBeginTime\')}',dateFmt:'yyyy-MM-dd'})">
					</td>
					<td>
					<a class="easyui-linkbutton icon-search-btn" iconCls="icon-search" id="icon-search" OnClick ="return $('#feeSearchForm').form('validate');">查询</a>
					</td>
					<td>
					<a class="easyui-linkbutton icon-reload-btn" iconCls="icon-reload" id="btn-reset">重置</a>
					</td>
				</tr>
			</table>
			<td>
				<a class="easyui-linkbutton" iconCls="icon-reload" id="icon-refresh">刷新</a>
			</td>
			<td>
				<gd:btn btncode='FEEEXPORTS02'>	
				<a class="easyui-linkbutton" iconCls="icon-save" id="btn-import">导出EXCEL</a>
				</gd:btn>
			</td>
		</form>
	</div>
	<!-- <div id="feeDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#feeDetail">
		<div id="feeDetail" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:save()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#feeDialog').dialog('close')">关闭</a>
		</div>
	</div>
	<div id="updateFeeDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true" buttons="#editfeeDetail">
		<div id="editfeeDetail" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:update_submit()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#updateFeeDialog').dialog('close')">关闭</a>
		</div>
	</div>
	<div id="showFeeDetailDialog" class="easyui-dialog" style="width:600px;height:500px;" closed="true" modal="true" buttons="#showfeeDetail">
		<div id="showfeeDetail" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#showFeeDetailDialog').dialog('close')">关闭</a>
		</div>
	</div> -->
</body>
<script type="text/javascript" src="${CONTEXT}js/fee/main.js"></script>
</html>
