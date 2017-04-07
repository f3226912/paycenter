/**
 * Created by Administrator on 2015/8/6 0006.
 */
var xlcMenu={
    init:function(){
        $("#xlc_menu").find("a.menu-lv1").each(function(){
            !$(this).hasClass("c") && $(this).hover(function(){
                $(this).addClass("c");
            },function(){
                if(!$(this).hasClass("on")){
                	$(this).removeClass("c");
                } 
                
            });
            
            $(this).on("click",function(){
            	$("#xlc_menu").find("a.menu-lv1.on").removeClass("on c");
            	$(this).addClass("on c");
            });
            
        });
    }
};

$(function () {
    xlcMenu.init();
});
