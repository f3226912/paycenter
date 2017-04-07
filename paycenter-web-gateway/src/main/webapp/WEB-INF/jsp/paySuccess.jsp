<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
	//String scheme = request.getHeader("X-Forwarded-Scheme"); //是否NGINX代理
	String path = "";
	String scheme = request.getHeader("X-Forwarded-Proto"); //阿里
	if(scheme != null && !"".equals(scheme)){
		path = scheme + "://" + request.getServerName()+ request.getContextPath() + "/";
	} 
	if(scheme == null || "".equals(scheme)){
		scheme = request.getHeader("X-Forwarded-Scheme"); //是否NGINX代理
		if(scheme != null && !"".equals(scheme)){
			path = scheme + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/";
		}
	}
	if(scheme == null || "".equals(scheme)){
		path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";
	}

	session.setAttribute("path", path);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta content="black" name="apple-mobile-web-app-status-bar-style">
		<meta name="format-detection" content="telephone=no">
		<meta http-equiv="x-rim-auto-match" content="none">
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">
		<title>支付</title>
		<link rel="stylesheet" type="text/css" href="${path}css/global.css"/>
		<link rel="stylesheet" type="text/css" href="${path}css/style.css"/>
		<script src="${path}js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${path}js/jquery.mobile.custom.min.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>		
		<div class="paySuccess">
			<img src="${path}images/successBig.png"/>
			<div class="con">支付成功</div>
			<ul>
				<li>支付金额:<fmt:formatNumber type="number" pattern="0.00">${payAmt}</fmt:formatNumber>元</li>
				<!--li>订单号:${orderNo}</li -->
			</ul>
			<!--div class="details" ontouchstart="viewOrder()">查看订单详情></div -->
			<style type="text/css">
				.back{
				    text-align: center;
				    width: 9rem;
				    height:2.5rem;
				    line-height:2.5rem;
				    margin:2rem auto;
				    border:1px solid #999999;
				    border-radius:5px;
				}
				.back a{
					font-size: 1.6rem;
					color:#666666;
				}
			</style>
			<div class="back" onclick="viewOrder()"><a href="###">返回</a></div>
		</div>
		<form action="" id="successForm" method="post" name="successForm">		
			<input type="hidden" name="version" value="${version}" />
			<input type="hidden" name="appKey" value="${appKey}" /> 
			<input type="hidden" name="orderNo" value="${orderNo}" /> 
			<input type="hidden" name="title" value="${title}" /> 
			<input type="hidden" name="payCenterNumber" value="${payCenterNumber}" /> 
			<input type="hidden" name="thirdPayNumber" value="${thirdPayNumber}" /> 
			<input type="hidden" name="payAmt" value="${payAmt}" /> 
			<input type="hidden" name="reParam" value="${reParam}" /> 
			<input type="hidden" name="payType" value="${payType}" /> 
			<input type="hidden" name="timeOut" value="${timeOut}" /> 
			<input type="hidden" name="sign" value="${sign}" /> 
			<input type="hidden" name="keyType" value="${keyType}" /> 
			<input type="hidden" name="payStatus" value="${payStatus}" /> 
			<input type="hidden" name="payerUserId" value="${payerUserId}" />
			<input type="hidden" name="payerAccount" value="${payerAccount}" />
			<input type="hidden" name="payerName" value="${payerName}" />
			<input type="hidden" name="payeeUserId" value="${payeeUserId}" />
			<input type="hidden" name="payeeAccount" value="${payeeAccount}" />
			<input type="hidden" name="payeeName" value="${payeeName}" />
			<input type="hidden" name="totalAmt" value="${totalAmt}" />	
			<input type="hidden" name="respCode" value="${respCode}" />
			<input type="hidden" name="respMsg" value="${respMsg}" />	
			<input type="hidden" name="returnUrl" id="returnUrl" value="${returnUrl}" />
			<input type="hidden" name="notifyUrl"	value="${notifyUrl}" /> 
			<input type="hidden" name="payerMobile" value="${payerMobile}" />
			<input type="hidden" name="payeeMobile" value="${payeeMobile}" />
			<input type="hidden" name="requestNo" value="${requestNo}" />
			<input type="hidden" name="logisticsUserId" value="${logisticsUserId}" />
			<input type="hidden" name="payCount" value="${payCount}" />
		</form>
		
	</body>
	<script>
		var path="<%=path%>";
	</script>
	<script src="${path}js/global.js" type="text/javascript" charset="utf-8"></script>
	<script src="${path}js/paysuccess.js" type="text/javascript"	charset="utf-8"></script>
</html>
