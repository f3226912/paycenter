<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../pub/constants.inc" %>
<%@ include file="../../pub/tags.inc" %>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../../pub/head.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<title>交易记录</title>
	</head>
	<script>
		var disableExport = false;
		$(document).ready(function(){
			loadData(null);
			/***数据导出功能***/
			$("#exportData").click(function(){
				var payeeAccount = $("#payeeAccount").searchbox("getValue");
				var payeeMobile = $("#payeeMobile").searchbox("getValue");
				var realName = $("#realName").searchbox("getValue");
				var startDate = $("#startDate").val();
				var endDate=$("#endDate").val();
				if(endDate.length>0){
					endDate=endDate+" 23:59:59";
				}
				var params = {
					"payeeAccount" : payeeAccount,
					"payeeMobile" : payeeMobile,
					"realName" : realName,
					"startDate" : startDate,
					"endDate" : endDate
				};
				var paramList = "exportType=2" + '&payeeAccount=' + payeeAccount + '&payeeMobile=' + payeeMobile + 
					'&realName=' + realName + '&startDate=' + startDate + '&endDate=' + endDate;
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
								$.download(CONTEXT+'paycenter/paytrade/exportData',paramList,'post' );
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
			});
		});
		
		function loadData(dataParams){
			//数据加载
			$('#dg').datagrid({
	    		url:CONTEXT+'paycenter/paytrade/queryPayTradeList',
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
		}
		
		//查询
		function query(){
			var endDate=$("#endDate").val();
			if(endDate.length>0){
				endDate=endDate+" 23:59:59";
			}
			var dataParams={
				payeeAccount : $("#payeeAccount").searchbox("getValue"),
				payeeMobile : $("#payeeMobile").searchbox("getValue"),
				realName : $("#realName").searchbox("getValue"),
				"startDate" : $("#startDate").val(),
				"endDate" : endDate
			};
			loadData(dataParams);
		}
		
		//单独查询
		function query2(value,name){
			var dataParams;
			 if(name=='payeeAccount'){
				dataParams={
					payeeAccount : value
				};
			 }
			 if(name=='payeeMobile'){
				dataParams={
					payeeMobile : value
				};
			 }
			 if(name=='realName'){
				dataParams={
					realName : value
				};
			 }
			loadData(dataParams);
		}
		
		//重置
		function cleardata(){
			$("#payeeAccount").searchbox("setValue","");
			$("#payeeMobile").searchbox("setValue","");
			$("#realName").searchbox("setValue","");
			$("#startDate").val("");
			$("#endDate").val("");
		}
		
		//查看
		function viewAction(id){
			$('<div></div>').dialog({
			      id : 'viewDialog',
			      title : '付款详情',
			      width : 600,
			      height : 500,
			      closed : false,
			      cache : false,
			      href : CONTEXT+'paycenter/paytrade/viewpaydetail?id='+id,
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
			//$('#viewDialog').dialog({'title':'付款详情','href':CONTEXT+'paycenter/paytrade/viewpaydetail?id='+id}).dialog('open');
			//$('#dg').datagrid("uncheckAll");
		}
		
		//修改
		function updateAction(id, type){
			//$('#editDialog').dialog({'title':'付款详情','href':CONTEXT+'paycenter/paytrade/paydetail?id='+id}).dialog('open');
			//$('#dg').datagrid("uncheckAll");
			var buttonText = "";
			if(type == "add") {
				buttonText = "提交保存";
			} else if(type == "update") {
				buttonText = "修改";
			}
			$('<div></div>').dialog({
			      id : 'editDialog',
			      title : '付款详情',
			      width : 600,
			      height : 500,
			      closed : false,
			      cache : false,
			      href : CONTEXT+'paycenter/paytrade/paydetail?id='+id,
			      modal : true,
			      onLoad : function() {
			          //初始化表单数据
			      },
			      onClose : function() {
			          $(this).dialog('destroy');
			      },
			      buttons : [ {
			          text : buttonText,
			          iconCls : 'icon-save',
			          handler : function() {
			        	  save();
			              return false; // 阻止表单自动提交事件
			          }
			      }, {
			          text : '关闭',
			          iconCls : 'icon-cancel',
			          handler : function() {
			              $("#editDialog").dialog('destroy');
			          }
			      } ]
			  });
		}
		
		//操作修改
		function updateOperate(value,row,index){
			var html="";
			html=html+"<a class='operate' href='javascript:;' onclick=viewAction('"+row.id+"');>查看</a>";
			if('1' == row.transferStatus){
				html=html+"<a class='operate' href='javascript:;' onclick=updateAction('"+row.id+"','add');>转账</a>";
			} else if('2' == row.transferStatus){
				html=html+"<a class='operate' href='javascript:;' onclick=updateAction('"+row.id+"','update');>修改</a>";
			} 
			return html;
		}
		// 审核
		function audit(){
			var rows = $('#dg').datagrid("getSelections");
			if ($(rows).length < 1) {
				warningMessage("请选择要操作的数据！");
				return false;
			}
			for(var i=0; i < rows.length; i++){
				if(rows[i].auditUserName && rows[i].auditUserName.length > 0) {
					warningMessage("不能选择已复核的数据！");
					return false;
				}
			}
			var ids = getSelections("id");
			jQuery.messager.confirm('提示', '您确定要复核所选数据吗?', function(r){
				if (r){
					$.ajax({
				        type: "POST",
				        url: CONTEXT + "paycenter/paytrade/auditPayTrade",
				        data:{"ids": ids},
				        dataType: "json",
				        success: function(data){
				        	if (!data.errMsg) {
				    			slideMessage("操作成功！");
				    			$("#dg").datagrid('reload');
				    		}else{
				    			warningMessage(data.errMsg);
				    			return;
				    		}
				        },
				        error: function(){
				        	warningMessage("服务器出错");
				        }
					});
				}
			});
		}
		
		//操作修改
		function orderNoFormatter(value,row,index){
			var newValue = "订单号：" + value + "/平台支付流水：" + row.payCenterNumber;
			return newValue;
		}
		
		function provinceNameFormatter(value,row,index){
			var newValue = value;
			if(row.cityName != null && row.cityName != ""){
				newValue += "-" + row.cityName;
			}
			if(row.areaName != null && row.areaName != ""){
				newValue += "-" + row.areaName;
			}
			return newValue;
		}
		function save() {
			if ($('#form1').form("validate")) {
				var url=CONTEXT+"paycenter/remitrecord/update";
				jQuery.messager.confirm('提示', '确定要转账' + $('#payAmt').val() + '？', function(r){
					if (r){
						jQuery.post(url, $('#form1').serialize(), function (data) {
							if (data == "success") {
								slideMessage("操作成功！");
								//刷新父页面列表
								$("#dg").datagrid('reload');
								$('#editDialog').dialog('destroy');
							} else {
								warningMessage(data);
								return;
							}
						});
					}else{
						return;
					}
				});
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
		
		function transferStatusFormatter(val, row, index){
			if(val == '1'){
				return "未转账";
			} else if(val == '2'){
				return "转账成功";
			}
		}
    </script>
<body>
	<table id="dg" title="">
		<thead>
			<tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'payTime',width:130,align:'left',sortable:true">付款时间</th>
				<th data-options="field:'payAmt',width:80,align:'left',formatter:decimalFormatter,sortable:true">付款金额</th>
				<th data-options="field:'orderNo',width:220,align:'left',formatter:orderNoFormatter,sortable:true">汇款用途</th>
				<th data-options="field:'payeeAccount',width:130,align:'left',sortable:true">收款方账号</th>
				<th data-options="field:'payeeMobile',width:100,align:'left',sortable:true">收款方手机号</th>
				<th data-options="field:'realName',width:80,align:'left',sortable:true">持卡人姓名</th>
				<th data-options="field:'depositBankName',width:120,align:'left',sortable:true">开户银行名称</th>
				<th data-options="field:'provinceName',width:120,align:'left',formatter:provinceNameFormatter,sortable:true">开户银行所在地</th>
				<th data-options="field:'subBankName',width:120,align:'left',sortable:true">支行名称</th>
				<th data-options="field:'bankCardNo',width:150,align:'left',sortable:true">银行卡号</th>
				<th data-options="field:'transferStatus',width:80,align:'left',formatter:transferStatusFormatter,sortable:true">转账状态</th>
				<th data-options="field:'auditUserName',width:80,align:'left',sortable:true">复核人</th>
				<th data-options="field:'operUserName',width:80,align:'left',sortable:true">操作人</th>
				<th data-options="field:'payCenterNumber',width:100,align:'center',sortable:true,hidden:true"></th>
				<th data-options="field:'cityName',width:100,align:'center',sortable:true,hidden:true"></th>
				<th data-options="field:'areaName',width:100,align:'center',sortable:true,hidden:true"></th>
				<th data-options="field:'action',width:120,align:'center',formatter:updateOperate">操作</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:5px;height:auto">
		<form id="searchform" method="post">
		<div>
			收款方账号: <input class="easyui-searchbox" type="text" id="payeeAccount" name="payeeAccount" style="width:150px" data-options="prompt:'请输入收款方账号',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
			收款方手机号: <input class="easyui-searchbox" type="text" id="payeeMobile" name="payeeMobile" style="width:150px" data-options="prompt:'请输入收款方手机号',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
			持卡人姓名: <input class="easyui-searchbox" type="text" id="realName" name="realName" style="width:150px" data-options="prompt:'请输入持卡人姓名',  
            searcher:function(value,name){query2(value,name)}">&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="margin-top:10px;">
			付款时间: 
			<input  type="text" id="startDate" name="startDate"  
					onFocus="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){startDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> ~
			<input  type="text" id="endDate" name="endDate"   
					onFocus="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){endDate.focus();},minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick="cleardata()">重置</a>
			
		</div>
		</form>
		<div style="margin-bottom:5px">
			<a class="easyui-linkbutton icon-add-btn" iconCls="icon-edit" id="btn-close" onclick="audit()">复核</a>
			<a id="exportData" class="easyui-linkbutton" iconCls="icon-account-excel">导出EXCEL</a>
			<a class="easyui-linkbutton" iconCls="icon-reload" onclick='location.reload(true)'">刷新</a>
		</div>
	</div>
</body>
</html>