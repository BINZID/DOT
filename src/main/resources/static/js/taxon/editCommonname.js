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
	
	// 1.Commonname及参考文献的回显
	var url = "/console/commonname/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var commonnames = data.commonnames;
        	if(null != commonnames){
        		for (var i = 0; i < commonnames.length; i++) {
        			addCommonname();
        			$("#commonnameCollapseTitle_" + (i + 1)).text("俗名信息" + (i + 1));
        			$("#commonnameId_" + (i+1)).val(commonnames[i].id);
        			$("#commonname_" + (i+1)).val(commonnames[i].commonname);
        			$("#commonnameLanguage_" + (i+1)).val(commonnames[i].language).trigger('change');
        			// 2.数据源回显
        			$("#commonnameSourcesid_" + (i + 1)).prepend("<option value='" + commonnames[i].sourcesid + "' selected='selected'>" + commonnames[i].sourcesTitle +"</option>");
        			/* 2.5语言JSON
        			$("#commonnameLanguage_" + (i+1)).prepend("<option value='" + commonnames[i].languageCode + "' selected='selected'>" + commonnames[i].language +"</option>");
        			*/
        			// 3.参考文献回显
        			var refs = JSON.parse(commonnames[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addCommonnameReferences((i + 1));
        			    $('#commonnameReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#commonnameReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("commonnameReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			}
        	    }
        	}
        }
    });
	
})