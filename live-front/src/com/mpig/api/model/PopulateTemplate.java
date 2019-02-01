package com.mpig.api.model;

import java.sql.ResultSet;

public interface PopulateTemplate<T> {

	public T populateFromResultSet(ResultSet rs);	
}
