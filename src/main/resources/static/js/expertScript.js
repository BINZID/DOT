/**
 * Created by BinZi on 18-9-25.
 */
    //清空表单
    function clearInput() {
        $('#thisForm')[0].reset();
        $('#thisForm').data('bootstrapValidator')
            .updateStatus('cnName', 'NOT_VALIDATED',null)
            .updateStatus('enName', 'NOT_VALIDATED',null)
            .updateStatus('cnAddress', 'NOT_VALIDATED',null)
            .updateStatus('enAddress', 'NOT_VALIDATED',null)
            .updateStatus('cnCompany', 'NOT_VALIDATED',null)
            .updateStatus('enCompany', 'NOT_VALIDATED',null)
            .updateStatus('cnHomePage', 'NOT_VALIDATED',null)
            .updateStatus('enHomePage', 'NOT_VALIDATED',null)
            .updateStatus('expEmail', 'NOT_VALIDATED',null)
            .updateStatus('expINfo', 'NOT_VALIDATED',null)
            .validateField('cnName')
            .validateField('enName')
	        .validateField('cnAddress')
	        .validateField('enAddress')
	        .validateField('cnCompany')
	        .validateField('enCompany')
	        .validateField('cnHomePage')
	        .validateField('enHomePage')
	        .validateField('expEmail')
	        .validateField('expInfo');
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
                'cnName': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 1,
                            max: 30,
                        },
                    }
                },
                'enName': {
                    validators: {
                        stringLength: {
                            min: 1,
                            max: 30,
                        },
                    }
                },
                'cnCompany': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 1,
                            max: 50,
                        },
                    }
                },
                'enCompany': {
                	validators: {
                		stringLength: {
                			min: 1,
                			max: 100,
                		},
                	}
                },
                'cnAddress': {
                	validators: {
                		notEmpty: {
                        },
                		stringLength: {
                			min: 1,
                			max: 50,
                		},
                	}
                },
                'enAddress': {
                	validators: {
                		stringLength: {
                			min: 1,
                			max: 100,
                		},
                	}
                },
                'cnHomePage': {
                	validators: {
                		notEmpty: {
                        },
                		stringLength: {
                			min: 1,
                			max: 200,
                		},
                	}
                },
                'enHomePage': {
                	validators: {
                		stringLength: {
                			min: 1,
                			max: 200,
                		},
                	}
                },
                'expEmail': {
                	validators: {
                		notEmpty: {
                		},
                		stringLength: {
                			min: 1,
                			max: 50,
                		},
                	}
                },
                'expInfo': {
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