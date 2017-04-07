<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
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
<link rel="stylesheet" type="text/css" href="${path}css/global.css" />
<link rel="stylesheet" type="text/css" href="${path}css/style.css" />
<style type="text/css">
	.mark {
	    position: fixed;
	    z-index: 998;
	    left: 0;
	    top: 0;
	    width: 100%;
	    height: 100%;
	    background: #000;
	    opacity: .1;
	}
	.markAll img{width:32px;height:32px;position: absolute;left:50%;top:50%;margin-left:-16px;margin-top:-16px;}
</style>
<script src="${path}js/jquery-1.8.3.min.js" type="text/javascript"
	charset="utf-8"></script>
<script src="${path}js/jquery.mobile.custom.min.js"
	type="text/javascript" charset="utf-8"></script>
</head>
<body>
	<div class="markAll">
		<div class="mark"></div>
		<img src="${path}images/loading.gif"/>
	</div>
	<div class="pay-index">
		<div class="payHeader">
			<div class="header">支付金额:</div>
			<div class="headerMoney">
				<span><fmt:formatNumber type="number" pattern="0.00">${sumPayAmt}</fmt:formatNumber></span>元
			</div>
			<div class="payHeaderLine"></div>
		</div>
		<div class="pay-remind">请选择支付方式</div>
		<ul class="choice-pay">
			<c:forEach items="${payTypeList}" var="payWay">
				<li style="border: none;"><img class="logo"
					src="${path}${payWay.image}" />
					<div class="con">
						<p class="big">${payWay.payTypeName}</p>
						<p>${payWay.bak}</p>
					</div> <img class="choice-logo" id="${payWay.payType}" src="" value="" />
				</li>
			</c:forEach>
		</ul>

		<div class="footer">
			<div class="bottom" ontouchstart="clickPay()">立即支付</div>
		</div>
	</div>

	<form action="" id="gwForm" method="post" name="gwForm">
		<input type="hidden" name="version" value="${version}" /> 
		<input type="hidden" name="appKey" value="${appKey}" /> 
		<input type="hidden" name="orderNo" value="${orderNo}" /> 
		<input type="hidden" name="timeOut" value="${timeOut}" /> 
		<input type="hidden" name="title" value="${title}" /> 
		<input type="hidden" name="keyType"	value="${keyType}" />
		<input type="hidden" name="orderTime"	value="${orderTime}" /> 
		<input type="hidden" name="payerUserId"	value="${payerUserId}" />
		<input type="hidden" name="payerAccount" value="${payerAccount}" />
		<input type="hidden" name="payerName"	value="${payerName}" />
		<input type="hidden" name="payeeUserId"	value="${payeeUserId}" />
		<input type="hidden" name="payeeAccount"	value="${payeeAccount}" />
		<input type="hidden" name="payeeName"	value="${payeeName}" />		
		<input type="hidden" name="totalAmt"	value="${totalAmt}" />
		<input type="hidden" name="payAmt"	value="${payAmt}" /> 
		<input type="hidden" name="notifyUrl"	value="${notifyUrl}" /> 
		<input type="hidden" name="returnUrl"	value="${returnUrl}" />
		<input type="hidden" name="reParam"	 value="${reParam}" />
		<input type="hidden" name="requestIp" value="${requestIp}" />
		<input type="hidden" name="payerMobile" value="${payerMobile}" />
		<input type="hidden" name="payeeMobile" value="${payeeMobile}" />
		<input type="hidden" name="requestNo" value="${requestNo}" />
		<input type="hidden" name="payCount" value="${payCount}" />
		<input type="hidden" name="logisticsUserId" value="${logisticsUserId}" />
		<input type="hidden" name="sumPayAmt" value="${sumPayAmt}" />
		<input type="hidden" name="orderInfos" value='${orderInfos}' />
	</form>
</body>
<script>
		var path="<%=path%>";
</script>
<script src="${path}js/global.js" type="text/javascript" charset="utf-8"></script>
<script src="${path}js/nst-pay.js" type="text/javascript"	charset="utf-8"></script>
</html>
