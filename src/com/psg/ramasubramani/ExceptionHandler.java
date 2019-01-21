package com.psg.ramasubramani;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ExceptionHandler
{
	//execute vs submit
	//execute cannot return anything,execute can take only Runnable. But submit can return,when we use Callable we will return values,
	//Submit is applicable for the classes which implement callable
	//so, we need to use submit, submit return Future Generics.
	//Future<String> s = executors.submit(new CallableClass());
	//we can stop particular thread started by using future reference. 
	//s.cancel() sends interrupt to only the particular thread and not to other threads started by the executors
	//But if we use executors. executor.ShutdownNow() interrupts all the threads started by the particular executors, 
	//but it is impossible to interrupt specific thread in this case.
	public static void main( String[] args )
	{
		//ExecutorService executors = Executors.newCachedThreadPool(); //Returns thread pool executor extends AbstractExecutorService
		ExecutorService executors = Executors.newCachedThreadPool( new MyThreadFactory() );
		executors.execute( new RunnableType() );
		executors.execute( new RunnableType() );
		executors.execute( new RunnableType() );
		executors.execute( new RunnableType() );
		executors.execute( new RunnableType() );
	}
}

class RunnableType implements Runnable
{
	@Override
	public void run()
	{
		throw new RuntimeException("Exception");
	}
}

class MyThreadFactory implements ThreadFactory
{
	private static int i = 0;

	@Override
	public Thread newThread( Runnable r )
	{
		i++;
		Thread thread = new Thread( r );
		thread.setName( "Thread - " + i );
		if ( i % 2 == 1 )
			Thread.setDefaultUncaughtExceptionHandler( new OddUnCaughtExceptionHandler() );
		else if ( i % 2 == 0 )
			thread.setUncaughtExceptionHandler( new EvenUncaughtExceptionHandler() );
		else if ( i > 4 )//this will never be executed
			thread.setUncaughtExceptionHandler( new DefaultUnCaughtExceptionHandler() );
		return thread;
	}
}

class EvenUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	@Override
	public void uncaughtException( Thread t, Throwable e )
	{
		System.out.println( "Even Exception Handler" );
		System.out.println( t.getName() );
		System.out.println( e.getMessage() );
	}
}

class OddUnCaughtExceptionHandler implements UncaughtExceptionHandler
{

	@Override
	public void uncaughtException( Thread t, Throwable e )
	{
		System.out.println( "Odd Exception Handler" );
		System.out.println( t.getName() );
		System.out.println( e.getMessage() );
	}

}

class DefaultUnCaughtExceptionHandler implements UncaughtExceptionHandler
{

	@Override
	public void uncaughtException( Thread t, Throwable e )
	{
		System.out.println( "Default Exception Handler" );
		System.out.println( t.getName() );
		System.out.println( e.getMessage() );
	}

}