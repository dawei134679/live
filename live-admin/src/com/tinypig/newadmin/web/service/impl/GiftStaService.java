package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.web.dao.GiftStaDao;
import com.tinypig.newadmin.web.entity.GiftHistoryStaDto;
import com.tinypig.newadmin.web.service.IGiftStaService;
import com.tinypig.newadmin.web.vo.GiftStaHistoryVo;

@Service
public class GiftStaService implements IGiftStaService {
	private static Logger log = Logger.getLogger(GiftStaService.class);

	@Autowired
	private GiftStaDao giftStaDao;

	@Override
	public Map<String, Object> giftHistorySta(GiftHistoryStaDto giftHistorySta) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", 0);
		map.put("rows", new ArrayList());
		try {
			do {
				Integer startIndex = (giftHistorySta.getPage() - 1) * giftHistorySta.getRows();
				giftHistorySta.setStartIndex(startIndex);
				giftHistorySta.setPageSize(giftHistorySta.getRows());

				// 处理时间
				String startTime = giftHistorySta.getStartTime();
				String endTime = giftHistorySta.getEndTime();
				if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
					log.info("giftHistorySta：开始时间或结束时间为空");
					break;
				}
				Long startTimeLong = DateUtil.dateToLong(startTime);
				Long endTimeLong = DateUtil.dateToLong(endTime);
				if (startTimeLong > endTimeLong) {
					log.info("giftHistorySta：开始时间大于结束时间");
					break;
				}
				Date now = new Date();
				Long nowMonthLastDay = DateUtil.dateToLong(
						DateUtil.formatDate(now, "yyyy-MM") + "-" + DateUtil.getLastDayOfMonth(now) + " 23:59:59");// 月底最后一天
				if (endTimeLong > nowMonthLastDay) {
					log.info("giftHistorySta：结束日期大于当前月份");
					endTime = DateUtil.formatDate(now, DateUtil.DATE_FORMAT);// 设置结束日期等于当前日期
				}
				List<String> tableNames = new ArrayList<String>();
				Date startDate = DateUtil.truncateToMonth(DateUtil.formatString(startTime, DateUtil.DATE_FORMAT));
				Date endDate = DateUtil.truncateToMonth(DateUtil.formatString(endTime, DateUtil.DATE_FORMAT));
				while (true) {
					tableNames.add(DateUtil.formatDate(startDate, DateUtil.DATE_FORMAT_YYYYMM));
					startDate = DateUtil.addDate(startDate, Calendar.MONTH, 1);
					if (startDate.getTime() > endDate.getTime()) {
						break;
					}
				}

				giftHistorySta.setTableNames(tableNames);

				List<GiftStaHistoryVo> list = giftStaDao.giftHistorySta(giftHistorySta);
				Long total = giftStaDao.giftHistoryTotalCount(giftHistorySta);
				map.put("total", total);
				map.put("rows", list);
				return map;
			} while (false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("giftHistorySta error :", e);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getGiftTotal(GiftHistoryStaDto giftHistorySta) {
		try {
			do {
				// 处理时间
				String startTime = giftHistorySta.getStartTime();
				String endTime = giftHistorySta.getEndTime();
				if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
					log.info("giftHistorySta：开始时间或结束时间为空");
					break;
				}
				Long startTimeLong = DateUtil.dateToLong(startTime);
				Long endTimeLong = DateUtil.dateToLong(endTime);
				if (startTimeLong > endTimeLong) {
					log.info("giftHistorySta：开始时间大于结束时间");
					break;
				}
				Date now = new Date();
				Long nowMonthLastDay = DateUtil.dateToLong(
						DateUtil.formatDate(now, "yyyy-MM") + "-" + DateUtil.getLastDayOfMonth(now) + " 23:59:59");// 月底最后一天
				if (endTimeLong > nowMonthLastDay) {
					log.info("giftHistorySta：结束日期大于当前月份");
					endTime = DateUtil.formatDate(now, DateUtil.DATE_FORMAT);// 设置结束日期等于当前日期
				}
				List<String> tableNames = new ArrayList<String>();
				Date startDate = DateUtil.truncateToMonth(DateUtil.formatString(startTime, DateUtil.DATE_FORMAT));
				Date endDate = DateUtil.truncateToMonth(DateUtil.formatString(endTime, DateUtil.DATE_FORMAT));
				while (true) {
					tableNames.add(DateUtil.formatDate(startDate, DateUtil.DATE_FORMAT_YYYYMM));
					startDate = DateUtil.addDate(startDate, Calendar.MONTH, 1);
					if (startDate.getTime() > endDate.getTime()) {
						break;
					}
				}

				giftHistorySta.setTableNames(tableNames);
			} while (false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return giftStaDao.getGiftTotal(giftHistorySta);
	}

	@Override
	public List<Map<String, Object>> getAllHistorySta(GiftHistoryStaDto giftHistorySta) {

		List<Map<String, Object>> list = new ArrayList<>();
		try {
			do {
				// 处理时间
				String startTime = giftHistorySta.getStartTime();
				String endTime = giftHistorySta.getEndTime();
				if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
					log.info("giftHistorySta：开始时间或结束时间为空");
					break;
				}
				Long startTimeLong = DateUtil.dateToLong(startTime);
				Long endTimeLong = DateUtil.dateToLong(endTime);
				if (startTimeLong > endTimeLong) {
					log.info("giftHistorySta：开始时间大于结束时间");
					break;
				}
				Date now = new Date();
				Long nowMonthLastDay = DateUtil.dateToLong(
						DateUtil.formatDate(now, "yyyy-MM") + "-" + DateUtil.getLastDayOfMonth(now) + " 23:59:59");// 月底最后一天
				if (endTimeLong > nowMonthLastDay) {
					log.info("giftHistorySta：结束日期大于当前月份");
					endTime = DateUtil.formatDate(now, DateUtil.DATE_FORMAT);// 设置结束日期等于当前日期
				}
				List<String> tableNames = new ArrayList<String>();
				Date startDate = DateUtil.truncateToMonth(DateUtil.formatString(startTime, DateUtil.DATE_FORMAT));
				Date endDate = DateUtil.truncateToMonth(DateUtil.formatString(endTime, DateUtil.DATE_FORMAT));
				while (true) {
					tableNames.add(DateUtil.formatDate(startDate, DateUtil.DATE_FORMAT_YYYYMM));
					startDate = DateUtil.addDate(startDate, Calendar.MONTH, 1);
					if (startDate.getTime() > endDate.getTime()) {
						break;
					}
				}

				giftHistorySta.setTableNames(tableNames);

				list = giftStaDao.giftAllHistorySta(giftHistorySta);
			} while (false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("giftHistorySta error :", e);
		}
		return list;
	}

}
