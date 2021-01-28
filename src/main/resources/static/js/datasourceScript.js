    //清空表单
    function clearInput() {
        $('#thisForm')[0].reset();
        $("#dType").empty();
        $("#dVerifier").empty();
        $('#thisForm').data('bootstrapValidator')
        .updateStatus('versions', 'NOT_VALIDATED',null)
        .updateStatus('title', 'NOT_VALIDATED',null)
        .updateStatus('info', 'NOT_VALIDATED',null)
        .updateStatus('creater', 'NOT_VALIDATED',null)
        .updateStatus('createtime', 'NOT_VALIDATED',null)
        .updateStatus('dRightsholder', 'NOT_VALIDATED',null)
        .updateStatus('dCopyright', 'NOT_VALIDATED',null)
        .updateStatus('dAbstract', 'NOT_VALIDATED',null)
        .updateStatus('dKeyword', 'NOT_VALIDATED',null)
        .updateStatus('dLink', 'NOT_VALIDATED',null)
        .validateField('versions')
        .validateField('title')
        .validateField('info')
        .validateField('creater')
        .validateField('createtime')
        .validateField('dRightsholder')
        .validateField('dCopyright')
        .validateField('dAbstract')
        .validateField('dKeyword')
        .validateField('dLink');
    };
    
    $(document).ready(function () {
    	/*数据源添加/编辑*/
    	// 1.数据源类型
    	$("#dType").select2({
            placeholder: "请选择数据源类型，可以是一个数据库或一篇文献等",
    	});
    	// 2.审核专家
        buildSelect2("dVerifier", "console/expert/rest/select", "审核该数据源信息的专家，引用 “13 专家信息”");
    	
        // 4.后端验证错误提示
        if($('#errorMsg').val()==""||$('#errorMsg').val()==null){
        } else{
            layer.msg($('#errorMsg').val(), function(){
            });
        }
        
        // 6.日期选择器
        $("#createtime").datetimepicker({
            language: 'zh-CN',
            minView: "month", //选择日期后，不会再跳转去选择时分秒 
            forceParse:true,//当选择器关闭的时候，是否强制解析输入框中的值
            autoclose: true,//选中之后自动隐藏日期选择框
            todayBtn: true,//今日按钮
            format: 'yyyy-mm-dd'//format: 'yyyy-mm-dd hh:ii:ss'
        }).on('hide',function(e) {
            $('#thisForm').data('bootstrapValidator')
                .updateStatus('createtime', 'NOT_VALIDATED',null)
                .validateField('createtime');
        });
    });
    
  //前端验证错误提示
    $(document).ready(function() {
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
            	'versions': {
                    validators: {
                    	notEmpty: {
                    	},
                        stringLength: {
                            min: 0,
                            max: 15
                        },
                    }  
                },
                'title': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 1,
                            max: 30,
                        },
                    } 
                },	
                'dType': {
                    validators: {
                        notEmpty: {
                        },
                    }
                },	
                'dVerifier': {
                    validators: {
                        notEmpty: {
                        },
                    }
                },	
                'creater': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 1,
                            max: 30,
                        },
                    }
                },	
                'createtime': {
                    validators: {
                        notEmpty: {
                        },
                    }
                },	
                'dAbstract': {
                    validators: {
                        notEmpty: {
                        },
                        stringLength: {
                            min: 1,
                            max: 500,
                        },
                    }
                },	
                'info': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 500,
                        },
                    }
                },
                'dLink': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 500,
                        },
                    }
                },
                'dKeyword': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 100,
                        },
                    }
                },
                'dCopyright': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 200,
                        },
                    }
                },
                'dRightsholder': {
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 100,
                        },
                    }
                },
            }
        });
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