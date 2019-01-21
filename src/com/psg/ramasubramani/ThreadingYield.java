package com.psg.ramasubramani;
public class ThreadingYield
{

	public static void main( String[] args )
	{
		Thread t1 = new Thread( new C() );
		t1.start();
		D d = new D();
		Thread t2 = new Thread( null, d, "Thread D" ); //Thread group,runnable target and thread name
		t2.start();
		/*yield just says, end my time slice prematurely (preemption), look around for other
		threads to run. If there is nothing better than me, continue.
		Sleep says I don't want to run for x milliseconds. Even if no other
		thread wants to run, don't make me run.
		Yield works on same priority threads.	
		It makes same priority thread from running to runnable state.
		Then other same priority thread comes to running state from runnable state */
		
	}

}

/* Result : Yield doesn't work properly
C : 0

D : 0
C : 1
D : 1
D : 2
D : 3
D : 4
D : 5
D : 6
D : 7
D : 8
D : 9
D : 10
C : 2
C : 3
C : 4
C : 5
C : 6
C : 7
C : 8
C : 9
C : 10
 */

class C implements Runnable
{

	int count = 0;

	@Override
	public void run()
	{
		while ( count <= 10 )
		{
			System.out.println( "C : " + count );
			Thread.yield();//Causes the currently executing thread object to temporarily pause and allow other threads to execute.
			count++;
		}

	}

}

class D implements Runnable
{
	int count = 0;

	@Override
	public void run()
	{
		while ( count <= 10 )
		{
			System.out.println( "D : " + count );
			Thread.yield(); //Causes the currently executing thread object to temporarily pause and allow other threads to execute.
			count++;
		}
	}

}