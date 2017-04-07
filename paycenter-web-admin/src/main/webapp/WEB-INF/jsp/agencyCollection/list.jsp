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
	</head>
<body>
	<table id="dg" title="">
		<thead>
			<tr>
				<th data-options="field:'id',width:10,align:'left',sortable:true,hidden:true"></th>			    
				<th data-options="field:'thirdPayNumber',width:120,align:'left',sortable:true">第三方支付流水</th>
				<th data-options="field:'payCenterNumber',width:120,align:'left',sortable:true">平台支付流水</th>
				<th data-options="field:'orderNo',width:120,align:'left',sortable:true">关联订单</th>
				<th data-options="field:'payAmt',width:100,align:'left',sortable:true,formatter:decimalFormatter">实付金额</th>
				<th data-options="field:'payAmt',width:100,align:'left',sortable:true,formatter:decimalFormatter">谷登代收金额</th>
				<th data-options="field:'payerMobile',width:100,align:'left',sortable:true">付款方手机</th>
				<th data-options="field:'payType',width:120,align:'left',sortable:true,formatter:payTypeFormatter">支付方式</th>
				<th data-options="field:'payTime',width:150,align:'left',sortable:true">支付时间</th>
				<th data-options="field:'posClientNo',width:80,align:'left',sortable:true">终端号</th>
				<th data-options="field:'payStatus',width:100,align:'center',sortable:true,formatter:payStatusFormatter">支付状态</th>
				<th data-options="field:'checkStatus',width:100,align:'center',sortable:true,formatter:checkStatusFormatter">对账状态</th>
				<th data-options="field:'marketName',width:100,align:'center',sortable:true">所属市场</th>
				<th data-options="field:'appKey',width:100,align:'center',sortable:true,formatter:appKeyFormatter">付款方</th>
				<gd:btn btncode="BTNDSKJLGL01"><th data-options="field:'doOperate',width:120,align:'center',formatter:doOperate">操作</th></gd:btn>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:5px;height:auto">
	
		 <div style="margin-top:10px;">
		 	付款方: 
			 	<select id="orderType" style="width:120px;margin-top:10px;">   
				    <option value="">全部</option>
				    <option value="1">农商友</option>
				    <option value="2">农批商</option>
				    <option value="4">供应商</option>
				    <option value="21">车主</option>
				    <option value="22">货主</option>
	            </select>
			付款方手机: <input type="text" id="payerMobile" name="payerMobile" style="width:150px" placeholder="请输入付款方手机号">&nbsp;&nbsp;&nbsp;&nbsp;
			第三方支付流水: <input type="text" id="thirdPayWater" name="thirdPayWater" style="width:150px" placeholder="请输入第三方支付流水">&nbsp;&nbsp;&nbsp;&nbsp;
			平台支付流水: <input type="text" id="platformPayWater" name="platformPayWater" style="width:150px" placeholder="请输入平台支付流水">&nbsp;&nbsp;&nbsp;&nbsp;
			订单号: <input type="text" id="orderNo" name="orderNo" style="width:150px" placeholder="请输入订单号">&nbsp;&nbsp;&nbsp;&nbsp;                     
		</div>
		<div>
		            所属市场：
		    <select id="marketId" style="width:120px;margin-top:10px;">   
			    <option value="">全部</option> 
			    <c:if test="${!empty marketList}">
			          <c:forEach var="market" items="${marketList}">
			              <option value="${market.id}">${market.marketName}</option> 
			          </c:forEach>
			    </c:if> 
            </select>&nbsp;&nbsp;&nbsp;&nbsp;
            支付方式：
            <select id="payType"  style="width:120px;">   
			    <option value="">全部</option>
			    <c:if test="${!empty payTypeList}">
			          <c:forEach var="pt" items="${payTypeList}">
			          		<c:choose>
			          			<c:when test="${pt.payType == payType}">
			          				<option value="${pt.payType}" selected="selected">${pt.payTypeName}</option>
			          			</c:when>
			          			<c:otherwise>
			          				<option value="${pt.payType}">${pt.payTypeName}</option>
			          			</c:otherwise>
			          		</c:choose>
			          </c:forEach>
			    </c:if>
			  <%--  <option value="ALIPAY_H5" ${payType=='ALIPAY_H5'? 'selected' : ''}>支付宝支付</option>
                <option value="WEIXIN_APP" ${payType=='WEIXIN_APP'? 'selected' : ''}>微信支付 </option>
                <option value="ENONG" ${payType=='ENONG'? 'selected' : ''}>武汉E农</option>
                <option value="NNCCB" ${payType=='NNCCB'? 'selected' : ''}>南宁建行</option>
                <option value="GXRCB" ${payType=='GXRCB'? 'selected' : ''}>广西农信社</option>
                <option value="WANGPOS" ${payType=='WANGPOS'? 'selected' : ''}>旺POS</option>  --%>
            </select>&nbsp;&nbsp;&nbsp;&nbsp; 
			<!-- 支付状态: 
			 	<select id="payStatus" style="width:120px;margin-top:10px;">   
				    <option value="">全部</option>
				    <option value="1">待付款</option>
				    <option value="2">已付款</option>
				    <option value="3">已关闭</option>
				    <option value="4">已退款</option>
	            </select> -->
	         对账状态: 
			 	<select id="checkStatus" style="width:120px;margin-top:10px;">   
				    <!-- <option value="">全部</option> -->
				    <option value="1"<c:if test="${checkStatus==1}"> selected='true'</c:if>>对账成功</option>
				    <!-- <option value="2">对账失败</option> -->
	            </select>
			支付时间:
			<input  type="text" id="startDate" name="startDate" value="${startDate}" 
					onFocus="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endDate" name="endDate"  value="${endDate}"  
					onFocus="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
        </div>

		<div style="margin-bottom:10px">
			<a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="cleardata()">重置</a>
			<gd:btn btncode="BTNDSKJLGL02"><a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel">导出EXCEL</a></gd:btn>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)'">刷新</a>
		</div>
	</div>
</body>
<script type="text/javascript" src="${CONTEXT}js/agencyCollection/list.js"></script>
</html>