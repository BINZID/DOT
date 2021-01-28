$(document).ready(function() {
});
function clearInput() {
	$('#thisForm')[0].reset()
};
function resetFrom() {
	$('#thisForm')[0].reset()
};
function editThisObject(id, type) {
	window.location.href = "/console/" + type + "/edit/" + id
};

/* 删除选中对象，刷新页面 */
function removeObject(id, type) {
	$.get("/console/" + type + "/rest/canRemoveObj/" + id, {}, function(data){
		if (data.rsl) {
			var mark = "";
			if (type == "taxon") {
				mark = "该操作将删除与此 " + type + " 相关的所有数据(包括分类树节点)，确定删除该 " + type + " ?";
			}else if(type == "taxaset"){
				mark = "该操作将删除此 " + type + " 下所有数据(包括分类树节点)，确定删除该 " + type + " ?";
			}else{
				mark = "确定删除该 " + type + " ?";
			}
			var r = confirm(mark);
			if (r == true) {
				$.get("/console/" + type + "/rest/remove/" + id, {}, function(data, status) {
					if (status) {
						if (data) {
							layer.msg('删除成功', {
								time : 500,
							}, function() {
								 $('[name="refresh"]').click() 
								window.location.reload();
							})
						} else {
							layer.msg('您没有此权限', function() {
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
		}else {
			layer.msg('您没有权限')
		}
	})
};
/*删除选中对象，刷新Table数据*/
function removeThisObject(id, type) {
	var r = confirm("确定删除该" + type + "?");
	if (r == true) {
		$.get("/console/" + type + "/rest/remove/" + id, {}, function(data, status) {
			if (status) {
				if (data) {
					layer.msg('删除成功', {time : 500}, function() {
						$('[name="refresh"]').click()
					})
				} else {
					layer.msg('您没有此权限', function() {
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

function editSelectObject(type) {
	var number = 0;
	var checkId = "";
	$("input:checkbox[id^='sel']:checked").each(function(i) {
		number = number + 1;
		checkId = $(this).attr('id');
		checkId = checkId.substring(4)
	});
	if (number == 0) {
		alert("请选择数据")
	} else if (number > 1) {
		alert("您选择了" + number + "条数据，只能选择1条数据进行编辑")
	} else {
		editThisObject(checkId, type)
	}
};
function removeSelectObject(type) {
	var number = 0;
	var checkId = "";
	$("input:checkbox[id^='sel']:checked").each(function(i) {
		number = number + 1
	});
	if (number == 0) {
		alert("请选择数据")
	} else {
		var msg = "您确定要删除这" + number + "条记录吗？";
		var ids = "";
		if (confirm(msg) == true) {
			$("input:checkbox[id^='sel']:checked").each(function(i) {
				checkId = $(this).attr('id');
				checkId = checkId.substring(4);
				if (i == 0) {
					ids = checkId
				} else {
					ids = ids + "￥" + checkId
				}
			});
			/*$.ajax({
				url : '/console/' + type + '/rest/removeMany/' + ids,
				cache : false,
				success : function() {
					layer.msg('已批量删除' + number + '数据', {
						time : 500,
					}, function() {
						$('[name="refresh"]').click()
					})
				},
				error : function() {
					layer.msg('操作失败')
				}
			})*/
			$.get("/console/" + type + "/rest/removeMany/" + ids, {}, function(data,
				status) {
			if (status) {
				if (data) {
					layer.msg('删除成功', {
						time : 500,
					}, function() {
						$('[name="refresh"]').click()
					})
				} else {
					layer.msg('您没有此权限', function() {
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
	}
};

function exportSelectObject(type) {
	var number = 0;
	var checkId = "";
	$("input:checkbox[id^='sel']:checked").each(function(i) {
		number = number + 1
	});
	if (number == 0) {
		alert("请选择导出数据")
	} else {
		var msg = "您确定要导出这" + number + "条" + type + "记录吗？";
		var ids = "";
		if (confirm(msg) == true) {
			$("input:checkbox[id^='sel']:checked").each(function(i) {
				checkId = $(this).attr('id');
				checkId = checkId.substring(4);
				if (i == 0) {
					ids = checkId
				} else {
					ids = ids + "￥" + checkId
				}
			});
			$.get("/console/" + type + "/rest/export/" + ids, {}, function(data, status) {
				if (status) {
					if (data) {
						layer.msg('导出成功', {time : 500,}, function() {
							$('[name="refresh"]').click()
						})
					} else {
						layer.msg('您没有此权限', function() {
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
	}
};