/**
 * 多图片上传
 */

var S = KISSY;
var hnKissyMultipleUploader = (function(){
	return {
		uploader : null ,
		queueIndex : 0 , //队列中的序号,
		uploaderContainer :[],
		/**
 		 * 初始化上传组件 
 		 * @param galleryUrl :惠农网静态资源gallery所在位置
		 * @returns
		 */
		init : function(config,callback,_success,_failure){
			initConfig = {
				  maxSize : 5000,
				  max:8,
				  multiple : true ,
				  type : "ajax"
			};
			$.extend(initConfig,config);
			initConfig = replaceParam(initConfig,true);
		    var srcPath = initConfig.galleryUrl;
		    S.config({
		        packages:[
		            {
		                name:"gallery",
		                path:srcPath,
		                charset:"utf-8"
		            }
		        ]
		    });
		    S.use('gallery/uploader/1.4/index,gallery/uploader/1.4/themes/imageUploader/index,gallery/uploader/1.4/themes/imageUploader/style.css', function (S, Uploader,ImageUploader) {
		        //上传组件插件
		        var plugins = 'gallery/uploader/1.4/plugins/auth/auth,' +
		                'gallery/uploader/1.4/plugins/urlsInput/urlsInput,' +
		                'gallery/uploader/1.4/plugins/proBars/proBars,' +
		                'gallery/uploader/1.4/plugins/filedrop/filedrop,' +
		                'gallery/uploader/1.4/plugins/preview/preview,'+
		                'gallery/uploader/1.4/plugins/imageZoom/imageZoom';
		        
		        S.use(plugins,function(S,Auth,UrlsInput,ProBars,Filedrop,Preview,ImageZoom){
		        	uploader = new Uploader('#'+config.uploadBtn,{
		                //处理上传的服务器端脚本路径
		             	 action:initConfig.action,
		            	 multiple : initConfig.multiple,
						 type: initConfig.type
		            });
		            //使用主题
		            uploader.theme(new ImageUploader({
		                queueTarget:'#'+ config.queueId ,
		                imageUrlPrefix : config.imgUrlPrefix,
						_success : _success,
						_failure : _failure
		            }))
		           //验证插件
                    uploader.plug(new Auth({
                        //最多上传个数
                        max:initConfig.max,
                        //图片最大允许大小
                        maxSize:initConfig.maxSize
                    }))
                    //url保存插件
                    .plug(new UrlsInput({target:'#'+initConfig.JUrls}))
                    //进度条集合
                    .plug(new ProBars())
                    //拖拽上传
                    .plug(new Filedrop())
                    //图片预览
                    .plug(new Preview()).plug(new ImageZoom());
		            //将上传对象添加进入容器中。
		        	var uploaderObject = {id:initConfig.uploadBtn , target : uploader};
		        	hnKissyMultipleUploader.uploaderContainer.push(uploaderObject);
		            if(typeof callback === 'function'){
		            	callback.apply(window,[uploader]);
		            }
		        });
		    });
		},
		
		addFiles :function(files,uploader){
			//默认拿第一个
		   if(!uploader) uploader = this.uploaderContainer[0].target;
		   var theme = uploader.get("theme");
		   var queue = theme.get("queue");
		   if(!files) files = [];
		   var successFiles = queue.getFiles('success');
	       var len = successFiles.length;   
	       var auth = uploader.getPlugin('auth');
	       var max = auth.get('max');
	       var selectNumber = files.length || 0 ; 
	       if(len +  selectNumber> max){
			  alert("文件数量超出限制,最大上传数量为:"+max);          	 
	          return ;            	
	       }
		   $(files).each(function(i){
			   var file = this; 
			   theme._appendEditFileDom(file);
			   //设置目标对象
			   var $target =  $("#queue-file-"+file.id);
			   file.target = $target;
			   //向队列中添加文件。供再次上传判断长度所用
			   queue.get('files').push(file);
			   //初始化时间。删除、左移动、右移动
			   var ev = {index:hnKissyMultipleUploader.queueIndex,file:file,target:$target};
			   //注册事件。源代码中使用的on来处理，无法在注册事件以后再来关联到动态添加的dom上，需要手动添加。
			   theme._addHandler(ev);
			   //放大镜插件
			   var imagezoom = uploader.getPlugin('imageZoom');
			   $img = $(".J_Pic_"+file.id);
			   //修改了放大镜的代码，下面这个参数用做了判断。
			   $img.forUpdate = true ;
			   //注册放大镜事件。
			   imagezoom._renderIMGDD($img);
			   hnKissyMultipleUploader.queueIndex = hnKissyMultipleUploader.queueIndex + 1 ;
		   });
	   },
	   getUploader : function(uploadBtn){
		   var length = this.uploaderContainer.length;
		   if(length > 0 ){
			   for(var i = 0 ; i < length ; i++ ){
				   var uploaderObj = this.uploaderContainer[i];
				   if(uploaderObj &&  uploaderObj.id == uploadBtn){
					   return uploaderObj.target ;
				   }
			   }
		   }
		   return null ;
	   }
	};
})();


/**
 * 单图片上传
 */
var hnKissySingleUploader = (function(){
	return {
		uploader :null,
		uploaderContainer :[],
		init :function(config,callback){
			//默认配置
			var initConfig = {
				  maxSize : 2000,
				  max:1,
    				  type: "ajax"
		    } ;
			$.extend(initConfig,config);
			initConfig = replaceParam(initConfig,false);
		    var srcPath = initConfig.galleryUrl;
		    S.config({
		        packages:[
		            {
		                name:"gallery",
		                path:srcPath,
		                charset:"utf-8"
		            }
		        ]
		    });
		    S.use('gallery/uploader/1.4/index,gallery/uploader/1.4/themes/singleImageUploader/index,gallery/uploader/1.4/themes/singleImageUploader/style.css', function (S, Uploader,SingleImageUploader) {
		        //上传组件插件
		        var plugins = 'gallery/uploader/1.4/plugins/auth/auth,' +
		                'gallery/uploader/1.4/plugins/urlsInput/urlsInput,' +
		                'gallery/uploader/1.4/plugins/proBars/proBars,' +
		                'gallery/uploader/1.4/plugins/filedrop/filedrop,' +
		                'gallery/uploader/1.4/plugins/preview/preview,'+
		                'gallery/uploader/1.4/plugins/imageZoom/imageZoom';
		        S.use(plugins,function(S,Auth,UrlsInput,ProBars,Filedrop,Preview,ImageZoom){
		            var uploader = new Uploader('#'+initConfig.uploadBtn,{
		                //处理上传的服务器端脚本路径
		                action: initConfig.action,
				type: initConfig.type
		            });
		            //使用主题
		            uploader.theme(new SingleImageUploader({
		            	 queueTarget:'#'+ initConfig.queueId ,
		                 imageUrlPrefix : initConfig.imgUrlPrefix
		            }));
		            //验证插件
		            uploader.plug(new Auth({
		                        //最多上传个数
		                        max:initConfig.max,
		                        //图片最大允许大小
		                        maxSize:initConfig.maxSize
		                    }))
		                //url保存插件
		                    .plug(new UrlsInput({target:'#'+initConfig.JUrls}))
		                //进度条集合
		                    .plug(new ProBars())
		                //拖拽上传
		                    .plug(new Filedrop())
		                //图片预览
		                    .plug(new Preview()).plug(new ImageZoom());
		            
		            var uploaderObject = {id:initConfig.uploadBtn , target : uploader};
		            hnKissySingleUploader.uploaderContainer.push(uploaderObject);
		            if(typeof callback === 'function'){
		            	callback.apply(window,[uploader]);
		            }
		            ;
		        });
		    })
		},
		addFile :function(file,uploader){
			   if(!uploader) uploader = this.uploaderContainer[0].target;
			   var theme = uploader.get("theme");
			   var queue = theme.get("queue");
			   var successFiles = queue.getFiles('success');
		       var len = successFiles.length;   
		       var auth = uploader.getPlugin('auth');
		       var max = auth.get('max');
		       if(len +  1> max){
				  alert("文件数量超出限制,最大上传数量为:"+max);          	 
		          return ;            	
		       }
			   theme._appendEditFileDom(file);
			   //设置目标对象
			   var $target =  $("#queue-file-"+file.id);
			   file.target = $target;
			   //向队列中添加文件。供再次上传判断长度所用
			   queue.get('files').push(file);
			   //初始化时间。删除、左移动、右移动
			   var ev = {index:hnKissySingleUploader.queueIndex,file:file,target:$target};
			   //注册事件。源代码中使用的on来处理，无法在注册事件以后再来关联到动态添加的dom上，需要手动添加。
			   theme._addHandler(ev);
			   //放大镜插件
			   var imagezoom = uploader.getPlugin('imageZoom');
			   $img = $(".J_Pic_"+file.id);
			   //修改了放大镜的代码，下面这个参数用做了判断。
			   $img.forUpdate = true ;
			   //注册放大镜事件。
			   imagezoom._renderIMGDD($img);
			   hnKissySingleUploader.queueIndex = hnKissySingleUploader.queueIndex + 1 ;
		     },
			 getUploader : function(uploadBtn){
			   var length = this.uploaderContainer.length;
			   if(length > 0 ){
				   for(var i = 0 ; i < length ; i++ ){
					   var uploaderObj = this.uploaderContainer[i];
					   if(uploaderObj &&  uploaderObj.id == uploadBtn){
						   return uploaderObj.target ;
					   }
				   }
			   }
			   return null ;
		   }
	}
})();


/**
 * 文件上传
 */
var hnKissyFileUploader = (function(){
	 return{
		 uploader :null,
		 init :function(config,callback){
			//默认配置
			var initConfig = {
					  maxSize : 2000,
					  max:1,
					  type :'iframe'
			    } ;
				$.extend(initConfig,config);
			    var srcPath = initConfig.galleryUrl;
			    S.config({
			        packages:[
			            {
			                name:"gallery",
			                path:srcPath,
			                charset:"utf-8"
			            }
			        ]
			 });
			 
			 S.use('gallery/uploader/1.4/index,gallery/uploader/1.4/themes/default/index,gallery/uploader/1.4/themes/default/style.css', function (S, Uploader,DefaultTheme) {
			        //上传组件插件
			        var plugins = 'gallery/uploader/1.4/plugins/auth/auth,' +
			                'gallery/uploader/1.4/plugins/urlsInput/urlsInput,' +
			                'gallery/uploader/1.4/plugins/proBars/proBars';

			        S.use(plugins,function(S,Auth,UrlsInput,ProBars){
			        	var uploader = new Uploader('#'+initConfig.uploadBtn,{
			                //处理上传的服务器端脚本路径
			             	 action:initConfig.action,
			             	 type : initConfig.type
			            });
			            //使用主题
			            uploader.theme(new DefaultTheme({
			                //queueTarget:'#J_UploaderQueue'
			                queueTarget:'#'+ initConfig.queueId 
			            }));
			                    //验证插件
			            uploader.plug(new Auth({
			                        //最多上传个数
			                        max:initConfig.max,
			                       //图片最大允许大小
			                        maxSize:initConfig.maxSize
			            }))
			                     //url保存插件
			            .plug(new UrlsInput({target:'#'+initConfig.JUrls}))
			                    //进度条集合
			            .plug(new ProBars()) ;
			            hnKissyFileUploader.uploader = uploader;
			            if(typeof callback === 'function'){
			            	callback.apply(window,[uploader]);
			            }
			        });
			  })
		 }
	 }
})();

/**
 * 替换参数，因为IE内核下不支持跨域上传。iframe上传不支持多文件上传。在IE内核下直接设置为iframe形式以及单文件上传
 **/
function replaceParam(param, multipAble){
   if($.browser.msie){
	   //ie状态下
		param['type'] = "iframe";
		param['multiple'] = false ; 
   }else{
		param['type'] = "ajax"; 
		if(multipAble)
	    param['multiple'] = true ; 
		var action = param.action ;
		if(action)
		param['action'] = removeDomain(action);

   }
   return param ;
}


// http://aaa.cnhnkj.cn?domain=cnhnkj.cn
// http://aaa.cnhnkj.cn?domain=cnhnkj.cn&aaa=1&domain=2
function removeDomain(action){ 
  return action.replace(/domain=(\w+\.\w+)?/g,"domain="); 
}