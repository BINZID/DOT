//清空表单
function clearInput() {
    $('#thisForm')[0].reset();
    $('#thisForm').data('bootstrapValidator')
        .updateStatus('tsname', 'NOT_VALIDATED',null)
        .updateStatus('tsinfo', 'NOT_VALIDATED',null)
        .validateField('tsname')
        .validateField('tsinfo');
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
            'tsname': {
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
                        	var tsname = $('#tsname').val();
                        	var editdsname = $('#editdsname').val();
                        	if (tsname != editdsname) {
                        		return $.ajax({
                        			url: '/console/taxaset/rest/isReDsname',
                        			data: {
                        				name: tsname
                        			},
                        			cache: false,
                        			dataType: "json",
                        			success: function (data) {
                        				if (data) {
                        					validator.updateStatus('tsname', 'VALID', 'callback');
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
            'tsinfo': {
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