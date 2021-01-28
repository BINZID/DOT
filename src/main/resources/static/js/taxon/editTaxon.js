/**
 * Created by BINZI on 18-08-21.
 */
//Taxon基础信息参考文献回显
$(function(){
	// 0.数据源回显
	var sourcesid = $("#datasourceid").val();
	if (null != sourcesid) {
		$.get("/console/datasource/rest/editSource/"+sourcesid, function(data){
			$("#sourcesid").append(new Option(data.text, sourcesid, false, true));
			$("#sourcesid").trigger("change");
		});
	}
	
	var nomencode = $("#tnomencode").val();
	if (null != nomencode && "" != nomencode) {
		$("#nomencode").append(new Option(nomencode, nomencode, false, true));
		$("#nomencode").trigger("change");
	}

	// 2.参考文献回显
	var refjson = $("#taxonRef").val();
	var json = JSON.parse(refjson);	// 返回值：对象 | 数组
	
	for (var i = 0; i < json.length; i++) {
		//添加一个新参考文献
		addReferences();
		
	    $('#referencesPageS_' + (i+1)).val(json[i].refS);
	    $('#referencesPageE_' + (i+1)).val(json[i].refE);
	    
	    var data1 = [{"id":json[i].refId, "text":json[i].refstr}]; 
	    echoSelect2("references_" + (i+1), data1);			// 参考文献名称
	}   
})