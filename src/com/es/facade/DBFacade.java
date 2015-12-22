package com.es.facade;

import java.util.List;

import com.es.facade.impl.Param;

public interface DBFacade<T> {

	void persist(T obj);
	
	List<T> find(Param param);
	
	List<T> findAll();
}
