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
		<title>代付佣金记录</title>
	</head>
<body>
	<table id="paidCommissiondg" title=""></table>
	<div id="paidCommissiontb" style="padding:5px;height:auto">
		<form id="searchform" method="post">
		<div>
			市场名称：
		    <select id="marketId" style="width:120px;margin-top:10px;">   
			    <option value="">全部</option> 
			    <c:if test="${!empty marketList}">
			          <c:forEach var="market" items="${marketList}">
			              <option value="${market.id}">${market.marketName}</option> 
			          </c:forEach>
			    </c:if> 
            </select>&nbsp;&nbsp;&nbsp;&nbsp;
            <!-- 佣金支出方:
            <select id="marketId" style="width:120px;margin-top:10px;">   
			    <option value="">全部</option> 
			    <option value="nsy">农商友</option>
				<option value="nps">农批商</option>
            </select>&nbsp;&nbsp;&nbsp;&nbsp; -->
			佣金支出方: <select class="easyui-combobox" id="userType" name="userType" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="nsy">农商友</option>
				<option value="nps">农批商</option>
				<!-- <option value="gys">供应商</option>
				<option value="nst_company">物流公司</option>
				<option value="nst_car">车主</option>
				<option value="nst_goods">货主</option> -->
				</select>&nbsp;&nbsp;&nbsp;&nbsp;
			代付状态:  <select class="easyui-combobox" id="payStatus" name="payStatus" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="0">待付款</option>
				<option value="1">付款成功</option>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;
			转结状态:
			 <select class="easyui-combobox" id="hasChange"  name="hasChange" editable="false" panelHeight="auto" style="width:100px">
				<option value="">请选择</option>
				<option value="1">已转结</option>
				<option value="0">未转结</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			关联订单号:		
			<input  type="text" id="orderNo" name="orderNo" maxlength="100"
								style="width:150px" placeholder="请输入订单号">&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="margin-top:10px;">
        	收款方手机:  
			<input  type="text" id="mobile" name="mobile" maxlength="100"
								style="width:150px" placeholder="请输入收款方手机">&nbsp;&nbsp;&nbsp;&nbsp;
			下单时间: 
			<input  type="text" id="startOrderTime" name="startOrderTime"  
					onFocus="WdatePicker({onpicked:function(){startOrderTime.focus();},maxDate:'#F{$dp.$D(\'endOrderTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startOrderTime.focus();},maxDate:'#F{$dp.$D(\'endOrderTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endOrderTime" name="endOrderTime"   
					onFocus="WdatePicker({onpicked:function(){endOrderTime.focus();},minDate:'#F{$dp.$D(\'startOrderTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endOrderTime.focus();},minDate:'#F{$dp.$D(\'startOrderTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
			代付时间: 
			<input  type="text" id="startPayTime" name="startPayTime"  
					onFocus="WdatePicker({onpicked:function(){startPayTime.focus();},maxDate:'#F{$dp.$D(\'endPayTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startPayTime.focus();},maxDate:'#F{$dp.$D(\'endPayTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endPayTime" name="endPayTime"   
					onFocus="WdatePicker({onpicked:function(){endPayTime.focus();},minDate:'#F{$dp.$D(\'startPayTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endPayTime.focus();},minDate:'#F{$dp.$D(\'startPayTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
        <div style="margin-top: 5px;">
        	<gd:btn btncode="BTNDFKYJ02"><a class="easyui-linkbutton" iconCls="icon-search" onclick="commissionOrder.query()">查询</a></gd:btn>&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="commissionOrder.cleardata()">重置</a>
			<gd:btn btncode="BTNDFKYJ01"><a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel" onclick="commissionOrder.exportData()">导出EXCEL</a></gd:btn>
        </div>
		</form>
	</div>
	<div id="editDialog" class="easyui-dialog" style="width:1000px;height:500px;" closed="true" modal="true" buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/paidRecord/commissionMain.js"></script>
</html>