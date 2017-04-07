var NSTAdmin = {};
NSTAdmin.client = function(path){
	//编写公共服务
};
NSTAdmin.client.prototype = {
		/**
		 * 
		 * @param str
		 * @param length
		 * @returns string
		 * @description 截取指定长度字符串 默认10
		 */
		subString : function(str , length){
			if(undefined == length){
				length = 12;
			}
			if(null == str || str.trim().length <= 0){
				return "";
			}
			str = str.trim();
			if(str.length <= length){
				return str;
			}
			return str.substr(0 , length)+"...";
		},
		/**
		 * 去除字符串左右空格,如果是null,NULL,Null......则返回空白
		 * @returns
		 */
		getString : function (str) {
			if(null == str){
				return "";
			}
			if(typeof(str) != "string"){
				return str;
			}
			if("NULL" == str.toUpperCase().trim()){
				return "";
			}
			return str .trim();
		}
};

/**
 * 去除字符串左右空格
 * 为了兼容IE
 */
String.prototype.trim = function () {
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
};
/** 
 * 时间对象的格式化; 
 */
Date.prototype.format = function(format) {
	/* 
	 * eg:format="yyyy-MM-dd hh:mm:ss"; 
	 */
	var o = {
		"M+" : this.getMonth() + 1, // month  
		"d+" : this.getDate(), // day  
		"h+" : this.getHours(), // hour  
		"m+" : this.getMinutes(), // minute  
		"s+" : this.getSeconds(), // second  
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
		"S" : this.getMilliseconds()
	// millisecond  
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};
