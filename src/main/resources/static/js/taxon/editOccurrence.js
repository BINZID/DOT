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
	
	// 1.Occurrence及参考文献的回显
	var url = "/console/occurrence/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var occurrences = data.occurrences;
        	if(null != occurrences){
        		for (var i = 0; i < occurrences.length; i++) {
        			addOccurrence();
        			$("#occurrenceCollapseTitle_" + (i + 1)).text("观测记录" + (i + 1));
        			$("#occurrenceId_" + (i+1)).val(occurrences[i].id);
        			$("#eventdate_" + (i+1)).val(occurrences[i].eventdate);
        			$("#occurrence_city_" + (i+1)).val(occurrences[i].city);
        			$("#occurrence_country_" + (i+1)).val(occurrences[i].country);
        			$("#occurrence_county_" + (i+1)).val(occurrences[i].county);
        			$("#occurrence_province_" + (i+1)).val(occurrences[i].province);
        			$("#occurrence_location_" + (i+1)).val(occurrences[i].location);
        			$("#occurrence_locality_" + (i+1)).val(occurrences[i].locality);
        			$("#occurrence_lat_" + (i+1)).val(occurrences[i].lat);
        			$("#occurrence_lng_" + (i+1)).val(occurrences[i].lng);
        			$("#occurrence_type_" + (i + 1)).val(occurrences[i].type).trigger('change');
        			
        			// 2.数据源回显
        			$("#occurrenceSourcesid_" + (i + 1)).prepend("<option value='" + occurrences[i].sourcesid + "' selected='selected'>" + occurrences[i].sourcesTitle +"</option>");

        			// 3.参考文献回显
        			var refs = JSON.parse(occurrences[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addOccurrenceReferences((i + 1));
        			    $('#occurrenceReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#occurrenceReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("occurrenceReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			}
        	    }
        	}
        }
    });
	
})