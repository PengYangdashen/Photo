package com.example.contact.thread;

import java.util.concurrent.TimeUnit;

/**
 * 线程-休眠工具类
 */
public class QuickSleep {

	/**
	 * 挂起当前线程
	 * 
	 * @param millis 挂起的毫秒数
	 * @return 被中断返回false，否则true
	 */
	public static boolean sleep(Number millis) {
		if (millis == null) {
			return true;
		}
		try {
			Thread.sleep(millis.longValue());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	

	/**
	 * 考虑{@link Thread#sleep(long)}方法有可能时间不足给定毫秒数，此方法保证sleep时间不小于给定的毫秒数
	 * @see ThreadUtil#sleep(Number)
	 * @param millis 给定的sleep时间
	 * @return 被中断返回false，否则true
	 */
	public static boolean safeSleep(Number millis) {
		long millisLong = millis.longValue();
		long done = 0;
		while (done < millisLong) {
			long before = System.currentTimeMillis();
			if (false == sleep(millisLong - done)) {
				return false;
			}
			long after = System.currentTimeMillis();
			done += (after - before);
		}
		return true;
	}
	/**
	 * 挂起当前线程
	 * 
	 * @param seconds 挂起的秒数
	 * @return 被中断返回false，否则true
	 */
	public static boolean sleepTimeUnit(Number seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds.longValue());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 挂起当前线程
	 * 
	 * @param timeout 挂起的时长
	 * @param timeUnit 时长单位
	 * @return 被中断返回false，否则true
	 */
	public static boolean sleepTimeUnit(Number timeout, TimeUnit timeUnit) {
		try {
			timeUnit.sleep(timeout.longValue());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

		
}
