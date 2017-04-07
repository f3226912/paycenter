$(document).ready(function(){
	initValidateRules();
});
function toAlerDialog(){
	var payType = $("#update input[name='payInfoType']:checked").val();
	var resultType = $("#update input[id='resultType']").val();
	var tradeMoney = 0;
	if(payType == 1){
		tradeMoney = $("#update input[id='tradeMoney']").val();
	    if ($('#update #form1').form('validate') && $('#update #form3').form('validate')) {
	    	if(total != tradeMoney){
	    		jQuery.messager.alert('提示',"银行/第三方对账单【谷登代收金额】与谷登对账单【实付金额】不等，请重新检查提交数据.",'warning');
	    		return;
	    	}
	    	$('#alertMsgDialog').dialog({'title':'提示','href':CONTEXT+'paycenter/failedBill/toAlertMsg/'+resultType,'width': 380,'height': 200}).dialog('open');
	    }
    }else if(payType == 2){
    	tradeMoney = $("#update input[id='tradeMoneyThird']").val();
    	if ($('#update #form2').form('validate') && $('#update #form3').form('validate')) {
    		if(total != tradeMoney){
    			jQuery.messager.alert('提示',"银行/第三方对账单【谷登代收金额】与谷登对账单【实付金额】不等，请重新检查提交数据.",'warning');
    			return;
    		}
    		$('#alertMsgDialog').dialog({'title':'提示','href':CONTEXT+'paycenter/failedBill/toAlertMsg/'+resultType,'width': 380,'height': 200}).dialog('open');
      }
    }
	
}
//短信验证
function toValidateSms(){
	var thirdPayNumber = $("#update input[id='thirdPayNumber']").val();
	var resultType = $("#update input[id='resultType']").val();
	var remark = $("#update textarea[id='remark']").val();
	var payType = $("#update input[name='payInfoType']:checked").val();
	var parameter = thirdPayNumber + "_" + resultType;
	
	$('#alertMsgDialog').dialog('close');
	if(remark.length>225){
		jQuery.messager.alert('错误',"备注最大长度为225！",'error');
		return;
	}
	
    $('#smsValidateDialog').dialog({'title':'验证管理员手机号码','href':CONTEXT+'paycenter/failedBill/toSmsValidate/'+parameter,'width': 380,'height': 200}).dialog('open');
	
}

function initValidateRules(){
	$.extend($.fn.validatebox.defaults.rules, {
		fee:{
			validator: function (value) {
				var procedures=$("#update input[name='fee']:checked").val()
				if(procedures==2||procedures=='2'){
					return true;
				}
				return /^(\d{1,8}(\.\d{1,2})?|100000000.00)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100000000.00. 精确到小数点后两位.'
	   },
	   feethird:{
			validator: function (value) {
				var procedures=$("#update input[name='feethird']:checked").val();
				if(procedures==2||procedures=='2'){
					return true;
				}
				return /^(\d{1,8}(\.\d{1,2})?|100000000.00)$/.test(value);
			},
	        message: '请输入正确的范围，可填范围0.00-100000000.00. 精确到小数点后两位.'
	   },
	   tradeMoney:{
		validator: function (value) {
			var procedures=$("#update input[name='tradeMoney']:checked").val()
			if(procedures==2||procedures=='2'){
				return true;
			}
			return /^(\d{1,8}(\.\d{1,2})?|100000000.00)$/.test(value);
		},
        message: '请输入正确的范围，可填范围0.00-100000000.00. 精确到小数点后两位.'
     },
     tradeMoneythird:{
 		validator: function (value) {
 			var procedures=$("#update input[name='tradeMoneythird']:checked").val();
 			if(procedures==2||procedures=='2'){
 				return true;
 			}
 			return /^(\d{1,8}(\.\d{1,2})?|100000000.00)$/.test(value);
 		},
        message: '请输入正确的范围，可填范围0.00-100000000.00. 精确到小数点后两位.'
   },
     payAmt:{
 		validator: function (value) {
 			var procedures=$("#update input[name='payAmt']:checked").val()
 			if(procedures==2||procedures=='2'){
 				return true;
 			}
 			return /^(\d{1,8}(\.\d{1,2})?|100000000.00)$/.test(value);
 		},
         message: '请输入正确的范围，可填范围0.00-100000000.00. 精确到小数点后两位.'
      }
	   
		
	});
}

function changePayInfo(type){
	if(type == 1){
		$("div[id='bankInfoDiv']").show(); 
		$("div[id='thirdInfoDiv']").hide(); 
	}else if(type == 2){
		$("div[id='thirdInfoDiv']").show(); 
		$("div[id='bankInfoDiv']").hide(); 
	}
	
}

function formatNumber(v,r) {
	if(v==null) {
		return "-";
	}
	return v;
}

function formatDouble(v,r) {
	if(v!=null) {
		return v.toFixed(2)
	}
	return "-";
}


function formatDate(v,r) {
	if(v!=null) {
		return v;
	}
	return "-";
}
function formatPayStatus(v,r) {
	if(v=="0") {
		return "待支付";
	} else if(v=="1") {
		return "支付成功";
	} else {
		return "";
	}
}