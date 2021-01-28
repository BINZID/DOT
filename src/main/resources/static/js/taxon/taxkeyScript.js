/**
 * Created by Tianshan on 18-5-22.
 */

// 删除一个新检索表
function removeTaxkey(taxkeyNum) {
	var r = confirm("是否删除?");
	if (r == true) {
		$.post("/console/taxkey/rest/delete", {
			"_csrf" : $('input[name="_csrf"]').val(),
			"taxkeyId" : $("#taxkeyId_" + taxkeyNum).val()
		}, function(status) {
			if (status) {
				layer.msg('删除成功', {
					time : 500
				}, function() {
					$("#taxkeyForm_" + taxkeyNum).remove();
				})
			} else {
				layer.msg('操作失败', function() {
				})
			}
		})
	} else {
		layer.msg('操作取消', {
			time : 500
		})
	}
}

// 提交一个检索表
function submitTaxkey(taxkeyNum) {
	/*taxkeyFormValidator(taxkeyNum);*/
	if (true) {
		// 处理ajax提交
		var obj = $('#taxkeyForm_' + taxkeyNum).serialize();
		var url = "/console/taxkey/rest/add/" + $('#taxonId').val();
		var postSuccess = false;
		/*return $.ajax({
			type : "POST",
			url : url,
			data : obj, // 要提交的表单
			dataType : "json",
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			success : function(msg) {
				if (msg.result == true) {
					layer.msg('提交成功，请继续填写其他内容', {time : 1500},function() {
								if ($('#taxkeyCollapse_' + taxkeyNum).hasClass('in')) {
									$('#taxkeyCollapseTitle_' + taxkeyNum).trigger("click");
								}
								$('#taxkeyForm_' + taxkeyNum).removeClass("panel-default");
								$('#taxkeyForm_' + taxkeyNum).removeClass("panel-danger");
								$('#taxkeyForm_' + taxkeyNum).addClass("panel-success");
								$('#taxkeyStatus_' + taxkeyNum).removeClass("hidden");
								postSuccess = true;
							});
				} else {
					layer.msg("添加失败！", {
						time : 1000
					});
					postSuccess = false;
				}
			},
			error : function() {
				postSuccess = false;
			}
		});*/
		if (!$('#taxkeyCollapse_' + taxkeyNum).hasClass('in')) {
			$('#taxkeyCollapseTitle_' + taxkeyNum).trigger("click");
		}
		$('#taxkeyForm_' + taxkeyNum).removeClass("panel-danger");
		$('#taxkeyForm_' + taxkeyNum).addClass("panel-success");
		$('#taxkeyStatus_' + taxkeyNum).removeClass("hidden");
		postSuccess = true;
		layer.msg("提交成功，请继续填写其他内容", function() {
		});
		return postSuccess;
	} else {
		if (!$('#taxkeyCollapse_' + taxkeyNum).hasClass('in')) {
			$('#taxkeyCollapseTitle_' + taxkeyNum).trigger("click");
		}
		$('#taxkeyForm_' + taxkeyNum).removeClass("panel-success");
		$('#taxkeyForm_' + taxkeyNum).addClass("panel-danger");
		$('#taxkeyStatus_' + taxkeyNum).addClass("hidden");
		/*$('#tab-taxkey').addClass("alert alert-danger");*/
		layer.msg("请完成此文本描述的填写", function() {
		});
		return false;
	}
}

// 提交所有检索表
function submitAllTaxkey() {
	var submitResult = true;
	$('#tab-taxkey').removeClass("alert-danger");
	$('#tab-taxkey').removeClass("alert");
	$("form[id^='taxkeyForm_']").each(
			function() {
				if (submitTaxkey($(this).attr("id").substr(
						$(this).attr("id").lastIndexOf("_") + 1))) {
				} else {
					submitResult = false;
				}
			});
	return submitResult;
}

// 加入检索表
function submitKeyitem(taxkeyNum) {
	/**
	 * 0.校验检索表 1.检测Taxkey是否存储 -- 未存先存 2.存储Keyitem(Ajax的回调函数) -- 构造table
	 * 3.清空Keyitem表格
	 */
	taxkeyFormValidator(taxkeyNum);
	if ($('#taxkeyForm_' + taxkeyNum).data('bootstrapValidator').isValid()) {
		/* 1.存储Taxkey */
		$.post("/console/taxkey/rest/addTaxkey/" + $('#taxonId').val(),
			{
				"_csrf" : $('input[name="_csrf"]').val(),
				"taxkeyId" : $("#taxkeyId_" + taxkeyNum).val(),
				"abstraction" : $("#abstraction_" + taxkeyNum)
						.val(),
				"keytitle" : $("#keytitle_" + taxkeyNum).val(),
				"keytype" : $("#keytype_" + taxkeyNum).val()
			},
			function(status) {
				if (status) {
					/* 2.为Keyitem设置唯一标识UUID */
					$.get("/console/taxon/rest/uuid", function(id) {
						$("#keyitemId_" + taxkeyNum).val(id);
					});
					layer.msg('检索表加入成功',{time : 500},function() {
						buildKeytable(taxkeyNum);
						/* 3.提交Keyitem */
						var obj = $('#taxkeyForm_'+ taxkeyNum).serialize();
						return $.ajax({
							type : "POST",
							url : "/console/taxkey/rest/add/" + $('#taxonId').val(),
							data : obj, // 要提交的表单
							dataType : "json",
							contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
							success : function(msg) {
								if (msg.result == true) {
									layer.msg('提交成功，请继续填写其他内容',{time : 1500},function() {
										showKeyTable(taxkeyNum);
										$("#keytable_" + taxkeyNum).bootstrapTable('refresh');
										/* 5.清空Table */
										$('#orderid_' + taxkeyNum).val("");
										$('#innerorder_' + taxkeyNum).val("");
										$('#branchid_'+ taxkeyNum).val("");
										$('#item_'+ taxkeyNum).val("");
										$('#taxonid_'+ taxkeyNum).empty();
								});
								} else {
									layer.msg("添加失败！",{time : 1000});
								}
							}
						});
					})
				} else {
					layer.msg('操作失败', function() {
					})
				}
			})
	}
}

//显示检索表详情
function showKeyTable(taxkeyNum) {
	 $('#keytable_' + taxkeyNum).bootstrapTable('load', "[]");
	    $.get("/console/keyitem/rest/list/" + $("#taxkeyId_" + taxkeyNum).val(), function(result){
	    	$('#keytable_' + taxkeyNum).bootstrapTable('load',result);
	    });
};

// 添加一个新检索表
function addTaxkey() {

	var countTaxkey = parseInt($('#countTaxkey').val());

	var thisTaxkeyNum = {
		num : countTaxkey + 1
	};
	$('#taxkeyForm').tmpl(thisTaxkeyNum).appendTo('#newTaxkey');
	
	// 当前Taxon所属Taxaset
	$.get("/console/taxon/rest/taxasetId/" + $("#taxonId").val(), function(data) {
		if (null != data.taxasetId) {
			// 指向分类单元下拉选
			buildSelectForTaxon("taxonid_" + (countTaxkey + 1),
					"console/taxon/rest/select/" + data.taxasetId,
			"请选择指向分类单元");
		}
	});

	$('#countTaxkey').val(countTaxkey + 1);

	addTaxkeyValidator(countTaxkey + 1);

	$("#keytypes_" + (countTaxkey + 1)).select2({
		placeholder : "请选择检索表样式",
	});

	buildKeytable(countTaxkey + 1);

	// title变化后处理的函数
	$("#keytitle_" + (countTaxkey + 1)).on("change",
			function() {
				$("#taxkeyCollapseTitle_" + (countTaxkey + 1)).text(
						$("#keytitle_" + (countTaxkey + 1)).val());
			});

	// 唯一标识UUID
	$.get("/console/taxon/rest/uuid", function(id) {
		if (null == $("#taxkeyId_" + (countTaxkey + 1)).val() || "" == $("#taxkeyId_" + (countTaxkey + 1)).val()) {
			$("#taxkeyId_" + (countTaxkey + 1)).val(id);
		}
	});

	// 清空存储在Session中Select2联动选中值
	$.get("/console/taxkey/rest/removeSession");

	// 切换检索表类型
	changeKeyitem((countTaxkey + 1));
}

// 切换检索表类型
function changeKeyitem(countTaxkey){
	$("#keytypes_" + countTaxkey).on("change", function() {
		var val = $("#keytypes_" + countTaxkey).val();
		if ($("#taxkeyType" + val + "_" + countTaxkey).hasClass("active")) {

		} else {
			if (val == 1) {
				$("#taxkeyType1_" + countTaxkey).addClass("active");
				$("#taxkeyType2_" + countTaxkey).removeClass("active");
				$("#keytype_" + countTaxkey).val(1);
				$("#inputinnerorder_" + countTaxkey).show();
	
				var taxkeyData = [ {
					"orderid" : "",
					"item" : "",
					"ellipsis" : "",
					"branchid" : "",
					"images" : "",
					"edit" : "",
					"remove" : ""
				}];
			} else if (val == 2) {
				$("#taxkeyType2_" + countTaxkey).addClass("active");
				$("#taxkeyType1_" + countTaxkey).removeClass("active");
				$("#keytype_" + countTaxkey).val(2);
				$("#branchid_" + countTaxkey).attr("readOnly", false);
				$("#taxonid_" + countTaxkey).prop("disabled", false);
				$("#inputinnerorder_" + countTaxkey).hide();
	
				var taxkeyData = [ {
					"orderid" : "",
					"item" : "",
					"ellipsis" : "",
					"branchid" : "",
					"images" : "",
					"edit" : "",
					"remove" : ""
				} ];
			} else if (val == "3") {
		 }
	   }
	});
}


// 分支序号与分类单元二选一
function taxonIsNull(num){
	var keytype = $("#keytype_" + num).val();
	if (keytype == 1) {
		var taxon = $("#taxonid_" + num).select2("val");
		if (null != taxon) {
			$("#branchid_" + num).attr("readOnly", true);
			layer.msg("双向式中分支序号与分类单元不能同时填写", {time: 1500});
		}else{
			$("#branchid_" + num).attr("readOnly", false);
			$("#taxonid_" + num).prop("disabled", false);
		}
	}
}

// 构造检索表预览
function buildKeytable(taxkeyNum) {
    var $table = $('#keytable_' + taxkeyNum);
    var taxkeyDefaultData = [
        {
            /*"orderid": "",
            "item": "产生种子",
            "ellipsis": "……………………………………………………………………………………",
            "branchid": "种子植物门",
            "images": "<button type='button' class='btn btn-default btn-xs' onclick='showImages('1');'>特征图片</button>",
            "edit": "<button type='button' class='btn btn-default btn-xs' onclick='editKeyitem('1');'>编辑</button>",
            "remove": "<button type='button' class='btn btn-default btn-xs' onclick='removeKeyitem('1');'>删除</button>"*/
        }];
    $(function () {
        $table.bootstrapTable({
            data: taxkeyDefaultData
        });
    });
}

// 重载检索表预览
function loadKeytable(taxkeyNum, data) {
	var $table = $('#keytable_' + taxkeyNum);
	$(function() {
		$table.bootstrapTable('load', data);
	});
}

// 特征图片的弹出层(管理|上传)
function optionFeatureimg(keyitemId) {
	var width = $(window).width();
	var height = $(window).height();
	var layer_width = "90%";
	var layer_height = "90%";
	if (width > 760) {
		layer_width = "1000px";
		layer_height = "600px";
	}

	layer.confirm('请选择对特征图片的操作', {
		title : false,
		scroller : false,
		skin : 'layui-layer-lan',
		btn : [ '查看/管理', '上传图片' ]
	// 按钮
	}, function() {
		layer.closeAll();
		layer.open({
			type : 2,
			title : '<h4>查看/管理特征图片</h4>',
			fixed : false, // 不固定
			area : [ layer_width, layer_height ],
			content : '/console/taxkey/optionFeatureImg/' + keyitemId
		});

	}, function() {
		layer.closeAll();
		layer.open({
			type : 2,
			title : '<h4>上传特征图片</h4>',
			fixed : false, // 不固定
			area : [ layer_width, layer_height ],
			content : '/console/taxkey/uploadFeatureImg/' + keyitemId
		});
	});
}

// 删除keyitem并刷新table
function removeObject(id, type) {
	var countTaxkey = parseInt($('#countTaxkey').val());
	var r = confirm("确定删除该" + type + "?");
	if (r == true) {
		$.get("/console/" + type + "/rest/remove/" + id, {}, function(data,
				status) {
			if (status) {
				if (data) {
					showKeyTable(countTaxkey);
					layer.msg('删除成功', {
						time : 500
					}, function() {
					})
				} else {
					layer.msg('操作失败', function() {
					})
				}
			} else {
				layer.msg('操作失败', function() {
				})
			}
		})
	} else {
		layer.msg('操作取消', {
			time : 500,
		})
	}
};

// 删除resource并刷新table
function removeThisResource(id, type) {
	var r = confirm("确定删除该" + type + "?");
	if (r == true) {
		$.get("/console/" + type + "/rest/remove/" + id, {}, function(data,
				status) {
			if (status) {
				if (data) {
					$("#results_table").bootstrapTable('refresh');
					layer.msg('删除成功', {
						time : 500
					}, function() {
					})
				} else {
					layer.msg('操作失败', function() {
					})
				}
			} else {
				layer.msg('操作失败', function() {
				})
			}
		})
	} else {
		layer.msg('操作取消', {
			time : 500,
		})
	}
};