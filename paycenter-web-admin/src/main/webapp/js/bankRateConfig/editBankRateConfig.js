$(document).ready(function(){
	payChannel();
	initValidateRules();
});
function payChannel(){
	var id=$("#update #payChannelId").val();
	var areaList = payChannelAll();
	$.each(areaList, function(i, item){
		if(id==item.id){
			$("#update #payChannel").append("<option value='"+item.id+"' selected='selected' >"+item.payChannelName+"</option>"); 
		}else{
		$("#update #payChannel").append("<option value='"+item.id+"'>"+item.payTypeName+"</option>"); 
		}
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


$("#update #btn-updateSave").click(function(){
	var payChannel=$("#update #payChannel").val()
	,cardType=$("#update #cardType").val()
	,areaBankFlag=$("#update #areaBankFlag").val()
	,type=$("#update #type").val()
	,procedures=$("#update input[name='procedures']:checked").val()
	,status=$("#update input[name='status']:checked").val()
	,remark=$("#update #remark").val()
	,proportion=$("#update #proportion").val()
	,max=$("#update #max").val()
	,fixed=$("#update #fixed").val();
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
	if ($('#update #updateForm').form('validate')) {
	var url = CONTEXT + "bankRateConfigController/update";
	jQuery.post(url, $('#update #updateForm').serialize(), function(data) {
		if (data.msg == "success") {
			slideMessage("保存成功！");
			// 刷新父页面列表
			$("#bankRateConfigdg").datagrid('reload');
			$('#editBankRateConfigDialog').dialog('close');
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
				var procedures=$("#update input[name='procedures']:checked").val()
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
				var procedures=$("#update input[name='procedures']:checked").val()
				if(procedures==2||procedures=='2'){
					return true;
				}
	            return /^(\d{1,8}(\.\d{1,2})?|100000000)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100000000.00'
	   },
	   fixed:{
			validator: function (value) {
				var procedures=$("#update input[name='procedures']:checked").val()
				if(procedures==1||procedures=='1'){
					return true;
				}
	            return /^(\d{1,8}(\.\d{1,2})?|100000000)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100000000.00'
	   }
		
	});
}

