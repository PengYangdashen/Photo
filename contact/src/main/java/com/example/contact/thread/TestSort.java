package com.example.contact.thread;

import java.util.concurrent.ThreadPoolExecutor;

public class TestSort {
	
	public static ThreadPoolExecutor executor;

	public static synchronized ThreadPoolExecutor getInstance() {
		if(executor==null) {
			ExecutorBuilder eb=new ExecutorBuilder();
			eb.setCorePoolSize(2);
			eb.setMaxPoolSize(2);
			executor=ExecutorFactory.getInstance().newExecutor(eb);
		}
		return executor;
	}

}
