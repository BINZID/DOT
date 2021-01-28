/**
 * Created by Tianshan on 18-5-22.
 */

//删除一个新特征数据
function removeTraitdata(traitdataNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/traitdata/rest/delete",
		{
	        "_csrf":$('input[name="_csrf"]').val(),
	        "traitdataId":$("#traitdataId_" + traitdataNum).val()
	    },
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 500}, 
				function() {
					$("#traitdataForm_" + traitdataNum).remove();
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
//提交一个特征数据
function submitTraitdata(traitdataNum) {
    traitdataFormValidator(traitdataNum);
    if (($("tr[id^='"+'traitdataValueForm_'+traitdataNum+"_']").length>0 && traitdataValidator('newTraitdataValue_'+traitdataNum,3)) &&
        ($("tr[id^='"+'traitdataReferencesForm_'+traitdataNum+"_']").length<=0 || referencesValidator('newTraitdataReferences_'+traitdataNum,3))) {
        //处理ajax提交
    	var obj = $('#traitdataForm_' + traitdataNum).serialize();
    	var url = "/console/traitdata/rest/add/" + $('#taxonId').val();
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
		        	    if ($('#traitdataCollapse_' + traitdataNum).hasClass('in')) {
		        	        $('#traitdataCollapseTitle_' + traitdataNum).trigger("click");
		        	    }
		        	    $('#traitdataForm_' + traitdataNum).removeClass("panel-default");
		        	    $('#traitdataForm_' + traitdataNum).removeClass("panel-danger");
		        	    $('#traitdataForm_' + traitdataNum).addClass("panel-success");
		        	    $('#traitdataStatus_' + traitdataNum).removeClass("hidden");
		        	    postSuccess = true;
        			});
    		}else{
    			layer.msg("添加失败！", function () {
                });
    			postSuccess = false;
    		}
          },
          error: function() {
        	  postSuccess = true;
          }
        });
    	if (!$('#traitdataCollapse_' + traitdataNum).hasClass('in')) {
            $('#traitdataCollapseTitle_' + traitdataNum).trigger("click");
        }
        $('#traitdataForm_' + traitdataNum).removeClass("panel-success");
        $('#traitdataForm_' + traitdataNum).addClass("panel-danger");
        $('#traitdataStatus_' + traitdataNum).addClass("hidden");
        layer.msg("添加失败", function () {
        });
        return postSuccess;
    } else {
        if (!$('#traitdataCollapse_' + traitdataNum).hasClass('in')) {
            $('#traitdataCollapseTitle_' + traitdataNum).trigger("click");
        }
        $('#traitdataForm_' + traitdataNum).removeClass("panel-success");
        $('#traitdataForm_' + traitdataNum).addClass("panel-danger");
        $('#traitdataStatus_' + traitdataNum).addClass("hidden");
        /*$('#tab-traitdata').addClass("alert alert-danger");*/
        layer.msg("请完成此文本描述的填写", function () {
        });
        return false;
    }
}

//提交所有特征数据
function submitAllTraitdata() {
    var submitResult = true;
    $('#tab-traitdata').removeClass("alert-danger");
    $('#tab-traitdata').removeClass("alert");
    $("form[id^='traitdataForm_']").each(function () {
        if (submitTraitdata($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}

//添加一个新特征数据
function addTraitdata() {
	
    var countTraitdata = parseInt($('#countTraitdata').val());

    var thisTraitdataNum = {num: countTraitdata + 1};

    $('#traitdataForm').tmpl(thisTraitdataNum).appendTo('#newTraitdata');
    // 术语集下拉选    TraitSet		-- 对应字段 -- traitsetId	
    buildSelect2("trainsetid_" + (countTraitdata + 1), "console/traitset/rest/select", "请选择特征所属的大类");
    
 // Form3 Distribution下的Datasource变化检测
    var width=$(window).width();
    var height=$(window).height();
    var layer_width="90%";
    var layer_height="90%";
    if(width>760){
    	layer_width="500px";
    	layer_height="500px";
    }						  
	var sourcesForm = "traitdataForm_" + (countTraitdata + 1);
    var sourcesId = "trainSourcesid_" + (countTraitdata + 1);
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

    // 源数据下拉选    Datasource		-- 对应字段 -- sourcesjson
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$("#sourcesid").val()+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");
    // 描述来源下拉选 Description	-- 对应字段 -- desid
    buildSelect2("desid_" + (countTraitdata + 1), "console/description/rest/select/" + $("#taxonId").val(), "请选择特征来源的描述信息");
    
    $('#countTraitdata').val(countTraitdata + 1);

    addTraitdataValidator(countTraitdata + 1);

    function buildTraitdataTitle(countTraitdata) {
        var titleText = "";
        if ($("#trainsetid_" + (countTraitdata + 1)).select2('data')[0]) {
            titleText = $("#trainsetid_" + (countTraitdata + 1)).select2('data')[0].text
        }
        if ($("#desid_" + (countTraitdata + 1)).select2('data')[0]) {
            titleText = titleText
                + " (" + $("#desid_" + (countTraitdata + 1)).select2('data')[0].text  + ") "
        }
        return titleText;
    }

    //术语集变化后处理的函数
    $("#trainsetid_" + (countTraitdata + 1)).on("change", function () {
        $("#traitdataCollapseTitle_" + (countTraitdata + 1)).text(buildTraitdataTitle(countTraitdata));
    });
    //源数据变化后处理的函数
    $("#desid_" + (countTraitdata + 1)).on("change", function () {
        $("#traitdataCollapseTitle_" + (countTraitdata + 1)).text(
            buildTraitdataTitle(countTraitdata)
        );
    });
    
    //唯一标识UUID
    $.get("/console/taxon/rest/uuid", function(id){
    	if (null == $("#traitdataId_" + (countTraitdata + 1)).val() || "" == $("#traitdataId_" + (countTraitdata + 1)).val()) {
    		$("#traitdataId_" + (countTraitdata + 1)).val(id);
		}
    });
    
    //清空存储在Session中Select2联动选中值
     $.get("/console/traitontology/rest/removeSel");
     
     // 初始化select2插件
     $("#traitdataSearchPara_" + (countTraitdata + 1)).select2({
         placeholder: "请选择检索字段",
     });
}
//删除一个特征详细描述
function removeTraitdataValue(traitdataNum, referencesNum) {
    $("#traitdataValueForm_" + traitdataNum + "_" + referencesNum).remove();
 // 计算实际特征详细描述的数量
    var count = parseInt($("#countTraitdataValue_" + traitdataNum).val());
    var num1 = parseInt($("#descTraitValue_" + traitdataNum).val());
    $("#descTraitValue_" + traitdataNum).val(num1 + 1);
    var num2 = parseInt($("#descTraitValue_" + traitdataNum).val());
    $("#countTraitValue_" + traitdataNum).val(count - num2);
}
//添加一个特征详细描述
function addTraitdataValue(traitdataNum) {
    var countTraitdataValue = parseInt($('#countTraitdataValue_' + traitdataNum).val());
    var desc = parseInt($("#descTraitValue_" + traitdataNum).val());
    $("#countTraitValue_" + traitdataNum).val(countTraitdataValue -desc + 1);

    var thisValueNum = {tnum: traitdataNum, num: countTraitdataValue + 1};

    $('#traitdataValueForm').tmpl(thisValueNum).appendTo('#newTraitdataValue_' + traitdataNum);
    
    function buildTraitdataTitle(traitdataNum) {
        var titleText = "";
        if ($("#trainsetid_" + traitdataNum).select2('data')[0]) {
            titleText = $("#trainsetid_" + traitdataNum).select2('data')[0].text
        }
        return titleText;
    }

  //构造url_parameter
    function buildURLparameter (type) {
        var trainsetid=$("#trainsetid_" + traitdataNum).val();
        var url_parameter=
            "?type="+type+
            "&trainsetid="+trainsetid;
        return url_parameter;
    }
    
    //构造select2
    buildSelect2 (
    		"traitontology_" + traitdataNum + "_" + (countTraitdataValue + 1),
    		"console/traitontology/rest/select",
    		"请选择术语(特征)"
    );
    
    // 描述详情属性下拉选 Property	-- 对应字段 -- traitdataProperty
    buildSelect2(
    		"traitdataProperty_" + traitdataNum + "_" + (countTraitdataValue + 1), 
    		"/console/traitproperty/rest/select", 
    		"请选择描述属性");

    // 描述详情单位下拉选 Unit	-- 对应字段 -- traitdataUnit
    $("#traitdataUnit_" + traitdataNum + "_" + (countTraitdataValue + 1)).select2({
        placeholder: "请选择单位",
        tags: true,
    });
    
    traitontologyByTraitset(traitdataNum, (countTraitdataValue + 1));

    //特征详细描述验证规则
    addTraitdataValueValidator(
        "newTraitdataValue_"+traitdataNum,
        (countTraitdataValue+1),
        "traitontology_"+traitdataNum+"_",
        "traitdataProperty_"+traitdataNum+"_",
        "traitdataValue_"+traitdataNum+"_",
        "traitdataBase_"+traitdataNum+"_"
    );

    $('#countTraitdataValue_' + traitdataNum).val(countTraitdataValue + 1);

}

//术语集变化后处理的函数 -- 联动选择
function traitontologyByTraitset(traitdataNum, countTraitdataValue) {
    $("#trainsetid_" + traitdataNum).on("change", function () {
        $("#traitontology_" + traitdataNum + "_" + countTraitdataValue).empty();
        $("#traitontology_" + traitdataNum + "_" + countTraitdataValue).prepend("<option value='-1'>请选择术语(特征)</option>");
        //构造select2
        buildSelect2 (
        		"traitontology_" + traitdataNum + "_" + countTraitdataValue,
        		"console/traitontology/rest/select" + buildURLparameter($("#trainsetid_" + traitdataNum).select2('data')[0].text),
        		"请选择术语(特征)"
        );
    });
}
//删除一个新参考文献
function removeTraitdataReferences(traitdataNum, referencesNum) {
    $("#traitdataReferencesForm_" + traitdataNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countTraitdataReferences_" + traitdataNum).val());
    var num1 = parseInt($("#descTraitReferences_" + traitdataNum).val());
    $("#descTraitReferences_" + traitdataNum).val(num1 + 1);
    var num2 = parseInt($("#descTraitReferences_" + traitdataNum).val());
    $("#countTraitReferences_" + traitdataNum).val(count - num2);
}
//添加一个新参考文献
function addTraitdataReferences(traitdataNum) {

    var countTraitdataReferences = parseInt($('#countTraitdataReferences_' + traitdataNum).val());
    var desc = parseInt($("#descTraitReferences_" + traitdataNum).val());
    $("#countTraitReferences_" + traitdataNum).val(countTraitdataReferences - desc + 1);

    var thisReferencesNum = {tnum: traitdataNum, num: countTraitdataReferences + 1};

    $('#traitdataReferencesForm').tmpl(thisReferencesNum).appendTo('#newTraitdataReferences_' + traitdataNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Description下的References变化监测
	var refForm = "newTraitdataReferences_" + traitdataNum; 
	var refId = "traitdataReferences_" + traitdataNum + "_" + (countTraitdataReferences + 1);
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
    
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "traitdataSearchPara_" + traitdataNum, "traitdataPyear_" + traitdataNum);

    //参考文献验证规则
    addReferencesValidator(
        "newTraitdataReferences_"+traitdataNum,
        (countTraitdataReferences+1),
        "traitdataReferences_"+traitdataNum+"_",
        "traitdataReferencesPageS_"+traitdataNum+"_",
        "traitdataReferencesPageE_"+traitdataNum+"_"
    );

    $('#countTraitdataReferences_' + traitdataNum).val(countTraitdataReferences + 1);
}