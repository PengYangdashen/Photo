package com.example.contact.thread;

import java.util.concurrent.ExecutorService;
/**
 * 公共线程池
 */
public class GlobalExecutor {

	private static ExecutorService executor;
	
	
	public static synchronized ExecutorService getExecutor() {
        if (executor == null) {
        	executor = ExecutorFactory.getInstance().newExecutor();
        }
        if(executor!=null && executor.isShutdown()) {
        	executor = ExecutorFactory.getInstance().newExecutor();
        }     
        return executor;
    }
	
	public static synchronized void initExecutor(ExecutorService executor) {
		if (null != executor) {
			executor.shutdownNow();
		}
		GlobalExecutor.executor=executor;
    }
	
	/**
	 * 关闭公共线程池(等待线程完成才关闭)
	 */
	 public static synchronized void shutdown() {
		shutdown(false);
	}
	/**
	 * 关闭公共线程池
	 * 
	 * @param isNow 是否立即关闭而不等待正在执行的线程
	 */
	 public static synchronized void shutdown(boolean isNow) {
		if (null != executor) {
			if (isNow) {
				executor.shutdownNow();
			} else {
				executor.shutdown();
			}
		}
	}
	
}
