package com.psg.ramasubramani;

import java.util.concurrent.TimeUnit;

public class ChildThreads
{

	public static void main( String[] args ) throws InterruptedException
	{
		Thread t = new Thread( new Parent( new NestedObject() ) );
		t.setDaemon( true );
		t.start();
		System.out.println( t.isDaemon() );
		TimeUnit.MILLISECONDS.sleep( 10000 );
	}
}

//When a daemon thread creates threads, the child threads are also daemon.
//When a thread is created inside another thread, then, child thread inherits group, priority and isDaemon properties from its parent
//These things are performed inside THREAD CONSTRUCTOR --> INIT () method.
//so if parent is daemon, all child threads created by the parent will also be daemon, 
//If you want normal thread then you have to explicitly set the child's isDaemon property.

class Parent implements Runnable
{
	NestedObject nestedObject;
	Thread thread;

	Parent( NestedObject child )
	{
		this.nestedObject = child;
	}

	@Override
	public void run()
	{
		System.out.println( "Parent Thread : " + Thread.currentThread().getPriority() );
		System.out.println( "Is Parent thread daemon ?" + ( Thread.currentThread().isDaemon() ? "Yes" : "No" ) );
		thread = new Thread( nestedObject );
		thread.start();
	}

}

class NestedObject implements Runnable
{

	@Override
	public void run()
	{
		if ( Thread.currentThread().isDaemon() )
		{
			System.out.println( "Child Thread : " + Thread.currentThread().getPriority() );
			System.out.println( "Parent and child both are daemon" );
		}
		else
		{
			System.out.println( "Parent is daemon but child is not daemon" );
		}
	}

}
