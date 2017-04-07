<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script src="js/jquery.min.js"></script>
<head>
<style type="text/css">
      table.browsersupport {
            border-collapse: collapse;
      }
	  table.browsersupport th {
		    color: #333333;
		    font-weight: 400;
	  }
	  table.browsersupport th {
		    background-color: #efefef;
		    border: 1px solid #c3c3c3;
		    height: 15px;
		    padding: 3px;
		    text-align: center;
		    vertical-align: middle;
       }
       table.dataintable th {
		    background-color: #d5d5d5;
		    border: 1px solid #aaa;
		    padding: 5px 15px 5px 6px;
		    text-align: center;
		    vertical-align: baseline;
	   }
	  
	  /*td*/
	  able.browsersupport td {
		    color: #333333;
		    font-weight: 400;
	  }
       table.dataintable td {
		    border: 1px solid #aaa;
		    padding: 5px 15px 5px 6px;
		    text-align: center;
		    vertical-align: baseline;
	   }
	   
	   
</style>

<script>
   var total = ${entity.actualTotalAmt};
   extend = function (a, b) { //合并对象
      if (typeof a !== "object" || typeof b !== "object") {
         return;
      }
      for (key in b) {
         a[key] = b[key];
      }
      return a;
   };
 
   extend(Math, {
      /*************格式化数值***************/
      format: function (num, style) {
         if (!/#+([^#]+)?(#+)(?:\.(#+))?/.test(style)) {
            return;
         }
         //分割符
         var split = RegExp.$1;
         //保留小数位数
         num = parseFloat(num).toFixed(RegExp.$3.length);
         var re = new RegExp('(\\d{1,' +  RegExp.$2.length + '})(?=(\\d{' +  RegExp.$2.length + '})+(?:$|\\.))', 'g');
         //console.log(re);
         if(!/(-?\d+)(?:\.(\d+))?/.test(num)){
            return;
         }
         var d = (RegExp.$2 ? '.' + RegExp.$2 : ''),
               str = RegExp.$1.replace(re, function (a, b, c, d) {
                  return b + split;
               });
         return str + d;
      }
   });
 
   $(function(){
      /* var total = 0; */
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
   });
</script>
</head>

<table id="failedBillDetaildg" align="center"></table>
	<div id="update" style="margin: 0px 5px">
		<div style="padding: 15px 0px 5px 8px; border-bottom: 1px solid #ccc;">银行/第三方信息</div>
		<%-- <form id="form1" method="post">
		<input type="hidden" id="payCenterNumber" value="${entity.payCenterNumber}" />
		<input type="hidden" id="resultType" value="${entity.resultType}" />
		<input type="hidden" id="thirdPayNumber" value="${entity.thirdTransNo}" />
		<input type="hidden" id="transDate" value="${entity.trans_date}" /> --%>
		
		<table class="beneficiaryDetail" align="center" width="100%">
		    <td align="right" width="25%" height="15px"><b>支付渠道类型：</b></td>
		    <td width="25%">
		       <input type="radio" name="payInfoType" onchange="changePayInfo(1)" value="1" checked="true">银行</input>
		       <input type="radio" name="payInfoType" onchange="changePayInfo(2)" value="2">第三方</input>
		   </td>
		</table>
		<div id="bankInfoDiv">
		<form id="form1" method="post">
		<input type="hidden" id="thirdPayNumber" value="${entity.thirdTransNo}" />
		<input type="hidden" id="resultType" value="${entity.resultType}" />
		<table class="beneficiaryDetail" align="center" width="100%">
			<tr>
				<td align="right" width="25%" height="15px"><em style="color:red">*</em><b>POS终端号：</b></td>
				<td width="25%"><input type="text" id="clientNo" name="clientNo" data-options="required:true" class="easyui-validatebox"></td>
			</tr>
			<tr>
			<td align="right" width="25%"><em style="color:red">*</em><b>POS商户号：</b></td>
				<td width="25%"><input type="text" id="businessNo" name="businessNo" data-options="required:true"  class="easyui-validatebox"></td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>交易类型：</b></td>
				<td>
				   <select id="tradeType">
				    <option value="">请选择</option>
				    <option value="0">订单支付</option>
				    <option value="1">刷卡消费</option>
				  </select>
				</td>
			</tr>
			<tr>
				<td align="right" height="15px"><em style="color:red">*</em><b>第三方支付流水：</b></td>
				<td><input type="text" id="thirdTransNo" name="thirdTransNo" data-options="required:true" class="easyui-validatebox"></td>
			</tr>
			<tr>
			    <td align="right"><em style="color:red">*</em><b>谷登代收金额：</b></td>
				<td><input type="text" id="tradeMoney" name="tradeMoney" onblur="formatPayAmt(this);" data-options="required:true,validType:'tradeMoney'" class="easyui-validatebox"></td>
			</tr>
			<tr>
			    <td align="right"><em style="color:red">*</em><b>谷登代收手续费：</b></td>
				<td><input type="text" id="fee" name="fee" onblur="formatPayAmt(this);" data-options="required:true,validType:'fee'" class="easyui-validatebox"></td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>支付卡号：</b></td>
				<td><input type="text" id="cardNo" name="cardNo" ></td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>卡类型：</b></td>
				<td>
				  <select id="cardType">
				    <option value="">请选择</option>
				    <option value="1">借贷卡</option>
				    <option value="2">借记卡</option>
				  </select>
				</td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>是否本行：</b></td>
				<td>
				<select id="isInnerBank">
				    <option value="">请选择</option>
				    <option value="1">本行</option>
				    <option value="2">跨行</option>
				</select>
				</td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>是否同城：</b></td>
				<td>
				<select id="isOneCity">
				    <option value="">请选择</option>
				    <option value="1">同城</option>
				    <option value="2">异地</option>
				</select>
				</td>
			</tr>
			<tr>
				<td align="right" height="15px"><em style="color:red">*</em><b>支付时间：</b></td>
				<td>
				   <input  type="text" id="payTime" name="payTime" data-options="required:true" class="easyui-validatebox"
					onFocus="WdatePicker({onpicked:function(){payTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"   
					onClick="WdatePicker({onpicked:function(){payTime.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
					style="width:100px"></input>
				</td>
			</tr>
			<tr>
			    <td align="right"><em style="color:red">*</em><b>支付状态：</b></td>
				<td>
				  <input name="payStatus" id="payStatus1" type="radio" value="1" checked="true"/>待付款 
				  <input name="payStatus" id="payStatus2" type="radio" value="2"/>付款成功
				</td>
			</tr>
		</table>
		</form>
		</div>
		
		<div id="thirdInfoDiv" style="display:none">
		<form id="form2" method="post">		
		<input type="hidden" id="thirdPayNumber" value="${entity.thirdTransNo}" />
		<input type="hidden" id="resultType" value="${entity.resultType}" />
		<table class="beneficiaryDetail" align="center" width="100%">
			<tr>
				<td align="right" width="50%" height="15px"><em style="color:red">*</em><b>第三方支付流水：</b></td>
				<td><input type="text" id="thirdTransNoThird" name="thirdTransNoThird" data-options="required:true" class="easyui-validatebox"></td>
			</tr>
			<tr>
			    <td align="right"><em style="color:red">*</em><b>谷登代收金额：</b></td>
				<td><input type="text" id="tradeMoneyThird" name="tradeMoneyThird" onblur="formatPayAmt(this);" data-options="required:true,validType:'tradeMoney'" class="easyui-validatebox"></td> 
			</tr>
			<tr>
			    <td align="right"><em style="color:red">*</em><b>谷登代收手续费：</b></td>
				<td><input type="text" id="feeThird" name="feeThird" onblur="formatPayAmt(this);" data-options="required:true,validType:'feeThird'" class="easyui-validatebox"></td>
			</tr>
			<tr>
				<td align="right" height="15px"><b>支付卡号：</b></td>
				<td><input type="text" id="cardNoThird" name="cardNoThird" ></td>
			</tr>
			<tr>
				<td align="right" height="15px"><em style="color:red">*</em><b>支付时间：</b></td>
				<td>
				<input  type="text" id="payTimeThird" name="payTimeThird" data-options="required:true" class="easyui-validatebox"
					onFocus="WdatePicker({onpicked:function(){payTimeThird.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})"   
					onClick="WdatePicker({onpicked:function(){payTimeThird.focus();},dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
					style="width:100px" ></input>
				</td>	
			</tr>
			<tr>
			    <td align="right"><em style="color:red">*</em><b>支付状态：</b></td>
				<td>
				  <input name="payStatusThird"<c:if test="${entity.payStatus==1 || empty entity.payStatus}"> checked="checked"</c:if>id="payStatus1" type="radio" value="1"/>待付款 
				  <input name="payStatusThird"<c:if test="${entity.payStatus==2 }"> checked="checked"</c:if> id="payStatus2" type="radio" value="2"/>付款成功
				</td>
			</tr>
		</table>
		</form>
		</div>
		
		<div style="margin:0px 5px">
		<form id="form3" method="post">
	         <div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">谷登订单信息</div>
	          <table id="orderTab" class="dataintable browsersupport" width="95%" align="center" style="margin-top: 12px">
               <tbody>
			     <tr>
				   <th>谷登订单号</th>
				   <th>平台支付流水</th>
				   <th><em style="color:red">*</em>实付金额</th>
				   <th>卖家手机号</th>
				   <th>是否需要清分</th>
			    </tr>
					<c:if test="${!empty orderInfoList}">
						<c:forEach var="gdOrderInfo" items="${orderInfoList}">
							<tr>
								<td>${gdOrderInfo.orderNo}<input type="hidden" id="orderNo${status.index+1}" name="orderNo" value="${gdOrderInfo.orderNo}"></input></td>
								<td>${gdOrderInfo.payCenterNumber}</td>
								<td><input type="text" id="payAmt${status.index+1}" name="payAmt" class="int easyui-validatebox" value="${gdOrderInfo.payAmt}" onblur="formatPayAmt(this);" data-options="required:true,validType:'payAmt'" class="easyui-validatebox"></input></td>
								<td>${gdOrderInfo.payeeMobile}</td>
								<td>${gdOrderInfo.hasClear=='1'? '是' : '否'}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
				
			   </table>
			   </form>
			   </div>
			   
			   <table class="beneficiaryDetail" align="center" width="100%">
			        <tr >
				      <td colspan="2" align="right" width="25%" height="15px"><b><div class="total"><b>实付合计：${entity.actualTotalAmt}</b></div></td>
			       </tr>
			       <tr>
				      <td align="right" width="25%" height="15px"><b>付款方手机：</b></td>
				      <td width="25%">${entity.payerMobile}</td>
			       </tr>
			       <tr>
			          <td align="right" width="25%"><b>对账失败原因：</b></td>
				      <td width="25%">${entity.failReason}</td>
			       </tr>
			     <tr>
			         <td align="right" height="15px"><b>备注：</b></td>
				     <td><textarea id="remark" cols=30 rows=4 maxlength="225">${entity.remark}</textarea></td>
			     </tr>
			   </table>
		
			
		</div>
	</div>
	
<script type="text/javascript" src="${CONTEXT}../../js/failedBill/detailImcomplete.js"></script>
<script language="javascript">
function formatPayAmt(obj){
	
	    if(isNaN(obj.value)){
	    	return;
	    }
	    if(obj.value.indexOf(".") == 0 || obj.value < 0 || obj.value == '' ){
	    	return;
	    }
	    
	    if(obj.value.indexOf(".") > 0 ){
	    	if(obj.value.split(".")[1].length >= 2 || obj.value.split(".")[1].length == 0){
	    		return;
	    	}
	    }
	    
		if(obj.value.indexOf(".") < 0){
			obj.value = obj.value + ".00";
		}else{
			var decimalValue = obj.value.split(".")[1]; 
			if(decimalValue.length >= 2 ){
				obj.value = obj.value.split(".")[0] + "." + decimalValue.substring(0,2);
			}else{
				obj.value = obj.value + "0";
			}
		}
	}
</script>
