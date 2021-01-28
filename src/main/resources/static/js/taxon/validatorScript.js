/**
 * Created by Tianshan on 18-5-16.
 */
$(document).ready(function() {
	// 获取host
	$("#base_url").val(window.location.host);
	// 基础信息验证规则
	$('#form_1').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			'scientificname' : {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 2,
						max : 80
					},
				}
			},
			'chname' : {
				validators : {
					stringLength : {
						min : 0,
						max : 80
					},
				}
			},
			'authorstr' : {
				validators : {
					stringLength : {
						min : 0,
						max : 80
					},
				}
			},
			'epithet' : {
				validators : {
					stringLength : {
						min : 0,
						max : 80
					},
				}
			},
			'nomencode' : {
				validators : {
					stringLength : {
						min : 0,
						max : 50
					},
				}
			},
			'rank.id' : {
				validators : {
					notEmpty : {}
				}
			},
			'sourcesid' : {
				validators : {
					notEmpty : {}
				}
			},
			'tci' : {
				validators : {
					stringLength : {
						min : 0,
						max : 100
					},
				}
			},
			'taxaset.id' : {
				validators : {
					notEmpty : {}
				}
			},
			'remark' : {
				validators : {
					stringLength : {
						min : 0,
						max : 1000
					},
				}
			},
		}
	});
	// 引证验证规则
	$('#form_2').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
		// 'description1': {
		// validators: {
		// notEmpty: {
		// },
		// stringLength: {
		// min: 2,
		// max: 80
		// },
		// }
		// },
		}
	});
	// 描述验证规则
	$('#form_3').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
		// 'description1': {
		// validators: {
		// notEmpty: {
		// },
		// stringLength: {
		// min: 2,
		// max: 80
		// },
		// }
		// },
		}
	});
	$('#form_4').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
		// 'molecular1': {
		// validators: {
		// notEmpty: {},
		// stringLength: {
		// min: 2,
		// max: 80
		// },
		// }
		// },
		}
	});
	$('#form_5').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
		// 'commonname1': {
		// validators: {
		// notEmpty: {},
		// stringLength: {
		// min: 2,
		// max: 80
		// },
		// }
		// },
		}
	});
	$('#form_6').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
		// 'occurrence2': {
		// validators: {
		// notEmpty: {},
		// stringLength: {
		// min: 2,
		// max: 80
		// },
		// }
		// },
		}
	});
	$('#form_7').bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
		// 'multimedia1': {
		// validators: {
		// notEmpty: {},
		// stringLength: {
		// min: 2,
		// max: 80
		// },
		// }
		// },
		}
	});
});

// 验证状态变更
function changeVerificationTab(tabNum, status) {
	this['verificationTab' + tabNum] = status;
}

// 统一验证form
function formValidator(formNum) {
	$('#form_' + formNum).data('bootstrapValidator').validate();
	var canPass = false;
	// 是否通过校验
	if (!$('#form_' + formNum).data('bootstrapValidator').isValid()) {
		// 没有通过校验
	} else {
		// 处理参考文献
		switch (formNum) {
		case 1:
			if ($("tr[id^='referencesForm_']").length <= 0)
				canPass = true;// 通过校验
			else
				canPass = referencesValidator('newReferences', 1);
			break;
		case 2:
			canPass = submitAllCitation();
			break;
		case 3:
			canPass = submitAllDescription();
			break;
		case 4:
			canPass = (submitAllDistributiondata() && submitAllProtection()
					&& submitAllTaxkey() && submitAllTraitdata());
			break;
		case 5:
			canPass = (submitAllCommonname());
			break;
		case 6:
			canPass = (submitAllOccurrence());
			break;
		case 7:
			canPass = (submitAllMultimedia());
			break;
		default:
			canPass = true;
		}
	}
	if (canPass) {
		changeVerificationTab(formNum, 1);
		completeStep(formNum);
		return true;// 通过校验
	} else {
		changeVerificationTab(formNum, -1);
		incompleteStep(formNum);
		return false;// 没有通过校验
	}
}

// 构造一个ref验证规则
function addReferencesValidator(formId, refId, references, referencesPageS, referencesPageE) {
	$("#" + formId).bootstrapValidator("addField", references + refId, {
		validators : {
			notEmpty : {}
		}
	});
	$("#" + formId).bootstrapValidator("addField", referencesPageS + refId, {
		validators : {
			stringLength : {
				min : 0,
				max : 20
			},
			notEmpty : {}
		}
	});
	$("#" + formId).bootstrapValidator("addField", referencesPageE + refId, {
		validators : {
			stringLength : {
				min : 0,
				max : 20
			},
			notEmpty : {}
		}
	});
	
}

// 统一验证ref
function referencesValidator(formId, formNum) {
	$('#' + formId).data('bootstrapValidator').validate();
	// 是否通过校验
	if (!$('#' + formId).data('bootstrapValidator').isValid()) {
		changeVerificationTab(formNum, -1);
		incompleteStep(formNum);
		return false;// 没有通过校验
	} else {
		changeVerificationTab(formNum, 1);
		completeStep(formNum);
		return true;// 通过校验
	}
}

// 构造一个description验证规则
function addDescriptionValidator(descriptionNum) {
	$("#descriptionForm_" + descriptionNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"destitle_" + descriptionNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 100
					},
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"destypeid_" + descriptionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"desdate_" + descriptionNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 50
					},
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"describer_" + descriptionNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 50
					},
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"rightsholder_" + descriptionNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 100
					},
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"descontent_" + descriptionNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
					// max: 100
					},
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"language_" + descriptionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"licenseid_" + descriptionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#descriptionForm_" + descriptionNum).bootstrapValidator("addField",
			"descriptionremark_" + descriptionNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 500
					},
				}
			});
}
// 统一验证description
function descriptionFormValidator(Num) {
	$('#descriptionForm_' + Num).data('bootstrapValidator').validate();
}

// 构造一个distributiondata验证规则
function addDistributiondataValidator(distributiondataNum) {
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"distitle_" + distributiondataNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 100
					},
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"distypeid_" + distributiondataNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"disdate_" + distributiondataNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 50
					},
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"discriber_" + distributiondataNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 50
					},
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"disrightsholder_" + distributiondataNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 100
					},
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"discontent_" + distributiondataNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
					},
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"dislanguage_" + distributiondataNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"dislicenseid_" + distributiondataNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator("addField",
			"disremark_" + distributiondataNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 500
					},
				}
			});
	
	/*$("#distributiondataForm_" + distributiondataNum).bootstrapValidator(
			"addField", 
			"geojson_" + distributiondataNum, {
				validators : {
					notEmpty : {}
				}
			});*/
	$("#distributiondataForm_" + distributiondataNum).bootstrapValidator(
			"addField", 
			"distributiondatasourcesid_" + distributiondataNum, {
				validators : {
					notEmpty : {}
				}
			});
}
// 统一验证distributiondata
function distributiondataFormValidator(Num) {
	$('#distributiondataForm_' + Num).data('bootstrapValidator').validate();
}

// 构造一个citation验证规则
function addCitationValidator(citationNum) {
	$("#citationForm_" + citationNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#citationForm_" + citationNum).bootstrapValidator("addField",
			"sciname_" + citationNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 100
					},
				}
			});
	$("#citationForm_" + citationNum).bootstrapValidator("addField",
			"authorship_" + citationNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 100
					},
				}
			});
	$("#citationForm_" + citationNum).bootstrapValidator("addField",
			"nametype_" + citationNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#citationForm_" + citationNum).bootstrapValidator("addField",
			"citationSourcesid_" + citationNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#citationForm_" + citationNum).bootstrapValidator("addField",
			"citationstr_" + citationNum, {
				validators : {
					stringLength : {
						min : 1,
						max : 500
					},
				}
			});
}
// 统一验证citation
function citationFormValidator(Num) {
	$('#citationForm_' + Num).data('bootstrapValidator').validate();
}

// 构造一个protection验证规则
function addProtectionValidator(protectionNum) {
	$("#protectionForm_" + protectionNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#protectionForm_" + protectionNum).bootstrapValidator("addField",
			"standardname_" + protectionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#protectionForm_" + protectionNum).bootstrapValidator("addField",
			"version_" + protectionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#protectionForm_" + protectionNum).bootstrapValidator("addField",
			"protlevel_" + protectionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#protectionForm_" + protectionNum).bootstrapValidator("addField",
			"protectionsourcesid_" + protectionNum, {
				validators : {
					notEmpty : {}
				}
			});
	$("#protectionForm_" + protectionNum).bootstrapValidator("addField",
			"proassessment_" + protectionNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 1500
					},
				}
			});
}
// 统一验证protection
function protectionFormValidator(Num) {
	$('#protectionForm_' + Num).data('bootstrapValidator').validate();
}
// 构造一个taxkey验证规则
function addTaxkeyValidator(taxkeyNum) {
	var keytype = $("#keytype_" + taxkeyNum).val();
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"keytypes_" + taxkeyNum, {
				validators : {
					notEmpty : {},
				},
			});
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"orderid_" + taxkeyNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 5
					},
					regexp : {
						regexp : /^[0-9]*$/,
						message : '输入不合法'
					},
				},
			});
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"branchid_" + taxkeyNum, {
				validators : {
					stringLength : {
						min : 1,
						max : 5
					},
					regexp : {
						regexp : /^[0-9]*$/,
						message : '输入不合法'
					},
				},
			});
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"taxonid_" + taxkeyNum,{
				validators : {
					callback : {
						message : 'This value is not valid',
						callback : function(value, validator) {
							var keytype = $("#keytype_" + taxkeyNum).val();
							if (keytype == 1) {
								var branch = $("#branchid_" + taxkeyNum).val();
								if (null == branch || "" == branch) {
									$("#taxonid_" + taxkeyNum).prop("disabled", false);
								}else {
									layer.msg("双向式中分支序号与分类单元不能同时填写", {time: 1500});
									$("#taxonid_" + taxkeyNum).prop("disabled", true);
									$("#taxonid_" + taxkeyNum).empty();
								}
							}
							return true;
						}
					},
				},
			});

	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"item_" + taxkeyNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 500
					},
				},
			});
	if (1 == keytype) {
		$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
				"innerorder_" + taxkeyNum, {
					validators : {
						notEmpty : {},
						stringLength : {
							min : 1,
							max : 5
						},
						regexp : {
							regexp : /^[0-9]*$/,
							message : '输入不合法'
						},
					},
				});
	} else if (2 == keytype) {

	}
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"keytitle_" + taxkeyNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 100
					},
				},
			});
	$("#taxkeyForm_" + taxkeyNum).bootstrapValidator("addField",
			"abstraction_" + taxkeyNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 1500
					},
				},
			});
	
}
// 统一验证taxkey
function taxkeyFormValidator(Num) {
	$('#taxkeyForm_' + Num).data('bootstrapValidator').validate();
}

// 构造一个traitdata验证规则
function addTraitdataValidator(traitdataNum) {
	$("#traitdataForm_" + traitdataNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#traitdataForm_" + traitdataNum).bootstrapValidator("addField",
			"trainsetid_" + traitdataNum,{
				validators : {
					notEmpty : {},
					callback : {
						message : 'This value is not valid',
						callback : function(value, validator) {
							$.ajax({
								url : '/console/traitontology/rest/sel',
								data : {
									"traitsetSel" : $(
											"#trainsetid_" + traitdataNum).select2('data')[0].id
								},
								cache : false,
								dataType : "json",
								success : function(data) {
									if (data) {
										validator.updateStatus('trainsetid_'
												+ traitdataNum, 'VALID',
												'callback');
										return true;
									} else {
										return false;
									}
								},
								error : function() {
									return false;
								}
							});
							return false;
						}
					},
				},
			});
}
// 统一验证traitdata
function traitdataFormValidator(Num) {
	$('#traitdataForm_' + Num).data('bootstrapValidator').validate();
}

// 构造一个TraitdataValue验证规则
function addTraitdataValueValidator(formId, traitId, traitontology, traitdataProperty, traitdataValue) {
	$("#" + formId).bootstrapValidator("addField", 
		traitontology + traitId, {
			validators : {
				notEmpty : {}
			}
		});
	
	$("#" + formId).bootstrapValidator("addField", 
		traitdataValue + traitId, {
			validators : {
				notEmpty : {},
				stringLength : {
					min : 1,
					max : 500
				},
			}
		});
	
	$("#" + formId).bootstrapValidator("addField", 
			traitdataProperty + traitId, {
				validators : {
					notEmpty : {},
				}
			});
	
	/*$("#" + formId).bootstrapValidator("addField", 
			traitdataBase + traitId, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 200
					},
				}
			});*/
}

// 统一验证TraitdataValue
function traitdataValidator(formId, formNum) {
	$('#' + formId).data('bootstrapValidator').validate();
	// 是否通过校验
	if (!$('#' + formId).data('bootstrapValidator').isValid()) {
		changeVerificationTab(formNum, -1);
		incompleteStep(formNum);
		return false;// 没有通过校验
	} else {
		changeVerificationTab(formNum, 1);
		completeStep(formNum);
		return true;// 通过校验
	}
}

// 构造一个Commonname验证规则
function addCommonnameValidator(commonnameNum) {
	$("#commonnameForm_" + commonnameNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#commonnameForm_" + commonnameNum).bootstrapValidator("addField",
			"commonname_" + commonnameNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 200
					},
				}
			});
	$("#commonnameForm_" + commonnameNum).bootstrapValidator("addField",
			"commonnameLanguage_" + commonnameNum, {
				validators : {
					notEmpty : {},
				}
			});
	$("#commonnameForm_" + commonnameNum).bootstrapValidator("addField",
			"commonnameSourcesid_" + commonnameNum, {
				validators : {
					notEmpty : {}
				}
			});
}
// 统一验证Commonname
function commonnameFormValidator(Num) {
	$('#commonnameForm_' + Num).data('bootstrapValidator').validate();
}
// 构造一个Occurrence验证规则
function addOccurrenceValidator(occurrenceNum) {
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	$("#occurrenceForm_" + occurrenceNum)
			.bootstrapValidator(
					"addField",
					"eventdate_" + occurrenceNum,
					{
						validators : {
							regexp : {
								regexp : /^((?:19|20)\d\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/,
								/*regexp : /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/,*/
							}
						}
					});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_type_" + occurrenceNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 50
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_country_" + occurrenceNum, {
				validators : {
					notEmpty : {},
					stringLength : {
						min : 1,
						max : 200
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_province_" + occurrenceNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 200
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_city_" + occurrenceNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 200
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_county_" + occurrenceNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 200
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_locality_" + occurrenceNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 200
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_location_" + occurrenceNum, {
				validators : {
					stringLength : {
						min : 0,
						max : 200
					},
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_lng_" + occurrenceNum, {
				validators : {
					regexp : {
						regexp : /^-?\d+\.?\d*$/,
						message : '请输入十进制小数'
					}
				}
			});
	$("#occurrenceForm_" + occurrenceNum).bootstrapValidator("addField",
			"occurrence_lat_" + occurrenceNum, {
				validators : {
					regexp : {
						regexp : /^-?\d+\.?\d*$/,
						message : '请输入十进制小数'
					}
				}
			});

}
// 统一验证Occurrence
function occurrenceFormValidator(Num) {
	$('#occurrenceForm_' + Num).data('bootstrapValidator').validate();
}

// 构造一个Multimedia验证规则
function addMultimediaValidator(multimediaNum) {
	$("#multimediaForm_" + multimediaNum).bootstrapValidator({
		excluded : [ ":disabled" ],// 只对于禁用域不进行验证，其他的表单元素都要验证
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon ',
			invalid : 'glyphicon ',
			validating : 'glyphicon glyphicon-refresh'
		}
	});
	
	$("#multimediaForm_" + multimediaNum).bootstrapValidator("addField",
			"media_type_" + multimediaNum, {
				validators : {
					notEmpty : {},
				}
			});
	
	$("#multimediaForm_" + multimediaNum).bootstrapValidator("addField",
			"multimedia_licenseid_" + multimediaNum, {
				validators : {
					notEmpty : {}
				}
			});
	
	$("#multimediaForm_" + multimediaNum).bootstrapValidator("addField",
			"multimedia_lng_" + multimediaNum, {
				validators : {
					regexp : {
						regexp : /^-?\d+\.?\d*$/,
						message : '请输入十进制小数'
					}
				}
			});
	$("#multimediaForm_" + multimediaNum).bootstrapValidator("addField",
			"multimedia_lat_" + multimediaNum, {
				validators : {
					regexp : {
						regexp : /^-?\d+\.?\d*$/,
						message : '请输入十进制小数'
					}
				}
			});

}
// 统一验证Multimedia
function multimediaFormValidator(Num) {
	$('#multimediaForm_' + Num).data('bootstrapValidator').validate();
}