//后端验证错误提示
$(document).ready(function () {
    if($('#errorMsg').val()==""||$('#errorMsg').val()==null){
    }else{
        layer.msg($('#errorMsg').val(), function(){ });
    }
    
	// 文献类型
	$("#ptype").select2({
        placeholder: "请选择参考资料的类型",
	});
	// 文献描述语言
	$("#languages").select2({
		placeholder: "请选择描述语言",
	});
	// 文献原始描述语言
	$("#olang").select2({
		placeholder: "请选择原始描述语言",
	});
});
// 默认期刊
$(document).ready(function() {
	/* 1.期刊：著者．题名[J]．刊名．出版年，卷（期）∶起止页码 */
	$("#requireRefInfo").show();
	$("#refAuthor").show();
	$("#refTitle").show();
	$("#refPyear").show();
	$("#refJournal").show();
	$("#referenceJournal").html("刊名：").show();
	$("#refVolume").show();
	$("#refPeriod").show();
	$("#refPageStart").show();
	$("#refPageEnd").show();
	$("#refTranslator").hide();
	$("#refPlace").hide();
	$("#refPress").hide();
	$("#refVersion").hide();
})

$(document).ready(function() {
	var val = $("#ptype").val();
	$("#requireRefInfo").hide();
	$("select[id = ptype]").on("change", function(){
		if($("#ptype").val()=="1"){
			/* 1.期刊：著者．题名[J]．刊名．出版年，卷（期）∶起止页码 */
			$("#requireRefInfo").show();
			$("#refAuthor").show();
			$("#refTitle").show();
			$("#refPyear").show();
			$("#refJournal").show();
			$("#referenceJournal").html("刊名：").show();
			$("#refVolume").show();
			$("#refPeriod").show();
			$("#refPageStart").show();
			$("#refPageEnd").show();
			$("#refTranslator").hide();
			$("#refPlace").hide();
			$("#refPress").hide();
			$("#refVersion").hide();
		}else if($("#ptype").val()=="2"){
			/* 2.专著：著者．书名[M]．版本（第一版不录）．出版地∶出版者，出版年∶起止页码 */
			$("#requireRefInfo").show();
			$("#refAuthor").show();
			$("#refTitle").show();
			$("#refVersion").show();
			$("#refPlace").show();
			$("#refPress").show();
			$("#refPyear").show();
			$("#refPageStart").show();
			$("#refPageEnd").show();
			$("#refJournal").hide();
			$("#referenceJournal").html("刊名：").show();
			$("#refTranslator").hide();
			$("#refVolume").hide();
			$("#refPeriod").hide();
		}else if($("#ptype").val()=="3"){
			/* 3.论文集：著者．题名．编者．论文集名[C]．出版地∶出版者，出版年∶起止页码 */
			$("#requireRefInfo").show();
			$("#refAuthor").show();
			$("#refTitle").show();
			$("#refTranslator").show();
			$("#refJournal").show();
			$("#referenceJournal").html("论文集名：").show();
			$("#refPlace").show();
			$("#refPress").show();
			$("#refPyear").show();
			$("#refPageStart").show();
			$("#refPageEnd").show();
			$("#refVolume").hide();
			$("#refPeriod").hide();
			$("#refVersion").hide();
		}else if($("#ptype").val()=="4"){
			$("#requireRefInfo").show();
			$("#refAuthor").show();
			$("#refTitle").show();
			$("#refTranslator").show();
			$("#refJournal").show();
			$("#referenceJournal").html("论文集名：").show();
			$("#refPlace").show();
			$("#refPress").show();
			$("#refPyear").show();
			$("#refPageStart").show();
			$("#refPageEnd").show();
			$("#refVolume").show();
			$("#refPeriod").show();
			$("#refVersion").show();
		}else {
			$("#requireRefInfo").hide();
			$("#ptype").select2({
		        placeholder: "请选择文献类型",
		 	});
		}
	});
});

//清空表单
function clearInput() {
    $('#thisForm')[0].reset();
    $('#thisForm').data('bootstrapValidator')
    .updateStatus('author', 'NOT_VALIDATED',null)
    .updateStatus('translator', 'NOT_VALIDATED',null)
    .updateStatus('title', 'NOT_VALIDATED',null)
    .updateStatus('keywords', 'NOT_VALIDATED',null)
    .updateStatus('tpage', 'NOT_VALIDATED',null)
    .updateStatus('tchar', 'NOT_VALIDATED',null)
    .updateStatus('ptype', 'NOT_VALIDATED',null)
    .updateStatus('journal', 'NOT_VALIDATED',null)
    .updateStatus('languages', 'NOT_VALIDATED',null)
    .updateStatus('olang', 'NOT_VALIDATED',null)
    .updateStatus('place', 'NOT_VALIDATED',null)
    .updateStatus('press', 'NOT_VALIDATED',null)
    .updateStatus('pyear', 'NOT_VALIDATED',null)
    .updateStatus('version', 'NOT_VALIDATED',null)    
    .updateStatus('refstr', 'NOT_VALIDATED',null)
    .updateStatus('refs', 'NOT_VALIDATED',null)    
    .updateStatus('refe', 'NOT_VALIDATED',null)    
    .updateStatus('rVolume', 'NOT_VALIDATED',null)    
    .updateStatus('rPeriod', 'NOT_VALIDATED',null)    
    .updateStatus('isbn', 'NOT_VALIDATED',null)    
    .updateStatus('rLink', 'NOT_VALIDATED',null)    
    .validateField('author')
    .validateField('translator')
    .validateField('title')
    .validateField('keywords')
    .validateField('tpage')
    .validateField('tchar')
    .validateField('ptype')
    .validateField('journal')
    .validateField('languages')
    .validateField('olang')
    .validateField('place')
    .validateField('press')
    .validateField('pyear')
    .validateField('version')
    .validateField('refstr')
    .validateField('refs')
    .validateField('refe')
    .validateField('rVolume')
    .validateField('rPeriod')
    .validateField('isbn')
    .validateField('rLink')
};
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
	    	'author': {
	            validators: {
	            	notEmpty: {
	                },
	                stringLength: {
	                    min: 0,
	                    max: 50
	                },
	            }
	        },
	    	'refstr': {
	            validators: {
	            	notEmpty: {
	                },
	                stringLength: {
	                    min: 0,
	                    max: 500
	                },
	            }
	        },
	        'translator': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 30
	                },
	            }
	        },
	        'title': {
	            validators: {
	                notEmpty: {
	                },
	                stringLength: {
	                    min: 1,
	                    max: 100,
	                },
	            }
	        },
	        'pyear': {
	            validators: {
	            	notEmpty: {
	                },
	                stringLength: {
	                    min: 0,
	                    max: 30
	                },
	            }
	        },
	        'keywords': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 100
	                },
	            }
	        },
	        'tpage': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'tchar': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'ptype': {
	            validators: {
	            	notEmpty: {
	                },
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'journal': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 50
	                },
	            }
	        },
	        'place': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 50
	                },
	            }
	        },
	        'press': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 50
	                },
	            }
	        },
	        'languages': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'olang': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'version': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'refs': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'refe': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 15
	                },
	            }
	        },
	        'isbn': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 30
	                },
	            }
	        },
	        'rVolume': {
	            validators: {
	                stringLength: {
	                    min: 0,
	                    max: 30
	                },
	            }
	        },
	        'rLink': {
	        	validators: {
	        		stringLength: {
	        			min: 0,
	        			max: 500
	        		},
	        	}
	        },
	        'remark': {
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

//自动解析完整题录
function handleRefstr() {
	var refstr = $("#refstr").val();
	if (null != refstr && "" != refstr) {
		$.get("/console/ref/rest/handleRefstr", { refstr: refstr },
				function(data){
			$("#title").val(data.title);
			$("#pyear").val(data.pyear);
			$("#author").val(data.author);
			$("#journal").val(data.journal);
			$("#refs").val(data.refs);
			$("#refe").val(data.refe);
			$("#rVolume").val(data.rVolume);
			$("#place").val(data.place);
			$("#press").val(data.press);
			$("#translator").val(data.translator);
			var ptype = data.ptype;
			switch(ptype){
			case 1:
			  $("#ptype").prepend("<option value='" + data.ptype + "' selected='selected'>" + "期刊[J]" +"</option>");
			  $("#requireRefInfo").show();
			  $("#refAuthor").show();
			  $("#refTitle").show();
			  $("#refPyear").show();
			  $("#refJournal").show();
			  $("#referenceJournal").html("刊名：").show();
			  $("#refVolume").show();
			  $("#refPeriod").show();
			  $("#refPageStart").show();
			  $("#refPageEnd").show();
			  $("#refTranslator").hide();
			  $("#refPlace").hide();
			  $("#refPress").hide();
			  $("#refVersion").hide();
			  break;
			case 2:
			  $("#ptype").prepend("<option value='" + data.ptype + "' selected='selected'>" + "专著[M]" +"</option>");
			  $("#requireRefInfo").show();
			  $("#refAuthor").show();
			  $("#refTitle").show();
			  $("#refVersion").show();
			  $("#refPlace").show();
			  $("#refPress").show();
			  $("#refPyear").show();
			  $("#refPageStart").show();
			  $("#refPageEnd").show();
			  $("#refJournal").hide();
			  $("#referenceJournal").html("刊名：").show();
			  $("#refTranslator").hide();
			  $("#refVolume").hide();
			  $("#refPeriod").hide();
			  break;
			case 3:
				$("#ptype").prepend("<option value='" + data.ptype + "' selected='selected'>" + "论文集[C]" +"</option>");
				$("#requireRefInfo").show();
				$("#refAuthor").show();
				$("#refTitle").show();
				$("#refTranslator").show();
				$("#refJournal").show();
				$("#referenceJournal").html("论文集名：").show();
				$("#refPlace").show();
				$("#refPress").show();
				$("#refPyear").show();
				$("#refPageStart").show();
				$("#refPageEnd").show();
				$("#refVolume").hide();
				$("#refPeriod").hide();
				$("#refVersion").hide();
				break;
			default:
				$("#ptype").prepend("<option value='" + 4 + "' selected='selected'>" + "其他" +"</option>");
				$("#requireRefInfo").show();
				$("#refAuthor").show();
				$("#refTitle").show();
				$("#refTranslator").show();
				$("#refJournal").show();
				$("#referenceJournal").html("论文集名：").show();
				$("#refPlace").show();
				$("#refPress").show();
				$("#refPyear").show();
				$("#refPageStart").show();
				$("#refPageEnd").show();
				$("#refVolume").show();
				$("#refPeriod").show();
				$("#refVersion").show();
				layer.msg("解析失败");
			}
		});
	}else {
		layer.msg("请先填写完整题录");
	}
}