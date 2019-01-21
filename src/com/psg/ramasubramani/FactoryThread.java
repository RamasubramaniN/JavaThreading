package com.psg.ramasubramani;

import java.util.concurrent.ThreadFactory;

//Don't follow this approach, use executors, it takes threadfactory as arguments,
//It takes care of thread creation using this thread factory
public class FactoryThread
{
	static int i = 0;
	static ThreadGenerator h = new ThreadGenerator();

	public static void main( String[] args )
	{
		while ( i <= 10 )
		{
			i++;
			Thread thread = h.newThread( new J() );
			thread.start();
		}
		System.out.println("Main thread's priority : " + Thread.currentThread().getPriority());//5
	}
}

class ThreadGenerator implements ThreadFactory
{
	static int i = 0;
	@Override
	public Thread newThread( Runnable r )
	{
		Thread t = new Thread( r, String.valueOf( ++i )); //Name of the thread
		//t.setDaemon( true ); in case if you want daemon threads to be created
		t.setPriority( 1 );//Never play with priority. your tasks will end up in starvation.
		return t;
	}
}

class J implements Runnable
{
	int count = 0;
	static int i = 0;

	@Override
	public void run()
	{
		while ( count <= 100 )
		{
			System.out.println( ++count );
			i++;
		}
		System.out.println( "Static : " + i );
	}
}
