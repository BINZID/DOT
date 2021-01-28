/**
 * Created by BINZI on 18-08-15.
 */
//显示引证
function showMultimedia(taxonId,offset,type) {
    if(type=='first')
        $("#showMultimediaFrame").empty();
    else if(type=='more')
        $("#moreMultimediaButton").remove();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/multimedia/rest/multimediaList/"+taxonId,
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
                        $("#showCountMultimedia").text("共"+data.total+"条");
                    else
                        $("#showCountMultimedia").text("无");
                    //配置Multimedia列表
                    $("#multimediaList").tmpl(data).appendTo("#showMultimediaFrame").ready(function () {
                        //配置下一页
                        if((offset+10)<=data.total)
                            $("#moreMultimedia").tmpl({ offset: offset+10, taxonid:taxonId, type:'more'}).appendTo("#showMultimediaFrame");
                    });
                } catch (err) {
                    $("#reLoadMultimedia").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showMultimediaFrame");
                }
             }
            else{
                $("#reLoadMultimedia").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showMultimediaFrame");
            }
        });
}
//显示大图
function showLargeImage(url) {
    var imageData={
        "title": "", //标题
        "id": 1, //id
        "start": 0, //初始显示的图片序号，默认0
        "data": [   //相册包含的图片，数组格式
            {
                "alt": "",
                "pid": 1, //图片id
                "src": url, //原图地址
                "thumb": "" //缩略图地址
            }
        ]
    };
    layer.photos({
        photos: imageData
        ,anim: 5
    });
}