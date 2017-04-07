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
		<title>交易记录</title>
	</head>
	<style type="text/css">
		.panel-body {
		    overflow: hidden;
		}
		.datagrid .panel-body{
			overflow: auto;
		}
	/* 	
		.datagrid-cell-c2-orderNo {
    		width: 130px !important;
		}
		.datagrid-cell-c2-orderTypeStr {
		    width: 90px !important;
		}
		.datagrid-cell-c2-payerMobile {
		    width: 90px !important;
		}
		.datagrid-cell-c2-payerCommissionAmt {
		    width: 89px !important;
		}
		.datagrid-cell-c2-gdAmt {
		    width: 80px !important;
		}
		.datagrid-cell-c2-gdFeeAmt {
		    width: 112px !important;
		}
		.datagrid-cell-c2-payeeMobile {
		    width: 80px !important;
		}
		.datagrid-cell-c2-payeeCommissionAmt {
		    width: 80px !important;
		}
		.datagrid-cell-c2-payeeSubsidyAmt {
		    width: 50px !important;
		}
		.datagrid-cell-c2-payeeAmt {
		    width: 80px !important;
		}
		.datagrid-cell-c2-marketCommissionAmt {
		    width: 84px !important;
		}
		.datagrid-cell-c2-payerOrderSourceStr {
		    width: 60px !important;
		}
		.datagrid-cell-c2-orderTime {
		    width: 119px !important;
		}
		
		
		 */
		
		
	</style>

	
<body>


<!-- 	<table id="dg" class="easyui-datagrid"  title="交易记录" > -->
<!-- 		 <thead> -->
<!-- 		</thead> -->
<!-- 	</table> -->
	<table id="dg" title=""></table>

	<div id="tradeListTB" style="padding:5px;height:auto">
		<form id="searchform" method="post">
		<div>
			付款方来源:
			<select id="payerOrderSource" class="easyui-combobox" editable="false" panelHeight="auto" style="width:100px" >
				<option value="">请选择</option>
				<c:forEach var="payerOrderSource" items="${payerOrderSourceList }">
					<option value="${payerOrderSource.code}">${payerOrderSource.name }</option>
				</c:forEach>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
				
			所属市场:	
			<select id="marketId" class="easyui-combobox" editable="false" panelHeight="auto" style="width:100px" >
				<option value="">请选择</option>
				<c:forEach var="marketList" items="${marketList }">
					<option value="${marketList.id}">${marketList.marketName }</option>
				</c:forEach>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			订单类型:
			<select id="orderType" class="easyui-combobox" editable="false" panelHeight="auto" style="width:100px" >
				<option value="">请选择</option>
				<c:forEach var="orderTypeList" items="${orderTypeList }">
					<option value="${orderTypeList.code}">${orderTypeList.name }</option>
				</c:forEach>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;

        	订单号:<input class="easyui-searchbox" type="text" id="orderNo" name="orderNo" style="width:150px" data-options="prompt:'请输入订单号'">&nbsp;&nbsp;&nbsp;&nbsp;
           
        </div>
        <div style="margin-top:10px;">
	
            
			付款方手机: <input class="easyui-searchbox" type="text" id="payerMobile" name="payerMobile" style="width:150px" data-options="prompt:'请输入付款方手机' ">&nbsp;&nbsp;&nbsp;&nbsp;
			收款方手机: <input class="easyui-searchbox" type="text" id="payeeMobile" name="payeeMobile" style="width:150px" data-options="prompt:'请输入收款方手机'">&nbsp;&nbsp;&nbsp;&nbsp;
			<!-- 	收款方手机: <input class="easyui-searchbox" type="text" id="payeeMobile" name="payeeMobile" style="width:150px" data-options="prompt:'请输入收款方手机',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp; -->
			下单时间: 
			<input  type="text" id="startDate" name="startDate"  
					onFocus="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endDate" name="endDate"   
					onFocus="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
			<gd:btn btncode="BTDTRQU01"><a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>&nbsp;&nbsp;</gd:btn>
			<gd:btn btncode="BTDTRQRE01"><a class="easyui-linkbutton" iconCls="icon-reload" onclick="cleardata()">重置</a></gd:btn>
			
			<div style="margin-bottom:5px">
				<gd:btn btncode="BTDTRDEP01"><a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel"  onclick='tradeRecord.toolbarAction.exportData()'>导出EXCEL</a></gd:btn>
				<gd:btn btncode="BTDTRDRE01"><a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)' >刷新</a></gd:btn>
			</div>
		</div>
		</form>
	</div>
	<div id="editDialog" class="easyui-dialog" style="width:900px;height:500px;overflow: auto;"  class="easyui-dialog" closed="true" modal="true"  buttons="#dlg-buttonsEdit">
		<div id="dlg-buttonsEdit" style="text-align:center">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">关闭</a>
        </div>
	</div>

</body>
 <script type="text/javascript" src="${CONTEXT}js/paytrade/tradeRecordList.js"></script> 
 <script type="text/javascript" src="${CONTEXT}js/easyui-utils.js"></script>
</html>