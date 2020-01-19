package com.psg.ramasubramani;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//wait, notify and notifyall methods are in object class because implicit monitor 
//is applicable for all kinds of class Objects. Not for primitive types. 
//Sleep() will not release lock and it just blocks the current thread, 
//but wait() releases the lock/monitor, so, it has to own a monitor. So, wait can be called only 
//in synchronized context (i.e. Inside a Synchronized method/block, wait() throws 
//java.lang.IllegalMonitorStateException if it is called inside a non-synchronized context) but sleep need not.
//Callable can return values and Runnable cannot return any value, 
//Callable can throw checked exceptions but Runnable cant. i.e. call method has a return type.
//So, Callable can be used to communicate the exception to the parent thread.
//You can return task status from each callable and based on that you can decide 
//to kill all other tasks or to continue. i.e. In some cases
//even if part of the task fails you want to fail the complete task and start from beginning.
public class CallableExample
{
	public static void main( String args[] ) throws InterruptedException, ExecutionException, TimeoutException
	{
		System.out.println("We use callable to return values from a thread");
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<String> future = exec.submit( new E() );
		
		//waits for 1 second, if the task is not completed it throws timeout exception. Blocking call.
		System.out.println( future.get( 1000, TimeUnit.MILLISECONDS ) ); 
		/**
		 * future.get() - Get the return value from the execution.
		 * Throws:CancellationException - if the computation was cancelled
		 * ExecutionException - if the computation threw an exception
		 * InterruptedException - if the current thread was interrupted while waiting
		 * TimeoutException - if the wait timed out
		 */
		
		//If you want callable not to return anything you can use runnable or your can use Void return type.
		Future<Void> voidFuture = exec.submit( new F() );
		// checks whether task has been completed or not, because get() blocks execution until the task is over,
		voidFuture.isDone();  //so better use isdone() and then get the result.
		
		System.out.println( voidFuture.get( 1000, TimeUnit.MILLISECONDS ) );
		//Thread thread = new thread(Only Runnable Allowed. No callable.)
		
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
			TimeUnit.MILLISECONDS.sleep( 500 );
			//if we give more than one second delay, it will throw timeout exception in main, 
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