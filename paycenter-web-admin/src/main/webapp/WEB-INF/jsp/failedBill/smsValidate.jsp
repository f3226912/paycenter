<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
/* .confirm_cn{height:230px!important;} */
.dialog-button .l-btn{
    margin-left: 5px;
    border-radius: 2px;
    width: 100px;
    }
/* #smsValidateDialog{ height:aout!important;} */
/* .window{height:230px;} */

.dialog-content
#editSmsValidateDetail{ margin-top:20px;}
.obtain{ width:100px;height:31px;border:none; background:none;  margin-left:10px; border-radius:2px; text-decoration:none; color:#fff; text-align:center; line-height:31px; font-size:12px; background:#f98425; display:inline-block;}
</style>
<table id="smsValidatedg" align="center"></table>
	<div style="margin: 0px 5px">
		<div style="padding: 15px 0px 5px 8px; text-align:center; font-size:16px; border-bottom: 1px solid #ccc;">验证管理员手机号码</div>
		<form id="form1" method="post">
		<input type="hidden" id="thirdPayNumber" value="${thirdPayNumber}"></input>
		<input type="hidden" id="mobile" value="${mobile}"></input>
		<input type="hidden" id="resultType" value="${resultType}"></input>
		<table class="beneficiaryDetail" align="center" width="100%" style="padding: 20px 0 0 90px;">
			<tr>
				<td width="25%"><input style="width:180px; padding-left:10px; height:28px; border-radius:2px; border:solid #cecece 1px; margin-bottom:10px;" type="text" id="vmobile" name="vmobile" value="${vmobile}" disabled="true"/></td>
			</tr>
			<tr>
				<td width="25%"><input style="width:70px; padding-left:10px; height:28px; border-radius:2px; border:solid #cecece 1px;" type="text" id="verifyCode" name="verifyCode"/>
				<input type="button" class="obtain" onclick="generateVerifyCode(this);" value="获取手机验证码"/></td>
			</tr>
		</table>
		</form>
	</div>
	
<script type="text/javascript" src="${CONTEXT}../../js/failedBill/verify.js"></script>

