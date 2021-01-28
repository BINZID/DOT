/**
 * Created by Tianshan on 18-5-16.
 */

	function inviteThisObject(tid) {
		$.get("/console/team/rest/canInviteUser/" + tid, {}, function(data){
			if (data.rsl) {
				window.location.href = "/console/team/compose/" + tid
			}else {
				layer.msg("您没有权限")
			}
		})
	};
	
	function inviteObject() {
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
			alert("您选择了" + number + "条数据，只能选择1条团队发出邀请")
		} else {
			inviteThisObject(checkId)
		}
	};


	//删除Team成员
	function delThisMember(teamId, userId) {
		$.get("/console/team/rest/canTransMember/" + teamId, {}, function(data){
			if (data.rsl) {
				var r = confirm("是否将该成员删除?");
				if (r == true) {
					$.post("/console/team/rest/delMember", {
						"_csrf" : $('input[name="_csrf"]').val(),
						teamId : teamId,
						userId : userId
					}, function(status) {
						if (status) {
							layer.msg('删除成功', {
								time : 500,
							}, function() {
								$('[name="refresh"]').click()
							})
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
				layer.msg('您没有权限', function() {
				})
			}
		})
	};
	

	//授权
	function transThisMember() {
		var tid = $("#tid").val();
		var uid = $("#user").select2("val");
		if (null == uid || "" == uid){
			layer.msg('请先选择授权对象', function() {
			})
		}else{
			var r = confirm("确定授权?");
			if (r == true) {
				layer.load(2);
				$.post("/console/team/rest/transMember", {
					"_csrf" : $('input[name="_csrf"]').val(),
					teamId : tid,
					userId : uid
				}, function(status) {
					if (status) {
						var href = window.location.protocol + "/console/team/details/" + tid;
						layer.msg('授权成功', {
							time : 500,
						}, function() {
							parent.layer.close(parent.layer.getFrameIndex(window.name));
							parent.window.location.href = href;
						})
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
	}	
	//权限转移 -- 初始化成员下拉选
	$(document).ready(function () {
	    buildSelect2("user", "/console/user/rest/select/" + $("#tid").val(), "请选择授权成员");
	});
	//权限转移 -- 成员选择
	function transObj(tid){
		$.get("/console/team/rest/canTransMember/" + tid, {}, function(data){
			if (data.rsl) {
				var width=$(window).width();
				var height=$(window).height();
				var layer_width="90%";
				var layer_height="90%";
				if(width>760){
					layer_width="500px";
					layer_height="215px";
				}
				layer.open({
				    type: 2,
				    title:'<h4>请选择授权对象</h4>',
				    fixed: true, //不固定
				    area: [layer_width, layer_height],
				    content: '/console/user/select/' + tid
				});
			}else {
				layer.msg("您没有权限");
			}
		});
	}
    //清空表单
    function clearInput() {
        $('#thisForm')[0].reset();
        $('#thisForm').data('bootstrapValidator')
            .updateStatus('name', 'NOT_VALIDATED',null)
            .updateStatus('note', 'NOT_VALIDATED',null)
            .validateField('name')
            .validateField('note');
    };
    //不对默认团队名称做修改
    $(document).ready(function () {
    	var mark = $("#mark").val();
    	if(mark == 'Default'){
    		$("#name").attr('readOnly', true);
    	}
    });
    
    //后端验证错误提示
    $(document).ready(function () {
        if($('#errorMsg').val()==""||$('#errorMsg').val()==null){
        }
        else{
            layer.msg($('#errorMsg').val(), function(){
            });
        }
    });
    
  //前端验证错误提示
    $(document).ready(function() {
    	var warning_rename = "名称不可用";
        //获取host
        $("#base_url").val(window.location.host);
        $('#thisForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon ',
                invalid: 'glyphicon ',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                'name': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 3,
                            max: 30,
                        },
                        callback: {
                            message: warning_rename,
                            callback: function (value, validator) {
                            	var name = $('#name').val();
                            	var editdsname = $('#editdsname').val();
                            	if (name != editdsname) {
                            		return $.ajax({
                            			url: '/console/team/rest/isReDsname',
                            			data: {
                            				name: name
                            			},
                            			cache: false,
                            			dataType: "json",
                            			success: function (data) {
                            				if (data) {
                            					validator.updateStatus('name', 'VALID', 'callback');
                            					return true;
                            				}else {
                            					return false;
                            				}
                            			},
                            			error: function () {
                            				return false;
                            			}
                            		});
								}else {
									return true;
								}
                            }
                        },
                    }
                },
                'note': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 500,
                        },
                    }
                },
            }
        });
    });