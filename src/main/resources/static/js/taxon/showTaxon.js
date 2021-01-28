/**
 * Created by Tianshan on 18-5-22.
 */
//显示Taxon
function showTaxon(taxonId) {
    $("#showTaxonFrame").empty();
    $("#loading").tmpl().appendTo("#imagePageFrame");
    $.get("console/taxon/rest/taxonBasics/"+taxonId,
        {
            async:true
        },
        function(data,status){
            if(status){
                try {
                    //配置Taxon列表
                    $("#taxonList").tmpl(data).appendTo("#showTaxonFrame").ready(function () {
                        $("#showCountTaxon").text(data.scientificname);
                    });
                } catch (err) {
                    $("#reLoadTaxon").tmpl({ offset: 0, taxonid:taxonId, type:'first'}).appendTo("#showTaxonFrame");
                }
            }
            else{
                $("#reLoadTaxon").tmpl({ offset: 0, taxonId:taxonId, type:'first'}).appendTo("#showTaxonFrame");
            }
        });
}