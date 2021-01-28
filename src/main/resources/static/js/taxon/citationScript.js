/**
 * Created by Tianshan on 18-5-22.
 */
//删除一个新引证
function removeCitation(citationNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/citation/rest/delete", 
		{
			"_csrf":$('input[name="_csrf"]').val(),
			"citationId":$("#citationId_" + citationNum).val()
		}, 
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 500}, function() {
					 $("#citationForm_" + citationNum).remove();
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
//提交一个引证
function submitCitation(citationNum) {
    citationFormValidator(citationNum);
    if ($('#citationForm_' + citationNum).data('bootstrapValidator').isValid() &&
        ($("tr[id^='"+'citationReferencesForm_'+citationNum+"_']").length<=0 || referencesValidator('newCitationReferences_'+citationNum,2))) {
    	// 处理Ajax提交
        var obj = $('#citationForm_' + citationNum).serialize();
        var url = "/console/citation/rest/add/" + $('#taxonId').val();
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
                            if ($('#citationCollapse_' + citationNum).hasClass('in')) {
                                $('#citationCollapseTitle_' + citationNum).trigger("click");
                            }
                            $('#citationForm_' + citationNum).removeClass("panel-default");
                            $('#citationForm_' + citationNum).removeClass("panel-danger");
                            $('#citationForm_' + citationNum).addClass("panel-success");
                            $('#citationStatus_' + citationNum).removeClass("hidden");
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
        if (!$('#citationCollapse_' + citationNum).hasClass('in')) {
            $('#citationCollapseTitle_' + citationNum).trigger("click");
        }
        $('#citationForm_' + citationNum).removeClass("panel-success");
        $('#citationForm_' + citationNum).addClass("panel-danger");
        $('#citationStatus_' + citationNum).addClass("hidden");
        layer.msg("添加失败", function () {
        });
        return postSuccess;
    } else {
        if (!$('#citationCollapse_' + citationNum).hasClass('in')) {
            $('#citationCollapseTitle_' + citationNum).trigger("click");
        }
        $('#citationForm_' + citationNum).removeClass("panel-success");
        $('#citationForm_' + citationNum).addClass("panel-danger");
        $('#citationStatus_' + citationNum).addClass("hidden");
        layer.msg("请完成此引证信息的填写", function () {
        });
        return false;
    }
}

//提交所有citation
function submitAllCitation() {
    var submitResult = true;
    $("form[id^='citationForm_']").each(function () {
        if (submitCitation($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}

//添加一个新引证
function addCitation() {

    var countCitation = parseInt($('#countCitation').val());

    var thisCitationNum = {num: countCitation + 1};

    $('#citationForm').tmpl(thisCitationNum).appendTo('#newCitation');

    $('#countCitation').val(countCitation + 1);

    $("#nametype_" + (countCitation + 1)).select2({
        placeholder: "请选择名称类型",
    });

    addCitationValidator(countCitation + 1);
    
    // 构造每一个引证的Title
    function buildCitationTitle(countCitation) {
        var htmlText = "";
        if ($("#sciname_" + (countCitation + 1)).val()) {
        	htmlText = "<i>" + $("#sciname_" + (countCitation + 1)).val() + "</i>";
        }
        htmlText = htmlText + " " + $("#authorship_" + (countCitation + 1)).val();
        return htmlText;
    }

    //名称变化后处理的函数
    $("#sciname_" + (countCitation + 1)).on("change", function () {
        $("#citationCollapseTitle_" + (countCitation + 1)).html(buildCitationTitle(countCitation));
    });
    $("#authorship_" + (countCitation + 1)).on("change", function () {
        $("#citationCollapseTitle_" + (countCitation + 1)).html(buildCitationTitle(countCitation));
    });
    
    // 唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
	    if (null == $("#citationId_" + (countCitation + 1)).val() || "" == $("#citationId_" + (countCitation + 1)).val()) {
		    $("#citationId_" + (countCitation + 1)).val(id);
	    }
    });
    
    // 初始化select2插件
    $("#citationSearchPara_" + (countCitation + 1)).select2({
        placeholder: "请选择检索字段",
    });
    
    // Form2下的Datasource变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="500px";
    }
	var sourcesForm = "citationForm_" + (countCitation + 1);
    var sourcesId = "citationSourcesid_" + (countCitation + 1);
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

    //给接受名赋值
    var citationTypeid = "nametype_" + (countCitation + 1);
    $("select[id = " + citationTypeid + "]").on("change", function(){
        if($("#" + citationTypeid).val()==1){
            $("#sciname_" + (countCitation + 1)).val($("#scientificname").val());
        }
        else{
            $("#sciname_" + (countCitation + 1)).val("");
        }
    });
    
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$("#sourcesid").select2("val")+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");
}
//选择参考文献类型
function selectCitationReferences(citationNum, referencesNum, referencesId, referencesText) {
    $("#citationReferencesType_" + citationNum + "_" + referencesNum).val(referencesId);
    $("#citationReferencesBtn_" + citationNum + "_" + referencesNum).text(referencesText);
}

//删除一个新参考文献
function removeCitationReferences(citationNum, referencesNum) {
    $("#citationReferencesForm_" + citationNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countCitationReferences_" + citationNum).val());
    var num1 = parseInt($("#descCitatReferences_" + citationNum).val());
    $("#descCitatReferences_" + citationNum).val(num1 + 1);
    var num2 = parseInt($("#descCitatReferences_" + citationNum).val());
    $("#countCitatReferences_" + citationNum).val(count - num2);
}
//添加一个新参考文献
function addCitationReferences(citationNum) {

    var countCitationReferences = parseInt($('#countCitationReferences_' + citationNum).val());
    var desc = parseInt($("#descCitatReferences_" + citationNum).val());
    $("#countCitatReferences_" + citationNum).val(countCitationReferences - desc + 1);

    var thisReferencesNum = {cnum: citationNum, num: countCitationReferences + 1};

    $('#citationReferencesForm').tmpl(thisReferencesNum).appendTo('#newCitationReferences_' + citationNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Form2下的References变化监测
	var refForm = "newCitationReferences_" + citationNum;
	var refId = "citationReferences_" + citationNum + "_" + (countCitationReferences + 1);
	$("select[id=" + refId + "]").on("change", function() {
		if ($("#" + refId).val() == "addNew") {
			$("#" + refId).empty();
			layer.open({
				type : 2,
				title : '<h4>添加参考文献</h4>',
				fixed : false, // 不固定
				area : [ layer_width, layer_height ],
				content :  'console/ref/addNew/' + refId + '/' + refForm
			});
		}
	});
    
    
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "citationSearchPara_" + citationNum, "citationPyear_" + citationNum);

    $('#countCitationReferences_' + citationNum).val(countCitationReferences + 1);

    //参考文献验证规则
    addReferencesValidator(
        "newCitationReferences_"+citationNum,
        (countCitationReferences+1),
        "citationReferences_"+citationNum+"_",
        "citationReferencesPageS_"+citationNum+"_",
        "citationReferencesPageE_"+citationNum+"_"
    );

}