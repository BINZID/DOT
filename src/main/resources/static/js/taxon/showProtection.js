/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showProtection(taxonId,offset,type) {
    if(type=='first')
        $("#showProtectionFrame").empty();
    else if(type=='more')
        $("#moreProtectionButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/protection/rest/protectionList/"+taxonId,
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
                        $("#showCountDescriptionInfo").text("共"+data.total+"条保护数据");
                    else
                        $("#showCountDescriptionInfo").text("无保护数据");
                    //配置Protection列表
                    $("#protectionList").tmpl(data).appendTo("#showProtectionFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreProtection").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showProtectionFrame");
                    });
                } catch (err) {
                    $("#reLoadProtection").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showProtectionFrame");
                }
             }
            else{
                $("#reLoadProtection").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showProtectionFrame");
            }
        });
}
//显示保护数据模态框
function showTraitdataTable(traitjson) {
    $('#traitTable').bootstrapTable('load', "[]");
    $("#traitModalTable").modal({
        keyboard:true,//当按下esc键时，modal框消失
    });
    $.get("/console/traitdata/rest/buildTrait",
        {
            "traitjson":JSON.stringify(traitjson)
        },
        function(result){
            $('#traitTable').bootstrapTable('load',result);
        });
};