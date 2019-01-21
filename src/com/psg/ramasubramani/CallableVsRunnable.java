package com.psg.ramasubramani;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableVsRunnable
{

	public static void main( String[] args )
	{
		//Runnable section
		Thread t = new Thread( new RunnableClass() );
		t.start();
		//Use Callable with Executors "submit" method. It will give the handle to task output,it can throw checked exceptions
		//(i.e.) it can communicate the exception to the parent threads.Here class should implement Callable interface.
		
		//Use Runnable with Executors "execute" method, if run() does not return any output.
		//Here class should implement Runnable interface.
		
		//Callable section
		//Thread t1 = new Thread( new CallableClass() ); -- syntax wrong
		//Callable can return the values to its future object
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<Exception> result = exec.submit( new CallableClass() );
		
		System.out.println( "Communicating the exception happened in the child threads to the parent thread" );
		try
		{
			System.out.println(result.get());
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( ExecutionException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class RunnableClass implements Runnable
{
	//There are two kinds of exception, checked and unchecked exception...
	//Checked exceptions need to be handled(compiler forces, else the code wont compile
	//Unchecked exceptions are not forced by the compiler to handle it, the code will compile successfully
	//hierarchy : 
	//Object --->
	//Throwable -->
	// 1)Errors, 2)Exception
	//2) Exception --> 1) Unchecked(RuntimeException) 2)Checked Exception
	@Override
	public void run()
	{
		int x;
		throw new RuntimeException();//this is unchecked exception, this is thrown here itself(you can see it in console) and this is not passed to the top thread.

		//throw new Exception() --> not possible , checked exceptions need to be handled, here it is  a thread's run method, so only way to handle
		//is by surrounding with try catch block, because when we use runnable, you can't return values to the caller of the thread(here main method)
		//This is done in the below class
	}

}

//NoclassDefFoundError vs ClassNotFoundException
//Compile any package. Delete one of its " .class " files. You will get NoclassDefFoundError. 
//Because compile time there were no issues reported, everything went fine.
//But during run time, a class file is missing. (Class is available at compile time but missing at run time). 
//This is an error/serious issue,needs to be resolved
//Use class.forName("com.sts.ts.MyClass"), here path is a string and there no compile time verification, at run time JVM searches 
//the specified class file in the specified path.
//If the class is not present in the specified location, you will get ClassNotFoundException.
// Not a serious issue, can fix it easily, just make class files available at run time in the specified location. 

class RunnableCheckedExcetpion implements Runnable
{

	@Override
	public void run()
	{
		int x; //checked exception. so need to be handled here itself, else code wont compile.
		//here, there is no option to pass the exception to the 
		try //upper thread also, because run cannot return any values
		{
			throw new Exception();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

}

class CallableClass implements Callable<Exception>
{
	@Override
	public Exception call() throws Exception
	{
		return new Exception();
	}
}