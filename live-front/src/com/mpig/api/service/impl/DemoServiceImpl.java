package com.mpig.api.service.impl;

import org.springframework.stereotype.Service;

import com.mpig.api.service.IDemoService;

@Service
public class DemoServiceImpl implements IDemoService {

	@Override
	public int demo(){
		return 1;
	}
}
