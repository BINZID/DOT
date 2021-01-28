/**
 * Created by Tianshan on 18-5-22.
 */
//删除一个新俗名
function removeCommonname(commonnameNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/commonname/rest/delete",
		{
			"_csrf":$('input[name="_csrf"]').val(),
			"commonnameId":$("#commonnameId_" + commonnameNum).val()
		},
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 500}, function() {
					 $("#commonnameForm_" + commonnameNum).remove();
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
//提交一个俗名
function submitCommonname(commonnameNum) {
    commonnameFormValidator(commonnameNum);
    if ($('#commonnameForm_' + commonnameNum).data('bootstrapValidator').isValid() &&
        ($("tr[id^='"+'commonnameReferencesForm_'+commonnameNum+"_']").length<=0 || referencesValidator('newCommonnameReferences_'+commonnameNum,2))) {
    	// 处理Ajax提交
        var obj = $('#commonnameForm_' + commonnameNum).serialize();
        var postSuccess=false;
        return $.ajax({
            type: "POST",
            url: "/console/commonname/rest/add/" + $('#taxonId').val(),
            data: obj,	// 要提交的表单
            dataType: "json",
            contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
            success: function (msg) {
	          	if (msg.result == true) {
                    layer.msg('提交成功，请继续填写其他内容', {time: 1500}, function () {
                            if ($('#commonnameCollapse_' + commonnameNum).hasClass('in')) {
                                $('#commonnameCollapseTitle_' + commonnameNum).trigger("click");
                            }
                            $('#commonnameForm_' + commonnameNum).removeClass("panel-default");
                            $('#commonnameForm_' + commonnameNum).removeClass("panel-danger");
                            $('#commonnameForm_' + commonnameNum).addClass("panel-success");
                            $('#commonnameStatus_' + commonnameNum).removeClass("hidden");
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
        if (!$('#commonnameCollapse_' + commonnameNum).hasClass('in')) {
            $('#commonnameCollapseTitle_' + commonnameNum).trigger("click");
        }
        $('#commonnameForm_' + commonnameNum).removeClass("panel-success");
        $('#commonnameForm_' + commonnameNum).addClass("panel-danger");
        $('#commonnameStatus_' + commonnameNum).addClass("hidden");
        layer.msg("添加失败", function () {
        });
        return postSuccess;
    }
    else {
        if (!$('#commonnameCollapse_' + commonnameNum).hasClass('in')) {
            $('#commonnameCollapseTitle_' + commonnameNum).trigger("click");
        }
        $('#commonnameForm_' + commonnameNum).removeClass("panel-success");
        $('#commonnameForm_' + commonnameNum).addClass("panel-danger");
        $('#commonnameStatus_' + commonnameNum).addClass("hidden");
        layer.msg("请完成此俗名信息的填写", function () {
        });
        return false;
    }
}

//提交所有commonname
function submitAllCommonname() {
    var submitResult = true;
    $("form[id^='commonnameForm_']").each(function () {
        if (submitCommonname($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}

//添加一个新俗名
function addCommonname() {

    var countCommonname = parseInt($('#countCommonname').val());

    var thisCommonnameNum = {num: countCommonname + 1};

    $('#commonnameForm').tmpl(thisCommonnameNum).appendTo('#newCommonname');

    $('#countCommonname').val(countCommonname + 1);
    
	// 俗名信心语言下拉选
	$("#commonnameLanguage_" + (countCommonname + 1)).select2({
		placeholder: "请选择俗名信息语言类型",
	});
    
/*
    $.ajax({
        url: "json/language.json",//json文件位置
        type: "GET",//请求方式为get
        dataType: "json", //返回数据格式为json
        success: function(data) {//请求成功完成后要执行的方法
            $("#commonnameLanguage_" + (countCommonname + 1)).select2({
                data: data,
                placeholder: "请选择描述语言",
                //allowClear:true
            })
        }
    });
*/
    addCommonnameValidator(countCommonname + 1);

    //名称变化后处理的函数
    $("#commonname_" + (countCommonname + 1)).on("change", function () {
        $("#commonnameName_" + (countCommonname + 1)).text($("#commonname_" + (countCommonname + 1)).val());
    });
    // 唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
    	if (null == $("#commonnameId_" + (countCommonname + 1)).val() || "" == $("#commonnameId_" + (countCommonname + 1)).val()) {
    		$("#commonnameId_" + (countCommonname + 1)).val(id);
		}
    });
    
    // 初始化select2插件
    $("#commonnameSearchPara_" + (countCommonname + 1)).select2({
        placeholder: "请选择检索字段",
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
	var sourcesForm = "commonnameForm_" + (countCommonname + 1);
    var sourcesId = "commonnameSourcesid_" + (countCommonname + 1);
    $("select[id = " + sourcesId + "]").on("change", function(){
    	if($("#" + sourcesId).val()=="addNew"){
    		$("#" + sourcesId).empty();
    		layer.open({
    			type: 2,
    			title:'<h4>添加数据源</h4>',
    			fixed: false, //不固定
    			area: [layer_width, layer_height],
    			content: '/console/datasource/addNew/' + sourcesId + '/' + sourcesForm
    		});
    	}
    });
    
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$('#sourcesid').val()+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");
}
//删除一个新参考文献
function removeCommonnameReferences(commonnameNum, referencesNum) {
    $("#commonnameReferencesForm_" + commonnameNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countCommonnameReferences_" + commonnameNum).val());
    var num1 = parseInt($("#descCommReferences_" + commonnameNum).val());
    $("#descCommReferences_" + commonnameNum).val(num1 + 1);
    var num2 = parseInt($("#descCommReferences_" + commonnameNum).val());
    $("#countCommReferences_" + commonnameNum).val(count - num2);
}
//添加一个新参考文献
function addCommonnameReferences(commonnameNum) {

    var countCommonnameReferences = parseInt($('#countCommonnameReferences_' + commonnameNum).val());
    var desc = parseInt($("#descCommReferences_" + commonnameNum).val());
    $("#countCommReferences_" + commonnameNum).val(countCommonnameReferences - desc + 1);

    var thisReferencesNum = {cnnum: commonnameNum, num: countCommonnameReferences + 1};

    $('#commonnameReferencesForm').tmpl(thisReferencesNum).appendTo('#newCommonnameReferences_' + commonnameNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Form5下的References变化监测
	var refForm = "newCommonnameReferences_" + commonnameNum;
	var refId = "commonnameReferences_" + commonnameNum + "_" + (countCommonnameReferences + 1);
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
    
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "commonnameSearchPara_" + commonnameNum, "commonamePyear_" + commonnameNum);

    $('#countCommonnameReferences_' + commonnameNum).val(countCommonnameReferences + 1);

    //参考文献验证规则
    addReferencesValidator(
        "newCommonnameReferences_"+commonnameNum,
        (countCommonnameReferences+1),
        "commonnameReferences_"+commonnameNum+"_",
        "commonnameReferencesPageS_"+commonnameNum+"_",
        "commonnameReferencesPageE_"+commonnameNum+"_"
    );

}