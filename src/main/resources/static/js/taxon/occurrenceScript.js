/**
 * Created by Tianshan on 18-5-22.
 */
//删除一个新观测记录
function removeOccurrence(occurrenceNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/occurrence/rest/delete",
		{
			"_csrf":$('input[name="_csrf"]').val(),
			"occurrenceId":$("#occurrenceId_" + occurrenceNum).val()
		},
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 500}, function() {
					 $("#occurrenceForm_" + occurrenceNum).remove();
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
function submitOccurrence(occurrenceNum) {
    occurrenceFormValidator(occurrenceNum);
    if ($('#occurrenceForm_' + occurrenceNum).data('bootstrapValidator').isValid() &&
        ($("tr[id^='"+'occurrenceReferencesForm_'+occurrenceNum+"_']").length<=0 || referencesValidator('newOccurrenceReferences_'+occurrenceNum,2))) {
    	// 处理Ajax提交
        var obj = $('#occurrenceForm_' + occurrenceNum).serialize();
        var url = "/console/occurrence/rest/add/" + $('#taxonId').val();
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
                            if ($('#occurrenceCollapse_' + occurrenceNum).hasClass('in')) {
                                $('#occurrenceCollapseTitle_' + occurrenceNum).trigger("click");
                            }
                            $('#occurrenceForm_' + occurrenceNum).removeClass("panel-default");
                            $('#occurrenceForm_' + occurrenceNum).removeClass("panel-danger");
                            $('#occurrenceForm_' + occurrenceNum).addClass("panel-success");
                            $('#occurrenceStatus_' + occurrenceNum).removeClass("hidden");
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
        if (!$('#occurrenceCollapse_' + occurrenceNum).hasClass('in')) {
            $('#occurrenceCollapseTitle_' + occurrenceNum).trigger("click");
        }
        $('#occurrenceForm_' + occurrenceNum).removeClass("panel-success");
        $('#occurrenceForm_' + occurrenceNum).addClass("panel-danger");
        $('#occurrenceStatus_' + occurrenceNum).addClass("hidden");
        layer.msg("添加失败", function () {
        });
        return postSuccess;
    }
    else {
        if (!$('#occurrenceCollapse_' + occurrenceNum).hasClass('in')) {
            $('#occurrenceCollapseTitle_' + occurrenceNum).trigger("click");
        }
        $('#occurrenceForm_' + occurrenceNum).removeClass("panel-success");
        $('#occurrenceForm_' + occurrenceNum).addClass("panel-danger");
        $('#occurrenceStatus_' + occurrenceNum).addClass("hidden");
        layer.msg("请完成此观测记录信息的填写", function () {
        });
        return false;
    }
}

//提交所有occurrence
function submitAllOccurrence() {
    var submitResult = true;
    $("form[id^='occurrenceForm_']").each(function () {
        if (submitOccurrence($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}

//添加一个新观测记录
function addOccurrence() {

    var countOccurrence = parseInt($('#countOccurrence').val());

    var thisOccurrenceNum = {num: countOccurrence + 1};

    $('#occurrenceForm').tmpl(thisOccurrenceNum).appendTo('#newOccurrence');

    $('#countOccurrence').val(countOccurrence + 1);

    $("#occurrenceLanguage_" + (countOccurrence + 1)).select2({
        placeholder: "请选择描述语言"
    });
    $("#occurrence_type_" + (countOccurrence + 1)).select2({
    	placeholder: "请选择观测记录类型"
    });

    addOccurrenceValidator(countOccurrence + 1);

    //名称变化后处理的函数
    $("#eventdate_" + (countOccurrence + 1)).on("change", function () {
        $("#occurrenceName_" + (countOccurrence + 1)).text($("#eventdate_" + (countOccurrence + 1)).val());
    });
    // 唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
	    if (null == $("#occurrenceId_" + (countOccurrence + 1)).val() || "" == $("#occurrenceId_" + (countOccurrence + 1)).val()) {
	    	$("#occurrenceId_" + (countOccurrence + 1)).val(id);
		}
    });
    
    // Form5下的Datasource变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="500px";
    }
	var sourcesForm = "occurrenceForm_" + (countOccurrence + 1);
    var sourcesId = "occurrenceSourcesid_" + (countOccurrence + 1);
    var occurrenceEventdate = "eventdate_" + (countOccurrence + 1);
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

    $("#"+occurrenceEventdate).datetimepicker({
        language: 'zh-CN',
        forceParse:true,//当选择器关闭的时候，是否强制解析输入框中的值
        autoclose: true,//选中之后自动隐藏日期选择框
        todayBtn: true,//今日按钮
        format: 'yyyy-mm-dd'//format: 'yyyy-mm-dd hh:ii:ss'
    }).on('hide',function(e) {
        $('#occurrenceForm_'+(countOccurrence + 1)).data('bootstrapValidator')
            .updateStatus('eventdate_'+(countOccurrence + 1), 'NOT_VALIDATED',null)
            .validateField('eventdate_'+(countOccurrence + 1));
    });
    
    // 初始化select2插件
    $("#occurrenceSearchPara_" + (countOccurrence + 1)).select2({
        placeholder: "请选择检索字段",
    });
}
//删除一个新参考文献
function removeOccurrenceReferences(occurrenceNum, referencesNum) {
    $("#occurrenceReferencesForm_" + occurrenceNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countOccurrenceReferences_" + occurrenceNum).val());
    var num1 = parseInt($("#descOccuReferences_" + occurrenceNum).val());
    $("#descOccuReferences_" + occurrenceNum).val(num1 + 1);
    var num2 = parseInt($("#descOccuReferences_" + occurrenceNum).val());
    $("#countOccuReferences_" + occurrenceNum).val(count - num2);
}
//添加一个新参考文献
function addOccurrenceReferences(occurrenceNum) {

    var countOccurrenceReferences = parseInt($('#countOccurrenceReferences_' + occurrenceNum).val());
    var desc = parseInt($("#descOccuReferences_" + occurrenceNum).val());
    $("#countOccuReferences_" + occurrenceNum).val(countOccurrenceReferences - desc + 1);

    var thisReferencesNum = {onum: occurrenceNum, num: countOccurrenceReferences + 1};

    $('#occurrenceReferencesForm').tmpl(thisReferencesNum).appendTo('#newOccurrenceReferences_' + occurrenceNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Form5下的References变化监测
	var refForm = "newOccurrenceReferences_" + occurrenceNum;
	var refId = "occurrenceReferences_" + occurrenceNum + "_" + (countOccurrenceReferences + 1);
	$("select[id=" + refId + "]").on("change", function() {
		if ($("#" + refId).val() == "addNew") {
			$("#" + refId).empty();
			layer.open({
				type : 2,
				title : '<h4>添加参考文献</h4>',
				fixed : false, // 不固定
				area : [ layer_width, layer_height ],
				content : '/console/ref/addNew/' + refId + '/' + refForm
			});
		}
	});
    
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "occurrenceSearchPara_" + occurrenceNum, "occurrencePyear_" + occurrenceNum);

	$('#countOccurrenceReferences_' + occurrenceNum).val(countOccurrenceReferences + 1);

    //参考文献验证规则
    addReferencesValidator(
        "newOccurrenceReferences_"+occurrenceNum,
        (countOccurrenceReferences+1),
        "occurrenceReferences_"+occurrenceNum+"_",
        "occurrenceReferencesPageS_"+occurrenceNum+"_",
        "occurrenceReferencesPageE_"+occurrenceNum+"_"
    );

}

//地图选点
function selectCoordinates (occurrenceId) {
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
        content : 'console/occurrence/map/'+occurrenceId
    });
}