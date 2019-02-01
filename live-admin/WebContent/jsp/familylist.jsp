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
<title>公会列表</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {

		dataGrid = $("#familyList").datagrid(
				{
					url : '../UnionGetList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '公会列表',
					pagination : true,
					idField : "credit",
					sortName : 'credit',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'unionid',
								title : '公会id',
								align : 'center',
								sortable : false
							},
							{
								field : 'unionname',
								title : '公会名称',
								align : 'center',
								sortable : false
							},
							{
								field : 'ownerid',
								title : '会长ID',
								align : 'center',
								sortable : false
							},
							{
								field : 'ownername',
								title : '会长',
								align : 'center',
								sortable : false
							},
							{
								field : 'adminname',
								title : '归属人',
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorcount',
								title : '主播数量',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUnionAnchors();" class="easyui-tooltip" title="查看公会主播">'+data+'</a>';
								}
							},
							{
								field : 'credit',
								title : '声援值',
								align : 'center',
								sortable : true
							},
							{
								field : 'totalmoney',
								title : '房间总消耗',
								align : 'center',
								sortable : true
							},
							{
								field : 'profit',
								title : '已提现',
								align : 'center',
								sortable : true
							},
							{
								field : 'desc',
								title : '说明',
								align : 'left',
								sortable : false
							},
							{
								field : 'createtime',
								title : '添加时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'status',
								title : '家族状态',
								align : 'left',
								sortable : false,
								formatter : function(data){
									var view = "";
									if(data == 1){
										view = "启用";
									}else if(data == 0){
										view = "禁用";
									}
									return view;
								}
							},
							{
								field : 'operatorname',
								title : '操作人',
								align : 'left',
								sortable : false
							}
							] ]
				})
	})

	function queryUnion() {
		$("#familyList").datagrid('reload', {
			content : $("#content").textbox('getValue'),
			condition : $("#condition").combobox('getValue'),
			searchstatus : $("#searchstatus").combobox('getValue'),
		})
	}

	function clearQuery() {
		$("#content").textbox('reset');
		$("#condition").combobox('reset');
		$("#searchstatus").combobox('reset');

	}
	
	function newUnion(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','创建公会');
        $('#fm').form('clear');
        url = '../unionAdd';
    } 
	
	function editUnion(){
		var row = $('#familyList').datagrid('getSelected');
        if (row){
            $('#dlg').dialog('open').dialog('center').dialog('setTitle','编辑公会');
            $('#fm').form('clear');
            $('#fm').form('load',row);
            url = '../unionEdit?unionid='+row.unionid;
        }
	}
	
	function delUnion(){
		var row = $('#familyList').datagrid('getSelected');
        if (row){
            $.messager.confirm('确认','确定删除此公会?',function(r){
                if (r){
                    $.post('unionDel',{unionid:row.unionid},function(result){
                        if (result.success){
                            $('#familyList').datagrid('reload');    // reload the user data
                        } else {
                            $.messager.show({    // show error message
                                title: 'Error',
                                msg: result.errorMsg
                            });
                        }
                    },'json');
                }
            });
        }
	}
	
	function banOrUnbanUnion(){
		var row = $('#familyList').datagrid('getSelected');
		var msg = "";
        if (row){
        	if(row.status == 1){
        		$.messager.confirm('确认','确定禁用此公会?',function(r){
                    if (r){
                        $.post('../unionBan',{unionid:row.unionid, status:0},function(result){
                            if (result.success){
                                $('#familyList').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.show({    // show error message
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            }
                        },'json');
                    }
                });
        	}else{
        		$.messager.confirm('确认','确定启用此公会?',function(r){
                    if (r){
                        $.post('../unionBan',{unionid:row.unionid, status:1},function(result){
                            if (result.success){
                                $('#familyList').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.show({    // show error message
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            }
                        },'json');
                    }
                });
        	}
            
        }
	}
	
	function saveUnion(){
        $('#fm').form('submit',{
            url: url,
            onSubmit: function(){
                return $(this).form('validate');
            },
            success: function(result){
                var result = eval('('+result+')');
                if (result.errorMsg){
                    $.messager.show({
                        title: 'Error',
                        msg: result.errorMsg
                    });
                } else {
                    $('#dlg').dialog('close');        
                    $('#familyList').datagrid('reload');
                }
            }
        });
    }
	
	function saveAnchor(){
		$('#fmAnchor').form('submit',{
            url: url+'?unionid='+unionid,
            onSubmit: function(){
                return $(this).form('validate');
            },
            success: function(result){
                var result = eval('('+result+')');
                if (result.fail){
                    $.messager.show({
                        title: 'Error',
                        msg: result.errorMsg
                    });
                } else {
                    $('#dlgAddAnchor').dialog('close');        
                    $('#anchorList').datagrid('reload');
                    $('#familyList').datagrid('reload');
                }
            }
        });
	}
	
	function addAnchor(){
		$('#dlgAddAnchor').dialog('open').dialog('center').dialog('setTitle','添加主播');
        $('#fmAnchor').form('clear');
        url = '../unionAddAnchor';
	}
	
	function delAnchor(){
		url = '../unionDeleteAnchor';
		var row = $('#anchorList').datagrid('getSelected');
        if (row){
            $.messager.confirm('确认','确定从工会移除该主播?',function(r){
                if (r){
                    $.post(url,{unionid:unionid,uid:row.uid},function(result){
                        if (result.success){
                        	$('#anchorList').datagrid('reload');
                            $('#familyList').datagrid('reload');
                        } else {
                            $.messager.show({    // show error message
                                title: 'Error',
                                msg: result.errorMsg
                            });
                        }
                    },'json');
                }
            });
        }
	}
	
	function queryAnchor(){
		$("#anchorList").datagrid('reload', {
			startDate : $("#liveStartDate").textbox('getValue'),
			endDate : $("#liveEndDate").textbox('getValue')
		})
	}
	
	function getLiveDetail(){
		var row = $('#anchorList').datagrid('getSelected');
		$('#dlgLiveTimeDetail').dialog('open').dialog('center').dialog('setTitle','直播详情');
		$('#liveTimeDetail').datagrid(
				{
					url : '../anchorLiveDetail',
					width : getWidth(0.97),
					height : getHeight(0.97),
					pagination : false,
					rownumbers : true,
					singleSelect : true,
					queryParams :{
						uid : row.uid,
						startDate : $("#liveStartDate").textbox('getValue'),
						endDate : $("#liveEndDate").textbox('getValue')
					},
					columns : [ [
							{
								field : 'liveStartTime',
								title : '开始时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'liveEndTime',
								title : '结束时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'liveSec',
								title : '直播时长',
								align : 'center',
								sortable : true ,
								formatter : function(data) {
									var hour = parseInt(data/3600);
									var minute = parseInt(data%3600/60);
									if(hour > 0){
										data = hour+"小时"+minute+"分钟";
									}else{
										data = minute+"分钟";
									}
									return data;
								}
							}] ]
				});
	}
	
	function getUnionAnchors(){
		$("#liveStartDate").textbox('reset');
		$("#liveEndDate").textbox('reset');
		var row = $('#familyList').datagrid('getSelected');
		unionid = row.unionid;
		$('#dlgAnchorList').dialog('open').dialog('center').dialog('setTitle',row.unionname+'公会的主播');
		dataGrid = $("#anchorList").datagrid(
				{
					url : '../unionGetAnchors?unionid='+unionid,
					width : getWidth(0.97),
					height : getHeight(0.97),
					pagination : false,
					idField : "anchorLevel",
					sortName : 'anchorLevel',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					queryParams :{
						startDate : '',
						endDate : ''
					},
					toolbar : '#tbAnchor',
					columns : [ [
							{
								field : 'uid',
								title : '主播id',
								align : 'center',
								sortable : false
							},
							{
								field : 'nickname',
								title : '主播昵称',
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorLevel',
								title : '主播等级',
								align : 'center',
								sortable : true
							},
							{
								field : 'userLevel',
								title : '用户等级',
								align : 'center',
								sortable : true
							},
							{
								field : 'validLiveDay',
								title : '有效直播天数',
								align : 'center',
								sortable : false
							},
							{
								field : 'totalLiveTime',
								title : '直播时长',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									var hour = parseInt(data/3600);
									var minute = parseInt(data%3600/60);
									if(hour > 0){
										data = hour+"小时"+minute+"分钟";
									}else{
										data = minute+"分钟";
									}
									return '<a href="javascript:getLiveDetail();" class="easyui-tooltip" title="直播详情">'+data+'</a>';
								}
							},
							{
								field : 'profit',
								title : '充值金额',
								align : 'center',
								sortable : true
							},
							{
								field : 'registtime',
								title : '注册时间',
								align : 'left',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'creditTotal',
								title : '累计声援',
								align : 'center',
								sortable : false
							},
							{
								field : 'credit',
								title : '持有声援',
								align : 'center',
								sortable : false
							},
							{
								field : 'phone',
								title : '绑定手机',
								align : 'center',
								sortable : false
							}] ]
				})
	}
	
	function closeDlg(){
		$('#dlg').dialog('close');
	}
	
	function closeDlgAddAnchor(){
		$('#dlgAddAnchor').dialog('close');
	}
</script>
</head>
<body>
	<div>
		<table id="familyList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:editUnion()"
					class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
				<a
					href="javascript:newUnion()" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加</a>
				<a
					href="javascript:delUnion()" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">删除</a>
				<a
					href="javascript:banOrUnbanUnion()" class="easyui-linkbutton"
					iconCls="icon-cancel" plain="true">禁用/启用</a>
			</div>
			<div>
				<select id="searchstatus" class="easyui-combobox" name="searchstatus"
					style="width:90px;">
					<option value="">公会状态</option>
					<option value="1">启用</option>
					<option value="0">禁用</option>
				</select>
				<select id="condition" class="easyui-combobox" name="condition"
					style="width:80px;">
					<option value="1">公会id</option>
					<option value="2">公会名称</option>
					<option value="3">会长id</option>
				</select><input class="easyui-textbox" style="width: 150px" id="content">
				<a href="javascript:queryUnion()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>


		<div id="dlg" class="easyui-dialog"
			style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>会长uid：</td>
						<td><input type="text" name="ownerid" id="ownerid"
							class="easyui-textbox" required="true"
							data-options="disabled:false" /></td>
						<td>公会名称：</td>
						<td><input type="text" name="unionname" id="unionname"
							class="easyui-textbox" required="true"
							data-options="disabled:false" />
						</td>
					</tr>
					<tr>
						<td>归属人:</td>
						<td>
							<input id="adminuid" name="adminuid" class="easyui-combobox" required="true"
							data-options="valueField: 'uid',
    									textField: 'username',
    									url: '../manageuser?method=getUserNameList'"
    						/>
						</td>
					</tr>
					<tr>
						<td>工会说明:</td>
						<td>
							<input class="easyui-textbox" name="desc" data-options="multiline:true" style="height:60px" />
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveUnion()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeDlg()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
		
	</div>
	<div id="dlgAnchorList" class="easyui-dialog"
			style="width:80%;height:80%" closed="true" maximizable="true"
			>
		<div id="tbAnchor" style="padding: 2px 5px; height: auto">
			<div>
				<a
					href="javascript:addAnchor();" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加主播</a>
				<a
					href="javascript:delAnchor();" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">删除主播</a>
			</div>
			<div>
			<label>开始时间</label>
			<input class="easyui-datebox" id="liveStartDate" style="width:150px;height:24px">
			<label>结束时间</label>
			<input class="easyui-datebox" id="liveEndDate" style="width:150px;height:24px">
			<a href="javascript:queryAnchor();" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>
			</div>
		</div>
		<div id="dlgAddAnchor" class="easyui-dialog"
			style="width: 400px; height: 200px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons-add-anchor">
			<form id="fmAnchor" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>主播uid：</td>
						<td><input type="text" name="uid" id="ownerid"
							class="easyui-validatebox" required="true"
							data-options="disabled:false" /></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons-add-anchor">
			<a href="javascript:saveAnchor()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeDlgAddAnchor()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
		<table id="anchorList"></table>
		
	</div>
	<div id="dlgLiveTimeDetail" class="easyui-dialog"
			style="width:50%;height:100%" closed="true" maximizable="true"
			>
			<table id="liveTimeDetail"></table>
	</div>
</body>
</html>
