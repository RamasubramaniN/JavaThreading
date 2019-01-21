package com.psg.ramasubramani;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//wait, notify and notifyall methods are in object class because implicit monitor is applicable for all kinds of class instances.
//Sleep will not release lock, but wait releases the lock. So, wait can be called only in synchronized context but sleep need not.
//Callable can return values and runnable cannot return any value, runnable cannot throw checked exceptions but callable can
//So, Callable can be used to communicate the exception to the parent thread.
//You can return task status from each callable and based on that you can decide to kill all other tasks or to continue. i.e. in some cases
//even if part of the task fails you want to fail the complete task and start from beginning.
public class CallableExample
{
	public static void main( String args[] ) throws InterruptedException, ExecutionException, TimeoutException
	{
		System.out.println("We use callable to return values from a thread");
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<String> future = exec.submit( new E() );
		System.out.println( future.get( 1000, TimeUnit.MILLISECONDS ) ); //waits for 1 second, if the task is not completed it throws timeout exception
		/**
		 * future.get()
		 * Throws:CancellationException - if the computation was cancelled
		 * ExecutionException - if the computation threw an exception
		 * InterruptedException - if the current thread was interrupted while waiting
		 * TimeoutException - if the wait timed out
		 */
		
		//If you want callable not to return anything you can use runnable or your can use Void return type.
		Future<Void> voidFuture = exec.submit( new F() );
		voidFuture.isDone(); // checks whether task has been completed or not, because get() blocks execution until the task is over, 
		//so better use isdone() and then get the result.
		System.out.println( voidFuture.get( 1000, TimeUnit.MILLISECONDS ) );
		
		//You can even return exception from the callable if your logic is based on the exception
		Future<Exception> exceptionFuture = exec.submit( new H() );
		System.out.println(exceptionFuture.get());
	}
}

class E implements Callable<String>
{

	static int count = 0;
	String returnValue = null;

	@Override
	public String call()
	{
		count++;
		try
		{
			TimeUnit.MILLISECONDS.sleep( 500 );//if we give more than one second delay, it will throw timeout exception in main, 
			//because there we wait maximum for one second to get the result from the callable
			System.out.println( "Name of the thread : " + Thread.currentThread().getName() );
			System.out.println( "Priority of the thread : " + Thread.currentThread().getPriority() );
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnValue = "E" + count;
		return returnValue;
	}

}

class F implements Callable<Void>
{
	static int count = 0;

	@Override
	public Void call()
	{
		for ( ; count <= 10; count++ )
		{
			System.out.println( "Callable with void type : " + count );
		}
		return null;
	}

}
class H implements Callable<Exception>
{
	@Override
	public Exception call() throws Exception
	{
		return new Exception();
	}
}