<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
$(function(){
	//获取菜单
// 	$.post("${CONTEXT}getMenu",null,function(data){
// 		var menu=eval("("+data+")");
// // 		var menu=data;
// 		var menuHtml='';
// 		for(var i=0;i<menu.length;i++){
// // 			alert(menu[i].menuName);
// // 			if(i==0){
// 				menuHtml=menuHtml+'<div><a onmouseover=showStyle(\"'+menu[i].menuCode+'\") onmouseout=hideStyle(\"'+menu[i].menuCode+'\") id="'+menu[i].menuCode+'" class=\"menu-lv1 '+menu[i].iconCls+'\" href=javascript:openTab(\"'+CONTEXT+menu[i].actionUrl+'\",\"'+menu[i].menuName+'\")><i></i><span>'+menu[i].menuName+'</span></a></div>';
// // 			}else{
// 				menuHtml=menuHtml+'<div><a onmouseover=showStyle(\"'+menu[i].menuCode+'\") onmouseout=hideStyle(\"'+menu[i].menuCode+'\") id="'+menu[i].menuCode+'" class=\"menu-lv1 '+menu[i].iconCls+'\" href=javascript:openTab(\"'+CONTEXT+menu[i].actionUrl+'\",\"'+menu[i].menuName+'\")><i></i><span>'+menu[i].menuName+'</span></a></div>';
// // 			}
// 		}
//         $("#xlc_menu").html(menuHtml);
// 	});
	
	var my_tree;
	var my_tree_url="${CONTEXT}getMenu";
	$(function() {
		my_tree = $('#my_tree').tree({
			url : my_tree_url,
			parentField : 'menuModuleID',
			onClick : function(node) {
				 if(node.attributes.curLevel!=1){
					//打开tab				
					addTab({
						url : "${CONTEXT}"+node.attributes.actionUrl,
						title : node.text,
						iconCls : node.iconCls
					});
				}
			},
			onBeforeLoad : function(node, param) {
				//只有刷新页面才会执行这个方法
				if (my_tree_url) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
				}
			},
			onLoadSuccess : function(node, data) {
				parent.$.messager.progress('close');
			}
		});
	});
	
});

//验证URL
function checkUrl(actionUrl,title){
	if(actionUrl!=""&&actionUrl!="#"){
		$("#mainTitle").html(title);
		//验证是否有URL权限
		 $.post("${CONTEXT}/checkUrl",{"actionUrl":actionUrl},function(data){
			 if(data=="success"){
				//打开tab页面				
				$("#indexMainIframe").attr("src",actionUrl);
			 }else{
				 warningMessage("你没有访问权限哟！");
			 }
		 });
	}else{
		warningMessage("正在建设中哟...");
	}
}

//菜单显示控制
function showStyle(id){
	$("#"+id).addClass("c");
}
//菜单显示控制
function hideStyle(id){
	$("#"+id).removeClass("c");
}
  
</script>
<!-- <div class="menu-content" id="xlc_menu" ></div> -->

<div class="easyui-accordion" data-options="fit:true,border:false">
	<div title="系统菜单">
		<ul id="my_tree"></ul>  
	</div>
</div>