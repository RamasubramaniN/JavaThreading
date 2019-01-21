package com.psg.ramasubramani;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

//No need to use wait, notify, notifyAll(). use blocking queue, they easily solve producer consumer problems
//In the following problem toaster is consumer for bread factory
//butterer is consumer for toaster
//eater is consumer for butterer
public class BlockingQueues
{
	public static void main( String[] args )
	{
		BlockingQueue<Bread> freshBreadQueue = new LinkedBlockingQueue<Bread>();
		BlockingQueue<Bread> toastedBreadQueue = new LinkedBlockingQueue<Bread>();
		BlockingQueue<Bread> butteredBreadQueue = new LinkedBlockingQueue<Bread>();
		BlockingQueue<Bread> finishedBreadQueue = new LinkedBlockingQueue<Bread>();

		ExecutorService executor = Executors.newCachedThreadPool();//Creates a thread pool that creates new threads as needed, 
		//but will reuse previously constructed threads when they are available. These pools will typically improve the performance of programs 
		//that execute many short-lived asynchronous tasks. Calls to execute will reuse previously constructed threads if available. 
		//If no existing thread is available, a new thread will be created and added to the pool. 

		Toasting toaster = new Toasting( freshBreadQueue, toastedBreadQueue );
		Buttering butterer = new Buttering( toastedBreadQueue, butteredBreadQueue );
		Packing pack = new Packing( butteredBreadQueue, finishedBreadQueue );

		BreadFactory breadFactory = new BreadFactory( freshBreadQueue );
		executor.submit( breadFactory );

		executor.submit( toaster );
		executor.submit( butterer );
		executor.submit( pack );

		try
		{
			Thread.sleep( 3000 );
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}
		executor.shutdownNow();//Send interrupt flag to all threads created by the executor
		try
		{
			//Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs,
			//or the current thread is interrupted, whichever happens first.
			executor.awaitTermination( 3000, TimeUnit.MILLISECONDS );

			int finishedBreads = finishedBreadQueue.size();
			int butteredThreads = butteredBreadQueue.size() + finishedBreads;
			int toastedBreads = toastedBreadQueue.size() + butteredThreads;
			int freshBreads = freshBreadQueue.size() + toastedBreads;

			System.out.println( "Finished Breads : " + finishedBreads );
			System.out.println( "Buttered Breads : " + butteredThreads );
			System.out.println( "Remaining Buttered Breads : " + butteredBreadQueue.size() );
			System.out.println( "Toasted Breads : " + toastedBreads );
			System.out.println( "Remaining Toasted Breads : " + toastedBreadQueue.size() );
			System.out.println( "Total Breads Produced : " + freshBreads );
			System.out.println( "Remaining Fresh Breads : " + freshBreadQueue.size() );
		}
		catch ( InterruptedException e )
		{
		}
	}
}

class Bread
{
	private static int count = 1;
	private int id = count++;

	public int getId()
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}
}

class BreadFactory implements Runnable
{
	BlockingQueue<Bread> freshBreadQueue;

	public BreadFactory( BlockingQueue<Bread> freshBreadQueue )
	{
		this.freshBreadQueue = freshBreadQueue;
	}

	@Override
	public void run()
	{
		while ( !Thread.interrupted() )//Tests whether the current thread has been interrupted.
			//The interrupted status of the thread is cleared by this method. In other words, 
			//if this method were to be called twice in succession, the second call would return false
		{
			try
			{
				Bread bread = new Bread();
				freshBreadQueue.put( bread );
				System.out.println( "Creating Bread : " + bread.getId() );
			}
			catch ( Exception e )
			{
			}
		}
	}

}

class Toasting implements Runnable
{
	BlockingQueue<Bread> freshBreadQueue, toastedQueue;

	Toasting( BlockingQueue<Bread> freshBreadQueue, BlockingQueue<Bread> toastedQueue )
	{
		this.freshBreadQueue = freshBreadQueue;
		this.toastedQueue = toastedQueue;
	}

	@Override
	public void run()
	{
		while ( !Thread.interrupted() )
		{
			try
			{
				Bread bread = freshBreadQueue.take();
				System.out.println( "Toasting Bread : " + bread.getId() );
				toastedQueue.put( bread );
			}
			catch ( InterruptedException e )
			{
			}
		}
	}
}

class Buttering implements Runnable
{
	BlockingQueue<Bread> toastedQueue, ButteredQueue;

	Buttering( BlockingQueue<Bread> toastedQueue, BlockingQueue<Bread> butteredQueue )
	{
		this.toastedQueue = toastedQueue;
		this.ButteredQueue = butteredQueue;
	}

	@Override
	public void run()
	{
		while ( !Thread.interrupted() )
		{
			Bread bread;
			try
			{
				bread = toastedQueue.take();
				System.out.println( "Buttering Bread : " + bread.getId() );
				ButteredQueue.put( bread );
			}
			catch ( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Packing implements Runnable
{
	BlockingQueue<Bread> butteredQueue, finishedQueue;

	Packing( BlockingQueue<Bread> butteredQueue, BlockingQueue<Bread> finishedQueue )
	{
		this.butteredQueue = butteredQueue;
		this.finishedQueue = finishedQueue;
	}

	@Override
	public void run()
	{
		while ( !Thread.interrupted() )
		{
			try
			{
				Bread bread = butteredQueue.take();
				System.out.println( "Packing Bread : " + bread.getId() );
				finishedQueue.put( bread );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}
}