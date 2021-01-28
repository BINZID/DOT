/**
 * Created by Tianshan on 18-8-7.
 */
//初始化所有taxon备选Tree
$(document).ready(function () {
    $('#treeType').select2();
    buildSelect2("noteName", "console/taxtree/rest/findTreeNode/" + $("#taxaTreeId").val(), "请选择分类单元名称检索分类树");
    buildSelect2("taxtreeId", "console/taxtree/rest/select", "请选择分类树");
    buildSelect2("rankId", "console/rank/rest/select", "请选择分类阶元");
    buildSelect2("taxasetId", "console/taxaset/rest/select", "请选择分类单元集");
});
//拼接option
function formatRepo(repo) {
    if (repo.loading) return repo.text;
    var markup = repo.text;
    return markup;
}
//拼接option
function formatRepoSelection(repo) {
    return repo.text || repo.text;
}
//构造select2的方法
function buildSelect2(select_id, url, content) {
    $("#" + select_id).select2({
        language: "zh-CN",
        placeholder: content,
        ajax: {
            url: url,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    find: params.term,
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * 30) < data.total_count
                    }
                };
            },
            cache: true
        },
        escapeMarkup: function (markup) {
            return markup;
        },
        minimumInputLength: 0,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection
    });
}
//TreeType变化时处理函数
$("#treeType").on("change", function () {
    //从分类单元选择
    if($("#treeType").val()=="taxon"){
        $(".treeType-taxtree").hide();
        $(".treeType-taxon").show();
    }
    else {//从分类树选择
        $(".treeType-taxon").hide();
        $(".treeType-taxtree").show();
    }
});


//Taxon时，选项变化后处理的函数
$("#treeType").on("change", function () {
    if($("#treeType").val()=="taxon"){
        buildTaxonTree();
    }
    else if($("#treeType").val()=="taxtree"){
        buildOtherTaxTree();
    }
});
//Taxon时，选项变化后处理的函数
$("#taxonName").on("change input propertychange oninput onpropertychange", function () {
    buildTaxonTree();
});

$("#taxasetId").on("change", function () {
    buildTaxonTree();
});
//Taxon时，选项变化后处理的函数
$("#rankId").on("change", function () {
    buildTaxonTree();
});
//Taxtree时，选项变化后处理的函数
$("#taxtreeId").on("change", function () {
    buildOtherTaxTree();
});

//全选right
function selectAllRightTree() {
    var zTree = $.fn.zTree.getZTreeObj("rightTree");
    zTree.checkAllNodes(true);
}
//全选left
function selectAllLeftTree() {
    var zTree = $.fn.zTree.getZTreeObj("leftTree");
    zTree.checkAllNodes(true);
}
//全不选right
function noSelectAllRightTree() {
    var zTree = $.fn.zTree.getZTreeObj("rightTree");
    zTree.checkAllNodes(false);
}
//全不选left
function noSelectAllLeftTree() {
    var zTree = $.fn.zTree.getZTreeObj("leftTree");
    zTree.checkAllNodes(false);
}
//重新加载treeright
function refreshRightTree() {
    buildTaxonTree();
}
//重新加载treeleft
function refreshLeftTree() {
    //buildTaxonTree();
}
//加入新树
function toThisTaxtree() {
    alert("ok");
}
//删除选中项
function removeSelectTaxon() {
    //buildTaxonTree();
}
