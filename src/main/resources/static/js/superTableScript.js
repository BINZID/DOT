/**
 * Created by Tianshan on 16-7-28.
 */
$(document).ready(function () {

});

function clearInput() {
    $('#thisForm')[0].reset();
};
function resetFrom() {
    $('#thisForm')[0].reset();
};
function editThisObject(id, type) {
    window.location.href = "/super/" + type + "/edit/" + id;
};
// 逻辑删除
function removeThisObject(id, type) {
    var r = confirm("是否删除?");
    if (r == true) {
        $.get("/super/" + type + "/rest/remove/" + id,
            {},
            function (data, status) {
                if (status) {
                    if (data) {
                        layer.msg('删除成功',
                            {
                                time: 500, //1.5s后自动关闭
                            },
                            function () {
                                //window.location.reload();//刷新当前页面.
                                $('[name="refresh"]').click();//刷新当前页面.
                            });
                    }
                    else {
                        layer.msg('操作失败', function () {
                        });
                    }
                }
                else {
                    layer.msg('操作失败', function () {
                    });
                }
            });
    }
    else {
        layer.msg('操作取消',
            {
                time: 500, //0.5s后自动关闭
            });
    }
};
// 物理删除
function deleteThisObject(id, type) {
    var r = confirm("是否删除?");
    if (r == true) {
        $.get("/super/" + type + "/rest/delete/" + id,
            {},
            function (data, status) {
                if (status) {
                    if (data) {
                        layer.msg('删除成功',
                            {
                                time: 500, //1.5s后自动关闭
                            },
                            function () {
                                //window.location.reload();//刷新当前页面.
                                $('[name="refresh"]').click();//刷新当前页面.
                            });
                    }
                    else {
                        layer.msg('操作失败', function () {
                        });
                    }
                }
                else {
                    layer.msg('操作失败', function () {
                    });
                }
            });
    }
    else {
        layer.msg('操作取消',
            {
                time: 500, //0.5s后自动关闭
            });
    }
};
//选择编辑
function editSelectObject(type) {
    var number = 0;
    var checkId = "";
    $("input:checkbox[id^='sel']:checked").each(function (i) {
        number = number + 1;
        checkId = $(this).attr('id');
        checkId = checkId.substring(4);
    });
    if (number == 0) {
        alert("请选择数据");
    }
    else if (number > 1) {
        alert("您选择了" + number + "条数据，只能选择1条数据进行编辑");
    }
    else {
        editThisObject(checkId, type);
    }
};
//删除
function removeSelectObject(type) {
    var number = 0;
    var checkId = "";
    $("input:checkbox[id^='sel']:checked").each(function (i) {
        number = number + 1;
    });

    if (number == 0) {
        alert("请选择数据");
    }
    else {
        var msg = "您确定要删除这" + number + "条记录吗？";
        var ids = "";
        if (confirm(msg) == true) {
            $("input:checkbox[id^='sel']:checked").each(function (i) {
                checkId = $(this).attr('id');
                checkId = checkId.substring(4);
                if (i == 0) {
                    ids = checkId;
                }
                else {
                    ids = ids + "￥" + checkId;
                }
            });
            $.ajax({
                url: '/super/' + type + '/rest/removeMany/' + ids,
                cache: false,
                success: function () {
                    layer.msg('已批量删除' + number + '数据',
                        {
                            time: 500, //1.5s后自动关闭
                        },
                        function () {
                            $('[name="refresh"]').click();//刷新当前页面.
                        });
                },
                error: function () {
                    layer.msg('操作失败');
                }
            });
        } else {
            layer.msg('操作取消',
                {
                    time: 500, //0.5s后自动关闭
                });
        }
    }
};

//批量删除
function deleteSelectObject(type) {
    var number = 0;
    var checkId = "";
    $("input:checkbox[id^='sel']:checked").each(function (i) {
        number = number + 1;
    });

    if (number == 0) {
        alert("请选择数据");
    }
    else {
        var msg = "您确定要删除这" + number + "条记录吗？";
        var ids = "";
        if (confirm(msg) == true) {
            $("input:checkbox[id^='sel']:checked").each(function (i) {
                checkId = $(this).attr('id');
                checkId = checkId.substring(4);
                if (i == 0) {
                    ids = checkId;
                }
                else {
                    ids = ids + "￥" + checkId;
                }
            });
            $.ajax({
                url: 'super/' + type + '/rest/deleteMany/' + ids,
                cache: false,
                success: function (data) {
                    if (data > 0) {
                        if (data == number) {
                            layer.msg('已批量删除' + number + '条数据',
                                {
                                    time: 1000, //1.5s后自动关闭
                                },
                                function () {
                                    $('[name="refresh"]').click();//刷新当前页面.
                                });
                        }
                        else {
                            layer.msg('已批量删除' + data + '条数据，' + (number - data) + '条数据失败，请检查这些数据是否存在绑定关系',
                                {
                                    time: 2000, //1.5s后自动关闭
                                },
                                function () {
                                    $('[name="refresh"]').click();//刷新当前页面.
                                });
                        }
                    }
                    else {
                        layer.msg('操作失败，请检查该数据是否存在绑定关系');
                    }
                },
                error: function () {
                    layer.msg('操作失败');
                }
            });
        } else {
            layer.msg('操作取消',
                {
                    time: 500, //0.5s后自动关闭
                });
        }
    }
};