/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showOccurrence(taxonId,offset,type) {
    if(type=='first')
        $("#showOccurrenceFrame").empty();
    else if(type=='more')
        $("#moreOccurrenceButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/occurrence/rest/occurrenceList/"+taxonId,
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
                        $("#showCountOccurrence").text("共"+data.total+"条");
                    else
                        $("#showCountOccurrence").text("无");
                    //配置Occurrence列表
                    $("#occurrenceList").tmpl(data).appendTo("#showOccurrenceFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreOccurrence").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showOccurrenceFrame");
                    });
                } catch (err) {
                    $("#reLoadOccurrence").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showOccurrenceFrame");
                }
             }
            else{
                $("#reLoadOccurrence").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showOccurrenceFrame");
            }
        });
}