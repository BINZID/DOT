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
	
	// 1.Taxkey及参考文献的回显
	var url = "/console/taxkey/rest/edit/" + $("#taxonId").val();
	$.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
        	var taxkeys = data.taxkeys;
        	if(null != taxkeys){
        		for (var i = 0; i < taxkeys.length; i++) {
        			var taxkeyId = taxkeys[i].id;
        			addTaxkey();
        			$("#taxkeyCollapseTitle_" + (i + 1)).text("检索表" + (i + 1));
        			$("#taxkeyId_" + (i+1)).val(taxkeys[i].id);
        			$("#keytypes_" + (i+1)).val(taxkeys[i].keytype).trigger('change');
        			$("#keytitle_" + (i+1)).val(taxkeys[i].title);
        			$("#abstraction_" + (i+1)).val(taxkeys[i].abstraction);
        			
        			$("#editmark_" + (i+1)).val(1);
        			
        			buildKeytable((i + 1));
        			loadKeytable((i + 1), taxkeys[i].keyitems);
        	    }
        	}
        }
    });
})