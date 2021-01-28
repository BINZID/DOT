// 格式化完整题录
function formatRefstr(value, row, index) {
	var values = row.refstr;
	var rsl = "";
	if (values.length<15) {
		rsl = values;
	}else{
		rsl = values.substring(0, 30) + "...";
	}
    var span=document.createElement('span');
    span.setAttribute('title',values);
    span.innerHTML = rsl; 
    return span.outerHTML; 
	} 
// 格式化作者
function formatAuthor(value, row, index) {
	var values = row.author;
	var rsl = "";
	if (values.length<6) {
		rsl = values;
	}else{
		rsl = values.substring(0, 5) + "...";
	}
    var span=document.createElement('span');
    span.setAttribute('title',values);
    span.innerHTML = rsl; 
    return span.outerHTML; 
	} 
// 格式化题目
function formatTitle(value, row, index) {
	var values = row.title;
	var rsl = "";
	if (values.length<10) {
		rsl = values;
	}else{
		rsl = values.substring(0, 9) + "...";
	}
    var span=document.createElement('span');
    span.setAttribute('title',values);
    span.innerHTML = rsl; 
    return span.outerHTML; 
	} 
// 格式化期刊/专著名
function formatJournal(value, row, index) {
	var values = row.journal;
	if (null != values) {
		var rsl = "";
		if (values.length<15) {
			rsl = values;
		}else{
			rsl = values.substring(0, 15) + "...";
		}
		var span=document.createElement('span');
		span.setAttribute('title',values);
		span.innerHTML = rsl; 
		return span.outerHTML; 
	}else {
		return null;
	}
} 