package com.tinypig.admin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tinypig.admin.dao.LiveDao;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.AnchorModel;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.util.ResponseUtil;

/**
 * Servlet implementation class AnchorOpenServlet
 */
public class AnchorOpenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public final static String OPERATION_TYPE_GET_LIST = "1";
	public final static String OPERATION_TYPE_SET_RECOMMEND = "2";
	public final static String OPERATION_TYPE_CLOSE_ROOM = "3";
    
	UserDao userDao = new UserDao();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnchorOpenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");// 当前管理员信息
		int adminUid = adminUser.getUid();
		
		String type = request.getParameter("type");
		
		if(OPERATION_TYPE_GET_LIST.equals(type)){
			getLivingList(request, response);
		} else if(OPERATION_TYPE_SET_RECOMMEND.equals(type)){
			setRecommend(request, response, adminUid);
		} else if(OPERATION_TYPE_CLOSE_ROOM.equals(type)){
			try {
				closeRoom(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * 获取开播列表
	 * @param request
	 * @param response
	 */
	protected void getLivingList(HttpServletRequest request,HttpServletResponse response){
		List<AnchorModel> list = new ArrayList<AnchorModel>();
 		String suid = request.getParameter("uid");

		//=0 普通主播 ＝1最新主播 ＝2热门主播 =3头牌主播 其他则所有
		String srecommend = request.getParameter("recommend");
		if(StringUtils.isEmpty(srecommend)){
			srecommend = "-1";
		}
		
		UserBaseInfoModel userBaseInfo = null;
		AnchorModel anchorModel = null;
		
		if (!StringUtils.isEmpty(suid)) {

			userBaseInfo = userDao.getUserBaseInfo(Integer.valueOf(suid),false);
			anchorModel = new AnchorModel()
					.populateFromUserBaseInfoModel(userBaseInfo);
			list.add(anchorModel);
			
		}else {
			// 没有指定uid查询
			Set<String> set = LiveDao.getInstance().getLivingList(Integer.valueOf(srecommend));
			
			for (String string : set) {
				
				userBaseInfo = userDao.getUserBaseInfo(Integer.valueOf(string),false);
				if (userBaseInfo == null) {
					continue;
				}
				if (!"-1".equalsIgnoreCase(srecommend)) {
					if (!srecommend.equals(userBaseInfo.getRecommend().toString())) {
						continue;
					}
				}
				
				anchorModel = new AnchorModel()
						.populateFromUserBaseInfoModel(userBaseInfo);
				list.add(anchorModel);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() > 0) {
			map.put("total", list.size());
			map.put("rows", list);
		}else {
			map.put("total", 0);
			map.put("rows", new ArrayList<AnchorModel>());
		}
		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getLivingList->Exception:"+e.getMessage());
		}
	}
	
	/**
	 * 设置 主播推荐
	 * @param request
	 * @param response
     */
	protected void setRecommend(HttpServletRequest request,HttpServletResponse response,int adminUid){

		// =0普通主播 ＝1最新主播 ＝2推荐主播 =3头牌主播
		int recommend = Integer.valueOf(request.getParameter("recommend"));
		int uid = Integer.valueOf(request.getParameter("uid"));
		int contrRq = Integer.valueOf(request.getParameter("contrRq"));
		//获取评级
		int grade = Integer.valueOf(request.getParameter("grade"));
		Map<String, Object> setUserCommend = userDao.setUserCommend(uid, recommend, contrRq,grade,adminUid);
		
		try {
			ResponseUtil.writeJSON(response, setUserCommend);
		} catch (Exception e) {
			System.out.println("setRecommend->Exception:"+e.getMessage());
		}
	}

	/**
     * 关房间
     * @param request
     * @param response
	 * @throws Exception 
     */
    protected  void closeRoom(HttpServletRequest request,HttpServletResponse response) throws Exception{
        AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
        if(null == adminUser){
            return;
        }
        int uid = Integer.valueOf(request.getParameter("uid"));
        Map<String, Object> map = LiveDao.getInstance().closeRoom(uid,adminUser.getUid());
        ResponseUtil.writeJSON(response,map);
    }
}
