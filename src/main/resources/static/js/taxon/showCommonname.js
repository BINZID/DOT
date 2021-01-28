/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showCommonname(taxonId,offset,type) {
    if(type=='first')
        $("#showCommonnameFrame").empty();
    else if(type=='more')
        $("#moreCommonnameButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/commonname/rest/commonnameList/"+taxonId,
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
                        $("#showCountCommonname").text("共"+data.total+"条");
                    else
                        $("#showCountCommonname").text("无");
                    //配置Commonname列表
                    $("#commonnameList").tmpl(data).appendTo("#showCommonnameFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreCommonname").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showCommonnameFrame");
                    });
                } catch (err) {
                    $("#reLoadCommonname").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showCommonnameFrame");
                }
             }
            else{
                $("#reLoadCommonname").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showCommonnameFrame");
            }
        });
}