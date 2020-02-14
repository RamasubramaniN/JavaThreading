package com.psg.ramasubramani;

import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
* InterruptedException is a checked exception. So, you cannot have a catch block catching this exception unless any of your
* statements throwing this exception in try block. Sleep(), Wait() throws InterruptedException when a thread is interrupted.
* So, when you interrupt a thread only if any of sleep(), wait(), lock.interruptibly() methods used in try block, then
* Interrupted Exception will be thrown. -- One way to come out of while loop inside call/run method.
* 
* But what if you don't have sleep() or wait() call inside your run/call method. Check for Thread.Interrupted() 
* run () {
*	while(Thread.interrupted()) {
*          Business Logic
*	}
* }
* If you don't have a While loop in your subtask, periodically check Thread.interrupted() flag. Please note Thread.interrupted()
* call clears the flag. If you make 2 successive calls, after thread's interruption first call returns true but second call
* returns false.
*
* So, always use Thread.interrupted() as a safer way to figure out thread interruption even if run() has sleep/wait.
* You will also have catch (InterruptedException e) in this case.You can break out of the loop here by adding break; statement.
* Thread.interrupted() flag will be false inside  InterruptedException catch block. i.e.
* catch( InterruptedException e ) {
*	Thread.interrupted(); //always false because interrupted flag is cleared here.
* }
*
*/
public class ThreadInterruption
{
	public static void main( String[] args )
	{
		//System.out.println( "Enter 1 for Executor Routine, 2 for Thread Routine" );
		Scanner in = new Scanner( new InputStreamReader( System.in ) );
		while ( in.hasNextInt() )
		{
			int x = in.nextInt();
			if ( x == 1 )
			{
				System.out.println( "Executor Thread Pool Interrupt Mechanism" );
				ExecutorService executors = Executors.newCachedThreadPool();
				executors.execute( new RunnableGenerator( new OddGenerator() ) );
				executors.execute( new RunnableGenerator( new EvenGenerator() ) );
				executors.execute( new RunnableGenerator( new OddGenerator() ) );
				executors.execute( new RunnableGenerator( new EvenGenerator() ) );
				executors.execute( new RunnableGenerator( new OddGenerator() ) );
				executors.execute( new RunnableGenerator( new EvenGenerator() ) );
				try
				{
					Thread.sleep( 1000 ); //In milliseconds
				}
				catch ( InterruptedException e ) 
				{
					e.printStackTrace();
				}
				//No new tasks can be assigned.
				executors.shutdown();
				
				//Internally calls Thread.interrupt() on all the threads started by this particular 
				//executor. So each thread routine throws Interrupted exception
				executors.shutdownNow();
			}
			else if ( x == 2 )
			{
				System.out.println( "Thread Interrupt Mechanism" );
				Thread t1 = new Thread( new RunnableGenerator( new OddGenerator() ) );
				Thread t2 = new Thread( new RunnableGenerator( new EvenGenerator() ) );
				Thread t3 = new Thread( new RunnableGenerator( new OddGenerator() ) );
				Thread t4 = new Thread( new RunnableGenerator( new EvenGenerator() ) );
				Thread t5 = new Thread( new RunnableGenerator( new OddGenerator() ) );
				Thread t6 = new Thread( new RunnableGenerator( new EvenGenerator() ) );
				t1.start();
				t2.start();
				t3.start();
				t4.start();
				t5.start();
				t6.start();
				try
				{
					TimeUnit.SECONDS.sleep(2);
				}
				catch ( InterruptedException e ) //7483147657
				{
					e.printStackTrace();
				}
				t1.interrupt();
				t2.interrupt();
				t3.interrupt();
				t4.interrupt();
				t5.interrupt();
				t6.interrupt();
			}
			else
			{
				System.out.println( "Executor Individual Thread Interrupt Mechanism" );
				ExecutorService executors = Executors.newCachedThreadPool();
				Future< ? > future1 = executors.submit( new RunnableGenerator( new OddGenerator() ) );
				Future< ? > future2 = executors.submit( new RunnableGenerator( new EvenGenerator() ) );
				Future< ? > future3 = executors.submit( new RunnableGenerator( new OddGenerator() ) );
				Future< ? > future4 = executors.submit( new RunnableGenerator( new EvenGenerator() ) );
				Future< ? > future5 = executors.submit( new RunnableGenerator( new OddGenerator() ) );
				Future< ? > future6 = executors.submit( new RunnableGenerator( new EvenGenerator() ) );
				try
				{
					TimeUnit.SECONDS.sleep(2);
				}
				catch ( InterruptedException e ) //7483147657
				{
					e.printStackTrace();
				}
				executors.shutdown();
				future1.cancel( true );//Attempts to cancel execution of this task. 
				//This attempt will fail if the task has already completed, 
				//has already been cancelled, or could not be cancelled for some other reason. 
				//If successful, and this task has not started when cancel is 
				//called, this task should never run. If the task has already started, 
				//then the mayInterruptIfRunning parameter determines 
				//whether the thread executing this task should be interrupted 
				//in an attempt to stop the task. 
				future2.cancel( true ); //This is the way to stop individual 
				//threads in executor pool.
				future3.cancel( true );
				future4.cancel( true );
				future5.cancel( true );
				future6.cancel( true );
			}
		}
	}
}

class OddGenerator implements Marker
{
	private int id = idGenerator++;
	private int count = 1;
	public static int idGenerator = 1;

	public int getId()
	{
		return id;
	}

	//This method must be synchronized. 
	//i.e. Getters & Setters (generate()) should be synchronized.
	synchronized int getCount()
	{
		return count;
	}

	synchronized void generate()
	{
		count++;
		count++;
	}
}

class EvenGenerator implements Marker
{
	private int id = idGenerator++;

	public int getId()
	{
		return id;
	}

	private int count = 0;
	private static int idGenerator = 1;

	synchronized int getCount()
	{
		return count;
	}

	synchronized void generate()
	{
		count++;
		count++;
	}
}

class RunnableGenerator implements Runnable
{
	private Marker markerInstance = null;
	private volatile boolean isCancelled = false;

	RunnableGenerator( Marker marker )
	{
		this.markerInstance = marker;
	}

	@Override
	public void run()
	{
		while ( !isCancelled )
		{
			OddGenerator oddGenerator = null;
			EvenGenerator evenGenerator = null;
			try
			{
				if ( markerInstance instanceof OddGenerator )
				{
					oddGenerator = ( OddGenerator ) markerInstance;
					oddGenerator.generate();
					System.out.println( "Odd Generator : " + oddGenerator.getId() + "   count : " + oddGenerator.getCount() );
					Thread.sleep( 100 );
				}
				else if ( markerInstance instanceof EvenGenerator )
				{
					evenGenerator = ( EvenGenerator ) markerInstance;
					evenGenerator.generate();
					System.out.println( "Even Generator : " + evenGenerator.getId() + "   count : " + evenGenerator.getCount() );
					Thread.sleep( 100 );
				}
			}
			catch ( InterruptedException ie )
			{
				isCancelled = true;
				String generator = ( oddGenerator != null ? "Odd Generator" : "Even Generator" );
				int id = ( oddGenerator != null ? oddGenerator.getId() : evenGenerator.getId() );
				System.out.println( "Stopping Thread : " + generator + " - " + id );
			}
		}
	}

}

interface Marker
{

}
