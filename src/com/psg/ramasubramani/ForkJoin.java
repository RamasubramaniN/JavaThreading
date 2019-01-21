package com.psg.ramasubramani;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * @author nramasubramani
 *
 */
public class ForkJoin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File file = new File("C://code/maas/DNA");
		
		//Single Thread : No concurrency.
		SingleThreadProcessor singleThreadProcessor = new SingleThreadProcessor();
		long t1 = System.currentTimeMillis();
		long size1 = singleThreadProcessor.getDirectorySize(file);
		long t2 = System.currentTimeMillis();
		System.out.println("Single Thread. Size " + size1 + ", Time taken : " + (t2-t1) + " milliseconds.");
		
		//Recursive Task - Fork Join Pool
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		RecursiveTaskProcessor recursiveTaskProcessor = new RecursiveTaskProcessor(file);
		long t3 = System.currentTimeMillis();
		long size2;
		try {
			size2 = forkJoinPool.invoke(recursiveTaskProcessor);
		} finally {
			forkJoinPool.shutdown();
		}
		long t4 = System.currentTimeMillis();
		System.out.println("Recursive Task. Size " + size2 + ", Time taken : " + (t4-t3) + " milliseconds.");
		
		long startIndex = 2;
		long endIndex = 1000000;
		long t5 = System.currentTimeMillis();
		singleThreadProcessor.printPrimeNumbers(startIndex, endIndex);
		long t6 = System.currentTimeMillis();
		System.out.println("Single Thread. Time taken : " + (t6-t5) + " milliseconds.");
		
		RecursiveActionProcessor recursiveActionProcessor = new RecursiveActionProcessor(startIndex, endIndex);
		ForkJoinPool forkJoinPool2 = new ForkJoinPool();
		try {
			long t7 = System.currentTimeMillis();
			forkJoinPool2.invoke(recursiveActionProcessor);
			long t8 = System.currentTimeMillis();
			System.out.println("Recursive Action. Time taken : " + (t8-t7) + " milliseconds.");
		} finally {
			forkJoinPool2.shutdown();
		}
	}

}

class SingleThreadProcessor {
	
	private PrimeFinder primeFinder = new PrimeFinder();
	
	public Long getDirectorySize(File file){
		
		long size = 0;
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f : files) {
				size += getDirectorySize(f);
			}
		}
		else {
			size = file.length();
		}
		
		return size;
	}
	
	public void printPrimeNumbers(long startIndex, long endIndex){
		for(long number = startIndex ; number <= endIndex ; number++) {
			if(primeFinder.isPrime(number)){
				//System.out.print(number + "\t");
			}
		}
		System.out.println();
	}
}

//Recursive task returns an argument
class RecursiveTaskProcessor extends RecursiveTask<Long>{

	private File file;
	
	public RecursiveTaskProcessor(File file) {
		this.file = file;
	}

	@Override
	protected Long compute() {

		List<RecursiveTaskProcessor> recursiveTaskProcessorList = null;

		if (file.isDirectory()) {
			File[] children = file.listFiles();
			recursiveTaskProcessorList = new ArrayList<RecursiveTaskProcessor>();

			for (File f : children) {
				RecursiveTaskProcessor recursiveTaskProcessor = new RecursiveTaskProcessor(f);
				recursiveTaskProcessorList.add(recursiveTaskProcessor);
				recursiveTaskProcessor.fork();
			}
		} else {
			return file.length();
		}

		long size = 0;
		if (recursiveTaskProcessorList != null && recursiveTaskProcessorList.size() > 0) {
			for (RecursiveTaskProcessor recursiveTaskProcessor : recursiveTaskProcessorList) {
				size += recursiveTaskProcessor.join();
			}
		}

		return size;
	}

}

class RecursiveActionProcessor extends RecursiveAction {
	
	private long startIndex;
	private long endIndex;
	private PrimeFinder primeFinder = new PrimeFinder();
	
	public RecursiveActionProcessor(long startIndex, long endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	@Override
	protected void compute() {
		
		List<RecursiveActionProcessor> recursiveActionProcessorList = new ArrayList<RecursiveActionProcessor>();
	
		if(endIndex - startIndex > 10000){
			
			long mid = startIndex + (endIndex - startIndex) / 2;
			
			RecursiveActionProcessor recursiveActionProcessor1 = new RecursiveActionProcessor(startIndex, mid);
			recursiveActionProcessor1.fork();
			recursiveActionProcessorList.add(recursiveActionProcessor1);
			
			RecursiveActionProcessor recursiveActionProcessor2 = new RecursiveActionProcessor(mid + 1, endIndex);
			recursiveActionProcessor2.fork();
			recursiveActionProcessorList.add(recursiveActionProcessor2);
		}
		else {
			for(long number = startIndex ; number <= endIndex ; number++) {
				if(primeFinder.isPrime(number)){
					//System.out.print(number + "\t");
				}
			}
			//System.out.println();
		}
		for(RecursiveActionProcessor recursiveActionProcessor : recursiveActionProcessorList){
			recursiveActionProcessor.join();
		}
	}
	

	
}

class PrimeFinder {

	public boolean isPrime(long number) {

		boolean isPrime = true;

		for (long i = 2; i <= Math.sqrt(number); i++) {
			if (number % i == 0) {
				isPrime = false;
				break;
			}
		}

		return isPrime;
	}
}