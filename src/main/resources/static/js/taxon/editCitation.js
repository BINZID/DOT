$(function(){
	/**
	 * 说明：
	 * 		1.通过原始Ajax获取Controller层返回的JSON数据；
	 * 		2.JSON数据中存放实体集合list转JSON的结果，JS遍历JSON数据，得到每一个实体对象的JSON数据；
	 * 		3.解析每一个实体对象的JSON数据，得到实体的参考文献(一个实体中由若干个参考文献)、数据源及其他属性
	 */
	
	// 1.Citation及参考文献的回显
	var url = "/console/citation/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var citations = data.citations;
        	if(null != citations){
        		for (var i = 0; i < citations.length; i++) {
        			addCitation();
        			$("#citationId_" + (i+1)).val(citations[i].id);
        		  	$("#nametype_" + (i+1)).val(citations[i].nametype).trigger('change');
        			$("#sciname_" + (i+1)).val(citations[i].sciname);
        			$("#authorship_" + (i+1)).val(citations[i].authorship);
        			$("#citationstr_" + (i+1)).val(citations[i].citationstr);
        			
        			// 2.数据源回显
        			$("#citationSourcesid_" + (i + 1)).prepend("<option value='" + citations[i].sourcesid + "' selected='selected'>" + citations[i].sourcesTitle +"</option>");

        			// 3.参考文献回显
        			var refs = JSON.parse(citations[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addCitationReferences((i + 1));
        			    $('#citationReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#citationReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("citationReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			    
        			    var context;
        			    if(refs[j].refType == 0){
        			    	context = "原始文献";
        			    }else if(refs[j].refType == 1){
        			    	context = "其他文献";
        			    }else{
        			    	context = "选择参考类型";
        			    }
        			    selectCitationReferences((i + 1), (j + 1), refs[j].refType, context);		// 参考文献类型
        			}
        			
        			// 4。
        			$("#citationCollapseTitle_" + (i+1)).html(
        					"<i>" + $("#sciname_" + (i+1)).val()
    						+ "</i> " + 
    						$("#authorship_" + (i+1)).val());
        	    }
        	}
        }
    });
	
})