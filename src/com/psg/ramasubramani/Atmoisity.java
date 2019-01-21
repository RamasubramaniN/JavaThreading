package com.psg.ramasubramani;

import java.util.concurrent.TimeUnit;

//When to use threads?
//1) Threads are very useful in Responsive UI, (Ex) we could have pressed calculate button in a screen, but it is time
//consuming operation and UI will not respond if application is implemented as single threaded,
//it will respond only when calculate finishes
//Create a thread, which sends interrupt to the above thread, introduce stop button in UI.

//2)Do not use threads in single processor environment and none of 
//your operation in the task is blocking operation, context switching is overhead.
//But if your task blocks for I/O you can still perform some other operation during waiting time, 
//so you can use threads even in single processor systems only if you identify blocking operation in the task

//In a multiprocessor environment obviously we can go for multiple threads, divide the tasks into many parts.
//Each part will be executed by a separate thread, each processor will take one or more threads, increased throughput

//How to avoid starvation?
//If it happens because of deadlock then, we should have handled proper cyclic dependencies in our code very carefully
//Don't play with thread priorities. Don't use priorities only. It may make the lower priority threads for a long time
//Visibility may be a reason, One thread updates its status but another thread is unable to read this status because changes are
//not visible to it. so, better use volatile in such cases
//Use reentrant lock with fairness factor so that lock will be release to proper threads
//Increase the number of processors to avoid starvation

/**
 * 
1) New

The thread is in new state if you create an instance of Thread class but before the invocation of start() method.

2) Runnable

The thread is in runnable state after invocation of start() method, but the thread scheduler has not selected it to be the running thread.

3) Running

The thread is in running state if the thread scheduler has selected it.

4) Non-Runnable (Blocked)

This is the state when the thread is still alive, but is currently not eligible to run.

5) Terminated

A thread is in terminated or dead state when its run() method exits.
 *
+ */
public class Atmoisity
{

	public static void main( String[] args ) throws InterruptedException
	{
		long i = 1;
		Atomic atomic = new Atomic();
		Thread t = new Thread( atomic );
		t.start();
		while ( i <= 100000000 )
		{
			atomic.printValues(); //prints non even values? 
			//No. because the field is thread safe only if both getters and setters are synchronized, 
			//here printValues() is not synchronized
			i++;
		}
		TimeUnit.SECONDS.sleep( 10 );

		System.out.println( "Proper synchronization" );
		i = 1;
		while ( i <= 2000000 )
		{
			atomic.printValues();
			i++;
		}
		t.interrupt();//Throws interrupted exception
	}

}

class Atomic implements Runnable
{
	private int i = 0;
	private volatile boolean stop = false;

	@Override
	public void run()
	{
		while ( !stop )
		{
			evenIncrement();
			try
			{
				TimeUnit.MILLISECONDS.sleep( 1 );
			}
			catch ( InterruptedException e )
			{
				stop = true;
				System.out.println( "Thread exits" );
			}
		}
	}

	private synchronized void evenIncrement()
	{
		i++;
		i++;
	}

	void printValues()
	{
		if ( i % 2 != 0 )
			System.out.println( i );
	}

}

class SynchronizedTask implements Runnable
{
	private int i = 0;
	private volatile boolean stop = false;

	@Override
	public void run()
	{
		while ( !stop )
		{
			evenIncrement();
			try
			{
				TimeUnit.MILLISECONDS.sleep( 1 );
			}
			catch ( InterruptedException e )
			{
				stop = true;
				System.out.println( "Thread exits" );
			}
		}
	}

	private synchronized void evenIncrement()
	{
		i++;
		i++;
	}

	public synchronized void printValues()
	{
		if ( i % 2 != 0 )
			System.out.println( i );
	}

}