/**
 * Created by Tianshan on 18-5-22.
 */
//选择关系
function selectDisRelation(distributiondataNum, relationId, relationText) {
    $("#disrelationId_" + distributiondataNum).val(relationId);
    $("#disrelationBtn_" + distributiondataNum).text(relationText);
}

function parseGeoobject(distributiondataNum) {
	var disContent = $("#discontent_" + distributiondataNum).val();
	var parseType = $("#parseType_" + distributiondataNum).select2("val");
	if (disContent.length <= 0) {
		layer.msg("请输入分布描述信息！", {time : 1000})
	}else if(parseType <= 0){
		layer.msg("请选择地理实体解析标准！", {time : 1000})
	}else {
		// Ajax解析
		$.get("/console/distributiondata/rest/parseGeoobject/" + disContent + "/" + parseType,
			function(data){
			var items = data.items
			if (null != items) {
				// 行政区分布地回显
    			for (var i = 0; i < items.length; i++) {
    			    var data2 = [{"id":items[i].id, "text":items[i].text}]; 
    			    echoSelect2("geojson_" + distributiondataNum, data2);			// 参考文献名称
    			}
			}
		})
	}
}

//选择关系
function selectRelation(distributiondataNum, relationId, relationText) {
    $("#relationId_" + distributiondataNum).val(relationId);
    $("#relationBtn_" + distributiondataNum).text(relationText);
}
//删除一个新分布
function removeDistributiondata(distributiondataNum) {
    var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/distributiondata/rest/delete",
		{
	        "_csrf":$('input[name="_csrf"]').val(),
	        "distributiondataId":$("#distributiondataId_" + distributiondataNum).val()
	    },
		function(status) {
			if (status) {
				layer.msg('删除成功', {time : 1000}, 
				function() {
					$("#distributiondataForm_" + distributiondataNum).remove();
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
//提交一个分布
function submitDistributiondata(distributiondataNum) {
    distributiondataFormValidator(distributiondataNum);
    if ($('#distributiondataForm_' + distributiondataNum).data('bootstrapValidator').isValid() &&
        ($("tr[id^='"+'distributiondataReferencesForm_'+distributiondataNum+"_']").length<=0 || referencesValidator('newDistributiondataReferences_'+distributiondataNum,3))) {
    	//处理ajax提交
    	var obj = $('#distributiondataForm_' + distributiondataNum).serialize();
    	var url = "/console/distributiondata/rest/add/" + $('#taxonId').val();
        var postSuccess=false;
        return $.ajax({
          type: "POST",
          url: url,
          data: obj,	// 要提交的表单
          dataType: "json",
          contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
          success: function (msg) {
        	if (msg.result == true) {
        		layer.msg('提交成功，请继续填写其他内容',
        	            {time: 1500},
        	            function () {
        	                if ($('#distributiondataCollapse_' + distributiondataNum).hasClass('in')) {
        	                    $('#distributiondataCollapseTitle_' + distributiondataNum).trigger("click");
        	                }
        	                $('#distributiondataForm_' + distributiondataNum).removeClass("panel-default");
        	                $('#distributiondataForm_' + distributiondataNum).removeClass("panel-danger");
        	                $('#distributiondataForm_' + distributiondataNum).addClass("panel-success");
        	                $('#distributiondataStatus_' + distributiondataNum).removeClass("hidden");
        	            });
                postSuccess = true;
			}else{
				layer.msg("添加失败！", {time: 1000});
                postSuccess = false;
			}
          }
        });
        return postSuccess;
    }
    else {
        if (!$('#distributiondataCollapse_' + distributiondataNum).hasClass('in')) {
            $('#distributiondataCollapseTitle_' + distributiondataNum).trigger("click");
        }
        $('#distributiondataForm_' + distributiondataNum).removeClass("panel-success");
        $('#distributiondataForm_' + distributiondataNum).addClass("panel-default");
        $('#distributiondataStatus_' + distributiondataNum).addClass("hidden");
        layer.msg("请完成此分布数据的填写", function () {
        });
        return false;
    }
}

//提交所有distributiondata
function submitAllDistributiondata() {
    var submitResult = true;
    $('#tab-distributiondata').removeClass("alert-danger");
    $('#tab-distributiondata').removeClass("alert");
    $("form[id^='distributiondataForm_']").each(function () {
        if (submitDistributiondata($(this).attr("id").substr($(this).attr("id").lastIndexOf("_") + 1))) {
        }
        else {
            submitResult = false;
        }
    });
    return submitResult;
}
//添加一个新分布
function addDistributiondata() {

    var countDistributiondata = parseInt($('#countDistributiondata').val());

    var thisDistributiondataNum = {
        num: countDistributiondata + 1
    };

    $('#distributiondataForm').tmpl(thisDistributiondataNum).appendTo('#newDistributiondata');

    // 描述类型下拉选
    buildSelect2("distypeid_" + (countDistributiondata + 1), "console/descriptiontype/rest/select/distribution", "请选择分布类型");
    // 共享协议下拉选
    buildSelect2("dislicenseid_" + (countDistributiondata + 1), "console/license/rest/select", "请选择共享协议");

    // 描述下拉选
    buildSelect2("disrelationDes_" + (countDistributiondata + 1), "console/description/rest/select/" + $("#taxonId").val(), "请选择关系");
	// 俗名信心语言下拉选
	$("#dislanguage_" + (countDistributiondata + 1)).select2({
		placeholder: "请选择描述语言",
	});
	$("#geogroup_" + (countDistributiondata + 1)).select2({
		placeholder: "请选择地理实体划分标准",
	});
    
    // 行政区下拉选
    buildSelect("geojson_" + (countDistributiondata + 1), "console/geoobject/rest/select/administrative", "请选择行政区分布地");
    $("#distributiondatasourcesid_" + (countDistributiondata + 1)).select2({
        placeholder: "请选择数据来源"
    });
    var geojson = "geojson_" + (countDistributiondata + 1);
    var mark = $("#" + geojson).val;
    if (mark != null && mark != undefined && mark != "") {
    	$("#" + geojson).on("change",function(e){
    		/*alert($("#geojson_" + (countDistributiondata + 1)).select2('data'));*/
    		var o = document.getElementById(geojson).getElementsByTagName('option');
    		var geoids = "";
    		for(var i = 0; i < o.length; i++){
    			if(o[i].selected){
    				geoids += o[i].value + ",";
    			}
    		}
    		
    		geoids = geoids.substr(0, geoids.length - 1);//去掉末尾的逗号
    		$.get("/console/geoobject/rest/getGeoobject/" + geoids,
    				function(data){
    			var geoobjects = "";
    			for (var j = 0; j < data.length; j++) {
    				geoobjects += data[j].geoobject + "\n";
    			}
    			
    			$("#geoobjects_" + (countDistributiondata + 1)).val(geoobjects);
    		});
    	})
	}
    
    // 描述来源下拉选 Description	-- 对应字段 -- desid
    buildSelect2("disdesid_" + (countDistributiondata + 1), "console/description/rest/select/" + $("#taxonId").val(), "请选择分布数据的描述信息");
    
    // 分布描述原文通过下拉选自动补充
    var disdesid = "disdesid_" + (countDistributiondata + 1);
    $("select[id = " + disdesid + "]").on("change", function(){
    	var descid = $("#" + disdesid).val();
    	$.get("/console/description/rest/getDescContent/" + descid,
    			function(data){
    			    $("#distitle_" + (countDistributiondata + 1)).val(data.distitle);
    			    $("#disdate_" + (countDistributiondata + 1)).val(data.disdate);
    			    $("#discriber_" + (countDistributiondata + 1)).val(data.discriber);
    			    $("#disrightsholder_" + (countDistributiondata + 1)).val(data.disrightsholder);
    			    $("#discontent_" + (countDistributiondata + 1)).val(data.discontent);
    			    $("#dislanguage_" + (countDistributiondata + 1)).val(data.dislanguage).trigger('change');
        			$("#dislicenseid_" + (countDistributiondata + 1)).prepend("<option value='" + data.licenseid + "' selected='selected'>" + data.licenseTitle+"</option>");
    			});
    });
    
    $('#countDistributiondata').val(countDistributiondata + 1);

    addDistributiondataValidator(countDistributiondata + 1);

    //分布描述变化后处理的函数
    $("#discontent_" + (countDistributiondata + 1)).on("change",
        function () {
            if($("#discontent_" + (countDistributiondata + 1)).val().length>10){
                $("#distributiondataCollapseTitle_" + (countDistributiondata + 1)).text($("#discontent_" + (countDistributiondata + 1)).val().substring(1,10)+'...');
            }
            else{
                $("#distributiondataCollapseTitle_" + (countDistributiondata + 1)).text($("#discontent_" + (countDistributiondata + 1)).val());
            }

        });
    
    // 唯一标识UUID 分布
    $.get("/console/taxon/rest/uuid", function(id){
    	if (null == $("#distributiondataId_" + (countDistributiondata + 1)).val() || "" == $("#distributiondataId_" + (countDistributiondata + 1)).val()) {
    		$("#distributiondataId_" + (countDistributiondata + 1)).val(id);
		}
    });
    
    // 唯一标识UUID 描述
    $.get("/console/taxon/rest/uuid", function(id){
    	if (null == $("#disdescriptionId_" + (countDistributiondata + 1)).val() || "" == $("#disdescriptionId_" + (countDistributiondata + 1)).val()) {
    		$("#disdescriptionId_" + (countDistributiondata + 1)).val(id);
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
	var sourcesForm = "distributiondataForm_" + (countDistributiondata + 1);
    var sourcesId = "distributiondatasourcesid_" + (countDistributiondata + 1);
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

    // 数据源下拉选
    buildSelect2(sourcesId, "console/datasource/rest/select", "请选择数据来源");
    $("#" + sourcesId).prepend("<option value='"+$('#sourcesid').val()+"' selected='selected'>"+$("#sourcesid").select2("data")[0].text+"</option>");

    // 初始化select2插件
    $("#distributiondataSearchPara_" + (countDistributiondata + 1)).select2({
        placeholder: "请选择检索字段",
    });
    
    // 初始化select2插件
    $("#parseType_" + (countDistributiondata + 1)).select2({
    	placeholder: "请选择地理实体解析标准",
    });
}

//删除一个新参考文献
function removeDistributiondataReferences(distributiondataNum, referencesNum) {
    $("#distributiondataReferencesForm_" + distributiondataNum + "_" + referencesNum).remove();
    // 计算实际ref的数量
    var count = parseInt($("#countDistributiondataReferences_" + distributiondataNum).val());
    var num1 = parseInt($("#descDistReferences_" + distributiondataNum).val());
    $("#descDistReferences_" + distributiondataNum).val(num1 + 1);
    var num2 = parseInt($("#descDistReferences_" + distributiondataNum).val());
    $("#countDistReferences_" + distributiondataNum).val(count - num2);
}
//添加一个新参考文献
function addDistributiondataReferences(distributiondataNum) {

    var countDistributiondataReferences = parseInt($('#countDistributiondataReferences_' + distributiondataNum).val());
    var desc = parseInt($("#descDistReferences_" + distributiondataNum).val());
    $("#countDistReferences_" + distributiondataNum).val(countDistributiondataReferences - desc + 1);

    var thisReferencesNum = {dinum: distributiondataNum, num: countDistributiondataReferences + 1};

    $('#distributiondataReferencesForm').tmpl(thisReferencesNum).appendTo('#newDistributiondataReferences_' + distributiondataNum);

    var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "500px";
		layer_height = "500px";
	}
	// Description下的References变化监测
	var refForm = "newDistributiondataReferences_" + distributiondataNum;
	var refId = "distributiondataReferences_" + distributiondataNum + "_" + (countDistributiondataReferences + 1);
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
	
	buildSelectForRef(refId, "console/ref/rest/select", "请选择或输入内容检索参考文献", "distributiondataSearchPara_" + distributiondataNum, "distributiondataPyear_" + distributiondataNum);

    //参考文献验证规则
    addReferencesValidator(
        "newDistributiondataReferences_"+distributiondataNum,
        (countDistributiondataReferences+1),
        "distributiondataReferences_"+distributiondataNum+"_",
        "distributiondataReferencesPageS_"+distributiondataNum+"_",
        "distributiondataReferencesPageE_"+distributiondataNum+"_"
    );

    $('#countDistributiondataReferences_' + distributiondataNum).val(countDistributiondataReferences + 1);
}