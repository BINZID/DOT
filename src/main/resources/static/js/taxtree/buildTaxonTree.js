/**
 * Created by Tianshan on 18-8-7.
 */

//加载备选tree
function buildTaxonTree(){
    //判断条件合法
    if(
        ($("#taxasetId").val()==null||$("#taxasetId").val()==undefined||$("#taxasetId").val()=="")||
        ($("#rankId").val()==null||$("#rankId").val()==undefined||$("#rankId").val()=="")
    ){
        $("#rightTree").html("请选择分类单元范围");
    }else{
        var taxasetId=$("#taxasetId").val();
        var rankId=$("#rankId").val();
        var taxonName=$("#taxonName").val();
        var settingTaxonTree={
            edit: {
                enable: true,
                showRemoveBtn: false,
                showRenameBtn: false,
                drag: {
                    isCopy: true,
                    isMove: false
                }
            },
            data: {
                simpleData: {
                    enable: true
                },
                key: {
                    title:"title"
                }
            },
            check: {
                enable: false,
                chkboxType:{ "Y" : "s", "N" : "ps" }
            },
            view: {
                fontCss : {'font-style':'italic'}
            },
            async: {
                enable: true,
                type:"get",
                url:"/console/taxon/rest/tree",
                autoParam:["id", "name", "title"],
                otherParam:{"taxasetId":taxasetId,"rankId":rankId,"taxonName":taxonName},
                dataFilter: filter
            },
            callback:{
                onAsyncError: onAsyncError,
                onAsyncSuccess: onAsyncSuccess,
                beforeClick:beforeClick, //捕获单击节点之前的事件回调函数
                beforeDrag: beforeDrag,
                beforeDrop: beforeDropRight,
                onDrop: onDropRight
            }
        };
        $.fn.zTree.init($("#rightTree"), settingTaxonTree);
    }
}

//挪动前判断
function beforeDropRight(treeId, treeNodes, targetNode, moveType, isCopy) {
    if(treeId=="leftTree"){
        var canDrop=false;
        canDrop=hasThisTaxon(taxTreeId, treeNodes[0].id);
        if(canDrop){
            layer.msg('操作失败：该分类树已有此分类单元', {time : 1500}, function() {
            })
            return false;
        }
        else{
            return targetNode ? targetNode.drop !== false : true;
        }
    }
    else{
        return false;
    }
}
//挪动的处理(copy)
function onDropRight(event, treeId, treeNodes, targetNode, moveType, isCopy) {
    if(treeId=="leftTree"){
        // console.log(treeNodes);//被移动的目标
        // console.log(targetNode);//被放入的节点
        //loading

        //加入tree
        addTaxon(taxTreeId, treeNodes[0].id, targetNode==null ? null:targetNode.id,moveType);
    }
}