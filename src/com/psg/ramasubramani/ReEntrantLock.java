package com.psg.ramasubramani;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntrantLock
{

	public static void main( String[] args )
	{
		//Synchronized
		//Here T1 throws exception, it exits after that ,so lock is released for the object printer.
		//Then T2 gets the lock and executes the critical section
		//Assume this exception is throws because of coding issue, so cleanup is not done properly
		
		//When we use synchronized method or synchronized block, if exception is thrown during run time lock is automatically released. 
		//This is taken care and assured functionality by JVM
		//But when we use explicit LOCKS, when runtime exception is thrown, lock won't be released automatically., 
		//you need to release it in finally block
		
		//When we use synchronized keyword it blocks the thread infinitely, no time limit.
		//But using explicit lock we can wait and try to acquire the lock for certain period time. If you are not able to acquire, 
		//you do other things. no blocking here.
		
		//Explicit locks are used in LINKEDLISTS where we need to acquire the next node's lock before releasing current node's lock
		
		//Javarevisited
/**

main difference between synchronized and ReentrantLock is ability to trying for lock interruptibly, and with timeout. 
Thread doesn’t need to block infinitely, which was the case with synchronized

1) Significant difference between ReentrantLock and synchronized keyword is fairness. synchronized keyword doesn't support fairness. 
 Any thread can acquire lock once released, no preference can be specified, on the other hand you can make ReentrantLock fair by specifying fairness 
 property, while creating instance of ReentrantLock. Fairness property provides lock to longest waiting thread, in case of contention.

2) Second difference between synchronized and Reentrant lock is tryLock() method. ReentrantLock provides convenient tryLock() method, which acquires 
lock only if its available or not held by any other thread. This reduce blocking of thread waiting for lock in Java application.

3) One more worth noting difference between ReentrantLock and synchronized keyword in Java is, ability to interrupt Thread while waiting for Lock. 
In case of synchronized keyword, a thread can be blocked waiting for lock, for an indefinite period of time and there was no way to control that. 
ReentrantLock provides a method called lockInterruptibly(), which can be used to interrupt thread when it is waiting for lock. Similarly tryLock() 
with timeout can be used to timeout if lock is not available in certain time period.

4) ReentrantLock also provides convenient method to get List of all threads waiting for lock.

 */
		Printer printer = new Printer();
		Thread t1 = new Thread( printer, "T1" );
		t1.start();
		Thread t2 = new Thread( printer, "T2" );
		t2.start();

		Writer writer = new Writer();
		Thread t3 = new Thread( writer, "T3" );
		t3.start();
		Thread t4 = new Thread( writer, "T4" );
		t4.start();
	}

}

class Printer implements Runnable
{
	@Override
	public void run()
	{
		print();
	}

	synchronized private void print()
	{
		int i = 0;
		while ( i < 30000 )
		{
			System.out.print( Thread.currentThread().getName() + " : " + i );
			if ( i == 100 )
			{
				throw new RuntimeException();
			}
			i++;
		}
	}

	synchronized private void type()
	{

	}

}

class Writer implements Runnable
{
//	/As the name says, ReentrantLock allow threads to enter into lock on a resource more than once. 
	//When the thread first enters into lock, a hold count is set to one. Before unlocking the thread can re-enter into lock again 
	//and every time hold count is incremented by one. For every unlock request, 
	//hold count is decremented by one and when hold count is 0, the resource is unlocked. 
	private Lock lock = new ReentrantLock( true ); //Creates an instance of ReentrantLock with the given fairness policy.

	@Override
	public void run()
	{
		print();
	}

	private void print()
	{
		try
		{
			//If you called a synchronized method which is already locked, it will wait indefinitely also you cannot interrupt. 
			//Things are different in Reentrantlock.
			lock.tryLock( 1, TimeUnit.SECONDS );//true if the lock was acquired and false if the waiting time elapsed before the lock was acquired
			lock.lockInterruptibly(); //Acquires the lock unless the current thread is interrupted. If the current thread: 
			//1) has its interrupted status set on entry to this method; 2) is interrupted while acquiring the lock, and interruption of lock acquisition 
			//is supported,then InterruptedException is thrown and the current thread's interrupted status is cleared.
			//nterruptible lock acquisition allows locking to be used within cancellable activities.
			//The lockInterruptibly method allows us to try and acquire lock while being available for interruption. So basically it means that; it allows the thread to immediately react to the interrupt signal sent to it from another thread.
			//This can be helpful when we want to send a KILL signal to all the waiting locks.
			//Lets see one example, suppose we have a shared line to send messages, we would want to design it in way that if another thread comes and interrupts the current thread should release the lock and perform the exit or shut down operations to cancel the current task.
		}
		catch ( InterruptedException e )
		{
			//InterruptedException thrown-if the current thread is interrupted while acquiring the lock (and interruption of lock acquisition is supported)
			e.printStackTrace();
		}  

		lock.lock();// do explicit lock here. Acquires the lock. If the lock is not available then the current thread becomes disabled for thread 
		//scheduling purposes and lies dormant 
		//until the lock has been acquired.
		try
		{
			int i = 0;
			while ( i < 30000 )
			{
				System.out.print( Thread.currentThread().getName() + " : " + i );
				if ( i == 100 )
				{
					throw new RuntimeException();
				}
				i++;
			}
		}
		finally
		{
			lock.unlock();//do unlock here, clean up operation in case of exceptions can be done here
			//If you use synchronized this is handled by JVM even in the case of exceptions.
		}
	}

	synchronized private void type()
	{

	}
}