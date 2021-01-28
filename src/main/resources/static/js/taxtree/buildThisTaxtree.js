/**
 * Created by Tianshan on 18-8-7.
 */
var settingThisTaxtree = {
    view: {
        addHoverDom: addHoverDom, 			//当鼠标移动到节点上时，显示用户自定义控件
        removeHoverDom: removeHoverDom, 	//离开节点时的操作
        fontCss : {'font-style':'italic'} 	//个性化样式
    },
    edit: {
        enable: true,
        showRemoveBtn: true,
        showRenameBtn: true,
        renameTitle:'edit',
        removeTitle:'remove'
    },
    data: {
        simpleData: {
            enable: true,
        },
        key: {
            title:"title"
        }
    },
    async: {
        enable: true,
        type:"get",
        url:"/console/taxtree/rest/showChildren",
        //contentType: "application/json",
        autoParam:["id", "name", "title"],
        otherParam:{"taxtreeId":taxTreeId},
        dataFilter: filter
    },
    check: {
        enable: false,
        chkboxType:{ "Y" : "s", "N" : "ps" }
    },
    callback:{
    	beforeRemove: beforeRemoveLeft,		//点击删除时触发，用来提示用户是否确定删除（可以根据返回值 true|false 确定是否可以删除）
    	beforeEditName: beforeRenameLeft,	//点击编辑时触发，用来判断该节点是否能编辑
    	beforeRename: beforeRename,			//编辑结束时触发，用来验证输入的数据是否符合要求(也是根据返回值 true|false 确定是否可以编辑完成)
        onAsyncError: zTreeOnAsyncError, 	//加载错误的fun
        onAsyncSuccess: zTreeOnAsyncSuccess,//异步加载成功的fun
        beforeClick:beforeClick, 			//捕获单击节点之前的事件回调函数
        beforeDrag: beforeDrag,				//拖拽之前
        beforeDrop: beforeDropLeft,			//拖拽结束
        beforeDragOpen: beforeDragOpen,
        onDrag: onDrag,						//拖拽的时候
        onDrop: onDropLeft,					//拖拽结束后
        onExpand: onExpand
    }
};

//挪动前判断
function beforeDropLeft(treeId, treeNodes, targetNode, moveType, isCopy) {
    if(treeId=="leftTree"){
        console.log("可移动");
        if(targetNode ? targetNode.drop !== false : true){
            return moveTaxon(taxTreeId, treeNodes[0].id, targetNode==null ? null:targetNode.id,moveType);
        }
        else
            false;
    }
    else{
        console.log("不可移动");
        return false;
    }
}

//挪动的处理（move）
function onDropLeft(event, treeId, treeNodes, targetNode, moveType, isCopy) {
    // if(treeId=="leftTree"){
    //     console.log(treeNodes);//被移动的目标
    //     console.log(targetNode);//被放入的节点
    //     //修改tree
    //     moveTaxon(taxTreeId, treeNodes[0].id, targetNode==null ? null:targetNode.id,moveType);
    // }
}
//删除处理
function beforeRemoveLeft(treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("leftTree");
    zTree.selectNode(treeNode);
    if(confirm("确认删除 节点: [ " + treeNode.name + " ]吗？")){
        if(treeId=="leftTree"){
            return removeThisNode(taxTreeId,treeNode.id);
        }
        else
            return false;
    }
    else
        return false;
}
/* 点击修改按钮调转到修改页面，修改完成后跳转到分类树页面，遍历展开到修改的节点 */
function beforeRenameLeft(treeId, treeNode) {
	parentNoteTree(treeNode.id);
	window.open("/console/taxon/edit/" + treeNode.id, "_blank");
	return false;
}
/* 鼠标移到节点上，出现添加按钮*/
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span"); //获取节点信息
    if (treeNode.editNameFlag || $("#"+treeNode.tId+"_add").length>0) return;
    var addStr = "<span class='button add' id='" + treeNode.tId + "_add" + "' title='add'></span>"; //定义添加按钮
    sObj.after(addStr);	 						//加载添加按钮
    var btn = $("#"+treeNode.tId+"_add");
    if (btn) btn.bind("click", function(){		//绑定添加事件，并定义添加操作
    	addNewTaxon(treeNode.id, "inner");
    	return false;
    });
};
/* 鼠标移出节点，添加按钮消失*/
function removeHoverDom(treeId, treeNode) {
	$("#"+treeNode.tId+"_add").unbind().remove();
};