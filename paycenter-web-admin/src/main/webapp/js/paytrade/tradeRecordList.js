/*
 * 交易记录列表
 */

//是否禁止导出
var disableExport = false;

var tradeRecord={
		url:{
			queryPage:CONTEXT+'paycenter/paytrade/queryTradeList',
		},
	//查询数据
	loadData:loadData,
	//获取查询所需参数
	queryParams:getQueryParams,
	//重置参数
	cleanParams:cleardata,
	
	//工具栏事件
	toolbarAction: {
		//导出结果集
		exportData:exportDataClick
	}

}

function loadData(){
	var queryParams=tradeRecord.queryParams();

	//数据加载
	$('#dg').datagrid({
		url:tradeRecord.url.queryPage,
		queryParams : queryParams,
		height: 'auto', 
		nowrap:true,
		toolbar:'#tradeListTB',
		pageSize:20,
		rownumbers:true,
		pagination:true,
		fitColumns:false,
		fit:true,
		remoteSort:false,
		striped:true, 
		view: ViewUtils.getEmptyTipView(),
		emptyMsg: '没有找到相关数据。',
		onLoadSuccess:function(data){  
	        if(data.total > 0) return;  
	        //$(this).datagrid('appendRow', { payerAmt: '<span style="color:red;">没有相关记录!</span>' }).datagrid('mergeCells', { index: 0, field: 'payerAmt', colspan: 15 });  
	        //隐藏分页导航条，这个需要熟悉datagrid的html结构，直接用jquery操作DOM对象，easyui datagrid没有提供相关方法隐藏导航条  
	        $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();  
	        //如果通过调用reload方法重新加载数据有数据时显示出分页导航容器  
	        //$(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();  
	    }, 
		 columns: [[  
		            { field:'id',checkbox:true },  
		            { field:'orderNo',width:145,align:'left',sortable:true , title: '订单号',formatter:orderNoFormatter},  
		            { field:'orderTypeStr',width:120,align:'left',sortable:true, title: '订单类型' },  
		            { field:'orderTime',width:160,align:'left',sortable:true, title: '下单时间' },  
		            { field:'orderAmt',width:110,align:'right',sortable:true, title: '商品总金额'},  
		            { field:'payerMobile',width:110,align:'left',sortable:true , title: '付款方手机'},  
		            { field:'payerCommissionAmt',width:110,align:'right',sortable:true, title: '付款方支付市场佣金' },
		            { field:'payerPlatCommissonAmt',width:110,align:'right',sortable:true, title: '付款方支付平台佣金' },
		            {field:'payerAmt',width:110,align:'right',sortable:true, title: '付款方实付' },
		            {field:'payerAmt',width:110,align:'right',sortable:true, title: '谷登代收'},
		            {field:'gdFeeAmt',width:140,align:'right',sortable:true, title: '谷登代收支付手续费'},
		            {field:'payeeMobile',width:110,align:'left',sortable:true, title: '收款方手机'},
		            {field:'payeeCommissionAmt',width:110,align:'right',sortable:true, title: '收款方支付市场佣金'},
		            {field:'payeePlatCommissonAmt',width:110,align:'right',sortable:true, title: '收款方支付平台佣金'},
		            {field:'payeeSubsidyAmt',width:70,align:'right',sortable:true, title: '刷卡补贴'},
		            {field:'payeeAmt',width:110,align:'right',sortable:true, title: '收款方应收金额'},
		            {field:'marketCommissionAmt',width:110,align:'right',sortable:true, title: '市场方应收金额'},
		            {field:'marketName',width:130,align:'left',sortable:true, title: '所属市场'},
		            {field:'payerOrderSourceStr',width:80,align:'left',sortable:true, title: '付款方来源'},
//		            {field:'action',width:100, fit:true,align:'center',formatter:updateOperate, title: '操作'}
		            ]]
	});
	
	//分页加载 formatter:decimalFormatter
	$("#dg").datagrid("getPager").pagination({
		pageList: [20,50,100,200]
	}); 
}

//查询参数
function getQueryParams() {
	var endDate=$("#endDate").val();
	var endDate=$("#endDate").val();
	if(endDate.length>0){
		endDate=endDate+" 23:59:59";
	}
	var dataParams={
			"marketId" : $('#marketId').combobox('getValue'),
			"payerOrderSource" : $('#payerOrderSource').combobox('getValue'),
			"orderType" : $('#orderType').combobox('getValue'),
			"payerMobile" : $("#payerMobile").searchbox("getValue"),
			"payeeMobile" : $("#payeeMobile").searchbox("getValue"),
			"orderNo" : $("#orderNo").searchbox("getValue"),
			"startDate" : $("#startDate").val(),
			"endDate" : endDate
	};
	return dataParams;
}
//重置参数
function cleardata(){
	$('#marketId').combobox('setValue',"");
	$('#payerOrderSource').combobox('setValue',"");
	$('#orderType').combobox('setValue',"");
	$("#payerMobile").searchbox("setValue","");
	$("#payeeMobile").searchbox("setValue","");
	$("#orderNo").searchbox("setValue","");
	$("#startDate").val("");
	$("#endDate").val("");
}

//导出数据
function exportDataClick(){
	var params = tradeRecord.queryParams();
	console.log(params);
	$.ajax({
		url: CONTEXT+'paycenter/paytrade/checkExportParams',
		data : params,
		type:'post',
		success : function(data){
			//检测通过
			if (data && data.status == 1){
				/* $("#Loading2").show(); */
				if (!disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					disableExport = true ;
					//启动下载
					$.download(CONTEXT+'paycenter/paytrade/tradeRecordExportData',params,'post' );
				}else{
					slideMessage("已进行过一次数据导出,导出功能已禁用,勿频繁点击导出,若要重新启用导出,请点击刷新按钮...");
				}
			}else{
				warningMessage(data.message);
			}
		},
		error : function(data){
			warningMessage("error : " + data);
		}
	});
	// 10秒后导出按钮重新可用
	setInterval(function(){
		disableExport = false;
	}, 10000);
	
}

	//查询
	function query(){
	/*	var endDate=$("#endDate").val();
		if(endDate.length>0){
			endDate=endDate+" 23:59:59";
		}
		var dataParams={
				"marketId" : $('#marketId').combobox('getValue'),
				"payerOrderSource" : $('#payerOrderSource').combobox('getValue'),
				"orderType" : $('#orderType').combobox('getValue'),
				"payerMobile" : $("#payerMobile").searchbox("getValue"),
				"payeeMobile" : $("#payeeMobile").searchbox("getValue"),
				"orderNo" : $("#orderNo").searchbox("getValue"),
				"startDate" : $("#startDate").val(),
				"endDate" : endDate
		};*/
		tradeRecord.loadData();
	}
		
	//打开查看详情窗口
		function updateAction(orderNo){
			$('#editDialog').dialog({'title':'交易详情','href':CONTEXT+'paycenter/paytrade/detail?orderNo='+orderNo}).dialog('open');
			$('#dg').datagrid("uncheckAll");
		}
		
		function orderNoFormatter(value,row,index){
			if(!value){
			}else{
				return "<a class='operate' href='javascript:;' onclick=updateAction('"+value+"');>"+value+"</a>";
			}
		}
		
		//查看详情
		function updateOperate(value,row,index){
			var html="";
			html=html+"<gd:btn btncode='BTDTRQD01'><a class='operate' href='javascript:;' onclick=updateAction('"+row.orderNo+"');>查看详情</a></gd:btn>";
			return html;
		}

		
//初始化数据
$(function(){
	tradeRecord.loadData();
});
	