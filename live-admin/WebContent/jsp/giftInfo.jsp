<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>消费信息</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {

		dataGrid = $("#giftinfo").datagrid(
				{
					url : '../giftInfo',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '消费信息',
					pagination : true,
					showFooter : true,
					idField : "addtime",
					sortName : 'addtime',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
					        {
								field : 'srcuid',
								title : '送礼用户id',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'srcnickname',
								title : '送礼用户昵称',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'dstuid',
								title : '接收用户id',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'dstnickname',
								title : '接收用户昵称',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'giftname',
								title : '礼物名',
								align : 'center',
								width : "100",
								sortable : false,
							},
							{
								field : 'count',
								title : '礼物数量',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'zhutou',
								title : '金币数',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'getmoney',
								title : '声援值',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'addtime',
								title : '消费时间',
								align : 'center',
								sortable : false,
								width : "150",
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							}
							] ]
				})
	})
	
	function srcUser(){
		var uid=$("#userid").val();
		if(uid==""){
			$("#userid").attr("placeholder","请输入用户id");
			return;
		}
		var year = document.reg_testdate.YYYY.value; 
		var month = document.reg_testdate.MM.value
		if(month==""){
			alert("请输入月份");
			return;
		}
		
		if(month<10&&month!=""){
			month="0"+month;
		}
		var day = document.reg_testdate.DD.value
		if(day<10&&day!=""){
			day="0"+day;
		}
		var date=year+month+day;
		$("#giftinfo").datagrid({
			url : '../giftInfo?start='+date,
			queryParams: {
				method:"src",
		        uid: uid
			}
		})
	}
	
	function dstUser(){
		var uid=$("#userid").val();
		if(uid==""){
			$("#userid").attr("placeholder","请输入用户id");
			return;
		}
		var year = document.reg_testdate.YYYY.value;  
		var month = document.reg_testdate.MM.value
		if(month==""){
			alert("请输入月份");
			return;
		}
		if(month<10&&month!=""){
			month="0"+month;
		}
		var day = document.reg_testdate.DD.value
		if(day<10&&day!=""){
			day="0"+day;
		}
		var date=year+month+day;
		$("#giftinfo").datagrid({
			url : '../giftInfo?start='+date,
			queryParams: {
				method:"dst",
		        uid: uid
			}
		})
	}
	
	function YYYYMMDDstart(){   
		MonHead = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];   
		
		//先给年下拉框赋内容   
		var y  = new Date().getFullYear();  
		for (var i = (y-30); i < (y+30); i++) //以今年为准，前30年，后30年   
			   document.reg_testdate.YYYY.options.add(new Option(""+i,i));   
		
		//赋月份的下拉框   
		for (var i = 1; i < 13; i++)
			
				document.reg_testdate.MM.options.add(new Option(""+i,i));  
			
			    
		
		document.reg_testdate.YYYY.value = y;   
		document.reg_testdate.MM.value = new Date().getMonth() + 1;   
		var n = MonHead[new Date().getMonth()];   
		if (new Date().getMonth() ==1 && IsPinYear(YYYYvalue)) n++;   
			writeDay(n); //赋日期下拉框Author:meizz   
		document.reg_testdate.DD.value = new Date().getDate();   
	}   
	if(document.attachEvent)   
		window.attachEvent("onload", YYYYMMDDstart);   
	else   
	window.addEventListener('load', YYYYMMDDstart, false);   
	function YYYYDD(str) //年发生变化时日期发生变化(主要是判断闰平年)   
	{   
		var MMvalue = document.reg_testdate.MM.options[document.reg_testdate.MM.selectedIndex].value;   
		if (MMvalue == ""){ var e = document.reg_testdate.DD; optionsClear(e); return;}   
		var n = MonHead[MMvalue - 1];   
		if (MMvalue ==2 && IsPinYear(str)) n++;   
		writeDay(n)   
	}   
	function MMDD(str)   //月发生变化时日期联动   
	{   
		var YYYYvalue = document.reg_testdate.YYYY.options[document.reg_testdate.YYYY.selectedIndex].value;   
		if (YYYYvalue == ""){ var e = document.reg_testdate.DD; optionsClear(e); return;}   
		var n = MonHead[str - 1];   
		if (str ==2 && IsPinYear(YYYYvalue)) n++;   
		writeDay(n)   
	}   
	function writeDay(n)   //据条件写日期的下拉框   
	{   
		var e = document.reg_testdate.DD; optionsClear(e);   
		for (var i=1; i<(n+1); i++)
		
				e.options.add(new Option(""+i,i));
				   
	}   
	function IsPinYear(year)//判断是否闰平年   
	{
		return(0 == year%4 && (year%100 !=0 || year%400 == 0));
	}   
	function optionsClear(e)   
	{   
		e.options.length = 1;   
	}   
</script>
</head>
<body>
	<div>
		<table id="giftinfo"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;用户ID：&nbsp;<input  type="text" name="userid" id="userid" size="10" />
				<form name="reg_testdate">
					<select id="year" name="YYYY" onChange="YYYYDD(this.value)">
						<option value=""></option>
					</select>
					<select id="month" name="MM" onChange="MMDD(this.value)">
						<option value=""></option>
					</select>
					<select id="day" name="DD">
						<option value=""></option>
					</select>
				</form>
				<table>
					<tr>
						<td>
							<input type="button" value="送礼查询" class="easyui-linkbutton"
					iconCls="icon-search" onclick="srcUser()">  
							<input type="button" value="收礼查询"  class="easyui-linkbutton"
					iconCls="icon-search"onclick="dstUser()"> 
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</div>
</body>
</html>