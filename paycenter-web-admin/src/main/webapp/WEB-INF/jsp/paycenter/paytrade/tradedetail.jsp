<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/tags.inc" %>

<!-- <div class="easyui-window" title="My Window" style="width:100%;height:80%"
    data-options="iconCls:'icon-save',modal:true"  > -->
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">交易信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>订单号：</b></td>
			<td width="25%">${dto.orderNo}</td>
			<td align="right" width="25%"><b>付款方来源：</b></td>
			<td width="25%">${dto.payerOrderSourceStr}</td>
		</tr>
		
		<tr>
			<td align="right" height="30px"><b>下单时间：</b></td>
			<td ><fmt:formatDate value="${dto.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td align="right" height="30px"><b>订单类型：</b></td>
			<td  width="25%">${dto.orderTypeStr}</td>
		</tr>
		
		<tr>
			<td align="right" height="30px"><b>商品信息：</b></td>
			<td width="25%">${dto.title}</td>
			
			<td align="right" height="30px"><b>商品总金额：</b></td>
			<%-- <td width="25%"><fmt:formatNumber type='number' value='${dto.orderAmt}' minFractionDigits='2' /></td> --%>
			<td width="25%">${dto.orderAmt}</td>
		</tr>
	
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">付款方信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>付款方账号：</b></td>
			<td width="25%">${dto.payerAccount}</td>
			<td align="right" width="25%"><b>平台佣金：</b></td>
			<td width="25%">${dto.payerPlatCommissionAmt}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>付款方手机号：</b></td>
			<td width="25%">${dto.payerMobile}</td>
			<td align="right" width="25%"><b>市场佣金：</b></td>
			<td width="25%">${dto.payerCommissionAmt}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b></b></td>
			<td width="25%"></td>
			<td align="right"><b>应付金额：</b></td>
			<td width="25%">${dto.payAmt}</td>
		</tr>
		<tr>
			<table id="tradeDetailTable"></table>
			<script type="text/javascript">
			var orderNo = '${dto.orderNo}'
			console.log(orderNo)
			$(document).ready(function(){
				load({
					'orderNo':orderNo
				});
			});
			function load(params){
				$('#tradeDetailTable').datagrid({
					url:CONTEXT+'paycenter/paytrade/detailByOrderNo',
					queryParams: params,
					fitcolumns:true,
					fit:true,
					columns:[[
							{field:'payType',title:'支付方式',width:100,align:'center'},
							{field:'payAmt',title:'实付金额',width:100,align:'center',formatter:payAmtFormat},
							{field:'payTime',title:'支付时间',width:200,align:'center'},
							{field:'thirdPayNumber',title:'关联第三方支付流水',width:200,align:'center'},
							{field:'payCenterNumber',title:'关联平台支付流水',width:200,align:'center'},
							]]
				});
			}
			
			function payAmtFormat(value,row,index){
				if(!value){
					return '-';
				}else{
					return value.toFixed(2);
				}
			}
			
			</script>
		</tr>
		
		<%-- <tr>
			<td align="right" height="30px"><b>付款方式：</b></td>
			<td width="25%">${dto.payTypeStr}</td>
			<td align="right" width="25%"><b>支付时间：</b></td>
			<td width="25%"><fmt:formatDate value="${dto.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>平台支付流水：</b></td>
			<td colspan="3">${dto.payCenterNumber}</td>
		</tr>
		
		<tr>
			<td align="right" height="30px"><b>第三方支付流水：</b></td>
			<td colspan="3">${dto.thirdPayNumber}</td>
		</tr> --%>
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">收款方信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>收款方账号：</b></td>
			<td width="25%">${dto.payeeAccount}</td>
			<td align="right" width="25%"><b>平台佣金：</b></td>
			<td width="25%">${dto.payeePlatCommissionAmt}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款方手机号：</b></td>
			<td>${dto.payeeMobile}</td>
			<td align="right" width="25%"><b>市场佣金：</b></td>
			<td width="25%">${dto.payeeCommissionAmt}</td>
		</tr>
			<tr>
			<td align="right" height="30px"><b>商铺ID：</b></td>
			<td>${dto.businessId}</td>
			<td align="right" height="30px"><b>谷登代收支付手续费：</b></td>
			<td width="25%">${dto.gdFeeAmt}</td>
		</tr>
			<tr>
			<td align="right" height="30px"><b>商铺名称：</b></td>
			<td>${dto.businessName}</td>
			<td align="right" height="30px"><b>刷卡补贴：</b></td>
			<td width="25%">${dto.payeeSubsidyAmt}</td>
			
		</tr>
		<tr>
			<td align="right" height="30px"><b>所属市场：</b></td>
			<td>${dto.marketName}</td>
			<td align="right" height="30px"><b>应收金额：</b></td>
			<td width="25%">${dto.payeeAmt}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款方姓名：</b></td>
			<td>${dto.payeeRealName}</td>
			<td align="right" height="30px"><b>实收金额：</b></td>
			<td>${dto.payeeAmt}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>收款方式：</b></td>
			<td>${dto.payeeBankCardNoStr}</td>
		</tr>
	</table>
</div>

<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">市场方信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>市场账号：</b></td>
			<td width="25%">${dto.marketAccount}</td>
			<td align="right" width="25%"><b>佣金收益：</b></td>
			<td width="25%">${dto.marketCommissionAmt}</td>
		</tr>
		<tr>
			<td align="right" width="25%" height="30px"><b>市场手机号：</b></td>
			<td width="25%">${dto.marketMobile}</td>
			<td align="right" width="25%"><b>应收金额：</b></td>
			<td width="25%">${dto.marketCommissionAmt}</td>
		</tr>
		<tr>
			<td align="right" width="25%" height="30px"><b>市场名称：</b></td>
			<td width="25%">${dto.marketName}</td>
			<td align="right" width="25%"><b>实收金额：</b></td>
			<td width="25%">${dto.marketCommissionAmt}</td>
		</tr>
		<tr>
			<td align="right" width="25%" height="30px"><b>收款方姓名：</b></td>
			<td width="25%">${dto.marketRealName}</td>
			<td align="right" width="25%"><b>收款方式：</b></td>
			<td width="25%">${dto.marketBankCardNoStr}</td>
		</tr>
	</table>
</div>


<%-- <div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">谷登第三方账户收款信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>实收净额：</b></td>
			<td width="25%"><fmt:formatNumber type='number' value='${dto.receiptAmt}' minFractionDigits='2' /></td>
			<td align="right" width="25%"><b>手续费：</b></td>
			<td width="25%"><fmt:formatNumber type='number' value='${dto.feeAmt}' minFractionDigits='2' /></td>
		</tr>
	</table>
</div>



<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">谷登转账信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%" height="30px"><b>开户人：</b></td>
			<td width="25%">${dto.transferPayerName}</td>
			<td align="right" width="25%"><b>转账时间：</b></td>
			<td width="25%"><fmt:formatDate value="${dto.transferTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>支付银行：</b></td>
			<td>${dto.payerBankName}</td>
			<td align="right"><b>操作人：</b></td>
			<td>${dto.operUserName}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>转账流水：</b></td>
			<td>${dto.bankTradeNo}</td>
			<td align="right"><b>复核人：</b></td>
			<td>${dto.auditUserName}</td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>转账金额：</b></td>
			<td colspan="3"><fmt:formatNumber type='number' value='${dto.transferAmt}' minFractionDigits='2' /></td>
		</tr>
		<tr>
			<td align="right" height="30px"><b>转账手续费：</b></td>
			<td colspan="3"><fmt:formatNumber type='number' value='${dto.transferFeeAmt}' minFractionDigits='2' /></td>
		</tr>
	</table>
</div> --%>
<!-- </div> -->