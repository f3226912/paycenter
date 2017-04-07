/** 操作提示*/
function slideMessage(msg){
	parent.$.messager.show({
		title:'操作提示',
		msg:msg,
		timeout:3000,
		showType:'slide'
	});
}	

/** 错误（失败）操作提示 */
function errorMessage(msg){
	parent.$.messager.alert("操作提示",msg,"error");
}	

/** 警告操作提示 */
function warningMessage(msg){
	parent.$.messager.alert("操作提示",msg,"warning");
}	

/**
 * 获取树的所有选中节点，系统信息作为根节点,[且包含子节点非全选仍包含父节点]
 * @param objId
 * @returns {String}
 * @author songhui
 */
function getAllChecked(objId){
	var treeChecked=$("#"+objId).tree("getChecked");
	//父节点
	var parentNode=null;
	var parentId="";
	var parnetIdStr="";
	var childrenStr="";
	for(var i=0;i<treeChecked.length;i++){
		//只遍历第二级菜单
		if(treeChecked[i].menuLevel==2){
			//获取父节点
			parentNode=$("#"+objId).tree('getParent',treeChecked[i].target);
			if(parentNode!=null){
				if(parentId!=parentNode.id){
					parentId=parentNode.id;
					parnetIdStr+=parentNode.id+",";
				}
			}
			if(i+1<treeChecked.length){
				childrenStr+=treeChecked[i].id+",";
			}
			if(i+1==treeChecked.length){
				childrenStr+=treeChecked[i].id;
			}
		}
	}
	return parnetIdStr+childrenStr;
}

/**
 * 取列表id集合
 * @param objId
 * @return objStr id集合
 */
function getSelections(objId){
	var objStr = "";
	$("[name='" + objId + "']").each(function(){
	    if(this.checked){
	      var value = $(this).val();
	      if(objStr!=""){
	    	  objStr += ","+value;
	      }else{
	    	  objStr = value;
	      }
	    }
    });
	return  objStr;
}

//单行先择
function singleChose(id){

	var objStr = "";
	var count=0;
	$("[name='"+id+"']").each(function(){
	    if(this.checked){
	      var value = $(this).val();
	      if(objStr!=""){
	    	  objStr += ","+value;
	      }else{
	    	  objStr = value;
	      }
		  count++;
	    }
    });
	//请选择数据
	if(count==0){
		return "noData";
	}
	//只能选择一条数据
	if(count>1){
		return "dataError";
	}
	return objStr;
}

/**
 * json转标准的树格式
 * @author tanjun
 * @param rows
 * @return 树对象
 */
function convert(rows){
    var nodes = [];    
    // 得到顶层节点  
    for(var i=0; i<rows.length; i++){    
        var row = rows[i];   
        //如果父ID为""，则为顶层节点
		if(row.parentID=="" || row.parentID=="0"){
            nodes.push({
                id:row.id,   
                text:row.name,
                checked:row.checked,
                attributes:{   
                	appTypeFor:row.appTypeFor,
                	level:row.level
                }   
            });
		}
    }
    if(nodes.length==0){
    	//没有父节点时只加载子集
    	var nodes2 = [];
    	for(var i=0; i<rows.length; i++){    
             var row = rows[i];    
             nodes2.push({
                 id:row.id,   
                 text:row.name,
                 checked:row.checked,
                 attributes:{   
                	appTypeFor:row.appTypeFor,
                 	level:row.level
                 }   
             });
         }  
    	 return nodes2;
    }
    var toDo = []; 
    for(var i=0; i<nodes.length; i++){    
        toDo.push(nodes[i]);    
    }    
    while(toDo.length){    
    	// 父节点
        var node = toDo.shift();   
        // 得到子节点   
        for(var i=0; i<rows.length; i++){    
            var row = rows[i];    
            if (row.parentID == node.id){    
                var child = {id:row.id,text:row.name,checked:row.checked,attributes:{appTypeFor:row.appTypeFor,level:row.level}};    
                if (node.children){    
                    node.children.push(child);    
                } else {    
                    node.children = [child];    
                }    
                toDo.push(child);    
            }    
        }    
    }    
    return nodes;    
}

//禁用按键
function keyDown(){
	var k = window.event.keyCode;
	//禁用回退键
	if (8 == k) {
		window.event.keyCode=0;
		window.event.returnValue=false;
		return false;
	}
}

//自动生成年份
function createYear(startY){
	
	
}

//更新当前tab页  url:页面url,title:tab显示名称，iconCls：图标，如果没有就传null
function updateCurTab(url,title){
	//创建页面隐藏元素，存原始title
	var iframe = '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	var t = $('#my_tabs');
	var tab = t.tabs('getSelected'); // 获取选择的面板
	//更新页面内容
	t.tabs('update', {
		tab:tab,
		options: {
			title:title,
			content: iframe
		}
	});
}

//新增tab页
function addTab(params) {
	var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	var t = $('#my_tabs');
	if (t.tabs('exists', params.title)) {
		t.tabs('select', params.title);
		var tab = t.tabs('getSelected');  // 获取选择的面板
		//刷新页面
		t.tabs('update', {
			tab:tab,
			options: {
				content: iframe
			}
		});
	} else {
		t.tabs('add', {
			title: params.title,
			closable: true,
			content: iframe,
			border: false,
			iconCls:params.iconCls
		});
	}
}

//打开tab页：url:页面url,title:tab显示名称，iconCls：图标，如果没有就传null
function openTab(url,title){
	addTab({
		url : url,
		title : title,
		iconCls : null
	});
}

//文本字段截取显示title
function formaterLongText(value,row,index){
	var remark;
	if(value!=undefined){
		remark=value;
		if(value.length>50){
			remark=value.substring(0,50);
		}
		return "<span title=\""+value+"\">"+remark+"</span>";
	}
}

jQuery.download = function(url, data, method){
	// 获得url和data
    if( url && data ){
        // data 是 string或者 array/object
        data = typeof data == 'string' ? data : jQuery.param(data);
        // 把参数组装成 form的  input
        var inputs = '';
        jQuery.each(data.split('&'), function(){
            var pair = this.split('=');
            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
        });
        // request发送请求
        jQuery('<form action="'+ url +'" method="'+ (method || 'post') +'">'+inputs+'</form>')
        	.appendTo('body').submit().remove();
    };
};