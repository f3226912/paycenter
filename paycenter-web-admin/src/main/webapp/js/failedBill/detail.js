var total = 0;
$(document).ready(function(){
	initValidateRules();
	$("#btnAddRow").click(function(){
        var _len = $("#orderTab tr").length;
        var content = "<tr id="+_len+" align='center' onclick='getSelectedRow("+_len+");'>"
        +"<td>" +
           "<input type='radio' onchange='changeOptType(this);' id='optTypeBind_"+_len+"' name='optType_"+_len+"' checked='true' value='1'>绑定订单</input>" +
           "<input type='radio' onchange='changeOptType(this);' id='optTypeCreate_"+_len+"' name='optType_"+_len+"' value='2'>制单</input>" +
           "</td>"
        +"<td>-</td>"
        +"<td><input type='text' id='orderNo"+_len+"' class='easyui-validatebox validatebox-text' data-options='required:true'/></td>"
        +"<td>-</td>"
        +"<td><input type='text' id='amt"+_len+"' name='amt' onblur='formatPayAmt(this);'  class='int easyui-validatebox validatebox-text' data-options=\"required:true,validType:\'amt\'\" title/></td>"  
        +"<td>-</td>"
        +"</tr>"
       $("#orderTab").append(content);
        
       $("#amt"+_len).validatebox(); //初始化校验 
       $("#orderNo"+_len).validatebox();
        if($("#orderTab tr").length > 1){
        	$("#delTrHref").show();
        }
       
            $('.int').each(function(){
      	      $(this).keypress(function(event){
      	         if(!((event.charCode >= 48 && event.charCode <= 57) || event.charCode === 46 || event.charCode === 45 || event.charCode === 0)){
      	            if(event.preventDefault){
      	               event.preventDefault()
      	            }else{
      	               event.returnValue = false;
      	            }
      	         }
      	      }).keyup(function(){
      	         total = 0;
      	         $('.int').each(function(){
      	            if($(this).val() === '.' || $(this).val() === '-'){
      	               return;
      	            }
      	             
      	            total += parseFloat($(this).val() || 0);
      	         });

      	         $('.total').html("实付合计：" + Math.format(total,'#,###.##'));
      	      });
      	   });
        })
});


var currentRow;
var lastSelectdRow;
function getSelectedRow(rowNo){
	if(lastSelectdRow != 'undefined'){
		$("tr[id='"+lastSelectdRow+"']").css('background-color', 'white');
	}
	currentRow = rowNo;
	$("tr[id='"+rowNo+"']").css('background-color', '#EAEAEA');
	lastSelectdRow = rowNo;
}

function changeOptType(obj){
	var replaceAmt;
	var checkedId = obj.id;
	var type = checkedId.substring(0, checkedId.indexOf("_"));
	var index = checkedId.substring(checkedId.indexOf("_") + 1, checkedId.length);
	if(type == 'optTypeBind'){
		replaceAmt = $("#amt"+index).val();
		$("tr[id=\'"+index+"\']").replaceWith("<tr id="+index+" align='center' onclick='getSelectedRow("+index+");'>"
				+"<td>" +
                "<input type='radio' onchange='changeOptType(this);' id='optTypeBind_"+index+"' name='optType_"+index+"' value='1' checked='true'>绑定订单</input>" +
                "<input type='radio' onchange='changeOptType(this);' id='optTypeCreate_"+index+"' name='optType_"+index+"' value='2'>制单</input>" +
                "</td>"
                +"<td>-</td>"
                +"<td><input type='text' id='orderNo"+index+"' /></td>"
                +"<td>-</td>"
                +"<td><input type='text' id='amt"+index+"' name='amt' onblur='formatPayAmt(this);'  class='int easyui-validatebox validatebox-text' data-options=\"required:true,validType:\'amt\'\" title/></td>"
                +"<td>-</td>"
            +"</tr>");
	}else{
		replaceAmt = $("#amt"+index).val();
		$("tr[id=\'"+index+"\']").replaceWith("<tr id="+index+" align='center' onclick='getSelectedRow("+index+");'>"
				+"<td>" +
                "<input type='radio' onchange='changeOptType(this);' id='optTypeBind_"+index+"' name='optType_"+index+"' value='1'>绑定订单</input>" +
                "<input type='radio' onchange='changeOptType(this);' id='optTypeCreate_"+index+"' checked='true' name='optType_"+index+"' value='2'>制单</input>" +
                "</td>"
                +"<td><select id='orderType"+index+"'>"
                +"<option value='nst_car'>信息费订单</option>"
                +"<option value='nst_goods'>货运订单</option>"
                +"<option value='gys'>服务订单</option>"
                +"<option value='nsy'>农商友采购订单</option>"
                +"</select></td>"
		        +"<td>-</td>"
		        +"<td>-</td>"
                +"<td><input type='text' id='amt"+index+"' name='amt' onblur='formatPayAmt(this);'  class='int easyui-validatebox validatebox-text' data-options=\"required:true,validType:\'amt\'\" title/></td>"
                +"<td><input type='text' id='mobile"+index+"' name='mobile' data-options=\"required:true,validType:\'tel\'\" /></td>"
            +"</tr>");
	}
	
	total = total - replaceAmt;
	$('.total').html("实付合计：" + Math.format(total,'#,###.##'));
	$("#amt"+index).validatebox(); //初始化校验 
	$("#mobile"+index).validatebox(); //初始化校验 
	$('.int').each(function(){
	      $(this).keypress(function(event){
	         if(!((event.charCode >= 48 && event.charCode <= 57) || event.charCode === 46 || event.charCode === 45 || event.charCode === 0)){
	            if(event.preventDefault){
	               event.preventDefault()
	            }else{
	               event.returnValue = false;
	            }
	         }
	      }).keyup(function(){
	         total = 0;
	         $('.int').each(function(){
	            if($(this).val() === '.' || $(this).val() === '-'){
	               return;
	            }
	            total += parseFloat($(this).val() || 0);
	         });

	         $('.total').html("实付合计：" + Math.format(total,'#,###.##'));
	      });
	   });
	
}
//删除表格行
function detTr(){
    var _len = $("#orderTab tr").length;
    var minusAmt = 0;
    if(currentRow == undefined){
    	if(_len > 1){
    	  minusAmt = $("#amt"+(_len-1)).val();
    	  $("tr[id='"+(_len-1)+"']").remove();//删除最后一行
    	  
    	}
    } else{
    	minusAmt = $("#amt"+currentRow).val();
        $("tr[id='"+currentRow+"']").remove();//删除当前行
        for(var i=currentRow+1,j=_len;i<j;i++)
        {
        	$("tr[id=\'"+i+"\']").attr("id", i-1);
        	$("tr[id=\'"+(i-1)+"\']").attr("onclick","getSelectedRow("+(i-1)+");");
        	$("input[id=\'optTypeBind_"+i+"\']").attr("id", "optTypeBind_" + (i-1));
        	$("input[id=\'optTypeCreate_"+i+"\']").attr("id", "optTypeCreate_" + (i-1));
        	$("input[id=\'amt"+i+"\']").attr("id", "amt" + (i-1));
        }
        currentRow = undefined;
    }
    if($("#orderTab tr").length == 1){
    	$("#delTrHref").hide();
    }
    total = total - minusAmt;
	$('.total').html("实付合计：" + Math.format(total,'#,###.##'));
}

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
    	tradeMoney = $("#update input[id='tradeMoneythird']").val();
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
	/*var thirdPayNumber = $('#thirdPayNumber').val();
	var resultType = $('#resultType').val();
	var remark = $('#remark').val();*/
	var parameter = thirdPayNumber + "_" + resultType;
	var payType = $("#update input[name='payInfoType']:checked").val();
	$('#alertMsgDialog').dialog('close');
	if(remark.length>225){
		jQuery.messager.alert('错误',"备注最大长度为225！",'error');
		return;
	}
	
	$('#smsValidateDialog').dialog({'title':'验证管理员手机号码','href':CONTEXT+'paycenter/failedBill/toSmsValidate/'+parameter,'width': 380,'height': 200}).dialog('open');
	
	/*if(payType == 1){
	    if ($('#update #form1').form('validate') && $('#update #form3').form('validate')) {
	    	$('#smsValidateDialog').dialog({'title':'验证管理员手机号码','href':CONTEXT+'paycenter/failedBill/toSmsValidate/'+parameter,'width': 380,'height': 200}).dialog('open');
	    }
    }else if(payType == 2){
    	if ($('#update #form2').form('validate') && $('#update #form3').form('validate')) {
    		$('#smsValidateDialog').dialog({'title':'验证管理员手机号码','href':CONTEXT+'paycenter/failedBill/toSmsValidate/'+parameter,'width': 380,'height': 200}).dialog('open');
      }
    }*/
	
}


function initValidateRules(){
	$.extend($.fn.validatebox.defaults.rules, {
		fee:{
			validator: function (value) {
				var procedures=$("#update input[name='fee']:checked").val();
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
			var procedures=$("#update input[name='tradeMoney']:checked").val();
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
   amt:{
		validator: function (value) {
			var procedures=$("#update input[name='amt']:checked").val();
			if(procedures==2||procedures=='2'){
				return true;
			}
			return /^(\d{1,8}(\.\d{1,2})?|100000000.00)$/.test(value);
		},
       message: '请输入正确的范围，可填范围0.00-100000000.00. 精确到小数点后两位.'
  },
  tel:{
		validator: function (value) {
			var procedures=$("#update input[name='mobile']:checked").val();
			if(procedures==2||procedures=='2'){
				return true;
			}
			return /^1[3|4|5|8]\d{9}$/.test(value);
		},
     message: '请输入正确的手机号.'
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