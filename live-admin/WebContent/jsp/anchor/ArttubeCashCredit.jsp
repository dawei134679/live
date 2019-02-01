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
<title>艺管审核兑现列表</title>
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
					url : '../../anchor/getAnchorCashOfArttube',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '播兑换列表',
					queryParams: {
						uid : $('#uid').textbox("getValue"),
						times : $("#times").combobox('getValue'),
						unionid : $("#unionid").combobox('getValue')
					},
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
								field : 'id',
								title : 'id',
								align : 'center',
								width : 30,
				            	hidden:true,
								sortable : false
							},
							{
								field : 'times',
								title : '时段',
								align : 'center',
								width : 80,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd") : "";
								}
							},
							{
								field : 'unionid',
								title : '公会ID',
								align : 'center',
								width : 50,
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
								field : 'uid',
								title : '主播UID',
								align : 'center',
								width : 70,
								sortable : false
							},
							{
								field : 'nickname',
								title : '主播昵称',
								align : 'center',
								width : 100,
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
								field : 'operatebak',
								title : '备注说明',
								align : 'center',
								width : 120,
								sortable : false
							},
							{
								field : 'addtime',
								title : '提交时间',
								align : 'center',
								width : 120,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'status',
								title : '操作',
								align : 'center',
								width : 100,
								sortable : false,
								formatter:function(data){
									if(data == 0 || data == 4){
										return "<a href='javascript:verify(1);'>通过</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:verify(0);'>拒绝</a>";
									}else if(data == 1){
										return "运营审核通过";
									}else if(data == 2){
										return "运营审核驳回";
									}else if(data == 3){
										return "财务审核通过";
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
			var id = row.id;
			var uid = row.uid;
			var unionid = row.unionid;
			var msg = "";
			if(type == 1){
				msg = "确认通过兑换审核吗？";
			}else{
				msg = "确认拒绝兑换审核吗？";
			}
            $.messager.confirm('确认',msg,function(r){
                if (r){
                    $.post('../../anchor/verifyByArttube',{id:id,uid:uid,unionid:unionid,type:type},function(result){
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
				&nbsp;&nbsp;时段:&nbsp;<select id="times" class="easyui-combobox" name="times" panelHeight="auto" style="width: 145px">
								<option value="" selected="selected">全部</option>
								<option value="15">15号</option>
								<option value="25">25号</option>
								<option value="5">次月5号</option>
				</select>
				&nbsp;&nbsp;<a href="javascript:searchCash()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a> [只能审核所负责公会的兑换单]
			</div>
		</div>
</body>
</html>
