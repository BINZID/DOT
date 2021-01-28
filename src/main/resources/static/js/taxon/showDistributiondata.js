/**
 * Created by Tianshan on 18-5-22.
 */
//显示引证
function showDistributiondata(taxonId,offset,type) {
    if(type=='first')
        $("#showDistributiondataFrame").empty();
    else if(type=='more')
        $("#moreDistributiondataButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/distributiondata/rest/distributiondataList/"+taxonId,
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
                        $("#showCountDistributiondata").text("共"+data.total+"条分布数据");
                    else
                        $("#showCountDistributiondata").text("无分布数据");
                    //配置Distributiondata列表
                    $("#distributiondataList").tmpl(data).appendTo("#showDistributiondataFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreDistributiondata").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showDistributiondataFrame");
                    });
                } catch (err) {
                    $("#reLoadDistributiondata").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showDistributiondataFrame");
                }
             }
            else{
                $("#reLoadDistributiondata").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showDistributiondataFrame");
            }
        });
}