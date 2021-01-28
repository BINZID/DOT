/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showTaxkey(taxonId,offset,type) {
    if(type=='first')
        $("#showTaxkeyFrame").empty();
    else if(type=='more')
        $("#moreTaxkeyButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/taxkey/rest/taxkeyList/"+taxonId,
        {
            limit:10,
            offset:offset,
            async:true
        },
        function(data,status){
            if(status){
                try {
                    //计数
                    if(data.total>0)
                        $("#showCountDescriptionInfo").text("共"+data.total+"条检索表");
                    else
                        $("#showCountDescriptionInfo").text("无检索表");
                    //配置Taxkey列表
                    $("#taxkeyList").tmpl(data).appendTo("#showTaxkeyFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreTaxkey").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showTaxkeyFrame");
                    });
                } catch (err) {
                    $("#reLoadTaxkey").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showTaxkeyFrame");
                }
            }
            else{
                $("#reLoadTaxkey").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showTaxkeyFrame");
            }
        });
}


//显示检索表详情模态框
function showTaxkeyTable(taxkeyId) {
    $('#taxkeyTable').bootstrapTable('load', "[]");
    $("#taxkeyModalTable").modal({
        keyboard:true,//当按下esc键时，modal框消失
    });
    $.get("/console/keyitem/rest/table",
        {
            "taxkeyId":taxkeyId
        },
        function(result){
            $('#taxkeyTable').bootstrapTable('load',result);
        });
};

//显示检索表图片模态框
function showTaxkeyImages(keyitemId) {
    var width = $(window).width();
    var layer_width = "90%";
    var layer_height = "90%";
    if (width > 760) {
        layer_width = "1000px";
        layer_height = "600px";
    }
    layer.open({
        type : 2,
        title : '<h4>查看特征图片</h4>',
        fixed : false, // 不固定
        area : [ layer_width, layer_height ],
        content : '/console/taxkey/optionFeatureImg/' + keyitemId
    });
};