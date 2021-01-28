/**
 * Created by BinZi on 18-9-25.
 */
    //清空表单
    function clearInput() {
        $('#thisForm')[0].reset();
        $('#thisForm').data('bootstrapValidator')
            .updateStatus('treename', 'NOT_VALIDATED',null)
            .updateStatus('treeinfo', 'NOT_VALIDATED',null)
            .validateField('treename')
            .validateField('treeinfo');
    };
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
                'treename': {
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
                            	var treename = $('#treename').val();
                            	var editdsname = $('#editdsname').val();
                            	if (treename != editdsname) {
                            		return $.ajax({
                            			url: '/console/taxtree/rest/isReDsname',
                            			data: {
                            				name: treename
                            			},
                            			cache: false,
                            			dataType: "json",
                            			success: function (data) {
                            				if (data) {
                            					validator.updateStatus('treename', 'VALID', 'callback');
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
                'treeinfo': {
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