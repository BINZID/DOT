/**
 * Created by Tianshan on 18-8-7.
 */
// 刷新
/** 
 *@param treeId:对应zTree的treeId,便于用户操控 
 *@param parentNode:进行异步加载的父节点 JSON 数据对象 
 *@param childNodes:异步加载获取到的数据转换后的 Array(JSON) / JSON / String 数据对象 
 */  
function filter(treeId, parentNode, childNodes) {
    if (!childNodes) return null;
    for (var i=0, l=childNodes.length; i<l; i++) {
        childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
    }
    return childNodes;
}
// 点击查看详情
function beforeClick(treeId,treeNode){
    if(!treeNode.isParent){
        return false;
    }else{
        return true;
    }
}

function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    $("#"+treeId).html("加载异常");
    //$("#rightTree").html("加载异常");
    //showLog("[ "+getTime()+" onAsyncError ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
}
// 选择分类等级
function onAsyncSuccess(event, treeId, treeNode, msg) {
    if(msg == "[ ]")
        $("#"+treeId).html("无");
    //showLog("[ "+getTime()+" onAsyncSuccess ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
}

// 拖拽之前
function beforeDrag(treeId, treeNodes) {
    for (var i=0,l=treeNodes.length; i<l; i++) {
        if (treeNodes[i].drag === false) {
            return false;
        }
    }
    return true;
}
function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
    return targetNode ? targetNode.drop !== false : true;
}
function beforeDragOpen(treeId, treeNode) {
    autoExpandNode = treeNode;
    return true;
}
function onDrag(event, treeId, treeNodes) {
}
function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
}
//点击展开树
function onExpand(event, treeId, treeNode) {
}
function beforeRemove(treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("leftTree");
    zTree.selectNode(treeNode);
    return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
}
function beforeRename(treeId, treeNode, newName) {
    if (newName.length == 0) {
        setTimeout(function() {
            var zTree = $.fn.zTree.getZTreeObj("leftTree");
            zTree.cancelEditName();
            alert("节点名称不能为空.");
        }, 0);
        return false;
    }
    return true;
}

//判断是否有目标节点（Taxon）
// 拖拽到左侧判断左侧是否存在
function hasThisTaxon(taxTreeId, taxonId) {
    var hasThisNode=true;
    $.ajax({
        type : "get",
        url : "/console/taxtree/rest/hasThisTaxon",
        data : {
            "taxTreeId":taxTreeId,
            "taxonId":taxonId
        },
        async : false,
        success : function(status){
            if (status) {
                hasThisNode = true;
            }else {
                hasThisNode = false;
            }
        }
    });
    console.log("hasThisNode="+hasThisNode);
    return hasThisNode;
}

//添加节点（Taxon）
function addTaxon(taxTreeId, taxonId, targetTaxonId, moveType) {
   /*console.log("taxTreeId="+taxTreeId);
     console.log("taxonId="+taxonId);
     console.log("targetTaxonId="+targetTaxonId);
     console.log("moveType="+moveType);*/
    return $.get("/console/taxtree/rest/addOneTaxon",
        {
            "taxTreeId":taxTreeId,
            "taxonId":taxonId,
            "targetTaxonId":targetTaxonId,
            "moveType":moveType,
        },
        function(status) {
            if (status) {
                layer.msg('成功', {time : 500}, function() {
                    return true;
                })
            }else {
                layer.msg('操作失败', function(){
                    return false;
                })
            }
        })
}

//添加部分tree
function addOtherTaxTree(taxTreeId, taxonId, targetTaxonId,moveType,origTaxtreeId) {
    return $.get("/console/taxtree/rest/addOtherTaxTree",
        {
            "taxTreeId":taxTreeId,
            "taxonId":taxonId,
            "targetTaxonId":targetTaxonId,
            "moveType":moveType,
            "origTaxtreeId":origTaxtreeId
        },
        function(status) {
            if (status) {
                layer.msg('成功', {time : 500}, function() {
                    return true;
                })
            }else {
                layer.msg('操作失败', function(){
                    return false;
                })
            }
        })
}

//修改节点（Taxon）
function moveTaxon(taxTreeId, taxonId, targetTaxonId,moveType) {
    console.log("修改节点");
    return $.get("/console/taxtree/rest/moveOneTaxon",
        {
            "taxTreeId":taxTreeId,
            "taxonId":taxonId,
            "targetTaxonId":targetTaxonId,
            "moveType":moveType,
        },
        function(status) {
            if (status) {
                layer.msg('成功', {time : 500}, function() {
                    return true;
                })
            }else {
                layer.msg('操作失败', function(){
                    return false;
                })
            }
        })

}
//删除单一个节点（Taxon）
function removeOneNode(taxTreeId, taxonId) {
    return $.post("console/taxtree/rest/removeOneNode",
        {
            "taxtreeId":taxTreeId,
            "taxonId":taxonId,
            "_csrf":$("input[name='_csrf']").val()
        },
        function(status) {
            if (status) {
                layer.msg('删除成功', {time : 500}, function() {
                    return true;
                })
            }else {
                layer.msg('操作失败', function(){
                    return false;
                })
            }
        })

}
// removeThisNode删除节点及子节点（Taxon）
function removeThisNode(taxTreeId, taxonId) {
    return $.post("console/taxtree/rest/removeNodeAndAllChindren",
        {
            "taxtreeId":taxTreeId,
            "taxonId":taxonId,
            "_csrf":$("input[name='_csrf']").val()
        },
        function(status) {
            if (status) {
                layer.msg('删除成功', {time : 500}, function() {
                    return true;
                })
            }else {
                layer.msg('操作失败', function(){
                    return false;
                })
            }
        })

}
function zTreeOnAsyncError(event, treeId, treeNode){
    alert("加载失败!");
}
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){
}