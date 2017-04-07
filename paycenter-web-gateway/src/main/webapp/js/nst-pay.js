var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

//设置金额的样子
	function setMoney(){
		var money=$(".headerMoney span").text();
		money=parseFloat(money);
		money=toFix(money,2);
		$(".headerMoney span").text(money);
	}
	
	function beginChoice(){
		var bottom = $(".choice-pay li .choice-logo");
		bottom.attr("src",path + "images/not_choice.png").attr("value","0");
		var bottom_1 = $(".choice-pay li .choice-logo").eq(0);
		bottom_1.attr("src",path + "images/yes_choice.png").attr("value","1");	
	}
//点击选择支付类型
	function clickChoice(){
		var bottom=$(".choice-pay li .choice-logo");
		bottom.tap(function(){
			bottom.attr("src",path + "images/not_choice.png").attr("value","0");
			$(this).attr("src",path + "images/yes_choice.png").attr("value","1");
		})
	}
//点击支付
	function clickPay(){
		hideMarkAll(true);
		var oChoice=document.getElementsByClassName("choice-logo");
		var bol;
		for(var i=0;i<oChoice.length;i++){
			var v=oChoice[i].value;
			if(v=="1"){
				bol=oChoice[i];
			}
		}
		try {
			$.ajax({
		        type: "POST",
		        url:  path + 'gw/pay/' + bol.id,
		        data:$('#gwForm').serialize(),
		        dataType: "json",
		        success: function(data){	        	
		        	if(data.respCode == 10000){
		        		if (data.payType) {
		        			if(data.url && data.payType == 'ALIPAY_H5'){
			        			location.href = data.url;
		        			} else if(data.jsonStr && data.payType == 'WEIXIN_APP') {
		        				if(isAndroid){
		        					window.JsBridge.payByWX(data.jsonStr);
		        				}
		        				if(isIOS){
		        					window.webkit.messageHandlers.NativeWeixinPay.postMessage(data.jsonStr);
		        				}
		        			} else {
		        				$('#gwForm').attr("action", path + "gw/payFail?respCode="+data.respCode+"&respMsg="+data.respMsg).submit();
		        			}
			    		}else{
			        		$('#gwForm').attr("action", path + "gw/payFail?respCode="+data.respCode+"&respMsg="+data.respMsg).submit();
			    		}
		        	}else{	        		
		        		$('#gwForm').attr("action", path + "gw/payFail?respCode="+data.respCode+"&respMsg="+data.respMsg).submit();
		        	}
		        	
		        }
			});
		} finally {
			hideMarkAll(false);
		}
	}
	
	
	var returnUrl = $("#returnUrl").val();
	function viewOrder(){
		$('#failForm').attr("action", returnUrl).submit();
		//window.webkit.messageHandlers.NativeMethod.postMessage("close");	
	}
	
	function hideMarkAll(flag){
		if(!flag){
			$(".markAll").hide();
		}else{
			$(".markAll").show();
		}
	}
	
$(document).ready(function(){
	hideMarkAll(false);
	//setMoney();
	beginChoice();	
	clickChoice();
}); 






