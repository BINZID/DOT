/**
 * Created by Tianshan on 18-5-16.
 */
//全局变量
//当前tab
var activeTab = "1";
//tab验证结果：0-未验证，1-验证通过，-1-验证失败，
var verificationTab1 = 0;
var verificationTab2 = 0;
var verificationTab3 = 0;
var verificationTab4 = 0;
var verificationTab5 = 0;
var verificationTab6 = 0;

$(document).ready(function () {
    //获取host
    $("#base_url").val(window.location.host);
    $('#form_1').bootstrapValidator({
        excluded: [":disabled"], //只对于禁用域不进行验证，其他的表单元素都要验证
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon ',
            invalid: 'glyphicon ',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'scientificname': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            },
            'authorstr': {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 80
                    },
                }
            },
            'epithet': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            }
        }
    });
    $('#form_2').bootstrapValidator({
        excluded: [":disabled"], //只对于禁用域不进行验证，其他的表单元素都要验证
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon ',
            invalid: 'glyphicon ',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'description1': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            },
        }
    });
    $('#form_3').bootstrapValidator({
        excluded: [":disabled"], //只对于禁用域不进行验证，其他的表单元素都要验证
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon ',
            invalid: 'glyphicon ',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'molecular1': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            },
        }
    });
    $('#form_4').bootstrapValidator({
        excluded: [":disabled"], //只对于禁用域不进行验证，其他的表单元素都要验证
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon ',
            invalid: 'glyphicon ',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'commonname1': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            },
        }
    });
    $('#form_5').bootstrapValidator({
        excluded: [":disabled"], //只对于禁用域不进行验证，其他的表单元素都要验证
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon ',
            invalid: 'glyphicon ',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'occurrence2': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            },
        }
    });
    $('#form_6').bootstrapValidator({
        excluded: [":disabled"], //只对于禁用域不进行验证，其他的表单元素都要验证
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon ',
            invalid: 'glyphicon ',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'multimedia1': {
                validators: {
                    notEmpty: {},
                    stringLength: {
                        min: 2,
                        max: 80
                    },
                }
            },
        }
    });
});


//验证状态变更
function changeVerificationTab(tabNum, status) {
    this['verificationTab' + tabNum] = status;
}

//统一验证
function formValidator(formNum) {
    $('#form_' + formNum).data('bootstrapValidator').validate();
    //是否通过校验
    if (!$('#form_' + formNum).data('bootstrapValidator').isValid()) {
        changeVerificationTab(formNum, -1);
        incompleteStep(formNum);
        return false; //没有通过校验
    } else {
        changeVerificationTab(formNum, 1);
        completeStep(formNum);
        return true; //通过校验
    }
}

//点击标签页的操作
$('#addSteps a').click(function (e) {
    var idNum = this.hash;
    idNum = idNum.substr(idNum.lastIndexOf("_") + 1);
    //alert("当前："+activeTab+" | 去往："+idNum);
    if (verifierStep(idNum)) {
        e.preventDefault();
        $(this).unbind('show.bs.tab');
        completeStep(idNum - 1);
        activeStep(idNum);
        $(this).tab('show');
        activeTab = idNum;
    } else {
        $(this).on('show.bs.tab', function (e) {
            e.preventDefault();
        });
        layer.msg("请先完成前置步骤的填写", function () {
        });
    }
})

//验证前置步骤完善程度
function verifierStep(stepNum) {
    if (Number(stepNum) < Number(activeTab)) {
        changeVerificationTab(activeTab, 0);
        return true;
    }
    switch (stepNum) {
        case "2":
            formValidator(1);
            if (verificationTab1 == 1) { //验证通过
                return true;
            } else { //验证不通过
                return false;
            }
            break;
        case "3":
            formValidator(1);
            formValidator(2);
            if (
                verificationTab1 == 1 &&
                verificationTab2 == 1
            ) { //验证通过
                return true;
            } else { //验证不通过
                return false;
            }
            break;
        case "4":
            formValidator(1);
            formValidator(2);
            formValidator(3);
            if (
                verificationTab1 == 1 &&
                verificationTab2 == 1 &&
                verificationTab3 == 1
            ) { //验证通过
                return true;
            } else { //验证不通过
                return false;
            }
            break;
        case "5":
            formValidator(1);
            formValidator(2);
            formValidator(3);
            formValidator(4);
            if (
                verificationTab1 == 1 &&
                verificationTab2 == 1 &&
                verificationTab3 == 1 &&
                verificationTab4 == 1
            ) { //验证通过
                return true;
            } else { //验证不通过
                return false;
            }
            break;
        case "6":
            formValidator(1);
            formValidator(2);
            formValidator(3);
            formValidator(4);
            formValidator(5);
            if (
                verificationTab1 == 1 &&
                verificationTab2 == 1 &&
                verificationTab3 == 1 &&
                verificationTab4 == 1 &&
                verificationTab5 == 1
            ) { //验证通过
                return true;
            } else { //验证不通过
                return false;
            }
            break;
        case "7":
            formValidator(1);
            formValidator(2);
            formValidator(3);
            formValidator(4);
            formValidator(5);
            formValidator(6);
            if (
                verificationTab1 == 1 &&
                verificationTab2 == 1 &&
                verificationTab3 == 1 &&
                verificationTab4 == 1 &&
                verificationTab5 == 1 &&
                verificationTab6 == 1
            ) { //验证通过
                return true;
            } else { //验证不通过
                return false;
            }
            break;
        default:
            return true;
    }
};

//步骤跳转
function toStep(stepNum) {
    var lable = $("#label_" + stepNum);
    lable.click();
    //activeTab(stepNum);
};

//添加当前步骤样式
function activeStep(stepNum) {
    var labelTitle = $("#label_title_" + stepNum);
    labelTitle.removeClass();
    labelTitle.addClass("badge label label-primary");
    var labelIcon = $("#lable_icon_" + stepNum);
    labelIcon.removeClass();
    labelIcon.addClass("fa fa-chevron-right");
    labelIcon.css("color", "#3c8dbc");

};

//配置已完成步骤样式
function completeStep(stepNum) {
    var labelTitle = $("#label_title_" + stepNum);
    labelTitle.removeClass();
    labelTitle.addClass("badge label label-success");
    var labelIcon = $("#lable_icon_" + stepNum);
    labelIcon.removeClass();
    labelIcon.addClass("fa fa-chevron-right");
    labelIcon.css("color", "#00a65a");

};

//配置未完成步骤样式
function incompleteStep(stepNum) {
    var labelTitle = $("#label_title_" + stepNum);
    labelTitle.removeClass();
    labelTitle.addClass("badge label label-danger");
    var labelIcon = $("#lable_icon_" + stepNum);
    labelIcon.removeClass();
    labelIcon.addClass("fa fa-angle-right");
    labelIcon.css("color", "#dd4b39");

};