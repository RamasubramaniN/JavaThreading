package com.psg.ramasubramani;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutors
{

	public static void main( String[] args )
	{
		int count = 0;
		ExecutorService exec = Executors.newFixedThreadPool( 3 );
		for ( ; count < 3; count++ )
		{
			exec.execute( new Task() );
		}
		Thread.currentThread().isDaemon();
	}

}

class Task implements Runnable
{
	static File file = new File( "D:\\counter.txt" );
	static FileWriter fw;
	static int x = 0;
	int i = 1;
	int id = ++x;

	Task()
	{
		if ( fw == null )
			try
			{
				fw = new FileWriter( file );
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void run()
	{
		while ( i <= 1000 )
		{
			try
			{
				writeToFile();
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}

	synchronized private void writeToFile() throws IOException
	{
		fw.append( "Thread : " + id + ", i=" + i );
	}

}
