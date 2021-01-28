/**
 * Created by Tianshan on 18-5-22.
 */

//选择关系
function selectRelation(descriptionNum, relationId, relationText) {
    $("#relationId_" + descriptionNum).val(relationId);
    $("#relationBtn_" + descriptionNum).text(relationText);
}
//删除一个新描述
function removeDescription(descriptionNum) {
		var r = confirm("是否删除?");
		if (r == true) {
			$.post("/console/description/rest/delete",
			{
		        "_csrf":$('input[name="_csrf"]').val(),
		        "descriptionId":$("#descriptionId_" + descriptionNum).val()
		    },
			function(status) {
				if (status) {
					layer.msg('删除成功', {time : 500}, 
					function() {
						$("#descriptionForm_" + descriptionNum).remove();
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
//提交一个描述
function submitDescription(descriptionNum) {
    descriptionFormValidator(descriptionNum);
    if ($('#descriptionForm_' + descriptionNum).data('bootstrapValidator').isValid() &&
        ($("tr[id^='"+'descriptionReferencesForm_'+descriptionNum+"_']").length<=0 || referencesValidator('newDescriptionReferences_'+descriptionNum,3))) {
        //处理ajax提交
    	var obj = $('#descriptionForm_' + descriptionNum).serialize();
    	var url = "/console/description/rest/add/" + $('#taxonId').val();
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
                              if ($('#descriptionCollapse_' + descriptionNum).hasClass('in')) {
                                  $('#descriptionCollapseTitle_' + descriptionNum).trigger("click");
                              }
                              $('#descriptionForm_' + descriptionNum).removeClass("panel-default");
                              $('#descriptionForm_' + descriptionNum).removeClass("panel-danger");
                              $('#descriptionForm_' + descriptionNum).addClass("panel-success");
                              $('#descriptionStatus_' + descriptionNum).removeClass("hidden");
                              postSuccess = true;
                          });
              }else{
                  layer.msg("添加失败！", function () {
                  });
                  postSuccess = false;
              }
            },
            error: function() {
          	  postSuccess = false;
            }
         });
    	if (!$('#descriptionCollapse_' + descriptionNum).hasClass('in')) {
            $('#descriptionCollapseTitle_' + descriptionNum).trigger("click");
        }
        $('#descriptionForm_' + descriptionNum).removeClass("panel-success");
        $('#descriptionForm_' + descriptionNum).addClass("panel-danger");
        $('#descriptionStatus_' + descriptionNum).addClass("hidden");
        layer.msg("添加失败", function () {
        });
        return postSuccess;
    } else {
        if (!$('#descriptionCollapse_' + descriptionNum).hasClass('in')) {
            $('#descriptionCollapseTitle_' + descriptionNum).trigger("click");
        }
        $('#descriptionForm_' + descriptionNum).removeClass("panel-success");
        $('#descriptionForm_' + descriptionNum).addClass("panel-danger");
        $('#descriptionStatus_' + descriptionNum).addClass("hidden");
        layer.msg("请完成此文本描述的填写", function () {
        });
        return false;
    }
}

//提交所有description
function submitAllDescription() {
    var submitResult = true;
    $("form[id^='descriptionForm_']").each(function () {
        if (submitDescription($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}
//添加一个新描述
function addDescription() {

    var countDescription = parseInt($('#countDescription').val());

    var thisDescriptionNum = {num: countDescription + 1};

    $('#descriptionForm').tmpl(thisDescriptionNum).appendTo('#newDescription');
    // 描述类型下拉选
    buildSelect2("destypeid_" + (countDescription + 1), "console/descriptiontype/rest/select/description", "请选择描述类型");
    // 共享协议下拉选
    buildSelect2("licenseid_" + (countDescription + 1), "console/license/rest/select", "请选择共享协议");
    // 共享协议下拉选
    buildSelect2("relationDes_" + (countDescription + 1), "console/description/rest/select/" + $("#taxonId").val(), "请选择关系");

	// 俗名信心语言下拉选
	$("#language_" + (countDescription + 1)).select2({
		placeholder: "请选择描述语言",
	});

	$('#countDescription').val(countDescription + 1);

    addDescriptionValidator(countDescription + 1);

    // 唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
    	if (null == $("#descriptionId_" + (countDescription + 1)).val() || "" == $("#descriptionId_" + (countDescription + 1)).val()) {
    		$("#descriptionId_" + (countDescription + 1)).val(id);
		}
    });
    
    // Form3Description下的Datasource变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="500px";
    }			
    
	var sourcesForm = "descriptionForm_" + (countDescription + 1);
    var sourcesId = "descriptionsourcesid_" + (countDescription + 1);
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
    
    // 数据源下拉选
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$('#sourcesid').val()+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");
    // 构造每一个描述的Title
    function buildDescriptionTitle(countDescription) {
        var htmlText = "";
        if ($("#destypeid_" + (countDescription + 1)).select2('data')[0]) {
        	htmlText = $("#destypeid_" + (countDescription + 1)).select2('data')[0].text;
        	htmlText = handleHtmlText(htmlText);
        }
        htmlText = htmlText + " (" + $("#destitle_" + (countDescription + 1)).val() + ") ";
        return htmlText;
    }
    
  //描述类型变化后处理的函数
    $("#destypeid_" + (countDescription + 1)).on("change", function () {
    	$("#descriptionCollapseTitle_" + (countDescription + 1)).text(buildDescriptionTitle(countDescription));
    });
    $("#destitle_" + (countDescription + 1)).on("change", function () {
        $("#descriptionCollapseTitle_" + (countDescription + 1)).text(
        		handleHtmlText($("#destypeid_" + (countDescription + 1)).select2('data')[0].text) + " (" + $("#destitle_" + (countDescription + 1)).val() + ")");		
    });
    
    // 初始化select2插件
    $("#descriptionSearchPara_" + (countDescription + 1)).select2({
        placeholder: "请选择检索字段",
    });
}

function handleHtmlText(htmlText){
	htmlText = htmlText.replace(/(\n)/g, "");  
	htmlText = htmlText.replace(/(\t)/g, "");  
	htmlText = htmlText.replace(/(\r)/g, "");  
	htmlText = htmlText.replace(/<\/?[^>]*>/g, "");  
	htmlText = htmlText.replace(/\s*/g, "");
	htmlText = htmlText.replace(/&nbsp;/g, "");  
	return htmlText;
}

//删除一个新参考文献
function removeDescriptionReferences(descriptionNum, referencesNum) {
    $("#descriptionReferencesForm_" + descriptionNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countDescriptionReferences_" + descriptionNum).val());
    var num1 = parseInt($("#descDescReferences_" + descriptionNum).val());
    $("#descDescReferences_" + descriptionNum).val((num1) + 1);
    var num2 = parseInt($("#descDescReferences_" + descriptionNum).val());
    $("#countDescReferences_" + descriptionNum).val(count - num2);
}
//添加一个新参考文献
function addDescriptionReferences(descriptionNum) {
    var countDescriptionReferences = parseInt($('#countDescriptionReferences_' + descriptionNum).val());
    var desc = parseInt($("#descDescReferences_" + descriptionNum).val());
    $("#countDescReferences_" + descriptionNum).val(countDescriptionReferences - desc + 1);

    var thisReferencesNum = {dnum: descriptionNum, num: countDescriptionReferences + 1};

    $('#descriptionReferencesForm').tmpl(thisReferencesNum).appendTo('#newDescriptionReferences_' + descriptionNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Description下的References变化监测
	var refForm = "newDescriptionReferences_" + descriptionNum;
	var refId = "descriptionReferences_" + descriptionNum + "_" + (countDescriptionReferences + 1);
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
    
    buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "descriptionSearchPara_" + descriptionNum, "descriptionPyear_" + descriptionNum);
    
    //参考文献验证规则
    addReferencesValidator(
        "newDescriptionReferences_"+descriptionNum,
        (countDescriptionReferences+1),
        "descriptionReferences_"+descriptionNum+"_",
        "descriptionReferencesPageS_"+descriptionNum+"_",
        "descriptionReferencesPageE_"+descriptionNum+"_"
    );

    $('#countDescriptionReferences_' + descriptionNum).val(countDescriptionReferences + 1);

}
