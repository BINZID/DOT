$(function(){
	/**
	 * 说明：
	 * 		1.通过原始Ajax获取Controller层返回的JSON数据；
	 * 		2.JSON数据中存放实体集合list转JSON的结果，JS遍历JSON数据，得到每一个实体对象的JSON数据；
	 * 		3.解析每一个实体对象的JSON数据，得到实体的参考文献(一个实体中由若干个参考文献)、数据源及其他属性
	 */
	
	// 1.Distributiondata及参考文献的回显
	var url = "/console/distributiondata/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var distributiondatas = data.distributiondatas;
        	if(null != distributiondatas){
        		for (var i = 0; i < distributiondatas.length; i++) {
        			addDistributiondata();
        			$("#distributiondataCollapseTitle_" + (i + 1)).text("分布数据" + (i + 1));
        			$("#distributiondataId_" + (i+1)).val(distributiondatas[i].id);
        			$("#discontent_" + (i+1)).val(distributiondatas[i].discontent);
        			$("#disremark_" + (i+1)).val(distributiondatas[i].disremark);
        			$("#disdescriptionId_" + (i+1)).val(distributiondatas[i].descid);
        			// 2.数据源回显
        			$("#distributiondatasourcesid_" + (i + 1)).prepend("<option value='" + distributiondatas[i].sourcesid + "' selected='selected'>" + distributiondatas[i].sourcesTitle +"</option>");
        			
        			// 5.描述
        			$("#disdesid_" + (i + 1)).prepend("<option value='" + distributiondatas[i].descid + "' selected='selected'>" + distributiondatas[i].descTitle +"</option>");
        			$("#distitle_" + (i + 1)).val(distributiondatas[i].distitle);
    			    $("#disdate_" + (i + 1)).val(distributiondatas[i].disdate);
    			    $("#discriber_" + (i + 1)).val(distributiondatas[i].discriber);
    			    $("#disrightsholder_" + (i + 1)).val(distributiondatas[i].disrightsholder);
    			    $("#discontent_" + (i + 1)).val(distributiondatas[i].discontent);
    			    $("#dislanguage_" + (i + 1)).val(distributiondatas[i].dislanguage).trigger('change');
        			$("#dislicenseid_" + (i + 1)).prepend("<option value='" + distributiondatas[i].licenseid + "' selected='selected'>" + distributiondatas[i].licenseTitle+"</option>");

        			// 6.描述类型回显
        			$("#distypeid_" + (i + 1)).prepend("<option value='" + distributiondatas[i].distypeid + "' selected='selected'>" + distributiondatas[i].distypeName+"</option>");
        			var relationDes = distributiondatas[i].disrelationDes;
        			var relationDestitle = distributiondatas[i].disrelationDestitle;
        			if (null != relationDes && null != relationDestitle) {
        				// 7.关系回显
        				$("#disrelationDes_" + (i + 1)).prepend("<option value='" + relationDes + "' selected='selected'>" + relationDestitle +"</option>");
					}else {
						$("#disrelationDes_" + (i + 1)).prepend("<option value='" + "-1" + "' selected='selected'>" + "无" +"</option>");
					}
        			
        			// 3.参考文献回显
        			var refjson = distributiondatas[i].refjson;
        			if (null != refjson) {
        				var refs = JSON.parse(refjson);
        				for (var j = 0; j < refs.length; j++) {
        					addDistributiondataReferences((i + 1));
        					$('#distributiondataReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        					$('#distributiondataReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        					
        					var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        					echoSelect2("distributiondataReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        				}
					}
        			
        			// 4.行政区分布地回显
        			var geojson = distributiondatas[i].geojson;
        			if (null != geojson) {
            			var geos = JSON.parse(geojson);
            			for (var k = 0; k < geos.length; k++) {
            			    var data2 = [{"id":geos[k].geoId, "text":geos[k].geoName}]; 
            			    echoSelect2("geojson_" + (i+1), data2);			// 参考文献名称
            			}

					}
        		}
        	}
        }
    });
	
})