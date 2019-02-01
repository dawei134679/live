<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>会员列表</title>
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/md5.js"></script>
<script>
	var dataGrid;
	var url;
	$(function() {
		dataGrid = $("#memberList").datagrid({
			url : '../../member/getMemberList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '会员列表',
			pagination : true,
			rownumbers : true,
			striped : true,
			singleSelect : true,
			fit : true,
			fitColumns : true,
			pageSize : 20,
			pageList : [ 20, 50, 100 ],
			toolbar : '#tb',
			queryParams: {
				strategicPartnerId:'${param.strategicPartnerId}',
				extensionCenterId:'${param.extensionCenterId}',
				promotersId:'${param.promotersId}',
				agentUserId:'${param.agentUserId}',
				agentUserId:'${param.agentUserId}',
				salesmanId:'${param.salesmanId}'
			},
			columns : [ [ {
				field : 'headimage',
				title : '头像',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					return "<img src='"+data+"' width='80px' height='80px'/>";
				}
			}, {
				field : 'uid',
				title : '用戶ID',
				align : 'center',
				width : 40,
				sortable : false
			},{
				field : 'nickname',
				title : '昵称',
				align : 'center',
				width : 40,
				sortable : false
			}
			//权限控制
			<html:auth action="member:list-phone">
			, {field : 'phone',title : '手机号码',align : 'center',width : 50,sortable : false}
			</html:auth>
			, {
				field : 'anchorlevel',
				title : '主播等级',
				align : 'center',
				width : 30,
				sortable : false
			}, {
				field : 'userlevel',
				title : '用户等级',
				align : 'center',
				width : 30,
				sortable : false
			}, {
				field : 'supportUserFlag',
				title : '虚拟号',
				align : 'center',
				width : 30,
				sortable : false,
				formatter:function(data,row){
					var ary = ['否','是'];
					return ary[data];
				}
			}, {
				field : 'status',
				title : '封号',
				align : 'center',
				width : 30,
				sortable : false,
				formatter:function(data,row){
					var ary = ['是','否'];
					return ary[data];
				}
			}, {
				field : 'money',
				title : '金币',
				align : 'center',
				width : 40,
				sortable : true
			}, {
				field : 'moneyRmb',
				title : '充值总金额人民币',
				align : 'center',
				width : 60,
				sortable : true
			}, {
				field : 'registtime',
				title : '注册时间',
				align : 'center',
				width : 70,
				sortable : true,
				formatter:function(data,row){
					if(data==null||data==0){
						return "";
					}
					var date = new Date();  
				    date.setTime(data * 1000);  
				    var y = date.getFullYear();      
				    var m = date.getMonth() + 1;      
				    m = m < 10 ? ('0' + m) : m;      
				    var d = date.getDate();      
				    d = d < 10 ? ('0' + d) : d;      
				    var h = date.getHours();    
				    h = h < 10 ? ('0' + h) : h;    
				    var minute = date.getMinutes();    
				    var second = date.getSeconds();    
				    minute = minute < 10 ? ('0' + minute) : minute;      
				    second = second < 10 ? ('0' + second) : second;     
				    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;        
				}
			}
			//权限控制
			<html:auth action="member:list-salesmanName">
			, {field : 'salesmanName',title : '所属家族助理',align : 'center',width : 80,sortable : false,formatter:function(data,row){
				return row.salesmanName+'<br>'+row.salesmanContactsPhone
			}}
			</html:auth>
			//权限控制
			<html:auth action="member:list-agentUserName">
			, {field : 'agentUserName',title : '所属黄金公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
				return data+'<br>'+row.agentUserContactsName+","+row.agentUserContactsPhone
			}}
			</html:auth>
			//权限控制
			<html:auth action="member:list-promotersName">
			,{field : 'promotersName',title : '所属铂金公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
				return data+'<br>'+row.promotersContactsName+","+row.promotersContactsPhone
			}}
			</html:auth>
			//权限控制
			<html:auth action="member:list-extensionCenterName">
			,{field : 'extensionCenterName',title : '所属钻石公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
				return data+'<br>'+row.extensionCenterContactsName+","+row.extensionCenterContactsPhone
			}}
			</html:auth>
			//权限控制
			<html:auth action="member:list-strategicPartnerName">
			,{field : 'strategicPartnerName',title : '所属星耀公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
				return data+"<br/>"+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone;
			}}
			</html:auth>
			<html:auth action="memberList:fenghao">
			,{field :'opt',title : '操作',align : 'center',width : 30,formatter:function(data,row){
				var optstr = "";
				if(row.status == 0){//封号了
					optstr = '<a href="javascript:void(0);" onclick="caozuo('+row.uid+',1);">解封</a>';
				}else if(row.status == 1){//正常
					optstr = '<a href="javascript:void(0);" onclick="caozuo('+row.uid+',0);">封号</a>';
				}
				return optstr;
			}}
			</html:auth>
			,{field : '_sta',title : '统计',align : 'center',width : 30,sortable : false,formatter:function(data,row){
				return '<a href="javascript:openTongji(\''+row.nickname+'\','+row.uid+');">统计</a>';
			}}
			] ]
		})
	});

	//检查图片的格式是否正确,同时实现预览
	function setImagePreview(obj, localImagId, imgObjPreview) {
		var array = new Array('gif', 'jpeg', 'png', 'jpg', 'bmp'); //可以上传的文件类型
		if (obj.value == '') {
			$.messager.alert("让选择要上传的图片!");
			return false;
		} else {
			var fileContentType = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3]; //这个文件类型正则很有用 
			////布尔型变量
			var isExists = false;
			//循环判断图片的格式是否正确
			for ( var i in array) {
				if (fileContentType.toLowerCase() == array[i].toLowerCase()) {
					//图片格式正确之后，根据浏览器的不同设置图片的大小
					if (obj.files && obj.files[0]) {
						//火狐下，直接设img属性 
						imgObjPreview.style.display = 'block';
						//                         imgObjPreview.style.width = '400px';
						//                         imgObjPreview.style.height = '400px';
						//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式 
						imgObjPreview.src = window.URL
								.createObjectURL(obj.files[0]);
					} else {
						//IE下，使用滤镜 
						obj.select();
						var imgSrc = document.selection.createRange().text;
						//必须设置初始大小 
						//                         localImagId.style.width = "400px";
						//                         localImagId.style.height = "400px";
						//图片异常的捕捉，防止用户修改后缀来伪造图片 
						try {
							localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
							localImagId.filters
									.item("DXImageTransform.Microsoft.AlphaImageLoader=").src = imgSrc;
						} catch (e) {
							$.messager.alert("您上传的图片格式不正确，请重新选择!");
							return false;
						}
						imgObjPreview.style.display = 'none';
						document.selection.empty();
					}
					isExists = true;
					return true;
				}
			}
			if (isExists == false) {
				$.messager.alert("上传图片类型不正确!");
				return false;
			}
			return false;
		}
	}
	
	//统计界面
	function openTongji(name,uid){
		parent.openTab("【"+name+"】的统计数据",'jsp/sta/userTransactionHisSta.jsp?uid='+uid);
	}		
	
	//解封、封号 status==1正常 0封号
	function caozuo(uid,status){
		var statusName = '封号';
		if(status == 1){
			statusName = '解封';
		}
		$.messager.confirm("系统提示","确定要"+statusName+"吗？",function(r){
			if(r){
				$.get("../../monitor?method=block&uid="+uid+"&status="+status,function(data){
					if(data.code == 200){
						$.messager.alert("系统提示",statusName+"成功！");
						$("#memberList").datagrid('reload');
						return;
					}else{
						$.messager.alert("系统提示",statusName+"失败！");
						$("#memberList").datagrid('reload');
						return;
					}
				},'json')
			}
		})
	}
	
	function search() {
		//家族助理
		var salesmanId = '${param.salesmanId}';
		if(!salesmanId){
			//权限控制
			<html:auth action="member:list-salesmanName">
			salesmanId = $('#s_salesmanId').textbox("getValue");
			</html:auth>
		}
		
		//黄金公会
		var agentUserId = '${param.agentUserId}';
		if(!agentUserId){
			//权限控制
			<html:auth action="member:list-agentUserName">
			agentUserId = $('#s_agentUserId').textbox("getValue");
			</html:auth>
		}

		//铂金公会
		var promotersId = '${param.promotersId}';
		if(!promotersId){
			//权限控制
			<html:auth action="member:list-promotersName">
			promotersId = $('#s_promotersId').textbox("getValue");
			</html:auth>
		}

		//钻石公会
		var extensionCenterId = '${param.extensionCenterId}';
		if(!extensionCenterId){
			//权限控制
			<html:auth action="member:list-extensionCenterName">
			extensionCenterId = $('#s_extensionCenterId').textbox("getValue");
			</html:auth>
		}
	
		//战略伙伴
		var strategicPartnerId = '${param.strategicPartnerId}';
		if(!strategicPartnerId){
			//权限控制
			<html:auth action="member:list-strategicPartnerName">
			strategicPartnerId = $('#s_strategicPartnerId').textbox("getValue");
			</html:auth>
		}
		
		var phone = "";
		//权限控制
		<html:auth action="member:list-phone">
		phone = $('#s_phone').textbox("getValue")
		</html:auth>
		
		$('#memberList').datagrid('load', {
			uid : $('#s_uid').textbox("getValue"),
			phone : phone,
			supportUserFlag : $("#s_supportUserFlag").combobox('getValue'),
			startRegisterTime : $("#s_startRegisterTime").datebox('getValue'),
			endRegisterTime : $("#s_endRegisterTime").datebox('getValue'),
			strategicPartnerId:strategicPartnerId,
			extensionCenterId:extensionCenterId,
			promotersId:promotersId,
			agentUserId:agentUserId,
			salesmanId:salesmanId
		});
	}
	
	function openDialog(){
		var row = $("#memberList").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','修改会员家族助理');
        $("#fm").form("clear");
        $("#fm").form("load", row);
	}
	
	function save(){
		var params = {
				"uid" : $("#fm #uid").val(),
				"salesmanId" : $("#fm #salesmanId").val()
			};
		$.post('../../member/modifyMemberSalesmanId', params, function(result) {
			var data = eval("(" + result + ")");
			if (data.result) {
				$.messager.alert("系统提示", "修改成功");
				$("#fm").form("clear");
				$("#dlg").dialog("close");
				$("#memberList").datagrid('reload');
			} else {
				$.messager.alert("系统提示", data.msg);
			}
		})
	}
	
	
	function showUserInfoDialog(){
		var row = $("#memberList").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
        $('#dlg1').dialog('open').dialog('center').dialog('setTitle','修改会员基本信息');
        $("#fm1").form("clear");
        $("#showHeadImage").attr("src", row.headimage);
        $("#showLivImage").attr("src", row.livimage);
        $("#headimageUrl").val(row.headimage);
        $("#livimageUrl").val(row.livimage);
		delete row['headimage'];
		delete row['livimage'];
        $("#fm1").form("load", row);
	}
	
	function saveUserInfo(){
		$("#fm1").form("submit", {
			url : '../../member/modifyMemberInfoById',
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("(" + data + ")");
				if (!result.result) {
					$.messager.alert("系统提示", result.msg);
					return;
				} else {
					$.messager.alert("系统提示", result.msg);
					$("#fm1").form("clear");
					$("#dlg1").dialog("close");
					$("#memberList").datagrid('reload');
				}
			},
			error : function(result) {
				alert(result);
			}
		});
	}
	
	function showUserPwordDialog(){
		var row = $("#memberList").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
        $('#dlg2').dialog('open').dialog('center').dialog('setTitle','修改会员密码');
        $("#fm2").form("clear");
        $("#fm2").form("load", row);
	}
	
	function updateUserPassword(){
		var passWord =  md5($("#fm2 #passWord").val());
		var params = {
				"uid" : $("#fm2 #uid").val(),
				"passWord" : passWord
			};
		$.post('../../member/updateUserPassword', params, function(result) {
			var data = eval("(" + result + ")");
			if (data.result) {
				$.messager.alert("系统提示", "修改成功");
				$("#fm").form("clear");
				$("#dlg").dialog("close");
			} else {
				$.messager.alert("系统提示", data.msg);
			}
		})
	}
	
	
	//导出数据
	function exportData(){
		//家族助理
		var salesmanId = '${param.salesmanId}';
		if(!salesmanId){
			//权限控制
			<html:auth action="member:list-salesmanName">
			salesmanId = $('#s_salesmanId').textbox("getValue");
			</html:auth>
		}
		
		//黄金公会
		var agentUserId = '${param.agentUserId}';
		if(!agentUserId){
			//权限控制
			<html:auth action="member:list-agentUserName">
			agentUserId = $('#s_agentUserId').textbox("getValue");
			</html:auth>
		}

		//铂金公会
		var promotersId = '${param.promotersId}';
		if(!promotersId){
			//权限控制
			<html:auth action="member:list-promotersName">
			promotersId = $('#s_promotersId').textbox("getValue");
			</html:auth>
		}

		//钻石公会
		var extensionCenterId = '${param.extensionCenterId}';
		if(!extensionCenterId){
			//权限控制
			<html:auth action="member:list-extensionCenterName">
			extensionCenterId = $('#s_extensionCenterId').textbox("getValue");
			</html:auth>
		}
	
		//战略伙伴
		var strategicPartnerId = '${param.strategicPartnerId}';
		if(!strategicPartnerId){
			//权限控制
			<html:auth action="member:list-strategicPartnerName">
			strategicPartnerId = $('#s_strategicPartnerId').textbox("getValue");
			</html:auth>
		}
		
		var phone = "";
		//权限控制
		<html:auth action="member:list-phone">
		phone = $('#s_phone').textbox("getValue")
		</html:auth>
		
		submitIFrameForm({
			url:"../../member/expExcel",
			data:{
				uid : $('#s_uid').textbox("getValue"),
				phone : phone,
				supportUserFlag : $("#s_supportUserFlag").combobox('getValue'),
				startRegisterTime : $("#s_startRegisterTime").datebox('getValue'),
				endRegisterTime : $("#s_endRegisterTime").datebox('getValue'),
				strategicPartnerId:strategicPartnerId,
				extensionCenterId:extensionCenterId,
				promotersId:promotersId,
				agentUserId:agentUserId,
				salesmanId:salesmanId
			}
		});
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="memberList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<html:auth action="member:edit">
				<a href="javascript:openDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="btn-update">修改会员家族助理</a> 
			</html:auth>
			<html:auth action="member:edit">
				<a href="javascript:showUserPwordDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="btn-update">修改会员密码</a> 
			</html:auth>
			<html:auth action="memberInfo:edit">
				<a href="javascript:showUserInfoDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="btn-update">修改会员基本信息</a> 
			</html:auth>
		</div>
		<div>
			用户ID:&nbsp;<input id="s_uid" name="uid" class="easyui-numberbox" style="width:100px"/>
			<html:auth action="member:list-phone">
			手机号:&nbsp;<input id="s_phone" name="phone" class="easyui-textbox" style="width:100px"/>
			</html:auth>
			<html:auth action="member:list-salesmanName">
			家族助理ID:&nbsp;<input id="s_salesmanId" name="salesmanId" class="easyui-textbox" style="width:100px"/>
			</html:auth>
			<html:auth action="member:list-agentUserName">
			黄金公会ID:&nbsp;<input id="s_agentUserId" name="agentUserId" class="easyui-textbox" style="width:100px"/>
			</html:auth>
			<html:auth action="member:list-promotersName">
			铂金公会ID:&nbsp;<input id="s_promotersId" name="promotersId" class="easyui-textbox" style="width:100px"/>
			</html:auth>
			<html:auth action="member:list-extensionCenterName">
			钻石公会ID:&nbsp;<input id="s_extensionCenterId" name="extensionCenterId" class="easyui-textbox" style="width:100px"/>
			</html:auth>
			<html:auth action="member:list-strategicPartnerName">
			星耀公会ID:&nbsp;<input id="s_strategicPartnerId" name="strategicPartnerId" class="easyui-textbox" style="width:100px"/>
			</html:auth>
			虚拟号：
			<select id="s_supportUserFlag" class="easyui-combobox" editable="false" name="supportUserFlag" panelHeight="auto" style="width:60px">
				<option value="">全部</option><option value="1">是</option><option value="0">否</option>
			</select>
			&nbsp;&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="startRegisterTime" id="s_startRegisterTime" size="10" />
			&nbsp;&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="endRegisterTime"  id="s_endRegisterTime" size="10" />
			<a href="javascript:search()"class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			<a href="javascript:exportData()"class="easyui-linkbutton" iconCls="icon-excel" plain="true">导出</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>用户UID:</td>
						<td>
							<input id="uid" name="uid" class="easyui-numberbox" min="1000000" max="100000000" data-options="disabled:true"/>
						</td>
					</tr>
					<tr>
						<td>手机号:</td>
						<td>
							<input id="phone" name="phone" class="easyui-numberbox" data-options="disabled:true"/>
						</td>
					</tr>
					<tr>
						<td>用户昵称:</td>
						<td>
							<input id="nickname" name="nickname" class="easyui-textbox"  data-options="disabled:true"/>
						</td>
					</tr>
					<tr>
						<td>所属家族助理ID:</td>
						<td>
							<input id="salesmanId" name="salesmanId" class="easyui-textbox"/>
						</td>
					</tr>
				</table>
			</form>
	</div>
	<div id="dlg1" class="easyui-dialog" style="width: 500px; height: 400px; padding: 10px 20px" closed="true" buttons="#dlg-buttons1">
			<form id="fm1" method="post" enctype="multipart/form-data">
				<table cellspacing="5px;">
				<tr>
					<td>用户UID:</td>
					<td>
						<input id="uid" name="uid" class="easyui-numberbox" min="1000000" max="100000000" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td>手机号:</td>
					<td>
						<input id="phone" name="phone" class="easyui-numberbox" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td>用户昵称:</td>
					<td>
						<input id="nickname" name="nickname" class="easyui-textbox"/>
					</td>
				</tr>
				<tr>
					<td>性別:</td>
					<td>
						<select id="sex" name="sex" class="easyui-combobox" onClick required="true" missingMessage="状态必填" style="width: 170px;">
							<option value="1">男</option>
							<option value="0">女</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>头像:</td>
					<td><input type="hidden" name="headimageUrl" id="headimageUrl"/>
					    <input type="file" name="headimage" id="headimage"
						onchange="javascript:setImagePreview(this,headImageDiv,showHeadImage);" required="true" /></td>
				</tr>
				<tr>
					<td></td>
					<td><div id="headImageDiv">
							<img id="showHeadImage" alt="预览图片" src="" width='120px' height='120px' />
						</div>
					</td>
				</tr>
				<tr>
					<td>封面:</td>
					<td><input type="hidden" name="livimageUrl" id="livimageUrl"/>
					    <input type="file" name="livimage" id="livimage"
						onchange="javascript:setImagePreview(this,livImageDiv,showLivImage);" required="true" /></td>
				</tr>
				<tr>
					<td></td>
					<td><div id="livImageDiv">
							<img id="showLivImage" alt="预览图片" src="" width='120px' height='120px' />
						</div>
					</td>
				</tr>
				<tr>
					<td>爱好:</td>
					<td>
						<input id="hobby" name="hobby" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td>个性签名:</td>
					<td>
						<input id="signature" name="signature" class="easyui-textbox" />
					</td>
				</tr>
			</table>
			</form>
	</div>
	<div id="dlg2" class="easyui-dialog" style="width: 370px; height: 250px; padding: 10px 20px" closed="true" buttons="#dlg-buttons2">
			<form id="fm2" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>用户UID:</td>
						<td>
							<input id="uid" name="uid" class="easyui-numberbox" min="1000000" max="100000000" data-options="disabled:true"/>
						</td>
					</tr>
					<tr>
						<td>用户昵称:</td>
						<td>
							<input id="nickname" name="nickname" class="easyui-textbox"  data-options="disabled:true"/>
						</td>
					</tr>
					<tr>
						<td>新密码:</td>
						<td>
							<input id="passWord" name="passWord" class="easyui-textbox"/>
						</td>
					</tr>
				</table>
			</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:save()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	</div>
	<div id="dlg-buttons1">
		<a href="javascript:saveUserInfo()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	</div>
	<div id="dlg-buttons2">
		<a href="javascript:updateUserPassword()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	</div>
	
</body>
</html>