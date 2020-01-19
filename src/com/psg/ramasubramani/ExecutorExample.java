package com.psg.ramasubramani;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorExample
{

	public static void main( String[] args )
	{
		System.out.println("**********Cached Thread Pool**********");
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.submit( new ABC() );
		try
		{
			exec.shutdown();
			//Initiates an orderly shutdown in which previously submitted tasks will continue to execute, 
			//but no new tasks will be accepted. 
			//Invocation has no additional effect if already shut down. 
			// This will shut down the pool, so, after this statement
			//whatever the tasks we submit to this executor will not be executed by this executor.
			//So, new tasks we have blocked, and you cannot add any new tasks to the the pool.
		}
		catch ( Exception e )
		{

		}
		finally
		{

		}
		try
		{
			exec.execute( new B() );
			//pool is shut down, so this task wont be added to the thread pool. This throws exception
		}
		catch ( Exception e )
		{

		}
		exec.shutdownNow();
		// This is the statement which terminates all the threads. (ie) it is not exactly terminating
		//It interrupts all the threads started by this executor via cancel i.e through interrupts.  
		//Then it is our implementation, to stop the threads.
		
		ExecutorService exe = java.util.concurrent.Executors.newFixedThreadPool( 2 );
		System.out.println("**********Fixed Thread Pool**********");
		exe.submit( new ABC() );
		//exe.execute( new B() );
		exe.submit( new ABC() );
		//exe.submit( new ABC() );
		//exe.submit( new ABC() );
	}

}

class ABC implements Callable<Void>
{
	static int instanceCount = 0;
	int count = 0;

	@Override
	public Void call()
	{
		instanceCount++;
		while ( count <= 10 )
		{
			System.out.println( Thread.currentThread().getName() + " : " + ++count );
		}
		return null;
	}

}

class B implements Runnable
{
	static int instanceCount = 1;
	int count = 0;

	@Override
	public void run()
	{
		instanceCount++;
		while ( count <= 10 )
		{
			System.out.println( "Class B : Instance : " + instanceCount + " : " + ++count );
		}
	}
}