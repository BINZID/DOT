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
	
	// 0.Citation及参考文献的回显
	var url = "/console/description/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var descriptions = data.descriptions;
        	if(null != descriptions){
        		for (var i = 0; i < descriptions.length; i++) {
        			addDescription();
        			$("#descriptionCollapseTitle_" + (i + 1)).text("描述信息" + (i + 1));
        			$("#descriptionId_" + (i+1)).val(descriptions[i].id);
        		  	$("#destitle_" + (i+1)).val(descriptions[i].destitle);
        			$("#desdate_" + (i+1)).val(descriptions[i].desdate);
        			$("#describer_" + (i+1)).val(descriptions[i].describer);
        			$("#language_" + (i+1)).val(descriptions[i].language).trigger('change');
        			$("#rightsholder_" + (i+1)).val(descriptions[i].rightsholder);
        			$("#descontent_" + (i+1)).val(descriptions[i].descontent);
        			$("#descriptionremark_" + (i+1)).val(descriptions[i].remark);
        			var relationId = descriptions[i].relationId;
        			switch (relationId) {
	        			case "1":
	        				selectRelation((i+1), relationId, "的 翻译")
	        				break;
	        			case "2":
	        				selectRelation((i+1), relationId, "的 总结")
	        				break;
	        			case "3":
	        				selectRelation((i+1), relationId, "的 说明")
	        				break;
	        			default:
	        				selectRelation((i+1), "0", "无关系")
        			}
        			
        			var relationDes = descriptions[i].relationDes;
        			var relationDestitle = descriptions[i].relationDestitle;
        			if (null != relationDes && null != relationDestitle) {
        				// 1.关系回显
        				$("#relationDes_" + (i + 1)).prepend("<option value='" + relationDes + "' selected='selected'>" + relationDestitle +"</option>");
					}else {
						$("#relationDes_" + (i + 1)).prepend("<option value='" + "-1" + "' selected='selected'>" + "无" +"</option>");
					}
        			// 2.数据源回显
        			$("#descriptionSourcesid_" + (i + 1)).prepend("<option value='" + descriptions[i].sourcesid + "' selected='selected'>" + descriptions[i].sourcesTitle +"</option>");

        			// 3.共享协议回显
        			$("#licenseid_" + (i + 1)).prepend("<option value='" + descriptions[i].licenseid + "' selected='selected'>" + descriptions[i].licenseTitle+"</option>");
        			
        			// 4.描述类型回显
        			$("#destypeid_" + (i + 1)).prepend("<option value='" + descriptions[i].destypeid + "' selected='selected'>" + descriptions[i].destypeName+"</option>");
        			
        			// 5.参考文献回显
        			var refs = JSON.parse(descriptions[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addDescriptionReferences((i + 1));
        			    $('#descriptionReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#descriptionReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("descriptionReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			}
        			
        			// 6.描述标题回显
        			$("#descriptionCollapseTitle_" + (i + 1)).text(
        					handleHtmlText($("#destypeid_" + (i+1)).select2('data')[0].text) 
        						+ " (" + 
        						$("#destitle_" + (i+1)).val()
        						+ ") ");
        	    }
        	}
        }
    });
	
})