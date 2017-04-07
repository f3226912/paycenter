/**
 * 基于easyui的工具类
 */

/**弹出加载框效果。
 * 用法：
 * 	显示 MaskUtils.mask('自定义的提示文字...');
 * 	隐藏 MaskUtils.unmask();
 * 
 */
var MaskUtils = (function(){  
    var $mask,$maskMsg;  
      
    var defMsg = '正在处理，请稍待。。。';  
      
    function init(){  
        if(!$mask){  
            $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");  
        }  
        if(!$maskMsg){  
            $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")  
                .appendTo("body").css({'font-size':'12px'});  
        }  
          
        $mask.css({width:"100%",height:$(document).height()});  
          
        var scrollTop = $(document.body).scrollTop();  
          
        $maskMsg.css({  
            left:( $(document.body).outerWidth(true) - 190 ) / 2  
            ,top:( ($(window).height() - 45) / 2 ) + scrollTop  
        });   
                  
    }  
      
    return {  
        mask:function(msg){  
            init();  
            $mask.show();  
            $maskMsg.html(msg||defMsg).show();  
        }  
        ,unmask:function(){  
            $mask.hide();  
            $maskMsg.hide();  
        }  
    }  
      
}()); 


/**获取提示空数据时的视图。
 * ps：当没有查询数据时，在datagrid中显示提示语。
 * 用法：
 * 在定义表格的时候指定
 * $('#mydatagrid').datagrid({
 * 		view: ViewUtils.getEmptyTipView(),
 *      emptyMsg: '没有找到相关数据。',
 *       ......
 *   });
 * 
 * 
 * 
 * 
 * 
 */
var ViewUtils = (function(){
	function getEmptyTipView() {
		//扩展view对象，没有数据时进行提示。
	    return $.extend({},$.fn.datagrid.defaults.view,{
	        onAfterRender:function(target){
	            $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
	            var opts = $(target).datagrid('options');
	            var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
	            vc.children('div.datagrid-empty').remove();
	            if (!$(target).datagrid('getRows').length){
	                var d = $('<div class="datagrid-empty"></div>').html(opts.emptyMsg || 'no records').appendTo(vc);
	                d.css({
	                    position:'absolute',
	                    left:0,
	                    top:50,
	                    width:'100%',
	                    textAlign:'center',
	                    color:'red'
	                });
	            }
	        }
	     });
	}
	
	return {
		getEmptyTipView : getEmptyTipView
	};
}());



/**tab工具类。
 * 用法：
 * 	TabUtils("定制标题", "urlxxx/xxxx/xxxx");
 * 
 */
var TabUtils =  (function(){
	//添加新的tab页面。title为标题，url为请求地址。
	function addTab(title,url){
	    var jq = top.jQuery; 
	    if (jq("#my_tabs").tabs('exists', title)){    
	        jq("#my_tabs").tabs('close', title);    
	    }
	    	
        var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
        jq("#my_tabs").tabs('add',{title:title,content:content,closable:true});    
	}
	
	return {
		addTab : addTab
	};
}());
