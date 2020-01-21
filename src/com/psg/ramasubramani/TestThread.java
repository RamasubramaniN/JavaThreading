package com.psg.ramasubramani;

public class TestThread implements Runnable
{
	A a = new A();
	public static void main( String[] args )
	{
		Thread thread = new Thread(new TestThread());
		thread.start();
	}

	/**
	 * When one thread T1 enters critical section on monitor x,
	 * Even though other threads cannot enter the critical section on the same object, 
	 * the thread T1 which has the lock can enter into any number of critical sections
	 * (can execute any number of synchronous methods nestedly)
	 * each time it calls synchronous method count is increased by 1 and whenever it leaves the method, 
	 * the count is decremented by 1, so when the thread comes out of all synchronized(nested) methods,
	 * count becomes 0 and the thread releases the lock.
	 */
	@Override
	public void run()
	{
		a.method1();
	}
}

class A{
	public synchronized void  method1(){
		System.out.println("Method1");
		method2();
	}
	public  synchronized void method2(){
		System.out.println("Method2");
	}
}