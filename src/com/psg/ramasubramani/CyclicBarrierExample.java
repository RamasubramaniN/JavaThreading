package com.psg.ramasubramani;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ramasubramani
 * 
 * CyclicBarrier
 * --------------
 * A CyclicBarrier is a synchronization aid that allows a set of threads to all wait for each other 
 * to reach a common barrier point. CyclicBarriers are useful in programs involving a fixed sized 
 * party of threads that must occasionally wait for each other. The barrier is called cyclic 
 * because it can be re-used after the waiting threads are released. 
 * 
 * CountDownLatch Vs CyclicBarrier
 * ---------------------------------
 * CountDownLatch is a synchronization aid that allows one or more threads to wait until a set of 
 * operations being performed in other threads completes. CyclicBarrier is a synchronization aid 
 * that allows a set of threads to all wait for each other to reach a common barrier point.

 */
public class CyclicBarrierExample {
	
	private static int TOTAL_SUB_TASKS = 10;

	public static void main(String[] args) throws InterruptedException {
		GlobalSum globalSum = new GlobalSum();
		CyclicBarrier cyclicBarrier = new CyclicBarrier(TOTAL_SUB_TASKS);
		ExecutorService executors = Executors.newFixedThreadPool(TOTAL_SUB_TASKS);
		
		//1 + 2 + 3 + ... + 10 = 55. Idea is to wait for all threads to update global sum.
		// Wait for all subtasks to reach a common barrier point and then 
		//resume all threads from the point. Global sum before the barrier will be random.
		//after the barrier = 55.
		for(int i = 1; i <= TOTAL_SUB_TASKS; i++) {
			SubTask subTask = new SubTask(globalSum, cyclicBarrier);
			executors.execute(subTask);
		}
		TimeUnit.SECONDS.sleep(5);
		executors.shutdownNow();
	}
}

class SubTask implements Runnable {
	
	private static int count = 1;
	private int threadId = count++;
	
	private GlobalSum globalSum;
	private CyclicBarrier cyclicBarrier;
	
	public SubTask(GlobalSum globalSum, CyclicBarrier cyclicBarrier) {
		this.globalSum = globalSum;
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		globalSum.updateSum(threadId);//Step1
		try {
			System.out.println("Thread "+ threadId + " updated global sum. Value : " + globalSum.getSum()
					+ " Waiting for other threads/subtasks to complete. Waiting in the barrier.");
			
			//All 10 threads completes step1 and wait for each other to reach this point.
			//new CyclicBarrier(TOTAL_SUB_TASKS) --> Once waiting count becomes TOTAL_SUB_TASKS
			//all threads cross this point and execute remaining of run method.
			cyclicBarrier.await();
			
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("Thread " + threadId + " crossed the barrier. "
				+ "Global Sum : " + globalSum.getSum());
	}
}

class GlobalSum {
	
	private int sum;
	
	public synchronized void updateSum(int value) {
		this.sum += value;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
}
