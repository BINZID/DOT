$(function(){
	/**
	 * 说明：
	 * 		1.通过原始Ajax获取Controller层返回的JSON数据；
	 * 		2.JSON数据中存放实体集合list转JSON的结果，JS遍历JSON数据，得到每一个实体对象的JSON数据；
	 * 		3.解析每一个实体对象的JSON数据，得到实体的参考文献(一个实体中由若干个参考文献)、数据源及其他属性
	 */
	
	// 1.Distributiondata及参考文献的回显
	var url = "/console/protection/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var protections = data.protections;
        	if(null != protections){
        		for (var i = 0; i < protections.length; i++) {
        			addProtection();
        			$("#protectionId_" + (i+1)).val(protections[i].id);
        			$("#proassessment_" + (i+1)).val(protections[i].proassessment);
        			
        			// 2.数据源回显
        			$("#protectionsourcesid_" + (i + 1)).prepend("<option value='" + protections[i].sourcesid + "' selected='selected'>" + protections[i].sourcesTitle +"</option>");

        			// 3.参考文献回显
        			var refs = JSON.parse(protections[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addProtectionReferences((i + 1));
        			    $('#protectionReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#protectionReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("protectionReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			}
        			
        			// 4.保护级别及保护标题回显
        			var protectstandards = protections[i].protectstandard;
        			
        			$("#standardname_" + (i+1)).append(new Option(protectstandards.standardname, protectstandards.id, false, true));
        			$("#standardname_" + (i+1)).trigger("change");
        			
        			$("#version_" + (i+1)).append(new Option(protectstandards.version, protectstandards.id, false, true));
        			$("#version_" + (i+1)).trigger("change");
        			
        			$("#protlevel_" + (i+1)).append(new Option(protectstandards.protlevel, protectstandards.id, false, true));
        			$("#protlevel_" + (i+1)).trigger("change");
        			
        			$("#protectionCollapseTitle_" + (i + 1)).text(protectstandards.standardname + " (" + protectstandards.version + ") " + protectstandards.protlevel);
        	    }
        	}
        }
    });
	
})