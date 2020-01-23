package com.psg.ramasubramani;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLocking
{
	public static void main( String[] args )
	{
		ExecutorService executor = Executors.newFixedThreadPool( 2 );
		BlockedMutex mutex = new BlockedMutex();
		executor.submit( new OddCounter( mutex ) );
		executor.submit( new EvenCounter( mutex ) );
		executor.shutdown();
		executor.shutdownNow();
	}
}

class BlockedMutex
{
	private Lock mutexLock = new ReentrantLock();

	//Two counters are mutually exclusive
	public void oddCounter()
	{
		try {
			mutexLock.lock();
			/**
			 * Blocked mutex of this instance has been locked. oddcounter() , 		
			 * evencounter() any one of the methods acquire lock. The other method has to 
			 * wait until one finishes its execution	
			 */
			for ( int i = 1; i <= 10; i += 2 )
			{
				System.out.println( "Odd Counter Method : " + i );
			}
		} finally {
			//Always release locks in finally. This ensures locks are
			//released always.
			mutexLock.unlock();
		}
	}

	public void evenCounter()
	{
		try {
			mutexLock.lock();
			for ( int i = 0; i <= 10; i += 2 )
			{
				System.out.println( "Even Counter Method : " + i );
			}
		}
		finally {
			mutexLock.unlock();
		}
	}
}

class OddCounter implements Runnable
{
	BlockedMutex mutex = null;

	public OddCounter( BlockedMutex mutex )
	{
		this.mutex = mutex;
	}

	@Override
	public void run()
	{
		mutex.oddCounter();
	}

}

class EvenCounter implements Runnable
{
	BlockedMutex mutex = null;

	public EvenCounter( BlockedMutex mutex )
	{
		this.mutex = mutex;
	}

	@Override
	public void run()
	{
		mutex.evenCounter();
	}

}