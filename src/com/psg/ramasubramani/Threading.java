package com.psg.ramasubramani;

public class Threading
{
	public static void main( String args[] )
	{
		System.out.println("Thread Name : "+Thread.currentThread().getName());
		Counter counter = new Counter();
		
		//Just a method call from main, This wont create a separate thread, this uses main's thread,
		//i.e. run method doesn't produce any threading ability,you need to create a thread 
		//and attach a task to that thread.
		counter.run();  
		
		AlphaCounter alpha = new AlphaCounter();
		alpha.run(); //Same here as well.
	
		//Method : 1 : Attach Counter task to Thread1 for execution.
		//Argument of thread is runnable target
		Thread thread1 = new Thread(new Counter()); 
		
		// creates a separate thread and task(code inside run method) is attached to the thread.
		thread1.start(); 
	
		AlphaCounter acounter = new AlphaCounter();
		
		// arg1 --> Runnable Target , arg2 --> Name of the thread
		Thread thread2 = new Thread( acounter, "Alpha Counter" );
		
		// creates a separate thread and task(code inside run method) is attached to the thread.
		thread2.start();
		
		//Method : 2 : Extending thread class. Drawback. MyThread class cannot inherit
		//any other class. Multiple Inheritance is not allowed in java.
		MyThread myThread = new MyThread(); 
		myThread.start();
		//myThread.start(); IllegalThreadStateException if the thread was already started.
		
		//Method : 3
		/*AlphaCounter alphaCounter = new AlphaCounter();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute( alphaCounter );*/
	}
}

///It is very difficult to increase the clock rates of computers because 
//it will have effect on heat, so system parts will degrade
///So, it is better to have more processors than increasing clock rates, Multiple 
//processors have certain advantages.
///1)speed - if you have 3 processes, and 4 processors, assign one process to each processor, 
//each processor will execute process simultaneously, 
//in this case speed is increased by n times.
///This is called multi-processing. Executing multiple programs simultaneously, 
//this can be achieved by having multiple processors.
//2)Fairness, modern computers allow multiple users accessing the same system, 
//so they have equal claims on resources, each user may run a single long running program
//assume 10 users logged in and each user executes a process, 
//to these processes are shared across 5 processors.  

//Single processor handling multiple processes - not multi processing, this is called multi tasking.
//(multi processing means, at the same time multiple processes should get executed.) 
//Because at any point in time, only one process is executed in this case, 
//each process gets CPU handle for sometime,
// executes the program and then CPU handle is given to some other process (context switching).
//Multi Threading - Within a process divide the program into independent or almost 
//independent tasks and assign the tasks to threads. 
//Since they execute inside the same process, they share the same processor memory.
//So,they will share heap space and resources, 
//need to synchronise when multiple threads modify a shared resource.
//(Ex) In servlet request handling Servlet Context is shared resource across 
//the requests, need to synchronize it.
//When to use threading, assume you have single processor, you have single program to execute, 
//you divided the program into multiple tasks and inside the tasks if some tasks need to wait 
//for some input from the user,
//good to have multi threading but if no tasks are blocking tasks no use to have multi threading.
//Because the intention is when one sub task waits another sub task can execute since they are independent, 
//but in the above case, since there are no blocking issue, no need to use threading , 
//no need to split the tasks into sub tasks,
//we could run it sequentially.
//But in the above case,if multiple processors or multi core processors are available, 
//then we can divide it into sub tasks,
//because we can assign each sub task to each processor and more than one task 
//will be executed at any point in time.
//So, the idea is either you have to have a blocking thing in any one of the sub task 
//or you need to have multiple processes.
//Multi core processor - (example) dual core, it has two separate processors inside, 
//so it can execute two processes at the same time,
//each processor each process, quad core, 4 processors.
//8085 Von Newmann architecture, program and data memory is shared,amount 16 address lines, 
//8 lines have been shared for data lines. 
//8051 Havard architecture, separate program and data memory, separate address and data lines,
//so it is a bit faster than 8085 because data and program instructions can be 
//fetched simultaneously since they are separate.

//Threads are everywhere, even if you dont create a thread,the framework may 
//create threads.gc runs as a thread, main is a thread, 
//thread is used in remote method invocation (Asynchronous call).servlets request 
//handling uses threads for each request, 
//UI event handling uses fixed thread pool to increase responsiveness of the application.
//Housekeeping tasks(garbage collection,finalisation) are executed by the threads created by JVM

//Stateless objects are always thread safe, because object's state is defined by its member variables, 
//so, here the state will not be damaged at any point of time.
//The class which has only final(cannot recreate using new operator) immutable
//(object members cannot be edited) fields are also thread safe.
//Better you encapsulate your fields better thread safety you will get as result

//atomic operation is a indivisible operation, when one thread executes atomic operation, 
//no other threads can execute the same operation(routine) 
//until the thread completes the atomic operation.
//when you use normal variables in your class x++ will execute as 3 instructions, 
//so, thread safety is not ensured.
//If you use atomic variables the above x++ will be an indivisible operation,
//but if you have two atomic variables 
//and both should be updated to maintain proper state of object,thread safety is not ensured, 
//because individual atomic variable operation is indivisible, but there is a 
//possible way after updating atomic 1, 
//some other thread gets CPU handle and updates atomic 2, and then 
//atomic 2 is modified by first thread.
//So, all the related operations should be performed as indivisible operations.

//When we use synchronised method, the method is locked by the intrinsic 
//lock of the object(the instance is intrinsic object). 
//no other method can call any synchronized methods on the same object until the object 
//finishes synchronized call/block (but the method can be called on some other object).
//So, only the routines are locked, member variables of the particular 
//object is not locked, the object's member variables can be 
//modified by some other part of the application when synchronized method 
//is executing. Only one lock per object, 
//so, make sure the same lock should be used to protect all the member variables 


//You have a long running task and a timer, timer triggers a separate thread to 
//get the snapshot of the entire program 
//(If the program crashes no need to start from the beginning, just use the snapshot and resume from there.)
//The problem is when getting snapshot you may need to get the value of objects 
//or variables,but the task is progressing and object's 
//state keep changing, so there is a possibility that you do not get the 
//most recent values of some variables,possibly you will get corrupted data.
//so lock the complete object and save the snapshot. Synchronize all getters and setters.
//Make them private members. Now read all member values.

class MyThread extends Thread {
	@Override
	public void run()
	{
		System.out.println("Extending thread. Your class can't extend any other class. dont do this.");
	}
}

class Counter implements Runnable
{
	int i = 1;

	@Override
	public void run()
	{
		/*try
		{
			FileWriter fw = new FileWriter( "D:\\new.txt", false )*/;
			while ( i < 10 )
			{
				System.out.println( "Name : " + Thread.currentThread().getName() + " Counter : " + i++ );
				/*fw.write( i++ + "," );*/
			}
			/*fw.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}*/

	}
}

class AlphaCounter implements Runnable
{
	int i = 0;

	@Override
	public void run()
	{
		while ( i <= 10 )
		{
			System.out.println( "alpha : " + i++ );
		}
	}

}
