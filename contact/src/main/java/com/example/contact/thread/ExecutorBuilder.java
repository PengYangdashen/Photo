package com.example.contact.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

public class ExecutorBuilder {
	/**
	 * corePoolSize:核心线程数
	 */
	private int corePoolSize;
	/**
	 * 	maxPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
	 */
	private int maxPoolSize;
	/**
	 * keepAliveTime：非核心线程闲置时间超时时长
	 */	
	private long keepAliveTime;
	/**
	 * workQueue：等待队列，存储还未执行的任务
	 */
	private BlockingQueue<Runnable> workQueue;
	/**
	 * threadFactory：线程创建的工厂
	 */
	private ThreadFactory threadFactory;
	/**
	 * handler：异常处理机制
	 */
	private RejectedExecutionHandler handler;	
	
	private float blockingCoefficient;
	/**
	 * 设置初始池大小，默认0
	 * 
	 * @param corePoolSize 初始池大小
	 * @return this
	 */
	public ExecutorBuilder setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}
	/**
	 * 设置阻尼系数，默认0
	 * 
	 * @param corePoolSize 初始池大小
	 * @return this
	 */
	public ExecutorBuilder setBlockingCoefficient(float blockingCoefficient) {
		this.blockingCoefficient = blockingCoefficient;
		return this;
	}
	/**
	 * 设置最大池大小（允许同时执行的最大线程数）
	 * 
	 * @param maxPoolSize 最大池大小（允许同时执行的最大线程数）
	 * @return this
	 */
	public ExecutorBuilder setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}	
	/**
	 * 设置线程存活时间，既当池中线程多于初始大小时，多出的线程保留的时长，单位纳秒
	 * 
	 * @param keepAliveTime 线程存活时间，单位纳秒
	 * @return this
	 */
	public ExecutorBuilder setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
		return this;
	}	
	/**
	 * 设置队列，用于存在未执行的线程<br>
	 * 可选队列有：
	 * <pre>
	 * 1. SynchronousQueue    它将任务直接提交给线程而不保持它们。当运行线程小于maxPoolSize时会创建新线程
	 * 2. LinkedBlockingQueue 无界队列，当运行线程大于corePoolSize时始终放入此队列，此时maximumPoolSize无效
	 * 3. ArrayBlockingQueue  有界队列，相对无界队列有利于控制队列大小，队列满时，运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
	 * </pre>
	 * 
	 * @param workQueue 队列
	 * @return this
	 */
	public ExecutorBuilder setWorkQueue(BlockingQueue<Runnable> workQueue) {
		this.workQueue = workQueue;
		return this;
	}
	/**
	 * 设置线程工厂，用于自定义线程创建
	 * 
	 * @param threadFactory 线程工厂
	 * @return this
	 */
	public ExecutorBuilder setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
		return this;
	}	
	/**
	 * 设置当线程阻塞（block）时的异常处理器，所谓线程阻塞既线程池和等待队列已满，无法处理线程时采取的策略
	 * <p>
	 * 此处可以使用JDK预定义的几种策略
	 * 
	 * @param handler {@link RejectedExecutionHandler}
	 * @return this
	 */
	public ExecutorBuilder setHandler(RejectedExecutionHandler handler) {
		this.handler = handler;
		return this;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public BlockingQueue<Runnable> getWorkQueue() {
		return workQueue;
	}

	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public RejectedExecutionHandler getHandler() {
		return handler;
	}
	
	public float getBlockingCoefficient() {
		return blockingCoefficient;
	}
}
