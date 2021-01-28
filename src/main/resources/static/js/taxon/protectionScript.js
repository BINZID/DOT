/**
 * Created by Tianshan on 18-5-22.
 */

//删除一个新保护数据
function removeProtection(protectionNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/protection/rest/delete",
		{
	        "_csrf":$('input[name="_csrf"]').val(),
	        "protectionId":$("#protectionId_" + protectionNum).val()
	    },
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 500}, 
				function() {
					 $("#protectionForm_" + protectionNum).remove();
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
//提交一个保护数据
function submitProtection(protectionNum) {
    protectionFormValidator(protectionNum);
    if ($('#protectionForm_' + protectionNum).data('bootstrapValidator').isValid() &&
        ($("tr[id^='"+'protectionReferencesForm_'+protectionNum+"_']").length<=0 || referencesValidator('newProtectionReferences_'+protectionNum,3))) {
        //处理ajax提交
    	var obj = $('#protectionForm_' + protectionNum).serialize();
    	var url = "/console/protection/rest/add/" + $('#taxonId').val();
    	var postSuccess=false;
    	return $.ajax({
          type: "POST",
          url: url,
          data: obj,	// 要提交的表单
          dataType: "json",
          contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
          success: function (msg) {
        	if (msg.result == true) {
        		layer.msg(
        		    '提交成功，请继续填写其他内容',
        		    {time: 1500},
        		    function () {
        		        if ($('#protectionCollapse_' + protectionNum).hasClass('in')) {
        		            $('#protectionCollapseTitle_' + protectionNum).trigger("click");
        		        }
        		        $('#protectionForm_' + protectionNum).removeClass("panel-default");
        		        $('#protectionForm_' + protectionNum).removeClass("panel-danger");
        		        $('#protectionForm_' + protectionNum).addClass("panel-success");
        		        $('#protectionStatus_' + protectionNum).removeClass("hidden");
        		        postSuccess = true;
        		    });
			}else{
				layer.msg("添加失败！", {time: 1000});
				postSuccess = false;
			}
          },
          error: function() {
        	  postSuccess = false;
          }
        });
    	if (!$('#protectionCollapse_' + protectionNum).hasClass('in')) {
            $('#protectionCollapseTitle_' + protectionNum).trigger("click");
        }
        $('#protectionForm_' + protectionNum).removeClass("panel-success");
        $('#protectionForm_' + protectionNum).addClass("panel-danger");
        $('#protectionStatus_' + protectionNum).addClass("hidden");
        layer.msg("添加失败", function () {
        });
        return postSuccess;
    }
    else {
        if (!$('#protectionCollapse_' + protectionNum).hasClass('in')) {
            $('#protectionCollapseTitle_' + protectionNum).trigger("click");
        }
        $('#protectionForm_' + protectionNum).removeClass("panel-success");
        $('#protectionForm_' + protectionNum).addClass("panel-danger");
        $('#protectionStatus_' + protectionNum).addClass("hidden");
        /*$('#tab-protection').addClass("alert alert-danger");*/
        layer.msg("请完成此文本描述的填写", function () {
        });
        return false;
    }
}

//提交所有保护数据
function submitAllProtection() {
    var submitResult = true;
    $('#tab-protection').removeClass("alert-danger");
    $('#tab-protection').removeClass("alert");
    $("form[id^='protectionForm_']").each(function () {
        if (submitProtection($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}
//添加一个新保护数据
function addProtection() {

    var countProtection = parseInt($('#countProtection').val());

    var thisProtectionNum = {num: countProtection + 1};

    $('#protectionForm').tmpl(thisProtectionNum).appendTo('#newProtection');
   
    $('#countProtection').val(countProtection + 1);

    addProtectionValidator(countProtection + 1);

    // 唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
	    if (null == $("#protectionId_" + (countProtection + 1)).val() || "" == $("#protectionId_" + (countProtection + 1)).val()) {
	    	$("#protectionId_" + (countProtection + 1)).val(id);
	    }
    });
    
 // Form3 Distribution下的Datasource变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="500px";
    }						  
	var sourcesForm = "protectionForm_" + (countProtection + 1);
    var sourcesId = "protectionsourcesid_" + (countProtection + 1);
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
    // 数据源
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$('#sourcesid').val()+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");

    function buildProtectionTitle(countProtection) {
        var titleText = "";
        if ($("#standardname_" + (countProtection + 1)).select2('data')[0]) {
            titleText = $("#standardname_" + (countProtection + 1)).select2('data')[0].text
        }
        if ($("#version_" + (countProtection + 1)).select2('data')[0]) {
            titleText = titleText
                + " ("
                + $("#version_" + (countProtection + 1)).select2('data')[0].text
                + ") "
        }
        if ($("#protlevel_" + (countProtection + 1)).select2('data')[0]) {
            titleText = titleText
                + $("#protlevel_" + (countProtection + 1)).select2('data')[0].text
        }
        return titleText;
    }
    
    //构造url_parameter
    function buildURLparameter (type) {
        var standardname=$("#standardname_" + (countProtection + 1)).val();
        var version=$("#version_" + (countProtection + 1)).val();
        var protlevel=$("#protlevel_" + (countProtection + 1)).val();
        var url_parameter=
            "?type="+type+
            "&standardname="+standardname+
            "&version="+version+
            "&protlevel="+protlevel;
        return url_parameter;
    }
    
    //保护标准下拉选
    buildSelect2("standardname_" + (countProtection + 1), "console/protectstandard/rest/selectStandard", "请选择保护标准");

    buildSelect2("version_" + (countProtection + 1), "console/protectstandard/rest/selectVersion", "请先选择标准名称");
    buildSelect2("protlevel_" + (countProtection + 1), "console/protectstandard/rest/selectProtlevel", "请先选择标准名称");
    //保护标准变化后处理的函数
    $("#standardname_" + (countProtection + 1)).on("change", function () {
        $("#protectionCollapseTitle_" + (countProtection + 1)).text(buildProtectionTitle(countProtection));
        
        $("#version_" + (countProtection + 1)).empty();
        $("#protlevel_" + (countProtection + 1)).empty();
        $("#version_" + (countProtection + 1)).prepend("<option value=''>请选择标准版本</option>");
        $("#protlevel_" + (countProtection + 1)).prepend("<option value=''>请先选择标准版本</option>");
        //构造select2
        buildSelect2 (
        		"version_" + (countProtection + 1),
        		"console/protectstandard/rest/selectVersion" + buildURLparameter($("#standardname_" + (countProtection + 1)).select2('data')[0].text), 
        		"请选择标准版本"
        );
        //构造select2
        buildSelect2 (
        		"protlevel_" + (countProtection + 1),
        		"console/protectstandard/rest/selectProtlevel"+buildURLparameter($("#version_" + (countProtection + 1)).select2('data')[0].text),
        		"请选择保护级别"
        );
    });
    
    //保护标准版本变化后处理的函数
    $("#version_" + (countProtection + 1)).on("change", function () {
        $("#protectionCollapseTitle_" + (countProtection + 1)).text(buildProtectionTitle(countProtection));
        
        $("#protlevel_" + (countProtection + 1)).empty();
        $("#protlevel_" + (countProtection + 1)).prepend("<option value='all'>请选择保护级别</option>");
        //构造select2
        buildSelect2 (
        		"protlevel_" + (countProtection + 1),
        		"console/protectstandard/rest/selectProtlevel"+buildURLparameter($("#version_" + (countProtection + 1)).select2('data')[0].text),
        		"请选择保护级别"
        );
    });
    //保护级别变化后处理的函数
    $("#protlevel_" + (countProtection + 1)).on("change", function () {
        $("#protectionCollapseTitle_" + (countProtection + 1)).text(buildProtectionTitle(countProtection));
    });

    // 初始化select2插件
    $("#protectionSearchPara_" + (countProtection + 1)).select2({
        placeholder: "请选择检索字段",
    });
}

//删除一个新参考文献
function removeProtectionReferences(protectionNum, referencesNum) {
    $("#protectionReferencesForm_" + protectionNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countProtectionReferences_" + protectionNum).val());
    var num1 = parseInt($("#descProtReferences_" + protectionNum).val());
    $("#descProtReferences_" + protectionNum).val(num1 + 1);
    var num2 = parseInt($("#descProtReferences_" + protectionNum).val());
    $("#countProtReferences_" + protectionNum).val(count - num2);
}
//添加一个新参考文献
function addProtectionReferences(protectionNum) {

    var countProtectionReferences = parseInt($('#countProtectionReferences_' + protectionNum).val());
    var desc = parseInt($("#descProtReferences_" + protectionNum).val());
    $("#countProtReferences_" + protectionNum).val(countProtectionReferences - desc + 1);
   
    var thisReferencesNum = {pnum: protectionNum, num: countProtectionReferences + 1};

    $('#protectionReferencesForm').tmpl(thisReferencesNum).appendTo('#newProtectionReferences_' + protectionNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Description下的References变化监测
	var refForm = "newProtectionReferences_" + protectionNum;
	var refId = "protectionReferences_" + protectionNum + "_" + (countProtectionReferences + 1);
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
    
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "protectionSearchPara_" + protectionNum, "protectionPyear_" + protectionNum);

    //参考文献验证规则
    addReferencesValidator(
        "newProtectionReferences_"+protectionNum,
        (countProtectionReferences+1),
        "protectionReferences_"+protectionNum+"_",
        "protectionReferencesPageS_"+protectionNum+"_",
        "protectionReferencesPageE_"+protectionNum+"_"
    );

    $('#countProtectionReferences_' + protectionNum).val(countProtectionReferences + 1);

}