package com.example.contact.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ExecutorFactory {
	  /**
     * 要确保该类只有一个实例对象，避免产生过多对象消费资源，所以采用单例模式
     */
    private ExecutorFactory() {}
 
    private static ExecutorFactory instance;

	public static ExecutorFactory getInstance() {
        if (instance == null) {
        	instance = new ExecutorFactory();
        }
        return instance;
    }

	public static ThreadPoolExecutor executor;

	public static synchronized ThreadPoolExecutor getThreadInstance() {
		if(executor==null) {
			ExecutorBuilder eb=new ExecutorBuilder();
			eb.setCorePoolSize(2);
			eb.setMaxPoolSize(2);
			executor=ExecutorFactory.getInstance().newExecutor(eb);
		}
		return executor;
	}
	
	/**
	 * 获得一个新的线程池，只有单个线程
	 * 
	 * @return ExecutorService
	 */
	public static ExecutorService newSingleExecutor() {
		return Executors.newSingleThreadExecutor();
	}
	/**
	 * 新建一个线程池
	 * @return ExecutorService
	 */
	public ThreadPoolExecutor newExecutor() {
		return newExecutor(new ExecutorBuilder());
	}
	/**
	 * 按ExecutorBuilder新建一个线程池
	 * @param builder {@link ExecutorBuilder}
	 * @return {@link ThreadPoolExecutor}
	 */
	public ThreadPoolExecutor newExecutor(ExecutorBuilder builder) {		
		//不可空
		int corePoolSize = builder.getCorePoolSize();
		
		if(corePoolSize==0) {
			/**
			 * 传入阻塞系数，线程池的大小计算公式为：CPU可用核心数 / (1 - 阻塞因子)
			 * Blocking Coefficient(阻塞系数) = 阻塞时间／（阻塞时间+使用CPU的时间
			 * 计算密集型任务的阻塞系数为0，而IO密集型任务的阻塞系数则接近于1。
			 */			
			float blockingCoefficient = builder.getBlockingCoefficient();
			corePoolSize = (int) (Runtime.getRuntime().availableProcessors() / (1 - blockingCoefficient));
		}		
		int maxPoolSize = builder.getMaxPoolSize();
		if(maxPoolSize==0) {
			maxPoolSize=corePoolSize;
		}
		long keepAliveTime = builder.getKeepAliveTime();
		if(keepAliveTime==0) {
			keepAliveTime=TimeUnit.SECONDS.toNanos(60);
		}		
		//可为空
		BlockingQueue<Runnable> workQueue=builder.getWorkQueue();
		if(workQueue == null) {
			//corePoolSize为0则要使用SynchronousQueue避免无限阻塞
			workQueue = (corePoolSize <= 0) ? new SynchronousQueue<Runnable>() : new LinkedBlockingQueue<Runnable>();
		}
		ThreadFactory threadFactory = builder.getThreadFactory();
		if(threadFactory == null) {
			threadFactory=Executors.defaultThreadFactory();
		}
		RejectedExecutionHandler handler = builder.getHandler();
		if (handler==null) {
			return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.NANOSECONDS, workQueue, threadFactory);
		} else {
			return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.NANOSECONDS, workQueue, threadFactory, handler);
		}
	}
	
}
