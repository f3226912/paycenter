/**com dialog**/
__DIALOG_WRAPPER__ = {};
/*todo IE6有个Bug，如果不给定对话框的宽度的话，在IE6下，对话框将以100%宽度显示 */
DialogManager = {
	'create' : function (id) {
		var d = {};
		if (!__DIALOG_WRAPPER__[id]) {
			d = new Dialog(id);
			__DIALOG_WRAPPER__[id] = d;
		} else {
			d = DialogManager.get(id);
		}
		return d;
	},
	'get' : function (id) {
		return __DIALOG_WRAPPER__[id];
	},
	'close' : function (id) {
        if(__DIALOG_WRAPPER__[id]!==null){
            if (__DIALOG_WRAPPER__[id].close()) {
                __DIALOG_WRAPPER__[id] = null;
            }
        }
	},
	'hide' : function (id) {
        if(__DIALOG_WRAPPER__[id]!==null){
            if (__DIALOG_WRAPPER__[id].hide()) {
                __DIALOG_WRAPPER__[id] = null;
            }
        }
	},
	'onClose' : function () {
		return true;
	},
	/* 加载对话框样式 */
	'loadStyle' : function () {
		var doc = document, id = 'getElementById', tag = 'getElementsByTagName';
		// var _dialog_js_path = $('#gduiJs').attr('src');
		// var _path = _dialog_js_path.split('/');
		// var _dialog_css = _path.slice(0, _path.length - 1).join('/') + '/skin/dialog.css';
		// $('#gduiJs').after('<link href="' + _dialog_css + '" rel="stylesheet" type="text/css" />');
		var skin = 'gduiDialog';
		DialogManager.loadStyle.dir = 'dir' in DialogManager.loadStyle ? DialogManager.loadStyle.dir : DialogManager.getpath + '/skin/dialog.css';
	    if(DialogManager.loadStyle.dir && !doc[id](skin)){
	        DialogManager.useStyle(DialogManager.loadStyle.dir, skin);
	    }
	},

	'useStyle' : function(lib, id){
		var doc = document, tag = 'getElementsByTagName';
	    var link = doc.createElement('link');
	    link.type = 'text/css';
	    link.rel = 'stylesheet';
	    link.href = DialogManager.loadStyle.dir;
	    id && (link.id = id);
	    doc[tag]('head')[0].appendChild(link);
	    link = null;
	},

	'getpath' : (function(){
	    var js = document.scripts, jsPath = js[js.length - 1].src;
	    return jsPath.substring(0, jsPath.lastIndexOf("/") + 1);
	})()
};
ScreenLocker = {
	'style' : {
		'position' : 'absolute',
		'top' : '0px',
		'left' : '0px',
		'backgroundColor' : '#000',
		'opacity' : 0.2,
		'zIndex' : 999
	},
	'masker' : null,
	'lock' : function (zIndex) {
		if (this.masker !== null) {
			this.masker.width($(document).width()).height($(document).height());
			return true;
		}
		this.masker = $('<div id="dialog_manage_screen_locker"></div>');
		/* 样式 */
		this.masker.css(this.style);
		if (zIndex) {
			this.masker.css('zIndex', zIndex);
		}

		/* 整个文档的宽高 */
		this.masker.width($(document).width()).height($(document).height());
		$(document.body).append(this.masker);
		$("#dialog_manage_screen_locker").show();
	},
	'unlock' : function () {
		if (this.masker === null) {
			return true;
		}
		this.masker.remove();
		this.masker = null;
	}
};

Dialog = function (id) {
	/* 构造函数生成对话框代码，并加入到文档中 */
	this.id = id;
	this.init();
};
Dialog.prototype = {
	/* 唯一标识 */
	'id' : null,
	/* 文档对象 */
	'dom' : null,
	'lastPos' : null,
	'status' : 'complete',
	'onClose' : function () {
		return true;
	},
	'tmp' : {},
	/* 初始化 */
	'init' : function () {
		this.dom = {
			'wrapper' : null,
			'body' : null,
			'head' : null,
			'title' : null,
			'close_button' : null,
			'content' : null
		};
		/**加载样式**/
		DialogManager.loadStyle();

		/* 创建外层容器 */
		this.dom.wrapper = $('<div id="fwin_' + this.id + '" class="dialog_wrapper"></div>').get(0);
		/* 创建对话框主体 */
		this.dom.body = $('<div class="dialog_body"></div>').get(0);
		/* 创建标题栏 */
		this.dom.head = $('<h3 class="dialog_head"></h3>').get(0);
		/* 创建标题文本 */
		this.dom.title = $('<span class="dialog_title_icon"></span>').get(0);
		/* 创建关闭按钮 */
		this.dom.close_button = $('<span class="dialog_close_button"></span>').get(0);
		/* 创建内容区域 */
		this.dom.content = $('<div class="dialog_content"></div>').get(0);
		/* 组合 */
		$(this.dom.head).append($('<span class="dialog_title clearfix"/>').append(this.dom.title).append(this.dom.close_button));
		$(this.dom.body).append(this.dom.head).append(this.dom.content);
		$(this.dom.wrapper).append(this.dom.body).append('<div style="clear:both; display:block;"></div>');

		/* 初始化样式 */
		$(this.dom.wrapper).css({
			'zIndex' : 1100,
			'display' : 'none',
			'position' : 'absolute'
		});
		$(this.dom.body).css({
			'position' : 'relative'
		});
		/**禁用拖动**/
		// $(this.dom.head).css({
		// 	'cursor' : 'move'
		// });
		/*$(this.dom.close_button).css({
		'position'   : 'absolute',
		'text-indent': '-9999px',
		'cursor'     : 'pointer',
		'overflow'   : 'hidden'
		});*/
		$(this.dom.content).css({
			'margin' : '0px',
			'padding' : '0px'
		});

		var self = this;

		/* 初始化组件事件 */
		$(this.dom.close_button).click(function () {			
			DialogManager.close(self.id);
		});

		/* 可拖放 */
		//        if(typeof draggable != 'undefined'){

		//			$(this.dom.wrapper).draggable({
		//                'handle' : this.dom.head
		//            });
		//        }

		/* 放入文档流 */
		$(document.body).append(this.dom.wrapper);
	},

	/* 隐藏 */
	'hide' : function (lock) {
		$(this.dom.wrapper).hide();
		if (typeof lock == 'undefined') {
			ScreenLocker.unlock();
		}
		return true;
	},

	/* 显示 */
	'show' : function (pos, lock) {
		if (pos) {
			this.setPosition(pos);
		}
		/* 锁定屏幕 */
		if (lock == 1)
			ScreenLocker.lock(999);
		// $(this.dom.wrapper).draggable({
		// 	'handle' : this.dom.head
		// });
		/* 显示对话框 */
		$(this.dom.wrapper).show();
		$(this.dom.content).children().show();
	},

	/* 关闭 */
	'close' : function (lock) {
		if (!this.onClose()) {
			return false;
		}
		/* 关闭对话框 */
		$(this.dom.wrapper).remove();
		//$(this.dom.wrapper).hide();
		/* 解锁屏幕 */
		if (typeof lock == 'undefined') {
			ScreenLocker.unlock();
		}
		return true;
	},

	/* 对话框标题 */
	'setTitle' : function (title) {
		$(this.dom.title).html(title);
	},

	/* 改变对话框内容 */
	'setContents' : function (type, options) {
		contents = this.createContents(type, options);
		if (typeof(contents) == 'string') {
			$(this.dom.content).html(contents);
		} else {
			$(this.dom.content).empty();
			$(this.dom.content).append(contents);
		}
	},

	/* 设置对话框样式 */
	'setStyle' : function (style) {
		if (typeof(style) == 'object') {
			/* 否则为CSS */
			$(this.dom.wrapper).css(style);
		} else {
			/* 如果是字符串，则认为是样式名 */
			$(this.dom.wrapper).addClass(style);
		}
	},
	'setWidth' : function (width) {
		this.setStyle({
			'width' : width + 'px'
		});
	},
	'setHeight' : function (height) {
		this.setStyle({
			'height' : height + 'px'
		});
	},

	/* 生成对话框内容 */
	'createContents' : function (type, options) {
		var _html = '',
		self = this,
		status = 'complete';
		if (!options) {
			/* 如果只有一个参数，则认为其传递的是HTML字符串 */
			this.setStatus(status);
			return type;
		}
		switch (type) {
		case 'ajax':
			/* 通过Ajax取得HTML，显示到页面上，此过程是异步的 */
			$.get(options, function (data) {
				self.setContents(data);
				/* 使用上次定位重新定位窗口位置 */
				self.setPosition(self.lastPos);
			});
			/* 先提示正在加载 */
			_html = this.createContents('loading', {
					'text' : 'loading...'
				});
			break;
		case 'ajax_notice':
			/* 通过Ajax取得HTML，显示到页面上，此过程是异步的 */
			$.getJSON(options, function (data) {
				var json = data;
				console.log(data.content)
				var MsgTxt = '<div class="dialog_message_body"></div><div class="dialog_message_contents dialog_message_notice">' + json.content + '</div><div class="dialog_buttons_bar"></div>';
					self.setContents(MsgTxt);
				/* 使用上次定位重新定位窗口位置 */
				self.setPosition(self.lastPos);
			});
			/* 先提示正在加载 */
			_html = this.createContents('loading', {
					'text' : '正在处理...'
				});
			break;
			/* 以下是内置的几种对话框类型 */
		case 'loading':
			_html = '<div class="dialog_loading"><div class="dialog_loading_text">' + options.text + '</div></div>';
			status = 'loading';
			break;
		case 'message':
			var type = 'notice';
			if (options.type) {
				type = options.type;
			}
			_message_body = $('<div class="dialog_message_body"></div>');
			//_message_contents = $('<div class="dialog_message_contents dialog_message_' + type + '">' + options.text + '</div>');
			_message_contents = $('<div class="dialog_message_contents dialog_message_' + type + '"></div>').html(options.text);
			_buttons_bar = $('<div class="dialog_buttons_bar clearfix"></div>');
			switch (type) {
			case 'notice':
			case 'warning':
				var button_name = '确定';
				if (options.button_name) {
					button_name = options.button_name;
				}
				_ok_button = $('<input type="button" class="btn btn-blue" value="' + button_name + '" />');
				//$('.dialog_close_button').hide();
				// $(_ok_button).click(function () {
				// 	if (options.onclick) {
				// 		if (!options.onclick.call()) {
				// 			return;
				// 		}
				// 	}					
				// 	DialogManager.close(self.id);
				// });
				$(_ok_button).click(function () {
					if (options.onClickYes) {
						if (options.onClickYes.call() === false) {
							return;
						}
						//options.onClickYes(confirmYes)
					}
					if(options.closeHide){
						DialogManager.get(self.id).hide();
					}else{
						DialogManager.close(self.id);
					}
					//DialogManager.close(self.id);
				});
				$(_buttons_bar).append(_ok_button);
				break;
			case 'confirm':
				var yes_button_name = "确定";
				var no_button_name = "取消";
				if (options.yes_button_name) {
					yes_button_name = options.yes_button_name;
				}
				if (options.no_button_name) {
					no_button_name = options.no_button_name;
				}
				_yes_button = $('<input type="button" class="btn-dialog-ok" value="' + yes_button_name + '" />');
				_no_button = $('<input type="button" class="btn-dialog-cancle" value="' + no_button_name + '" />');
				//$('.dialog_close_button').hide();
				$(_yes_button).click(function () {
					if (options.onClickYes) {
						if (options.onClickYes.call() === false) {
							return;
						}
						//options.onClickYes(confirmYes)
					}
					if(options.closeHide){
						DialogManager.get(self.id).hide();
					}else{
						DialogManager.close(self.id);
					}
					//DialogManager.close(self.id);
				});
				$(_no_button).click(function () {
					if (options.onClickNo) {
						if (options.onClickNo.call() === false) {
							return;
						}
						//options.onClickNo(cancleNo)
					}
					if(options.closeHide){
						DialogManager.get(self.id).hide();
					}else{
						DialogManager.close(self.id);
					}
				});
				$(_buttons_bar).append(_yes_button).append(_no_button);
				break;
			}
			_html = $(_message_body).append(_message_contents).append(_buttons_bar);
			break;
		}
		this.setStatus(status);

		return _html;
	},
	/* 定位 */
	'setPosition' : function (pos) {
		/* 上次定位 */
		this.lastPos = pos;
		if (typeof(pos) == 'string') {
			switch (pos) {
			case 'center':
			$(this.dom.content).children().show();
				var left = 0;
				var top = 0;
				var dialog_width = $(this.dom.wrapper).width();
				var dialog_height = $(this.dom.wrapper).height();

				/* left=滚动条的宽度  + (当前可视区的宽度 - 对话框的宽度 ) / 2 */
				left = $(window).scrollLeft() + ($(window).width() - dialog_width) / 2;

				/* top =滚动条的高度  + (当前可视区的高度 - 对话框的高度 ) / 2 */
				top = $(window).scrollTop() + ($(window).height() - dialog_height) / 2;

				$(this.dom.wrapper).css({
					left : left + 'px',
					top : top + 'px'
				});
				break;
			}
		} else {
			var _pos = {};
			if (typeof(pos.left) != 'undefined') {
				_pos.left = pos.left;
			}
			if (typeof(pos.top) != 'undefined') {
				_pos.top = pos.top;
			}
			$(this.dom.wrapper).css(_pos);
		}

	},
	/* 设置状态 */
	'setStatus' : function (code) {
		this.status = code;
	},
	/* 获取状态 */
	'getStatus' : function () {
		return this.status;
	},
	'disableClose' : function (msg) {
		this.tmp['oldOnClose'] = this.onClose;
		this.onClose = function () {
			if (msg)
				alert(msg);
			return false;
		};
	},
	'enableClose' : function () {
		this.onClose = this.tmp['oldOnClose'];
		this.tmp['oldOnClose'] = null;
	},
    // 'onClickType'  :function (confirmYes, cancleNo) {
    //     confirmYes()
    // },
    // 'cancleNo'  :function (cancleNo) {
    //     cancleNo()
    // }
};
//DialogManager.loadStyle();

var gduiDialog = {
	/**实例化dialog**/
	/* 显示Ajax表单 */
	'ajaxForm' : function (id, title, url, width, model) {
		if (!width)
			width = 480;
		if (!model)
			model = 0;
		var d = DialogManager.create(id);
		d.setTitle(title);
		d.setContents('ajax', url);
		d.setWidth(width);
		d.show('center', model);
		return d;
	},
	'ajaxNotice' : function(id, title, url, width, model) {
		if (!width)
			width = 480;
		if (!model)
			model = 0;
		var d = DialogManager.create(id);
		d.setTitle(title);
		d.setContents('ajax_notice', url);
		d.setWidth(width);
		d.show('center', model);
		return d;
	},
	//显示一个正在等待的消息
	'loadingForm' : function (id, title, _text, width, model) {
		if (!width)
			width = 480;
		if (!model)
			model = 0;
		var d = DialogManager.create(id);
		d.setTitle(title);
		d.setContents('loading', {
			text : _text
		});
		d.setWidth(width);
		d.show('center', model);
		return d;
	},

	//显示一个提示消息
	'messageNotice' : function (id, title, _text, width, model, confirmYes) {
		if (!width)
			width = 480;
		if (!model)
			model = 0;
		var d = DialogManager.create(id);
		d.setTitle(title);
		d.setContents('message', {
			type : 'notice',
			text : _text,
			onclick : confirmYes
		});
		d.setWidth(width);
		d.show('center', model);
		return d;
	},
	//显示一个带确定、取消按钮的消息
	'messageConfirm' : function (id, title, _text, width, model, confirmYes, cancleNo) {
		if (!width)
			width = 480;
		if (!model)
			model = 0;
		var d = DialogManager.create(id);
		d.setTitle(title);
		d.setContents('message', {
			type : 'confirm',
			text : _text,
			onClickYes:confirmYes,
			onClickNo:cancleNo,
			closeHide:true
		});
		d.setWidth(width);
		d.show('center', model);
		// d.onClickYes(confirmYes)
		// d.onClickNo(cancleNo);
		return d;
	},
	//显示一个内容为自定义HTML内容的消息
	'htmlForm' : function (id, title, _html, width, model) {
		if (!width)
			width = 480;
		if (!model)
			model = 0;
		var d = DialogManager.create(id);
		d.setTitle(title);
		d.setContents(_html);
		d.setWidth(width);
		d.show('center', model);
		return d;
	},
	//显示一个消息 消息的内容为IFRAME方式
	'iframeForm' : function (id, title, _url, width, height, fresh) {
		if (!width)
			width = 480;
		var rnd = Math.random();
		rnd = Math.floor(rnd * 10000);

		var d = DialogManager.create(id);
		d.setTitle(title);
		var _html = "<iframe id='iframe_" + rnd + "' src='" + _url + "' width='" + width + "' height='" + height + "' frameborder='0'></iframe>";
		d.setContents(_html);
		d.setWidth(width + 20);
		d.setHeight(height + 60);
		d.show('center');

		$("#iframe_" + rnd).attr("src", _url);
		return d;
	}

};

/**com slides**/
;(function($){
	$.fn.gduiSlide = function(options){

		var defaults = {
            speed:5000,//播放速度
            auto : true,//是否自动播放
            eventType:"click",//事件触发
            customeClass:""//自定义类名
		}
		
		var options = $.extend(defaults, options);

		return this.each(function(){
			var clean,
			$ele = $(this),
			picNum = $ele.find(".ty_pic").length,
			$focuBarTemp = $("<div class='focuBar mgauto'><ul class='focu-bar-list'></ul></div>"),
			barListTemp= "<li><a href='javascript:;'><span>&#9679;</span></a></li>",			
			flag = 0;
			

			if(picNum <= 1){
            	clearInterval(clean);
            }else{
					for(var i=0; i<picNum; i++){
						$focuBarTemp.children().append(barListTemp);
					}
            	
// 	            	$focuBarTemp.children().empty().append(barListTemp)
            	//checkAutoPlay();
            }
			if($ele.find(".focuBar").length<=0){
				$ele.append($focuBarTemp).addClass(typeof(options.customeClass)?options.customeClass:'');
			}
			
			if($ele.find(".barCurrent").length<=0){
				$ele.find(".ty_pic").eq(0).show().siblings().hide();
				$ele.find(".focuBar").find("li").eq(0).addClass("barCurrent");
			}				
			
			$focuBarTemp.find("li").on(typeof(options.eventType)?options.eventType:defaults.eventType, function(){
				clearInterval(clean);
	            // clean = setInterval(autoPlay, options.speed);
				var $this = $(this)
				var index = $this.index();
				picShow(index);
				flag = index;
				checkAutoPlay();
			});
			$ele.children(".ty_tabInfo").find("li").hover(function(){
				clearInterval(clean);
			},function(){
				checkAutoPlay();
			});
			checkAutoPlay();
			function checkAutoPlay(){
				clearInterval(clean);
				if(options.auto){
					if(picNum <= 1){
						clearInterval(clean);
					}else{
						clearInterval(clean);
						clean = setInterval(autoPlay, options.speed)	
					}						
				}	
			}
			
			function picShow(obj){			
				$ele.find(".focuBar").find("li").removeClass("barCurrent").eq(obj).addClass("barCurrent");
				$ele.children(".ty_tabInfo").children("ul").find(".ty_pic").eq(obj).css({"z-index":"1"}).fadeIn(500).siblings().css({"z-index":"0"}).fadeOut(500);
			}
			function autoPlay(){
				flag++;
				if(flag > picNum-1){
					flag=0
				}
				picShow(flag)
			}
		});
	}
})(jQuery);


/**com page**/
;!function(){
"use strict";

function gduiPage(options){
    var skin = 'gduiPagecss';
    gduiPage.dir = 'dir' in gduiPage ? gduiPage.dir : Page.getpath + '/skin/gduiPage.css';
    new Page(options);
    if(gduiPage.dir && !doc[id](skin)){
        Page.use(gduiPage.dir, skin);
    }
}

gduiPage.v = '1.2';

var doc = document, id = 'getElementById', tag = 'getElementsByTagName';
var index = 0, Page = function(options){
    var that = this;
    var conf = that.config = options || {};
    conf.item = index++;
    that.render(true);
};

Page.on = function(elem, even, fn){
    elem.attachEvent ? elem.attachEvent('on'+ even, function(){
        fn.call(elem, window.even); //for ie, this指向为当前dom元素
    }) : elem.addEventListener(even, fn, false);
    return Page;
};

Page.getpath = (function(){
    var js = document.scripts, jsPath = js[js.length - 1].src;
    return jsPath.substring(0, jsPath.lastIndexOf("/") + 1);
}())

Page.use = function(lib, id){
    var link = doc.createElement('link');
    link.type = 'text/css';
    link.rel = 'stylesheet';
    link.href = gduiPage.dir;
    id && (link.id = id);
    doc[tag]('head')[0].appendChild(link);
    link = null;
};

//判断传入的容器类型
Page.prototype.type = function(){
    var conf = this.config;
    if(typeof conf.cont === 'object'){
        return conf.cont.length === undefined ? 2 : 3;
    }
};

//分页视图
Page.prototype.view = function(){
    var that = this, conf = that.config, view = [], dict = {};
    conf.pages = conf.pages|0;
    conf.curr = (conf.curr|0) || 1;
    conf.groups = 'groups' in conf ? (conf.groups|0) : 5;
    conf.first = 'first' in conf ? conf.first : 1;
    conf.last = 'last' in conf ? conf.last : conf.pages;
    conf.prev = 'prev' in conf ? conf.prev : '\u4e0a\u4e00\u9875';
    conf.next = 'next' in conf ? conf.next : '\u4e0b\u4e00\u9875';
    
    if(conf.groups > conf.pages){
        conf.groups = conf.pages;
    }
    
    //计算当前组
    dict.index = Math.ceil((conf.curr + ((conf.groups > 1 && conf.groups !== conf.pages) ? 1 : 0))/(conf.groups === 0 ? 1 : conf.groups));
    
    //当前页非首页，则输出上一页
    if(conf.curr > 1 && conf.prev){
        view.push('<a href="javascript:;" class="gduiPage_prev" data-page="'+ (conf.curr - 1) +'">'+ conf.prev +'</a>');
    }
    
    //当前组非首组，则输出首页
    if(dict.index > 1 && conf.first && conf.groups !== 0){
        view.push('<a href="javascript:;" class="gduiPage_first" data-page="1"  title="\u9996\u9875">'+ conf.first +'</a><span>\u2026</span>');
    }
    
    //输出当前页组
    dict.poor = Math.floor((conf.groups-1)/2);
    dict.start = dict.index > 1 ? conf.curr - dict.poor : 1;
    dict.end = dict.index > 1 ? (function(){
        var max = conf.curr + (conf.groups - dict.poor - 1);
        return max > conf.pages ? conf.pages : max;
    }()) : conf.groups;
    if(dict.end - dict.start < conf.groups - 1){ //最后一组状态
        dict.start = dict.end - conf.groups + 1;
    }
    for(; dict.start <= dict.end; dict.start++){
        if(dict.start === conf.curr){
            view.push('<span class="gduiPage_curr" '+ (/^#/.test(conf.skin) ? 'style="background-color:'+ conf.skin +'"' : '') +'>'+ dict.start +'</span>');
        } else {
            view.push('<a href="javascript:;" data-page="'+ dict.start +'">'+ dict.start +'</a>');
        }
    }
    
    //总页数大于连续分页数，且当前组最大页小于总页，输出尾页
    if(conf.pages > conf.groups && dict.end < conf.pages && conf.last && conf.groups !== 0){
         view.push('<span>\u2026</span><a href="javascript:;" class="gduiPage_last" title="\u5c3e\u9875"  data-page="'+ conf.pages +'">'+ conf.last +'</a>');
    }
    
    //当前页不为尾页时，输出下一页
    dict.flow = !conf.prev && conf.groups === 0;
    if(conf.curr !== conf.pages && conf.next || dict.flow){
        view.push((function(){
            return (dict.flow && conf.curr === conf.pages) 
            ? '<span class="page_nomore" title="\u5df2\u6ca1\u6709\u66f4\u591a">'+ conf.next +'</span>'
            : '<a href="javascript:;" class="gduiPage_next" data-page="'+ (conf.curr + 1) +'">'+ conf.next +'</a>';
        }()));
    }
    
    return '<div name="gduiPage'+ gduiPage.v +'" class="gduiPage_main gduiPageskin_'+ (conf.skin ? (function(skin){
        return /^#/.test(skin) ? 'molv' : skin;
    }(conf.skin)) : 'default') +'" id="gduiPage_'+ that.config.item +'">'+ view.join('') + function(){
        return conf.skip 
        ? '<span class="gduiPage_total"><label>\u5230\u7b2c</label><input type="number" min="1" onkeyup="this.value=this.value.replace(/\\D/, \'\');" class="gduiPage_skip"><label>\u9875</label>'
        + '<button type="button" class="gduiPage_btn">\u786e\u5b9a</button></span>' 
        : '';
    }() +'</div>';
};

//跳页
Page.prototype.jump = function(elem){
    var that = this, conf = that.config, childs = elem.children;
    var btn = elem[tag]('button')[0];
    var input = elem[tag]('input')[0];
    for(var i = 0, len = childs.length; i < len; i++){
        if(childs[i].nodeName.toLowerCase() === 'a'){
            Page.on(childs[i], 'click', function(){
                var curr = this.getAttribute('data-page')|0;
                conf.curr = curr;
                that.render();
                
            });
        }
    }
    if(btn){
        Page.on(btn, 'click', function(){
            var curr = input.value.replace(/\s|\D/g, '')|0;
            if(curr && curr <= conf.pages){
                conf.curr = curr;
                that.render();
            }
        });
    }
};

//渲染分页
Page.prototype.render = function(load){
    var that = this, conf = that.config, type = that.type();
    var view = that.view();
    if(type === 2){
        conf.cont.innerHTML = view;
    } else if(type === 3){
        conf.cont.html(view);
    } else {
        doc[id](conf.cont).innerHTML = view;
    }
    conf.jump && conf.jump(conf, load);
    that.jump(doc[id]('gduiPage_' + conf.item));
    if(conf.hash && !load){
        location.hash = '!'+ conf.hash +'='+ conf.curr;
    }
};

//for 页面模块加载、Node.js运用、页面普通应用
"function" === typeof define ? define(function() {
    return gduiPage;
}) : "undefined" != typeof exports ? module.exports = gduiPage : window.gduiPage = gduiPage;

}();


/*
Ajax 三级省市联动

settings 参数说明
-----
url:省市数据josn文件路径
prov:默认省份
city:默认城市
dist:默认地区（县）
nodata:无数据状态
required:必选项
------------------------------ */
(function($){
	$.fn.gduiCitySelect=function(settings){
		if(this.length<1){return;};

		// 默认值
		settings=$.extend({
			url:"city.min.js",
			prov:null,
			city:null,
			dist:null,
			nodata:null,
			required:true
		},settings);

		var box_obj=this;
		var prov_obj=box_obj.find(".prov");
		var city_obj=box_obj.find(".city");
		var dist_obj=box_obj.find(".dist");
		var prov_val=settings.prov;
		var city_val=settings.city;
		var dist_val=settings.dist;
		var select_prehtml=(settings.required) ? "" : "<option value=''>请选择</option>";
		var city_json;

		// 赋值市级函数
		var cityStart=function(){
			var prov_id=prov_obj.get(0).selectedIndex;
			if(!settings.required){
				prov_id--;
			};
			city_obj.empty().attr("disabled",true);
			dist_obj.empty().attr("disabled",true);

			if(prov_id<0||typeof(city_json.citylist[prov_id].c)=="undefined"){
				if(settings.nodata=="none"){
					city_obj.css("display","none");
					dist_obj.css("display","none");
				}else if(settings.nodata=="hidden"){
					city_obj.css("visibility","hidden");
					dist_obj.css("visibility","hidden");
				};
				return;
			};
			
			// 遍历赋值市级下拉列表
			temp_html=select_prehtml;
			$.each(city_json.citylist[prov_id].c,function(i,city){
				temp_html+="<option value='"+city.n+"'>"+city.n+"</option>";
			});
			city_obj.html(temp_html).attr("disabled",false).css({"display":"","visibility":""});
			//city_obj.selectric();
			distStart();
		};

		// 赋值地区（县）函数
		var distStart=function(){
			var prov_id=prov_obj.get(0).selectedIndex;
			var city_id=city_obj.get(0).selectedIndex;
			if(!settings.required){
				prov_id--;
				city_id--;
			};
			dist_obj.empty().attr("disabled",true);

			if(prov_id<0||city_id<0||typeof(city_json.citylist[prov_id].c[city_id].a)=="undefined"){
				if(settings.nodata=="none"){
					dist_obj.css("display","none");
				}else if(settings.nodata=="hidden"){
					dist_obj.css("visibility","hidden");
				};
				return;
			};
			
			// 遍历赋值市级下拉列表
			temp_html=select_prehtml;
			$.each(city_json.citylist[prov_id].c[city_id].a,function(i,dist){
				temp_html+="<option value='"+dist.s+"'>"+dist.s+"</option>";
			});
			dist_obj.html(temp_html).attr("disabled",false).css({"display":"","visibility":""});
		};

		var init=function(){
			// 遍历赋值省份下拉列表
			temp_html=select_prehtml;
			$.each(city_json.citylist,function(i,prov){
				temp_html+="<option value='"+prov.p+"'>"+prov.p+"</option>";
			});
			prov_obj.html(temp_html);

			//prov_obj.selectric();
			// 若有传入省份与市级的值，则选中。（setTimeout为兼容IE6而设置）
			setTimeout(function(){
				if(settings.prov!=null){
					prov_obj.val(settings.prov);
					cityStart();
					setTimeout(function(){
						if(settings.city!=null){
							city_obj.val(settings.city);
							distStart();
							setTimeout(function(){
								if(settings.dist!=null){
									dist_obj.val(settings.dist);
								};
							},1);
						};
					},1);
				};
			},1);

			// 选择省份时发生事件
			prov_obj.bind("change",function(){
				cityStart();
			});

			// 选择市级时发生事件
			city_obj.bind("change",function(){
				distStart();
			});
		};

		// 设置省市json数据
		if(typeof(settings.url)=="string"){
			$.getJSON(settings.url,function(json){
				city_json=json;
				init();
			});
		}else{
			city_json=settings.url;
			init();
		};
	};
})(jQuery);