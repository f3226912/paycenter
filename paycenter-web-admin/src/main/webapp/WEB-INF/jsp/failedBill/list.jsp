<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../pub/constants.inc" %>
<%@ include file="../pub/tags.inc" %>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../pub/head.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<title>代收款记录</title>
		<style type="text/css">
		 #smsValidateDialog{ height:201px!important;};
		</style>
	</head>
<body>
	<table id="failedBilldg" title="">
		<thead>
			<tr>
				<th data-options="field:'thirdPayNumber',width:210,align:'left',sortable:true">第三方支付流水</th>
				<th data-options="field:'payCenterNumber',width:160,align:'left',sortable:true">平台支付流水</th>
				<th data-options="field:'orderNo',width:160,align:'left',sortable:true">关联订单</th>
				<th data-options="field:'payerMobile',width:100,align:'left',sortable:true">付款方手机</th>
				<th data-options="field:'orderAmt',width:110,align:'left',sortable:true,formatter:decimalFormatter">实付金额</th>
				<th data-options="field:'payAmt',width:110,align:'left',sortable:true,formatter:decimalFormatter">谷登代收金额</th>
				<th data-options="field:'payTime',width:150,align:'left',sortable:true">支付时间</th>
				<th data-options="field:'payType',width:100,align:'left',sortable:true,formatter:payTypeFormatter">支付方式</th>
				<th data-options="field:'posClientNo',width:80,align:'left',sortable:true">终端号</th>
				<th data-options="field:'payStatus',width:100,align:'center',sortable:true,formatter:payStatusFormatter">支付状态</th>
				<!-- <th data-options="field:'checkStatus',width:100,align:'center',sortable:true,formatter:checkStatusFormatter">对账状态</th> -->
				<th data-options="field:'failReason',width:220,align:'left',sortable:true" >对账失败原因</th>
				<gd:btn btncode="BTNDZSBMX01"><th data-options="field:'doOperate',width:80,align:'center',formatter:doOperate">操作</th></gd:btn>
		</thead>
	</table>
	<div id="tb" style="padding:5px;height:auto">
		<form id="searchform" method="post">
		<div>
                                    支付方式：
            <select type="text" id="payType" name="payType"> 
			    <option value="">全部</option>  
                <c:forEach items="${payTypes }" var="type" varStatus="status">
                    <option value="${type.payType} " ${payType==type.payType ? 'selected' : ''}>${type.payTypeName }</option>
                </c:forEach>
            </select>&nbsp;&nbsp;&nbsp;&nbsp;    
			  支付时间: 
			<input  type="text" id="startDate" name="startDate" value="${startDate}"
					onFocus="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endDate" name="endDate" value="${endDate}" 
					onFocus="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;			                                                                                   
            <gd:btn btncode="BTNDZSBMX02"><a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a></gd:btn>&nbsp;&nbsp;
        </div>
            
		</form>
	</div>
	<div id="alertMsgDialog" class="easyui-dialog" style="width:600px;height:auto!important;" closed="true" modal="true" buttons="#editMsgValidateDetail">
		<div id="editMsgValidateDetail" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:toValidateSms()">确认</a>&nbsp;&nbsp;&nbsp;&nbsp;
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#alertMsgDialog').dialog('close')">取消</a>
		</div>
	</div>

	
	<div id="smsValidateDialog" class="easyui-dialog" style="width:600px;height:auto!important;" closed="true" modal="true" buttons="#editSmsValidateDetail">
		<div id="editSmsValidateDetail" style="text-align:center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save confirm_cn" onclick="javascript:confirm()">确认</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</div>
	<div id="updateFailedBillDialog" class="easyui-dialog" style="width:880px;height:420px;" closed="true" modal="true" buttons="#editFailedBillDetail">
		<div id="editFailedBillDetail" style="text-align:center">
			<!-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:toValidateSms()">提交保存</a>&nbsp;&nbsp;&nbsp;&nbsp; -->
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:toAlerDialog()">提交保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#updateFailedBillDialog').dialog('close')">关闭</a>
		</div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/easyui-utils.js"></script>
<script type="text/javascript" src="${CONTEXT}js/failedBill/list.js"></script>

</html>