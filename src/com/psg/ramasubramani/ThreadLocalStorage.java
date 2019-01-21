package com.psg.ramasubramani;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


//For each thread separate instances of connection objects are maintained, ie connection object is local to thread
//This is very useful in a situation where connection object is mutable.
//Once a thread starts its execution, even though some other thread modifies connection object, those will not appear to this thread because the object is local to thread.
//This will very useful when your application moves from single threaded to multiple threaded, and you want to protect all
//global variables, where these variables are mutable and you don't want these objects to be modified by any thread or you don't
//want changes in one thread affecting another thread's object
public class ThreadLocalStorage
{
	public static void main( String[] args )
	{
		ExecutorService executor = Executors.newCachedThreadPool();
		for ( int i = 1; i <= 5; i++ )
			executor.execute( new RunnableThreadClass() );
	}
}

class Global
{
	public static ThreadLocal<Connection> connectionObject = new ThreadLocal<Connection>()
	{
		protected Connection initialValue()
		{
			return new Connection();
		}
	};
}

class RunnableThreadClass implements Runnable
{
	@Override
	public void run()
	{
		Global.connectionObject.get().setConnectionName( Thread.currentThread().getName() + "-" + Global.connectionObject.get().getConnectionName() );
		System.out.println("Changed connection name");
		try {
			TimeUnit.SECONDS.sleep(2);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printConnectionName();
	}

	private void printConnectionName()
	{
		System.out.println( "Thread Name : " + Thread.currentThread().getName() + ", Connection Name : " + Global.connectionObject.get().getConnectionName() );
	}
}

class Connection
{
	private String connectionName;
	private String userName;
	private String password;

	public String getConnectionName()
	{
		return connectionName;
	}

	public void setConnectionName( String connectionName )
	{
		this.connectionName = connectionName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName( String userName )
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword( String password )
	{
		this.password = password;
	}

	public Connection()
	{
		connectionName = new String( "default" );
		userName = new String( "default" );
		password = new String( "default" );
	}
}
