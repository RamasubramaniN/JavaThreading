package com.psg.ramasubramani;
import java.util.concurrent.TimeUnit;

public class DaemonThread
{

	static int i = 0;

	public static void main( String[] args ) throws InterruptedException
	{
		i++;
		Thread thread = new Thread( new G() ); 
		//All background threads can be used as daemon. 
		//Ex : GC, you want to take snapshot your object's state
		//comment the following and see the difference in output
		thread.setDaemon( true );//assume parent and child threads are non daemon, 
		//child thread continues execution even if parent thread dies.
		
		thread.start();
		
		//When all non-daemon threads complete its execution, all daemon threads will be terminated 
		//(ie) As long as the program runs daemon thread runs. Once the program terminates 
		//all daemon threads are forcefully terminated.
		TimeUnit.MILLISECONDS.sleep( 10 ); //Increasing the non daemon's thread life time 
		//in order to print more numbers by daemon thread.
		
		System.out.println("Main thread dies");
	}
}

class G implements Runnable
{
	int count = 0;
	static int i=0;
	@Override
	public void run()
	{
		while ( count <= 100 )
		{
			try
			{
				TimeUnit.MILLISECONDS.sleep( 10 );
				System.out.println("Daemon Thread : " + count );
				i++;
				count++;
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	System.out.println("Static : "+ i);
	}
}
