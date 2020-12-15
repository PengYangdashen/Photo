package com.example.contact.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CallExecutor {
	
	private CallExecutor() {}

	/**
	 * 直接在公共线程池中执行线程
	 * 
	 * @param runnable 可运行对象
	 */
	public static void execute(Runnable runnable) {
		execute(GlobalExecutor.getExecutor(),runnable);
	}
	
	/**
	 * 需要执行线程
	 * 
	 * @param runnable 可运行对象
	 */
	public static void execute(ExecutorService executor,Runnable runnable) {
		executor.execute(runnable);
	}

	/**
	 * 执行异步方法
	 * 
	 * @param runnable 需要执行的方法体
	 * @param isDeamon 是否守护线程。守护线程会在主线程结束后自动结束
	 * @return 执行的方法体
	 */
	public static Runnable excAsync(final Runnable runnable, boolean isDeamon) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				runnable.run();
			}
		};
		thread.setDaemon(isDeamon);
		thread.start();
		return runnable;
	}
	
	/**
	 * 执行有返回值的异步方法<br>
	 * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
	 * 
	 * @param <T> 回调对象类型
	 * @param task {@link Callable}
	 * @return Future
	 */
	public static <T> Future<T> execAsync(Callable<T> task) {
		return GlobalExecutor.getExecutor().submit(task);
	}

	/**
	 * 执行有返回值的异步方法<br>
	 * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
	 * 
	 * @param runnable 可运行对象
	 * @return {@link Future}
	 * @since 3.0.5
	 */
	public static Future<?> execAsync(Runnable runnable) {
		return GlobalExecutor.getExecutor().submit(runnable);
	}

	/**
	 * 需要并发同步使用
	 * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。<br>
	 * 若未完成，则会阻塞
	 * @param <T> 回调对象类型
	 * @return CompletionService
	 */
	public static <T> CompletionService<T> concurrentSync() {	
		return new ExecutorCompletionService<T>(GlobalExecutor.getExecutor());
	}
	
	/**
	 * 需要并发同步使用
	 * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。<br>
	 * 若未完成，则会阻塞
	 * @param <T> 回调对象类型
	 * @param executor 执行器 {@link ExecutorService}
	 * @return CompletionService
	 */
	public static <T> CompletionService<T> concurrentSync(ExecutorService executor) {
		return new ExecutorCompletionService<T>(executor);
	}
	
	
	
}
