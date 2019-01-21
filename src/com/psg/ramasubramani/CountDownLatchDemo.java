package com.psg.ramasubramani;

import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.*;

//Performs some portion of a task:
class TaskPortion implements Runnable
{
	private static int counter = 0;
	private final int id = counter++;
	private static Random rand = new Random( 47 );
	private final CountDownLatch latch;

	TaskPortion( CountDownLatch latch )
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
		System.out.println( this + "completed" );
	}

	public String toString()
	{
		return String.format( "%1$-3d ", id );
	}
}

//Waits on the CountDownLatch:
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
			latch.await(1000, TimeUnit.MILLISECONDS);
			System.out.println( "Latch barrier passed for " + this );
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
		//All must share a single CountDownLatch object:
		CountDownLatch latch = new CountDownLatch( SIZE );
		for ( int i = 0; i < 10; i++ )
			exec.execute( new WaitingTask( latch ) );
		for ( int i = 0; i < SIZE; i++ )
			exec.execute( new TaskPortion( latch ) );
		System.out.println( "Launched all tasks" );
		exec.shutdown(); // Quit when all tasks complete
	}
}

