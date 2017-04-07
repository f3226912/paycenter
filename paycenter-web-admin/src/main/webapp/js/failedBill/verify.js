

function confirm(){
	var resultType = $('#resultType').val(); //场景类型
	var code = $('#verifyCode').val();//验证码
	var mobile = $('#mobile').val();
	var param = {code:code, mobile:mobile};
	jQuery.post(CONTEXT + "paycenter/failedBill/verifyCode", param, function(data) {
 		if (data.status == "1") { //验证通过
// 			$('#updateFailedBillDialog').dialog({'title':'修改对账明细','href':CONTEXT+'paycenter/failedBill/failedBillDetail/get?payCenterNumber='+payCenterNumber,'width': 500,'height': 700}).dialog('open');
 			$('#updateFailedBillDialog').dialog('close');
			$('#smsValidateDialog').dialog('close');
			if(resultType == '1'){  //流水-银行有谷登有
 			     submitFailedBill();
			}else if(resultType == '2'){ //流水-银行无谷登有
			     submitFailedBillBankNone();
			}else if(resultType == '3'){  //流水-银行有谷登无
				 submitFailedBillGudengNone();
			}
		}else{
			/*slideMessage(data.msg);*/
			jQuery.messager.alert('错误',data.msg,'error');
		}
});
}


function generateVerifyCode(obj){
	var mobile = $('#mobile').val();//电话
	var param = {mobile:mobile};
	jQuery.post(CONTEXT + "paycenter/failedBill/generateVerifyCode", param, function(data) {
 	    if(data.status == '1'){
 	    	jQuery.messager.alert('提示',data.msg,'warning');
 	    }else{
 	    	jQuery.messager.alert('错误',data.msg,'error');
 	    }
 	    settime(obj);
    });
   
}

var countdown=60;
function settime(obj) { 
	    if (countdown == 0) { 
	        obj.removeAttribute("disabled");  
	        obj.value="重新获取验证码";
	        obj.style.background="#f98425";
	        countdown = 60;
	        return;
	    } else { 
	        obj.setAttribute("disabled", true); 
	        obj.value=countdown+"秒 重新发送 "; 
	        obj.style.background="#8a8989";
	        countdown--;    
	    } 
	  setTimeout(function() { 
		  settime(obj) 
		},1000) 	 
  }


function submitFailedBill() {
	var dataJson="[";
	var tradeMoney = "";
    var fee = "";
    var payType = $("#update input[name='payInfoType']:checked").val();
    if(payType == 1){
    	tradeMoney = $('#tradeMoney').val();
    	fee = $('#fee').val();
    }else{
    	tradeMoney = $('#tradeMoneyThird').val();
    	fee = $('#feeThird').val();
    }
    
    var payStatus = $("#update input[name='payStatus']:checked").val();
    var remark = $('#remark').val();
	var thirdPayNumber = $('#thirdPayNumber').val();
	var payTime = $('#payTime').val();
	var resultType = $('#resultType').val();
	var remark = $('#remark').val();
	$("#orderTab tr").each(function (index, domEle){
        orderNo = "";  
        amt = "";  
        if(index != 0){//遍历除去第一行的之外的所有input作为json数据传入后台  
            $(domEle).find("input").each(function(index,data){  
                if(index == 0){  
                	orderNo = $(data).val();  
                }else{  
                    if($(data).val() != "" && $(data).val() != null){//如果没有输入的情况下传的值是0  
                    	amt += "," + $(data).val();  
                    }else{  
                    	amt += "," + 0;  
                    }  
                }  
            });  
            if(!amt.indexOf(",")){  
            	amt = amt.substring(1);  
            }  
            dataJson += "{"+"\"orderNo\":\""+orderNo+"\","+"\"actualAmt\":\""+amt+"\"},";    
        }
	}
	);
	
	if (dataJson.lastIndexOf(",")) {  
        dataJson = dataJson.substring(0,dataJson.length -1);  
        dataJson += "]";
    }
	var param = {
		resultType : resultType,
		tradeMoney : tradeMoney,
		fee : fee,
		payStatus : payStatus,
		thirdPayNumber : thirdPayNumber,
		remark : remark,
		orderList : dataJson

	};
	jQuery.post(CONTEXT + "paycenter/failedBill/updateFailedBill", param,
			function(data) {
		      if(data.result == "0"){
		    	  jQuery.messager.alert('错误',data.msg,'error');
		      }else{
		    	  jQuery.messager.alert('提示',data.msg,'warning');
		      }
		      $("#failedBilldg").datagrid('reload');
			  /*$('#updateFailedBillDialog').dialog('close');
			    $('#smsValidateDialog').dialog('close');*/
			});
}

function submitFailedBillBankNone() {
	var param_banknone;
	var dataJson="[";
	var tradeMoney = "";
    var fee = "";
    var thirdPayNumber;
    var cardNo;
    var payTime;
    var payStatus;
    
    var clientNo = $('#clientNo').val();
	var businessNo = $('#businessNo').val();
	var tradeType = $('#tradeType').val();
	var cardType = $('#cardType').val();
	var isInnerBank = $("#isInnerBank").val();
	var isOneCity = $("#isOneCity").val();
    var remark = $('#remark').val();
    var payType = $("#update input[name='payInfoType']:checked").val();
    var resultType = $('#resultType').val();
    $("#orderTab tr").each(function (index, domEle){
        orderNo = "";  
        amt = "";  
        if(index != 0){//遍历除去第一行的之外的所有input作为json数据传入后台  
            $(domEle).find("input").each(function(index,data){  
                if(index == 0){  
                	orderNo = $(data).val();  
                }else{  
                    if($(data).val() != "" && $(data).val() != null){//如果没有输入的情况下传的值是0  
                    	amt += "," + $(data).val();  
                    }else{  
                    	amt += "," + 0;  
                    }  
                }  
            });  
            if(!amt.indexOf(",")){  
            	amt = amt.substring(1);  
            }  
            dataJson += "{"+"\"orderNo\":\""+orderNo+"\","+"\"actualAmt\":\""+amt+"\"},";    
        }
	}
	);
	
	if (dataJson.lastIndexOf(",")) {  
        dataJson = dataJson.substring(0,dataJson.length -1);  
        dataJson += "]";
    }
	
	if(payType == 1){
    	tradeMoney = $('#tradeMoney').val();
    	fee = $('#fee').val();
    	thirdPayNumber = $('#thirdTransNo').val();
    	cardNo = $("#cardNo").val();
    	payTime = $('#payTime').val();
    	payStatus = $("#update input[name='payStatus']:checked").val();
    	
    	param_banknone = {
    			resultType : resultType,
    			clientNo : clientNo,
    			businessNo : businessNo,
    			tradeType : tradeType,
    			thirdPayNumber : thirdPayNumber,
    			tradeMoney : tradeMoney,
    			fee : fee,
    			cardNo : cardNo,
    			cardType : cardType,
    			bankType : isInnerBank,
    			areaType : isOneCity,
    			payTime : payTime,
    			payStatus : payStatus,
    			remark : remark,
    			orderList : dataJson
    		};
    }else{
    	tradeMoney = $('#tradeMoneyThird').val();
    	fee = $('#feeThird').val();
    	thirdPayNumber = $('#thirdTransNoThird').val();
    	cardNo = $("#cardNoThird").val();
    	payTime = $('#payTimeThird').val();
    	payStatus = $("#update input[name='payStatusThird']:checked").val();
    	
    	param_banknone = {
    			resultType : resultType,
    			thirdPayNumber : thirdPayNumber,
    			tradeMoney : tradeMoney,
    			fee : fee,
    			cardNo : cardNo,
    			payTime : payTime,
    			payStatus : payStatus,
    			remark : remark,
    			orderList : dataJson
    		};
    }
	
		    
	jQuery.post(CONTEXT + "paycenter/failedBill/updateFailedBill", param_banknone,
			function(data) {
		      if(data.result == "0"){
		    	  jQuery.messager.alert('错误',data.msg,'error');
		      }else{
		    	  jQuery.messager.alert('提示',data.msg,'warning');
		      }
		      $("#failedBilldg").datagrid('reload');
			});
}


function submitFailedBillGudengNone() {
	var dataJson="[";
	var tradeMoney = "";
    var fee = "";
    var cardNo = "";
    var payType = $("#update input[name='payInfoType']:checked").val();
    if(payType == 1){
    	tradeMoney = $('#tradeMoney').val();
    	fee = $('#fee').val();
    	cardNo =  $('#cardNo').val();
    }else{
    	tradeMoney = $('#tradeMoneyThird').val();
    	fee = $('#feeThird').val();
    	cardNo =  $('#cardNoThird').val();
    }
    var payStatus = $("#update input[name='payStatus']:checked").val();
    var remark = $('#remark').val();
	var thirdPayNumber = $('#thirdPayNumber').val();
	var payTime = $('#payTime').val();
	var resultType = $('#resultType').val();
	var tradeType = $("#tradeType").val();
	var clientNo = $('#clientNo').val();
	
	$("#orderTab tr").each(function (index, domEle){
        orderNo = "";  
        payCenterNo = "";
        amt = "";  
        mobile = "";
        if(index != 0){//遍历除去第一行的之外的所有input作为json数据传入后台  
        	type = $('input:radio[name="optType_'+index+'"]:checked').val();
            $(domEle).find("input").each(function(index,data){  
            	if(type == 1){
            		if(index == 2){
            			orderNo = $(data).val();
            		}else if(index == 3){
            			amt = $(data).val();
            		}
            	}
            	
            	if(type == 2){
            		if(index == 2){
            			amt = $(data).val();
            		}else if(index == 3){
            			mobile = $(data).val();
            		}
            	}
            });  
    		if(!amt.indexOf(",")){  
            	amt = amt.substring(1);  
            }  
    		if(type == 1){
    		    dataJson += "{"+"\"orderFlag\":\""+type+"\","+"\"orderNo\":\""+orderNo+"\","+"\"actualAmt\":\""+amt+"\"},"; 
    		}else{
    			var orderType = $('#orderType'+index+' option:selected').val();
    			dataJson += "{"+"\"orderFlag\":\""+type+"\","+"\"appKey\":\""+orderType+"\","+"\"actualAmt\":\""+amt+"\","+"\"sellerMobile\":\""+mobile+"\"},";
    		}
        }
	}
	);
	if (dataJson.lastIndexOf(",")) {  
        dataJson = dataJson.substring(0,dataJson.length -1);  
        dataJson += "]";
    }
	
	var param_gudengnone = {
		tradeMoney : tradeMoney,
		fee : fee,
		payStatus : payStatus,
		thirdPayNumber : thirdPayNumber,
		resultType : resultType,
		remark : remark,
		tradeType : tradeType,
		clientNo : clientNo,
		cardNo : cardNo,
	    orderList : dataJson
	};
	jQuery.post(CONTEXT + "paycenter/failedBill/updateFailedBill", param_gudengnone,
			function(data) {
		      if(data.result == "0"){
		    	  jQuery.messager.alert('错误',data.msg,'error');
		      }else{
		    	  jQuery.messager.alert('提示',data.msg,'warning');
		      }
		      $("#failedBilldg").datagrid('reload');
			  /*$('#updateFailedBillDialog').dialog('close');
			    $('#smsValidateDialog').dialog('close');*/
			});
}




