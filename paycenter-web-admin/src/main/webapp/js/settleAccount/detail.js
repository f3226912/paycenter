$(document).ready(function(){
	var params={
			"memberId":$("#memberId").val(),
			"batNo":$("#batNo").val()
		}
	loadDetail(params,CONTEXT+'settleAccount/queryOrderDetailPage');
});

function loadDetail(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#settleAccountDetaildg').datagrid({
		url:loadUrl,
		queryParams: params,
		width:'auto', 
		height: 'auto', 
		nowrap:true,
		toolbar:'#settleAccountDetailtb',
		pageSize:20,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singSelect:true,
		fit:false,
		columns:[[
					{field:'orderNo',title:'订单号',width:100,align:'center'},
					{field:'feeType',title:'代付款类型',width:100,align:'center',formatter:feeTypeFormat},
					{field:'createOrderDate',title:'下单时间',width:100,align:'center'},
					{field:'paidAmt',title:'代付款金额',width:100,align:'center',formatter:formatDouble},
					{field:'payStatusStr',title:'代付状态',width:100,align:'center'}
				]]
	}); 
	grandTotal();
}
function grandTotal(){
	var payAmt = $('#realPay').val(); //实付代付款
	var feeAmt=$('#generationPayProcesseFee').val(); //代付手续费
	if(payAmt!=""&&feeAmt!=""){
	var generationPayNet = $('#generationPayNet').html((parseFloat(payAmt)+parseFloat(feeAmt)).toFixed(2)); //代付净额
	$('#realPay').val(parseFloat(payAmt).toFixed(2));
	$('#generationPayProcesseFee').val(parseFloat(feeAmt).toFixed(2));
	}
}

function update_submit(){
	var payerName = $('#generationPayUnit').val();//代付单位
	var payAmt = $('#realPay').val(); //实付代付款
	var feeAmt=$('#generationPayProcesseFee').val(); //代付手续费
	var payBankName = $('#payBank').val();//支付银行
	var thirdPartyWaterNo = $('#thirdPartyWaterNo').val(); //第三方支付流水
	var generationPayTime = $('#generationPayTime').val();//代付时间
	var platformPayWater = $('#platformPayWater').val();//平台支付流水
	var remark = $('#remark').val();//备注
	var id=$('#settleAccountId').val();
	var memberId = $('#memberId').val();//用户
	var batNo = $('#batNo').val(); //批次号
	if(!payBankName){
		slideMessage("请选择支付银行");
		return;
	}
	if(!generationPayTime){
		slideMessage("请选择代付时间");
		return;
	}
	if(!thirdPartyWaterNo){
		slideMessage("请输入第三方支付流水");
		return;
	}
	 var param = {id:id, comment:remark,payerName:payerName,payerBankName:payBankName,payAmt:payAmt,
			 feeAmt:feeAmt,bankTradeNo:thirdPartyWaterNo, payCenterNumber:platformPayWater,
			 payTime:generationPayTime,memberId:memberId,batNo:batNo};
	 if ($('#generationPayform').form('validate')) {
	 jQuery.post(CONTEXT + "settleAccount/updateSettleAccount", param, function(data) {
		 		if (data.success == true) {
		 			slideMessage("保存成功！");
		 			$("#settleAccountdg").datagrid('reload');
					$('#updateSettleAccountDialog').dialog('close');
				}else{
					slideMessage('保存失败！');
				}
	 	});
	 }
}

function formatDouble(v,r) {
	if(v!=null) {
		return v.toFixed(2)
	}
	return "-";
}

function feeTypeFormat(v,r) {
	if(v == '100001') {
		return "订单";
	} else if (v == '100002') {
		return "市场佣金";
	} else if (v == '100003') {
		return "手续费";
	} else if (v == '100004') {
		return "补贴";
	} else if (v == '100005') {
		return "平台佣金";
	} else if (v == '100006') {
		return "违约金";
	} else if (v == '100007') {
		return "退款";
	}
	return v;
}
