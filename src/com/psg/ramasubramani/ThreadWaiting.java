package com.psg.ramasubramani;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadWaiting
{
	public static void main( String[] args )
	{
		Car car = new Car();
		ExecutorService executor = Executors.newFixedThreadPool( 2 );
		executor.submit( new Waxing( car ) );
		executor.submit( new Polishing( car ) );
		executor.shutdown();//No more tasks to this thread pool

		try
		{
			Thread.sleep( 10000 );//milliseconds.
		}
		catch ( InterruptedException e )
		{
		}
		executor.shutdownNow();
	}
}

class Car
{
	public volatile boolean controlFlag = false;

	synchronized void doWaxing()
	{
		controlFlag = true;
		System.out.println( "Doing Waxing" );
		notify();
	}

	synchronized void waitForPolishing()
	{/**
	* Dont put if, use while loop. Dont use notify always use notifyall(), 
	* inform all the remaining threads, only the thread which satisfies 
	* the condition come out of while loop and starts doing its task 
	* because randomly waking up some tasks is not a good approach.
	*/
		while ( controlFlag )
		{
			try
			{
				wait();
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}

	synchronized void waitForWaxing()
	{
		while ( !controlFlag )
		{
			try
			{
				wait();
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}

	synchronized void doPolishing()
	{
		controlFlag = false;
		System.out.println( "Doing Polishing" );
		notify();
	}
}

class Waxing implements Runnable
{
	private Car car = null;

	Waxing( Car car )
	{
		this.car = car;
	}

	@Override
	public void run()
	{
		while ( !Thread.interrupted() )
		{
			//'Thread.interrupted()' Tests whether the current thread has been interrupted. 
			//The interrupted status of the thread is cleared by this method.
			//In other words, if this method were to be called twice in succession, 
			//the second call would return false 
			//(unless the current thread were interrupted again, after the first call 
			//had cleared its interrupted status 
			//and before the second call had examined it). 

			//We can use 'Thread.isInterrupted()' also, 
			//this will not clear the status of the flag.
			//The thread throws interrupted exception only when the thread is waiting() 
			//or sleeping() or waiting to join()
			//When there is no such statements inside our run() method, 
			//this exception will never be thrown, 
			//but 'Thread.interrupted()' & 'Thread.isInterrupted()' will be set.
			
			//When a thread throws interrupted exception, inside the catch block 
			//always 'Thread.interrupted()' will be false.
			//ie the flag value will be cleared, in such a situation you need to 
			//use volatile variables to indicate the thread is interrupted,
			//and use this variable in the while() clause of run() method
			car.doWaxing();
			car.waitForPolishing();
		}
	}
}

class Polishing implements Runnable
{
	private Car car = null;

	Polishing( Car car )
	{
		this.car = car;
	}

	@Override
	public void run()
	{
		while ( !Thread.interrupted() )
		{
			//'Thread.interrupted()' Tests whether the current thread has been interrupted. 
			//The interrupted status of the thread is cleared by this method.
			//In other words, if this method were to be called twice in succession, 
			//the second call would return false 
			//(unless the current thread were interrupted again, after the first 
			//call had cleared its interrupted status 
			//and before the second call had examined it). 
			car.waitForWaxing();
			car.doPolishing();
		}
	}
}
