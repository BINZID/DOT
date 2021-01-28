/**
 * Created by BinZi on 18-9-25.
 */
    //清空表单
    function clearInput() {
     	$('#thisForm')[0].reset();
        $("#email").empty();
        CKEDITOR.instances.editor.setData(' ');
        $("#editor").val("");
        $('#thisForm').data('bootstrapValidator')
            .updateStatus('title', 'NOT_VALIDATED',null)
            .updateStatus('text', 'NOT_VALIDATED',null)
            .validateField('title')
            .validateField('text');
    }; 
    //后端验证错误提示
    $(document).ready(function () {
        if($('#errorMsg').val()==""||$('#errorMsg').val()==null){
        }else{
            layer.msg($('#errorMsg').val(), function(){
            });
        }
    });
    
    //前端验证错误提示
    $(document).ready(function() {
	    $("#base_url").val(window.location.host);
        $('#thisForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon ',
                invalid: 'glyphicon ',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
            	'email': {
                    validators: {
                        notEmpty: {
                        },
                    }
                },
                'title': {
                    validators: {
                        notEmpty: {
                        },
                        regexp: {
                            regexp: /^[\da-zA-Z_\u4e00-\u9f5a【】]+$/,
                            message: warning_Illegal
                        },
                        stringLength: {
                            min: 2,
                            max: 30,
                        },
                    }
                },
                'text': {
                    validators: {
                       
                    }
                },
            }
        });
    });