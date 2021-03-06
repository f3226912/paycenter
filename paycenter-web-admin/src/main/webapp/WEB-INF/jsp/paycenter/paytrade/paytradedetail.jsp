<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>
<script>
$("#payAmt").keyup(function () {
                var reg = $(this).val().match(/\d+\.?\d{0,2}/);
                var txt = '';
                if (reg != null) {
                    txt = reg[0];
                }
                $(this).val(txt);
            }).change(function () {
                $(this).keypress();
                var v = $(this).val();
                if (/\.$/.test(v))
                {
                    $(this).val(v.substr(0, v.length - 1));
                }
            });
$("#feeAmt").keyup(function () {
		    var reg = $(this).val().match(/\d+\.?\d{0,2}/);
		    var txt = '';
		    if (reg != null) {
		        txt = reg[0];
		    }
		    $(this).val(txt);
		}).change(function () {
		    $(this).keypress();
		    var v = $(this).val();
		    if (/\.$/.test(v))
		    {
		        $(this).val(v.substr(0, v.length - 1));
		    }
		});
            
</script>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">汇款用途</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>订单号：</b></td>
			<td width="25%">${dto.orderNo}</td>
			<td align="right" width="25%"><b>平台支付流水：</b></td>
			<td width="25%">${dto.payCenterNumber}</td>
		</tr>
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">收款方信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>收款方账号：</b></td>
			<td width="25%">${dto.payeeAccount}</td>
			<td align="right" width="25%"><b>持卡人姓名：</b></td>
			<td width="25%">${dto.realName}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款方手机号：</b></td>
			<td>${dto.payeeMobile}</td>
			<td align="right"><b>银行卡号：</b></td>
			<td>${dto.bankCardNo}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款方姓名：</b></td>
			<td>${dto.realName}</td>
			<td align="right"><b>开户银行名称：</b></td>
			<td>${dto.depositBankName}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款方式：</b></td>
			<td>${dto.depositBankName}
			<c:if test="${fn:length(dto.bankCardNo) > 0}">  
		        (尾号<c:out value="${fn:substring(dto.bankCardNo, fn:length(dto.bankCardNo) - 4, fn:length(dto.bankCardNo))}" />)  
		    </c:if>
    		</td>
			<td align="right"><b>开户行所在地：</b></td>
			<td>
				<c:if test="${dto.provinceName != null}">${dto.provinceName}</c:if>
				<c:if test="${dto.cityName != null}">-${dto.cityName}</c:if>
				<c:if test="${dto.areaName != null}">-${dto.areaName}</c:if>
			</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款时间：</b></td>
			<td><fmt:formatDate value="${dto.transferTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td align="right"><b>支行名称：</b></td>
			<td>${dto.subBankName}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>实收金额：</b></td>
			<td><fmt:formatNumber type='number' value='${dto.transferAmt}' minFractionDigits='2' /></td>
			<td align="right"><b>银行卡预留手机：</b></td>
			<td>${dto.userBankMobile}</td>
		</tr>
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">谷登转账信息</div>
	<form id="form1" method="post" >
	<input type="hidden" name="id" value="${dto.id}"/>
	<input type="hidden" name="payerName" value="深圳谷登科技有限公司"/>
	<input type="hidden" name="orderNo" value="${dto.orderNo}"/>
	<input type="hidden" name="payCenterNumber" value="${dto.payCenterNumber}"/>
	<input type="hidden" name="payeeUserId" value="${dto.payeeUserId}"/>
	<input type="hidden" name="updateUserId" value="${dto.updateUserId}"/>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>开户人：</b></td>
			<td>深圳谷登科技有限公司</td>
		</tr>
		<tr>
			<td align="right" height="30px"><em style="color:red">*</em>&nbsp;<b>支付银行：</b></td>
			<td><select id="payerBankName" name="payerBankName" style="width:100px">
					<option value="农业银行" <c:if test="${dto.payerBankName == '农业银行'}">selected</c:if>>农业银行</option>
					<option value="工商银行" <c:if test="${dto.payerBankName == '工商银行'}">selected</c:if>>工商银行</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right" height="30px"><em style="color:red">*</em>&nbsp;<b>转账流水：</b></td>
			<td><input type="text" id="bankTradeNo" name="bankTradeNo" class="easyui-validatebox" 
				data-options="required:true" maxlength="30" value="${dto.bankTradeNo}"/></td>
		</tr>
		<tr>
			<td align="right" height="30px"><em style="color:red">*</em>&nbsp;<b>转账金额：</b></td>
			<td colspan="3"><input type="text" id="payAmt" name="payAmt" class="easyui-validatebox" 
				data-options="required:true,validType:'decimal'" maxlength="11" value="<fmt:formatNumber type='number' value='${dto.transferAmt}' minFractionDigits='2' />"/></td>
		</tr>
		<tr>
			<td align="right" height="30px"><em style="color:red">*</em>&nbsp;<b>转账手续费：</b></td>
			<td colspan="3"><input type="text" id="feeAmt" name="feeAmt" class="easyui-validatebox" 
			data-options="required:true,validType:'decimal'" maxlength="11" 
			value="<c:choose><c:when test='${dto.transferFeeAmt != null}'><fmt:formatNumber type='number' value='${dto.transferFeeAmt}' minFractionDigits='2' /></c:when><c:otherwise>0.00</c:otherwise></c:choose>"/></td>
		</tr>
		<tr>
			<td align="right" height="30px"><em style="color:red">*</em>&nbsp;<b>转账时间：</b></td>
			<td><input type="text" id="payTime" name="payTime" class="easyui-validatebox" data-options="required:true" 
				value="<fmt:formatDate value="${dto.transferTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onClick="WdatePicker({onpicked:function(){payTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})" placeholder=""></td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>操作人：</b></td>
			<td>${dto.operUserName}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>复核人：</b></td>
			<td>${dto.auditUserName}</td>
		</tr>
	</table>
	</form>
</div>