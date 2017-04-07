$(document).ready(function(){
	payChannel();
	initValidateRules();
});
function payChannel(){
	$("#add #payChannel").html("<option value=''>请选择渠道</option>");
	var areaList = payChannelAll();
	$.each(areaList, function(i, item){
		$("#add #payChannel").append("<option value='"+item.id+"'>"+item.payTypeName+"</option>"); 
	});
}

//0-500
function payChannelAll(){
	var result;
	$.ajax({
		async: false, 
		url: CONTEXT+'bankRateConfigController/getPayChannel',
		type:'post',
		dataType:"json",
		success : function(data){
			result = data.result;
		}
	});
	return result;
}

$("#add #btn-save").click(function(){
	var payChannel=$("#add #payChannel").val()
	,cardType=$("#add #cardType").val()
	,areaBankFlag=$("#add #areaBankFlag").val()
	,type=$("#add #type").val()
	,procedures=$("#add input[name='procedures']:checked").val()
	,status=$("#add input[name='status']:checked").val()
	,remark=$("#remark").val()
	,proportion=$("#add #proportion").val()
	,max=$("#add #max").val()
	,fixed=$("#add #fixed").val();
	if(payChannel==0||payChannel=='0'){
		alert("请选择渠道！");
		return;
	}
	if(cardType==0||cardType=='0'){
		alert("请选择卡类型！");
		return;
	}
	if(areaBankFlag==0||areaBankFlag=='0'){
		alert("请选择业务类型！");
		return;
	}
	if(remark.length>500){
		alert("备注最大长度为500！");
		return;
	}
	if(procedures==1||procedures=='1'){
		if(proportion.length==0){
			alert("请完善手续费信息");
			return ;
		}
	}else if(procedures==2||procedures=='2'){
		if(fixed.length==0){
			alert("请完善固定费用信息");
			return ;
		}
	}else{
		alert("请选择手续费类型!");
		return;
	}
	if(status== undefined){
		alert("请选择状态类型!");
		return;
	}
	if ($('#add #addForm').form('validate')) {
	var url = CONTEXT + "bankRateConfigController/save";
	jQuery.post(url, $('#add #addForm').serialize(), function(data) {
		if (data.msg == "success") {
			slideMessage("保存成功！");
			// 刷新父页面列表
			$("#bankRateConfigdg").datagrid('reload');
			$('#addBankRateConfigDialog').dialog('close');
		} else {
			warningMessage(data.msg);
			return;
		}
	});
	}
});


function initValidateRules(){
	$.extend($.fn.validatebox.defaults.rules, {
		proportion:{
			validator: function (value) {
				var procedures=$("#add input[name='procedures']:checked").val()
				if(procedures==2||procedures=='2'){
					return true;
				}
				/*if(parseFloat(value) == 0){
					return false;
				}*/
	            return  /^(\d{1,2}(\.\d{1,2})?|100)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100'
		},
	   max:{
			validator: function (value) {
				var procedures=$("#add input[name='procedures']:checked").val()
				if(procedures==2||procedures=='2'){
					return true;
				}
	            return /^(\d{1,8}(\.\d{1,2})?|100000000)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100000000.00'
	   },
	   fixed:{
			validator: function (value) {
				var procedures=$("#add input[name='procedures']:checked").val()
				if(procedures==1||procedures=='1'){
					return true;
				}
	            return /^(\d{1,8}(\.\d{1,2})?|100000000)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100000000.00'
	   }
		
	});
}

