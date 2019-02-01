var _pageCount=0;
function queryPageComp(_UrlPath,_jsonData,_tbodyId,_cell){
	proceSeq();
	var _pageJson = {"pageCurrent":$("#_pageCurrent").val(),"pageSize":$("#_pageSize").val()};
	var _fiPageJson = mergeJson(_pageJson,_jsonData);
	$.ajax({
         url:_UrlPath,
         type:"post",
		 dataType:"json",
		 data:_fiPageJson,
		 contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		 beforeSend:function(){ 
		 				closeAllButto();
					},
		 error:function(){
		 				openAllButto();
		 			},
		 success:function(msg){
		 			if(msg!=null && msg.TOTALNUM >= 0){
		 				$("#" + _tbodyId).show();
		 				//openAllButto();
		 				compositeData(msg,_tbodyId,_cell);
		 			}else{
		 				$("#" + _tbodyId).hide();
		 			}
				 }
   });
}
//当前页码大于总页数的时候执行此操作
function arrWritePage(pnum){
	if(pnum > 1){
		if(pnum*1>($("#_totalPage").val())*1){
			pnum=$("#_totalPage").val();
			$("#_pageCurrent").val(pnum);
			query();
		}
	}
}
function mergeJson(fir, sec){
	var finObj = {};
    if(fir!=null){
    	for(var attr in fir){
    		finObj[attr] = fir[attr];
    	}
    }
    if(sec!=null){
    	for(var attr in sec){
    		finObj[attr] = sec[attr];
    	}
    }
    return finObj;
}

function compositeData(msg,tbodyid,cellFun){
	$("#_pageCurrent").val(msg.PAGECURRENT);
	DWRUtil.setValue("_totalPage",msg.TOTALPAGE);
	$("#_totalPage").val(msg.TOTALPAGE);
	$("#_pageSize").val(msg.PAGESIZE);
	$("#_totalCount").val(msg.TOTALNUM);
	$("#_totalCount_view").html(msg.TOTALNUM);
	$("#totalRecord").val(msg.TOTALNUM);
	var sta = msg.PAGESIZE * (msg.PAGECURRENT - 1)+1;
	var end = msg.PAGESIZE * msg.PAGECURRENT;
	if(end >msg.TOTALNUM){
		end = msg.TOTALNUM;
	}
	$("#_page_view_se").html(sta+" ~ "+end);
	processPageButt();
	processDataList(msg.DATALIST,tbodyid,cellFun);
	arrWritePage(msg.PAGECURRENT);
}

function processPageButt(){
	var curPage = $("#_pageCurrent").val()*1;
	var totalPage = $("#_totalPage").val()*1;
	var pageNum = $("#_pageSize").val()*1;
	var totalNum = $("#_totalCount").val()*1;
	if(curPage==1){
		$("#first_pag").addClass("ui-state-disabled");
		$("#prev_pag").addClass("ui-state-disabled");
		if(curPage>=totalPage){
			$("#next_pag").addClass("ui-state-disabled");
			$("#last_pag").addClass("ui-state-disabled");
		}else{
			$("#next_pag").removeClass("ui-state-disabled");
			$("#last_pag").removeClass("ui-state-disabled");
			$("#next_pag").addClass("ui-state-open");
			$("#last_pag").addClass("ui-state-open");
		}
	}else if(curPage==totalPage){
		$("#first_pag").removeClass("ui-state-disabled");
		$("#prev_pag").removeClass("ui-state-disabled");
		$("#first_pag").addClass("ui-state-open");
		$("#prev_pag").addClass("ui-state-open");
		$("#next_pag").addClass("ui-state-disabled");
		$("#last_pag").addClass("ui-state-disabled");
	}else{
		$("#first_pag").removeClass("ui-state-disabled");
		$("#prev_pag").removeClass("ui-state-disabled");
		$("#next_pag").removeClass("ui-state-disabled");
		$("#last_pag").removeClass("ui-state-disabled");
		$("#first_pag").addClass("ui-state-open");
		$("#prev_pag").addClass("ui-state-open");
		$("#next_pag").addClass("ui-state-open");
		$("#last_pag").addClass("ui-state-open");
	}
	if(totalNum<=pageNum){
		$("#first_pag").addClass("ui-state-disabled");
		$("#prev_pag").addClass("ui-state-disabled");
		$("#next_pag").addClass("ui-state-disabled");
		$("#last_pag").addClass("ui-state-disabled");
	}
}

function processDataList(dataLi,tbodyid,cellFun){
	DWRUtil.removeAllRows(tbodyid);
	DWRUtil.addRows(tbodyid,dataLi,cellFun,{escapeHtml: false});
	this.trs = document.getElementById("_dataList").getElementsByTagName("tr");
	if(trs.length == 0){
		$("#" + tbodyid).html("<tr class='ui-widget-content jqgrow ui-row-ltr'><td colspan='"+cellFun.length+"'><p style='margin-top:5px;margin-left:0px;'>当前无查询数据</p></td></tr>");
	}
	backquery(dataLi);//回调处理
}

function toChangeNumberFirst(){
	$("#_pageCurrent").val(1);
	query();
}

function toChangeNumberFirstDynamic(){
	var temp=$("#_pageSize").val();
	
	if(parseInt(temp)>=20){
		$("#_pageSize").empty();
		//30,40,50
		temp=parseInt(temp)-parseInt(10);
		$("#_pageSize").append("<option value='"+temp+"'>"+temp+"</option>"); 
		temp=parseInt(temp)+parseInt(10);
		$("#_pageSize").append("<option selected='selected' value='"+temp+"'>"+temp+"</option>"); 
		temp=parseInt(temp)+parseInt(10);
		$("#_pageSize").append("<option value='"+temp+"'>"+temp+"</option>");
	}
	
	$("#_pageCurrent").val(1);
	query();
}


function toFirst(){
    var curPage = $("#_pageCurrent").val();
	if(curPage*1>1){
		$("#_pageCurrent").val(1);
		query();
	}

}
function toPrivious(){
	var curPage = $("#_pageCurrent").val();
	if(curPage*1>1){
		$("#_pageCurrent").val(curPage*1-1);
		query();
	}
}
function toNext(){
	var curPage = $("#_pageCurrent").val();
	var allPages = $("#_totalPage").val();
	if(allPages*1>curPage*1){
		$("#_pageCurrent").val(curPage*1+1);
		query();
	}
}
function toLast(){
	var curPage = $("#_pageCurrent").val();
	var allPages = $("#_totalPage").val();
	if(allPages*1>curPage*1){
		$("#_pageCurrent").val($("#_totalPage").val());
		query();
	}
}

function proceSeq(){
	var seqCurPage = $("#_pageCurrent").val()*1;
	var seqPageNum = $("#_pageSize").val()*1;
	_pageCount = (seqCurPage-1)*seqPageNum;
}

function initQuery(){
	_pageCount=0;
	$("#_pageCurrent").val(1);
}
function closeAllButto(){
	$("#first_pag").addClass("ui-state-disabled");
	$("#prev_pag").addClass("ui-state-disabled");
	$("#next_pag").addClass("ui-state-disabled");
	$("#last_pag").addClass("ui-state-disabled");
	$("#first_pag").removeClass("ui-state-open");
	$("#prev_pag").removeClass("ui-state-open");
	$("#next_pag").removeClass("ui-state-open");
	$("#last_pag").removeClass("ui-state-open");
}
function openAllButto(){
	$("#first_pag").removeClass("ui-state-disabled");
	$("#prev_pag").removeClass("ui-state-disabled");
	$("#next_pag").removeClass("ui-state-disabled");
	$("#last_pag").removeClass("ui-state-disabled");
	$("#first_pag").addClass("ui-state-open");
	$("#prev_pag").addClass("ui-state-open");
	$("#next_pag").addClass("ui-state-open");
	$("#last_pag").addClass("ui-state-open");
}
function backquery(dataLi){
	this.trs = document.getElementById("_dataList").getElementsByTagName("tr");
	for ( var i = 0; i < trs.length; i++) {
		trs[i].setAttribute("class", "ui-widget-content jqgrow ui-row-ltr");
		
	}
}
/**
 * 去掉时间中的T
 * @param t
 * @returns
 */
function timedelt(t){
	if(t==null||t==""){
		return "";
	}else {
		var time;
		time = t.substring(0,10)+" "+t.substring(11,19)
		return time;
	}
}
/**
 * 截取时间到日期
 * @param t
 * @returns
 */
function timeqian(t){
	if(t==null||t==""){
		return "";
	}else {
		var time;
		time = t.substring(0,10);
		return time;
	}
}
$(document).ready(function() {
	$("#_pageCurrent").keydown(function(e){ 
		var curKey = e.which; 
		if(curKey == 13){ 
			query();
			return false; 
		} 
	});
});