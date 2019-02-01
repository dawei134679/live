package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.IosVersion;

public interface IIosVersionService {

	List<IosVersion> getIosVersionList(Integer page, Integer rows);

	List<IosVersion> getAllIosVersion();

	int saveIosVersion(IosVersion iosVersion);

	int updateIosVersion(IosVersion iosVersion);

	Map<String, Object> refreshRedisCache();

	int delIosVersion(Long id);
}
