<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String BASE_PATH = "/";
    String path = request.getContextPath();
    String CONTEXT = request.getScheme() + "://"
        + request.getServerName() + ":" + request.getServerPort()
        + path + "/";
	request.setAttribute("CONTEXT",CONTEXT);
%>
<!DOCTYPE html>
<html class="columnsCss">
<head>
	<meta charset="utf-8">
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<link href="${CONTEXT }css/login/global.css" rel="stylesheet">
	<link href="${CONTEXT }css/login/layout.css" rel="stylesheet">
	<link href="${CONTEXT }css/login/login.css" rel="stylesheet">
	<link href="${CONTEXT }css/login/style.css" rel="stylesheet" />
	<link href="${CONTEXT }css/login/perspectiveRules.css" rel="stylesheet" />
	<link href="${CONTEXT }images/favicon.ico" type="image/x-icon" rel="icon" /> 
	<script type="text/javascript" src="${CONTEXT}js/jquery.min.js"></script> 
	<script type="text/javascript" src="${CONTEXT }js/login/login.js"></script> 
	<script src="${CONTEXT }js/lhgdialog/lhgdialog.min.js?skin=dblue"></script>
	<!--  <script src="${CONTEXT }js/login/jquery.logosDistort.min.js"></script> -->
	<script src="${CONTEXT }js/login/jquery.particleground.min.js"></script> 
	<title>谷登支付中心系统登录</title>
	<style>
	.header .logo {
	    top: 30px;
	    width:370px;
	    position: fixed;
	    margin-left: -146px;
	    left: 48%;
	}
	.p_02, .p_03 {
	    width: 560px;
	    height: 352px;
    	margin: -180px 0 0 -280px;
    }
    .p_02{
    	left:51%;
    	top:52%;
    }
	.login_head {
    	background-color: #94C315;
    }
    .btn_div {
    	height: 96px;
    	text-align: center;
    	margin-top: -30px;
    }
    .input_div{
    	margin-left: 68px;
    	   
    }
	.input_div input {
		border:none;
	    float: left;
	    width: 250px;
	    margin-left: 37px;
	    background-color: #f5f5f6;
	    border-radius: 5px;
    	color: #333;
    	padding-left: 75px;
	}
	input.greenBtn {
    	background-color: #94C315;
    	border-color: #94C315;
    	width: 340px;
    	margin-left: -9px;
	}
	input.greenBtn:hover{
    	background-color: #6383d8;
    	border-color: #6383d8;
	}
	input.greenBtn {
    	background-color: #6383d8;
    	border-color: #6383d8;
	}
	.welcome{
		font-size:20px;
		text-align: center;
    	margin-left: -10px;
    	margin-bottom: 40px;
    	line-height:30px;
    	font-family: "Microsoft Yahei";  
	}
	.form_div{
		top:0%;
	}
	
	#error{
	margin-top: -8px;
    margin-left: -49px;
	}
	#login{
/* 		background-image: url('${CONTEXT }images/background.jpg'); */
/* 		height: 100%; */
	}
.obtain{ 
	width:136px;
	height:44px; 
	margin-left:5px; 
	border-radius:3px; 
	text-decoration:none; 
	color:#fff!important; 
	text-align:center; 
	line-height:44px; 
	font-size:13px; 
	/* background:#f98425;  */
/* 	display:inline-block; */
    border:none;
    outline:none;
	background: linear-gradient(to top, #ff7004 30%,#fbb870 100%);
	}
	</style>
</head>
<body>
<div id="login" style="text-align:left;"> 
<img id="loginBg" alt="background" src="${CONTEXT }images/background.png" style="width: 100%;"/>
  <!-- <div id="particle-target" ></div>-->
</div>
<!--header-->
<div class="header">
  <div class="headerNr">
    <div class="logo"><a href="#"></a></div>
  </div>
</div>
<!--end header-->
<form action="${CONTEXT }sys/login" method="post" id="loginForm">
<input type="text" style="display: none;"/>
<input type="password" style="display: none;"/>
<div class="content">
  <!-- <div class="div_bg"></div> -->
  <div class="p_02" style="display:block; width:554px;height:407px;">
    <!--  <div class="login_head"><span class="hf_left">谷登运营后台登录</span></div>-->
    <div class="div_bg01" style="border-radius: 10px; height:100%;top:0%;"></div>
    <div class="form_div">
      <div class="welcome p_04">支付中心管理后台 </div>
      
      <div class="input_div"> <span style="position: absolute; left: 37px; top: 0; z-index: 2;">
      <img id="loginBg" alt="background" src="${CONTEXT }images/ico1_gdeng.png" /></span>
        <input type="text" id="userCode" class="logi_ico1" name="userCode" value="" placeholder="请输入用户名" min="6" maxlength="20"/>
      </div>
      <div class="input_div" style="margin-top: -15px;">
      <span style="position: absolute; left: 37px; top: 0; z-index: 2;">
      <img id="loginBg" alt="background" src="${CONTEXT }images/ico2_gdeng.png" /></span>
        <input type="password" id="userPassword" name="userPassword" value="" placeholder="请输入密码" min="6" maxlength="20" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" style="ime-mode:disabled"/>
      </div>
      <div style="margin-bottom: 40px; margin-top:-15px; float:left; margin-left:105px;">
         <input style="width:195px; background:#f7f7f7; padding-left:10px; height:44px; border-radius:5px; border:solid #cecece 1px;" type="text" id="verifyCode" name="verifyCode"/>
         <input type="button" class="obtain" onclick="generateVerifyCode(this);" value="获取手机验证码"/>
      </div>
      <div class="btn_div">
        <input type="button" class="greenBtn" value="登录" onclick="loginFun()" /> 
        <div style="color:#FFE274" id="error">${error }</div>
        <!-- <a href="#" onclick="fogetPwd();">忘记密码</a> --> 
       </div>
    </div>
  </div>
</div>
</form>
<!--[if lte IE 8]>
<script type="text/javascript" src="${CONTEXT }js/jquery.min.js"></script> 
<![endif]-->
<script>
    var particles = true,
        particleDensity,
        options = {
            effectWeight: 1,
            outerBuffer: 1.08,
            elementDepth: 220
        };

    $(document).ready(function() {
    	
    	$("#userCode").focus();
    	
    	if (window != top){
    		top.location.href = location.href; 
    	}

        $("#login").logosDistort(options);

        if (particles) {
            particleDensity = window.outerWidth * 20;
            if (particleDensity < 10000) {
                particleDensity = 10000;
            } else if (particleDensity > 50000) {
                particleDensity = 50000;
            }
            return $('#particle-target').particleground({
				parallaxMultiplier: 6,
				particleRadius: 12,	  
				proximity: 160,
				lineWidth: 1,
				curvedLines: false,
				directionX: 'center',
				directionY: 'down',
				minSpeedX: 0.1,
				maxSpeedX: 0.7,
				minSpeedY: 0.1,
				maxSpeedY: 0.7,
//                 dotColor: 'rgba(179,223,168,0.6)',
//                 lineColor: 'rgba(179,223,168,0.4)',
                dotColor: 'rgba(255, 255, 255, 0.7)',
                lineColor: 'rgba(255, 255, 255, 0.5)',
                density: particleDensity.toFixed(0),
                parallax: true
            });
        }

    });
    
    //验证
    function loginFun(){
    	if($("#userCode").val()==""){
    		$("#error").html("请输入用户名");
    		return false;
    	}else if($("#userPassword").val()==""){
    		$("#error").html("请输入登录密码");
    		return false;
    	}
    	if(validateUser()){
	    	$("#loginForm").submit();
    	}
    }
    function fogetPwd(){
    	$.dialog({
    		title:"忘记密码！",
    		width:640,
    		height: 220,
    		fixed: true,
    		max: false,
    		min: false,
    		resize: false,
    		drag: false,
    		lock: true,
    		top: '48%',
    		content: '<p class="fs16">密码忘记了，需要进行密码重置操作。请咨询....</p>',
    		ok: function(){
    		}
    	});
    	return false;
    }   
    
    function validateUser(){
		var userCode=$.trim($('#userCode').val());
		var len=userCode.replace(/[\u2E80-\u9FFF]/g,"aa").length;
		if(len>=6&&/^[0-9]/g.test(userCode)){
			$("#error").html("用户名不能以数字开头");
			return false;
		}
		if(len<6||len>20||!/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/g.test(userCode)){
			$("#error").html("用户名只能6-20个字符");
			return false;
		} 
		var password=$.trim($("#userPassword").val());
		var len2=password.length;
		if(len2==0){
			$("#error").html("请重新输入密码");
			return false;
		}
		if(len2<6||len2>20){
			$("#error").html("密码长度不正确，只能6-20个字符");
			return false;
		}
		if(len2>=6&&/^\s+$/g.test(password)){
			$("#error").html("密码不能包含空格");
			return false;
		}
		return true;
	}
    
   document.onkeydown = function(evt){
	   var evt = window.event?window.event:evt;
	   if (evt.keyCode==13) {
		   loginFun();
	    }
   }
   
 		
	function generateVerifyCode(obj) {
		var userCode = $('#userCode').val();//电话
		var param = {userCode:userCode};
		jQuery.post( '${CONTEXT }' + "sys/generateVerifyCode", param,
				function(data) {
					/* slideMessage(data.msg); */
					settime(obj);
				});
	}

	var countdown = 60;
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
</script>
</body>
</html>