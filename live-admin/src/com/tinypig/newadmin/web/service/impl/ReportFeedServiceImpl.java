package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.ReportFeedDao;
import com.tinypig.newadmin.web.dao.ReportFeedUserDao;
import com.tinypig.newadmin.web.dao.UserFeedDao;
import com.tinypig.newadmin.web.entity.ReportFeed;
import com.tinypig.newadmin.web.entity.ReportFeedParamDto;
import com.tinypig.newadmin.web.entity.ReportFeedUser;
import com.tinypig.newadmin.web.entity.UserFeed;
import com.tinypig.newadmin.web.service.ReportFeedService;

@Service
@Transactional
public class ReportFeedServiceImpl implements ReportFeedService {

	@Autowired
	private ReportFeedDao reportFeedDao;

	@Autowired
	private ReportFeedUserDao reportFeedUserDao;

	@Autowired
	private UserFeedDao userFeedDao;

	@Override
	public List<ReportFeed> pageList(ReportFeedParamDto reportFeedParamDto) {
		int page = reportFeedParamDto.getPage();
		int rows = reportFeedParamDto.getRows();
		Integer startIndex = (page - 1) * rows;
		reportFeedParamDto.setStartIndex(startIndex);
		reportFeedParamDto.setPageSize(rows);
		return reportFeedDao.pageList(reportFeedParamDto);
	}

	@Override
	public Map<String, Object> details(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == id || id == 0) {
			map.put("success", false);
			map.put("msg", "查询失败，编号不能为空");
			return map;
		}
		// 举报详情
		ReportFeed reportFeed = reportFeedDao.selectByPrimaryKey(id);
		if (null != reportFeed) {
			map.put("reportFeed", reportFeed);
		} else {
			map.put("success", false);
			map.put("msg", "查询失败，未找到该记录");
			return map;
		}
		// 举报人详情
		List<ReportFeedUser> reportFeedUsers = reportFeedUserDao.selectByReportFeedId(id);
		if (null != reportFeed) {
			map.put("reportFeedUsers", reportFeedUsers);
		}
		// 被举报动态详情
		UserFeed userFeed = userFeedDao.selectByPrimaryKey(reportFeed.getReportFid());
		map.put("userFeed", userFeed);

		map.put("success", true);
		map.put("msg", "查询成功");
		return map;
	}

}
