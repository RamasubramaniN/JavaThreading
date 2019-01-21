package com.psg.ramasubramani;


public class ConsumerProducer
{
	// This program is incomplete, don't refer this
	/**
	 * wait - wait( ) tells the calling thread to give up the monitor and go to sleep until some other thread enters the same monitor 
	 * and calls notify( ).Object wait methods has three variance, one which waits indefinitely for any other thread to 
	 * call notify or notifyAll method on the object to wake up the current thread. Other two variances puts 
	 * the current thread in wait for specific amount of time before they wake up.
	 * 
	 * notify - notify method wakes up only one thread waiting on the object and that thread starts execution. 
	 * So if there are multiple threads waiting for an object, this method will wake up only one of them. 
	 * The choice of the thread to wake depends on the OS implementation of thread management.
	 * 
	 * notifyAll - notifyAll method wakes up all the threads waiting on the object, although which one will process first depends 
	 * on the OS implementation. The highest priority thread will run first.
	 * Sleep will not release lock, but wait releases the lock
	 * 
	 * Wait/notify/notifyall should be called on the shared resource where we deal with locks.
	 * lockObject.wait();
	 * lockObject.notify();
	 * lockObject.notifyAll();
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main( String[] args ) throws InterruptedException
	{
		Q q = new Q();
		new Producer( q );
		new Consumer( q );
	}

}

class Q
{
	int n;
	boolean valueSet = false;

	synchronized int get()
	{
		if ( !valueSet )
			try
			{
				wait();
			}
			catch ( InterruptedException e )
			{
				System.out.println( "InterruptedException caught" );
			}
		System.out.println( "Got: " + n );
		valueSet = false;
		notify();
		return n;
	}

	synchronized void put( int n )
	{
		if ( valueSet )
			try
			{
				wait();
			}
			catch ( InterruptedException e )
			{
				System.out.println( "InterruptedException caught" );
			}
		this.n = n;
		valueSet = true;
		System.out.println( "Put: " + n );
		notify();
	}
}

class Producer implements Runnable
{
	Q q;

	Producer( Q q )
	{
		this.q = q;
		new Thread( this, "Producer" ).start();
	}

	public void run()
	{
		int i = 1;
		while ( i <= 10 )
		{
			q.put( i++ );
		}
	}
}

class Consumer implements Runnable
{
	Q q;

	Consumer( Q q )
	{
		this.q = q;
		new Thread( this, "Consumer" ).start();
	}

	public void run()
	{
		while ( true )
		{
			q.get();
		}
	}
}