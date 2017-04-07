<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="../pub/constants.inc" %>
		<%@ include file="../pub/head.inc" %>
		<%@ include file="../pub/tags.inc" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=7, IE=9, IE=10, IE=11, IE=12"/>
		<title>结算记录</title>
	</head>
	  <script type="text/javascript" src="${CONTEXT}js/My97DatePicker/WdatePicker.js"></script> 
<body>
	<table id="settleAccountdg" title="">
	</table>
	<div id="settleAccounttb" style="padding:5px;height:auto">
		<form id="settleAccountSearchForm" method="post">
		<div>
			代付状态：
					<select type="text" id="payStatus" name="payStatus">
						<option value="">全部</option>
						<option value="0">待支付</option>
						<option value="1">支付成功</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
			异常标记：
					<select type="text" id="exceptionMark" name="exceptionMark">
						<option value="">全部</option>
						<option value="1">1次</option>
						<option value="2">2次</option>
						<option value="3">3次</option>
						<option value="4">超过3次</option>
					</select>
			&nbsp;&nbsp;&nbsp;&nbsp;
			收款方手机号: <input class="easyui-searchbox" type="text" id="mobile" name="mobile" style="width:150px" data-options="prompt:'请输入收款方手机号'">&nbsp;&nbsp;&nbsp;&nbsp;
			持卡人姓名: <input class="easyui-searchbox" type="text" id="realName" name="realName" style="width:150px" data-options="prompt:'请输入持卡人姓名'">&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="margin-top:10px;">
			转结算时间: 
			<input  type="text" id="changeTimeBeginTime" name="changeTimeBeginTime"  
					onFocus="WdatePicker({onpicked:function(){changeTimeBeginTime.focus();},maxDate:'#F{$dp.$D(\'changeTimeEndTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){changeTimeBeginTime.focus();},maxDate:'#F{$dp.$D(\'changeTimeEndTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> -
			<input  type="text" id="changeTimeEndTime" name="changeTimeEndTime"   
					onFocus="WdatePicker({onpicked:function(){changeTimeEndTime.focus();},minDate:'#F{$dp.$D(\'changeTimeBeginTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){changeTimeEndTime.focus();},minDate:'#F{$dp.$D(\'changeTimeBeginTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;
			
			代付时间: 
			<input  type="text" id="payTimeBeginTime" name="payTimeBeginTime"  
					onFocus="WdatePicker({onpicked:function(){payTimeBeginTime.focus();},maxDate:'#F{$dp.$D(\'payTimeEndTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){payTimeBeginTime.focus();},maxDate:'#F{$dp.$D(\'payTimeEndTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="开始时间"> -
			<input  type="text" id="payTimeEndTime" name="payTimeEndTime"   
					onFocus="WdatePicker({onpicked:function(){payTimeEndTime.focus();},minDate:'#F{$dp.$D(\'payTimeBeginTime\')}',dateFmt:'yyyy-MM-dd'})"   
					onClick="WdatePicker({onpicked:function(){payTimeEndTime.focus();},minDate:'#F{$dp.$D(\'payTimeBeginTime\')}',dateFmt:'yyyy-MM-dd'})" 
					style="width:100px" placeholder="结束时间">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			<a class="easyui-linkbutton" iconCls="icon-search" id="icon-searchList" OnClick ="return $('#settleAccountSearchForm').form('validate');">查询</a>&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-reload" id="btn-reset" >重置</a>
			
		</div>
			<div style="background:#efefef;padding:5px 0 5px 0;height:25px;">
			<div style="float: left;font-size:14px;margin-left:5px;">结算信息列表</div>
			<div style="float:right;margin-right:10px;">
					<gd:btn btncode='BTNJSGL01'>	
						<a class="easyui-linkbutton" iconCls="icon-help" onclick="openException()">标记为异常</a>&nbsp;&nbsp;
					</gd:btn>
					<gd:btn btncode='BTNJSGL02'>	
						<a class="easyui-linkbutton" iconCls="icon-account-excel" id="btn-import">导入数据</a>&nbsp;&nbsp;
					</gd:btn>
					<gd:btn btncode='BTNJSGL03'>	
						<a class="easyui-linkbutton" iconCls="icon-account-excel" id="btn-export">导出EXCEL</a>
					</gd:btn>
			</div>
		</div>
		</form>
	</div>
	<div id="updateSettleAccountDialog" class="easyui-dialog" style="width:600px;height:220px;" closed="true" modal="true">
	</div>
	
	<div id="addSettleAccountErrorDialog" class="easyui-dialog" style="width:400px;height:220px;" closed="true" modal="true">
	<div style="margin: 0px 5px">
		<div style="padding: 5px 0px 5px 8px; border-bottom: 1px solid #ccc;"></div>
		<input type="hidden" id="idMark"/>
		<input type="hidden" id="accountMark"/>
		<input type="hidden" id="mobileMark"/>
		<input type="hidden" id="depositBankNameMark"/>
		<table class="grid" align="center" width="100%">
				<tr>
					<td height="20px">标记原因:</td>
					<td height="20px"><textarea id="markReason" cols=40 rows=4></textarea></td>
				</tr>
				<tr>
					<td height="20px">关联银行卡:</td>
					<td height="20px"><span id="bankNoMark"></span></td>
				</tr>
				<tr>
					<td height="20px"></td>
					<td height="20px"><input id="checkboxValidate" name="checkboxValidate" type="checkbox"/>标记银行卡验证不通过</td>
				</tr>
			</table>
	</div>
	<div id="addSettleAccountError" style="text-align:center;padding: 15px 0px 5px 3px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:addErrorSubmit()">提交保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addSettleAccountErrorDialog').dialog('close')">关闭</a>
		</div>
	</div>
	
	<div class="easyui-dialog" id="importExcelDialog" title="导入结算信息" style="width:400px;height:180px;" closed="true" modal="true">
       <form id="importFileForm"  method="post" enctype="multipart/form-data" >
        <table style="margin:5px;height:70px;">
            <tr>
                <td width="5px;" ></td>
                <td><input type="file" class="easyui-filebox" id="fileImport" name="fileImport" style="width:260px;"></td>
                <td></td>
            </tr>
            <tr>
                <td colspan="4" height="20px"><span id="fileName"  style="text-align: center;color: red"/></td>
            </tr>
            <tr>
                <td colspan="4">
                    &nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;text-align: center" id="uploadInfo" />
                </td>
            </tr>
        </table>
        <div style="text-align:center;padding: 30px 0px 5px 3px;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'"  id="btn-uploadFile" onclick="javascript:uploadFileData()">上传</a>&nbsp;&nbsp;
         	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#importExcelDialog').dialog('close')">关闭</a>
        </div>
        </form>
</div>
	
</body>
<script type="text/javascript" src="${CONTEXT}js/settleAccount/main.js"></script>
<script type="text/javascript" src="${CONTEXT}js/easyui-utils.js"></script>
<script type="text/javascript">
function optFormat(value,row,index){
	var opt = "";
	opt += "<gd:btn btncode='BTNJSGL04'><a class='operate' href='javascript:;' onclick=updateSettleAccountDetail('"+row.id+"','query');>查看</a></gd:btn>";
	opt += "<gd:btn btncode='BTNJSGL05'><a class='operate' href='javascript:;' onclick=updateSettleAccountDetail('"+row.id+"','edit');>结算</a></gd:btn>";
	return opt;
}
</script>
</html>
