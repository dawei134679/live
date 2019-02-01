<%--
  Created by IntelliJ IDEA.
  User: fangwuqing
  Date: 16/5/6
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>运营审核列表</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;

	$(function() {
		
		dataGrid = $("#cashlist").datagrid(
				{
					url : '../../anchor/getAnchorCashOfOperate',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '运营审核列表',
					queryParams: {
						uid : $('#uid').textbox("getValue"),
						times : $("#times").combobox('getValue'),
						unionid : $("#unionid").combobox('getValue')
					},
					showFooter : true,
					pagination : true,
			        rownumbers: true,
					striped : true,
			        singleSelect: true,
			        fit:true,
			        fitColumns:true,
			        pageSize:20,
			        pageList:[20,50,100],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'times',
								title : '日期',
								align : 'center',
								width : 30,
				            	hidden:true,
								sortable : false,
								formatter:function(data){
									if(data == 5){
										return "上月底";
									}else if(data == 15){
										return "本月初";
									}else if(data == 25){
										return "本月中";
									}
								}
							},
							{
								field : 'unionid',
								title : '公会ID',
								align : 'center',
								width : 50,
				            	hidden:true,
								sortable : false
							},
							{
								field : 'uname',
								title : '公会名称',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'adminname',
								title : '归属人',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'uid',
								title : '主播UID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'nickname',
								title : '主播昵称',
								align : 'center',
								width : 70,
								sortable : false
							},
							{
								field : 'credits',
								title : '声援值提现',
								align : 'center',
								width : 70,
								sortable : false
							},
							{
								field : 'rate',
								title : '分成比率',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'amount',
								title : '礼物提成',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'money',
								title : '提成金额(元)',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'status',
								title : '操作',
								align : 'center',
								width : 120,
								sortable : false,
								formatter:function(data){
									if(data == 0){
										return "待艺管审核";
									}else if(data == 1){
										return "<a href='javascript:verify(1);'>通过</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:verify(0);'>拒绝</a>";
									}else if(data == 2){
										return "运营审核驳回";
									}else if(data == 3){
										return "财务审核通过";
									}else if(data == 4){
										return "再通过<被驳回>";
									}else{
										return "<font color='red'>异常</font>";
									}
								}
							}
							] ]
				})
	})
	
	function verify(type){
		var row = $('#cashlist').datagrid('getSelected');
		if (row){
			var uid = row.uid;
			var times = row.times;
			
			var msg = "";
			if(type == 1){
				msg = "确认通过兑换审核吗？";
			}else{
				msg = "确认拒绝兑换审核吗？";
			}
            $.messager.confirm('确认',msg,function(r){
                if (r){
                    $.post('../../anchor/verifyByOperate',{times:times,uid:uid,type:type},function(result){
                        if (result.success == 200){
                            $('#cashlist').datagrid('reload');    // reload the user data
                        } else {
                            $.messager.show({    // show error message
                                title: 'Error',
                                msg: result.msg
                            });
                        }
                    },'json');
                }
            });
        }else{
        	$.messager.show({    // show error message
                title: 'Error',
                msg: "请选择一行"
            });
        }
	}
	
	function searchCash() {
		$('#cashlist').datagrid('load', {
			uid : $('#uid').textbox("getValue"),
			times : $("#times").combobox('getValue'),
			unionid : $("#unionid").combobox('getValue')
		});
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="cashlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				用户UID:&nbsp;<input id="uid" name="uid" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
				&nbsp;&nbsp;选择公会:&nbsp;<input id="unionid" name="unionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>
				时段:&nbsp;<select id="times" class="easyui-combobox" name="times" panelHeight="auto" style="width: 145px">
								<option value="" selected="selected">全部</option>
								<option value="15">15号</option>
								<option value="25">25号</option>
								<option value="5">次月5号</option>
				</select>
				&nbsp;&nbsp;<a href="javascript:searchCash()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
</body>
</html>