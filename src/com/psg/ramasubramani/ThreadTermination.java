package com.psg.ramasubramani;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTermination
{
	public static void main( String[] args ) throws InterruptedException
	{
		
		Thread t = new Thread(new MyRunnableClass());
		System.out.println("Just after creating thread; \n" + 
				"	The thread state is: " + t.getState()); 
		t.start();
		System.out.println("Just after calling t.start(); \n" + 
				"	The thread state is: " + t.getState());
		t.join(); //Waiting for other threads to complete in Thread model.
		System.out.println("Just after main calling t.join(); \n" + 
				"	The thread state is: " + t.getState());
		
		ExecutorService executor = Executors.newFixedThreadPool( 5 );
		for ( int i = 1; i <= 5; i++ )
			executor.execute( new MyRunnableClass() );
		Thread.sleep( 1 );
		
		executor.shutdown();
		//Waiting for other threads to join in Executor model.
		executor.awaitTermination( 1, TimeUnit.MICROSECONDS );
		//Equivalent to join in thread...Blocks until all tasks have completed execution 
		//after a shutdown request, or the timeout occurs, or the current thread is 
		//interrupted, whichever happens first.
	}
}

class MyRunnableClass implements Runnable
{

	@Override
	public void run()
	{
		int i = 0;
		while ( i < 10 )
		{
			try
			{
				TimeUnit.SECONDS.sleep( 1 );
				System.out.println( Thread.currentThread().getName() + " - " + ++i );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
		System.out.println( Thread.currentThread().getName() + " is stopping. Last counter value : " + i );
	}
}
