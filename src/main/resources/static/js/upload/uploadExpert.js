/**
 * Created by Tianshan on 18-11-27.
 */

//初始化上传插件
function initFileInputForExpert(id, url, tableId, tableUrl){
   $("#" + id).fileinput({
	   uploadUrl: url, 				   //上传的地址(访问接口地址)
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
           return obj;
        } 
	}).on('filebatchselected', function(event, files) {
        layer.load(2);
    	$(event.target).fileinput('upload');
    	$("#upload-alert").remove();
    	$('#uploadExpert-table-area').bootstrapTable('destroy');
        $('#uploadExpert-table-errorData').bootstrapTable('destroy');
    }).on("fileuploaded", function (event, data, previewId, index) {
        layer.closeAll();
    	$("#expFileMark").val(data.response.expFileMark);
    	showUploadExpertAlert(data.response.message,data.response.status);
    	//重复序号
		if(data.response.code==-5){
		    if(data.response.errorTable!=null)
		    	showExpertErrorTable(data.response.errorTable);
        }
    	//必填数据
		if(data.response.code==-4){
		    if(data.response.errorData!=null)
		    	showExpertErrorDataTable(data.response.errorData);
        }
        var opt = {
           url: tableUrl+$('#expFileMark').val(),
        };
        $("#expert_table").bootstrapTable('refresh', opt);
		$(event.target).fileinput('clear').fileinput('unlock')
        $(event.target).parent().siblings('.fileinput-remove').hide()
	});
}

//初始化Table
function showExpertErrorTable(tableData){
    var columns = [{
        field: 'row',
        title: '对应Excel行数'
    }, {
        field: 'num',
        title: '序号'
    }, {
        field: 'title',
        title: '专家姓名'
    }];
    $('#uploadExpert-table-area').bootstrapTable(
        {columns: columns,
            data: tableData});
}

//初始化错误数据Table
function showExpertErrorDataTable(tableData){
    var columns = [{
        field: 'num',
        title: '对应Excel行数'
    },{
        field: 'cnName',
        title: '专家姓名'
    }, {
        field: 'cnCompany',
        title: '单位名称'
    }, {
        field: 'cnAddress',
        title: '单位地址'
    }, {
        field: 'cnHomePage',
        title: '专家主页'
    }, {
        field: 'expEmail',
        title: '邮箱'
    }];
    $('#uploadExpert-table-errorData').bootstrapTable(
        {columns: columns,
            data: tableData});
}

//显示登陆错误提示
function showUploadExpertAlert(message,status) {
    $("#upload-alert").remove();
    var msg = [{message: message}];
    if(status){
        $("#tmpl-upload-success").tmpl(msg).appendTo("#uploadExpert-alert-area").ready(function () {
        });
    }
    else{
        $("#tmpl-upload-error").tmpl(msg).appendTo("#uploadExpert-alert-area").ready(function () {
        });
    }
}