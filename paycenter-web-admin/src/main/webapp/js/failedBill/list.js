        var disableExport = false;
		$(document).ready(function(){
			loadData(getParams());
		})
		
		function loadData(dataParams){
			//数据加载
			$('#failedBilldg').datagrid({
	    		url:CONTEXT+'paycenter/failedBill/queryPage',
	    		queryParams : dataParams,
				width: 1000,  
	    		height: 'auto', 
				nowrap:false,
				toolbar:'#tb',
				pageSize:20,
				rownumbers:true,
				pagination:true,
				fitColumns:false,
				fit:true,
				remoteSort:false,//页面排序必须加
				view: ViewUtils.getEmptyTipView(),
		        emptyMsg: '没有找到相关数据。'
			}); 
			//分页加载
			$("#failedBilldg").datagrid("getPager").pagination({
				pageList: [20,50,100,200]
			}); 
		}
		
		//查询
		function query(){
			var dataParams = getParams();
			loadData(dataParams);
		}
		
		//获取参数
		function getParams(){
			var endDate=$("#endDate").val();
			if(endDate.length>0){
				endDate=endDate+" 23:59:59";
			}
			var dataParams={
				payType:$("#payType").val(),
				startDate : $("#startDate").val(),
				endDate : endDate
			};
			return dataParams;
		}
		
		//修改
		function updateFailedBillDetail(thirdPayNumber){
			$('#updateFailedBillDialog').dialog({'title':'修改对账明细','href':CONTEXT+'paycenter/failedBill/failedBillDetail/'+thirdPayNumber,'width': 880,'height': 450}).dialog('open');
		}
		
		
		//操作修改
		function doOperate(value,row,index){
			return "<a class='operate' href='javascript:;' onclick=updateFailedBillDetail('"+row.thirdPayNumber+"');>修改</a>";
		}
		
		function payStatusFormatter(val, row, index){
			if(val == '1'){
				return "待付款";
			} else if(val == '2'){
				return "付款成功";
			} else if(val == '3'){
				return "已关闭";
			} else if(val == '4'){
				return "已退款";
			} 
		}
		
		function appKeyFormatter(val, row, index){
			if(val == 'nst'){
				return "农速通";
			} else if(val == 'nst_car'){
				return "农速通-车主";
			} else if(val == 'nst_goods'){
				return "农速通-货主";
			} else if(val == 'nps'){
				return "农批商";
			} else if(val == 'nsy'){
				return "农商友";
			} else if(val == 'gys'){
				return "供应商";
			} 
		}

		function payTypeFormatter(val, row, index){
			if(val == 'ALIPAY_H5'){
				return "支付宝支付";
			} else if(val == 'WEIXIN_APP'){
				return "微信支付";
			} else if(val == 'ENONG'){
				return "武汉E农";
			} else if(val == 'NNCCB'){
				return "南宁建行";
			} else if(val == 'GXRCB'){
				return "广西农信";
			} else if(val == 'WANGPOS'){
				return "旺POS";
			}
		}
		
		function decimalFormatter(val, row, index){
			if(val != null && val != "") {
				var xsd = val.toString().split(".");
				if(xsd.length == 1){
					val = val.toString()+".00";
				 	return val;
				}
				if(xsd.length > 1){
					if(xsd[1].length < 2){
				 		val = val.toString() + "0";
				 	}
				 	return val;
				}
			}
		}
		
		function checkStatusFormatter(val, row, index){
			if(val == '1'){
				return "对账成功";
			} else if(val == '2'){
				return "对账失败";
			}
		}
		
