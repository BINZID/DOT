/**
 * Created by Tianshan on 18-11-27.
 */

//初始化上传插件
function initFileInputForDistributiondata(id, url, tableId, tableUrl){
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
           return obj;
        } 
	}).on('filebatchselected', function(event, files) {
		layer.load(2);
    	$(event.target).fileinput('upload');
    	$("#upload-alert").remove();
        $('#uploadDistributiondata-table-errorData').bootstrapTable('destroy');
    }).on("fileuploaded", function (event, data, previewId, index) {
    	layer.closeAll();
    	showUploadDistributiondataAlert(data.response.message,data.response.status);
    	//必填数据
		if(data.response.code==-4){
		    if(data.response.errorData!=null)
		    	showDistributiondataErrorDataTable(data.response.errorData);
        }
		var opt = {
	            url: tableUrl + $("#timestamp").val(),
	        };
		$('#distributiondata_table').bootstrapTable('refresh', opt);
		$(event.target).fileinput('clear').fileinput('unlock')
        $(event.target).parent().siblings('.fileinput-remove').hide()
	});
}

//初始化错误数据Table
function showDistributiondataErrorDataTable(tableData){
  var columns = [{
      field: 'num',
      title: '对应Excel行数'
  },{
      field: 'scientificname',
      title: '学名'
  }, {
      field: 'geojson',
      title: '地理实体'
  }, {
      field: 'dismark',
      title: '地理标识'
  }, {
      field: 'dismapstandard',
      title: '地图标准'
  }, {
	  field: 'county',
	  title: '行政区'
  }, {
      field: 'sourcesid',
      title: '数据源'
  }, {
      field: 'expert',
      title: '审核专家'
  }, {
      field: 'refjson',
      title: '参考文献'
  }];
  $('#uploadDistributiondata-table-errorData').bootstrapTable(
      {columns: columns,
          data: tableData});
}

//显示登陆错误提示
function showUploadDistributiondataAlert(message,status) {
  $("#upload-alert").remove();
  var msg = [{message: message}];
  if(status){
      $("#tmpl-upload-success").tmpl(msg).appendTo("#uploadDistributiondata-alert-area").ready(function () {
      });
  }
  else{
      $("#tmpl-upload-error").tmpl(msg).appendTo("#uploadDistributiondata-alert-area").ready(function () {
      });
  }
}
