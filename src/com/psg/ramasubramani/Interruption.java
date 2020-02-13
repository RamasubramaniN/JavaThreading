package com.psg.ramasubramani;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author rn5
 *
 */
public class Interruption {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		SmallTask smallTask = new SmallTask();
		Thread thread = new Thread(smallTask);
		thread.start();
		TimeUnit.SECONDS.sleep(1);
		thread.interrupt();
		
		
		ExecutorTask executorTask = new ExecutorTask();
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(executorTask);
		TimeUnit.SECONDS.sleep(1);
		executorService.shutdownNow();
		TimeUnit.SECONDS.sleep(5);
	}
}

class SmallTask implements Runnable {
	private int count = 0;

	@Override
	public void run() {
		boolean x = false;
		while (!x) {
			x = Thread.interrupted(); // Thread is interrupted even though there is no wait/sleep methods.
			System.out.println("Thread : " + count++);
		}
		System.out.println("*****************Thread Done********************" + x);// True
	}

}

class ExecutorTask implements Runnable {
	private int count = 0;

	@Override
	public void run() {
		boolean x = false;
		while (!x) {
			x = Thread.interrupted(); // Thread is interrupted even though there is no wait/sleep methods.
			System.out.println("Executor Service : " + count++);
		}
		System.out.println("*****************Executor Service Done********************" + x);// True
	}

}
