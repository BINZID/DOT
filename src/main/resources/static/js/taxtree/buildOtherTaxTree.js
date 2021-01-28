/**
 * Created by Tianshan on 18-8-7.
 */

//加载备选tree
function buildOtherTaxTree(){
    //判断条件合法
    if(
        ($("#taxtreeId").val()==null||$("#taxtreeId").val()==undefined||$("#taxtreeId").val()=="")
    )
    {
        $("#rightTree").html("请选择分类树");
    }
    else{
        var settingOtherTaxTree={
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
                url:"/console/taxtree/rest/showChildren",
                autoParam:["id", "name", "title"],
                otherParam:{"taxtreeId":$("#taxtreeId").val()},
                dataFilter: filter
            },
            callback:{
                onAsyncError: onAsyncError,
                onAsyncSuccess: onAsyncSuccess,
                beforeClick:beforeClick, //捕获单击节点之前的事件回调函数
                beforeDrag: beforeDrag,
                beforeDrop: beforeDropOtherTaxTree,
                onDrop: onDropOtherTaxTree
            }
        };
        $.fn.zTree.init($("#rightTree"), settingOtherTaxTree);
    }
}

//挪动前判断
function beforeDropOtherTaxTree(treeId, treeNodes, targetNode, moveType, isCopy) {
    if(treeId=="leftTree"){
        var canDrop=false;
        for (var i=0;i<treeNodes.length;i++)
        {
            canDrop=hasThisTaxon(taxTreeId, treeNodes[i].id);
            if(!canDrop)
                break;
        }
        if(canDrop){
            layer.msg('操作失败：该分类树已包含此分类单元或其下的子单元', {time : 500}, function() {
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
function onDropOtherTaxTree(event, treeId, treeNodes, targetNode, moveType, isCopy) {
    if(treeId=="leftTree"){
        console.log(treeNodes);//被移动的目标
        // console.log(targetNode);//被放入的节点
        //loading
        addOtherTaxTree(taxTreeId, treeNodes[0].id, targetNode==null ? null:targetNode.id,moveType,$("#taxtreeId").val());
    }
}