package com.es.test;

import com.es.core.LoggerBean;
import com.es.core.LoggerDao;
import com.es.facade.impl.Param;
import com.google.gson.Gson;

public class Logger {
	
	public void getLog(Param param) {
		display(new LoggerDao().find(param));
	}
	
	public void insert(LoggerBean bean) {
		new LoggerDao().persist(bean);
	}
	
	public static void getLogs() {
		for(LoggerBean bean : new LoggerDao().findAll()) {
			display(bean);
		}
	}
	
	private static void display(Object obj) {
		System.out.println(new Gson().toJson(obj));
	}
}
