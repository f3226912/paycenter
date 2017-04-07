/**
 * 例子
 * @author zhangnf
 * @since 2016-8-9
 */
var ruleInfoCtrl  = new NSTAdmin.client();

NSTAdmin.client.prototype.ruleInfoAdd = {
	/**
	 * 请求地址URL
	 */
	urlItems : {
		queryByPageURL :"/admin/product/queryByPage.do",
		detailURL : "/admin/product/detail.do"
	},
	/**
	 * 业务方法例子
	 * @param id
	 */
	deleteDialog : function(id,productName){
		_this = this;
		$("#title").html(productName);
		$("#deleteProductW").show();
		$("#deleteProduct").attr("onclick","productCtrl.product.doDelete("+id+")");
	},
	/**
	 * 业务方法例子
	 * @param id
	 */
	doDelete : function(id){
		productCtrl.ajaxA({
			url :_this.urlItems.deleteURL,
			data : {id:id},
			type : 'post',
			success : function(data) {
				if(null == data){
					return;
				}
				if(0== data.flag){
					productCtrl.alertDialog(data.msg);
					$("#deleteProductW").hide();
					_this.doSearchCallback(0);
				}else{
					productCtrl.errorDialog(data.msg);
				}
			}
		});
	},
	/**
	 * 事件绑定
	 */
	event : function(){
		_this = this;
		$("#deleteView").click(function(){
			_this.deleteDialog();
		});
		$("#deleteBtn").click(function(){
			_this.doDelete();
		});
	}
};

/**
 * 初始化
 */
$(function(){
	//初始化按钮事件
	ruleInfoCtrl.ruleInfoAdd.event();
});
