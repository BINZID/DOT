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
	
	// 1.Multimedia及参考文献的回显
	var url = "/console/multimedia/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var multimedias = data.multimedias;
        	if(null != multimedias){
        		for (var i = 0; i < multimedias.length; i++) {
        			addMultimedia();
        			$("#multimediaCollapseTitle_" + (i + 1)).text("多媒体信息" + (i + 1));
        			$("#multimediaId_" + (i+1)).val(multimedias[i].id);
        			$("#multimedia_title_" + (i+1)).val(multimedias[i].title);
        			$("#multimedia_context_" + (i+1)).val(multimedias[i].context);
        			$("#multimedia_rightsholder_" + (i+1)).val(multimedias[i].rightsholder);
        			$("#multimedia_licenseid_" + (i+1)).val(multimedias[i].lisenceid);
        			$("#media_type_" + (i+1)).val(multimedias[i].mediatype).trigger('change');
        			
        			$("#multimedia_city_" + (i+1)).val(multimedias[i].city);
        			$("#multimedia_country_" + (i+1)).val(multimedias[i].country);
        			$("#multimedia_county_" + (i+1)).val(multimedias[i].county);
        			$("#multimedia_province_" + (i+1)).val(multimedias[i].province);
        			$("#multimedia_location_" + (i+1)).val(multimedias[i].location);
        			$("#multimedia_locality_" + (i+1)).val(multimedias[i].locality);
        			$("#multimedia_type_" + (i+1)).val(multimedias[i].type);
        			$("#multimedia_lat_" + (i+1)).val(multimedias[i].lat);
        			$("#multimedia_lng_" + (i+1)).val(multimedias[i].lng);
        			
        			var val = multimedias[i].mediatype;
        			
        			$("#multimediaPath_" + (i + 1)).val(multimedias[i].path);
        			$("#input-fa_" + (i + 1)).hide();
        			$("#input-fa_" + (i + 1)).fileinput('destroy');
        			$("#file_" + (i + 1)).before(
        			"<img src=\"" + "upload/images/" + multimedias[i].path + "\" class='img-responsive img-thumbnail' width='30%' height='30%' title='点击查看大图' alt='点击查看大图' onclick= \"showLargeImage('upload/images/" + multimedias[i].path + "')\">");
        			 
        			//非图片、音频、视频隐藏拍摄地点信息
        			$("select[id = media_type_" + (i + 1) + "]").on("change", function(){
        				if (val == 1 || val == 2 || val == 3) {
        					$("#oldpath_" + (i + 1)).show();
        					$("#locationInfo_" + (i + 1)).show();
        				}else{
        					$("#oldpath_" + (i + 1)).hide();
        					$("#locationInfo_" + (i + 1)).hide();
        				}
        		    });
        			
        			// 2.数据源回显
        			$("#multimediasourcesid_" + (i + 1)).prepend("<option value='" + multimedias[i].sourcesid + "' selected='selected'>" + multimedias[i].sourcesTitle +"</option>");

        			// 3.共享协议回显
        			$("#multimedia_licenseid_" + (i + 1)).prepend("<option value='" + multimedias[i].licenseid + "' selected='selected'>" + multimedias[i].licenseTitle+"</option>");
        			
        			// 4.参考文献回显
        			var refs = JSON.parse(multimedias[i].refjson);
        			for (var j = 0; j < refs.length; j++) {
        				addMultimediaReferences((i + 1));
        			    $('#multimediaReferencesPageS_' + (i+1) + "_" + (j+1)).val(refs[j].refS).trigger('change');
        			    $('#multimediaReferencesPageE_' + (i+1) + "_" + (j+1)).val(refs[j].refE).trigger('change');
        			    
        			    var data1 = [{"id":refs[j].refId, "text":refs[j].refstr}]; 
        			    echoSelect2("multimediaReferences_" + (i+1) + "_" + (j + 1), data1);			// 参考文献名称
        			}
        	    }
        	}
        }
    });
	
})