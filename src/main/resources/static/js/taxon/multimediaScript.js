/**
 * Created by Tianshan on 18-5-22.
 */
//删除一个新观测记录
function removeMultimedia(multimediaNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/multimedia/rest/delete",
		{
			"_csrf":$('input[name="_csrf"]').val(),
			"multimediaId":$("#multimediaId_" + multimediaNum).val()
		},
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 500}, function() {
					 $("#multimediaForm_" + multimediaNum).remove();
				})
			}else {
				layer.msg('操作失败', function(){})
			}
		})
	}else {
		layer.msg(
			'操作取消', 
			{time : 500}
		)
	}
}

//提交一个观测记录
function submitMultimedia(multimediaNum) {
    multimediaFormValidator(multimediaNum);
   
    if ($('#multimediaForm_' + multimediaNum).data('bootstrapValidator').isValid() &&
            ($("tr[id^='"+'multimediaReferencesForm_'+multimediaNum+"_']").length<=0 || referencesValidator('newMultimediaReferences_'+multimediaNum,2))) {
        	// 处理Ajax提交
            var obj = $('#multimediaForm_' + multimediaNum).serialize();
            var url = "/console/multimedia/rest/add/" + $('#taxonId').val();
            var postSuccess=false;
            return $.ajax({
                type: "POST",
                url: url,
                data: obj,	// 要提交的表单
                dataType: "json",
                contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
                success: function (msg) {
    	          	if (msg.result == true) {
                        layer.msg('提交成功，请继续填写其他内容', {time: 1500}, function () {
                                if ($('#multimediaCollapse_' + multimediaNum).hasClass('in')) {
                                    $('#multimediaCollapseTitle_' + multimediaNum).trigger("click");
                                }
                                $('#multimediaForm_' + multimediaNum).removeClass("panel-default");
                                $('#multimediaForm_' + multimediaNum).removeClass("panel-danger");
                                $('#multimediaForm_' + multimediaNum).addClass("panel-success");
                                $('#multimediaStatus_' + multimediaNum).removeClass("hidden");
                                postSuccess = true;
                            });
    				}else{
                        layer.msg("添加失败", function () {
                        });
                        postSuccess = false;
    				}
                },
                error: function() {
                    postSuccess = false;
                },
            });
            if (!$('#multimediaCollapse_' + multimediaNum).hasClass('in')) {
                $('#multimediaCollapseTitle_' + multimediaNum).trigger("click");
            }
            $('#multimediaForm_' + multimediaNum).removeClass("panel-success");
            $('#multimediaForm_' + multimediaNum).addClass("panel-danger");
            $('#multimediaStatus_' + multimediaNum).addClass("hidden");
            layer.msg("添加失败", function () {
            });
            return postSuccess;
        }
        else {
            if (!$('#multimediaCollapse_' + multimediaNum).hasClass('in')) {
                $('#multimediaCollapseTitle_' + multimediaNum).trigger("click");
            }
            $('#multimediaForm_' + multimediaNum).removeClass("panel-success");
            $('#multimediaForm_' + multimediaNum).addClass("panel-danger");
            $('#multimediaStatus_' + multimediaNum).addClass("hidden");
            layer.msg("请完成此多媒体信息的填写", function () {
            });
            return false;
        }
}

//提交所有multimedia
function submitAllMultimedia() {
    var submitResult = true;
    $("form[id^='multimediaForm_']").each(function () {
        if (submitMultimedia($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}

//初始化fileinput控件（第一次初始化）
function initFileInput(ctrlName) {
	var MediaNum = parseInt($('#countMultimedia').val());
	
    $('#' + ctrlName).fileinput({
        theme: "fa",
        language: 'zh',
        uploadUrl: "console/multimedia/rest/batch/" + $('#taxonId').val(),
        /*allowedFileTypes:['image', 'html', 'text', 'video', 'audio', 'flash', 'object'],*/
        allowedFileExtensions: ['jpg', 'gif', 'png', 'img', 'jpeg', 'mp4','avi', 'swf', 'flv', 'wmv', 'mp3', 'cda', 'wav', 'mid', 'xls', 'xlsx', 'csv', 'pmd', 'rdf', 'tmv', 'def', 'fm', 'nb', 'numbers', 'xlsb', 'sdc', 'shp', 'doc', 'docx', 'txt', 'pdf', 'pptx', 'ppt'],
        uploadAsync: true, //默认异步上传
        showUpload: false, //是否显示上传按钮
        showClose:false,
        maxFileCount:100,	//表示允许同时上传的最大文件个数
        maxFileSize:50000,	//单位为kb，如果为0表示不限制文件大小
        minImageWidth:50,	//图片的最小宽度
        maxImageWidth:50000,	//图片的最大宽度
        minImageHeight:50,
        maxImageHeight:50000,
        uploadExtraData: function(previewId, index) {   //额外参数的关键点
        	
        	var obj = {};
        	var formNum = $("#countMultimedia").val();
        	var countReference = $("#countMultimediaReferences_" + MediaNum).val();
        	var multimediaReferences = [];
            obj._csrf = $('input[name="_csrf"]').val();
            obj.multimediaId = $("#multimediaId_" + MediaNum).val();
            obj.multimediaSourcesid = $("#multimediaSourcesid_" + MediaNum).val();
            obj.multimedia_title = $("#multimedia_title_" + MediaNum).val();
            obj.media_type = $("#media_type_" + MediaNum).val();
            obj.multimedia_rightsholder = $("#multimedia_rightsholder_" + MediaNum).val();
            obj.multimedia_oldpath = $("#multimedia_oldpath_" + MediaNum).val();
            obj.multimedia_licenseid = $("#multimedia_licenseid_" + MediaNum).val();
            if (null == obj.multimedia_licenseid) {
            	layer.msg("上传失败，请完成此多媒体信息的填写", function () {
                });
			}
            obj.multimedia_city = $("#multimedia_city_" + MediaNum).val();
            obj.multimedia_country = $("#multimedia_country_" + MediaNum).val();
            obj.multimedia_county = $("#multimedia_county_" + MediaNum).val();
            obj.multimedia_locality = $("#multimedia_locality_" + MediaNum).val();
            obj.multimedia_location = $("#multimedia_location_" + MediaNum).val();
            obj.multimedia_province = $("#multimedia_province_" + MediaNum).val();
            obj.multimedia_lat = $("#multimedia_lat_" + MediaNum).val();
            obj.multimedia_lng = $("#multimedia_lng_" + MediaNum).val();
            obj.multimedia_context = $("#multimedia_context_" + MediaNum).val();
            
            obj.countMultimediaReferences = $("#countMultimediaReferences_" + MediaNum).val();
            var refId = [];
            var refS = [];
            var refE = [];
            for (var i = 0; i <= countReference; i++) {
            	refId[formNum, i] = $("#multimediaReferences_" + formNum + "_" + i).val();
            	refS[formNum, i] = $("#multimediaReferencesPageS_" + formNum + "_" + i).val();
            	refE[formNum, i] = $("#multimediaReferencesPageE_" + formNum + "_" + i).val();
            	if (null != refId[formNum, i] && null != refS[formNum, i] && null != refE[formNum, i]) {
            		multimediaReferences.push(refId[formNum,i] + "&" + refS[formNum, i] + "&" + refE[formNum, i]);
            	}
				if (null != multimediaReferences && "" != multimediaReferences) {
					obj.multimediaReferences = multimediaReferences;
				}
			}
            return obj;
        }
    }).on("filebatchselected", function(event, files) {  
        	$(this).fileinput("upload");  
    	}).on("fileuploaded", function(event, data) {  
    			if(data.response){  
    				layer.msg("上传成功", function () {
                    });
    			}  
    	}); 
}

//添加一个新观测记录
function addMultimedia() {

    var countMultimedia = parseInt($('#countMultimedia').val());

    var thisMultimediaNum = {num: countMultimedia + 1};

    $('#multimediaForm').tmpl(thisMultimediaNum).appendTo('#newMultimedia');

    $('#countMultimedia').val(countMultimedia + 1);

    addMultimediaValidator(countMultimedia + 1);
    
    $("#media_type_" + (countMultimedia + 1)).select2({
        placeholder: "请选择媒体类型",
    });

    //名称变化后处理的函数
    $("#eventdate_" + (countMultimedia + 1)).on("change", function () {
        $("#multimediaName_" + (countMultimedia + 1)).text($("#eventdate_" + (countMultimedia + 1)).val());
    });
    // 唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
    	if (null == $("#multimediaId_" + (countMultimedia + 1)).val() || "" == $("#multimediaId_" + (countMultimedia + 1)).val()) {
    		$("#multimediaId_" + (countMultimedia + 1)).val(id);
		}
    });
    
    // Form7下的Datasource变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="500px";
    }
	var sourcesForm = "multimediaForm_" + (countMultimedia + 1);
    var sourcesId = "multimediaSourcesid_" + (countMultimedia + 1);
    var multimediaEventdate = "eventdate_" + (countMultimedia + 1);
    $("select[id = " + sourcesId + "]").on("change", function(){
    	if($("#" + sourcesId).val()=="addNew"){
    		$("#" + sourcesId).empty();
    		layer.open({
    			type: 2,
    			title:'<h4>添加数据源</h4>',
    			fixed: false, //不固定
    			area: [layer_width, layer_height],
    			content: '/console/description/addDatasource/' + sourcesId + '/' + sourcesForm
    		});
    	}
    });
    
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$('#sourcesid').val()+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");

    // 文本描述下拉选
    buildSelect2("multimedia_desid_" + (countMultimedia + 1), "console/description/rest/select", "请选择文本描述来源");
    // 分布描述下拉选
    buildSelect2("multimedia_disid_" + (countMultimedia + 1), "console/distributiondata/rest/select", "请选择分布描述来源");
    // 共享协议下拉选
    buildSelect2("multimedia_licenseid_" + (countMultimedia + 1), "console/license/rest/select", "请选择共享协议");

    $("#"+multimediaEventdate).datetimepicker({
        language: 'zh-CN',
        forceParse:true,//当选择器关闭的时候，是否强制解析输入框中的值
        autoclose: true,//选中之后自动隐藏日期选择框
        todayBtn: true,//今日按钮
        format: 'yyyy-mm-dd hh:ii:ss'
    }).on('hide',function(e) {
        $('#multimediaForm_'+(countMultimedia + 1)).data('bootstrapValidator')
            .updateStatus('eventdate_'+(countMultimedia + 1), 'NOT_VALIDATED',null)
            .validateField('eventdate_'+(countMultimedia + 1));
    });

    initFileInput("input-fa_" + (countMultimedia + 1));
    $('#input-fa').on('filepreupload', function(event, data, previewId, index) {
        
    });
    
    //非图片、音频、视频隐藏拍摄地点信息
    $("#locationInfo_" + (countMultimedia + 1)).hide();
    $("#oldpath_" + (countMultimedia + 1)).hide();
    $("#media_type_" + (countMultimedia + 1)).on("change", function(){
		var val = $("#media_type_" + (countMultimedia + 1)).val();
		if (val == 1 || val == 2 || val == 3) {
			$("#oldpath_" + (countMultimedia + 1)).show();
			$("#locationInfo_" + (countMultimedia + 1)).show();
		}else{
			$("#oldpath_" + (countMultimedia + 1)).hide();
			$("#locationInfo_" + (countMultimedia + 1)).hide();
		}
    });
    
    // 初始化select2插件
    $("#multimediaSearchPara_" + (countMultimedia + 1)).select2({
        placeholder: "请选择检索字段",
    });
}

//删除一个新参考文献
function removeMultimediaReferences(multimediaNum, referencesNum) {
    $("#multimediaReferencesForm_" + multimediaNum + "_" + referencesNum).remove();
 // 计算实际ref的数量
    var count = parseInt($("#countMultimediaReferences_" + multimediaNum).val());
    var num1 = parseInt($("#descMediaReferences_" + multimediaNum).val());
    $("#descMediaReferences_" + multimediaNum).val(num1 + 1);
    var num2 = parseInt($("#descMediaReferences_" + multimediaNum).val());
    $("#countMediaReferences_" + multimediaNum).val(count - num2);
}

//添加一个新参考文献
function addMultimediaReferences(multimediaNum) {

    var countMultimediaReferences = parseInt($('#countMultimediaReferences_' + multimediaNum).val());
    var desc = parseInt($("#descMediaReferences_" + multimediaNum).val());
    $("#countMediaReferences_" + multimediaNum).val(countMultimediaReferences - desc + 1);

    var thisReferencesNum = {mnum: multimediaNum, num: countMultimediaReferences + 1};

    $('#multimediaReferencesForm').tmpl(thisReferencesNum).appendTo('#newMultimediaReferences_' + multimediaNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Form5下的References变化监测
	var refForm = "newMultimediaReferences_" + multimediaNum;
	var refId = "multimediaReferences_" + multimediaNum + "_" + (countMultimediaReferences + 1);
	$("select[id=" + refId + "]").on("change", function() {
		if ($("#" + refId).val() == "addNew") {
			$("#" + refId).empty();
			layer.open({
				type : 2,
				title : '<h4>添加参考文献</h4>',
				fixed : false, // 不固定
				area : [ layer_width, layer_height ],
				content : '/console/multimedia/addRef/' + refId + '/' + refForm
			});
		}
	});
    
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "multimediaSearchPara_" + multimediaNum, "multimediaPyear_" + multimediaNum);

    $('#countMultimediaReferences_' + multimediaNum).val(countMultimediaReferences + 1);

    //参考文献验证规则
    addReferencesValidator(
        "newMultimediaReferences_"+multimediaNum,
        (countMultimediaReferences+1),
        "multimediaReferences_"+multimediaNum+"_",
        "multimediaReferencesPageS_"+multimediaNum+"_",
        "multimediaReferencesPageE_"+multimediaNum+"_"
    );

}

//添加地图选点
function selectImageCoordinates (multimediaId) {
    var width = $(window).width();
    var height = $(window).height();
    var layer_width = "90%";
    var layer_height = "90%";
    if (width > 760) {
        layer_width = "700px";
        layer_height = "700px";
    }
    layer.open({
        type : 2,
        scrollbar: false,
        title : '<h4>地图选点</h4>',
        fixed : false, // 不固定
        area : [ layer_width, layer_height ],
        content : '/console/multimedia/map/'+multimediaId
    });
}