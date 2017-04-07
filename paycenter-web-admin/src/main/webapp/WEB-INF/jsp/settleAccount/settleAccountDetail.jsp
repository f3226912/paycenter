<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input type="hidden" value="${entity.memberId}" id="memberId"/>
<input type="hidden" value="${entity.batNo}" id="batNo"/>
<table id="settleAccountDetaildg" align="center"></table>
	<div id="settleAccountDetailtb" style="margin: 0px 5px;height:auto;">
		<div style="padding: 15px 0px 5px 8px; border-bottom: 1px solid #ccc;">代付款记录</div>
	</div>
	<div style="margin: 0px 5px">
		<div style="padding: 15px 0px 5px 8px; border-bottom: 1px solid #ccc;">收款方信息</div>
		<table class="beneficiaryDetail" align="center" width="100%">
			<tr>
				<td align="right" width="25%" height="15px"><b>收款方账号：</b></td>
				<td width="25%">${entity.account}</td>
				<td align="right" width="25%"><b>持卡人姓名：</b></td>
				<td width="25%">${entity.bankCardRealName}</td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>收款方手机号：</b></td>
				<td>${entity.mobile}</td>
				<td align="right"><b>银行卡号：</b></td>
				<td>${entity.bankCardNo}</td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>收款方姓名：</b></td>
				<td>${entity.realName}</td>
				<td align="right"><b>开户银行名称：</b></td>
				<td>${entity.depositBankName}</td>
			</tr>
			<tr>
<!-- 				<td align="right" height="15px"><b>商铺ID：</b></td> -->
<%-- 				<td>${entity.businessId}</td> --%>
				<td align="right" height="15px"><b>收款时间：</b></td>
				<td>
				    <c:if test="${empty entity.generationPayTime}">-</c:if>
				    <c:if test="${not empty entity.generationPayTime}">${entity.generationPayTime}</c:if>
				</td>
				<td align="right"><b>开户行所在地：</b></td>
				<td>${entity.address}</td>
			</tr>
			<tr>
<!-- 				<td align="right" height="15px"><b>商铺名称：</b></td> -->
<%-- 				<td>${entity.businessName}</td> --%>
				<td align="right" height="15px"><b>代付款记录合计：</b></td>
				<td><fmt:formatNumber type="number" value="${entity.dueAmt}" pattern="#,##0.00" maxFractionDigits="2"/></td>
				<td align="right" height="15px"><b>支行名称：</b></td>
				<td>${entity.subBankName}</td>
			</tr>
			<tr>
<!-- 				<td align="right" height="15px"><b>所属市场：</b></td> -->
<%-- 				<td>${entity.marketName}</td> --%>
				<td align="right" height="15px"></td>
				<td></td>
				<td align="right" height="15px"><b>银行卡预留手机号：</b></td>
				<td>${entity.bankMobile}</td>
			</tr>
		</table>
	</div>
	<div style="margin: 0px 5px">
		<div style="padding: 15px 0px 5px 8px; border-bottom: 1px solid #ccc;">谷登代付信息</div>
		<form id="generationPayform" method="post">
			 <input type="hidden" name="payerName" value="深圳谷登科技有限公司" />
			  <input type="hidden" id="settleAccountId" value="${entity.id}" />
			<table class="generationPayDetail" align="center" width="100%">
				<tr>
					<td align="right" width="25%" height="15px"><b>代付单位：</b></td>
					<td><input type="text" id="generationPayUnit" name="generationPayUnit"
						class="easyui-validatebox"  size="20"
						maxlength="30" value="深圳谷登科技有限公司" /></td>
					<td align="right" height="15px"><b>代付金额：</b><input type="hidden" id="realPay" value="${entity.dueAmt}"/></td><td><fmt:formatNumber type="number"  value="${entity.dueAmt}" pattern="#,##0.00" maxFractionDigits="2"/></td>
					<%-- <td><fmt:formatNumber type="number"  value="${entity.dueAmt}" pattern="#,##0.00" maxFractionDigits="2"/>
						<em style="color: red">*</em>&nbsp;<b>代付手续费：</b><input  
						type="text" id="generationPayProcesseFee" name="generationPayProcesseFee"
						class="easyui-validatebox" data-options="required:true"
						maxlength="30" value="${entity.feeAmt}" size="9" onblur="grandTotal()" />
						&nbsp;<b>代付净额：</b><span id="generationPayNet"></span></td>  --%>
				</tr>
				<tr>
					<td align="right" height="15px"><em style="color: red">*</em>&nbsp;<b>代付手续费：</b></td>
					<td><input  
						type="text" id="generationPayProcesseFee" name="generationPayProcesseFee"
						class="easyui-validatebox" data-options="required:true"
						maxlength="30" value="${entity.feeAmt}" size="9" onblur="grandTotal()" /></td>
					<td align="right" height="15px"><b>代付净额：</b></td>
					<td><span id="generationPayNet"></td>
				</tr>
				<tr>
					<td align="right" height="15px"><b>支付银行：</b></td>
					<%-- <td><select id="payBank" name="payBank"
						style="width: 150px">
							<option value="农业银行" <c:if test="${entity.payerBankName == '农业银行'}">selected</c:if>>农业银行 (尾号<c:if test="${entity.payerBankName == '农业银行'}">${entity.payerBankCardNo}</c:if>)</option>
					<option value="工商银行" <c:if test="${entity.payerBankName == '工商银行'}">selected</c:if>>工商银行 (尾号<c:if test="${entity.payerBankName == '工商银行'}">${entity.payerBankCardNo}</c:if>)</option>
					</select></td> --%>
					<td><select id="payBank" name="payBank"
						style="width: 150px">
							<option value="农业银行" selected>农业银行 (尾号3958)</option>
					        <option value="工商银行" >工商银行 (尾号1516)</option>
					</select></td>
					<td align="right" height="15px"><em style="color: red">*</em>&nbsp;<b>第三方支付流水：</b></td>
					<td><input type="text" id="thirdPartyWaterNo" name="thirdPartyWaterNo" size="28"
						class="easyui-validatebox" data-options="required:true"
						maxlength="30" value="${entity.bankTradeNo}" /></td>
				</tr>
				<tr>
					<td align="right" height="15px"><em style="color: red">*</em>&nbsp;<b>代付时间：</b></td>
					<td>
					<input  type="text" id="generationPayTime" name="generationPayTime"   
					onFocus="WdatePicker({onpicked:function(){generationPayTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"   
					onClick="WdatePicker({onpicked:function(){generationPayTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
					style="width:145px" class="easyui-validatebox" data-options="required:true"
						maxlength="30" size="60" placeholder="请输入代付时间" value="${entity.generationPayTime}">
</td>
					<%-- <td align="right" height="15px"><b>平台支付流水：</b></td>
					<td><input type="text" id="platformPayWater" name="platformPayWater"  size="28"
						class="easyui-validatebox"  value="${entity.payCenterNumber}" /></td> --%>
					<td align="right" height="15px"><b>代付状态：</b></td>
					<td>${entity.payStatusStr}</td>
				</tr>
				<tr>
					<td align="right" height="15px">&nbsp;<b>备注：</b></td>
					<td><textarea id="remark" cols=21 rows=4>${entity.comment}</textarea></td>
					
					<td align="right" height="15px"><b>操作人：</b></td>
					<td>${entity.updateUserId}</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="margin: 0px 5px 20px">
		<div style="padding: 15px 0px 5px 8px; border-bottom: 1px solid #ccc;">异常标记记录</div>
		<table class="errorTable" align="center" width="100%">
			<thead>
				<tr align=center>
					<th align="center" width="20%" height="15px">标记时间</th>
					<th align="center" width="15%" height="15px">标记人</th>
					<th align="center" width="60%" height="15px">标记说明</th>
				</tr>
				<c:forEach items="${entity.listError}" var="error">
					<tr align=center>
						<td align="center" width="20%" height="15px"><fmt:formatDate
								value="${error.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td align="center" width="15%" height="15px"><c:out
								value="${error.createUserId}" /></td>
						<td align="center" width="50%" height="15px"><c:out
								value="${error.comment}" /></td>
					</tr>
				</c:forEach>
			</thead>
		</table>
	</div>
		<c:if test="${option == 'edit'}">
			<div id="editSettleAccountDetail" style="text-align:center">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:update_submit()">提交保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#updateSettleAccountDialog').dialog('close')">关闭</a>
			</div>
		</c:if>
<script type="text/javascript" src="${CONTEXT}../js/settleAccount/detail.js"></script>