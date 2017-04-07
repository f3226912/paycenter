<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../pub/tags.inc" %>
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
</head>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">付款方信息</div>
	<table class="grid" align="center" width="100%">
		<tr>
			<td align="right" width="25%">付款方账号：</td>
			<td width="25%">${payInfo.payerAccount}</td>
			<td align="right" width="25%">支付方式：</td>
			<td width="25%">
			   <c:choose>
					<c:when test="${payInfo.payType == 'ALIPAY_H5'}">支付宝</c:when>
					<c:when test="${payInfo.payType == 'WEIXIN_APP'}">微信</c:when>
					<c:when test="${payInfo.payType == 'PINAN'}">平安银行</c:when>
					<c:when test="${payInfo.payType == 'ENONG'}">E农</c:when>
					<c:when test="${payInfo.payType == 'NNCCB'}">南宁建行</c:when>
					<c:when test="${payInfo.payType == 'GXRCB'}">广西农信</c:when>
					<c:when test="${payInfo.payType == 'WANGPOS'}">旺POS</c:when>
			   </c:choose>
			</td>
		</tr>
		<tr>
			<td align="right">付款方手机：</td>
			<td>${payInfo.payerMobile}</td>
			<td align="right">终端号：</td>
			<td>${payInfo.posClientNo}</td>
		</tr>
		<tr>
			<td align="right">实付金额：</td>
			<td>${payInfo.payAmt}</td>
			<td align="right">平台支付流水：</td>
			<td>${payInfo.payCenterNumber}</td>
		</tr>
		<tr>
			<td align="right">支付状态：</td>
			<td>
			    <c:choose>
					<c:when test="${payInfo.payStatus == '1'}">待付款</c:when>
					<c:when test="${payInfo.payStatus == '2'}">付款成功</c:when>
					<c:when test="${payInfo.payStatus == '3'}">已关闭</c:when>
					<c:when test="${payInfo.payStatus == '4'}">已退款</c:when>
			    </c:choose>
			</td>
			<td align="right">第三方支付流水：</td>
			<td>${payInfo.thirdPayNumber}</td>
		</tr>
		<tr>
			<td align="right">支付时间：</td>
			<td><fmt:formatDate value="${payInfo.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td align="right">对账状态：</td>
			<td>
			   <c:choose>
			      <c:when test="${payInfo.checkStatus == '1'}">对账成功</c:when>
			      <c:when test="${payInfo.checkStatus == '2'}">对账失败</c:when>
			   </c:choose>
			</td>
		</tr>
	</table>
	<table class="dataintable browsersupport" align="center">
       <tbody>
			<tr>
				<th width="200px">订单号</th>
				<th width="200px">下单时间</th>
				<th width="100px">订单类型</th>
				<th width="100px">付款方来源</th>
				<th width="100px">商品总金额</th>
				<!-- <th width="100px">佣金支出</th> -->
				<th width="100px">市场佣金</th>
				<th width="100px">平台佣金</th>
				<th width="100px">实付金额</th>
				<th width="100px">清分状态</th>
			</tr>
			<c:if test="${!empty payerInfoList}">
				<c:forEach var="payerInfo" items="${payerInfoList}">  
				   <tr>
					<td>${payerInfo.orderNo}</td>
					<td><fmt:formatDate value="${payerInfo.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
					      <c:choose>
								<c:when test="${payerInfo.orderType == '1'}">农商友采购订单</c:when>
								<c:when test="${payerInfo.orderType == '2'}">农批商采购订单</c:when>
								<c:when test="${payerInfo.orderType == '3'}">农商友6+1订单</c:when>
								<c:when test="${payerInfo.orderType == '4'}">服务订单</c:when>
								<c:when test="${payerInfo.orderType == '21'}">信息订单</c:when>
								<c:when test="${payerInfo.orderType == '22'}">货运订单</c:when>
				          </c:choose>
				    </td>
					<td>  
					     <c:choose>
						      <c:when test="${payerInfo.appKey == 'nst'}">农速通</c:when>
						      <c:when test="${payerInfo.appKey == 'nst_car'}">农速通-车主</c:when>
						      <c:when test="${payerInfo.appKey == 'nst_goods'}">农速通-货主</c:when>
						      <c:when test="${payerInfo.appKey == 'nps'}">农批商</c:when>
						      <c:when test="${payerInfo.appKey == 'nsy_pay'}">农商友</c:when>
						      <c:when test="${payerInfo.appKey == 'gys'}">供应商</c:when>
						      <c:when test="${payerInfo.appKey == 'nst_fare'}">农速通-运费</c:when>
						      <c:when test="${payerInfo.appKey == 'nsy'}">农商友</c:when>
				         </c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${empty payerInfo.orderAmt}">-</c:when>
							<c:otherwise>
								<fmt:formatNumber type='number' value='${payerInfo.orderAmt}' minFractionDigits='2'/>
							</c:otherwise>
						</c:choose>
					</td>
					<%-- <td><fmt:formatNumber type='number' value='${payerInfo.commissionAmt}' minFractionDigits='2'/></td> --%>
					<td>
						<c:choose>
							<c:when test="${empty payerInfo.marketCommissionAmt}">-</c:when>
							<c:otherwise>
								<fmt:formatNumber type='number' value='${payerInfo.marketCommissionAmt}' minFractionDigits='2'/>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${empty payerInfo.platCommissionAmt}">-</c:when>
							<c:otherwise>
								<fmt:formatNumber type='number' value='${payerInfo.platCommissionAmt}' minFractionDigits='2'/>
							</c:otherwise>
						</c:choose>
					</td>
					<td><fmt:formatNumber type='number' value='${payerInfo.actualAmt}' minFractionDigits='2'/></td>
					<td>
						<%-- <c:if test="${payerInfo.clearStatus }"></c:if> --%>
						<c:choose>
							<c:when test="${payerInfo.clearStatus == '2'}">清分成功</c:when>
							<c:when test="${payerInfo.clearStatus == '0'}">不需清分</c:when>
							<c:when test="${payerInfo.clearStatus == '1'}">待清分</c:when>
						</c:choose>
					</td>
			      </tr>
			   </c:forEach> 
		   </c:if>   
        </tbody>
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">谷登代收信息</div>
	<table class="dataintable browsersupport" align="center" style="margin-top: 15px">
       <tbody>
			<tr>
				<th width="200px">代收单位</th>
				<th width="200px">代收时间</th>
				<th width="100px">代收方式</th>
				<th width="100px">代收账号</th>
				<th width="100px">代收金额</th>
				<th width="150px">谷登代收支付手续费</th>
				<th width="100px">代收净额</th>
			</tr>
			<c:if test="${!empty valleyInfoList}">
				<c:forEach var="valleyInfo" items="${valleyInfoList}">  
				     <tr>
						<td>${valleyInfo.realName}</td>
						<td><fmt:formatDate value="${valleyInfo.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>
						    <c:choose>
								<c:when test="${valleyInfo.payType == 'ALIPAY_H5'}">支付宝</c:when>
								<c:when test="${valleyInfo.payType == 'WEIXIN_APP'}">微信</c:when>
								<c:when test="${valleyInfo.payType == 'PINAN'}">平安银行</c:when>
								<c:when test="${valleyInfo.payType == 'ENONG'}">E农</c:when>
								<c:when test="${valleyInfo.payType == 'NNCCB'}">南宁建行</c:when>
								<c:when test="${valleyInfo.payType == 'GXRCB'}">广西农信</c:when>
								<c:when test="${valleyInfo.payType == 'WANGPOS'}">旺POS</c:when>
						   </c:choose>
						</td>
						<td>${valleyInfo.gdBankCardNo}</td>
						<td><fmt:formatNumber type='number' value='${valleyInfo.payAmt}' minFractionDigits='2'/></td>
						<td><fmt:formatNumber type='number' value='${valleyInfo.feeAmt}' minFractionDigits='2'/></td>
						<td><fmt:formatNumber type='number' value='${valleyInfo.receivableAmt}' minFractionDigits='2'/></td>
				      </tr>
				</c:forEach>
			</c:if>
        </tbody>
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">收款方信息</div>
	<table class="dataintable browsersupport" align="center" style="margin-top: 15px">
       <tbody>
			<tr>
				<th width="">收款方账号</th>
				<th width="">收款方手机号</th>
				<th width="">商铺ID</th>
				<th width="">商铺名称</th>
				<th width="">所属市场</th>
				<th width="">订单号</th>
				<th width="">商品总金额</th>
				<th width="">谷登代收支付手续费</th>
				<!-- <th width="">佣金支出</th> -->
				<th width="">市场佣金</th>
				<th width="">平台佣金</th>
				<th width="">刷卡补贴</th>
				<th width="">应收金额</th>
			</tr>
			<c:if test="${!empty beneficiaryInfoList}">
			   <c:forEach var="beneficiaryInfo" items="${beneficiaryInfoList}">  
				    <tr>
						<td>${beneficiaryInfo.payeeAccount}</td>
						<td>${beneficiaryInfo.payeeMobile}</td>
						<td>${beneficiaryInfo.businessId}</td>
						<td>${beneficiaryInfo.businessName}</td>
						<td>${beneficiaryInfo.marketName}</td>
						<td>${beneficiaryInfo.orderNo}</td>
						<td>
							<c:choose>
								<c:when test="${empty beneficiaryInfo.orderAmt}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber type='number' value='${beneficiaryInfo.orderAmt}' minFractionDigits='2'/>
								</c:otherwise>
							</c:choose>
						</td>
						<td><fmt:formatNumber type='number' value='${beneficiaryInfo.feeAmt}' minFractionDigits='2'/></td>
						<%-- <td><fmt:formatNumber type='number' value='${beneficiaryInfo.commissionAmt}' minFractionDigits='2'/></td> --%>
						<td>
							<c:choose>
								<c:when test="${empty beneficiaryInfo.marketCommissionAmt}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber type='number' value='${beneficiaryInfo.marketCommissionAmt}' minFractionDigits='2'/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty beneficiaryInfo.platCommissionAmt}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber type='number' value='${beneficiaryInfo.platCommissionAmt}' minFractionDigits='2'/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty beneficiaryInfo.subsidyAmt}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber type='number' value='${beneficiaryInfo.subsidyAmt}' minFractionDigits='2'/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty beneficiaryInfo.receivableAmt}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber type='number' value='${beneficiaryInfo.receivableAmt}' minFractionDigits='2'/>
								</c:otherwise>
							</c:choose>
						</td>
				    </tr>
			    </c:forEach>
		    </c:if>
        </tbody>
	</table>
</div>
<div style="margin:0px 5px">
	<div style="padding:15px 0px 5px 8px;border-bottom:1px solid #ccc;">市场方信息</div>
	<table class="dataintable browsersupport" align="center" style="margin-top: 15px">
       <tbody>
			<tr>
				<th width="200px">市场账号</th>
				<th width="200px">市场手机号</th>
				<th width="100px">市场名称</th>
				<th width="100px">订单号</th>
				<th width="100px">商品总金额</th>
				<th width="150px">佣金收益</th>
				<th width="100px">应收金额</th>
			</tr>
			<c:if test="${!empty marketInfoList}">
			    <c:forEach var="marketInfo" items="${marketInfoList}"> 
				    <tr>
						<td>${marketInfo.account}</td>
						<td>${marketInfo.mobile}</td>
						<td>${marketInfo.marketName}</td>
						<td>${marketInfo.orderNo}</td>
						<td><fmt:formatNumber type='number' value='${marketInfo.orderAmt}' minFractionDigits='2'/></td>
						<td><fmt:formatNumber type='number' value='${marketInfo.commissionAmt}' minFractionDigits='2'/></td>
						<td><fmt:formatNumber type='number' value='${marketInfo.receivableAmt}' minFractionDigits='2'/></td>
				    </tr>
			    </c:forEach>
		    </c:if>
        </tbody>
	</table>
</div>