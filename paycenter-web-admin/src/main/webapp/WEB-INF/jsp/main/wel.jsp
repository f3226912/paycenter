<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../pub/constants.inc" %>
<%@ include file="../pub/tags.inc" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>老黄历特效 </title>
		<style type="text/css">
		.tips-ind{margin-top:15px;padding:10px;}
		.tips-ind p{font: 16px/30px "Microsoft YaHei";}
		
		</style>
	</head>
	<body>
		<div class="container">
<!-- 			<div class="title"> -->
<!-- 				程序员老黄历<sup></sup> -->
<!-- 			</div> -->
			<!--  <div class="date">
			</div>-->
			<div class="tips-ind">
				<p>各位领导，经过韩总、陶总对谷登业务管理后台账号、权限管理流程的确认，平台账号管理将做如下调整：</p>
				<p>1，现有全部谷登业务管理后台用户账号权限将会被禁用，经过下面申请流程后将会被重新启用、进行重新授权；</p>
				<p>2，用户中心、订单中心、促销管理（即补贴管理）三个模块默认不对任何账户开放，有需要权限的同事需要由部门总监邮件发起申请、抄送 人力资源陶总，经韩总审批后，由运维经理孙永明负责执行授权操作；</p>
				<p>3，其他模块的权限，由各部门总监审批，抄送人力资源陶总，运维经理孙永明负责执行授权操作；</p>
				<p>4，在权限调整期间，运维孙永明经理将会与各位领导进行沟通，详细确认各部门账号、权限的调整方案。</p>
			</div>
			<div class="good">
				<div class="title">
					<table>
						<tr><td>宜</td></tr>
					</table>
				</div>
				<div class="content">
					<ul></ul>
				</div>
				<div class="clear"></div>
			</div>
			<div class="split"></div>
			<div class="bad">
				<div class="title">
					<table>
						<tr><td>不宜</td></tr>
					</table>
				</div>
				<div class="content">
					<ul></ul>
				</div>
				<div class="clear"></div>
			</div>
			<div class="split"></div>
			<div class="line-tip">
				<strong>座位朝向：</strong>面向<span class="direction_value"></span>写程序，BUG 最少。
			</div>
			<div class="line-tip">
				<strong>今日宜饮：</strong><span class="drink_value"></span>
			</div>
			<div class="line-tip">
				<strong>女神亲近指数：</strong><span class="goddes_value"></span>
			</div>
			
			<div class="comment">
				<ul>
<!-- 					<li>本老黄历尚处于beta阶段，作者随时会修改，所以如果上午看到的内容跟下午不同，请勿惊慌；</li> -->
<!-- 					<li>本老黄历仅面向程序员；</li> -->
<!-- 					<li>本老黄历内容是程序生成的，因为只有这样程序员才会信。</li> -->
				</ul>
			</div>
		</div>
	</body>
</html>
    	<style>body * {
	font-family:"Consolas","Microsoft Yahei", Arial, sans-serif;
}

body {
	background: white;
	margin: 0;
	padding: 0;
}

.container {
/* 	width: 320px; */
/* 	margin: 0 auto 50px; */
	height: 100%;
	width: 100%;
}

.container>.title {
	color: #bbb;
	font-weight: bold;
	margin-bottom: 10px;
	background: #555;
	padding: 5px 15px;
}

.date{
	font-size:17pt;
	font-weight: bold;
	line-height: 30pt;
	text-align: center;
	padding-top:10px;
}

.split, .clear {
	clear: both;
	height: 1px;
	overflow-y: hidden;
}

.good, .bad {
	clear: both;
	position: relative;
}

.bad {
	top: -1px;
}

.good .title, .bad .title {
	float: left;
	width: 100px;
	font-weight: bold;
	text-align: center; 
	font-size: 30pt;
	position:absolute;
	top:0;
	bottom:0;
}

.good .title>table, .bad .title>table {
	position:absolute;
	width:100%;
	height:100%;
	border:none;
}

.good .title {
	background: #ffee44;
}

.bad .title {
	background: #ff4444;
	color: #fff;
}

.good .content, .bad .content {
	margin-left: 115px; 
	padding-right: 10px;
	padding-top: 1px;
	font-size:15pt;
}

.good {
	background: #ffffaa;
}

.bad {
	background: #ffddd3;
}

.content ul {
	list-style: none;
	margin:10px 0 0;
	padding:0;
}

.content ul li {
	line-height:150%;
	font-size: 15pt;
	font-weight: bold;
	color: #444;
}

.content ul li div.description {
	font-size: 10pt;
	font-weight: normal;
	color: #777;
	line-height: 110%;
	margin-bottom: 10px;
}

.line-tip {
	font-size: 11pt;
	margin-top: 10px;
	margin-left: 10px;
}

.direction_value {
	color:#4a4;
	font-weight: bold;
}

.comment {
	margin-top: 50px;
	font-size: 11pt;
	margin-left: 10px;
}

.comment ul {
	margin-left: 0;
	padding-left: 20px;
	color: #999;
}</style>
	    		<script>//

/*
 * 注意：本程序中的“随机”都是伪随机概念，以当前的天为种子。
 */
function random(dayseed, indexseed) {
	var n = dayseed % 11117;
	for (var i = 0; i < 100 + indexseed; i++) {
		n = n * n;
		n = n % 11117;   // 11117 是个质数
	}
	return n;
}

var today = new Date();
var iday = today.getFullYear() * 10000 + (today.getMonth() + 1) * 100 + today.getDate();

var weeks = ["日","一","二","三","四","五","六"];
var directions = ["北方","东北方","东方","东南方","南方","西南方","西方","西北方"];
var activities = [
	{name:"写单元测试", good:"写单元测试将减少出错",bad:"写单元测试会降低你的开发效率"},
	{name:"洗澡", good:"你几天没洗澡了？",bad:"会把设计方面的灵感洗掉"},
	{name:"锻炼一下身体", good:"",bad:"能量没消耗多少，吃得却更多"},
	{name:"抽烟", good:"抽烟有利于提神，增加思维敏捷",bad:"除非你活够了，死得早点没关系"},
	{name:"白天上线", good:"今天白天上线是安全的",bad:"可能导致灾难性后果"},
	{name:"重构", good:"代码质量得到提高",bad:"你很有可能会陷入泥潭"},
	{name:"使用%t", good:"你看起来更有品位",bad:"别人会觉得你在装逼"},
	{name:"跳槽", good:"该放手时就放手",bad:"鉴于当前的经济形势，你的下一份工作未必比现在强"},
	{name:"招人", good:"你遇到千里马的可能性大大增加",bad:"你只会招到一两个混饭吃的外行"},
	{name:"面试", good:"面试官今天心情很好",bad:"面试官不爽，会拿你出气"},
	{name:"提交辞职申请", good:"公司找到了一个比你更能干更便宜的家伙，巴不得你赶快滚蛋",bad:"鉴于当前的经济形势，你的下一份工作未必比现在强"},
	{name:"申请加薪", good:"老板今天心情很好",bad:"公司正在考虑裁员"},
	{name:"晚上加班", good:"晚上是程序员精神最好的时候",bad:""},
	{name:"在妹子面前吹牛", good:"改善你矮穷挫的形象",bad:"会被识破"},
	//{name:"撸管", good:"避免缓冲区溢出",bad:"小撸怡情，大撸伤身，强撸灰飞烟灭"},
	//{name:"浏览成人网站", good:"重拾对生活的信心",bad:"你会心神不宁"},
	{name:"命名变量\"%v\"", good:"",bad:""},
	{name:"写超过%l行的方法", good:"你的代码组织的很好，长一点没关系",bad:"你的代码将混乱不堪，你自己都看不懂"},
	{name:"提交代码", good:"遇到冲突的几率是最低的",bad:"你遇到的一大堆冲突会让你觉得自己是不是时间穿越了"},
	{name:"代码复审", good:"发现重要问题的几率大大增加",bad:"你什么问题都发现不了，白白浪费时间"},
	{name:"开会", good:"写代码之余放松一下打个盹，有益健康",bad:"你会被扣屎盆子背黑锅"},
	{name:"打DOTA", good:"你将有如神助",bad:"你会被虐的很惨"},
	{name:"晚上上线", good:"晚上是程序员精神最好的时候",bad:"你白天已经筋疲力尽了"},
	{name:"修复BUG", good:"你今天对BUG的嗅觉大大提高",bad:"新产生的BUG将比修复的更多"},
	{name:"设计评审", good:"设计评审会议将变成头脑风暴",bad:"人人筋疲力尽，评审就这么过了"},
	{name:"需求评审", good:"",bad:""},
	{name:"上微博", good:"今天发生的事不能错过",bad:"会被老板看到"},
	{name:"上AB站", good:"还需要理由吗？",bad:"会被老板看到"}
];

var specials = [
	{date:20130221, type:'good', name:'防核演习', description:'万一哪个疯子丢颗核弹过来...'}
];

var tools = ["Eclipse写程序", "MSOffice写文档", "记事本写程序", "Windows8", "Linux", "MacOS", "IE", "Android设备", "iOS设备"];

var varNames = ["jieguo", "huodong", "pay", "expire", "zhangdan", "every", "free", "i1", "a", "virtual", "ad", "spider", "mima", "pass", "ui"];

var drinks = ["水","茶","红茶","绿茶","咖啡","奶茶","可乐","牛奶","豆奶","果汁","果味汽水","苏打水","运动饮料","酸奶","酒"];

function getTodayString() {
	return "" + today.getFullYear() + "年" + (today.getMonth() + 1) + "月" + today.getDate() + "日 星期" + weeks[today.getDay()];
}

// 生成今日运势
function pickTodaysLuck() {
	var numGood = random(iday, 98) % 3 + 2;
	var numBad = random(iday, 87) % 3 + 2;
	var eventArr = pickRandomActivity(numGood + numBad);
	
	var specialSize = pickSpecials();
	
	for (var i = 0; i < numGood; i++) {
		addToGood(eventArr[i]);
	}
	
	for (var i = 0; i < numBad; i++) {
		addToBad(eventArr[numGood + i]);
	}
	
	
}

// 添加预定义事件
function pickSpecials() {
	var specialSize = [0,0];
	
	for (var i = 0; i < specials.length; i++) {
		var special = specials[i];
		
		if (iday == special.date) {
			if (special.type == 'good') {
				specialSize[0]++;
				addToGood({name: special.name, good: special.description});
			} else {
				specialSize[1]++;
				addToBad({name: special.name, bad: special.description});
			}
		}
	}
	
	return specialSize;
}

// 从 activities 中随机挑选 size 个
function pickRandomActivity(size) {
	var picked_events = pickRandom(activities, size);
	
	for (var i = 0; i < picked_events.length; i++) {
		picked_events[i] = parse(picked_events[i]);
	}
	
	return picked_events;
}

// 从数组中随机挑选 size 个
function pickRandom(array, size) {
	var result = [];
	
	for (var i = 0; i < array.length; i++) {
		result.push(array[i]);
	}
	
	for (var j = 0; j < array.length - size; j++) {
		var index = random(iday, j) % result.length;
		result.splice(index, 1);
	}
	
	return result;
}

// 解析占位符并替换成随机内容
function parse(event) {
	var result = {name: event.name, good: event.good, bad: event.bad};  // clone
	
	if (result.name.indexOf('%v') != -1) {
		result.name = result.name.replace('%v', varNames[random(iday, 12) % varNames.length]);
	}
	
	if (result.name.indexOf('%t') != -1) {
		result.name = result.name.replace('%t', tools[random(iday, 11) % tools.length]);
	}
	
	if (result.name.indexOf('%l') != -1) {
		result.name = result.name.replace('%l', (random(iday, 12) % 247 + 30).toString());
	}
	
	return result;
}

// 添加到“宜”
function addToGood(event) {
	$('.good .content ul').append('<li><div class="name">' + event.name + '</div><div class="description">' + event.good + '</div></li>');
}

// 添加到“不宜”
function addToBad(event) {
	$('.bad .content ul').append('<li><div class="name">' + event.name + '</div><div class="description">' + event.bad + '</div></li>');
}

$(function(){
	$('.date').html(getTodayString());
	$('.direction_value').html(directions[random(iday, 2) % directions.length]);
	$('.drink_value').html(pickRandom(drinks,2).join());
	$('.goddes_value').html(random(iday, 6) % 50 / 10.0);
	pickTodaysLuck();
});</script>
	
<!-- Generated by RunJS (Wed Mar 13 14:42:09 CST 2013) 0ms -->
<div style="text-align:center;clear:both">
<!-- <p>适用浏览器：IE8、360、FireFox、Chrome、Opera、傲游、搜狗、世界之窗. 不支持Safari浏览器。</p> -->
<!-- <p>来源：<a href="http://runjs.cn/" target="_blank">runjs</a></p> -->
</div>