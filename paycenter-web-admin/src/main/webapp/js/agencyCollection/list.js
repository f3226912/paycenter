        var disableExport = false;
		$(function(){
			loadData(getParams());
			/***数据导出功能***/
			$("#exportData").click(function(){				
				var params = getParams();			
				if(disableExport){
					slideMessage("已进行过一次数据导出,导出功能已禁用,请10秒钟过后再点...");								
					setInterval(function(){
						disableExport = false;
					}, 10000);
		        }else{
		    		$.ajax({
						url: CONTEXT+'paycenter/agencyCollection/validateSign/2',
						data : getParams(),
						type:'post',
						success : function(data){
							if (data && data.code == 2000){
								warningMessage(data.msg);
							}
				        	$.ajax({
								url: CONTEXT+'paycenter/agencyCollection/exportCheck',
								data : params,
								type:'post',
								success : function(data){
									//检测通过
									if (data && data.code == 10000){
										if (!disableExport){
											slideMessage("数据正在导出中, 请耐心等待...");
											disableExport = true;
											//启动下载
											$.download(CONTEXT+'paycenter/agencyCollection/exportData',params,'post');
										}							
									}else{
										warningMessage(data.result);
									}
								},
								error : function(){
									warningMessage("请求错误");
								}
							})							
						},
						error : function(){
							warningMessage("请求错误");
						}
					})
		        }			
			})						
		})
		
		function loadData(dataParams){
			$.ajax({
				url: CONTEXT+'paycenter/agencyCollection/validateSign/1',
				data : dataParams,
				type:'post',
				success : function(data){
					if (data && data.code == 2000){
						warningMessage(data.msg);
					}
					//数据加载
					$('#dg').datagrid({
			    		url:CONTEXT+'paycenter/agencyCollection/queryPage',
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
						remoteSort:false//页面排序必须加
					}); 
					//分页加载
					$("#dg").datagrid("getPager").pagination({
						pageList: [20,50,100,200]
					}); 
				},
				error : function(){
					warningMessage("请求错误");
				}
			})				
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
				marketId:$("#marketId").val(),
				payType:$("#payType").val(),
			    payStatus:$("#payStatus").val(),
			    checkStatus:$("#checkStatus").val(),
			    payerMobile : $("#payerMobile").val(),
			    thirdPayNumber : $("#thirdPayWater").val(),
			    payCenterNumber : $("#platformPayWater").val(),
				orderNo : $("#orderNo").val(),
				startDate : $("#startDate").val(),
				endDate : endDate,
				orderType : $("#orderType").val()
			};
			return dataParams;
		}
		
		//单独查询
		function query2(value,name){
			var dataParams;
			 if(name=='payeeMobile'){
				dataParams={
					payeeMobile : value
				};
			 }
			 if(name=='thirdPayWater'){
				dataParams={
				    thirdPayWater : value
				};
			 }
			 if(name=='platformPayWater'){
				dataParams={
					platformPayWater : value
				};
			 }
			 if(name=='orderNo'){
				dataParams={
					orderNo : value
				};
		     }			 
			 loadData(dataParams);
		}
		
		//重置
		function cleardata(){
			$("#marketId").val("");
			$("#payType").val("");
			$("#payStatus").val("");
			$("#checkStatus").val("");
			$("#payerMobile").val("");
			$("#thirdPayWater").val("");
			$("#platformPayWater").val("");
			$("#orderNo").val("");
			$("#startDate").val("");
			$("#endDate").val("");
		}
		
		//查看
		function viewAction(id){
			$.ajax({
				url: CONTEXT+'paycenter/agencyCollection/validateSign/3',
				data : {id:id},
				type:'post',
				success : function(data){
					if (data && data.code == 2000){
						warningMessage(data.msg);
					}
					//检测通过
					$('<div></div>').dialog({
					      id : 'viewDialog',
					      title : '代收款详情',
					      width : 1000,
					      height : 400,
					      closed : false,
					      cache : false,
					      href : CONTEXT+'paycenter/agencyCollection/detail/'+id,
					      modal : true,
					      onLoad : function() {
					          //初始化表单数据
					      },
					      onClose : function() {
					          $(this).dialog('destroy');
					      },
					      buttons : [ {
					          text : '关闭',
					          iconCls : 'icon-cancel',
					          handler : function() {
					              $("#viewDialog").dialog('destroy');
					          }
					      } ]
					  });					
				},
				error : function(){
					warningMessage("请求错误");
				}
			})
		}
		
		
		//操作修改
		function doOperate(value,row,index){
			//查看不能用pay_trade的id进行查询，因为一条流水对应多个订单的情况，取交易id查询是不正确的，应该是流水号(payCenterNumber)查询
			//return "<a class='operate' href='javascript:;' onclick=viewAction('"+row.id+"');>查看</a>";
			return "<a class='operate' href='javascript:;' onclick=viewAction('"+row.payCenterNumber+"');>查看</a>";
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
			} else if(val == 'gys'){
				return "供应商";
			}else if(val == 'nst_fare'){
				return "农速通-运费";
			}else if(val == 'nsy_pay'){
				return "农商友";
			} else if (val == 'nsy') {
				return "农商友";
			}
		}

		function payTypeFormatter(val, row, index){
			if(val == 'ALIPAY_H5'){
				return "支付宝";
			} else if(val == 'WEIXIN_APP'){
				return "微信";
			} else if(val == 'PINAN'){
				return "平安银行";
			}else if(val == 'ENONG'){
				return "E农";
			}else if(val == 'NNCCB'){
				return "南宁建行";
			}else if(val == 'GXRCB'){
				return "广西农信";
			} else if (val == 'WANGPOS') {
				return '旺POS';
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