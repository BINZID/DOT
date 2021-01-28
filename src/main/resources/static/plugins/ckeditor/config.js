/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'colors' },
		{ name: 'styles' },
		{ name: 'tools' },
		{ name: 'about' }
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';
	
	config.removePlugins = 'elementspath';
	
	config.width = "1305"; //文本域宽度
	config.height = "300";//文本域高度
	
	//工具栏是否可以被收缩
    config.toolbarCanCollapse = false;
    //工具栏默认是否展开
    config.toolbarStartupExpanded = false;
    
    //设置快捷键
    config.keystrokes = [
       [ CKEDITOR.SHIFT + 121 /*F10*/,'contextMenu' ],  //文本菜单

       [ CKEDITOR.CTRL + 90 /*Z*/, 'undo' ], //撤销
       [ CKEDITOR.CTRL + 89 /*Y*/, 'redo' ],  //重做
    ]
    
    //是否使用完整的html编辑模式如使用，其源码将包含：<html><body></body></html>等标签
    config.fullPage = false;
    
    // 界面语言，默认为 'en'
    config.language = 'zh-cn';
    
};
