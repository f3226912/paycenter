<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/constants.inc" %>
<%@ include file="../../pub/tags.inc" %>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../../pub/head.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<title>对账日志</title>
		<style type="text/css">
			.payTypeSel {
				width: 104px;
				height: 24px;
				margin-right: 20px;
			}
			.dateInp{
				width: 100px;
				height: 17px;
				cursor: pointer;
			}
			.queryBtn {
				margin-left: 15px;
			}
			.dialogContent {
				padding: 5px;
   				margin: 5px;
			}
			.dialogItem {
				margin: 10px;
			}
		</style>
	</head>
<body>
	<table id="billCheckDG" title=""></table>
	<div id="billCheckTb" style="padding:0px;height:auto">
		<form id="searchform" method="post" style="padding:10px">
			支付渠道：  
			<select id="payType" class="payTypeSel">
				<option value="">全部</option>
				<c:forEach var="payType" items="${requestScope.payTypes }">
					<option value="${payType.payType }">${payType.payTypeName }</option>
				</c:forEach>
			</select>
			
			交易支付日期：
			<input  type="text" id="startDate" name="startDate"  
					onFocus="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})" 
					class="dateInp" placeholder="开始时间" readonly="readonly"> ——
			<input  type="text" id="endDate" name="endDate"   
					onFocus="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})" 
					class="dateInp" placeholder="结束时间" readonly="readonly">
			
			<gd:btn btncode="BTDZRZ01"><a id="queryBtn" class="easyui-linkbutton queryBtn" iconCls="icon-search" onclick="billCheck.query()">查询</a></gd:btn>
		</form>
		<div style="background:#efefef;padding:5px 0 5px 0;height:25px;line-height:25px;">
			<div style="float: left;font-size:14px;margin-left:10px;">对账日志</div>
			<div style="float:right;margin-right:10px;">
				<gd:btn btncode="BTDZRZ03"><a id="afreshCheckBillBtn" class="easyui-linkbutton" iconCls="icon-edit" onclick="billCheck.toolbarAction.afreshCheck()">重新对账</a></gd:btn>
				<gd:btn btncode="BTDZRZ02"><a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel" onclick="billCheck.toolbarAction.exportData()">导出EXCEL</a></gd:btn>
				<!-- 对账日志权限标识。type为hidden类型，仅仅是为了方便js判断是否有 对账日志 的权限。 -->
				<gd:btn btncode="BTDZRZ04"><input type="hidden" id="chekLogPrivilegeFlag" /></gd:btn>
			</div>
		</div>
	</div>
	
	<!-- 重新对账弹出框 -->
	<div id="afreshCheckDialog" class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttonsTransfer">
		<div class="dialogContent">
			<div class="dialogItem">
				对账支付渠道：  
				<select id="payTypeForDialog" class="payTypeSel">
					<c:forEach var="payType" items="${requestScope.payTypes }">
						<option value="${payType.payType }">${payType.payTypeName }</option>
					</c:forEach>
				</select>
			</div>
			<div class="dialogItem">
				交易支付日期：
				<input  type="text" id="payTimeForDialog" name="startDate"  
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"   
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})" 
						class="dateInp"  readonly="readonly"  >
			</div>
		</div>
		<div id="dlg-buttonsTransfer" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="billCheck.toolbarAction.confirmAfreshCheck()">确定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#afreshCheckDialog').dialog('close')">取消</a>
        </div>
	</div>
	
	<!-- 对账日志项列表弹出框 -->
	<div id="itemLogListDialog" class="easyui-dialog" closed="true" modal="true">
		
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/easyui-utils.js"></script>
<script type="text/javascript" src="${CONTEXT}js/paytrade/billCheckList.js"></script>
</html>