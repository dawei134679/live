<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>统计信息</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {
		//设置时间
 		var curr_time = new Date();   
 		$("#startTime").datetimebox("setValue",todayformatterStart(curr_time));
		$("#endTime").datetimebox("setValue",todayformatterEnd(curr_time));
		
		var startTime = $('#startTime').datetimebox('getValue');
		var endTime = $('#endTime').datetimebox('getValue');
		
		dataGrid = $("#_table").datagrid(
				{
					url : '../../userTransactionHisSta/pageList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '统计信息',
					pagination : true,
					showFooter : true,
					rownumbers : true,
					singleSelect : true,
					fit : true,
					fitColumns : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					queryParams: {
				        startTime:startTime,
				        endTime:endTime,
						uid:"${param.uid}",
				        gstype:$('#gstype').combobox("getValue"),
						gsid : $('#gsid').textbox("getValue"),
						<html:auth action="userTransactionHisSta:dataType">
						dataType:$("#dataType").combobox("getValue"),
						</html:auth>
						transType:$("#transType").combobox("getValue"),
						strategicPartnerId:'${param.strategicPartnerId}',
						extensionCenterId:'${param.extensionCenterId}',
						promotersId:'${param.promotersId}',
						agentUserId:'${param.agentUserId}',
						agentUserId:'${param.agentUserId}',
						salesmanId:'${param.salesmanId}',
						showDataTypeEq1:$("#showDataTypeEq1").val()
					},
					columns : [ [
							{
								field : 'uid',
								title : '用户UID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'transTypeStr',
								title : '类型',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'amount',
								title : '金额',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'money',
								title : '金币',
								align : 'center',
								width : 50,
								sortable : false,
							},
							{
								field : 'createTimeStr',
								title : '时间',
								align : 'center',
								sortable : false,
								width : 80
								
							}
							
							//权限控制
							<html:auth action="userTransactionHisSta:list-salesmanName">
							, {field : 'salesmanName',title : '所属家族助理',align : 'center',width : 80,sortable : false,formatter:function(data,row){
								return row.salesmanName+'<br>'+row.salesmanContactsPhone
							}}
							</html:auth>
							//权限控制
							<html:auth action="userTransactionHisSta:list-agentUserName">
							, {field : 'agentUserName',title : '所属黄金公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
								return data+'<br>'+row.agentUserContactsName+","+row.agentUserContactsPhone
							}}
							</html:auth>
							//权限控制
							<html:auth action="userTransactionHisSta:list-promotersName">
							,{field : 'promotersName',title : '所属铂金公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
								return data+'<br>'+row.promotersContactsName+","+row.promotersContactsPhone
							}}
							</html:auth>
							//权限控制
							<html:auth action="userTransactionHisSta:list-extensionCenterName">
							,{field : 'extensionCenterName',title : '所属钻石公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
								return data+'<br>'+row.extensionCenterContactsName+","+row.extensionCenterContactsPhone
							}}
							</html:auth>
							//权限控制
							<html:auth action="userTransactionHisSta:list-strategicPartnerName">
							,{field : 'strategicPartnerName',title : '所属星耀公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
								return data+"<br/>"+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone;
							}}
							</html:auth>
							//权限控制
							<html:auth action="userTransactionHisSta:dataType">
							,{
								field : 'dataTypeStr',
								title : '数据类型',
								align : 'center',
								width : 80,
								sortable : false,
							}
							</html:auth>
							]],onLoadSuccess:function(data){
								
								var col = 1;
								var gstypeVal = $('#gstype').combobox("getValue");
								
								var uid = "${param.uid}";
								if(!uid){
									uid = $('#uid').textbox("getValue");
								}
								
								//家族助理
								var salesmanId = '${param.salesmanId}';
								if(!salesmanId){
									//权限控制
									<html:auth action="userTransactionHisSta:list-salesmanName">
									if(gstypeVal == 5){
										salesmanId = $('#gsid').textbox("getValue");
									}
									col+=1;
									</html:auth>
								}else{
									<html:auth action="userTransactionHisSta:list-salesmanName">
										col+=1;
									</html:auth>
								}
								
								//黄金公会
								var agentUserId = '${param.agentUserId}';
								if(!agentUserId){
									//权限控制
									<html:auth action="userTransactionHisSta:list-agentUserName">
									if(gstypeVal == 4){
										agentUserId = $('#gsid').textbox("getValue");
									}
									col+=1;
									</html:auth>
								}else{
									<html:auth action="userTransactionHisSta:list-agentUserName">
										col+=1;
									</html:auth>
								}

								//铂金公会
								var promotersId = '${param.promotersId}';
								if(!promotersId){
									//权限控制
									<html:auth action="userTransactionHisSta:list-promotersName">
									if(gstypeVal == 3){
										promotersId = $('#gsid').textbox("getValue");
									}
									col+=1;
									</html:auth>
								}else{
									<html:auth action="userTransactionHisSta:list-promotersName">
										col+=1;
									</html:auth>
								}

								//钻石公会
								var extensionCenterId = '${param.extensionCenterId}';
								if(!extensionCenterId){
									//权限控制
									<html:auth action="userTransactionHisSta:list-extensionCenterName">
									if(gstypeVal == 2){
										extensionCenterId = $('#gsid').textbox("getValue");
									}
									col+=1;
									</html:auth>
								}else{
									//权限控制
									<html:auth action="userTransactionHisSta:list-extensionCenterName">
									col+=1;
									</html:auth>
								}
							
								//战略伙伴
								var strategicPartnerId = '${param.strategicPartnerId}';
								if(!strategicPartnerId){
									//权限控制
									<html:auth action="userTransactionHisSta:list-strategicPartnerName">
									if(gstypeVal == 1){
										strategicPartnerId = $('#gsid').textbox("getValue");
									}
									col+=1;
									</html:auth>
								}else{
									<html:auth action="userTransactionHisSta:list-strategicPartnerName">
									  col+=1;
									</html:auth>
								}
								//权限控制
								<html:auth action="userTransactionHisSta:dataType">
									col +=1;
								</html:auth>
								$.ajax({
									url : '../../userTransactionHisSta/getUserTransactionHisTotal',
									type: 'POST',
									data:{
										uid:uid,
								        startTime:$('#startTime').datetimebox('getValue'),
								        endTime:$('#endTime').datetimebox('getValue'),
								        <html:auth action="userTransactionHisSta:dataType">
										dataType:$("#dataType").combobox("getValue"),
										</html:auth>
										transType:$("#transType").combobox("getValue"),
										strategicPartnerId:strategicPartnerId,
										extensionCenterId:extensionCenterId,
										promotersId:promotersId,
										agentUserId:agentUserId,
										salesmanId:salesmanId,
										showDataTypeEq1:$("#showDataTypeEq1").val()
									},
									success:function(result){
										if(result==null){
											return;
										}
										var d = result;
										$('#_table').datagrid('appendRow', {
											uid: '<span class="subtotal">合计</span>',
											nickname: '<span></span>',
											transType: '<span></span>',
											amount:  '<span class="subtotal">'+result.data.amount+'</span>',
											money:  '<span class="subtotal">'+result.data.money+'</span>',
											createTimeStr: '<span></span>'
								        });
							            $("#_table").datagrid('mergeCells',{ 
							      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
											field: 'uid',                	//合并单元格的区域，就是clomun中的filed对应的列
											colspan: 3                		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
										});
							            
							            $("#_table").datagrid('mergeCells',{ 
							      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
											field: 'createTimeStr',                	//合并单元格的区域，就是clomun中的filed对应的列
											colspan: col               		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
										});
									}
								});
							},
				})
	})
	
	 function myformatterStart(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            return y+'-'+(m<10?('0'+m):m)+'-01 00:00:00';
        }

	 function myformatterEnd(date){
           var y = date.getFullYear();
           var m = date.getMonth()+1;
           var d = date.getDate();
           return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' 23:59:59';
       }
	
	 function todayformatterStart(date){
         var y = date.getFullYear();
         var m = date.getMonth()+1;
         var d = date.getDate();
         return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' 00:00:00';
     }

	 function todayformatterEnd(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' 23:59:59';
    }
	 
	function search(showDataTypeEq1){
		if(showDataTypeEq1){
			$("#showDataTypeEq1").val("1");
		}else{
			$("#showDataTypeEq1").val("");
		}
		var uid = "${param.uid}";
		if(!uid){
			uid = $('#uid').textbox("getValue");
		}
		
		var gstypeVal = $('#gstype').combobox("getValue");
		//家族助理
		var salesmanId = '${param.salesmanId}';
		if(!salesmanId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-salesmanName">
			if(gstypeVal == 5){
				salesmanId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}
		
		//黄金公会
		var agentUserId = '${param.agentUserId}';
		if(!agentUserId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-agentUserName">
			if(gstypeVal == 4){
				agentUserId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}

		//铂金公会
		var promotersId = '${param.promotersId}';
		if(!promotersId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-promotersName">
			if(gstypeVal == 3){
				promotersId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}

		//钻石公会
		var extensionCenterId = '${param.extensionCenterId}';
		if(!extensionCenterId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-extensionCenterName">
			if(gstypeVal == 2){
				extensionCenterId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}
	
		//战略伙伴
		var strategicPartnerId = '${param.strategicPartnerId}';
		if(!strategicPartnerId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-strategicPartnerName">
			if(gstypeVal == 1){
				strategicPartnerId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}
		
		$("#_table").datagrid({
			url : '../../userTransactionHisSta/pageList',
			queryParams: {
		        uid:uid,
		        startTime:$('#startTime').datetimebox('getValue'),
		        endTime:$('#endTime').datetimebox('getValue'),
		        <html:auth action="userTransactionHisSta:dataType">
				dataType:$("#dataType").combobox("getValue"),
				</html:auth>
				transType:$("#transType").combobox("getValue"),
				strategicPartnerId:strategicPartnerId,
				extensionCenterId:extensionCenterId,
				promotersId:promotersId,
				agentUserId:agentUserId,
				salesmanId:salesmanId,
				showDataTypeEq1:$("#showDataTypeEq1").val()
			}
		})
	}
	
	function refresh(){
		search(1);
	}
	
	function exportExcel(){
		var uid = "${param.uid}";
		if(!uid){
			uid = $('#uid').textbox("getValue");
		}
		
		var gstypeVal = $('#gstype').combobox("getValue");
		//家族助理
		var salesmanId = '${param.salesmanId}';
		if(!salesmanId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-salesmanName">
			if(gstypeVal == 5){
				salesmanId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}
		
		//黄金公会
		var agentUserId = '${param.agentUserId}';
		if(!agentUserId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-agentUserName">
			if(gstypeVal == 4){
				agentUserId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}

		//铂金公会
		var promotersId = '${param.promotersId}';
		if(!promotersId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-promotersName">
			if(gstypeVal == 3){
				promotersId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}

		//钻石公会
		var extensionCenterId = '${param.extensionCenterId}';
		if(!extensionCenterId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-extensionCenterName">
			if(gstypeVal == 2){
				extensionCenterId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}
	
		//战略伙伴
		var strategicPartnerId = '${param.strategicPartnerId}';
		if(!strategicPartnerId){
			//权限控制
			<html:auth action="userTransactionHisSta:list-strategicPartnerName">
			if(gstypeVal == 1){
				strategicPartnerId = $('#gsid').textbox("getValue");
			}
			</html:auth>
		}
		
		submitIFrameForm({
			url:"../../userTransactionHisSta/expExcel",
			data:{
				uid:uid,
		        startTime:$('#startTime').datetimebox('getValue'),
		        endTime:$('#endTime').datetimebox('getValue'),
		        <html:auth action="userTransactionHisSta:dataType">
				dataType:$("#dataType").combobox("getValue"),
				</html:auth>
				transType:$("#transType").combobox("getValue"),
				strategicPartnerId:strategicPartnerId,
				extensionCenterId:extensionCenterId,
				promotersId:promotersId,
				agentUserId:agentUserId,
				salesmanId:salesmanId,
				showDataTypeEq1:$("#showDataTypeEq1").val()
			}
		});
	}
// 	function del(orderId){
// 		$.messager.confirm("系统提示", "是否删除该虚拟交易?", function(r) {
// 			if (r) {
// 				$.post("../payOrder/delPayOrder", {"orderId":orderId}, function(result) {
// 					if (result.code == 200) {
// 						$.messager.alert("系统提示", "删除成功");
// 						$("#payInfo").datagrid("reload");
// 					} else {
// 						$.messager.alert('系统提示', result.msg);
// 					}
// 				}, "json");
// 			}
// 		});
// 	}
</script>
</head>
<body>
	<table id="_table"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
		 <form id="ff" method="post" action="../../userTransactionHisSta/expExcel" target="ifr">
				&nbsp;用户ID：&nbsp;<input  type="text" name="uid" id="uid" size="10"  class="easyui-numberbox" data-options="precision:0"/>
				&nbsp;类型：&nbsp;<select id="transType" class="easyui-combobox" name="transType">
									<option value="">全部</option>
									<option value="1">欢乐六选三下注</option>
									<option value="2">欢乐六选三胜出</option>
									<option value="3">欢乐六选三手续费</option>
									<option value="4">抓娃娃投下注</option>
									<option value="5">抓娃娃手胜出</option>
									<option value="7">开心敲敲乐下注</option>
									<option value="8">开心敲敲乐胜出</option>
									<option value="10">获得礼物金币</option>
									<option value="11">赠送礼物金币</option>
									<option value="12">金币充值</option>
									<option value="13">购买守护</option>
									<option value="14">购买VIP</option>
									<option value="15">购买座驾</option>
								</select>
								<html:auth action="userTransactionHisSta:dataType">
				&nbsp;数据类型：&nbsp;<select id="dataType" class="easyui-combobox" name="dataType">
									<option value="" selected="selected">全部</option>
									<option value="1">真实数据</option>
									<option value="2">虚拟数据</option>
								</select>
								</html:auth>
				&nbsp;归属：&nbsp;<select id="gstype" name="gstype" class="easyui-combobox">
									<html:auth action="userTransactionHisSta:list-strategicPartnerName">
									<option value="1" selected="selected">星耀公会</option>
									</html:auth>
									<html:auth action="userTransactionHisSta:list-extensionCenterName">
									<option value="2">钻石公会</option>
 									</html:auth>
									<html:auth action="userTransactionHisSta:list-promotersName">
									<option value="3">铂金公会</option>
									</html:auth>
									<html:auth action="userTransactionHisSta:list-agentUserName">
									<option value="4">黄金公会</option>
									</html:auth>
									<html:auth action="userTransactionHisSta:list-salesmanName">
									<option value="5">家族助理</option>
									</html:auth>
								</select>ID:&nbsp;<input id="gsid" name="gsid" class="easyui-numberbox"/>
						<!-- parents id -->
						<input type="hidden" value="" id="s_strategicPartnerId" name="strategicPartnerId"/>
						<input type="hidden" value="" id="s_extensionCenterId" name="extensionCenterId"/>
						<input type="hidden" value="" id="s_promotersId" name="promotersId"/>
						<input type="hidden" value="" id="s_agentUserId" name="agentUserId"/>
						<input type="hidden" value="" id="s_salesmanId" name="salesmanId"/>
						<input type="hidden" value="" id="showDataTypeEq1" name="showDataTypeEq1"/>
				&nbsp;开始时间：&nbsp;<input class="easyui-datetimebox" style="width:160px;height:24px" name="startTime" id="startTime"/>
				&nbsp;结束时间：&nbsp;<input class="easyui-datetimebox" style="width:160px;height:24px" name="endTime"  id="endTime"/>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				<html:auth action="userTransactionHisSta:reloadShowDataTypeEq1">
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:refresh()" class="easyui-linkbutton" iconCls="icon-reload">刷新</a>
				</html:auth>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
	</div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>