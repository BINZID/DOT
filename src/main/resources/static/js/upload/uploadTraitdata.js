/**
 * Created by Tianshan on 18-11-27.
 */

//初始化所有select组件
$(document).ready(function () {
    buildSelect2("traitset", "console/traitset/rest/selectForUpload", "请先为上传的特征数据选择一个术语集");
    
    // Form2下的Traitset变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="430px";
    }
    $("select[id = traitset]").on("change", function(){
    	if($("#traitset").val()=="addNew"){
    		$("#traitset").empty();
    		layer.open({
    			type: 2,
    			title:'<h4>添加术语集</h4>',
    			fixed: false, //不固定
    			area: [layer_width, layer_height],
    			content: '/console/traitset/addNew'
    		});
    	}
    });
});

//初始化上传插件
function initFileInputForTraitdata(id, url, tableId, tableUrl){
	$("#" + id).fileinput({
	   uploadUrl: url + "/" + $("#taxasetId").val(), //上传的地址(访问接口地址)
	   language: "zh",                 //设置语言
	   uploadAsync: true,              //异步上传
	   showCaption: true,              //是否显示标题
	   showUpload: false,              //是否显示上传按钮
	   showRemove: true,               //是否显示移除按钮
	   showPreview : false,            //是否显示预览按钮
	   browseClass: "btn btn-primary", //按钮样式 
	   dropZoneEnabled: false,         //是否显示拖拽区域
	   minFileCount: 1,
       maxFileCount: 1,                //最大上传文件数限制
       maxFileSize:51200,
       allowedFileExtensions: ["xls", "xlsx"], //接收的文件后缀
       msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
       uploadExtraData: function(previewId, index) {   //额外参数的关键点
    	   var obj = {};
           obj._csrf = $('input[name="_csrf"]').val();
           obj.refFileMark = $("#refFileMark").val();
           obj.dsFileMark = $("#dsFileMark").val();
           obj.expFileMark = $("#expFileMark").val();
           obj.traitset = $("#traitset").val();
           return obj;
        } 
	}).on('filebatchselected', function(event, files) {
		layer.load(2);
    	$(event.target).fileinput('upload');
    	$("#upload-alert").remove();
        $('#uploadTraitdata-table-errorData').bootstrapTable('destroy');
    }).on("fileuploaded", function (event, data, previewId, index) {
    	layer.closeAll();
    	showUploadTraitdataAlert(data.response.message,data.response.status);
    	//必填数据
		if(data.response.code==-4){
		    if(data.response.errorData!=null)
		    	showTraitdataErrorDataTable(data.response.errorData);
        }
		var opt = {
	            url: tableUrl + $("#timestamp").val(),
	        };
		$('#traitdata_table').bootstrapTable('refresh', opt);
		$(event.target).fileinput('clear').fileinput('unlock')
        $(event.target).parent().siblings('.fileinput-remove').hide()
	});
}


//初始化错误数据Table
function showTraitdataErrorDataTable(tableData){
  var columns = [{
      field: 'num',
      title: '对应Excel行数'
  },{
      field: 'scientificname',
      title: '学名'
  },{
	  field: 'traitjson',
	  title: '特征数据'
  }, {
      field: 'sourcesid',
      title: '数据源'
  }, {
      field: 'expert',
      title: '审核专家'
  }];
  $('#uploadTraitdata-table-errorData').bootstrapTable(
      {columns: columns,
          data: tableData});
}

//显示登陆错误提示
function showUploadTraitdataAlert(message,status) {
  $("#upload-alert").remove();
  var msg = [{message: message}];
  if(status){
      $("#tmpl-upload-success").tmpl(msg).appendTo("#uploadTraitdata-alert-area").ready(function () {
          //$('#upload-alert-error').fadeIn();
      });
  }
  else{
      $("#tmpl-upload-error").tmpl(msg).appendTo("#uploadTraitdata-alert-area").ready(function () {
          //$('#upload-alert-error').fadeIn();
      });
  }
}

//拼接option
function formatRepo(repo) {
    if (repo.loading) return repo.text;
    var markup = repo.text;
    return markup;
}
//拼接option
function formatRepoSelection(repo) {
    return repo.text || repo.text;
}
//构造select2的方法
function buildSelect2(select_id, url, content) {
    $("#" + select_id).select2({
        language: "zh-CN",
        placeholder: content,
        ajax: {
            url: url,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    find: params.term,
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * 30) < data.total_count
                    }
                };
            },
            cache: true
        },
        escapeMarkup: function (markup) {
            return markup;
        },
        minimumInputLength: 0,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection
    });
}
