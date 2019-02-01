package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.ReportFeed;
import com.tinypig.newadmin.web.entity.ReportFeedParamDto;

public interface ReportFeedService {

	List<ReportFeed> pageList(ReportFeedParamDto reportFeedParamDto);

	Map<String, Object> details(Integer id);

}
