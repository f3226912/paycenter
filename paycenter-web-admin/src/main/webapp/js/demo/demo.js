$(document).ready(function(){
	//数据加载
	$('#demodg').datagrid({
		url:CONTEXT+'demo/query',
		//width: 1000,  
		height: 'auto', 
		nowrap:true,
		toolbar:'#demotb',
		pageSize:10,
		rownumbers:true,
		pagination:true,
		fitColumns:true,
		fit:true,
		columns:[[
			{field:'id',title:'',width:50,checkbox:true},
			{field:'name',title:'文件名称',width:100,align:'center'},
			{field:'opt',title:'操作',width:100,align:'center',formatter:function(value,row,index){
				return "<a class='operate' href='javascript:;' onclick=delObj('"+row.id+"');>删除</a>";
			}}
		]]
	}); 
	//分页加载
	$("#demodg").datagrid("getPager").pagination({
		pageList: [10,20,50,100]
	});
	
});