package com.psg.ramasubramani;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rn5
 *
 */
public class AtomicUsage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IdGenerator idGenerator = new IdGenerator();
		for(int index = 0; index < 10; index++) {
			new Thread(new AtomicRunnable(idGenerator)).start();
		}
	}
	
	private static class AtomicRunnable implements Runnable {
		
		private IdGenerator idGenerator;
		
		public AtomicRunnable(IdGenerator idGenerator) {
			this.idGenerator = idGenerator;
		}

		@Override
		public void run() {
			System.out.println(idGenerator.Increment());
		}
		
	}
	
	private static class IdGenerator {
		private AtomicInteger someValue = new AtomicInteger(100);
		
		private int Increment() {
			return someValue.getAndIncrement();
		}
	}

}
