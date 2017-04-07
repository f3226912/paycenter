/**
 * 文件、图片上传js
 * @since 1.0
 * @author tanjun
 * */
//上传类型
var _type="";
//已上传的个数
var total=0;
//记录截图上传的下标
var _uploadIndex=0;
//图标个数
var iconLimitCount=0;
//上传返回的所有图片路径
//var obj=null;

(function($) {
	//参数设置
	_setting={
		id		   :		'uploadPic',	//定义按钮的id，默认为uploadPic
		title	   :		'图片上传',		//定义弹出窗口的title,默认值为图片上传
		name	   :		'imageList',	//定义返回页面接收的path隐藏域的name值
		url		   :		'',				//定义远程访问的url
		type	   :		'image',		//定义上传类型,默认为图片
		listable   :		true,	 		//定义是否显示图片列表,默认显示
		strategy   :		false,			//定义是否显示图片策略，默认不显示
		remove	   :		false,			//定义是否可以删除，默认不可以删除
		cate	   :		false,			//定义是否显示附件分类，默认不显示
		close	   :		true,			//定义上传完后是否关闭窗口，默认关闭
		closetime  :		2000,			//定义上传后关闭窗口的时间，默认2秒
		fileSpan   :		'',				//定义上传后的文件显示列表前面的名称
		uploadLimit:		1,				//定义允许上传的数量,默认为1个
		handler	   :		true,			//定义是否启用回调，默认启用
		upSuccess  : 		function(obj){},//上传成功后的回调函数
		systemFlag :		'',				//区分不同系统的策略标识，需要配合附件管理系统配置使用
		otherFlag  :		'',				//调用哪些策略，多策略以“,”分开,需要配合附件管理系统配置使用
		checked	   :		'',				//当otherFlag有值时才有效，图片策略是否全部勾选（默认为不勾选），当checked =yes则全部勾选，不传值则不勾选
		mustChecked:		''				//策略是否必选（默认为不必选），当mustChecked =yes则必须选择一个，不传值则不必选。
	};

	$.fn.uploadfile = function(uSetting) {
		var setting = _setting;
		$.extend(true, setting, uSetting);
	};
	
})(jQuery);

$(function(){
  //最大可以上传的截图个数
  uploadLimit=_setting.uploadLimit;
  //初始化页面
  initPage();
  //初始化跨域消息传递js
  var messenger=Messenger.initInParent(document.getElementById('uploadIframe'));
  //接收跨域回传的值
  messenger.onmessage = function (data) {
	  var json=eval("("+data+")");
	  if(json.length>0){
		  var paths="";
		  var fileNames="";
		  for(var i=0;i<json.length;i++){
			paths+=json[i].path+",";
			fileNames+=json[i].fileName+",";
		  }
		  if(_type=="image"){
			//图片列表
			var images="<li id=\"li"+_uploadIndex+"\" class=\"g-u\">"+
   					"<div class=\"pic\" style=\"float:right;\">"+
							"<div title=\"删除\" id='"+json[0].annexID+"' onclick=\"deleteImage('"+_uploadIndex+"')\" class=\"closeImgX\" ></div>"+
   						"<a href=\"javascript:showImage('"+json[0].fileUrl+"');\">"+
   							"<img id=\"img"+_uploadIndex+"\" class=\"preview-img\" title=\"查看原图\" src=\""+json[0].fileUrl+"\" path=\""+json[0].path+"\" style=\"display: inline;\" />"+
   						"</a>"+"<input type='hidden' id=\"url"+_uploadIndex+"\" name=\""+_setting.name+"\" value=\""+paths+"\">"+
   					"</div></li>";
			$("#imagesUl").append(images);
			//截图上传录记数加1
			_uploadIndex++;
			//记录上传成功的截图个数加1
			total++;
			if(total>=uploadLimit){
				total=uploadLimit;
				$("#"+_setting.id).attr("disabled","disabled");
			}
			//每上传一张截图，修改一次最大传图数量
			var num=uploadLimit-total;
			//修改参数
			_setting.uploadLimit=num;
			$("#maxCount").html(num);
		  }
		  //图标隐藏生成
		  if(_type=="icon"){
				var imgSrcHtml="";
				iconLimitCount=json.length-1;
				for(var i=1;i<json.length;i++){
					//图标展标
					imgSrcHtml=imgSrcHtml+"<a href=\"javascript:showImage('"+json[i].fileUrl+"');\" id=\"icon"+(i-1)+"Href\" style=\"display:inline;\" >"
								+"<img title=\"查看原图\" id=\"icon"+(i-1)+"Src\" src=\""+json[i].fileUrl+"\" width=\"35\" height=\"35\" style=\"padding-right:10px;border:none;\" /></a>";
				}
				$("#iconShowDiv").html(imgSrcHtml);
		  }
		 if(_type=="file"){
		 	//软件大小
		  	$("#appSize").val(json[0].fileSize);
			$("#appUploadUrl").val(json[0].path);
			//文件名
			$("#fileName").html(_setting.fileSpan+fileNames);
		 }
	 }
	  if(_setting.close){
		  setTimeout(closeDiv,_setting.closetime);
	  }
	  if(_setting.handler){
		 _setting.upSuccess.call(this,json);
	  }
  };
});

//上传
function upload(){
	_type=_setting.type;
	//弹出层title
	$(".titleText").html(_setting.title);
	if(_type=="image"||_setting.type=="icon"){
		$("#tip").hide();
	}
	//文件请求
	if(_type=="file"){
		$("#tip").show();
		$("#uploadIframe").attr("style","width:100%;height:390px;");
	}
	if(_setting.listable==false){
		$("#images").css("display","none");
	}
	var url =_setting.url+"?strategy="+_setting.strategy+"&uploadCount="+_setting.uploadLimit+
			"&remove="+_setting.remove+"&cate="+_setting.cate+"&systemFlag="+_setting.systemFlag+"&otherFlag="+_setting.otherFlag+
			"&mustChecked="+_setting.mustChecked+"&checked="+_setting.checked;
	$("#uploadIframe").attr("src",url);
	//弹出框显示位置
	var top = ($(window).height() - $("#upload_div").height())/2;     
	var left = ($(window).width() - $("#upload_div").width())/2; 
	var scrollTop = $(document).scrollTop();     
	var scrollLeft = $(document).scrollLeft();
	$("#upload_div").css( { position : 'absolute', 'top' : top + scrollTop, left : left + scrollLeft } ).show();    
	//添加遮罩
	addMask();
}

//初始化当前图片的个数
function initPage(){
	//已上传的图片个数
	total=$("#imagesUl li").length;
	if(total>=uploadLimit){
		total=uploadLimit;
		$("#"+_setting.id).attr("disabled","disabled");
    }
    var num=uploadLimit-total;
    //修改参数
	_setting.uploadLimit=num;
    $("#maxCount").html(num);
	//修改记录截图个数的下标
	_uploadIndex=total;
}

//删除截图
function deleteImage(index){
	$("#li"+index).remove();
	//每删除一张截图，修改一次最大传图数量
	total--;
	if(total==0){
		total=0;
	}
	$("#"+_setting.id).attr("disabled",false);
	var num=uploadLimit-total;
	//修改参数
	_setting.uploadLimit=num;
	//修改记录截图个数的下标
	_uploadIndex=total;
	$("#maxCount").html(num);
	$("#image"+index).val("");
	$("#image"+index+"Url").val("");
}

//关闭弹出框
function closeDiv(){
	$("#upload_div").hide();
	$("#uploadIframe").attr("src","");
	//去掉遮罩
	$("#Mask").remove();
}

//图片显示
function showImage(url){
	window.open(url,"_blank");
}

//遮罩层
function addMask() {
	var bodyH=$(document).height();
	$("body").append("<div id=\"Mask\" style=\"width:100%;height:"+bodyH+"px;background-color:#333333;opacity:0.6;filter:alpha(opacity=60);_background-image:url(../../images/transBg.gif);_background-repeat:repeat;_background-color:none;z-index:1001;position:absolute;left:0px;top:0px;overflow:hidden;\"></div>");
}