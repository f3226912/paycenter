$(document).ready(function(){
	load(null,CONTEXT+'settleAccount/getSettleAccountList');
});
var disableExport = false;
function getSearchParams(){
	var params={
			"exceptionMark":$("#exceptionMark").val(),
			"payStatus":$("#payStatus").val(),
			"changeTimeBeginTime":$("#changeTimeBeginTime").val(),
			"changeTimeEndTime":$("#changeTimeEndTime").val(),
			"realName":$("#realName").searchbox("getValue"),
			"mobile":$("#mobile").searchbox("getValue"),
			"payTimeBeginTime":$("#payTimeBeginTime").val(),
			"payTimeEndTime":$("#payTimeEndTime").val()
		}
	return params;
}
$('#icon-searchList').click(function(){
	disableExport = false;
	load(getSearchParams(),CONTEXT+'settleAccount/getSettleAccountList');
});

function load(params, loadUrl){
	params = !params ? {}: params;
	//数据加载
	$('#settleAccountdg').datagrid({
		url:loadUrl,
		queryParams: params,
		height: 'auto', 
		nowrap:true,
		toolbar:'#settleAccounttb',
		pageSize:50,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		singleSelect:true,
		fit:true,
		columns:[[
					{field:'account',title:'用户账号',width:110,align:'center'},
					{field:'userBankCardNo',title:'用户银行卡号',width:100,hidden:true,formatter:formatNumber},
					{field:'depositBankName',title:'用户银行名称',width:100,hidden:true},
					{field:'batNo',title:'结算批次号',width:100,hidden:true},
					{field:'memberId',title:'会员ID',width:100,hidden:true},
					{field:'mobile',title:'收款方手机',width:110,align:'center',formatter:formatNumber},
					{field:'changeTime',title:'转结算时间',width:110,align:'center',formatter:formatDate},
					{field:'orderNum',title:'代付订单笔数',width:85,align:'center',formatter:orderCountFommatter},
					{field:'commissionNum',title:'代付市场佣金笔数',width:85,align:'center',formatter:commissionCountFormatter},
					{field:'refundNum',title:'代付退款笔数',width:85,align:'center',formatter:checkRefundNumFormatter},
					{field:'penaltyNum',title:'代付违约金笔数',width:85,align:'center',formatter:checkPenaltyNumFormatter},
					{field:'dueAmt',title:'代付金额',width:90,align:'center',formatter:formatDouble},
					{field:'payTime',title:'代付时间',width:110,align:'center',formatter:formatDate},
					{field:'thirdPayNumber',title:'第三方支付流水',width:100,align:'center',formatter:formatNumber},
					{field:'payStatus',title:'代付状态',width:100,align:'center',formatter:formatPayStatus},
					{field:'realName',title:'持卡人姓名',width:100,align:'center'},
					{field:'generationBankStr',title:'代付银行',width:100,align:'center',formatter:formatDate},
					{field:'exceptionMackCount',title:'异常标记',width:80,align:'center'},
					{field:'opt',title:'操作',width:100,align:'center',formatter:optFormat}
				]],
				//过滤提示签名验证
				loadFilter: function(data){
				//数字签名验证异常
					if (data.result&&data.code=='60003'){
						warningMessage(data.msg);
						return data.result;
					} else {
						return data.result;
					}
				}
	}); 
	
}

function updateSettleAccountDetail(id,option){
	$('#updateSettleAccountDialog').dialog({'title':'处理结算信息','href':CONTEXT+'settleAccount/settleAccountDetail/get?option='+option+'&id='+id,'width': 880,'height': 450}).dialog('open');
}
function openException(){
	$("#checkboxValidate").attr("checked",false)
	var rows = $('#settleAccountdg').datagrid("getSelections");
    if(rows.length < 1 ) {
    	warningMessage("请选择要操作的记录！");
        return false;
    }
    if(typeof(rows[0].depositBankName)!="undefined"||typeof(rows[0].userBankCardNo)!="undefined"){
    	 $('#bankNoMark').html(rows[0].depositBankName+"("+rows[0].userBankCardNo+")");
    	 $('#checkboxValidate').val(rows[0].userBankCardNo);
    }else{
    	 $('#bankNoMark').html("");
    	  $('#checkboxValidate').val("");
    }
    $('#checkboxValidate').removeAttr("disabled");
    //市场方没有银行卡验证
    if(typeof(rows[0].commissionNum)!="undefined"&&rows[0].commissionNum>0){
    	$('#checkboxValidate').attr("disabled","disabled");
    }
    $('#idMark').val(rows[0].id);
    $('#accountMark').val(rows[0].account);
    $('#mobileMark').val(rows[0].mobile);
    $('#addSettleAccountErrorDialog').dialog({'title':'标记为异常记录'}).dialog('open');
}

function addErrorSubmit(){
	 var bankNoMark=$('#bankNoMark').html();
	 var idMark=$('#idMark').val();
	 var accountMark=$('#accountMark').val();
	 var telephoneMark=$('#mobileMark').val();
	 
	 var markReason=$('#markReason').val();
	 if(!markReason){
		slideMessage("请输入标记原因！");
	    return;
	 }
	 var param = {id:idMark, comment:markReason, 
				account:accountMark, bankCardNo:$("input[name='checkboxValidate']:checked").val()};
	 jQuery.post(CONTEXT + "settleAccount/addErrorMark", param, function(data) {
		 		if (data.success == true) {
		 			slideMessage("标记成功！");
		 			$("#settleAccountdg").datagrid('reload');
					$('#addSettleAccountErrorDialog').dialog('close');
					$('#markReason').val(" ");
				}else{
					slideMessage('标记失败！');
				}
	 });
}

function orderCountFommatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	if(val == 0) {
		return '-';
	}
	return "<a href='javascript:void(0)' onclick=\"openOrderDetail('"+row.memberId+"','"+row.batNo+"','1')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";
}

function commissionCountFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	if(val == 0) {
		return '-';
	}
	return "<a href='javascript:void(0)' onclick=\"openCommissionDetail('"+row.memberId+"','"+row.batNo+"','1')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";
}

function refundCountFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	/*return "<a href='javascript:void(0)' onclick=\"openCommissionDetail('"+row.memberId+"','"+row.batNo+"','1')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";*/
}

function penaltyCountFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	/*return "<a href='javascript:void(0)' onclick=\"openCommissionDetail('"+row.memberId+"','"+row.batNo+"','1')\" class='operate'>&nbsp;&nbsp;&nbsp;"+val+"</a>";*/
}


function checkPenaltyNumFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	if(val == 0) {
		return '-';
	}
	return "<a class='operate' href='javascript:;' onclick=\"checkPenaltyNum('"+index+"');\" >" + val + "</a>";
}

/**
 * 违约金笔数
 * */
function checkPenaltyNum(rowIndex) {
	var rows = $('#settleAccountdg').datagrid("getRows");
	var curRow = rows[rowIndex];
	var memberId = curRow.memberId;
	var batNo = curRow.batNo;
	//去除时分秒
	var targetUrl = CONTEXT + "penaltyRecord/penaltyRecordTab";
	targetUrl += "?memberId="+memberId;
	targetUrl += "&batNo="+batNo;
	//切换tab
	TabUtils.addTab("代付违约金明细", targetUrl);
}



function checkRefundNumFormatter(val, row, index) {
	if(val == undefined) {
		return '-';
	}
	if(val == 0) {
		return '-';
	}
	return "<a class='operate' href='javascript:;' onclick=\"checkRefundNum('"+index+"');\" >" + val + "</a>";
}

/**
 * 退款笔数
 * */
function checkRefundNum(rowIndex) {
	var rows = $('#settleAccountdg').datagrid("getRows");
	var curRow = rows[rowIndex];
	var memberId = curRow.memberId;
	var batNo = curRow.batNo;
	
	//去除时分秒
	var targetUrl = CONTEXT + "refundRecord/refundRecordTab";
	targetUrl += "?memberId="+memberId;
	targetUrl += "&batNo="+batNo;
	//切换tab
	TabUtils.addTab("代付退款明细", targetUrl);
}

function formatNumber(v,r) {
	if(v==null) {
		return "-";
	}
	return v;
}

function formatDouble(v,r) {
	if(v!=null) {
		return v.toFixed(2)
	}
	return "-";
}


function formatDate(v,r) {
	if(v!=null) {
		return v;
	}
	return "-";
}
function formatPayStatus(v,r) {
	if(v=="1") {
		return "支付成功";
	} else {
		return "待支付";
	}
}
$("#btn-reset").click(function(){
	$("#settleAccountSearchForm")[0].reset();
	$("#mobile").searchbox("setValue","");
	$("#realName").searchbox("setValue","");
	disableExport=false;
});

$("#btn-export").click(function(){
	var params = getSearchParams();
	var paramList = 'payStatus='+params.payStatus+"&changeTimeBeginTime="+params.changeTimeBeginTime+
	"&changeTimeEndTime="+params.changeTimeEndTime+"&realName="+params.realName+"&mobile="+params.mobile+
	"&payTimeBeginTime="+params.payTimeBeginTime+"&payTimeEndTime="+params.payTimeEndTime+"&exceptionMark="+params.exceptionMark;
	$.ajax({
		url: CONTEXT+'settleAccount/exportCheck',
		data : params,
		type:'post',
		success : function(data){
			//检测签名不通过
			if(data && data.success == true&&data.code=='60003'){
				warningMessage(data.msg);
			//检测通过
			}else if (data && data.success == true){
				if (!disableExport){
					slideMessage("数据正在导出中, 请耐心等待...");
					disableExport = true ;
					//启动下载
					$.download(CONTEXT+'settleAccount/export',paramList,'post' );
				}else{
					slideMessage("已进行过一次数据导出,导出功能已禁用,勿频繁点击导出,若要重新启用导出,请重新查询数据...");
				}
			}else{
				warningMessage(data.msg);
			}
		},
		error : function(data){
			warningMessage("error : " + data.msg);
		}
	});
	$("#settleAccountSearchForm")[0].reset();
});

//代付款-订单笔数详情
function openOrderDetail(memberId,batNo) {
	if(mobile==undefined){
		mobile = '';
	}
	var url = CONTEXT+"paycenter/paidOrderRecord/showOrderList?redirect=1&memberId="+memberId+"&batNo="+batNo+"&hasChange=1";
	addTab("代付款-订单记录", url);
}
//代付款-佣金笔数详情
function openCommissionDetail(memberId,batNo) {
	if(mobile==undefined){
		mobile = '';
	}
	var url = CONTEXT+"paycenter/paidCommissionRecord/showCommissionList?redirect=1&memberId="+memberId+"&batNo="+batNo+"&hasChange=1";
	addTab("代付款-佣金记录", url);
}

function addTab(title,url){
    var jq = top.jQuery; 
    if (jq("#my_tabs").tabs('exists', title)){
    	jq("#my_tabs").tabs('close', title); 
    }
    var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
    jq("#my_tabs").tabs('add',{title:title,content:content,closable:true});        
} 


$("#btn-import").click(function(){   
	  $('#fileImport').val("");
      $('#uploadInfo').html("");
	  $('#fileName').html("");
	$('#importExcelDialog').dialog('open');
});  

function uploadFileData(){
	 $('#uploadInfo').html("");
	  $('#fileName').html("");
	//获取上传文件控件内容
     var file = $('#fileImport').val();
     //判断控件中是否存在文件内容，如果不存在，弹出提示信息，阻止进一步操作
     if (file == null) { slideMessage('错误，请选择文件'); return; }
     //获取文件类型名称
     var file_typename = file.substring(file.lastIndexOf('.'), file.length);
     if(file_typename==null ||file_typename==""){
    	 $('#fileName').html("错误提示:请选择导入的文件")
    	 return;
     }
     //这里限定上传文件文件类型必须为.xlsx，如果文件类型不符，提示错误信息
     if (file_typename == '.xlsx'||file_typename == '.xls'){
    	//计算文件大小
         var fileSize = 0;
         //如果文件大小大于1024字节X1024字节，则显示文件大小单位为MB，否则为KB
         if (file.size > 1024 * 1024) {
        	 fileSize = Math.round(file.size * 100 / (1024 * 1024)) / 100;

        	 if (fileSize > 10) {
                 alert('错误，文件超过10MB，禁止上传！'); return;
             }
        	 fileSize = fileSize.toString() + 'MB';
         }
         else {
             fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
         }
         //获取form数据
         var formData = $("#importFileForm").form().serialize();
         $("#importFileForm").ajaxSubmit({  
        	 url: CONTEXT+'settleAccount/importUpdate',
             type: 'POST',
             data: formData,
             success: function (data) {
                 //上传成功后将控件内容清空，并显示上传成功信息
                 $('#fileImport').val("");
                 $('#uploadInfo').html("导入结果:"+data.msg);
                 $("#settleAccountdg").datagrid('reload');
             }
         });
     } else {
    	 slideMessage("导入格式出错，限导入EXCEL表格");
         //将错误信息显示在前端span文本中
        $('#fileName').html("错误提示:上传文件应该是.xlsx后缀而不应该是" + file_typename + ",请重新选择文件")
     }
}; 


