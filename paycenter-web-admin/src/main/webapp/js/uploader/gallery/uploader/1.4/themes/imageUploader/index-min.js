/**
 * @fileoverview 图片上传主题（带图片预览），第一版由紫英同学完成，苏河同学做了大量优化，明河整理优化
 * @author 苏河、紫英、明河
 **/
KISSY.add('gallery/uploader/1.4/themes/imageUploader/index',function (S, Node, Theme) {
    var EMPTY = '', $ = Node.all;

    /**
     * @name ImageUploader
     * @class 图片上传主题（带图片预览），第一版由紫英同学完成，苏河同学做了大量优化，明河整理优化
     * @constructor
     * @extends Theme
     * @requires Theme
     * @requires  ProgressBar
     * @author 苏河、紫英、明河
     */
    function ImageUploader(config) {
        var self = this;
        //调用父类构造函数
        ImageUploader.superclass.constructor.call(self, config);
    }

    S.extend(ImageUploader, Theme, /** @lends ImageUploader.prototype*/{
        /**
         * 在完成文件dom插入后执行的方法
         * @param {Object} ev 类似{index:0,file:{},target:$target}
         */
        _addHandler:function(ev){
            var self = this;
            var file = ev.file;
            var id = file.id;
            var $target = file.target;
            var $delBtn = $('#imageUploader_del_'+id) ;
            $delBtn.data('data-file',file);
            //点击删除按钮
            $delBtn.on('click',self._delHandler,self);            
            var $leftMvBtn = $('#imageUploader_but1_'+id) ;
            $leftMvBtn.data('data-file',file);
            $leftMvBtn.on('click',self._leftMove,self);            
            var $rightMvBtn = $('#imageUploader_but2_'+id) ;
            $rightMvBtn.data('data-file',file);
            $rightMvBtn.on('click',self._rightMove,self);     
            $(self.userConfig.queueTarget).removeClass("multPic-box")
        },
        /**
         * 删除文件后重新计算下上传数
         * @private
         */
        _removeHandler:function(){
            this._setCount();
        },
        /**
         * 文件处于开始上传状态时触发
         */
        _startHandler:function (ev) {

        },
        /**
         * 文件处于正在上传状态时触发
         */
        _progressHandler:function (ev) {

        },
        /**
         * 文件处于上传成功状态时触发
         */
        _successHandler:function (ev) {
            var self = this;
            var file = ev.file;
            var id = file.id;
            //服务器端返回的数据
            var result = file.result;
            self._setCount();
            //获取服务器返回的图片路径写入到src上
            if(result) self._changeImageSrc(ev);
            $('.J_Mask_'+id).hide();

            //如果不存在进度条插件，隐藏进度条容器
            var uploader = self.get('uploader');
            var proBars = uploader.getPlugin('proBars');
            if(!proBars){
                var target = file.target;
                if(!target) return false;
                target.all('.J_ProgressBar_'+id).hide();
            }		
			 
			var _success = self.get('_success');
			if(typeof _success === 'function' && result && result.url){
				_success.apply(window,[result.url]);
			}
        },
         /**
         * 文件处于上传错误状态时触发
         */
        _errorHandler:function (ev) {
             var self = this;
             var msg = ev.msg || ev.result.msg || ev.result.message;
             var file = ev.file;
             if(!file) return false;
             var id = ev.file.id;
            //打印错误消息
            $('.J_ErrorMsg_' + id).html(msg);
             self._setDisplayMsg(true,ev.file);
             //向控制台打印错误消息
             S.log(msg);
			 var _failure = self.get('_failure');
			 if(typeof _failure === 'function'){
				_failure.apply(window,[msg]);
			}
        },
        /**
         * 显示“你还可以上传几张图片”
         */
        _setCount:function(){
            var self = this;
            //用于显示上传数的容器
            var elCount = self.get('elCount');
            if(!elCount.length) return false;
            var uploader = self.get('uploader');
            var auth = uploader.getPlugin('auth') ;
            if(!auth) return false;

            var max = auth.get('max');
            if(!max) return false;

            var len = self.getFilesLen();

            elCount.text(Number(max)-len);
        },
        /**
         * 显示/隐藏遮罩层（遮罩层在出现状态消息的时候出现）
          * @param isShow
         * @param data
         * @return {Boolean}
         * @private
         */
        _setDisplayMsg:function(isShow,data){
            if(!data) return false;
            var $mask = $('.J_Mask_' + data.id);
            //出错的情况不允许隐藏遮罩层
            if($mask.parent('li') && $mask.parent('li').hasClass('error')) return false;
            $mask[isShow && 'show' || 'hide']();
        },
        /**
         * 删除图片后触发
         */
        _delHandler:function(ev){
            var self = this;
            var uploader = self.get('uploader');
            var queue = uploader.get('queue');
            var file = $(ev.target).data('data-file');
            var index = queue.getFileIndex(file.id);
            var status = file.status;
            //如果文件还在上传，取消上传
             if(status == 'start' || status == 'progress'){
                 uploader.cancel(index);
             }
            queue.remove(index);
            if($(self.userConfig.queueTarget).children().length-1==0){
                $(self.userConfig.queueTarget).addClass("multPic-box")
            }
        },
        /**
         * 获取成功上传的图片张数，不传参的情况获取成功上传的张数
         * @param {String} status 状态
         * @return {Number} 图片数量
         */
        getFilesLen:function(status){
            if(!status) status = 'success';
            var self = this,
            queue = self.get('queue'),
            //成功上传的文件数
            successFiles = queue.getFiles(status);
            return successFiles.length;
        },
        /**
         * 将服务器返回的图片路径写到预览图片区域，部分浏览器不支持图片预览
          */
        _changeImageSrc:function(ev){
        	  var self = this;
            var file = ev.file;
            var id = file.id;
            var result = ev.result;
            var url = result.url;
            var $img = $('.J_Pic_' + id);
            //if($img.attr('src') == EMPTY || S.UA.safari){
                $img.show();     
                if(url != null) {
                	var urlArray = url.split(",");
                	if(urlArray.length >= 0 ){
                	var $imageUrlPrefix = self.get('imageUrlPrefix');
                	if(!$imageUrlPrefix) $imageUrlPrefix = "";
                    var src = '';
                    if(urlArray[0].indexOf($imageUrlPrefix) >= 0 ){
                        src = urlArray[0];
                    }else{
                        src = $imageUrlPrefix+urlArray[0];
                    }
                	 	
                	$img.attr('src',src);                
                }
              }
            //}
        },
        /**
        * 向左边移动
        */        
        _leftMove : function(ev){   
        	 var  self = this;      	
        	 var $queueTarget = self.get('queueTarget');
        	 var file = $(ev.target).data('data-file');
        	 var id = file.id ;
        	 var previousId = 0 ;
        	  $queueTarget.children().each(function(index){
        	 	   var liId = $(this).attr("id");        	 	   
        	 	   if(liId === 'queue-file-'+id){
        	 	   	 if(index == 0){
        	 	   	    //如果第第一个 则不处理 	
        	 	   	 	return false;
        	 	   	 }else{        	 	   	 	
        	 	   	 	$(this).insertBefore("#"+previousId);
        	 	   	 	return false ;
        	 	   	 }        	 	   	
        	 	   }
        	 	   previousId = liId;        	 	
        	 	}); 
        	 
        },
        //向右移动
        _rightMove : function(ev){           
        	 var self = this;      	
        	 var $queueTarget = self.get('queueTarget');
        	 var file = $(ev.target).data('data-file');
        	 var id = file.id ;
        	 var target_file_id = 0 ;
        	 var isStop = false ;
        	  $queueTarget.children().each(function(index){
        	 	   var liId = $(this).attr("id");        	 	   
        	 	   if(liId === 'queue-file-'+id){        	 	     
        	 	   	 target_file_id = liId ;
        	 	   	 isStop = true ;        	 	   	         	 	   	
        	 	   }else{
        	 	   	 if(isStop)
        	 	   	 {
        	 	   	 	$(this).insertBefore("#"+target_file_id);
        	 	   	 	 return false ;
        	 	   	 }
        	 	   }      	 	
        	 	}); 
        	 	if(isStop)return ;
       }
    }, {ATTRS:/** @lends ImageUploader.prototype*/{
        /**
         *  主题名（文件名），此名称跟样式息息相关
         * @type String
         * @default "imageUploader"
         */
        name:{value:'imageUploader'},
        /**
         * 队列使用的模板
         * @type String
         * @default ""
         */
        fileTpl:{value:
            '<li id="queue-file-{id}" class="g-u" data-name="{name}">' +
                '<div class="pic">' +
                    '<a href="javascript:void(0);"><img class="J_Pic_{id} preview-img" src="" /></a>' +
                '</div>' +
                '<div class=" J_Mask_{id} pic-mask"></div>' +
                '<div class="status-wrapper">' +
                    '<div class="status waiting-status"><p>等待上传，请稍候</p></div>' +
                    '<div class="status start-status progress-status success-status">' +
                        '<div class="J_ProgressBar_{id}"><s class="loading-icon"></s>上传中...</div>' +
                    '</div>' +
                    '<div class="status error-status">' +
                        '<p class="J_ErrorMsg_{id}">服务器故障，请稍候再试！</p></div>' +
                '</div>' +
                '<div class="imageUploader_menu"> <a href="javascript:void(0);" class="imageUploader_but1" id="imageUploader_but1_{id}"  title="左移">&nbsp;</a><a href="javascript:void(0);" class="imageUploader_but2" title="右移"  id="imageUploader_but2_{id}">&nbsp;</a><a href="javascript:void(0);" class="imageUploader_del" id="imageUploader_del_{id}" title="删除" >&nbsp;</a> </div>' +
            '</li>'
        },
        editFileTpl:{value:
            '<li id="queue-file-{id}" class="g-u success" data-name="{name}">' +
                '<div class="pic">' +
                    '<a href="javascript:void(0);"><img class="J_Pic_{id} preview-img J_ImgDD" src="{url}" data-original-url="{dataOriginalUrl}"  style="display: inline;" /></a>' +
                '</div>' +
                '<div class=" J_Mask_{id} pic-mask" style="display: none;></div>' +
                '<div class="status-wrapper">' +                  
                '</div>' +
                '<div class="imageUploader_menu"> <a href="javascript:void(0);" class="imageUploader_but1" id="imageUploader_but1_{id}"  title="左移">&nbsp;</a><a href="javascript:void(0);" class="imageUploader_but2" title="右移"  id="imageUploader_but2_{id}">&nbsp;</a><a href="javascript:void(0);" class="imageUploader_del" id="imageUploader_del_{id}" title="删除" >&nbsp;</a> </div>' +
            '</li>'
        },
        /**
         * 允许上传的文件类型
         * @since 1.4
         * @type String
         * @default jpg,png,gif,jpeg
         */
        allowExts:{
            value:'jpg,png,gif,jpeg,bmp'
        },
        /**
         * 验证消息
         * @type Object
         * @since 1.4
         * @default {}
         */
        authMsg:{
            value:{
                max:'每次最多上传{max}个图片！',
                maxSize:'图片超过{maxSize}！',
                required:'至少上传一张图片！',
                allowExts:'不支持{ext}格式！',
                allowRepeat:'该图片已经存在！',
                widthHeight:'图片尺寸不符合要求！'
            }
        },
        /**
         * 统计上传张数的容器
         * @type KISSY.NodeList
         * @default '#J_UploadCount'
         */
        elCount:{
            value:'#J_UploadCount',
            getter:function(v){
                return $(v);
            }
        }
    }});
    return ImageUploader;
}, {requires:['node', 'gallery/uploader/1.4/theme']});