/**
 * Created by Tianshan on 18-5-16.
 */
//初始化所有select组件
$(document).ready(function () {
    buildSelect2("rank", "console/rank/rest/select", "请选择分类等级(分类阶元或分类单元)");
    buildSelect2("sourcesid", "console/datasource/rest/select", "请选择数据来源");
    buildSelect2("taxaset", "console/taxaset/rest/selectAndNew", "请选择存放的分类单元集");
    buildSelectForCompose("email","console/user/rest/userInfo", "选择收信人");
    // taxon基础信息
    $("#nomencode").select2({
        placeholder: "请选择命名法规",
    });
    // 数据集添加/编辑
    $("#status").select2({
        placeholder: "请选择数据集状态",
    });
    $("#searchPara").select2({
        placeholder: "请选择检索字段",
    });

});
//拼接option
function formatRepo1(repo) {
    if (repo.loading){
    	return repo.text;
    }else {
    	var markup = repo.text;
    	return markup;
	}
}
//拼接option
function formatRepoSelection1(repo) {
	var rsl = repo.text.length < 50 ? repo.text : repo.text.substring(0, 40) + "...";
}
//拼接option
function formatRepo(repo) {
    if (repo.loading){
    	return repo.text;
    }else {
    	var markup = repo.text;
    	return markup;
	}
}
//拼接option
function formatRepoSelection(repo) {
	var rsl = repo.text.length < 50 ? repo.text : repo.text.substring(0, 40) + "...";
    return rsl;
}
//回显数据
function echoSelect2(select_id, data){
    $.each(data, function(index, data){
        $("#" + select_id).append(new Option(data.text, data.id, false, true));
    });
    $("#" + select_id).trigger("change");
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
                    page: params.page,
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

//构造select2的方法 -- 多选
function buildSelect(select_id, url, content) {
    $("#" + select_id).select2({
        language: "zh-CN",
        placeholder: content,
        multiple: true,
        closeOnSelect: false,
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

//构造select2的方法 -- 单选可移除选中项
function buildSelectForTaxon(select_id, url, content) {
    $("#" + select_id).select2({
        language: "zh-CN",
        placeholder: content,
        allowClear : true,
        tags: true,
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

//构造select2的方法
function buildSelectForCompose(select_id, url, content) {
	$("#" + select_id).select2({
		tags: true,
        language: "zh-CN",
        placeholder: content,
        ajax: {
            url: url,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    find: params.term,
                    page: params.page,
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

//构造select2的方法
function buildSelectForRef(select_id, url, content, searchFiled, searchContent) {
	$("#" + select_id).select2({
		tags: true,
        language: "zh-CN",
        placeholder: content,
        ajax: {
            url: url,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    find: params.term,
                    page: params.page,
                    searchFiled: getSearchFiled(searchFiled),
                    searchContent: getSearchContent(searchContent),
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
	/*检索字段*/
	function getSearchFiled(searchFiled){
		var searchFiled = $("#" + searchFiled).select2("val");
		if (typeof searchFiled == "undefined" || searchFiled == null || searchFiled == "") {
			return searchFiled = '';
		}else{
			return searchFiled;
		}
	}
	/*检索字段对应的内容*/
	function getSearchContent(searchContent){
		var searchContent = $("#" + searchContent).val();
		if (typeof searchContent == "undefined" || searchContent == null || searchContent == "") {
			return searchContent = '';
		}else{
			return searchContent;
		}
	}
}