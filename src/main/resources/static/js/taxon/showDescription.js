/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showDescription(taxonId,offset,type) {
    if(type=='first')
        $("#showDescriptionFrame").empty();
    else if(type=='more')
        $("#moreDescriptionButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/description/rest/descriptionList/"+taxonId,
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
                        $("#showCountDescription").text("共"+data.total+"条文本描述");
                    else
                        $("#showCountDescription").text("无文本描述");
                    //配置Description列表
                    $("#descriptionList").tmpl(data).appendTo("#showDescriptionFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreDescription").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showDescriptionFrame");
                    });
                } catch (err) {
                    $("#reLoadDescription").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showDescriptionFrame");
                }
             }
            else{
                $("#reLoadDescription").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showDescriptionFrame");
            }
        });
}