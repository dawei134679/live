/**
 * 表格相关JS
 * 
 */
function seltab(id) {
	// ejiaA1("名称","奇数行背景","偶数行背景","鼠标经过背景","点击后背景");
	ejiaA1(id, "#FFF8DC", "#F5F5F5", "#e5f1fd", "#CCC");
}
function ejiaA1(o, a, b, c, d) {
	var t = document.getElementById(o).getElementsByTagName("tr");
	for ( var i = 1; i < t.length; i++) {
		t[i].style.backgroundColor = (t[i].sectionRowIndex % 2 == 0) ? a : b;
		t[i].onclick = function() {
			if (window.cur) {
				this.x = "0";
				window.cur.style.background = (this.sectionRowIndex % 2 == 0) ? b
						: a;
			}
			this.style.background = d;
			this.x = "1";
			window.cur = this;
			$("#_selected").val(this.id);
		}
		t[i].onmouseover = function() {
			if (this.x != "1")
				this.style.backgroundColor = c;
		}
		t[i].onmouseout = function() {
			if (this.x != "1")
				this.style.backgroundColor = (this.sectionRowIndex % 2 == 0) ? a : b;
		}
	}
}
