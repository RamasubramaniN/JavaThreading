package com.psg.ramasubramani;

import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.*;

/*
 * CountDownLatch works in latch principle,  main thread will wait until Gate is open. 
 * One thread waits for n number of threads specified while creating CountDownLatch in Java. 
 * Any thread, usually main thread of application,  which calls CountDownLatch.await() will 
 * wait until count reaches zero or its interrupted by another Thread. All other thread are required
 * to do count down by calling CountDownLatch.countDown() once they are completed. 
 * As soon as count reaches zero, Thread awaiting starts running. One of the
 * disadvantage of CountDownLatch is that its not reusable once count reaches to zero 
 * you cannot use CountDownLatch any more, but don't worry Java concurrency API has another
 * concurrent utility called CyclicBarrier for such requirements.
 */

//Performs some portion of a task. Each task does its job also reduces counter value by 1.
//Waiting task will wait for all 10 tasks to complete.
class TaskPortion implements Runnable
{
	private static int counter = 0;
	private final int id = counter++;
	private static Random rand = new Random( 47 );
	private final CountDownLatch latch;

	public TaskPortion( CountDownLatch latch )
	{
		this.latch = latch;
	}

	public void run()
	{
		try
		{
			doWork();
			latch.countDown();
		}
		catch ( InterruptedException ex )
		{
			//Acceptable way to exit
		}
	}

	public void doWork() throws InterruptedException
	{
		TimeUnit.MILLISECONDS.sleep( rand.nextInt( 2000 ) );
		System.out.println( this + " completed" );
	}

	public String toString()
	{
		return String.format( "%1$-3d ", id );
	}
}

//Waits on the CountDownLatch. This task will be executed after all 10 dependent tasks
//Complete. Each dependent task will reduce the counter by 1. once counter reaches 0
//this task will resume its operation.
class WaitingTask implements Runnable
{
	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;

	public WaitingTask( CountDownLatch latch )
	{
		this.latch = latch;
	}

	public void run()
	{
		try
		{
			System.out.println("This task will wait for other tasks to complete.");
			//Wait till counter becomes zero. i.e. Wait till all dependent tasks complete.
			latch.await(); 
			System.out.println( "Other tasks completed. Latch reaches Zero. "
					+ "Resuming waiting task. Crossed Latch barrier. " + this );
		}
		catch ( InterruptedException ex )
		{
			System.out.println( this + " interrupted" );
		}
	}

	public String toString()
	{
		return String.format( "WaitingTask %1$-3d ", id );
	}
}

public class CountDownLatchDemo
{
	static final int SIZE = 7;

	public static void main( String[] args ) throws Exception
	{
		ExecutorService exec = Executors.newCachedThreadPool();
		//All must share a single CountDownLatch object.
		CountDownLatch latch = new CountDownLatch( SIZE );
		
		//Waiting task will wait until all TaskPortion jobs complete.
		exec.execute( new WaitingTask( latch ) );
		TimeUnit.SECONDS.sleep(3);
		
		for ( int i = 0; i < SIZE; i++ ) //Number of dependent jobs = count of countdownlatch
			exec.execute( new TaskPortion( latch ) );
		System.out.println( "Launched all tasks" );
		exec.shutdown(); // Quit when all tasks complete
	}
}

