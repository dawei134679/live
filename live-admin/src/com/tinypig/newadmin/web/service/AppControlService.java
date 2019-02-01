package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.AppControlBtnDto;

public interface AppControlService {

	Map<String, String> getIosShow();

	Map<String, Object> saveControlBtn(AppControlBtnDto appControlBtnDto);

}
