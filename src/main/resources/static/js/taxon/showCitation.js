/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showCitation(taxonId,offset,type) {
    if(type=='first')
        $("#showCitationFrame").empty();
    else if(type=='more')
        $("#moreCitationButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/citation/rest/citationList/"+taxonId,
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
                        $("#showCountCitation").text("共"+data.total+"条");
                    else
                        $("#showCountCitation").text("无");
                    //配置Citation列表
                    $("#citationList").tmpl(data).appendTo("#showCitationFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreCitation").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showCitationFrame");
                    });
                } catch (err) {
                    $("#reLoadCitation").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showCitationFrame");
                }
             }
            else{
                $("#reLoadCitation").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showCitationFrame");
            }
        });
}