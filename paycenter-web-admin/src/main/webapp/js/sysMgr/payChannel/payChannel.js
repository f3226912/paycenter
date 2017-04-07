$(document).ready(function(){
	load(null,CONTEXT+'payType/getPageByCondition');
});

$('#icon-search').click(function(){
	disableExport = false;
	var params={
		"payTypeName":$("#payTypeName").val()
	}
	load(params,CONTEXT+'payType/getPageByCondition');
});

$("#icon-refresh").click(function(){
	disableExport = false;
	$("#payTypeSearchForm")[0].reset();
	$("#payTypeDatagrid").datagrid('load',{});
});

$("#btn-reset").click(function(){
	$("#payTypeSearchForm")[0].reset();
});

function load(params, loadUrl){
	var emptyMessage;
	params = !params ? {}: params;
	//数据加载
	$('#payTypeDatagrid').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:false,
		toolbar:'#payTypeToolbar',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		
		onLoadSuccess : function(data){
			console.debug(data);
			if (data.code == 60004) {
                //添加一个新数据行，第一列的值为你需要的提示信息，然后将其他列合并到第一列来，注意修改colspan参数为你columns配置的总列数
                $(this).datagrid('appendRow', { payTypeName: '<div style="text-align:center;color:red">无相关数据！</div>' }).datagrid('mergeCells', { index: 0, field: 'payTypeName', colspan: 4 })
                //隐藏分页导航条，这个需要熟悉datagrid的html结构，直接用jquery操作DOM对象，easyui datagrid没有提供相关方法隐藏导航条
                $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            } else if (data.code == 60003) {
            	 $(this).datagrid('appendRow', { payTypeName: '<div style="text-align:center;color:red">数据签名验证异常！</div>' }).datagrid('mergeCells', { index: 0, field: 'payTypeName', colspan: 4 })
                 $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            }
            //如果通过调用reload方法重新加载数据有数据时显示出分页导航容器
            else $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();
		},
		
		/*loadFilter : function(data) {
			console.debug(data);
			if (data.code == 60004) {
				data.rows = [];
				emptyMessage  = "<span>无相关数据</span>";
				console.debug(data);
				return data;
			} else if  (data.code == 60003) {
//				data = [];
				emptyMessage = "数据签名验证异常";
				return data;
			} 
			console.debug("emptyMessage : " + emptyMessage);
			return data;
		},
		emptyMsg : emptyMessage,*/
		
		columns:[[
		          {field:'payTypeName',title:'支付渠道名称',width:100,align:'center'},
		          {field:'contact',title:'渠道联系人',width:100,align:'center'},
		          {field:'telephone',title:'联系电话',width:100,align:'center'},
		          {field:'mail',title:'联系人邮箱',width:100,align:'center'}
				]]
	}); 
	
}

function optFormat(value,row,index){
	var opt = "";
	opt += "<a class='operate' href='javascript:;' onclick=showFeeDetail('"+row.id+"');>查看</a>";
	opt += "<a class='operate' href='javascript:;' onclick=updateFeeDetail('"+row.id+"');>修改</a>";
	return opt;
}

