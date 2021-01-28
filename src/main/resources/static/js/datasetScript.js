/**
 * Created by BinZi on 18-9-25.
 */
	$(document).ready(function () {
	    // 数据集添加/编辑
	    $("#status").select2({
	        placeholder: "请选择数据集状态",
	    });
	    
	    // 共享协议
	    buildSelect2("lisenceid", "console/license/rest/select", "请选择共享协议");
	});
    //清空表单
    function clearInput() {
        $('#thisForm')[0].reset();
        $("#status").empty();
        $('#thisForm').data('bootstrapValidator')
            .updateStatus('dsname', 'NOT_VALIDATED',null)
            .updateStatus('status', 'NOT_VALIDATED',null)
            .updateStatus('lisenceid', 'NOT_VALIDATED',null)
            .updateStatus('dsabstract', 'NOT_VALIDATED',null)
            .validateField('dsname')
            .validateField('status')
            .validateField('lisenceid')
            .validateField('dsabstract');
    };
    //不对默认数据集名称做修改
    $(document).ready(function () {
    	var mark = $("#mark").val();
    	if(mark == 'Default'){
    		$("#dsname").attr('readOnly', true);
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
		$("#base_url").val(window.location.href);
        $('#thisForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon ',
                invalid: 'glyphicon ',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                'dsname': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 1,
                            max: 30,
                        },
                        callback: {
                            message: warning_rename,
                            callback: function (value, validator) {
                            	var dsname = $('#dsname').val();
                            	var editdsname = $('#editdsname').val();
                            	if (dsname != editdsname) {
                            		return $.ajax({
                            			url: '/console/dataset/rest/isReDsname',
                            			data: {
                            				name: dsname
                            			},
                            			cache: false,
                            			dataType: "json",
                            			success: function (data) {
                            				if (data) {
                            					validator.updateStatus('dsname', 'VALID', 'callback');
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
                'status': {
                    validators: {
                    	notEmpty: {
                        },
                    }
                },
                'lisenceid': {
                	validators: {
                		notEmpty: {
                		},
                	}
                },
                'dsabstract': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 500
                        },
                    }
                },
            }
        });
    });