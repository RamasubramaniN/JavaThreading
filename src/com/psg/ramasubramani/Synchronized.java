package com.psg.ramasubramani;

public class Synchronized
{
//When multiple tasks(threads) try to access the shared resource, the access should be serialized, means several tasks should not
//access the shared resource at the same time, for example when one thread is reading a file, another thread should not write into the file.
//If it happens simultaneously, reading thread will not get the proper object(file).
//Let the writing thread finishes first and next the reading thread can pick up the recent file
//So, in case of shared resources , the critical section should be handled by mutex(mutual exclusion)
//Piece of code - mutex (print, type)- only one thread can execute it on the same object at any time, 
//until the thread completes its critical section other threads have to wait.
	
	
//In the following case print() is called on the same object printwriter(). Since the method is synchronized the critical section(print method)
//can't be executed by T1,T2 simultaneously. T2 can execute print() method only when T1 finishes it. Once T1 enters print method, 
//the object locks all the synchronized instance methods, 
//so any other thread cannot execute any synchronized methods(print(),type()) on the same NumberWriter object.
//If you try to call print method on different NumberWriter object, then both threads will run simultaneously, 
//because lock is for an object(one lock per object) example T3,T4 runs simultaneously.
	public static void main( String[] args )
	{
		//Synchronized
		NumberPrinter numberPrinter = new NumberPrinter();
		Thread t1 = new Thread( numberPrinter, "T1" );
		t1.start();
		Thread t2 = new Thread( numberPrinter, "T2" );
		t2.start();
		
		//T3 T4 runs simultaneously, because the method is locked by numberPrinter3 when T3 executes the print method on numberPrinter3. 
		//so, this time if you try to call print or type() method on numberPrinter3  by some other thread you can't
		//But here we are calling on numberPrinter4, so methods are not locked for numberPrinter4 when T3 executes,
		//so both threads execute the method simultaneously,after this both numberPrinter3 and numberPrinter4 both are locked.
		//so no other thread cannot execute print or type (synchronized methods) on numberPrinter3,numberPrinter4 on until they finishes their execution
		//Assume runnable target is 'x' and runnable class is 'X'(x is of type X). Assume there are two synchronized functions type() and print() in class X.
		//run() calls type() first and print() next. You have t1, t2 threads. attach the task with t1 and start the thread, (ie) for t1,runnable target is x.
		//now object 'x' is locked, so,if you attach the task t2 with object x. t2 cannot execute type() and print() methods, but only object x is locked.
		//If you create some other object of class X, and if you assign it to t2 you can call. When one thread locks the object(runnable target), 
		//none of the synchronized methods can be called on the same object until it finishes its execution.
		//When one thread enters critical section it acquires the lock of the calling object(runnable target).Even though other threads cannot enter
		//the critical section on the same object. the thread which has the lock can enter into any number of critical sections
		//(can execute any number of synchronous methods nestedly)
		//each time it calls synchronous method count is increased by 1 and whenever it leaves the method, 
		//the count is decremented by 1, so when the thread releases the lock this count will be zero.
		NumberPrinter numberPrinter3 = new NumberPrinter();
		NumberPrinter numberPrinter4 = new NumberPrinter();
		Thread t3 = new Thread( numberPrinter3, "T3" );
		t3.start();
		Thread t4 = new Thread(numberPrinter4, "T4" );
		t4.start();
	}

}

class NumberPrinter implements Runnable
{
	static int count = 0;
	int id = ++count;

	@Override
	public void run()
	{
		print();
	}

	synchronized private void print() //equivalent to synchronized(this) before int i=0 statement.
	{
		//prefer synchronized blocks over synchronized methods because if you have 100 lines of code and only 2 lines of code is critical session
		//then synchronized(ObjectToBeLocked) is better --> less critical session --> less locking, more throughput
		int i = 0;
		while ( i < 10 )
		{
			System.out.println( Thread.currentThread().getName() + " : " + i );
			i++;
		}
	}
	
	synchronized private void type()
	{
		
	}

}