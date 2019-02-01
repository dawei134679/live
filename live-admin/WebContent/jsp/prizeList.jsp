<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
<script type="text/javascript" src="../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
    <script>

    	$(function(){
        	//获取渠道列表
    	    $("#gid").combobox({
    				url:"../luckyGift?method=getAllLuckyGiftList",
    				valueField:'gid',
    				textField:'gname',
    				onLoadSuccess: function (data) {
    		            if (data) {
    		                $('#gid').combobox('setValue',"");
    		            }
    				}
    		});
      
    	});

        var dataGrid;

        $(function () {

            dataGrid = $("#prizeList").datagrid({
                url: '../luckyGift?method=getPrizeList',
                width: getWidth(0.97),
                height: getHeight(0.97),
				remoteSort : true,
				striped : true,
				showFooter : true,
				nowrap : false,
                title: '中奖列表',
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                fitColumns:true,
                pageSize:20,
                pageList:[20,50,100],
                toolbar: '#tb',
                columns: [[
                    {field: 'createdate', width:"100", title: '日期', align: 'center', sortable: false},
                    {field: 'totalsendcount', width:"200", title: '送出数量',  align: 'center', sortable: false},
                    {field: 'totalsendprice',width:"200", title: '送出价值(金币)', align: 'center', sortable: false},
                    {field: 'totalcount10',
                    	width:"100", 
                    	title: '10倍',  
                    	align: 'center', 
                    	sortable: false,
                    	formatter : function(data) {
							return '<a href="javascript:getPrizeList(10);" class="easyui-tooltip" title="查看公会主播">'+data+'</a>';
						}
                    },
                    {field: 'totalcount100',
                    	width:"100", 
                    	title: '100倍',  
                    	align: 'center', 
                    	sortable: false,
                    	formatter : function(data) {
							return '<a href="javascript:getPrizeList(100);" class="easyui-tooltip" title="查看公会主播">'+data+'</a>';
						}
                    },
                    {field: 'totalcount500',
                    	width:"100", 
                    	title: '500倍', 
                    	align: 'center', 
                    	sortable: false,
                    	formatter : function(data) {
							return '<a href="javascript:getPrizeList(500);" class="easyui-tooltip" title="查看公会主播">'+data+'</a>';
						}
                    },
                    {field: 'totalcount1000',
                    	width:"100", 
                    	title: '1000倍', 
                    	align: 'center', 
                    	sortable: false,
                    	formatter : function(data) {
							return '<a href="javascript:getPrizeList(1000);" class="easyui-tooltip" title="查看公会主播">'+data+'</a>';
						}
                    },
                    {field: 'totalprize', width:"200",title: '中出(金币)', align: 'center', sortable: false},
                    {field: 'totalprofit',width:"200", title: '收益(金币)', align: 'center', sortable: false},
                ]]
            });
        });


        function query() {
            dataGrid.datagrid('reload', {
            	startdate:$('#startdate').datebox("getValue"),
            	enddate:$('#enddate').datebox("getValue"),
            	gid:$('#gid').combobox("getValue")
            });
        }

        function clearQuery() {
        	$('#startdate').datebox('clear');
        	$('#enddate').datebox('clear');
            $("#gid").combobox('clear');

        }
        
        function getPrizeList(multiple){
    		var row = $('#prizeList').datagrid('getSelected');
    		$('#dlgPrizeDetail').dialog('open').dialog('center').dialog('setTitle','中奖详情');
    		$('#prizeDetail').datagrid(
    				{
    					url : '../luckyGift?method=getPrizeDetailList',
    					width : getWidth(0.97),
    					height : getHeight(0.97),
    					pagination : true,
    					rownumbers : true,
    					singleSelect : true,
    					remoteSort : true,
    					striped : true,
    					showFooter : true,
    					nowrap : false,
    	                title: '中奖详情',
    	                fitColumns:true,
    	                pageSize:20,
    	                pageList:[20,50,100],
    					queryParams :{
    						createdate : row.createdate,
    						gid : $('#gid').combobox("getValue"),
    						multiple : multiple
    					},
    					columns : [ [
    							{
    								field : 'createdate',
    								title : '日期',
    								align : 'center',
    								sortable : false
    							},
    							{
    								field : 'uid',
    								title : '赠送人ID',
    								align : 'center',
    								sortable : false
    							},{
    								field : 'usernickname',
    								title : '赠送人昵称',
    								align : 'center',
    								sortable : false
    							},
    							{
    								field : 'userlevel',
    								title : '赠送人等级',
    								align : 'center',
    								sortable : false
    							},{
    								field : 'gname',
    								title : '礼物名称',
    								align : 'center',
    								sortable : false
    							},
    							{
    								field : 'anchoruid',
    								title : '收礼人ID',
    								align : 'center',
    								sortable : false
    							},{
    								field : 'anchornickname',
    								title : '收礼人昵称',
    								align : 'center',
    								sortable : false
    							},
    							{
    								field : 'anchorlevel',
    								title : '收礼人等级',
    								align : 'center',
    								sortable : false
    							},{
    								field : 'luckycount',
    								title : '中奖次数',
    								align : 'center',
    								sortable : false
    							}
    							] ]
    				});
    	}

    </script>
</head>
<body>
<div>
	<table>
	<tr>
	<td></td>
	</tr>
	</table>
    <table id="prizeList" >
	</table>
    <div id="tb" style="padding:2px 5px;height:auto">
        <div>
            <label for="b_status">幸运道具：</label>
            <select class="easyui-combobox" id="gid" style="width:80px;">
            </select>
&nbsp;<input class="easyui-datebox" name="startdate" id="startdate" editable="false" size="20"/>-><input class="easyui-datebox" name="enddate" id="enddate" editable="false" size="20"/>
            &nbsp;&nbsp;<a href="javascript:query()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
            &nbsp;&nbsp;<a href="javascript:clearQuery()" class="easyui-linkbutton">清空</a>
        </div>
    </div>
</div>
<div id="dlgPrizeDetail" class="easyui-dialog" style="width:800;height:300" closed="true" maximizable="true" >
	<table id="prizeDetail"></table>
</div>
</body>
</html>
