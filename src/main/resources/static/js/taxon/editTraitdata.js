/**
 * Created by BINZI on 18-8-24.
 */
$(function(){
	/**
	 * 说明：
	 * 		1.通过原始Ajax获取Controller层返回的JSON数据；
	 * 		2.JSON数据中存放实体集合list转JSON的结果，JS遍历JSON数据，得到每一个实体对象的JSON数据；
	 * 		3.解析每一个实体对象的JSON数据，得到实体的参考文献(一个实体中由若干个参考文献)、数据源及其他属性
	 */
	
	// 0.Traitdata及参考文献的回显
	var url = "/console/traitdata/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var traitdatas = data.traitdatas;
        	if(null != traitdatas){
        		for (var i = 0; i < traitdatas.length; i++) {
        			addTraitdata();
        			$("#traitdataId_" + (i+1)).val(traitdatas[i].id);
        			
        			// 1.术语集回显
        			$("#trainsetid_" + (i + 1)).prepend("<option value='" + traitdatas[i].trainsetid + "' selected='selected'>" + traitdatas[i].trainsetName +"</option>");
        			
        			// 2.数据源回显
        			$("#trainSourcesid_" + (i + 1)).prepend("<option value='" + traitdatas[i].sourcesid + "' selected='selected'>" + traitdatas[i].sourcesTitle +"</option>");
        			// 3.特征描述回显
        			var desid = traitdatas[i].desid;
        			var destitle = traitdatas[i].destitle;
        			if (null != desid && null != destitle) {
        				$("#desid_" + (i + 1)).prepend("<option value='" + traitdatas[i].desid + "' selected='selected'>" + traitdatas[i].destitle +"</option>");
					}else{
						// 描述来源下拉选 Description	-- 对应字段 -- desid
					    buildSelect2("desid_" + (i + 1), "console/description/rest/select/" + $("#taxonId").val(), "请选择特征来源的描述信息");
					}
        			
        			// 4.特征描述详情回显
        			var traits = JSON.parse(traitdatas[i].traitjson);
        			for (var j = 0; j < traits.length; j++) {
        				addTraitdataValue((i + 1));
        			    $('#traitdataBase_' + (i+1) + "_" + (j+1)).val(traits[j].traitbase).trigger('change');
        			    $('#traitdataValue_' + (i+1) + "_" + (j+1)).val(traits[j].traitvalue).trigger('change');
        			    var traitunit = traits[j].traitunit;
        			    if (traitunit != 1 && traitunit != 2 && traitunit != 3 && traitunit != 4) {
        			    	 $("#traitdataUnit_" + (i+1) + "_" + (j+1)).append(new Option(traitunit, traitunit, false, true));
        			    	 $("#traitdataUnit_" + (i+1) + "_" + (j+1)).trigger("change");
						}else {
							$("#traitdataUnit_" + (i+1) + "_" + (j+1)).val(traits[j].traitunit).trigger('change');
						}
        			    
        			    var data1 = [{"id":traits[j].traitontology, "text":traits[j].traitontologyName}]; 
        			    echoSelect2("traitontology_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称

        			    var data2 = [{"id":traits[j].traitproperty, "text":traits[j].traitpropertyName}]; 
        			    echoSelect2("traitdataProperty_" + (i+1) + "_" + (j + 1), data2);			// 参考文献名称
        			}
        			
        			// 5.特征数据标题回显
        			/*if (null != desid && null != traitdatas[i].trainsetid) {
						
					}*/
        			
        			$("#traitdataCollapseTitle_" + (i + 1)).text(
        					$("#trainsetid_" + (i+1)).select2('data')[0].text 
        						+ " (" + 
        					$("#desid_" + (i+1)).select2('data')[0].text 
        						+ ") ");
        			
        			// 6.参考文献回显
        			var refs = JSON.parse(traitdatas[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addTraitdataReferences((i + 1));
        			    $('#traitdataReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#traitdataReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("traitdataReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			}
        	    }
        	}
        }
    });
	
})