/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showTraitdata(taxonId,offset,type) {
    if(type=='first')
        $("#showTraitdataFrame").empty();
    else if(type=='more')
        $("#moreTraitdataButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/traitdata/rest/traitdataList/"+taxonId,
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
                        $("#showCountDescriptionInfo").text("共"+data.total+"条特征数据");
                    else
                        $("#showCountDescriptionInfo").text("无特征数据");
                    //配置Traitdata列表
                    $("#traitdataList").tmpl(data).appendTo("#showTraitdataFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreTraitdata").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showTraitdataFrame");
                    });
                } catch (err) {
                    $("#reLoadTraitdata").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showTraitdataFrame");
                }
             }
            else{
                $("#reLoadTraitdata").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showTraitdataFrame");
            }
        });
}