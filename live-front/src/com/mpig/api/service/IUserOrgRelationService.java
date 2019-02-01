package com.mpig.api.service;

import com.mpig.api.model.ReturnModel;

public interface IUserOrgRelationService {

	public ReturnModel saveUserOrgRelation(Integer uid,String phone,Long registTime,Long salesmanId);
}
