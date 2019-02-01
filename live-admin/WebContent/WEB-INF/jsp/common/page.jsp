<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!---分页--->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/dwr/util.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/dwr/page.js"></script>
<div class="pagination pagination-right">
	<ul>
		<li class="disabled" id="first_pag" >
		<a href="javascript:toFirst();">首页</a>
		</li>
		<li id="prev_pag"><a href="javascript:toPrivious();">上一页</a>
		</li>
		<li id="next_pag"><a href="javascript:toNext();">下一页</a>
		</li>
		<li id="last_pag"><a href="javascript:toLast();">尾页</a>
		</li>
		<li><input type="text" class="form-control input"
			id="_pageCurrent" width="60" value="1">
		</li>
		<li><p>
				共<span id="_totalPage">0</span>页
			</p>
		</li>
		<li><select class="selectpicker" id="_pageSize"
			onchange="toChangeNumberFirst();">
				<option>10</option>
				<option>20</option>
				<option>50</option>
		</select></li>
		<li><p>
				<span id="_page_view_se">1-20</span>共<span id="_totalCount_view">0</span>条
				<input type="hidden" id="totalRecord" />
			</p>
		</li>
	</ul>
</div>
<!---分页结束--->
